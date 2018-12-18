package com.topvision.ems.cmc.spectrum.video;

import java.io.IOException;
import java.io.RandomAccessFile;

import com.topvision.ems.cmc.spectrum.domain.SpectrumVideoFrameData;

public interface SpectrumVideoTool {
    /**
     * 读取一帧数据
     * 
     * @param out
     * @param frameIndex
     * @return
     * @throws IOException
     */
    public SpectrumVideoFrameData readFrame(RandomAccessFile out, Long frameIndex) throws IOException;

    /**
     * 获取Long型字段，例如版本号
     * 
     * @param out
     * @param field
     * @return
     * @throws IOException
     */
    public Long readLong(RandomAccessFile out, int field) throws IOException;

    /**
     * 读取帧数
     * @param rf
     * @return
     */
    Long readFrameCount(RandomAccessFile rf) throws IOException;
}
