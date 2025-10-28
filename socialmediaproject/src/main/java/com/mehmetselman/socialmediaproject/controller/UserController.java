package com.mehmetselman.socialmediaproject.controller;

import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.repository.UserRepository;
import com.mehmetselman.socialmediaproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ✅ UserController
 *
 * Bu controller kullanıcılarla ilgili işlemleri yönetir:
 * - Belirli bir kullanıcıyı ID ile getirir.
 * - Giriş yapmış kullanıcının şifresini günceller.
 * - Giriş yapmış kullanıcının hesabını siler.
 *
 * Tüm endpointler "/api/users" ile başlar.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * UserService, kullanıcıyla ilgili iş mantığını (business logic) barındırır.
     */
    @Autowired
    private UserService userService;

    /**
     * UserRepository doğrudan veritabanına erişim sağlar.
     * (Burada constructor injection kullanılmıştır.)
     */
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 🔹 GET /api/users/{id}
     *
     * Belirli bir kullanıcıyı ID’sine göre getirir.
     * Örnek: GET http://localhost:8080/api/users/5
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    /**
     * 🔹 PUT /api/users/me/password
     *
     * Giriş yapmış kullanıcının şifresini günceller.
     *
     * İstek gövdesi (JSON):
     * {
     *   "oldPassword": "12345",
     *   "newPassword": "67890"
     * }
     *
     * - Token'dan gelen kullanıcı bilgisi (AuthFilter) `request` üzerinden alınır.
     * - Eski şifre doğruysa yeni şifre kaydedilir.
     */
    @PutMapping("/me/password")
    public ResponseEntity<String> updatePassword(HttpServletRequest request,
                                                 @RequestBody Map<String, String> body) {
        User currentUser = (User) request.getAttribute("currentUser");
        userService.updatePassword(currentUser, body.get("oldPassword"), body.get("newPassword"));
        return ResponseEntity.ok("Şifre başarıyla güncellendi.");
    }

    /**
     * 🔹 DELETE /api/users/me
     *
     * Giriş yapmış kullanıcının hesabını tamamen siler.
     *
     * - Token'dan gelen kullanıcı bilgisi alınır.
     * - Kullanıcının tüm ilişkili verileri (post, comment, token) cascade sayesinde silinir.
     */
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMe(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        userService.deleteMe(currentUser);
        return ResponseEntity.ok("Kullanıcı hesabı başarıyla silindi.");
    }

    /**
     * 🔹 GET /api/users
     *
     * (İsteğe bağlı)
     * Tüm kullanıcıları listelemek için örnek endpoint.
     * Admin işlemlerinde kullanılabilir.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
