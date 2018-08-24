package com.xy.permission.core;

import com.xy.permission.annotation.Permission;
import com.xy.permission.bean.Power;
import com.xy.permission.constant.RefuseType;
import com.xy.permission.exception.PermissionException;
import com.xy.permission.util.PermissionUtil;
import com.xy.permission.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lyh
 * @date 2018/8/21 11:29
 * 拒绝访问核心方法
 */
public enum RefuseCore {

    /** 枚举单例模式 */
    INSTANCE;

    private static Logger logger = LoggerFactory.getLogger(RefuseCore.class);

    private static final String MSG = "权限不足，拒绝访问！";

    /**
     * 直接拒绝访问
     */
    private void refuse(Permission permission) {
        if(RefuseType.EXCEPTION == permission.refuseType()) {
            logger.debug("[PermissionDebugInfo] 权限不足，拒绝访问，抛出异常！");
            throwException();
        }
        logger.debug("[PermissionDebugInfo] 权限不足，拒绝访问！");
    }

    /**
     * 抛出权限不足异常
     */
    private void throwException() {
        throw new PermissionException(MSG);
    }

    private boolean refuse(String resource, Permission permission) {
        if(PermissionUtil.judgeByResource(resource, permission)) {
            refuse(permission);
            return true;
        }
        logger.debug("[PermissionDebugInfo] 拥有权限，正常调用！");
        return false;
    }

    private boolean refuse(Integer level, Permission permission) {
        if(PermissionUtil.judgeByLevel(level, permission)) {
            refuse(permission);
            return true;
        }
        logger.debug("[PermissionDebugInfo] 拥有权限，正常调用！");
        return false;
    }

    /**
     * 判断是否权限不足，不足则拒绝访问
     * @param power 拥有的权限
     * @param permission 方法权限注解
     * @return true需要拦截  false不需要拦截
     */
    public boolean refuse(Power power, Permission permission) {
        if(StringUtil.isNotEmpty(power.getResource())) {
            logger.debug("[PermissionDebugInfo] 通过字符串权限限制！");
            return refuse(power.getResource(), permission);
        } else if(null != power.getLevel()) {
            logger.debug("[PermissionDebugInfo] 通过数字等级限制！");
            return refuse(power.getLevel(), permission);
        }
        return false;
    }

}
