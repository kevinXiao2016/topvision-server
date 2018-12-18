/***********************************************************************
 * $Id: ByteUtils.java,v1.0 2013-1-22 下午04:00:25 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author RodJohn
 * @created @2013-1-22-下午04:00:25
 * 
 */
public class ByteUtils {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public static String toString(byte[] bytes) {
        StringBuilder sbBuilder = new StringBuilder();
        if (bytes != null && bytes.length > 0) {
            for (byte b : bytes) {
                sbBuilder.append(":").append(bytes2hex(b));
            }
            return sbBuilder.substring(1);
        } else {
            return null;
        }
    }

    public static String toStringWithoutSplit(byte[] bytes) {
        StringBuilder sbBuilder = new StringBuilder();
        if (bytes != null && bytes.length > 0) {
            for (byte b : bytes) {
                sbBuilder.append(bytes2hex(b));
            }
            return sbBuilder.toString();
        } else {
            return null;
        }
    }

    public static byte[] toBytes(String bStrings) {
        if (bStrings != null && bStrings.equals("")) {
            return new byte[0];
        }
        if (bStrings != null) {
            String[] byteStrings = bStrings.split(":");
            byte[] result = new byte[byteStrings.length];
            for (int i = 0; i < result.length; i++) {
                byte b = (byte) Integer.parseInt(byteStrings[i], 16);
                result[i] = (byte) ((b < 0) ? 256 + b : b);
            }
            return result;
        } else {
            return null;
        }
    }

    private static String bytes2hex(byte bs) {
        StringBuilder hex = new StringBuilder();
        String s = "00" + Integer.toHexString((bs < 0) ? 256 + bs : bs).toUpperCase();
        hex.append(s.substring(s.length() - 2));
        return hex.toString();
    }
}
