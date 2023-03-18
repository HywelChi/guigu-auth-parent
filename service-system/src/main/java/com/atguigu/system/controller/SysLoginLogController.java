package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.base.BaseEntity;
import com.atguigu.model.system.SysLoginLog;
import com.atguigu.model.vo.SysLoginLogQueryVo;
import com.atguigu.system.service.SysLoginLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.controller
 * @Author: hywel
 * @CreateTime: 2023-03-17  14:10
 * @Description: TODO
 * @Version: 1.0
 */
@Api(tags = "登录日志管理")
@RestController
@RequestMapping("/admin/system/sysLoginLog")
public class SysLoginLogController {
    @Autowired
    private SysLoginLogService sysLoginLogService;

    @ApiOperation("带条件的分页查询")
    @GetMapping("/{current}/{size}")
    public Result findPage(@PathVariable Long current, @PathVariable Long size, SysLoginLogQueryVo logQueryVo) {
        Page<SysLoginLog> page = new Page<>(current, size);
        LambdaQueryWrapper<SysLoginLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String username = logQueryVo.getUsername();
        if (!StringUtils.isEmpty(username)) {
            lambdaQueryWrapper.like(SysLoginLog::getUsername, logQueryVo.getUsername());
        }
        String createTimeBegin = logQueryVo.getCreateTimeBegin();
        if (!StringUtils.isEmpty(createTimeBegin)) {
            lambdaQueryWrapper.ge(BaseEntity::getCreateTime, logQueryVo.getCreateTimeBegin());
        }
        String createTimeEnd = logQueryVo.getCreateTimeEnd();
        if (!StringUtils.isEmpty(createTimeEnd)) {
            lambdaQueryWrapper.le(BaseEntity::getCreateTime, logQueryVo.getCreateTimeEnd());
        }
        IPage<SysLoginLog> iPage = sysLoginLogService.page(page, lambdaQueryWrapper);

        return Result.ok(iPage);

    }
}
