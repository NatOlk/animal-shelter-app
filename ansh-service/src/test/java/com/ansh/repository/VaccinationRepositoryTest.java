package com.ansh.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(properties = "spring.config.location=classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VaccinationRepositoryTest {

  @Autowired
  private VaccinationRepository vaccinationRepository;

  @Autowired
  private AnimalRepository animalRepository;

  @Test
  @Transactional
  void shouldThrowException_whenDuplicateVaccinationInserted() {

    Animal animal = new Animal();
    animal.setName("Buddy");
    animal.setSpecies("Dog");
    animal.setBreed("Golden Retriever");
    animal.setGender('M');
    animal.setBirthDate(LocalDate.of(2021, 5, 10));
    animalRepository.saveAndFlush(animal);

    Vaccination vaccination1 = new Vaccination();
    vaccination1.setAnimal(animal);
    vaccination1.setVaccine("Rabies");
    vaccination1.setBatch("BT01");
    vaccination1.setVaccinationTime(LocalDate.of(2023, 1, 1));
    vaccinationRepository.saveAndFlush(vaccination1);

    Vaccination vaccination2 = new Vaccination();
    vaccination2.setAnimal(animal);
    vaccination2.setVaccine("Rabies");
    vaccination2.setBatch("BT01");
    vaccination2.setVaccinationTime(LocalDate.of(2023, 1, 1));

    assertThrows(DataIntegrityViolationException.class, () -> {
      vaccinationRepository.saveAndFlush(vaccination2);
    });
  }

  @Test
  @Transactional
  void shouldFindAnimalByVaccinationId() {
    Animal animal = new Animal();
    animal.setName("Buddy");
    animal.setSpecies("Dog");
    animal.setBreed("Golden Retriever");
    animal.setGender('M');
    animal.setBirthDate(LocalDate.of(2021, 5, 10));
    animal = animalRepository.saveAndFlush(animal);

    Vaccination vaccination = new Vaccination();
    vaccination.setAnimal(animal);
    vaccination.setVaccine("Rabies");
    vaccination.setVaccinationTime(LocalDate.of(2023, 1, 1));
    vaccination = vaccinationRepository.saveAndFlush(vaccination);

    Animal foundAnimal = animalRepository.findAnimalByVaccinationId(vaccination.getId());

    assertNotNull(foundAnimal);
    assertEquals(animal.getId(), foundAnimal.getId());
    assertEquals(animal.getName(), foundAnimal.getName());
  }
}
