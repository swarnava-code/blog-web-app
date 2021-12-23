package com.mukut.demo.repo;

import com.mukut.demo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    //Custom query
    @Query(value = "select * from posts s where s.title like %:keyword% or s.author like %:keyword%", nativeQuery = true)
    List<Post> findByKeyword(@Param("keyword") String keyword);
}
