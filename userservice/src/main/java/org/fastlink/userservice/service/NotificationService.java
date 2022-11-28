package org.fastlink.userservice.service;

import lombok.AllArgsConstructor;
import org.fastlink.userservice.model.FastlinkUser;
import org.fastlink.userservice.notif.model.SubNotifStatus;
import org.fastlink.userservice.notif.model.SubNotification;
import org.fastlink.userservice.repository.SubNotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static org.fastlink.userservice.notif.model.SubNotifStatus.UNREAD;

@AllArgsConstructor
@Service
public class NotificationService
{

    private final SubNotificationRepository notificationRepository;

    private final SimpMessagingTemplate messagingTemplate;

    public List<SubNotification> getAllUnreadNotifications(Long userId)
    {
        return notificationRepository.findBySubscribedUserIdAndNotifStatus(userId, "UNREAD");
    }

    public void sendSubscriptionNotification(FastlinkUser subscriber, String recipientUsername, Long recipientId)
    {
        SubNotification notification = SubNotification.builder()
                .subscriberId(subscriber.getId())
                .subscribedUserId(recipientId)
                .notifContent(String.format("User %s has subscribed to you", subscriber.getUsername()))
                .notifStatus(UNREAD.getStatus())
                .createdAt(new Date())
                .build();
        notificationRepository.save(notification);

        messagingTemplate.convertAndSendToUser(recipientUsername, "/topic/notif", notification);
    }

}
