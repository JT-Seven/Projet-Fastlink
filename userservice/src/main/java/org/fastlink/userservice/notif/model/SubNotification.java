package org.fastlink.userservice.notif.model;


import lombok.*;
import org.fastlink.userservice.model.FastlinkUser;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;

import java.util.Date;

import static org.fastlink.userservice.notif.model.SubNotifStatus.UNREAD;

@Entity
@Table(name = "user_subscription_notifications")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubNotification
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "NID")
    private Long notificationId;

    @Column(name = "subscriber_id")
    private Long subscriberId;

    @Column(name = "subscribed_user_id")
    private Long subscribedUserId;

    @Column(name = "notif_content")
    private String notifContent;

    @Column(name = "notif_status")
    private String notifStatus = UNREAD.getStatus();

    @CreatedDate
    @Column(name = "date_created", updatable = false, nullable = false)
    private Date createdAt = new Date();
}
