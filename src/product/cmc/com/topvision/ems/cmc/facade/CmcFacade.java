/***********************************************************************
 * $Id: CmcFacade.java,v1.0 2011-7-1 下午02:43:48 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade;

import java.util.List;

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
import com.topvision.ems.cmc.ipqam.domain.CmcEqamStatus;
import com.topvision.ems.cmc.ipqam.domain.IpqamData;
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
import com.topvision.ems.facade.Facade;
import com.topvision.ems.facade.domain.DeviceBaseInfo;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-7-1-下午02:43:48
 * 
 */
@EngineFacade(serviceName = "CmcFacade", beanName = "cmcFacade")
public interface CmcFacade extends Facade {
    /**
     * 修改system信息(RFC-1213)
     * 
     * @param snmpParam
     * @param DeviceBaseInfo
     */
    void modifySystemBasicInfo(SnmpParam snmpParam, DeviceBaseInfo deviceBaseInfo);

    /**
     * 刷新单个CC上的（重新采集）服务流基本信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcQosServiceFlowAttribute>
     */
    List<CmcQosServiceFlowAttribute> refreshServiceFlowBaseInfoOnCC(SnmpParam snmpParam, Long cmcIndex);

    /**
     * 刷新（重新采集）服务流基本信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcQosServiceFlowAttribute>
     */
    List<CmcQosServiceFlowAttribute> refreshServiceFlowBaseInfo(SnmpParam snmpParam);

    /**
     * 刷新（重新采集）服务流状态信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcQosServiceFlowStatus>
     */
    List<CmcQosServiceFlowStatus> refreshServiceFlowStatusInfo(SnmpParam snmpParam);

    /**
     * 刷新（重新采集）服务流包分类器信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcQosPktClassInfo>
     */
    List<CmcQosPktClassInfo> refreshServiceFlowPktClassInfos(SnmpParam snmpParam);

    /**
     * 刷新（重新采集）服务流参数集信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcQosParamSetInfo>
     */
    List<CmcQosParamSetInfo> refreshServiceFlowParamSetInfos(SnmpParam snmpParam);

    /**
     * 刷新单台CC设备的（重新采集）服务流参数集信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcQosParamSetInfo>
     */
    List<CmcQosParamSetInfo> refreshServiceFlowParamSetOnCC(SnmpParam snmpParam, Long cmcIndex,
            List<Long> serviceFlowIdList);

    /**
     * 刷新（重新采集）服务流所属CM信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmMacToServiceFlow>
     */
    List<CmMacToServiceFlow> refreshCmMacToServiceFlows(SnmpParam snmpParam);

    /**
     * 修改mac域基本信息
     * 
     * @param snmpParam
     *            SnmpParam dol snmp参数
     * @param macDomainBaseInfo
     *            MacDomainBaseInfo
     * @return MacDomainBaseInfo
     */
    MacDomainBaseInfo modifyMacDomainBaseInfo(SnmpParam snmpParam, MacDomainBaseInfo macDomainBaseInfo);

    /**
     * 修改设备上下行信道基本信息
     * 
     * @param snmpParam
     *            SnmpParam 所连olt snmp参数
     * @param cmcDownChannelBaseShowInfo
     *            CmcDownChannelBaseShowInfo
     * @return 返回修改后的cmcDownChannelBaseShowInfo基本信息
     */

    CmcDownChannelBaseShowInfo modifyDownChannelBaseShowInfo(SnmpParam snmpParam,
            CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo);

    /**
     * 修改设备上下行信道基本信息
     * 
     * @param snmpParam
     *            SnmpParam 所连olt snmp参数
     * @param cmcUpChannelSignalQualityInfo
     *            cmcUpChannelSignalQualityInfo
     * @return 返回修改后的cmcDownChannelBaseShowInfo基本信息
     */

    CmcUpChannelSignalQualityInfo modifyUpChannelSignalQualityInfo(SnmpParam snmpParam,
            CmcUpChannelSignalQualityInfo cmcUpChannelSignalQualityInfo);

    /**
     * 修改CMC上行接收电平
     * 
     * @param snmpParam
     *            SnmpParam 所连olt snmp参数
     * @param rxPower
     * 
     */
    void modifyCmcUpChannelRxPower(SnmpParam snmpParam, Integer ifIndex, Integer rxPower);

    /**
     * 修改Bsr上行接收电平
     * 
     * @param snmpParam
     *            SnmpParam 所连olt snmp参数
     * @param rxPower
     * 
     */
    void modifyBsrUpChannelRxPower(SnmpParam snmpParam, Integer ifIndex, Integer rxPower);

    /**
     * 修改设备下行信道DOCSIS信息
     * 
     * @param snmpParam
     *            SnmpParam 所连olt snmp参数
     * @param cmcDownChannelBaseInfo
     *            CmcDownChannelBaseInfo
     * @return 返回修改后的cmcDownChannelBaseInfo基本信息
     */

