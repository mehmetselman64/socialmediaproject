package com.mehmetselman.socialmediaproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "comment")
@Data
@ToString(exclude = {"author", "post"})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    // 🧩 Yorumu yazan kullanıcı
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonBackReference // Sonsuz JSON döngüsünü önler
    private User author;

    // 🧩 Yorumun ait olduğu post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonBackReference // Post -> Comment -> Post döngüsünü önler
    private Post post;
}
