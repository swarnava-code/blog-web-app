package com.mukut.demo.comparator;

import com.mukut.demo.entity.Post;

import java.util.Comparator;

public class PostTitleComparatorDesc implements Comparator<Post> {

    @Override
    public int compare(Post firstPost, Post secondPost) {
        return secondPost.getTitle().compareTo(firstPost.getTitle());
    }
}
