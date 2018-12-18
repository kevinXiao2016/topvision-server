package com.topvision.ems.fault.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.dao.AlertFilterDao;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertFilter;
import com.topvision.ems.fault.service.AlertFilterService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.SystemPreferencesService;

@Service("alertFilterService")
public class AlertFilterServiceImpl extends BaseService implements AlertFilterService {
    private static final Logger logger = LoggerFactory.getLogger(AlertFilterService.class);
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private AlertFilterDao alertFilterDao;
    @Autowired
    protected EntityService entityService;
    @Autowired
    protected EntityTypeService entityTypeService;
    private List<AlertFilter> alertFilters = null;
    private byte minLevel = 1;
    private boolean filterActived = true;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertFilterService#addAlertFilter(com
     * .topvision.ems.fault .domain.AlertFilter)
     */
    @Override
    public void addAlertFilter(AlertFilter filter) {
        alertFilterDao.insertEntity(filter);
        alertFilters.add(filter);
    }

    @Override
    public AlertFilter getAlertFilter(Long filterId) {
        for (AlertFilter alertFilter : alertFilters) {
            if (alertFilter.getFilterId() != null && alertFilter.getFilterId().equals(filterId)) {
                return alertFilter;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertFilterService#filter(com.topvision
     * .ems.fault.domain. Alert)
     */
    @Override
    public Boolean filter(Alert alert) {
        if (alert.getLevelId() < minLevel) {
            return true;
        }
        if (!filterActived) {
            return false;
        }
        AlertFilter filter = null;
        // Modify by Rod
        // Modify by fanzidong, 新的过滤逻辑
        // 获取ONU服务等级
        Entity entity = entityService.getEntity(alert.getEntityId());
        Integer onuLevel = null;
        if (entityTypeService.isOnu(entity.getTypeId())) {
            onuLevel = entityService.getOnuLevel(entity.getEntityId());
        }
        String ip = alert.getIp();
        if (ip == null) {
            ip = alert.getHost().substring(0, alert.getHost().indexOf("["));
        }
        for (int i = 0; i < alertFilters.size(); i++) {
            filter = alertFilters.get(i);
            List<String> alertTypeIds = Arrays.asList(filter.getTypeIds().split(","));
            // 只有过滤器涉及该告警类型时，才进一步判断
            if (alertTypeIds.contains(String.valueOf(alert.getTypeId()))) {
                if (filter.getOnuLevel() != null && filter.getOnuLevel().equals(-1)) {
                    filter.setOnuLevel(null);
                }
                if (filter.getIp() != null && filter.getOnuLevel() != null) {
                    // IP过滤条件和ONU等级同时满足过滤
                    if (filter.getIp().equals(ip) && filter.getOnuLevel().equals(onuLevel)) {
                        return true;
                    }
                } else if (filter.getIp() != null && filter.getOnuLevel() == null) {
                    // 仅有IP过滤条件
                    if (filter.getIp().equals(ip)) {
                        return true;
                    }
                } else if (filter.getIp() == null && filter.getOnuLevel() != null) {
                    // 仅有ONU等级
                    if (filter.getOnuLevel().equals(onuLevel)) {
                        return true;
                    }
                } else if (filter.getIp() == null && filter.getOnuLevel() == null) {
                    // 直接过滤
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertFilterService#getAlertFilter()
     */
    @Override
    public List<AlertFilter> getAllAlertFilter() {
        return alertFilters;
    }

    @Override
    public List<AlertFilter> getAlertFilter(Map<String, Object> queryMap) {
        return alertFilterDao.getAlertFilter(queryMap);
    }

    @Override
    public Integer getAlertFilterCount(Map<String, Object> queryMap) {
        return alertFilterDao.getAlertFilterCount(queryMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertFilterService#getAlertFilterByType
     * (java.lang.Integer)
     */
    @Override
    public List<AlertFilter> getAlertFilterByType(Integer type) {
        return alertFilterDao.getAlertFilterByType(type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertFilterService#getMinLevel()
     */
    @Override
    public Byte getMinLevel() {
        return minLevel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertFilterService#getTypeIdsInFilter
     * (java.lang.Integer)
     */
    @Override
    public Set<Integer> getTypeIdsInFilter(Integer type) {
        Set<Integer> set = new HashSet<Integer>();
        int size = alertFilters.size();
        AlertFilter filter = null;
        for (int i = 0; i < size; i++) {
            filter = alertFilters.get(i);
            /*
             * if (filter.getType() == AlertFilter.TYPE_FILTER) { set.add(filter.getTypeId()); }
             */
        }
        return set;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        try {
            // 得到当前系统处理的最低级别告警
            SystemPreferences preference = systemPreferencesService.getSystemPreference("min.level");
            if (preference != null && preference.getValue() != null) {
                minLevel = Byte.parseByte(preference.getValue());
            }

            // 告警过滤是否有效
            preference = systemPreferencesService.getSystemPreference("alertFilter.actived");
            if (preference != null && preference.getValue() != null) {
                filterActived = "true".equals(preference.getValue());
            }

            // 得到当前系统的告警过滤器
            alertFilters = alertFilterDao.selectByMap(null);
            if (alertFilters == null) {
                alertFilters = new ArrayList<AlertFilter>();
            }
        } catch (DataAccessException e1) {
            if (logger.isDebugEnabled()) {
                logger.debug("AlertFilterServiceImpl initialize.", e1.getMessage());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertFilterService#isFilterActived()
     */
    @Override
    public Boolean isFilterActived() {
        return filterActived;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertFilterService#removeAlertFilter( java.util.List)
     */
    @Override
    public void removeAlertFilter(List<Long> filterIds) {
        // 同步更新缓存
        AlertFilter filter = null;
        for (int i = 0; i < filterIds.size(); i++) {
            for (int j = alertFilters.size() - 1; j >= 0; j--) {
                filter = alertFilters.get(j);
                if (filter.getFilterId().equals(filterIds.get(i))) {
                    alertFilters.remove(j);
                    break;
                }
            }
        }
        alertFilterDao.deleteByPrimaryKey(filterIds);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertFilterService#saveFilterActived( java.lang.Boolean)
     */
    @Override
    public void saveFilterActived(Boolean filterActived) {
        SystemPreferences preferences = new SystemPreferences();
        preferences.setName("alertFilter.actived");
        preferences.setModule("AlertFilter");
        preferences.setValue(String.valueOf(filterActived));
        systemPreferencesService.savePreferences(preferences);
        this.filterActived = filterActived;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertFilterService#saveMinLevel(java. lang.Byte)
     */
    @Override
    public void saveMinLevel(Byte level) {
        SystemPreferences preferences = new SystemPreferences();
        preferences.setName("min.level");
        preferences.setModule("AlertFilter");
        preferences.setValue(String.valueOf(level));
        systemPreferencesService.savePreferences(preferences);
        minLevel = level;
    }

    /**
     * 
     * @param alertFilterDao
     */
    public void setAlertFilterDao(AlertFilterDao alertFilterDao) {
        this.alertFilterDao = alertFilterDao;
    }

    /**
     * 
     * @param systemPreferencesService
     */
    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertFilterService#txSaveAlertTypeFilter
     * (java.util.List)
     */
    @Override
    public void txSaveAlertTypeFilter(List<AlertFilter> filters) {
        alertFilterDao.deleteAlertFilterByType(AlertFilter.TYPE_FILTER);
        alertFilterDao.insertEntity(filters);
        int size = alertFilters.size();
        AlertFilter filter = null;
        for (int i = size - 1; i >= 0; i--) {
            filter = alertFilters.get(i);
            // if (filter.getType() == AlertFilter.TYPE_FILTER) {
            // alertFilters.remove(i);
            // }
        }
        alertFilters.addAll(filters);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertFilterService#updateAlertFilter(
     * com.topvision.ems.fault .domain.AlertFilter)
     */
    @Override
    public void updateAlertFilter(AlertFilter filter) {
        // 更新数据库
        alertFilterDao.updateEntity(filter);
        AlertFilter temp = null;
        for (int i = 0; i < alertFilters.size(); i++) {
            temp = alertFilters.get(i);
            if (filter.getFilterId() == temp.getFilterId()) {
                // 同步更新缓存
                alertFilters.add(i, filter);
                break;
            }
        }
    }

}
