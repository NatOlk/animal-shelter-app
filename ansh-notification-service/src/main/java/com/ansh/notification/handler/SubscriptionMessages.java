package com.ansh.notification.handler;

public final class SubscriptionMessages {
  private SubscriptionMessages(){}

  public static final String ADD_ANIMAL_TEMPLATE = "addAnimalTemplate";
  public static final String REMOVE_ANIMAL_TEMPLATE = "removeAnimalTemplate";
  public static final String ADD_VACCINE_TEMPLATE = "addVaccineTemplate";
  public static final String REMOVE_VACCINE_TEMPLATE = "removeVaccineTemplate";
  public static final String ADD_ANIMAL_SUBJECT = "[animal-shelter-app] New animal added";
  public static final String REMOVE_ANIMAL_SUBJECT = "[animal-shelter-app] Animal removed";
  public static final String ADD_VACCINE_SUBJECT = "[animal-shelter-app] New vaccine added";
  public static final String REMOVE_VACCINE_SUBJECT = "[animal-shelter-app] Vaccine removed";
  public static final String SUBSCRIPTION_SUBJECT = "[animal-shelter-app] Please accept subscription for Animal Shelter app";
  public static final String SUCCESS_SUBSCRIPTION_SUBJECT = "[animal-shelter-app] Subscription for Animal Shelter app";
  public static final String ACCEPT_SUBSCRIPTION_TEMPLATE = "acceptSubscription";
  public static final String SUCCESS_SUBSCRIPTION_TEMPLATE = "successSubscription";
  public static final String REPEAT_SUBSCRIPTION_TEMPLATE = "repeatSubscription";
}
