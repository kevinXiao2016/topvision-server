/***********************************************************************
 * $Id: Group.java,v1.0 2014-7-24 下午2:21:03 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.domain;

/**
 * @author Rod John
 * @created @2014-7-24-下午2:21:03
 * 
 */
public class Group {
    private String compute;
    private String groupColumn;

    /**
     * 
     * 
     */
    public Group() {
    }

    /**
     * 
     * 
     * @param groupColumn
     */
    public Group(String groupColumn) {
        this.compute = "SUM";
        this.groupColumn = groupColumn;
    }

    /**
     * @param compute
     * @param groupColumn
     */
    public Group(String compute, String groupColumn) {
        this.compute = compute.toUpperCase();
        this.groupColumn = groupColumn;
    }

    /**
     * @return the compute
     */
    public String getCompute() {
        return compute;
    }

    /**
     * @param compute
     *            the compute to set
     */
    public void setCompute(String compute) {
        this.compute = compute;
    }

    /**
     * @return the groupColumn
     */
    public String getGroupColumn() {
        return groupColumn;
    }

    /**
     * @param groupColumn
     *            the groupColumn to set
     */
    public void setGroupColumn(String groupColumn) {
        this.groupColumn = groupColumn;
    }

}
