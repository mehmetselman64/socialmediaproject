package com.mehmetselman.socialmediaproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

/**
 * ✅ Like entity: Kullanıcıların gönderilere yaptığı beğenileri temsil eder.
 *
 * - Aynı kullanıcı aynı post'u yalnızca bir kez beğenebilir (unique constraint).
 * - Lazy yükleme hataları ve döngüsel JSON hataları önlenmiştir.
 */
@Entity
@Table(
        name = "likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"})
)
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Beğeni ID'si

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"posts", "comments", "tokens", "hibernateLazyInitializer", "handler"})
    private User user; // Beğeniyi yapan kullanıcı

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties({"comments", "likes", "hibernateLazyInitializer", "handler"})
    private Post post; // Beğenilen gönderi
}
