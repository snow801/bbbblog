package com.wzn.azblog.search.feign;

import com.wzn.ablog.common.entity.Category;
import com.wzn.ablog.common.entity.Comment;
import com.wzn.ablog.common.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "azblog-article")
public interface SearchFeign{

    @GetMapping("/article/findAll")
    Result findAll();

    @GetMapping("/category/all")
    List<Category> all();

    @GetMapping("/comment/all")
    List<Comment> allComment();
}
