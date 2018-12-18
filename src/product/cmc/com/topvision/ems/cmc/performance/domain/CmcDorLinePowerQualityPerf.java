package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;

@Scope("prototype")
@Service("cmcDorLinePowerQualityPerf")
public class CmcDorLinePowerQualityPerf extends OperClass implements Serializable {

    private static final long serialVersionUID = -5410391486391702674L;
    private Long cmcId;
    private Long cmcIndex;
    private Boolean isNecessary = false;

    public CmcDorLinePowerQualityPerf() {
        super("cmcDorLinePowerQualityPerfSaver", "cmcDorLinePowerPerfScheduler", "cmcDorLinePowerQualityPerf");
    }

    @Override
    public boolean isTaskCancle() {
        return !isNecessary;
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
        isNecessary = false;
    }

    @Override
    public void startUpTarget(String targetName, Object data) {
        @SuppressWarnings("unchecked")
        List<Long> indexList = (List<Long>) data;
        if (indexList != null && indexList.size() > 0) {
            cmcIndex = indexList.get(0);
        }
        isNecessary = true;
    }

    @Override
    public long getIdentifyKey() {
        return cmcId;
    }

    @Override
    public void setIdentifyKey(Long identifyKey) {
        cmcId = identifyKey;
    }

    @Override
    public String[] makeOids() {
        return null;
    }

    @Override
    public PerformanceDomain[] makeObjects() {
        return null;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Boolean getIsNecessary() {
        return isNecessary;
    }

    public void setIsNecessary(Boolean isNecessary) {
        this.isNecessary = isNecessary;
    }

}
