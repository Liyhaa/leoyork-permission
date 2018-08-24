package com.xy.permission.annotation;

import com.xy.permission.constant.LevelSort;
import com.xy.permission.constant.PermissionType;
import com.xy.permission.constant.RefuseType;

import java.lang.annotation.*;

/**
 * @author lyh
 * @date 2018/8/21 09:50
 * 权限控制注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Permission {

    /** level权限需求 包含值在内 默认为0 */
    int value() default 0;

    /** level权限需求排序大小 asc升序即level越高权限越大 desc相反 默认为asc */
    LevelSort sort() default LevelSort.ASC;

    /** 字符串形式权限需求 需要什么权限，多个使用逗号分隔 默认为空 */
    String resource() default "";

    /** 控制形式，REFUSE拒绝调用，RETURN_PART部分返回，LIST_PART列表部分返回 */
    PermissionType type() default PermissionType.REFUSE;

    /** 拒绝调用的拒绝形式，EXCEPTION抛出异常，NULL_OBJECT返回null值 */
    RefuseType refuseType() default RefuseType.EXCEPTION;

}
