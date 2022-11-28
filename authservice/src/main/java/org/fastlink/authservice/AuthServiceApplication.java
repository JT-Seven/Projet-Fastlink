package org.fastlink.authservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class AuthServiceApplication
{
    public static void main(String[] args)
    {

        ConfigurableApplicationContext context =
                SpringApplication.run(AuthServiceApplication.class, args);


    }
}
