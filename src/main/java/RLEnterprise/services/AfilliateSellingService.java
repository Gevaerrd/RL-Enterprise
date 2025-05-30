package RLEnterprise.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import RLEnterprise.entities.AfilliateSelling;
import RLEnterprise.entities.User;
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

    public double getTotalSales(Integer month) {
        List<AfilliateSelling> sales = (month == null) ? findAll() : findAllByMonth(month);
        return sales.stream().mapToDouble(AfilliateSelling::getComission).sum();
    }

    public double getTotalAfilliateSales(Integer month) {
        List<AfilliateSelling> sales = (month == null) ? findAll() : findAllByMonth(month);
        // Supondo que vendas de afiliados têm seller != null
        return sales.stream().filter(s -> s.getSeller() != null).mapToDouble(AfilliateSelling::getComission).sum();
    }

    public List<AfilliateSelling> findAllByMonth(Integer month) {
        if (month == null)
            return findAll();
        LocalDateTime start = LocalDateTime.of(LocalDateTime.now().getYear(), month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);
        return repository.findAllBySelledAtBetween(start, end);
    }

    public List<AfilliateSelling> findAllByUser(Long userId) {
        User seller = new User();
        seller.setId(userId);
        return repository.findAllBySeller(seller);
    }

    public List<AfilliateSelling> findAllByMonthAndUser(Integer month, Long userId) {
        if (month == null || userId == null)
            return findAll();
        LocalDateTime start = LocalDateTime.of(LocalDateTime.now().getYear(), month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);
        User seller = new User();
        seller.setId(userId);
        return repository.findAllBySellerAndSelledAtBetween(seller, start, end);
    }

}
