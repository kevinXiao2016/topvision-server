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
public class CmtsDiscoverySnmpWorker<T extends CmtsDiscoveryData> extends BaseCmtsDiscoverySnmpWorker<T> {
    private static final long serialVersionUID = 6933018077071595792L;

    /**
     * @param snmpParam
     */
    public CmtsDiscoverySnmpWorker(SnmpParam snmpParam) {
        super(snmpParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.snmp.SnmpWorker#exec()
     */
    @Override
    protected void exec() throws Exception {
        super.exec();

        // 获取上行信道信号质量信息
        try {
            List<String> excludeOids = new ArrayList<String>();
            excludeOids.add("1.3.6.1.4.1.4981.2.1.2.1.1");
            List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfos = snmpUtil.getTable(
                    CmcUpChannelSignalQualityInfo.class, true, excludeOids);
            if (cmcUpChannelSignalQualityInfos != null) {
                result.setCmcUpChannelSignalQualityInfos(cmcUpChannelSignalQualityInfos);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology CmcUpChannelSignalQualityInfo finished!");

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
    }
}
