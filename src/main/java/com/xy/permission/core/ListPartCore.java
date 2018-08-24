package com.xy.permission.core;

import com.xy.permission.annotation.BlockField;
import com.xy.permission.annotation.Permission;
import com.xy.permission.bean.Power;
import com.xy.permission.constant.LevelSort;
import com.xy.permission.util.PermissionUtil;
import com.xy.permission.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author lyh
 * @date 2018/8/22 10:34
 * 列表部分返回核心方法
 */
public enum ListPartCore {

    /** 枚举单例模式 */
    INSTANCE;

    private static Logger logger = LoggerFactory.getLogger(RefuseCore.class);

    private ReturnPartCore returnPartCore = ReturnPartCore.INSTANCE;

    private boolean ifBlock(Field field, Object object, Permission permission, Power power) {
        if(null != field.getAnnotation(BlockField.class)) {
            logger.debug("[PermissionDebugInfo] 部分列表：{}属性为权限属性......", field.getName());
            try {
                Method getter = PermissionUtil.getGetter(field, object);
                if(getter != null) {
                    Object permit = getter.invoke(object);
                    if(null == permit) {
                        logger.debug("[PermissionDebugInfo] 部分列表：需要的权限为空......");
                        return false;
                    }
                    logger.debug("[PermissionDebugInfo] 部分列表：getter方法获取到的值={}......", permit.toString());
                    if(permit instanceof String && StringUtil.isNotEmpty(power.getResource())) {
                        String permitString = (String) permit;
                        logger.debug("[PermissionDebugInfo] 部分列表：拥有的权限={}，需要的权限={}......", power.getResource(), permitString);
                        String[] powers = permitString.replaceAll(PermissionUtil.WHITE_SPACE, "").split(PermissionUtil.REGEX);
                        List<String> powerList = Arrays.asList(powers);
                        for (String s : power.getResource().replaceAll(PermissionUtil.WHITE_SPACE, "").split(PermissionUtil.REGEX)) {
                            if(powerList.contains(s)) {
                                return false;
                            }
                        }
                        return true;
                    } else if(permit instanceof Number && null != power.getLevel()) {
                        Integer castPermit = Integer.parseInt(permit.toString());
                        logger.debug("[PermissionDebugInfo] 部分列表：拥有的权限={}，需要的权限={}......", power.getLevel(), castPermit);
                        if(LevelSort.ASC == permission.sort()) {
                            return power.getLevel() < castPermit;
                        } else if(LevelSort.DESC == permission.sort()) {
                            return power.getLevel() > castPermit;
                        }
                        return false;
                    }
                }
            } catch (Exception e) {
                logger.debug("[PermissionException] 调用getter方法失败，field={},Exception={}", field.getName(), e);
            }
        }
        return false;
    }

    private List iteratorList(List list, Permission permission, Power power) {
        List<Object> result = new ArrayList<Object>();
        boolean ifBlock;
        for (Object object : list) {
            ifBlock = false;
            for (Field field : object.getClass().getDeclaredFields()) {
                if(ifBlock = ifBlock(field, object, permission, power)) {
                    break;
                }
            }
            if(!ifBlock) {
                if(!result.contains(object)) {
                    result.add(object);
                }
                logger.debug("[PermissionDebugInfo] 部分列表：权限足够......");
            } else {
                logger.debug("[PermissionDebugInfo] 该项权限不足，不返回！");
            }
            returnPartCore.filter(power, permission, object);
        }
        return result;
    }

    private Object filter(Object arg, Permission permission, Power power) {
        if(arg instanceof List) {
            logger.debug("[PermissionDebugInfo] 部分列表：返回值为List......");
            List objectList = (List) arg;
            arg = iteratorList(objectList, permission, power);
        } else {
            Map<Field, List> map = PermissionUtil.findList(arg);
            if(0 < map.size()) {
                logger.debug("[PermissionDebugInfo] 部分列表：List在返回值属性中......");
                for (Map.Entry<Field, List> entry : map.entrySet()) {
                    Method setter = PermissionUtil.getSetter(entry.getKey(), arg);
                    if(setter != null) {
                        try {
                            setter.invoke(arg, iteratorList(entry.getValue(), permission, power));
                        } catch (Exception e) {
                            logger.debug("[PermissionException] 调用setter方法失败，field={},Exception={}", entry.getKey().getName(), e);
                        }
                    }
                }
            } else {
                logger.debug("[PermissionDebugInfo] 部分列表：返回值中未找到列表......");
            }
        }
        return arg;
    }

    public Object filter(Power power, Permission permission, Object result) {
        result = filter(result, permission, power);
        return result;
    }

}
