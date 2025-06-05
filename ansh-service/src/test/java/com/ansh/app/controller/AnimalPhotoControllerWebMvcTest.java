package com.ansh.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ansh.config.AnshSecurityConfig;
import com.ansh.app.facade.AnimalFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest({AnimalPhotoController.class})
@Import({AnshSecurityConfig.class, GlobalExceptionHandler.class})
class AnimalPhotoControllerWebMvcTest extends AbstractControllerWebMvcTest {

  @MockBean
  private AnimalFacade animalFacade;

  @Mock
  private MultipartFile file;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
  void testUploadPhoto_whenFacadeThrowsException_thenHandledGlobally() throws Exception {
    Long animalId = 1L;

    when(animalFacade.updateAnimalPhoto(eq(animalId), any(MultipartFile.class)))
        .thenThrow(new RuntimeException("IO error"));

    mockMvc.perform(multipart("/api/{id}/upload-photo", animalId)
            .file("file", "dummy content".getBytes())
            .header(AUTH_HEADER, BEARER_TOKEN))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("Internal server error: IO error"));
  }

  @Test
  @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
  void testUploadPhoto() throws Exception {
    Long animalId = 1L;

    when(animalFacade.updateAnimalPhoto(eq(animalId), any(MultipartFile.class)))
        .thenReturn("1.jpeg");

    mockMvc.perform(multipart("/api/{id}/upload-photo", animalId)
            .file("file", "dummy content".getBytes())
            .header(AUTH_HEADER, BEARER_TOKEN))
        .andExpect(status().isOk())
        .andExpect(content().string("1.jpeg"));
  }
}
