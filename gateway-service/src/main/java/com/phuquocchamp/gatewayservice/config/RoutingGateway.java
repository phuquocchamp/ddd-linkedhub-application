package com.phuquocchamp.gatewayservice.config;

import com.phuquocchamp.gatewayservice.filter.AuthGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RoutingGateway {
    private final AuthGatewayFilterFactory authGatewayFilterFactory;

    public RoutingGateway(AuthGatewayFilterFactory authGatewayFilterFactory) {
        this.authGatewayFilterFactory = authGatewayFilterFactory;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // auth-service
                .route("auth-service", r -> r
                        .path("/linkedhub/auth-service/**")
                        .filters(f -> f
                                .rewritePath("/linkedhub/auth-service/(?<remaining>.*)", "/${remaining}")
                        )
                        .uri("lb://AUTH-SERVICE")
                )
                // profile-service
                .route("profile-service", r -> r
                        .path("/linkedhub/profile-service/**")
                        .filters(f -> f
                                .rewritePath("/linkedhub/profile-service/(?<remaining>.*)", "/${remaining}")
                                .filter(authGatewayFilterFactory.apply(new AuthGatewayFilterFactory.Config()))
                        )
                        .uri("lb://PROFILE-SERVICE")
                )
                .build();
    }
}