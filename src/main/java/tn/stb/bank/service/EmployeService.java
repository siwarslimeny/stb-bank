package tn.stb.bank.service;

import tn.stb.bank.model.Employe;
import tn.stb.bank.repository.EmployeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeService {

    private final EmployeRepository repo;
    private int seq = 9;

    public List<Employe> findAll() {
        return repo.findAll();
    }

    public Optional<Employe> findById(Long id) {
        return repo.findById(id);
    }

    public List<Employe> search(String q) {
        return (q == null || q.isBlank()) ? findAll() : repo.search(q);
    }

    @Transactional
    public Employe save(Employe e) {
        if (e.getMatricule() == null || e.getMatricule().isBlank()) {
            e.setMatricule("EMP-" + String.format("%03d", seq++));
        }
        return repo.save(e);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    public double avgSalaire() {
        Double v = repo.avgSalaire();
        return v != null ? v : 0.0;
    }

    public long count() {
        return repo.count();
    }
}