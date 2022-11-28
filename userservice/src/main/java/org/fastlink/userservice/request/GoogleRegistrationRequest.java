package org.fastlink.userservice.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode @ToString
public class GoogleRegistrationRequest
{

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String profilePicture;

}
