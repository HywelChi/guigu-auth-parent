package com.atguigu.system.service;

import com.atguigu.model.system.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysDeptService extends IService<SysDept> {
    void updateStatus(Long id, Integer status);

    List<SysDept> findNodes();

    List<SysDept> findUserNodes();

    void deleteById(Long id);
}
