package com.mehmetselman.socialmediaproject.controller;

import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * ✅ AuthController kimlik doğrulama işlemlerini yönetir.
 * - Kullanıcı kayıt olma
 * - Giriş (JWT üretimi)
 * - Çıkış (token silme)
 * - Me (kimlik doğrulama testi)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 🧩 Yeni kullanıcı kaydı.
     */
    @PostMapping("/signup")
    public User signup(@RequestBody Map<String, String> body) {
        return authService.signup(body.get("username"), body.get("password"));
    }

    /**
     * 🧩 Giriş yapar, JWT token döner.
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        return authService.login(body.get("username"), body.get("password"));
    }

    /**
     * 🧩 JWT token’ı siler (logout işlemi).
     */
    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            authService.logout(token);
        } else {
            throw new RuntimeException("Token bulunamadı!");
        }
    }

    /**
     * 🧩 Kullanıcının kimlik bilgilerini döner (me endpoint).
     * 🔒 JWT doğrulaması sonrası AuthFilter tarafından `currentUser` atanır.
     */
    @GetMapping("/me")
    public User me(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        return authService.getMe(currentUser);
    }
}
