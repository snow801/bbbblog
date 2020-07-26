package com.wzn.azblog.base.web.controller;

import com.wzn.ablog.common.contants.AzContants;
import com.wzn.ablog.common.contants.AzStatus;
import com.wzn.ablog.common.entity.Link;
import com.wzn.ablog.common.vo.AzResult;
import com.wzn.ablog.common.vo.PageResult;
import com.wzn.azblog.base.service.LinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/link")
@Api(tags = {"友情链接"})
public class LinkController {

    @Autowired
    private LinkService linkService;

    @ApiOperation(value = "全部友情链接")
    @GetMapping
    public PageResult list(int page,int limit) {
        Page<Link> list = linkService.list(page, limit);
        return new PageResult(AzStatus.PAGE, AzContants.SUCCESS_MSG, list.getTotalElements(),
                list.getTotalPages(), list.getContent());
    }

    @ApiOperation(value = "添加友情链接")
    @PostMapping
    public AzResult add(@RequestBody Link link){
        linkService.add(link);
        return AzResult.ok();
    }

    @ApiOperation(value = "删除友情链接")
    @DeleteMapping("/{ids}")
    public AzResult del(@PathVariable String[] ids){
        linkService.del(ids);
        return AzResult.ok();
    }

    @ApiOperation(value = "查找友情链接")
    @GetMapping("/{id}")
    public AzResult findById(@PathVariable String id){
        Link link =  linkService.findById(id);
        return AzResult.ok().data(link);
    }

    @ApiOperation(value = "修改友情链接")
    @PutMapping
    public AzResult update(@RequestBody Link link){
        linkService.update(link);
        return AzResult.ok();
    }

    @ApiOperation(value = "搜索友情链接")
    @GetMapping("/search/{keywords}")
    public PageResult search(@PathVariable String keywords,int page,int limit){
        Page<Link> links = linkService.search(keywords, page, limit);
        return new PageResult(AzStatus.PAGE,AzContants.SUCCESS_MSG,
                links.getTotalElements(),links.getTotalPages(),links.getContent());
    }
}
