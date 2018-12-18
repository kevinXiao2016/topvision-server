package com.topvision.ems.network.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

public class EntityAvailableStat extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = -4589098398952116449L;
    private Boolean available;
    private Integer count;

    /**
     * @return the available
     */
    public Boolean getAvailable() {
        return available;
    }

    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param available
     *            the available to set
     */
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    /**
     * @param count
     *            the count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }
}
