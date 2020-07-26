package com.wzn.ablog.article.service;

import com.wzn.ablog.article.dao.ArticleDao;
import com.wzn.ablog.article.utils.MsgSender;
import com.wzn.ablog.common.entity.Article;
import com.wzn.ablog.common.utils.IdWorker;
import com.wzn.ablog.common.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MsgSender msgSender;

    private Integer totaPage;

    //加载列表
    public Page<Article> list(Integer page, Integer limit, String userId) {
        Page<Article> list = (Page<Article>) redisTemplate.opsForValue().get("articles" + userId + page);
        if (list == null) {
            list = articleDao.findAll(PageRequest.of(page - 1, limit, Sort.by("updateTime").descending()));
            totaPage = list.getTotalPages();
            redisTemplate.opsForValue().set("articleTotaPage", totaPage);
            redisTemplate.opsForValue().set("articles" + userId + page, list);
        } else {
            return list;
        }
        return list;
    }

    //删除
    public void del(String[] ids, String uId) {
        msgSender.send("syncIndex");
        for (String id : ids) {
            articleDao.deleteById(id);
        }
        totaPage = (Integer) redisTemplate.opsForValue().get("articleTotaPage");
        RedisUtils.clearRedis(totaPage, uId, redisTemplate);
    }

    //添加
    public void add(Article reqArticle, String username, String userId) {
        msgSender.send("syncIndex");
        String isOriginal = reqArticle.getIs_original();
        String recommended = reqArticle.getRecommended();
        String status = reqArticle.getStatus();
        Article article = new Article()
                .setId(String.valueOf(idWorker.nextId()))
                .setTitle(reqArticle.getTitle())
                .setIntro(reqArticle.getIntro())
                .setCategory_id(reqArticle.getCategory_id())
                .setIs_original(isOriginal == null ? "0" : isOriginal.equals("on") ? "1" : "0")
                .setRecommended(recommended == null ? "0" : recommended.equals("on") ? "1" : "0")
                .setStatus(status == null ? "0" : status.equals("on") ? "1" : "0")//1通过 0待审
                .setContent(reqArticle.getContent())
                .setCreate_time(new Date())
                .setUpdateTime(new Date())
                .setUsername(username);
        articleDao.save(article);
        totaPage = (Integer) redisTemplate.opsForValue().get("articleTotaPage");
        RedisUtils.clearRedis(totaPage, userId, redisTemplate);//清除缓存
    }

    //更新
    public void update(Article oldArticle, String username, String userId) {
        msgSender.send("syncIndex");
        String isOriginal = oldArticle.getIs_original();
        String recommended = oldArticle.getRecommended();
        String status = oldArticle.getStatus();
        Article newArticle = new Article(
                oldArticle.getId(),
                oldArticle.getTitle(),
                oldArticle.getIntro(),
                oldArticle.getContent(),
                username,
                null, oldArticle.getCategory_id(),
                null, null, null, null,
                status == null ? "0" : status.equals("on") ? "1" : "0",
                null,
                new Date(),
                isOriginal == null ? "0" : isOriginal.equals("on") ? "1" : "0",
                recommended == null ? "0" : recommended.equals("on") ? "1" : "0"
        );
        articleDao.save(newArticle);
        totaPage = (Integer) redisTemplate.opsForValue().get("articleTotaPage");
        RedisUtils.clearRedis(totaPage, userId, redisTemplate);
    }

    //查找全部
    public List<Article> findAll() {
        return articleDao.findAll();
    }

    //根据id查找文章
    public Article findById(String id) {
        Optional<Article> article = articleDao.findById(id);
        return article.get();
    }

    //统计今天的文章数量
    public int todayCount(){
        return articleDao.todayCount();
    }
}
