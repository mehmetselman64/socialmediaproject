package com.mehmetselman.socialmediaproject;

import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.enums.Role;
import com.mehmetselman.socialmediaproject.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ✅ SocialMediaProjectApplication
 *
 * Bu sınıf, Spring Boot uygulamasının giriş noktasıdır.
 * Uygulama başlatıldığında:
 *  - Spring Context yüklenir.
 *  - Bean tanımlamaları yapılır.
 *  - Eğer veritabanında hiç admin yoksa, otomatik olarak bir "admin" hesabı oluşturulur.
 */
@SpringBootApplication
public class SocialMediaProjectApplication {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(SocialMediaProjectApplication.class, args);
    }

    /**
     * 🧩 Uygulama başlatıldığında otomatik olarak çalışır.
     *
     * Eğer veritabanında “admin” kullanıcı adıyla bir kullanıcı yoksa,
     * yeni bir ADMIN rolüne sahip kullanıcı oluşturur.
     *
     * Username: admin
     * Password: admin123
     */
    @PostConstruct
    public void initAdmin() {
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
            System.out.println("✅ Varsayılan admin hesabı oluşturuldu: admin / admin123");
        } else {
            System.out.println("ℹ️ Admin hesabı zaten mevcut, yeniden oluşturulmadı.");
        }
    }
}
