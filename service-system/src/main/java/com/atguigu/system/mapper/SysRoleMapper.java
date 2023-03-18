package com.atguigu.system.mapper;

import com.atguigu.model.system.SysRole;
import com.atguigu.model.vo.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.mapper
 * @Author: hywel
 * @CreateTime: 2023-03-08  09:52
 * @Description: TODO
 * @Version: 1.0
 */
//@Mapper
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {
    IPage<SysRole> findPage(Page<SysRole> page, @Param("vo") SysRoleQueryVo vo);

}
