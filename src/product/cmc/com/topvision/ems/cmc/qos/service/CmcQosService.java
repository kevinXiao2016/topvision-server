/***********************************************************************
 * $Id: CmcQosService.java,v1.0 2011-12-8 下午12:46:03 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.qos.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceClassInfo;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowInfo;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowStats;
import com.topvision.ems.cmc.qos.facade.domain.CmMacToServiceFlow;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosDynamicServiceStats;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosParamSetInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosPktClassInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceClass;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceFlowAttribute;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceFlowStatus;
import com.topvision.framework.service.Service;

/**
 * 服务流功能
 * 
 * @author loyal
 * @created @2011-12-8-下午12:46:03
 * 
 */
public interface CmcQosService extends Service {
    /**
     * 刷新单个CC上的服务流基本信息
     * 
     * @param cmcId
     *            CMC ID
     */
    List<CmcQosServiceFlowAttribute> refreshServiceFlowBaseInfoOnCC(Long cmcId,Long cmcIndex);
    /**
     * 刷新服务流基本信息
     * 
     * @param cmcId
     *            CMC ID
     */
    List<CmcQosServiceFlowAttribute> refreshServiceFlowBaseInfo(Long cmcId);

    /**
     * 刷新服务流状态信息
     * 
     * @param cmcId
     *            CMC ID
     */
    List<CmcQosServiceFlowStatus> refreshServiceFlowStatus(Long cmcId);

    /**
     * 刷新服务流包分类器
     * 
     * @param cmcId
     *            CMC ID
     */
    List<CmcQosPktClassInfo> refreshServiceFlowPktClassInfos(Long cmcId);
    
    /**
     * 刷新单个CC上的服务流参数集
     * 
     * @param cmcId
     *            CMC ID
     */
    List<CmcQosParamSetInfo> refreshServiceFlowParamSetOnCC(Long cmcId,Long cmcIndex);
    /**
     * 刷新服务流参数集
     * 
     * @param cmcId
     *            CMC ID
     */
    List<CmcQosParamSetInfo> refreshServiceFlowParamSetInfos(Long cmcId);

    /**
     * 刷新服务流所属CM信息
     * 
     * @param cmcId
     *            CMC ID
     */
    List<CmMacToServiceFlow> refreshCmMacToServiceFlows(Long cmcId);

    /**
     * 获取上行动态服务流统计信息
     * 
     * @param cmcId
     *            CMC ID
     * @param direction
     *            方向
     * @return CmcQosDynamicServiceStats 动态服务流统计信息
     */
    CmcQosDynamicServiceStats getQosUpDynamicServiceStatsInfo(Long cmcId);

    /**
     * 获取下行动态服务流统计信息
     * 
     * @param cmcId
     *            CMC ID
     * @param direction
     *            方向
     * @return CmcQosDynamicServiceStats 动态服务流统计信息
     */
    CmcQosDynamicServiceStats getQosDownDynamicServiceStatsInfo(Long cmcId);

    /**
     * 获取上行服务流数量统计
     * 
     * @param map
     *            CMC ID 和当前时间
     * @return CmcQosServiceFlowStats 服务流数量
     */
    CmcQosServiceFlowStats getUpQosServiceFlowStatsInfo(Map<String, Object> map);

    /**
     * 获取下行服务流数量统计
     * 
     * @param cmcId
     *            CMC ID 和当前时间
     * @return
     */
    CmcQosServiceFlowStats getDownQosServiceFlowStatsInfo(Map<String, Object> map);

    /**
     * 通过过滤条件过滤服务流信息列表
     * 
     * @param map
     *            包含cmcId、direction、mac
     * @param start
     *            分页start
     * @param limit
     *            分页limit
     * @return List<CmcQosServiceFlowInfo> 服务流列表
     */
    List<CmcQosServiceFlowInfo> getCmcQosServiceFlowListInfoWithCondition(Map<String, String> map, Integer start,
            Integer limit);

    /**
     * 获取通过过滤条件过滤的服务流列表数量
     * 
     * @param map
     *            包含CMC ID、direction、mac
     * @param direction
     *            方向
     * @return Integer 服务流列表数量
     */
    Integer getCmcQosServiceFlowListNumWithCondition(Map<String, String> map);

    /**
     * 获取服务流参数集信息
     * 
     * @param cmcId
     *            CMC ID
     * @param sId
     *            服务流sId
     * @return List<CmcQosParamSetInfo> 服务流参数集信息
     */
    List<CmcQosParamSetInfo> getQosParamSetInfo(Long sId);

    /**
     * 获取服务流包分类器信息
     * 
     * @param cmcId
     *            CMC ID
     * @param sId
     *            服务流sId
     * @return List<CmcQosPktClassInfo> 服务流包分类器信息
     */
    List<CmcQosPktClassInfo> getQosPktClassInfo(Long sId);

    /**
     * 查询服务流关联的CM信息
     * 
     * @param mac
     *            CM Mac
     * @return CmAttribute CM属性
     */
    CmAttribute getServiceFlowConnectedCm(Long sId);

    /**
     * 获取服务流模板列表
     * 
     * @param cmcId
     *            CMC ID
     * @return
     */
    List<CmcQosServiceClassInfo> getQosServiceClassList(Long cmcId);

    /**
     * 获取单条服务流模板的信息
     * 
     * @param map
     * @return
     */
    CmcQosServiceClass getQosServiceClassInfo(Long scId);

    /**
     * 创建或修改服务流模板
     * 
     * @param serviceClass
     * @return
     */
    Boolean createOrModifyServiceClassInfo(CmcQosServiceClass serviceClass, Long cmcId);

    /**
     * 删除服务流模板
     * 
     * @param scId
     * @return
     */
    Boolean deleteServiceClassInfo(Long scId, Long cmcId);

    /**
     * 通过cmcId查询entityId
     * 
     * @param cmcId
     *            CMC ID
     * @return entityId entityId
     */
    Long getEntityIdByCmcId(Long cmcId);
}
