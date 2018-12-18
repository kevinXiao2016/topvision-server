/***********************************************************************
 * $Id: ReportNode.java,v1.0 2014-6-16 下午2:56:19 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rod John
 * @created @2014-6-16-下午2:56:19
 * 
 */
public class ReportNode {
    public static final String ROOTKEY = "ReportRoot";
    public static final String ROOTVALUE = "ReportNode";
    private ReportNode parentNode;
    private Map<String, String> nodeMaps = new HashMap<String, String>();
    // Define Root Node For HashCode Function
    private String nodeKey = ROOTKEY;
    private String nodeValue = ROOTVALUE;
    private List<ReportNode> childrenNode;
    private ReportGroup reportGroup;

    /**
     * @return the parentNode
     */
    public ReportNode getParentNode() {
        return parentNode;
    }

    /**
     * @param parentNode
     *            the parentNode to set
     */
    public void setParentNode(ReportNode parentNode) {
        this.parentNode = parentNode;
    }

    /**
     * @return the nodeMaps
     */
    public Map<String, String> getNodeMaps() {
        return nodeMaps;
    }

    /**
     * @param nodeMaps
     *            the nodeMaps to set
     */
    public void setNodeMaps(Map<String, String> nodeMaps) {
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
     * @return the nodeValue
     */
    public String getNodeValue() {
        return nodeValue;
    }

    /**
     * @param nodeValue
     *            the nodeValue to set
     */
    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    /**
     * @return the childrenNode
     */
    public List<ReportNode> getChildrenNode() {
        return childrenNode;
    }

    /**
     * @param childrenNode
     *            the childrenNode to set
     */
    public void setChildrenNode(List<ReportNode> childrenNode) {
        this.childrenNode = childrenNode;
    }

    public void insertChildrenNode(ReportNode reportNode) {
        if (this.childrenNode == null) {
            childrenNode = new ArrayList<>();
        }
        childrenNode.add(reportNode);
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        ReportNode objNode = (ReportNode) obj;
        if (this.parentNode != null && objNode.parentNode != null) {
            if (this.nodeValue.equals(objNode.getNodeValue())) {
                return this.parentNode.equals(objNode.parentNode);
            } else {
                return false;
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int h = 17;
        if (this.parentNode != null) {
            h = 31 * h + parentNode.hashCode();
            h = 31 * h + nodeValue.hashCode();
        } else {
            h = 31 * h + nodeValue.hashCode();
        }
        return h;
    }

}
