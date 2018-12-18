package com.topvision.ems.cmc.cm.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.topvision.ems.cmc.cm.domain.Cm3Signal;
import com.topvision.ems.cmc.cm.domain.CmLocationInfo;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmPartialSvcState;
import com.topvision.framework.service.Service;
import com.topvision.platform.domain.UserContext;

/**
 * 
 * @author YangYi
 * @created @2013-10-30-下午8:28:55 从CmService拆分,CM级联列表
 * 
 */
public interface CmListService extends Service {

    /**
     * 根据指定的type值获取设备列表
     * 
     * @param typeId
     * @return (entityId, name, ip)
     */
    JSONArray loadDeviceListByType(Long typeId);

    /**
     * 根据指定的typeId值获取设备列表
     * 
     * @param typeId
     * @return (entityId, name, ip)
     */
    JSONArray loadDeviceListByTypeId(Long typeId);

    /**
     * 加载指定设备下指定PON口下的CCMTS列表
     * 
     * @param entityId
     * @param ponId
     * @return
     */
    JSONArray loadCcmtsOfPon(Long entityId, Long ponId);

    /**
     * 加载符合查询条件的CM列表
     * 
     * @param queryMap
     * @return
     */
    List<CmAttribute> getCmList(Map<String, Object> queryMap);

    /**
     * 加载符合查询条件的CM列表的总数
     * 
     * @param queryMap
     * @return
     */
    Integer getCmNum(Map<String, Object> queryMap);

    /**
     * 获取CM定位信息
     * 
     * @param cmId
     * @param cmcDeviceStyle
     * @return
     */
    CmLocationInfo getCmLocation(Long cmId, Long cmcDeviceStyle);

    /**
     * 重启CM，用dwr向前台推送
     * 
     * @param cmId
     */
    void restartCm(Long cmId, String dwrId);

    /**
     * 刷新单条CM数据
     * 
     * @param cmcId
     * @param statusIndex
     * @param cmMac
     * @return
     */
    JSONObject refreshCm(Long cmcId, Long cmId, Long statusIndex, String cmMac, Long cmcDeviceStyle, UserContext uc);

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

    /**
     * 刷新某个OLT下的某个CC下的CM列表
     * 
     * @param entityId
     * @param cmcId
     *            当cmcId是Null的时候表示刷新整台OLT下的Cm列表
     */
    void refreshCmList(Long entityId, Long cmcId);

    /**
     * 获取一批CM的信号质量，支持多信道
     * 
     * @param cmIds
     * @return
     */
    List<Cm3Signal> getCmSignalByCmIds(List<Long> cmIds);

    /**
     * 刷新partial service数据
     * 
     * @param entityId
     */
    void refreshCmPartialSvcState(Long entityId);

    /**
     * 存储partial service数据
     * 
     * @param entityId
     * @param partialData
     */
    void saveCmPartialSvcState(Long entityId, List<CmPartialSvcState> partialData);

    /**
     * 清除单个离线CM
     * @param
     * @return void
     */
    Integer clearSingleCm(Long cmId);
}
