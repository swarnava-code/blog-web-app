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
//SELECT * FROM posts LIMIT 10 OFFSET 20 //order by s.published_at
    // select u from posts u where lower(u.author) like lower(concat('%', :keyword,'%'))
    @Transactional
    @Query(value = "select * from posts s where lower(s.title) like lower(concat('%', :keyword,'%')) or lower(s.author) like lower(concat('%', :keyword,'%')) or lower(s.content) like lower(concat('%', :keyword,'%')) order by s.published_at", nativeQuery = true)
    Set<Post> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "select * from posts u where lower(u.author) like lower(concat('%', :keyword,'%')) ", nativeQuery = true)
    Set<Post> findByAuthor(@Param("keyword") String keyword);

    @Query(value = "select * from posts s order by s.published_at offset :offsetNo limit :limitNo", nativeQuery = true)
    Set<Post> pagination(@Param("limitNo") Integer limitNo, @Param("offsetNo") Integer offsetNo);

    //    @Query(value = "select * from posts s where s.title like %:keyword% or s.author like %:keyword% or s.content like %:keyword% order by s.published_at LIMIT 3 OFFSET 0", nativeQuery = true)
    //    Set<Post> findByKeyword(@Param("keyword") String keyword);

}
