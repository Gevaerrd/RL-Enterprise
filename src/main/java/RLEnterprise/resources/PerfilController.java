/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import RLEnterprise.dto.UserProfileDTO;
import RLEnterprise.entities.User;
import RLEnterprise.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PerfilController {

    @Autowired
    UserService us;

    @RequestMapping("/profile")
    public String userDashboard(Model model) {
        // Obtém o usuário autenticado do contexto do Spring Security
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userAuth = (User) auth.getPrincipal();
        User user = us.findByEmail(userAuth.getEmail()); // Busca o usuário atualizado do banco
        UserProfileDTO userDTO = us.findUserDTOByEmail(user.getEmail());
        model.addAttribute("user", userDTO);

        // Lógica do aviso de expiração
        if (user.getPlan() != null && user.getPlanStartDate() != null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiration = user.getPlanStartDate().plusDays(30); // ou a duração do seu plano
            long daysLeft = java.time.Duration.between(now, expiration).toDays();

            if (daysLeft <= 3 && daysLeft > 0) {
                model.addAttribute("planExpiringSoon", true);
                model.addAttribute("daysLeft", daysLeft);
            }
        }

        if (user.getPlan() != null) {
            return "UserUIWP";
        } else {
            return "UserUINP";
        }
    }

    @RequestMapping("/userlogout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
