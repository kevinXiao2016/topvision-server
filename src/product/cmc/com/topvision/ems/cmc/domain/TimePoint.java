/**
 * 
 */
package com.topvision.ems.cmc.domain;

import com.topvision.framework.constants.Symbol;

/**
 * @author dosion
 * 
 */
public class TimePoint {
    protected int y;
    protected long x;

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TimePoint [y=");
        builder.append(y);
        builder.append(", x=");
        builder.append(x);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
    
}
