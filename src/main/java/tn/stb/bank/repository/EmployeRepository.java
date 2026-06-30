package tn.stb.bank.repository;

import tn.stb.bank.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {
    Optional<Employe> findByMatricule(String matricule);
    List<Employe> findByDepartement(Employe.Departement departement);
    List<Employe> findByStatut(Employe.StatutEmploye statut);

    @Query("SELECT e FROM Employe e WHERE " +
           "LOWER(e.nom) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(e.prenom) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(e.poste) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(e.matricule) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Employe> search(@Param("q") String query);

    @Query("SELECT AVG(e.salaire) FROM Employe e WHERE e.statut = 'ACTIF'")
    Double avgSalaire();
}
