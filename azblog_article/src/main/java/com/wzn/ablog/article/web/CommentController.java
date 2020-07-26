package com.wzn.ablog.article.web;

import com.wzn.ablog.article.feign.SearchFeign;
import com.wzn.ablog.article.service.CommentService;
import com.wzn.ablog.common.annotation.Authorized;
import com.wzn.ablog.common.contants.AzContants;
import com.wzn.ablog.common.contants.AzStatus;
import com.wzn.ablog.common.entity.Comment;
import com.wzn.ablog.common.vo.AzResult;
import com.wzn.ablog.common.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@Api(tags = {"评论"})
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private SearchFeign searchFeign;

    @ApiOperation(value = "评论列表", notes = "获取评论列表并分页")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    @GetMapping
    public PageResult list(int page,int limit){
        Page<Comment> pageInfo = commentService.list(page, limit);
        return  new PageResult(AzStatus.PAGE, AzContants.SUCCESS_MSG,pageInfo.getTotalElements()
                ,pageInfo.getTotalPages(),pageInfo.getContent());
    }

    @ApiOperation(value = "查找评论")
    @ApiImplicitParam(name = "Authorization", value = "令牌", required = true)
    @GetMapping("/{id}")
    public AzResult findbyId(@PathVariable String id){
        Comment comment = commentService.findById(id);
        return AzResult.ok().data(comment);
    }

    @Authorized
    @ApiOperation(value = "删除评论",notes = "token需要有root权限")
    @ApiImplicitParam(name = "Authorization", value = "令牌", required = true)
    @DeleteMapping("/{ids}")
    public AzResult del(@PathVariable String[] ids){
        commentService.del(ids);
        return AzResult.ok();
    }

    @PostMapping
    @ApiOperation(value = "添加评论")
    @ApiImplicitParam(name = "Authorization", value = "令牌", required = true)
    public AzResult add(@RequestBody Comment comment){
        commentService.add(comment);
        return AzResult.ok();
    }

    @ApiOperation(value = "修改评论")
    @ApiImplicitParam(name = "Authorization", value = "令牌", required = true)
    @PutMapping
    public AzResult update(@RequestBody Comment comment){
        commentService.update(comment);
        return AzResult.ok();
    }

    @ApiOperation(value = "全部评论")
    @ApiImplicitParam(name = "Authorization", value = "令牌", required = true)
    @GetMapping("/all")
    public List<Comment> allComment(){
        List<Comment> all =  commentService.all();
        return all;
    }

    @ApiOperation(value = "搜索评论")
    @ApiImplicitParam(name = "Authorization", value = "令牌", required = true)
    @GetMapping("/search")
    public PageResult search(String keywords,int page,int limit){
        return searchFeign.searchComment(keywords, page, limit);
    }
}
