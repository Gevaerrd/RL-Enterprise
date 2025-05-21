/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import RLEnterprise.entities.User;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService us;

    public String coderGenerater() {
        int codeLength = 5;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        String code;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(characters.length());
            builder.append(characters.charAt(index));
        }
        code = builder.toString();

        return code;
    }

    public String send2FACode(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        String generatedCode = coderGenerater();
        User user = us.findByEmail(to);
        if (user != null) {
            user.setTwoFactorCode(generatedCode);
            user.setTwoFactorCodeGeneratedAt(System.currentTimeMillis());
            us.save(user); // Salva o código e o timestamp no banco!
        }
        message.setTo(to);
        message.setSubject("Código de verificação. RL Enterprise");
        message.setText(generatedCode);
        mailSender.send(message);
        return generatedCode;

    }

    public void sendPasswordReset(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        User user = us.findByEmail(to);
        if (user != null) {
            user.setTwoFactorCode(to);
            user.setTwoFactorCodeGeneratedAt(System.currentTimeMillis());
            us.save(user);
        }
        message.setTo(to);
        message.setSubject("Recuperação de senha. RL Enterprise");
        message.setText("Clique no link para redefinir sua senha: " + resetLink);
        mailSender.send(message);
    }

}
