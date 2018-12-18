package com.topvision.ems.fault.dao;

import com.topvision.ems.fault.domain.ActionType;
import com.topvision.framework.dao.BaseEntityDao;

public interface ActionTypeDao extends BaseEntityDao<ActionType> {

    /**
     * 通过名称获得动作类型
     * 
     * @param name
     * @return ActionType
     */
    ActionType getActionTypeByName(String name);
}
