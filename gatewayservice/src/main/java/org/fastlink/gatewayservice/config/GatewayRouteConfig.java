package org.fastlink.gatewayservice.config;

import lombok.AllArgsConstructor;
import org.fastlink.gatewayservice.filter.CustomAuthorizationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration @AllArgsConstructor
public class GatewayRouteConfig
{

    private final CustomAuthorizationFilter authorizationFilter;

    @Bean
    public RouteLocator gatewayRouteLocator(RouteLocatorBuilder builder)
    {
        return builder.routes()
                .route("media-storage-service", r -> r.path(  "/api/v*/file/**").filters(f -> f.filter(authorizationFilter)).uri("lb://FASTLINK-MEDIASTORAGE"))
                .route("media-service", r -> r.path("/api/v*/medias/**", "/api/v*/posts/**").filters(f -> f.filter(authorizationFilter)).uri("lb://FASTLINK-MEDIA"))
                .route("user-service", r -> r.path("/api/v*/users/**", "/api/v*/roles/**" ).filters(f -> f.filter(authorizationFilter)).uri("lb://FASTLINK-USER"))
                .route("auth-service", r -> r.path("/login", "/api/v*/auth/**").filters(f -> f.filter(authorizationFilter)).uri("lb://FASTLINK-AUTH"))
                .build();
    }

/*    @Bean
    public DiscoveryClientRouteDefinitionLocator dynamicRouteLocator(
            ReactiveDiscoveryClient discoveryClient,
            DiscoveryLocatorProperties properties
    )
    {
        return new DiscoveryClientRouteDefinitionLocator(discoveryClient, properties);
    }*/
}
