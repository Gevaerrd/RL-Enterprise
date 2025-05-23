/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package RLEnterprise.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import RLEnterprise.entities.User;

@Service
public class EmailService {

    @Autowired
    private UserService us;

    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    public String coderGenerater() {
        int codeLength = 5;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(characters.length());
            builder.append(characters.charAt(index));
        }
        return builder.toString();
    }

    public void sendEmail(String to, String subject, String content) {
        Email fromEmail = new Email("rlenterprise.ia@gmail.com");
        Email toEmail = new Email(to);
        Content emailContent = new Content("text/plain", content);
        Mail mail = new Mail(fromEmail, subject, toEmail, emailContent);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public String send2FACode(String to) {
        String generatedCode = coderGenerater();
        String htmlContent = """
                    <html>
                    <body style="font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;">
                        <div style="max-width: 500px; margin: auto; background: #fff; padding: 20px; border-radius: 10px; text-align: center; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                            <h2 style="color: #333;">Seu código de verificação</h2>
                            <p style="font-size: 16px; color: #555;">Use o código abaixo para continuar:</p>
                            <div style="margin: 20px auto; padding: 15px 25px; background-color: #eebb44; color: white; font-size: 24px; font-weight: bold; border-radius: 5px; display: inline-block;">
                                %s
                            </div>
                            <p style="font-size: 12px; color: #999;">Esse código expira em alguns minutos.</p>
                        </div>
                    </body>
                    </html>
                """
                .formatted(generatedCode);
        User user = us.findByEmail(to);
        if (user != null) {
            user.setTwoFactorCode(generatedCode);
            user.setTwoFactorCodeGeneratedAt(System.currentTimeMillis());
            us.save(user);
            sendEmail(to, "Código para redefinir sua senha", htmlContent);
        }
        return generatedCode;
    }

    public void sendPasswordReset(String to, String resetLink) {
        User user = us.findByEmail(to);
        if (user != null) {
            user.setTwoFactorCode(to);
            user.setTwoFactorCodeGeneratedAt(System.currentTimeMillis());
            us.save(user);
        }
        String content = "Clique no link para redefinir sua senha: " + resetLink;
        sendEmail(to, "Recuperação de senha. RL Enterprise", content);
    }
}
