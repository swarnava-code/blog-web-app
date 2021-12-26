package com.mukut.demo.comparator;

import com.mukut.demo.entity.Post;

import java.util.Comparator;

public class PostAuthorComparatorDesc implements Comparator<Post> {

    @Override
    public int compare(Post firstPost, Post secondPost) {
        return secondPost.getAuthor().compareTo(firstPost.getAuthor());
    }
}
