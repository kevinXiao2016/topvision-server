/***********************************************************************
 * $Id: CmService.java,v1.0 2011-12-8 下午12:45:42 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.cm.domain.Cm3Signal;
import com.topvision.ems.cmc.domain.CmTopologyInfo;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.CmStaticIp;
import com.topvision.ems.cmc.facade.domain.CmStatus;
import com.topvision.ems.cmc.facade.domain.CpeAttribute;
import com.topvision.ems.cmc.performance.domain.CmAct;
import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.cmc.performance.domain.CpeAct;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowInfo;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.UserContext;

/**
 * CM相关功能
 * 
 * @author loyal
 * @created @2011-12-8-下午12:45:42
 * 
 */
public interface CmService extends Service {
    
    /**
     * 获取CM的SNMP参数
     * @return
     */
    SnmpParam getCmSnmpParam();

    /**
     * 获取cc下所有cm基本信息
     * 
     * @param cmcId
     * @return
     */
    List<CmAttribute> getCmAttributeByCmcId(Long cmcId);

    /**
     * 获取cc下所有cm实时基本信息，
     * 
     * @param cmcId
     * @return
     */
    List<CmAttribute> getRealtimeCmAttributeByCmcId(Long cmcId);

    /**
     * 获取信道cm统计信息
     * 
     * @param cmcId
     * @return
     */
    CmNum getChannelCmNum(Long cmcId, Long channelIndex);

    /**
     * 获取信道CM实时统计信息
     * 
     * @param cmcId
     * @return
     */
    CmNum getRealtimeChannelCmNum(Long cmcId, Long channelIndex);

    /**
     * 获取信道CM统计信息历史数据
     * 
     * @param cmcId
     * @param startTime
     * @param endTime
     * @return
     */
    List<CmNum> getChannelCmNumHis(Long cmcId, Long channelIndex, String startTime, String endTime);

    /**
     * 刷新Cmts页面中关联的CM列表tab
     * 
     * @param cmcId
     *            Long
     * @return List<CmAttribute>
     */
    List<CmAttribute> refreshCmtsContactedCmList(Long cmcId);

    /**
     * 实时查询CM的状态信息
     * 
     * @param cmIp
     *            String
     * @return CmStatus
     */
    CmStatus showCmStatus(String cmIp, Long cmId);

    /**
     * 获取CM上cpe的信息列表
     * 
     * @param cmId
     *            Long
     * @return List<CpeAttribute>
     */
    List<CpeAttribute> getCpeListByCmId(Long cmId);

    /**
     * 获取关联的cm数
     * 
     * @param cmId
     *            Long
     * @return String
     */
    String getRealIpNum(Long cmId);

    /**
     * 最多允许的IP地址数
     * 
     * @param cmId
     * @return
     */
    String getCpeMaxIpNum(Long cmId);

    /**
     * 通过条件查询不同状态的CM数
     * 
     * @param map
     *            entityId，PonID，upchannelId,DownChannelId,status
     * @return Long
     */
    Long getCmNumByStatus(Map<String, Object> map);

    /**
     * @author YangYi 2013-10-18 查询CMTS下不同状态的CM数量
     * 
     * @param map
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
     * 获取CMC关联的CM列表
     * 
     * @param cmcId
     *            Long CMC ID
     * @param start
     *            分页start
     * @param limit
     *            分页limit
     * @return List<CmAttribute> CM设备信息列表
     */
    List<CmAttribute> getCmListByCmcId(Long cmcId, Integer start, Integer limit, UserContext uc);

    /**
     * 查询CM拓扑信息
     * 
     * @param cmId
     *            Long CM ID
     * @return TopologyInfo
     */
    CmTopologyInfo getTopologyInfo(Long cmId);

    /**
     * 重启CM
     * 
     * @param cmId
     *            Long CM ID
     * @return 0：重启成功 1：CM不在设备上或CM已离线 2：Ping超时3：SNMP不可达
     */
    Integer resetCm(Long cmId);

    /**
     * 获取所在OLT下的CM列表总数
     * 
     * @param cmId
     *            Long CM ID
     * @return Long 列表总数
     */
    Long getCmListByOltCount(Long cmId);

    /**
     * 获取所在PON口下的CM列表
     * 
     * @param cmId
     *            Long CM ID
     * @param start
     *            分页start
     * @param limit
     *            分页limit
     * @return List<CmAttribute> CM基本属性列表
     */
    List<CmAttribute> getCmListByPon(Long cmId, Integer start, Integer limit);

    /**
     * 获取所在PON口下的CM列表总数
     * 
     * @param cmId
     *            Long CM ID
     * @return Long
     */
    Long getCmListByPonCount(Long cmId);

    /**
     * 获取所在OLT下的CM列表
     * 
     * @param cmId
     *            Long CM ID
     * @param start
     *            分页start
     * @param limit
     *            分页limit
     * @return List<CmAttribute> CM基本属性列表
     */
    List<CmAttribute> getCmListByOlt(Long cmId, Integer start, Integer limit);

