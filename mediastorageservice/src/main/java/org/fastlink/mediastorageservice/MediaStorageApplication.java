package org.fastlink.mediastorageservice;

import org.fastlink.mediastorageservice.config.MediaStorageConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties({ MediaStorageConfig.class })
@EnableEurekaClient
@EnableFeignClients
public class MediaStorageApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MediaStorageApplication.class, args);
    }
}
