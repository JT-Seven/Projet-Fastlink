package org.fastlink.userservice.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_subscriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SID")
    private Long id;

    @OneToOne
    private FastlinkUser subscriber;

    @OneToOne
    private FastlinkUser subscribedTo;

    @CreatedDate
    @Column(name = "date_created", updatable = false, nullable = false)
    private Date createdAt = new Date();

}

