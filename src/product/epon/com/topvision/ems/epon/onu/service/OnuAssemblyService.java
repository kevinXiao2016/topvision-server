package com.topvision.ems.epon.onu.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OnuInfo;
import com.topvision.ems.epon.onu.domain.UniPort;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.framework.service.Service;

/**
 * 将原OnuDeviceService中的方法移动到这里，原OnuServiceService用来做ONU设备列表service
 * 
 * @author w1992wishes
 * @created @2017年12月22日-上午9:44:21
 *
 */
public interface OnuAssemblyService extends Service {

    /**
     * 根据ONUID获取上联PON口的光信息
     * 
     * @param onuId
     * @return
     */
    OltPonOptical getOltOnuPonOptical(Long onuId);

    /**
     * 加载UNI端口列表
     * 
     * @param onuId
     * @return
     */
    List<UniPort> loadUniList(Long onuId);

    /**
     * 查询ONU列表
     * 
     * @param queryMap
     * @return
     */
    List<OnuInfo> queryForOnuList(Map<String, Object> queryMap);

    /**
     * 刷新ONU四个列表的信息，包括onu设备信息，链路信息，业务信息
     * 
     * @param entityId
     * @param onuId
     * @param onuIndex
     */
    void refreshOnuInfo(Long entityId, Long onuId, Long onuIndex);

    /**
     * 刷新ONU信号质量等
     * 
     * @param entityId
     * @param onuId
     * @param onuIndex
     */
    void refreshOnuQuality(Long entityId, Long onuId, Long onuIndex);

    /**
     * 刷新ONU的UNI信息
     * 
     * @param onuId
     */
    void refreshOnuUniInfo(Long onuId);

    /**
     * 获取所有ONU告警
     * 
     * @return
     */
    List<AlertType> getOnuAlertTypes();

    /**
     * 查询ONU告警列表
     * 
     * @param map
     * @return
     */
    List<Alert> getOnuAlertList(Map<String, Object> map);

    /**
     * 查询ONU告警列表总数
     * 
     * @param map
     * @return
     */
    int getOnuAlertListNum(Map<String, Object> map);

    /**
     * 查询ONU历史告警列表
     * 
     * @param map
     * @return
     */
    List<HistoryAlert> getOnuHistoryAlertList(Map<String, Object> map);

    /**
     * 查询ONU历史告警列表总数
     * 
     * @param map
     * @return
     */
    int getOnuHistoryAlertListNum(Map<String, Object> map);

    /**
     * 查询ONU总数
     * 
     * @param queryMap
     * @return
     */
    int queryForOnuCount(Map<String, Object> queryMap);

    /**
     * 刷新ONU光功率信息
     * 
     * @param onuIdList
     * @param jConnectedId
     * @param sessionId
     */
    void refreshOnuOptical(List<String> onuIdList, String jConnectedId, String sessionId);

    /**
     * 查询ONU和CATV 24h内最低收光功率
     * 
     * @param onuIdList
     * @param jConnectedId
     * @param sessionId
     */
    void queryOnuOpticalHistory(List<String> onuIdList, String jConnectedId, String sessionId);

    void saveOnuServerLevel(Long onuId, Integer onuLevel);

    Integer getOnuServerLevel(Long onuId);

    /**
     * 给ONU打上标签
     * 
     * @param onuId
     * @param tagId
     */
    void saveOnuTagRelation(Long onuId, Integer tagId);

}
