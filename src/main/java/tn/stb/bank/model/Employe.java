package tn.stb.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String matricule;

    @NotBlank private String nom;
    @NotBlank private String prenom;

    @Email
    @Column(unique = true)
    private String email;

    private String telephone;

    @NotBlank
    private String poste;

    @Enumerated(EnumType.STRING)
    private Departement departement;

    @Enumerated(EnumType.STRING)
    private TypeContrat contrat = TypeContrat.CDI;

    @Enumerated(EnumType.STRING)
    private StatutEmploye statut = StatutEmploye.ACTIF;

    private Double salaire;

    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;

    @Column(name = "date_fin_contrat")
    private LocalDate dateFinContrat;

    private String agence;
    private String adresse;
    private String cin;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public String getInitiales() {
        String p = (prenom != null && !prenom.isEmpty()) ? String.valueOf(prenom.charAt(0)) : "";
        String n = (nom != null && !nom.isEmpty()) ? String.valueOf(nom.charAt(0)) : "";
        return (p + n).toUpperCase();
    }

    public enum Departement {
        DIRECTION, INFORMATION, COMPTABILITE, RH, IT, JURIDIQUE, MARKETING, OPERATIONS
    }
    public enum TypeContrat { CDI, CDD, STAGE, INTERIM }
    public enum StatutEmploye { ACTIF, INACTIF, CONGE, SUSPENDU }
}