package com.wzn.ablog.article.feign;

import com.wzn.ablog.common.contants.AzStatus;
import com.wzn.ablog.common.vo.AzResult;
import com.wzn.ablog.common.vo.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class AdminFeignFallback implements AdminFeign {
    @Override
    public AzResult getUsernameById(@PathVariable String id) {
        return AzResult.err(AzStatus.UNABLE_GET_USERNSAME);
    }
}
