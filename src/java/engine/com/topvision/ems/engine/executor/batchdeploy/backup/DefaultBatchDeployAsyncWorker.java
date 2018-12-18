/***********************************************************************
 * $Id: DefaultBatchDeployAsyncWorker.java,v1.0 2013年12月2日 下午5:38:34 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor.batchdeploy.backup;

import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013年12月2日-下午5:38:34
 *
 */
public class DefaultBatchDeployAsyncWorker<T> extends AsynchronousSnmpWorker<T> {
    private static final long serialVersionUID = -5528682360520401410L;

    /**
     * @param snmpParam
     * @param data
     * @param params
     */
    public DefaultBatchDeployAsyncWorker(SnmpParam snmpParam, T data, Object[] params) {
        super(snmpParam, data, params);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.engine.executor.batchdeploy.backup.BatchDeployAsyncWorker#exec()
     */
    @Override
    protected void exec() throws Exception {
        snmpUtil.set(result);
    }

}
