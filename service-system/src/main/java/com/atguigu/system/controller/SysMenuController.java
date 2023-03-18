package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.vo.AssignMenuVo;
import com.atguigu.model.vo.AssignRoleVo;
import com.atguigu.system.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleList;
import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.controller
 * @Author: hywel
 * @CreateTime: 2023-03-13  16:34
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/admin/system/sysMenu")
@Api(tags = "菜单管理")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation("查询菜单树列表")
    @GetMapping("/findMenuNodes")
    public Result findMenuNodes() {
        List<SysMenu> list = sysMenuService.findMenuNodes();
        return Result.ok(list);
    }

    @ApiOperation("根据id删除")
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Long id) {
        // 自定义一个删除的方法，校验是否能删除（有子节点不能直接删除）
        sysMenuService.deleteById(id);
        return Result.ok();
    }

    @ApiOperation("添加")
    @PostMapping("save")
    public Result save(@RequestBody SysMenu sysMenu) {
        boolean save = sysMenuService.save(sysMenu);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("根据id查询")
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable Long id) {
        SysMenu sysMenu = sysMenuService.getById(id);
        return Result.ok(sysMenu);
    }

    @ApiOperation("根据id更新")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysMenu sysMenu) {
        sysMenu.setUpdateTime(new Date());
        boolean b = sysMenuService.updateById(sysMenu);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("去分配权限的页面")
    @GetMapping("/getRoleMenuList/{roleId}")
    public Result getRoleMenuList(@PathVariable Long roleId) {
        List<SysMenu> list = sysMenuService.getRoleMenuList(roleId);
        return Result.ok(list);
    }
    @ApiOperation("分配权限")
    @PostMapping("/assignMenu")
    public Result assignMenu(@RequestBody AssignMenuVo assignMenuVo){
        sysMenuService.assignMenu(assignMenuVo);
        return Result.ok();

    }
}
