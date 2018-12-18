package com.topvision.ems.fault.service;

import java.util.List;

import com.topvision.ems.fault.domain.ActionType;
import com.topvision.ems.fault.domain.Level;
import com.topvision.framework.service.Service;
import com.topvision.platform.domain.Action;

import net.sf.json.JSONArray;

public interface FaultService extends Service {

    /**
     * 批量删除动作
     * 
     * @param actionIds
     */
    void deleteActions(List<Long> actionIds);

    /**
     * 通过动作Id获得动作对象
     * 
     * @param actionId
     * @return Action
     */
    Action getActionById(Long actionId);

    /**
     * 获得动作列表
     * 
     * @return List<Action>
     */
    List<Action> getActions();

    /**
     * 通过名称获得动作类型
     * 
     * @param name
     * @return ActionType
     */
    ActionType getActionTypeByName(String name);

    /**
     * 获得所有动作类型
     * 
     * @return List<ActionType>
     */
    List<ActionType> getActionTypes();

    /**
     * 获取所有的告警级别.
     * 
     * @return List<Level>
     */
    List<Level> getAllAlertLevel();

    /**
     * 获得级别对象
     * 
     * @param level
     * @return Level
     */
    Level getLevel(Byte level);

    /**
     * 获得级别名称
     * 
     * @param level
     * @return String
     */
    String getLevelName(Byte level);

    /**
     * 插入动作
     * 
     * @param action
     */
    void insertAction(Action action);

    /**
     * 获得动作级别json列表
     * 
     * @return JSONArray
     * @throws Exception
     */
    JSONArray loadJSONAlertLevel() throws Exception;

    /**
     * 更新动作
     * 
     * @param action
     */
    void updateAction(Action action);

    /**
     * 更新动作状态
     * 
     * @param actionIds
     * @param enable
     */
    void updateActionStatus(List<Long> actionIds, Boolean enable);

    /**
     * 更新动作类型
     * 
     * @param type
     */
    void updateActionType(ActionType type);

    /**
     * 判断同一个动作类型下是否存在同名动作
     * 
     * @param actionType
     * @param name
     * @return
     */
    boolean existActionName(int actionType, String name, Long actionId);
}
