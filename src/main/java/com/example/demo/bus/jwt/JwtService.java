package com.example.demo.bus.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private final String MY_SECRET = "30c4e9db5ed793142c60011bf20761b83a1f15fea5698c08a456f7b2983a2a3b";

    private SecretKey getSigningKey() {
        byte[] key = Decoders.BASE64.decode(MY_SECRET);
        return Keys.hmacShaKeyFor(key);
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(getSigningKey())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 dakika
                .compact();
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("ZAMAN ASIMI.LOGIN SAYFASINA YONLENDIRILIYORSUNUZ " + e.getMessage());
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, String username) {
        return (extractUsername(token).equals(username) && !isExpired(token));
    }

    private boolean isExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
