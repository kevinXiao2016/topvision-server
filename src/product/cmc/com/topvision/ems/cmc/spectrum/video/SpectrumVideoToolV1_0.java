/***********************************************************************
 * $ VideoVersionToolV1_0_0_0.java,v1.0 2014-1-22 12:00:26 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.video;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.cmc.spectrum.domain.SpectrumVideoFrameData;
import com.topvision.ems.cmc.spectrum.utils.ByteUtil;

/**
 * @author jay
 * @created @2014-1-22-12:00:26
 */
public class SpectrumVideoToolV1_0 implements SpectrumVideoTool {
    public static final long serialVersionUID = 7342180206202447869L;
    public static final int intLength = 4;
    public static final int longLength = 8;
    public static final int doubleLength = 8;

    public static final int versionLength = intLength;
    public static final int entityIdLength = longLength;
    public static final int cmcIdLength = longLength;
    public static final int dtLength = longLength;
    public static final int pointCountLength = intLength;
    public static final int frameCountLength = longLength;
    public static final int pointLength = doubleLength + doubleLength;

    public static final int fileStartIndex = 0;
    public static final int versionIndex = fileStartIndex;
    public static final int entityIdIndex = versionIndex + versionLength;
    public static final int cmcIdIndex = entityIdIndex + entityIdLength;
    public static final int dtIndex = cmcIdIndex + cmcIdLength;
    public static final int pointCountIndex = dtIndex + dtLength;
    public static final int frameCountIndex = pointCountIndex + pointCountLength;
    public static final int frameStartIndex = frameCountIndex + frameCountLength;

    private final static DecimalFormat df = new DecimalFormat("0.00");

    public static void writeInt(RandomAccessFile out, Integer value, Integer field) throws IOException {
        out.seek(field);
        out.writeInt(value);
    }

    public static void writeLong(RandomAccessFile out, Long value, Integer field) throws IOException {
        out.seek(field);
        out.writeLong(value);
    }

    public static void writePoint(RandomAccessFile out, Double frqIndex, Double power, Long frameIndex,
            Integer pointCount, Integer pointIndex) throws IOException {
        seekPoint(out, frameIndex, pointCount, pointIndex);
        out.writeDouble(frqIndex);
        out.writeDouble(power);
    }

    public static void writeFrame(RandomAccessFile out, List<List<Number>> list, Long frameIndex, Integer pointCount)
            throws IOException {
        seekFrame(out, frameIndex, pointCount);
        int listSize = list.size() * 2 * 8; // 要准备的byte数据的大小
        byte[] bytes = new byte[listSize];
        int i = 0;
        for (List<Number> l : list) {
            double freq = (Double) l.get(0);
            double value = (Double) l.get(1);
            byte[] freqBytes = ByteUtil.getBytes(freq);
            byte[] valueBytes = ByteUtil.getBytes(value);
            for (int j = 0; j < freqBytes.length; i++, j++) {
                bytes[i] = freqBytes[j];
            }
            for (int j = 0; j < valueBytes.length; i++, j++) {
                bytes[i] = valueBytes[j];
            }
        }
        out.write(bytes);
    }

    public static Integer readInt(RandomAccessFile out, int field) throws IOException {
        out.seek(field);
        return out.readInt();
    }

    @Override
    public Long readLong(RandomAccessFile out, int field) throws IOException {
        out.seek(field);
        return out.readLong();
    }

    @Override
    public Long readFrameCount(RandomAccessFile out) throws IOException {
        return readLong(out,frameCountIndex);
    }

    @Override
    public SpectrumVideoFrameData readFrame(RandomAccessFile out, Long frameIndex)
            throws IOException {
        SpectrumVideoFrameData sd = new SpectrumVideoFrameData();
        Integer pointCount = readInt(out, SpectrumVideoToolV1_1.pointCountIndex);
        List<List<Number>> list = new ArrayList<List<Number>>();
        seekFrame(out, frameIndex, pointCount);
        int listSize = pointCount * 2 * 8;
        byte[] readbytes = new byte[listSize];
        byte[] desByte = new byte[8];
        out.read(readbytes);
        for (int i = 0; i < readbytes.length;) {
            // SpectrumPoint spectrumPoint = new SpectrumPoint();
            List<Number> xylist = new ArrayList<Number>();
            for (int l = 0; l < 8; l++, i++) {
                desByte[l] = readbytes[i];
            }
            double x = ByteUtil.getDouble(desByte);
            xylist.add(x);
            for (int k = 0; k < 8; k++, i++) {
                desByte[k] = readbytes[i];
            }
            double y = ByteUtil.getDouble(desByte);
            y = Double.valueOf(df.format(y));
            xylist.add(y);
            list.add(xylist);
        }
        sd.setDataList(list);
        return sd;
    }

    private static void seekPoint(RandomAccessFile out, Long frameIndex, Integer pointCount, Integer pointIndex)
            throws IOException {
        // 起始位子 + (完整桢数 * 完整点个数 + 当前点index -1) * 每个点的占用字节数
        out.seek(frameStartIndex + ((frameIndex - 1) * pointCount + pointIndex - 1) * pointLength);
    }

    private static void seekFrame(RandomAccessFile out, Long frameIndex, Integer pointCount) throws IOException {
        // 起始位子 + (完整桢数 * 完整点个数 ) * 每个点的占用字节数
        out.seek(frameStartIndex + ((frameIndex - 1) * pointCount) * pointLength);
    }
}
