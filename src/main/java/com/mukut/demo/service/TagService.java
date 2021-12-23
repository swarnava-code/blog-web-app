package com.mukut.demo.service;

import com.mukut.demo.entity.Post;
import com.mukut.demo.entity.Tag;
import com.mukut.demo.repo.PostRepository;
import com.mukut.demo.repo.TagRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TagService {

    public void saveEachTag(TagRepository tagRepository, String string, Integer id, String createdAt, String updatedAt){
        //Tag tag = new Tag();
        string = string.replaceAll(" ", "");
        List<String> tagList = Arrays.asList(string.split(","));
        for(String tag : tagList){
            Tag tagEntity = new Tag();
            tagEntity.setPostId(id);
            tagEntity.setCreatedAt(createdAt);
            tagEntity.setUpdatedAt(updatedAt);
            tagEntity.setName(tag);
            tagRepository.save(tagEntity);
            System.out.println(tagEntity.toString());
        }
    }

    public List<Post> findAll(PostRepository postsRepository){
        return postsRepository.findAll();
    }

    public List<Tag> findByPostId(TagRepository tagRepository, Integer postId){
        return tagRepository.findByPostId(postId);
    }
}
