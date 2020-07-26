package com.wzn.ablog.common.annotation;

import java.lang.annotation.*;

/*
 * 安全认证
 * */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authorized {

    String value() default "";

}