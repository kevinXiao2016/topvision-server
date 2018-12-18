package com.topvision.ems.cmc.performance.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceDomain;
import com.topvision.ems.performance.domain.PerfTargetConstants;

/**
 * CM Flap定时采集器，作为server端和采集端远程调用时传递的对象，封装了采集定时器和数据保存service等对象。
 * 
 * @author lzs
 * 
 */
@Service("cmFlapMonitor")
@Scope("prototype")
public class CmFlapMonitor extends OperClass {
    private static final long serialVersionUID = 3040395509829449691L;
    private Boolean isNecessary = false;
    private Boolean isInsGrow = false;

    public CmFlapMonitor() {
        super("flapDBSaver", "flapScheduler", "cmFlapMonitor");
    }

    @Override
    public boolean isTaskCancle() {
        return !isInsGrow;
    }

    @Override
    public void shutdownTarget(String targetName, Object data) {
        if (targetName.equals(PerfTargetConstants.CMC_CMFLAP)) {
            this.isInsGrow = false;
        }
    }

    @Override
    public void startUpTarget(String targetName, Object data) {
        if (targetName.equals(PerfTargetConstants.CMC_CMFLAP)) {
            this.isInsGrow = true;
        }

    }

    @Override
    public long getIdentifyKey() {
        return entityId;
    }

    @Override
    public void setIdentifyKey(Long identifyKey) {
        this.entityId = identifyKey;
    }

    @Override
    public String[] makeOids() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PerformanceDomain[] makeObjects() {
        // TODO Auto-generated method stub
        return null;
    }

    public Boolean getIsNecessary() {
        return isNecessary;
    }

    public void setIsNecessary(Boolean isNecessary) {
        this.isNecessary = isNecessary;
    }

    /**
     * @return the isInsGrow
     */
    public Boolean getIsInsGrow() {
        return isInsGrow;
    }

    /**
     * @param isInsGrow the isInsGrow to set
     */
    public void setIsInsGrow(Boolean isInsGrow) {
        this.isInsGrow = isInsGrow;
    }
}
