/***********************************************************************
 * $ StringRead.java,v1.0 14-4-27 上午9:38 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.utils;

/**
 * @author jay
 * @created @14-4-27-上午9:38
 */
public class SpectrumIIStringRead {
    private String str;
    private int index = 0;
    private int BYTELENGTH = 2;

    public SpectrumIIStringRead(String str) {
        this.str = str;
    }

    public long readNByteToNumber(int n) {
        long re = 0;
        for (int i = n - 1; i >= 0; i--) {
            byte b = readByte();
            long ub = b & 0xFF;
            long m = ub << (8 * i);
            re = re + m;
        }
        return re;
    }

    public byte[] readNByte(int n) {
        byte[] bs = new byte[n];
        for (int i = 0; i < n; i++) {
            bs[i] = readByte();
        }
        return bs;
    }

    public byte readByte() {
        if (str == null) {
            throw new NullPointerException();
        }
        if (str.length() >= index + BYTELENGTH) {
            String n = str.substring(index, index + BYTELENGTH);
            byte b = (byte) Integer.parseInt(n, 16);
            index = index + BYTELENGTH;
            return b;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public String excessStr() {
        return str.substring(index);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String readNByteString(int n) {
        if (str == null) {
            throw new NullPointerException();
        }
        if (str.length() >= index + n * BYTELENGTH) {
            String num = str.substring(index, index + n * BYTELENGTH);
            index = index + n * BYTELENGTH;
            return num;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
}
