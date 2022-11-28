package org.fastlink.userservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Getter
@NoArgsConstructor
public class RegistrationVerificationToken
{
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "token_id")
    private Long tokenId;

    @Column(name = "confirmation_token")
    private String token;

    @Temporal(TIMESTAMP)
    private Date createdDate = new Date();

    @OneToOne(targetEntity = FastlinkUser.class, fetch = EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private FastlinkUser user;

    public RegistrationVerificationToken(FastlinkUser user)
    {
        this.user = user;
        token = UUID.randomUUID().toString();
    }

}
