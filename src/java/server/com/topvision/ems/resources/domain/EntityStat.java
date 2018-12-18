/**
 * 
 */
package com.topvision.ems.resources.domain;

import com.topvision.framework.domain.BaseEntity;

/**
 * 设备统计实体.
 * 
 * @author niejun
 * 
 */
public class EntityStat extends BaseEntity {

    private static final long serialVersionUID = -4033299882261303418L;

    private long total = 0;

    private long countSnmpSupport = 0;

    private long countAgentInstalled = 0;

    public long getCountAgentInstalled() {
        return countAgentInstalled;
    }

    public long getCountSnmpSupport() {
        return countSnmpSupport;
    }

    public long getTotal() {
        return total;
    }

    public void setCountAgentInstalled(long countAgentInstalled) {
        this.countAgentInstalled = countAgentInstalled;
    }

    public void setCountSnmpSupport(long countSnmpSupport) {
        this.countSnmpSupport = countSnmpSupport;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}