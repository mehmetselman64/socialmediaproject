package com.mehmetselman.socialmediaproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mehmetselman.socialmediaproject.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.util.List;

/**
 * ✅ User entity: Sistemdeki kullanıcıları temsil eder.
 *
 * - Lazy yükleme problemlerine karşı @JsonIgnoreProperties eklendi.
 * - Döngüsel JSON serialization hatalarını önlemek için @JsonIdentityInfo eklendi.
 * - @ToString(exclude = ...) sayesinde sonsuz döngü log hataları engellendi.
 */
@Entity
@Table(name = "users")
@Data
@ToString(exclude = {"posts", "comments", "tokens"})
@JsonIgnoreProperties({"posts", "comments", "tokens", "hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Kullanıcı ID’si (Primary Key)

    @Column(unique = true, nullable = false)
    private String username; // Kullanıcının benzersiz kullanıcı adı

    @Column(nullable = false)
    private String password; // Şifre (hashlenmiş şekilde saklanmalı — ileride iyileştirilebilir)

    @Enumerated(EnumType.STRING)
    private Role role; // Kullanıcının rolü (ADMIN veya USER)

    // 🧩 Kullanıcının gönderileri
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    // 🧩 Kullanıcının yaptığı yorumlar
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    // 🧩 Kullanıcının token kayıtları
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens;
}
