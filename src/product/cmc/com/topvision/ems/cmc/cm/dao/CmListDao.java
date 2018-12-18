package com.topvision.ems.cmc.cm.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.cm.domain.CcmtsLocation;
import com.topvision.ems.cmc.cm.domain.Cm3Signal;
import com.topvision.ems.cmc.cm.domain.CmLocation;
import com.topvision.ems.cmc.cm.domain.CmSignal;
import com.topvision.ems.cmc.cm.domain.CmTopo;
import com.topvision.ems.cmc.cm.domain.OltCcmtsRela;
import com.topvision.ems.cmc.cm.domain.OltLocation;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmPartialSvcState;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

public interface CmListDao extends BaseEntityDao<Entity> {

    /**
     * 根据设备type加载设备名称-id
     * 
     * @param typeId
     * @return
     */
    List<Map<String, String>> loadDeviceListByType(Long typeId);

    /**
     * 根据设备typeId加载设备名称-id
     * 
     * @param typeId
     * @return
     */
    List<Map<String, String>> loadDeviceListByTypeId(Long typeId);

    /**
     * 加载指定OLT的指定PON口下的CCMTS列表
     * 
     * @param entityId
     * @param ponId
     * @return
     */
    List<Map<String, String>> loadCcmtsOfPon(Long entityId, Long ponId);

    /**
     * 获取CMC/CMTS指定信道的ifDescr
     * 
     * @param cmcId
     * @param channelIndex
     * @return
     */
    String selectCmcPortIfDescr(Long cmcId, Long channelIndex);

    /**
     * 获取CM的拓扑信息
     * 
     * @param cmcId
     * @return
     */
    List<CmTopo> selectCmTopos(Long cmcId);

    /**
     * 获取CM上联cmcId
     * 
     * @param cmId
     * @return
     */
    Long selectCmcIdByCmId(Long cmId);

    /**
     * 获取OLT的entityId
     * 
     * @param cmcId
     * @return
     */
    Long selectOltIdByCmcId(Long cmcId);

    /**
     * 获取cmc的onuPonIndex
     * 
     * @param oltEntityId
     * @param cmcId
     * @return
     */
    Long selectOnuIndex(Long oltEntityId, Long cmcId);

    /**
     * 获取cmc上联的ponindex
     * 
     * @param oltEntityId
     * @param cmcId
     * @return
     */
    Long selectPonIndex(Long oltEntityId, Long cmcId);

    /**
     * 获取OLT location
     * 
     * @param oltEntityId
     * @return
     */
    OltLocation selectOltLocation(Long oltEntityId);

    /**
     * 获取OLT pon口流量
     * 
     * @param oltEntityId
     * @param ponIndex
     * @return
     */
    Double selectPonOutSpeed(Long oltEntityId, Long ponIndex);

    /**
     * 获取onupon的光功率
     * 
     * @param cmcId
     * @param onuPonIndex
     * @return
     */
    OltCcmtsRela selectOltCcmtsRelaForOnuPon(Long cmcId, Long onuPonIndex);

    /**
     * 获取pon的光功率
     * 
     * @param oltEntityId
     * @param ponIndex
     * @return
     */
    OltCcmtsRela selectOltCcmtsRelaForPon(Long oltEntityId, Long ponIndex);

    /**
     * 获取OLT的最高告警级别
     * 
     * @param oltEntityId
     * @return
     */
    Integer selectMaxAlarmLevel(Long oltEntityId);

    /**
     * 获取cmc定位信息
     * 
     * @param cmcId
     * @param cmId
     * @return
     */
    CcmtsLocation getCcmtsLocation(Long cmcId, Long cmId);

    /**
     * 获取CC的最高告警级别
     * 
     * @param cmcId
     * @param mac
     * @return
     */
    Integer getCcmtsMaxAlarmLevel(Long cmcId, String mac);

    /**
     * 获取CC和CM关联信息中CcmtsOutPower
     * 
     * @param cmcId
     * @param cmId
     * @return
     */
    Long selectCcmtsOutPower(Long cmcId, Long cmId);

    /**
     * 获取CC和CM关联信息中CcmtsInPower
     * 
     * @param cmId
     * @return
     */
    Long selectCcmtsInPower(Long cmId);

    /**
     * 获取cmsigal
     * 
     * @param cmId
     * @return
     */
    CmSignal selectCmsignal(Long cmId);

    /**
     * 获取cm定位信息
     * 
     * @param cmId
     * @return
     */
    CmLocation selectCmLocation(Long cmId);

    /**
     * 获取CPE NUM
     * 
     * @param cmId
     * @return
     */
    Long selectCpeNum(Long cmId);

    /**
     * 获取cm最高告警等级
     * 
     * @param ip
     * @return
     */
    Integer getCmMaxAlarmLevel(String ip);

    /**
     * 通过cmId获取单条CM属性
     * 
     * @return
     */
    CmAttribute selectOneCmByCmId(Long cmId);

    /**
     * 获取cmts的状态
     * 
     * @param cmcId
     * @return
     */
    Integer getCmtsStateById(Long cmcId);

    /**
     * 根据CMC的id去获取对应CCMTS下的CM列表
     * 
     * @param queryMap
     * @return
     */
    List<CmAttribute> getCmByCmcId(Map<String, Object> queryMap);

    /**
     * 根据CMC的id去获取对应CCMTS下对应条件下的的CM总数
     * 
     * @param queryMap
     * @return
     */
    Integer getCmNumByCmcId(Map<String, Object> queryMap);

    List<CmAttribute> selectCmList(Map<String, Object> queryMap);

    Integer selectCmListNum(Map<String, Object> queryMap);

    List<Cm3Signal> getCmSignalByCmIds(List<Long> cmIds);

    /**
     * 更新entityId设备下的partial service的信息
     * @param entityId
     * @param data
     */
    void updateCmPartialSvcState(Long entityId, final List<CmPartialSvcState> data);

}