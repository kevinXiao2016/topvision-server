/***********************************************************************
 * $ SpectrumIIDataParse.java,v1.0 14-4-27 下午2:56 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.cmc.spectrum.domain.SpectrumShowData;
import com.topvision.framework.utils.ZlibUtils;

/**
 * @author jay
 * @created @14-4-27-下午2:56
 */
public class SpectrumIIDataParse extends SpectrumParse {
    Logger logger = LoggerFactory.getLogger(SpectrumIIDataParse.class);
    private String srcData;
    private int compressedDataLength;
    private long startFrequency;
    private long endFrequency;
    private long frequencyInterval;
    private long frequencyPointCount;

    public void prase() {
        srcData = srcData.replaceAll(":", "");
        long l = srcData.length();
        SpectrumIIStringRead stringRead = new SpectrumIIStringRead(srcData);
        // 获取压缩数据字节数
        compressedDataLength = (int) stringRead.readNByteToNumber(2);
        // 压缩字符串
        String excessStr = stringRead.excessStr();
        // 比较压缩数据字节数与压缩字符串长度是否能对应上
        if (compressedDataLength != 0 && compressedDataLength * 2 == excessStr.length()) {
            if (logger.isDebugEnabled()) {
                logger.debug("data length compressedDataLength[" + compressedDataLength + "]");
            }
            // 解压缩
            byte[] ds = ZlibUtils.decompress(stringRead.readNByte(compressedDataLength));
            // 构造解压缩字符串
            String decompressString = ZlibUtils.printString(ds);
            if (logger.isTraceEnabled()) {
                logger.trace("decompressString = " + decompressString);
            }

            SpectrumIIStringRead deStringRead = new SpectrumIIStringRead(decompressString);
            // 读取起始频点
            startFrequency = deStringRead.readNByteToNumber(4);
            if (logger.isDebugEnabled()) {
                logger.debug("startFrequency = " + startFrequency);
            }
            // 读取结束频点
            endFrequency = deStringRead.readNByteToNumber(4);
            if (logger.isDebugEnabled()) {
                logger.debug("endFrequency = " + endFrequency);
            }
            // 读取频点间隔
            frequencyInterval = deStringRead.readNByteToNumber(4);
            if (logger.isDebugEnabled()) {
                logger.debug("frequencyInterval = " + frequencyInterval);
            }
            // 读取频点总数
            frequencyPointCount = deStringRead.readNByteToNumber(4);
            if (logger.isDebugEnabled()) {
                logger.debug("frequencyPointCount = " + frequencyPointCount);
            }
            // 频谱原始数据，格式为每两个字节一个频点的powerlevel
            String powerLevelString = deStringRead.excessStr();
            if (logger.isTraceEnabled()) {
                logger.trace("powerLevelString = " + powerLevelString);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("powerLevelString.length() = " + powerLevelString.length());
            }
            if (frequencyPointCount * 2 * 2 != powerLevelString.length()) {
                logger.debug("powerLevelString error");
            } else {
                List<SpectrumShowData> spectrumShowDatas = makeSpectrumShowData(powerLevelString);
                //经过turnSpectrumBuffToStrings 计算RBW和VBW之后会少30个点
                turnSpectrumBuffToStrings(spectrumShowDatas);
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("data length error compressedDataLength[" + compressedDataLength + "] excessStr.length["
                        + excessStr.length() + "]");
            }
        }
    }

    protected List<SpectrumShowData> makeSpectrumShowData(String data) {
        SpectrumIIStringRead powerLevelStringReader = new SpectrumIIStringRead(data);
        List<SpectrumShowData> cmcSpectrumShowDatas = new ArrayList<SpectrumShowData>();
        for (int i = 0; i < frequencyPointCount; i++) {
            SpectrumShowData cmcSpectrumShowData = new SpectrumShowData();
            Integer frequencyIndex = (int) (startFrequency + i * frequencyInterval);
            cmcSpectrumShowData.setFrequencyIndex(frequencyIndex);
            Integer powerLevel = Integer.parseInt(powerLevelStringReader.readNByteString(2), 16);
            cmcSpectrumShowData.setPower(powerLevel.shortValue());
            cmcSpectrumShowData.setCmcId(cmcId);
            cmcSpectrumShowDatas.add(cmcSpectrumShowData);
        }
        return cmcSpectrumShowDatas;
    }

    public void setSrcData(String srcData) {
        this.srcData = srcData;
    }

    public long getStartFrequency() {
        return startFrequency;
    }

    public void setStartFrequency(long startFrequency) {
        this.startFrequency = startFrequency;
    }

    public long getEndFrequency() {
        return endFrequency;
    }

    public void setEndFrequency(long endFrequency) {
        this.endFrequency = endFrequency;
    }
}
