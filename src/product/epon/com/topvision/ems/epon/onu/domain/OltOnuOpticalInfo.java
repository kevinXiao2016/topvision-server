/***********************************************************************
 * $Id: OltOnuOpticalInfo.java,v1.0 2014-2-20 上午11:52:16 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-2-20-上午11:52:16
 *
 */
public class OltOnuOpticalInfo implements AliasesSuperType {
    private static final long serialVersionUID = -2179636151020691635L;
    //pon口接收与发送光功率
    private Double ponOpticalRev;
    private Double ponOpticalTrans;

    public OltOnuOpticalInfo() {

    }

    public OltOnuOpticalInfo(Double ponOpticalRev, Double ponOpticalTrans) {
        super();
        this.ponOpticalRev = ponOpticalRev;
        this.ponOpticalTrans = ponOpticalTrans;
    }

    public Double getPonOpticalRev() {
        return ponOpticalRev;
    }

    public void setPonOpticalRev(Double ponOpticalRev) {
        this.ponOpticalRev = ponOpticalRev;
    }

    public Double getPonOpticalTrans() {
        return ponOpticalTrans;
    }

    public void setPonOpticalTrans(Double ponOpticalTrans) {
        this.ponOpticalTrans = ponOpticalTrans;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltOnuOpticalInfo [ponOpticalRev=");
        builder.append(ponOpticalRev);
        builder.append(", ponOpticalTrans=");
        builder.append(ponOpticalTrans);
        builder.append("]");
        return builder.toString();
    }

}
