/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
import RLEnterprise.entities.AfilliateSelling;
import RLEnterprise.entities.Plan;
import RLEnterprise.entities.User;
import RLEnterprise.repositories.AfilliateSellingRepository;
import RLEnterprise.services.AfilliateCodeService;
import RLEnterprise.services.PlanService;
import RLEnterprise.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping()
public class RLEFrontPage {

    @Autowired
    UserService us;

    @Autowired
    PlanService ps;

    @Autowired
    AfilliateCodeService acs;

    @Autowired
    AfilliateSellingRepository asr;

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
            if (user.getPlan() != null) {
                model.addAttribute("userPlanId", user.getPlan().getId());
            }
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
            MercadoPagoConfig.setAccessToken("TEST-6319189669364487-060211-2f159b54e42c2ca497cd6d1459bc635b-544953103");
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

            // Recupera o usuário pelo e-mail e seta na sessão se necessário
            User originalUser = null;
            if (session == null || session.getAttribute("user") == null) {
                session = request.getSession(true);
                originalUser = us.findByEmail(email);
                if (originalUser != null) {
                    UserProfileDTO userDTO = new UserProfileDTO(originalUser);
                    session.setAttribute("user", userDTO);
                } else {
                    return "redirect:/";
                }
            } else {
                UserProfileDTO userDTO = (UserProfileDTO) session.getAttribute("user");
                originalUser = us.findByEmail(userDTO.getEmail());
            }

            // Associa o plano ao usuário e código de afiliado
            if (planoId != null && originalUser != null) {
                Plan plano = ps.findById(Long.parseLong(planoId));
                if (plano != null) {
                    plano.addUser(originalUser);

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

                            // Verifica no banco se já existe uma venda igual
                            boolean jaExiste = asr.existsBySellerAndBuyerNameAndPlan(
                                    afiliador, originalUser.getName(), plano);
                            if (!jaExiste) {
                                double valorVenda = plano.getPrice();
                                double comissao = plano.comissionCalculate(valorVenda);
                                comissao = Math.round(comissao * 100.0) / 100.0;

                                AfilliateSelling afilliateSelling = new AfilliateSelling();
                                afilliateSelling.setUser(afiliador);
                                afilliateSelling.setBuyerName(originalUser.getName());
                                afilliateSelling.setPlan(plano);
                                afilliateSelling.setComission(comissao);
                                afilliateSelling.setSelledAt(LocalDateTime.now());
                                asr.save(afilliateSelling);

                                afiliador.addBalance(comissao);
                                afiliador.addAfilliateSellings(afilliateSelling);
                                us.save(afiliador);
                            }
                        }
                    }

                    UserProfileDTO updatedDTO = new UserProfileDTO(originalUser);
                    session.setAttribute("user", updatedDTO);

                    // Atualiza o contexto do Spring Security para refletir o usuário atualizado
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            originalUser,
                            null,
                            Collections.emptyList() // ou use as authorities do seu usuário, se necessário
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

                    return "PurchaseCompleted";
                }
            }
            return "redirect:/";
        } catch (Exception e) {
            // Se der qualquer erro, redireciona para a home
            return "redirect:/";
        }
    }
}