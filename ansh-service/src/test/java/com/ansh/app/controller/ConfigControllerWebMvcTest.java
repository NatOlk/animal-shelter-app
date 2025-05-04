package com.ansh.app.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ansh.AnshSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

@WebMvcTest(ConfigController.class)
@Import(AnshSecurityConfig.class)
class ConfigControllerWebMvcTest extends AbstractControllerWebMvcTest {

  @Test
  void testGetConfig() throws Exception {
    System.out.println("Start testing get config...");
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

  @Test
  void shouldGetConfig_whenNoAuth() throws Exception {
    mockMvc.perform(get("/api/config"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldReturnForbidden_GetConfig_whenWrongAuth() throws Exception {
    mockMvc.perform(get("/api/config")
            .header(AUTH_HEADER, BEARER_TOKEN + "Fake"))
        .andExpect(status().isForbidden());
  }
}
