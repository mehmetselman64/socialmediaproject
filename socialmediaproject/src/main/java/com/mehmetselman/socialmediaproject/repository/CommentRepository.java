package com.mehmetselman.socialmediaproject.repository;

import com.mehmetselman.socialmediaproject.entity.Comment;
import com.mehmetselman.socialmediaproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post); //bir postun tüm yorumlarını listeleme
}
