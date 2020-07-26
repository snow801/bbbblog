package com.wzn.ablog.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EchartsResult {

    private List<String> dimensions;

    private List<Map<String,String>> source;
}
