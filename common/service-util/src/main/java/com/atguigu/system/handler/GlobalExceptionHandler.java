package com.atguigu.system.handler;

import com.atguigu.common.result.Result;
import com.atguigu.system.execption.GuiguException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;



/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: handler
 * @Author: hywel
 * @CreateTime: 2023-03-08  20:01
 * @Description: 全局异常处理类
 * @Version: 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(ArithmeticException.class)
    public Result error(ArithmeticException e) {
        e.printStackTrace();
        return Result.fail().message("执行特定异常");
    }

    @ExceptionHandler(GuiguException.class)
    @ResponseBody
    public Result error(GuiguException e) {
        e.printStackTrace();
        return Result.fail().message(e.getMessage()).code(e.getCode());
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result doAccessDeniedException(AccessDeniedException e){
        e.printStackTrace();
        return Result.fail().message("没有权限");
    }
}
