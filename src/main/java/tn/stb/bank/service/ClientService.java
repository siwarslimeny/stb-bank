package tn.stb.bank.service;

import tn.stb.bank.model.Client;
import tn.stb.bank.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repo;
    private int seq = 9;

    public List<Client> findAll() {
        return repo.findAll();
    }

    public Optional<Client> findById(Long id) {
        return repo.findById(id);
    }

    public List<Client> search(String q) {
        return (q == null || q.isBlank()) ? findAll() : repo.search(q);
    }

    @Transactional
    public Client save(Client c) {
        if (c.getNumeroClient() == null || c.getNumeroClient().isBlank()) {
            c.setNumeroClient("CL-" + String.format("%03d", seq++));
        }
        return repo.save(c);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    public long countActifs() {
        return repo.countActifs();
    }

    public long countAll() {
        return repo.count();
    }
}