    CmcDownChannelBaseInfo modifyDownChannelBaseInfo(SnmpParam snmpParam, CmcDownChannelBaseInfo cmcDownChannelBaseInfo);

    /**
     * 修改设备信道关联的IfTable信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcPort
     *            CmcPort
     * @return 返回修改后的cmcPort信息
     */
    CmcPort modifyCmcPortInfo(SnmpParam snmpParam, CmcPort cmcPort);

    /**
     * 修改设备上上行信道基本信息
     * 
     * @param snmpParam
     *            SnmpParam 所连olt snmp参数
     * @param cmcUpChannelBaseShowInfo
     *            CmcUpChannelBaseShowInfo
     * @return 返回修改后的cmcUpChannelBaseShowInfo基本信息
     */
    CmcUpChannelBaseShowInfo modifyUpChannelBaseShowInfo(SnmpParam snmpParam,
            CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo);

    /**
     * 修改上行信道DOCSIS基本信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcUpChannelBaseInfo
     *            CmcUpChannelBaseInfo
     * @return CmcUpChannelBaseInfo
     */
    CmcUpChannelBaseInfo modifyUpChannelBaseInfo(SnmpParam snmpParam, CmcUpChannelBaseInfo cmcUpChannelBaseInfo);

    /**
     * 重启CM
     * 
     * @param snmpParam
     *            SnmpParam 所连olt snmp参数
     * @param cmMac
     *            cmMac
     */
    void resetCm(SnmpParam snmpParam, String cmMac);

    /**
     * 获取CM基本属性
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmAttribute
     *            CmAttribute
     * @return CmAttribute
     */
    CmAttribute getCmAttribute(SnmpParam snmpParam, CmAttribute cmAttribute);

    /**
     * 创建或修改服务流模板
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcQosServiceClass
     *            CmcQosServiceClass
     * @return CmcQosServiceClass
     */
    public CmcQosServiceClass createOrModifyServiceClassInfo(SnmpParam snmpParam, CmcQosServiceClass cmcQosServiceClass);

    /**
     * 删除服务流模板
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcQosServiceClass
     *            CmcQosServiceClass
     * @return CmcQosServiceClass
     */
    public CmcQosServiceClass deleteServiceClassInfo(SnmpParam snmpParam, CmcQosServiceClass cmcQosServiceClass);

    /**
     * 重启CCMTS
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcSysControl
     *            CmcSysControl
     * @return CmcSysControl
     */
    public CmcSysControl cmcSysControlSetWithotAgent(SnmpParam snmpParam, CmcSysControl cmcSysControl);

    /**
     * 设置物理端口主备状态 main(1), backup(2), autoMainPreferred(3), autoBackupPreferred(4)
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcSniStatus
     *            Integer
     */
    void setCmcSniStatus(SnmpParam snmpParam, Integer cmcSniStatus);

    /**
     * 设置主物理口传输模式 copper(1), fiber(2), autoCopperPreferred(3), autoFiberPreferred(4)
     * 
     * @param snmpParam
     *            SnmpParam
     * @param sniMainTransmisionMode
     *            Integer
     */
    void setCmcSniMainTransmisionMode(SnmpParam snmpParam, Integer sniMainTransmisionMode);

    /**
     * 设置备用物理口传输模式 copper(1), fiber(2), autoCopperPreferred(3), autoFiberPreferred(4)
     * 
     * @param snmpParam
     *            SnmpParam
     * @param sniBackupTransmisionMode
     *            Integer
     */
    void setCmcSniBackupTransmisionMode(SnmpParam snmpParam, Integer sniBackupTransmisionMode);

    /**
     * 设置CMC EMS配置信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcEmsConfig
     *            CmcEmsConfig
     * @return CmcEmsConfig
     */
    CmcEmsConfig modifyCmcEmsConfig(SnmpParam snmpParam, CmcEmsConfig cmcEmsConfig);

    /**
     * 设置不同等级事件的动作
     * 
     * @param snmpParam
     *            SnmpParam
     * @param docsDevEvControl
     *            DocsDevEvControl
     * @return String
     */
    String modifyDocsDevEvControl(SnmpParam snmpParam, DocsDevEvControl docsDevEvControl);

    /**
     * 获取上行信道基本信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcIndex
     *            Long
     * @return List<CmcUpChannelBaseInfo>
     */
    List<CmcUpChannelBaseInfo> getUpChannelBaseInfo(SnmpParam snmpParam, Long cmcIndex);

    /**
     * 获取给定上行信道的通道类型
     * 
     * @param snmpParam
     *            SnmpParam
     * @param channelIndex
     *            Long
     * @return String
     */
    String getUpChannelType(SnmpParam snmpParam, Long channelIndex);

    /**
     * 获取8800b上行信道基本信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcUpChannelBaseInfo>
     */
    List<CmcUpChannelBaseInfo> getCC8800BUpChannelBaseInfo(SnmpParam snmpParam);

