package com.phuquocchamp.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RoutingGateway {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // auth-service
                .route("auth-service", r -> r
                        .path("/linkedhub/auth-service/**")
                        .filters(f -> f
                                .rewritePath("/linkedhub/auth-service/(?<remaining>.*)", "/${remaining}")
                                .addRequestHeader("X-User-ID", "user-id-from-jwt")
                        )
                        .uri("lb://AUTH-SERVICE")
                )
                // profile-service
                .route("profile-service", r -> r
                        .path("/linkedhub/profile-service/**")
                        .filters(f -> f
                                .rewritePath("/linkedhub/profile-service/(?<remaining>.*)", "/${remaining}")
                                .addRequestHeader("X-User-ID", "user-id-from-jwt")
                        )
                        .uri("lb://PROFILE-SERVICE")
                ).build();
    }
}
