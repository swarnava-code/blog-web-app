package com.mukut.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "comment")
    private String comment;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "post_id")
    private int postId;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Comment{" + "id=" + id + ", postId=" + postId + ", name='" + name + '\'' + ", email='" + email + '\'' + ", comment='" + comment + '\'' + ", createdAt='" + createdAt + '\'' + ", updatedAt='" + updatedAt + '\'' + '}';
    }
}
