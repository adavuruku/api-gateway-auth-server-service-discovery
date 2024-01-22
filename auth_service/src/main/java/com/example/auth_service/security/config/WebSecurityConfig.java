package com.example.auth_service.security.config;


import com.example.auth_service.security.filters.AuthEntryPointJwt;
import com.example.auth_service.security.filters.JWTAuthenticationFilter;
import com.example.auth_service.security.filters.JWTVerifierFilter;
import com.example.auth_service.security.services.ApplicationUserDetailsService;
import com.example.auth_service.services.redis.TokensRedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Value("${security-jwt-config.jwtIssuer}")
    public String ISSUER;
    @Value("${security-jwt-config.jwtSecret}")
    public String JWT_KEY;
    @Value("${security-jwt-config.jwtExpiry}")
    public long TOKEN_EXPIRY;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ApplicationUserDetailsService applicationUserDetailsService;

    @Autowired
    TokensRedisService redisService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private AuthenticationManagerBuilder authManagerBuilder;

    @Bean
    public JWTVerifierFilter jwtVerifierFilter(TokensRedisService redisService) {
        return new JWTVerifierFilter(redisService);
    }

    ;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/v1/validateToken/whitelisted").permitAll()
                                .requestMatchers("/actuator/**").permitAll()
                                .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilter(new JWTAuthenticationFilter(
                authManagerBuilder.getOrBuild(),
                redisService, new ObjectMapper(), ISSUER, JWT_KEY, TOKEN_EXPIRY
        ));
        http.addFilterAfter(jwtVerifierFilter(redisService), JWTAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(encoder);
        authenticationProvider.setUserDetailsService(applicationUserDetailsService);

        return authenticationProvider;
    }
}