package org.example.javaprojekt151898.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.javaprojekt151898.entity.User;
import org.example.javaprojekt151898.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    private JwtAuthFilter jwtAuthFilter;
    private JwtUtil jwtUtil;
    private UserRepository userRepository;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        userRepository = mock(UserRepository.class);
        jwtAuthFilter = new JwtAuthFilter();
        org.springframework.test.util.ReflectionTestUtils.setField(jwtAuthFilter, "jwtUtil", jwtUtil);
        org.springframework.test.util.ReflectionTestUtils.setField(jwtAuthFilter, "userRepository", userRepository);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSkipIfNoAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldSkipIfHeaderNotBearer() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic xyz");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldSkipIfTokenIsEmpty() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer ");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldSkipIfInvalidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidtoken");
        when(jwtUtil.validateToken("invalidtoken")).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldSkipIfUserNotFound() throws ServletException, IOException {
        String token = "validtoken";
        String email = "user@domain.com";
        String role = "HR";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getLoginEmailFromToken(token)).thenReturn(email);
        when(jwtUtil.getRoleFromToken(token)).thenReturn(role);

        when(userRepository.findByLoginEmail(email)).thenReturn(Optional.empty());

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldAuthenticateIfTokenIsValidAndUserExists() throws ServletException, IOException {
        String token = "validtoken";
        String email = "user@domain.com";
        String role = "HR";
        String password = "pass";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getLoginEmailFromToken(token)).thenReturn(email);
        when(jwtUtil.getRoleFromToken(token)).thenReturn(role);
        when(jwtUtil.getAuthorities(role)).thenReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_HR")));

        User user = mock(User.class);
        when(user.getLoginEmail()).thenReturn(email);
        when(user.getPassword()).thenReturn(password);

        when(userRepository.findByLoginEmail(email)).thenReturn(Optional.of(user));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(email, SecurityContextHolder.getContext().getAuthentication().getName());
        assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
        verify(filterChain).doFilter(request, response);
    }
} 