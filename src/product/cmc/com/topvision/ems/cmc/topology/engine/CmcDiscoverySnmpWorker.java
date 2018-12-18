/***********************************************************************
 * $Id: CmcDiscoverySnmpWorker.java,v1.0 2011-11-13 下午01:58:10 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.cmc.topology.domain.CmcDiscoveryData;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.facade.domain.DeviceBaseInfo;
import com.topvision.ems.facade.domain.IpAddressTable;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author Victor
 * @created @2011-11-13-下午01:58:10
 * 
 */
public class CmcDiscoverySnmpWorker<T extends CmcDiscoveryData> extends SnmpWorker<T> {
    private static final long serialVersionUID = 9128632270697295733L;

    /**
     * @param snmpParam
     */
    public CmcDiscoverySnmpWorker(SnmpParam snmpParam) {
        super(snmpParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.snmp.SnmpWorker#exec()
     */
    @Override
    protected void exec() throws Exception {
        // 获取CMC的数据
        logger.info("begin to topology cmc({})...", snmpParam);

        result.setDiscoveryTime(System.currentTimeMillis());
        result.setIp(snmpParam.getIpAddress());
        result.setSnmpParam(snmpParam);

        snmpUtil.reset(snmpParam);
        // Index
        List<Long> indexList = result.getCmcIndexs();

        if (indexList == null || indexList.size() == 0) {
            return;
        }

        // DataMap
        Map<Long, CmcEntityData> dataMap = new HashMap<Long, CmcEntityData>();
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
            List<CmcAttribute> cmcAttributes = new ArrayList<>();
            for (Long cmcIndex : indexList) {
            	try {
	                CmcAttribute attribute = new CmcAttribute(cmcIndex);
	                cmcAttributes.add(snmpUtil.getTableLine(attribute));
            	} catch(Exception e) {
                    logger.error("", e);
            	}
            }
            result.setCmcAttributes(cmcAttributes);

            List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos = snmpUtil.getTable(CmcUpChannelBaseInfo.class, true);
            result.setCmcUpChannelBaseInfos(cmcUpChannelBaseInfos);

            List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos = snmpUtil.getTable(CmcDownChannelBaseInfo.class,
                    true);
            result.setCmcDownChannelBaseInfos(cmcDownChannelBaseInfos);

            try {
                result.setCmcPhyConfigs(snmpUtil.getTable(CmcPhyConfig.class, true));
            } catch (Exception e) {
                logger.error("", e);
            }

            for (CmcAttribute cmcAttribute : cmcAttributes) {
            	Long cmcIndex = cmcAttribute.getCmcIndex();
                CmcEntityData entity_data = new CmcEntityData(cmcIndex);
                dataMap.put(cmcIndex, entity_data);
            }

            for (CmcUpChannelBaseInfo upChannelBaseInfo : cmcUpChannelBaseInfos) {
                Long cmcIndex = CmcIndexUtils.getCmcIndexFromChannelIndex(upChannelBaseInfo.getChannelIndex());
                upChannelBaseInfo.setCmcIndex(cmcIndex);
                CmcEntityData entity_data = dataMap.get(cmcIndex);
                if (entity_data != null) {
                    entity_data.addCmcUpChannelIndex(upChannelBaseInfo.getChannelIndex());
                }
            }

            for (CmcDownChannelBaseInfo downChannelBaseInfo : cmcDownChannelBaseInfos) {
                Long cmcIndex = CmcIndexUtils.getCmcIndexFromChannelIndex(downChannelBaseInfo.getChannelIndex());
                downChannelBaseInfo.setCmcIndex(cmcIndex);
                CmcEntityData entity_data = dataMap.get(cmcIndex);
                if (entity_data != null) {
                    entity_data.addCmcDownChannelIndex(downChannelBaseInfo.getChannelIndex());
                }
            }

            for (CmcAttribute cmcAttribute : cmcAttributes) {
            	Long cmcIndex = cmcAttribute.getCmcIndex();
                CmcEntityData entity_data = dataMap.get(cmcIndex);
                List<CmcPort> cmcPorts = new ArrayList<>();
                List<CmcUpChannelSignalQualityInfo> upChannelSignalQualityInfos = new ArrayList<>();
                // Up Channel
                for (Long upChannelIndex : entity_data.getCmcUpChannelIndexs()) {
                    // CmcPort
                    CmcPort cmcPort = new CmcPort(upChannelIndex, cmcIndex);
                    cmcPorts.add(cmcPort);
                    // CmcUpChannelSignalQualityInfo
                    CmcUpChannelSignalQualityInfo upChannelSignalQualityInfo = new CmcUpChannelSignalQualityInfo(
                            upChannelIndex, cmcIndex);
                    upChannelSignalQualityInfos.add(upChannelSignalQualityInfo);
                }
                // Down Channel
                for (Long downChannelIndex : entity_data.getCmcDownChannelIndexs()) {
                    CmcPort cmcPort = new CmcPort(downChannelIndex, cmcIndex);
                    cmcPorts.add(cmcPort);
                }

                for (CmcPort cmcPort : cmcPorts) {
                    cmcPort = snmpUtil.getTableLine(cmcPort);
                }
                //cmcPorts = snmpUtil.getTableLine(cmcPorts);

                if (result.getIndexCollectType() != null && result.getIndexCollectType().equals(1)) {
                    upChannelSignalQualityInfos = snmpUtil.getTableLine(upChannelSignalQualityInfos);
                } else if (result.getIndexCollectType() != null && result.getIndexCollectType().equals(2)) {
                    for (CmcUpChannelSignalQualityInfo cmcUpChannelSignalQualityInfo : upChannelSignalQualityInfos) {
                        cmcUpChannelSignalQualityInfo = snmpUtil.getTableLine(cmcUpChannelSignalQualityInfo);
                    }
                } else {
                    upChannelSignalQualityInfos = snmpUtil.getTableLine(upChannelSignalQualityInfos);
                }

                result.addCmcPorts(cmcPorts);
                result.addCmcUpChannelSignalQualityInfos(upChannelSignalQualityInfos);
            }

        } catch (Exception e) {
            logger.error("", e);
        }

    }
}

class CmcEntityData {

