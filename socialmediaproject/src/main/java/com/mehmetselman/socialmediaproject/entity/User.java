package com.mehmetselman.socialmediaproject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id otomatik artiyor
    private Long id;

    @Column(unique = true, nullable = false) //benzersiz olmali ve bos gecilemez
    private String username;

    @Column(nullable = false) //bos gecilemez
    private String password;

    @Enumerated(EnumType.STRING) //enum degerlerini string olarak saklar(admin - user)
    private String role;

}
