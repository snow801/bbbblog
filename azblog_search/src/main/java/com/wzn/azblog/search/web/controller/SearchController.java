package com.wzn.azblog.search.web.controller;

import com.wzn.ablog.common.contants.AzContants;
import com.wzn.ablog.common.contants.AzStatus;
import com.wzn.ablog.common.vo.PageResult;
import com.wzn.azblog.search.entity.EsArticle;
import com.wzn.azblog.search.entity.EsCategory;
import com.wzn.azblog.search.entity.EsComment;
import com.wzn.azblog.search.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@Api(tags = {"搜索模块"})
public class SearchController {

    @Autowired
    private SearchService articleService;

    @ApiOperation(value = "文章搜索")
    @GetMapping("/{keywords}/{page}/{limit}")
    public PageResult search(@PathVariable("keywords") String keywords, @PathVariable Integer page, @PathVariable Integer limit){

        Page<EsArticle> pageInfo = articleService.searchArticle(keywords, page, limit);
        return new PageResult(AzStatus.PAGE, AzContants.SUCCESS_MSG,pageInfo.getTotalElements(),
                pageInfo.getTotalPages(),pageInfo.getContent());
    }

    @ApiOperation(value = "分类搜索")
    @GetMapping("/categroy/{keywords}/{page}/{limit}")
    public PageResult searchCategroy(@PathVariable("keywords") String keywords, @PathVariable Integer page, @PathVariable Integer limit){
        Page<EsCategory> pageInfo = articleService.searchCategroy(keywords, page, limit);
        return new PageResult(AzStatus.PAGE,AzContants.SUCCESS_MSG,pageInfo.getTotalElements(),
                pageInfo.getTotalPages(),pageInfo.getContent());
    }

    @ApiOperation(value = "评论搜索")
    @GetMapping("/comment/{keywords}/{page}/{limit}")
    public PageResult searchComment(@PathVariable("keywords") String keywords, @PathVariable Integer page, @PathVariable Integer limit){
        Page<EsComment> pageInfo = articleService.searchComment(keywords, page, limit);
        return new PageResult(AzStatus.PAGE,AzContants.SUCCESS_MSG,pageInfo.getTotalElements(),
                pageInfo.getTotalPages(),pageInfo.getContent());
    }
}
