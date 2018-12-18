/***********************************************************************
 * $Id: OnuService.java,v1.0 2013-10-25 上午11:14:13 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OltUniPortRateLimit;
import com.topvision.ems.epon.onu.domain.OltUniStormSuppressionEntry;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-25-上午11:14:13
 *
 */
public interface UniService extends Service {

    /**
     * 获取UNI口广播风暴数据
     * 
     * @param uniId
     *            uni口ID
     * 
     * @return OltUniStormSuppressionEntry
     */
    OltUniStormSuppressionEntry getUniStormSuppressionByUniId(Long uniId);

    /**
     * UNI自协商状态使能
     * 
     * @param entityId
     *            设备ID
     * @param uniId
     *            uni口ID
     * @param uniAutoNegotiationStatus
     *            自协商状态
     */
    void setUniAutoNegotiationStatus(Long entityId, Long uniId, Integer uniAutoNegotiationStatus);

    /**
     * 修改UNI口广播风暴抑制信息
     * 
     * @param oltUniStormSuppressionEntry
     *            UNI广播风暴抑制属性
     */
    void modifyUniStormInfo(OltUniStormSuppressionEntry oltUniStormSuppressionEntry);

    /**
     * 设置UNI口使能
     * 
     * @param entityId
     *            设备ID
     * @param uniId
     *            UNI口ID
     * @param uniAdminStatus
     *            uni使能状态
     */
    void setUniAdminStatus(Long entityId, Long uniId, Integer uniAdminStatus);

    /**
     * 设置UNI端口隔离使能
     * 
     * @param entityId
     *            设备ID
     * @param uniId
     *            UNI口ID
     * @param uniIsolationEnable
     *            uni端口隔离使能状态
     */
    void setUniIsolationEnable(Long entityId, Long uniId, Integer uniIsolationEnable);

    /**
     * 设置UNI流控使能
     * 
     * @param entityId
     *            设备ID
     * @param uniId
     *            UNI口ID
     * @param uniFlowCtrlEnable
     *            uni流控使能状态
     */
    void setUniFlowCtrlEnable(Long entityId, Long uniId, Integer uniFlowCtrlEnable);

    /**
     * 设置UNI的15min性能统计使能
     * 
     * @param entityId
     *            设备ID
     * @param uniId
     *            UNI口ID
     * @param uni15minEnable
     *            UNI的15min性能统计使能状态
     */
    void setUni15minEnable(Long entityId, Long uniId, Integer uni15minEnable);

    /**
     * 批量设置UNI的15min性能统计使能
     * 
     * @param uniAttributes
     * 
     */
    void updateEntityUni15minEnable(List<OltUniAttribute> uniAttributes);

    /**
     * 设置UNI的24h性能统计使能
     * 
     * @param entityId
     *            设备ID
     * @param uniId
     *            UNI口ID
     * @param uni24hEnable
     *            UNI的24h性能统计使能状态
     */
    void setUni24hEnable(Long entityId, Long uniId, Integer uni24hEnable);

    /**
     * UNI端口限速信息
     * 
     * @param uniId
     *            UNI口ID
     * @return OltUniPortRateLimit
     */
    OltUniPortRateLimit getUniRateLimitInfo(Long uniId);

    /**
     * 修改UNI端口限速信息
     * 
     * @param oltUniPortRateLimit
     *            UNI端口限速属性
     */
    void modifyUniRateLimitInfo(OltUniPortRateLimit oltUniPortRateLimit);

    /**
     * 刷新UNI端口限速信息
     * 
     * @param entityId
     * @param uniId
     */
    void refreshUniRateLimit(Long entityId, Long uniId);

    /**
     * UNI端口自协商
     * 
     * @param uniId
     *            UNI口ID
     * @return OltUniExtAttribute
     */
    OltUniExtAttribute getOltUniExtAttribute(Long uniId);

    /**
     * 更新UNI端口自协商模式
     * 
     * @param uniId
     *            UNI口ID
     */
    void updateOltUniAutoNegotiationMode(Long entityId, Long uniId, Integer modeType);

    /**
     * 重新进行UNI端口自协商
     * 
     * @param entityId
     *            设备ID
     * @param uniId
     *            uni的ID
     */
    void restartUniAutoNego(Long entityId, Long uniId);

    /**
     * 设置UNI端口自协商使能
     * 
     * @param entityId
     *            设备ID
     * @param uniId
     *            UNI口ID
     * @param uniAutoNegoEnable
     *            UNI端口自协商使能状态
     */
    void setUniAutoNegoEnable(Long entityId, Long uniId, Integer uniAutoNegoEnable);

    /**
     * 获取UNI端口自协商状态
     * 
     * @param entityId
     *            设备ID
     * @param uniId
     *            UNI端口ID
     * @return Integer
     */
    Integer getUniAutoNegoStatus(Long entityId, Long uniId);

    /**
     * 修改UNI端口mac老化时间
     * 
     * @param oltUniExtAttribute
     * @return
     */
    void modifyUniMacAgeTime(OltUniExtAttribute oltUniExtAttribute);

    /**
     * 设置板卡状态
     * 
     * @param entityId
     * @param uniId
     */
    void onuMacClear(Long entityId, Long uniId);

    /**
     * 获得UNI MAC最大学习数
     * 
     * @param uniId
     * @return
     */
    Long getUniAttrMacAddrLearnMaxNum(Long uniId);

    /**
     * 修改uni mac地址最大学习数
     * 
     * @param uniId
     * @param topUniAttrMacAddrLearnMaxNum
     */
    void modifyUniMacAddrLearnMaxNum(Long entityId, Long uniId, Long topUniAttrMacAddrLearnMaxNum);

    /**
     * 刷新uni mac地址最大学习数
     * 
     * @param entityId
     * @param uniId
     * @return
     */
    OltUniExtAttribute refreshUniUSUtgPri(Long entityId, Long uniId);

    /**
     * 刷新UNI口的广播风暴抑制参数
     * 
     * @param entityId
     */
    void refreshUniStormOutPacketRate(Long entityId);

    /**
     * 更新所有UNI端口15分钟性能统计状态
     * 
     * @return
     */
    void updateUni15MinStatus(Long entityId);

    /**
     * 刷新OLT所有UNI口限速配置
     * 
     * @param Entity
     */
    void refreshOltUniRateLimit(Long entityId);

    /**
     * 刷新单个ONU下UNI口限速配置
     * 
     * @param entityId
     * @param onuIndex
     */
    void refreshOltUniRateLimit(Long entityId, Long onuIndex);

    /**
     * 刷新UNI口属性
     * 
     * @param entityId
     */
    void refreshOltUniExtAttribute(Long entityId);
}
