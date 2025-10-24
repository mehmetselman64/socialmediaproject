package com.mehmetselman.socialmediaproject.repository;

import com.mehmetselman.socialmediaproject.entity.Like;
import com.mehmetselman.socialmediaproject.entity.Post;
import com.mehmetselman.socialmediaproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like,Long> {

    Like findByUserAndPost(User user, Post post); //bir kullanıcının belirli bir postu beğenip beğenmediğini kontrol etmek için kullanilir

}
