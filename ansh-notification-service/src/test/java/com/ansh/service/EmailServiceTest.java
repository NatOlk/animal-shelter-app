package com.ansh.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

class EmailServiceTest {

  @Mock
  private JavaMailSender emailSender;

  @Mock
  private ITemplateEngine templateEngine;

  @InjectMocks
  private EmailService emailService;

  @Mock
  private MimeMessage mimeMessage;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

    MimeMultipart multipart = new MimeMultipart();
    MimeBodyPart bodyPart = new MimeBodyPart();
    bodyPart.setContent("<html><body><h1>Hello, Test User!</h1></body></html>", "text/html");
    multipart.addBodyPart(bodyPart);
    when(mimeMessage.getContent()).thenReturn(multipart);
  }


  @Test
  void testSendSimpleMessage() throws Exception {
    String email = "test@example.com";
    String subject = "Test Subject";
    String template = "emailTemplate";
    Map<String, Object> templateModel = Map.of("name", "Test User");

    String htmlContent = "<html><body><h1>Hello, Test User!</h1></body></html>";
    when(templateEngine.process(eq(template), any(Context.class))).thenReturn(htmlContent);

    emailService.sendSimpleMessage(email, subject, template, templateModel);

    ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
    verify(emailSender, times(1)).send(messageCaptor.capture());

    MimeMessage sentMessage = messageCaptor.getValue();
    MimeMultipart multipart = (MimeMultipart) sentMessage.getContent();
    String actualContent;

    try (var reader = new BufferedReader(
        new InputStreamReader(multipart.getBodyPart(0).getInputStream()))) {
      actualContent = reader.lines().reduce("", (acc, line) -> acc + line);
    }

    assertEquals(htmlContent, actualContent);
  }


  @Test
  void testInvalidEmailAddress() {
    String invalidEmail = "Fake";
    String subject = "Test Subject";
    String template = "emailTemplate";
    Map<String, Object> templateModel = Map.of("name", "Test User");

    String htmlContent = "<html><body><h1>Hello, Test User!</h1></body></html>";
    when(templateEngine.process(eq(template), any(Context.class))).thenReturn(htmlContent);

    emailService.sendSimpleMessage(invalidEmail, subject, template, templateModel);

    verify(emailSender, never()).send(any(MimeMessage.class));
  }
}
