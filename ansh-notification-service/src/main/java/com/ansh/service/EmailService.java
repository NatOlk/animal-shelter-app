package com.ansh.service;

import java.util.Map;

/**
 * Service for sending emails.
 */
public interface EmailService {

  /**
   * Sends an email with a specified template and dynamic content.
   *
   * @param email the recipient's email address
   * @param subject the subject of the email
   * @param template the email template name or identifier
   * @param templateModel a map containing dynamic values to be inserted into the template
   */
  void sendEmail(String email, String subject, String template,
      Map<String, Object> templateModel);
}