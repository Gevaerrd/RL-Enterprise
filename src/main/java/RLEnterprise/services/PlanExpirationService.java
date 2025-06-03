package RLEnterprise.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import RLEnterprise.entities.User;

@Service
public class PlanExpirationService {

    @Autowired
    private UserService userService;

    // Roda a cada 24 horas (86_400_000 ms) - ajuste para produção
    @Scheduled(fixedRate = 86_400_000)
    public void removeExpiredPlans() {
        List<User> users = userService.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (User user : users) {
            if (user.getPlan() != null && user.getPlanStartDate() != null) {
                // Expira após 30 dias
                if (user.getPlanStartDate().plusDays(30).isBefore(now)) {
                    System.out.println(
                            "[PlanExpiration] Plano removido do usuário: " + user.getEmail() +
                                    " | Plano: " + user.getPlan().getName() +
                                    " | Início: " + user.getPlanStartDate() +
                                    " | Removido em: " + now);
                    user.setPlan(null);
                    user.setAfilliateCode(null);
                    user.setPlanStartDate(null);
                    userService.save(user);
                }
            }
        }
    }
}