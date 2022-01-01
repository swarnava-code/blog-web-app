package com.mukut.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "excerpt")
    private String excerpt;

    //@Lob
    @Column(name = "content")
    private String content;

    @Column(name = "author")
    private String author;

    @Column(name = "published_at")
    private String publishedAt;

    @Column(name = "is_published")
    boolean isPublished;

    @Column(name = "created_at")
    String createdAt;

    @Column(name = "updated_at")
    String updatedAt;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isPublished() {
        return isPublished;
    }

    @Override
    public String toString() {
        return "Posts{" + ", title='" + title + '\'' + ", excerpt='" + excerpt + '\'' + ", content='" + content + '\'' + ", author='" + author + '\'' + ", published_at='" + publishedAt + '\'' + ", is_published='" + isPublished + '\'' + ", created_at='" + createdAt + '\'' + ", updated_at='" + updatedAt + '\'' + '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setPublished(boolean published) {
        this.isPublished = published;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public boolean getIs_published() {
        return isPublished;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

}
