package com.mehmetselman.socialmediaproject.controller;

import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{id}/likes")
public class LikeController {

    @Autowired
    private PostService postService;

    @PostMapping
    public void like(@PathVariable Long id, HttpServletRequest request){
        User user = (User) request.getAttribute("currentUser");
        postService.likePost(id,user);
    }

    @DeleteMapping
    public void unlike(@PathVariable Long id, HttpServletRequest request){
        User user = (User) request.getAttribute("currentUser");
        postService.unlikePost(id,user);
    }

}
