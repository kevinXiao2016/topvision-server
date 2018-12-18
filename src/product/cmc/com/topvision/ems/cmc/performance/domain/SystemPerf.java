/***********************************************************************
 * $Id: SystemPerf.java,v1.0 2012-7-11 下午02:15:40 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.List;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author dosion
 * @created @2012-7-11-下午02:15:40
 * 
 */
public class SystemPerf extends OperClass {
    private static final long serialVersionUID = -4923315755347924759L;
    private long cmcId;
    private List<Integer> ifIndexs;

    public SystemPerf() {
        super("systemPerfDBSaver", "systemPerfScheduler", "CC_SYSTEM");
    }

    protected SystemPerf(String perfService, String scheduler, String category) {
        super(perfService, scheduler, category);
    }

    @Override
    public long getIdentifyKey() {
        return cmcId;
    }

    @Override
    public void setIdentifyKey(Long identifyKey) {
        this.cmcId = identifyKey;
    }

    @Override
    public String[] makeOids() {
        oids = new String[3];
        oids[0] = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.4.1";
        oids[1] = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.5.1";
        oids[2] = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.6.1";
        //oids[3] = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.3." + ifIndexs.get(0);
        return oids;
    }

    @Override
    public PerformanceDomain[] makeObjects() {
        return null;
    }

    /**
     * @return the cmcId
     */
    public long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the ifIndexs
     */
    public List<Integer> getIfIndexs() {
        return ifIndexs;
    }

    /**
     * @param ifIndexs
     *            the ifIndexs to set
     */
    public void setIfIndexs(List<Integer> ifIndexs) {
        this.ifIndexs = ifIndexs;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SystemPerf [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", ifIndexs=");
        builder.append(ifIndexs);
        builder.append(", perfService=");
        builder.append(perfService);
        builder.append(", scheduler=");
        builder.append(scheduler);
        builder.append(", category=");
        builder.append(category);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

    @Override
    public boolean isTaskCancle() {
        return false;
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
    }

    @Override
    public void startUpTarget(String targetName, Object data) {
    }
}
