package RLEnterprise.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import RLEnterprise.entities.AfilliateSelling;
import RLEnterprise.repositories.AfilliateSellingRepository;

@Service
public class AfilliateSellingService {

    @Autowired
    private AfilliateSellingRepository repository;

    public AfilliateSelling findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("AfilliateSelling not found with ID: " + id));
    }

    public List<AfilliateSelling> findAll() {
        return repository.findAll();
    }

    public AfilliateSelling save(AfilliateSelling afilliateSelling) {
        return repository.save(afilliateSelling);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Cannot delete. AfilliateSelling not found with ID: " + id);
        }
        repository.deleteById(id);
    }

}
