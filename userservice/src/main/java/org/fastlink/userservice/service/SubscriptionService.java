package org.fastlink.userservice.service;

import lombok.AllArgsConstructor;
import org.fastlink.userservice.model.FastlinkUser;
import org.fastlink.userservice.model.Subscription;
import org.fastlink.userservice.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class SubscriptionService
{

    private final SubscriptionRepository subscriptionRepository;
    private final FastlinkUserService userService;
    private final NotificationService notificationService;

    public void addSubscription(Long subscriberId, Long friendId) {
        FastlinkUser subscriber = userService.findById(subscriberId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));

        FastlinkUser friend = userService.findById(friendId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));

        Subscription subscription = new Subscription();
        subscription.setSubscriber(subscriber);
        subscription.setSubscribedTo(friend);
        subscriptionRepository.save(subscription);

        notificationService.sendSubscriptionNotification(subscriber, friend.getUsername(), friend.getId());
    }

    public List<Subscription> getSubscriptions(Long userId) {
        return subscriptionRepository.findBySubscriberId(userId);
    }

    public List<Subscription> getSubscribers(Long userId) {
        return subscriptionRepository.findBySubscribedToId(userId);
    }
}
