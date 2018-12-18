/***********************************************************************
 * $Id: OltSniDao.java,v1.0 2013-10-25 上午10:14:44 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.domain.OltSniMacAddress;
import com.topvision.ems.epon.olt.domain.OltSniRedirect;
import com.topvision.ems.epon.olt.domain.OltSniStormSuppressionEntry;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-10-25-上午10:14:44
 *
 */
public interface OltSniDao extends BaseEntityDao<Object> {
    /**
     * 获取SNI口属性
     * 
     * @param sniId
     *            SNI口ID
     * @return
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
     * 更新SNI口名称
     * 
     * @param sniId
     *            SNI口ID
     * @param name
     *            SNI口名称
     */
    void updateSniPortName(Long sniId, String name);

    /**
     * 更新SNI口使能状态
     * 
     * @param sniId
     *            SNI口ID
     * @param adminStatus
     *            SNI口使能状态
     */
    void updateSniAdminStatus(Long sniId, Integer adminStatus);

    /**
     * 更新SNI口在位状态
     * 
     * @param sniId
     * @param operationStatus
     */
    void updateSniOperationStatus(Long sniId, Integer operationStatus);

    /**
     * 更新SNI端口隔离使能状态
     * 
     * @param sniId
     *            SNI口ID
     * @param isolationStatus
     *            SNI端口隔离使能状态
     */
    void updateSniIsolationStatus(Long sniId, Integer isolationStatus);

    /**
     * 更新SNI端口流控状态
     * 
     * @param oltSniAttribute
     *            SNI口
     */
    void updateSniFlowControl(OltSniAttribute oltSniAttribute);

    /**
     * 更新SNI端口隔离使能状态
     * 
     * @param sniId
     *            SNI口ID
     * @param sniAutoNegotiationMode
     *            SNI端口隔离使能状态
     */
    void updateSniAutoNegotiationMode(Long sniId, Integer sniAutoNegotiationMode);

    /**
     * 获取SNI口MAC地址管理数据
     * 
     * @param sniId
     *            SNI口ID
     * @return List<OltSniMacAddress>
     */
    List<OltSniMacAddress> getSniMacAddress(Long sniId);

    /**
     * 更新SNI口MAC老化时间
     * 
     * @param entityId
     *            设备ID
     * @param sniAddressAgingTime
     *            SNI口MAC老化时间
     * @param topSysArpAgingTime
     */
    void updateSniMacAddressAgingTime(Long entityId, Integer sniAddressAgingTime, Integer topSysArpAgingTime);

    /**
     * 增加Sni口的Mac地址管理记录
     * 
     * @param entityId
     * @param sniMacAddress
     */
    void insertSniMacAddress(Long entityId, OltSniMacAddress sniMacAddress);

    /**
     * 删除Sni口的Mac地址管理记录
     * 
     * @param entityId
     * @param sniMacAddress
     */
    void deleteSniMacAddress(Long entityId, OltSniMacAddress sniMacAddress);

    /**
     * 更新SNI口MAC最大学习数
     * 
     * @param sniId
     *            SNI口ID
     * @param sniMacAddrLeranMaxNum
     *            SNI口MAC最大学习数
     */
    void updateSniMacAddrLearnMaxNum(Long sniId, Long sniMacAddrLeranMaxNum);

    /**
     * 获取SNI口广播风暴数据
     * 
     * @param sniId
     *            sni口ID
     * 
     * @return OltSniStormSuppressionEntry
     */
    OltSniStormSuppressionEntry getSniStormSuppression(Long sniId);

    /**
     * 更新SNI口广播风暴参数
     * 
     * @param oltSniStormSuppressionEntry
     *            SNI口广播风暴参数
     */
    void updateSniStormSuppression(OltSniStormSuppressionEntry oltSniStormSuppressionEntry);

    /**
     * 插入SNI口广播风暴参数
     * 
     * @param oltSniStormSuppressionEntry
     *            SNI口广播风暴参数
     */
    void insertSniStormSuppression(OltSniStormSuppressionEntry oltSniStormSuppressionEntry);

