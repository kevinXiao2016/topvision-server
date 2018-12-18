/***********************************************************************
 * $Id: ItServiceExceptionCode.java,v1.0 2016年4月18日 上午11:06:34 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.domain;

import java.io.Serializable;

/**
 * 从异常中获取错误信息的接口类
 * 
 * @author vanzand
 * @created @2016年4月18日-上午11:06:34
 *
 */
public interface ExceptionCode extends Serializable {
    public Integer getCode();

    public String getMessage();

}
