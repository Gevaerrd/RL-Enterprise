package RLEnterprise.resources;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import RLEnterprise.dto.UserProfileDTO;
import RLEnterprise.entities.AfilliateSelling;
import RLEnterprise.entities.User;
import RLEnterprise.entities.WithdrawRequest;
import RLEnterprise.repositories.UserRepository;
import RLEnterprise.services.AfilliateSellingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class PerfilRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AfilliateSellingService afilliateSellingService;

    @GetMapping("/profile/withdrawals")
    public ResponseEntity<?> getWithdrawals(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Usuário não autenticado."));
        }

        UserProfileDTO userProfile = (UserProfileDTO) session.getAttribute("user");
        // Busque o usuário do banco pelo ID
        User user = userRepository.findByEmail(userProfile.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Usuário não encontrado."));
        }

        List<WithdrawRequest> withdrawals = user.getWithdrawRequests();
        if (withdrawals == null) {
            return ResponseEntity.ok(Collections.singletonMap("withdrawals", List.of()));
        }

        List<Object> result = withdrawals.stream().map(w -> {
            return new Object() {
                public final Double value = w.getValue();
                public final int status = w.getStatus();
                public final String requestedAt = w.getRequestedAt().toString();
            };
        }).collect(Collectors.toList());

        return ResponseEntity.ok(Collections.singletonMap("withdrawals", result));
    }

    @GetMapping("/profile/afilliate-sales")
    public ResponseEntity<?> getAfilliateSales(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Usuário não autenticado."));
        }

        UserProfileDTO userProfile = (UserProfileDTO) session.getAttribute("user");
        User user = userRepository.findByEmail(userProfile.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Usuário não encontrado."));
        }

        List<AfilliateSelling> sales = user.getAfilliateSellings();
        List<Map<String, Object>> result = sales.stream().map(sale -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", sale.getSelledAt());
            map.put("firstName", sale.getBuyerName() != null ? sale.getBuyerName().split(" ")[0] : "");
            map.put("comission", sale.getComission());
            map.put("plan", sale.getPlan().getName());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(Collections.singletonMap("sales", result));
    }
}