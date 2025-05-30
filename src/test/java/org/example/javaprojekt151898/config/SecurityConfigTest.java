package org.example.javaprojekt151898.config;

import org.example.javaprojekt151898.security.JwtAuthFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import({SecurityConfig.class, JwtAuthFilter.class})
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void shouldCreatePasswordEncoderBean() {
        assertThat(passwordEncoder).isNotNull();
        String encoded = passwordEncoder.encode("test123");
        assertThat(passwordEncoder.matches("test123", encoded)).isTrue();
    }

    @Test
    void shouldCreateSecurityFilterChain() throws Exception {
        SecurityFilterChain chain = securityConfig.securityFilterChain(null);
        assertThat(chain).isNotNull();
    }

    @Test
    void shouldInjectJwtAuthFilter() {
        assertThat(jwtAuthFilter).isNotNull();
    }
} 