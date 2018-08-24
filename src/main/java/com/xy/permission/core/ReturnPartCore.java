package com.xy.permission.core;

import com.xy.permission.annotation.Permission;
import com.xy.permission.annotation.RefuseField;
import com.xy.permission.bean.Power;
import com.xy.permission.util.PermissionUtil;
import com.xy.permission.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author lyh
 * @date 2018/8/21 11:37
 * 部分返回核心方法
 */
public enum ReturnPartCore {

    /** 枚举单例模式 */
    INSTANCE;

    private static Logger logger = LoggerFactory.getLogger(ReturnPartCore.class);

    public void filter(Field field, Object target) {
        if(null != field.getAnnotation(RefuseField.class)) {
            logger.debug("[PermissionDebugInfo] 权限不足字段：{}！", field.getName());
            safetySet(field, target);
        }
    }

    private <T> T filter(T target){
        if(target instanceof List) {
            List list = (List) target;
            for (Object object : list) {
                filter(object);
            }
        } else {
            Class clazz = target.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                filter(field, target);
            }
        }
        return target;
    }

    private void safetySet(Field field, Object source) {
        Method method = getSetter(source, field.getName(), field.getType());
        if(null != method) {
            try {
                method.invoke(source, (Object) null);
            } catch (Exception e) {
                logger.debug("[PermissionException] 调用setter方法失败，field={}", field.getName());
            }
        }
    }

    private Method getSetter(Object source, String fieldName, Class clazz) {
        String methodName = getSetter(fieldName);
        try {
            return source.getClass().getMethod(methodName, clazz);
        } catch (NoSuchMethodException e) {
            logger.debug("[PermissionException] 获取setter方法失败，field={}", fieldName);
            return null;
        }
    }

    private String getSetter(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }



    private Object filter(String resource, Permission permission, Object result) {
        if(PermissionUtil.judgeByResource(resource, permission)) {
            result = filter(result);
        }
        return result;
    }

    private Object filter(Integer level, Permission permission, Object result) {
        if(PermissionUtil.judgeByLevel(level, permission)) {
            result = filter(result);
        }
        return result;
    }

    public Object filter(Power power, Permission permission, Object result) {
        if(StringUtil.isNotEmpty(power.getResource())) {
            logger.debug("[PermissionDebugInfo] 通过字符串权限限制！");
            result = filter(power.getResource(), permission, result);
        } else if(null != power.getLevel()) {
            logger.debug("[PermissionDebugInfo] 通过数字等级限制！");
            result = filter(power.getLevel(), permission, result);
        }
        return result;
    }
}
