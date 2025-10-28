package com.mehmetselman.socialmediaproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "token")
@Data
@ToString(exclude = "user")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    // 🧩 Her kullanıcı birden fazla token’a sahip olabilir (örneğin farklı cihazlardan giriş)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;
}
