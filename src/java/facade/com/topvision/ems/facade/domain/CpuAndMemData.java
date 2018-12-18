/***********************************************************************
 * $Id: CpuAndMemData.java,v1.0 2015-6-24 上午11:16:50 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * @author flack
 * @created @2015-6-24-上午11:16:50
 *
 */
public class CpuAndMemData implements Serializable {
    private static final long serialVersionUID = 8225547517384917952L;

    private Map<String, String> nodeMap;
    private Map<String, String> resultMap;
    private Map<String, Double> computedMap;

    public Map<String, String> getNodeMap() {
        return nodeMap;
    }

    public void setNodeMap(Map<String, String> nodeMap) {
        this.nodeMap = nodeMap;
    }

    public Map<String, String> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, String> resultMap) {
        this.resultMap = resultMap;
    }

    public Map<String, Double> getComputedMap() {
        return computedMap;
    }

    public void setComputedMap(Map<String, Double> computedMap) {
        this.computedMap = computedMap;
    }

}
