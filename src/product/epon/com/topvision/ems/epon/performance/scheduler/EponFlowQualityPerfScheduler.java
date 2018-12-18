/***********************************************************************
 * $Id: EponFlowQualityPerfScheduler.java,v1.0 2013-8-7 下午04:35:37 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.scheduler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.ems.epon.performance.domain.EponFlowQualityPerf;
import com.topvision.ems.epon.performance.domain.EponFlowQualityPerfResult;
import com.topvision.ems.epon.performance.engine.OltPerfDao;
import com.topvision.ems.epon.performance.facade.OltFlowQuality;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2013-8-7-下午04:35:37
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("eponFlowQualityPerfScheduler")
public class EponFlowQualityPerfScheduler extends AbstractExecScheduler<EponFlowQualityPerf> {
    @Autowired
    private EngineDaoFactory engineDaoFactory;
    public static String[] GPON_BOARDTYPE = {"11","23"};

    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("EponFlowQualityPerfScheduler  entityId[" + operClass.getEntityId() + "] exec start.");
        }
        try {
            OltPerfDao perfDao = engineDaoFactory.getEngineDao(OltPerfDao.class);
            long entityId = operClass.getEntityId();
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            // TODO Victor 需要验证是否正确
            // Modify by Victor@20150618原来是通过回调接口调用performanceDao对应方法，现在改为engine端OltPerfDao方法
            List<Long> oltSniIndex = perfDao.getOltSniIndexList(entityId);// performanceDao.getOltSniIndex(entityId);
            List<Long> oltPonIndex = perfDao.getOltPonIndexList(entityId);// performanceDao.getOltPonIndex(entityId);
            // TODO 要兼容已经开启的设备,所以只能在原来的机制上进行处理
            List<Long> preSniIndexList = operClass.getSniIndexList();
            if (preSniIndexList != null && !preSniIndexList.isEmpty()) {
                operClass.setSniIndexList(oltSniIndex);
            }
            List<Long> prePonIndexList = operClass.getPonIndexList();
            if (prePonIndexList != null && !prePonIndexList.isEmpty()) {
                operClass.setPonIndexList(oltPonIndex);
            }
            // Add by Rod 增加在Engine端回调记录性能任务执行时间
            try {
                if (operClass.getMonitorId() != null) {
                    getCallback().recordTaskCollectTime(operClass.getMonitorId(),
                            new Timestamp(System.currentTimeMillis()));
                }
            } catch (Exception e) {
                logger.debug("", e);
            }
            List<OltFlowQuality> portFlowPerfs = new ArrayList<OltFlowQuality>();
            // 采集步骤
            Timestamp collectTime = new Timestamp(System.currentTimeMillis());
            try {
                // 数据组织
                if (operClass.getSniIndexList() != null && operClass.getSniIndexList().size() > 0) {
                    // 处理SNI
                    for (Long sniIndex : operClass.getSniIndexList()) {
                        OltFlowQuality sniPerf = new OltFlowQuality();
                        sniPerf.setEntityId(entityId);
                        sniPerf.setPortType("SNI");
                        // sniPerf.setIfIndex(getIfIndexFronPortIndex(sniIndex));
                        sniPerf.setPortIndex(sniIndex);
                        sniPerf.setCollectStatus(true);
                        sniPerf.setCollectTime(collectTime);
                        try {
                            sniPerf = snmpExecutorService.getTableLine(snmpParam, sniPerf);
                            portFlowPerfs.add(sniPerf);
                        } catch (Exception e) {
                            logger.debug(
                                    "[EponFlowQualityPerfScheduler] [GET Sni Port FlowQuality error] happens. [Probably Because {}]. [Probably need to check device using this oid]   [entityId:{}, sniIndex:{}].",
                                    e.getMessage(), entityId, sniIndex);
                        }
                    }
                }
                if (operClass.getPonIndexList() != null && operClass.getPonIndexList().size() > 0) {
                    // 处理PON
                    for (Long ponIndex : operClass.getPonIndexList()) {
                        OltFlowQuality ponPerf = new OltFlowQuality();
                        String boardType=null;
                        String usBandWidth;
                        String dsBandWidth;
                        Long deviceIndex=1L;
                        Long slotNo = EponIndex.getSlotNo(ponIndex);
                        Long portNo = EponIndex.getPonNo(ponIndex);
                        String gponBandWidthIndex = deviceIndex+"."+slotNo+"."+portNo;
                        ponPerf.setPortType("PON");
                        ponPerf.setEntityId(entityId);
                        // ponPerf.setIfIndex(getIfIndexFronPortIndex(ponIndex));
                        ponPerf.setPortIndex(ponIndex);
                        ponPerf.setCollectStatus(true);
                        ponPerf.setCollectTime(collectTime);
                        //需要判断是不是gpon
                        try{
                            boardType = snmpExecutorService.get(snmpParam,"1.3.6.1.4.1.32285.11.2.3.1.3.1.1.3."+slotNo);
                        }catch(Exception e){
                            logger.debug("get boardType falied");
                        }
                        try {
                            ponPerf = snmpExecutorService.getTableLine(snmpParam, ponPerf);
                            if(Arrays.asList(GPON_BOARDTYPE).contains(boardType)){
                                //如果是gpon
                                usBandWidth = snmpExecutorService.get(snmpParam,"1.3.6.1.4.1.17409.2.3.3.1.1.18."+gponBandWidthIndex);
                                dsBandWidth = snmpExecutorService.get(snmpParam,"1.3.6.1.4.1.17409.2.3.3.1.1.12."+gponBandWidthIndex);
                                ponPerf.setUsBandwidth(Long.parseLong(usBandWidth));
                                ponPerf.setDsBandwidth(Long.parseLong(dsBandWidth));
                            }else{
                                //不是gpon
                                ponPerf.setUsBandwidth(ponPerf.getIfHighSpeed());
                                ponPerf.setDsBandwidth(ponPerf.getIfHighSpeed());
                            }
                            portFlowPerfs.add(ponPerf);
                        } catch (Exception e) {
                            logger.debug(
                                    "[EponFlowQualityPerfScheduler] [GET Pon Port OltFlowQuality error] happens. [Probably Because {}]. [Probably need to check device using this oid]   [entityId:{}, ponIndex:{}].",
                                    e.getMessage(), entityId, ponIndex);
                        }
                    }
                }

                /*
                 * for (OltFlowQuality perf : portFlowPerfs) { perf.setCollectStatus(true);
                 * perf.setEntityId(entityId); perf.setCollectTime(collectTime); try { perf =
                 * snmpExecutorService.getTableLine(snmpParam, perf); } catch
                 * (SnmpNoSuchInstanceException e) { perf.setCollectStatus(false); } }
                 */
            } catch (Exception e) {
                logger.debug("", e);
            }
            logger.trace("EponFlowQualityPerfScheduler write result to file.");
            EponFlowQualityPerfResult eponFlowQualityPerfResult = new EponFlowQualityPerfResult(operClass);
            eponFlowQualityPerfResult.setEntityId(entityId);
            eponFlowQualityPerfResult.setFlowPerfs(portFlowPerfs);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(eponFlowQualityPerfResult);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("EponFlowQualityPerfScheduler exec end.");
    }

    private Long getIfIndexFronPortIndex(Long portIndex) {
        return ((portIndex & 0xFF00000000L) >> 5) | ((portIndex & 0xFF000000L) >> 1) | (portIndex & 0xBF0000L);
    }
}
