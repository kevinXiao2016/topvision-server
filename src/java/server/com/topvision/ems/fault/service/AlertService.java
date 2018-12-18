package com.topvision.ems.fault.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertAboutUsers;
import com.topvision.ems.fault.domain.AlertStat;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.LevelStat;
import com.topvision.ems.network.domain.TopoFolderEx;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.service.Service;

import net.sf.json.JSONArray;

public interface AlertService extends Service {

    /**
     * 通知远程告警接收器
     * 
     * @param alert
     * @param immediately
     */
    void fireAlert(Alert alert, Boolean immediately);

    /**
     * 获得告警类型的动作集合
     * 
     * @param typeId
     * @return List<Integer>
     */
    List<Long> getActionsOfAlertType(Integer typeId);

    /**
     * 通过主键Id获得告警信息
     * 
     * @param alertId
     * @return Alert
     */
    Alert getAlertById(Long alertId);

    /**
     * 获取给定TreePath的TopoFolder的所有子文件夹的告警级别.
     * 
     * @param path
     * @return List<TopoFolderEx>
     */
    List<TopoFolderEx> getAlertLevelInChildFolder(String path);

    /**
     * 通过告警类型Id获得告警类型
     * 
     * @param typeId
     * @return AlertType
     */
    AlertType getAlertTypeById(Integer typeId);

    /**
     * 获得所有告警类型
     * 
     * @return List<AlertType>
     */
    List<AlertType> getAllAlertTypes();

    /**
     * 获得用户相关的告警级联类型
     * 
     * @return
     */
    Set<Integer> getUserAlertTypeCollection(List<Integer> userAlertTypes);

    /**
     * 获取设备的当前告警, 即没有被清除的告警. 查询条件: 设备ID:entityId, 设备地址:ip, 设备名称:name, 确认状态:status, 起始时间:startTime,
     * 结束时间:endTime, 描述(模糊匹配):message
     * 
     * @param map
     * @return PageData
     */
    PageData<Alert> getEntityCurrentAlert(Long entityId, Page p, Map<String, String> map);

    /**
     * 获得所有告警id和告警对应的HashMap
     * 
     * @return Map<Integer, AlertType>
     */
    Map<Integer, AlertType> getMapOfAlertTypes();

    /**
     * 处理满足给定条件的当前告警.
     * 
     * @param handler
     * @param map
     */
    void handleCurrentAlert(MyResultHandler handler, Map<String, String> map);

    /**
     * 获得当前用户的当前告警信息
     * 
     * @param limit
     * @param handler
     */
    void handleRecentAlert(Integer limit, MyResultHandler handler);

    /**
     * 插入一条告警信息
     * 
     * @param alert
     */
    void insertAlert(Alert alert);

    /**
     * 获得设备告警，取记录前六条
     * 
     * @param entityId
     * @return List<Alert>
     */
    List<Alert> loadFloatingAlert(Long entityId);

    /**
     * 获得告警类型json格式数据
     * 
     * @return JSONArray
     * @throws Exception
     */
    JSONArray loadJSONAlertType() throws Exception;

    /**
     * 得到当前的告警列表.
     * 
     * @param p
     * @return PageData
     */
    PageData<Alert> queryCurrentAlert(Page p, Map<String, String> map);

    /**
     * 当系统重新启动的时候恢复设备的告警状态到设备快照中.
     * 
     * @param handler
     */
    void restoreEntityCurrentAlertState(MyResultHandler handler);

    /**
     * 保存告警类型相关参数
     * 
     * @param alertType
     * @param eventIds
     * @param actionIds
     */
    void saveAlertTypeParam(AlertType alertType, List<Long> eventIds, List<Long> actionIds);

    /**
     * 根据map信息获取符合条件的alert。
     * 
     * @param map
     * @return List<Alert>
     */
    List<Alert> selectByMap(Map<String, String> map);

    /**
     * 获取当前告警列表（未清除的告警列表）
     * 
     * @param map
     * @return
     */
    List<Alert> selectCurrentAlert(Map<String, String> map);

    /**
     * 统计设备当前的告警次数排行.
     * 
     * @param map
     * @return List<AlertStat>
     */
    List<AlertStat> statAlertByEntity(Map<String, Object> map);

    /**
     * 按级别统计当前告警的数量.
     * 
     * @param map
     * @return List<LevelStat>
     */
    List<LevelStat> statAlertByLevel(Map<String, String> map);

    /**
     * 按服务统计告警次数
     * 
     * @param map
     * @return List<AlertStat>
     */
    List<AlertStat> statAlertByServer(Map<String, String> map);

