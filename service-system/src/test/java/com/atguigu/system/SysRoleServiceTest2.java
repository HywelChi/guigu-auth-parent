package com.atguigu.system;

import com.atguigu.model.system.SysRole;
import com.atguigu.system.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system
 * @Author: hywel
 * @CreateTime: 2023-03-09  11:31
 * @Description: TODO
 * @Version: 1.0
 */
@SpringBootTest
public class SysRoleServiceTest2 {
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 测试查询所有
     */
    @Test
    public void testFindAll() {
        sysRoleService.list().forEach(System.out::println);
    }

    /**
     * 测试查询单个
     */
    @Test
    public void testFindOne() {
        System.out.println(sysRoleService.getById(9));
    }

    /**
     * 测试添加
     */
    @Test
    public void testAdd() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("佐助");
        sysRole.setRoleCode("Sasuke");
        sysRole.setDescription("阴之力");
        sysRoleService.save(sysRole);

    }

    /**
     * 测试修改
     */
    @Test
    public void testUpdate() {
        SysRole sysRole = new SysRole();
        sysRole.setId(14L);
        sysRole.setRoleName("佐小助");
        sysRole.setRoleCode("Sasuke");
        sysRole.setDescription("阴之力");
        sysRoleService.updateById(sysRole);

    }

    /**
     * 测试删除
     */
    @Test
    public void testDelete() {
        sysRoleService.removeById(14);
    }

    /**
     * 测试分页
     */
    @Test
    public void testPage() {
        Page<SysRole> page = new Page<>(2, 2);

        sysRoleService.page(page, new QueryWrapper<SysRole>().like("role_name", "管理员"));
        page.getRecords().forEach(System.out::println);
        System.out.println("page.getTotal() = " + page.getTotal());
    }

}
