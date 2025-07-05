package com.example.User.Service.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.example.User.Service.entity.User;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;



@Service
public class JwtService {

    private Key key;

    @Value("${jwt.secret}")
    private String secret;

    /*@PostConstruct
    public void init1() {
        // توليد مفتاح سري باستخدام خوارزمية HS256
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }*/
    @Value("${jwt.expiration}")
    private long expiration;

    @PostConstruct
    public void init() {
        // تحويل الـ secret النصي إلى مفتاح Key مناسب
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

   public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", List.of("ROLE_" + role)) // هكذا تحفظ الصلاحيات مع ROLE_
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 10 ساعات صلاحية
                //.signWith(SignatureAlgorithm.HS256, key)
                .signWith(key)
                .compact();
    }


    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            return !extractAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}