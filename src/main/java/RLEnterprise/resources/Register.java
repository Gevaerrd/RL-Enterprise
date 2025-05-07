/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

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

/**
 *
 * @author Pichau
 */
@RestController // Usamos rest controller porque queremos retornar dados diretamente e não um
                // HTML
@RequestMapping("/register")
public class Register {

    @Autowired
    private UserService us;

    @PostMapping("")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {

        if (us.emailExists(userDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("Error", "Email já cadastrado"));
        }

        us.saveUser(userDTO); // O singletonMap retorna um MAP rapidamente, a key é a chave e o value é a
                              // mensagem
        return ResponseEntity.ok(Collections.singletonMap("message", "Usuário registrado"));

    }

}
