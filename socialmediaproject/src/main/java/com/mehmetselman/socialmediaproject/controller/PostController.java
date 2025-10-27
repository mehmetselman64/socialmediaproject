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

    public Post createPost(HttpServletRequest request, @RequestBody Map<String, String> body){
        User author = (User) request.getAttribute("currentUser");
        return postService.createPost(author, body.get("decription"), body.get("imageUrl") );

    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    @GetMapping
    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }

    @PutMapping("/{id}")
    public void updatePost(@PathVariable Long id, HttpServletRequest request,@RequestBody Map<String, String> body){
        User currentUser = (User) request.getAttribute("currentUser");
        postService.updatePost(id,currentUser, body.get("description"), body.get("imageUrl"));

    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id, HttpServletRequest request){
        User currentUser = (User) request.getAttribute("currentUser");
        postService.deletePost(id,currentUser);
    }

    @PostMapping("/{id}/view")
    public void viewPost(@PathVariable Long id){
        postService.viewPost(id);
    }

}
