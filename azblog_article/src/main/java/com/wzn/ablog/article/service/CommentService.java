package com.wzn.ablog.article.service;

import com.netflix.discovery.converters.Auto;
import com.wzn.ablog.article.dao.CommentDao;
import com.wzn.ablog.common.entity.Comment;
import io.lettuce.core.Limit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    //查找全部
    public Page<Comment> list(int page, int limit){
        Page<Comment> pageInfo = commentDao.findAll(PageRequest.of(page - 1, limit,
                Sort.by("updateTime").descending()));
        return pageInfo;
    }

    //根据id查找
    public Comment findById(String id){
        Comment comment = commentDao.findById(id).get();
        return comment;
    }

    //添加
    public void add(Comment comment){
        commentDao.save(comment);
    }

    //更新
    public void update(Comment comment){
        commentDao.save(comment);
    }

    //根据id删除
    public void del(String[] ids){
        for(String id : ids){
            commentDao.deleteById(id);
        }
    }

    //查找全部b不分页
    public List<Comment> all() {
        return commentDao.findAll();
    }
}
