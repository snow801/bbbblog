package com.wzn.ablog.admin.web.controller;

import com.wzn.ablog.admin.service.RoleService;
import com.wzn.ablog.common.annotation.Authorized;
import com.wzn.ablog.common.contants.AzContants;
import com.wzn.ablog.common.contants.AzStatus;
import com.wzn.ablog.common.entity.Port;
import com.wzn.ablog.common.entity.Role;
import com.wzn.ablog.common.utils.BlogUtils;
import com.wzn.ablog.common.utils.RsaKeyConfig;
import com.wzn.ablog.common.utils.TokenUtils;
import com.wzn.ablog.common.vo.AzResult;
import com.wzn.ablog.common.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/role")
@Api(tags = {"权限"})
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RsaKeyConfig rsaKeyConfig;

    @Authorized
    @GetMapping("/roleList")
    @ApiOperation(value = "角色列表",notes = "分页")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    public PageResult roleList(int page, int limit) {
        Page<Role> rolePage = roleService.roleList(page, limit);
        return new PageResult(AzStatus.PAGE, AzContants.SUCCESS_MSG, rolePage.getTotalElements(), rolePage.getTotalPages()
                , rolePage.getContent());
    }

    @Authorized
    @GetMapping("/allRole")
    @ApiOperation(value = "角色列表",notes = "不分页")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    public AzResult allRole(){
        List<Role> list = roleService.allRole();
        return AzResult.ok(AzStatus.SUCCESS).data(list);
    }

    @Authorized
    @GetMapping("/portList")
    @ApiOperation(value = "接口列表")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    public AzResult portList(String roleId) {
        List<Map<String, Object>> maps = roleService.portList(roleId);
        return AzResult.ok(AzStatus.PAGE).data(maps);
    }

    @Authorized
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查找接口")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    public AzResult findbyId(@PathVariable String id) {
        Role role = roleService.findById(id);
        if(role == null){
            return AzResult.err(AzStatus.ROOT_ISNOT_EDIT);
        }
        return AzResult.ok(AzStatus.SUCCESS).data(role);
    }

    @Authorized
    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除角色")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    public AzResult del(@PathVariable String[] ids) {
        boolean b = roleService.del(ids);
        if(b){
            return AzResult.ok(AzStatus.SUCCESS);
        }else {
            return AzResult.err(AzStatus.ROOT_ISNOT_DELETE);
        }

    }

    @Authorized
    @PostMapping
    @ApiOperation(value = "添加权限")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    public AzResult add(@RequestBody Role Role) {
        roleService.add(Role);
        return AzResult.ok();
    }

    @Authorized
    @PutMapping
    @ApiOperation(value = "修改角色")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    public AzResult update(@RequestBody Role role) {
        log.debug(String.valueOf(role));
        return AzResult.ok();
    }

    @Authorized
    @GetMapping("/search/{roleName}")
    @ApiOperation(value = "搜索角色")
    @ApiImplicitParam(name = "Authorization", value = "token", required = true)
    public PageResult searchRole(@PathVariable String roleName, int page, int limit) {
        Page<Role> rolePage = roleService.searchRole(roleName,page,limit);
        return new PageResult(AzStatus.PAGE,AzContants.SUCCESS_MSG,rolePage.getTotalElements(),
                rolePage.getTotalPages(),rolePage.getContent());
    }

    @Authorized
    @ApiOperation(value = "给角色添加接口访问权限")
    @PostMapping("/roleAddPort")
    public AzResult roleAddPort(@RequestBody Map map){
        roleService.roleAddPort(map.get("roleId")+"",map.get("portId")+"",
                map.get("checked")+"");
        return AzResult.ok();
    }
}
