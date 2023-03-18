package com.atguigu.system.service.impl;

import com.atguigu.common.helper.MenuHelper;
import com.atguigu.common.helper.RouterHelper;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.RouterVo;
import com.atguigu.model.vo.SysUserQueryVo;
import com.atguigu.system.mapper.SysMenuMapper;
import com.atguigu.system.mapper.SysUserMapper;
import com.atguigu.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.service.impl
 * @Author: hywel
 * @CreateTime: 2023-03-12  13:32
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Transactional
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public IPage<SysUser> findPage(Page<SysUser> userPage, SysUserQueryVo queryVo) {
        return baseMapper.findPage(userPage, queryVo);
    }

    @Override
    public void updateStatus(Long userId, Integer status) {
        SysUser sysUser = new SysUser();
        sysUser.setId(userId);
        sysUser.setStatus(status);
        baseMapper.updateById(sysUser);
    }

    @Override
    public SysUser getSysUserByUsername(String username) {
        return baseMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    @Override
    public Map<String, Object> getUserInfoByUserId(Long id) {
        List<SysMenu> userMenuList = getUserMenuByUserId(id);
        List<SysMenu> userMenuTree = MenuHelper.buildTree(userMenuList);
        List<RouterVo> routers = RouterHelper.buildRouters(userMenuTree);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("routers", routers);
        List<String> userBtnPermsList = getUserBtnPermsByUserId(id);
        returnMap.put("buttons", userBtnPermsList);
        return returnMap;
    }

    // 根据用户id获取用户的按钮权限标识符
    @Override
    public List<String> getUserBtnPermsByUserId(Long userId) {
        List<SysMenu> userMenuList = getUserMenuByUserId(userId);

        List<String> btnPermsList = new ArrayList<>();

        for (SysMenu sysMenu : userMenuList) {
            if (sysMenu.getType().intValue() == 2) {
                String perms = sysMenu.getPerms();
                if (!StringUtils.isEmpty(perms)) {
                    btnPermsList.add(perms);
                }
            }
        }
        return btnPermsList;
    }

    // 根据用户id获取用户权限菜单
    private List<SysMenu> getUserMenuByUserId(Long id) {
        List<SysMenu> sysMenuList = null;
        if (id == 1) {
            sysMenuList = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1).or().orderByAsc(SysMenu::getSortValue));
        } else {
            sysMenuList = sysMenuMapper.getUserMenuListByUserId(id);
        }
        return sysMenuList;
    }


}
