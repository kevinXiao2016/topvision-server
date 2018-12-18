/***********************************************************************
 * $Id: CmServiceType.java,v1.0 2016年11月3日 上午10:09:12 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2016年11月3日-上午10:09:12
 *
 */
public class CmServiceType implements AliasesSuperType {
    private static final long serialVersionUID = 6464227770733035297L;

    private String fileName;
    private String serviceType;

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the serviceType
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType
     *            the serviceType to set
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmServiceType [fileName=");
        builder.append(fileName);
        builder.append(", serviceType=");
        builder.append(serviceType);
        builder.append("]");
        return builder.toString();
    }

}
