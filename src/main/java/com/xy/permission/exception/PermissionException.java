package com.xy.permission.exception;

/**
 * @author LeoYork
 * @date 2018/8/21 11:10
 * 访问控制异常
 */
public class PermissionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** 需要用的可以用，错误代码51000 */
    private int code = 51000;

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(Exception e) {
        super(e);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
