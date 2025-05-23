/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import RLEnterprise.entities.User;
import RLEnterprise.services.EmailService;
import RLEnterprise.services.UserService;

@RestController
@RequestMapping("/api2fa")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService us;

    @PostMapping("/request")
    public Map<String, String> sendEmail(@RequestBody Map<String, String> payload) {
        String to = payload.get("to");
        // {to=email@hotmail.com} -> a resposta vem assim
        emailService.send2FACode(to);
        return Map.of("message", "E-mail enviado para " + to);
    }

    @PostMapping("/verify-2fa")
    public Map<String, String> verify2fa(@RequestBody Map<String, String> payload) {
        String code2fa = payload.get("2facode");
        User user = us.findBytwoFactorCode(code2fa);

        if (user != null && code2fa != null &&
                code2fa.equals(user.getTwoFactorCode())) {

            Long generatedAt = user.getTwoFactorCodeGeneratedAt();
            long now = System.currentTimeMillis();
            if (generatedAt == null || now - generatedAt > 5 * 60 * 1000) { // 5 min
                return Map.of("not-verified", "Código expirado");
            }

            user.setTwoFactorCode(null);
            user.setTwoFactorCodeGeneratedAt(null);
            us.save(user);
            return Map.of("verified", "Código certo");
        }

        else {
            return Map.of("not-verified", "Código errado");
        }
    }

    @PostMapping("/reset-password")
    public Map<String, String> resetPassword(@RequestBody Map<String, String> payload) {
        String code2fa = payload.get("2facode");
        String newPassword = payload.get("newPassword");
        User user = us.findBytwoFactorCode(code2fa);

        // Filtro de senha
        String regex = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-={}:;\"',.<>/?]).{8,}$";
        if (newPassword == null || !newPassword.matches(regex)) {
            return Map.of("Error",
                    "A senha deve ter no mínimo 8 caracteres, 1 letra maiúscula e 1 caractere especial.");
        }

        if (user != null && code2fa != null &&
                code2fa.equals(user.getTwoFactorCode())) {

            Long generatedAt = user.getTwoFactorCodeGeneratedAt();
            long now = System.currentTimeMillis();
            if (generatedAt == null || now - generatedAt > 5 * 60 * 1000) {
                return Map.of("not-verified", "Código expirado");
            }

            // Valide a senha conforme sua regra de negócio
            us.setPassword(newPassword, user);
            user.setTwoFactorCode(null);
            user.setTwoFactorCodeGeneratedAt(null);
            us.save(user);
            return Map.of("success", "Senha redefinida com sucesso!");
        }

        else {
            return Map.of("not-verified", "Código errado");
        }
    }
}
