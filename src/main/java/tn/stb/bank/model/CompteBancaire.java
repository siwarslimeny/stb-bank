package tn.stb.bank.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comptes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompteBancaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroCompte;

    @Column(name = "rib", unique = true)
    private String rib;

    @Column(name = "iban", unique = true)
    private String iban;

    @Enumerated(EnumType.STRING)
    private TypeCompte typeCompte = TypeCompte.COURANT;

    private Double solde = 0.0;

    @Column(name = "plafond_debit")
    private Double plafondDebit = 0.0;

    @Enumerated(EnumType.STRING)
    private StatutCompte statut = StatutCompte.ACTIF;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "date_ouverture")
    private LocalDateTime dateOuverture = LocalDateTime.now();

    @Column(name = "date_fermeture")
    private LocalDateTime dateFermeture;

    private String devise = "TND";

    @Column(name = "taux_interet")
    private Double tauxInteret = 0.0;

    public enum TypeCompte { COURANT, EPARGNE, TERME, PROFESSIONNEL }
    public enum StatutCompte { ACTIF, BLOQUE, FERME, EN_ATTENTE }
}