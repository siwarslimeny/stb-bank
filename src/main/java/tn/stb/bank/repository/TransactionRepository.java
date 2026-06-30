package tn.stb.bank.repository;

import tn.stb.bank.model.Transaction;
import tn.stb.bank.model.CompteBancaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByReference(String reference);
    List<Transaction> findByCompteSourceOrderByDateOperationDesc(CompteBancaire compte);
    List<Transaction> findByStatutOrderByDateOperationDesc(Transaction.StatutTransaction statut);
    List<Transaction> findAllByOrderByDateOperationDesc();

    @Query("SELECT t FROM Transaction t WHERE t.dateOperation BETWEEN :debut AND :fin ORDER BY t.dateOperation DESC")
    List<Transaction> findByPeriode(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);

    @Query("SELECT SUM(t.montant) FROM Transaction t WHERE t.sens = 'CREDIT' AND t.statut = 'VALIDE'")
    Double totalCredits();

    @Query("SELECT SUM(t.montant) FROM Transaction t WHERE t.sens = 'DEBIT' AND t.statut = 'VALIDE'")
    Double totalDebits();

    @Query("SELECT t FROM Transaction t WHERE " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(t.reference) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Transaction> search(@Param("q") String query);
}
