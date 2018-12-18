/***********************************************************************
 * $Id: PhysAddress.java,v1.0 2013-1-22 下午02:13:06 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.domain;

/**
 * @author RodJohn
 * @created @2013-1-22-下午02:13:06
 * 
 */
public class PhysAddress extends BaseEntity {
    private static final long serialVersionUID = -5944039119268804575L;
    private byte[] macByte;

    public PhysAddress(byte[] mac) {
        if (mac.length == 6) {
            macByte = new byte[6];
            for (int i = 0; i < 6; i++) {
                macByte[i] = mac[i];
            }
        }
    }

    public PhysAddress(String mac) {
        String[] macString = mac.split(":");
        if (macString.length == 6) {
            macByte = new byte[6];
            for (int i = 0; i < 6; i++) {
                macByte[i] = (byte) Integer.parseInt(macString[i], 16);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (macByte != null) {
            for (int i = 0; i < 6; i++) {
                sb.append(":").append(bytes2hex(macByte[i]));
            }
            return sb.substring(1);
        }
        return null;
    }

    private static String bytes2hex(int bs) {
        StringBuilder hex = new StringBuilder();
        String s = "00" + Integer.toHexString(bs).toUpperCase();
        hex.append(s.substring(s.length() - 2));
        return hex.toString();
    }
}
