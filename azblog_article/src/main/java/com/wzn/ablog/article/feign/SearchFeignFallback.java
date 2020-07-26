package com.wzn.ablog.article.feign;

import com.wzn.ablog.common.contants.AzContants;
import com.wzn.ablog.common.contants.AzStatus;
import com.wzn.ablog.common.vo.PageResult;
import org.springframework.stereotype.Component;

@Component
public class SearchFeignFallback implements SearchFeign {
    @Override
    public PageResult search(String keywords, Integer page, Integer limit) {
        return new PageResult(AzStatus.ERR,AzContants.ERR_SEARCH_SERVICE);
    }

    @Override
    public PageResult searchCategroy(String keywords, Integer page, Integer limit) {
        return new PageResult(AzStatus.ERR, AzContants.ERR_SEARCH_SERVICE);
    }

    @Override
    public PageResult searchComment(String keywords, Integer page, Integer limit) {
        return new PageResult(AzStatus.ERR,AzContants.ERR_SEARCH_SERVICE);
    }
}
