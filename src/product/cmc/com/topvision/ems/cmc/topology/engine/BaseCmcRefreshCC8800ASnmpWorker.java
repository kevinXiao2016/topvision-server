/***********************************************************************
 * $Id: BaseCmcRefreshCC8800ASnmpWorker.java,v1.0 2014-10-18 下午2:01:59 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology.engine;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.cmc.topology.domain.CmcDiscoveryData;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.ems.facade.domain.DeviceBaseInfo;
import com.topvision.ems.facade.domain.IpAddressTable;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author Rod John
 * @created @2014-10-18-下午2:01:59
 * 
 */
public class BaseCmcRefreshCC8800ASnmpWorker<T extends CmcDiscoveryData> extends SnmpWorker<T> {
    private static final long serialVersionUID = -6719096318180299314L;

    /**
     * @param snmpParam
     */
    public BaseCmcRefreshCC8800ASnmpWorker(SnmpParam snmpParam) {
        super(snmpParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.snmp.SnmpWorker#exec()
     */
    @Override
    protected void exec() throws Exception {

        CmcAttribute cmcAttribute = result.getCmcAttributes().get(0);
        Long cmcId = cmcAttribute.getCmcId();
        Long cmcIndex = cmcAttribute.getCmcIndex();
        Long cmcNextIndex = cmcIndex + 0x00010000;
        logger.info("begin to refresh cmc({})...", cmcIndex);

        result.setDiscoveryTime(System.currentTimeMillis());
        // result.setEntityId(snmpParam.getEntityId());
        result.setIp(snmpParam.getIpAddress());
        // snmpParam.setVersion(snmpUtil.getVersion());
        result.setSnmpParam(snmpParam);
        snmpUtil.reset(snmpParam);
        if (!snmpUtil.verifySnmpVersion()) {
            result.setStackTrace(new SnmpException("Community Error"));
            return;
        }

        // DataType
        Integer dataType = result.getDataType();

        if (dataType != null && dataType.equals(CmcDiscoveryData.CMC_TYPE_B)) {
            try {
                result.setSystem(snmpUtil.get(DeviceBaseInfo.class));
            } catch (Exception e) {
                logger.error("", e);
            }
            try {
                result.setIpAddressTables(snmpUtil.getTable(IpAddressTable.class, true));
            } catch (Exception e) {
                logger.error("", e);
            }
        }

        try {
            // 采集cmcAttribute信息
            cmcAttribute = snmpUtil.getTableLine(cmcAttribute);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("auto topology CmcAttribute finished!");

        /*List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos = result.getCmcUpChannelBaseInfos();
        try {
            if (result.getIndexCollectType() != null && result.getIndexCollectType().equals(1)) {
                result.setCmcUpChannelBaseInfos(snmpUtil.getTableLine(cmcUpChannelBaseInfos));
            } else if (result.getIndexCollectType() != null && result.getIndexCollectType().equals(2)) {
                for (CmcUpChannelBaseInfo cmcUpChannelBaseInfo : cmcUpChannelBaseInfos) {
                    cmcUpChannelBaseInfo = snmpUtil.getTableLine(cmcUpChannelBaseInfo);
                }
            } else {
                result.setCmcUpChannelBaseInfos(snmpUtil.getTableLine(cmcUpChannelBaseInfos));
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("auto topology CmcUpChannelBaseInfo finished!");*/

        List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos = new ArrayList<>();
        try {
            cmcUpChannelBaseInfos = snmpUtil.getTableRangeLines(new CmcUpChannelBaseInfo(), cmcIndex, cmcNextIndex);
            for (CmcUpChannelBaseInfo info : cmcUpChannelBaseInfos) {
                info.setCmcId(cmcId);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        result.setCmcUpChannelBaseInfos(cmcUpChannelBaseInfos);
        logger.info("auto topology CmcUpChannelBaseInfo finished!");

        /*List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos = result.getCmcDownChannelBaseInfos();
        try {
            if (result.getIndexCollectType() != null && result.getIndexCollectType().equals(1)) {
                result.setCmcDownChannelBaseInfos(snmpUtil.getTableLine(cmcDownChannelBaseInfos));
            } else if (result.getIndexCollectType() != null && result.getIndexCollectType().equals(2)) {
                for (CmcDownChannelBaseInfo cmcDownChannelBaseInfo : cmcDownChannelBaseInfos) {
                    cmcDownChannelBaseInfo = snmpUtil.getTableLine(cmcDownChannelBaseInfo);
                }
            } else {
                result.setCmcDownChannelBaseInfos(snmpUtil.getTableLine(cmcDownChannelBaseInfos));
            }
        } catch (Exception e) {
            logger.error("", e);
        }*/

        List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos = new ArrayList<>();
        try {
            cmcDownChannelBaseInfos = snmpUtil.getTableRangeLines(new CmcDownChannelBaseInfo(), cmcIndex, cmcNextIndex);
            for (CmcDownChannelBaseInfo info : cmcDownChannelBaseInfos) {
                info.setCmcId(cmcId);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        result.setCmcDownChannelBaseInfos(cmcDownChannelBaseInfos);
        logger.info("auto topology CmcDownChannelBaseInfo finished!");

        List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfos = new ArrayList<CmcUpChannelSignalQualityInfo>();
        List<CmcPort> cmcPorts = new ArrayList<CmcPort>();

        for (CmcUpChannelBaseInfo upInfo : cmcUpChannelBaseInfos) {
            // CmcUpChannelBaseInfo
            upInfo.setCmcIndex(cmcIndex);
            // CmcPort
            CmcPort port = new CmcPort(upInfo.getChannelIndex(), cmcIndex);
            cmcPorts.add(port);
            // CmcUpChannelSignalQualityInfo
            CmcUpChannelSignalQualityInfo upChannelSignalQualityInfo = new CmcUpChannelSignalQualityInfo(upInfo.getChannelIndex(), cmcIndex);
            cmcUpChannelSignalQualityInfos.add(upChannelSignalQualityInfo);
        }

        for (CmcDownChannelBaseInfo downInfo : cmcDownChannelBaseInfos) {
            // CmcUpChannelBaseInfo
            downInfo.setCmcIndex(cmcIndex);
            // CmcPort
            CmcPort port = new CmcPort(downInfo.getChannelIndex(), cmcIndex);
            cmcPorts.add(port);
        }

        try {
            for (CmcPort cmcPort : cmcPorts) {
                cmcPort = snmpUtil.getTableLine(cmcPort);
            }
            //cmcPorts = snmpUtil.getTableLine(cmcPorts);
        } catch (Exception e) {
            logger.error("", e);
        }
        result.addCmcPorts(cmcPorts);
        logger.info("auto topology cmcPorts finished!");

        try {
            if (result.getIndexCollectType() != null && result.getIndexCollectType().equals(1)) {
                cmcUpChannelSignalQualityInfos = snmpUtil.getTableLine(cmcUpChannelSignalQualityInfos);
            } else if (result.getIndexCollectType() != null && result.getIndexCollectType().equals(2)) {
                for (CmcUpChannelSignalQualityInfo cmcUpChannelSignalQualityInfo : cmcUpChannelSignalQualityInfos) {
                    cmcUpChannelSignalQualityInfo = snmpUtil.getTableLine(cmcUpChannelSignalQualityInfo);
                }
            } else {
                cmcUpChannelSignalQualityInfos = snmpUtil.getTableLine(cmcUpChannelSignalQualityInfos);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        result.addCmcUpChannelSignalQualityInfos(cmcUpChannelSignalQualityInfos);
        logger.info("auto topology cmcUpChannelSignalQualityInfos finished!");

        try {
            result.setCmcPhyConfigs(snmpUtil.getTable(CmcPhyConfig.class, true));
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("auto topology cmcPhyConfigs finished");
    }

}
