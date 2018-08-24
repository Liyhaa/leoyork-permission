package com.xy.permission.util;

/**
 * @author lyh
 * @date 2018/8/22 11:58
 * 字符串工具类
 */
public class StringUtil {

    public static boolean isNotEmpty(String s) {
        return null != s && !"".equals(s);
    }

    public static boolean isEmpty(String s) {
        return null == s || "".equals(s);
    }

}
