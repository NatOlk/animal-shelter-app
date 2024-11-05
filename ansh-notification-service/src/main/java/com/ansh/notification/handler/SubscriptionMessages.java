package com.ansh.notification.handler;

public interface SubscriptionMessages {

  String ADD_ANIMAL_TEMPLATE = "addAnimalTemplate";
  String REMOVE_ANIMAL_TEMPLATE = "removeAnimalTemplate";
  String ADD_VACCINE_TEMPLATE = "addVaccineTemplate";
  String REMOVE_VACCINE_TEMPLATE = "removeVaccineTemplate";
  String ADD_ANIMAL_SUBJECT = "[animal-shelter-app] New animal added";
  String REMOVE_ANIMAL_SUBJECT = "[animal-shelter-app] Animal removed";
  String ADD_VACCINE_SUBJECT = "[animal-shelter-app] New vaccine added";
  String REMOVE_VACCINE_SUBJECT = "[animal-shelter-app] Vaccine removed";
  String SUBSCRIPTION_SUBJECT = "[animal-shelter-app] Please accept subscription for Animal Shelter app";
  String SUCCESS_SUBSCRIPTION_SUBJECT = "[animal-shelter-app] Subscription for Animal Shelter app";
  String ACCEPT_SUBSCRIPTION_TEMPLATE = "acceptSubscription";
  String SUCCESS_SUBSCRIPTION_TEMPLATE = "successSubscription";
  String REPEAT_SUBSCRIPTION_TEMPLATE = "repeatSubscription";
}
