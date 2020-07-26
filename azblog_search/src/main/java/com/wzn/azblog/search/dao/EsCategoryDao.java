package com.wzn.azblog.search.dao;

import com.wzn.ablog.common.entity.Category;
import com.wzn.azblog.search.entity.EsArticle;
import com.wzn.azblog.search.entity.EsCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsCategoryDao extends ElasticsearchRepository<EsCategory,String> {

}
