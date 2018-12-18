/***********************************************************************
 * $Id: SnmpFacadeImpl.java,v 1.1 Oct 25, 2008 5:04:55 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.executor;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.facade.SnmpFacade;
import com.topvision.ems.facade.domain.FormulaBinding;
import com.topvision.ems.facade.domain.SnmpData;
import com.topvision.ems.facade.domain.SnmpMonitorParam;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @Create Date Oct 25, 2008 5:04:55 PM
 * 
 * @author kelers
 * 
 */
@Facade("snmpFacade")
public class SnmpFacadeImpl extends EmsFacade implements SnmpFacade {
    private class MonitorSnmpWorker extends SnmpWorker<SnmpMonitorParam> {
        private static final long serialVersionUID = -5861200856992660892L;

        /**
         * @param snmpParam
         */
        public MonitorSnmpWorker(SnmpParam snmpParam) {
            super(snmpParam);
        }

        /**
         * get data from device
         * 
         * @param binding
         */
        private void getData(FormulaBinding binding) {
            switch (binding.getType()) {
            case FormulaBinding.TYPE_LEAF:
                try {
                    binding.setSingle(snmpUtil.get(binding.getOid()));
                } catch (Exception ex) {
                    binding.setError(ex.getMessage());
                }
                break;
            case FormulaBinding.TYPE_LEAFS:
                try {
                    binding.setList(snmpUtil.get(binding.getOids()));
                } catch (Exception ex) {
                    binding.setError(ex.getMessage());
                }
                break;
            case FormulaBinding.TYPE_COLUMN:
                try {
                    binding.setTable(snmpUtil.getTable(binding.getOids()));
                } catch (Exception ex) {
                    binding.setError(ex.getMessage());
                }
                break;
            case FormulaBinding.TYPE_TABLE:
                try {
                    binding.setTable(snmpUtil.getTable(binding.getOid()));
                } catch (Exception ex) {
                    binding.setError(ex.getMessage());
                }
                break;
            default:
                binding.setError("Unknown type!");
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.topvision.framework.snmp.SnmpWorker#exec()
         */
        @Override
        protected void exec() {
            snmpUtil.reset(result.getSnmpParam());
            if (!snmpUtil.verifySnmpVersion()) {
                result.setError("The device no snmp supported!");
                return;
            }
            for (Iterator<FormulaBinding> itr = result.iteratorBindings(); itr.hasNext();) {
                getData(itr.next());
            }
        }
    }

    private class StandardSnmpWorker extends SnmpWorker<SnmpData> {
        private static final long serialVersionUID = -4269451201115353489L;

        /**
         * @param snmpParam
         */
        public StandardSnmpWorker(SnmpParam snmpParam) {
            super(snmpParam);
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.topvision.framework.snmp.SnmpWorker#exec()
         */
        @Override
        protected void exec() {
            snmpUtil.reset(result.getSnmpParam());
            if (!snmpUtil.verifySnmpVersion()) {
                result.setError("The device no snmp supported!");
                return;
            }
            StringBuilder error = new StringBuilder();
            for (String oid : result.getData().keySet()) {
                try {
                    if (result.getData(oid) != null) {
                        snmpUtil.set(oid, result.getData(oid));
                    }
                } catch (SnmpException e) {
                    error.append(oid).append("=").append(result.getData(oid)).append("set error:").append(e.toString())
                            .append("\n");
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
            }
            for (String oid : result.getData().keySet()) {
                try {
                    result.setValue(oid, snmpUtil.get(oid));
                } catch (SnmpException e) {
                    error.append(oid).append("=").append(result.getData(oid)).append("set error:").append(e.toString())
                            .append("\n");
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Result data:" + result.getData());
            }
            if (error.length() > 0) {
                result.setError(error.toString());
                if (logger.isDebugEnabled()) {
                    logger.debug("**********Set data error:" + error);
                }
            }
        }
    }

    @Autowired
    private SnmpExecutorService snmpExecutorService;

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.service.SnmpService#get(com.topvision.ems.facade.domain.SnmpParam,
     *      java.lang.Class)
     */
    @Override
    public <T> T get(SnmpParam param, Class<T> clazz) throws SnmpException {
        return null;
    }

    /**
     * @return the snmpExecutorService
     */
    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.engine.service.MonitorService#
     *      getSnmpMonitor(com.topvision.ems.facade.domain.SnmpMonitorParam)
     */
    @Override
    public SnmpMonitorParam getSnmpMonitor(SnmpMonitorParam monitorParam) throws SnmpException {
        return snmpExecutorService.execute(new MonitorSnmpWorker(null), monitorParam);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.service.SnmpService#
     *      getTable(com.topvision.ems.facade.domain.SnmpParam,java.lang.Class)
     */
    @Override
    public <T> List<T> getTable(SnmpParam param, Class<T> clazz) throws SnmpException {
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.service.SnmpService#set(com.topvision.ems.facade.domain.SnmpData)
     */
    @Override
    public SnmpData set(SnmpData data) throws SnmpException {
        data = snmpExecutorService.execute(new StandardSnmpWorker(null), data);
        if (data.getError() != null) {
            throw new SnmpException(data.getError());
        }
        return data;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.service.SnmpService#set(com.topvision.ems.facade.domain.SnmpParam,
     *      java.lang.Object)
     */
    @Override
    public <T> T set(SnmpParam param, T data) throws SnmpException {
        return null;
    }

    /**
     * @param snmpExecutorService
     *            the snmpExecutorService to set
     */
    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }
}
