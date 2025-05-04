package com.ansh.app.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ansh.AnshSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ConfigController.class)
@Import(AnshSecurityConfig.class)
class ConfigControllerWebMvcTest extends AbstractControllerWebMvcTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void testGetConfig() throws Exception {
    mockMvc.perform(get("/api/config")
            .header(AUTH_HEADER, BEARER_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.genders").isArray())
        .andExpect(jsonPath("$.genders[0]").value("F"))
        .andExpect(jsonPath("$.species").isArray())
        .andExpect(jsonPath("$.species[0]").value("Cat"))
        .andExpect(jsonPath("$.colors").isArray())
        .andExpect(jsonPath("$.colors[0]").value("White"))
        .andExpect(jsonPath("$.vaccines").isArray())
        .andExpect(jsonPath("$.vaccines[0]").value("Rabies"));
  }

  void shouldGetConfig_whenNoAuth() throws Exception {
    mockMvc.perform(get("/api/config"))
        .andExpect(status().isForbidden());
  }

  void shouldReturnForbidden_GetConfig_whenWrongAuth() throws Exception {
    mockMvc.perform(get("/api/config")
            .header(AUTH_HEADER, BEARER_TOKEN + "Fake"))
        .andExpect(status().isForbidden());
  }
}
