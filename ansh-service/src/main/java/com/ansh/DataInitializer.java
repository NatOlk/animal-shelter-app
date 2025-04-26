package com.ansh;

import com.ansh.app.service.notification.animal.AnimalInfoNotificationService;
import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import com.ansh.repository.AnimalRepository;
import com.ansh.repository.VaccinationRepository;
import com.ansh.utils.IdentifierMasker;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
    ClassPathResource resource = new ClassPathResource("init.sql");
    String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

    sql = sql.replace(":defaultEmail", defaultEmail);

    jdbcTemplate.execute(sql);
    LOG.debug("init default user SQL script executed with default email: {}", IdentifierMasker.maskEmail(defaultEmail));

    ClassPathResource resourceAnimals = new ClassPathResource("init_animals.sql");
    String sqlAnimals = StreamUtils.copyToString(resourceAnimals.getInputStream(), StandardCharsets.UTF_8);

    sqlAnimals = sqlAnimals.replace(":defaultEmail", defaultEmail);

    jdbcTemplate.execute(sqlAnimals);

    LOG.debug("init animals SQL script executed with default email: {}", IdentifierMasker.maskEmail(defaultEmail));
    f();
  }

  private void f() {
    LOG.debug("Starting event sending...");

    List<Animal> animals = animalRepository.findAllWithVaccinations();

    LOG.debug("Find {} animals...", animals.size());
    for (Animal animal : animals) {
      notificationService.sendAddAnimalMessage(animal);
      LOG.debug("Send notification for {}", animal);
    }

    List<Vaccination> vaccinations = vaccinationRepository.findAllWithAnimal();
    LOG.debug("Find {} vaccinations...", vaccinations.size());
    for (Vaccination vaccination : vaccinations) {
      if (vaccination.getAnimal() != null) {
        vaccination.getAnimal().setVaccinations(null);
      }
      notificationService.sendAddVaccinationMessage(vaccination);
      LOG.debug("Send notification for {}", vaccination);
    }
  }
}
