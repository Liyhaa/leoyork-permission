package com.xy.permission.annotation;

import java.lang.annotation.*;

/**
 * @author lyh
 * @date 2018/8/22 10:29
 * 列表部分返回时用于判断权限的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface BlockField {
}
