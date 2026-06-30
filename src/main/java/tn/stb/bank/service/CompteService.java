package tn.stb.bank.service;

import tn.stb.bank.model.CompteBancaire;
import tn.stb.bank.repository.ClientRepository;
import tn.stb.bank.repository.CompteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompteService {

    private final CompteRepository repo;
    private final ClientRepository clientRepo;

    public List<CompteBancaire> findAll() {
        return repo.findAll();
    }

    public Optional<CompteBancaire> findById(Long id) {
        return repo.findById(id);
    }

    public List<CompteBancaire> findByClient(Long clientId) {
        return clientRepo.findById(clientId)
                .map(repo::findByClient)
                .orElse(List.of());
    }

    @Transactional
    public CompteBancaire save(CompteBancaire c) {
        return repo.save(c);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Double totalSoldes() {
        Double t = repo.totalSoldes();
        return t != null ? t : 0.0;
    }
}