package org.fastlink.authservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.fastlink.authservice.dto.UserDto;
import org.fastlink.authservice.helper.UserServiceRequestHelper;
import org.fastlink.authservice.jwt.util.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.fastlink.authservice.helper.UserServiceRequestHelper.USER_REQUEST_URL_BY_USERNAME;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthentificationFilter extends UsernamePasswordAuthenticationFilter
{
    private final AuthenticationManager authenticationManager;

    private final RestTemplate restTemplate;

    public CustomAuthentificationFilter(AuthenticationManager authenticationManager, RestTemplate restTemplate)
    {
        this.authenticationManager = authenticationManager;
        this.restTemplate = restTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("Username is: {}", username);
        log.info("Password is: {}", password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        log.info("Authentication is: {}", authentication);
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException
    {
        User user = (User) authentication.getPrincipal();
        UserDto dto;
        try
        {
            dto = restTemplate.getForObject(USER_REQUEST_URL_BY_USERNAME, UserDto.class, user.getUsername());
        } catch (Exception e)
        {
            log.error("Unable to fetch user from user service");
            throw new RuntimeException("unable to fetch user");
        }
        assert dto != null;
        String accessToken = JwtTokenUtil.generateAccessToken(dto, user);
        String refreshToken = JwtTokenUtil.generateRefreshToken(dto, user);
        Map<String, String> tokens = new HashMap<>();

        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        tokens.put("user_id", String.valueOf(JwtTokenUtil.getUserIdFromJwt(accessToken)));
        tokens.put("username", user.getUsername());

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setHeader("authenticated", "true");
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException
    {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Username or password is incorrect");

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), errors);
    }
}


