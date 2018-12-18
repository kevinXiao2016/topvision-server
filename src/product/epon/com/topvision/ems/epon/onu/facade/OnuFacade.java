/***********************************************************************
 * $Id: OnuFacade.java,v1.0 2013-10-25 上午11:22:35 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.facade;

import java.util.List;

import com.topvision.ems.epon.cpelocation.domain.OnuCpeLocation;
import com.topvision.ems.epon.onu.domain.CC8800ABaseInfo;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgBand;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgProfile;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgRecord;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuCatv;
import com.topvision.ems.epon.onu.domain.OltOnuRstp;
import com.topvision.ems.epon.onu.domain.OltOnuUpgrade;
import com.topvision.ems.epon.onu.domain.OltOnuUpgradeEx;
import com.topvision.ems.epon.onu.domain.OltOnuVoip;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltTopOnuProductTable;
import com.topvision.ems.epon.onu.domain.OnuBaseInfo;
import com.topvision.ems.epon.onu.domain.OnuCatvInfo;
import com.topvision.ems.epon.onu.domain.OnuDeregisterTable;
import com.topvision.ems.epon.onu.domain.OnuQualityInfo;
import com.topvision.ems.epon.onu.domain.OnuReplaceEntry;
import com.topvision.ems.epon.onu.domain.TopGponOnuSpeed;
import com.topvision.ems.epon.onu.domain.TopOnuSpeed;
import com.topvision.ems.epon.performance.domain.OnuLinkCollectInfo;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-上午11:22:35
 *
 */
@EngineFacade(serviceName = "OnuFacade", beanName = "onuFacade")
public interface OnuFacade extends Facade {

    /**
     * ONU使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     * @param status
     *            ONU使能状态
     * @return Integer
     */
    Integer setOnuAdminStatus(SnmpParam snmpParam, Long onuIndex, Integer status);

    /**
     * ONU使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     * @param onuName
     *            ONU别名
     * @return String
     */
    String modifyOnuName(SnmpParam snmpParam, Long onuIndex, String onuName);

    /**
     * ONU复位
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     */
    void resetOnu(SnmpParam snmpParam, Long onuIndex);

    /**
     * 获得ONU的基本信息
     * 
     * @param snmpParam
     * @return
     */
    List<OltOnuAttribute> getOnuAttributes(SnmpParam snmpParam);

    /**
     * 获得ONU的能力信息
     * 
     * @param snmpParam
     * @return
     */
    List<OltOnuCapability> getOnuCapabilities(SnmpParam snmpParam);

    /**
     * ONU解注册
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     */
    void deregisterOnu(SnmpParam snmpParam, Long onuIndex);

    /**
     * ONU使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     * @param onuVoipEnable
     *            ONU的VOIP服务使能状态
     * @return Integer
     */
    Integer setOnuVoipEnable(SnmpParam snmpParam, Long onuIndex, Integer onuVoipEnable);

    /**
     * ONU温度检测使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     * @param onuTemperatureDetectEnable
     *            ONU温度检测使能状态
     * @return Integer
     */
    Integer setOnuTemperatureDetectEnable(SnmpParam snmpParam, Long onuIndex, Integer onuTemperatureDetectEnable);

    /**
     * ONU的FEC使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     * @param onuFecEnable
     *            ONU的FEC使能状态
     * @return Integer
     */
    Integer setOnuFecEnable(SnmpParam snmpParam, Long onuIndex, Integer onuFecEnable);

    /**
     * ONU的端口隔离使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     * @param onuIsolationEnable
     *            ONU的端口隔离使能状态
     * @return Integer
     */
    Integer setOnuIsolationEnable(SnmpParam snmpParam, Long onuIndex, Integer onuIsolationEnable);

    /**
     * ONU的15min性能统计使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     * @param onu15minEnable
     *            ONU的15min性能统计使能状态
     * @return Integer
     */
    Integer setOnu15minEnable(SnmpParam snmpParam, Long onuIndex, Integer onu15minEnable);

    /**
     * ONU的24h性能统计使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     * @param onu24hEnable
     *            ONU的24h性能统计使能状态
     * @return Integer
     */
    Integer setOnu24hEnable(SnmpParam snmpParam, Long onuIndex, Integer onu24hEnable);

    /**
     * ONU的CATV使能
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     * @param onuCatvEnable
     *            ONU的CATV使能状态
     * @return Integer
     */
    Integer setOnuCatvEnable(SnmpParam snmpParam, Long onuIndex, Integer onuCatvEnable);

