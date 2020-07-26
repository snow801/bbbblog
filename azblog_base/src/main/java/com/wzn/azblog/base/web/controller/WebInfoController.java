package com.wzn.azblog.base.web.controller;

import com.wzn.ablog.common.entity.WebInfo;
import com.wzn.ablog.common.vo.AzResult;
import com.wzn.ablog.common.vo.Result;
import com.wzn.azblog.base.service.WebInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webInfo")
@Api(tags = {"网站信息"})
public class WebInfoController {

    @Autowired
    private WebInfoService webInfoService;

    @ApiOperation(value = "显示网站信息")
    @GetMapping
    public AzResult showInfo(){
        List<WebInfo> webInfo = webInfoService.showInfo();
        return AzResult.ok().data(webInfo);
    }

    @ApiOperation(value = "修改网站信息")
    @PutMapping("/{content}")
    public AzResult updateInfo(@PathVariable String content){
        webInfoService.updateInfo(content);
        return AzResult.ok();
    }
}
