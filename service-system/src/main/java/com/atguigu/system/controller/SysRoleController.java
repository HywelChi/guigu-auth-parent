package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.vo.AssignRoleVo;
import com.atguigu.model.vo.SysRoleQueryVo;
import com.atguigu.system.annotation.Log;
import com.atguigu.system.enums.BusinessType;
import com.atguigu.system.execption.GuiguException;
import com.atguigu.system.service.SysRoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.controller
 * @Author: hywel
 * @CreateTime: 2023-03-08  18:05
 * @Description: TODO
 * @Version: 1.0
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @ApiOperation("获取全部角色列表")
    @GetMapping("/findAll")
    public Result findAll() {
        try {
//            int i = 10 / 0;
        } catch (Exception e) {
            throw new GuiguException(20001, "出现自定义异常");
        }
        List<SysRole> roleList = sysRoleService.list();
        return Result.ok(roleList);
    }

    @Log(title = "角色管理",businessType = BusinessType.INSERT,isSaveRequestData = true)
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("添加角色")
    @PostMapping("/save")
    public Result save(@RequestBody @Validated SysRole sysRole) {

        boolean save = sysRoleService.save(sysRole);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("获取单个角色")
    @GetMapping("/findById/{id}")
    public Result findById(@PathVariable Integer id) {
        SysRole sysRole = sysRoleService.getById(id);
        if (sysRole != null) {
            return Result.ok(sysRole);
        }
        return Result.fail();
    }

    @Log(title = "角色管理",businessType = BusinessType.UPDATE,isSaveRequestData = true)
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation("更新单个角色")
    @PutMapping("/updateById")
    public Result updateById(@RequestBody SysRole sysRole) {
        // 方式一：设置更新日期为当前时间
        sysRole.setUpdateTime(new Date());
        // 方式二：将更新日期设置为null
//        sysRole.setUpdateTime(null);
        boolean b = sysRoleService.updateById(sysRole);
        if (b) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Log(title = "角色管理",businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("删除单个角色")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Long id) {
        boolean b = sysRoleService.removeById(id);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //    @ApiOperation("批量删除")
//    @DeleteMapping("/batchRemove/{ids}")
//    public Result batchRemove(@PathVariable List ids){
//        boolean b = sysRoleService.removeByIds(ids);
//        if (b){
//            return Result.ok();
//        } else {
//            return Result.fail();
//        }
//    }

    @Log(title = "角色管理",businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("批量删除")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids) {
        boolean b = sysRoleService.removeByIds(ids);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("带条件的分页查询")
    @GetMapping("/{current}/{size}")
    public Result getPageList(@PathVariable Long current, @PathVariable Long size, SysRoleQueryVo vo) {
        Page<SysRole> page = new Page<>(current, size);
        IPage<SysRole> iPage = sysRoleService.findPage(page, vo);
        return Result.ok(iPage);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("根据用户id查询用户已分配的角色")
    @GetMapping("/toAssign/{userId}")
    public Result getRolesByUserId(@PathVariable Long userId) {
        Map<String, Object> roleMap = sysRoleService.getRolesByUserId(userId);
        return Result.ok(roleMap);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    @ApiOperation("分配角色")
    @PostMapping("/doAssign")
    public Result assignRoles(@RequestBody AssignRoleVo assignRoleVo){
        sysRoleService.assignRoles(assignRoleVo);
        return Result.ok();
    }
}
