package com.ansh.event;

import com.ansh.entity.animal.Animal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoveAnimalEvent extends AnimalEvent {

  private static final Logger LOG = LoggerFactory.getLogger(RemoveAnimalEvent.class);

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
