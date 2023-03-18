package com.atguigu.system.service;

import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.SysUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface SysUserService extends IService<SysUser> {
    IPage<SysUser> findPage(Page<SysUser> userPage, SysUserQueryVo queryVo);

    void updateStatus(Long userId, Integer status);

    SysUser getSysUserByUsername(String username);

    Map<String, Object> getUserInfoByUserId(Long id);

    List<String> getUserBtnPermsByUserId(Long userId);
}
