package com.wzn.ablog.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result {
    private Integer status;
    private String message;
    private Object data;

    public Result(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
