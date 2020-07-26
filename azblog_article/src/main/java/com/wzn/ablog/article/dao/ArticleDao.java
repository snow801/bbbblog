package com.wzn.ablog.article.dao;

import com.wzn.ablog.common.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ArticleDao extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {

    @Query(value = "SELECT COUNT(*) FROM article WHERE TO_DAYS(update_time) = TO_DAYS(NOW());",
            nativeQuery = true)
    int todayCount();
}
