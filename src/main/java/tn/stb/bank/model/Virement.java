package tn.stb.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "virements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Virement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_debiteur_id")
    private CompteBancaire compteDebiteur;

    @Column(name = "iban_crediteur")
    private String ibanCrediteur;

    @Column(name = "nom_beneficiaire")
    private String nomBeneficiaire;

    @NotNull
    @Positive
    private Double montant;

    private String motif;
    private String devise = "TND";

    @Enumerated(EnumType.STRING)
    private StatutVirement statut = StatutVirement.EN_ATTENTE;

    @Enumerated(EnumType.STRING)
    private TypeVirement type = TypeVirement.ORDINAIRE;

    @Column(name = "date_demande")
    private LocalDateTime dateDemande = LocalDateTime.now();

    @Column(name = "date_execution")
    private LocalDateTime dateExecution;

    @Column(name = "motif_rejet")
    private String motifRejet;

    private Double frais = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operateur_id")
    private Utilisateur operateur;

    public enum StatutVirement { EN_ATTENTE, VALIDE, REJETE, ANNULE, EXECUTE }
    public enum TypeVirement { ORDINAIRE, URGENT, PERMANENT, INTERNATIONAL }
}