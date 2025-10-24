package com.mehmetselman.socialmediaproject.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.util.List;

@Entity
@Table(name = "post")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)  //1000 karakterli aciklama
    private String description; //Kullanıcının yazdığı açıklama

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    private User author; //Post’u oluşturan kullanıcı

    private int viewCount = 0; //Goruntuleme sayaci

    private int likeCount = 0; //Like sayaci

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<Like> likes;



}
