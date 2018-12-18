/***********************************************************************
 * $Id: CmcQosDao.java,v1.0 2011-12-8 下午03:39:47 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.qos.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceClassInfo;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowCmRelation;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowInfo;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowStats;
import com.topvision.ems.cmc.qos.facade.domain.CmMacToServiceFlow;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosDynamicServiceStats;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosParamSetInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosPktClassInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceClass;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceFlowAttribute;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceFlowStatus;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2011-12-8-下午03:39:47
 * 
 */
public interface CmcQosDao extends BaseEntityDao<Entity> {
    /**
     * 通过Sid与Mac查询服务流关联的CM信息
     * 
     * @param SId
     *            服务流id
     * @param cmMac
     *            cm设备的mac地址
     * @return CmAttribute cm信息
     */
    CmAttribute getServiceFlowConnectedCm(Long SId, String cmMac);

    /**
     * 获取CMC上某个服务流的包分类器信息
     * 
     * @param sId
     *            cmc设备id
     * @param sId
     *            服务流sId
     * @return List<CmcQosPktClassInfo> 单个服务流包分类器列表
     */
    List<CmcQosPktClassInfo> getQosPktClassInfo(Long sId);

    /**
     * 获取CMC上由过滤条件过滤的服务流列表数量
     * 
     * @param map
     *            包含cmcId、direction、Mac
     * @return Integer 服务流列表数量
     */
    Integer getCmcQosServiceFlowListNumWithCondition(Map<String, String> map);

    /**
     * 获取某个CMC下行方向的动态服务流统计信息
     * 
     * @param cmcId
     *            CMC设备ID
     * @return CmcQosDynamicServiceStats 动态服务流统计信息
     */
    CmcQosDynamicServiceStats getQosDownDynamicServiceStatsInfo(Long cmcId);

    /**
     * 获取某个CMC上行方向的动态服务流统计信息
     * 
     * @param cmcId
     *            CMC设备ID
     * @return CmcQosDynamicServiceStats 动态服务流统计信息
     */
    CmcQosDynamicServiceStats getQosUpDynamicServiceStatsInfo(Long cmcId);

    /**
     * 获取某个CMC上的上行服务流数量统计
     * 
     * @param map
     *            cmc设备id 当前时间
     * @return List<CmcQosServiceFlowStats> 服务流统计信息列表
     */
    CmcQosServiceFlowStats getUpQosServiceFlowStatsInfo(Map<String, Object> map);

    /**
     * 获取某个CMC上的下行服务流数量统计
     * 
     * @param map
     *            cmc设备id 当前时间
     * @return List<CmcQosServiceFlowStats> 服务流统计信息列表
     */
    CmcQosServiceFlowStats getDownQosServiceFlowStatsInfo(Map<String, Object> map);

    /**
     * 获取CMC上某个服务流的参数集信息
     * 
     * @param sId
     *            cmc设备id
     * @param sId
     *            服务流sId
     * @return List<CmcQosParamSetInfo> 单个服务流参数集列表
     */
    List<CmcQosParamSetInfo> getQosParamSetInfo(Long sId);

    /**
     * 查询服务流关联CM信息
     * 
     * @param sId
     *            cm设备mac地址
     * @return CmAttribute cm信息
     */
    CmAttribute getServiceFlowConnectedCm(Long sId);

    /**
     * 插入一条服务流属性
     * 
     * @param cmcQosServiceFlowAttribute
     *            所要插入的服务流属性
     * @return Boolean 插入是否成功
     */
    Boolean insertServiceFlowAttribute(CmcQosServiceFlowAttribute cmcQosServiceFlowAttribute);

    /**
     * 更新一条服务流属性
     * 
     * @param cmcQosServiceFlowAttribute
     *            所要更新的服务流属性
     */
    void updateServiceFlowAttribute(CmcQosServiceFlowAttribute cmcQosServiceFlowAttribute);

    /**
     * 插入或更新服务流属性
     * 
     * @param list
     *            所要插入或更新的服务流属性List
     */
    void inSertOrUpdateServiceFlowAttribute(List<CmcQosServiceFlowAttribute> list);

    /**
     * 获取某个服务流某个方向服务流列表信息
     * 
     * @param map
     *            包含cmcId、direction
     * @param start
     *            分页起始页码
     * @param limit
     *            每页限定显示长度
     * @return List<CmcQosServiceFlowInfo> 服务流列表信息
     */
    List<CmcQosServiceFlowInfo> getCmcQosServiceFlowListInfoWithCondition(Map<String, String> map, Integer start,
            Integer limit);

    /**
     * 获取服务流模板列表
     * 
     * @param entityId
     *            CMC ID
     * @return
     */
    List<CmcQosServiceClassInfo> getQosServiceClassList(Long entityId);

