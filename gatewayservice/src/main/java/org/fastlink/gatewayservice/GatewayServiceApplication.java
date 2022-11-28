package org.fastlink.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableEurekaClient
//@EnableWebSecurity
//@EnableHystrix
public class GatewayServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }
}
