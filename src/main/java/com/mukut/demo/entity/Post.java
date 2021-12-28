package com.mukut.demo.entity;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name="posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name="title")
    private String title;

    @Column(name = "excerpt")
    private String excerpt;

    //@Lob
    @Column(name = "content")
    private String content;

    @Column(name = "author")
    private String author;

    @Column(name = "published_at")
    private String published_at;

    @Column(name = "is_published")
    boolean is_published;

    @Column(name = "created_at")
    String created_at;

    @Column(name = "updated_at")
    String updated_at;




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isIs_published() {
        return is_published;
    }

    @Override
    public String toString() {
        return "Posts{" +
                ", title='" + title + '\'' +
                ", excerpt='" + excerpt + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", published_at='" + published_at + '\'' +
                ", is_published='" + is_published + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
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

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public void setIs_published(boolean is_published) {
        this.is_published = is_published;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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

    public String getPublished_at() {
        return published_at;
    }

    public boolean getIs_published() {
        return is_published;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

}
