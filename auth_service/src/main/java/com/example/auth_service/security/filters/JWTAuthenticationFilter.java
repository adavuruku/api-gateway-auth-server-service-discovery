package com.example.auth_service.security.filters;


import com.example.auth_service.model.ConnValidationResponse;
import com.example.auth_service.model.JwtAuthenticationModel;
import com.example.auth_service.model.redis.TokensEntity;
import com.example.auth_service.security.SecurityConstants;
import com.example.auth_service.services.redis.TokensRedisService;
import com.example.auth_service.utils.Utilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   TokensRedisService tokensRedisService, ObjectMapper mapper, String ISSUER,
                                   String JWT_KEY, long TOKEN_EXPIRY) {
        this.authenticationManager = authenticationManager;
        this.tokensRedisService = tokensRedisService;
        this.mapper = mapper;
        this.ISSUER = ISSUER;
        this.JWT_KEY = JWT_KEY;
        this.TOKEN_EXPIRY = TOKEN_EXPIRY;
    }

    private AuthenticationManager authenticationManager;

    private TokensRedisService tokensRedisService;

    private ObjectMapper mapper = new ObjectMapper();

    private String ISSUER;
    private String JWT_KEY;
    private long TOKEN_EXPIRY;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            JwtAuthenticationModel authModel = mapper.readValue(request.getInputStream(), JwtAuthenticationModel.class);
//            log.info(" Request mapper filter {} {}", mapper.writeValueAsString(authModel));
            Authentication authentication = new UsernamePasswordAuthenticationToken(authModel.getUsername(), authModel.getPassword());
            return authenticationManager.authenticate(authentication);

        } catch (IOException e) {
            log.info(" IOException Request mapper filter {}",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
//                .claim("principal", authResult.getPrincipal())
                .setIssuedAt(new Date())
                .setIssuer(ISSUER)
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(TOKEN_EXPIRY).toInstant(ZoneOffset.UTC)))
                .signWith(key(),SignatureAlgorithm.HS512)
                .compact();

        log.info("auth token {}",token);
        TokensEntity tokensEntity = TokensEntity.builder().id(Utilities.generateUuid()).authenticationToken(token)
                .username(authResult.getName())
                .createdBy("SYSTEM").createdOn(LocalDateTime.now())
                .modifiedBy("SYSTEM").modifiedOn(LocalDateTime.now())
                .build();
        tokensEntity = tokensRedisService.save(tokensEntity);
        response.addHeader(SecurityConstants.HEADER, String.format("Bearer %s", tokensEntity.getId()));
        response.addHeader("Expiration", String.valueOf(TOKEN_EXPIRY));

        ConnValidationResponse respModel = ConnValidationResponse.builder().status(HttpStatus.OK.name()).token(String.format("Bearer %s", tokensEntity.getId())).methodType(HttpMethod.GET.name()).isAuthenticated(true).build();
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(mapper.writeValueAsBytes(respModel));
    }


    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_KEY));
    }
}
