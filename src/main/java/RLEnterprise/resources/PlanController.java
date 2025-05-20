/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

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
    private PlanService ps;

    @Autowired
    private UserService us;

    @PostMapping("/signplan/{id}")
    public ResponseEntity<?> planControl(@PathVariable Long id, Model model, HttpServletRequest request)
            throws MPApiException {
        try {

            HttpSession session = request.getSession(false); // Checando se tem usuario logado
            if (session == null || session.getAttribute("user") == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
            }

            Plan plan = ps.findById(id);
            if (plan == null) { // Pegando o plano e vendo se não foi alterado no front end
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plano não encontrado.");
            }

            UserProfileDTO userProfile = (UserProfileDTO) session.getAttribute("user"); // Pegando o usuarioDTO através
            // de quem tá logado
            User originalUser = us.findByEmail(userProfile.getEmail()); // Pegando o original pelo email

            String externalReference = plan.getId() + ":" + originalUser.getEmail();
            String referenceCode = (String) session.getAttribute("afCode");
            if (referenceCode != null) {
                externalReference += ":" + referenceCode;
            }

            // Configura Mercado Pago
            MercadoPagoConfig.setAccessToken("TEST-4636741219981499-051910-ac3b75d31d236ac8dfa10b1d52903529-544953103");

            // Cria item da preferência
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id(String.valueOf(plan.getId()))
                    .title(plan.getName())
                    .quantity(1)
                    .unitPrice(new BigDecimal(plan.getPrice()))
                    .currencyId("BRL")
                    .build();

            // URLs de redirecionamento após o pagamento
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("https://0ebd-2804-1530-64e-ee00-98f0-d59-66d4-7b6e.ngrok-free.app/sucesso")
                    .failure("https://0ebd-2804-1530-64e-ee00-98f0-d59-66d4-7b6e.ngrok-free.app/")
                    .pending("https://0ebd-2804-1530-64e-ee00-98f0-d59-66d4-7b6e.ngrok-free.app/")
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(List.of(item))
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .externalReference(externalReference)
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);
            System.out.println(preference.getInitPoint());

            // Retorna o link de pagamento
            return ResponseEntity.ok()
                    .body(Map.of(
                            "message", "Plano assinado com sucesso.",
                            "paymentLink", preference.getInitPoint()));
        }

        catch (MPException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar pagamento: " + e.getMessage());
        }

        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plano não encontrado.");
        }
    }
}