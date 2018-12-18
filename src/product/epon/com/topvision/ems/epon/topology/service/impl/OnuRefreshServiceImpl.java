/***********************************************************************
 * $Id: OnuRefreshServiceImpl.java,v1.0 2015-8-8 下午2:50:44 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.topology.domain.OnuDiscoveryData;
import com.topvision.ems.epon.topology.facade.OnuDiscoveryFacade;
import com.topvision.ems.epon.topology.service.OnuRefreshService;
import com.topvision.ems.gpon.onu.dao.GponOnuDao;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onuvoip.domain.TopGponOnuCapability;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Rod John
 * @created @2015-8-8-下午2:50:44
 *
 */
@Service("onuRefreshService")
public class OnuRefreshServiceImpl extends BaseService implements OnuRefreshService {
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private GponOnuDao gponOnuDao;
    @Autowired
    private UniDao uniDao;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;
    @Resource(name = "onuDiscoveryService")
    private DiscoveryService<OnuDiscoveryData> onuDiscoveryService;
    @Autowired
    protected MessageService messageService;

    @PostConstruct
    public void initialize() {
        super.initialize();
    }

    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.topology.service.OnuRefreshService#refreshOnu(java.lang.Long, java.lang.Long, java.lang.Long)
     */
    @Override
    public void refreshOnu(Long entityId, Long onuIndex, Long onuId) {
        OnuDiscoveryData onuDiscoveryData = new OnuDiscoveryData(entityId);
        onuDiscoveryData.addOnuIndex(onuIndex);
        onuDiscoveryData.addOltOnuPonAttributes(onuDao.getOnuPonAttributeByOnuId(onuId));
        onuDiscoveryData.setOltUniAttributes(uniDao.getUniIndexAttributes(onuId));
        onuDiscoveryData.setOnuEorG(onuDao.getOnuEntityById(onuId).getOnuEorG());
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        onuDiscoveryData = facadeFactory.getFacade(snmpParam.getIpAddress(), OnuDiscoveryFacade.class).discoveryOnu(
                snmpParam, onuDiscoveryData);
        updateOnuEntity(onuDiscoveryData, onuId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.topology.service.OnuRefreshService#autoRefreshOnu(java.lang.Long, java.lang.Long, java.lang.Long)
     */
    @Override
    public void autoRefreshOnu(Long entityId, Long onuIndex, Long onuId) {
        // TODO Auto-generated method stub

    }

    private void updateOnuEntity(OnuDiscoveryData onuDiscoveryData, Long onuId) {
        Long entityId = onuDiscoveryData.getEntityId();

        OltOnuAttribute oltOnuAttribute = onuDiscoveryData.getOltOnuAttributes().get(0);

        oltOnuAttribute.setOnuId(onuId);

        List<OltOnuPonAttribute> oltOnuPonAttributes = onuDiscoveryData.getOltOnuPonAttributes();

        List<OltUniAttribute> oltUniAttributes = onuDiscoveryData.getOltUniAttributes();

        List<OltUniExtAttribute> oltUniExtAttributes = onuDiscoveryData.getOltUniExtAttributes();

        List<OltOnuCapability> onuCapabilities = onuDiscoveryData.getOltOnuCapabilities();

        List<OltTopOnuCapability> topOnuCapabilities = onuDiscoveryData.getOltTopOnuCapabilities();

        List<GponOnuCapability> gponOnuCapabilities = onuDiscoveryData.getGponOnuCapabilities();
        
        List<TopGponOnuCapability> topGponOnuCapabilities = onuDiscoveryData.getTopGponOnuCapabilities();
        
        //将TopGponOnuCapability中的POTS能力信息设置到GponOnuCapability实体中
        if (gponOnuCapabilities!=null && topGponOnuCapabilities != null) {
            for (GponOnuCapability gponOnuCapability : gponOnuCapabilities) {
                Long onuIndex = gponOnuCapability.getOnuIndex();
                for (TopGponOnuCapability topGponOnuCapability : topGponOnuCapabilities) {
                    if (topGponOnuCapability.getOnuIndex() == onuIndex) {
                        gponOnuCapability.setOnuTotalPotsNum(topGponOnuCapability.getTopGponOnuCapOnuPotsNum());
                    }
                }
            }
        }
        
        HashMap<Long, Long> onuMap;
        onuMap = (HashMap<Long, Long>) onuDao.getOnuMap(entityId);

        try {
            onuDao.syncOltOnuAttribute(oltOnuAttribute);
        } catch (Exception e) {
            logger.error("Sync OltOnu Attribute error:", e);
        }

        try {
            onuDao.syncOnuPonAttribute(oltOnuPonAttributes, onuMap);
        } catch (Exception e) {
            logger.error("Sync OnuPon Attribute error:", e);
        }

        try {
            uniDao.syncUniAttribute(oltUniAttributes, entityId, onuMap, false);
        } catch (Exception e) {
            logger.error("Sync Uni Attribute error:", e);
        }

        try {
            uniDao.syncUniExtAttribute(oltUniExtAttributes, onuMap);
        } catch (Exception e) {
            logger.error("Sync Uni ExtAttribute error:", e);
        }

        try {
            onuDao.syncOnuCapatility(onuCapabilities, topOnuCapabilities, onuMap);
        } catch (Exception e) {
            logger.error("update Onu Capability error:", e);
        }

        try {
            gponOnuDao.syncGponOnuCapability(gponOnuCapabilities, onuMap);
        } catch (Exception e) {
            logger.error("update gpon Capability error:", e);
        }

        // Sync Onu Entity Info
        onuDiscoveryService.syncEntityInfo(null, onuDiscoveryData);

        // Start Onu Perf Task
        // onuPerfService.startOnuPerfCollect(entityId, onuId, onuIndex);

        // Send SynchronizedEvent
        onuDiscoveryService.sendSynchronizedEvent(entityId, onuDiscoveryData);
    }

}
