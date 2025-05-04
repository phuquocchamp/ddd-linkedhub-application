package com.phuquocchamp.gatewayservice.filter;

import com.phuquocchamp.gatewayservice.utils.JwtTokenProvider;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthGatewayFilterFactory.class);
    private final JwtTokenProvider jwtTokenProvider;

    public AuthGatewayFilterFactory(JwtTokenProvider jwtTokenProvider) {
        super(Config.class);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            if (config.getBypassPaths().stream().anyMatch(path::startsWith)) {
                LOGGER.info(">>> Gateway Service: Bypassing authentication for path {}", path);
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                LOGGER.error(">>> Error at Gateway Service: Missing or invalid Authorization header");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);
            try {
                if (!jwtTokenProvider.validateToken(token)) {
                    LOGGER.error(">>> Error at Gateway Service: Invalid token");
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
                if (jwtTokenProvider.isTokenExpired(token)) {
                    LOGGER.error(">>> Error at Gateway Service: Expired token");
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String userID = jwtTokenProvider.getUserIDFromToken(token);
                if (userID == null) {
                    LOGGER.error(">>> Error at Gateway Service: User ID is null");
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                ServerHttpRequest request = exchange.getRequest().mutate().header("X-User-ID", userID).build();
                LOGGER.info(">>> Gateway Service: Request from {} passed", userID);
                return chain.filter(exchange.mutate().request(request).build());

            } catch (Exception e) {
                LOGGER.error(">>> Error at Gateway Service: Filter Error", e);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }

    @Getter
    @Setter
    public static class Config {
        private boolean enabled = true;
        private List<String> bypassPaths = new ArrayList<>();
    }
}