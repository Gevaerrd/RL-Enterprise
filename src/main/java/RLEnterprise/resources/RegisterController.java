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

import RLEnterprise.dto.UserDTO;
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

    @PostMapping("")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {

        System.out.println(userDTO.toString());

        if (us.emailExists(userDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("Error", "Email já cadastrado"));
        }

        us.saveUser(userDTO);

        // Cria a sessão e armazena os dados do usuário
        HttpSession session = request.getSession();
        session.setAttribute("user", userDTO);

        // Retorna a URL de redirecionamento e mensagem de sucesso
        Map<String, String> response = new HashMap<>();
        response.put("redirect", "/profile");
        response.put("message", "Cadastro realizado com sucesso!");

        return ResponseEntity.ok(response);
    }
}