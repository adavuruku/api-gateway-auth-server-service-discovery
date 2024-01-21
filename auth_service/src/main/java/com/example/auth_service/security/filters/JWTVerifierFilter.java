package com.example.auth_service.security.filters;

import com.example.auth_service.model.redis.TokensEntity;
import com.example.auth_service.security.SecurityConstants;
import com.example.auth_service.services.redis.TokensRedisService;
import com.example.auth_service.utils.Utilities;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JWTVerifierFilter extends OncePerRequestFilter {

    private final TokensRedisService tokensRedisService;

    @Value("${security-jwt-config.jwtIssuer}")
    public String ISSUER;
    @Value("${security-jwt-config.jwtSecret}")
    public String JWT_KEY;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = httpServletRequest.getHeader(SecurityConstants.HEADER);
        if(!(Utilities.validString(bearerToken) && bearerToken.startsWith(SecurityConstants.PREFIX))) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

//        if(!Jwts.parser().isSigned(bearerToken)) {
//            filterChain.doFilter(httpServletRequest, httpServletResponse);
//            return;
//        }

        log.info("Bear bear {}",bearerToken);

        String authToken = bearerToken.replace(SecurityConstants.PREFIX, "");

        log.info("Bear bear replace {} {} {}",JWT_KEY,ISSUER, authToken);

        Optional<TokensEntity> tokensEntity = tokensRedisService.findById(authToken);
        log.info("Bear bear replace present {}",tokensEntity.isPresent());
        if(!tokensEntity.isPresent()) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String token = tokensEntity.get().getAuthenticationToken();
        Jws<Claims> authClaim = Jwts.parser().setSigningKey(JWT_KEY)
                .requireIssuer(ISSUER)
                .parseClaimsJws(token);

        String username = authClaim.getBody().getSubject();
        log.info("auth {}", authClaim.getBody());
        List<Map<String, String>> authorities = (List<Map<String, String>>) authClaim.getBody().get("authorities");

        List<GrantedAuthority> grantedAuthorities = authorities.stream().map(map -> new SimpleGrantedAuthority(map.get("authority")))
                .collect(Collectors.toList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        httpServletRequest.setAttribute("username", username);
        httpServletRequest.setAttribute("authorities", grantedAuthorities);
        httpServletRequest.setAttribute("jwt", token);

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}