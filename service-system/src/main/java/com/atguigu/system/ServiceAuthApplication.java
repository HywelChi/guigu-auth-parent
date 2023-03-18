package com.atguigu.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system
 * @Author: hywel
 * @CreateTime: 2023-03-08  09:49
 * @Description: TODO
 * @Version: 1.0
 */
@SpringBootApplication
@MapperScan(basePackages = "com.atguigu.system.mapper")
public class ServiceAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthApplication.class, args);
    }
}
