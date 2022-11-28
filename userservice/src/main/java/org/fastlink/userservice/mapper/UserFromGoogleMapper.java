package org.fastlink.userservice.mapper;

import org.fastlink.userservice.model.FastlinkUser;

public class UserFromGoogleMapper {
    public static FastlinkUser mapRegistrationRequestToUser(String userDetails) {
        FastlinkUser fastlinkUser = new FastlinkUser();
        org.json.JSONObject jsonObject = new org.json.JSONObject(userDetails);
        fastlinkUser.setFirstName(jsonObject.getString("given_name"));
        fastlinkUser.setLastName(jsonObject.getString("family_name"));
        fastlinkUser.setEmail(jsonObject.getString("email"));
        fastlinkUser.setUsername(jsonObject.getString("given_name"));
        return fastlinkUser;

    }
}
