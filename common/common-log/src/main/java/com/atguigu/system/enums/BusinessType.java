package com.atguigu.system.enums;

/**
 * @BelongsProject: guigu-auth-parent
 * @BelongsPackage: com.atguigu.system.enums
 * @Author: hywel
 * @CreateTime: 2023-03-17  19:20
 * @Description: 业务操作类型
 * @Version: 1.0
 */
public enum BusinessType {
    /**
     * 其他类型
     */
    OTHER,

    /**
     * 新增
     */
    INSERT,

    /**
     * 修改
     */
    UPDATE,

    /**
     * 删除
     */
    DELETE,

    /**
     * 更新状态
     */
    STATUS,

    /**
     * 授权
     */
    ASSIGN
}
