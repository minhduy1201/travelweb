package com.travelweb.entity;

import javax.persistence.*;

@Entity
@Table(name = "favorites")
public class FavoriteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private NewsEntity post;

    public FavoriteEntity() {}

    public FavoriteEntity(UserEntity user, NewsEntity post) {
        this.user = user;
        this.post = post;
    }

    public Long getId() { return id; }
    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
    public NewsEntity getPost() { return post; }
    public void setPost(NewsEntity post) { this.post = post; }
}

