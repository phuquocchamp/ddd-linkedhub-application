package com.phuquocchamp.gatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class GatewaySwaggerUrlFilter extends AbstractGatewayFilterFactory<GatewaySwaggerUrlFilter.Config> {
    private static final String API_DOCS_PATH = "/v3/api-docs";
    private static final String GATEWAY_SERVICE_ID = "GATEWAY_SERVICE_ID";

    public GatewaySwaggerUrlFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String requestPath = exchange.getRequest().getURI().getPath();
            // Only apply filter for requests to API Docs endpoints, e.g. /v3/api-docs/auth-service
            if (requestPath.startsWith(API_DOCS_PATH)) {
                String serviceId = requestPath.replace(API_DOCS_PATH + "/", "");
                // Store serviceId in exchange attributes for later use
                exchange.getAttributes().put(GATEWAY_SERVICE_ID, serviceId);
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Add config fields if needed
    }
}