    /**
     * 获取上行通道信号质量
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcIndex
     *            Long
     * @return List<CmcUpChannelSignalQualityInfo>
     */
    List<CmcUpChannelSignalQualityInfo> getUpChannelSignalQualityInfo(SnmpParam snmpParam, Long cmcIndex);

    /**
     * 获取上行通道信号质量
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcIndex
     *            Long
     * @return List<CmcUpChannelSignalQualityInfo>
     */
    List<CmcUpChannelSignalQualityInfo> getBsr2000UpChannelSignalQualityInfo(SnmpParam snmpParam);

    /**
     * 获取8800B上行通道信号质量
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcUpChannelSignalQualityInfo>
     */
    List<CmcUpChannelSignalQualityInfo> getCC8800BUpChannelSignalQualityInfo(SnmpParam snmpParam);

    /**
     * 获取mac域状态息
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcIndex
     *            Long
     * @return MacDomainStatusInfo
     */
    MacDomainStatusInfo getMacDomainStatusInfo(SnmpParam snmpParam, Long cmcIndex);

    /**
     * 获取下行通道基本信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcIndex
     *            Long
     * @return List<CmcDownChannelBaseInfo>
     */
    List<CmcDownChannelBaseInfo> getDownChannelBaseInfo(SnmpParam snmpParam, Long cmcIndex);

    /**
     * 获取8800b下行信道基本信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcDownChannelBaseInfo>
     */
    List<CmcDownChannelBaseInfo> getCC8800BDownChannelBaseInfo(SnmpParam snmpParam);

    /**
     * 获取通道基本信息-ifTable
     * 
     * @param snmpParam
     * @param cmcIndex
     *            Long @return List<CmcPort>
     */
    List<CmcPort> getCmcPorts(SnmpParam snmpParam, Long cmcIndex);

    /**
     * 获取通道基本信息-ifTable
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcPort
     *            CmcPort
     * @return CmcPort
     */
    CmcPort getCmcPortByIfIndex(SnmpParam snmpParam, CmcPort cmcPort);

    /**
     * 获取8800B通道基本信息-ifTable
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcPort>
     */
    List<CmcPort> getCmc8800BPorts(SnmpParam snmpParam);

    /**
     * 获取通道基本信息-ifTable关联
     * 
     * @param cmcIndex
     *            Long
     * @return List<CmcPortPerf>
     */
    List<CmcPortPerf> getCmcPortPerfs(Long cmcIndex);

    /**
     * 获取8800B通道基本信息-ifTable关联
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcPortPerf>
     */
    List<CmcPortPerf> getCmc8800BPortPerfs(SnmpParam snmpParam);

    /**
     * 获取下行通道统计信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcIndex
     *            Long
     * @return List<CmcDownChannelStaticInfo>
     */
    List<CmcDownChannelStaticInfo> getCmcDownChannelStaticInfo(SnmpParam snmpParam, Long cmcIndex);

    /**
     * 获取8800B下行通道统计信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcDownChannelStaticInfo>
     */
    List<CmcDownChannelStaticInfo> getCC8800BCmcDownChannelStaticInfo(SnmpParam snmpParam);

    /**
     * 设置设备上信道利用率采集间隔
     * 
     * @param snmpParam
     *            SnmpParam
     * @param period
     *            Long
     */
    void setChannelUtilizationInteInterval(SnmpParam snmpParam, Long period);

    /**
     * 获取设备上信道利用率采集间隔
     * 
     * @param snmpParam
     *            SnmpParam
     * @return String
     */
    String getChannelUtilizationInteInterval(SnmpParam snmpParam);

    /**
     * 获取上行通道上CM数
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcIndex
     *            Long
     * @return List<CmcChannelCmNum>
     */
    List<ChannelCmNum> getUpChannelCmNums(SnmpParam snmpParam, Long cmcIndex);

    /**
     * 获取下行通道上CM数
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcIndex
     *            Long
     * @return List<CmcChannelCmNum>
     */
    List<ChannelCmNum> getDownChannelCmNums(SnmpParam snmpParam, Long cmcIndex);

    /**
     * 重命名cc
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcSystemBasicInfo
     *            CmcSystemBasicInfo
     */
    void renameCcname(SnmpParam snmpParam, CmcSystemBasicInfo cmcSystemBasicInfo);

    /**
     * 获取cc信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return CmcAttribute
     */
    CmcAttribute getCmcAttribute(SnmpParam snmpParam);

    /**
     * 获取cc VLAN全局信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return CmcVlanPrimaryInterface
     */
    CmcVlanPrimaryInterface getCmcVlanPrimaryInterface(SnmpParam snmpParam);

    /**
     * 获取cc 主VLAN IP信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcVlanPrimaryIp>
     */
    List<CmcVlanPrimaryIp> getCmcVlanPrimaryIpList(SnmpParam snmpParam);

    /**
     * 修改DHCP Bundle
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcDhcpBundle
     *            CmcDhcpBundle
     * @return CmcDhcpBundle
     */
    CmcDhcpBundle modifyDhcpBundleInfo(SnmpParam snmpParam, CmcDhcpBundle cmcDhcpBundle);

