package com.cgv.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGateWayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("movie-service", r -> r.path("/api/movies/**", "/api/schedules/**", "/api/seats/**")
                        .uri("lb://movie-service"))
                .route("reservation-service", r -> r.path("/api/reservations/**")
                        .uri("lb://reservation-service"))
                .route("queue-service", r -> r.path("/api/queues/**")
                        .uri("lb://queue-service"))
                .build();
    }
}
