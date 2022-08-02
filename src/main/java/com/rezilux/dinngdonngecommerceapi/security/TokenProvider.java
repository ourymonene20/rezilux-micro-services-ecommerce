package com.rezilux.dinngdonngecommerceapi.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    public static final String HEADER = "Authorization";
    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    public static final String PREFIX = "Bearer";
    @Value("{app.jwt.secret-base64}")
    private String SECRET = "ReziluxSecretKey";

    // EXPIRATION = 10 jours = 240 heures = 14400 minutes
    private final long EXPIRATION = 864_000_000;
    private final String AUTHORITIES = "auth";

    public String createForgotToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(Date.from(LocalDate.now().plusDays(2).atStartOfDay().toInstant(ZoneOffset.MAX)))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public String decodeForgetToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }


    public String createToken(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .claim(AUTHORITIES, authorities)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException exception) {
            log.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
        } catch (SignatureException exception) {
            log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
        }
        return false;
    }
}
