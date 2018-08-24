package com.xy.permission.aspect;

import com.xy.permission.annotation.Permission;
import com.xy.permission.bean.Power;
import com.xy.permission.constant.PermissionType;
import com.xy.permission.core.ListPartCore;
import com.xy.permission.core.RefuseCore;
import com.xy.permission.core.ReturnPartCore;
import com.xy.permission.util.PermissionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lyh
 * @date 2018/8/21 13:35
 */
public class PermissionAspect {

    private static Logger logger = LoggerFactory.getLogger(PermissionAspect.class);

    private RefuseCore refuseCore = RefuseCore.INSTANCE;

    private ReturnPartCore returnPartCore = ReturnPartCore.INSTANCE;

    private ListPartCore listPartCore = ListPartCore.INSTANCE;

    public Object around(ProceedingJoinPoint pjp, Permission permission) throws Throwable{

        logger.debug("[PermissionDebugInfo] 进入权限控制拦截器......");

        Object result = null;
        Object[] args = pjp.getArgs();
        Power power = PermissionUtil.getPower(args);
        PermissionType permissionType = permission.type();

        if(null != power) {
            switch (permissionType) {
                case REFUSE:
                    logger.debug("[PermissionDebugInfo] 权限类型为拒绝访问......");
                    if(!refuseCore.refuse(power, permission)) {
                        result = pjp.proceed(args);
                    }
                    break;

                case RETURN_PART:
                    logger.debug("[PermissionDebugInfo] 权限类型为部分屏蔽......");
                    result = pjp.proceed(args);
                    result = returnPartCore.filter(power, permission, result);
                    break;

                case LIST_PART:
                    logger.debug("[PermissionDebugInfo] 权限类型为部分列表......");
                    result = pjp.proceed(args);
                    result = listPartCore.filter(power, permission, result);
                    break;

                default:
                    logger.error("[PermissionException] 传入空的权限类型，不应该到达这里......");
                    result = pjp.proceed(args);
            }
        }

        logger.debug("[PermissionDebugInfo] 退出权限控制拦截器......");
        return result;

    }

}
