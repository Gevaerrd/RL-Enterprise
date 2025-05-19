/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;

import RLEnterprise.dto.UserProfileDTO;
import RLEnterprise.entities.Plan;
import RLEnterprise.entities.User;
import RLEnterprise.services.PlanService;
import RLEnterprise.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Pichau
 */
@Controller
@RequestMapping()
public class RLEFrontPage {

    @Autowired
    UserService us;

    @Autowired
    PlanService ps;

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

    @GetMapping("/sucesso")
    public String redirectController(
            @RequestParam(value = "payment_id", required = false) String paymentId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "preference_id", required = false) String preferenceId,
            Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            return "redirect:/";
        }

        if (paymentId == null || status == null || preferenceId == null) {
            return "redirect:/";
        }

        try {

            // Inicializa o SDK
            MercadoPagoConfig.setAccessToken("TEST-4636741219981499-051910-ac3b75d31d236ac8dfa10b1d52903529-544953103");

            // Cria o client de pagamentos
            PaymentClient paymentClient = new PaymentClient();

            // Recupera o pagamento pelo ID
            Payment payment = paymentClient.get(Long.parseLong(paymentId));

            if ("approved".equalsIgnoreCase(payment.getStatus())) { // Se for aprovado
                String planoId = payment.getExternalReference(); // Pega a referencia externa passada no PlanController

                if (planoId == null) // Se for Null já volta pra principal
                    return "redirect:/";

                UserProfileDTO userProfile = (UserProfileDTO) session.getAttribute("user"); // Pega o DTO pelo user
                User originalUser = us.findByEmail(userProfile.getEmail()); // Pega o original
                Plan plano = ps.findById(Long.parseLong(planoId)); // Procura o plano

                originalUser.setPlan(plano); // Seta ele
                us.save(originalUser); // Salva o usuário

                return "sucesso"; // Sucesso só enrola, depois envia para a pagina profile
            }

            else {
                return "redirect:/";
            }

        }

        catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

}
