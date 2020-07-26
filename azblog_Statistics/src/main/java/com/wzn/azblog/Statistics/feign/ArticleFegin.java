package com.wzn.azblog.Statistics.feign;

import com.wzn.ablog.common.vo.AzResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "azblog-article")
public interface ArticleFegin {

    @RequestMapping(value = "article/todayCount",method = RequestMethod.GET)
    AzResult todayCount();
}
