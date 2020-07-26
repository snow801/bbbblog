package com.wzn.azblog.search.dao;

import com.wzn.azblog.search.entity.EsCategory;
import com.wzn.azblog.search.entity.EsComment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsCommentDao extends ElasticsearchRepository<EsComment,String> {
    
}
