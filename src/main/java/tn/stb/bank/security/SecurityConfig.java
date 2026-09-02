package tn.stb.bank.security;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Authentification déléguée à Keycloak (OAuth2 / OpenID Connect).
 * Les rôles Realm Keycloak (realm_access.roles) sont mappés vers les
 * autorités Spring "ROLE_XXX" attendues par le reste de l'application
 * (mêmes noms que l'enum tn.stb.bank.model.Role : ADMIN, INFO, COMPTABILITE, RH).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    /**
     * Conserve pour DataInitializer (encodage des mots de passe stockés dans la table
     * locale "utilisateurs"). N'est plus utilisé pour l'authentification elle-même,
     * qui est maintenant déléguée entièrement à Keycloak via oauth2Login().
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository csrfRepo = CookieCsrfTokenRepository.withHttpOnlyFalse();
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName(null);

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfRepo)
                        .csrfTokenRequestHandler(requestHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/accueil", "/services", "/agences", "/contact",
                                "/css/**", "/js/**", "/images/**"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/clients/**").hasAnyRole("ADMIN", "INFO")
                        .requestMatchers("/transactions/**", "/virements/**", "/comptes/**")
                        .hasAnyRole("ADMIN", "COMPTABILITE")
                        .requestMatchers("/employes/**").hasAnyRole("ADMIN", "RH")
                        .requestMatchers("/dashboard").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestResolver(authorizationRequestResolver())
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(oidcUserService())
                        )
                        .successHandler(successHandler())
                        .failureUrl("/login?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/perform-logout")
                        .logoutSuccessHandler(oidcLogoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"))
                .headers(h -> h.frameOptions(f -> f.disable()));

        return http.build();
    }

    /**
     * Charge le OidcUser standard puis remplace ses autorités par celles
     * extraites de la claim "realm_access.roles" de l'ACCESS TOKEN (pas l'ID token :
     * le mapper "roles" de Keycloak, dans cette instance, n'ajoute les rôles
     * qu'à l'access token).
     */
    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcUserService delegate = new OidcUserService();
        return userRequest -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);
            Set<GrantedAuthority> mapped = new HashSet<>(
                    extractRolesFromAccessToken(userRequest.getAccessToken().getTokenValue())
            );
            return new CustomOidcUser(mapped, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
    }

    @SuppressWarnings("unchecked")
    private Set<GrantedAuthority> extractRolesFromAccessToken(String accessTokenValue) {
        Set<GrantedAuthority> result = new HashSet<>();
        try {
            JWTClaimsSet claims = JWTParser.parse(accessTokenValue).getJWTClaimsSet();
            Object realmAccess = claims.getClaim("realm_access");
            if (realmAccess instanceof Map<?, ?> realmMap) {
                Object roles = realmMap.get("roles");
                if (roles instanceof List<?> roleList) {
                    for (Object role : roleList) {
                        result.add(new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Impossible de lire les roles depuis l'access token : " + e.getMessage());
        }
        if (result.isEmpty()) {
            // Filet de sécurité : aucun rôle Keycloak trouvé -> pas d'accès aux zones protégées
            result.add(new SimpleGrantedAuthority("ROLE_NONE"));
        }
        return result;
    }

    /**
     * Force Keycloak a toujours afficher le formulaire de login (prompt=login),
     * meme si une session SSO Keycloak est deja active dans le navigateur.
     * Pratique en dev pour basculer entre comptes de test sans se deconnecter
     * manuellement a chaque fois. A retirer (ou rendre configurable) avant la prod,
     * sinon le SSO ne sert plus a rien pour les vrais utilisateurs.
     */
    private OAuth2AuthorizationRequestResolver authorizationRequestResolver() {
        DefaultOAuth2AuthorizationRequestResolver defaultResolver =
                new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");

        return new OAuth2AuthorizationRequestResolver() {
            @Override
            public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
                return addPromptLogin(defaultResolver.resolve(request));
            }

            @Override
            public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
                return addPromptLogin(defaultResolver.resolve(request, clientRegistrationId));
            }

            private OAuth2AuthorizationRequest addPromptLogin(OAuth2AuthorizationRequest req) {
                if (req == null) return null;
                Map<String, Object> extraParams = new java.util.HashMap<>(req.getAdditionalParameters());
                extraParams.put("prompt", "login");
                return OAuth2AuthorizationRequest.from(req).additionalParameters(extraParams).build();
            }
        };
    }

    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            // --- LOG TEMPORAIRE DE DEBUG — a retirer une fois le probleme resolu ---
            System.out.println("=== KEYCLOAK LOGIN DEBUG ===");
            System.out.println("Authorities recues : " + authentication.getAuthorities());
            System.out.println("=============================");
            // --- FIN LOG TEMPORAIRE ---

            // IMPORTANT : ne jamais faire authorities.iterator().next() — l'ordre d'un
            // HashSet n'est pas garanti et Keycloak ajoute toujours des roles "techniques"
            // (default-roles-xxx, offline_access, uma_authorization) en plus du vrai role
            // metier. On cherche explicitement le role attendu, par ordre de priorite.
            java.util.Set<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(java.util.stream.Collectors.toSet());

            if (roles.contains("ROLE_ADMIN")) {
                response.sendRedirect("/dashboard");
            } else if (roles.contains("ROLE_INFO")) {
                response.sendRedirect("/clients");
            } else if (roles.contains("ROLE_COMPTABILITE")) {
                response.sendRedirect("/transactions");
            } else if (roles.contains("ROLE_RH")) {
                response.sendRedirect("/employes");
            } else {
                response.sendRedirect("/access-denied");
            }
        };
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler handler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        // Retour sur l'accueil public apres deconnexion cote Keycloak
        handler.setPostLogoutRedirectUri("{baseUrl}/");
        return handler;
    }
}