/***********************************************************************
 * $Id: SpectrumIIHandle.java,v1.0 2015年6月13日 下午4:42:39 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.performance.handle;

import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumIIIData;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumIIIResult;
import com.topvision.ems.cmc.spectrum.service.SpectrumCallbackService1S;
import com.topvision.ems.cmc.spectrum.service.SpectrumMonitorService;
import com.topvision.ems.cmc.spectrum.utils.SpectrumIIDataParse;
import com.topvision.ems.cmc.spectrum.utils.SpectrumSmoothUtil;
import com.topvision.ems.cmc.spectrum.utils.SpectrumTestUtil;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Victor
 * @created @2015年6月13日-下午4:42:39
 * 
 */
@Service
public class SpectrumIIIHandle extends PerformanceHandle {
    @Autowired
    private SpectrumCallbackService1S spectrumCallbackService1S;
    @Autowired
    private SpectrumMonitorService spectrumMonitorService;
    public static int i = 0;
    public static String TYPE_CODE = "SpectrumIII";
    

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

    private static Long collectTime = 0L;
    private static Long minInterval = 0L;
    private static Long maxInterval = 0L;
    private static Double avgInterval = 0d;
    private static Integer index = 0;
    @Override
    public void handle(PerformanceData data) {
    	/*Long thisTime = System.currentTimeMillis();
    	Long thisInterval = collectTime == 0L ? 0 : thisTime - collectTime;
    	collectTime = thisTime;
    	if(minInterval == 0L || minInterval > thisInterval) {
    		minInterval = thisInterval;
    	}
    	if(maxInterval == 0L || maxInterval < thisInterval) {
    		maxInterval = thisInterval;
    	}
    	avgInterval = (avgInterval * index + thisInterval) / (++index);
    	System.out.println(String.format("Index: %s, thisInterval: %s, minInterval: %s, maxInterval: %s, avgInterval: %s",
        		index, thisInterval, minInterval, maxInterval, avgInterval));*/
    	
        SpectrumIIIResult spectrumResult = (SpectrumIIIResult) data.getPerfData();
        logger.debug("SpectrumIIIHandle identifyKey[" + spectrumResult.getDomain().getIdentifyKey() + "] exec start.");
        Long entityId = spectrumResult.getEntityId();
        Long cmcId = spectrumResult.getCmcId();
        Long dt = spectrumResult.getDt();
        String dataBuffer = spectrumResult.getDataBuffer();
        List<List<Number>> list = new ArrayList<List<Number>>();
        SpectrumIIDataParse spectrumIIDataParse = new SpectrumIIDataParse();
        if (dataBuffer != null && !dataBuffer.equals("")) {
            spectrumIIDataParse.setCmcId(cmcId);
            spectrumIIDataParse.setSrcData(dataBuffer);
            spectrumIIDataParse.prase();
            list = spectrumIIDataParse.getPoints();
            if (list.size() == 0) {// 如果采到的数据全部为0
                // this.fillZeroData(list);
                logger.debug("spectrum points list size error ");
                return;
            }
            logger.debug("End parse spectrum data ,List size = " + list.size());
        } else {
            logger.debug("spectrum is null ");
            return;
            // this.fillZeroData(list);
        }
        SpectrumTestUtil.addTestPoints(list);
        logger.debug("Adding test spectrum data");
        SpectrumSmoothUtil.leveling(list);
        logger.debug("Smoothing spectrum data completion");
        // Modify by Victor@20160823增加推送判断，如果没有推送接收方，则停止后台采集
        if (!spectrumCallbackService1S.pushResult(entityId, cmcId, spectrumIIDataParse.getStartFrequency(),
                spectrumIIDataParse.getEndFrequency(), list, dt)) {
            spectrumMonitorService.stopSpectrumMonitor(cmcId, data.getMonitorId());
        }
        logger.debug("Spectrum push data completion");
    }

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        return null;
    }

    // private void fillZeroData(List<List<Number>> list) {
    // list.clear();
    // double zero = 0.0d;
    // for (double i = 0.3d; i < 81.61d; i = i + 0.02d) {
    // List<Number> l = new ArrayList<Number>();
    // l.add(i);
    // l.add(zero);
    // list.add(l);
    // }
    // }
}
