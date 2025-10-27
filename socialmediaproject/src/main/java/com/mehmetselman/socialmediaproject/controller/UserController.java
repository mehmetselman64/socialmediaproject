package com.mehmetselman.socialmediaproject.controller;

import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    //sifre guncelleme
    @PutMapping("/me/password")
    public void updatePassword(HttpServletRequest request, @RequestBody Map<String, String> body){
        User currentUser = (User) request.getAttribute("currentUser");
        userService.updatePassword(currentUser, body.get("oldPassword"), body.get("newPassword"));

    }

    @DeleteMapping("/me")
    public void deleteMe(HttpServletRequest request){
        User currentUser = (User) request.getAttribute("currentUser");
        userService.deleteMe(currentUser);
    }


}
