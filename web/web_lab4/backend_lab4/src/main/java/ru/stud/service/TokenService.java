package ru.stud.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;
import ru.stud.model.UserEntity;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@ApplicationScoped
public class TokenService {
    public TokenService(){}
    private final String SECRET = "it-is-a-secret-name-more-words-for-key-to-make-it-safer";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(UserEntity user){
        String token = Jwts.builder().setSubject(String.valueOf(user.getId())).setIssuedAt(new Date()).setExpiration(Date.from(Instant.now().plus(30, ChronoUnit.MINUTES))).signWith(key, SignatureAlgorithm.HS256).compact();
        return token;
    }

    public long extractUserId(String token){
        long id = Long.parseLong(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject());
        return id;
    }
}
