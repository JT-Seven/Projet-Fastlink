package org.fastlink.authservice.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class UserDto
{
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String username;
    private Long profilePicture;
    private String description;
    private Long bannerPicture;
    private List<RoleDto> roles = new ArrayList<>();
    private String provider;
    private boolean active;
}
