package com.mehmetselman.socialmediaproject.repository;

import com.mehmetselman.socialmediaproject.entity.Token;
import com.mehmetselman.socialmediaproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ✅ TokenRepository
 *
 * Bu repository, JWT token'larını (kullanıcı oturumlarını) yönetmek için kullanılır.
 *
 * Spring Data JPA sayesinde CRUD işlemleri otomatik olarak sağlanır.
 *
 * Ek olarak:
 *  - Token'ı string değerine göre arama
 *  - Belirli bir kullanıcıya ait tüm token'ları listeleme
 *  - Belirli bir kullanıcının tüm token'larını silme
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    /**
     * 🔹 Belirli bir token string'ine göre token nesnesini döner.
     *
     * @param token Aranan token string'i
     * @return Token nesnesi veya null
     */
    Token findByToken(String token);

    /**
     * 🔹 Belirli bir kullanıcıya ait tüm token kayıtlarını döner.
     *
     * @param user Token’ları alınacak kullanıcı
     * @return Token listesi
     */
    List<Token> findByUser(User user);

    /**
     * 🔹 Belirli bir kullanıcıya ait tüm token'ları siler.
     *
     * Kullanıcı logout olduğunda veya yeniden login olduğunda çağrılır.
     *
     * @param user Token’ları silinecek kullanıcı
     */
    void deleteAllByUser(User user);
}
