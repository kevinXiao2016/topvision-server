package com.topvision.ems.fault.dao;

import java.util.List;
import java.util.Map;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.domain.Action;

public interface ActionDao extends BaseEntityDao<Action> {

    /**
     * 批量删除动作
     * 
     * @param actionIds
     */
    void deleteActions(List<Long> actionIds);

    /**
     * 获得动作id和动作对象的HashMap
     * 
     * @return Map<Long, Action>
     */
    Map<Long, Action> getMapOfActions();

    /**
     * 批量更新动作状态
     * 
     * @param actionIds
     * @param enable
     */
    void updateActionStatus(List<Long> actionIds, Boolean enable);

    /**
     * @param actionType
     * @param name
     * @return
     */
    int existActionName(int actionType, String name, Long actionId);
}
