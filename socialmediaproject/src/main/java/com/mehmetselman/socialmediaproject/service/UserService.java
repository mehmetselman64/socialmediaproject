package com.mehmetselman.socialmediaproject.service;

import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 🔹 Parola güncelleme
    @Transactional
    public void updatePassword(User currentUser, String oldPassword, String newPassword) {
        if (!currentUser.getPassword().equals(oldPassword))
            throw new RuntimeException("Yanlış parola!");
        currentUser.setPassword(newPassword);
        userRepository.save(currentUser);
    }

    // 🔹 Kullanıcının kendi hesabını silmesi
    @Transactional
    public void deleteMe(User currentUser) {
        userRepository.delete(currentUser);
    }

    // 🔹 Admin herhangi bir kullanıcıyı silebilir
    @Transactional
    public void adminDeleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 🔹 ID'ye göre kullanıcı getir
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));
    }
}
