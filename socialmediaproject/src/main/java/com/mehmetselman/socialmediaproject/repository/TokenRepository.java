package com.mehmetselman.socialmediaproject.repository;

import com.mehmetselman.socialmediaproject.entity.Token;
import com.mehmetselman.socialmediaproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);
    List<Token> findByUser(User user);
}
