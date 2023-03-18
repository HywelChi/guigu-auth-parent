package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysLoginLog;
import com.atguigu.system.mapper.SysLoginLogMapper;
import com.atguigu.system.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.service.impl
 * @Author: hywel
 * @CreateTime: 2023-03-17  14:43
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Transactional
public class LoginLogServiceImpl implements LoginLogService {
    @Autowired
    private SysLoginLogMapper sysLoginLogMapper;

    @Override
    public void save(String username, String ipAddr, Integer status, String msg) {
        SysLoginLog sysLoginLog = new SysLoginLog();
        sysLoginLog.setUsername(username);
        sysLoginLog.setIpaddr(ipAddr);
        sysLoginLog.setStatus(status);
        sysLoginLog.setMsg(msg);
        sysLoginLogMapper.insert(sysLoginLog);
    }
}
