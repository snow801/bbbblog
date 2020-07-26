package com.wzn.ablog.admin.web.controller;

import com.wzn.ablog.admin.service.PortService;
import com.wzn.ablog.common.annotation.Authorized;
import com.wzn.ablog.common.contants.AzContants;
import com.wzn.ablog.common.contants.AzStatus;
import com.wzn.ablog.common.entity.Port;
import com.wzn.ablog.common.entity.Role;
import com.wzn.ablog.common.vo.AzResult;
import com.wzn.ablog.common.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/port")
@Api(tags = {"接口"})
public class PortController {

    @Autowired
    private PortService portService;

    @Authorized
    @GetMapping
    @ApiOperation(value = "接口列表",notes = "分页")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    public PageResult roleList(int page, int limit) {
        Page<Port> portPage = portService.list(page, limit);
        return new PageResult(AzStatus.PAGE, AzContants.SUCCESS_MSG, portPage.getTotalElements(), portPage.getTotalPages()
                , portPage.getContent());
    }

    @Authorized
    @GetMapping("/search/{keyword}")
    @ApiOperation(value = "搜索接口")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    public PageResult search(@PathVariable String keyword, int page, int limit){
        Page<Port> ports = portService.search(keyword, page, limit);
        return new PageResult(AzStatus.PAGE,AzContants.SUCCESS_MSG,ports.getTotalElements(),
                ports.getTotalPages(),ports.getContent());
    }
}
