/***********************************************************************
 * $Id: TrapEvent.java,v1.0 2011-11-28 下午03:25:36 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.message;

import com.topvision.framework.snmp.Trap;
import com.topvision.platform.message.event.EmsEventObject;

/**
 * @author Victor
 * @created @2011-11-28-下午03:25:36
 * 
 */
public class TrapEvent extends EmsEventObject<TrapListener> {
    private static final long serialVersionUID = 3843061536533913272L;
    private Trap trap;
    private Integer code;
    private String source;
    private Boolean isClear;
    private Long entityId;
    private Long deviceIndex;

    /**
     * @param source
     */
    public TrapEvent(Object source) {
        super(source);
    }

    /**
     * @return the trap
     */
    public Trap getTrap() {
        return trap;
    }

    /**
     * @param trap
     *            the trap to set
     */
    public void setTrap(Trap trap) {
        this.trap = trap;
    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return the isClear
     */
    public Boolean getIsClear() {
        return isClear;
    }

    /**
     * @param isClear
     *            the isClear to set
     */
    public void setIsClear(Boolean isClear) {
        this.isClear = isClear;
    }

    /**
     * @return the source
     */
    @Override
    public String getSource() {
        return source;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

}
