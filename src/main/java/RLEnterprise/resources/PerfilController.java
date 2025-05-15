/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import RLEnterprise.dto.UserProfileDTO;
import RLEnterprise.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PerfilController {

    @Autowired
    UserService us;

    @RequestMapping("/profile")
    public String userDashboard(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/";
        }

        UserProfileDTO userDTO = (UserProfileDTO) session.getAttribute("user");
        model.addAttribute("user", userDTO);

        if (us.findByEmail(userDTO.getEmail()).getPlan() != null) { // Se o usuario tiver um plano
            return "UserUIWP"; // Retorna esse HTML
        }

        else {
            return "UserUINP"; // Caso contrário esse
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
