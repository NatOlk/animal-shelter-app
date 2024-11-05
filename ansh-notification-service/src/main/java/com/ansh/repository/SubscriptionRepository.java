package com.ansh.repository;

import com.ansh.entity.subscription.Subscription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

  void deleteByTokenAndTopic(@NonNull String token, @NonNull String topic);

  List<Subscription> findByTopicAndApprover(String topic, String approver);

  List<Subscription> findByTopicAndAcceptedTrueAndApprovedTrue(String topic);

  List<Subscription> findByEmailAndTopic(String email, String topic);

  List<Subscription> findByToken(String token);
}
