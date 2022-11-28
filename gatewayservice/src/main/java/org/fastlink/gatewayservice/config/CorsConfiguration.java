package org.fastlink.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfiguration extends org.springframework.web.cors.CorsConfiguration {
    @Bean
    public CorsWebFilter corsWebFilter() {


        final CorsConfiguration corsConfig = new CorsConfiguration();
        //corsConfig.setAllowedOrigin(Collections.singletonList("http://localhost:3000"));
        corsConfig.addAllowedOrigin("http://localhost:3000");
        corsConfig.setMaxAge(3600L);
        corsConfig.setAllowedMethods(Collections.singletonList("*"));
        corsConfig.addAllowedHeader("*");

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

}