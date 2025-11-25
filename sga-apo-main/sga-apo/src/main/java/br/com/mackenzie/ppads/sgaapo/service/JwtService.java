package br.com.mackenzie.ppads.sgaapo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Serviço responsável por todas as operações de JWT (Geração, Validação, Extração).
 * Atualizado para JJWT 0.12.5
 */
@Service
public class JwtService {

    // TODO: Mover para application.properties em produção
    private static final String SECRET_KEY = "suaChaveSecretaSuperForteDe256BitsQueVoceDeveMudarDepois";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 horas

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                // Sintaxe atualizada para 0.12.5:
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        // Sintaxe atualizada para 0.12.5:
        // 1. parser() em vez de parserBuilder()
        // 2. verifyWith() em vez de setSigningKey()
        // 3. parseSignedClaims() em vez de parseClaimsJws()
        // 4. getPayload() em vez de getBody()
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        // Retorna SecretKey especificamente para o verifyWith funcionar
        return Keys.hmacShaKeyFor(keyBytes);
    }
}