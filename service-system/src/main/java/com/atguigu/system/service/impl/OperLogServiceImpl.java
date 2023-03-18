package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysOperLog;
import com.atguigu.system.mapper.SysOperLogMapper;
import com.atguigu.system.service.OperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.service.impl
 * @Author: Hywel
 * @CreateTime: 2023-03-17  19:44
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Transactional
public class OperLogServiceImpl implements OperLogService {
    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    @Async// 异步调用该方法，会重开一个线程执行
    @Override
    public void saveSysOperLog(SysOperLog sysOperLog) {
        sysOperLogMapper.insert(sysOperLog);
    }
}
