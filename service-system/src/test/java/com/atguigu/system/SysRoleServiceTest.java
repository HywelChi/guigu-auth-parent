package com.atguigu.system;

import com.atguigu.model.system.SysRole;
import com.atguigu.system.service.SysRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system
 * @Author: hywel
 * @CreateTime: 2023-03-08  15:58
 * @Description: TODO
 * @Version: 1.0
 */
@SpringBootTest
public class SysRoleServiceTest {
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 测试查询所有
     */
    @Test
    public void testFindAll(){
        List<SysRole> roleList = sysRoleService.list();
        roleList.forEach(System.out::println);
    }
    /**
     * 测试添加
     */
    @Test
    public void testSave(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("张三");
        sysRole.setRoleCode("zs");
        sysRole.setDescription("大傻瓜");
        sysRoleService.save(sysRole);
    }
    /**
     * 根据id查询
     */
    @Test
    public void testFindById(){
        SysRole sysRole = sysRoleService.getById(10);
        System.out.println("sysRole = " + sysRole);
    }
    /**
     * 根据id更新
     */
    @Test
    public void testUpdateById(){
        SysRole sysRole = new SysRole();
        sysRole.setId(10L);
        sysRole.setRoleName("李四");
        sysRole.setRoleCode("zs");
        sysRole.setDescription("大傻瓜");
        sysRoleService.updateById(sysRole);
    }
    /**
     * 根据id删除
     */
    @Test
    public void testDeleteById(){
        sysRoleService.removeById(10);
    }
}
