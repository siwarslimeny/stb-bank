package tn.stb.bank.service;

import tn.stb.bank.model.Virement;
import tn.stb.bank.repository.VirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VirementService {

    private final VirementRepository repo;
    private int seq = 4;

    public List<Virement> findAll() {
        return repo.findAllByOrderByDateDemandeDesc();
    }

    public Optional<Virement> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Virement save(Virement v) {
        if (v.getReference() == null || v.getReference().isBlank()) {
            v.setReference("VIR-" + String.format("%03d", seq++));
        }
        return repo.save(v);
    }

    @Transactional
    public void valider(Long id) {
        repo.findById(id).ifPresent(v -> {
            v.setStatut(Virement.StatutVirement.EXECUTE);
            v.setDateExecution(LocalDateTime.now());
            repo.save(v);
        });
    }

    @Transactional
    public void rejeter(Long id, String motif) {
        repo.findById(id).ifPresent(v -> {
            v.setStatut(Virement.StatutVirement.REJETE);
            v.setMotifRejet(motif);
            repo.save(v);
        });
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}