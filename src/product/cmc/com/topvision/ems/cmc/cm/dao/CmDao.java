/***********************************************************************
 * $Id: CmDao.java,v1.0 2011-12-8 下午03:31:35 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.cm.domain.Cm3Signal;
import com.topvision.ems.cmc.cm.domain.CmSignal;
import com.topvision.ems.cmc.cpe.domain.CmLocateInfo;
import com.topvision.ems.cmc.domain.CmTopologyInfo;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.CmStaticIp;
import com.topvision.ems.cmc.facade.domain.CmStatus;
import com.topvision.ems.cmc.facade.domain.CpeAttribute;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.performance.domain.CmAct;
import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.cmc.performance.domain.CpeAct;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2011-12-8-下午03:31:35
 * 
 */
public interface CmDao extends BaseEntityDao<Entity> {

    /**
     * 获取cc下所有cm基本信息
     * 
     * @param cmcId
     * @return
     */
    List<CmAttribute> selectCmAttributeByCmcId(Long cmcId);

    /**
     * 获取cc下所有cm上行信道信息
     * 
     * @param cmcId
     * @return
     */
    List<DocsIf3CmtsCmUsStatus> selectCmUsStatusByCmcId(Long cmcId);

    /**
     * 获取信道cm统计信息
     * 
     * @param cmcId
     * @param channelIndex
     * @return
     */
    CmNum selectChannelCmNum(Long entityId, Long channelIndex);

    /**
     * 获取信道CM统计信息历史数据
     * 
     * @param entityId
     * @param channelIndex
     * @param startTime
     * @param endTime
     * @return
     */
    List<CmNum> selectChannelCmNumHis(Long entityId, Long channelIndex, String startTime, String endTime);

    /**
     * 获取cmc所在olt entityId
     * 
     * @param cmcId
     * @return cmc所在olt entityId
     */
    Long getEntityIdByCmcId(Long cmcId);

    /**
     * 获取所在OLT下的CM列表
     * 
     * @param cmId
     *            cm设备ID
     * @param start
     *            分页起始页码
     * @param limit
     *            每页限定显示长度
     * @return List<CmAttribute> cm列表
     */
    List<CmAttribute> getCmListByOlt(Long cmId, Integer start, Integer limit);

    /**
     * 获取所在OLT下的CM列表总数
     * 
     * @param cmId
     *            cm设备ID
     * @return Integer cm数目
     */
    Long getCmListByOltCount(Long cmId);

    /**
     * 获取所在PON口下的CM列表
     * 
     * @param cmId
     *            cm设备ID
     * @param start
     *            分页起始页码
     * @param limit
     *            每页限定显示长度
     * @return List<CmAttribute> cm列表
     */
    List<CmAttribute> getCmListByPon(Long cmId, Integer start, Integer limit);

    /**
     * 获取所在PON口下的CM列表总数
     * 
     * @param cmId
     *            cm设备ID
     * @return Integer cm数目
     */
    Long getCmListByPonCount(Long cmId);

    /**
     * 获取所在CMC下的CM列表
     * 
     * @param cmId
     *            cm设备ID
     * @param start
     *            分页起始页码
     * @param limit
     *            每页限定显示长度
     * @return List<CmAttribute> cm列表
     */
    List<CmAttribute> getCmListByCmc(Long cmId, Integer start, Integer limit);

    /**
     * 获取所在CMC下的CM列表总数
     * 
     * @param cmId
     *            cm设备ID
     * @return Integer cm数目
     */
    Long getCmListByCmcCount(Long cmId);

    /**
     * 获取所在上行通道下的CM列表
     * 
     * @param cmId
     *            cm设备ID
     * @param start
     *            分页起始页码
     * @param limit
     *            每页限定显示长度
     * @return List<CmAttribute> cm列表
     */
    List<CmAttribute> getCmListByUpPortId(Long cmId, Integer start, Integer limit);

    /**
     * 获取所在上行通道下的CM列表总数
     * 
     * @param cmId
     *            cm设备ID
     * @return Integer cm数目
     */
    Long getCmListByUpPortIdCount(Long cmId);

