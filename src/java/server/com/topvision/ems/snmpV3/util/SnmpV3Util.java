/***********************************************************************
 * $Id: keyc.java,v1.0 2013-1-19 下午6:35:12 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.util;

import org.snmp4j.security.AuthGeneric;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.smi.OctetString;

/**
 * @author Bravin
 * @created @2013-1-19-下午6:35:12
 * 
 */
public class SnmpV3Util {
    public void test() {
        System.out.println(getKeyChange("md5", "12345678", "12345679", "7E:1D:21:98:00:11:00:00:00:30"));
    }

    public static String getKeyChange(String type, String oldPass, String newPass, String engineId) {
        byte[] id = octo2byte(engineId);
        AuthGeneric auth = null;
        if ("md5".equalsIgnoreCase(type)) {
            auth = new AuthMD5();
        } else if ("sha".equalsIgnoreCase(type)) {
            auth = new AuthSHA();
        }
        byte[] oldKey = auth.passwordToKey(new OctetString(oldPass), id);
        byte[] newKey = auth.passwordToKey(new OctetString(newPass), id);
        byte[] keyChange = auth.changeDelta(oldKey, newKey, newKey);

        return bytes2OctetString(keyChange);
    }

    public static byte[] hex2bytes(String h) {
        byte[] b = new byte[h.length() / 2];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) Integer.parseInt(h.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }

    /**
     * 将一个十进制数转成标准格式的十六进制（每个标准格式均为两个字节）
     * 
     * @param i
     * @return
     */
    public static String getByteHexStringFromNum(Integer i) {
        String result = "";
        if (Integer.toHexString(i).length() == 1) {
            result = String.format("0%s", Integer.toHexString(i));
        } else if (Integer.toHexString(i).length() == 2) {
            result = String.format("%s", Integer.toHexString(i));
        }
        return result;
    }

    public static String bytes2hex(byte[] bs) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < bs.length; i++) {
            String s = "00" + Integer.toHexString(bs[i]).toUpperCase();
            hex.append(s.substring(s.length() - 2));
        }
        return hex.toString();
    }

    public static String bytes2OctetString(byte[] bs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bs.length; i++) {
            String s = "00" + Integer.toHexString(bs[i]).toUpperCase();
            sb.append(s.substring(s.length() - 2));
            sb.append(":");
        }
        return sb.substring(0, sb.length() - 1).toString();
    }

    public static byte[] octo2byte(String targetUserEngineId) {
        String[] sp = targetUserEngineId.split(":");
        byte[] id = new byte[sp.length];
        for (int i = 0; i < sp.length; i++) {
            id[i] = (byte) (Integer.parseInt(sp[i], 16));
        }
        return id;
    }

    public static byte[] number2Byte(String number) {
        String result = "";
        if (number.length() % 2 == 1) {
            result = String.format("0%s", number);
        } else if (number.length() % 2 == 0) {
            result = String.format("%s", number);
        }
        byte[] bytes = new byte[result.length() / 2];
        for (int i = 0; i < result.length() / 2; i++) {
            bytes[i] = (byte) Integer.parseInt(result.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }

    public static String byte2number(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String s = "00" + Integer.toHexString(bytes[i]).toUpperCase();
            sb.append(s.substring(s.length() - 2));
        }
        return sb.toString();
    }
}
