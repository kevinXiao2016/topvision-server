/***********************************************************************
 * $Id: RowStatus.java,v1.0 2011-10-17 下午05:28:02 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

/**
 * @author Administrator
 * @created @2011-10-17-下午05:28:02
 * 
 */
public class RowStatus {
    public static Integer ACTIVE = 1;
    public static Integer NOT_IN_SERVICE = 2;
    public static Integer NOT_READY = 3;
    public static Integer CREATE_AND_GO = 4;
    public static Integer CREATE_AND_WAIT = 5;
    public static Integer DESTORY = 6;
}
