package com.ansh.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ansh.entity.animal.Animal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(properties = "spring.config.location=classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnimalRepositoryTest {

  @Autowired
  private AnimalRepository animalRepository;

  @Test
  @Transactional
  void shouldThrowException_whenDuplicateAnimalInserted() {
    Animal animal1 = new Animal();
    animal1.setName("Fido");
    animal1.setSpecies("Dog");
    animal1.setBreed("Labrador");
    animal1.setGender('M');
    animal1.setBirthDate(LocalDate.of(2020, 1, 1));
    animalRepository.saveAndFlush(animal1);

    Animal animal2 = new Animal();
    animal2.setName("Fido");
    animal2.setSpecies("Dog");
    animal2.setBreed("Labrador");
    animal2.setGender('M');
    animal2.setBirthDate(LocalDate.of(2020, 1, 1));

    assertThrows(DataIntegrityViolationException.class, () -> {
      animalRepository.saveAndFlush(animal2);
    });
  }

  @Test
  @Transactional
  void shouldFindAllAnimalsOrderedByNameAsc() {
    Animal animal1 = new Animal();
    animal1.setName("Zebra");
    animal1.setSpecies("Wild");
    animal1.setBreed("Plain Zebra");
    animal1.setGender('F');
    animal1.setBirthDate(LocalDate.of(2015, 3, 10));
    animalRepository.saveAndFlush(animal1);

    Animal animal2 = new Animal();
    animal2.setName("Aardvark");
    animal2.setSpecies("Wild");
    animal2.setBreed("Antbear");
    animal2.setGender('M');
    animal2.setBirthDate(LocalDate.of(2012, 1, 1));
    animalRepository.saveAndFlush(animal2);

    List<Animal> animals = animalRepository.findAllByOrderByNameAsc();

    assertNotNull(animals);
    assertEquals(2, animals.size());
    assertEquals("Aardvark", animals.get(0).getName());
    assertEquals("Zebra", animals.get(1).getName());
  }
}
