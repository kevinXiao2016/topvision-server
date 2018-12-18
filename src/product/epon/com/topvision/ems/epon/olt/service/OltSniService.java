/***********************************************************************
 * $Id: OltService.java,v1.0 2013-10-25 上午10:25:28 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.domain.OltSniMacAddress;
import com.topvision.ems.epon.olt.domain.OltSniRedirect;
import com.topvision.ems.epon.olt.domain.OltSniStormSuppressionEntry;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-25-上午10:25:28
 *
 */
public interface OltSniService extends Service {
    /**
     * 获取SNI口属性
     * 
     * @param sniId
     *            sni口Id
     * @return OltSniAttribute
     */
    OltSniAttribute getSniAttribute(Long sniId);

    /**
     * 获取SNI口属性
     * 
     * @param entityId
     *            olt的Id
     * @param sniIndex
     *            sni口的数据库Index
     * @return OltSniAttribtue 属性
     */
    OltSniAttribute getSniAttribute(Long entityId, Long sniIndex);

    /**
     * 根据SNI INDEX列表获取对应的SNI属性
     * 
     * @param entityId
     * @param sniIndexList
     * @return
     */
    List<OltSniAttribute> getSniAttrList(Long entityId, List<Long> sniIndexList);

    /**
     * 获得所有SNI口信息
     * 
     * @param entityId
     * @return
     */
    List<OltSniAttribute> getAllSniList(Long entityId);

    /**
     * 设置SNI口名称
     * 
     * @param entityId
     *            设备ID
     * @param sniId
     *            sni口ID
     * @param name
     *            sni口名称
     */
    void setSniName(Long entityId, Long sniId, String name);

    /**
     * 设置SNI口使能
     * 
     * @param entityId
     *            设备ID
     * @param sniId
     *            sni口ID
     * @param adminStatus
     *            操作状态
     */
    void setSniAdminStatus(Long entityId, Long sniId, Integer adminStatus);

    /**
     * 设置SNI口隔离使能
     * 
     * @param entityId
     *            设备ID
     * @param sniId
     *            sni口ID
     * @param isolationStatus
     *            使能状态
     */
    void setSniIsolationStatus(Long entityId, Long sniId, Integer isolationStatus);

    /**
     * 设置SNI口流量控制
     * 
     * @param entityId
     *            设备ID
     * @param sniId
     *            sni口ID
     * @param ingressRate
     *            入方向流速
     * @param egressRate
     *            出方向流速
     */

    void setSniFlowControl(Long entityId, Long sniId, Integer ingressRate, Integer egressRate);

    /**
     * 设置SNI口流量控制
     * 
     * @param entityId
     *            设备ID
     * @param sniId
     *            sni口ID
     * @param ctrlFlowEnable
     *            端口流控使能
     */

    void setSniCtrlFlowEnable(Long entityId, Long sniId, Integer ctrlFlowEnable);

    /**
     * 设置SNI口自协商模式
     * 
     * @param entityId
     *            设备ID
     * @param sniId
     *            sni口ID
     * @param modeValue
     *            自协商模式
     * @return
     */

    int setSniAutoNegotiationMode(Long entityId, Long sniId, Integer modeValue);

    /**
     * 获取SNI口MAC地址管理数据
     * 
     * @param sniId
     *            SNI口ID
     * @return List<OltSniMacAddress>
     */

    List<OltSniMacAddress> getSniMacAddress(Long sniId);

    /**
     * 修改SNI口MAC老化时间
     * 
     * @param entityId
     *            设备ID
     * @param sniAddressAgingTime
     *            SNI口MAC老化时间
     * @param topSysArpAgingTime
     */

    void modifySniMacAddressAgingTime(Long entityId, Integer sniAddressAgingTime, Integer topSysArpAgingTime);

    /**
     * 修改SNI口MAC最大学习数
     * 
     * @param entityId
     *            设备ID
     * @param sniId
     *            SNI口ID
     * @param sniMacAddrMaxLearningNum
     *            SNI口MAC最大学习数
     */

    void modifySniMacAddressMaxLearningNum(Long entityId, Long sniId, Long sniMacAddrMaxLearningNum);

    /**
     * 获取SNI口广播风暴数据
     * 
     * @param sniId
     *            sni口ID
     * 
     * @return OltSniStormSuppressionEntry
     */
    OltSniStormSuppressionEntry getSniStormSuppressionBySniId(Long sniId);

    /**
     * 修改SNI口广播风暴设置
     * 
     * @param oltSniStormSuppressionEntry
     */
    void modifySniStoreSuppression(OltSniStormSuppressionEntry oltSniStormSuppressionEntry);

