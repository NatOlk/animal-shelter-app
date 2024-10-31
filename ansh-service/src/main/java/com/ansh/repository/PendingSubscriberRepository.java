package com.ansh.repository;

import com.ansh.repository.entity.PendingSubscriber;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingSubscriberRepository extends JpaRepository<PendingSubscriber, Long> {

  Optional<PendingSubscriber> findByEmail(String email);

  void deleteByEmail(String email);
}
