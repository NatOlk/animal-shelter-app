package com.ansh.service;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendSimpleMessage(String email, String subject, String template,
                                  Map<String, Object> templateModel) {
        try {

            Context context = new Context();
            context.setVariables(templateModel);

            String htmlContent = templateEngine.process(template, context);

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            InternetAddress address = new InternetAddress(email);
            address.validate();

            emailSender.send(message);
        } catch (AddressException e) {
            LOG.error("Invalid email address: " + email, e);
        } catch (Exception e) {
            LOG.error("Failed to send email to " + email, e);
        }
    }
}