package tn.stb.bank.service;

import tn.stb.bank.model.Transaction;
import tn.stb.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repo;
    private int seq = 9;

    public List<Transaction> findAll() {
        return repo.findAllByOrderByDateOperationDesc();
    }

    public Optional<Transaction> findById(Long id) {
        return repo.findById(id);
    }

    public List<Transaction> search(String q) {
        return (q == null || q.isBlank()) ? findAll() : repo.search(q);
    }

    @Transactional
    public Transaction save(Transaction t) {
        if (t.getReference() == null || t.getReference().isBlank()) {
            t.setReference("TX-" + String.format("%04d", 2400 + seq++));
        }
        return repo.save(t);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    public double totalCredits() {
        Double v = repo.totalCredits();
        return v != null ? v : 0.0;
    }

    public double totalDebits() {
        Double v = repo.totalDebits();
        return v != null ? v : 0.0;
    }

    public long count() {
        return repo.count();
    }
}