package com.example.ejournalgateway.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
public class DiscoveryConfig {

    @Value("${FRONTEND_URL:http://localhost:8080}")
    private String frontendUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        if (frontendUrl.endsWith("/")) {
            frontendUrl = frontendUrl.substring(0, frontendUrl.length()-1);
        }
        return builder.routes()
                .route("e-journal-auth", r -> r.path("/api/v1/auth/**")
                        .uri("lb://e-journal-auth"))
                .route("e-journal-back", r -> r.path("/api/v1/journal/**")
                        .uri("lb://e-journal-back"))
                .route("frontend", r -> r.path("/**")
                        .uri(frontendUrl))
                .build();
    }
}
