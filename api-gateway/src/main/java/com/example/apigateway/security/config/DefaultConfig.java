package com.example.apigateway.security.config;

import com.example.apigateway.security.filter.AuthenticationPrefilter;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class DefaultConfig {

    @Value("${spring.gateway.excludedURLs}")
    private String urlsStrings;

    @Bean
    @Qualifier("excludedUrls")
    public List<String> excludedUrls() {
        return Arrays.stream(urlsStrings.split(",")).collect(Collectors.toList());
    }

    @Bean
    public ObjectMapper objectMapper() {
        JsonFactory factory = new JsonFactory();
        factory.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);

        ObjectMapper objectMapper = new ObjectMapper(factory);
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        return objectMapper;
    }


    @Bean
    public RouteLocator routes(
            RouteLocatorBuilder builder,
            AuthenticationPrefilter authFilter) {
        return builder.routes()
//                .route("user-service-route", r -> r.path("/user-service/api/v1/users/create")
//                        .filters(f ->
//                                f.rewritePath("/user-service(?<segment>/?.*)", "$\\{segment}"))
//                        .uri("lb://user-service"))
//                .route("user-service-route", r -> r.path("/user-service/api/v1/users/authenticate")
//                        .filters(f ->
//                                f.rewritePath("/user-service(?<segment>/?.*)", "$\\{segment}"))
//                        .uri("lb://user-service"))
//                .route("first-microservice",r -> r.path("/user-service/**")
////                        .and().method("POST")
//                        .uri("lb://user-service"))
                .route("auth-service-route", r -> r.path("/authentication-service/**")
                        .filters(f ->
                                f.rewritePath("/authentication-service(?<segment>/?.*)", "$\\{segment}")
                                        .filter(authFilter.apply(
                                                new AuthenticationPrefilter.Config())))
                        .uri("lb://authentication-service"))
                .route("user-service-route", r -> r.path("/user-service/**")
                        .filters(f ->
                                f.rewritePath("/user-service(?<segment>/?.*)", "$\\{segment}")
                                        .filter(authFilter.apply(
                                                new AuthenticationPrefilter.Config())))
                        .uri("lb://user-service"))

                .build();
    }
}
