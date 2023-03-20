package com.atguigu.system.aspect;

import com.alibaba.fastjson.JSON;
import com.atguigu.common.util.IpUtil;
import com.atguigu.common.util.LoginUserInfoUtil;
import com.atguigu.model.system.SysOperLog;
import com.atguigu.system.annotation.Log;
import com.atguigu.system.service.OperLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.aspect
 * @Author: Hywel
 * @CreateTime: 2023-03-17  19:47
 * @Description: 日志切面
 * @Version: 1.0
 */
@Aspect
@Component
public class LogAspect {
    @Autowired
    private OperLogService operLogService;

    /**
     * @description:返回通知
     * @author: Hywel
     * @date: 2023/3/17 19:57
     * @param: joinPoint 切点
     * @param: controllerLog
     * @param: jsonResult
     **/
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturn(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handelLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * @description:异常通知
     * @author: Hywel
     * @date: 2023/3/17 19:57
     * @param: joinPoint
     * @param: controllerLog
     * @param: e
     **/
    @AfterThrowing(pointcut = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handelLog(joinPoint, controllerLog, e, null);
    }

    private void handelLog(JoinPoint joinPoint, Log controllerLog, Exception e, Object jsonResult) {

        SysOperLog sysOperLog = new SysOperLog();
        // 设置日志状态
        sysOperLog.setStatus(1);
        if (e != null) {
            sysOperLog.setStatus(0);
            sysOperLog.setErrorMsg(e.getMessage());
        }

        /*
         * joinPoint对象
         * request对象
         * controllerLog对象
         **/
        // 获取HttpServletRequest对象
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        // 设置请求方式
        String method = request.getMethod();
        sysOperLog.setRequestMethod(method);

        // 设置请求地址
        String requestURI = request.getRequestURI();
        sysOperLog.setOperUrl(requestURI);

//        String localAddr = request.getLocalAddr();
//        sysOperLog.setOperIp(localAddr);
        // 设置ip地址
        String ipAddress = IpUtil.getIpAddress(request);
        sysOperLog.setOperIp(ipAddress);

        // 设置方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        sysOperLog.setMethod(className + "." + methodName + "()");

        // 设置操作模块、操作类型、操作人
        sysOperLog.setTitle(controllerLog.title());
        sysOperLog.setBusinessType(controllerLog.businessType().name());
        String username = LoginUserInfoUtil.getUsername();
        sysOperLog.setOperName(username);


        // 判断是否保存请求数据
        if (controllerLog.isSaveRequestData()) {
            setRequestData(joinPoint, sysOperLog);
        }
        // 判断是否保存响应数据
        if (controllerLog.isSaveResponseData() && !StringUtils.isEmpty(jsonResult)) {
            sysOperLog.setJsonResult(JSON.toJSONString(jsonResult));
        }

        operLogService.saveSysOperLog(sysOperLog);
    }

    /**
     * 设置请求数据的方法
     *
     * @param joinPoint
     * @param sysOperLog
     */
    private void setRequestData(JoinPoint joinPoint, SysOperLog sysOperLog) {
        String requestMethod = sysOperLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            sysOperLog.setOperParam(params);
        }
    }

    /**
     * 参数拼装的方法
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (!StringUtils.isEmpty(o) && !isFilterObject(o)) {
                    try {
                        Object jsonObj = JSON.toJSON(o);
                        params += jsonObj.toString() + " ";
                    } catch (Exception e) {
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
