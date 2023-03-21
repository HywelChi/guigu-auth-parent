package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysPost;
import com.atguigu.model.vo.SysPostQueryVo;
import com.atguigu.system.service.SysPostService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.controller
 * @Author: Hywel
 * @CreateTime: 2023-03-20  22:43
 * @Description: TODO
 * @Version: 1.0
 */
@Api(tags = "岗位管理")
@RestController
@RequestMapping("/admin/system/sysPost")
public class SysPostController {
    @Autowired
    private SysPostService sysPostService;

    @ApiOperation("带条件的分页查询")
    @GetMapping("/{current}/{size}")
    public Result getPageList(@PathVariable Long current, @PathVariable Long size, SysPostQueryVo postQueryVo) {
        Page<SysPost> page = new Page<>(current, size);

        IPage<SysPost> iPage = sysPostService.findPage(page, postQueryVo);
        return Result.ok(iPage);
    }

    @ApiOperation("根据id查询")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id) {
        SysPost sysPost = sysPostService.getById(id);
        return Result.ok(sysPost);
    }

    @ApiOperation("查询所有")
    @GetMapping("/findAll")
    public Result findAll() {
        List<SysPost> list = sysPostService.findAll();
        return Result.ok(list);
    }

    @ApiOperation("保存")
    @PostMapping("/save")
    public Result save(@RequestBody SysPost sysPost) {
        sysPostService.save(sysPost);
        return Result.ok();
    }

    @ApiOperation("根据id更新")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysPost sysPost) {
        sysPostService.updateById(sysPost);
        return Result.ok();
    }

    @ApiOperation("根据id删除")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysPostService.removeById(id);
        return Result.ok();
    }

    @ApiOperation("根据id更新状态")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        sysPostService.updateStatus(id, status);
        return Result.ok();
    }
}
