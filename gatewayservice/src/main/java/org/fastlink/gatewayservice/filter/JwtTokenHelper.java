package org.fastlink.gatewayservice.filter;

import java.util.Base64;

public class JwtTokenHelper
{
    public final static String JWT_SECRET = "ZVRoV21acTR0N3cheiVDKkYtSmFOY1JmVWpYbjJyNXU=";

    public static String decodeJwtSecret(String secret)
    {
        return new String(Base64.getDecoder().decode(secret));
    }
}
