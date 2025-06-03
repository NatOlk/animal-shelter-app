package com.ansh.app.controller;

import com.ansh.app.facade.AnimalFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@Tag(name = "Animal Photo", description = "Upload and manage animal photos")
@SecurityRequirement(name = "bearerAuth")
public class AnimalPhotoController {

  private static final Logger LOG = LoggerFactory.getLogger(AnimalPhotoController.class);

  @Autowired
  private AnimalFacade animalFacade;

  @Operation(
      summary = "Upload a photo for an animal",
      description = "Uploads a photo and updates the animal's profile with the photo URL.",
      security = @SecurityRequirement(name = "bearerAuth"),
      responses = {
          @ApiResponse(responseCode = "200", description = "Photo uploaded successfully",
              content = @Content(mediaType = "text/plain")),
          @ApiResponse(responseCode = "500", description = "Failed to upload photo",
              content = @Content(mediaType = "text/plain")),
          @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required")
      }
  )
  @PostMapping("/{id}/upload-photo")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> uploadPhoto(@PathVariable Long id,
      @RequestParam("file") MultipartFile file) {
    String url = animalFacade.updateAnimalPhoto(id, file);
    return ResponseEntity.ok(url);
  }
}