    /**
     * 修改DHCP 服务器
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcDhcpServerConfig
     *            CmcDhcpServerConfig
     * @return CmcDhcpServerConfig
     */
    CmcDhcpServerConfig modifyDhcpServerInfo(SnmpParam snmpParam, CmcDhcpServerConfig cmcDhcpServerConfig);

    /**
     * 修改DHCP 中继
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcDhcpGiAddr
     *            CmcDhcpGiAddr
     * @return CmcDhcpGiAddr
     */
    CmcDhcpGiAddr modifyDhcpGiAddrInfo(SnmpParam snmpParam, CmcDhcpGiAddr cmcDhcpGiAddr);

    /**
     * 修改DHCP Option60
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcDhcpOption60
     *            CmcDhcpOption60
     * @return CmcDhcpOption60
     */
    CmcDhcpOption60 modifyDhcpOption60Info(SnmpParam snmpParam, CmcDhcpOption60 cmcDhcpOption60);

    /**
     * 修改虚拟IP
     * 
     * @param snmpParam
     * @param virtualIp
     * @return
     */
    CmcDhcpIntIp modifyDhcpVirtralIp(SnmpParam snmpParam, CmcDhcpIntIp virtualIp);

    /**
     * 修改DHCP Relay携带的Vlan Tag
     * 
     * @param dolSnmpParam
     * @param cmcDhcpPacketVlan
     * @return
     */
    CmcDhcpPacketVlan modifyDhcpPacketVlan(SnmpParam dolSnmpParam, CmcDhcpPacketVlan cmcDhcpPacketVlan);

    /**
     * 获取Dhcp Bundle信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcDhcpBundle>
     */
    List<CmcDhcpBundle> getCmcDhcpBundleInfo(SnmpParam snmpParam);

    /**
     * 获取Dhcp Server信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcDhcpServerConfig>
     */
    List<CmcDhcpServerConfig> getCmcDhcpServerConfigInfo(SnmpParam snmpParam);

    /**
     * 获取Dhcp 中继信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcDhcpGiAddr>
     */
    List<CmcDhcpGiAddr> getCmcDhcpGiAddrInfo(SnmpParam snmpParam);

    /**
     * 获取Dhcp Option60信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmcDhcpOption60>
     */
    List<CmcDhcpOption60> getCmcDhcpOption60Info(SnmpParam snmpParam);

    /**
     * 获取DHCP virtual IP信息
     * 
     * @param snmpParam
     * @return
     */
    List<CmcDhcpIntIp> getCmcDhcpIntIp(SnmpParam snmpParam);

    /**
     * 获取Packet VLAN信息
     * 
     * @param snmpParam
     * @return
     */
    List<CmcDhcpPacketVlan> getCmcDhcpPacketVlan(SnmpParam snmpParam);

    /**
     * 更新设备system信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcAttribute
     *            CmcAttribute
     * @return CmcAttribute
     */
    CmcAttribute modifyCmcAttribute(SnmpParam snmpParam, CmcAttribute cmcAttribute);

    /**
     * 更新设备默认网关
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcVlanPrimaryInterface
     *            CmcVlanPrimaryInterface
     * @return CmcVlanPrimaryInterface
     */
    CmcVlanPrimaryInterface modifyCmcDefaultRoute(SnmpParam snmpParam, CmcVlanPrimaryInterface cmcVlanPrimaryInterface);

    /**
     * 更新设备SNMP IP信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcVlanPrimaryIp
     *            CmcVlanPrimaryIp
     * @return CmcVlanPrimaryIp
     */
    CmcVlanPrimaryIp modifyCmcVlanPrimaryIp(SnmpParam snmpParam, CmcVlanPrimaryIp cmcVlanPrimaryIp);

    /**
     * 设置CC8800B 上联口链路配置
     * 
     * @param snmpParam
     *            SnmpParam
     * 
     * @param sni
     *            CcmtsSniObject
     * @return CcmtsSniObject
     */
    CcmtsSniObject updateCc8800bSniObject(SnmpParam snmpParam, CcmtsSniObject sni);

    /**
     * 升级CC
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcDevSoftware
     *            CmcDevSoftware
     * @return CmcDevSoftware
     */
    CmcDevSoftware updateCmc(SnmpParam snmpParam, CmcDevSoftware cmcDevSoftware);

    /**
     * 修改DHCP Relay基本信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcDhcpBaseConfig
     *            CmcDhcpBaseConfig
     * @return CmcDhcpBaseConfig
     */
    CmcDhcpBaseConfig modifyCmcDhcpRelayBaseInfo(SnmpParam snmpParam, CmcDhcpBaseConfig cmcDhcpBaseConfig);

    /**
     * 获取DHCP Relay基本信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return CmcDhcpBaseConfig
     */
    CmcDhcpBaseConfig getCmcDhcpRelayBaseInfo(SnmpParam snmpParam);

