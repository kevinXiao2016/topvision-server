/***********************************************************************
 * $Id: CmcSniObjectInfo.java,v1.0 2012-2-13 下午04:54:17 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sni.facade.domain;

import java.io.Serializable;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author huqiao
 * @created @2012-2-13-下午04:54:17
 * 
 */
public class CmcSniObjectInfo implements Serializable {
    private static final long serialVersionUID = -2146772031352954321L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.5.1.0", writable = true, type = "Integer")
    private Integer topCcmtsSniEthInt;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.5.2.0", writable = true, type = "Integer")
    private Integer topCcmtsSniMainInt;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.5.3.0", writable = true, type = "Integer")
    private Integer topCcmtsSniBackupInt;

    /**
     * @return the topCcmtsSniEthInt
     */
    public Integer getTopCcmtsSniEthInt() {
        return topCcmtsSniEthInt;
    }

    /**
     * @param topCcmtsSniEthInt
     *            the topCcmtsSniEthInt to set
     */
    public void setTopCcmtsSniEthInt(Integer topCcmtsSniEthInt) {
        this.topCcmtsSniEthInt = topCcmtsSniEthInt;
    }

    /**
     * @return the topCcmtsSniMainInt
     */
    public Integer getTopCcmtsSniMainInt() {
        return topCcmtsSniMainInt;
    }

    /**
     * @param topCcmtsSniMainInt
     *            the topCcmtsSniMainInt to set
     */
    public void setTopCcmtsSniMainInt(Integer topCcmtsSniMainInt) {
        this.topCcmtsSniMainInt = topCcmtsSniMainInt;
    }

    /**
     * @return the topCcmtsSniBackupInt
     */
    public Integer getTopCcmtsSniBackupInt() {
        return topCcmtsSniBackupInt;
    }

    /**
     * @param topCcmtsSniBackupInt
     *            the topCcmtsSniBackupInt to set
     */
    public void setTopCcmtsSniBackupInt(Integer topCcmtsSniBackupInt) {
        this.topCcmtsSniBackupInt = topCcmtsSniBackupInt;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcSniObjectInfo [topCcmtsSniEthInt=");
        builder.append(topCcmtsSniEthInt);
        builder.append(", topCcmtsSniMainInt=");
        builder.append(topCcmtsSniMainInt);
        builder.append(", topCcmtsSniBackupInt=");
        builder.append(topCcmtsSniBackupInt);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
