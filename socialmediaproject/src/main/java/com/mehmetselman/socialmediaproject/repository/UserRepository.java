package com.mehmetselman.socialmediaproject.repository;

import com.mehmetselman.socialmediaproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
