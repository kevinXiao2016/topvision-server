package com.topvision.ems.fault.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertAboutUsers;
import com.topvision.ems.fault.domain.AlertStat;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.LevelStat;
import com.topvision.ems.network.domain.TopoFolderEx;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.framework.event.MyResultHandler;

public interface AlertDao extends BaseEntityDao<Alert> {
    /**
     * 获取当前告警列表（未清除的告警列表）
     * 
     * @param map
     * @return
     */
    List<Alert> selectCurrentAlert(Map<String, String> map);

    /**
     * 确认给定的当前告警.
     * 
     * @param alert
     */
    void confirmAlert(Alert alert);

    /**
     * 批量确认给定的当前告警.
     * 
     * @param alerts
     */
    void confirmAlert(List<Alert> alerts);

    /**
     * 清除给定的当前告警.
     * 
     * @param alert
     */
    void clearAlert(Alert alert);

    /**
     * 清除确认给定的当前告警.
     * 
     * @param alerts
     */
    void clearAlert(List<Alert> alerts);

    /**
     * 获取给定TreePath的TopoFolder的所有子文件夹的告警级别
     * 
     * @param path
     * @return List<TopoFolderEx>
     */
    List<TopoFolderEx> getMaxAlertInChildFolder(String path);

    /**
     * 返回所有有告警的entity的最大告警级别
     * 
     * @return Map<Long, Alert>
     */
    Map<Long, Alert> getMaxAlerts();

    /**
     * 获得设备最大级别告警信息
     * 
     * @param entityId
     * @return Alert
     */
    Alert getMaxLevelAlertByEntityId(Long entityId);

    /**
     * 获得当前的告警信息，进行分页设置
     * 
     * @param limit
     * @return List<Alert>
     */
    List<Alert> getRecentAlert(Integer limit);

    /**
     * 获得当前的告警信息
     * 
     * @param map
     * @return List<Alert>
     */
    List<Alert> getRecentAlert(Map<String, String> map);

    /**
     * 获得当前用户的当前告警信息
     * 
     * @param limit
     * @param handler
     */
    void handleRecentAlert(Integer limit, MyResultHandler handler);

    /**
     * 通过设备ID获得告警信息列表
     * 
     * @param entityId
     * @return List<Alert>
     */
    List<Alert> loadFloatingAlert(Long entityId);

    List<Alert> loadEntityAndSubAlert(Long entityId);

    /**
     * 恢复设备当前告警状态, 系统内部使用.
     * 
     * @param handler
     */
    void restoreEntityCurrentAlertState(MyResultHandler handler);

    /**
     * 按设备统计告警次数, 取前10
     * 
     * @return List<LevelStat>
     */
    List<AlertStat> statAlertByEntity(Map<String, String> map);

    /**
     * 按设备统计告警次数, 取前10
     * 
     * @return List<LevelStat>
     */
    List<AlertStat> topAlertByEntity(Map<String, Object> map);

    /**
     * 按照级别统计当前告警数量.
     * 
     * @param map
     * @return List<LevelStat>
     */
    List<LevelStat> statAlertByLevel(Map<String, String> map);

    /**
     * 按服务统计告警次数，取前10
     * 
     * @param map
     * @return List<LevelStat>
     */
    List<AlertStat> statAlertByServer(Map<String, String> map);

    /**
     * 获得设备可用时间
     * 
     * @param entityId
     * @param alertType
     * @param dataTime
     * @return
     */
    Alert getEntityAvailability(Long entityId, String alertType);

    /**
     * 获取设备创建时间
     * 
     * @param entityId
     * @return
     */
    Entity getEntityCreateTime(Long entityId);

    List<Alert> getRecentAlertByEntityIdAndLimit(int limit, Long entityId);

    /**
     * 获取告警信息
     * 
     * @param alertId
     *            告警ID
     * @param type
     *            发布版本类型（1:内部使用/2:广电标准/3:电信标准）
     * @return Alert
     */
    Alert getAlertByAlertId(Long alertId);

    /**
     * 获取所有告警
     * 
     * @param
     * @return
     */
    List<AlertType> getAllAlertType(String type);

    /**
     * 根据ip获取设备名称
     * 
     * @param host
     * @return
     */
    String getEntityNameByIp(String host);

    /**
     * 根据mac获取entityid
     * 
     * @param cmcMac
     * @return
     */
    Long queryEntityIdByMac(String cmcMac);

    /**
     * 根据entityId获取mac
     * 
     * @param entityId
     * @return
     */
    String queryMacByEntityId(Long entityId);

    List<AlertStat> statAlertListByEntity(Map<String, Object> map);

    Integer statAlertCountByEntity(Map<String, Object> map);

    /**
     * 通过CMC的mac和断电告警typeid来获取告警信息
     * 
     * @param mac
     * @param cmcPowerOffAlerttype
     * @return
     */
    List<Alert> getAlert(String mac, Long cmcPowerOffAlerttype);

    /**
     * 获取CMC的下线，断纤，断电告警
     * 
     * @param cmcId
     * @param alertTypeList
     * @return
     */
    List<Alert> getSpecialCmcAlertList(Long cmcId, List<Integer> alertTypeList);
    
    List<AlertAboutUsers> getAlertInfoOfUsersAndChoose(Map<String, Object> map);
    
    String selectAlertNameById(Long userId);
    /**
     * 获取用户关注告警
     * 
     * @return
     */
    List<Long> getConcernAlertTypes();
}
