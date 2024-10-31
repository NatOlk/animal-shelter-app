package com.ansh.repository;

import com.ansh.entity.animal.Vaccination;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {

  @Query(name = "select * from Vaccination v where v.animal.id = :animalId")
  List<Vaccination> findByAnimalId(@NonNull Long animalId);

  @Query("select count(v) from Vaccination v where v.animal.id = :animalId")
  int findVaccinationCountByAnimalId(@NonNull Long animalId);

}
