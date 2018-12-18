/***********************************************************************
 * $Id: CmcFacadeImpl.java,v1.0 2011-7-1 下午02:56:35 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.engine.executor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import com.topvision.ems.cmc.acl.facade.domain.CmcAclDefAction;
import com.topvision.ems.cmc.acl.facade.domain.CmcAclInfo;
import com.topvision.ems.cmc.acl.facade.domain.CmcAclInstall;
import com.topvision.ems.cmc.auth.facade.domain.CcmtsAuthManagement;
import com.topvision.ems.cmc.ccmts.domain.CmcOpticalInfo;
import com.topvision.ems.cmc.ccmts.domain.CmcRealtimeInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcDevSoftware;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcSysControl;
import com.topvision.ems.cmc.config.facade.domain.CmcEmsConfig;
import com.topvision.ems.cmc.config.facade.domain.CmcSysConfig;
import com.topvision.ems.cmc.cpe.domain.CmSystemInfoExt;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBaseConfig;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBundle;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpGiAddr;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpIntIp;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpOption60;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpPacketVlan;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpServerConfig;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.domain.TxPowerLimit;
import com.topvision.ems.cmc.facade.CmcFacade;
import com.topvision.ems.cmc.facade.domain.ChannelCmNumStatic;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmPartialSvcState;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelStaticInfo;
import com.topvision.ems.cmc.facade.domain.CmcIpSubVlanCfgEntry;
import com.topvision.ems.cmc.facade.domain.CmcIpSubVlanScalarObject;
import com.topvision.ems.cmc.facade.domain.CmcPortPerf;
import com.topvision.ems.cmc.facade.domain.CmcSystemBasicInfo;
import com.topvision.ems.cmc.facade.domain.CmcSystemIpInfo;
import com.topvision.ems.cmc.facade.domain.DocsDevEvControl;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.ipqam.domain.CmcEqamProgram;
import com.topvision.ems.cmc.ipqam.domain.CmcEqamStatus;
import com.topvision.ems.cmc.ipqam.domain.IpqamData;
import com.topvision.ems.cmc.ipqam.domain.ProgramIn;
import com.topvision.ems.cmc.ipqam.domain.ProgramOut;
import com.topvision.ems.cmc.macdomain.facade.domain.MacDomainBaseInfo;
import com.topvision.ems.cmc.macdomain.facade.domain.MacDomainStatusInfo;
import com.topvision.ems.cmc.performance.domain.ChannelCmNum;
import com.topvision.ems.cmc.qos.facade.domain.CmMacToServiceFlow;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosParamSetInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosPktClassInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceClass;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceFlowAttribute;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceFlowStatus;
import com.topvision.ems.cmc.sharesecret.facade.domain.CmcShareSecretConfig;
import com.topvision.ems.cmc.sni.facade.domain.CcmtsSniObject;
import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.cmc.sni.facade.domain.CmcRateLimit;
import com.topvision.ems.cmc.sni.facade.domain.CmcSniConfig;
import com.topvision.ems.cmc.syslog.domain.CmcSyslogRecordTypeII;
import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogConfig;
import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogServerEntry;
import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogSwitchEntry;
import com.topvision.ems.cmc.systemtime.facade.domain.CmcSystemTimeConfig;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelRanging;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmtsModulationEntry;
import com.topvision.ems.cmc.vlan.domain.CmcVlanData;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVifSubIpEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanConfigEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanDhcpAllocEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryInterface;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryIp;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.facade.domain.DeviceBaseInfo;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;
import com.topvision.framework.utils.CmcIndexUtils;

/**
 * @author Victor
 * @created @2011-7-1-下午02:56:35
 * 
 */
