package com.atguigu.system.service.impl;

import com.atguigu.common.helper.MenuHelper;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRoleMenu;
import com.atguigu.model.vo.AssignMenuVo;
import com.atguigu.model.vo.AssignRoleVo;
import com.atguigu.system.execption.GuiguException;
import com.atguigu.system.mapper.SysMenuMapper;
import com.atguigu.system.mapper.SysRoleMenuMapper;
import com.atguigu.system.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.service.impl
 * @Author: hywel
 * @CreateTime: 2023-03-13  16:32
 * @Description: TODO
 * @Version: 1.0
 */
@Controller
@Transactional
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> findMenuNodes() {
        // 获取所有菜单
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        // 调用将List转换为菜单树的方法
        List<SysMenu> sysMenusTree = MenuHelper.buildTree(sysMenuList);
        return sysMenusTree;
    }

    @Override
    public List<SysMenu> getRoleMenuList(Long roleId) {
        // 获取所有状态为1的菜单列表
        List<SysMenu> sysMenuList = baseMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));
        // 根据角色id查询中间表获取当前角色已分配的权限菜单
        List<SysRoleMenu> roleMenuList = sysRoleMenuMapper.selectList(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId));
        // 获取角色已分配的权限菜单的所有的id
        // 方法一：使用stream流实现
        List<Long> roleMenuIds = roleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        // 方法二：放入List
//        List<Long> roleMenuIds = new ArrayList<>();
//        for (SysRoleMenu sysRoleMenu : roleMenuList) {
//            roleMenuIds.add(sysRoleMenu.getMenuId());
//        }
        // 遍历所有的菜单
        for (SysMenu sysMenu : sysMenuList) {
            // 获取当前菜单的id
            Long menuId = sysMenu.getId();
            // 判断roleMenuIds中是否包含当前菜单id
            if (roleMenuIds.contains(menuId)) {
                // 该权限菜单已经被当前角色分配，设置它被选中状态
                sysMenu.setSelect(true);
            }
        }
        // 通过MenuHelper工具类将所有菜单转换为菜单树
        List<SysMenu> sysMenusTree = MenuHelper.buildTree(sysMenuList);
        return sysMenusTree;
    }

    @Override
    public void deleteById(Long id) {
        Integer count = baseMapper.selectCount(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id));
        if (count > 0) {
            throw new GuiguException(777, "有子节点，不能删除");
        }
        baseMapper.deleteById(id);

    }

    @Override
    public void assignMenu(AssignMenuVo assignMenuVo) {
        // 删除已分配的权限
        sysRoleMenuMapper.delete(new QueryWrapper<SysRoleMenu>().eq("role_id", assignMenuVo.getRoleId()));
        // 遍历所有已选择的权限id
        List<Long> menuIdList = assignMenuVo.getMenuIdList();
        for (Long menuId : menuIdList) {
            if (menuId != null) {
                // 创建SysRoleMenu对象
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setMenuId(menuId);
                sysRoleMenu.setRoleId(assignMenuVo.getRoleId());
                // 添加新权限
                sysRoleMenuMapper.insert(sysRoleMenu);
            }

        }

    }

}
