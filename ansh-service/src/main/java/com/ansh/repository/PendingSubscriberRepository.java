package com.ansh.repository;

import com.ansh.repository.entity.PendingSubscriber;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingSubscriberRepository extends JpaRepository<PendingSubscriber, Long> {

  Optional<PendingSubscriber> findByEmailAndTopic(String email, String topic);

  void deleteByEmailAndTopic(String email, String topic);

  List<PendingSubscriber> findByApprover(String approver);

  @Query("SELECT p FROM PendingSubscriber p WHERE p.approver IS NULL OR p.approver = ''")
  List<PendingSubscriber> findByApproverIsNullOrEmpty();
}
