package com.mehmetselman.socialmediaproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ✅ Token entity: JWT tabanlı oturum yönetimi için kullanılan access token'ları temsil eder.
 *
 * - Her kullanıcı birden fazla token’a sahip olabilir.
 * - Token süresi dolduğunda AuthFilter kontrolüyle geçersiz hale gelir.
 * - Lazy yükleme hataları ve JSON serialization problemleri giderildi.
 */
@Entity
@Table(name = "token")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Token ID

    @Column(nullable = false, unique = true)
    private String token; // JWT değeri

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"posts", "comments", "tokens", "hibernateLazyInitializer", "handler"})
    private User user; // Token'ın ait olduğu kullanıcı

    private LocalDateTime expiryDate; // Token geçerlilik süresi
}
