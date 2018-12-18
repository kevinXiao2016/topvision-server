/***********************************************************************
 * $ StringRead.java,v1.0 14-4-27 上午9:38 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author jay
 * @created @14-4-27-上午9:38
 */
public class SpectrumByteRead {
    private byte[] bytes;
    private int index = 0;

    public SpectrumByteRead(byte[] bytes) {
        this.bytes = bytes;
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
        if (bytes.length >= index + 1) {
            byte b = bytes[index++];
            return b;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String readNByteString(int n) {
        StringBuilder sb = new StringBuilder();
        if (bytes.length >= index + n) {
            int i = index;
            for ( ;i < index + n ; i++) {
                String stmp = String.format("%02X",bytes[i]);
                sb.append(stmp);
            }
            index = i;
            return sb.toString();
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
}
