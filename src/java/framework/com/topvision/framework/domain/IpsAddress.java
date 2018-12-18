/***********************************************************************
 * $Id: IpsAddress.java,v1.0 2013-5-3 下午04:46:36 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.domain;

/**
 * @author Rod John
 * @created @2013-5-3-下午04:46:36
 * 
 */
public class IpsAddress extends BaseEntity {

    private static final long serialVersionUID = 3497336089453292045L;
    private String ipAddress;
    private byte[] ipByte;

    public IpsAddress(byte[] ip) {
        if (ip.length == 4) {
            ipByte = new byte[4];
            for (int i = 0; i < 4; i++) {
                ipByte[i] = ip[i];
            }
        }
    }

    public IpsAddress(String ipAddress) {
        String[] ipString = ipAddress.split("\\.");
        if (ipString.length == 4) {
            this.ipAddress = ipAddress;
            ipByte = new byte[4];
            for (int i = 0; i < 4; i++) {
                ipByte[i] = (byte) Integer.parseInt(ipString[i], 16);
            }
        }
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress
     *            the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return the ipByte
     */
    public byte[] getIpByte() {
        return ipByte;
    }

    /**
     * @param ipByte
     *            the ipByte to set
     */
    public void setIpByte(byte[] ipByte) {
        this.ipByte = ipByte;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ipAddress;
    }

}
