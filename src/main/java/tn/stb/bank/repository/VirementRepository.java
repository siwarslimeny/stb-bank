package tn.stb.bank.repository;

import tn.stb.bank.model.Virement;
import tn.stb.bank.model.CompteBancaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VirementRepository extends JpaRepository<Virement, Long> {
    Optional<Virement> findByReference(String reference);
    List<Virement> findByCompteDebiteurOrderByDateDemandeDesc(CompteBancaire compte);
    List<Virement> findByStatutOrderByDateDemandeDesc(Virement.StatutVirement statut);
    List<Virement> findAllByOrderByDateDemandeDesc();
}
