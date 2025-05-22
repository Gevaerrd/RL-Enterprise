package RLEnterprise.resources;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import RLEnterprise.dto.UserLoginDTO;
import RLEnterprise.dto.UserProfileDTO;
import RLEnterprise.services.CaptchaService;
import RLEnterprise.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService us;

    @Autowired
    private CaptchaService captchaService;

    @PostMapping("")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userDTO, HttpServletRequest request, Model model) {

        HttpSession existingSession = request.getSession(false);
        if (existingSession != null && existingSession.getAttribute("user") != null) {
            // Usuário já está logado
            return ResponseEntity.ok().body(Collections.singletonMap("redirect", "/profile"));
            // Retorna pra pagina de usuario
        }

        String recaptchaResponse = userDTO.getRecaptcha();
        if (!captchaService.isCaptchaValid(recaptchaResponse)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("Error", "Captcha inválido"));
        }

        // Valida o login
        if (us.validLogin(userDTO)) {
            HttpSession session = request.getSession(); // Cria uma sessão
            UserProfileDTO userFromDb = us.findUserDTOByEmail(userDTO.getEmail()); // Pega um DTO do Service
            userFromDb.updateFirstName();
            session.setAttribute("user", userFromDb); // Coloca a sessão pro usuario
            model.addAttribute("user", userFromDb); // Passa o usuario pro front end
            session.setMaxInactiveInterval(1800); // Tempo máximo de inatividade

            // Retorna pro JS o redirect com o usuario já setado pra sessão
            return ResponseEntity.ok().body(Collections.singletonMap("redirect", "/profile"));
        }

        // Login inválido
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("Error", "Email ou senha incorretos"));
    }
}
