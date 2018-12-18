package com.topvision.ems.fault.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertFilter;
import com.topvision.framework.service.Service;

public interface AlertFilterService extends Service {

    /**
     * 添加过滤的告警
     * 
     * @param filter
     */
    void addAlertFilter(AlertFilter filter);

    /**
     * 过滤给定的告警, 如果满足给定的条件, 该告警将被过滤.
     * 
     * @param alert
     * @return Boolean
     */
    Boolean filter(Alert alert);

    /**
     * 获取所有过滤的告警
     * 
     * @return List<AlertFilter>
     */
    List<AlertFilter> getAllAlertFilter();
    
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

    // TODO
    /**
     * 
     * 
     * @param type
     * @return List<AlertFilter>
     */
    List<AlertFilter> getAlertFilterByType(Integer type);

    // TODO
    /**
     * 
     * 
     * @return Byte
     */
    Byte getMinLevel();

    // TODO
    /**
     * 
     * 
     * @param type
     * @return Set<Integer>
     */
    Set<Integer> getTypeIdsInFilter(Integer type);

    // TODO
    /**
     * 
     * 
     * @return Boolean
     */
    Boolean isFilterActived();

    /**
     * 删除过滤的告警
     * 
     * @param filterIds
     */
    void removeAlertFilter(List<Long> filterIds);

    // TODO
    /**
     * 
     * 
     * @param active
     */
    void saveFilterActived(Boolean active);

    // TODO
    /**
     * 
     * @param level
     */
    void saveMinLevel(Byte level);

    // TODO
    /**
     * 
     * 
     * @param filters
     */
    void txSaveAlertTypeFilter(List<AlertFilter> filters);

    /**
     * 更新过滤的告警
     * 
     * @param filter
     */
    void updateAlertFilter(AlertFilter filter);

    /**
     * 根据filterId获取指定告警过滤器
     * @param filterId
     * @return
     */
    AlertFilter getAlertFilter(Long filterId);

}
