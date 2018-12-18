/***********************************************************************
 * $Id: Rack.java,v1.0 2011-9-26 上午09:02:13 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

/**
 * 机架
 * 
 * @author zhanglongyang
 * 
 */
public class Rack implements Serializable {
    private static final long serialVersionUID = -7854146222640428650L;
    private Integer rackNum;
    private Integer row;
    private Integer col;
    private String rackName;

    /**
     * @return the rackNum
     */
    public Integer getRackNum() {
        return rackNum;
    }

    /**
     * @param rackNum
     *            the rackNum to set
     */
    public void setRackNum(Integer rackNum) {
        this.rackNum = rackNum;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    /**
     * @return the rackName
     */
    public String getRackName() {
        return rackName;
    }

    /**
     * @param rackName
     *            the rackName to set
     */
    public void setRackName(String rackName) {
        this.rackName = rackName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Rack");
        sb.append("{col=").append(col);
        sb.append(", rackNum=").append(rackNum);
        sb.append(", row=").append(row);
        sb.append(", rackName='").append(rackName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
