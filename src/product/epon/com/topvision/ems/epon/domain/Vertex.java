/***********************************************************************
 * $Id: Vertex.java,v1.0 2011-9-26 下午01:17:34 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

/**
 * 机房拐角坐标
 * 
 * @author zhanglongyang
 * 
 */
public class Vertex implements Serializable {
    private static final long serialVersionUID = -445471800563591334L;
    private Integer x;
    private Integer y;

    /**
     * @return the x
     */
    public Integer getX() {
        return x;
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(Integer x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public Integer getY() {
        return y;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Vertex");
        sb.append("{x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }
}
