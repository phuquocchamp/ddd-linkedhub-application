package com.phuquocchamp.gatewayservice.application.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RoutingGateway {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes().
                route("profile-service", r -> r
                .path("/linkedhub/profile-service/**")
                .filters(f -> f
                        .rewritePath("/linkedhub/profile-service/(?<remaining>.*)", "/${remaining}")
                        .addRequestHeader("X-User-ID", "user-id-from-jwt")
                )
                .uri("lb://PROFILE-SERVICE")
                ).build();
    }
}
