package com.atguigu.system;

import com.atguigu.model.system.SysRole;
import com.atguigu.system.mapper.SysRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system
 * @Author: hywel
 * @CreateTime: 2023-03-08  09:53
 * @Description: TODO
 * @Version: 1.0
 */
@SpringBootTest
public class SysRoleMapperTest {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * @author hywel
     * @date 2023/3/8
     * 查询所有
     */
    @Test
    public void testSelectList() {
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        sysRoles.forEach(System.out::println);
    }

    /**
     * @author hywel
     * @date 2023/3/8
     * 根据id查询
     */
    @Test
    public void testSelectByID() {
        SysRole sysRole = sysRoleMapper.selectById(1);
        System.out.println("sysRole = " + sysRole);
    }

    /**
     * @author hywel
     * @date 2023/3/8
     * 添加
     */
    @Test
    public void testInsert() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("动物管理员");
        sysRole.setRoleCode("dwgly");
        sysRole.setDescription("负责动物的增删改查");
        int i = sysRoleMapper.insert(sysRole);
        System.out.println(i > 0 ? "添加成功" : "添加失败");
    }

    /**
     * @author hywel
     * @date 2023/3/8
     * 根据id更新
     */
    @Test
    public void testUpdateById() {
        SysRole sysRole = new SysRole();
        sysRole.setId(9L);
        sysRole.setDescription("负责动物的吃喝拉撒");
        int i = sysRoleMapper.updateById(sysRole);
        System.out.println(i > 0 ? "更新成功" : "更新失败");
    }

    /**
     * @author hywel
     * @date 2023/3/8
     * 根据id删除
     */
    @Test
    public void testDeleteById() {
        int i = sysRoleMapper.deleteById(9);
        System.out.println(i > 0 ? "逻辑删除成功" : "逻辑删除失败");
    }

    /**
     * testQueryWrapper
     */
    @Test
    public void testQueryWrapper() {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name", "动物管理员");
        List<SysRole> sysRoles = sysRoleMapper.selectList(queryWrapper);
        System.out.println("sysRoles = " + sysRoles);

        QueryWrapper<SysRole> queryWrapper1 = new QueryWrapper<>();
//        queryWrapper1.like("role_name", "管理").eq("role_code", "COMMON");
        queryWrapper1.likeRight("role_name", "普通");
        System.out.println("sysRoleMapper.selectList(queryWrapper1) = " + sysRoleMapper.selectList(queryWrapper1));
    }

    /**
     * testLambdaQueryWrapper
     */
    @Test
    public void testLambdaQueryWrapper() {
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.ge(SysRole::getId, 2);
        List<SysRole> sysRoles = sysRoleMapper.selectList(lambdaQueryWrapper);
        System.out.println("sysRoles = " + sysRoles);
    }

    /**
     * testUpdateWrapper
     */
    @Test
    public void testUpdateWrapper() {
        UpdateWrapper<SysRole> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("role_name", "测试管理员").eq("id", 9L);
        sysRoleMapper.update(null, updateWrapper);
    }

    /**
     * testSelectPage
     */
    @Test
    public void testSelectPage() {
        Page<SysRole> page = new Page<>(1, 2);
        sysRoleMapper.selectPage(page, null);
        long total = page.getTotal();
        System.out.println("总记录数 = " + total);
        long pages = page.getPages();
        System.out.println("总页数 = " + pages);
        long current = page.getCurrent();
        System.out.println("当前页 = " + current);
        long size = page.getSize();
        System.out.println("每页显示条数 = " + size);
        List<SysRole> records = page.getRecords();
        records.forEach(System.out::println);
    }
}
