/***********************************************************************
 * $Id: CmcDiscoveryFacadeTest.java,v1.0 2011-11-13 下午02:54:35 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBundle;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpGiAddr;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelStaticInfo;
import com.topvision.ems.cmc.facade.domain.CmcModulationProfileInfo;
import com.topvision.ems.cmc.facade.domain.CmcPortPerf;
import com.topvision.ems.cmc.macdomain.facade.domain.MacDomainStatusInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmMacToServiceFlow;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosDynamicServiceStats;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosParamSetInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosPktClassInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceFlowAttribute;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceFlowStatus;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelCounterInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpUtil;

/**
 * @author Victor
 * @created @2011-11-13-下午02:54:35
 * 
 */
public class CmcDiscoveryFacadeTest {
    private String ip = "172.16.18.46";
    private String roCommunity = "public";
    private String rwCommunity = "private";
    private String mib = "TOPVISION-CCMTS-MIB";
    private Logger logger;
    private SnmpUtil snmpUtil;
    private Long cmcIndex;// 测试数据index

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() {

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {

    }

    @Before
    public void setUpBefore() {
        logger = LoggerFactory.getLogger(this.getClass());
        snmpUtil = new SnmpUtil(getSnmpParam());
        cmcIndex = 427884544L;
    }

    @After
    public void setUpAfter() {

    }

    /**
     * Test method for
     * {@link com.topvision.ems.cmc.facade.CmcDiscoveryFacade#discovery(com.topvision.framework.snmp.SnmpParam)}
     * .
     */
    @Test
    public void testDiscovery() {
        // fail("Not yet implemented");
    }

    private SnmpParam getSnmpParam() {
        EnvironmentConstants.putEnv(EnvironmentConstants.MIB_HOME, "webapp/WEB-INF/mibs");
        SnmpParam snmpParam = new SnmpParam();
        snmpParam.setIpAddress(ip);
        snmpParam.setCommunity(roCommunity);
        snmpParam.setMibs(mib);
        return snmpParam;
    }

    @Test
    public void getMacDomainStatusInfo() {
        // snmpUtil.reset(getSnmpParam());
        MacDomainStatusInfo macDomainStatus = new MacDomainStatusInfo();
        macDomainStatus.setCmcIndex(cmcIndex);
        macDomainStatus = snmpUtil.getTableLine(macDomainStatus);
        logger.debug("\n\nMacStatusInfo:{}", macDomainStatus);
    }

    @Test
    public void getUpchannelBaseInfo() {
        // snmpUtil.reset(getSnmpParam());
        List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfoList = new ArrayList<CmcUpChannelBaseInfo>();
        for (int i = 1; i < 5; i++) {
            long l = (i << 8);
            CmcUpChannelBaseInfo cmcUpChannelBaseInfo = new CmcUpChannelBaseInfo();
            cmcUpChannelBaseInfo.setChannelIndex(cmcIndex.longValue() | l);
            cmcUpChannelBaseInfo = snmpUtil.getTableLine(cmcUpChannelBaseInfo);
            cmcUpChannelBaseInfoList.add(cmcUpChannelBaseInfo);
        }
        logger.debug("\n\ncmcUpChannelBaseInfoList:{}", cmcUpChannelBaseInfoList);
    }

    @Test
    public void getDownChannelBaseInfo() {
        // snmpUtil.reset(getSnmpParam());
        List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfoList = new ArrayList<CmcDownChannelBaseInfo>();
        for (int i = 1; i < 5; i++) {
            long l = (i << 8);
            CmcDownChannelBaseInfo cmcDownChannelBaseInfo = new CmcDownChannelBaseInfo();
            cmcDownChannelBaseInfo.setChannelIndex(cmcIndex.longValue() | 0x8000L | l);
            cmcDownChannelBaseInfo = snmpUtil.getTableLine(cmcDownChannelBaseInfo);
            cmcDownChannelBaseInfoList.add(cmcDownChannelBaseInfo);
        }
        logger.debug("\n\ncmcDownChannelBaseInfoList:{}", cmcDownChannelBaseInfoList);
    }

    @Test
    public void getCmcUpPortInfo() {
        // snmpUtil.reset(getSnmpParam());
        List<CmcPort> cmcPortList = new ArrayList<CmcPort>();
        List<CmcPortPerf> cmcPortPerfList = new ArrayList<CmcPortPerf>();
        for (int i = 1; i < 5; i++) {
            long l = (i << 8);
            CmcPort cmcPort = new CmcPort();
            CmcPortPerf cmcPortPerf = new CmcPortPerf();
            cmcPort.setIfIndex(cmcIndex.longValue() | l);
            cmcPortPerf.setIfIndex(cmcIndex.longValue() | l);
            cmcPort = snmpUtil.getTableLine(cmcPort);
            cmcPortPerf = snmpUtil.getTableLine(cmcPortPerf);
            cmcPortList.add(cmcPort);
            cmcPortPerfList.add(cmcPortPerf);
        }
        logger.debug("\n\ncmcUpPortList:{}", cmcPortList);
        logger.debug("\n\ncmcUpPortPerfList:{}", cmcPortPerfList);
    }

    @Test
    public void getCmcDownPortInfo() {
        // snmpUtil.reset(getSnmpParam());
        List<CmcPort> cmcPortList = new ArrayList<CmcPort>();
        List<CmcPortPerf> cmcPortPerfList = new ArrayList<CmcPortPerf>();
        for (int i = 1; i < 5; i++) {
            long l = (i << 8);
            CmcPort cmcPort = new CmcPort();
            CmcPortPerf cmcPortPerf = new CmcPortPerf();
            cmcPort.setIfIndex(cmcIndex.longValue() | 0x8000L | l);
            cmcPortPerf.setIfIndex(cmcIndex.longValue() | 0x8000L | l);
            cmcPort = snmpUtil.getTableLine(cmcPort);
            cmcPortPerf = snmpUtil.getTableLine(cmcPortPerf);
            cmcPortList.add(cmcPort);
            cmcPortPerfList.add(cmcPortPerf);
        }
        logger.debug("\n\ncmcDownPortList:{}", cmcPortList);
        logger.debug("\n\ncmcDownPortPerfList:{}", cmcPortPerfList);
    }

    @Test
    public void getCmcUpChannelChannelCounterInfo() {
        // snmpUtil.reset(getSnmpParam());
        List<CmcUpChannelCounterInfo> cmcUpChannelCounterInfoList = new ArrayList<CmcUpChannelCounterInfo>();
        for (int i = 1; i < 5; i++) {
            long l = (i << 8);
            CmcUpChannelCounterInfo cmcUpChannelCounterInfo = new CmcUpChannelCounterInfo();
            cmcUpChannelCounterInfo.setChannelIndex(cmcIndex.longValue() | l);
            cmcUpChannelCounterInfo = snmpUtil.getTableLine(cmcUpChannelCounterInfo);
            cmcUpChannelCounterInfoList.add(cmcUpChannelCounterInfo);
        }
        logger.debug("\n\ncmcUpChannelCounterInfoList:{}", cmcUpChannelCounterInfoList);
    }

    @Test
    public void getSignalQuqlityInfo() {
        // snmpUtil.reset(getSnmpParam());
        List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfoList = new ArrayList<CmcUpChannelSignalQualityInfo>();
        for (int i = 1; i < 5; i++) {
            long l = (i << 8);
            CmcUpChannelSignalQualityInfo cmcUpChannelSignalQualityInfo = new CmcUpChannelSignalQualityInfo();
            cmcUpChannelSignalQualityInfo.setChannelIndex(cmcIndex.longValue() | l);
            cmcUpChannelSignalQualityInfo = snmpUtil.getTableLine(cmcUpChannelSignalQualityInfo);
            cmcUpChannelSignalQualityInfoList.add(cmcUpChannelSignalQualityInfo);
        }
        logger.debug("\n\ncmcUpChannelSignalQualityInfoList:{}", cmcUpChannelSignalQualityInfoList);
    }

    @Test
    public void getDownChannelCounterInfo() {
        // snmpUtil.reset(getSnmpParam());
        List<CmcDownChannelStaticInfo> cmcDownChannelStaticInfoList = new ArrayList<CmcDownChannelStaticInfo>();
        for (int i = 1; i < 17; i++) {
            long l = (i << 8);
            CmcDownChannelStaticInfo cmcDownChannelStaticInfo = new CmcDownChannelStaticInfo();
            cmcDownChannelStaticInfo.setChannelIndex(cmcIndex.longValue() | 0x8000L | l);
            cmcDownChannelStaticInfo = snmpUtil.getTableLine(cmcDownChannelStaticInfo);
            cmcDownChannelStaticInfoList.add(cmcDownChannelStaticInfo);
        }
        logger.debug("\n\ncmcDownChannelStaticInfoList:{}", cmcDownChannelStaticInfoList);
    }

    @Test
    public void getModulationProfileInfo() {
        // snmpUtil.reset(getSnmpParam());
        List<CmcModulationProfileInfo> cmcModulationProfileInfos = new ArrayList<CmcModulationProfileInfo>();
        cmcModulationProfileInfos = snmpUtil.getTable(CmcModulationProfileInfo.class, true);
        logger.debug("\n\ncmcModulationProfileInfos:{}", cmcModulationProfileInfos);
    }

    @Test
    public void getDynamicServiceStatsInfo() {
        CmcQosDynamicServiceStats cmcQosUpDynamicServiceStats = new CmcQosDynamicServiceStats();
        CmcQosDynamicServiceStats cmcQosDownDynamicServiceStats = new CmcQosDynamicServiceStats();
        cmcQosUpDynamicServiceStats.setCmcIndex(cmcIndex);
        cmcQosUpDynamicServiceStats.setDocsQosIfDirection(2);
        cmcQosUpDynamicServiceStats = snmpUtil.getTableLine(cmcQosUpDynamicServiceStats);
        cmcQosDownDynamicServiceStats.setCmcIndex(cmcIndex);
        cmcQosDownDynamicServiceStats.setDocsQosIfDirection(1);
        cmcQosDownDynamicServiceStats = snmpUtil.getTableLine(cmcQosDownDynamicServiceStats);
        logger.debug("\n\ncmcQosDownDynamicServiceStats:{}", cmcQosDownDynamicServiceStats);
    }

    @Test
    public void getServiceFlowInfo() {
        List<CmcQosServiceFlowAttribute> cmcQosServiceFlowAttributes = new ArrayList<CmcQosServiceFlowAttribute>();
        CmcQosServiceFlowAttribute cmcQosServiceFlowAttribute = new CmcQosServiceFlowAttribute();
        cmcQosServiceFlowAttribute.setCmcIndex(cmcIndex);
        cmcQosServiceFlowAttributes = snmpUtil.getTableLines(cmcQosServiceFlowAttribute, 1, Integer.MAX_VALUE);
        logger.debug("\n\ncmcQosServiceFlowAttributes:{}", cmcQosServiceFlowAttributes);
        logger.debug("\n\ncmcQosServiceFlowAttributes:{}", snmpUtil.getTable(CmcQosServiceFlowAttribute.class, true));
    }

    @Test
    public void getParamSetInfo() {
        List<CmcQosServiceFlowAttribute> cmcQosServiceFlowAttributes = new ArrayList<CmcQosServiceFlowAttribute>();
        CmcQosServiceFlowAttribute cmcQosServiceFlowAttribute = new CmcQosServiceFlowAttribute();
        cmcQosServiceFlowAttribute.setCmcIndex(cmcIndex);
        cmcQosServiceFlowAttributes = snmpUtil.getTableLines(cmcQosServiceFlowAttribute, 1, Integer.MAX_VALUE);
        List<CmcQosParamSetInfo> allCmcQosParamSetInfos = new ArrayList<CmcQosParamSetInfo>();
        for (CmcQosServiceFlowAttribute serviceFlowAttribute : cmcQosServiceFlowAttributes) {
            List<CmcQosParamSetInfo> cmcQosParamSetInfos = new ArrayList<CmcQosParamSetInfo>();
            CmcQosParamSetInfo cmcQosParamSetInfo = new CmcQosParamSetInfo();
            cmcQosParamSetInfo.setCmcIndex(cmcIndex);
            cmcQosParamSetInfo.setServiceFlowId(serviceFlowAttribute.getDocsQosServiceFlowId());
            cmcQosParamSetInfos = snmpUtil.getTableLines(cmcQosParamSetInfo, 1, Integer.MAX_VALUE);
            allCmcQosParamSetInfos.addAll(cmcQosParamSetInfos);
        }
        logger.debug("\n\nallCmcQosParamSetInfos:{}", allCmcQosParamSetInfos);
    }

    @Test
    public void getPktClassInfo() {
        List<CmcQosServiceFlowAttribute> cmcQosServiceFlowAttributes = new ArrayList<CmcQosServiceFlowAttribute>();
        CmcQosServiceFlowAttribute cmcQosServiceFlowAttribute = new CmcQosServiceFlowAttribute();
        cmcQosServiceFlowAttribute.setCmcIndex(cmcIndex);
        cmcQosServiceFlowAttributes = snmpUtil.getTableLines(cmcQosServiceFlowAttribute, 1, Integer.MAX_VALUE);
        List<CmcQosPktClassInfo> allCmcQosPktClassInfos = new ArrayList<CmcQosPktClassInfo>();
        for (CmcQosServiceFlowAttribute serviceFlowAttribute : cmcQosServiceFlowAttributes) {
            List<CmcQosPktClassInfo> cmcQosPktClassInfos = new ArrayList<CmcQosPktClassInfo>();
            CmcQosPktClassInfo cmcQosPktClassInfo = new CmcQosPktClassInfo();
            cmcQosPktClassInfo.setCmcIndex(cmcIndex);
            cmcQosPktClassInfo.setServiceFlowId(serviceFlowAttribute.getDocsQosServiceFlowId());
            cmcQosPktClassInfos = snmpUtil.getTableLines(cmcQosPktClassInfo, 1, Integer.MAX_VALUE);
            allCmcQosPktClassInfos.addAll(cmcQosPktClassInfos);
        }
        logger.debug("\n\nallCmcQosPktClassInfos:{}", allCmcQosPktClassInfos);
    }

    @Test
    public void getCmAttribute() {
        List<CmAttribute> cmAttributes = snmpUtil.getTable(CmAttribute.class, true);
        logger.debug("\n\ncmAttributes:{}", cmAttributes);
    }

    @Test
    public void getCmcAttribute() {
        CmcAttribute cmcAttribute = snmpUtil.get(CmcAttribute.class);
        logger.debug("\n\ncmAttributes:{}", cmcAttribute);
    }

    @Test
    public void getMacToSrvFlowInfo() {
        List<CmMacToServiceFlow> cmMacToServiceFlows = snmpUtil.getTable(CmMacToServiceFlow.class, true);
        logger.debug("\n\ncmMacToServiceFlows:{}", cmMacToServiceFlows);
    }

    @Test
    public void getAllDynamicServiceStatsInfo() {
        logger.debug(snmpUtil.getTable(CmcQosDynamicServiceStats.class, true).toString());
    }

    @Test
    public void getAllCmcPortPerfs() {
        snmpUtil.getTable(CmcPortPerf.class, true);
    }

    @Test
    public void getAllCmcPorts() {
        /*
         * List<CmcPort> cmcPorts = snmpUtil.getTable(CmcPort.class, true);
         * System.out.println(cmcPorts);
         */
        Long c = CmcIndexUtils.getCmcIndexFromChannelIndex(142671872l);
        System.out.println(c);
        System.out.println(CmcIndexUtils.getCmcIndexFromChannelIndex(142671872l).longValue() == 142671872);
    }

    @Test
    public void getAllServiceFlowStatsInfos() {

        snmpUtil.getTable(CmcQosServiceFlowStatus.class, true);
    }

    @Test
    public void getAllParamSetInfos() {
        snmpUtil.getTable(CmcQosParamSetInfo.class, true);
    }

    @Test
    public void getAllPktClassInfos() {
        logger.debug(snmpUtil.getTable(CmcQosPktClassInfo.class, true).toString());
    }

    @Test
    public void getCmcDhcpBundle() {
        // System.out.println(snmpUtil.getTable(CmcDhcpBundle.class, true));
        // System.out.println(snmpUtil.getTable(CmcDhcpGiAddr.class, true));
        System.out.println(Integer.parseInt(snmpUtil.getLeaf("1.3.6.1.4.1.32285.11.1.1.2.5.1.9.3.0")));
    }
}
