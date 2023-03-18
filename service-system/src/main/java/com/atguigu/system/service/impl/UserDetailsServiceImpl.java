package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysUser;
import com.atguigu.system.custom.CustomUser;
import com.atguigu.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.service.impl
 * @Author: hywel
 * @CreateTime: 2023-03-15  11:30
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getSysUserByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        if (sysUser.getStatus() == 0) {
            throw new RuntimeException("用户被锁定");
        }
        // 获取用户按钮权限集
        List<String> userPermsList = sysUserService.getUserBtnPermsByUserId(sysUser.getId());
        sysUser.setUserPermsList(userPermsList);
        return new CustomUser(sysUser, Collections.emptyList());
    }
}
