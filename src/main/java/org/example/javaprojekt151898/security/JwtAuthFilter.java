package org.example.javaprojekt151898.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.javaprojekt151898.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        if (!StringUtils.hasText(token) || !jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtUtil.getLoginEmailFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);
        var authorities = jwtUtil.getAuthorities(role);

        var userOpt = userRepository.findByLoginEmail(email);
        if (userOpt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        var user = userOpt.get();

        org.springframework.security.core.userdetails.UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getLoginEmail())
                        .password(user.getPassword())
                        .authorities(authorities)
                        .build();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}