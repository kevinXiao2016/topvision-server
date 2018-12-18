/***********************************************************************
 * $Id: OltService.java,v1.0 2013-10-25 上午10:25:28 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.domain.CcmtsFftMonitorScalar;
import com.topvision.ems.epon.domain.DeviceListItem;
import com.topvision.ems.epon.domain.Olt;
import com.topvision.ems.epon.domain.PonUsedInfo;
import com.topvision.ems.epon.domain.Room;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltMacAddressLearnTable;
import com.topvision.ems.epon.olt.domain.OltPowerAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
import com.topvision.ems.epon.olt.domain.TopOnuGlobalCfgMgmt;
import com.topvision.ems.epon.olt.domain.TopSysFileDirEntry;
import com.topvision.ems.facade.domain.EntityAttribute;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.SubDeviceCount;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-上午10:25:28
 *
 */
public interface OltService extends Service {

    /**
     * 获取OLT属性
     * 
     * @param entityId
     *            设备ID
     * @return OltAttribute
     */
    OltAttribute getOltAttribute(Long entityId);

    /**
     * 获得OLT相应的快照信息
     * 
     * @param entityId
     *            设备ID
     * @return EntitySnap
     */
    EntitySnap getOltCurrentPerformance(Long entityId);

    /**
     * 获取OLT位置相关信息
     * 
     * @param location
     *            机房名
     * @return Room
     */
    Room getOltPosition(String location);

    /**
     * 获取OLT设备上的系统时间
     * 
     * @param entityId
     *            设备ID
     * @return Long
     */
    Long getOltSysTime(Long entityId);

    /**
     * 系统校时
     * 
     * @param entityId
     *            设备ID
     */
    void checkSysTiming(Long entityId);

    /**
     * OLT复位
     * 
     * @param entityId
     *            设备ID
     */
    void resetOlt(Long entityId);

    /**
     * OLT主备倒换
     * 
     * @param entityId
     *            设备ID
     */
    void switchoverOlt(Long entityId);

    /**
     * 备用主控板同步
     * 
     * @param entityId
     *            设备ID
     * @param syncAction
     *            同步类型（syncApp/syncConfig）
     */
    void syncSlaveBoard(Long entityId, Integer syncAction);

    /**
     * OLT恢复出厂设置
     * 
     * @param entityId
     *            设备ID
     */
    void restoreOlt(Long entityId);

    /**
     * 获取OLT结构数据
     * 
     * @param entityId
     *            设备ID
     * @return OLT
     */
    Olt getOltStructure(Long entityId);

    /**
     * 获取槽位属性
     * 
     * @param slotId
     *            槽位ID
     * @return OltSlotAttribute
     */
    OltSlotAttribute getSlotAttribute(Long slotId);

    /**
     * 获取电源属性
     * 
     * @param powerCardId
     *            电源板卡id
     * @return OltPowerAttribute
     */
    OltPowerAttribute getPowerAttribute(Long powerCardId);

    /**
     * 获取给定条件的EPON设备资源.
     * 
     * @param map
     * @return
     */
    List<DeviceListItem> getDeviceListItem(Map<String, Object> map);

    /**
     * 获得OLT设备列表
     * 
     * @param paramsMap
     * @return
     */
    List<OltAttribute> getOltList(Map<String, Object> paramsMap);

    /**
     * 获取查询OLT列表总行数
     * 
     * @param paramsMap
     * @return
     */
    int getOltListCount(Map<String, Object> paramsMap);

    /**
     * 获取设备dol状态
     * 
     * @param entityId
     * @return
     */
    String getEntityDolStatus(Long entityId);

    /**
     * 更新EntityAttribute(是否有外置Dol)
     * 
     * @param entityAttribute
     */
    void updateEntityDolStatus(EntityAttribute entityAttribute);

