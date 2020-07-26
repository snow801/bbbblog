package com.wzn.ablog.article.feign;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.wzn.ablog.common.vo.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(value = "azblog-search",fallback = SearchFeignFallback.class)
public interface SearchFeign {

    //有PathVariable的情况FeignClient不能使用GetMapping这种组合注解
    @RequestMapping(value = "/search/{keywords}/{page}/{limit}",method = RequestMethod.GET)
    PageResult search(@PathVariable("keywords") String keywords, @PathVariable("page") Integer page,
                      @PathVariable("limit") Integer limit);

    @RequestMapping(value = "/search/categroy/{keywords}/{page}/{limit}",method = RequestMethod.GET)
    PageResult searchCategroy(@PathVariable("keywords") String keywords, @PathVariable("page") Integer page,
                              @PathVariable("limit") Integer limit);

    @RequestMapping(value = "/search/comment/{keywords}/{page}/{limit}",method = RequestMethod.GET)
    PageResult searchComment(@PathVariable("keywords") String keywords, @PathVariable Integer page,
                                    @PathVariable Integer limit);
}
