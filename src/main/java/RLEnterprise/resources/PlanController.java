/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Plans.Plan;
import Plans.PlanFactory;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class PlanController {

    @PostMapping("/signplan")
    public ResponseEntity<?> planControl(@PathVariable int id, Model model, HttpServletRequest request) {

        try {
            Plan plan = PlanFactory.getPlanById(id);
            return ResponseEntity.ok().body("Plano assinado: " + plan.getPlanName());
        }

        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plano não encontrado.");
        }

    }

}
