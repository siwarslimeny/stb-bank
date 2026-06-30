package tn.stb.bank.repository;

import tn.stb.bank.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByNumeroClient(String numeroClient);
    Optional<Client> findByCin(String cin);
    Optional<Client> findByEmail(String email);

    @Query("SELECT c FROM Client c WHERE " +
           "LOWER(c.nom) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(c.prenom) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(c.numeroClient) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Client> search(@Param("q") String query);

    List<Client> findByStatut(Client.StatutClient statut);
    List<Client> findByAgence(String agence);

    @Query("SELECT COUNT(c) FROM Client c WHERE c.statut = 'ACTIF'")
    long countActifs();
}
