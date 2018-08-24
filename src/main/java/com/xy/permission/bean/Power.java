package com.xy.permission.bean;

import java.io.Serializable;

/**
 * @author lyh
 * @date 2018/8/21 09:58
 * 调用者拥有的权限类
 */
public class Power implements Serializable {

    /** 字符串形式权限 */
    private String resource;

    /** 数字形式权限 */
    private Integer level;

    public Power(String resource) {
        this.resource = resource;
    }

    public Power(Integer level) {
        this.level = level;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
