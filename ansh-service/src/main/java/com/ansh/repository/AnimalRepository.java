package com.ansh.repository;

import com.ansh.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

  @Query("SELECT v.animal FROM Vaccination v WHERE v.id = :vaccinationId")
  Animal findAnimalByVaccinationId(@Param("vaccinationId") @NonNull Long vaccinationId);
}
