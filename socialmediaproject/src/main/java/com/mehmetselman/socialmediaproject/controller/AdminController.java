package com.mehmetselman.socialmediaproject.controller;

import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    // ✅ Kullanıcı silme endpoint'i
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id, HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");

        if (currentUser == null || !"ADMIN".equals(currentUser.getRole().name())) {
            throw new RuntimeException("Bu işlem için yetkiniz yok!");
        }

        userService.adminDeleteUser(id);
    }
}
