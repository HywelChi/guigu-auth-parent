package com.atguigu.system.config;

import com.atguigu.system.filter.TokenAuthenticationFilter;
import com.atguigu.system.filter.TokenLoginFilter;
import com.atguigu.system.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.config
 * @Author: hywel
 * @CreateTime: 2023-03-15  19:17
 * @Description: 自定义过滤器的配置类
 * @Version: 1.0
 */
@SpringBootConfiguration
@EnableWebSecurity// @EnableWebSecurity是开启SpringSecurity的默认行为
@EnableGlobalMethodSecurity(prePostEnabled = true)// 开启注解功能，默认禁用注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private LoginLogService loginLogService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 这是配置的关键，决定哪些接口开启防护，哪些接口绕过防护
        http
                // 关闭跨域请求伪造功能（CSRF）
                .csrf().disable()
                // 添加TokenLoginFilter过滤器
                .addFilter(new TokenLoginFilter(authenticationManager(), redisTemplate, loginLogService))
                // 设置TokenAuthenticationFilter在UsernamePasswordAuthenticationFilter之前执行,
                // 这样做就是为了除了登录的时候去查询数据库外，其他时候都用token进行认证。
                .addFilterBefore(new TokenAuthenticationFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class)
                // 开启跨域以便前端调用接口
                .cors().and()
                .authorizeRequests()
                .antMatchers("/admin/system/index/login").permitAll()
                .anyRequest().authenticated();

        // 禁用session
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


    }

    /**
     * 配置哪些请求不拦截
     * 排除swagger相关请求
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favicon.ico", "/swagger-resources/**", "/webjars/**", "/v2/**", "/doc.html");
    }
}
