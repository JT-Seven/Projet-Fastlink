package org.fastlink.gatewayservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastlink.gatewayservice.request.AuthRequest;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Predicate;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component @AllArgsConstructor @Slf4j
public class CustomAuthorizationFilter implements GatewayFilter
{

    private final RestTemplate restTemplate;

    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //ServerResponse serverResponse = (ServerResponse) exchange.getResponse();
        WebClient webClient = webClientBuilder.baseUrl(AuthRequest.AUTH_BASE_URL_LB).build();

        final List<String> apiEndpoints =
                List.of(
                        "/login",
                        "/api/v1/auth/google",
                        "/api/v1/users/register",
                        "/api/v1/users/register/google",
                        "/api/v1/users/activate-account"
                );

        Predicate<ServerHttpRequest> isSecured =
                req -> apiEndpoints
                        .stream()
                        .noneMatch(uri -> req.getURI().getPath().contains(uri));


        if ( ! isSecured.test(request) )
        {
            log.info("You are authorized to access this endpoint");
            request.mutate().header("Access-Control-Allow-Origin","http://localhost:8080");
            return chain.filter(exchange);
        }

        String authHeader = getAuthHeader(request);

        if ( authHeader == null || !authHeader.startsWith("Bearer ") )
        {
            log.error("Invalid authorization header - {}", authHeader);
            return
                    onError(
                            response,
                            String.format("Invalid authorization header - %s", authHeader),
                            HttpStatus.FORBIDDEN
                    );
        }

        boolean tokenValidation = JwtUtil.validateToken(getTokenFromHeader(authHeader));

        if ( !tokenValidation )
        {
            log.error("Invalid token");
            return
                    onError(
                            response,
                            "Invalid token",
                            HttpStatus.FORBIDDEN
                    );
        }

        log.info("Token validated: You are authorized");
        return chain.filter(exchange);
    }

    /* Private Methods */

    private Mono<Void> onError(ServerHttpResponse res, String errorMessage, HttpStatus status)
    {
        res.setStatusCode(status);
        byte[] bytes = errorMessage.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = res.bufferFactory().wrap(bytes);
        res.getHeaders().add("Content-Type", "application/json");
        return res.writeWith(Flux.just(buffer));
    }


    private String getAuthHeader(ServerHttpRequest request)
    {
        return request.getHeaders().getOrEmpty(AUTHORIZATION).get(0);
    }

    private String getTokenFromHeader(String authHeader)
    {
        return authHeader.substring("Bearer ".length());
    }

}