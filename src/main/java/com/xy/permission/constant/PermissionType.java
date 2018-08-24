package com.xy.permission.constant;

/**
 * @author LeoYork
 * @date 2018/8/21 10:32
 * 访问控制类型常量
 */
public enum PermissionType {

    /**
     * REFUSE为拒绝访问
     * RETURN_PART为部分返回
     * LIST_PART为列表部分返回（同时包含RETURN_PART）
     */
    REFUSE, RETURN_PART, LIST_PART;

}
