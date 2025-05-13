package com.ansh.repository;

import com.ansh.repository.entity.PendingSubscriber;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingSubscriberRepository extends JpaRepository<PendingSubscriber, Long> {

  @Query("select p from PendingSubscriber p where p.email = :email and p.topic = :topic and p.approved is null")
  Optional<PendingSubscriber> findPendingByEmailAndTopic(@Param("email") String email,
      @Param("topic") String topic);

  @Query("select p from PendingSubscriber p where p.approver = :approver and p.topic = :topic and p.approved is null")
  List<PendingSubscriber> findPendingByApproverAndTopic(@Param("approver") String approver,
      @Param("topic") String topic);

  @Query("select p from PendingSubscriber p where p.topic = :topic and (p.approver is null or p.approver = '') and p.approved is null")
  List<PendingSubscriber> findPendingWithoutApproverAndByTopic(@Param("topic") String topic);

  @Modifying
  @Query("update PendingSubscriber p set p.approved = :approved where p.email = :email and p.topic = :topic")
  void updateApprovalStatus(@Param("email") String email, @Param("topic") String topic,
      @Param("approved") Boolean approved);
}
