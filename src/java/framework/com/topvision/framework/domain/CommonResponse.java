/***********************************************************************
 * $Id: CommonResponse.java,v1.0 2015年11月30日 下午3:17:41 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.domain;

/**
 * @author fanzidong
 * @created @2015年11月30日-下午3:17:41
 *
 */
public class CommonResponse {
    private Object data; // 响应的数据都在这里
    private Integer code = StatusCode.OK.getCode(); // 响应码
    private String msg = StatusCode.OK.getMessage();// 响应码对应的信息
    private Integer rowCount;  //表格读取分页信息时有用，总条数，其他情况下忽略

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void fillStatusCode(ExceptionCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMessage();
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

}
