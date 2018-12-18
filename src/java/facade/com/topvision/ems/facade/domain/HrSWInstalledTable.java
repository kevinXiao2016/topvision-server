/***********************************************************************
 * $Id: HrSWInstalledTable.java,v 1.1 Mar 20, 2009 10:51:44 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @Create Date Mar 20, 2009 10:51:44 PM
 * 
 * @author kelers
 * 
 */
public class HrSWInstalledTable extends AbstractProperty {
    private static final long serialVersionUID = -3575687057337269099L;
    @SnmpProperty(oid = ".1.3.6.1.2.1.25.6.3.1.1")
    private Integer hrSWInstalledIndex;
    @SnmpProperty(oid = ".1.3.6.1.2.1.25.6.3.1.2")
    private String hrSWInstalledName;
    @SnmpProperty(oid = ".1.3.6.1.2.1.25.6.3.1.3")
    private String hrSWInstalledID;
    @SnmpProperty(oid = ".1.3.6.1.2.1.25.6.3.1.4")
    private Byte hrSWInstalledType;
    @SnmpProperty(oid = ".1.3.6.1.2.1.25.6.3.1.5")
    private String hrSWInstalledDate;

    /**
     * @return the hrSWInstalledDate
     */
    public String getHrSWInstalledDate() {
        return hrSWInstalledDate;
    }

    /**
     * @return the hrSWInstalledID
     */
    public String getHrSWInstalledID() {
        return hrSWInstalledID;
    }

    /**
     * @return the hrSWInstalledIndex
     */
    public Integer getHrSWInstalledIndex() {
        return hrSWInstalledIndex;
    }

    /**
     * @return the hrSWInstalledName
     */
    public String getHrSWInstalledName() {
        return hrSWInstalledName;
    }

    /**
     * @return the hrSWInstalledType
     */
    public Byte getHrSWInstalledType() {
        return hrSWInstalledType;
    }

    /**
     * @param hrSWInstalledDate
     *            the hrSWInstalledDate to set
     */
    public void setHrSWInstalledDate(String hrSWInstalledDate) {
        this.hrSWInstalledDate = hrSWInstalledDate;
    }

    /**
     * @param hrSWInstalledID
     *            the hrSWInstalledID to set
     */
    public void setHrSWInstalledID(String hrSWInstalledID) {
        this.hrSWInstalledID = hrSWInstalledID;
    }

    /**
     * @param hrSWInstalledIndex
     *            the hrSWInstalledIndex to set
     */
    public void setHrSWInstalledIndex(Integer hrSWInstalledIndex) {
        this.hrSWInstalledIndex = hrSWInstalledIndex;
    }

    /**
     * @param hrSWInstalledName
     *            the hrSWInstalledName to set
     */
    public void setHrSWInstalledName(String hrSWInstalledName) {
        this.hrSWInstalledName = hrSWInstalledName;
    }

    /**
     * @param hrSWInstalledType
     *            the hrSWInstalledType to set
     */
    public void setHrSWInstalledType(Byte hrSWInstalledType) {
        this.hrSWInstalledType = hrSWInstalledType;
    }
}
