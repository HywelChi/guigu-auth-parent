package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.common.util.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.LoginVo;
import com.atguigu.system.execption.GuiguException;
import com.atguigu.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.controller
 * @Author: hywel
 * @CreateTime: 2023-03-10  16:36
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/admin/system/index")
@Api(tags = "后台登录管理")
public class IndexController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/login")
    @ApiOperation("登录")
    @PreAuthorize("hasAuthority('bnt.sysDept.list')")
    public Result login(@RequestBody LoginVo loginVo) {
        //获取用户名和密码
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();

        // 判断用户名和密码是否为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new GuiguException(10000, "用户名或密码为空");
        }
        // 调用SysUserService中根据用户名查询用户的方法
        SysUser sysUser = sysUserService.getSysUserByUsername(username);
        // 判断用户是否为空
        if (sysUser == null) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
        }
        // 判断密码是否正确
        if (!MD5.encrypt(password).equals(sysUser.getPassword())) {
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
        }
        // 判断用户是否可用(1可用 0停用)
        if (sysUser.getStatus().intValue() == 0) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_STOP);
        }
        // 使用UUID生成一个随机字符串作为token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        // 将用户信息保存到Redis中，有效时间是2个小时
        redisTemplate.boundValueOps(token).set(sysUser, 2, TimeUnit.HOURS);
        // 创建一个Map
        Map<String, Object> map = new HashMap<>();
        //给前端响应的token的值不能包含中文
        map.put("token", token);
        return Result.ok(map);
    }

//    @ApiOperation("获取用户信息")
//    @GetMapping("/info")
//    public Result info() {
//        Map map = new HashMap();
//        map.put("roles", "[admin]");
//        map.put("name", "admin");
//        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
//        return Result.ok(map);
//    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result info(HttpServletRequest request) {
        String token = request.getHeader("token");
        SysUser sysUser = (SysUser) redisTemplate.boundValueOps(token).get();
        Map<String, Object> map = sysUserService.getUserInfoByUserId(sysUser.getId());
        map.put("name", sysUser.getUsername());
        map.put("avatar", sysUser.getHeadUrl());
        return Result.ok(map);
    }

    @ApiOperation("注销")
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        redisTemplate.delete(token);
        return Result.ok();
    }
}
