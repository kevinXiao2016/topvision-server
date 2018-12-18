package com.topvision.ems.cm.cmpoll.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;

import java.io.Serializable;

/**
 * @author jay
 * @created 15-4-18.
 */
public class CmPollCmRemoteQuery implements Serializable {

    private static final long serialVersionUID = -8770204210975796096L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.4.1.1", index = true)
    private Long cmIndex; // CM的Index
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.9.4.1.2")
    private Integer cmRemoteQueryStatus; // CM remotequery 状态

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    public Integer getCmRemoteQueryStatus() {
        return cmRemoteQueryStatus;
    }

    public void setCmRemoteQueryStatus(Integer cmRemoteQueryStatus) {
        this.cmRemoteQueryStatus = cmRemoteQueryStatus;
    }

    @Override
    public String toString() {
        return "CmPollCmRemoteQuery{" +
                "cmIndex=" + cmIndex +
                ", cmRemoteQueryStatus=" + cmRemoteQueryStatus +
                '}';
    }
}
