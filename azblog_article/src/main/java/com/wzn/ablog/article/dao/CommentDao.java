package com.wzn.ablog.article.dao;

import com.wzn.ablog.common.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentDao extends JpaRepository<Comment,String>, JpaSpecificationExecutor<Comment> {

}