    /**
     * 获取CC配置保存状态
     * 
     * @param snmpParam
     *            SnmpParam 网管SNMP参数
     * @return Integer
     */
    Integer getCmcSaveStatus(SnmpParam snmpParam);

    /**
     * CC保存配置
     * 
     * @param snmpParam
     *            SnmpParam 网管SNMP参数
     */
    void saveCmcConfig(SnmpParam snmpParam);

    /**
     * 获取设备（拆分型CC）的升级进度
     * 
     * @param snmpParam
     *            SnmpParam
     * @return Integer
     */
    Integer getCmcUpdateProgress(SnmpParam snmpParam);

    /**
     * 获取全局授权状态
     * 
     * @param snmpParam
     *            SnmpParam
     * @return Integer
     */
    Integer getCmcAuthStatus(SnmpParam snmpParam);

    /**
     * 获取授权信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @param authMgmt
     *            CcmtsAuthManagement
     * @return CcmtsAuthManagement
     */
    CcmtsAuthManagement getCmcAuthInfo(SnmpParam snmpParam, CcmtsAuthManagement authMgmt);

    /**
     * 授权设置
     * 
     * @param snmpParam
     *            SnmpParam
     * @param authMgmt
     *            CcmtsAuthManagement
     * @return CcmtsAuthManagement
     */
    CcmtsAuthManagement setCmcAuthInfo(SnmpParam snmpParam, CcmtsAuthManagement authMgmt);

    /**
     * 设置cc基本信息
     * 
     * @param snmpParam
     * @param cmcSystemBasicInfo
     */
    void modifyCcmtsBasicInfo(SnmpParam snmpParam, CmcSystemBasicInfo cmcSystemBasicInfo);

    /**
     * 更改信道的管理状态 added by huangdongsheng
     * 
     * @param snmpParam
     * @param ifIndex
     * @param status
     * @return
     */
    Integer changeChannelAdminstatus(SnmpParam snmpParam, Long ifIndex, Integer status);

    /**
     * 获取一台8800B设备所有ACL 列表
     * 
     * @param snmpParam
     * @return
     */
    public List<CmcAclInfo> getAclAllList(SnmpParam snmpParam);

    /**
     * 获取一台CC设备的一个放置点的默认动作，目前8800B支持
     * 
     * @param snmpParam
     * @return
     */
    public CmcAclDefAction getAclPositionDefaultAct(SnmpParam snmpParam, CmcAclDefAction defAct);

    /**
     * 采集所有放置点的默认动作
     * 
     * @param snmpParam
     * @return
     */
    public List<CmcAclDefAction> getAllAclPositionDefAct(SnmpParam snmpParam);

    /**
     * 设置CC设备ACL一个放置点的默认动作
     * 
     * @param snmpParam
     * @param defAct
     * @return
     */
    public boolean modifyAclPositionDefAct(SnmpParam snmpParam, CmcAclDefAction defAct);

    /**
     * 设置一条ACL规则
     * 
     * @param snmpParam
     * @param aclInfo
     * @return
     */
    public CmcAclInfo modifyAclInfo(SnmpParam snmpParam, CmcAclInfo aclInfo);

    /**
     * 从设置采集一条ACL规则
     * 
     * @param snmpParam
     * @param aclInfo
     * @return
     */
    public CmcAclInfo getAclInfo(SnmpParam snmpParam, CmcAclInfo aclInfo);

    /**
     * 修改CPU端口限速
     * 
     * @param snmpParam
     * @param cmcRateLimit
     */
    void modifyCmcCpuPortRateLimit(SnmpParam snmpParam, CmcRateLimit cmcRateLimit);

    /**
     * 修改上联口限速
     * 
     * @param snmpParam
     * @param cmcRateLimit
     */
    void modifyCmcSniRateLimit(SnmpParam snmpParam, CmcRateLimit cmcRateLimit);

    /**
     * 修改上联口PHY信息
     * 
     * @param snmpParam
     * @param cmcPhyConfigList
     */
    void modifyCmcSniPhyConfig(SnmpParam snmpParam, List<CmcPhyConfig> cmcPhyConfigList);

    /**
     * 设置上联口环回使能
     * 
     * @param snmpParam
     * @param cmcSniConfig
     */
    void modifySniLoopbackStatus(SnmpParam snmpParam, CmcSniConfig cmcSniConfig);

    /**
     * 获得CMC VLAN全局配置
     * 
     * @param snmpParam
     * @return
     */
    public CmcIpSubVlanScalarObject getCmcIpSubVlanScalar(SnmpParam snmpParam);

    /**
     * 更新CMC VLAN全局配置
     * 
     * @param snmpParam
     * @param cmcIpSubVlanScalarObject
     * @return
     */
    public CmcIpSubVlanScalarObject setCmcIpSubVlanScalar(SnmpParam snmpParam, CmcIpSubVlanScalarObject obj);