    /**
     * 获取所在下行通道下的CM列表
     * 
     * @param cmId
     *            cm设备ID
     * @param start
     *            分页起始页码
     * @param limit
     *            每页限定显示长度
     * @return List<CmAttribute> cm列表
     */
    List<CmAttribute> getCmListByDownPortId(Long cmId, Integer start, Integer limit);

    /**
     * 获取所在下行通道下的CM列表总数
     * 
     * @param cmId
     *            cm设备ID
     * @return Integer cm数目
     */
    Long getCmListByDownPortIdCount(Long cmId);

    /**
     * 通过条件查询不同状态的CM数
     * 
     * @param map
     *            entityId，PonID，upchannelId,DownChannelId,status
     * @return
     */
    Long getCmNumByStatus(Map<String, Object> map);

    /**
     * 通过条件查询CMTS下不同状态的CM数
     * 
     * @param map
     *            entityId，PonID，upchannelId,DownChannelId,status
     * @return
     */
    Long getCmtsCmNumByStatus(Map<String, Object> map);

    /**
     * 通过MAC查询关联CM的服务流列表
     * 
     * @param cmMac
     *            cm设备Mac地址
     * @return List<CmcQosServiceFlowInfo> 服务流列表
     */
    List<CmcQosServiceFlowInfo> getCmServiceFlowListInfo(String cmMac);

    /**
     * 查询CM拓扑信息
     * 
     * @param cmId
     *            cm设备ID
     * @return TopologyInfo cm拓扑信息
     */
    CmTopologyInfo getTopologyInfo(Long cmId);

    /**
     * 查询CM拓扑信息
     *
     * @param cmId
     *            cm设备ID
     * @return typeId master device typeId
     */
    Long getDeviceTypeByCmId(Long cmId);

    /**
     * 查询CM拓扑信息
     *
     * @param cmId
     *            cm设备ID
     * @return TopologyInfo cm拓扑信息
     */
    CmTopologyInfo get8800BTopologyInfo(Long cmId);

    /**
     * 查询CM拓扑信息
     *
     * @param cmId
     *            cm设备ID
     * @return TopologyInfo cm拓扑信息
     */
    CmTopologyInfo getCmtsTopologyInfo(Long cmId);

    /**
     * 查询CM拓扑信息
     *
     * @param cmId
     *            cm设备ID
     * @return TopologyInfo cm拓扑信息
     */
    Long getUpChannelId(Long cmId);

    /**
     * 查询CM拓扑信息
     *
     * @param cmId
     *            cm设备ID
     * @return TopologyInfo cm拓扑信息
     */
    List<Long> getUpChannelIdList(Long cmId);

    /**
     * 查询CM拓扑信息
     *
     * @param cmId
     *            cm设备ID
     * @return TopologyInfo cm拓扑信息
     */
    Long getDownChannelId(Long cmId);

    /**
     * 获取CMC关联的CM列表
     * 
     * @param cmcId
     *            cmc设备id
     * @param start
     *            分页起始页码
     * @param limit
     *            每页限定显示长度
     * @return List<CmAttribute> cm列表
     */
    List<CmAttribute> getCmListByCmcId(Long cmcId, Integer start, Integer limit);

    /**
     * 根据cmId获取cm关联的cpe集合属性
     * 
     * @param cmId
     * @return
     */
    List<CpeAttribute> getCpeListByCmId(Long cmId);

    /**
     * 根据cmId查找该CM允许的最大cpe数
     * 
     * @param cmId
     * @return Integer
     */
    Integer getCpeMaxIpNum(Long cmId);

    /**
     * 根据cmId查找该cm上实际的cpe数目
     * 
     * @param cmId
     * @return
     */
    Integer getRealIpNum(Long cmId);

    /**
     * 根据CM Mac获取CM的Index
     * 
     * @param cmMac
     * @return
     */
    Long getCmIndexByMac(String cmMac);

    /**
     * 根据CM Mac获取CM所在OLT的entityId
     * 
     * @param cmMac
     * @return
     */
    Long getEntityIdByMac(String cmMac);

