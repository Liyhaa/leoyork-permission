package com.xy.permission.annotation;

import java.lang.annotation.*;

/**
 * @author lyh
 * @date 2018/8/21 10:01
 * 返回的类型里不返回的值
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface RefuseField {
}