    /**
     * 获取单条服务流模板信息
     * 
     * @param scId
     *            服务流模板ID
     * @return
     */
    CmcQosServiceClass getQosServiceClassInfo(Long scId);

    /**
     * 插入或更新服务流模板信息
     * 
     * @param serviceClass
     * @return
     */
    void insertOrUpdateServiceClass(CmcQosServiceClass serviceClass);

    /**
     * 删除服务流模板
     * 
     * @param scId
     *            服务流模板ID
     * @return
     */
    void deleteServiceClass(Long scId);

    /**
     * 获取cmc所在olt entityId
     * 
     * @param cmcId
     * @return cmc所在olt entityId
     */
    Long getEntityIdByCmcId(Long cmcId);

    /**
     * 通过OLT的entityId获得OLT下挂的CC的cmcId
     * 
     * @param entityId
     * @return
     */
    List<Long> getCmcIdByOlt(Long entityId);

    /**
     * 插入一条服务流与CM的关系记录
     * 
     * @param cmcQosServiceFlowCmRelation
     *            所要插入的服务流与Cm的关系记录
     * @return Boolean 是否成功
     */
    Boolean insertServiceFlowCmRelation(CmcQosServiceFlowCmRelation cmcQosServiceFlowCmRelation);

    /**
     * 更新一条服务流与CM关系记录
     * 
     * @param cmcQosServiceFlowCmRelation
     *            所要更新的服务流与CM的关系记录
     */
    void upDateServiceFlowCmRelation(CmcQosServiceFlowCmRelation cmcQosServiceFlowCmRelation);

    /**
     * 插入或更新一批服务流与CM的关系记录
     * 
     * @param list
     *            所要插入或更新的一批服务流与CM的关系记录
     */
    void insertOrUpdateServiceFlowCmRelation(List<CmcQosServiceFlowCmRelation> list);

    /**
     * 插入一条动态服务流统计信息
     * 
     * @param cmcQosDynamicServiceStats
     *            所要插入的动态服务流统计信息
     * @return Boolean 插入是否成功
     */
    Boolean insertDynamicServiceStatsInfo(CmcQosDynamicServiceStats cmcQosDynamicServiceStats);

    /**
     * 更新一条动态服务流统计信息
     * 
     * @param cmcQosDynamicServiceStats
     *            所要更新的动态服务流统计信息
     */
    void updateDynamicServiceStatsInfo(CmcQosDynamicServiceStats cmcQosDynamicServiceStats);

    /**
     * 插入或更新一批动态服务流统计信息
     * 
     * @param list
     *            所要插入或更新的一批动态服务流统计信息
     */
    void insertOrUpdateDynamicServiceStatsInfo(List<CmcQosDynamicServiceStats> list);

    /**
     * 插入一条服务流参数集信息
     * 
     * @param cmcQosParamSetInfo
     *            所要插入的服务流参数集信息
     * @return Boolean 插入是否成功
     */
    Boolean insertQosParamSetInfo(CmcQosParamSetInfo cmcQosParamSetInfo);

    /**
     * 更新一条服务流参数集信息
     * 
     * @param cmcQosParamSetInfo
     *            所要更新的服务流参数集信息
     */
    void updateQosParamSetInfo(CmcQosParamSetInfo cmcQosParamSetInfo);

    /**
     * 插入或更新一批服务流参数集信息
     * 
     * @param list
     *            所要插入或更新的服务流参数集信息
     */
    void insertOrUpdateQosParamSetInfo(List<CmcQosParamSetInfo> list);

    /**
     * 插入一条包分类器信息
     * 
     * @param cmcQosPktClassInfo
     *            所要插入的包分类器信息
     * @return Boolean 插入是否成功
     */
    Boolean insertQosPktClassInfo(CmcQosPktClassInfo cmcQosPktClassInfo);

    /**
     * 更新一条包分类器信息
     * 
     * @param cmcQosPktClassInfo
     *            所要更新的包分类器信息
     */
    void updateQosPktClassInfo(CmcQosPktClassInfo cmcQosPktClassInfo);

    /**
     * 插入或更新一批包分类器信息
     * 
     * @param list
     *            所要插入或更新的包分类器信息
     */
    void insertOrUpdateQosPktClassInfo(List<CmcQosPktClassInfo> list);

    /**
     * 获取cmc属性
     * 
     * @param cmcId
     *            cmc设备id
     * @return CmcAttribute cmc属性
     */
    CmcAttribute getCmcAttributeByCmcId(Long cmcId);
    
