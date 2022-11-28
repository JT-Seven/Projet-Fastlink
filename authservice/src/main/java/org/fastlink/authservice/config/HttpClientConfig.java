package org.fastlink.authservice.config;

import okhttp3.OkHttpClient;
import org.fastlink.authservice.interceptor.HttpResponseInterceptor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig
{

    @Bean
    @LoadBalanced
    public OkHttpClient.Builder okHttpClientBuilder()
    {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpResponseInterceptor());
    }

}
