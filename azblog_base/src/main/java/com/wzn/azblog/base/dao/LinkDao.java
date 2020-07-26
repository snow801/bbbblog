package com.wzn.azblog.base.dao;

import com.wzn.ablog.common.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LinkDao extends JpaRepository<Link, String>, JpaSpecificationExecutor<Link> {

    @Query(value = "SELECT * FROM link WHERE title LIKE CONCAT('%',:keywords,'%') LIMIT :page, :limit", nativeQuery = true)
    List<Link> search(@Param("keywords") String keywords, @Param("page") int page, @Param("limit") int limit);

    @Query(value = "UPDATE link SET title=:title,summary=:summary,url=:url WHERE id=:id", nativeQuery = true)
    @Modifying
    void update(@Param("title") String title, @Param("summary") String summary, @Param("url") String url, @Param("id") String id);
}
