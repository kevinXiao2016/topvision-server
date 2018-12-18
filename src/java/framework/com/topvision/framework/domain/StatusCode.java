/***********************************************************************
 * $Id: StatusCode.java,v1.0 2015年11月30日 下午3:21:15 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.domain;

/**
 * @author fanzidong
 * @created @2015年11月30日-下午3:21:15
 *
 */
public enum StatusCode {
    OK(200, "操作成功"),
    BadRequest(400, "从请求中读取数据失败"),
    Unauthorized(401, "您尚未登录或登录已失效"),
    InternalServerError(500, "服务器内部错误"),
    RoleNotExists(1001, "角色不存在");

    private int code;
    private String message;

    private StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
