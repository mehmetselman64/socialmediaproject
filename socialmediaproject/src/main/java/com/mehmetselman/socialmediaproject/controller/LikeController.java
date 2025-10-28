package com.mehmetselman.socialmediaproject.controller;

import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ✅ LikeController — Gönderi beğeni işlemlerini yönetir.
 *
 * Endpointler:
 *  - POST /api/posts/{id}/likes   → Gönderiyi beğen
 *  - DELETE /api/posts/{id}/likes → Beğeniyi kaldır
 */
@RestController
@RequestMapping("/api/posts")
public class LikeController {

    @Autowired
    private LikeService likeService;

    /**
     * 🩵 Bir gönderiyi beğenme.
     * Kullanıcı daha önce beğenmişse hata döner.
     */
    @PostMapping("/{id}/likes")
    public void likePost(@PathVariable Long id, HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        likeService.likePost(currentUser, id);
    }

    /**
     * 💔 Beğeniyi kaldırma.
     * Kullanıcı gönderiyi beğenmemişse hata döner.
     */
    @DeleteMapping("/{id}/likes")
    public void unlikePost(@PathVariable Long id, HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        likeService.unlikePost(currentUser, id);
    }
}