    /**
     * 根据CMCID获取当前所有服务流的ID 
     * @param cmcId
     * 			cmc设备id
     * @return 
     */
    List<Long> getServiceFlowIdListByCmcId(Long cmcId);
    /**
     * 根据CMCID获取当前所有服务流的数据库ID 
     * @param cmcId
     * 			cmc设备id
     * @return 
     */
    List<Long> getDBSIdListByCmcId(Long cmcId);

    /**
     * 批量插入更新cmc服务流状态信息
     * 
     * @param cmcQosServiceFlowStatusList
     * @param cmcId
     */
    void batchInsertCmcQosServiceFlowStatus(final List<CmcQosServiceFlowStatus> cmcQosServiceFlowStatusList, Long cmcId);
    
    /**
     * 批量删除cmc服务流信息
     */
    void deleteCmcQosServiceFlowRelation(Long cmcId);
    
    /**
     * 批量插入更新Cc8800B服务流属性信息
     * 
     * @param cmcQosServiceFlowAttributeList
     * @param cmcId
     */
    void batchInsertCmcQosServiceFlowAttribute8800B(
            final List<CmcQosServiceFlowAttribute> cmcQosServiceFlowAttributeList, Long cmcId);
    
    /**
     * 批量插入更新cmc服务流属性信息
     * 
     * @param cmcQosServiceFlowAttributeList
     * @param entityId
     */
    void batchInsertCmcQosServiceFlowAttribute(final List<CmcQosServiceFlowAttribute> cmcQosServiceFlowAttributeList,
            Long entityId);
    
    /**
     * 批量插入更新单个cmc上的服务流属性信息
     * 
     * @param cmMacToServiceFlows
     * @param cmcId
     */
    void batchInsertOrUpdateQosServiceFlowAttrOnCC(
            final List<CmcQosServiceFlowAttribute> cmcQosServiceFlowAttributeList, final Long cmcId,
            final List<Long> dbSidList);
    
    /**
     * 批量插入更新cc8800B参数集信息
     * 
     * @param cmcQosParamSetInfoList
     *            entityId
     */
    void batchInsertCmcQosParamSetInfo8800B(final List<CmcQosParamSetInfo> cmcQosParamSetInfoList, final Long cmcId);
    
    /**
     * 批量插入更新cmc参数集信息
     * 
     * @param cmcQosParamSetInfoList
     *            entityId
     */
    void batchInsertCmcQosParamSetInfo(final List<CmcQosParamSetInfo> cmcQosParamSetInfoList, final Long entityId);
    
    /**
     * 批量插入更新单个cmc上的QOS参数集信息
     * 
     * @param cmcQosParamSetInfoList
     * @param cmcId
     */
    void batchInsertOrUpdateCmcQosParamSetInfoOnCC(final List<CmcQosParamSetInfo> cmcQosParamSetInfoList,
            final Long cmcId, final List<Long> dbSidList);
    
    /**
     * 批量插入更新cc8800B包分类器信息
     * 
     * @param cmcQosPktClassInfoList
     * @param cmcId
     */
    void batchInsertCmcQosPktClassInfo8800B(final List<CmcQosPktClassInfo> cmcQosPktClassInfoList, final Long cmcId);
    
    /**
     * 批量插入更新cmc包分类器信息
     * 
     * @param cmcQosPktClassInfoList
     * @param entityId
     */
    void batchInsertCmcQosPktClassInfo(final List<CmcQosPktClassInfo> cmcQosPktClassInfoList, final Long entityId);
    
    /**
     * 批量插入更新cmc服务流属性信息
     * 
     * @param cmMacToServiceFlows
     * @param entityId
     */
    void batchInsertCmMacToServiceFlow(final List<CmMacToServiceFlow> cmMacToServiceFlows, final Long entityId);
    
    /**
     * 批量插入更新CC8800B cmMacToServiceFlows属性信息
     * 
     * @param cmMacToServiceFlows
     * @param cmcId
     * @param entityId
     */
    void batchInsertCC8800BCmMacToServiceFlow(final List<CmMacToServiceFlow> cmMacToServiceFlows, final Long cmcId,
            final Long enityId);
    
    /**
     * 根据cmcId serviceflowId获取sId
     * 
     * @param map
     * @return
     */
    public Long getSIdByCmcIdAndServiceFlowId(Map<String, Long> map);
    
    /**
     * 根据cmcIndex获取cmcId
     * 
     * @param map
     * @return
     */
    Long getCmcIdByCmcIndexAndEntityId(Map<String, Long> map);
    
    /**
     * 获取cmId
     * 
     * @param entityId
     * @param mac
     * @return
     */
    Long getCmIdByMac(Long entityId, Long mac);
    
    /**
     * 删除某CC下服务流的参数集信息
     * 
     * @param Long
     *            cmcId
     */
    void deleteServiceFlowParmsetRelationByCmcId(Long cmcId);

}