    /**
     * 新增CMC 子网VLAN IP
     * 
     * @param snmpParam
     * @param vlanCfgEntry
     * @return
     */
    public CmcIpSubVlanCfgEntry createCmcIpSubVlanCfg(SnmpParam snmpParam, CmcIpSubVlanCfgEntry vlanCfgEntry);

    /**
     * 修改CMC 子网VLAN IP
     * 
     * @param snmpParam
     * @param vlanCfgEntry
     * @return
     */
    public CmcIpSubVlanCfgEntry setCmcIpSubVlanCfg(SnmpParam snmpParam, CmcIpSubVlanCfgEntry vlanCfgEntry);

    /**
     * 删除CMC 子网VLAN IP
     * 
     * @param snmpParam
     * @param vlanCfgEntry
     * @return
     */
    public CmcIpSubVlanCfgEntry destoryCmcIpSubVlanCfg(SnmpParam snmpParam, CmcIpSubVlanCfgEntry vlanCfgEntry);

    /**
     * 刷新CMC 子网VLAN IP
     * 
     * @param snmpParam
     * @param vlanCfgEntry
     * @return
     */
    public List<CmcIpSubVlanCfgEntry> getCmcIpSubVlanCfgList(SnmpParam snmpParam);

    /**
     * 新增CMC VLAN
     * 
     * @param snmpParam
     * @param cmcVlanConfigEntry
     * @return
     */
    public CmcVlanConfigEntry createCmcVlanCfg(SnmpParam snmpParam, CmcVlanConfigEntry cmcVlanConfigEntry);

    /**
     * 删除CMC VLAN
     * 
     * @param snmpParam
     * @param cmcVlanConfigEntry
     * @return
     */
    public CmcVlanConfigEntry destoryCmcVlanCfg(SnmpParam snmpParam, CmcVlanConfigEntry cmcVlanConfigEntry);

    /**
     * 新增CMC VLAN主IP
     * 
     * @param snmpParam
     * @param cmcVlanConfigEntry
     * @return
     */
    public CmcVlanPrimaryIp createCmcVlanPrimaryIp(SnmpParam snmpParam, CmcVlanPrimaryIp cmcVlanPrimaryIp);

    /**
     * 新增CMC VLAN从IP
     * 
     * @param snmpParam
     * @param cmcVifSubIpEntry
     * @return
     */
    public CmcVifSubIpEntry createCmcVifSubIpEntry(SnmpParam snmpParam, CmcVifSubIpEntry cmcVifSubIpEntry);

    /**
     * 修改CMC VLAN从IP
     * 
     * @param snmpParam
     * @param cmcVifSubIpEntry
     * @return
     */
    public CmcVifSubIpEntry setCmcVifSubIpEntry(SnmpParam snmpParam, CmcVifSubIpEntry cmcVifSubIpEntry);

    /**
     * 删除CMC VLAN 主 IP
     * 
     * @param snmpParam
     * @param cmcVlanPrimaryIp
     * @return
     */
    public CmcVlanPrimaryIp destoryCmcVlanPrimaryIp(SnmpParam snmpParam, CmcVlanPrimaryIp cmcVlanPrimaryIp);

    /**
     * 删除CMC VLAN从IP
     * 
     * @param snmpParam
     * @param cmcVifSubIpEntry
     * @return
     */
    public CmcVifSubIpEntry destoryCmcVifSubIpEntry(SnmpParam snmpParam, CmcVifSubIpEntry cmcVifSubIpEntry);

    /**
     * 更新CMC VLAN主IP DHCP信息
     * 
     * @param snmpParam
     * @param cmcDhcpAlloc
     * @return
     */
    public CmcVlanDhcpAllocEntry setCmcVlanPriIpDhcpCfg(SnmpParam snmpParam, CmcVlanDhcpAllocEntry cmcDhcpAlloc);

    /**
     * 获取CMC VLAN主IP DHCP信息
     * 
     * @param snmpParam
     * @param topCcmtsVlanIndex
     * @return
     */
    public CmcVlanDhcpAllocEntry getCmcVlanPriIpDhcpCfg(SnmpParam snmpParam, Integer topCcmtsVlanIndex);

    /**
     * 获取设备上的syslog server信息
     * 
     * @param snmpParam
     * @param cmcSyslogServerAttr
     * @return
     */
    List<CmcSyslogServerEntry> getCmcSyslogServerAttrs(SnmpParam snmpParam);

    /**
     * 为CCMTS添加一条syslog服务器
     * 
     * @param snmpParam
     * @param cmcSyslogServerEntry
     */
    void createCmcSyslogServer(SnmpParam snmpParam, CmcSyslogServerEntry cmcSyslogServerEntry);

    /**
     * 为CCMTS删除一条syslog服务器
     * 
     * @param snmpParam
     * @param cmcSyslogServerEntry
     */
    void deleteCmcSyslogServer(SnmpParam snmpParam, CmcSyslogServerEntry cmcSyslogServerEntry);