    /**
     * 系统自动清除给定的告警.
     * 
     * @param alert
     * @param clearMsg
     */
    void txClearAlert(Alert alert, String clearMsg);

    /**
     * 某用户清除给定的告警, 并返回该告警所在设备的最新告警状态.
     * 
     * @param alert
     * @param clearUser
     * @param clearMsg
     */
    Alert txClearAlert(Alert alert, String clearUser, String clearMsg);

    /**
     * 某用户清除给定的告警, 并返回当前这些告警所在设备的最新告警.
     * 
     * @param alertIds
     * @param clearUser
     * @param clearMsg
     */
    List<Alert> txClearAlert(List<Long> alertIds, String clearUser, String clearMsg);

    /**
     * 确认给定的告警.
     * 
     * @param alert
     */
    Alert txConfirmAlert(Alert alert, String confirmUser, String confirmMsg);

    /**
     * 批量确认给定的告警.
     * 
     * @param alertIds
     * @param confirmUser
     * @param confirmMsg
     */
    List<Alert> txConfirmAlert(List<Long> alertIds, String confirmUser, String confirmMsg);

    /**
     * 保存某个告警类型
     * 
     * @param typeId
     * @param active
     */
    void updateAlertType(Integer typeId, Byte level, Boolean active, Integer updateLevel, String alertTimes,
            Boolean smartUpdate, List<Long> actionIds, String note);

    /**
     * 更新告警内容
     * 
     * @param alert
     */
    void updateAlert(Alert alert);

    /**
     * 获得设备在统计时间段内连通总时长
     * 
     * @param entityId
     * @param alertType
     * @param dataTime
     * @return
     */
    Long getEntityAvailability(Long entityId, String alertType, int d);

    /**
     * 获得某个设备最新5条告警信息
     * 
     * @param limit
     * @param entityId
     * @return
     */
    List<Alert> getRecentAlertByEntityIdAndLimit(int limit, Long entityId);

    /**
     * 获取设备创建时间
     * 
     * @param entityId
     * @return
     */
    Long getEntityCreateTime(Long entityId);

    /**
     * 参数设置后需要更新
     */
    void reset();

    /**
     * 根据ip获取设备名称
     * 
     * @param host
     * @return
     */
    String getEntityNameByIp(String host);

    /**
     * 根据MAC获取entityId
     * 
     * @param cmcMac
     * @return
     */
    Long getEntityIdByMac(String cmcMac);

    /**
     * 根据entityId获取mac
     * 
     * @param entityId
     * @return
     */
    String getMacById(Long entityId);

    /**
     * 根据entityId和typeId获取某种告警信息
     * 
     * @param entityId
     * @param typeId
     * @return
     */
    Alert getEntityAlertByType(Long entityId, String typeId);
    
    /**
     * 获取用户的告警通知列表
     * @param
     * @return List<AlertAboutUsers>
     */
    List<AlertAboutUsers> getuserAlertList(Map<String, Object> map);

    List<AlertStat> statAlertListByEntity(Map<String, Object> map);

    Integer statAlertCountByEntity(Map<String, Object> map);

    /**
     * 更新用户选择的告警通知方式
     * @param
     * @return void
     */
    void updateAlertTypeOfUsers(Integer typeId, Byte level, Boolean active, Integer updateLevel, String alertTimes,
            Boolean smartUpdate, Long userId, List<Integer> userActionChoose, List<Long> actionIds,String note);

    /**
     * 获取绑定了某条告警id的用户动作
     * @param Integer id 告警TypeId
     * @return List<Long>
     */
    List<Integer> getUserAlertActions(Integer id,Long userId);

    /**
     * 获取某个用户的告警方式
     * @param 用户userId
     * @return List<AlertAboutUsers>
     */
    List<AlertAboutUsers> getOneUserActionCs(Long userId);

    /**
     * 更新用户的告警方式
     * @param 用户UuserId，告警方式
     * @return void
     */
    void updateUserActionCs(Long oneUserId, String isTrue);

    /**
     * 获取告警用户列表行数
     * @param
     * @return int
     */
    int getUserAlertListCount();

    /**
     * 获取某条告警相关的发送信息
     * @param 告警Id
     * @return List<AlertAboutUsers>
     */
    List<AlertAboutUsers> getSendingInfoOfUsers(Integer alertTypeId);

    /**
     * 获取用户关注告警id
     * 
     * @return
     */
    List<Long> getConcernAlertTypes();

}
