/***********************************************************************
 * $Id: CmDiscoverySnmpWorker.java,v1.0 2015-11-27 下午2:47:23 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology.engine;

import java.util.List;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.CmStaticIp;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.topology.domain.CmcDiscoveryData;
import com.topvision.framework.common.LoggerUtil;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author Rod John
 * @created @2015-11-27-下午2:47:23
 *
 */
public class CmDiscoverySnmpWorker <T extends CmcDiscoveryData> extends SnmpWorker<T> {

    private static final long serialVersionUID = 4032686851473214561L;
    
    /**
     * @param snmpParam
     */
    public CmDiscoverySnmpWorker(SnmpParam snmpParam) {
        super(snmpParam);
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.snmp.SnmpWorker#exec()
     */
    @Override
    protected void exec() throws Exception {
        Long timeTmp = 0L;
        snmpUtil.reset(snmpParam);
        timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "CmAttribute");
        try {
            List<CmAttribute> cmAttributes = snmpUtil.getTable(CmAttribute.class, true);
            result.setCmAttributes(cmAttributes);
        } catch (Exception e) {
            logger.error("get CmAttribute info error ", e);
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "CmAttribute", timeTmp);
        logger.info("topology cmAttributes finished!");

        timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "CmCpe");
        try {
            List<CmCpe> cmCpeList = snmpUtil.getTable(CmCpe.class, true);
            if (cmCpeList != null) {
                result.setCmCpeList(cmCpeList);
            }
        } catch (Exception e) {
            logger.error("get cmCpeList info error");
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "CmCpe", timeTmp);
        logger.info("topology cmCpeList finished!");

        // 获取cm 大客户IP信息
        timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "CmStaticIp");
        try {
            List<CmStaticIp> cmStaticIpList = snmpUtil.getTable(CmStaticIp.class, true);
            if (cmStaticIpList != null) {
                result.setCmStaticIpList(cmStaticIpList);
            }
        } catch (Exception e) {
            logger.error("get cmStaticIpList info error");
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "CmStaticIp", timeTmp);
        logger.info("topology cmStaticIpList finished!");

        // 获取cm3.0 上行信道信号信息
        timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "DocsIf3CmtsCmUsStatus");
        try {
            List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList = snmpUtil.getTable(DocsIf3CmtsCmUsStatus.class,
                    true);
            if (docsIf3CmtsCmUsStatusList != null) {
                result.setDocsIf3CmtsCmUsStatusList(docsIf3CmtsCmUsStatusList);
            }
        } catch (Exception e) {
            logger.error("get docsIf3CmtsCmUsStatusList info error");
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "DocsIf3CmtsCmUsStatus", timeTmp);
        logger.info("topology docsIf3CmtsCmUsStatusList finished!");
    }

    
    
}
