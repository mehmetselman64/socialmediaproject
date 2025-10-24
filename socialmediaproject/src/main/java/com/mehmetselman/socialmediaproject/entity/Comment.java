package com.mehmetselman.socialmediaproject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comment")
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000) //1000 karakterli yorum
    private String text;

    @ManyToOne
    private User author;

    @ManyToOne
    private Post post;





}
