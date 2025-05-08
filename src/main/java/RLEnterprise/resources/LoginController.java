package RLEnterprise.resources;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import RLEnterprise.dto.UserDTO;
import RLEnterprise.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService us;

    @PostMapping("")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        // Valida o login (exemplo simples de validação)
        if (us.validLogin(userDTO)) {
            // Cria a sessão e armazena os dados do usuário
            HttpSession session = request.getSession();
            session.setAttribute("user", userDTO); // Salva o objeto UserDTO na sessão

            // Retorna o redirecionamento com a URL do dashboard do usuário
            String redirectUrl = "/user-dashboard"; // Página do usuário
            return ResponseEntity.ok().body(Collections.singletonMap("redirect", redirectUrl));
        }

        // Caso o login falhe
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("Error", "Email ou senha incorretos"));
    }
}