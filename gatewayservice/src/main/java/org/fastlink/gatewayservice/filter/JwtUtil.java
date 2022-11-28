package org.fastlink.gatewayservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtil
{
    private static final String jwtIssuer = "org.fastlink";
    private static final String jwtSecret = JwtTokenHelper.JWT_SECRET;

    private static final JWTVerifier verifier =
            JWT.require(secretSignAlgorithm(JwtTokenHelper.decodeJwtSecret(jwtSecret)))
                    .withIssuer(jwtIssuer)
                    .withClaimPresence("roles")
                    .withClaimPresence("userId")
                    .withClaimPresence("provider")
                    .withClaimPresence("email")
                    .build();

    private static Algorithm secretSignAlgorithm(String secret)
    {
        return Algorithm.HMAC256(secret.getBytes());
    }

    public static boolean validateToken(String token)
    {
        try {
            verifier.verify(token);
            return true;
        } catch (SignatureVerificationException e) {
            log.error("Invalid JWT signature - {}", e.getMessage());
        } catch (JWTDecodeException e) {
            log.error("Invalid JWT token - {}", e.getMessage());
        } catch (TokenExpiredException e) {
            log.error("Expired JWT token - {}", e.getMessage());
        } catch (InvalidClaimException e) {
            log.error("JWT claims string is empty - {}", e.getMessage());
        }
        return false;
    }
}
