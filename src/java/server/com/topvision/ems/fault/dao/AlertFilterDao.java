package com.topvision.ems.fault.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.fault.domain.AlertFilter;
import com.topvision.framework.dao.BaseEntityDao;

public interface AlertFilterDao extends BaseEntityDao<AlertFilter> {

    /**
     * 清除参数类型告警
     * 
     * @param type
     */
    void deleteAlertFilterByType(Integer type);

    /**
     * 获得参数类型告警信息
     * 
     * @param type
     * @return List<AlertFilter>
     */
    List<AlertFilter> getAlertFilterByType(Integer type);

    /**
     * 获取符合条件的告警过滤器
     * 
     * @param queryMap
     * @return
     */
    List<AlertFilter> getAlertFilter(Map<String, Object> queryMap);

    /**
     * 获取符合条件的告警过滤器的数量
     * 
     * @param queryMap
     * @return
     */
    Integer getAlertFilterCount(Map<String, Object> queryMap);

}
