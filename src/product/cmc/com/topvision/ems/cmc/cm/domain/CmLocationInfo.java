/***********************************************************************
 * $Id: CmLocationInfo.java,v1.0 2013-11-2 上午10:41:57 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.domain;

import java.util.List;

/**
 * @author haojie
 * @created @2013-11-2-上午10:41:57
 * 
 */
public class CmLocationInfo {
    List<CmTopo> cmTopos;// 上联CCMTS的拓扑信息
    OltLocation oltLocation;// OLT部分
    OltCcmtsRela oltCcmtsRela;// OLT和CCMTS连接部分
    CcmtsLocation ccmtsLocation;// CCMTS(CMTS)部分
    CcmtsCmRela ccmtsCmRela;// CCMTS(CMTS)和CM连接部分
    CmLocation cmLocation;// CM部分

    public List<CmTopo> getCmTopos() {
        return cmTopos;
    }

    public void setCmTopos(List<CmTopo> cmTopos) {
        this.cmTopos = cmTopos;
    }

    public OltLocation getOltLocation() {
        return oltLocation;
    }

    public void setOltLocation(OltLocation oltLocation) {
        this.oltLocation = oltLocation;
    }

    public OltCcmtsRela getOltCcmtsRela() {
        return oltCcmtsRela;
    }

    public void setOltCcmtsRela(OltCcmtsRela oltCcmtsRela) {
        this.oltCcmtsRela = oltCcmtsRela;
    }

    public CcmtsLocation getCcmtsLocation() {
        return ccmtsLocation;
    }

    public void setCcmtsLocation(CcmtsLocation ccmtsLocation) {
        this.ccmtsLocation = ccmtsLocation;
    }

    public CcmtsCmRela getCcmtsCmRela() {
        return ccmtsCmRela;
    }

    public void setCcmtsCmRela(CcmtsCmRela ccmtsCmRela) {
        this.ccmtsCmRela = ccmtsCmRela;
    }

    public CmLocation getCmLocation() {
        return cmLocation;
    }

    public void setCmLocation(CmLocation cmLocation) {
        this.cmLocation = cmLocation;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmLocationInfo [cmTopos=");
        builder.append(cmTopos);
        builder.append(", oltLocation=");
        builder.append(oltLocation);
        builder.append(", oltCcmtsRela=");
        builder.append(oltCcmtsRela);
        builder.append(", ccmtsLocation=");
        builder.append(ccmtsLocation);
        builder.append(", ccmtsCmRela=");
        builder.append(ccmtsCmRela);
        builder.append(", cmLocation=");
        builder.append(cmLocation);
        builder.append("]");
        return builder.toString();
    }

}
