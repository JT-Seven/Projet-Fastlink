package org.fastlink.authservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "jwt")
@Configuration
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtConfig
{
    private String secret;
}
