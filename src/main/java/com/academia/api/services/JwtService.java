package com.academia.api.services;

import com.academia.api.models.entities.Funcionario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secretKey;

    @Value("${security.jwt.expiration:86400000}")
    private long jwtExpiration;

    public String gerarToken(Funcionario funcionario) {
        Map<String, Object> claims = Map.of(
                "id", funcionario.getId(),
                "nome", funcionario.getNome(),
                "perfil", funcionario.getPerfil().name()
        );

        return Jwts.builder()
                .claims(claims)
                .subject(funcionario.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValido(String token) {
        try {
            return !isTokenExpirado(token);
        } catch (Exception e) {
            return false;
        }
    }

    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    public String extrairPerfil(String token) {
        return extrairClaims(token).get("perfil", String.class);
    }

    public Long extrairId(String token) {
        return extrairClaims(token).get("id", Long.class);
    }

    private boolean isTokenExpirado(String token) {
        return extrairClaims(token).getExpiration().before(new Date());
    }

    private io.jsonwebtoken.Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
