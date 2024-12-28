package com.example.ejournalgateway.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Arrays;

@Configuration
@EnableDiscoveryClient
public class DiscoveryConfig {
    @Value("${FRONTEND_URL:http://localhost:8080}")
    private String frontendUrl;

//    @Bean
//    public RouterFunction<ServerResponse> staticResourceRouter() {
//        return RouterFunctions.resources("/**", new ClassPathResource("static/"));
//    }

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
//                .route("frontend-service", r -> r.path("/**")
//                        .uri(frontendUrl))
//                .route("frontend-service", r -> r.path("/static/**")
//                        .uri("%s/static".formatted(frontendUrl)))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
