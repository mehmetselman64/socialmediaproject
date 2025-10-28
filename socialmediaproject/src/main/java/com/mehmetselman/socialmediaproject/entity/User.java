package com.mehmetselman.socialmediaproject.entity;

import com.mehmetselman.socialmediaproject.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@ToString(exclude = {"posts", "comments"}) // sonsuz döngüleri önlemek için
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // 🧩 Kullanıcının gönderileri
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    // 🧩 Kullanıcının yorumları
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
