package org.fastlink.authservice.jwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
//import org.fastlink.authservice.config.JwtConfig;
import org.fastlink.authservice.dto.UserDto;

import org.fastlink.authservice.service.CustomUserDetailsEmailService;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.stream.Collectors;

import static org.fastlink.authservice.helper.UserServiceRequestHelper.USERLB_REQUEST_URL_BY_USERNAME;

@Slf4j
@Component
@Getter
@Setter
public class JwtTokenUtil
{

    //private static JwtConfig config = SpringUtils.getBean(JwtConfig.class);
    private static String jwtSecret = JwtTokenHelper.JWT_SECRET;
    @LoadBalanced
    private static final RestTemplate restTemplate = new RestTemplate();

    private static final CustomUserDetailsEmailService userDetailsService = new CustomUserDetailsEmailService(restTemplate);
    private static final String jwtIssuer = "org.fastlink";
    private static final JWTVerifier verifier =
            JWT.require(secretSignAlgorithm(JwtTokenHelper.decodeJwtSecret(jwtSecret)))
                    .withIssuer(jwtIssuer)
                    .withClaimPresence("roles")
                    .withClaimPresence("userId")
                    .withClaimPresence("provider")
                    .withClaimPresence("email")
                    .build();

    public static String generateAccessToken(UserDto user, User userDetails)
    {
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer(jwtIssuer)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000)) // 2 hours
                .withClaim(
                        "roles", userDetails.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                )
                .withClaim("userId", user.getId())
                .withClaim("provider", user.getProvider())
                .withClaim("email", user.getEmail())
                .sign(secretSignAlgorithm(JwtTokenHelper.decodeJwtSecret(jwtSecret)));
    }

    public static String generateRefreshToken(UserDto user, User userDetails)
    {
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer(jwtIssuer)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 days
                .withClaim(
                        "roles", userDetails.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                )
                .withClaim("userId", user.getId())
                .withClaim("provider", user.getProvider())
                .withClaim("email", user.getEmail())
                .sign(secretSignAlgorithm(JwtTokenHelper.decodeJwtSecret(jwtSecret)));
    }

    public static String generateSecAccessToken(User user)
    {

        UserDto userDto = getUserViaRestTemplate(user.getUsername());

        assert userDto != null;
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer(jwtIssuer)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000)) // 2 hours
                .withClaim(
                        "roles", user.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                )
                .withClaim("userId", userDto.getId())
                .withClaim("provider", userDto.getProvider())
                .withClaim("email", userDto.getEmail())
                .sign(secretSignAlgorithm(JwtTokenHelper.decodeJwtSecret(jwtSecret)));
    }

    public static String generateSecRefreshToken(User user)
    {

        UserDto userDto = getUserViaRestTemplate(user.getUsername());

        assert userDto != null;
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer(jwtIssuer)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 days
                .withClaim(
                        "roles", user.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                )
                .withClaim("userId", userDto.getId())
                .withClaim("provider", userDto.getProvider())
                .withClaim("email", userDto.getEmail())
                .sign(secretSignAlgorithm(JwtTokenHelper.decodeJwtSecret(jwtSecret)));
    }

    public static String getUsernameFromJwt(String token)
    {
         return verifier.verify(token).getSubject();
    }

    public static String getEmailFromJwt(String token)
    {
         return verifier.verify(token).getClaim("email").asString();
    }

    public static Integer getUserIdFromJwt(String token)
    {
        return verifier.verify(token).getClaim("userId").asInt();
    }

    public static Date getExpirationDateFromjwt(String token)
    {
        return verifier.verify(token).getExpiresAt();
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

    public static String getTokenFromHeader(String authHeader)
    {
        return authHeader.substring("Bearer ".length());
    }
    //PRIVATE
    private static Algorithm secretSignAlgorithm(String secret)
    {
        return Algorithm.HMAC256(secret.getBytes());
    }

    private static UserDto getUserViaRestTemplate(String username)
    {
        UserDto userDto;
        try
        {
            userDto = restTemplate.getForObject(USERLB_REQUEST_URL_BY_USERNAME, UserDto.class, username);
        } catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found ...");
        }


        return userDto;
    }

}
