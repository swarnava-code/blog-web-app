package com.mukut.demo.comparator;

import com.mukut.demo.entity.Post;

import java.util.Comparator;

public class PostAuthorComparatorAsc implements Comparator<Post> {

    @Override
    public int compare(Post firstPost, Post secondPost) {
        return firstPost.getAuthor().compareTo(secondPost.getAuthor());
    }
}
