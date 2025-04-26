package com.ansh.repository;

import com.ansh.entity.subscription.Subscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

  void deleteByTokenAndTopic(@NonNull String token, @NonNull String topic);

  void deleteByEmailAndTopic(@NonNull String email, @NonNull String topic);

  @Query("select s from Subscription s where s.accepted = true and s.approved = true")
  List<Subscription> findApprovedAndAcceptedSubscriptions();

  List<Subscription> findByApproverAndTopic(String approver, String topic);

  Optional<Subscription> findByEmailAndTopic(String email, String topic);

  Optional<Subscription> findByTokenAndTopic(String token, String topic);
}
