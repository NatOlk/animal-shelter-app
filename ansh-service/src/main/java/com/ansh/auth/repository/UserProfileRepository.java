package com.ansh.auth.repository;

import com.ansh.entity.account.UserProfile;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

  @Query("select u from UserProfile u where u.email = :email or u.name = :identifier")
  Optional<UserProfile> findByEmailOrIdentifier(@Param("email") String email,
      @Param("identifier") String identifier);

  @Modifying
  @Query("update UserProfile u set u.animalNotifyStatus = :status where u.email = :identifier or u.name = :identifier")
  void updateAnimalNotificationSubscriptionStatus(@Param("identifier") String identifier,
      @Param("status") AnimalInfoNotifStatus status);

  @Query("select u from UserProfile u where not u.rolesRaw LIKE %:adminRole%")
  List<UserProfile> findAllNonAdminUsers(@Param("adminRole") String adminRole);

  @Query("select count(u) > 0 from UserProfile u where (u.email = :identifier or u.name = :identifier) and u.rolesRaw like %:adminRole%")
  boolean hasRole(@Param("identifier") String identifier, @Param("adminRole") String adminRole);
}