    /**
     * 获取所在CMC下的CM列表总数
     * 
     * @param cmId
     *            Long CM ID
     * @return Long CM列表数
     */
    Long getCmListByCmcCount(Long cmId);

    /**
     * 获取所在CMC下的CM列表
     * 
     * @param cmId
     *            Long CM ID
     * @param start
     *            分页start
     * @param limit
     *            分页limit
     * @return List<CmAttribute> CM基本属性
     */
    List<CmAttribute> getCmListByCmc(Long cmId, Integer start, Integer limit);

    /**
     * 获取所在上行通道下的CM列表
     * 
     * @param cmId
     *            Long CM ID
     * @param start
     *            分页start
     * @param limit
     *            分页limit
     * @return List<CmAttribute> CM基本属性
     */
    List<CmAttribute> getCmListByUpPortId(Long cmId, Integer start, Integer limit);

    /**
     * 获取所在上行通道下的CM列表总数
     * 
     * @param cmId
     *            Long CM ID
     * @return Long CM列表总数
     */
    Long getCmListByUpPortIdCount(Long cmId);

    /**
     * 获取所在下行通道下的CM列表
     * 
     * @param cmId
     *            Long CM ID
     * @param start
     *            分页start
     * @param limit
     *            分页limit
     * @return List<CmAttribute> CM基本属性列表
     */
    List<CmAttribute> getCmListByDownPortId(Long cmId, Integer start, Integer limit);

    /**
     * 获取所在下行通道下的CM列表总数
     * 
     * @param cmId
     *            Long CM ID
     * @return Long
     */
    Long getCmListByDownPortIdCount(Long cmId);

    /**
     * 刷新单条CM信息
     * 
     * @modifier huangdongsheng 修改入参，将cmcId与cmMac修改为cmId,使用cmId来唯一标示CM 修改方法体，将A、B的处理统一
     * @param cmId
     *            Long
     * @return CmAttribute
     */
    CmAttribute refreshCmInfo(Long cmId);

    /**
     * 刷新某个CCMTS上的CM信息
     * 
     * @param cmcId
     *            Long
     */
    void refreshCmOnCcmtsInfo(Long cmcId);

    /**
     * 根据CM ID获取cm基本属性
     * 
     * @param cmId
     *            Long
     * @return CmAttribute
     */
    CmAttribute getCmAttributeByCmId(Long cmId);

    /**
     * 根据cm mac地址获取cm属性
     * 
     * @param macs
     * @return
     */
    List<CmAttribute> queryCmListByCmMacs(List<Long> macs, Integer start, Integer limit);

    /**
     * 检测CM是否可以ping通
     * 
     * @param ip
     * @return
     */
    boolean checkCmReachable(String ip);

    /**
     * 检测CM的SNMP是否访问
     * 
     * @param ip
     * @param readCommunity
     * @param writeCommunity
     * @return
     */
    boolean checkCmSnmp(String ip, String readCommunity, String writeCommunity);

    /**
     * 获取CM下CPE信息
     * 
     * @param cmId
     * @return
     */
    List<CmCpe> getCmCpeList(Long cmId);

    /**
     * 获取CM大客户IP信息
     * 
     * @param cmId
     * @return
     */
    List<CmStaticIp> getCmStaticIpList(Long cmId);

    /**
     * 获取CM上下线行为信息
     * 
     * @param map
     * @return
     */
    List<CmAct> getCmActionInfo(Map<String, Object> map);

    /**
     * 获取Cpe上下线行为信息
     * 
     * @return
     */
    List<CpeAct> getCpeActionInfoByCmMac(Map<String, Object> map);

    /**
     * 清除CPE信息
     * 
     * @param cmId
     * @param cpeMac
     */
    void clearCpeInfo(Long cmId, String cpeMac);

    /**
     * 
     * 
     * @param cmId
     * @return
     */
    Long getCmcIdByCmId(Long cmId);

    /**
     * 获得CM采集模式，1为直接IP查询，2为使用REMOTEQuery查询
     * 
     * @return
     */
    Integer getCmQueryMode();

    /**
     * 将CM实时信号采集结果存入数据库,cmcpe查询页面查询后更新
     * 
     * @param cmAttribute
     */
    public void saveCmSignal(CmStatus cmStatus);

    /**
     * 刷新单个CM的相关属性，保持数据一致性
     * 
     * @param cmAttribute
     */
    void refreshSingleCmAttribute(CmAttribute cmAttribute);

    void updateCmAttribute(Long entityId, String cmmac, Integer status);

    /**
     * 根据CMID 获取 previous state
     * 
     * @param cmId
     * @return
     */
    CmAttribute getPreviousStateById(Long cmId);

    List<Cm3Signal> getUpChannelSignalByCmId(Long cmId);

    List<Cm3Signal> getDownChannelSignalByCmId(Long cmId);

    Integer clearSingleCM(Long cmId);
    
    /**
     * 批量删除已清除的cm
     * @param
     * @return void
     */
    void deleteCmCleared(List<Long> cmId);
    
    /**
     * 删除单个已清除的cm
     * @param
     * @return void
     */
    void deleteCmClearedOne(Long cmId);

}