package com.wzn.azblog.search.service;

import com.netflix.discovery.converters.Auto;
import com.wzn.ablog.common.entity.Category;
import com.wzn.azblog.search.dao.EsActicleDao;
import com.wzn.azblog.search.dao.EsCategoryDao;
import com.wzn.azblog.search.dao.EsCommentDao;
import com.wzn.azblog.search.entity.EsArticle;
import com.wzn.azblog.search.entity.EsCategory;
import com.wzn.azblog.search.entity.EsComment;
import com.wzn.azblog.search.feign.SearchFeign;
import com.wzn.azblog.search.quartz.EsQuartz;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SearchService {

    @Autowired
    private EsActicleDao esActicleDao;

    @Autowired
    private EsCategoryDao esCategoryDao;

    @Autowired
    private EsCommentDao esCommentDao;


    //搜索文章
    public Page<EsArticle> searchArticle(String keywords, Integer page, Integer limit) {
        //should：或者
       // fuzzyQuery：模糊查询
        QueryBuilder queryBuilder= QueryBuilders.boolQuery()
                .should(QueryBuilders.fuzzyQuery("intro",keywords))
                .should(QueryBuilders.fuzzyQuery("nickname",keywords))
                .should(QueryBuilders.fuzzyQuery("title",keywords))
                .should(QueryBuilders.fuzzyQuery("content",keywords));

        //分页
        PageRequest pageRequest = PageRequest.of(page-1, limit);
        Page<EsArticle> pageInfo = esActicleDao.search(queryBuilder, pageRequest);
        return pageInfo;
    }

    //搜索分类
    public Page<EsCategory> searchCategroy(String keywords, Integer page, Integer limit){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.fuzzyQuery("name", keywords));
        PageRequest pageRequest = PageRequest.of(page - 1, limit);
        Page<EsCategory> pageInfo = esCategoryDao.search(queryBuilder, pageRequest);
        return pageInfo;
    }

    //搜索评论
    public Page<EsComment> searchComment(String keywords, Integer page, Integer limit){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.fuzzyQuery("articleId", keywords))
                .should(QueryBuilders.fuzzyQuery("username", keywords))
                .should(QueryBuilders.fuzzyQuery("content", keywords));

        PageRequest pageRequest = PageRequest.of(page - 1, limit);
        Page<EsComment> searchInfo = esCommentDao.search(queryBuilder, pageRequest);
        return searchInfo;
    }
}
