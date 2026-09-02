package tn.stb.bank.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;


@Service
@Slf4j
public class OpaService {

    private final RestClient restClient = RestClient.create();

    @Value("${opa.url}")
    private String opaUrl;

    @Value("${opa.deletion-url}")
    private String opaDeletionUrl;

    /**
     * Interroge OPA pour savoir si la validation d'un virement est autorisée.
     *
     * @param role    rôle Spring de l'utilisateur courant, sans le préfixe "ROLE_" (ex: "ADMIN")
     * @param montant montant du virement concerné
     * @return true si OPA autorise l'action, false sinon (ou en cas d'erreur)
     */
    public boolean isVirementValidationAllowed(String role, Double montant) {
        try {
            Map<String, Object> body = Map.of(
                    "input", Map.of(
                            "role", role,
                            "montant", montant != null ? montant : 0.0
                    )
            );

            OpaResponse response = restClient.post()
                    .uri(opaUrl)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(OpaResponse.class);

            boolean allowed = response != null && Boolean.TRUE.equals(response.result());
            log.info("OPA decision - role={}, montant={}, allowed={}", role, montant, allowed);
            return allowed;

        } catch (Exception e) {
            log.error("Erreur lors de l'appel à OPA ({}) — refus par défaut (fail-closed)", opaUrl, e);
            return false;
        }
    }

    /**
     * Correspond au format de réponse standard d'OPA : { "result": true/false }
     */
    private record OpaResponse(Boolean result) {
    }

    /**
     * Interroge OPA pour savoir si la suppression d'un virement est autorisée.
     * Règle actuelle : réservée à ADMIN (voir stb_authz.rego -> allow_deletion).
     *
     * @param role rôle Spring de l'utilisateur courant, sans le préfixe "ROLE_" (ex: "ADMIN")
     */
    public boolean isVirementDeletionAllowed(String role) {
        try {
            Map<String, Object> body = Map.of("input", Map.of("role", role));

            OpaResponse response = restClient.post()
                    .uri(opaDeletionUrl)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(OpaResponse.class);

            boolean allowed = response != null && Boolean.TRUE.equals(response.result());
            log.info("OPA decision (deletion) - role={}, allowed={}", role, allowed);
            return allowed;

        } catch (Exception e) {
            log.error("Erreur lors de l'appel à OPA ({}) — refus par défaut (fail-closed)", opaDeletionUrl, e);
            return false;
        }
    }
}