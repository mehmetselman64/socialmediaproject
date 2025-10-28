package com.mehmetselman.socialmediaproject.service;

import com.mehmetselman.socialmediaproject.entity.Token;
import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.enums.Role;
import com.mehmetselman.socialmediaproject.repository.TokenRepository;
import com.mehmetselman.socialmediaproject.repository.UserRepository;
import com.mehmetselman.socialmediaproject.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    // Kullanıcı kaydı
    public User signup(String username, String password) {
        if (userRepository.findByUsername(username) != null)
            throw new RuntimeException("Kullanıcı adı zaten var. Farklı bir kullanıcı adı giriniz.");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    // Giriş ve token oluşturma
    @Transactional
    public Map<String, Object> login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password))
            throw new RuntimeException("Geçersiz kullanıcı adı veya parola!");

        // Aynı kullanıcıya ait eski token'ları temizle
        tokenRepository.deleteAllByUser(user);

        // Yeni JWT oluştur
        String tokenStr = JwtUtil.generateToken(user, 3600000L); // 1 saat geçerli
        Token token = new Token();
        token.setToken(tokenStr);
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusHours(1));
        tokenRepository.save(token);

        Map<String, Object> response = new HashMap<>();
        response.put("token", tokenStr);
        response.put("expiry", token.getExpiryDate());
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        return response;
    }

    // Çıkış işlemi (token siler)
    @Transactional
    public void logout(String tokenStr) {
        Token token = tokenRepository.findByToken(tokenStr);
        if (token != null) {
            tokenRepository.delete(token);
        }
    }

    // Me endpoint'i için kullanıcıyı döner
    public User getMe(User currentUser) {
        if (currentUser == null) {
            throw new RuntimeException("Kullanıcı bilgisi bulunamadı (token geçersiz veya eksik).");
        }
        return currentUser;
    }
}
