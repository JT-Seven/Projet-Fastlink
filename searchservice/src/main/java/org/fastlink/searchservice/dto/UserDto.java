package org.fastlink.searchservice.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class UserDto
{
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private Integer numberOfSubscriber;
    private Integer numberOfSubscribed_to;
    private String description;

}
