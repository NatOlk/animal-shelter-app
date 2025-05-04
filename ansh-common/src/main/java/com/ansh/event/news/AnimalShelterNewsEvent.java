package com.ansh.event.news;

import com.ansh.event.AnimalShelterEvent;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AnimalShelterNewsEvent extends AnimalShelterEvent {

  private String message;

  @Override
  public Map<String, Object> getParams() {
    Map<String, Object> params = super.getParams();

    params.put("message", message);
    return params;
  }
}
