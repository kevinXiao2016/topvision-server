/***********************************************************************
 * $ SpectrumDataParse.java,v1.0 14-4-27 下午3:22 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.cmc.spectrum.domain.SpectrumShowData;

/**
 * @author jay
 * @created @14-4-27-下午3:22
 */
public abstract class SpectrumParse {
    Logger logger = LoggerFactory.getLogger(SpectrumParse.class);
    protected long cmcId;
    protected List<List<Number>> points = new ArrayList<>();

    public abstract void prase();

    protected abstract List<SpectrumShowData> makeSpectrumShowData(String data);

    /**
     * 将获取的频谱16进制字符串数据转 修改后的turnSpectrumBuffToStrings
     */
    protected void turnSpectrumBuffToStrings(List<SpectrumShowData> cmcSpectrumShowDatas) {
        Integer freqInterval = 0;// 单位Hz
        if (cmcSpectrumShowDatas.size() >= 1 && cmcSpectrumShowDatas.get(1).getFrequencyIndex() != 0) {
            freqInterval = cmcSpectrumShowDatas.get(1).getFrequencyIndex()
                    - cmcSpectrumShowDatas.get(0).getFrequencyIndex();
        } else {
            logger.debug("turnSpectrumBuffToStrings error ; !(cmcSpectrumShowDatas.size() >= 1 && cmcSpectrumShowDatas.get(1).getFrequencyIndex() != 0)");
            points = new ArrayList<List<Number>>();
            freqInterval = 1000;
        }
        /*
         * 关于RBW和VBW计算 由于freqInterval的最小值20k,所以假如RBW和VBW都为300k的时候， 分别都是取15个点，则左右7个点来计算第8个点的值
         */
        // RBW
        Integer sp_rbw = SpectrumShowData.RBW / (freqInterval / 1000);// 需要计算的点数
        // VBW
        Integer sp_vbw = SpectrumShowData.VBW / (freqInterval / 1000);

        // 在RBW和VBW都为300K的时候，freqInterval允许值为20k，60k，100k
        Integer offSet = (sp_rbw + sp_vbw) / 2;// 不计算的首尾个数.偏移的数据
        for (int i = offSet; i < cmcSpectrumShowDatas.size() - offSet; i++) {
            // YangYi注释，2014-04-14，根据吕海艇的说话进行修改
            // 00:00:03:72 十六进制 372
            // 转化为十进制 882
            // 882除以100 = 8.82 dbmV
            // 8.82 + 60 = 68.82 dbμV
            List<Number> point = new ArrayList<Number>();
            Double nowPower = cmcSpectrumShowDatas.get(i).getPower() / 100;
            BigDecimal bg = new BigDecimal(nowPower + 60);
            double rp = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            point.add(((double) cmcSpectrumShowDatas.get(i).getFrequencyIndex()) / 1000000);
            point.add(rp);// dbmV--》dbμV
            points.add(point);
        }
    }

    public void setCmcId(long cmcId) {
        this.cmcId = cmcId;
    }

    public List<List<Number>> getPoints() {
        return points;
    }

}
