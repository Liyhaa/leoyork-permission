package com.xy.permission.util;

import com.xy.permission.annotation.Permission;
import com.xy.permission.bean.Power;
import com.xy.permission.constant.LevelSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author lyh
 * @date 2018/8/22 10:24
 * 权限控制工具类
 */
public class PermissionUtil {

    public static final String REGEX = ",";
    public static final String WHITE_SPACE = " ";

    private static Logger logger = LoggerFactory.getLogger(PermissionUtil.class);

    public static Map<Field, List> findList(Object object) {
        Map<Field, List> map = new HashMap<Field, List>();
        for (Field field : object.getClass().getDeclaredFields()) {
            if(List.class.equals(field.getType())) {
                try {
                    Method getter = getGetter(field, object);
                    if(null != getter) {
                        map.put(field, (List) getter.invoke(object));
                    }
                } catch (Exception e) {
                    logger.debug("[PermissionException] 调用getter方法失败，field={},Exception={}", field.getName(), e);
                }
            }
        }
        return map;
    }

    public static Method getGetter(Field field, Object object){
        String getterName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        try {
            return object.getClass().getDeclaredMethod(getterName);
        } catch (NoSuchMethodException e) {
            logger.debug("[PermissionException] 获取getter方法失败，field={},Exception={}", field.getName(), e);
        }
        return null;
    }

    public static Method getSetter(Field field, Object object){
        String setterName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        try {
            return object.getClass().getDeclaredMethod(setterName, field.getType());
        } catch (NoSuchMethodException e) {
            logger.debug("[PermissionException] 获取setter方法失败，field={},Exception={}", field.getName(), e);
        }
        return null;
    }

    /**
     * 从参数中获取Power
     * @param args 参数
     * @return Power对象
     */
    public static Power getPower(Object[] args) {
        for (Object arg : args) {
            if(arg instanceof Power) {
                return (Power) arg;
            }
        }
        return null;
    }

    /**
     * 通过字符串判断是否权限不足
     * @param resource 拥有的权限
     * @param permission 需要的权限
     * @return true 权限不足  false权限足够
     */
    public static boolean judgeByResource(String resource, Permission permission) {
        logger.debug("[PermissionDebugInfo] 拥有权限={}，需要权限={}", resource, permission.resource());
        String[] powers = permission.resource().replaceAll(WHITE_SPACE, "").split(REGEX);
        List<String> powerList = Arrays.asList(powers);
        for (String s : resource.trim().split(REGEX)) {
            if(powerList.contains(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 通过字符串判断是否权限不足
     * @param level 拥有的权限
     * @param permission 需要的权限
     * @return true 权限不足  false权限足够
     */
    public static boolean judgeByLevel(Integer level, Permission permission) {
        logger.debug("[PermissionDebugInfo] 拥有权限={}，需要权限={}", level, permission.value());
        if(LevelSort.ASC == permission.sort()) {
            logger.debug("[PermissionDebugInfo] 等级越高权限越大");
            return level < permission.value();
        } else if(LevelSort.DESC == permission.sort()) {
            logger.debug("[PermissionDebugInfo] 等级越低权限越大");
            return level > permission.value();
        }
        return false;
    }

}
