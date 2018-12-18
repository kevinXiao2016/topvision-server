/***********************************************************************
 * $Id: DispersionService.java,v1.0 2015-3-12 下午2:13:52 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.dispersion.service.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.topvision.ems.cm.dispersion.dao.DispersionDao;
import com.topvision.ems.cm.dispersion.domain.Dispersion;
import com.topvision.ems.cm.dispersion.facade.DispersionFacade;
import com.topvision.ems.cm.dispersion.service.DispersionService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author fanzidong
 * @created @2015-3-12-下午2:13:52
 * 
 */
@Service("dispersionService")
public class DispersionServiceImpl extends BaseService implements DispersionService, CmPollStateListener {

    @Autowired
    private MessageService messageService;
    @Autowired
    private DispersionDao dispersionDao;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;

    private Map<Long, Map<String, Map<Integer, Integer>>> finalDistMap;

    private Timestamp collectTime;

    private DecimalFormat df = new DecimalFormat("##.00");

    @PostConstruct
    public void init() {
        // 注册监听器
        messageService.addListener(CmPollStateListener.class, this);
    }

    @Override
    public void startRoundStatistics(CmPollStateEvent event) {
        // 一轮采集处理开始，初始化数据
        finalDistMap = new HashMap<Long, Map<String, Map<Integer, Integer>>>();
        Date time = new Date();
        collectTime = new Timestamp(time.getTime());
    }

