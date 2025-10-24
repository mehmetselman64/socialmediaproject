package com.mehmetselman.socialmediaproject.service;

import com.mehmetselman.socialmediaproject.entity.Comment;
import com.mehmetselman.socialmediaproject.entity.Post;
import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.repository.CommentRepository;
import com.mehmetselman.socialmediaproject.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;


    //Yorum ekleme
    public Comment addComment(Long postId, User author, String text){
        Post post = postRepository.findById(postId).orElseThrow(() ->new RuntimeException("Gonderi bulunamadi!"));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setText(text);
        return commentRepository.save(comment);

    }

    //yorumlari goruntule
    public List<Comment> getComments(Long postId){

        Post post = postRepository.findById(postId).orElseThrow(()->new RuntimeException("Gonderi bulunamadi"));
        return commentRepository.findByPost(post);

    }

    //yorum sil
    public void deleteComment(Long id, User currentUser){
        Comment comment = commentRepository.findById(id).orElseThrow(()->new RuntimeException("Yorum bulunamadi!"));
        Post post = comment.getPost();
        if (!comment.getAuthor().equals(currentUser) && !post.getAuthor().equals(currentUser) && !"ADMIN".equals(currentUser.getRole())) throw new RuntimeException("Bu islem icin yetkiniz bulunmamaktadir");
        commentRepository.delete(comment);

    }

}
