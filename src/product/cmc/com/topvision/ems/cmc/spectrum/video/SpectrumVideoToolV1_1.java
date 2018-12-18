/***********************************************************************
 * $Id: VideoVersionToolV1_0_1_0.java,v1.0 2015年3月13日 下午5:06:23 $
 * 
 * @author: Administrator
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
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;

/**
 * @author YangYi
 * @created @2015年3月13日-下午5:06:23
 * 
 */
public class SpectrumVideoToolV1_1 implements SpectrumVideoTool {
    public static final long serialVersionUID = 7342180206202447869L;
    public static final int shortLegth = 2;
    public static final int intLength = 4;
    public static final int longLength = 8;
    public static final int doubleLength = 8;

    // version,entityId,cmcId,dt,pointCount,frameCount,frameData
    // frameData:c1fre,c1width,c1status,c2fre,c2width,c2status,c3fre,c3width,c3status,c4fre,c4width,c4status
    // 4066 power-lever
    public static final int versionLength = intLength;
    public static final int entityIdLength = longLength;
    public static final int cmcIdLength = longLength;
    public static final int dtLength = longLength;
    public static final int pointCountLength = intLength;
    public static final int frameCountLength = longLength;
    public static final int pointLength = shortLegth;

    public static final int fileStartIndex = 0;
    public static final int versionIndex = fileStartIndex;
    public static final int entityIdIndex = versionIndex + versionLength;
    public static final int cmcIdIndex = entityIdIndex + entityIdLength;
    public static final int dtIndex = cmcIdIndex + cmcIdLength;
    public static final int pointCountIndex = dtIndex + dtLength;
    public static final int frameCountIndex = pointCountIndex + pointCountLength;
    public static final int frameStartIndex = frameCountIndex + frameCountLength;

    private final static DecimalFormat df = new DecimalFormat("#.00");

    public void writeInt(RandomAccessFile out, Integer value, Integer field) throws IOException {
        out.seek(field);
        out.writeInt(value);
    }

    public void writeLong(RandomAccessFile out, Long value, Integer field) throws IOException {
        out.seek(field);
        out.writeLong(value);
    }

    public void writePoint(RandomAccessFile out, Double frqIndex, Double power, Long frameIndex,
            Integer pointCount, Integer pointIndex) throws IOException {
        seekPoint(out, frameIndex, pointCount, pointIndex);
        out.writeDouble(frqIndex);
        out.writeDouble(power);
    }

    /**
     * 写入一帧数据
     * 
     * @param out
     *            需要写入的文件
     * @param list
     *            噪声数据
     * @param frameIndex
     *            帧编号
     * @param pointCount
     *            一帧中的点数
     * @param channelList
     *            上行信道列表
     * @throws IOException
     */
    public void writeFrame(RandomAccessFile out, List<List<Number>> list, Long frameIndex, Integer pointCount,
            List<CmcUpChannelBaseShowInfo> channelList)
            throws IOException {
        seekFrame(out, frameIndex, pointCount);
        for (int i = 0; i < 4; i++) {
            if (i < channelList.size()) {
                out.writeLong(channelList.get(i).getChannelFrequency());
                out.writeLong(channelList.get(i).getChannelWidth());
                out.writeShort(channelList.get(i).getIfAdminStatus());
            } else {
                out.writeLong(0L);
                out.writeLong(0L);
                out.writeShort(2);
            }
        }
        for (List<Number> l : list) {
            double value = (Double) l.get(1);
            short v = (short) (value * 100);
            out.writeShort(v);
        }
    }

    public Integer readInt(RandomAccessFile out, int field) throws IOException {
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

    /**
     * 
     * @param out
     *            需要读取的文件
     * @param frameIndex
     *            帧编号
     * @param pointCount
     * @return
     * @throws IOException
     */
    @Override
    public SpectrumVideoFrameData readFrame(RandomAccessFile out, Long frameIndex)
            throws IOException {
        Integer pointCount = readInt(out, SpectrumVideoToolV1_1.pointCountIndex);
        List<List<Number>> list = new ArrayList<List<Number>>();
        seekFrame(out, frameIndex, pointCount);
        SpectrumVideoFrameData sd = new SpectrumVideoFrameData();
        List<CmcUpChannelBaseShowInfo> channelList = new ArrayList<CmcUpChannelBaseShowInfo>();
        for (int i = 0; i < 4; i++) {
            CmcUpChannelBaseShowInfo c = new CmcUpChannelBaseShowInfo();
            c.setChannelFrequency(out.readLong());
            c.setChannelWidth(out.readLong());
            c.setIfAdminStatus((int) out.readShort());
            channelList.add(c);
        }
        sd.setChannelList(channelList);
        for(int i = 0 ; i <pointCount; i ++){
            List<Number> xylist = new ArrayList<Number>();
            double x = 0.3d + i * 0.02d;
            x = Double.valueOf(df.format(x));
            xylist.add(x);
            double y = (double) out.readShort() / 100;
            y = Double.valueOf(df.format(y));
            xylist.add(y);
            list.add(xylist);
        }
        sd.setDataList(list);
        return sd;
    }

    private void seekPoint(RandomAccessFile out, Long frameIndex, Integer pointCount, Integer pointIndex)
            throws IOException {
        // 起始位子 + (完整桢数 * 完整点个数 + 当前点index -1) * 每个点的占用字节数
        out.seek(frameStartIndex + ((frameIndex - 1) * pointCount + pointIndex - 1) * pointLength);
    }

    private void seekFrame(RandomAccessFile out, Long frameIndex, Integer pointCount) throws IOException {
        // 起始位子 + (完整桢数 * 完整点个数 ) * 每个点的占用字节数
        out.seek(frameStartIndex + (frameIndex - 1)
                * (4 * (longLength + longLength + shortLegth) + pointCount * pointLength));
    }
}