    /**
     * 更新SNI端口15min性能统计使能状态
     * 
     * @param sniId
     *            SNI口ID
     * @param sniPerfStats15minuteEnable
     *            SNI端口15min性能统计使能状态
     */
    void updateSni15MinPerfStatus(Long sniId, Integer sniPerfStats15minuteEnable);

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
     * 获取所有的重定向的SNI口
     * 
     * @param entityId
     * @return
     */
    List<OltSniRedirect> queryAllSniRedirect(Long entityId);

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
     * @param sniId
     *            SNI口ID
     */
    void deleteSniRedirect(Long sniId);

    /**
     * 更新SNI端口24h性能统计使能状态
     * 
     * @param sniId
     *            SNI口ID
     * @param sniPerfStats24hourEnable
     *            SNI端口24h性能统计使能状态
     */
    void updateSni24HourPerfStatus(Long sniId, Integer sniPerfStats24hourEnable);

    /**
     * 获取SNI口索引
     * 
     * @param sniId
     *            SNI口ID
     * @return Long
     */
    Long getSniIndex(Long sniId);

    /**
     * 获得SNI口ID
     * 
     * @param sniIndex
     * @param entityId
     * @return
     */
    Long getSniIdByIndex(Long sniIndex, Long entityId);

    /**
     * 批量插入以及更新Sni口广播风暴抑制
     * 
     * @param sniStormSuppressionEntries
     */
    void batchInsertOltSniStormSuppressionEntry(final List<OltSniStormSuppressionEntry> sniStormSuppressionEntries,
            Long entityId);

    /**
     * 批量插入以及更新Sni口重定向
     * 
     * @param sniRedirects
     */
    void batchInsertOltSniRedirect(final List<OltSniRedirect> sniRedirects, Long entityId);

    /**
     * 批量插入以及更新Sni口地址管理
     * 
     * @param macAddresses
     */
    void batchInsertOltSniMacAddress(final List<OltSniMacAddress> macAddresses, Long entityId);

    /**
     * 更新某个sni口的属性
     * 
     * @param sniAttribute
     *            sniAttribute
     */
    void updateOltSniAttribute(OltSniAttribute sniAttribute);

    /**
     * 批量插入以及更新Sni属性
     * 
     * @param entityId
     * @param list
     *            SNI口属性列表
     * @param oltMap
     *            oltMap
     */
    void batchInsertSniAttribute(Long entityId, final List<OltSniAttribute> list, HashMap<Long, Long> oltMap);

    /**
     * 查询所有SNI端口信息
     * 
     * @return
     */
    List<OltSniAttribute> getSniPortList(Map<String, Object> map);

    /**
     * 通过sni口的属性过滤可以加入Trunk组的sni口列表
     * 
     * @param oltSniAttr
     *            sni口属性过滤条件
     * @return 可以加入的sni口列表
     */
    List<OltSniAttribute> availableSniListForTrunkGroupBySniAttribute(OltSniAttribute oltSniAttr);

    /**
     * 通过entityId过滤可以加入Trunk组的sni口列表
     * 
     * @param entityId
     *            olt的id
     * @return 可以加入的sni口列表
     */
    List<OltSniAttribute> availableSniListForTrunkGroupByEntityId(Long entityId);

    /**
     * 批量更新SNI端口15min性能统计使能状态
     * 
     * @param oltSniAttrs
     * 
     */
    void batchUpdateSni15MinStatus(List<OltSniAttribute> oltSniAttrs);

    /**
     * 获取SNI端口展示名称
     * 
     * @param entityId
     * @param sniIndex
     * @return
     */
    String querySniDisplayName(Long entityId, Long sniIndex);

    /**
     * 更新SNI入&出方向流量控制
     * 
     * @param sniId
     * @param topSniAttrIngressRate
     */
    void updateSniIngressRateAndEgressRate(Long sniId, Integer topSniAttrIngressRate, Integer topSniAttrEgressRate);

}
