package com.mehmetselman.socialmediaproject.controller;

import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ✅ AdminController
 *
 * Bu controller, yalnızca **ADMIN** rolüne sahip kullanıcıların erişebileceği işlemleri içerir.
 *
 * Şu anda sadece kullanıcı silme işlemi (deleteUser) eklenmiştir.
 *
 * Erişim yolu: `/api/admin`
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    /**
     * 🔹 DELETE /api/admin/users/{id}
     *
     * Bir kullanıcıyı (ID'sine göre) siler.
     *
     * - Bu endpoint'e yalnızca **ADMIN** rolüne sahip kullanıcılar erişebilir.
     * - Giriş yapan kullanıcının rolü `AuthFilter` tarafından `request` nesnesine eklenir.
     * - Eğer kullanıcı ADMIN değilse, "Bu işlem için yetkiniz yok!" hatası fırlatılır.
     *
     * Örnek istek:
     * ```
     * DELETE http://localhost:8080/api/admin/users/5
     * Header: Authorization: Bearer <JWT_TOKEN>
     * ```
     *
     * @param id Silinmek istenen kullanıcının ID'si
     * @param request AuthFilter tarafından eklenen currentUser bilgisini içerir
     */
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id, HttpServletRequest request) {
        // Mevcut kullanıcıyı (token'dan gelen kullanıcı) al
        User currentUser = (User) request.getAttribute("currentUser");

        // Eğer giriş yapan kullanıcı ADMIN değilse hata fırlat
        if (!"ADMIN".equals(currentUser.getRole().name())) {
            throw new RuntimeException("Bu işlem için yetkiniz yok!");
        }

        // Yetki kontrolünü geçen kullanıcı hedef kullanıcıyı silebilir
        userService.adminDeleteUser(id);
    }
}
