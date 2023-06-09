package com.atguigu.system.annotation;

import com.atguigu.system.enums.BusinessType;
import com.atguigu.system.enums.OperatorType;

import java.lang.annotation.*;

/**
 * @description:自定义操作日志记录注解
 * @author: Hywel
 * @date: 2023/3/17 19:33
 **/
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    // 模块名称
    String title() default "";

    // 业务操作类型
    BusinessType businessType() default BusinessType.OTHER;

    // 操作人类别
    OperatorType operatorType() default OperatorType.MANAGE;

    // 是否保存请求参数
    boolean isSaveRequestData() default false;

    // 是否保存响应数据
    boolean isSaveResponseData() default true;
}
