/***********************************************************************
 * $Id: CmcDiscoveryDao.java,v1.0 2011-11-13 上午10:29:30 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.CmStaticIp;
import com.topvision.ems.cmc.facade.domain.CmcDownChannelBaseInfo;
import com.topvision.ems.cmc.facade.domain.CmcEntityRelation;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.facade.domain.TopCpeAttribute;
import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelBaseInfo;
import com.topvision.ems.cmc.upchannel.facade.domain.CmcUpChannelSignalQualityInfo;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2011-11-13-上午10:29:30
 * 
 */
public interface CmcDiscoveryDao extends BaseEntityDao<CmcEntity> {

    /**
     * 插入更新cmc信息
     * 
     * @param cmcEntityRelation
     * @param cmcAttribute
     */
    void syncCmcEntityInfo(Long entityId, List<CmcEntityRelation> cmcEntityRelations, List<CmcAttribute> cmcAttributes);

    /**
     * 
     * 
     * @param cmcUpChannelBaseInfoList
     * @param cmcUpChannelSignalQualityInfos
     * @param cmcDownChannelBaseInfos
     * @param cmcPorts
     * @param entityId
     */
    void syncCmcChannelBaseInfo(List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfoList,
            List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfos,
            List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfos, List<CmcPort> cmcPorts, Long entityId);
    
    Map<String, Long> getChannelMap(Long entityId);

    /**
     * 批量插入更新cmc上行信道基本信息
     * 
     * @param cmcUpChannelBaseInfoList
     * @param cmcId
     */
    void batchInsertCmcUpChannelBaseInfo(final List<CmcUpChannelBaseInfo> cmcUpChannelBaseInfoList, Long cmcId);

    /**
     * 批量插入更新上行信道信号质量信息
     * 
     * @param cmcUpChannelSignalQualityInfoList
     */
    void batchInsertCmcUpChannelSignalQualityInfo(
            final List<CmcUpChannelSignalQualityInfo> cmcUpChannelSignalQualityInfoList, final Long cmcId);

    /**
     * 批量插入更新下行信道基本信息
     * 
     * @param cmcDownChannelBaseInfoList
     * @param cmcId
     */
    void batchInsertCmcDownChannelBaseInfo(final List<CmcDownChannelBaseInfo> cmcDownChannelBaseInfoList,
            final Long cmcId);

    /**
     * 批量插入更新cmc端口信息（ifTable）
     * 
     * @modify by Rod 接口代码的含义是只更新上下行的信道接口的信息，注意是接口
     * 
     * @param cmcPortList
     * @param cmcId
     */
    void batchInsertCmcPortInfo(final List<CmcPort> cmcPortList, final Long cmcId);

    /**
     * 批量插入更新cmc信道信息（ifTable）
     * 
     * @modify by Rod 本接口为了解决ifType是129和205的问题
     * 
     * @param cmcPortList
     * @param cmcId
     */
    void batchInsertCmcChannelInfoForArris(final List<CmcPort> cmcPortList, final Long cmcId);

    /**
     * 批量插入更新cmc信道信息（ifTable）
     *
     * @modify by Rod 本接口为了解决ifType是129和205的问题
     *
     * @param cmcPortList
     * @param cmcId
     */
    void batchInsertCmcChannelInfoForUbr(final List<CmcPort> cmcPortList, final Long cmcId);
    /**
     * 批量插入更新cmc信道信息（ifTable）
     *
     * @modify by Rod 本接口为了解决ifType是129和205的问题
     *
     * @param cmcPortList
     * @param cmcId
     */
    void batchInsertCmcChannelInfo(final List<CmcPort> cmcPortList, final Long cmcId);

    /**
     * 批量插入更新8800B上cm属性信息
     * 
     * @param cmAttributeList
     */
    void batchInsertCmAttribute8800b(final List<CmAttribute> cmAttributeList, final Long cmcId, Long entityId);

    /**
     * 根据cmcIndex获取cmcId
     * 
     * @param map
     * @return
     */
    Long getCmcIdByCmcIndexAndEntityId(Map<String, Long> map);

    /**
     * 通过OLT的entityId获得OLT下挂的CC的cmcId
     * 
     * @param entityId
     * @return
     */
    List<Long> getCmcIdByOlt(Long entityId);

    /**
     * 根据ifIndex获取cmcPortId
     * 
     * @param map
     */
    Long getCmcPortIdByIfIndexAndCmcId(Map<String, Long> map);

    /**
     * 获取cmId
     * 
     * @param entityId
     * @param mac
     * @return
     */
    Long getCmIdByMac(Long entityId, Long mac);

    /**
     * 更新entity Mac
     * 
     * @param entityId
     * @param mac
     */
    void updateEntityMac(long entityId, String mac);

    /**
     * 删除cmc下状态为offline的cm
     * 
     * @param cmcId
     */
    void deleteCmcOfflineCm(long cmcId);

    /**
     * 查询某CC下的所有CmId信息
     * 
     * @param Long
     *            cmcId
     * @return List<Long>
     */
    List<Long> queryForCmIdListByCmcId(Long cmcId);

    /**
     * 根据cmId删除CM
     * 
     * @param cmId
     */
    void deleteCmByCmId(Long cmId);

    /**
     * 更新cmc 8800a相关信息到onu表
     * 
     * @param cmcAttribute
     */
    void updateCmcAttributeToOnuAttribute(CmcAttribute cmcAttribute);

    /**
     * 批量插入或更新CM CPE信息
     * 
     * @param cmCpeList
     * @param object
     */
    void batchInsertOrUpdateCmCpe(final List<CmCpe> cmCpeList, Long entityId);

    /**
     * 批量插入或更新CM大客户IP信息
     * 
     * @param cmStaticIpList
     * @param object
     */
    void batchInsertOrUpdateCmStaticIp(List<CmStaticIp> cmStaticIpList, Long entityId);

    /**
     * 批量插入或
     * 
     * @param docsIf3CmtsCmUsStatusList
     * @param cmcId
     */
    void batchInsertOrUpdateDocsIf3CmtsCmUsStatus(final List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList,
            Long cmcId);

    /**
     * 插入Cm轮询原始数据
     * 
     * @param dt
     * @param cmAttributes
     * @param entityId
     */
    void batchInsertInitialDataCmAction(long dt, List<CmAttribute> cmAttributes, Long entityId);

    /**
     * 插入Cm轮询原始数据
     * 
     * @param dt
     * @param attributes
     * @param entityId
     */
    void batchInsertOrUpdateCpeAttribute(long dt, List<TopCpeAttribute> attributes, Long entityId);

    /**
     * 刷新8800A的时候更新上行信道信息表
     * 
     * @param docsIf3CmtsCmUsStatusList
     * @param cmcId
     */
    void batchInsertOrUpdateDocsIf3CmtsCmUsStatusFor8800A(List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList,
            Long cmcId);

    /**
     * 插入cmts端口信息
     * 
     * @param cmcId
     * @param cmtsPorts
     */
    void batchInsertCmtsPorts(Long cmcId, List<CmcPort> cmtsPorts);

    /**
     * 插入cmcphy信息
     * @param cmcPhyConfigs
     * @param entityId
     */
    void batchInsertCmcPhyConfigs(List<CmcPhyConfig> cmcPhyConfigs, Long entityId);

}