    /**
     * ONU的MAC地址学习最大数目
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     * @param onuMacMaxNum
     *            ONU的MAC地址学习最大数目
     * @return Integer
     */
    Integer setOnuMacMaxNum(SnmpParam snmpParam, Long onuIndex, Integer onuMacMaxNum);

    /**
     * ONU的RSTP桥模式
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param onuIndex
     *            ONU索引
     * @param onuRstpBridgeMode
     *            ONU的RSTP桥模式
     * @return Integer
     */
    Integer setOnuRstpBridgeMode(SnmpParam snmpParam, Long onuIndex, Integer onuRstpBridgeMode);

    /**
     * 获得catv能力
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @return
     */
    List<OltOnuCatv> getOltOnuCatv(SnmpParam snmpParam);

    /**
     * 获得rstp能力
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @return
     */
    List<OltOnuRstp> getOltOnuRstp(SnmpParam snmpParam);

    /**
     * 获得voip能力
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @return
     */
    List<OltOnuVoip> getOnuVoip(SnmpParam snmpParam);

    /**
     * 获取当前设备上存在的ONU升级记录
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @return List<OltOnuUpgrade>
     */
    List<OltOnuUpgradeEx> getOnuUpgradeRecords(SnmpParam snmpParam);

    /**
     * 获取ONU升级状态
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param transactionIndex
     *            Transaction索引
     * @return String
     */
    String getOnuUpgradeStatus(SnmpParam snmpParam, Integer transactionIndex);

    /**
     * ONU升级
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltOnuUpgrade
     *            ONU升级参数
     * @return OltOnuUpgrade
     */
    OltOnuUpgrade addOnuUpgrade(SnmpParam snmpParam, OltOnuUpgrade oltOnuUpgrade);

    /**
     * 删除ONU升级记录
     * 
     * @param snmpParam
     *            网管SNMP参数
     * @param oltOnuUpgrade
     *            ONU升级参数
     */
    void deleteOnuUpgrade(SnmpParam snmpParam, OltOnuUpgradeEx oltOnuUpgrade);

    /**
     * 获取ONU温度
     * 
     * @param snmpParam
     * @param onuIndex
     * @return
     */
    Integer getOnuTemperature(SnmpParam snmpParam, Long onuIndex);

    List<OltTopOnuProductTable> getOnuPreType(SnmpParam snmpParam);

    /**
     * 设置ONU预配置类型
     */
    void setOnuPreType(SnmpParam snmpParam, OltTopOnuProductTable oltOnuPreType);

    /**
     * 新增模板
     * 
     * @param snmpParam
     * @param profile
     */
    void addOnuAutoUpgProfile(SnmpParam snmpParam, OltOnuAutoUpgProfile profile);

    /**
     * 修改模板
     * 
     * @param snmpParam
     * @param profile
     */
    void modifyOnuAutoUpgProfile(SnmpParam snmpParam, OltOnuAutoUpgProfile profile);

    /**
     * 删除模板
     * 
     * @param snmpParam
     * @param profileId
     */
    void delOnuAutoUpgProfile(SnmpParam snmpParam, Integer profileId);

    // manu
    /**
     * 绑定模板
     * 
     * @param snmpParam
     * @param OltOnuAutoUpgBand
     */
    void bandOnuAutoUpgProfile(SnmpParam snmpParam, OltOnuAutoUpgBand band);

    /**
     * 解绑定模板
     * 
     * @param snmpParam
     * @param OltOnuAutoUpgBand
     */
    void unbandOnuAutoUpgProfile(SnmpParam snmpParam, OltOnuAutoUpgBand band);

    /**
     * 重启绑定的模板
     * 
     * @param snmpParam
     * @param OltOnuAutoUpgBand
     */
    void restartOnuAutoUpg(SnmpParam snmpParam, OltOnuAutoUpgBand band);

    /**
     * 取消升级
     * 
     * @param snmpParam
     * @param OltOnuAutoUpgRecord
     */
    void cancelOnuAutoUpg(SnmpParam snmpParam, OltOnuAutoUpgRecord record);

    // refresh
    /**
     * 获取模板数据
     * 
     * @param snmpParam
     */
    List<OltOnuAutoUpgProfile> getOnuAutoUpgProfile(SnmpParam snmpParam);

