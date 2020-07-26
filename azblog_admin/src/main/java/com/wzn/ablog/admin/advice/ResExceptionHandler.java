package com.wzn.ablog.admin.advice;

import com.wzn.ablog.common.contants.AzStatus;
import com.wzn.ablog.common.vo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result error(Exception e) {
        e.printStackTrace();
        return new Result(AzStatus.ERR,e.getMessage());
    }


}