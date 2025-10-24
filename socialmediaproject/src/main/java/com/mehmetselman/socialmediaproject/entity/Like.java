package com.mehmetselman.socialmediaproject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "like", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))
@Data
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Post post;

}
