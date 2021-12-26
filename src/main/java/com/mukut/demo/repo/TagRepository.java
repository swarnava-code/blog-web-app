package com.mukut.demo.repo;

import com.mukut.demo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query(value = "select * from tags s where lower(s.name) like  lower(concat('%', :tag,'%'))", nativeQuery = true)
    List<Tag> findByTag(@Param("tag") String tag);

    List<Tag> findByPostId(Integer postId);
}
