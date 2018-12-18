/***********************************************************************
 * $Id: OnuDao.java,v1.0 2013-10-25 上午11:04:40 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao;

import java.util.HashMap;
import java.util.List;

import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OltUniPortRateLimit;
import com.topvision.ems.epon.onu.domain.OltUniStormSuppressionEntry;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-10-25-上午11:04:40
 *
 */
public interface UniDao extends BaseEntityDao<Object> {
    /**
     * 获得UNI口属性
     * 
     * @param uniId
     *            UNI ID
     * @return OltUniAttribute
     */
    OltUniAttribute getOnuUniAttribute(Long uniId);

    /**
     * getUniIndexAttributes For Topo
     * 
     * @param onuId
     * @return
     */
    List<OltUniAttribute> getUniIndexAttributes(Long onuId);

    /**
     * 更新UNI口使能状态
     * 
     * @param uniId
     *            UNI口ID
     * @param adminStatus
     *            UNI口使能状态
     */
    void updateUniAdminStatus(OltUniAttribute uniAttribute);

    void updateUniAdminStatus(Long uniId, Integer uniAdminStatus);

    /**
     * 更新UNI口实际状态
     * 
     * @param uniId
     *            UNI口ID
     * @param adminStatus
     *            UNI口使能状态
     */
    void updateUniOperaStatus(Long uniId, Integer status);

    /**
     * 更新UNI口自协商使能状态
     * 
     * @param uniId
     *            UNI ID
     * @param adminStatus
     *            UNI口自协商使能状态
     */
    void updateUniAutoNegotiationStatus(Long uniId, Integer adminStatus);

    /**
     * 更新UNI的基本信息
     * 
     * @param oltUniAttribute
     *            UNI属性
     */
    void updateOnuUniAttribute(OltUniAttribute oltUniAttribute);

    /**
     * 插入更新UNI基本信息
     * 
     * @param oltUniAttribute
     *            UNI属性
     */
    void insertOrUpdateOltUniAttribute(OltUniAttribute oltUniAttribute);

    /**
     * 批量插入以及更新UNI口限速
     * 
     * @param list
     *            UNI口限速
     */
    void batchInsertOltUniRateLimit(final List<OltUniPortRateLimit> list);

    /**
     * 批量插入以及更新Uni口广播风暴抑制
     * 
     * @param list
     */
    void batchInsertOltUniStormInfo(final List<OltUniStormSuppressionEntry> list);

    /**
     * 更新UNI端口自协商状态
     * 
     * @param uniId
     *            UNI口ID
     */
    void updateUniAutoNegoStatus(Long uniId, Integer autoNegoStatus);

    /**
     * 获取UNI索引
     * 
     * @param uniId
     *            UNI口索引
     * @return Long
     */
    Long getUniIndex(Long uniId);

    /**
     * 获取UNI ID
     * 
     * @param entityId
     * @param uniIndex    
     * @return Long
     */
    Long getUniIdByIndex(Long entityId, Long uniIndex);

    /**
     * 更新UNI流控使能
     * 
     * @param uniId
     *            UNI口ID
     * @param uniFlowCtrlEnable
     *            UNI流控使能状态
     */
    void updateUniFlowCtrlEnable(Long uniId, Integer uniFlowCtrlEnable);

    /**
     * 更新UNI端口自协商使能状态
     * 
     * @param uniId
     *            UNI口ID
     * @param autoNegoEnable
     *            UNI端口自协商使能状态
     */
    void updateUniAutoNegoEnable(Long uniId, Integer autoNegoEnable);

    /**
     * 获得UNI端口限速信息
     * 
     * @param uniId
     *            uni端口Id
     */
    OltUniPortRateLimit getUniPortRateLimit(Long uniId);

    /**
     * 更新UNI端口限速信息
     * 
     * @param oltUniPortRateLimit
     *            uni端口限速属性
     */
    void updateUniPortRateLimit(OltUniPortRateLimit oltUniPortRateLimit);

    /**
     * 设置uni 隔离使能
     * 
     * @param onuId
     *            onu Id
     * @param uniIsolationEnable
     *            使能状态
     */
    void updateUniIsolationEnable(Long uniId, Integer uniIsolationEnable);

