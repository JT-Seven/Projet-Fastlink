package org.fastlink.userservice.repository;

import org.fastlink.userservice.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findBySubscriberId(Long subscriberId);

    List<Subscription> findBySubscribedToId(Long subscribedToId);

    Integer countBySubscribedTo_Id(Long subscribedToId);

    Integer countBySubscriber_Id(Long subscriberId);
}