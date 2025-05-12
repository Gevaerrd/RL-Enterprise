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

        HttpSession existingSession = request.getSession(false);
        if (existingSession != null && existingSession.getAttribute("user") != null) {
            // Usuário já está logado
            return ResponseEntity.ok().body(Collections.singletonMap("redirect", "/user-profile"));
            // Retorna pra pagina de usuario
        }

        // Valida o login
        if (us.validLogin(userDTO)) {
            HttpSession session = request.getSession(); // Cria nova sessão
            session.setAttribute("user", userDTO); // Seta o userDTO pra sessão

            // Retorna pra pagina de usuário
            return ResponseEntity.ok().body(Collections.singletonMap("redirect", "/profile"));
        }

        // Login inválido
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("Error", "Email ou senha incorretos"));
    }
}
