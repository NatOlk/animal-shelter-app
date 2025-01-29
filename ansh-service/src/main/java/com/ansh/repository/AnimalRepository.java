package com.ansh.repository;

import com.ansh.entity.animal.Animal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

  @Query("select v.animal from Vaccination v where v.id = :vaccinationId")
  Animal findAnimalByVaccinationId(@Param("vaccinationId") @NonNull Long vaccinationId);
  List<Animal> findAllByOrderByNameAsc();
  @Modifying
  @Query("update Animal a set a.photoImgPath = :photoImgPath where a.id = :id")
  void updatePhotoPathById(@Param("id") Long id, @Param("photoImgPath") String photoImgPath);
}
