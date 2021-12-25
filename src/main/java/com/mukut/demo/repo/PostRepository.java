package com.mukut.demo.repo;

import com.mukut.demo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
//SELECT * FROM posts LIMIT 10 OFFSET 20
    @Transactional
    @Query(value = "select * from posts s where s.title like %:keyword% or s.author like %:keyword% or s.content like %:keyword% order by s.published_at LIMIT 3 OFFSET 0", nativeQuery = true)
    Set<Post> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "select * from posts s where s.author like %:keyword% order by s.published_at", nativeQuery = true)
    Set<Post> findByAuthor(@Param("keyword") String keyword);

    @Query(value = "select * from posts s order by s.published_at limit :limitNo offset :offsetNo", nativeQuery = true)
    Set<Post> pagination(@Param("limitNo") Integer limitNo, @Param("offsetNo") Integer offsetNo);

//    @Query(value = "select * from posts s where s.title like %:keyword% or s.author like %:keyword% or s.content like %:keyword% order by s.published_at LIMIT 3 OFFSET 0", nativeQuery = true)
//    Set<Post> findByKeyword(@Param("keyword") String keyword);

}
