package com.mukut.demo.repo;

import com.mukut.demo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Transactional
    @Query(value = "select * from posts s where lower(s.title) like lower(concat('%', :keyword,'%')) or lower(s.author) like lower(concat('%', :keyword,'%')) or lower(s.content) like lower(concat('%', :keyword,'%')) ", nativeQuery = true)
    Set<Post> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "select * from posts u where lower(u.author) like lower(concat('%', :keyword,'%')) ", nativeQuery = true)
    Set<Post> findByAuthor(@Param("keyword") String keyword);

    @Query(value = "select * from posts u where u.published_at like concat('%', :date,'%') ", nativeQuery = true)
    Set<Post> findByDate(@Param("date") String date);
}
