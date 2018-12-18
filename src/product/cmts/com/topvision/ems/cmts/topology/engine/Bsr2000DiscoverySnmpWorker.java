/***********************************************************************
 * $Id: CmtsDiscoverySnmpWorker.java,v1.0 2013-7-20 下午02:22:27 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.topology.engine;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.ems.cmts.topology.domain.CmtsDiscoveryData;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Rod John
 * @created @2013-7-20-下午02:22:27
 * 
 */
public class Bsr2000DiscoverySnmpWorker<T extends CmtsDiscoveryData> extends BaseCmtsDiscoverySnmpWorker<T> {
    private static final long serialVersionUID = 2045471513582071864L;

    /**
     * @param snmpParam
     */
    public Bsr2000DiscoverySnmpWorker(SnmpParam snmpParam) {
        super(snmpParam);
    }

    @Override
    protected void exec() throws Exception {
        super.exec();
        // 获取上行信道信号质量信息
        try {
            List<String> excludeOids = new ArrayList<String>();
            excludeOids.add("1.3.6.1.4.1.4491.2.1.20.1.25.1.2");
            List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfos = snmpUtil.getTable(
                    CmcUpChannelSignalQualityInfo.class, true, excludeOids);
            if (cmcUpChannelSignalQualityInfos != null) {
                result.setCmcUpChannelSignalQualityInfos(cmcUpChannelSignalQualityInfos);
            }
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("topology CmcUpChannelSignalQualityInfo finished!");

        // 获取cm属性
        List<CmAttribute> cmAttributes = new ArrayList<CmAttribute>();
        try {
            cmAttributes = snmpUtil.getTable(CmAttribute.class, true);
        } catch (Exception e) {
            logger.error("", e);
        }
        if (cmAttributes != null) {
            result.setCmAttributes(cmAttributes);
        }
        logger.info("topology CmAttribute finished!");

        /*CmcAttribute cmcAttribute = new CmcAttribute();
        try {
            BSRAttribute bsrAttribute = snmpUtil.get(BSRAttribute.class);
            if (bsrAttribute.getCpuUtilization() != null) {
                cmcAttribute.setTopCcmtsSysCPURatio(bsrAttribute.getCpuUtilization().intValue());
            }
            if (bsrAttribute.getAllocatableMem() != null && bsrAttribute.getMemAllocated() != null) {
                cmcAttribute.setMemUsed((double) bsrAttribute.getMemAllocated() / bsrAttribute.getAllocatableMem());
            }
        } catch (Exception e) {
            logger.info("topology BSR cpu and memory failed: ", e);
        }
        result.setCmcAttribute(cmcAttribute);
        logger.debug("topology BSR cpu and memory finished!");*/
    }
}
