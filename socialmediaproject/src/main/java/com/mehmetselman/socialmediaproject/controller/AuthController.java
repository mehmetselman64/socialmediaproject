package com.mehmetselman.socialmediaproject.controller;

import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public User signup(@RequestBody Map<String, String> body){
        return authService.signup(body.get("username"), body.get("password") );
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body){
        return authService.login(body.get("username"), body.get("password"));
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7); // bearer kelimesini atmak icin kelime ve bosluk 7 karakterli
        authService.logout(token);
    }

    //kullanici bilgisini doner
    @GetMapping("/me")
    public User me(HttpServletRequest request){
        User currentUser = (User) request.getAttribute("currentUser");
        return authService.getMe(currentUser);
    }

}
