package org.fastlink.userservice.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserSearchedDto implements Serializable {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private Integer numberOfSubscriber;
    private Integer numberOfSubscribed_to;
    private String description;;

    public void setNumberOfSubscribed_to(Integer numberOfSubscribed_to) {
        this.numberOfSubscribed_to = numberOfSubscribed_to;
    }
}
