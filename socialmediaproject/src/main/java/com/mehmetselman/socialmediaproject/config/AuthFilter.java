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

/**
 * 🔐 AuthFilter:
 * Tüm /api/** isteklerinde JWT doğrulaması yapar.
 * Eğer token geçersiz, süresi dolmuş veya eksikse 401 döner.
 */
@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println(">>> AuthFilter devrede: " + path);

        String authHeader = request.getHeader("Authorization");
        System.out.println("Header Authorization: " + authHeader);

        // 🔹 Login ve Signup işlemleri filtre dışı tutulur
        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔹 Token yoksa veya format hatalıysa 401
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"Unauthorized - Missing or invalid token\"}");
            return;
        }

        // "Bearer " kısmını temizle
        String token = authHeader.substring(7).trim();
        System.out.println("Token substring sonrası: " + token);

        // 🔹 Token veritabanında var mı?
        Token dbToken = tokenRepository.findByToken(token);

        if (dbToken == null) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"Token not found in database\"}");
            return;
        }

        // 🔹 Token süresi dolmuş mu?
        if (dbToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"Token expired\"}");
            return;
        }

        // 🔹 JWT yapısal olarak geçerli mi?
        if (!JwtUtil.validateToken(token)) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"Invalid JWT token\"}");
            return;
        }

        // 🔹 Token’dan kullanıcı bilgisi çıkar
        String username = JwtUtil.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username);

        if (user == null) {
            response.setStatus(401);
            response.getWriter().write("{\"error\": \"User not found\"}");
            return;
        }

        // Debug log (toString yerine güvenli alanlar)
        System.out.println("✅ Token sahibi kullanıcı: " + user.getUsername() + " (ID: " + user.getId() + ")");

        // 🔹 Request’e kullanıcı nesnesini ekle (Controller’da kullanmak için)
        request.setAttribute("currentUser", user);

        // Devam et
        filterChain.doFilter(request, response);

        System.out.println("AuthFilter tamamlandı: " + path);
    }
}
