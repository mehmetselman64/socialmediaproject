package com.mehmetselman.socialmediaproject.controller;

import com.mehmetselman.socialmediaproject.entity.Post;
import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ✅ PostController
 *
 * Bu controller, REST API üzerinden post (gönderi) işlemlerini yönetir.
 *
 * İçerdiği işlemler:
 *  - Post oluşturma (create)
 *  - Tek bir postu getirme (read)
 *  - Tüm postları listeleme
 *  - Post güncelleme
 *  - Post silme
 *  - Post görüntülenme sayısını artırma
 *
 * Erişim yolu: `/api/posts`
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 🔹 POST /api/posts
     *
     * Yeni bir gönderi (post) oluşturur.
     *
     * İstek gövdesi (JSON):
     * ```json
     * {
     *   "description": "Bugün hava çok güzel!",
     *   "imageUrl": "https://example.com/image.jpg"
     * }
     * ```
     *
     * Token’dan gelen kullanıcı bilgisi (`AuthFilter`) `request` nesnesi üzerinden alınır.
     *
     * @param request AuthFilter tarafından eklenen currentUser bilgisini içerir
     * @param body description (açıklama) ve imageUrl değerlerini içerir
     * @return Oluşturulan Post nesnesi
     */
    @PostMapping
    public Post createPost(HttpServletRequest request, @RequestBody Map<String, String> body) {
        User author = (User) request.getAttribute("currentUser");
        return postService.createPost(author, body.get("description"), body.get("imageUrl"));
    }

    /**
     * 🔹 GET /api/posts/{id}
     *
     * Belirli bir post’u ID’sine göre getirir.
     *
     * Örnek:
     * ```
     * GET http://localhost:8080/api/posts/5
     * ```
     *
     * @param id Post ID’si
     * @return Post nesnesi
     */
    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    /**
     * 🔹 GET /api/posts
     *
     * Tüm postları getirir.
     *
     * Örnek:
     * ```
     * GET http://localhost:8080/api/posts
     * ```
     *
     * @return Tüm postların listesi
     */
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    /**
     * 🔹 PUT /api/posts/{id}
     *
     * Belirli bir postu günceller.
     *
     * - Sadece gönderinin sahibi (author) güncelleme yapabilir.
     *
     * İstek gövdesi (JSON):
     * ```json
     * {
     *   "description": "Güncellenmiş açıklama",
     *   "imageUrl": "https://example.com/new-image.jpg"
     * }
     * ```
     *
     * @param id Güncellenecek post ID’si
     * @param request Token’dan gelen kullanıcıyı içerir
     * @param body Yeni description ve imageUrl değerlerini içerir
     */
    @PutMapping("/{id}")
    public void updatePost(@PathVariable Long id, HttpServletRequest request, @RequestBody Map<String, String> body) {
        User currentUser = (User) request.getAttribute("currentUser");
        postService.updatePost(id, currentUser, body.get("description"), body.get("imageUrl"));
    }

    /**
     * 🔹 DELETE /api/posts/{id}
     *
     * Belirli bir postu siler.
     *
     * - Sadece post’un sahibi veya ADMIN rolündeki kullanıcı silebilir.
     *
     * Örnek:
     * ```
     * DELETE http://localhost:8080/api/posts/7
     * ```
     *
     * @param id Silinecek post’un ID’si
     * @param request Token’dan gelen kullanıcıyı içerir
     */
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id, HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        postService.deletePost(id, currentUser);
    }

    /**
     * 🔹 POST /api/posts/{id}/view
     *
     * Post’un görüntülenme sayısını 1 artırır.
     *
     * Bu endpoint, kullanıcı girişine gerek olmadan da kullanılabilir.
     *
     * Örnek:
     * ```
     * POST http://localhost:8080/api/posts/3/view
     * ```
     *
     * @param id Görüntülenen post ID’si
     */
    @PostMapping("/{id}/view")
    public void viewPost(@PathVariable Long id) {
        postService.viewPost(id);
    }
}
