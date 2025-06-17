package com.phuquocchamp.gatewayservice.config;

import com.phuquocchamp.gatewayservice.filter.AuthGatewayFilterFactory;
import com.phuquocchamp.gatewayservice.filter.GatewaySwaggerUrlFilter;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RoutingGateway {
    private final AuthGatewayFilterFactory authGatewayFilterFactory;
    private final GatewaySwaggerUrlFilter gatewaySwaggerUrlFilter;

    public RoutingGateway(AuthGatewayFilterFactory authGatewayFilterFactory, GatewaySwaggerUrlFilter gatewaySwaggerUrlFilter) {
        this.authGatewayFilterFactory = authGatewayFilterFactory;
        this.gatewaySwaggerUrlFilter = gatewaySwaggerUrlFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
            return builder.routes()
                // Swagger OpenAPI Documentation
                // Explicit route for auth-service API docs
                .route("openapi-docs-auth", r -> r
                    .path("/linkedhub/api-docs/auth-service")
                    .filters(f -> f
                        .filter(gatewaySwaggerUrlFilter.apply(new GatewaySwaggerUrlFilter.Config()))
                        .rewritePath("/linkedhub/api-docs/auth-service", "/v3/api-docs")
                    )
                    .uri("lb://AUTH-SERVICE")
                )
                // Explicit route for profile-service API docs
                .route("openapi-docs-profile", r -> r
                    .path("/linkedhub/api-docs/profile-service")
                    .filters(f -> f
                        .filter(gatewaySwaggerUrlFilter.apply(new GatewaySwaggerUrlFilter.Config()))
                        .rewritePath("/linkedhub/api-docs/profile-service", "/v3/api-docs")
                    )
                    .uri("lb://PROFILE-SERVICE")
                            )
                // Gateway Routing
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