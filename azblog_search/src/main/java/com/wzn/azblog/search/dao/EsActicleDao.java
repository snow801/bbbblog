package com.wzn.azblog.search.dao;

import com.wzn.azblog.search.entity.EsArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsActicleDao extends ElasticsearchRepository<EsArticle,String> {


}