    /**
     * 从DB获取设备的Mac地址学习表
     * 
     * @param entityId
     * @return
     */
    List<OltMacAddressLearnTable> getOltMacLearnTableList(Long entityId);

    /**
     * 从设备获取Mac地址学习表
     * 
     * @param entityId
     */
    void refreshOltMacLearnTable(Long entityId);

    /**
     * Add by Rod
     * 
     * 更新OLT设备的在线时长
     * 
     * @param entityId
     * @param sysUpTime
     */
    void addOltDeviceUpTime(Long entityId, Long sysUpTime);

    /**
     * Add by lzt
     * 
     * 获得OLT设备软件版本
     * 
     * @param entityId
     */
    String getOltSoftVersion(Long entityId);

    /**
     * 更新DB中OLT属性
     * 
     * @param oltAttribute
     *            OLT属性
     */
    void updateOltAttribute(OltAttribute oltAttribute);

    /**
     * 获取CCMTS频谱全局开关配置
     * 
     * @param entityId
     * @return
     */
    CcmtsFftMonitorScalar getCcmtsFftGbStatus(Long entityId);

    /**
     * 修改CCMTS频谱全局开关
     * 
     * @param entityId
     * @param fftMonitorGlbStatus
     */
    void modifyCcmtsFftGbStatus(Long entityId, Integer fftMonitorGlbStatus);

    /**
     * 刷新设备升级文件路径
     * 
     * @param entityId
     */
    void refreshFileDir(Long entityId);

    /**
     * Olt版本文件路径
     * 
     * @param entityId
     * @param fileType
     */
    TopSysFileDirEntry getOltFileDirEntry(Long entityId, Integer fileType);

    /**
     * Olt版本比较
     * 
     * @param entityId
     */
    Integer oltVersionCompare(Long entityId);

    /**
     * 获取Olt下级设备统计信息
     * 
     * @return
     */
    SubDeviceCount getSubCountInfo(Long entityId);

    /**
     * 从设备获取板卡物理槽位与逻辑槽位的映射关系
     * 
     * @param entityId
     */
    void refreshSlotMapInfo(Long entityId);

    /**
     * 从telnet获取设备的Mac地址信息
     * 
     * @param entityId
     * @return
     */
    String getMacInfoStr(Long entityId, String matchMacAddr) throws IOException;

    /**
     * 获取Olt PON口统计信息
     * 
     * @return
     */
    PonUsedInfo getPonUsedInfo(Long entityId);

    /**
     * 刷新MTK WLAN WAN CATV全局开关
     * 
     * @param entityId
     */
    void refreshTopOnuGlobalCfgMgmt(Long entityId);

    /**
     * 修改MTK WLAN WAN CATV全局开关
     * 
     * @param topOnuGlobalCfgMgmts
     * @param entityId
     */
    void modifyTopOnuGlobalCfgMgmt(List<TopOnuGlobalCfgMgmt> topOnuGlobalCfgMgmts, Long entityId);

    /**
     * 获取MTK WLAN WAN CATV全局开关
     * 
     * @param entityId
     * @return
     */
    List<TopOnuGlobalCfgMgmt> getTopOnuGlobalCfgMgmt(Long entityId);

    OltSlotAttribute getOltSlotAttribute(Long entityId, SnmpParam snmpParam, OltSlotAttribute domain,
            Long masterSlotId, Long slaveSlotId);

    OltSlotStatus getOltSlotStatus(Long entityId, SnmpParam snmpParam, OltSlotStatus domain, Long masterSlotId,
            Long slaveSlotId);

    /**
     * 刷新自动清除离线CM时间
     * 
     * @param
     * @return void
     */
    void refreshCmClearTime(Long entityId);
    
    /**
     * 获取有onu的olt
     * @return
     */
    List<Long> queryEntityIdOfOlt();

    /**
     * 默认开启OLT频谱
     * 
     * @param entityId
     */
    void defaultStartSpectrumSwitchOlt(Long entityId);
}
