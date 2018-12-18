/***********************************************************************
 * $Id: CmtsInfoSummaryServiceImpl.java,v1.0 2017年9月12日 上午10:37:42 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmtsInfoSummary.service.impl;

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

import com.topvision.ems.cm.cmpoll.message.CmPollStateEvent;
import com.topvision.ems.cm.cmpoll.message.CmPollStateListener;
import com.topvision.ems.cm.cmtsInfoSummary.dao.CmtsInfoSummaryMaintainDao;
import com.topvision.ems.cm.cmtsInfoSummary.domain.CmtsInfoMaintain;
import com.topvision.ems.cm.cmtsInfoSummary.engine.dao.CmtsInfoSummaryDao;
import com.topvision.ems.cm.cmtsInfoSummary.facade.CmtsInfoSummaryFacade;
import com.topvision.ems.cm.cmtsInfoSummary.service.CmtsInfoSummaryService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author ls
 * @created @2017年9月12日-上午10:37:42
 *
 */

@Service("cmtsInfoSummaryService")
public class CmtsInfoSummaryServiceImpl extends BaseService implements CmtsInfoSummaryService, CmPollStateListener {

    @Autowired
    private MessageService messageService;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;
    @Autowired
    private CmtsInfoSummaryMaintainDao cmtsInfoSummaryMaintainDao;
    private Timestamp collectTime;
    private Map<Long, Map<String, Double>> finalCmtsDistMap;
    public static final String AVGUPSNR = "avgUpSnr";
    public static final String AVGUPPOWER = "avgUpPower";
    public static final String AVGDOWNPOWER = "avgDownPower";
    public static final String AVGDOWNSNR = "avgDownSnr";
    public static final String CM_TOTAL = "cmTotal";
    public static final String UPSNROUTRANGE = "upSnrOutRange";
    public static final String UPPOWEROUTRANGE = "upPowerOutRange";
    public static final String DOWNPOWEROUTRANGE = "downPowerOutRange";
    public static final String DOWNSNROUTRANGE = "downSnrOutRange";
    public static final String ONLINE_CM_NUM = "onlineCmNum";

    @PostConstruct
    public void init() {
        // 注册监听器
        messageService.addListener(CmPollStateListener.class, this);
    }