    private Long cmcIndex;
    private List<Long> cmcUpChannelIndexs = new ArrayList<>();
    private List<Long> cmcDownChannelIndexs = new ArrayList<>();

    public CmcEntityData(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    /**
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    /**
     * @return the cmcUpChannelIndexs
     */
    public List<Long> getCmcUpChannelIndexs() {
        return cmcUpChannelIndexs;
    }

    /**
     * @param cmcUpChannelIndexs the cmcUpChannelIndexs to set
     */
    public void setCmcUpChannelIndexs(List<Long> cmcUpChannelIndexs) {
        this.cmcUpChannelIndexs = cmcUpChannelIndexs;
    }

    /**
     * 
     * 
     * @param cmcUpChannelIndex
     */
    public void addCmcUpChannelIndex(Long cmcUpChannelIndex) {
        cmcUpChannelIndexs.add(cmcUpChannelIndex);
    }

    /**
     * @return the cmcDownChannelIndexs
     */
    public List<Long> getCmcDownChannelIndexs() {
        return cmcDownChannelIndexs;
    }

    /**
     * @param cmcDownChannelIndexs the cmcDownChannelIndexs to set
     */
    public void setCmcDownChannelIndexs(List<Long> cmcDownChannelIndexs) {
        this.cmcDownChannelIndexs = cmcDownChannelIndexs;
    }

    /**
     * 
     * 
     * @param cmcUpChannelIndex
     */
    public void addCmcDownChannelIndex(Long cmcDownChannelIndex) {
        cmcDownChannelIndexs.add(cmcDownChannelIndex);
    }
}
