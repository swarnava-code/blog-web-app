package com.mukut.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "post_id")
    private int postId;

    @Override
    public String toString() {
        return "Tag{" + "id=" + id + ", postId=" + postId + ", name='" + name + '\'' + ", created_at='" + createdAt + '\'' + ", updated_at='" + updatedAt + '\'' + '}';
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @Column(name = "name")
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
