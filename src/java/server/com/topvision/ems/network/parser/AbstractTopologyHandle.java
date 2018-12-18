/***********************************************************************
 * $Id: AbstraceTopologyHandle.java,v1.0 2015-2-2 上午10:28:17 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.topvision.ems.facade.domain.BatchDiscoveryInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.EntityAddressDao;
import com.topvision.ems.network.service.BatchDiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.service.OperationService;

/**
 * @author Rod John
 * @created @2015-2-2-上午10:28:17
 * 
 */
public abstract class AbstractTopologyHandle extends BaseService implements TopologyHandle {

    @Autowired
    protected EntityService entityService;

    @Autowired
    protected BatchDiscoveryService batchDiscoveryService;
    
    @Autowired
    protected OperationService operationService;

    @Autowired
    protected EntityAddressDao entityAddressDao;

    @Value("${entity.replace:false}")
    protected boolean entityReplace;
    @Value("${entity.unique:ip}")
    protected String entityUnique;

    @Override
    public String handleTopoResult(BatchDiscoveryInfo info, Entity entity) {
        if (UNIQUE_IP.equals(this.getEntityUnique()) && !"0.0.0.0".equals(info.getIpAddress())
                && entityAddressDao.selectByAddress(info.getIpAddress()) != null) {
            return ENTITYEXISTS;
        }
        return SUCCESS;
    }

    /**
     * @return the entityService
     */
    public EntityService getEntityService() {
        return entityService;
    }

    /**
     * @param entityService
     *            the entityService to set
     */
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    /**
     * @return the batchDiscoveryService
     */
    public BatchDiscoveryService getBatchDiscoveryService() {
        return batchDiscoveryService;
    }

    /**
     * @param batchDiscoveryService
     *            the batchDiscoveryService to set
     */
    public void setBatchDiscoveryService(BatchDiscoveryService batchDiscoveryService) {
        this.batchDiscoveryService = batchDiscoveryService;
    }

    /**
     * @return the entityReplace
     */
    public boolean isEntityReplace() {
        return entityReplace;
    }

    /**
     * @param entityReplace
     *            the entityReplace to set
     */
    public void setEntityReplace(boolean entityReplace) {
        this.entityReplace = entityReplace;
    }

    public String getEntityUnique() {
        return entityUnique;
    }

    public void setEntityUnique(String entityUnique) {
        this.entityUnique = entityUnique;
    }

}
