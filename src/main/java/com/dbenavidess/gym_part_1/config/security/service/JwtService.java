package com.dbenavidess.gym_part_1.config.security.service;

import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.JwtRepository;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.JwtEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.entitites.UserEntity;
import com.dbenavidess.gym_part_1.infrastructure.repository.jpa.jpaRepositories.UserEntityJpaRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.expiration:86400000}") // 24 hours in milliseconds
    private long jwtExpiration;

    private final SecretKey secretKey;
    private final JwtRepository repository;
    private final UserEntityJpaRepository userJpaRepository;

    public JwtService(JwtRepository repository, UserEntityJpaRepository userJpaRepository) throws NoSuchAlgorithmException {
        this.repository = repository;
        this.userJpaRepository = userJpaRepository;
        secretKey = KeyGenerator.getInstance("HmacSHA256").generateKey();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        String jwt = buildToken(extraClaims, userDetails, jwtExpiration);
        repository.save(new JwtEntity(jwt, userJpaRepository.findByUsername(userDetails.getUsername()).get()));
        return jwt;
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        if((username.equals(userDetails.getUsername())) && !isTokenExpired(token)){
            JwtEntity jwt = repository.findByJwt(token).orElse(null);
            return jwt != null;
        }
        return true;
    }
    public void deleteByUsername(String username){
        Optional<JwtEntity> jwt = repository.findByUser_Username(username);
        jwt.ifPresent(j -> {
            UserEntity user = j.getUser();
            if (user != null) {
                user.setJwt(null);
            }
            repository.delete(j);
        });
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    private SecretKey getSignInKey() {
        return secretKey;
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }
}