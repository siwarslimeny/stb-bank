package tn.stb.bank.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;

/**
 * Pont entre l'utilisateur Keycloak (OidcUser) et UserDetails, attendu par les
 * controleurs existants via @AuthenticationPrincipal UserDetails. Permet de
 * garder tous les controleurs (DashboardController, ClientController,
 * AdminController, CompteController, BankController) inchanges apres le
 * passage a Keycloak.
 */
public class CustomOidcUser extends DefaultOidcUser implements UserDetails {

    public CustomOidcUser(Collection<? extends GrantedAuthority> authorities,
                          OidcIdToken idToken, OidcUserInfo userInfo) {
        super(authorities, idToken, userInfo);
    }

    @Override
    public String getUsername() {
        String preferred = getPreferredUsername();
        return preferred != null ? preferred : getName();
    }

    @Override
    public String getPassword() {
        // Authentification geree par Keycloak — aucun mot de passe local a exposer
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}