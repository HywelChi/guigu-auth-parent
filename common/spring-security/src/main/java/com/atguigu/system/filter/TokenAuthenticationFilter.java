package com.atguigu.system.filter;

import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.common.util.LoginUserInfoUtil;
import com.atguigu.common.util.ResponseUtil;
import com.atguigu.model.system.SysUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.filter
 * @Author: hywel
 * @CreateTime: 2023-03-15  19:37
 * @Description: 认证解析token过滤器
 * @Version: 1.0
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * @description:处理过滤的方法
     * @author: Hywel
     * @date: 2023/3/16 10:22
     * @param: request
     * @param: response
     * @param: filterChain
     **/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求中的统一资源标识符URI
        String requestURI = request.getRequestURI();
        // 如果URI与登录的请求地址相同，则放行
        if (requestURI.equals("/admin/system/index/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 获取已认证的对象
        Authentication authentication = getAuthenticated(request);
        // 若不为空，则将已认证对象添加到Spring Security上下文中，并放行
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            // 否则返回无权限
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.PERMISSION));
        }
    }

    /**
     * @description:获取已认证的对象的方法
     * @author: Hywel
     * @date: 2023/3/16 10:31
     * @param: request
     * @return: org.springframework.security.core.Authentication
     **/
    private Authentication getAuthenticated(HttpServletRequest request) {
        // 获取请求头中的token
        String token = request.getHeader("token");
        // token不为空，则通过redis获取sysUser对象
        if (token != null) {
            SysUser sysUser = (SysUser) redisTemplate.boundValueOps(token).get();

            LoginUserInfoUtil.setUsername(sysUser.getUsername());
            // 若sysUser不为空，则获取其按钮权限集
            if (sysUser != null) {
                List<String> userPermsList = sysUser.getUserPermsList();
                if (userPermsList != null && userPermsList.size() > 0) {
                    // 将List<String>转化成List<GrantedAuthority>
                    List<GrantedAuthority> authorityList = userPermsList.stream().filter(btnPerm -> !StringUtils.isEmpty(btnPerm.trim())).map(btnPerm -> new SimpleGrantedAuthority(btnPerm)).collect(Collectors.toList());
                    // 返回一个带有权限集的UsernamePasswordAuthenticationToken对象
                    return new UsernamePasswordAuthenticationToken(sysUser.getUsername(), sysUser.getPassword(), authorityList);
                } else {
                    // 返回一个没有权限集的UsernamePasswordAuthenticationToken对象
                    return new UsernamePasswordAuthenticationToken(sysUser.getUsername(), sysUser.getPassword(), Collections.emptyList());
                }
            }
        }
        return null;
    }
}
