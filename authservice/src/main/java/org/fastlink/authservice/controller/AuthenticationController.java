package org.fastlink.authservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastlink.authservice.dto.UserDto;
import org.fastlink.authservice.google.util.GoogleAuthenticationUtil;
import org.fastlink.authservice.jwt.util.JwtTokenUtil;
import org.fastlink.authservice.service.CustomUserDetailsEmailService;
import org.fastlink.authservice.service.CustomUserDetailsUsernameService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthenticationController
{

    private final CustomUserDetailsUsernameService customUserDetailsUsernameService;
    private final CustomUserDetailsEmailService userDetailsService;
    private final GoogleAuthenticationUtil googleAuthenticationUtil;
    private final HttpServletRequest request;
    private final AuthenticationManager authenticationManager;


/*    @PostMapping(value = "/login", consumes = "application/x-www-form-urlencoded", produces = APPLICATION_JSON_VALUE)
    public void login
            (@RequestParam("username") String username,
             @RequestParam("password") String password,
             HttpServletResponse response
            ) throws AuthenticationException, IOException
    {

        Map<String, String> tokens = new HashMap<>();
        log.info("Authenticating user: {}", username);

        ///UserDetails userDetails = customUserDetailsUsernameService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);


        Authentication authentication = null;
        try
        {
            authentication = authenticationManager.authenticate(authRequest);
            log.info("Authentication is: {}", authentication.toString());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        }
        catch (AuthenticationException e)
        {
            log.error("Authentication failed for user: {}", username);
            log.error("Exception : \n{}", e.getMessage());
            tokens.put("error", "Authentication failed - Username or password is incorrect");
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        }

        assert authentication != null;
        User user = (User) customUserDetailsUsernameService.loadUserByUsername(username);

        String accessToken = JwtTokenUtil.generateSecAccessToken(user);
        String refreshToken = JwtTokenUtil.generateSecRefreshToken(user);
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }*/


    @GetMapping("/token/validate")
    public Boolean validateToken(/*@RequestParam("token") String token*/HttpServletResponse response) throws IOException
    {
        log.info("Enter validateToken - AuthenticationController");

        final String token = JwtTokenUtil.getTokenFromHeader(getHeaderFromRequest());

        if ( ! JwtTokenUtil.validateToken(token) )
        {
            invalidToken(response, String.format("Token: %s - Not valid !", token));
            return Boolean.FALSE;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(JwtTokenUtil.getEmailFromJwt(token));
        if ( userDetails == null )
        {
            invalidToken(response, String.format("Unable to fetch user details from token: %s", token));
            return Boolean.FALSE;
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return Boolean.TRUE;
    }

    @GetMapping("/token/username")
    public String getUsername()
    {
        return JwtTokenUtil
                .getUsernameFromJwt(JwtTokenUtil.getTokenFromHeader(getHeaderFromRequest()));
    }

    @GetMapping("/token/id")
    public ResponseEntity<Map<String, Integer>> getId()
    {
        Integer userId = JwtTokenUtil
                .getUserIdFromJwt(JwtTokenUtil.getTokenFromHeader(getHeaderFromRequest()));

        Map<String, Integer> map = new HashMap<>();
        map.put("userId", userId);

        return ResponseEntity.ok(map);
    }

/*    @GetMapping("/user-details/{username}")
    public UserDetails getUserDetails(@PathVariable("username") String username)
    {
        return userDetailsService.loadUserByUsername(username);
    }*/

    @GetMapping("/google")
    public void googleAuthentication(@RequestParam("code") String code, HttpServletResponse response) throws IOException
    {
        log.info("Enter googleAuthentication - AuthenticationController");
        log.info("Google authorization code - {}", code);

        UserDto AuthenticationPrincipal = googleAuthenticationUtil.getGoogleApiToken(code, response);

        if (googleAuthenticationUtil.getAccessToken() == null && googleAuthenticationUtil.getRefreshToken() == null)
        {
            log.error("Token is missing");
            invalidToken(response, "Token is missing");
        }

        if ( ! googleAuthenticationUtil.validateGoogleToken(googleAuthenticationUtil.getIdToken()) )
        {
            log.error("Invalid token - {}", googleAuthenticationUtil.getIdToken());
            invalidToken(response, String.format("Invalid token - %s", googleAuthenticationUtil.getIdToken()));
        }

        log.info("Token from google is valid - {}", googleAuthenticationUtil.getIdToken());
        log.info("Google Access token - {}", googleAuthenticationUtil.getAccessToken());
        log.info("Google Refresh token - {}", googleAuthenticationUtil.getRefreshToken());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("google_access_token", googleAuthenticationUtil.getAccessToken());
        tokens.put("google_refresh_token", googleAuthenticationUtil.getRefreshToken());
        tokens.put("google_id_token", googleAuthenticationUtil.getIdToken());

        User user = (User) userDetailsService.loadUserByUsername(AuthenticationPrincipal.getEmail());

        if ( user == null )
        {
            log.error("User not found - {}", AuthenticationPrincipal.getEmail());
            invalidToken(response, String.format("User not found - %s", AuthenticationPrincipal.getEmail()));
        }

        String FastlinkAccessToken = JwtTokenUtil.generateAccessToken(AuthenticationPrincipal, user);
        String FastlinkRefreshToken = JwtTokenUtil.generateRefreshToken(AuthenticationPrincipal, user);

        log.info("Fastlink Access token - {}", FastlinkAccessToken);
        log.info("Fastlink Refresh token - {}", FastlinkRefreshToken);

        tokens.put("fastlink_access_token", FastlinkAccessToken);
        tokens.put("fastlink_refresh_token", FastlinkRefreshToken);
        tokens.put("user_id", String.valueOf(JwtTokenUtil.getUserIdFromJwt(FastlinkAccessToken)));
        tokens.put("username", AuthenticationPrincipal.getUsername());

        Authentication authentication;
        try
        {
            log.info("Authenticating user - {}", AuthenticationPrincipal.getEmail());
            authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    "",
                                    user.getAuthorities()
                            )
                    );
            log.info("Authentication object - {}", authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e)
        {
            log.error("Unable to authenticate user - {}", e.getMessage());
            invalidToken(response, String.format("Unable to authenticate user - %s", e.getMessage()));
            return;
        }



        response.setContentType(APPLICATION_JSON_VALUE);
        response.setHeader("authenticated", "true");
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }


    private String getHeaderFromRequest()
    {
        final String header = request.getHeader(AUTHORIZATION.toLowerCase());

        if (header == null || ! header.startsWith("Bearer ") )
        {
            throw new RuntimeException("Authorization header is required ...");
        }

        return header;
    }

    private UserDetails getUserDetails(String username)
    {
        return userDetailsService.loadUserByUsername(username);
    }

    private void invalidToken(HttpServletResponse res, String message) throws IOException
    {
        res.setHeader("authenticated", "false");
        res.setStatus(FORBIDDEN.value());
        Map<String, String> errors = new HashMap<>();
        errors.put("error_message", message);
        res.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(res.getOutputStream(), errors);
    }
}
