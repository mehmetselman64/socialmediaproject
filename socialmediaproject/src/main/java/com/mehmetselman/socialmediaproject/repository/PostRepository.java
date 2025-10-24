package com.mehmetselman.socialmediaproject.repository;

import com.mehmetselman.socialmediaproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
