package com.ansh.auth.repository;

import com.ansh.entity.animal.UserProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

  @Query("select u from UserProfile u where (u.email = :identifier or u.name = :identifier) and u.password = :password")
  Optional<UserProfile> findByIdentifierAndPassword(@Param("identifier") String identifier,
      @Param("password") String password);

  @Query("select u from UserProfile u where (u.email = :identifier or u.name = :identifier)")
  Optional<UserProfile> findByIdentifier(@Param("identifier") String identifier);


}
