/***********************************************************************
 * $ ExecScheduler.java,v1.0 2012-5-2 17:19:15 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.exec;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author jay
 * @created @2012-5-2-17:19:15
 */
public interface ExecScheduler<T extends OperClass> extends Runnable {
    // 设置采集参数
    void setSnmpParam(SnmpParam snmpParam);

    // 设置DOMAIN
    void setOperClass(T operClass);
}
