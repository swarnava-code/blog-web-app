package com.mukut.demo.repo;

import com.mukut.demo.entity.Post;
import com.mukut.demo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findByPostId(Integer postId);

    @Query(value = "select * from tags s where s.tags like %:keyword%", nativeQuery = true)
    List<Tag> findByTag(@Param("keyword") String keyword);
}
