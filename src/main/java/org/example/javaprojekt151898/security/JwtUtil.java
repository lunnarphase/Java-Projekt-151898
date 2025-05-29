package org.example.javaprojekt151898.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-in-ms}")
    private Long expiration;

    /**
     * Generuje token JWT z określonym loginEmail (umieszczonym jako subject) i rolą
     * (przechowywaną w polu "role" w sekcji claims).
     */
    public String generateToken(String loginEmail, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setSubject(loginEmail)                  // subject = loginEmail
                .claim("role", role)                     // claim „role”
                .setIssuedAt(now)                        // data wystawienia
                .setExpiration(expiryDate)               // data wygaśnięcia
                .signWith(key, SignatureAlgorithm.HS256) // podpis algorytmem HS256
                .compact();
    }

    /**
     * Odczytuje loginEmail (subject) z przekazanego tokena.
     */
    public String getLoginEmailFromToken(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    /**
     * Pobiera z tokena wartość claima "role" i zwraca ją jako String.
     */
    public String getRoleFromToken(String token) {
        Claims claims = parseClaims(token).getBody();
        return claims.get("role", String.class);
    }

    /**
     * Sprawdza, czy token jest poprawny i nie wygasł.
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token); // jeśli tu nie wywoła wyjątku, token jest ważny
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    /**
     * Na podstawie roli tworzy listę autoryzacji w formacie akceptowanym przez Spring Security
     * (np. "ROLE_HR" lub "ROLE_CANDIDATE").
     */
    public List<SimpleGrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    /**
     * Prywatna metoda do parsowania i weryfikacji sygnatury tokena JWT.
     * Zwraca obiekt typu Jws<Claims>, z którego można odczytać np. getBody() (Claims).
     */
    private Jws<Claims> parseClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }
}