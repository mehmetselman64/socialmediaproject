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

/**
 * ✅ AuthService
 *
 * Kullanıcı kimlik doğrulama (authentication) işlemlerinden sorumludur.
 *
 * Sağladığı temel işlemler:
 *  - Yeni kullanıcı kaydı (signup)
 *  - Giriş yapma ve JWT token oluşturma (login)
 *  - Oturum kapatma / token silme (logout)
 *  - Me endpoint'i üzerinden mevcut kullanıcıyı döndürme (getMe)
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    /**
     * 🔹 Yeni kullanıcı kaydı oluşturur.
     * - Aynı kullanıcı adı mevcutsa hata fırlatır.
     * - Yeni kullanıcıya varsayılan olarak "USER" rolü atanır.
     *
     * @param username Yeni kullanıcının adı
     * @param password Yeni kullanıcının şifresi
     * @return Kaydedilen kullanıcı nesnesi
     */
    public User signup(String username, String password) {
        if (userRepository.findByUsername(username) != null)
            throw new RuntimeException("Kullanıcı adı zaten var. Farklı bir kullanıcı adı giriniz.");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    /**
     * 🔹 Kullanıcı giriş yapar ve yeni bir JWT token oluşturur.
     * - Kullanıcı adı veya parola yanlışsa hata fırlatır.
     * - Aynı kullanıcıya ait eski tokenlar veritabanından silinir.
     * - Yeni token 1 saat geçerli olacak şekilde oluşturulur.
     *
     * @param username Giriş yapan kullanıcının adı
     * @param password Giriş yapan kullanıcının parolası
     * @return Token bilgilerini içeren bir Map (token, expiry, username, role)
     */
    @Transactional
    public Map<String, Object> login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null || !user.getPassword().equals(password))
            throw new RuntimeException("Geçersiz kullanıcı adı veya parola!");

        // Eski token'ları temizle
        tokenRepository.deleteAllByUser(user);

        // Yeni JWT oluştur
        String tokenStr = JwtUtil.generateToken(user, 3600000L); // 1 saat = 3600000 ms

        Token token = new Token();
        token.setToken(tokenStr);
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusHours(1));
        tokenRepository.save(token);

        // Yanıt olarak kullanıcı bilgileri ve token döner
        Map<String, Object> response = new HashMap<>();
        response.put("token", tokenStr);
        response.put("expiry", token.getExpiryDate());
        response.put("username", user.getUsername());
        response.put("role", user.getRole());

        return response;
    }

    /**
     * 🔹 Çıkış işlemini gerçekleştirir.
     * - İlgili JWT token veritabanından silinir.
     *
     * @param tokenStr Silinecek token string değeri
     */
    @Transactional
    public void logout(String tokenStr) {
        Token token = tokenRepository.findByToken(tokenStr);
        if (token != null) {
            tokenRepository.delete(token);
        }
    }

    /**
     * 🔹 "Me" endpoint'i için mevcut kullanıcıyı döner.
     * - Eğer kullanıcı bulunamazsa (örneğin token geçersizse), hata fırlatır.
     *
     * @param currentUser Geçerli (token'dan alınan) kullanıcı
     * @return Kullanıcı nesnesi
     */
    public User getMe(User currentUser) {
        if (currentUser == null)
            throw new RuntimeException("Kullanıcı bilgisi bulunamadı (token geçersiz veya eksik).");

        return currentUser;
    }
}
