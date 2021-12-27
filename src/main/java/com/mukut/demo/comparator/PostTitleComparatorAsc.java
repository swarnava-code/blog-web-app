package com.mukut.demo.comparator;

import com.mukut.demo.entity.Post;

import java.util.Comparator;

public class PostTitleComparatorAsc implements Comparator<Post> {

    @Override
    public int compare(Post firstPost, Post secondPost) {
        return firstPost.getTitle().compareTo(secondPost.getTitle());
    }
}