@Facade("cmcFacade")
public class CmcFacadeImpl extends EmsFacade implements CmcFacade {
    @Resource(name = "snmpExecutorService")
    private SnmpExecutorService snmpExecutorService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#modifySystemBasicInfo(com.topvision
     * .framework.snmp .SnmpParam, com.topvision.ems.facade.domain.DeviceBaseInfo)
     */
    public void modifySystemBasicInfo(SnmpParam snmpParam, DeviceBaseInfo deviceBaseInfo) {
        snmpExecutorService.setData(snmpParam, deviceBaseInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#modifyMacDomainBaseInfo(com.topvision
     * .ems.epon.domain .SnmpParam, com.topvision.ems.cmc.facade.domain.MacDomainBaseInfo)
     */
    @Override
    public MacDomainBaseInfo modifyMacDomainBaseInfo(SnmpParam snmpParam, MacDomainBaseInfo macDomainBaseInfo) {
        return snmpExecutorService.setData(snmpParam, macDomainBaseInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#modifyDownChannelBaseInfo(com.
     * topvision.framework. snmp.SnmpParam,
     * com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo)
     */
    @Override
    public CmcDownChannelBaseShowInfo modifyDownChannelBaseShowInfo(SnmpParam snmpParam,
            CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo) {
        return snmpExecutorService.setData(snmpParam, cmcDownChannelBaseShowInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#modifyUpChannelBaseInfo(com.topvision
     * .framework.snmp .SnmpParam, com.topvision.ems.cmc.facade.domain.CmcUpChannelBaseInfo)
     */
    @Override
    public CmcUpChannelBaseShowInfo modifyUpChannelBaseShowInfo(SnmpParam snmpParam,
            CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo) {
        return snmpExecutorService.setData(snmpParam, cmcUpChannelBaseShowInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#resetCm(com.topvision.framework .snmp.SnmpParam,
     * java.lang.Long)
     */
    @Override
    public void resetCm(SnmpParam snmpParam, String cmMac) {
        // TODO 规则待确定
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#cmAttribute(com.topvision.framework
     * .snmp.SnmpParam)
     */
    @Override
    public CmAttribute getCmAttribute(SnmpParam snmpParam, CmAttribute cmAttribute) {
        return snmpExecutorService.getTableLine(snmpParam, cmAttribute);
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#createOrModifyServiceClassInfo
     * (com.topvision.ems.cmc .domain.SnmpParam,
     * com.topvision.ems.cmc.facade.domain.CmcQosServiceClass)
     */
    @Override
    public CmcQosServiceClass createOrModifyServiceClassInfo(SnmpParam snmpParam, CmcQosServiceClass cmcQosServiceClass) {
        return snmpExecutorService.setData(snmpParam, cmcQosServiceClass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#deleteServiceClassInfo(com.topvision
     * .ems.cmc.domain.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcQosServiceClass)
     */
    @Override
    public CmcQosServiceClass deleteServiceClassInfo(SnmpParam dolSnmpParam, CmcQosServiceClass cmcQosServiceClass) {
        return snmpExecutorService.setData(dolSnmpParam, cmcQosServiceClass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#resetCmcWithotAgent(com.topvision
     * .ems.cmc.domain.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcSysControl)
     */
    @Override
    public CmcSysControl cmcSysControlSetWithotAgent(SnmpParam dolSnmpParam, CmcSysControl cmcSysControl) {
        return snmpExecutorService.setData(dolSnmpParam, cmcSysControl);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#setCmcSniStatus(com.topvision.
     * framework.snmp.SnmpParam, java.lang.Integer)
     */
    @Override
    public void setCmcSniStatus(SnmpParam snmpParam, Integer cmcSniStatus) {
        // TODO 设置物理端口主备状态 main(1), backup(2), autoMainPreferred(3),
        // autoBackupPreferred(4)
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#setCmcSniMainTransmisionMode(com
     * .topvision.framework.snmp.SnmpParam, java.lang.Integer)
     */
    @Override
    public void setCmcSniMainTransmisionMode(SnmpParam snmpParam, Integer sniMainTransmisionMode) {
        // TODO 设置主物理口传输模式 copper(1), fiber(2), autoCopperPreferred(3),
        // autoFiberPreferred(4)
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#setCmcSniBackupTransmisionMode
     * (com.topvision.framework.snmp.SnmpParam, java.lang.Integer)
     */
    @Override
    public void setCmcSniBackupTransmisionMode(SnmpParam snmpParam, Integer sniBackupTransmisionMode) {
        // TODO 设置备用物理口传输模式 copper(1), fiber(2), autoCopperPreferred(3),
        // autoFiberPreferred(4)
    }

    @Override
    public CmcEmsConfig modifyCmcEmsConfig(SnmpParam snmpParam, CmcEmsConfig cmcEmsConfig) {
        return snmpExecutorService.setData(snmpParam, cmcEmsConfig);
    }

    @Override
    public String modifyDocsDevEvControl(SnmpParam snmpParam, DocsDevEvControl docsDevEvControl) {
        // snmpExecutorService.setData(snmpParam, docsDevEvControl);
        return snmpExecutorService.set(snmpParam,
                "1.3.6.1.2.1.69.1.5.7.1.2." + docsDevEvControl.getDocsDevEvPriority(),
                docsDevEvControl.getDocsDevEvReportingHex());
    }

    @Override
    public CmcUpChannelSignalQualityInfo modifyUpChannelSignalQualityInfo(SnmpParam snmpParam,
            CmcUpChannelSignalQualityInfo cmcUpChannelSignalQualityInfo) {
        // 将上行通道参数分开进行设置
        CmcUpChannelSignalQualityInfo afterModified = new CmcUpChannelSignalQualityInfo();
        try {
            String signalPower = snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.4491.2.1.20.1.25.1.2."
                    + cmcUpChannelSignalQualityInfo.getChannelIndex(), cmcUpChannelSignalQualityInfo
                    .getDocsIf3SignalPower().toString());
            afterModified.setDocsIf3SignalPower(Integer.parseInt(signalPower));
        } catch (Exception e) {
            logger.debug("", e);
        }
        return afterModified;
    }

    @Override
    public void modifyCmcUpChannelRxPower(SnmpParam snmpParam, Integer ifIndex, Integer power) {
        snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.4491.2.1.20.1.25.1.2." + ifIndex, "" + power);
    }

    @Override
    public void modifyBsrUpChannelRxPower(SnmpParam snmpParam, Integer ifIndex, Integer power) {
        snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.4981.2.1.2.1.1." + ifIndex, "" + power);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#modifyUpChannelBaseInfo(com.topvision
     * .ems.cmc.domain.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcUpChannelBaseInfo)
     */
    @Override
    public CmcUpChannelBaseInfo modifyUpChannelBaseInfo(SnmpParam snmpParam, CmcUpChannelBaseInfo cmcUpChannelBaseInfo) {
        // 1 关闭通道
        /*
         * String ifAdminStatus = snmpExecutorService .get(snmpParam, "1.3.6.1.2.1.2.2.1.7." +
         * cmcUpChannelBaseInfo.getIfIndex()); if (Integer.valueOf(ifAdminStatus) == 1) {
         * snmpExecutorService.set(snmpParam, "1.3.6.1.2.1.2.2.1.7." +
         * cmcUpChannelBaseInfo.getIfIndex(), "2"); }
         */
        // 2 修改上行通道参数值
        // TODO 将上行通道参数分开进行设置
        CmcUpChannelBaseInfo afterModified = new CmcUpChannelBaseInfo();
        /*
         * CmcUpChannelBaseInfo upChannel = new CmcUpChannelBaseInfo();
         * upChannel.setIfIndex(cmcUpChannelBaseInfo.getIfIndex());
         */
        try {
            /*
             * if (cmcUpChannelBaseInfo.getChannelWidth() != null) {
             * upChannel.setChannelWidth(cmcUpChannelBaseInfo .getChannelWidth());
             * afterModified.setChannelWidth(snmpExecutorService.setData( snmpParam,
             * upChannel).getChannelWidth()); upChannel.setChannelWidth(null); } if
             * (cmcUpChannelBaseInfo.getChannelFrequency() != null) {
             * upChannel.setChannelFrequency(cmcUpChannelBaseInfo .getChannelFrequency());
             * afterModified.setChannelFrequency(snmpExecutorService.setData( snmpParam,
             * upChannel).getChannelFrequency()); upChannel.setChannelFrequency(null); } if
             * (cmcUpChannelBaseInfo.getChannelModulationProfile() != null) {
             * upChannel.setChannelModulationProfile(cmcUpChannelBaseInfo
             * .getChannelModulationProfile());
             * afterModified.setChannelModulationProfile(snmpExecutorService .setData(snmpParam,
             * upChannel) .getChannelModulationProfile()); }
             */
            afterModified = snmpExecutorService.setData(snmpParam, cmcUpChannelBaseInfo);
        } finally {
            /*
             * // 3 开启通道 if (Integer.valueOf(ifAdminStatus) == 1) {
             * snmpExecutorService.set(snmpParam, "1.3.6.1.2.1.2.2.1.7." +
             * cmcUpChannelBaseInfo.getIfIndex(), "1"); }
             */
        }
        return afterModified;
        // return snmpExecutorService.setData(snmpParam,
        // cmcUpChannelBaseInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#modifyDownChannelBaseInfo(com.
     * topvision.ems.cmc.domain.SnmpParam,
     * com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo)
     */
    @Override
    public CmcDownChannelBaseInfo modifyDownChannelBaseInfo(SnmpParam snmpParam,
            CmcDownChannelBaseInfo cmcDownChannelBaseInfo) {
        return snmpExecutorService.setData(snmpParam, cmcDownChannelBaseInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#refreshUpChannelBaseInfo(com.topvision
     * .ems.cmc.domain.SnmpParam)
     */
    @Override
    public List<CmcUpChannelBaseInfo> getUpChannelBaseInfo(SnmpParam dolSnmpParam, Long cmcIndex) {
        List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfos = new ArrayList<CmcUpChannelBaseInfo>();
        for (int i = 1; i < 5; i++) {
            long l = (i << 8);
            CmcUpChannelBaseInfo cmcUpChannelBaseInfo = new CmcUpChannelBaseInfo();
            cmcUpChannelBaseInfo.setChannelIndex(cmcIndex | l);
            cmcUpChannelBaseInfo = snmpExecutorService.getTableLine(dolSnmpParam, cmcUpChannelBaseInfo);
            cmcUpChannelBaseInfos.add(cmcUpChannelBaseInfo);
            logger.debug("\n\nRefresh CmcUpChannelBaseInfo:{}", cmcUpChannelBaseInfo);
        }
        return cmcUpChannelBaseInfos;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getMacDomainBaseInfo(com.topvision
     * .ems.cmc.domain.SnmpParam, java.lang.Long)
     */
    @Override
    public MacDomainStatusInfo getMacDomainStatusInfo(SnmpParam dolSnmpParam, Long cmcIndex) {
        MacDomainStatusInfo macDomainStatusInfo = new MacDomainStatusInfo();
        macDomainStatusInfo.setCmcIndex(cmcIndex);
        return snmpExecutorService.getTableLine(dolSnmpParam, macDomainStatusInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getDownChannelBaseInfo(com.topvision
     * .ems.cmc.domain.SnmpParam, java.lang.Long) /* (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#refreshServiceFlowBaseInfo(com
     * .topvision.ems.cmc.domain.SnmpParam)
     */
    @Override
    public List<CmcDownChannelBaseInfo> getDownChannelBaseInfo(SnmpParam dolSnmpParam, Long cmcIndex) {
        List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos = new ArrayList<CmcDownChannelBaseInfo>();
        for (int i = 1; i < 17; i++) {
            long l = (i << 8);
            CmcDownChannelBaseInfo cmcDownChannelBaseInfo = new CmcDownChannelBaseInfo();
            cmcDownChannelBaseInfo.setChannelIndex(cmcIndex | 0x8000L | l);
            cmcDownChannelBaseInfo = snmpExecutorService.getTableLine(dolSnmpParam, cmcDownChannelBaseInfo);
            cmcDownChannelBaseInfos.add(cmcDownChannelBaseInfo);
            logger.debug("\n\ncmcDownChannelBaseInfo:{}", cmcDownChannelBaseInfo);
        }
        return cmcDownChannelBaseInfos;
    }

    @Override
    public String getUpChannelType(SnmpParam dolSnmpParam, Long channelIndex) {
        return snmpExecutorService.get(dolSnmpParam, "1.3.6.1.2.1.10.127.1.1.2.1.15." + channelIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getCmcDownChannelStaticInfo(com
     * .topvision.ems.cmc.domain.SnmpParam, java.lang.Long)
     */
    @Override
    public List<CmcDownChannelStaticInfo> getCmcDownChannelStaticInfo(SnmpParam dolSnmpParam, Long cmcIndex) {
        List<CmcDownChannelStaticInfo> cmcDownChannleStaticInfos = new ArrayList<CmcDownChannelStaticInfo>();
        for (int i = 1; i < 17; i++) {
            long l = (i << 8);
            CmcDownChannelStaticInfo cmcDownChannelStaticInfo = new CmcDownChannelStaticInfo();
            cmcDownChannelStaticInfo.setChannelIndex(cmcIndex | 0x8000L | l);
            cmcDownChannelStaticInfo = snmpExecutorService.getTableLine(dolSnmpParam, cmcDownChannelStaticInfo);
            cmcDownChannleStaticInfos.add(cmcDownChannelStaticInfo);
            logger.debug("\n\ncmcDownChannelStaticInfo:{}", cmcDownChannelStaticInfo);
        }
        return cmcDownChannleStaticInfos;
    }

    @Override
    public List<CmcQosServiceFlowAttribute> refreshServiceFlowBaseInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcQosServiceFlowAttribute.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#refreshServiceFlowStatusInfo(com
     * .topvision.ems.cmc.domain.SnmpParam)
     */
    @Override
    public List<CmcQosServiceFlowStatus> refreshServiceFlowStatusInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcQosServiceFlowStatus.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#refreshServiceFlowPktClassInfos
     * (com.topvision.ems.cmc.domain.SnmpParam)
     */
    @Override
    public List<CmcQosPktClassInfo> refreshServiceFlowPktClassInfos(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcQosPktClassInfo.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#refreshServiceFlowParamSetInfos
     * (com.topvision.ems.cmc.domain.SnmpParam)
     */
    @Override
    public List<CmcQosParamSetInfo> refreshServiceFlowParamSetInfos(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcQosParamSetInfo.class);
    }

    @Override
    public List<CmcQosParamSetInfo> refreshServiceFlowParamSetOnCC(SnmpParam snmpParam, Long cmcIndex,
            List<Long> serviceFlowIdList) {
        // TODO 采集单台CC上的参数集信息，当前getTableLines不支持，因此使用getTableLine进行替换
        List<CmcQosParamSetInfo> cmcQosParamSetInfoList = new ArrayList<CmcQosParamSetInfo>();
        for (Long serviceflowid : serviceFlowIdList) {
            List<CmcQosParamSetInfo> cmcQosSetlist = new ArrayList<CmcQosParamSetInfo>();
            for (int i = 1; i <= 3; i++) {
                CmcQosParamSetInfo cmcQosSet = new CmcQosParamSetInfo();
                cmcQosSet.setCmcIndex(cmcIndex);
                cmcQosSet.setServiceFlowId(serviceflowid);
                cmcQosSet.setType(i);
                try {
                    cmcQosSet = snmpExecutorService.getTableLine(snmpParam, cmcQosSet);
                    if (cmcQosSet.getPriority() == null || cmcQosSet.getPriority() == 129) {
                        continue;
                    }
                    cmcQosSetlist.add(cmcQosSet);
                } catch (Exception e) {
                    logger.info("the exception CmcQosParamSetInfo ", e.getClass());
                }
            }
            cmcQosParamSetInfoList.addAll(cmcQosSetlist);
        }
        return cmcQosParamSetInfoList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#refreshCmMacToServiceFlows(com
     * .topvision.ems.cmc.domain.SnmpParam)
     */
    @Override
    public List<CmMacToServiceFlow> refreshCmMacToServiceFlows(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmMacToServiceFlow.class);
    }

    @Override
    public List<CmcUpChannelSignalQualityInfo> getUpChannelSignalQualityInfo(SnmpParam dolSnmpParam, Long cmcIndex) {
        List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfos = new ArrayList<CmcUpChannelSignalQualityInfo>();
        for (int i = 1; i < 5; i++) {
            long l = (i << 8);
            CmcUpChannelSignalQualityInfo cmcUpChannelSignalQualityInfo = new CmcUpChannelSignalQualityInfo();
            cmcUpChannelSignalQualityInfo.setChannelIndex(cmcIndex | l);

            List<String> excludeOids = new ArrayList<String>();
            excludeOids.add("1.3.6.1.4.1.4981.2.1.2.1.1");
            cmcUpChannelSignalQualityInfo = snmpExecutorService.getTableLine(dolSnmpParam,
                    cmcUpChannelSignalQualityInfo, excludeOids);
            cmcUpChannelSignalQualityInfos.add(cmcUpChannelSignalQualityInfo);
        }
        return cmcUpChannelSignalQualityInfos;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#modifyCmcPortInfo(com.topvision
     * .ems.cmc.domain.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcPort)
     */
    @Override
    public CmcPort modifyCmcPortInfo(SnmpParam snmpParam, CmcPort cmcPort) {
        // 修改上行通道状态后需要从设备取数据，更新通道的工作状态
        snmpExecutorService.setData(snmpParam, cmcPort);
        return snmpExecutorService.getTableLine(snmpParam, cmcPort);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getCmcPort(java.lang.Long)
     */
    @Override
    public List<CmcPort> getCmcPorts(SnmpParam snmpParam, Long cmcIndex) {
        List<CmcPort> cmcPorts = new ArrayList<CmcPort>();
        for (int i = 1; i < 17; i++) {
            long l = (i << 8);
            CmcPort cmcPort = new CmcPort();
            cmcPort.setIfIndex(cmcIndex | 0x8000L | l);
            snmpExecutorService.getTableLine(snmpParam, cmcPort);
            cmcPorts.add(cmcPort);
        }
        for (int i = 1; i < 5; i++) {
            long l = (i << 8);
            CmcPort cmcPort = new CmcPort();
            cmcPort.setIfIndex(cmcIndex | l);
            cmcPort = snmpExecutorService.getTableLine(snmpParam, cmcPort);
            cmcPorts.add(cmcPort);
        }
        return cmcPorts;
    }

    @Override
    public CmcPort getCmcPortByIfIndex(SnmpParam snmpParam, CmcPort cmcPort) {
        return snmpExecutorService.getTableLine(snmpParam, cmcPort);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getUplinkSpeedPerf(java.lang.Long)
     */
    @Override
    public List<CmcPortPerf> getCmcPortPerfs(Long cmcIndex) {
        List<CmcPortPerf> cmcPortPerfs = new ArrayList<CmcPortPerf>();
        for (int i = 1; i < 17; i++) {
            long l = (i << 8);
            CmcPortPerf cmcPortPerf = new CmcPortPerf();
            cmcPortPerf.setIfIndex(cmcIndex | 0x8000L | l);
            cmcPortPerfs.add(cmcPortPerf);
        }
        return cmcPortPerfs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#setChannelUtilizationInteInterval
     * (com.topvision.ems.cmc.domain.SnmpParam, java.lang.Long)
     */
    @Override
    public void setChannelUtilizationInteInterval(SnmpParam dolSnmpParam, Long period) {
        snmpExecutorService.set(dolSnmpParam, "1.3.6.1.2.1.10.127.1.3.8.0", period.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getChannelUtilizationInteInterval
     * (com.topvision.ems.cmc.domain.SnmpParam, java.lang.Long)
     */
    @Override
    public String getChannelUtilizationInteInterval(SnmpParam dolSnmpParam) {
        return snmpExecutorService.get(dolSnmpParam, "1.3.6.1.2.1.10.127.1.3.8.0");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getUpChannelCmNums(com.topvision
     * .ems.cmc.domain.SnmpParam, java.lang.Long)
     */
    @Override
    public List<ChannelCmNum> getUpChannelCmNums(SnmpParam dolSnmpParam, Long cmcIndex) {
        List<ChannelCmNum> list = new ArrayList<ChannelCmNum>();
        Timestamp dt = new Timestamp(System.currentTimeMillis());
        for (int i = 1; i < 5; i++) {
            long l = (i << 8);
            ChannelCmNumStatic channelCmStatic = new ChannelCmNumStatic();
            channelCmStatic.setChannelIndex((cmcIndex | l));
            try {
                channelCmStatic = snmpExecutorService.getTableLine(dolSnmpParam, channelCmStatic);
                ChannelCmNum channelCmNum = new ChannelCmNum();
                channelCmNum.setDt(dt);
                channelCmNum.setChannelIndex(channelCmStatic.getChannelIndex());
                channelCmNum.setCmNumActive(channelCmStatic.getCmNumActive());
                channelCmNum.setCmNumOffline(channelCmStatic.getCmNumOffline());
                channelCmNum.setCmNumOnline(channelCmStatic.getCmNumOnline());
                channelCmNum.setCmNumRregistered(channelCmStatic.getCmNumRregistered());
                channelCmNum.setCmNumTotal(channelCmStatic.getCmNumTotal());
                channelCmNum.setCmNumUnregistered(channelCmStatic.getCmNumUnregistered());
                channelCmNum.setChannelType(CmcIndexUtils.getChannelType(channelCmStatic.getChannelIndex()).intValue());
                list.add(channelCmNum);
            } catch (Exception e) {
                logger.debug("", e);
            }
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getDownChannelCmNums(com.topvision
     * .ems.cmc.domain.SnmpParam, java.lang.Long)
     */
    @Override
    public List<ChannelCmNum> getDownChannelCmNums(SnmpParam dolSnmpParam, Long cmcIndex) {
        List<ChannelCmNum> list = new ArrayList<ChannelCmNum>();
        Timestamp dt = new Timestamp(System.currentTimeMillis());
        for (int i = 1; i < 17; i++) {
            long l = (i << 8);
            ChannelCmNumStatic channelCmStatic = new ChannelCmNumStatic();
            channelCmStatic.setChannelIndex((cmcIndex | 0x8000L | l));
            try {
                channelCmStatic = snmpExecutorService.getTableLine(dolSnmpParam, channelCmStatic);
                ChannelCmNum channelCmNum = new ChannelCmNum();
                channelCmNum.setDt(dt);
                channelCmNum.setChannelIndex(channelCmStatic.getChannelIndex());
                channelCmNum.setCmNumActive(channelCmStatic.getCmNumActive());
                channelCmNum.setCmNumOffline(channelCmStatic.getCmNumOffline());
                channelCmNum.setCmNumOnline(channelCmStatic.getCmNumOnline());
                channelCmNum.setCmNumRregistered(channelCmStatic.getCmNumRregistered());
                channelCmNum.setCmNumTotal(channelCmStatic.getCmNumTotal());
                channelCmNum.setCmNumUnregistered(channelCmStatic.getCmNumUnregistered());
                channelCmNum.setChannelType(CmcIndexUtils.getChannelType(channelCmStatic.getChannelIndex()).intValue());
                list.add(channelCmNum);
            } catch (Exception e) {
                logger.debug("", e);
            }
        }
        return list;
    }

    @Override
    public List<CmcUpChannelBaseInfo> getCC8800BUpChannelBaseInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcUpChannelBaseInfo.class);
    }

    @Override
    public List<CmcUpChannelSignalQualityInfo> getCC8800BUpChannelSignalQualityInfo(SnmpParam snmpParam) {
        List<String> excludeOids = new ArrayList<String>();
        excludeOids.add("1.3.6.1.4.1.4981.2.1.2.1.1");
        List<CmcUpChannelSignalQualityInfo> signalQualityInfos = null;
        signalQualityInfos = snmpExecutorService.getTable(snmpParam, CmcUpChannelSignalQualityInfo.class, excludeOids);
        return signalQualityInfos;
    }

    @Override
    public List<CmcUpChannelSignalQualityInfo> getBsr2000UpChannelSignalQualityInfo(SnmpParam snmpParam) {
        List<String> excludeOids = new ArrayList<String>();
        List<CmcUpChannelSignalQualityInfo> signalQualityInfos = null;
        excludeOids.add("1.3.6.1.4.1.4491.2.1.20.1.25.1.2");
        signalQualityInfos = snmpExecutorService.getTable(snmpParam, CmcUpChannelSignalQualityInfo.class, excludeOids);
        return signalQualityInfos;
    }

    @Override
    public List<CmcDownChannelBaseInfo> getCC8800BDownChannelBaseInfo(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcDownChannelBaseInfo.class);
    }

    @Override
    public List<CmcPort> getCmc8800BPorts(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcPort.class);
    }

    @Override
    public List<CmcPortPerf> getCmc8800BPortPerfs(SnmpParam snmpParam) {
        List<CmcPortPerf> cmcPortPerfs = null;
        try {
            cmcPortPerfs = snmpExecutorService.getTable(snmpParam, CmcPortPerf.class);
        } catch (Exception e) {
            logger.debug("", e);
        }
        return cmcPortPerfs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#renameCcname(com.topvision.ems
     * .cmc.domain.SnmpParam , com.topvision.ems.cmc.facade.domain.CmcSystemBasicInfo)
     */
    @Override
    public void renameCcname(SnmpParam dolSnmpParam, CmcSystemBasicInfo cmcSystemBasicInfo) {
        snmpExecutorService.setData(dolSnmpParam, cmcSystemBasicInfo);
    }

    @Override
    public List<CmcDownChannelStaticInfo> getCC8800BCmcDownChannelStaticInfo(SnmpParam snmpParam) {
        List<CmcDownChannelStaticInfo> list = new ArrayList<CmcDownChannelStaticInfo>();
        try {
            list = snmpExecutorService.getTable(snmpParam, CmcDownChannelStaticInfo.class);
        } catch (Exception e) {
            logger.debug("", e);
        }
        return list;
    }

    @Override
    public CmcAttribute getCmcAttribute(SnmpParam snmpParam) {
        CmcAttribute cmcAttribute = null;
        try {
            List<CmcAttribute> list = snmpExecutorService.getTable(snmpParam, CmcAttribute.class);
            cmcAttribute = list.get(0);
        } catch (Exception e) {
            logger.error("refresh action{} error", "getCmcAttribute");
        }
        return cmcAttribute;
    }

    @Override
    public CmcVlanPrimaryInterface getCmcVlanPrimaryInterface(SnmpParam snmpParam) {
        CmcVlanPrimaryInterface cmcVlanPrimaryInterface = null;
        try {
            cmcVlanPrimaryInterface = snmpExecutorService.getData(snmpParam, CmcVlanPrimaryInterface.class);
            // .get(snmpParam, cmcVlanPrimaryInterface);
        } catch (Exception e) {
            logger.error("refresh action{} error", "getCmcVlanPrimaryInterface");
        }
        return cmcVlanPrimaryInterface;
    }

    @Override
    public List<CmcVlanPrimaryIp> getCmcVlanPrimaryIpList(SnmpParam snmpParam) {
        List<CmcVlanPrimaryIp> list = new ArrayList<CmcVlanPrimaryIp>();
        try {
            list = snmpExecutorService.getTable(snmpParam, CmcVlanPrimaryIp.class);
        } catch (Exception e) {
            logger.error("refresh action{} error", "getCmcVlanPrimaryIpList");
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#modifyDhcpServerInfo(com.topvision
     * .ems.cmc.domain.SnmpParam, com.topvision.ems.cmc.facade.domain.cmcDhcpServerConfig)
     */
    @Override
    public CmcDhcpBundle modifyDhcpBundleInfo(SnmpParam snmpParam, CmcDhcpBundle cmcDhcpBundle) {
        CmcDhcpBundle afterModified = null;
        afterModified = snmpExecutorService.setData(snmpParam, cmcDhcpBundle);
        return afterModified;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#modifyDhcpServerInfo(com.topvision
     * .ems.cmc.domain.SnmpParam, com.topvision.ems.cmc.facade.domain.cmcDhcpServerConfig)
     */
    @Override
    public CmcDhcpServerConfig modifyDhcpServerInfo(SnmpParam snmpParam, CmcDhcpServerConfig cmcDhcpServerConfig) {
        return snmpExecutorService.setData(snmpParam, cmcDhcpServerConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#modifyDhcpGiAddrInfo(com.topvision
     * .ems.cmc.domain.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcDhcpGiAddr)
     */
    @Override
    public CmcDhcpGiAddr modifyDhcpGiAddrInfo(SnmpParam snmpParam, CmcDhcpGiAddr cmcDhcpGiAddr) {
        return snmpExecutorService.setData(snmpParam, cmcDhcpGiAddr);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#modifyDhcpOption60Info(com.topvision
     * .ems.cmc.domain.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcDhcpOption60)
     */
    @Override
    public CmcDhcpOption60 modifyDhcpOption60Info(SnmpParam dolSnmpParam, CmcDhcpOption60 cmcDhcpOption60) {
        CmcDhcpOption60 afterModified;
        afterModified = snmpExecutorService.setData(dolSnmpParam, cmcDhcpOption60);
        return afterModified;
    }

    @Override
    public CmcDhcpPacketVlan modifyDhcpPacketVlan(SnmpParam dolSnmpParam, CmcDhcpPacketVlan cmcDhcpPacketVlan) {
        CmcDhcpPacketVlan afterModified;
        afterModified = snmpExecutorService.setData(dolSnmpParam, cmcDhcpPacketVlan);
        return afterModified;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getCmcDhcpBundleInfo(com.topvision
     * .ems.cmc.domain.SnmpParam)
     */
    @Override
    public List<CmcDhcpBundle> getCmcDhcpBundleInfo(SnmpParam snmpParam) {
        List<CmcDhcpBundle> cmcDhcpBundles = snmpExecutorService.getTable(snmpParam, CmcDhcpBundle.class);
        return cmcDhcpBundles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getCmcDhcpServerConfigInfo(com .topvision
     * .ems.cmc.domain.SnmpParam)
     */
    @Override
    public List<CmcDhcpServerConfig> getCmcDhcpServerConfigInfo(SnmpParam snmpParam) {
        List<CmcDhcpServerConfig> cmcDhcpServerConfigs = snmpExecutorService.getTable(snmpParam,
                CmcDhcpServerConfig.class);
        return cmcDhcpServerConfigs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getCmcDhcpGiAddrInfo(com.topvision
     * .ems.cmc.domain.SnmpParam)
     */
    @Override
    public List<CmcDhcpGiAddr> getCmcDhcpGiAddrInfo(SnmpParam snmpParam) {
        List<CmcDhcpGiAddr> cmcDhcpGiAddrs = snmpExecutorService.getTable(snmpParam, CmcDhcpGiAddr.class);
        return cmcDhcpGiAddrs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getCmcDhcpOption60Info(com.topvision
     * .ems.cmc.domain.SnmpParam)
     */
    @Override
    public List<CmcDhcpOption60> getCmcDhcpOption60Info(SnmpParam snmpParam) {
        List<CmcDhcpOption60> cmcDhcpOption60s = snmpExecutorService.getTable(snmpParam, CmcDhcpOption60.class);
        return cmcDhcpOption60s;
    }

    @Override
    public List<CmcDhcpIntIp> getCmcDhcpIntIp(SnmpParam snmpParam) {
        List<CmcDhcpIntIp> intIps = snmpExecutorService.getTable(snmpParam, CmcDhcpIntIp.class);
        return intIps;
    }

    public List<CmcDhcpPacketVlan> getCmcDhcpPacketVlan(SnmpParam snmpParam) {
        List<CmcDhcpPacketVlan> packetVlans = snmpExecutorService.getTable(snmpParam, CmcDhcpPacketVlan.class);
        return packetVlans;
    }

    @Override
    public CmcAttribute modifyCmcAttribute(SnmpParam dolSnmpParam, CmcAttribute cmcAttribute) {
        CmcAttribute afterModified = new CmcAttribute();
        try {
            afterModified = snmpExecutorService.setData(dolSnmpParam, cmcAttribute);
        } catch (Exception e) {
            logger.debug("", e);
        }
        return afterModified;
    }

    @Override
    public CmcVlanPrimaryInterface modifyCmcDefaultRoute(SnmpParam dolSnmpParam,
            CmcVlanPrimaryInterface cmcVlanPrimaryInterface) {
        CmcVlanPrimaryInterface afterModified = new CmcVlanPrimaryInterface();
        try {
            afterModified = cmcVlanPrimaryInterface;
            afterModified.setCmcId(cmcVlanPrimaryInterface.getCmcId());
            afterModified.setVlanPrimaryInterface(cmcVlanPrimaryInterface.getVlanPrimaryInterface());
            String defaultRoute = snmpExecutorService.set(dolSnmpParam, "1.3.6.1.4.1.32285.11.1.1.2.7.1.2.0",
                    cmcVlanPrimaryInterface.getVlanPrimaryDefaultRoute());
            afterModified.setVlanPrimaryDefaultRoute(defaultRoute);
        } catch (Exception e) {
            logger.debug("", e);
        }
        return afterModified;
    }

    @Override
    public CmcVlanPrimaryIp modifyCmcVlanPrimaryIp(SnmpParam dolSnmpParam, CmcVlanPrimaryIp cmcVlanPrimaryIp) {
        CmcVlanPrimaryIp afterModified = new CmcVlanPrimaryIp();
        try {
            /*
             * 更改IP,所以不需要设置多次
             */
            dolSnmpParam.setRetry((byte) 0);
            afterModified = snmpExecutorService.setData(dolSnmpParam, cmcVlanPrimaryIp);
        } catch (Exception e) {
            logger.info("modifyCmcVlanPrimaryIp finished");
        }
        return afterModified;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#updateCc8800bSniObject(com.topvision
     * .ems.cmc.domain .SnmpParam, com.topvision.ems.cmc.facade.domain.CcmtsSniObject)
     */
    @Override
    public CcmtsSniObject updateCc8800bSniObject(SnmpParam snmpParam, CcmtsSniObject sni) {
        return snmpExecutorService.setData(snmpParam, sni);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#updateCmc(com.topvision
     * .ems.cmc.domain.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcDevSoftware)
     */
    @Override
    public CmcDevSoftware updateCmc(SnmpParam snmpParam, CmcDevSoftware cmcDevSoftware) {
        CmcDevSoftware afterModified = snmpExecutorService.setData(snmpParam, cmcDevSoftware);
        logger.debug("\n\nSet CmcDevSoftware:{}", cmcDevSoftware);
        return afterModified;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#modifyCmcDhcpRelayBaseInfo(com .topvision
     * .ems.cmc.domain.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcDhcpBaseConfig)
     */
    @Override
    public CmcDhcpBaseConfig modifyCmcDhcpRelayBaseInfo(SnmpParam snmpParam, CmcDhcpBaseConfig cmcDhcpBaseConfig) {
        return snmpExecutorService.setData(snmpParam, cmcDhcpBaseConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getCmcDhcpRelayBaseInfo(com.topvision
     * .ems.cmc.domain.SnmpParam, , java.lang.Long)
     */
    @Override
    public CmcDhcpBaseConfig getCmcDhcpRelayBaseInfo(SnmpParam snmpParam) {
        CmcDhcpBaseConfig cmcDhcpBaseConfig = snmpExecutorService.getData(snmpParam, CmcDhcpBaseConfig.class);
        return cmcDhcpBaseConfig;
    }

    @Override
    public Integer getCmcSaveStatus(SnmpParam snmpParam) {
        AtomicInteger status = new AtomicInteger(-1);
        try {
            status = snmpExecutorService.execute(new SnmpWorker<AtomicInteger>(snmpParam) {
                private static final long serialVersionUID = -727320896339548070L;

                @Override
                protected void exec() {
                    snmpUtil.reset(snmpParam);
                    result.set(Integer.parseInt(snmpUtil.get("1.3.6.1.4.1.32285.11.1.1.2.1.6.2.0")));
                }
            }, status);
        } catch (Exception e) {
            throw new SnmpSetException(e);
        }
        return status.intValue();
    }

    @Override
    public void saveCmcConfig(SnmpParam snmpParam) {
        snmpParam.setTimeout(3000L);
        String oid = "1.3.6.1.4.1.32285.11.1.1.2.1.6.1.0";
        snmpExecutorService.set(snmpParam, oid, "1");
    }

    @Override
    public Integer getCmcUpdateProgress(SnmpParam snmpParam) {
        return Integer.parseInt(snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.5.1.9.3.0"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getCmcAuthStatus(com.topvision
     * .ems.cmc.domain.SnmpParam )
     */
    @Override
    public Integer getCmcAuthStatus(SnmpParam snmpParam) {
        return Integer.parseInt(snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.8.2.0"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getCmcAuthInfo(com.topvision.ems
     * .cmc.domain.SnmpParam , com.topvision.ems.cmc.facade.domain.CcmtsAuthManagement)
     */
    @Override
    public CcmtsAuthManagement getCmcAuthInfo(SnmpParam snmpParam, CcmtsAuthManagement authMgmt) {
        return snmpExecutorService.getTableLine(snmpParam, authMgmt);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#setCmcAuthInfo(com.topvision.ems
     * .cmc.domain.SnmpParam , com.topvision.ems.cmc.facade.domain.CcmtsAuthManagement)
     */
    @Override
    public CcmtsAuthManagement setCmcAuthInfo(SnmpParam snmpParam, CcmtsAuthManagement authMgmt) {
        return snmpExecutorService.setData(snmpParam, authMgmt);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#modifyCcmtsBasicInfo(com.topvision
     * .framework.snmp. SnmpParam, com.topvision.ems.cmc.facade.domain.CmcSystemBasicInfo)
     */
    @Override
    public void modifyCcmtsBasicInfo(SnmpParam snmpParam, CmcSystemBasicInfo cmcSystemBasicInfo) {
        snmpExecutorService.setData(snmpParam, cmcSystemBasicInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#refreshServiceFlowBaseInfoOnCC
     * (com.topvision.framework .snmp.SnmpParam, java.lang.Long)
     */
    @Override
    public List<CmcQosServiceFlowAttribute> refreshServiceFlowBaseInfoOnCC(SnmpParam snmpParam, Long cmcIndex) {
        CmcQosServiceFlowAttribute cmcQosSFAttr = new CmcQosServiceFlowAttribute();
        cmcQosSFAttr.setCmcIndex(cmcIndex);
        // cmcQosSFAttr.setDocsQosServiceFlowId(1l);
        List<CmcQosServiceFlowAttribute> cmcQoSFAttrlist = new ArrayList<CmcQosServiceFlowAttribute>();
        try {
            // cmcOosSFAttrlist = snmpUtil.getTableLines(cmcQosSFAttr, 0,
            // Integer.MAX_VALUE);//
            // 假如以1开头，则如果某台CC没有1，就会造成中断，因此不能以1开头，应该以0开头，但是需要把获取的第一个值去掉
            // cmcOosSFAttrlist = snmpExecutorService.getTableLines(snmpParam,
            // cmcQosSFAttr,
            // 0,Integer.MAX_VALUE);// .getTableLines(cmcOosSFAttr, 0,
            // Integer.MAX_VALUE);//
            // 假如以1开头，则如果某台CC没有1，就会造成中断，因此不能以1开头，应该以0开头，但是需要把获取的第一个值去掉
            cmcQoSFAttrlist = snmpExecutorService.getTableLines(snmpParam, cmcQosSFAttr, 0, Integer.MAX_VALUE);
            // .getTableLines(cmcOosSFAttr,
            // 0,
            // Integer.MAX_VALUE);//
            // 假如以1开头，则如果某台CC没有1，就会造成中断，因此不能以1开头，应该以0开头，但是需要把获取的第一个值去掉
            cmcQoSFAttrlist.remove(0);

            /*
             * String str = snmpExecutorService.getNext(snmpParam,CmcConstants.
             * DOCSQOSSERVICEFLOWID+"." +cmcIndex+".0"); Long sfId = Long.parseLong(str);//第一个sfId值
             * while(true){ String temp=""; CmcQosServiceFlowAttribute cmcQosSFAttr = new
             * CmcQosServiceFlowAttribute(); cmcQosSFAttr.setCmcIndex(cmcIndex);
             * cmcQosSFAttr.setDocsQosServiceFlowId(sfId); cmcQosSFAttr =
             * snmpExecutorService.getTableLine(snmpParam, cmcQosSFAttr);
             * cmcosQSFAttrlist.add(cmcQosSFAttr);
             * 
             * temp = snmpExecutorService.getNext(snmpParam
             * ,CmcConstants.DOCSQOSSERVICEFLOWID+"."+cmcIndex+"."+sfId);
             * if(sfId>=Long.parseLong(temp)){ return cmcosQSFAttrlist; }else{ sfId =
             * Long.parseLong(temp); } }
             */
        } catch (Exception e) {
            // TODO: handle exception
            logger.info("refreshServiceFlowBaseInfoOnCC{}", e);
        }
        return cmcQoSFAttrlist;
    }

    @Override
    public Integer changeChannelAdminstatus(SnmpParam snmpParam, Long ifIndex, Integer status) {
        String oid = "1.3.6.1.2.1.2.2.1.7." + ifIndex;
        return Integer.valueOf(snmpExecutorService.set(snmpParam, oid, status.toString()));
    }

    @Override
    public void modifyCmcCpuPortRateLimit(SnmpParam snmpParam, CmcRateLimit cmcRateLimit) {
        snmpExecutorService.setData(snmpParam, cmcRateLimit);
    }

    @Override
    public CmcDhcpIntIp modifyDhcpVirtralIp(SnmpParam snmpParam, CmcDhcpIntIp virtualIp) {
        return snmpExecutorService.setData(snmpParam, virtualIp);
    }

    @Override
    public void modifyCmcSniRateLimit(SnmpParam snmpParam, CmcRateLimit cmcRateLimit) {
        snmpExecutorService.setData(snmpParam, cmcRateLimit);

    }

    @Override
    public void modifyCmcSniPhyConfig(SnmpParam snmpParam, List<CmcPhyConfig> cmcPhyConfigList) {
        for (int i = 0; i < cmcPhyConfigList.size(); i++) {
            snmpExecutorService.setData(snmpParam, cmcPhyConfigList.get(i));
        }
    }

    @Override
    public void modifySniLoopbackStatus(SnmpParam snmpParam, CmcSniConfig cmcSniConfig) {
        snmpExecutorService.setData(snmpParam, cmcSniConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getAclAllList(com.topvision.framework
     * .snmp.SnmpParam)
     */
    @Override
    public List<CmcAclInfo> getAclAllList(SnmpParam snmpParam) {
        List<CmcAclInfo> allAcl = snmpExecutorService.getTable(snmpParam, CmcAclInfo.class);
        return allAcl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getAclAllPositionDefaultAct(com
     * .topvision.framework .snmp.SnmpParam)
     */
    @Override
    public CmcAclDefAction getAclPositionDefaultAct(SnmpParam snmpParam, CmcAclDefAction defAct) {
        CmcAclDefAction aclAct = snmpExecutorService.getTableLine(snmpParam, defAct);
        return aclAct;
    }

    @Override
    public List<CmcAclDefAction> getAllAclPositionDefAct(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcAclDefAction.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#modifyAclPositionDefAct(com.topvision
     * .framework.snmp .SnmpParam, com.topvision.ems.cmc.facade.domain.CmcAclDefAction)
     */
    @Override
    public boolean modifyAclPositionDefAct(SnmpParam snmpParam, CmcAclDefAction defAct) {
        snmpExecutorService.setData(snmpParam, defAct);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#modifyAclInfo(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcAclInfo)
     */
    @Override
    public CmcAclInfo modifyAclInfo(SnmpParam snmpParam, CmcAclInfo aclInfo) {
        CmcAclInfo temp = snmpExecutorService.setData(snmpParam, aclInfo);

        return temp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#getAclInfo(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcAclInfo)
     */
    @Override
    public CmcAclInfo getAclInfo(SnmpParam snmpParam, CmcAclInfo aclInfo) {
        CmcAclInfo info = snmpExecutorService.getTableLine(snmpParam, aclInfo);
        return info;
    }

    @Override
    public CmcIpSubVlanScalarObject getCmcIpSubVlanScalar(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, CmcIpSubVlanScalarObject.class);
    }

    @Override
    public CmcIpSubVlanScalarObject setCmcIpSubVlanScalar(SnmpParam snmpParam, CmcIpSubVlanScalarObject obj) {
        return snmpExecutorService.setData(snmpParam, obj);
    }

    @Override
    public CmcIpSubVlanCfgEntry createCmcIpSubVlanCfg(SnmpParam snmpParam, CmcIpSubVlanCfgEntry vlanCfgEntry) {
        vlanCfgEntry.setTopCcmtsIpSubVlanRowStatus(RowStatus.CREATE_AND_GO);
        // 对CC8800B 索引给全FF
        vlanCfgEntry.setTopCcmtsIpSubVlanIfIndex(65535);
        return snmpExecutorService.setData(snmpParam, vlanCfgEntry);
    }

    @Override
    public CmcIpSubVlanCfgEntry setCmcIpSubVlanCfg(SnmpParam snmpParam, CmcIpSubVlanCfgEntry vlanCfgEntry) {
        // 修改时需要将index和rowStatus一起下发
        vlanCfgEntry.setTopCcmtsIpSubVlanIfIndex(65535);
        vlanCfgEntry.setTopCcmtsIpSubVlanRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, vlanCfgEntry);
    }

    @Override
    public CmcIpSubVlanCfgEntry destoryCmcIpSubVlanCfg(SnmpParam snmpParam, CmcIpSubVlanCfgEntry vlanCfgEntry) {
        vlanCfgEntry.setTopCcmtsIpSubVlanIfIndex(65535);
        vlanCfgEntry.setTopCcmtsIpSubVlanRowStatus(RowStatus.DESTORY);
        return snmpExecutorService.setData(snmpParam, vlanCfgEntry);
    }

    @Override
    public List<CmcIpSubVlanCfgEntry> getCmcIpSubVlanCfgList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcIpSubVlanCfgEntry.class);
    }

    @Override
    public List<CmcSyslogServerEntry> getCmcSyslogServerAttrs(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcSyslogServerEntry.class);
    }

    @Override
    public void createCmcSyslogServer(SnmpParam snmpParam, CmcSyslogServerEntry cmcSyslogServerEntry) {
        // TODO Auto-generated method stub
        cmcSyslogServerEntry.setTopCcmtsSyslogServerStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, cmcSyslogServerEntry);
    }

    @Override
    public CmcVlanConfigEntry createCmcVlanCfg(SnmpParam snmpParam, CmcVlanConfigEntry cmcVlanConfigEntry) {
        cmcVlanConfigEntry.setTopCcmtsVlanStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, cmcVlanConfigEntry);
    }

    @Override
    public CmcVlanConfigEntry destoryCmcVlanCfg(SnmpParam snmpParam, CmcVlanConfigEntry cmcVlanConfigEntry) {
        cmcVlanConfigEntry.setTopCcmtsVlanStatus(RowStatus.DESTORY);
        return snmpExecutorService.setData(snmpParam, cmcVlanConfigEntry);
    }

    @Override
    public CmcVlanPrimaryIp createCmcVlanPrimaryIp(SnmpParam snmpParam, CmcVlanPrimaryIp cmcVlanPrimaryIp) {
        // cmcVlanPrimaryIp.setTopCcmtsVifPriIpStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, cmcVlanPrimaryIp);
    }

    @Override
    public CmcVifSubIpEntry createCmcVifSubIpEntry(SnmpParam snmpParam, CmcVifSubIpEntry cmcVifSubIpEntry) {
        cmcVifSubIpEntry.setTopCcmtsVifSubIpStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, cmcVifSubIpEntry);
    }

    @Override
    public CmcVifSubIpEntry setCmcVifSubIpEntry(SnmpParam snmpParam, CmcVifSubIpEntry cmcVifSubIpEntry) {
        cmcVifSubIpEntry.setTopCcmtsVifSubIpStatus(3);
        return snmpExecutorService.setData(snmpParam, cmcVifSubIpEntry);
    }

    @Override
    public CmcVlanPrimaryIp destoryCmcVlanPrimaryIp(SnmpParam snmpParam, CmcVlanPrimaryIp cmcVlanPrimaryIp) {
        cmcVlanPrimaryIp.setTopCcmtsVifPriIpStatus(RowStatus.DESTORY);
        return snmpExecutorService.setData(snmpParam, cmcVlanPrimaryIp);
    }

    @Override
    public CmcVifSubIpEntry destoryCmcVifSubIpEntry(SnmpParam snmpParam, CmcVifSubIpEntry cmcVifSubIpEntry) {
        cmcVifSubIpEntry.setTopCcmtsVifSubIpStatus(RowStatus.DESTORY);
        return snmpExecutorService.setData(snmpParam, cmcVifSubIpEntry);
    }

    @Override
    public CmcVlanDhcpAllocEntry setCmcVlanPriIpDhcpCfg(SnmpParam snmpParam, CmcVlanDhcpAllocEntry cmcDhcpAlloc) {
        return snmpExecutorService.setData(snmpParam, cmcDhcpAlloc);
    }

    @Override
    public CmcSyslogConfig getCmcSyslogConfig(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, CmcSyslogConfig.class);
    }

    @Override
    public void updateCmcRcdEvtLvl(SnmpParam snmpParam, CmcSyslogConfig cmcSyslogConfig) {
        snmpExecutorService.setData(snmpParam, cmcSyslogConfig);
    }

    @Override
    public void updateCmcConfigParams(SnmpParam snmpParam, CmcSyslogConfig cmcSyslogConfig) {
        snmpExecutorService.setData(snmpParam, cmcSyslogConfig);
    }

    @Override
    public void deleteCmcSyslogServer(SnmpParam snmpParam, CmcSyslogServerEntry cmcSyslogServerEntry) {
        cmcSyslogServerEntry.setTopCcmtsSyslogServerStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, cmcSyslogServerEntry);
    }

    @Override
    public void update8800ASwitchEnable(SnmpParam snmpParam, CmcSyslogSwitchEntry cmcSyslogSwitchEntry) {
        snmpExecutorService.setData(snmpParam, cmcSyslogSwitchEntry);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.facade.CmcFacade#update8800BSystemIpInfo(com.topvision
     * .framework.snmp .SnmpParam, com.topvision.ems.cmc.facade.domain.CmcSystemBasicInfo)
     */
    @Override
    public void set8800BSystemIpInfo(SnmpParam snmpParam, CmcSystemIpInfo basicInfo) {
        snmpExecutorService.setData(snmpParam, basicInfo);
    }

    @Override
    public void clearConfig(SnmpParam snmpParam) {
        snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.1.6.1.0", "2");
    }

    @Override
    public void updateCmcSystemTime(SnmpParam snmpParam, CmcSystemTimeConfig cmcSystemTimeConfig) {
        snmpExecutorService.setData(snmpParam, cmcSystemTimeConfig);
    }

    @Override
    public CmcSystemTimeConfig getCmcSystemTime(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, CmcSystemTimeConfig.class);
    }

    @Override
    public void updateCmcSysConfig(SnmpParam snmpParam, CmcSysConfig cmcSysConfig) {
        snmpExecutorService.setData(snmpParam, cmcSysConfig);
    }

    @Override
    public CmcShareSecretConfig updateCmcShareSecret(SnmpParam snmpParam, CmcShareSecretConfig cmcShareSecretConfig) {
        snmpExecutorService.setData(snmpParam, cmcShareSecretConfig);
        CmcShareSecretConfig data = new CmcShareSecretConfig();
        data.setIfIndex(cmcShareSecretConfig.getIfIndex());
        return snmpExecutorService.getTableLine(snmpParam, data);
    }

    @Override
    public CmcUpChannelBaseInfo getCmcUpChannelBaseInfo(SnmpParam snmpParam, Integer ifIndex) {
        CmcUpChannelBaseInfo cmcUpChannel = new CmcUpChannelBaseInfo();
        cmcUpChannel.setChannelIndex(ifIndex.longValue());
        return snmpExecutorService.getTableLine(snmpParam, cmcUpChannel);
    }

    @Override
    public CmcUpChannelRanging getCmcUpChannelRanging(SnmpParam snmpParam, Integer ifIndex) {
        CmcUpChannelRanging cmcUpChannelRanging = new CmcUpChannelRanging();
        cmcUpChannelRanging.setChannelIndex(ifIndex.longValue());
        return snmpExecutorService.getTableLine(snmpParam, cmcUpChannelRanging);
    }

    @Override
    public CmcUpChannelSignalQualityInfo getCmcUpChannelSignalqualityInfo(SnmpParam snmpParam, Integer ifIndex) {
        CmcUpChannelSignalQualityInfo data = new CmcUpChannelSignalQualityInfo();
        data.setChannelIndex(ifIndex.longValue());
        return snmpExecutorService.getTableLine(snmpParam, data);
    }

    @Override
    public Integer getCmcUpChannelRxPower(SnmpParam snmpParam, Integer ifIndex) {
        try {
            String signalPower = snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.4491.2.1.20.1.25.1.2." + ifIndex);
            return Integer.parseInt(signalPower);
        } catch (Exception e) {
            logger.debug("", e);
        }
        return 0;
    }

    @Override
    public Integer getBsrUpChannelRxPower(SnmpParam snmpParam, Integer ifIndex) {
        try {
            String signalPower = snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.4981.2.1.2.1.1." + ifIndex);
            return Integer.parseInt(signalPower);
        } catch (Exception e) {
            logger.debug("", e);
        }
        return 0;
    }

    @Override
    public CmcDownChannelBaseInfo getCmcDownChannelBaseInfo(SnmpParam snmpParam, Integer ifIndex) {
        CmcDownChannelBaseInfo data = new CmcDownChannelBaseInfo();
        data.setChannelIndex(ifIndex.longValue());
        return snmpExecutorService.getTableLine(snmpParam, data);
    }

    @Override
    public List<CmcPhyConfig> getCmcPhyConfig(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcPhyConfig.class);
    }

    @Override
    public CmcVlanDhcpAllocEntry getCmcVlanPriIpDhcpCfg(SnmpParam snmpParam, Integer topCcmtsVlanIndex) {
        CmcVlanDhcpAllocEntry vlanDhcp = new CmcVlanDhcpAllocEntry();
        vlanDhcp.setTopCcmtsVlanIndex(topCcmtsVlanIndex);
        return snmpExecutorService.getTableLine(snmpParam, vlanDhcp);
    }

    @Override
    public CmcVlanData refreshCmcVlanConfig(SnmpParam snmpParam, Long cmcId) {
        CmcVlanData result = new CmcVlanData();
        // 获取CCMTS VLAN配置
        List<CmcVlanConfigEntry> cmcVlanConfigEntries = snmpExecutorService.getTable(snmpParam,
                CmcVlanConfigEntry.class);
        for (CmcVlanConfigEntry cmcVlanConfigEntry : cmcVlanConfigEntries) {
            cmcVlanConfigEntry.setCmcId(cmcId);
        }
        result.setCmcVlanConfigEntries(cmcVlanConfigEntries);
        CmcVlanPrimaryInterface cmcVlanPrimaryIfObject = snmpExecutorService.getData(snmpParam,
                CmcVlanPrimaryInterface.class);
        cmcVlanPrimaryIfObject.setCmcId(cmcId);
        result.setCmcVlanPrimaryInterface(cmcVlanPrimaryIfObject);
        // 获取CCMTS VLAN 主IP配置
        List<CmcVlanPrimaryIp> cmcVlanPriIpEntries = snmpExecutorService.getTable(snmpParam, CmcVlanPrimaryIp.class);
        for (CmcVlanPrimaryIp cmcVlanPrimaryIp : cmcVlanPriIpEntries) {
            cmcVlanPrimaryIp.setCmcId(cmcId);
        }
        result.setCmcVlanPrimaryIps(cmcVlanPriIpEntries);
        // 获取CCMTS VLAN 子IP配置
        List<CmcVifSubIpEntry> cmcVifSubIpEntries = snmpExecutorService.getTable(snmpParam, CmcVifSubIpEntry.class);
        for (CmcVifSubIpEntry cmcVifSubIpEntry : cmcVifSubIpEntries) {
            cmcVifSubIpEntry.setCmcId(cmcId);
        }
        result.setCmcVifSubIpEntries(cmcVifSubIpEntries);
        // 获取CCMTS VLAN Dhcp配置
        List<CmcVlanDhcpAllocEntry> cmcVlanDhcpAllocEntries = snmpExecutorService.getTable(snmpParam,
                CmcVlanDhcpAllocEntry.class);
        for (CmcVlanDhcpAllocEntry cmcVlanDhcpAllocEntry : cmcVlanDhcpAllocEntries) {
            cmcVlanDhcpAllocEntry.setCmcId(cmcId);
        }
        result.setCmcVlanDhcpAllocEntries(cmcVlanDhcpAllocEntries);
        return result;
    }

    @Override
    public void updateCmcSyslogServer(SnmpParam snmpParam, CmcSyslogServerEntry cmcSyslogServerEntry) {
        snmpExecutorService.setData(snmpParam, cmcSyslogServerEntry);
    }

    @Override
    public List<CmcSysConfig> getCmcSysConfigList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcSysConfig.class);
    }

    @Override
    public List<CmcShareSecretConfig> getCmcShareSecretConfigList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcShareSecretConfig.class);
    }

    @Override
    public void modifyUpChannelForSpe(SnmpParam snmpParam, CmcUpChannelBaseInfo chl) {
        snmpExecutorService.setData(snmpParam, chl);
    }

    @Override
    public CmcRealtimeInfo getCmcRealTimeInfo(SnmpParam snmpParam, Long cmcIndex) {
        CmcRealtimeInfo cmcRealtimeInfo = new CmcRealtimeInfo();
        cmcRealtimeInfo.setCmcIndex(cmcIndex);
        CmcRealtimeInfo re = snmpExecutorService.getTableLine(snmpParam, cmcRealtimeInfo);
        if (re == null) {
            re = new CmcRealtimeInfo();
        }
        return re;
    }

    @Override
    public CmcOpticalInfo getCmcOpticalInfo(SnmpParam snmpParam) {
        List<CmcOpticalInfo> cmcOpticalInfos = snmpExecutorService.getTable(snmpParam, CmcOpticalInfo.class);
        if (cmcOpticalInfos != null && cmcOpticalInfos.size() > 0) {
            return cmcOpticalInfos.get(0);
        } else {
            return new CmcOpticalInfo();
        }
    }

    @Override
    public CmcAttribute getCmcAttributeForSnmpTest(SnmpParam snmpParam) {
        CmcAttribute cmcAttribute = null;
        List<CmcAttribute> list = snmpExecutorService.getTable(snmpParam, CmcAttribute.class);
        cmcAttribute = list.get(0);
        return cmcAttribute;
    }

    @Override
    public Long getCmtsUpChannelModType(SnmpParam snmpParam, Long modulationProfile) {
        try {
            return Long.parseLong(snmpExecutorService.getNext(snmpParam, ".1.3.6.1.2.1.10.127.1.3.5.1.4."
                    + modulationProfile + ".4"));
        } catch (NumberFormatException e) {
            logger.debug("", e);
        } catch (Exception e) {
            logger.debug("", e);
        }
        return null;
    }

    @Override
    public List<CmtsModulationEntry> getCmtsModulationEntryList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmtsModulationEntry.class);
    }

    @Override
    public CmcUpChannelRanging modifyCmcUpChannelRanging(SnmpParam snmpParam, CmcUpChannelRanging cmcUpChannelRanging) {
        CmcUpChannelRanging afterModified = new CmcUpChannelRanging();
        try {
            afterModified = snmpExecutorService.setData(snmpParam, cmcUpChannelRanging);
        } finally {
        }
        return afterModified;
    }

    @Override
    public IpqamData refreshIpqamDataA(SnmpParam snmpParam, Long entityId, Long cmcIndex) {
        IpqamData result = new IpqamData();
        Long nextCmcIndex = CmcIndexUtils.getNextCmcIndex(cmcIndex);
        List<CmcEqamStatus> cmcEqamStatusList = new ArrayList<CmcEqamStatus>();
        CmcEqamStatus cmcEqamStatusClass = new CmcEqamStatus();
        try {
            cmcEqamStatusList = snmpExecutorService.getTableRangeLine(snmpParam, cmcEqamStatusClass, cmcIndex,
                    nextCmcIndex);
        } catch (Exception e) {
            logger.error("refresh cmcEqamStatus error", e);
        }
        for (CmcEqamStatus cmcEqamStatus : cmcEqamStatusList) {
            cmcEqamStatus.setEntityId(entityId);
        }
        result.setCmcEqamStatusList(cmcEqamStatusList);

        CmcEqamProgram cmcEqamProgramClass = new CmcEqamProgram();
        List<CmcEqamProgram> cmcEqamProgramList = new ArrayList<CmcEqamProgram>();
        try {
            cmcEqamProgramList = snmpExecutorService.getTableRangeLine(snmpParam, cmcEqamProgramClass, cmcIndex,
                    nextCmcIndex);
        } catch (Exception e) {
            logger.error("refresh cmcEqamProgram error", e);
        }
        for (CmcEqamProgram cmcEqamProgram : cmcEqamProgramList) {
            cmcEqamProgram.setEntityId(entityId);
        }
        result.setCmcEqamProgramList(cmcEqamProgramList);

        List<ProgramIn> programInList = new ArrayList<ProgramIn>();
        for (CmcEqamProgram cmcEqamProgram : cmcEqamProgramList) {
            try {
                ProgramIn programInClass = new ProgramIn();
                // 构造索引
                programInClass.setMpegInputUdpOriginationIndex(cmcEqamProgram.getMpegVideoSessionIndex());
                programInClass.setMpegInputUdpOriginationId(1L); // 固定为1
                programInList.add(snmpExecutorService.getTableLine(snmpParam, programInClass));

            } catch (Exception e) {
                logger.error("refresh programIn error", e);
            }
        }
        for (ProgramIn programIn : programInList) {
            programIn.setEntityId(entityId);
        }
        result.setProgramInList(programInList);

        List<ProgramOut> programOutList = new ArrayList<ProgramOut>();
        for (CmcEqamProgram cmcEqamProgram : cmcEqamProgramList) {
            try {

                ProgramOut programOutClass = new ProgramOut();
                // 构造索引
                programOutClass.setMpegOutputTSIndex(CmcIndexUtils.getChannelIndexFromEqamIndex(cmcEqamProgram
                        .getMpegVideoSessionIndex()));
                programOutClass.setMpegOutputProgIndex(CmcIndexUtils.getSessionIdFromSessionIndex(cmcEqamProgram
                        .getMpegVideoSessionIndex()));
                programOutList.add(snmpExecutorService.getTableLine(snmpParam, programOutClass));

            } catch (Exception e) {
                logger.error("refresh programOut error", e);
            }
        }
        for (ProgramOut programOut : programOutList) {
            programOut.setEntityId(entityId);
        }
        result.setProgramOutList(programOutList);
        return result;
    }

    @Override
    public List<CmcEqamStatus> refreshIpqamStatusA(SnmpParam snmpParam, Long entityId, Long cmcIndex) {
        Long nextCmcIndex = CmcIndexUtils.getNextCmcIndex(cmcIndex);
        CmcEqamStatus cmcEqamStatusClass = new CmcEqamStatus();
        List<CmcEqamStatus> cmcEqamStatusList = new ArrayList<CmcEqamStatus>();
        try {
            cmcEqamStatusList = snmpExecutorService.getTableRangeLine(snmpParam, cmcEqamStatusClass, cmcIndex,
                    nextCmcIndex);
        } catch (Exception e) {
            logger.error("refresh cmcEqamStatus error", e);
        }
        for (CmcEqamStatus cmcEqamStatus : cmcEqamStatusList) {
            cmcEqamStatus.setEntityId(entityId);
        }
        return cmcEqamStatusList;
    }

    @Override
    public IpqamData refreshIpqamProgramA(SnmpParam snmpParam, Long entityId, Long cmcIndex) {
        IpqamData result = new IpqamData();
        Long nextCmcIndex = CmcIndexUtils.getNextCmcIndex(cmcIndex);
        CmcEqamProgram cmcEqamProgramClass = new CmcEqamProgram();
        List<CmcEqamProgram> cmcEqamProgramList = new ArrayList<CmcEqamProgram>();
        try {
            cmcEqamProgramList = snmpExecutorService.getTableRangeLine(snmpParam, cmcEqamProgramClass, cmcIndex,
                    nextCmcIndex);
        } catch (Exception e) {
            logger.error("refresh cmcEqamProgram error", e);
        }
        for (CmcEqamProgram cmcEqamProgram : cmcEqamProgramList) {
            cmcEqamProgram.setEntityId(entityId);
        }
        result.setCmcEqamProgramList(cmcEqamProgramList);

        List<ProgramIn> programInList = new ArrayList<ProgramIn>();
        for (CmcEqamProgram cmcEqamProgram : cmcEqamProgramList) {
            try {
                ProgramIn programInClass = new ProgramIn();
                programInClass.setMpegInputUdpOriginationIndex(cmcEqamProgram.getMpegVideoSessionIndex());
                programInClass.setMpegInputUdpOriginationId(1L);
                programInList.add(snmpExecutorService.getTableLine(snmpParam, programInClass));

            } catch (Exception e) {
                logger.error("refresh programIn error", e);
            }
        }
        for (ProgramIn programIn : programInList) {
            programIn.setEntityId(entityId);
        }
        result.setProgramInList(programInList);

        List<ProgramOut> programOutList = new ArrayList<ProgramOut>();
        for (CmcEqamProgram cmcEqamProgram : cmcEqamProgramList) {
            try {

                ProgramOut programOutClass = new ProgramOut();
                programOutClass.setMpegOutputTSIndex(CmcIndexUtils.getChannelIndexFromEqamIndex(cmcEqamProgram
                        .getMpegVideoSessionIndex()));
                programOutClass.setMpegOutputProgIndex(CmcIndexUtils.getSessionIdFromSessionIndex(cmcEqamProgram
                        .getMpegVideoSessionIndex()));
                programOutList.add(snmpExecutorService.getTableLine(snmpParam, programOutClass));

            } catch (Exception e) {
                logger.error("refresh programOut error", e);
            }
        }
        for (ProgramOut programOut : programOutList) {
            programOut.setEntityId(entityId);
        }
        result.setProgramOutList(programOutList);
        return result;
    }

    @Override
    public IpqamData refreshIpqamDataB(SnmpParam snmpParam, Long entityId) {
        IpqamData result = new IpqamData();
        List<CmcEqamStatus> cmcEqamStatusList = snmpExecutorService.getTable(snmpParam, CmcEqamStatus.class);
        for (CmcEqamStatus cmcEqamStatus : cmcEqamStatusList) {
            cmcEqamStatus.setEntityId(entityId);
        }
        result.setCmcEqamStatusList(cmcEqamStatusList);

        List<CmcEqamProgram> cmcEqamProgramList = snmpExecutorService.getTable(snmpParam, CmcEqamProgram.class);
        for (CmcEqamProgram cmcEqamProgram : cmcEqamProgramList) {
            cmcEqamProgram.setEntityId(entityId);
        }
        result.setCmcEqamProgramList(cmcEqamProgramList);

        List<ProgramIn> programInList = snmpExecutorService.getTable(snmpParam, ProgramIn.class);
        for (ProgramIn programIn : programInList) {
            programIn.setEntityId(entityId);
        }
        result.setProgramInList(programInList);

        List<ProgramOut> programOutList = snmpExecutorService.getTable(snmpParam, ProgramOut.class);
        for (ProgramOut programOut : programOutList) {
            programOut.setEntityId(entityId);
        }
        result.setProgramOutList(programOutList);
        return result;
    }

    @Override
    public List<CmcEqamStatus> refreshIpqamStatusB(SnmpParam snmpParam, Long entityId) {
        List<CmcEqamStatus> cmcEqamStatusList = snmpExecutorService.getTable(snmpParam, CmcEqamStatus.class);
        for (CmcEqamStatus cmcEqamStatus : cmcEqamStatusList) {
            cmcEqamStatus.setEntityId(entityId);
        }
        return cmcEqamStatusList;
    }

    @Override
    public IpqamData refreshIpqamProgramB(SnmpParam snmpParam, Long entityId) {
        IpqamData result = new IpqamData();

        List<CmcEqamProgram> cmcEqamProgramList = snmpExecutorService.getTable(snmpParam, CmcEqamProgram.class);
        for (CmcEqamProgram cmcEqamProgram : cmcEqamProgramList) {
            cmcEqamProgram.setEntityId(entityId);
        }
        result.setCmcEqamProgramList(cmcEqamProgramList);

        List<ProgramIn> programInList = snmpExecutorService.getTable(snmpParam, ProgramIn.class);
        for (ProgramIn programIn : programInList) {
            programIn.setEntityId(entityId);
        }
        result.setProgramInList(programInList);

        List<ProgramOut> programOutList = snmpExecutorService.getTable(snmpParam, ProgramOut.class);
        for (ProgramOut programOut : programOutList) {
            programOut.setEntityId(entityId);
        }
        result.setProgramOutList(programOutList);
        return result;
    }

    @Override
    public CmcRateLimit getCmcRateLimit(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, CmcRateLimit.class);
    }

    @Override
    public CmcSniConfig getCmcSniConfig(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, CmcSniConfig.class);
    }

    @Override
    public Long getUpChannelSnr(SnmpParam snmpParam, Long statusIndex) {
        try {
            String upChannelSnr = snmpExecutorService.get(snmpParam, "1.3.6.1.2.1.10.127.1.3.3.1.13." + statusIndex);
            return Long.valueOf(upChannelSnr);
        } catch (Exception e) {
            logger.error("refresh upchannel SNR error,CMTS IP = " + snmpParam.getIpAddress() + " CM INDEX = "
                    + statusIndex, e);
        }
        return 0L;
    }

    @Override
    public List<DocsIf3CmtsCmUsStatus> getDocsIf3CmtsCmUsStatusByCmIndex(SnmpParam snmpParam, Long statusIndex) {
        DocsIf3CmtsCmUsStatus docsIf3CmtsCmUsStatus = new DocsIf3CmtsCmUsStatus();
        docsIf3CmtsCmUsStatus.setCmRegStatusId(statusIndex);
        return snmpExecutorService.getTableLines(snmpParam, docsIf3CmtsCmUsStatus, 1, Integer.MAX_VALUE);
    }

    @Override
    public void updateCmcRcdEvtLvlII(SnmpParam snmpParam, CmcSyslogRecordTypeII cmcSyslogRecordTypeII) {
        snmpExecutorService.setData(snmpParam, cmcSyslogRecordTypeII);
    }

    @Override
    public List<CmcSyslogRecordTypeII> getCmcSyslogRecordTypeII(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcSyslogRecordTypeII.class);
    }

    @Override
    public List<TxPowerLimit> getTxPowerLimit(SnmpParam sp) {
        return snmpExecutorService.getTable(sp, TxPowerLimit.class);
    }

    @Override
    public List<DocsIf3CmtsCmUsStatus> getDocsIf3CmtsCmUsStatus(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, DocsIf3CmtsCmUsStatus.class);
    }

    public CmSystemInfoExt getCmSystemInfoExtByCmIndex(SnmpParam snmpParam, Long statusIndex) {
        CmSystemInfoExt cmSystemInfoExt = new CmSystemInfoExt();
        cmSystemInfoExt.setCmIndex(statusIndex);
        cmSystemInfoExt = snmpExecutorService.getTableLine(snmpParam, cmSystemInfoExt);
        return cmSystemInfoExt;
    }

    @Override
    public List<CmPartialSvcState> refreshCmPartialSvcState(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmPartialSvcState.class);
    }

    @Override
    public void setRealTimeSnmpDataStatus(SnmpParam snmpParam, String state) {
        try {
            snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.18.2.1.0", state);
        } catch (Exception e) {
            logger.debug("set topUpChannelSignalQualityRealTimeSnmpDataStatus error", e);
        }
    }

    @Override
    public void modifyAclInstall(SnmpParam snmpParam, CmcAclInstall install) {
        snmpExecutorService.setData(snmpParam, install);
    }

}
