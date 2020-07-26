package com.wzn.blog.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {

    @Value("${aaa}")
    String aa;

    @RequestMapping("/test")
    public String test(){
        return aa;
    }
}
