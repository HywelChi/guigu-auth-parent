package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysDept;
import com.atguigu.system.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.controller
 * @Author: Hywel
 * @CreateTime: 2023-03-20  20:10
 * @Description: TODO
 * @Version: 1.0
 */
@Api(tags = "部门管理")
@RestController
@RequestMapping("/admin/system/sysDept")
public class SysDeptController {
    @Autowired
    private SysDeptService sysDeptService;

    @ApiOperation("通过id查询")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id){
        SysDept sysDept = sysDeptService.getById(id);
        return Result.ok(sysDept);
    }

    @ApiOperation("更新状态")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status){
         sysDeptService.updateStatus(id,status);
         return Result.ok();
    }

    @ApiOperation("保存")
    @PostMapping("/save")
    public Result save(@RequestBody SysDept sysDept){
        sysDeptService.save(sysDept);
        return Result.ok();
    }

    @ApiOperation("根据id更新")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysDept sysDept){
        sysDeptService.updateById(sysDept);
        return Result.ok();
    }

    @ApiOperation("根据id删除")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id){
        sysDeptService.deleteById(id);
        return Result.ok();
    }
    @ApiOperation("批量删除")
    @DeleteMapping("/batchRemove/{ids}")
    public Result removeById(@PathVariable List ids){
        sysDeptService.removeByIds(ids);
        return Result.ok();
    }
    @ApiOperation("查找全部节点")
    @GetMapping("/findNodes")
    public Result findNodes(){
        List<SysDept> sysDeptList = sysDeptService.findNodes();
        return Result.ok(sysDeptList);
    }

    @ApiOperation("查找用户节点")
    @GetMapping("/findUserNodes")
    public Result findUserNodes(){
        List<SysDept> sysDeptList = sysDeptService.findUserNodes();
        return Result.ok(sysDeptList);
    }



}