    /**
     * 更新Syslog服务器
     * 
     * @param snmpParam
     * @param cmcSyslogServerEntry
     */
    void updateCmcSyslogServer(SnmpParam snmpParam, CmcSyslogServerEntry cmcSyslogServerEntry);

    /**
     * 获取设备上的syslog config信息
     * 
     * @param snmpParam
     * @param cmcSyslogServerAttr
     * @return
     */
    CmcSyslogConfig getCmcSyslogConfig(SnmpParam snmpParam);

    /**
     * 修改CCMTS的一条记录方式的最低事件等级
     * 
     * @param snmpParam
     * @param cmcSyslogConfig
     */
    void updateCmcRcdEvtLvl(SnmpParam snmpParam, CmcSyslogConfig cmcSyslogConfig);

    /**
     * 修改CCMTS的配置项
     * 
     * @param snmpParam
     * @param cmcSyslogConfig
     */
    void updateCmcConfigParams(SnmpParam snmpParam, CmcSyslogConfig cmcSyslogConfig);

    /**
     * 修改8800A的设备上报使能值
     * 
     * @param snmpParam
     * @param cmcSyslogConfig
     */
    void update8800ASwitchEnable(SnmpParam snmpParam, CmcSyslogSwitchEntry cmcSyslogSwitchEntry);

    /**
     * 修改8800B的网络配置信息
     * 
     * @param snmpParam
     * @param basicInfo
     */
    void set8800BSystemIpInfo(SnmpParam snmpParam, CmcSystemIpInfo basicInfo);

    /**
     * 清除 CCMTS启动配置 （8800B）
     * 
     * @param snmpParam
     */
    void clearConfig(SnmpParam snmpParam);

    /**
     * 更新CCMTS系统时间
     * 
     * @param snmpParam
     * @param cmcSystemTimeConfig
     */
    void updateCmcSystemTime(SnmpParam snmpParam, CmcSystemTimeConfig cmcSystemTimeConfig);

    /**
     * 获取CCMTS系统时间
     * 
     * @param snmpParam
     * @return
     */
    CmcSystemTimeConfig getCmcSystemTime(SnmpParam snmpParam);

    /**
     * 更新CCMTS系统配置
     * 
     * @param snmpParam
     * @param cmcSysConfig
     */
    void updateCmcSysConfig(SnmpParam snmpParam, CmcSysConfig cmcSysConfig);

    /**
     * 更新CCMTS share secret配置
     * 
     * @param snmpParam
     * @param cmcShareSecretConfig
     * @return
     */
    CmcShareSecretConfig updateCmcShareSecret(SnmpParam snmpParam, CmcShareSecretConfig cmcShareSecretConfig);

    /**
     * 获取某个信道的当前配置
     * 
     * @param snmpParam
     * @param ifIndex
     * @return
     */
    CmcUpChannelBaseInfo getCmcUpChannelBaseInfo(SnmpParam snmpParam, Integer ifIndex);

    /**
     * 获取某个信道的信号质量
     * 
     * @param snmpParam
     * @param ifIndex
     * @return
     */
    CmcUpChannelSignalQualityInfo getCmcUpChannelSignalqualityInfo(SnmpParam snmpParam, Integer ifIndex);

    /**
     * 获取CMC上行信道的接收电平配置
     * 
     * @param snmpParam
     * @param ifIndex
     * @return
     */
    Integer getCmcUpChannelRxPower(SnmpParam snmpParam, Integer ifIndex);

    /**
     * 获取CMTS上行信道的接收电平配置
     * 
     * @param snmpParam
     * @param ifIndex
     * @return
     */
    Integer getBsrUpChannelRxPower(SnmpParam snmpParam, Integer ifIndex);

    /**
     * 获取下行信道的基本配置
     * 
     * @param snmpParam
     * @param ifIndex
     * @return
     */
    CmcDownChannelBaseInfo getCmcDownChannelBaseInfo(SnmpParam snmpParam, Integer ifIndex);

    /**
     * 获取Phy配置信息
     * 
     * @param snmpParam
     * @return
     */
    List<CmcPhyConfig> getCmcPhyConfig(SnmpParam snmpParam);

    /**
     * 获取VLAN配置
     * 
     * @param snmpParam
     * @param cmcId
     * @return
     */
    CmcVlanData refreshCmcVlanConfig(SnmpParam snmpParam, Long cmcId);

    /**
     * 获取CmcSysConfig
     * 
     * @param snmpParam
     * @return
     */
    List<CmcSysConfig> getCmcSysConfigList(SnmpParam snmpParam);

    /**
     * 
     * @param snmpParam
     * @return
     */
    List<CmcShareSecretConfig> getCmcShareSecretConfigList(SnmpParam snmpParam);

    /**
     * 修改上行信道频点和频宽（频谱页面）
     * 
     * @param snmpParam
     * @param chl
     */
    void modifyUpChannelForSpe(SnmpParam snmpParam, CmcUpChannelBaseInfo chl);

