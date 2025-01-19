package com.ansh.utils;

import java.util.HashMap;
import java.util.Map;

public class EmailParamsBuilder {

  private final Map<String, Object> params = new HashMap<>();

  public EmailParamsBuilder name(String email) {
    params.put("name", email);
    return this;
  }

  public EmailParamsBuilder unsubscribeLink(String unsubscribeLink) {
    params.put("unsubscribeLink", unsubscribeLink);
    return this;
  }

  public EmailParamsBuilder confirmationLink(String confirmationLink) {
    params.put("confirmationLink", confirmationLink);
    return this;
  }

  public EmailParamsBuilder subscriptionLink(String subscriptionLink) {
    params.put("subscriptionLink", subscriptionLink);
    return this;
  }

  public Map<String, Object> build() {
    return params;
  }
}
