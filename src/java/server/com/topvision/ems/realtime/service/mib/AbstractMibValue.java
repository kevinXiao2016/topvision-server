/***********************************************************************
 * $Id: AbstractMibValue.java,v 1.1 2009-10-5 上午10:21:20 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.realtime.service.mib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.facade.SnmpFacade;
import com.topvision.ems.facade.domain.FormulaBinding;
import com.topvision.ems.facade.domain.SnmpMonitorParam;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.facade.FacadeFactory;

import net.sf.json.JSONObject;

/**
 * @Create Date 2009-10-5 上午10:21:20
 * 
 * @author kelers
 * 
 */
public abstract class AbstractMibValue {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractMibValue.class);
    private FacadeFactory facadeFactory;
    private EntityDao entityDao;

    public abstract JSONObject getData(long entityId);

    public abstract String getDisplayName();

    public abstract String[] getHeaders();

    /**
     * 
     * @param key
     * @param strings
     * @return
     */
    protected String getString(String key, String... strings) {
        try {
            return ResourceManager.getResourceManager("com.topvision.ems.realtime.resources").getString(key, strings);
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * 
     * @param entityId
     * @param table
     * @return
     */
    protected String[][] getTable(long entityId, String table) {
        try {
            SnmpMonitorParam param = new SnmpMonitorParam();
            param.setSnmpParam(entityDao.getSnmpParamByEntityId(entityId));
            param.addBinding(table, table, FormulaBinding.TYPE_TABLE, null);
            param = facadeFactory.getFacade(param.getSnmpParam().getIpAddress(), SnmpFacade.class)
                    .getSnmpMonitor(param);
            return param.getBinding(table).getTable();
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 
     * @param entityId
     * @param cols
     * @return
     */
    protected String[][] getTable(long entityId, String[] cols) {
        try {
            SnmpMonitorParam param = new SnmpMonitorParam();
            param.setSnmpParam(entityDao.getSnmpParamByEntityId(entityId));
            param.addBinding("table", cols, FormulaBinding.TYPE_TABLE, null);
            param = facadeFactory.getFacade(param.getSnmpParam().getIpAddress(), SnmpFacade.class)
                    .getSnmpMonitor(param);
            return param.getBinding("table").getTable();
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            return null;
        }
    }

    /**
     * @param entityDao
     *            the entityDao to set
     */
    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    /**
     * @param facadeFactory
     *            the facadeFactory to set
     */
    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }
}
