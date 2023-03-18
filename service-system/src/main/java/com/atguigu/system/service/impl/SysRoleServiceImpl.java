package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.model.vo.AssignRoleVo;
import com.atguigu.model.vo.SysRoleQueryVo;
import com.atguigu.system.mapper.SysRoleMapper;
import com.atguigu.system.mapper.SysUserRoleMapper;
import com.atguigu.system.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.service.impl
 * @Author: hywel
 * @CreateTime: 2023-03-08  15:56
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Transactional
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public IPage<SysRole> findPage(Page<SysRole> page, SysRoleQueryVo vo) {
        return baseMapper.findPage(page,vo);
    }

    @Override
    public Map<String, Object> getRolesByUserId(Long userId) {
        // 获取所有角色
        List<SysRole> sysRoles = baseMapper.selectList(null);
        // 根据用户id查询
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        // 获取用户已分配的角色
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(queryWrapper);
        // 获取用户已分配的角色id
        List<Long> userRoleIds = new ArrayList<>();
        for (SysUserRole userRole : userRoles) {
            userRoleIds.add(userRole.getRoleId());
        }
        //创建返回的map
        Map<String ,Object> returnMap = new HashMap<>();
        returnMap.put("allRoles",sysRoles);
        returnMap.put("userRoleIds", userRoleIds);

        return returnMap;
    }

    @Override
    public void assignRoles(AssignRoleVo assignRoleVo) {
        // 根据用户id删除原来分配的角色
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId,assignRoleVo.getUserId()));
        // 获取所有角色id
        List<Long> roleIdList = assignRoleVo.getRoleIdList();
        for (Long roleId : roleIdList) {
            if (roleId != null){
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(roleId);
                sysUserRole.setUserId(assignRoleVo.getUserId());
                sysUserRoleMapper.insert(sysUserRole);
            }
        }
    }

}
