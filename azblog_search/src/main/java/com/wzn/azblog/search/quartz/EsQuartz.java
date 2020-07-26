package com.wzn.azblog.search.quartz;

import com.alibaba.fastjson.JSON;
import com.wzn.ablog.common.entity.Category;
import com.wzn.ablog.common.entity.Comment;
import com.wzn.ablog.common.vo.Result;
import com.wzn.azblog.search.dao.EsActicleDao;
import com.wzn.azblog.search.dao.EsCategoryDao;
import com.wzn.azblog.search.dao.EsCommentDao;
import com.wzn.azblog.search.entity.Article;
import com.wzn.azblog.search.entity.EsArticle;
import com.wzn.azblog.search.entity.EsCategory;
import com.wzn.azblog.search.entity.EsComment;
import com.wzn.azblog.search.feign.SearchFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class EsQuartz {

    @Autowired
    private SearchFeign searchFeign;

    @Autowired
    private EsActicleDao esActicleDao;

    @Autowired
    private EsCategoryDao esCategoryDao;

    @Autowired
    private EsCommentDao esCommentDao;

    @Scheduled(cron = "0 0 0/6 * * ? ") // 每6小时执行一次
    public void syncArticle() {
        esActicleDao.deleteAll();
        log.debug("同步索引库");
        Result all = searchFeign.findAll();
        String s = JSON.toJSONString(all.getData());
        List<Article> articles = JSON.parseArray(s, Article.class);
        articles.stream().forEach(item -> {
            EsArticle esArticle = new EsArticle()
                    .setContent(item.getContent())
                    .setId(item.getId())
                    .setIntro(item.getIntro())
                    .setNickname(item.getNickname())
                    .setTitle(item.getTitle())
                    .setStatus(item.getStatus())
                    .setUpdateTime(item.getUpdate_time())
                    .setCategory_id(item.getCategory_id())
                    .setIs_original(item.getIs_original())
                    .setRecommended(item.getRecommended());
            esActicleDao.save(esArticle);
        });
    }

    @Scheduled(cron = "* * 0/6 * * ? ")
    public void sycnCategriy() {
        System.out.println("同步分类索引");
        List<Category> list = searchFeign.all();
        esCategoryDao.deleteAll();
        list.stream().forEach(category -> {
            EsCategory esCategory = new EsCategory()
                    .setId(category.getId())
                    .setName(category.getName());
            esCategoryDao.save(esCategory);
        });
    }

    @Scheduled(cron = "* * 0/6 * * ? ")
    public void sycnComment() {
        System.out.println("同步评论索引");
        List<Comment> list = searchFeign.allComment();
        esCommentDao.deleteAll();
        list.stream().forEach(comment -> {
            EsComment esComment = new EsComment()
                    .setId(comment.getId())
                    .setUsername(comment.getUsername())
                    .setContent(comment.getContent())
                    .setAvatar(comment.getAvatar());
            esCommentDao.save(esComment);
        });
    }
}
