package com.ansh.event;

import com.ansh.entity.Animal;
import java.util.Date;
import java.util.Map;

public class RemoveAnimalEvent extends AnimalEvent {

  private String reason;

  public RemoveAnimalEvent(Animal animal) {
    super(animal);
  }

  public RemoveAnimalEvent(Animal animal, String reason) {
    super(animal);
    this.reason = reason;
  }

  @Override
  public Map<String, Object> getParams() {
    Map<String, Object> params = super.getParams();

    params.put("reason", reason);
    //TODO probably move to a property
    params.put("dateRemoved", new Date());

    return params;
  }
}
