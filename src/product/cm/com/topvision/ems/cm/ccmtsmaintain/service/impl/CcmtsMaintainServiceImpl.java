/***********************************************************************
 * $Id: CcmtsMaintainServiceImpl.java,v1.0 2015-5-27 下午3:58:44 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.ccmtsmaintain.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cm.ccmtsmaintain.dao.CcmtsMaintainDao;
import com.topvision.ems.cm.ccmtsmaintain.domain.CcmtsMaintain;
import com.topvision.ems.cm.ccmtsmaintain.facade.CcmtsMaintainFacade;
import com.topvision.ems.cm.ccmtsmaintain.service.CcmtsMaintainService;
import com.topvision.ems.cm.cmpoll.message.CmPollStateEvent;
import com.topvision.ems.cm.cmpoll.message.CmPollStateListener;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author fanzidong
 * @created @2015-5-27-下午3:58:44
 * 
 */
@Service("ccmtsmaintainService")
public class CcmtsMaintainServiceImpl extends BaseService implements CcmtsMaintainService, CmPollStateListener {

    @Autowired
    private MessageService messageService;
    @Autowired
    private CcmtsMaintainDao ccmtsMaintainDao;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;

    private Map<Long, Map<Integer, Map<String, Double>>> finalDistMap;

    private Timestamp collectTime;

    public static final String AVGSNR = "avgSnr";
    public static final String LOW_SNR_CMNUM = "lowSnrCmNum";
    public static final String BIG_POWER_CMNUM = "bigPowerCmNum";
    public static final String CHL_WIDTH = "channelWidth";
    public static final String CM_TOTAL = "cmTotal";

