package com.example.sbj.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.User;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    public static String generateToken(User user) {
        return Jwts
                .builder()
                .setSubject(user.getUsername())
                .expiration(new Date(System.currentTimeMillis() + 300_000))
                .signWith(getSigningKey())
                .compact();
    }

    public static Claims getClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static Boolean isTokenValid(String token) {
        return !isExpired(token);
    }

    private static boolean isExpired(String token) {
        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }


    private static SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode("yourSecretKeyAndMustBeLongEnoughForSecurity");
        return Keys.hmacShaKeyFor(keyBytes);
    }
}