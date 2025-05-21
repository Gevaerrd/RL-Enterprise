/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.resources;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import RLEnterprise.services.EmailService;

@RestController
@RequestMapping("/api/send-email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("")
    public Map<String, String> sendEmail(@RequestBody Map<String, String> payload) {
        String to = payload.get("to");
        emailService.send2FACode(to);
        return Map.of("message", "E-mail enviado para " + to);
    }
}
