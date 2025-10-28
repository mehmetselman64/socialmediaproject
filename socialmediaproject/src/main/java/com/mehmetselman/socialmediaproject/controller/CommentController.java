package com.mehmetselman.socialmediaproject.controller;

import com.mehmetselman.socialmediaproject.entity.Comment;
import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * ✅ CommentController (Nested yapı: /api/posts/{postId}/comments)
 *
 * Bu controller, belirli bir gönderi (Post) altındaki yorumları yönetir:
 *  - Yorum ekleme (POST)
 *  - Yorum listeleme (GET)
 *  - Yorum silme (DELETE)
 *
 * 🔒 Tüm işlemler JWT doğrulaması gerektirir (AuthFilter tarafından yönetilir).
 */
@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 🧩 Belirli bir gönderiye ait TÜM yorumları listeler.
     * Endpoint: GET /api/posts/{postId}/comments
     *
     * @param postId Gönderi ID'si
     * @return List<Comment> → İlgili gönderiye ait yorumlar
     */
    @GetMapping
    public List<Comment> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }

    /**
     * 🧩 Yeni bir yorum oluşturur.
     * Endpoint: POST /api/posts/{postId}/comments
     *
     * @param postId Gönderi ID'si
     * @param request JWT’den gelen kullanıcı bilgisi
     * @param body JSON gövdesinde "text" alanı beklenir
     * @return Oluşturulan Comment nesnesi
     */
    @PostMapping
    public Comment addComment(@PathVariable Long postId,
                              HttpServletRequest request,
                              @RequestBody Map<String, String> body) {

        User currentUser = (User) request.getAttribute("currentUser");
        if (currentUser == null) {
            throw new RuntimeException("Kullanıcı doğrulanamadı! Lütfen giriş yapınız.");
        }

        String text = body.get("text");
        if (text == null || text.trim().isEmpty()) {
            throw new RuntimeException("'text' alanı boş olamaz!");
        }

        return commentService.addComment(currentUser, postId, text.trim());
    }

    /**
     * 🧩 Belirli bir yorumu siler.
     * Endpoint: DELETE /api/posts/{postId}/comments/{commentId}
     *
     * Sadece yorumu oluşturan kullanıcı veya ADMIN rolündeki kullanıcı silebilir.
     *
     * @param postId Gönderi ID'si (kontrol amaçlı)
     * @param commentId Silinecek yorum ID'si
     * @param request JWT’den gelen kullanıcı bilgisi
     */
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long postId,
                              @PathVariable Long commentId,
                              HttpServletRequest request) {

        User currentUser = (User) request.getAttribute("currentUser");
        if (currentUser == null) {
            throw new RuntimeException("Kullanıcı doğrulanamadı! Lütfen giriş yapınız.");
        }

        commentService.deleteComment(commentId, currentUser);
    }
}
