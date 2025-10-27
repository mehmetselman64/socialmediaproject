package com.mehmetselman.socialmediaproject.config;

import com.mehmetselman.socialmediaproject.entity.Token;
import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.repository.TokenRepository;
import com.mehmetselman.socialmediaproject.repository.UserRepository;
import com.mehmetselman.socialmediaproject.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/") || path.equals("/api/posts")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }
        token = token.substring(7);

        Token dbToken = tokenRepository.findByToken(token);
        if (dbToken == null || dbToken.getExpiryDate().isBefore(LocalDateTime.now()) || !JwtUtil.validateToken(token)) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"Invalid token\"}");
            return;
        }

        String username = JwtUtil.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username);
        request.setAttribute("currentUser", user);

        filterChain.doFilter(request, response);
    }



}
