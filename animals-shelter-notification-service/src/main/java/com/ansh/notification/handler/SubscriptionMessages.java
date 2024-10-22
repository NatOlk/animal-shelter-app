package com.ansh.notification.handler;

public interface SubscriptionMessages {

  String ADD_ANIMAL_TEMPLATE = "addAnimalTemplate";
  String REMOVE_ANIMAL_TEMPLATE = "removeAnimalTemplate";
  String ADD_VACCINE_TEMPLATE = "addVaccineTemplate";
  String REMOVE_VACCINE_TEMPLATE = "removeVaccineTemplate";

  String ADD_ANIMAL_SUBJECT = "New Animal Added";
  String REMOVE_ANIMAL_SUBJECT = "Animal Removed";
  String ADD_VACCINE_SUBJECT = "New Vaccine Added";
  String REMOVE_VACCINE_SUBJECT = "Vaccine Removed";

  String SUBSCRIPTION_SUBJECT = "Please accept subscription for Animal Shelter app";
}
