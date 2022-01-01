package com.mukut.demo.comparator;

import com.mukut.demo.entity.Post;

import java.util.Comparator;

public class PostPublishedAtComparatorDesc implements Comparator<Post> {

    @Override
    public int compare(Post firstPost, Post secondPost) {
        return secondPost.getPublishedAt().compareTo(firstPost.getPublishedAt());
    }
}