    /**
     * 获取CC实时信息
     * 
     * @param snmpParam
     */
    CmcRealtimeInfo getCmcRealTimeInfo(SnmpParam snmpParam, Long cmcIndex);

    /**
     * 获取CC实时信息
     * 
     * @param snmpParam
     */
    CmcOpticalInfo getCmcOpticalInfo(SnmpParam snmpParam);

    /**
     * 获取cc信息，不捕获异常，用于snmp测试
     * 
     * @param snmpParam
     */
    CmcAttribute getCmcAttributeForSnmpTest(SnmpParam snmpParam);

    /**
     * 获取Cmts上行信道调整模式
     * 
     * @param snmpParam
     * @return
     */
    Long getCmtsUpChannelModType(SnmpParam snmpParam, Long modulationProfile);

    List<CmtsModulationEntry> getCmtsModulationEntryList(SnmpParam snmpParam);

    /**
     * 获取CMC上行信道测距信息
     * 
     * @param snmpParam
     * @param ifIndex
     * @return
     */
    public CmcUpChannelRanging getCmcUpChannelRanging(SnmpParam snmpParam, Integer ifIndex);

    /**
     * 修改CMC上行信道测距信息
     * 
     * @param snmpParam
     * @param setting
     * @return
     */
    CmcUpChannelRanging modifyCmcUpChannelRanging(SnmpParam snmpParam, CmcUpChannelRanging setting);

    /**
     * 获取CMC限速信息
     * 
     * @param snmpParam
     * @return
     */
    CmcRateLimit getCmcRateLimit(SnmpParam snmpParam);

    /**
     * 获取SNI配置信息
     * 
     * @param snmpParam
     * @return
     */
    CmcSniConfig getCmcSniConfig(SnmpParam snmpParam);

    /**
     * 获取类A型IPQAM信息
     * 
     * @param snmpParam
     * @param entityId
     * @return
     */
    IpqamData refreshIpqamDataA(SnmpParam snmpParam, Long entityId, Long cmcIndex);

    /**
     * 获取类B型IPQAM信息
     * 
     * @param snmpParam
     * @param entityId
     * @return
     */
    IpqamData refreshIpqamDataB(SnmpParam snmpParam, Long entityId);

    /**
     * 刷新类A型EQAM信息
     * 
     * @param snmpParam
     * @param entityId
     * @param cmcIndex
     * @return
     */
    List<CmcEqamStatus> refreshIpqamStatusA(SnmpParam snmpParam, Long entityId, Long cmcIndex);

    /**
     * 刷新类B型EQAM信息
     * 
     * @param snmpParam
     * @param entityId
     * @param cmcIndex
     * @return
     */
    List<CmcEqamStatus> refreshIpqamStatusB(SnmpParam snmpParam, Long entityId);

    /**
     * 刷新类A型节目流信息
     * 
     * @param snmpParam
     * @param entityId
     * @param cmcIndex
     * @return
     */
    IpqamData refreshIpqamProgramA(SnmpParam snmpParam, Long entityId, Long cmcIndex);

    /**
     * 刷新类B型节目流信息
     * 
     * @param snmpParam
     * @param entityId
     * @return
     */
    IpqamData refreshIpqamProgramB(SnmpParam snmpParam, Long entityId);

    List<DocsIf3CmtsCmUsStatus> getDocsIf3CmtsCmUsStatus(SnmpParam snmpParam);

    /**
     * 获取上行信道SNR
     * 
     * @param statusIndex
     * @return
     */
    Long getUpChannelSnr(SnmpParam snmpParam, Long statusIndex);

    List<DocsIf3CmtsCmUsStatus> getDocsIf3CmtsCmUsStatusByCmIndex(SnmpParam snmpParam, Long statusIndex);

    void updateCmcRcdEvtLvlII(SnmpParam snmpParam, CmcSyslogRecordTypeII cmcSyslogRecordTypeII);

    List<CmcSyslogRecordTypeII> getCmcSyslogRecordTypeII(SnmpParam snmpParam);

    List<TxPowerLimit> getTxPowerLimit(SnmpParam sp);

    /**
     * 获取一个CM的SystemInfo扩展信息
     * 
     * @param cmcSnmpParam
     * @param statusIndex
     * @return
     */
    CmSystemInfoExt getCmSystemInfoExtByCmIndex(SnmpParam snmpParam, Long statusIndex);

    /**
     * 刷新partial service信道数据
     * 
     * @param snmpParam
     * @return
     */
    List<CmPartialSvcState> refreshCmPartialSvcState(SnmpParam snmpParam);

    /**
     * 实时刷新时, 刷新前改成实时模式 再去刷新 完成后再改成轮询模式
     * 
     * @param snmpParam
     * @param state
     */
    void setRealTimeSnmpDataStatus(SnmpParam snmpParam, String state);
    /**
     * 修改ACL绑定状态
     * 
     * @param snmpParam
     * @param install
     */
    void modifyAclInstall(SnmpParam snmpParam, CmcAclInstall install);

}
