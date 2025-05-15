/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import RLEnterprise.dto.UserProfileDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Pichau
 */
@Controller
@RequestMapping()
public class RLEFrontPage {

    @GetMapping()
    public String home(HttpServletRequest request, Model model) {

        // Redirecionar pra outra página igual porém sem chance de entrar dnv

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {

            UserProfileDTO user = (UserProfileDTO) session.getAttribute("user"); // Pegando o DTO que esta logado

            model.addAttribute("user", user); // Passa para a view
            return "FrontPageWithLogin";
        }

        return "FrontPageWithoutLogin";
    }
}