    @Override
    public void completeRoundStatistics(CmPollStateEvent event) {
        try {
            // 获取engine列表
            List<EngineServer> engineServers = facadeFactory.getAllEngineServer();
            logger.debug("Dispersion::loaded engineServers");
            // 遍历engine
            DispersionFacade dispersionFacade;
            Map<Long, Map<String, Map<Integer, Integer>>> partDistMap;
            for (EngineServer engineServer : engineServers) {
                if (engineServer.getLinkStatus() == EngineServer.CONNECTED) {
                    // 取到离散度分布数据
                    try {
                        dispersionFacade = facadeFactory.getFacade(engineServer, DispersionFacade.class);
                        partDistMap = dispersionFacade.getDistributionData();
                        logger.debug("Dispersion::loaded engine distributionData");
                        // 合并入本轮map中
                        mergeDistMap(partDistMap);
                        logger.debug("Dispersion::merged engine distMap");
                    } catch (Exception e) {
                        // 本采集器数据处理异常，跳过
                        logger.debug("Dispersion::caculate engine data error:" + e.getMessage());
                        continue;
                    }
                }
            }
            // 将最终的分布map转成Dispersion的list
            List<Dispersion> dispersions = convertDistMapToList();
            logger.debug("Dispersion::converted distMapToList");
            // 入库
            dispersionDao.batchInsertDispersions(dispersions);
        } catch (Exception e) {
            logger.debug("Dispersion::current round caculate error, failed to insert data:" + e.getMessage());
        } finally {
            // distory操作
            finalDistMap = null;
            collectTime = null;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.dispersion.service.DispersionService#getDispersionList(java.util.Map)
     */
    @Override
    public List<Dispersion> getDispersionList(Map<String, Object> queryMap) {
        List<Dispersion> dispersions = dispersionDao.selectDispersionList(queryMap);
        // 对需要展示内容进行处理
        // 地域国际化资源对象
        ResourceManager rm = ResourceManager.getResourceManager("com.topvision.ems.report.resources");
        for (Dispersion dispersion : dispersions) {
            // 地域名称需要做国际化处理
            dispersion.setFolderNames(translateStringArray(dispersion.getFolderNames(), rm));
        }
        return dispersions;
    }

    @Override
    public Dispersion getDispersionById(Long opticalNodeId) {
        Dispersion dispersion = dispersionDao.getDispersionById(opticalNodeId);
        return dispersion;
    }

    @Override
    public Integer getDispersionListNum(Map<String, Object> queryMap) {
        return dispersionDao.getDispersionListNum(queryMap);
    }

    @Override
    public List<Dispersion> selectDispersionsByIdAndRange(Long opticalId, String startTime, String endTime) {
        return dispersionDao.selectDispersionsByIdAndRange(opticalId, startTime, endTime);
    }

    @Override
    public Dispersion loadDispersionByIdAndTime(Long opticalNodeId, String exactTime) {
        return dispersionDao.selectDispersionByIdAndTime(opticalNodeId, exactTime);
    }

    /**
     * 将从engine端拿到的部分分布数据合并入总数据中
     * 
     * @param partDistMap
     * @return
     */
    private void mergeDistMap(Map<Long, Map<String, Map<Integer, Integer>>> partDistMap) {
        // 遍历从engine获取的数据
        Iterator<Entry<Long, Map<String, Map<Integer, Integer>>>> opticalIter = partDistMap.entrySet().iterator();
        Map.Entry<Long, Map<String, Map<Integer, Integer>>> entry;
        Long curOpticalKey;
        Map<String, Map<Integer, Integer>> curOpticalValue;
        Map<String, Map<Integer, Integer>> final_curOpticalValue;
        Map<Integer, Integer> curUpSnr;
        Map<Integer, Integer> curUpPower;
        Map<Integer, Integer> final_curUpSnr;
        Map<Integer, Integer> final_curUpPower;
        while (opticalIter.hasNext()) {
            entry = (Map.Entry<Long, Map<String, Map<Integer, Integer>>>) opticalIter.next();
            curOpticalKey = entry.getKey();
            curOpticalValue = entry.getValue();
            // 从最终map中去查找是否存在对应光节点
            if (finalDistMap.containsKey(curOpticalKey)) {
                final_curOpticalValue = finalDistMap.get(curOpticalKey);
            } else {
                final_curOpticalValue = new HashMap<String, Map<Integer, Integer>>();
                final_curOpticalValue.put(Dispersion.UP_SNR, new HashMap<Integer, Integer>());
                final_curOpticalValue.put(Dispersion.UP_POWER, new HashMap<Integer, Integer>());
                finalDistMap.put(curOpticalKey, final_curOpticalValue);
            }
            // 当前光节点数据的snr数据和power数据
            curUpSnr = curOpticalValue.get(Dispersion.UP_SNR);
            curUpPower = curOpticalValue.get(Dispersion.UP_POWER);
            // 合入目标的snr数据和power数据
            final_curUpSnr = final_curOpticalValue.get(Dispersion.UP_SNR);
            final_curUpPower = final_curOpticalValue.get(Dispersion.UP_POWER);
            // 将curUpSnr数据合入final_curUpSnr中
            Iterator<Entry<Integer, Integer>> snrIter = curUpSnr.entrySet().iterator();
            while (snrIter.hasNext()) {
                Map.Entry<Integer, Integer> snrEntry = (Map.Entry<Integer, Integer>) snrIter.next();
                Integer curSnrKey = snrEntry.getKey();
                Integer curSnrValue = snrEntry.getValue();
                // 从finalMap中找到对应的snrKey
                Integer final_curSnrValue;
                if (final_curUpSnr.containsKey(curSnrKey)) {
                    final_curSnrValue = final_curUpSnr.get(curSnrKey);
                } else {
                    final_curSnrValue = 0;
                }
                final_curUpSnr.put(curSnrKey, final_curSnrValue + curSnrValue);
            }
            // 将curUpPower数据合入final_curUpPower中
            Iterator<Entry<Integer, Integer>> powerIter = curUpPower.entrySet().iterator();
            while (powerIter.hasNext()) {
                Map.Entry<Integer, Integer> powerEntry = (Map.Entry<Integer, Integer>) powerIter.next();
                Integer curPowerKey = powerEntry.getKey();
                Integer curPowerValue = powerEntry.getValue();
                // 从finalMap中找到对应的snrKey
                Integer final_curPowerValue;
                if (final_curUpPower.containsKey(curPowerKey)) {
                    final_curPowerValue = final_curUpPower.get(curPowerKey);
                } else {
                    final_curPowerValue = 0;
                }
                final_curUpPower.put(curPowerKey, final_curPowerValue + curPowerValue);
            }
        }
    }

    /**
     * 将最终的分布map转成Dispersion的list
     * 
     * @param finalMap
     * @return
     */
    private List<Dispersion> convertDistMapToList() {
        logger.debug("Dispersion::begin convertDistMapToList");
        List<Dispersion> dispersions = new ArrayList<Dispersion>();
        // 遍历每一个光节点
        Dispersion curDispersion;
        Iterator<Entry<Long, Map<String, Map<Integer, Integer>>>> opticalIter = finalDistMap.entrySet().iterator();
        Map.Entry<Long, Map<String, Map<Integer, Integer>>> entry;
        Long curOpticalKey;
        Map<String, Map<Integer, Integer>> curOpticalValue;
        Map<Integer, Integer> curUpSnrs;
        Map<Integer, Integer> curUpPowers;
        List<int[]> formatedUpSnr;
        List<int[]> formatedUpPower;
        while (opticalIter.hasNext()) {
            try{
                entry = (Map.Entry<Long, Map<String, Map<Integer, Integer>>>) opticalIter.next();
                curOpticalKey = entry.getKey();
                curOpticalValue = entry.getValue();
                logger.debug("Dispersion::begin convertDistMapToList:device:" + curOpticalKey);
                // 获取当前光节点的upsnr分布情况和uppower分布情况
                curUpSnrs = curOpticalValue.get(Dispersion.UP_SNR);
                curUpPowers = curOpticalValue.get(Dispersion.UP_POWER);
                // 转换成排序过的二维数组形式
                formatedUpSnr = formatDistribution(curUpSnrs);
                formatedUpPower = formatDistribution(curUpPowers);
                logger.debug("Dispersion::formated current device:" + curOpticalKey);
                // 构造对象并放入队列
                curDispersion = new Dispersion();
                curDispersion.setCollectTime(collectTime);
                curDispersion.setOpticalNodeId(curOpticalKey);
                logger.debug("Dispersion::begin encapeDispersion");
                encapeDispersion(curDispersion, formatedUpSnr, formatedUpPower);
                logger.debug("Dispersion::end encapeDispersion");
                dispersions.add(curDispersion);
            }catch(Exception e){
                logger.debug("Dispersion::error in convertOneDistMapToList");
            }
        }
        logger.debug("Dispersion::end convertDistMapToList");
        return dispersions;
    }

    /**
     * 将map格式的分布情况转换成排序后的数组格式
     * 
     * @param curDistribution
     * @return
     */
    private List<int[]> formatDistribution(Map<Integer, Integer> curDistribution) {
        logger.debug("Dispersion::begin formatDistribution");
        List<int[]> retList = new ArrayList<int[]>();
        
        if(curDistribution==null){
            return retList;
        }

        Iterator<Entry<Integer, Integer>> distIter = curDistribution.entrySet().iterator();
        while (distIter.hasNext()) {
            Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) distIter.next();
            Integer curKey = entry.getKey();
            Integer curValue = entry.getValue();
            int[] curPoint = { curKey.intValue(), curValue.intValue() };
            retList.add(curPoint);
        }
        Collections.sort(retList, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });
        // 在此去掉为0的数据
        if (retList.size() > 0 && retList.get(0)[0] == 0) {
            retList.remove(0);
        }
        logger.debug("Dispersion::end formatDistribution");
        return retList;
    }

