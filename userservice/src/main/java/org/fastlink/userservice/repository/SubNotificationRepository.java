package org.fastlink.userservice.repository;

import org.fastlink.userservice.notif.model.SubNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubNotificationRepository extends JpaRepository<SubNotification, Long>
{
    List<SubNotification> findBySubscribedUserIdAndNotifStatus(Long subscribedUserId, String notifStatus);
}