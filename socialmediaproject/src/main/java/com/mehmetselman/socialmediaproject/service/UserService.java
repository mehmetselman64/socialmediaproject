package com.mehmetselman.socialmediaproject.service;

import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ✅ UserService
 *
 * Bu servis sınıfı, kullanıcı (User) ile ilgili işlemleri yönetir.
 *
 * Sağladığı işlemler:
 *  - Kullanıcı parolasını güncelleme
 *  - Kullanıcının kendi hesabını silme
 *  - Admin tarafından kullanıcı silme
 *  - Kullanıcı bilgilerini ID’ye göre getirme
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 🧩 Kullanıcının kendi parolasını güncellemesini sağlar.
     *
     * - Eski parola doğru değilse hata fırlatır.
     * - Yeni parola kaydedilir.
     *
     * @param currentUser  Şu anda giriş yapmış kullanıcı
     * @param oldPassword  Eski parola
     * @param newPassword  Yeni parola
     */
    @Transactional
    public void updatePassword(User currentUser, String oldPassword, String newPassword) {
        if (!currentUser.getPassword().equals(oldPassword)) {
            throw new RuntimeException("❌ Eski parolanız hatalı!");
        }

        currentUser.setPassword(newPassword);
        userRepository.save(currentUser);
    }

    /**
     * 🧩 Kullanıcı kendi hesabını silebilir.
     *
     * - Bu işlem kullanıcının gönderilerini, yorumlarını ve beğenilerini
     *   cascade ilişkileri sayesinde otomatik olarak siler.
     *
     * @param currentUser Şu anda giriş yapmış kullanıcı
     */
    @Transactional
    public void deleteMe(User currentUser) {
        userRepository.delete(currentUser);
    }

    /**
     * 🧩 Admin herhangi bir kullanıcıyı silebilir.
     *
     * - İlişkili post, comment, like ve token kayıtları da
     *   cascade sayesinde otomatik olarak silinir.
     *
     * @param id Silinecek kullanıcının ID’si
     */
    @Transactional
    public void adminDeleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("❌ Silinmek istenen kullanıcı bulunamadı!");
        }

        userRepository.deleteById(id);
    }

    /**
     * 🧩 ID’ye göre kullanıcıyı getirir.
     *
     * - Eğer kullanıcı bulunamazsa hata fırlatır.
     *
     * @param id Kullanıcı ID’si
     * @return User nesnesi
     */
    @Transactional
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Kullanıcı bulunamadı!"));
    }
}
