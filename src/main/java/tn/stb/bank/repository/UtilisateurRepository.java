package tn.stb.bank.repository;

import tn.stb.bank.model.Role;
import tn.stb.bank.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByUsername(String username);
    List<Utilisateur> findByRole(Role role);
    boolean existsByUsername(String username);
}
