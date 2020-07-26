package com.wzn.ablog.article.web;

import com.wzn.ablog.article.feign.SearchFeign;
import com.wzn.ablog.article.service.CategoryService;
import com.wzn.ablog.common.annotation.Authorized;
import com.wzn.ablog.common.contants.AzContants;
import com.wzn.ablog.common.contants.AzStatus;
import com.wzn.ablog.common.entity.Category;
import com.wzn.ablog.common.vo.AzResult;
import com.wzn.ablog.common.vo.PageResult;
import com.wzn.ablog.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Api(tags = {"分类"})
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SearchFeign searchFeign;

    @ApiOperation(value = "分类列表", notes = "获取分类列表并分页")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    @GetMapping
    public PageResult list(Integer page, Integer limit){
        Page<Category> pageInfo = categoryService.list(page, limit);
        return new PageResult(AzStatus.PAGE, AzContants.SUCCESS_MSG,pageInfo.getTotalElements()
        ,pageInfo.getTotalPages(),pageInfo.getContent());
    }

    @ApiOperation(value = "全部分类")
    @ApiImplicitParam(name = "Authorization", value = "令牌", required = true)
    @GetMapping("/all")
    public AzResult all(){
      return AzResult.ok().data(categoryService.list());
    }


    @ApiOperation(value = "根据id查找分类")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    @GetMapping("/{id}")
    public AzResult findNameById(@PathVariable String id){
        String name = categoryService.findNameById(id);
        if(name != null){
            return AzResult.ok(AzStatus.SUCCESS).data(name);
        }
        return AzResult.ok(AzStatus.ERR);
    }

    @ApiOperation(value = "根据id删除分类",notes = "token需要有root权限")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    @Authorized
    @DeleteMapping("/{ids}")
    public AzResult del(@PathVariable String[] ids){
        categoryService.del(ids);
        return AzResult.ok();
    }

    @ApiOperation(value = "添加分类")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    @PostMapping
    public AzResult add(@RequestBody Category category){
        categoryService.add(category);
        return AzResult.ok();
    }

    @ApiOperation(value = "修改分类")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    @PutMapping
    public AzResult update(@RequestBody Category category){
        categoryService.update(category);
        return AzResult.ok();
    }

    @ApiOperation(value = "搜索分类")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    @GetMapping("/search/{keywords}")
    public PageResult search(@PathVariable String keywords,int page,int limit){
        return searchFeign.searchCategroy(keywords, page, limit);
    }
}
