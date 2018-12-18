/***********************************************************************
 * $ NoisePerf.java,v1.0 2012-5-3 15:20:26 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

/**
 * @author jay
 * @created @2012-5-3-15:20:26
 */
public class NoisePerf extends OperClass {
    private static final long serialVersionUID = 7342180206202447869L;
    private long cmcId;
    private List<Long> ifIndexs = new ArrayList<Long>();

    public NoisePerf() {
        super("noisePerfDBSaver", "noisePerfScheduler", "noisePerf");
    }

    public String[] makeOids() {
        if (oids == null) {
            oids = new String[ifIndexs.size()];
            for (int i = 0; i < ifIndexs.size(); i++) {
                Long ifIndex = ifIndexs.get(i);
                oids[i] = "1.3.6.1.2.1.10.127.1.1.4.1.5." + ifIndex;
            }
        }
        return oids;
    }

    /**
     * 监视器设备实例的唯一key
     * 
     * @return identifyKey
     */
    public long getIdentifyKey() {
        return cmcId;
    }

    @Override
    public void setIdentifyKey(Long identifyKey) {
        this.cmcId = identifyKey;
        this.entityId = identifyKey;
    }

    public long getCmcId() {
        return cmcId;
    }

    public void setCmcId(long cmcId) {
        this.cmcId = cmcId;
    }

    public List<Long> getIfIndexs() {
        return ifIndexs;
    }

    public void setIfIndexs(List<Long> ifIndexs) {
        this.ifIndexs = ifIndexs;
    }

    @Override
    public PerformanceDomain[] makeObjects() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NoisePerf [entityId=");
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
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void shutdownTarget(String targetName, Object data) {
        List<Long> channelIndexList = (List<Long>) data;
        ifIndexs.removeAll(channelIndexList);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void startUpTarget(String targetName, Object data) {
        List<Long> channelIndexList = (List<Long>) data;
        ifIndexs.addAll(channelIndexList);
    }
}
