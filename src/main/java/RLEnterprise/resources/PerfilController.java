/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

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
        User user = (User) auth.getPrincipal();
        // Monta o DTO para a view (opcional, se quiser usar o DTO)
        UserProfileDTO userDTO = us.findUserDTOByEmail(user.getEmail());
        model.addAttribute("user", userDTO);

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
