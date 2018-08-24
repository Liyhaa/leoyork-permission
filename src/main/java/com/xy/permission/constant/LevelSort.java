package com.xy.permission.constant;

/**
 * @author LeoYork
 * @date 2018/8/21 10:45
 * 访问控制排序常量
 */
public enum LevelSort {

    /**
     * ASC为升序，即level越大权限越高，大于等于value值则拥有权限
     * DESC为降序，即level越小权限越高，小于等于value值则拥有权限
     */
    ASC, DESC;

}
