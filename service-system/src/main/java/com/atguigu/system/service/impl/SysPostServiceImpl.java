package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysPost;
import com.atguigu.model.vo.SysPostQueryVo;
import com.atguigu.system.mapper.SysPostMapper;
import com.atguigu.system.service.SysPostService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.service.impl
 * @Author: Hywel
 * @CreateTime: 2023-03-20  22:41
 * @Description: TODO
 * @Version: 1.0
 */
@Service
@Transactional
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {
    @Override
    public void updateStatus(Long id, Integer status) {
        SysPost sysPost = baseMapper.selectById(id);
        sysPost.setStatus(status);
        baseMapper.updateById(sysPost);
    }

    @Override
    public List<SysPost> findAll() {
        List<SysPost> sysPostList = baseMapper.selectList(new LambdaQueryWrapper<SysPost>().eq(SysPost::getStatus, 1));
        return sysPostList;
    }

    @Override
    public IPage<SysPost> findPage(Page<SysPost> postPage, SysPostQueryVo postQueryVo) {
        return baseMapper.findPage(postPage,postQueryVo);
    }
}
