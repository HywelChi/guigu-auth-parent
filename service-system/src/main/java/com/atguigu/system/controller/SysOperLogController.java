package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysOperLog;
import com.atguigu.model.vo.SysOperLogQueryVo;
import com.atguigu.system.service.SysOperLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.controller
 * @Author: hywel
 * @CreateTime: 2023-03-17  15:56
 * @Description: TODO
 * @Version: 1.0
 */
@Api(tags = "操作日志管理")
@RestController
@RequestMapping("/admin/system/sysOperLog")
public class SysOperLogController {
    @Autowired
    private SysOperLogService sysOperLogService;

    @ApiOperation("分页及带条件的查询")
    @GetMapping("/{current}/{size}")
    public Result findPage(@PathVariable Long current, @PathVariable Long size, SysOperLogQueryVo operLogQueryVo) {
        Page<SysOperLog> page = new Page<>(current, size);
        IPage<SysOperLog> iPage = sysOperLogService.getPageList(page, operLogQueryVo);
        return Result.ok(iPage);
    }

    @ApiOperation("查询单个")
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable Long id) {
        SysOperLog sysOperLog = sysOperLogService.getById(id);
        return Result.ok(sysOperLog);
    }
}
