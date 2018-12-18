/**
 * 
 */
package com.topvision.ems.cmc.domain;

/**
 * @author dosion
 * 
 */
public class FloatTimePoint {
    protected float y;
    protected long x;

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FloatTimePoint [y=");
        builder.append(y);
        builder.append(", x=");
        builder.append(x);
        return builder.toString();

    }

}
