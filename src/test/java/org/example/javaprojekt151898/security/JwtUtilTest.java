package org.example.javaprojekt151898.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "12345678901234567890123456789012"); // 32 znaki
        ReflectionTestUtils.setField(jwtUtil, "expiration", 1000L * 60 * 60); // 1 godzina
    }

    @Test
    void shouldGenerateAndValidateToken() {
        String token = jwtUtil.generateToken("test@example.com", "HR");
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void shouldExtractLoginEmailAndRoleFromToken() {
        String token = jwtUtil.generateToken("user@domain.com", "ADMIN");
        assertEquals("user@domain.com", jwtUtil.getLoginEmailFromToken(token));
        assertEquals("ADMIN", jwtUtil.getRoleFromToken(token));
    }

    @Test
    void shouldReturnAuthorities() {
        var authorities = jwtUtil.getAuthorities("CANDIDATE");
        assertEquals(1, authorities.size());
        assertEquals("ROLE_CANDIDATE", authorities.get(0).getAuthority());
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        assertFalse(jwtUtil.validateToken("invalid.token.value"));
    }
} 