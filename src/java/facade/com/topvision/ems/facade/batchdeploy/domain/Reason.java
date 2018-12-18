/***********************************************************************
 * $Id: Reason.java,v1.0 2013年12月3日 下午5:26:16 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.batchdeploy.domain;


/**
 * 枚举批量配置失败的原因
 * @author Bravin
 * @created @2013年12月3日-下午5:26:16
 *
 */
public final class Reason {
    /** 网络不通 */
    public static final int SNMP_NO_RESPONSE = 1;
    /** 业务冲突,这种一般是NOT_WRITABLE，重复配置，端口已绑定等  */
    public static final int SERVICE_CONFLICT = 2;
    /** 内部错误,指网管自身业务处理有误，比如空指针，下标越界等异常 */
    public static final int INTERVAL_ERROR = 3;
    /** 内部错误,指网管自身业务处理有误，比如空指针，下标越界等异常 */
    public static final int LINK_ERROR = 4;
}
