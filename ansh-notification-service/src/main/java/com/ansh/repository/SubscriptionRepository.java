package com.ansh.repository;

import com.ansh.entity.subscription.Subscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

  void deleteByTokenAndTopic(@NonNull String token, @NonNull String topic);

  void deleteByEmailAndTopic(@NonNull String email, @NonNull String topic);

  List<Subscription> findByApproverAndTopic(String approver, String topic);

  List<Subscription> findByTopicAndAcceptedTrueAndApprovedTrue(String topic);

  Optional<Subscription> findByEmailAndTopic(String email, String topic);

  Optional<Subscription> findByTokenAndTopic(String token, String topic);
}