    /**
     * 封装对应光节点数据
     * 
     * @param dispersion
     * @param upSnr
     * @param upPower
     */
    private void encapeDispersion(Dispersion dispersion, List<int[]> upSnr, List<int[]> upPower) {
        logger.debug("Dispersion::begin encapeDispersion");
        int snrCmNum = 0;
        double numerator = 0;
        double upSnrAvg = 0d;
        double upSnrStd = 0d;
        StringBuilder upSnrDist = new StringBuilder();
        // 计算upsnr的平均值和标准差
        for (int[] point : upSnr) {
            snrCmNum += point[1];
            numerator += point[0] * point[1];
            upSnrDist.append(String.format("%d,%d;", point[0], point[1]));
        }
        if(snrCmNum == 0){
            logger.debug("Dispersion::snrCmNum is 0");
        }else{
            upSnrAvg = numerator / snrCmNum;
        }
        numerator = 0;
        for (int[] point : upSnr) {
            numerator += Math.pow(point[0] - upSnrAvg, 2) * point[1];
        }
        if(snrCmNum == 0){
            logger.debug("Dispersion::snrCmNum is 0");
        }else{
            upSnrStd = Math.sqrt(numerator / snrCmNum);
        }

        // 计算uppower的平均值和标准差
        int powerCmNum = 0;
        double upPowerAvg = 0d;
        double upPowerStd = 0d;
        numerator = 0;
        StringBuilder upPowerDist = new StringBuilder();
        for (int[] point : upPower) {
            powerCmNum += point[1];
            numerator += point[0] * point[1];
            upPowerDist.append(String.format("%d,%d;", point[0], point[1]));
        }
        // 计算上行电平平均值
        if(powerCmNum == 0){
            logger.debug("Dispersion::powerCmNum is 0");
        }else{
            upPowerAvg = numerator / powerCmNum;
        }
        // 计算上行电平标准差
        numerator = 0;
        for (int[] point : upPower) {
            numerator += Math.pow(point[0] - upPowerAvg, 2) * point[1];
        }
        if(powerCmNum == 0){
            logger.debug("Dispersion::powerCmNum is 0");
        }else{
            upPowerStd = Math.sqrt(numerator / powerCmNum);
        }

        // 封装数据
        dispersion.setCmNum(Math.max(snrCmNum, powerCmNum));
        dispersion.setUpSnrAvg(Double.valueOf(df.format(upSnrAvg)));
        dispersion.setUpSnrStd(Double.valueOf(df.format(upSnrStd)));
        dispersion.setUpSnrDist(upSnrDist.toString());
        dispersion.setUpPowerAvg(Double.valueOf(df.format(upPowerAvg)));
        dispersion.setUpPowerStd(Double.valueOf(df.format(upPowerStd)));
        dispersion.setUpPowerDist(upPowerDist.toString());
        logger.debug("Dispersion::end encapeDispersion");
    }

    /**
     * 字符串数组的国际化
     * 
     * @param names
     * @param rm
     * @return
     */
    private String translateStringArray(String names, ResourceManager rm) {
        if (names == null || names == "") {
            return "";
        }
        StringBuilder ret = new StringBuilder("");
        String[] nameArr = names.split(",");
        for (int i = 0, len = nameArr.length; i < len; i++) {
            ret.append(rm.getNotNullString(nameArr[i])).append(",");
        }
        return ret.substring(0, ret.length() - 1);
    }
}
