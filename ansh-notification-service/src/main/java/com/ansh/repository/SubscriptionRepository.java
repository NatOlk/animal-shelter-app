package com.ansh.repository;

import com.ansh.entity.subscription.Subscription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

  @Query(name = "remove from Subscription s where s.token=:token and s.topic=:topic")
  void removeAllByTokenAndTopic(@NonNull String token, @NonNull String topic);

  @Query(name = "select * from Subscription s where s.topic=:topic")
  List<Subscription> getAllSubscriptionsByTopic(String topic);

  @Query(name = "select * from Subscription s where s.email=:email")
  List<Subscription> getSubscriptionByEmail(String email);

  @Query(name = "select * from Subscription s where s.token=:token")
  List<Subscription> getAllSubscriptionsByToken(String token);
}
