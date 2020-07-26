package com.wzn.ablog.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult {
    private Integer status;
    private String message;
    private Long total;
    private int page;
    private Object data;

    public PageResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
