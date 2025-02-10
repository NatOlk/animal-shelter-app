package com.ansh.utils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdentifierMasker {

  private static final Pattern EMAIL_PATTERN = Pattern.compile(
      "^([a-zA-Z]{2,4})[a-zA-Z0-9._%+-]*@([a-zA-Z0-9.-]+)\\.([a-z]{2,})$"
  );

  private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^([a-zA-Z0-9]{2,4}).*");

  public static String maskIdentifier(String identifier) {
    return extractSafeIdentifier(identifier)
        .map(group -> group + "****")
        .orElse("Unknown");
  }

  public static String maskEmail(String email) {
    return extractEmailParts(email)
        .map(parts -> parts.localPart() + "***@" + "***." + parts.domain())
        .orElse("");
  }

  private static Optional<String> extractSafeIdentifier(String input) {
    Matcher matcher = IDENTIFIER_PATTERN.matcher(input);
    return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
  }

  private static Optional<EmailParts> extractEmailParts(String email) {
    Matcher matcher = EMAIL_PATTERN.matcher(email);
    if (matcher.find()) {
      return Optional.of(new EmailParts(matcher.group(1), matcher.group(3)));
    }
    return Optional.empty();
  }

  public record EmailParts(String localPart, String domain) {

  }
}

