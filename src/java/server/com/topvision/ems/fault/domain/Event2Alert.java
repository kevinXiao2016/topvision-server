package com.topvision.ems.fault.domain;

import com.topvision.framework.domain.BaseEntity;

public class Event2Alert extends BaseEntity {

    private static final long serialVersionUID = 1953713551467944780L;

    private Integer eventTypeId;
    private Integer alertTypeId;

    public Integer getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Integer eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public Integer getAlertTypeId() {
        return alertTypeId;
    }

    public void setAlertTypeId(Integer alertTypeId) {
        this.alertTypeId = alertTypeId;
    }

}
