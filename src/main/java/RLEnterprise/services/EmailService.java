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

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

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
        message.setTo(to);
        message.setSubject("Código de verificação. RL Enterprise");
        message.setText("Seu código de verificação é: " + generatedCode);
        mailSender.send(message);
        return generatedCode;

    }

    public void sendPasswordReset(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Recuperação de senha. RL Enterprise");
        message.setText("Clique no link para redefinir sua senha: " + resetLink);
        mailSender.send(message);
    }

}
