package com.mehmetselman.socialmediaproject.util;

import com.mehmetselman.socialmediaproject.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * ✅ JwtUtil
 *
 * Bu sınıf, JWT (JSON Web Token) oluşturma, çözme ve doğrulama işlemlerini yönetir.
 *
 * Kullanım alanları:
 *  - Kullanıcı giriş yaptığında JWT oluşturmak
 *  - İsteklerde gelen JWT’nin geçerliliğini doğrulamak
 *  - JWT’den kullanıcı adı (subject) ve rol bilgisini almak
 *
 * Güvenlik:
 *  - Token, HS512 algoritması ile şifrelenir.
 *  - SECRET_KEY uygulama her başlatıldığında yeniden oluşturulur (runtime key).
 *    -> Üretim ortamında (production) sabit bir key kullanılmalıdır.
 */
public class JwtUtil {

    // 🧩 Token'ları imzalamak için kullanılacak gizli anahtar.
    // Bu anahtar rastgele oluşturulur; kalıcı bir key için environment variable tercih edilmelidir.
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    /**
     * 🧩 Kullanıcı için JWT oluşturur.
     *
     * @param user       Token oluşturulacak kullanıcı
     * @param expiration Token süresi (milisaniye cinsinden)
     * @return Oluşturulan JWT string
     *
     * Token içeriği (payload):
     * {
     *   "sub": "mehmet",
     *   "role": "USER",
     *   "iat": "oluşturulma zamanı",
     *   "exp": "token bitiş zamanı"
     * }
     */
    public static String generateToken(User user, long expiration) {
        return Jwts.builder()
                .setSubject(user.getUsername())                     // Kullanıcı adı (sub claim)
                .claim("role", user.getRole().name())               // Rol bilgisi
                .setIssuedAt(new Date())                            // Oluşturulma zamanı
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Geçerlilik süresi
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)     // İmza (HS512)
                .compact();
    }

    /**
     * 🧩 Token geçerliliğini kontrol eder.
     *
     * @param token Doğrulanacak JWT
     * @return Token geçerliyse true, değilse false
     *
     * Hatalı veya süresi dolmuş token durumunda false döner.
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            System.out.println("❌ Token validation failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * 🧩 Token içinden kullanıcı adını (subject) döner.
     *
     * @param token JWT string
     * @return Kullanıcı adı
     */
    public static String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 🧩 Token içinden kullanıcı rolünü döner.
     *
     * @param token JWT string
     * @return Kullanıcı rolü (örnek: "ADMIN" veya "USER")
     */
    public static String getRoleFromToken(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }
}
