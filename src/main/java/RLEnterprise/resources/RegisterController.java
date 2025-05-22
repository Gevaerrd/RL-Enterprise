/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import RLEnterprise.dto.UserProfileDTO;
import RLEnterprise.dto.UserRegisterDTO;
import RLEnterprise.services.CaptchaService;
import RLEnterprise.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Pichau
 */
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService us;

    @Autowired
    private CaptchaService captchaService;

    @PostMapping("")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDTO userDTO, HttpServletRequest request) {

        // Filtro de senha
        // String senha = userDTO.getPassword();
        // String regex = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-={}:;\"',.<>/?]).{8,}$";
        // if (senha == null || !senha.matches(regex)) {
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body(Collections.singletonMap("Error",
        // "A senha deve ter no mínimo 8 caracteres, 1 letra maiúscula e 1 caractere
        // especial."));
        // }

        String captchaResponse = userDTO.getRecaptcha();

        if (us.emailExists(userDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("Error", "Email já cadastrado"));
        }

        if (!captchaService.isCaptchaValid(captchaResponse)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("Error", "Captcha inválido!"));
        }

        us.saveUser(userDTO);
        // Substituindo o register porque ele tem senha, o Profile oculta ela...
        UserProfileDTO userSessionDTO = (UserProfileDTO) us.findUserDTOByEmail(userDTO.getEmail());
        userSessionDTO.updateFirstName();

        // Cria a sessão e armazena os dados do usuário
        HttpSession session = request.getSession();
        userDTO.updateFirstName();
        session.setAttribute("user", userSessionDTO);

        // Retorna a URL de redirecionamento e mensagem de sucesso
        Map<String, String> response = new HashMap<>();
        response.put("redirect", "/profile");
        response.put("message", "Cadastro realizado com sucesso!");

        return ResponseEntity.ok(response);
    }
}