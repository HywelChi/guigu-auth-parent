package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysOperLog;
import com.atguigu.model.vo.SysOperLogQueryVo;
import com.atguigu.system.mapper.SysOperLogMapper;
import com.atguigu.system.service.SysOperLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.service.impl
 * @Author: hywel
 * @CreateTime: 2023-03-17  15:55
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Transactional
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {
    @Override
    public IPage<SysOperLog> getPageList(Page page, SysOperLogQueryVo operLogQueryVo) {
        return baseMapper.getPageList(page,operLogQueryVo);
    }
}
