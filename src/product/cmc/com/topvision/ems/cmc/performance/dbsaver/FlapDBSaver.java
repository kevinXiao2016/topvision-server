package com.topvision.ems.cmc.performance.dbsaver;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.perf.engine.CmFlapDao;
import com.topvision.ems.cmc.performance.domain.CMFlapHis;
import com.topvision.ems.cmc.performance.domain.CmFlapMonitor;
import com.topvision.ems.cmc.performance.domain.FlapResult;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.cmc.performance.handle.CmcCMFlapInsFailGrowHandle;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;

/**
 * 定时采集器返回数据后，调用这个server将数据保存到数据库
 * 
 * @author smsx
 * 
 */
@Engine("flapDBSaver")
public class FlapDBSaver extends NbiSupportEngineSaver implements PerfEngineSaver<FlapResult, OperClass> {
    public static Map<Long, Map<String, CMFlapHis>> lastStaticMap = Collections
            .synchronizedMap(new HashMap<Long, Map<String, CMFlapHis>>());
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(FlapResult performanceResult) {
        CmFlapDao flapDao = engineDaoFactory.getEngineDao(CmFlapDao.class);
        Long entityId = performanceResult.getEntityId();
        FlapResult cmFlapResult = performanceResult;
        List<CmFlap> cmFlapList = cmFlapResult.getCmFlapList();
        Long collectFinishTime = cmFlapResult.getCollectFinishTime();
        Timestamp time = new Timestamp(collectFinishTime);

        List<PerformanceData> perfDataList = new ArrayList<PerformanceData>();
        CmFlapMonitor perf = (CmFlapMonitor) performanceResult.getDomain();

        /**
         * 处理已经不存在的CM
         */
        if (lastStaticMap.get(entityId) != null) {
            Iterator<String> it = lastStaticMap.get(entityId).keySet().iterator();
            while (it.hasNext()) {
                String oldKey = it.next();
                CMFlapHis flapHis = (CMFlapHis) lastStaticMap.get(entityId).get(oldKey);
                boolean cmExist = false;
                for (CmFlap flap : cmFlapList) {
                    if (oldKey.equals(flap.getTopCmFlagMacAddrString())) {
                        cmExist = true;
                        continue;
                    }
                }
                if (!cmExist) {
                    flapHis.setIncreaseInsNum(0);
                    PerformanceData cmflapData = new PerformanceData(entityId,
                            CmcCMFlapInsFailGrowHandle.CC_CMFLAP_INSFAILGROW_FLAG, flapHis);
                    perfDataList.add(cmflapData);
                    it.remove();
                }
            }
        }

        List<CMFlapHis> flapHisList = new ArrayList<CMFlapHis>();
        for (CmFlap flap : cmFlapList) {
            CMFlapHis flapHis = new CMFlapHis();
            flapHis.setCmcId(entityId);
            flapHis.setCmMac(flap.getTopCmFlagMacAddrString());
            flapHis.setCollectTime(time);
            flapHis.setLastFlapTime(flap.getTopCmFlapLastFlapTime());
            flapHis.setInsFailNum(flap.getTopCmFlapInsertionFailNum());
            flapHis.setHitNum(flap.getTopCmFlapHitNum());
            flapHis.setMissNum(flap.getTopCmFlapMissNum());
            flapHis.setCrcErrorNum(flap.getTopCmFlapCrcErrorNum());
            flapHis.setPowerAdjHigherNum(flap.getTopCmFlapPowerAdjHigherNum());
            flapHis.setPowerAdjLowerNum(flap.getTopCmFlapPowerAdjLowerNum());

            String key = new StringBuilder(flap.getTopCmFlagMacAddrString()).toString();
            CMFlapHis lastFlap = null;
            if (lastStaticMap.get(entityId) != null) {
                lastFlap = lastStaticMap.get(entityId).get(key);
            } else {
                lastStaticMap.put(entityId, new HashMap<String, CMFlapHis>());
            }
            Map<String, CMFlapHis> cmflapMap = lastStaticMap.get(entityId);
            cmflapMap.put(key, flapHis);
            if (lastFlap == null) {
                continue;
            }

            /* 添加需要计算的值 */
            // 测距命中率
            // 这一句好像写错了 getHit getMiss YangYi mark
            int nowN = flapHis.getHitNum() + flapHis.getMissNum();
            int increaseTotal = flapHis.getHitNum() + flapHis.getMissNum() - lastFlap.getHitNum()
                    - lastFlap.getMissNum();// //flap.getTopCmFlapHitNum()+flap.getTopCmFlapMissNum()lastFlap.getTopCmFlapHitNum()-lastFlap.getTopCmFlapMissNum();
            float hitPercentInc = 0f;
            if (increaseTotal > 0) {
                hitPercentInc = (flapHis.getHitNum() - lastFlap.getHitNum()) * 100 / increaseTotal;
            } else if (nowN != 0) {
                hitPercentInc = flapHis.getHitNum() * 100 / nowN;
            }
            flapHis.setIncreaseHitPercent(hitPercentInc);

            // 电平抖动次数 //自动调整电平次数统计
            nowN = flapHis.getPowerAdjHigherNum() + flapHis.getPowerAdjLowerNum();
            increaseTotal = flapHis.getPowerAdjHigherNum() + flapHis.getPowerAdjLowerNum()
                    - lastFlap.getPowerAdjHigherNum() - lastFlap.getPowerAdjLowerNum();

            int powAdj = 0;
            if (increaseTotal > 0) {
                powAdj = increaseTotal;
            }
            flapHis.setIncreasePowerAdjNum(powAdj);

            // 异常上线次数
            int insFail = flapHis.getInsFailNum() - lastFlap.getInsFailNum();
            if (insFail >= 0) {
                flapHis.setIncreaseInsNum(insFail);

                flapHisList.add(flapHis);

                PerformanceData cmflapData = new PerformanceData(entityId,
                        CmcCMFlapInsFailGrowHandle.CC_CMFLAP_INSFAILGROW_FLAG, flapHis);
                perfDataList.add(cmflapData);
            }
        }

        if (flapHisList.size() > 0) {
            if (perf.getIsInsGrow()) {
                flapDao.batchInsertCmFlapHis(flapHisList, entityId);
            }
        }
        getCallback(PerformanceCallback.class).sendPerfomaceResult(perfDataList);
    }
}