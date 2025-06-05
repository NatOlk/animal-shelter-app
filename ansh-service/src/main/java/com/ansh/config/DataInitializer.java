package com.ansh.config;

import com.ansh.app.service.notification.animal.AnimalInfoNotificationService;
import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import com.ansh.repository.AnimalRepository;
import com.ansh.repository.VaccinationRepository;
import com.ansh.utils.IdentifierMasker;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
@Profile("init-user")
public class DataInitializer implements CommandLineRunner {

  private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);

  @Value("${app.admin.email}")
  private String defaultEmail;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private AnimalRepository animalRepository;
  @Autowired
  private VaccinationRepository vaccinationRepository;
  @Autowired
  private AnimalInfoNotificationService notificationService;

  @Override
  public void run(String... args) throws Exception {
    initAccountsIfEmpty();
    initAnimalsIfEmpty();
  }

  private void initAccountsIfEmpty() throws IOException {
    Long accountCountRaw = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM user_profiles", Long.class);

    long accountCount = Optional.ofNullable(accountCountRaw).orElse(0L);
    LOG.debug("UserProfile entries in DB: {}", accountCount);

    if (accountCount > 0) {
      LOG.debug("Skipping account initialization — user_profile already has data.");
      return;
    }

    LOG.debug("No accounts found. Initializing default user...");
    executeSqlScript("init.sql");
  }

  private void initAnimalsIfEmpty() throws IOException {
    long animalCount = animalRepository.count();
    LOG.debug("Animal entries in DB: {}", animalCount);

    if (animalCount > 0) {
      LOG.debug("Skipping animal initialization — animals already exist.");
      return;
    }

    LOG.debug("No animals found. Initializing animals and vaccinations...");
    executeSqlScript("init_animals.sql");

    sendAnimalAndVaccinationEvents();
  }

  private void executeSqlScript(String resourcePath) throws IOException {
    ClassPathResource resource = new ClassPathResource(resourcePath);
    String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    sql = sql.replace(":defaultEmail", defaultEmail);
    jdbcTemplate.execute(sql);
    LOG.debug("Executed SQL script {} with default email {}", resourcePath, IdentifierMasker.maskEmail(defaultEmail));
  }

  private void sendAnimalAndVaccinationEvents() {
    LOG.info("Sending notification events for initialized data...");

    List<Animal> animals = animalRepository.findAllWithVaccinations();
    LOG.debug("Found {} animals", animals.size());

    for (Animal animal : animals) {
      notificationService.sendAddAnimalMessage(animal);
      LOG.debug("Sent animal event: {}", animal);
    }

    List<Vaccination> vaccinations = vaccinationRepository.findAllWithAnimal();
    LOG.debug("Found {} vaccinations", vaccinations.size());

    for (Vaccination vaccination : vaccinations) {
      if (vaccination.getAnimal() != null) {
        vaccination.getAnimal().setVaccinations(null);
      }
      notificationService.sendAddVaccinationMessage(vaccination);
      LOG.debug("Sent vaccination event: {}", vaccination);
    }

    LOG.debug("Event sending completed.");
  }
}