    /**
     * 更新CM属性
     * 
     * @param cmAttribute
     */
    void updateCmAttribute(CmAttribute cmAttribute);

    /**
     * 根据CM ID获取cm基本属性
     * 
     * @param cmId
     * @return
     */
    CmAttribute getCmAttributeByCmId(Long cmId);

    /**
     * 根据cmId获取cmcId
     */
    Long getCmcIdByCmId(Long cmId);

    /**
     * 根据cm mac地址获取cm属性
     * 
     * @param macs
     * @return
     */
    List<CmAttribute> selectCmListByCmMacs(String macs, Integer start, Integer limit);

    /**
     * 获取CM下CPE信息
     * 
     * @param cmId
     * @return
     */
    List<CmCpe> queryCmCpeList(Long cmId);

    /**
     * 获取CM大客户IP信息
     * 
     * @param cmId
     * @return
     */
    List<CmStaticIp> queryCmStaticIpList(Long cmId);

    /**
     * 获取3.0cm上行信号数据
     * 
     * @param cmId
     * @return
     */
    List<DocsIf3CmtsCmUsStatus> queryDocsIf3CmtsCmUsStatusList(Long cmId);

    /**
     * 获取CM上下线行为信息
     * 
     * @param map
     * @return
     */
    List<CmAct> selectCmActionInfo(Map<String, Object> map);

    /**
     * 获取Cpe上下线行为信息
     * 
     * @param map
     * @return
     */
    List<CpeAct> selectCpeActionInfoByCmMac(Map<String, Object> map);

    void updateCmStatusValue(Long cmcId, String mac, int statusValue);

    void deleteCmCpe(Long cmId, String cpeMac);

    /**
     * 根据CmId查询CmAttribute
     * 
     * @param cmId
     * @return
     */
    CmAttribute selectCmAttributeByCmId(Long cmId);

    /**
     * 插入或者更新CmSignal表,cmcpe查询页面查询后更新
     * 
     * @param map
     */
    void insertOrUpdateCmSignal(CmStatus cmStatus);

    /**
     * 根据CM mac查询局端设备的entityid
     * 
     * @param cmMac
     * @return
     */
    List<Long> queryEntityIdByCmMac(String cmMac);

    /**
     * 查询CC信息
     * 
     * @param entityId
     * @param ccIndex
     * @return
     */
    List<CmLocateInfo> queryCmLocate(Long entityId, Long ccIndex);

    Long selectcmcIdByEntityIdAndCmmac(Long entityId, String cmmac);

    /**
     * 更新CM的上行SNR
     * 
     * @param cmId
     * @param upChannelSnr
     */
    void updateUpChannelSnr(CmAttribute cmAttribute);

    /**
     * 根据CMID获取 previous state
     * 
     * @param cmId
     * @return
     */
    CmAttribute getPreviousStateById(Long cmId);

    /**
     * 插入
     * 
     * @param cmSingal
     */
    void insertOrUpdateCmSignal(CmSignal cmSingal);

    void insertOrUpdateCm3Signal(Cm3Signal cm3Signal);

    CmSignal selectCmSignal(Long cmId);

    List<Cm3Signal> selectUpChannelCm3Signal(Long cmId, Long channelType);
    
    void deleteCmSignal(Long cmId);
    
    void deleteCm3Signal(Long cmId);

    List<Cm3Signal> getUpChannelSignalByCmId(Long cmId);

    List<Cm3Signal> getDownChannelSignalByCmId(Long cmId);
    
    /**
     * 更新指定CM的statusValue
     * @param cmId
     * @param statusValue
     */
    void updateStatusValue(Long cmId, Integer statusValue);

    /**
     * 更新CM配置文件名
     * @param cmId
     * @param docsDevServerConfigFile
     */
    void updateCmConfigFile(Long cmId, String docsDevServerConfigFile);

    /**
     * 删除清除的cm
     * @param
     * @return void
     */
    void deleteCmCleared(List<Long> cmId);

    /**
     * 清除单个离线cm
     * @param
     * @return void
     */
    void deleteCmClearedOne(Long cmId);

}
