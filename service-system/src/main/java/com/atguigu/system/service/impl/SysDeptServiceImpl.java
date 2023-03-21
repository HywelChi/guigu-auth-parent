package com.atguigu.system.service.impl;

import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.model.system.SysDept;
import com.atguigu.system.execption.GuiguException;
import com.atguigu.system.helper.DeptHelper;
import com.atguigu.system.mapper.SysDeptMapper;
import com.atguigu.system.service.SysDeptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.service.impl
 * @Author: Hywel
 * @CreateTime: 2023-03-20  20:09
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Transactional
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Override
    public void updateStatus(Long id, Integer status) {
        SysDept sysDept = new SysDept();
        sysDept.setId(id);
        sysDept.setStatus(status);
        baseMapper.updateById(sysDept);
    }

    @Override
    public List<SysDept> findNodes() {
        List<SysDept> goodsTypeList = baseMapper.selectList(null);
        return DeptHelper.buildTree(goodsTypeList,0L);
    }

    @Override
    public List<SysDept> findUserNodes() {
        List<SysDept> sysDeptList = baseMapper.selectList(new LambdaQueryWrapper<SysDept>().eq(SysDept::getStatus, 1));
        return sysDeptList;
    }

    @Override
    public void deleteById(Long id) {
        Integer count = baseMapper.selectCount(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, id));
        if (count>0){
            throw new GuiguException(ResultCodeEnum.NODE_ERROR);
        }
        sysDeptMapper.deleteById(id);
    }
}
