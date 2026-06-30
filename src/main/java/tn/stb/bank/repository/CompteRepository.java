package tn.stb.bank.repository;

import tn.stb.bank.model.CompteBancaire;
import tn.stb.bank.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompteRepository extends JpaRepository<CompteBancaire, Long> {
    Optional<CompteBancaire> findByNumeroCompte(String numeroCompte);
    Optional<CompteBancaire> findByIban(String iban);
    List<CompteBancaire> findByClient(Client client);
    List<CompteBancaire> findByStatut(CompteBancaire.StatutCompte statut);

    @Query("SELECT SUM(c.solde) FROM CompteBancaire c WHERE c.statut = 'ACTIF'")
    Double totalSoldes();
}
