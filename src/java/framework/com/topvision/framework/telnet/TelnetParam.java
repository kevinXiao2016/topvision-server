/***********************************************************************
 * $Id: TelnetParam.java,v1.0 2013-11-23 下午5:25:39 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.telnet;

import com.topvision.framework.domain.DeviceParam;

/**
 * @author Victor
 * @created @2013-11-23-下午5:25:39
 *
 */
public class TelnetParam extends DeviceParam {
    private static final long serialVersionUID = -8026866765143249120L;
    private String enableUser;
    private String enablePasswd;
    private String superUser;
    private String superPasswd;

    /**
     * @return the enableUser
     */
    public String getEnableUser() {
        return enableUser;
    }

    /**
     * @param enableUser the enableUser to set
     */
    public void setEnableUser(String enableUser) {
        this.enableUser = enableUser;
    }

    /**
     * @return the enablePasswd
     */
    public String getEnablePasswd() {
        return enablePasswd;
    }

    /**
     * @param enablePasswd the enablePasswd to set
     */
    public void setEnablePasswd(String enablePasswd) {
        this.enablePasswd = enablePasswd;
    }

    /**
     * @return the superUser
     */
    public String getSuperUser() {
        return superUser;
    }

    /**
     * @param superUser the superUser to set
     */
    public void setSuperUser(String superUser) {
        this.superUser = superUser;
    }

    /**
     * @return the superPasswd
     */
    public String getSuperPasswd() {
        return superPasswd;
    }

    /**
     * @param superPasswd the superPasswd to set
     */
    public void setSuperPasswd(String superPasswd) {
        this.superPasswd = superPasswd;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TelnetParam [enableUser=");
        builder.append(enableUser);
        builder.append(", enablePasswd=");
        builder.append(enablePasswd);
        builder.append(", superUser=");
        builder.append(superUser);
        builder.append(", superPasswd=");
        builder.append(superPasswd);
        builder.append(", attributes()=");
        builder.append(attributes());
        builder.append(", getEntityId()=");
        builder.append(getEntityId());
        builder.append(", getIpAddress()=");
        builder.append(getIpAddress());
        builder.append(", getUsername()=");
        builder.append(getUsername());
        builder.append(", getPassword()=");
        builder.append(getPassword());
        builder.append("]");
        return builder.toString();
    }
}
