package com.atguigu.system.service;

import com.atguigu.model.system.SysMenu;
import com.atguigu.model.vo.AssignMenuVo;
import com.atguigu.model.vo.AssignRoleVo;
import com.atguigu.system.mapper.SysMenuMapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> findMenuNodes();

    List<SysMenu> getRoleMenuList(Long roleId);

    void deleteById(Long id);

    void assignMenu(AssignMenuVo assignMenuVo);
}
