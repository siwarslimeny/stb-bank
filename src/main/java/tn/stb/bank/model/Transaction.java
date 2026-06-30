package tn.stb.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeTransaction type;

    @NotNull
    @Positive(message = "Le montant doit être positif")
    private Double montant;

    @Enumerated(EnumType.STRING)
    private SensTransaction sens;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @Enumerated(EnumType.STRING)
    private StatutTransaction statut = StatutTransaction.EN_COURS;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_source_id")
    private CompteBancaire compteSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_dest_id")
    private CompteBancaire compteDestination;

    @Column(name = "date_operation")
    private LocalDateTime dateOperation = LocalDateTime.now();

    @Column(name = "date_valeur")
    private LocalDateTime dateValeur;

    @Column(name = "motif_rejet")
    private String motifRejet;

    private String devise = "TND";

    @Column(name = "frais")
    private Double frais = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operateur_id")
    private Utilisateur operateur;

    public enum TypeTransaction {
        VIREMENT, DEPOT, RETRAIT, PAIEMENT, CHEQUE, PRELEVEMENT, CREDIT, REMBOURSEMENT
    }
    public enum SensTransaction { CREDIT, DEBIT }
    public enum StatutTransaction { EN_COURS, VALIDE, REJETE, ANNULE }
}