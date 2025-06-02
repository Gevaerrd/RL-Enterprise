package RLEnterprise.resources;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import RLEnterprise.dto.UserProfileDTO;
import RLEnterprise.dto.UserRegisterDTO;
import RLEnterprise.entities.User;
import RLEnterprise.services.CaptchaService;
import RLEnterprise.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService us;

    @Autowired
    private CaptchaService captchaService;

    @PostMapping("")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDTO userDTO, HttpServletRequest request) {

        // Filtro de senha (opcional)
        // String senha = userDTO.getPassword();
        // String regex = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-={}:;\"',.<>/?]).{8,}$";
        // if (senha == null || !senha.matches(regex)) {
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body(Collections.singletonMap("Error", "A senha deve ter no mínimo 8
        // caracteres, 1 letra maiúscula e 1 caractere especial."));
        // }

        String captchaResponse = userDTO.getRecaptcha();

        if (us.emailExists(userDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("Error", "Email já cadastrado"));
        }

        // if (!captchaService.isCaptchaValid(captchaResponse)) {
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body(Collections.singletonMap("Error", "Captcha inválido!"));
        // }

        us.saveUser(userDTO);
        UserProfileDTO userSessionDTO = us.findUserDTOByEmail(userDTO.getEmail());
        userSessionDTO.updateFirstName();

        // Cria a sessão e armazena os dados do usuário
        HttpSession session = request.getSession();
        session.setAttribute("user", userSessionDTO);

        // >>> INÍCIO: Integração com Spring Security <<<
        User userEntity = us.findByEmail(userDTO.getEmail());
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userEntity,
                null,
                java.util.List.of(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole())));
        SecurityContextHolder.getContext().setAuthentication(auth);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        // >>> FIM: Integração com Spring Security <<<

        // Retorna a URL de redirecionamento e mensagem de sucesso
        Map<String, String> response = new HashMap<>();
        response.put("redirect", "/profile");
        response.put("message", "Cadastro realizado com sucesso!");

        return ResponseEntity.ok(response);
    }
}