    @Override
    public void completeRoundStatistics(CmPollStateEvent event) {
        try {
            // 获取engine列表
            List<EngineServer> engineServers = facadeFactory.getAllEngineServer();
            // 遍历engine
            CmtsInfoSummaryFacade cmtsInfoSummaryFacade;
            Map<Long, Map<String, Double>> partDistMap;
            logger.info("engineServerSize="+engineServers.size());
            for (EngineServer engineServer : engineServers) {
                if (engineServer.getLinkStatus() == EngineServer.CONNECTED) {
                    // 取到各个指标数据
                    try {
                        logger.info("show cmtsSummaryEngineIp:"+engineServer.getIp()+engineServer.getPort());
                        cmtsInfoSummaryFacade = facadeFactory.getFacade(engineServer, CmtsInfoSummaryFacade.class);
                        partDistMap = cmtsInfoSummaryFacade.getCmtsInfoDist();
                        logger.info("show cmtsInfoPartDistMap:"+engineServer.getIp()+engineServer.getPort()+partDistMap);
                        // 合并入本轮map中
                        mergeCmtsDistMap(partDistMap);
                    } catch (Exception e) {
                        // 本采集器数据处理异常，跳过
                        logger.debug("cmtsInfo::caculate engine data error:" + e.getMessage());
                        continue;
                    }
                }
            }
            logger.info("show cmtsInfoFinalCmtsDistMap:"+finalCmtsDistMap);
            // 根据最终的map计算要存储的数据
            List<CmtsInfoMaintain> cmtsInfoMaintain = new ArrayList<CmtsInfoMaintain>();
            try {
                cmtsInfoMaintain = transMapToArray();
                logger.info("cmtsFinalInfo:"+cmtsInfoMaintain);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.debug("cmtsInfo::transMapToArray");
            // 存数据库
            if(cmtsInfoMaintain.size()!=0){
                logger.debug("cmtsInfo::insertIntoDB");
                cmtsInfoSummaryMaintainDao.batchInsertCmtsInfoMaintainLast(cmtsInfoMaintain);
                cmtsInfoSummaryMaintainDao.batchInsertCmtsInfoMaintain(cmtsInfoMaintain);
                logger.debug("cmtsInfo::insertDBOver"); 
            }
        } catch (Exception e) {
            logger.debug("cmtsInfo::current round caculate error, failed to insert data:" + e.getMessage());
        } finally {
            logger.info("end cmtsInfoSummaryJob");
            finalCmtsDistMap = null;
        }
    }

    @Override
    public void startRoundStatistics(CmPollStateEvent event) {
        logger.info("start cmtsInfoSummaryJob");
        finalCmtsDistMap = new HashMap<Long, Map<String, Double>>();
        Date time = new Date();
        collectTime = new Timestamp(time.getTime());
    }

    private void mergeCmtsDistMap(Map<Long, Map<String, Double>> map) {
        Iterator<Entry<Long, Map<String, Double>>> iter = map.entrySet().iterator();
        Map.Entry<Long, Map<String, Double>> entry;
        Long cmcIdKey;
        Map<String, Double> curCmcInfo;
        Map<String, Double> finalCmcInfo;
        while (iter.hasNext()) {
            entry = iter.next();
            cmcIdKey = entry.getKey();
            curCmcInfo = entry.getValue();
            if (finalCmtsDistMap.containsKey(cmcIdKey)) {
                finalCmcInfo = finalCmtsDistMap.get(cmcIdKey);
            } else {
                finalCmcInfo = new HashMap<String, Double>();
                finalCmcInfo.put(AVGUPSNR, 0d);
                finalCmcInfo.put(AVGUPPOWER, 0d);
                finalCmcInfo.put(AVGDOWNPOWER, 0d);
                finalCmcInfo.put(AVGDOWNSNR, 0d);
                finalCmcInfo.put(CM_TOTAL, 0d);
                finalCmcInfo.put(UPSNROUTRANGE, 0d);
                finalCmcInfo.put(UPPOWEROUTRANGE, 0d);
                finalCmcInfo.put(DOWNPOWEROUTRANGE, 0d);
                finalCmcInfo.put(DOWNSNROUTRANGE, 0d);
                finalCmcInfo.put(ONLINE_CM_NUM, 0d);
                finalCmtsDistMap.put(cmcIdKey, finalCmcInfo);
            }
            finalCmcInfo.put(
                    AVGUPSNR,
                    (curCmcInfo.get(AVGUPSNR) * curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(AVGUPSNR)
                            * finalCmcInfo.get(CM_TOTAL))
                            / (curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(CM_TOTAL)));
            finalCmcInfo.put(
                    AVGUPPOWER,
                    (curCmcInfo.get(AVGUPPOWER) * curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(AVGUPPOWER)
                            * finalCmcInfo.get(CM_TOTAL))
                            / (curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(CM_TOTAL)));
            finalCmcInfo.put(
                    AVGDOWNPOWER,
                    (curCmcInfo.get(AVGDOWNPOWER) * curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(AVGDOWNPOWER)
                            * finalCmcInfo.get(CM_TOTAL))
                            / (curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(CM_TOTAL)));
            finalCmcInfo.put(
                    AVGDOWNSNR,
                    (curCmcInfo.get(AVGDOWNSNR) * curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(AVGDOWNSNR)
                            * finalCmcInfo.get(CM_TOTAL))
                            / (curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(CM_TOTAL)));
            finalCmcInfo.put(
                    UPSNROUTRANGE,
                    (curCmcInfo.get(UPSNROUTRANGE) * curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(UPSNROUTRANGE)
                            * finalCmcInfo.get(CM_TOTAL))
                            / (curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(CM_TOTAL)));
            finalCmcInfo.put(
                    UPPOWEROUTRANGE,
                    (curCmcInfo.get(UPPOWEROUTRANGE) * curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(UPPOWEROUTRANGE)
                            * finalCmcInfo.get(CM_TOTAL))
                            / (curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(CM_TOTAL)));
            finalCmcInfo.put(
                    DOWNPOWEROUTRANGE,
                    (curCmcInfo.get(DOWNPOWEROUTRANGE) * curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(DOWNPOWEROUTRANGE)
                            * finalCmcInfo.get(CM_TOTAL))
                            / (curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(CM_TOTAL)));
            finalCmcInfo.put(
                    DOWNSNROUTRANGE,
                    (curCmcInfo.get(DOWNSNROUTRANGE) * curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(DOWNSNROUTRANGE)
                            * finalCmcInfo.get(CM_TOTAL))
                            / (curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(CM_TOTAL)));
            finalCmcInfo.put(ONLINE_CM_NUM,curCmcInfo.get(ONLINE_CM_NUM) + finalCmcInfo.get(ONLINE_CM_NUM));
            finalCmcInfo.put(CM_TOTAL,curCmcInfo.get(CM_TOTAL) + finalCmcInfo.get(CM_TOTAL));
        }
    }
    
    private List<CmtsInfoMaintain> transMapToArray(){
        List<CmtsInfoMaintain> cmtsInfoMaintains = new ArrayList<CmtsInfoMaintain>();
        if(finalCmtsDistMap!=null){
            Iterator<Entry<Long, Map<String, Double>>> iter = finalCmtsDistMap.entrySet().iterator();
            Map.Entry<Long, Map<String, Double>> entry;
            Long cmcIdKey;
            Map<String, Double> curCmcInfo;
            CmtsInfoMaintain cim;
            while(iter.hasNext()){
                entry = iter.next();
                cmcIdKey = entry.getKey();
                curCmcInfo = entry.getValue();
                cim=new CmtsInfoMaintain(cmcIdKey,collectTime);
                cim.setCm_online(curCmcInfo.get(ONLINE_CM_NUM));
                cim.setCm_total(curCmcInfo.get(CM_TOTAL));
                cim.setUpChannelSnrAvg(curCmcInfo.get(AVGUPSNR));
                cim.setUpChannelSnrOutRange(curCmcInfo.get(UPSNROUTRANGE));
                cim.setUpChannelTxAvg(curCmcInfo.get(AVGUPPOWER));
                cim.setUpChannelTxOutRange(curCmcInfo.get(UPPOWEROUTRANGE));
                cim.setDownChannelSnrAvg(curCmcInfo.get(AVGDOWNSNR));
                cim.setDownChannelSnrOutRange(curCmcInfo.get(DOWNSNROUTRANGE));
                cim.setDownChannelTxAvg(curCmcInfo.get(AVGDOWNPOWER));
                cim.setDownChannelTxOutRange(curCmcInfo.get(DOWNPOWEROUTRANGE));
                cmtsInfoMaintains.add(cim);
            }  
        }
        return cmtsInfoMaintains;
    }
}
