package org.fastlink.gatewayservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
public class GatewaySecurityConfig
{

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http)
    {
        http.cors().and()
                .csrf().disable();

        return http.build();
    }

}
