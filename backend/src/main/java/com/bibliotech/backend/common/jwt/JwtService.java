package com.bibliotech.backend.common.jwt;

import com.bibliotech.backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.key}")
    private String llaveSecreta;

    @Value("${jwt.expiration}")
    private long expiracionMilisegundos;

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(llaveSecreta);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("userType", user.getUserType());
        return Jwts.builder()
                .claims(claims)
                .subject(user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiracionMilisegundos))
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public Object getClaimByName(String token, String claimName) {
        final Claims claims = getAllClaims(token);
        return claims.get(claimName);
    }

    public boolean isTokenValid(String token, User user) {
        final String usuarioId = getUserIdFromToken(token);
        return (usuarioId.equals(user.getId().toString()) && !isTokenExpired(token));
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        try{
            return getExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}