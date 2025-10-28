package com.mehmetselman.socialmediaproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

/**
 * ✅ Comment entity: Gönderilere yapılan yorumları temsil eder.
 *
 * - Lazy yükleme hatalarını önlemek için JsonIgnoreProperties eklendi.
 * - Döngüsel JSON serialization hataları JsonIdentityInfo ile engellendi.
 */
@Entity
@Table(name = "comment")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Yorum ID

    private String text; // Yorum içeriği

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @JsonIgnoreProperties({"posts", "comments", "tokens", "hibernateLazyInitializer", "handler"})
    private User author; // Yorumu yapan kullanıcı

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties({"comments", "likes", "hibernateLazyInitializer", "handler"})
    private Post post; // Yorumun ait olduğu gönderi
}
