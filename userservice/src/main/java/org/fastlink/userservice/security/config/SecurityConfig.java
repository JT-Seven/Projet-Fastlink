package org.fastlink.userservice.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration @EnableWebSecurity @AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    //private final JwtTokenUtil jwtTokenUtil;
    private final ObjectMapper objectMapper;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.csrf().disable()
//                .sessionManagement().sessionCreationPolicy(STATELESS)
//                .and()
//                .authorizeRequests()
//         //       .antMatchers(GET,  "/api/v*/authorization/token/refresh/**", "/api/v*/users/activate-account/**", "/api/v*/users/exist/**", "/api/v*/auth/**").permitAll()
////                .antMatchers(PUT,  "/api/v*/users/activate-account/**").permitAll()
//                .antMatchers(POST, "/api/v*/users/register/**", "/api/v*/auth/login/**", "/api/v*/authorization/token/refresh/**", "/api/v*/users/activate-account/**").permitAll()
//                .antMatchers(GET, "/api/v*/users/**").permitAll()
////                .antMatchers(GET, "/api/v*/users/**", "/api/v*/authorization/token/validate").hasAnyAuthority("ROLE_USER")
////                .antMatchers(PUT, "/api/v*/roles/to-user/**").hasAnyAuthority("ROLE_ADMIN")
//                .anyRequest().permitAll()
//                .and()
//                .exceptionHandling().accessDeniedHandler( authenticationEntryPoint() )
//                ;
        http.cors();
        http.csrf().disable().authorizeRequests().anyRequest().permitAll();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public AccessDeniedHandler authenticationEntryPoint()
    {
        return (httpServletRequest, httpServletResponse, e) -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Map<String, Object> errorObject = new HashMap<>();
            int errorCode = 403;

            errorObject.put("message", String.format("User: %s attempted to access the protected URL: %s", auth.getName(), httpServletRequest.getRequestURI()));
            errorObject.put("code", errorCode);
            errorObject.put("error", HttpStatus.UNAUTHORIZED);
            errorObject.put("timestamp", new Timestamp(new Date().getTime()));
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.setStatus(errorCode);
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorObject));
        };
    }

}