package com.mehmetselman.socialmediaproject.service;

import com.mehmetselman.socialmediaproject.entity.Token;
import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.repository.TokenRepository;
import com.mehmetselman.socialmediaproject.repository.UserRepository;
import com.mehmetselman.socialmediaproject.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;


    //kullanici kaydi
    public User signup(String username, String password){
        if (userRepository.findByUsername(username) != null) throw new RuntimeException("Kullanici adi zaten var. Farkli bir kullanici adi giriniz...");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole("USER");
        return userRepository.save(user);

    }

    //token üretip DB’ye kaydediyor.
    public Map<String, Object> login (String username, String password){
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) throw new RuntimeException("Gecersiz kullanici adi veya parola!");

        String tokenStr = JwtUtil.generateToken(user,3600000L);
        Token token = new Token();
        token.setToken(tokenStr);
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusHours(1));
        tokenRepository.save(token);

        Map<String, Object> response = new HashMap<>();
        response.put("token", tokenStr);
        response.put("expiry", token.getExpiryDate());
        return response;

    }


    //token silme işlemi yapıyor.
    public void logout(String tokenStr){
        Token token = tokenRepository.findByToken(tokenStr);
        if (token != null) tokenRepository.delete(token);
    }


    //Kullaniciyi getirir
    public User getMe(User currentUser) {
        return currentUser;
    }



}
