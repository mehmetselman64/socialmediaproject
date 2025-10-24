package com.mehmetselman.socialmediaproject.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "token")
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne //birden cok tokeni olacagi icin
    private User user;

    private LocalDateTime expiryDate;

}
