/***********************************************************************
 * $Id: EponTopologyHandle.java,v1.0 2013-1-16 下午05:10:41 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.batchtopology;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.facade.domain.BatchDiscoveryInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.TopoHandlerProperty;
import com.topvision.ems.network.parser.AbstractTopologyHandle;

/**
 * @author RodJohn
 * @created @2013-1-16-下午05:10:41
 * 
 */
@Service("eponTopologyHandle")
public class EponTopologyHandle extends AbstractTopologyHandle {
    private final static String PN8601_SYSOID = "1.3.6.1.4.1.32285.11.2.1.1";
    private final static String PN8602_SYSOID = "1.3.6.1.4.1.32285.11.2.1.2";
    private final static String PN8602_E_SYSOID = "1.3.6.1.4.1.32285.11.2.1.2.1";
    private final static String PN8602_EF_SYSOID = "1.3.6.1.4.1.32285.11.2.1.2.2";
    private final static String PN8603_SYSOID = "1.3.6.1.4.1.32285.11.2.1.3";

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.parser.TopologyHandle#getTopoInfo()
     */
    @Override
    public TopoHandlerProperty getTopoInfo() {
        return new TopoHandlerProperty(new OltAttribute(), TopoHandlerProperty.TOPO_OBJECT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.parser.TopologyHandle#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        batchDiscoveryService.registerTopoHandler(PN8601_SYSOID, this);
        batchDiscoveryService.registerTopoHandler(PN8602_SYSOID, this);
        batchDiscoveryService.registerTopoHandler(PN8603_SYSOID, this);
        batchDiscoveryService.registerTopoHandler(PN8602_E_SYSOID, this);
        batchDiscoveryService.registerTopoHandler(PN8602_EF_SYSOID, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.parser.TopologyHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        batchDiscoveryService.unregisterTopoHandler(PN8601_SYSOID);
        batchDiscoveryService.unregisterTopoHandler(PN8602_SYSOID);
        batchDiscoveryService.unregisterTopoHandler(PN8603_SYSOID);
        batchDiscoveryService.unregisterTopoHandler(PN8602_E_SYSOID);
        batchDiscoveryService.unregisterTopoHandler(PN8602_EF_SYSOID);
    }

    @Override
    public String handleTopoResult(BatchDiscoveryInfo info, Entity newEntity) {
        String result = super.handleTopoResult(info, newEntity);
        if (!SUCCESS.equals(result)) {
            return result;
        }

        // add by fanzidong, OLT没有MAC唯一性的概念，只根据IP进行唯一性判断
        if (!"0.0.0.0".equals(info.getIpAddress()) && entityAddressDao.selectByAddress(info.getIpAddress()) != null) {
            return ENTITYEXISTS;
        }

        // 赋值mac
        try {
            OltAttribute oltAttribute = (OltAttribute) info.getProductInfo();
            newEntity.setMac(oltAttribute.getInbandMac());
        } catch (Exception e) {
            logger.error("", e);
        }
        return SUCCESS;
    }

}
