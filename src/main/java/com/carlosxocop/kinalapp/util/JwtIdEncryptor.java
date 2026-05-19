package com.carlosxocop.kinalapp.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtIdEncryptor {
    private static final String SECRET = "2025287KinalAppIN5AM_odmon5Am";
    private static final long EXPIRATION = 86400000;

    private static SecretKey getKey() {
        byte[] keyBytes = Base64.getEncoder().encode(SECRET.getBytes());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String encryptId(Long id) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .claim("id", id)
                .claim("type", "entityId")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getKey())
                .compact();
    }

    public static Long decryptId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            if (!"entityId".equals(claims.get("type"))) {
                throw new RuntimeException("Tipo de token inválido");
            }

            return claims.get("id", Long.class);
        } catch (Exception e) {
            throw new RuntimeException("Token inválido o expirado", e);
        }
    }
    
    public static boolean isValid(String token) {
        try {
            decryptId(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}