/***********************************************************************
 * $Id: CachedThreshholdAlertServiceImpl.java,v1.0 2016年8月13日 下午2:34:33 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.performance.domain.ThresholdAlertValue;
import com.topvision.ems.performance.service.CachedThresholdAlertService;
import com.topvision.ems.performance.service.PerfThresholdService;
import com.topvision.framework.service.BaseService;

/**
 * @author Bravin
 * @created @2016年8月13日-下午2:34:33
 *
 */
@Service
public class CachedThreshholdAlertServiceImpl extends BaseService implements CachedThresholdAlertService {
    private static Logger logger = LoggerFactory.getLogger(CachedThreshholdAlertServiceImpl.class);
    private Map<AlertValueKey, AlertValueAndLevel> CACHED_THRESHOLD_ALERT_VALUES = new ConcurrentHashMap<AlertValueKey, AlertValueAndLevel>();
    @Autowired
    private PerfThresholdService perfThresholdService;

    class AlertValueAndLevel {
        private Integer levelId;
        private Float perfValue;

        public AlertValueAndLevel(Integer levelId, Float perfValue) {
            this.levelId = levelId;
            this.perfValue = perfValue;
        }

        public Integer getLevelId() {
            return levelId;
        }

        public void setLevelId(Integer levelId) {
            this.levelId = levelId;
        }

        public Float getPerfValue() {
            return perfValue;
        }

        public void setPerfValue(Float perfValue) {
            this.perfValue = perfValue;
        }

    }

    class AlertValueKey {
        private Long entityId;
        private Integer alertEventId;
        private String source;

        public AlertValueKey(Long entityId, Integer alertEventId, String source) {
            this.entityId = entityId;
            this.alertEventId = alertEventId;
            this.source = source;
        }

        @Override
        public int hashCode() {
            return entityId.hashCode() + source.hashCode() + alertEventId.hashCode();
        };

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof AlertValueKey) {
                AlertValueKey key = (AlertValueKey) obj;
                return entityId.equals(key.getEntityId()) && alertEventId.equals(key.getAlertEventId())
                        && source.equals(key.getSource());
            }
            return false;
        };

        @Override
        public String toString() {
            return "AlertValueKey [entityId=" + entityId + ", alertEventId=" + alertEventId + ", source=" + source
                    + "]";
        }

        public Long getEntityId() {
            return entityId;
        }

        public void setEntityId(Long entityId) {
            this.entityId = entityId;
        }

        public Integer getAlertEventId() {
            return alertEventId;
        }

        public void setAlertEventId(Integer alertEventId) {
            this.alertEventId = alertEventId;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

    }

    @Override
    public void initialize() {
        List<ThresholdAlertValue> values = perfThresholdService.queryLastedAlertValue();
        for (ThresholdAlertValue value : values) {
            AlertValueAndLevel vl = new AlertValueAndLevel(value.getLevelId(), value.getPerfValue());
            AlertValueKey vk = generateKey(value.getEntityId(), value.getAlertEventId(), value.getSource());
            CACHED_THRESHOLD_ALERT_VALUES.put(vk, vl);
        }
    }

    private AlertValueKey generateKey(Long entityId, Integer alertEventId, String source) {
        // AlertValueKey 通过entityId， alertEventId， source三个值生成hashCode，不能为空,如果为空，日志记录，后续分析
        if (entityId == null || alertEventId == null || source == null) {
            logger.info(
                    "-----generateKey error, the params must not be null, the entityId={}, the alertEventId={}, the source={}-----",
                    entityId, alertEventId, source);
            throw new RuntimeException("generateKey error, the params must not be null");
        }
        return new AlertValueKey(entityId, alertEventId, source);
    }

    @Override
    public boolean alertKeyExist(Long entityId, Integer alertEventId, String source) {
        return CACHED_THRESHOLD_ALERT_VALUES.containsKey(generateKey(entityId, alertEventId, source));
    }

    @Override
    public Float takePrevAlertValue(Long entityId, Integer alertEventId, String source) {
        AlertValueKey keyString = generateKey(entityId, alertEventId, source);
        AlertValueAndLevel alertValueAndLevel = CACHED_THRESHOLD_ALERT_VALUES.get(keyString);
        return alertValueAndLevel == null ? null : alertValueAndLevel.getPerfValue();
    }

    @Override
    public Integer takePrevAlertLevelId(Long entityId, Integer alertEventId, String source) {
        AlertValueKey keyString = generateKey(entityId, alertEventId, source);
        AlertValueAndLevel alertValueAndLevel = CACHED_THRESHOLD_ALERT_VALUES.get(keyString);
        return alertValueAndLevel == null ? 0 : alertValueAndLevel.getLevelId();
    }

    @Override
    public void cacheThresholdAlertValue(Long entityId, Integer alertEventId, String source, Float perfValue,
            Integer levelId) {
        CACHED_THRESHOLD_ALERT_VALUES.put(generateKey(entityId, alertEventId, source), new AlertValueAndLevel(levelId,
                perfValue));
        perfThresholdService.insertOrUpdateAlertValue(entityId, source, alertEventId, perfValue, levelId);
    }

    @Override
    public void removeThresholdAlertValue(Long entityId, Integer alertEventId, String source) {
        AlertValueKey alertValueKey = generateKey(entityId, alertEventId, source);
        if (CACHED_THRESHOLD_ALERT_VALUES.containsKey(alertValueKey)) {
            CACHED_THRESHOLD_ALERT_VALUES.remove(alertValueKey);
            perfThresholdService.removeLastedAlertData(entityId, source, alertEventId);
        }
    }

    @Override
    public void removeThresholdAlertValue(List<Alert> list) {
        Iterator<Alert> iterator = list.iterator();
        while (iterator.hasNext()) {
            Alert alert = iterator.next();
            long entityId = alert.getEntityId();
            int alertEventId = alert.getLevelId();
            String source = alert.getSource();
            AlertValueKey alertValueKey = generateKey(entityId, alertEventId, source);
            if (CACHED_THRESHOLD_ALERT_VALUES.containsKey(alertValueKey)) {
                CACHED_THRESHOLD_ALERT_VALUES.remove(alertValueKey);
            } else {
                iterator.remove();
            }
        }
        perfThresholdService.removeLastedAlertData(list);
    }

}
