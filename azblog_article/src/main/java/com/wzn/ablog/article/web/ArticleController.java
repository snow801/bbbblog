package com.wzn.ablog.article.web;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.wzn.ablog.article.feign.SearchFeign;
import com.wzn.ablog.article.service.ArticleService;
import com.wzn.ablog.common.annotation.Authorized;
import com.wzn.ablog.common.contants.AzContants;
import com.wzn.ablog.common.contants.AzStatus;
import com.wzn.ablog.common.entity.Article;
import com.wzn.ablog.common.utils.RsaKeyConfig;
import com.wzn.ablog.common.utils.TokenUtils;
import com.wzn.ablog.common.vo.AzResult;
import com.wzn.ablog.common.vo.PageResult;
import com.wzn.ablog.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/article")
@Api(tags = {"文章"})
public class ArticleController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RsaKeyConfig rsaKeyConfig;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SearchFeign searchFeign;

    @ApiOperation(value = "文章列表", notes = "获取文章列表并分页")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    @GetMapping
    public PageResult list(Integer page, Integer limit) {
        Page<Article> list = articleService.list(page, limit, TokenUtils.getUserId(request,rsaKeyConfig));
        return new PageResult(AzStatus.PAGE, AzContants.SUCCESS_MSG, list.getTotalElements(),
                list.getTotalPages(), list.getContent());
    }

    @ApiOperation("根据id查找文章")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    @GetMapping("/{id}")
    public AzResult findById(@PathVariable String id) {
        Article article = articleService.findById(id);
        return AzResult.ok().data(article);
    }

    @Authorized
    @ApiOperation("根据id删除文章")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    @DeleteMapping("/{ids}")
    public AzResult del(@PathVariable("ids") String[] ids) {
        articleService.del(ids, TokenUtils.getUserId(request,rsaKeyConfig));
        return AzResult.ok();
    }

    @ApiOperation("添加文章")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    @PostMapping
    public AzResult add(@RequestBody Article article) {
        log.debug(String.valueOf(article));
        articleService.add(article, TokenUtils.getUsername(request,rsaKeyConfig)
        ,TokenUtils.getUserId(request,rsaKeyConfig));
        return AzResult.ok();
    }

    @ApiOperation("更新文章")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    @PutMapping
    public AzResult update(@RequestBody Article article) {
        articleService.update(article, TokenUtils.getUsername(request,rsaKeyConfig),
                TokenUtils.getUserId(request,rsaKeyConfig));
        return AzResult.ok();
    }

    @ApiOperation("查找全部文章")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    @GetMapping("/findAll")
    public AzResult findAll() {
        List<Article> all = articleService.findAll();
        return AzResult.ok().data(all);
    }

    @ApiOperation(value = "搜索",notes = "搜索文章并分页")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    @GetMapping("/{keywords}/{page}/{limit}")
    public PageResult search(@PathVariable("keywords") String keywords, @PathVariable("page") Integer page, @PathVariable("limit") Integer limit) {
        return searchFeign.search(keywords, page, limit);
    }

    @ApiOperation(value = "今天的文章数")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    @GetMapping("/todayCount")
    public AzResult todayCount(){
        int count = articleService.todayCount();
        return AzResult.ok().data(count);
    }
}
