package org.fastlink.authservice.config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google.client")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleClientConfig
{
    private String id;
    private String secret;
}
