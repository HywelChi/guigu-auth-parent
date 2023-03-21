package com.atguigu.system.service;

import com.atguigu.model.system.SysPost;
import com.atguigu.model.vo.SysPostQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysPostService extends IService<SysPost> {
    void updateStatus(Long id, Integer status);

    List<SysPost> findAll();

    IPage<SysPost> findPage(Page<SysPost> postPage, SysPostQueryVo postQueryVo);
}
