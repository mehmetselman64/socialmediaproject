package com.mehmetselman.socialmediaproject.controller;

import com.mehmetselman.socialmediaproject.entity.Post;
import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // ✅ Yeni gönderi oluşturma (CREATE)
    @PostMapping
    public Post createPost(HttpServletRequest request, @RequestBody Map<String, String> body) {
        User author = (User) request.getAttribute("currentUser");
        return postService.createPost(author, body.get("description"), body.get("imageUrl"));
    }

    // ✅ Tek gönderiyi ID ile getirme (READ)
    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // ✅ Tüm gönderileri listeleme (READ ALL)
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    // ✅ Gönderi güncelleme (UPDATE)
    @PutMapping("/{id}")
    public void updatePost(@PathVariable Long id,
                           HttpServletRequest request,
                           @RequestBody Map<String, String> body) {
        User currentUser = (User) request.getAttribute("currentUser");
        postService.updatePost(id, currentUser, body.get("description"), body.get("imageUrl"));
    }

    // ✅ Gönderi silme (DELETE)
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id, HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        postService.deletePost(id, currentUser);
    }

    // ✅ Gönderi görüntülenme sayısını artırma
    @PostMapping("/{id}/view")
    public void viewPost(@PathVariable Long id) {
        postService.viewPost(id);
    }
}
