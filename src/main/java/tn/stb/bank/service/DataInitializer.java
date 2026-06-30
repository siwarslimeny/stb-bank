package tn.stb.bank.service;

import tn.stb.bank.model.*;
import tn.stb.bank.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository userRepo;
    private final ClientRepository clientRepo;
    private final CompteRepository compteRepo;
    private final TransactionRepository txRepo;
    private final EmployeRepository empRepo;
    private final VirementRepository virRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        log.info("=== Initialisation des données STB ===");
        seedUsers();
        seedClients();
        seedComptes();
        seedTransactions();
        seedEmployes();
        seedVirements();
        log.info("=== Données initialisées avec succès ===");
    }

    private void seedUsers() {
        if (userRepo.count() > 0) return;
        userRepo.save(Utilisateur.builder().username("admin").password(encoder.encode("admin123"))
                .nom("Mansouri").prenom("Tarek").email("t.mansouri@stb.com.tn")
                .telephone("22 100 001").role(Role.ADMIN).actif(true).build());
        userRepo.save(Utilisateur.builder().username("alice.info").password(encoder.encode("pass123"))
                .nom("Ben Said").prenom("Alice").email("a.bensaid@stb.com.tn")
                .telephone("22 100 002").role(Role.INFO).actif(true).build());
        userRepo.save(Utilisateur.builder().username("bob.compta").password(encoder.encode("pass123"))
                .nom("Trabelsi").prenom("Bob").email("b.trabelsi@stb.com.tn")
                .telephone("22 100 003").role(Role.COMPTABILITE).actif(true).build());
        userRepo.save(Utilisateur.builder().username("carol.rh").password(encoder.encode("pass123"))
                .nom("Amira").prenom("Carol").email("c.amira@stb.com.tn")
                .telephone("22 100 004").role(Role.RH).actif(true).build());
    }

    private void seedClients() {
        if (clientRepo.count() > 0) return;
        clientRepo.save(Client.builder().numeroClient("CL-001").nom("Gharbi").prenom("Ahmed")
                .email("ahmed.gharbi@mail.tn").telephone("22 456 789").agence("Tunis Centre")
                .cin("08124567").profession("Ingénieur").typeClient(Client.TypeClient.PARTICULIER)
                .statut(Client.StatutClient.ACTIF).dateNaissance(LocalDate.of(1985, 6, 15))
                .revenuMensuel(4500.0).ville("Tunis").build());
        clientRepo.save(Client.builder().numeroClient("CL-002").nom("Sfar").prenom("Leila")
                .email("l.sfar@mail.tn").telephone("55 321 654").agence("La Marsa")
                .cin("08234678").profession("Médecin").typeClient(Client.TypeClient.PARTICULIER)
                .statut(Client.StatutClient.ACTIF).dateNaissance(LocalDate.of(1980, 3, 22))
                .revenuMensuel(7200.0).ville("La Marsa").build());
        clientRepo.save(Client.builder().numeroClient("CL-003").nom("Turki").prenom("Karim")
                .email("k.turki@mail.tn").telephone("23 987 654").agence("Sousse")
                .cin("08345789").profession("Commerçant").typeClient(Client.TypeClient.PARTICULIER)
                .statut(Client.StatutClient.ACTIF).dateNaissance(LocalDate.of(1978, 9, 10))
                .revenuMensuel(3500.0).ville("Sousse").build());
        clientRepo.save(Client.builder().numeroClient("CL-004").nom("Bouzid").prenom("Mariem")
                .email("m.bouzid@mail.tn").telephone("98 765 432").agence("Sfax")
                .cin("08456890").profession("Professeur").typeClient(Client.TypeClient.PARTICULIER)
                .statut(Client.StatutClient.BLOQUE).dateNaissance(LocalDate.of(1992, 1, 5))
                .revenuMensuel(2200.0).ville("Sfax").build());
        clientRepo.save(Client.builder().numeroClient("CL-005").nom("Hamdi").prenom("Sonia")
                .email("s.hamdi@mail.tn").telephone("42 135 246").agence("Nabeul")
                .cin("08567901").profession("Architecte").typeClient(Client.TypeClient.PARTICULIER)
                .statut(Client.StatutClient.INACTIF).dateNaissance(LocalDate.of(1990, 7, 18))
                .revenuMensuel(5100.0).ville("Nabeul").build());
        clientRepo.save(Client.builder().numeroClient("CL-006").nom("Keskes").prenom("Nabil")
                .email("n.keskes@mail.tn").telephone("71 234 000").agence("Tunis Centre")
                .cin("08678012").profession("Avocat").typeClient(Client.TypeClient.PARTICULIER)
                .statut(Client.StatutClient.ACTIF).dateNaissance(LocalDate.of(1975, 11, 28))
                .revenuMensuel(8500.0).ville("Tunis").build());
        clientRepo.save(Client.builder().numeroClient("CL-007").nom("ElAmal SARL").prenom("Société")
                .email("contact@elamal.tn").telephone("71 888 999").agence("Ariana")
                .cin("E001234").typeClient(Client.TypeClient.ENTREPRISE)
                .statut(Client.StatutClient.ACTIF).revenuMensuel(50000.0).ville("Ariana").build());
        clientRepo.save(Client.builder().numeroClient("CL-008").nom("InvTech SARL").prenom("Société")
                .email("info@invtech.tn").telephone("71 777 888").agence("Tunis Centre")
                .cin("E005678").typeClient(Client.TypeClient.ENTREPRISE)
                .statut(Client.StatutClient.ACTIF).revenuMensuel(150000.0).ville("Tunis").build());
    }

    private void seedComptes() {
        if (compteRepo.count() > 0) return;
        List<Client> clients = clientRepo.findAll();
        if (clients.isEmpty()) return;
        String[][] data = {
                {"10011234560001","TN5910011234560001","12400.0","COURANT"},
                {"10012345670001","TN5910012345670001","3850.0","COURANT"},
                {"10023456780001","TN5910023456780001","5500.0","EPARGNE"},
                {"10034567890001","TN5910034567890001","1200.0","COURANT"},
                {"10045678900001","TN5910045678900001","720.0","COURANT"},
                {"10056789010001","TN5910056789010001","9800.0","EPARGNE"},
                {"20067890120001","TN5920067890120001","87200.0","PROFESSIONNEL"},
                {"20078901230001","TN5920078901230001","145000.0","PROFESSIONNEL"},
        };
        for (int i = 0; i < clients.size() && i < data.length; i++) {
            compteRepo.save(CompteBancaire.builder()
                    .numeroCompte(data[i][0]).rib(data[i][0]).iban(data[i][1])
                    .solde(Double.parseDouble(data[i][2]))
                    .typeCompte(CompteBancaire.TypeCompte.valueOf(data[i][3]))
                    .statut(CompteBancaire.StatutCompte.ACTIF)
                    .client(clients.get(i)).devise("TND").build());
        }
    }

    private void seedTransactions() {
        if (txRepo.count() > 0) return;
        List<CompteBancaire> comptes = compteRepo.findAll();
        if (comptes.isEmpty()) return;
        Utilisateur op = userRepo.findByUsername("bob.compta").orElse(null);
        Object[][] txData = {
                {"TX-2401","Salaire janvier",5200.0,
                        Transaction.TypeTransaction.VIREMENT, Transaction.SensTransaction.CREDIT,
                        Transaction.StatutTransaction.VALIDE, 0},
                {"TX-2402","Retrait ATM",800.0,
                        Transaction.TypeTransaction.RETRAIT, Transaction.SensTransaction.DEBIT,
                        Transaction.StatutTransaction.VALIDE, 1},
                {"TX-2403","Facture fournisseur",3750.0,
                        Transaction.TypeTransaction.PAIEMENT, Transaction.SensTransaction.DEBIT,
                        Transaction.StatutTransaction.VALIDE, 2},
                {"TX-2404","Remboursement crédit",1100.0,
                        Transaction.TypeTransaction.REMBOURSEMENT, Transaction.SensTransaction.CREDIT,
                        Transaction.StatutTransaction.VALIDE, 0},
                {"TX-2405","Virement inter-banque",25000.0,
                        Transaction.TypeTransaction.VIREMENT, Transaction.SensTransaction.CREDIT,
                        Transaction.StatutTransaction.VALIDE, 6},
                {"TX-2406","Règlement loyer",450.0,
                        Transaction.TypeTransaction.CHEQUE, Transaction.SensTransaction.DEBIT,
                        Transaction.StatutTransaction.EN_COURS, 4},
                {"TX-2407","Assurance annuelle",1200.0,
                        Transaction.TypeTransaction.PAIEMENT, Transaction.SensTransaction.DEBIT,
                        Transaction.StatutTransaction.REJETE, 1},
                {"TX-2408","Dépôt espèces",500.0,
                        Transaction.TypeTransaction.DEPOT, Transaction.SensTransaction.CREDIT,
                        Transaction.StatutTransaction.VALIDE, 4},
        };
        for (int i = 0; i < txData.length; i++) {
            Object[] d = txData[i];
            int idx = (int) d[6];
            txRepo.save(Transaction.builder()
                    .reference((String) d[0]).description((String) d[1])
                    .montant((Double) d[2])
                    .type((Transaction.TypeTransaction) d[3])
                    .sens((Transaction.SensTransaction) d[4])
                    .statut((Transaction.StatutTransaction) d[5])
                    .compteSource(comptes.get(idx < comptes.size() ? idx : 0))
                    .dateOperation(LocalDateTime.now().minusDays(i))
                    .dateValeur(LocalDateTime.now())
                    .operateur(op).devise("TND").frais(0.0).build());
        }
    }

    private void seedEmployes() {
        if (empRepo.count() > 0) return;
        empRepo.save(Employe.builder().matricule("EMP-001").nom("Mansouri").prenom("Tarek")
                .email("t.mansouri@stb.com.tn").telephone("22 111 001").poste("Directeur général")
                .departement(Employe.Departement.DIRECTION).salaire(8500.0)
                .contrat(Employe.TypeContrat.CDI).statut(Employe.StatutEmploye.ACTIF)
                .dateEmbauche(LocalDate.of(2015, 1, 1)).agence("Tunis Centre").build());
        empRepo.save(Employe.builder().matricule("EMP-002").nom("Ben Said").prenom("Alice")
                .email("a.bensaid@stb.com.tn").telephone("22 111 002").poste("Agent info clients")
                .departement(Employe.Departement.INFORMATION).salaire(2800.0)
                .contrat(Employe.TypeContrat.CDI).statut(Employe.StatutEmploye.ACTIF)
                .dateEmbauche(LocalDate.of(2019, 3, 15)).agence("Tunis Centre").build());
        empRepo.save(Employe.builder().matricule("EMP-003").nom("Trabelsi").prenom("Bob")
                .email("b.trabelsi@stb.com.tn").telephone("22 111 003").poste("Chef comptable")
                .departement(Employe.Departement.COMPTABILITE).salaire(4200.0)
                .contrat(Employe.TypeContrat.CDI).statut(Employe.StatutEmploye.ACTIF)
                .dateEmbauche(LocalDate.of(2017, 9, 1)).agence("Tunis Centre").build());
        empRepo.save(Employe.builder().matricule("EMP-004").nom("Amira").prenom("Carol")
                .email("c.amira@stb.com.tn").telephone("22 111 004").poste("Responsable RH")
                .departement(Employe.Departement.RH).salaire(3900.0)
                .contrat(Employe.TypeContrat.CDI).statut(Employe.StatutEmploye.ACTIF)
                .dateEmbauche(LocalDate.of(2018, 6, 20)).agence("Tunis Centre").build());
        empRepo.save(Employe.builder().matricule("EMP-005").nom("Keskes").prenom("Nadia")
                .email("n.keskes@stb.com.tn").telephone("22 111 005").poste("Conseillère clientèle")
                .departement(Employe.Departement.INFORMATION).salaire(2600.0)
                .contrat(Employe.TypeContrat.CDI).statut(Employe.StatutEmploye.ACTIF)
                .dateEmbauche(LocalDate.of(2021, 1, 10)).agence("La Marsa").build());
        empRepo.save(Employe.builder().matricule("EMP-006").nom("Oueslati").prenom("Samir")
                .email("s.oueslati@stb.com.tn").telephone("22 111 006").poste("Analyste financier")
                .departement(Employe.Departement.COMPTABILITE).salaire(3500.0)
                .contrat(Employe.TypeContrat.CDI).statut(Employe.StatutEmploye.ACTIF)
                .dateEmbauche(LocalDate.of(2020, 4, 5)).agence("Tunis Centre").build());
        empRepo.save(Employe.builder().matricule("EMP-007").nom("Zouari").prenom("Ines")
                .email("i.zouari@stb.com.tn").telephone("22 111 007").poste("Assistante RH")
                .departement(Employe.Departement.RH).salaire(2200.0)
                .contrat(Employe.TypeContrat.CDD).statut(Employe.StatutEmploye.ACTIF)
                .dateEmbauche(LocalDate.of(2023, 2, 1)).agence("Tunis Centre").build());
        empRepo.save(Employe.builder().matricule("EMP-008").nom("Jlassi").prenom("Mehdi")
                .email("m.jlassi@stb.com.tn").telephone("22 111 008").poste("Développeur SI")
                .departement(Employe.Departement.IT).salaire(3800.0)
                .contrat(Employe.TypeContrat.CDI).statut(Employe.StatutEmploye.CONGE)
                .dateEmbauche(LocalDate.of(2022, 7, 15)).agence("Tunis Centre").build());
    }

    private void seedVirements() {
        if (virRepo.count() > 0) return;
        List<CompteBancaire> comptes = compteRepo.findAll();
        if (comptes.size() < 2) return;
        Utilisateur op = userRepo.findByUsername("bob.compta").orElse(null);
        virRepo.save(Virement.builder().reference("VIR-001").montant(2000.0)
                .compteDebiteur(comptes.get(0)).ibanCrediteur("TN5910012345670001")
                .nomBeneficiaire("Leila Sfar").motif("Remboursement prêt")
                .type(Virement.TypeVirement.ORDINAIRE).statut(Virement.StatutVirement.EXECUTE)
                .dateExecution(LocalDateTime.now().minusDays(2))
                .operateur(op).frais(1.0).build());
        virRepo.save(Virement.builder().reference("VIR-002").montant(5500.0)
                .compteDebiteur(comptes.get(1)).ibanCrediteur("TN5910023456780001")
                .nomBeneficiaire("Karim Turki").motif("Paiement facture")
                .type(Virement.TypeVirement.URGENT).statut(Virement.StatutVirement.EN_ATTENTE)
                .operateur(op).frais(5.0).build());
        virRepo.save(Virement.builder().reference("VIR-003").montant(15000.0)
                .compteDebiteur(comptes.get(comptes.size() > 6 ? 6 : 0))
                .ibanCrediteur("TN5920078901230001").nomBeneficiaire("InvTech SARL")
                .motif("Règlement contrat").type(Virement.TypeVirement.ORDINAIRE)
                .statut(Virement.StatutVirement.VALIDE).operateur(op).frais(2.5).build());
    }
}