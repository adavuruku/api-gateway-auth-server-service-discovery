package com.example.user_service.config.security.filters;

import com.example.user_service.dtos.request.JWTDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private ObjectMapper mapper = new ObjectMapper();

    @Value("${security-jwt-config.jwtIssuer}")
    public String ISSUER;
    @Value("${security-jwt-config.jwtSecret}")
    public String JWT_KEY;
    @Value("${security-jwt-config.jwtExpiry}")
    public long TOKEN_EXPIRY;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            JWTDto jwtModel = mapper.readValue(request.getInputStream(), JWTDto.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(jwtModel.getUsername(), jwtModel.getPassword());
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Date expDate = new Date(System.currentTimeMillis() + TOKEN_EXPIRY);
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(expDate).setIssuer(ISSUER)
                .signWith(key(),SignatureAlgorithm.HS512).compact();

//
//        Claims claims = Jwts.claims().setSubject(authResult.getName());
//        String token = Jwts.builder().setClaims(claims).setIssuedAt(new Date()).setIssuer(ISSUER)
//                .setExpiration(expDate).signWith(key(),SignatureAlgorithm.HS512)
//                .compact();


        log.debug(token);
        response.addHeader("Authorization", String.format("Bearer %s", token));
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_KEY));
    }
}