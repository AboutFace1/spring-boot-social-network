package com.JimsonBobson.SocialNetwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class EmailService {

    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.enable}")
    private Boolean enable;

    private void send(MimeMessagePreparator preparator) {
        if(enable) {
            mailSender.send(preparator);
        }
    }

    @Autowired
    public EmailService(TemplateEngine templateEngine) {

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setPrefix("mail/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCacheable(false);
        templateEngine.setTemplateResolver(templateResolver);

        this.templateEngine = templateEngine;
    }

    public void sendVerificationEmail(String emailAddress) {

        Context context = new Context();
        context.setVariable("name", "Bob");

        String emailContent = templateEngine.process("verifyemail", context);

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {

                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

                message.setTo(emailAddress);
                message.setFrom(new InternetAddress("no-reply@caveofprogramming.com"));
                message.setSubject("Please verify your email address");
                message.setSentDate(new Date());

                message.setText(emailContent);
            }
        };

        send(preparator);
    }
}
