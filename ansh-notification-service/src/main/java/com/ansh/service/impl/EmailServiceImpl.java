package com.ansh.service.impl;

import com.ansh.service.EmailService;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

  private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);
  @Autowired
  private JavaMailSender emailSender;

  @Autowired
  private ITemplateEngine templateEngine;

  @Override
  public void sendEmail(String email, String subject, String template,
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