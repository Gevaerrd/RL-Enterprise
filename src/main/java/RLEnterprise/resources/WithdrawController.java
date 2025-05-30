/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import RLEnterprise.dto.UserProfileDTO;
import RLEnterprise.entities.User;
import RLEnterprise.entities.WithdrawRequest;
import RLEnterprise.repositories.WithdrawRequestRepository;
import RLEnterprise.services.UserService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/profile")
public class WithdrawController {

    @Autowired
    private UserService userService;

    @Autowired
    private WithdrawRequestRepository withdrawRequestRepository;

    private boolean isValidCPF(String cpf) {
        if (cpf == null)
            return false;
        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1)
            return false;
        try {
            int d1 = 0, d2 = 0;
            for (int i = 0; i < 9; i++) {
                int digito = Character.getNumericValue(cpf.charAt(i));
                d1 += digito * (10 - i);
                d2 += digito * (11 - i);
            }
            int resto1 = (d1 * 10) % 11;
            if (resto1 == 10)
                resto1 = 0;
            d2 += resto1 * 2;
            int resto2 = (d2 * 10) % 11;
            if (resto2 == 10)
                resto2 = 0;
            return resto1 == Character.getNumericValue(cpf.charAt(9)) &&
                    resto2 == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping("/withdraw")
    public Map<String, Object> withdraw(@RequestBody Map<String, String> payload, HttpSession session) {
        Map<String, Object> resp = new HashMap<>();

        // Checa se o usuário está logado e é UserProfileDTO
        UserProfileDTO userDTO = (UserProfileDTO) session.getAttribute("user");
        if (userDTO == null) {
            resp.put("error", "Usuário não autenticado.");
            return resp;
        }

        User user = userService.findByEmail(userDTO.getEmail());
        if (user == null) {
            resp.put("error", "Usuário não encontrado.");
            return resp;
        }

        // Checa se já existe saque pendente
        boolean hasPending = user.getWithdrawRequests().stream()
                .anyMatch(wr -> wr.getStatus() == 0); // 0 = PENDENTE
        if (hasPending) {
            resp.put("error", "Você já possui um saque pendente. Aguarde aprovação.");
            return resp;
        }

        // Checa saldo
        if (user.getBalance() <= 0) {
            resp.put("error", "Saldo insuficiente.");
            return resp;
        }

        // Checa/salva CPF se necessário
        String cpf = payload.get("cpf");
        cpf = (cpf != null) ? cpf.replaceAll("\\D", "") : "";
        if (user.getCpf() == null || user.getCpf().isEmpty()) {
            if (cpf.length() != 11) {
                resp.put("error", "Informe um CPF válido para PIX.");
                return resp;
            }
            // Validação de formato e dígitos
            if (!isValidCPF(cpf)) {
                resp.put("error", "CPF inválido.");
                return resp;
            }
            // Checa se já existe usuário com esse CPF
            User userByCpf = userService.findByCpf(cpf);
            if (userByCpf != null && !userByCpf.getId().equals(user.getId())) {
                resp.put("error", "Este CPF já está cadastrado em outra conta.");
                return resp;
            }
            user.setCpf(cpf);
            userService.save(user);
        }

        // Cria solicitação de saque
        WithdrawRequest wr = new WithdrawRequest();
        wr.setUser(user);
        wr.setStatus(0); // 0 = PENDENTE
        wr.setRequestedAt(java.time.LocalDateTime.now());
        wr.setValue(user.getBalance());
        withdrawRequestRepository.save(wr);

        user.addWithdrawRequest(wr);
        userService.save(user);

        resp.put("success", true);
        resp.put("message", "Saque solicitado! Aguarde aprovação.");
        return resp;
    }
}