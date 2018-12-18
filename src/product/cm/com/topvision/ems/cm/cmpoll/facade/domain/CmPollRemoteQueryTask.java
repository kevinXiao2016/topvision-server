/***********************************************************************
 * $Id: CmPollRemoteQueryTask.java,v1.0 2015年3月13日 下午3:50:56 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.facade.domain;

import java.util.List;

/**
 * @author loyal
 * @created @2015年3月13日-下午3:50:56
 * 
 */
public class CmPollRemoteQueryTask extends CmPollTask {
    private static final long serialVersionUID = 7309997578006124621L;
    private Integer cmRemoteQueryStatus;
    private List<CmPoll3UsRemoteQuery> cm3UsRemoteQueryList;
    private List<CmPoll3DsRemoteQuery> cm3DsRemoteQueryList;

    public List<CmPoll3UsRemoteQuery> getCm3UsRemoteQueryList() {
        return cm3UsRemoteQueryList;
    }

    public void setCm3UsRemoteQueryList(List<CmPoll3UsRemoteQuery> cm3UsRemoteQueryList) {
        this.cm3UsRemoteQueryList = cm3UsRemoteQueryList;
    }

    public List<CmPoll3DsRemoteQuery> getCm3DsRemoteQueryList() {
        return cm3DsRemoteQueryList;
    }

    public void setCm3DsRemoteQueryList(List<CmPoll3DsRemoteQuery> cm3DsRemoteQueryList) {
        this.cm3DsRemoteQueryList = cm3DsRemoteQueryList;
    }

    public Integer getCmRemoteQueryStatus() {
        return cmRemoteQueryStatus;
    }

    public void setCmRemoteQueryStatus(Integer cmRemoteQueryStatus) {
        this.cmRemoteQueryStatus = cmRemoteQueryStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append("CmPollRemoteQueryTask [cm3UsRemoteQueryList=");
        builder.append(cm3UsRemoteQueryList);
        builder.append(", cm3DsRemoteQueryList=");
        builder.append(cm3DsRemoteQueryList);
        builder.append(", cmRemoteQueryStatus=");
        builder.append(cmRemoteQueryStatus);
        builder.append("]");
        return builder.toString();
    }

}
