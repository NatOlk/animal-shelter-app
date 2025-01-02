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

  @Query("select p from PendingSubscriber p where p.approver is null or p.approver = ''")
  List<PendingSubscriber> findByApproverIsNullOrEmpty();
}
