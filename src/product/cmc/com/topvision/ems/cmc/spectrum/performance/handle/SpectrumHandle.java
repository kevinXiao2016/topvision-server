/***********************************************************************
 * $Id: SpectrumHandle.java,v1.0 2015年6月13日 下午4:37:09 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.performance.handle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.spectrum.domain.SpectrumShowData;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumData;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumResult;
import com.topvision.ems.cmc.spectrum.service.SpectrumCallbackService1S;
import com.topvision.ems.cmc.spectrum.service.SpectrumMonitorService;
import com.topvision.ems.cmc.spectrum.utils.SpectrumSmoothUtil;
import com.topvision.ems.cmc.spectrum.utils.SpectrumTestUtil;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;

/**
 * @author Victor
 * @created @2015年6月13日-下午4:37:09
 *
 */
@Service
public class SpectrumHandle extends PerformanceHandle {
    public static String TYPE_CODE = "Spectrum";
    @Autowired
    private SpectrumCallbackService1S spectrumCallbackService1S;
    @Autowired
    private SpectrumMonitorService spectrumMonitorService;

    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(TYPE_CODE, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(TYPE_CODE);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        return TYPE_CODE;
    }

    @Override
    public void handle(PerformanceData data) {
        SpectrumResult spectrumResult = (SpectrumResult) data.getPerfData();
        if (logger.isDebugEnabled()) {
            logger.debug("CmcSpectrum identifyKey[" + spectrumResult.getDomain().getIdentifyKey() + "] exec start.");
        }
        List<SpectrumData> spectrums = spectrumResult.getSpectrumData();
        Long entityId = spectrumResult.getEntityId();
        Long cmcId = spectrumResult.getCmcId();
        Long dt = spectrumResult.getDt();
        List<List<Number>> list = new ArrayList<List<Number>>();
        if (spectrums != null) {
            list = turnSpectrumBuffToStrings(spectrums);
            if (list.size() == 0) {// 如果采到的数据全部为0
                this.fillZeroData(list);
            }
        } else {
            this.fillZeroData(list);
        }
        SpectrumTestUtil.addTestPoints(list);
        SpectrumSmoothUtil.leveling(list);
        // Modify by Victor@20160823增加推送判断，如果没有推送接收方，则停止后台采集
        if (!spectrumCallbackService1S.pushResult(entityId, cmcId, 500000L, 81600000L, list, dt)) {
            spectrumMonitorService.stopSpectrumMonitor(cmcId, data.getMonitorId());
        }
    }

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        return null;
    }

    private void fillZeroData(List<List<Number>> list) {
        list.clear();
        double zero = 0.0d;
        for (double i = 0.3d; i < 81.61d; i = i + 0.02d) {
            List<Number> l = new ArrayList<Number>();
            l.add(i);
            l.add(zero);
            list.add(l);
        }
    }

    /**
     * 将获取的频谱16进制字符串数据转 修改后的turnSpectrumBuffToStrings
     */
    private List<List<Number>> turnSpectrumBuffToStrings(List<SpectrumData> spectrums) {
        List<SpectrumShowData> cmcSpectrumShowDatas = new ArrayList<SpectrumShowData>();
        @SuppressWarnings("unused")
        int len = 0;
        /* 根据当采集的频点间隔算出L(startIndex)，根据endIndex-startIndex.算出H(endIndex)+1 */
        for (SpectrumData spectrumData : spectrums) {
            String tes = spectrumData.getTopCcmtsFftDataBuffer();
            String[] testS = tes.split("\\:");
            if ((testS.length - 2) % 8 == 0) {
                len += (testS.length - 2) / 8;
                for (int i = 2; i <= testS.length - 8; i = i + 8) {
                    SpectrumShowData cmcSpectrumShowData = new SpectrumShowData();
                    Integer fIndex = Integer.parseInt(testS[i] + testS[i + 1] + testS[i + 2] + testS[i + 3], 16);
                    cmcSpectrumShowData.setFrequencyIndex(fIndex);
                    // cmcSpectrumShowData.setPow((Long.parseLong(testS[i+4]+testS[i+5]+testS[i+6]+testS[i+7],
                    // 16)<<32)>>32);
                    cmcSpectrumShowData.setPower((Long
                            .parseLong(testS[i + 4] + testS[i + 5] + testS[i + 6] + testS[i + 7], 16) << 32) >> 32);
                    cmcSpectrumShowData.setCmcId(spectrumData.getCmcId());
                    cmcSpectrumShowDatas.add(cmcSpectrumShowData);
                }
            }
        }
        Integer freqInterval = 0;// 单位Hz
        if (cmcSpectrumShowDatas.size() >= 1 && cmcSpectrumShowDatas.get(1).getFrequencyIndex() != 0) {
            freqInterval = cmcSpectrumShowDatas.get(1).getFrequencyIndex()
                    - cmcSpectrumShowDatas.get(0).getFrequencyIndex();
        } else {
            logger.debug(
                    "turnSpectrumBuffToStrings error ; !(cmcSpectrumShowDatas.size() >= 1 && cmcSpectrumShowDatas.get(1).getFrequencyIndex() != 0)");
            return new ArrayList<List<Number>>();
        }

        /*
         * 关于RBW和VBW计算 由于freqInterval的最小值20k,所以假如RBW和VBW都为300k的时候， 分别都是取15个点，则左右7个点来计算第8个点的值
         */
        // RBW
        Integer sp_rbw = SpectrumShowData.RBW / (freqInterval / 1000);// 需要计算的点数
        // VBW
        Integer sp_vbw = SpectrumShowData.VBW / (freqInterval / 1000);
        List<List<Number>> pointList = new ArrayList<List<Number>>();

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
            // Double d = 0d;
            // for (int j = 1; j < offSet; j++) {
            // double db = (cmcSpectrumShowDatas.get(i - j).getPower()) / 100 / 20;
            // d += (offSet - j) * (StrictMath.pow(10d, db));
            // }
            // for (int j = 1; j < offSet; j++) {
            // double db = (cmcSpectrumShowDatas.get(i + j).getPower()) / 100 / 20;
            // d += (offSet - j) * (StrictMath.pow(10d, db));
            // }
            // d += (StrictMath.pow(10d, nowPower / 100 / 20)) * sp_vbw;
            // Double resultPower = (StrictMath.log10(d / sp_vbw)) * 20;
            BigDecimal bg = new BigDecimal(nowPower + 60);
            // if (spectrumUnit.equals("dBmV")) {// dbmV不加60
            // bg = new BigDecimal(nowPower);
            // } else {// dbuV加60
            // bg = new BigDecimal(nowPower + 60);
            // }
            double freq = ((double) cmcSpectrumShowDatas.get(i).getFrequencyIndex()) / 1000000;
            double rp = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            point.add(Double.valueOf(df.format(freq)));
            point.add(Double.valueOf(df.format(rp)));// dbmV--》dbμV
            // TODO
            // point.add(resultPower+60+Math.random()*10);//dbmV--》dbμV
            pointList.add(point);
        }
        return pointList;
    }
}
