package com.ansh.auth.repository;

import com.ansh.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where (u.email = :identifier or u.name = :identifier) and u.password = :password")
    Optional<User> findByIdentifierAndPassword(@Param("identifier") String identifier, @Param("password") String password);

    @Query("select u from User u where (u.email = :identifier or u.name = :identifier)")
    Optional<User> findByIdentifier(@Param("identifier") String identifier);


}
