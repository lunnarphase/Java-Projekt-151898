package org.example.javaprojekt151898.config;

import org.example.javaprojekt151898.security.JwtAuthFilter;
import org.example.javaprojekt151898.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // udostępnij publicznie Swaggera
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger").permitAll()
                        // zezwól na rejestrację i logowanie bez autentykacji
                        .requestMatchers("/auth/login", "/auth/register").permitAll()

                        // Przykład: endpointy dla HR
                        // Tylko zautoryzowany użytkownik z rolą HR może tworzyć oferty
                        .requestMatchers("/api/job-offers/**").hasRole(UserRole.HR.name())

                        // Każdy może przeglądać oferty i aplikować, dodatkowe uprawnienia delegowane przez @PreAuthorize
                        .requestMatchers("/api/applications/**").hasAnyRole(UserRole.HR.name(), UserRole.ADMIN.name(), UserRole.CANDIDATE.name())

                        // Pozostałe muszą być zalogowane
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(16, 32, 1, 65536, 3);
    }
}