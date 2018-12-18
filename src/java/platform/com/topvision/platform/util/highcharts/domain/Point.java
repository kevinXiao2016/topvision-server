/***********************************************************************
 * $ Point.java,v1.0 2012-7-11 14:55:38 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util.highcharts.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2012-7-11-14:55:38
 */
public class Point implements Serializable, Comparable<Point>, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Double x;
    private Timestamp xTime;
    private Double y;
    private Integer tipStr;

    public Double getX() {
        if (x == null) {
            return (double) xTime.getTime();
        } else {
            return x;
        }
    }

    public void setX(Double x) {
        this.x = x;
    }

    //modify by loyal 处理取值为null的情况
    public Double getY() {
        if (y != null) {
            return y;
        } else {
            return -1d;
        }

    }

    public void setY(Double y) {
        this.y = y;
    }

    public Timestamp getXTime() {
        return xTime;
    }

    public void setXTime(Timestamp xTime) {
        this.xTime = xTime;
    }

    public Integer getTipStr() {
        return tipStr;
    }

    public void setTipStr(Integer tipStr) {
        this.tipStr = tipStr;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Point");
        sb.append("{x=").append(x);
        sb.append(", xTime=").append(xTime);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(Point o) {
        if (this.xTime.before(o.xTime)) {
            return -1;
        } else if (this.xTime.after(o.xTime)) {
            return 1;
        }
        return 0;
    }
}
