/***********************************************************************
 * $Id: ReportStructure.java,v1.0 2014-6-16 下午4:01:03 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rod John
 * @created @2014-6-16-下午4:01:03
 * 
 */
public class ReportStructure {

    private Map<String, Object> nodeMaps = new HashMap<String, Object>();
    private String nodeKey;
    private String nodeValues;
    private ReportGroup reportGroup;
    private ReportStructure childreNode;

    /**
     * @return the nodeMaps
     */
    public Map<String, Object> getNodeMaps() {
        return nodeMaps;
    }

    /**
     * @param nodeMaps
     *            the nodeMaps to set
     */
    public void setNodeMaps(Map<String, Object> nodeMaps) {
        this.nodeMaps = nodeMaps;
    }

    /**
     * @return the nodeKey
     */
    public String getNodeKey() {
        return nodeKey;
    }

    /**
     * @param nodeKey
     *            the nodeKey to set
     */
    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    /**
     * @return the nodeValues
     */
    public String getNodeValues() {
        return nodeValues;
    }

    /**
     * @param nodeValues
     *            the nodeValues to set
     */
    public void setNodeValues(String nodeValues) {
        this.nodeValues = nodeValues;
    }

    /**
     * @return the childreNode
     */
    public ReportStructure getChildreNode() {
        return childreNode;
    }

    /**
     * @param childreNode
     *            the childreNode to set
     */
    public void setChildreNode(ReportStructure childreNode) {
        this.childreNode = childreNode;
    }

    /**
     * @return the reportGroup
     */
    public ReportGroup getReportGroup() {
        return reportGroup;
    }

    /**
     * @param reportGroup
     *            the reportGroup to set
     */
    public void setReportGroup(ReportGroup reportGroup) {
        this.reportGroup = reportGroup;
    }

}
