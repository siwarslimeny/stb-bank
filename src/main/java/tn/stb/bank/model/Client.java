package tn.stb.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroClient;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    private String adresse;
    private String ville;
    private String codePostal;

    @Column(name = "cin", unique = true)
    private String cin;

    @Enumerated(EnumType.STRING)
    private TypeClient typeClient = TypeClient.PARTICULIER;

    @Enumerated(EnumType.STRING)
    private StatutClient statut = StatutClient.ACTIF;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "date_inscription")
    private LocalDateTime dateInscription = LocalDateTime.now();

    @Column(name = "agence")
    private String agence;

    private String profession;

    @Column(name = "revenu_mensuel")
    private Double revenuMensuel;

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public String getInitiales() {
        String p = (prenom != null && !prenom.isEmpty()) ? String.valueOf(prenom.charAt(0)) : "";
        String n = (nom != null && !nom.isEmpty()) ? String.valueOf(nom.charAt(0)) : "";
        return (p + n).toUpperCase();
    }

    public enum TypeClient { PARTICULIER, ENTREPRISE, PROFESSIONNEL }
    public enum StatutClient { ACTIF, INACTIF, BLOQUE, EN_ATTENTE }
}