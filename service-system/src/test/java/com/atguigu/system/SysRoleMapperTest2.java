package com.atguigu.system;

import com.atguigu.model.system.SysRole;
import com.atguigu.system.mapper.SysRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system
 * @Author: hywel
 * @CreateTime: 2023-03-09  09:57
 * @Description: TODO
 * @Version: 1.0
 */
@SpringBootTest
public class SysRoleMapperTest2 {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 测试查询所有
     */
    @Test
    public void testFindAll() {
        List<SysRole> sysRoleList = sysRoleMapper.selectList(null);
        sysRoleList.forEach(System.out::println);
    }

    /**
     * 测试查询单个
     */
    @Test
    public void testFindOne() {
        SysRole sysRole = sysRoleMapper.selectById(1);
        System.out.println("sysRole = " + sysRole);
    }

    /**
     * 测试添加
     */
    @Test
    public void testAdd() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("我爱罗");
        sysRole.setRoleCode("gala");
        sysRole.setDescription("风影");
        int i = sysRoleMapper.insert(sysRole);
        System.out.println(i > 0 ? "添加成功" : "添加失败");

    }

    /**
     * 测试根据id修改
     */
    @Test
    public void testUpdateById() {
        SysRole sysRole = new SysRole();
        sysRole.setId(12L);
        sysRole.setRoleName("我爱罗");
        sysRole.setRoleCode("gala");
        sysRole.setDescription("鸣人的好朋友");
        int i = sysRoleMapper.updateById(sysRole);
        System.out.println(i > 0 ? "修改成功" : "修改失败");
    }

    /**
     * 测试删除
     */
    @Test
    public void testDeleteById() {
        int i = sysRoleMapper.deleteById(12);
        System.out.println(i > 0 ? "删除成功" : "删除失败");

    }

    /**
     * 根据条件查询
     */
    @Test
    public void testQueryWrapper() {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("role_code","gala");
//        queryWrapper.like("role_name", "管理");
//        queryWrapper.likeLeft("role_name","员");
        queryWrapper.like("role_name", "我").eq("description", "风影");
        List<SysRole> roleList = sysRoleMapper.selectList(queryWrapper);
        roleList.forEach(System.out::println);
    }

    /**
     * lambda条件查询
     */
    @Test
    public void testLambdaQueryWrapper() {
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.ge(SysRole::getId, 3);
        List<SysRole> sysRoleList = sysRoleMapper.selectList(lambdaQueryWrapper);
        sysRoleList.forEach(System.out::println);
    }

    /**
     * 测试分页
     */
    @Test
    public void testFindPage() {
        Page<SysRole> page = new Page<>(1, 2);
        sysRoleMapper.selectPage(page, null);
        System.out.println("page.getCurrent() = " + page.getCurrent());
        System.out.println("page.getPages() = " + page.getPages());
        System.out.println("page.getRecords() = " + page.getRecords());
        System.out.println("page.getSize() = " + page.getSize());
    }
}
