/***********************************************************************
 * $Id: CmMutipleInfo.java,v1.0 2013-7-17 下午5:27:13 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.util.List;

import com.topvision.ems.cmc.cm.domain.RealtimeCpe;
import com.topvision.ems.cmc.facade.domain.CmStatus;
import com.topvision.ems.cmc.performance.domain.CmAct;
import com.topvision.ems.cmc.performance.domain.CpeAct;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.fault.domain.Alert;

/**
 * @author loyal
 * @created @2013-7-17-下午5:27:13
 *
 */
public class CmMutipleInfo {
    private CmStatus cmStatus;
    private List<Alert> oltAlert;
    private List<Alert> cmcAlert;
    private List<CmAct> cmActs;
    private List<RealtimeCpe> cpeInfo;
    private List<CpeAct> cpeActs;
    private List<CmCpeAct> cmCpeActs;
    private CmFlap cmFlap;
    private CmCmcRunningInfo cmCmcRunningInfo;
    private CmOltRunningInfo cmOltRunningInfo;
    private Boolean isSupportRealtimeCpeQuery = false;

    public CmStatus getCmStatus() {
        return cmStatus;
    }

    public void setCmStatus(CmStatus cmStatus) {
        this.cmStatus = cmStatus;
    }

    public List<Alert> getOltAlert() {
        return oltAlert;
    }

    public void setOltAlert(List<Alert> oltAlert) {
        this.oltAlert = oltAlert;
    }

    public List<Alert> getCmcAlert() {
        return cmcAlert;
    }

    public void setCmcAlert(List<Alert> cmcAlert) {
        this.cmcAlert = cmcAlert;
    }

    public List<CmAct> getCmActs() {
        return cmActs;
    }

    public void setCmActs(List<CmAct> cmActs) {
        this.cmActs = cmActs;
    }

    public List<CpeAct> getCpeActs() {
        return cpeActs;
    }

    public void setCpeActs(List<CpeAct> cpeActs) {
        this.cpeActs = cpeActs;
    }

    public List<CmCpeAct> getCmCpeActs() {
        return cmCpeActs;
    }

    public void setCmCpeActs(List<CmCpeAct> cmCpeActs) {
        this.cmCpeActs = cmCpeActs;
    }

    /**
     * @return the cmFlap
     */
    public CmFlap getCmFlap() {
        return cmFlap;
    }

    /**
     * @param cmFlap the cmFlap to set
     */
    public void setCmFlap(CmFlap cmFlap) {
        this.cmFlap = cmFlap;
    }

    public CmCmcRunningInfo getCmCmcRunningInfo() {
        return cmCmcRunningInfo;
    }

    public void setCmCmcRunningInfo(CmCmcRunningInfo cmCmcRunningInfo) {
        this.cmCmcRunningInfo = cmCmcRunningInfo;
    }

    public CmOltRunningInfo getCmOltRunningInfo() {
        return cmOltRunningInfo;
    }

    public void setCmOltRunningInfo(CmOltRunningInfo cmOltRunningInfo) {
        this.cmOltRunningInfo = cmOltRunningInfo;
    }

    public List<RealtimeCpe> getCpeInfo() {
        return cpeInfo;
    }

    public void setCpeInfo(List<RealtimeCpe> cpeInfo) {
        this.cpeInfo = cpeInfo;
    }

    public Boolean getIsSupportRealtimeCpeQuery() {
        return isSupportRealtimeCpeQuery;
    }

    public void setIsSupportRealtimeCpeQuery(Boolean isSupportRealtimeCpeQuery) {
        this.isSupportRealtimeCpeQuery = isSupportRealtimeCpeQuery;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CmMutipleInfo [cmStatus=" + cmStatus + ", oltAlert=" + oltAlert + ", cmcAlert=" + cmcAlert
                + ", cmActs=" + cmActs + ", cpeActs=" + cpeActs + ", cmCpeActs=" + cmCpeActs + ", cmFlap=" + cmFlap
                + ", cmCmcRunningInfo=" + cmCmcRunningInfo + ", cmOltRunningInfo=" + cmOltRunningInfo + "]";
    }

}
