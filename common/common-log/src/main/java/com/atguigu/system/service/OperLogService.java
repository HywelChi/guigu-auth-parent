package com.atguigu.system.service;

import com.atguigu.model.system.SysOperLog;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.service
 * @Author: Hywel
 * @CreateTime: 2023-03-17  19:42
 * @Description: 保存操作日志的接口
 * @Version: 1.0
 */
public interface OperLogService {
    // 保存操作日志
    void saveSysOperLog(SysOperLog sysOperLog);
}
