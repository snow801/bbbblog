package com.wzn.ablog.article.feign;

import com.wzn.ablog.common.vo.AzResult;
import com.wzn.ablog.common.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(value = "azblog-admin", fallback = AdminFeignFallback.class)
public interface AdminFeign {

    @RequestMapping(value = "/admin/getUserame/{id}", method = RequestMethod.POST)
    AzResult getUsernameById(@PathVariable String id);
}
