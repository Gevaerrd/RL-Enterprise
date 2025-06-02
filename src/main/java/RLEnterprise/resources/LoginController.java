package RLEnterprise.resources;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import RLEnterprise.dto.UserLoginDTO;
import RLEnterprise.dto.UserProfileDTO;
import RLEnterprise.entities.User;
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

    // Mapa para tentativas e bloqueio por IP
    private final java.util.Map<String, Integer> loginAttempts = new java.util.HashMap<>();
    private final java.util.Map<String, Long> blockedUntil = new java.util.HashMap<>();

    @PostMapping("")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userDTO, HttpServletRequest request, Model model) {

        String clientIp = request.getRemoteAddr();

        // Proteção brute-force por IP
        if (blockedUntil.containsKey(clientIp) && blockedUntil.get(clientIp) > System.currentTimeMillis()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Collections.singletonMap("Error", "Muitas tentativas. Tente novamente em alguns minutos."));
        }

        // Invalida a sessão antiga antes de criar uma nova (importante para troca de
        // usuário)
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        HttpSession session = request.getSession(true);

        // String recaptchaResponse = userDTO.getRecaptcha();
        // if (!captchaService.isCaptchaValid(recaptchaResponse)) {
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body(Collections.singletonMap("Error", "Captcha inválido"));
        // }

        // Valida o login
        if (us.validLogin(userDTO)) {
            loginAttempts.put(clientIp, 0); // Zera tentativas ao logar
            UserProfileDTO userFromDb = us.findUserDTOByEmail(userDTO.getEmail());
            User userEntity = us.findByEmail(userDTO.getEmail());
            userFromDb.updateFirstName();
            session.setAttribute("user", userFromDb);
            model.addAttribute("user", userFromDb);
            session.setMaxInactiveInterval(1800);

            // >>> INÍCIO: Integração com Spring Security <<<
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userEntity,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole())));
            SecurityContextHolder.getContext().setAuthentication(auth);
            // ESSENCIAL: Salva o contexto na sessão para manter o login nas próximas
            // requisições
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            // >>> FIM: Integração com Spring Security <<<

            // Retorna pro JS o redirect com o usuario já setado pra sessão
            return ResponseEntity.ok().body(Collections.singletonMap("redirect", "/profile"));
        }

        // Incrementa tentativas
        int attempts = loginAttempts.getOrDefault(clientIp, 0) + 1;
        loginAttempts.put(clientIp, attempts);

        if (attempts >= 5) {
            blockedUntil.put(clientIp, System.currentTimeMillis() + 5 * 60 * 1000); // 5 minutos bloqueado
            loginAttempts.put(clientIp, 0);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Collections.singletonMap("Error", "Muitas tentativas. Tente novamente em alguns minutos."));
        }

        // Login inválido
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("Error", "Email ou senha incorretos"));
    }
}