package org.fastlink.userservice.mapper;


import org.fastlink.userservice.dto.UserSearchedDto;
import org.fastlink.userservice.model.FastlinkUser;
import org.fastlink.userservice.request.GoogleRegistrationRequest;
import org.fastlink.userservice.request.UserRegistrationRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper
{
    public static FastlinkUser mapRegistrationRequestToUser(UserRegistrationRequest request) {
        FastlinkUser fastlinkUser = new FastlinkUser();

        fastlinkUser.setFirstName(request.getFirstName());
        fastlinkUser.setLastName(request.getLastName());
        fastlinkUser.setPassword(request.getPassword());
        fastlinkUser.setEmail(request.getEmail());
        fastlinkUser.setUsername(request.getUsername());

        return fastlinkUser;
    }

    public static FastlinkUser mapGoogleRegistrationRequestToUser(GoogleRegistrationRequest request)
    {
        FastlinkUser fastlinkUser = new FastlinkUser();

        fastlinkUser.setFirstName(request.getFirstName());
        fastlinkUser.setLastName(request.getLastName());
        fastlinkUser.setEmail(request.getEmail());
        fastlinkUser.setUsername(request.getUsername());
        fastlinkUser.setProfilePictureUrl(request.getProfilePicture());

        return fastlinkUser;
    }

    public static List<UserSearchedDto> convertListEntityToDto(List<FastlinkUser> users) {
        return users.stream()
                .map(UserMapper::convertEntityToDto)
                .collect(Collectors.toList());
    }
    public static UserSearchedDto convertEntityToDto(FastlinkUser user) {
        return new UserSearchedDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getUsername(), 0, 0, user.getDescription());
    }
}