    /**
     * SNI口重定向
     * 
     * @param entityId
     *            设备ID
     * @param sniId
     *            sni口ID
     * @param direction
     *            目标
     * @param name
     *            名字
     * @param dstPort
     *            目标端口列表
     */
    @Deprecated
    void redirectSni(Long entityId, Long sniId, Integer direction, String name, Integer dstPort);

    /**
     * 获取SNI口重定向数据
     * 
     * @param entityId
     *            设备ID
     * @return List<OltSniRedirect>
     */
    List<OltSniRedirect> getSniRedirect(Long entityId);

    /**
     * 获取可以添加重定向的SNI口
     * 
     * @param entityId
     *            设备ID
     * @return List<OltSniRedirect>
     */
    List<OltSniRedirect> getAvailableSniRedirect(Long entityId);

    /**
     * 获取所有的重定向SNI口
     * 
     * @param entityId
     * @return
     */
    List<OltSniRedirect> getAllSniRedirect(Long entityId);

    /**
     * 添加SNI口重定向规则
     * 
     * @param oltSniRedirect
     *            SNI口重定向规则
     */
    void addSniRedirect(OltSniRedirect oltSniRedirect);

    /**
     * 删除SNI口重定向规则
     * 
     * @param entityId
     *            设备ID
     * @param sniId
     *            SNI口ID
     */
    void deleteSniRedirect(Long entityId, Long sniId);

    /**
     * 设置SNI口15分钟性能统计使能状态
     * 
     * @param entityId
     *            设备ID
     * @param sniId
     *            sni口ID
     * @param perfStatus
     *            SNI口15分钟性能统计使能状态
     */
    void setSni15MinPerfStatus(Long entityId, Long sniId, Integer perfStatus);

    /**
     * 批量设置SNI口15分钟性能统计使能状态
     * 
     * @param entityId
     * @param sniAttributes
     */
    void updateEntitySni15MinPerfStatus(Long entityId, List<OltSniAttribute> sniAttributes);

    /**
     * 设置SNI口24小时性能统计使能状态
     * 
     * @param entityId
     *            设备ID
     * @param sniId
     *            sni口ID
     * @param perfStatus
     *            SNI口24小时性能统计使能状态
     */
    void setSni24HourPerfStatus(Long entityId, Long sniId, Integer perfStatus);

    /**
     * 修改SNI口MAC地址管理 （包括新增与删除）
     * 
     * @param entityId
     * @param sniMacAddress
     */
    void modifySniMacAddress(Long entityId, OltSniMacAddress sniMacAddress);

    /**
     * 通过sniIndex 获得sniId
     * 
     * @param entityId
     *            设备ID
     * @param sniIndex
     */
    Long getSniIdByIndex(Long sniIndex, Long entityId);

    /**
     * 通过sniId获取sniIndex
     * 
     * @param sniId
     * @return
     */
    public Long getSniIndex(Long sniId);

    /**
     * 判断一个SNI口是否是XGUx上的光口
     * 
     * @param entityId
     * @param sniIndex
     * @return
     */
    Boolean portIsXGUxFiber(Long entityId, Long sniIndex);

    /**
     * 查询所有SNI端口信息
     * 
     * @return
     */
    List<OltSniAttribute> getSniPortList(Map<String, Object> map);

    /**
     * 更新所有SNI端口15分钟性能统计状态
     * 
     * @return
     */
    void updateSniPort15MinStatus(Long entityId);

    /**
     * 获取SNI口信息
     * 
     * @param entityId
     * @param sniId
     */
    void refreshSniBaseInfo(Long entityId, Long sniId);

    /**
     * 更新某些SNI端口的15分钟性能统计状态
     * 
     * @param entityId
     * @param targetIndex
     * @param status
     */
    void updateSni15PerfStatus(Long entityId, Long targetIndex, Integer status);

    /**
     * 获取SNI风暴抑制信息
     * 
     * @param entityId
     */
    void refreshOltSniStormSuppressionEntry(Long entityId);

    /**
     * 获取SNI端口展示名称
     * 
     * @param entityId
     * @param sniIndex
     * @return
     */
    String getSniDisplayName(Long entityId, Long sniIndex);

    /**
     * 更新SNI口信息
     * 
     * @param entityId
     * @param sniId
     */
    OltSniAttribute updateSniPortStatus(Long entityId, Long sniId);

    /**
     * 获取SNI流量限制信息
     * 
     * @param entityId
     * @param sniId
     */
    void refreshSniFlowControl(Long entityId, Long sniId);

}
