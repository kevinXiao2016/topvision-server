/**
 * 
 */
package com.topvision.ems.network.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author kelers
 * 
 */
public class StateStat extends BaseEntity implements AliasesSuperType {
    private static final long serialVersionUID = -1795754272733089825L;
    private Integer state;
    private Integer count;

    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @return the state
     */
    public Integer getState() {
        return state;
    }

    /**
     * @param count
     *            the count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(Integer state) {
        this.state = state;
    }
}
