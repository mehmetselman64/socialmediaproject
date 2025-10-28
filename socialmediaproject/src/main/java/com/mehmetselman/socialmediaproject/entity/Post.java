package com.mehmetselman.socialmediaproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * ✅ Post entity: Kullanıcıların oluşturduğu gönderileri temsil eder.
 *
 * - Lazy yükleme hatalarını önlemek için JsonIgnoreProperties eklendi.
 * - Döngüsel JSON serialization hataları JsonIdentityInfo ile engellendi.
 */
@Entity
@Table(name = "post")
@Data
@ToString(exclude = {"comments", "likes", "author"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "comments", "likes"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Post ID (Primary Key)

    private String description; // Gönderinin açıklaması

    @Column(name = "image_url")
    private String imageUrl; // Görsel URL'si

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @JsonIgnoreProperties({"posts", "comments", "tokens", "hibernateLazyInitializer", "handler"})
    private User author; // Gönderiyi oluşturan kullanıcı

    private int viewCount = 0; // Görüntülenme sayısı
    private int likeCount = 0; // Beğeni sayısı

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"post", "hibernateLazyInitializer", "handler"})
    private List<Comment> comments; // Gönderiye yapılan yorumlar

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"post", "hibernateLazyInitializer", "handler"})
    private List<Like> likes; // Gönderiye yapılan beğeniler
}
