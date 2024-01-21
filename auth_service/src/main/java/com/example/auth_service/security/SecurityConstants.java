package com.example.auth_service.security;

import org.springframework.beans.factory.annotation.Value;

public class SecurityConstants {
    @Value("${security-jwt-config.jwtSecret}")
    public static String KEY;

    @Value("${security-jwt-config.jwtIssuer}")
    public static String ISSUER;
    public static String PREFIX = "Bearer ";
    public static String HEADER = "Authorization";

    @Value("${security-jwt-config.jwtExpiry}")
    public static long TOKEN_EXPIRY;
}
