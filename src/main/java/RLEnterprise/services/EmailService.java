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
        Content emailContent = new Content("text/html", content);
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
                        <body style="margin: 0; padding: 0; background-color: #f4f4f4; font-family: 'Segoe UI', sans-serif;">
                            <div style="max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 10px; padding: 30px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); text-align: center;">
                            <h1 style="color: #eebb44; margin-bottom: 10px;">RL Enterprise</h1>
                            <p style="font-size: 18px; color: #333;">Olá!</p>
                            <p style="font-size: 16px; color: #555;">Aqui está seu código de verificação. Ele é necessário para continuar com segurança no nosso sistema.</p>
                            <div style="display: inline-block; margin: 20px 0; padding: 15px 30px; font-size: 28px; font-weight: bold; background-color: #eebb44; color: white; border-radius: 8px;">
                                %s
                            </div>
                            <p style="font-size: 14px; color: #888;">Esse código expira em alguns minutos. Se você não solicitou esse código, pode ignorar este e-mail.</p>
                            <p style="margin-top: 30px; font-size: 13px; color: #ccc;">© 2025 RL Enterprise - Todos os direitos reservados</p>
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
}