    @PostConstruct
    public void init() {
        // 注册监听器
        messageService.addListener(CmPollStateListener.class, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cm.cmpoll.message.CmPollStateListener#startRoundStatistics(com.topvision
     * .ems.cm.cmpoll.message.CmPollStateEvent)
     */
    @Override
    public void startRoundStatistics(CmPollStateEvent event) {
        // 一轮采集处理开始，初始化数据
        finalDistMap = new HashMap<Long, Map<Integer, Map<String, Double>>>();
        Date time = new Date();
        collectTime = new Timestamp(time.getTime());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cm.cmpoll.message.CmPollStateListener#completeRoundStatistics(com.topvision
     * .ems.cm.cmpoll.message.CmPollStateEvent)
     */
    @Override
    public void completeRoundStatistics(CmPollStateEvent event) {
        try {
            // 获取engine列表
            List<EngineServer> engineServers = facadeFactory.getAllEngineServer();
            // 遍历engine
            CcmtsMaintainFacade ccmtsMaintainFacade;
            Map<Long, Map<Integer, Map<String, Double>>> partDistMap;
            for (EngineServer engineServer : engineServers) {
                if (engineServer.getLinkStatus() == EngineServer.CONNECTED) {
                    // 取到离散度分布数据
                    try {
                        ccmtsMaintainFacade = facadeFactory.getFacade(engineServer, CcmtsMaintainFacade.class);
                        partDistMap = ccmtsMaintainFacade.getDistributionData();
                        // 合并入本轮map中
                        mergeDistMap(partDistMap);
                    } catch (Exception e) {
                        // 本采集器数据处理异常，跳过
                        logger.debug("Dispersion::caculate engine data error:" + e.getMessage());
                        continue;
                    }

                }
            }
            // 根据最终的map计算要存储的数据
            List<CcmtsMaintain> ccmtsMaintains = new ArrayList<CcmtsMaintain>();
            try{
                ccmtsMaintains = convertDistMapToList();
            }catch(Exception e){
                e.printStackTrace();
            }
            logger.debug("Dispersion::convertDistMapToList");
            // 入库
            ccmtsMaintainDao.batchInsertMaintainData(ccmtsMaintains);
        } catch (Exception e) {
            logger.debug("Dispersion::current round caculate error, failed to insert data:" + e.getMessage());
        } finally {
            // distory操作
            finalDistMap = null;
        }
    }

    /**
     * 将从engine端拿到的部分分布数据合并入总数据中
     * 
     * @param partDistMap
     * @return
     */
    private void mergeDistMap(Map<Long, Map<Integer, Map<String, Double>>> partDistMap) {
        Iterator<Entry<Long, Map<Integer, Map<String, Double>>>> iter = partDistMap.entrySet().iterator();
        Map.Entry<Long, Map<Integer, Map<String, Double>>> entry;
        Long curCmcKey;
        Map<Integer, Map<String, Double>> curCmcValue;
        Map<Integer, Map<String, Double>> final_curCmcValue;
        Iterator<Entry<Integer, Map<String, Double>>> chlIter;
        Map.Entry<Integer, Map<String, Double>> chlEntry;
        Integer curChlKey;
        Map<String, Double> curChlValue;
        Map<String, Double> final_curChlValue;
        // 遍历CCMTS
        while (iter.hasNext()) {
            entry = iter.next();
            curCmcKey = entry.getKey();
            curCmcValue = entry.getValue();
            // 从最终map中去查找是否存在对应CCMTS
            if (finalDistMap.containsKey(curCmcKey)) {
                final_curCmcValue = finalDistMap.get(curCmcKey);
            } else {
                final_curCmcValue = new HashMap<Integer, Map<String, Double>>();
                finalDistMap.put(curCmcKey, final_curCmcValue);
            }
            // 遍历当前CCMTS的各信道
            chlIter = curCmcValue.entrySet().iterator();
            while (chlIter.hasNext()) {
                chlEntry = chlIter.next();
                curChlKey = chlEntry.getKey();
                curChlValue = chlEntry.getValue();
                // 从finalDistMap中查找是否存在对应信道
                if (final_curCmcValue.containsKey(curChlKey)) {
                    final_curChlValue = final_curCmcValue.get(curChlKey);
                } else {
                    final_curChlValue = new HashMap<String, Double>();
                    final_curChlValue.put(AVGSNR, 0d);
                    final_curChlValue.put(LOW_SNR_CMNUM, 0d);
                    final_curChlValue.put(BIG_POWER_CMNUM, 0d);
                    final_curChlValue.put(CHL_WIDTH, curChlValue.get(CHL_WIDTH));
                    final_curChlValue.put(CM_TOTAL, 0d);
                    final_curCmcValue.put(curChlKey, final_curChlValue);
                }
                // 更新当前信道的平均SNR
                final_curChlValue.put(AVGSNR,
                        (final_curChlValue.get(AVGSNR) * final_curChlValue.get(CM_TOTAL) + curChlValue.get(AVGSNR)
                                * curChlValue.get(CM_TOTAL))
                                / (final_curChlValue.get(CM_TOTAL) + curChlValue.get(CM_TOTAL)));
                // 更新当前信道的SNR低于20dB的CM个数
                final_curChlValue.put(LOW_SNR_CMNUM,
                        final_curChlValue.get(LOW_SNR_CMNUM) + curChlValue.get(LOW_SNR_CMNUM));
                // 更新当前信道的发送电平大于115的CM个数
                final_curChlValue.put(BIG_POWER_CMNUM,
                        final_curChlValue.get(BIG_POWER_CMNUM) + curChlValue.get(BIG_POWER_CMNUM));
                // 更新当前信道的CM总个数
                final_curChlValue.put(CM_TOTAL, final_curChlValue.get(CM_TOTAL) + curChlValue.get(CM_TOTAL));
            }
        }
    }

    private List<CcmtsMaintain> convertDistMapToList() {
        List<CcmtsMaintain> ccmtsMaintains= new ArrayList<CcmtsMaintain>();
        
        if(finalDistMap==null){
            return ccmtsMaintains;
        }
        
        // 遍历finalDistMap中的CCMTS
        Iterator<Entry<Long, Map<Integer, Map<String, Double>>>> iter = finalDistMap.entrySet().iterator();
        Map.Entry<Long, Map<Integer, Map<String, Double>>> entry;
        Long curCmcKey;
        Map<Integer, Map<String, Double>> curCmcValue;
        Iterator<Entry<Integer, Map<String, Double>>> chlIter;
        Map.Entry<Integer, Map<String, Double>> chlEntry;
        Map<String, Double> curChlValue;
        CcmtsMaintain ccmtsMaintain;
        while (iter.hasNext()) {
            try{
                entry = iter.next();
                curCmcKey = entry.getKey();
                curCmcValue = entry.getValue();
                // 初始化该CCMTS运维对象
                ccmtsMaintain = new CcmtsMaintain(curCmcKey, collectTime);
                // 遍历该CCMTS下的信道
                chlIter = curCmcValue.entrySet().iterator();
                while (chlIter.hasNext()) {
                    chlEntry = chlIter.next();
                    curChlValue = chlEntry.getValue();
                    // 更新在线且有活动用户的信道总数
                    ccmtsMaintain.increaseAllChlNum();
                    // 判断该信道是否满足平均SNR大于等于20dB的条件，满足则统计CC下满足该条件的信道数
                    if (curChlValue.get(AVGSNR) >= 20) {
                        ccmtsMaintain.increaseAvgSnrStdNum();
                    }
                    // 判断该信道是否满足SNR<20dBCM个数<=6%的条件，满足则统计CC下满足该条件的信道数
                    if (curChlValue.get(LOW_SNR_CMNUM) / curChlValue.get(CM_TOTAL) <= 0.06) {
                        ccmtsMaintain.increaseLowSnrStdNum();
                    }
                    // 判断该信道是否满足发送电平>=115的CM个数<=10%的条件，满足则统计CC下满足该条件的信道数
                    if (curChlValue.get(BIG_POWER_CMNUM) / curChlValue.get(CM_TOTAL) <= 0.1) {
                        ccmtsMaintain.increaseBigPowerStdNum();
                    }
                    // 判断该信道是否满足频宽>=3.2MHz的条件，满足则统计CC下满足该条件的信道数
                    if (curChlValue.get(CHL_WIDTH) >= 3.2) {
                        ccmtsMaintain.increaseChlWidthStdNum();
                    }
                    //判断四个条件是否都满足
                    if(curChlValue.get(AVGSNR) >= 20
                            && (curChlValue.get(LOW_SNR_CMNUM) / curChlValue.get(CM_TOTAL) <= 0.06)
                            && (curChlValue.get(BIG_POWER_CMNUM) / curChlValue.get(CM_TOTAL) <= 0.1)
                            &&curChlValue.get(CHL_WIDTH) >= 3.2){
                        ccmtsMaintain.increaseAllStdNum();
                    }
                }
                ccmtsMaintains.add(ccmtsMaintain);
            }catch(Exception e){
                //转换一个CCMTS异常，继续
                logger.debug("CCMTSMAINTAIN::convert ccmts error:" + e.getMessage());
            }
            
        }
        return ccmtsMaintains;
    }

}
