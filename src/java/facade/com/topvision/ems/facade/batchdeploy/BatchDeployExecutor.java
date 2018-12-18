/***********************************************************************
 * $Id: BatchDeployExecutor.java,v1.0 2013年11月30日 下午3:20:25 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.batchdeploy;

import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013年11月30日-下午3:20:25
 *
 */
public interface BatchDeployExecutor<T, V> {

    /**
     * 每个需要做批量配置单元都必须实现的一个接口
     * @param target
     * @param bundle
     * @param snmpParam
     * @return
     */
    boolean deploy(T target, V bundle, SnmpParam snmpParam);
}
