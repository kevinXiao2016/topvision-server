/***********************************************************************
 * $Id: BatchDeployAsyncWorker.java,v1.0 2013年12月2日 下午3:18:53 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor.batchdeploy.backup;

import com.topvision.framework.snmp.SnmpDataProxy;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author Bravin
 * @created @2013年12月2日-下午3:18:53
 *
 */
public abstract class AsynchronousSnmpWorker<T> extends SnmpWorker<T> {
    private static final long serialVersionUID = -813519689775588276L;
    protected final SnmpDataProxy<T> proxy;

    /**
     * @param snmpParam
     * @param bindTable 
     */
    public AsynchronousSnmpWorker(SnmpParam snmpParam, T data, Object... params) {
        super(snmpParam);
        proxy = new SnmpDataProxy<T>();
        proxy.setData(data);
        if (params != null) {
            proxy.setParams(params);
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.snmp.SnmpWorker#exec()
     */
    @Override
    protected abstract void exec() throws Exception;

}
