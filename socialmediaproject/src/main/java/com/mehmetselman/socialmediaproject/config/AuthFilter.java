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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println(">>> AuthFilter devrede: " + path);
        System.out.println("Header Authorization: " + request.getHeader("Authorization"));

        // Sadece login ve signup filtre dışı bırakılıyor
        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");

        if (token == null) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"Unauthorized - Missing token\"}");
            return;
        }

        // Fazladan "Bearer " varsa temizle
        token = token.replace("Bearer ", "").trim();
        System.out.println("Token substring sonrası: " + token);

        Token dbToken = tokenRepository.findByToken(token);
        System.out.println("DB’den gelen token: " + dbToken);
        System.out.println("validateToken sonucu: " + JwtUtil.validateToken(token));

        if (dbToken == null || dbToken.getExpiryDate().isBefore(LocalDateTime.now()) || !JwtUtil.validateToken(token)) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"Invalid or expired token\"}");
            return;
        }

        String username = JwtUtil.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username);

        System.out.println("Token içinden çıkan username: " + username);
        System.out.println("DB’den dönen user: " + user);

        if (user == null) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"User not found\"}");
            return;
        }

        // request'e kullanıcıyı ekle
        request.setAttribute("currentUser", user);

        filterChain.doFilter(request, response);
        System.out.println("AuthFilter tamamlandı: " + request.getRequestURI());
    }
}