    /**
     * 获取模板绑定数据
     * 
     * @param snmpParam
     */
    List<OltOnuAutoUpgBand> getOnuAutoUpgBand(SnmpParam snmpParam);

    /**
     * 获取模板升级数据
     * 
     * @param snmpParam
     */
    List<OltOnuAutoUpgRecord> getOnuAutoUpgRecord(SnmpParam snmpParam);

    /**
     * 通过设备ONU PON口的属性
     * 
     * @param snmpParam
     *            snmpParam
     * @return OltTopOnuCapability
     */
    List<OltTopOnuCapability> getOnuListAttribute(SnmpParam snmpParam);

    /**
     * 根据ONU的Index获取ONU的基本信息
     * 
     * @param snmpParam
     * @param baseInfo
     * @return
     */
    OnuBaseInfo getOnuBaseInfo(SnmpParam snmpParam, OnuBaseInfo baseInfo);

    /**
     * 根据cc的Index获取cc的基本信息
     * 
     * @param snmpParam
     * @param caBaseInfo
     * @return
     */
    CC8800ABaseInfo getCC8800ABaseInfo(SnmpParam snmpParam, CC8800ABaseInfo caBaseInfo);

    /**
     * 
     * @param snmpParam
     * @param onuQualityInfo
     * @return
     */
    OnuQualityInfo refreshOnuQuality(SnmpParam snmpParam, OnuQualityInfo onuQualityInfo);

    /**
     * 
     * @param snmpParam
     * @param onuCatvInfo
     * @return
     */
    OnuCatvInfo refreshOnuCatv(SnmpParam snmpParam, OnuCatvInfo onuCatvInfo);

    /**
     * 获取单个ONU Catv
     * 
     * @param snmpParam
     * @param onuIndex
     * @return
     */
    OltOnuCatv getOltOnuCatv(SnmpParam snmpParam, Long onuIndex);

    /**
     * 获取单个ONU Voip
     * 
     * @param snmpParam
     * @param onuIndex
     * @return
     */
    OltOnuVoip getOnuVoip(SnmpParam snmpParam, Long onuIndex);

    /**
     * 获取单个ONU Rstp
     * 
     * @param snmpParam
     * @param onuIndex
     * @return
     */
    OltOnuRstp getOltOnuRstp(SnmpParam snmpParam, Long onuIndex);

    /**
     * Replace Onu
     * 
     * @param snmpParam
     * @param onuReplaceEntry
     */
    void replaceOnuEntry(SnmpParam snmpParam, OnuReplaceEntry onuReplaceEntry);

    /**
     * 获取ONU CPE定位信息
     * 
     * @param snmpParam
     * @param cpeMac
     * @return
     */
    OnuCpeLocation getOnuCpeLocation(SnmpParam snmpParam, String cpeMac);

    /**
     * 获取ONU光功率信息
     * 
     * @param snmpParam
     * @param onuLinkList
     * @return
     */
    List<OnuLinkCollectInfo> getOnuLinkInfoList(SnmpParam snmpParam, List<OnuLinkCollectInfo> onuLinkList);

    /**
     * 获取ONU CATV光功率信息
     * 
     * @param snmpParam
     * @param onuCatvInfo
     * @return
     */
    List<OnuCatvInfo> getOnuCatvInfoList(SnmpParam snmpParam, List<OnuCatvInfo> onuCatvInfo);

    /**
     * 更新ONU最近一次下线时间
     * 
     * @return
     */
    List<OnuDeregisterTable> getOnuDeregisterInfo(List<OnuDeregisterTable> tables);

    /**
     * 获取EPON ONU的下行速率
     * 
     * @param snmpParam
     * @param onuIndex
     * @return
     */
    TopOnuSpeed getEponOnuDownRate(SnmpParam snmpParam, Long onuIndex);

    /**
     * 获取GPON ONU的下行速率
     * 
     * @param snmpParam
     * @param onuIndex
     * @return
     */
    TopGponOnuSpeed getGponOnuDownRate(SnmpParam snmpParam, Long onuIndex);

    /**
     * 设置Epon onu开始测速
     * 
     * @param snmpParam
     * @param onuIndex
     */
    void startEponOnuDownRateTest(SnmpParam snmpParam, Long onuIndex);

    /**
     * 设置Gpon onu开始测速
     * 
     * @param snmpParam
     * @param onuIndex
     */
    void startGponOnuDownRateTest(SnmpParam snmpParam, Long onuIndex);

}
