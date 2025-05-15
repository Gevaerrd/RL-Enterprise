/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import RLEnterprise.dto.UserProfileDTO;
import RLEnterprise.entities.Plan;
import RLEnterprise.entities.User;
import RLEnterprise.services.PlanService;
import RLEnterprise.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class PlanController {

    @Autowired
    PlanService ps;

    @Autowired
    UserService us;

    @PostMapping("/signplan/{id}")
    public ResponseEntity<?> planControl(@PathVariable Long id, Model model, HttpServletRequest request) {

        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
            }

            UserProfileDTO userProfile = (UserProfileDTO) session.getAttribute("user");
            User originalUser = us.findByEmail(userProfile.getEmail());
            Plan plan = ps.findById(id);
            System.out.println(plan.getModulesToFree());

            if (plan == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plano não encontrado.");
            }

            originalUser.setPlan(plan);
            us.save(originalUser); // Certifique-se que existe esse método no seu UserService

            return ResponseEntity.ok().body("Plano assinado: " + plan.getName());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plano não encontrado.");
        }
    }

}
