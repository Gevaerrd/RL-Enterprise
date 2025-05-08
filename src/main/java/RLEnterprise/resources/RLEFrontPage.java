/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String home(HttpServletRequest request) {

        // Redirecionar pra outra página igual porém sem chance de entrar dnv

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            return "FrontPageWithLogin";
        }

        return "FrontPageWithoutLogin";
    }
}
