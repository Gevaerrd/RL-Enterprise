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
import RLEnterprise.entities.AfilliateCode;
import RLEnterprise.entities.Plan;
import RLEnterprise.entities.User;
import RLEnterprise.services.AfilliateCodeService;
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

    @Autowired
    AfilliateCodeService acs;

    @GetMapping()
    public String home(@RequestParam(value = "afCode", required = false) String afCode,
            HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(true);

        if (afCode != null && !afCode.isEmpty()) {
            session.setAttribute("afCode", afCode);
        }

        if (session != null && session.getAttribute("user") != null) {

            UserProfileDTO user = (UserProfileDTO) session.getAttribute("user"); // Pegando o DTO que esta logado

            model.addAttribute("user", user); // Passa para a view
            return "FrontPageWithLogin";
        }

        return "FrontPageWithoutLogin";
    }

    @GetMapping("/sucesso") // NGROK
    public String redirectController(
            @RequestParam(value = "payment_id", required = false) String paymentId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "preference_id", required = false) String preferenceId,
            Model model, HttpServletRequest request) {

        String planoId = null;
        String email = null;
        String referenceCode = null;

        try {
            // Inicializa o SDK do Mercado Pago
            MercadoPagoConfig.setAccessToken("TEST-4636741219981499-051910-ac3b75d31d236ac8dfa10b1d52903529-544953103");
            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(Long.parseLong(paymentId));
            String externalReference = payment.getExternalReference();

            // Pegando a referencia do plan controller e trabalhando aqui, setando o id do
            // plano e o email do DTO
            if (externalReference != null && externalReference.contains(":")) {
                String[] parts = externalReference.split(":");
                planoId = parts[0];
                email = parts[1];
                if (parts.length > 2) {
                    referenceCode = parts[2]; // afCode do afiliado, se existir
                }
            }

            HttpSession session = request.getSession(false);

            // Caso tenha dado bug na sessão eu recupero pela externalReference
            if (session == null || session.getAttribute("user") == null) {
                // Cria nova sessão se não existir
                session = request.getSession(true);

                // Recupera o usuário pelo e-mail e seta na sessão
                User originalUser = us.findByEmail(email);
                if (originalUser != null) {
                    UserProfileDTO userDTO = new UserProfileDTO(originalUser);
                    session.setAttribute("user", userDTO);

                    // Associa o plano ao usuário e cód afiliado
                    if (planoId != null) {
                        Plan plano = ps.findById(Long.parseLong(planoId));
                        plano.addUser(originalUser);

                        // Cria o código de afiliado para o novo usuário
                        AfilliateCode afCode = new AfilliateCode();
                        afCode.setCode(acs.generateCode());
                        afCode.setUser(originalUser);
                        acs.save(afCode);

                        originalUser.setAfilliateCode(afCode);
                        us.save(originalUser);

                        // Bonifica o afiliador, se houver referenceCode
                        if (referenceCode != null) {
                            AfilliateCode code = acs.findByCode(referenceCode);
                            if (code != null && code.getUser() != null) {
                                User afiliador = code.getUser();
                                double valorVenda = plano.getPrice();
                                double comissao = plano.comissionCalculate(valorVenda);
                                afiliador.addBalance(comissao);
                                us.save(afiliador);
                            }
                        }

                        UserProfileDTO updatedDTO = new UserProfileDTO(originalUser);
                        session.setAttribute("user", updatedDTO);
                    }
                    return "PurchaseCompleted";
                } else {
                    return "redirect:/";
                }
            }

            // Se o usuário já está logado, associa o plano normalmente
            if (email != null && planoId != null) {
                User originalUser = us.findByEmail(email);
                Plan plano = ps.findById(Long.parseLong(planoId));

                // Cria o código de afiliado para o novo usuário
                AfilliateCode afCode = new AfilliateCode();
                afCode.setCode(acs.generateCode());
                afCode.setUser(originalUser);
                acs.save(afCode);

                originalUser.setAfilliateCode(afCode);
                originalUser.setPlan(plano);
                us.save(originalUser);

                // Bonifica o afiliador, se houver referenceCode
                if (referenceCode != null) {
                    AfilliateCode code = acs.findByCode(referenceCode);
                    if (code != null && code.getUser() != null) {
                        User afiliador = code.getUser();
                        double valorVenda = plano.getPrice();
                        double comissao = plano.comissionCalculate(valorVenda);
                        afiliador.addBalance(comissao);
                        us.save(afiliador);
                    }
                }

                UserProfileDTO updatedDTO = new UserProfileDTO(originalUser);
                session.setAttribute("user", updatedDTO);
                return "PurchaseCompleted";
            } else {
                return "redirect:/";
            }
        } catch (Exception e) {
            // Se der qualquer erro, redireciona para a home
            return "redirect:/";
        }
    }
}