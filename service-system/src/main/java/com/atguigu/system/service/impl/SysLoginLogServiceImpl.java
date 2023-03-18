package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysLoginLog;
import com.atguigu.system.mapper.SysLoginLogMapper;
import com.atguigu.system.service.SysLoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.service.impl
 * @Author: hywel
 * @CreateTime: 2023-03-17  14:08
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Transactional
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {
}
