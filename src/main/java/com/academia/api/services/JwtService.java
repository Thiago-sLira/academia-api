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

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
