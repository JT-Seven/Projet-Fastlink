package org.fastlink.authservice.helper;

public class UserServiceRequestHelper
{
    public static final String USER_REQUEST_URL_BY_EMAIL = "lb://FASTLINK-USER/api/v1/users/email/{email}";
    public static final String USERLB_REQUEST_URL_BY_USERNAME = "lb://FASTLINK-USER/api/v1/users/{username}";

    public static final String USER_REQUEST_URL_BY_USERNAME = "http://localhost:8083/api/v1/users/{username}";
    public static final String GOOGLE_REGISTRATION_URL = "lb://FASTLINK-USER/api/v1/users/register/google";
}