package com.wzn.azblog.Statistics.web.controller;

import com.wzn.ablog.common.vo.AzResult;
import com.wzn.azblog.Statistics.service.VisitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
@Api(tags = {"数据统计"})
public class DataController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private VisitorService visitorService;

    @ApiOperation(value = "在线人数")
    @GetMapping("/onlineCount")
    public AzResult onlineCount(){
        Object onlineCount = redisTemplate.opsForValue().get("onlineCount");
        return AzResult.ok().data(onlineCount);
    }

    @ApiOperation(value = "获取访客数量")
    @GetMapping("/getVisitorCount")
    public AzResult getVisitorCount(){
        long visitorCount = visitorService.getVisitorCount();
        return AzResult.ok().data(visitorCount);
    }
}
