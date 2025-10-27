package com.mehmetselman.socialmediaproject.controller;

import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    public void deleteUser(@PathVariable Long id, HttpServletRequest request){
        User currentUser = (User) request.getAttribute("currentUser");
        if (!"ADMIN".equals(currentUser.getRole())) throw new RuntimeException("Bu islem icin yetkiniz yok!");
        userService.adminDeleteUser(id);
    }

}
