package com.ansh.repository;

import com.ansh.entity.subscription.Subscription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

  void deleteByTokenAndTopic(@NonNull String token, @NonNull String topic);

  void deleteByEmailAndTopic(@NonNull String email, @NonNull String topic);

  List<Subscription> findByApproverAndTopic(String approver, String topic);

  List<Subscription> findByTopic(String topic);

  List<Subscription> findByTopicAndAcceptedTrueAndApprovedTrue(String topic);

  List<Subscription> findByEmailAndTopic(String email, String topic);

  List<Subscription> findByToken(String token);
}
