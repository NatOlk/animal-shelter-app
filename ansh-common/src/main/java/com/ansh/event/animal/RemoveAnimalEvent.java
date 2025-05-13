package com.ansh.event.animal;

import com.ansh.entity.animal.Animal;
import com.ansh.event.AnimalShelterEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RemoveAnimalEvent extends AnimalShelterEvent {

  @JsonProperty("reason")
  private String reason;

  public RemoveAnimalEvent(Animal animal) {
    super(animal);
  }

  public RemoveAnimalEvent(Animal animal, String reason) {

    super(animal);
    this.reason = reason;
  }

  public RemoveAnimalEvent() {
    super();
  }

  @Override
  public Map<String, Object> getParams() {
    Map<String, Object> params = super.getParams();

    params.put("reason", reason);
    //TODO probably move to a property
    params.put("dateRemoved", LocalDate.now());

    return params;
  }
}
