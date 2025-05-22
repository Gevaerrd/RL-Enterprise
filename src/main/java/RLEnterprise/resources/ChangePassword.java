/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import RLEnterprise.entities.User;
import RLEnterprise.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class ChangePassword {

    @Autowired
    private UserService userService;

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body, HttpServletRequest request) {
        Map<String, Object> resp = new HashMap<>();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.put("error", "Usuário não autenticado.");
            return ResponseEntity.status(401).body(resp);
        }

        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        if (currentPassword == null || newPassword == null) {
            resp.put("error", "Preencha todos os campos.");
            return ResponseEntity.badRequest().body(resp);
        }

        // Filtro de senha forte
        String regex = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-={}:;\"',.<>/?]).{8,}$";
        if (!newPassword.matches(regex)) {
            resp.put("error",
                    "A nova senha deve ter no mínimo 8 caracteres<br>. 1 letra maiúscula<br>. 1 caractere especial.");
            return ResponseEntity.badRequest().body(resp);
        }

        // Pega o usuário da sessão
        RLEnterprise.dto.UserProfileDTO userDTO = (RLEnterprise.dto.UserProfileDTO) session.getAttribute("user");
        User user = userService.findByEmail(userDTO.getEmail());

        // Checa se a senha atual está correta (ajuste se usar hash)
        if (!userService.getPasswordEncoder().matches(currentPassword, user.getPassword())) {
            resp.put("error", "Senha atual incorreta.");
            return ResponseEntity.badRequest().body(resp);
        }

        // Atualiza a senha
        userService.setPassword(newPassword, user);

        resp.put("success", true);
        return ResponseEntity.ok(resp);
    }
}
