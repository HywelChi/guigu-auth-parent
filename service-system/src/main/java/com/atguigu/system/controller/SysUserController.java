package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.common.util.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.SysUserQueryVo;
import com.atguigu.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.controller
 * @Author: hywel
 * @CreateTime: 2023-03-12  13:28
 * @Description: TODO
 * @Version: 1.0
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    /*
     * @description:mapper方式
     * @author: Hywel
     * @date: 2023/3/13 10:03
     * @param: current
     * @param: size
     * @param: queryVo
     * @return: com.atguigu.common.result.Result
     **/
//    @ApiOperation("分页及带条件查询")
//    @GetMapping("/{current}/{size}")
//    public Result findPageList(@PathVariable Long current, @PathVariable Long size, SysUserQueryVo queryVo){
//        Page<SysUser> userPage = new Page<>(current, size);
//        IPage<SysUser> page = sysUserService.findPage(userPage,queryVo);
//        return Result.ok(page);
//    }
    /*
     * @description:使用自带的分页方法，需要自己封装查询条件
     * @author: Hywel
     * @date: 2023/3/13 10:53
     * @param: current 当前页码
     * @param: size 每页显示的数据大小
     * @param: queryVo 查询条件
     * @return: com.atguigu.common.result.Result
     **/
    @ApiOperation("分页及带条件查询")
    @GetMapping("/{current}/{size}")
    public Result findPageList(@PathVariable Long current, @PathVariable Long size, SysUserQueryVo queryVo) {
        Page<SysUser> userPage = new Page<>(current, size);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        // 降序排列
        queryWrapper.orderByDesc("id");

        // 创建时间大于等于开始时间
        if (!StringUtils.isEmpty(queryVo.getCreateTimeBegin())) {
            queryWrapper.gt("create_time", queryVo.getCreateTimeBegin());
        }
        // 创建时间小于等于开始时间
        if (!StringUtils.isEmpty(queryVo.getCreateTimeEnd())) {
            queryWrapper.lt("create_time", queryVo.getCreateTimeEnd());
        }
        // 关键字的模糊查询
        String keyword = queryVo.getKeyword();
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like("username", keyword)
                    .or().like("name", keyword)
                    .or().eq("phone", keyword);
        }
        Page<SysUser> page = sysUserService.page(userPage, queryWrapper);
        return Result.ok(page);
    }

    @ApiOperation("根据id删除")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        boolean b = sysUserService.removeById(id);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("添加")
    @PostMapping("save")
    public Result save(@RequestBody SysUser sysUser) {
        //将用户密码通过MD5加密
        sysUser.setPassword(MD5.encrypt(sysUser.getPassword()));
        //设置一个默认的头像
        sysUser.setHeadUrl("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        boolean save = sysUserService.save(sysUser);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }
    @ApiOperation("根据id查询")
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable Long id) {
        SysUser sysUser = sysUserService.getById(id);
        return Result.ok(sysUser);
    }
    @ApiOperation("更新")
    @PutMapping("/update")
    public Result update(@RequestBody SysUser sysUser) {
        sysUser.setUpdateTime(null);
        boolean b = sysUserService.updateById(sysUser);

        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("更新用户状态")
    @GetMapping("/updateStatus/{userId}/{status}")
    public Result updateStatus(@PathVariable Long userId, @PathVariable Integer status) {
        sysUserService.updateStatus(userId, status);
        return Result.ok();
    }
}
