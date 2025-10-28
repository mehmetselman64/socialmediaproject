package com.mehmetselman.socialmediaproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "post")
@Data
@ToString(exclude = {"author", "comments", "likes"})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description; // Kullanıcının yazdığı açıklama

    @Column(name = "image_url")
    private String imageUrl;

    // 🧩 Post'u oluşturan kullanıcı
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonBackReference // JSON sonsuz döngü engeli
    private User author;

    private int viewCount = 0; // Görüntüleme sayacı
    private int likeCount = 0; // Beğeni sayacı

    // 🧩 Post’a ait yorumlar
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> comments;

    // 🧩 Post’a ait beğeniler
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Like> likes;
}
