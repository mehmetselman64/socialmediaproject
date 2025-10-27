package com.mehmetselman.socialmediaproject.controller;

import com.mehmetselman.socialmediaproject.entity.Comment;
import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/posts/{id}/comments")
    public Comment addComment(@PathVariable Long id, HttpServletRequest request, @RequestBody Map<String, String> body){
        User author = (User) request.getAttribute("currentUser");
        return commentService.addComment(id,author, body.get("text"));
    }

    @GetMapping("/posts/{id}/comments")
    public List<Comment> getComments(@PathVariable Long id){
        return commentService.getComments(id);
    }

    @DeleteMapping("/comments/{id}")
    public void deleteComment(@PathVariable Long id, HttpServletRequest request){
        User currentUser = (User) request.getAttribute("currentUser");
        commentService.deleteComment(id, currentUser);
    }

}
