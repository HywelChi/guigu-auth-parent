package com.atguigu.system.filter;

import com.atguigu.common.result.Result;
import com.atguigu.common.util.IpUtil;
import com.atguigu.common.util.ResponseUtil;
import com.atguigu.model.vo.LoginVo;
import com.atguigu.system.custom.CustomUser;
import com.atguigu.system.service.LoginLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.custom.filter
 * @Author: hywel
 * @CreateTime: 2023-03-15  18:46
 * @Description: TODO
 * @Version: 1.0
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    private RedisTemplate redisTemplate;

    @Autowired
    private LoginLogService loginLogService;

    public TokenLoginFilter(AuthenticationManager authenticationManager, RedisTemplate redisTemplate,LoginLogService loginLogService) {
        // 设置认证管理器
        this.setAuthenticationManager(authenticationManager);
        // 设置请求地址以及请求方式
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login", "POST"));
        // 为redisTemplate赋值
        this.redisTemplate = redisTemplate;
        this.loginLogService = loginLogService;
    }

    /**
     * @description:开始认证的方法
     * @author: Hywel
     * @date: 2023/3/16 9:54
     * @param: request
     * @param: response
     * @return: org.springframework.security.core.Authentication
     **/
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 通过ObjectMapper读取前端传过来的LoginVo对象
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
            // 创建未认证的对象
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            // 将未认证对象交给AuthenticationManager认证
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * @description:认证成功的方法
     * @author: Hywel
     * @date: 2023/3/16 9:58
     * @param: request
     * @param: response
     * @param: chain
     * @param: authResult
     **/
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 获取自定义customUser对象
        CustomUser customUser = (CustomUser) authResult.getPrincipal();
        // 通过UUID生成token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        // 将生成的token通过redisTemplate放到sysUser对象中
        redisTemplate.boundValueOps(token).set(customUser.getSysUser(), 2, TimeUnit.HOURS);
        loginLogService.save(customUser.getSysUser().getUsername(), IpUtil.getIpAddress(request), 1, "登陆成功");
        // 存入map
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        // 通过自定义的ResponseUtil将请求响应给前端
        ResponseUtil.out(response, Result.ok(map));
    }

    /**
     * @description:认证失败的方法
     * @author: Hywel
     * @date: 2023/3/16 10:10
     * @param: request
     * @param: response
     * @param: failed
     **/
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 响应失败消息
        ResponseUtil.out(response, Result.build(null, 110, failed.getMessage()));
    }
}