    /**
     * 设置uni 15分钟使能
     * 
     * @param uniId
     *            uni Id
     * @param uni15minEnable
     *            使能状态
     */
    void updateUni15minEnable(Long uniId, Integer uni15minEnable);

    /**
     * 设置uni 24小时使能
     * 
     * @param uniId
     *            uni Id
     * @param uni24hEnable
     *            使能状态
     */
    void updateUni24hEnable(Long uniId, Integer uni24hEnable);

    /**
     * 获取UNI口广播风暴数据
     * 
     * @param uniId
     *            uni口ID
     * 
     * @return OltUniStormSuppressionEntry
     */
    OltUniStormSuppressionEntry getUniStormSuppression(Long uniId);

    /**
     * 更新UNI口广播风暴数据
     * 
     * @param OltUniStormSuppressionEntry
     *            广播风暴抑制管理属性
     * 
     */
    void updateUniStormSuppression(OltUniStormSuppressionEntry oltUniStormSuppressionEntry);

    /**
     * 获取自协商属性
     * 
     * @param uniId
     *            uni端口ID
     * 
     * @return OltUniExtAttribute
     */
    OltUniExtAttribute getOltUniExtAttribute(Long uniId);

    /**
     * 更新自协商模式
     * 
     * @param OltUniExtAttribute
     *            UNI扩展属性
     */
    void updateOltUniAutoNegoMode(OltUniExtAttribute oltUniExtAttribute);

    /**
     * 获取UNI MAC最大学习数
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
    void modifyUniMacAddrLearnMaxNum(Long uniId, Long topUniAttrMacAddrLearnMaxNum);

    /**
     * 修改uni端口Mac地址老花时间
     * 
     * @param uniId
     * @param uniMacAgeTime
     */
    void modifyUniMacAgeTime(Long uniId, Integer uniMacAgeTime);

    /**
     * 修改UNI扩展属性表的属性值
     * 
     * @param OltUniExtAttribute
     * @throws Exception
     */
    void updateOltUniExtAttribute(OltUniExtAttribute v);

    /**
     * Add by lzt
     * 
     * 更新UNI口环回使能
     * 
     * @param OltUniExtAttribute
     *            v
     */
    void updateUniUniDSLoopBackEnable(OltUniExtAttribute v);

    /**
     * Add by lzt
     * 
     * 更新UNI口untag优先级
     * 
     * @param OltUniExtAttribute
     *            v
     */
    void updateUniUSUtgPri(OltUniExtAttribute v);

    /**
     * 配置ONU UNI端口环回使能
     * 
     * @param v
     */
    void updateUniLoopDetectEnable(OltUniExtAttribute v);

    /**
     * @param uniExtAttributes
     */
    void batchInsertOltUniExtAttribute(List<OltUniExtAttribute> uniExtAttributes);

    /**
     * 批量更新UNI端口15min性能统计使能状态
     * 
     * @param uniAttrs
     *           
     */
    void batchUpdateUni15MinStatus(List<OltUniExtAttribute> uniAttrs);

    /**
     * syncUniAttribute
     * 
     * @param list
     * @param entityId
     * @param onuMap
     */
    void syncUniAttribute(List<OltUniAttribute> list, Long entityId, HashMap<Long, Long> onuMap, boolean delUniFlag);

    /**
     * syncUniExtAttribute
     * 
     * @param uniExtAttributes
     * @param onuMap
     */
    void syncUniExtAttribute(List<OltUniExtAttribute> uniExtAttributes, HashMap<Long, Long> onuMap);

    /**
     * 通过OLT ID和ONU INDEX 获取 UNI 端口
     * @param entityId
     * @param onuIndex
     * @return
     */
    List<Long> getUniIndexListByEntityIdAndOnuIndex(Long entityId, Long onuIndex);

    /**
     * 通过UNI ID 获得ONU的类型,GPON or EPON
     * @param uniId
     * @return
     */
    String getOnuEorGByUniId(Long uniId);

}
