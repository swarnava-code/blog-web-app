package com.mukut.demo.model;

public class SortModel {
    private Boolean publishedAt;
    private Boolean excerpt;
    private Boolean author;
    private Boolean title;
    private Boolean asc;
    private Boolean desc;

    public Boolean getAsc() {
        return asc;
    }

    public void setAsc(Boolean asc) {
        this.asc = asc;
    }

    public Boolean getDesc() {
        return desc;
    }

    public void setDesc(Boolean desc) {
        this.desc = desc;
    }

    public Boolean getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Boolean publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Boolean getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(Boolean excerpt) {
        this.excerpt = excerpt;
    }

    public Boolean getAuthor() {
        return author;
    }

    public void setAuthor(Boolean author) {
        this.author = author;
    }

    public Boolean getTitle() {
        return title;
    }

    public void setTitle(Boolean title) {
        this.title = title;
    }
}
