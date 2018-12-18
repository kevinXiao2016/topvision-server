/***********************************************************************
 * $Id: RealtimeCmListServiceImpl.java,v1.0 2014年5月11日 下午2:48:55 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.dao.CmDao;
import com.topvision.ems.cmc.cm.domain.RealtimeCm;
import com.topvision.ems.cmc.cm.domain.RealtimeCmSignal;
import com.topvision.ems.cmc.cm.domain.RealtimeCmUpSnr;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.cm.service.RealtimeCmListService;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.remotequerycm.dao.CmRemoteQueryDao;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3DsRemoteQuery;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3UsRemoteQuery;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.cmc.util.RealtimeCmUtil;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.dao.PerfThresholdDao;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author YangYi
 * @created @2014年5月11日-下午2:48:55
 * 
 */

@Service("realtimeCmListService")
public class RealtimeCmListServiceImpl extends CmcBaseCommonService implements RealtimeCmListService {
    @Resource(name = "cmDao")
    private CmDao cmDao;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "cmService")
    private CmService cmService;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Resource(name = "entityTypeService")
    private EntityTypeService entityTypeService;
    @Resource(name = "snmpExecutorService")
    private SnmpExecutorService snmpExecutorService;
    @Resource(name = "perfThresholdDao")
    private PerfThresholdDao perfThresholdDao;
    @Resource(name = "cmRemoteQueryDao")
    private CmRemoteQueryDao cmRemoteQueryDao;

    @Override
    public List<RealtimeCm> getRealtimeCmAttributeByCmcId(Long cmcId, List<Long> cmIndexs, String[] status) {
        List<RealtimeCm> cmList = new ArrayList<RealtimeCm>();
        if (cmIndexs != null && cmIndexs.size() > 0 && cmIndexs.get(0).equals(-1l)
                && status[0].equals("JUMPSTATUS_ALLSHOW")) {// 指标区间显示0时，标记为-1
            return cmList;
        }
        Long entityId = cmcService.getEntityIdByCmcId(cmcId);
        SnmpParam snmpParam = getSnmpParamByEntityId(entityId);
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
        Long cmcNextIndex = CmcIndexUtils.getNextCmcIndex(cmcIndex);
        List<RealtimeCm> cmAttributeList = getCmFacade(snmpParam.getIpAddress()).getRealtimeCmOfSingleCC(snmpParam,
                cmcIndex, cmcNextIndex);
        if (cmIndexs != null && cmIndexs.size() > 0
                && (status == null || status[0].equals("") || status[0].equals("JUMPSTATUS_ALLSHOW"))) { // 电平异常、SNR异常传index过来查询
            for (Long cmIndex : cmIndexs) {
                for (RealtimeCm cm : cmAttributeList) {
                    if (cmIndex.equals(cm.getStatusIndex())) {
                        this.addCmIntoList(cm, cmList, cmcId);
                    }
                }
            }
        } else if (status != null && !status[0].equals("")) {// 选择了状态
            for (RealtimeCm cm : cmAttributeList) {
                if (status.length == 1) {// cmts实时信息页面跳转过来时传递的一个参数或筛选时选择单个状态
                    if (status[0].equals("JUMPSTATUS_ALLSHOW")) {// 所有cm
                        this.addCmIntoList(cm, cmList, cmcId);
                    } else if (status[0].equals("JUMPSTATUS_ONLINE")) {// 在线cm
                        if (CmAttribute.isCmOnline(cm.getStatusValue())) {
                            this.addCmIntoList(cm, cmList, cmcId);
                        }
                    } else if (status[0].equals("JUMPSTATUS_OFFLINE")) {// 下线cm
                        if (CmAttribute.isCmOffline(cm.getStatusValue())) {
                            this.addCmIntoList(cm, cmList, cmcId);
                        }
                    } else if (status[0].equals("JUMPSTATUS_ONLINING")) {// 上线中cm
                        if (!CmAttribute.isCmOnline(cm.getStatusValue())
                                && !CmAttribute.isCmOffline(cm.getStatusValue())) {
                            this.addCmIntoList(cm, cmList, cmcId);
                        }
                    } else if (status[0].equals(RealtimeCmUtil.STATUS_ELSE)) {
                        if (!RealtimeCmUtil.cmStatusTrans("JUMPSTATUS_ALLSHOW")
                                .contains(cm.getStatusValue().toString())) {
                            this.addCmIntoList(cm, cmList, cmcId);
                        }
                    } else {
                        if (cm.getStatusValue().equals(Integer.parseInt(status[0]))) {
                            this.addCmIntoList(cm, cmList, cmcId);
                        }
                    }
                } else {// 多于一个状态的情况
                    if (RealtimeCmUtil.mapMirrorList(status).contains(RealtimeCmUtil.STATUS_ELSE)) {
                        if (RealtimeCmUtil.cmStatusTrans("JUMPSTATUS_ALLSHOW")
                                .contains(cm.getStatusValue().toString())) {
                            if (RealtimeCmUtil.mapMirrorList(status).contains(cm.getStatusValue().toString())) {
                                this.addCmIntoList(cm, cmList, cmcId);
                            }
                        } else {
                            this.addCmIntoList(cm, cmList, cmcId);
                        }
                    } else {
                        if (RealtimeCmUtil.mapMirrorList(status).contains(cm.getStatusValue().toString())) {
                            this.addCmIntoList(cm, cmList, cmcId);
                        }
                    }
                }
            }
        } else { // 如果没有cmIndexs和cmStatus传入，则为CM总数
            for (RealtimeCm cm : cmAttributeList) {
                this.addCmIntoList(cm, cmList, cmcId);
            }
        }
        return cmList;
    }

    private void addCmIntoList(RealtimeCm cm, List<RealtimeCm> cmList, Long cmcId) {
        Long cmcIndexFromCm = CmcIndexUtils.getCmcIndexFromCmIndex(cm.getStatusIndex());
        Long cmcIdFromCmcId = cmcService.getCmcIndexByCmcId(cmcId);
        if (cmcIndexFromCm.equals(cmcIdFromCmcId)) {
            cmList.add(cm);
        }
    }

    @Override
    public Map<String, List<String>> loadUpChannelSnr(Long cmcId, Long cmIndex) {
        Long entityId = cmcService.getEntityIdByCmcId(cmcId);
        SnmpParam snmpParam = getSnmpParamByEntityId(entityId);
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        List<DocsIf3CmtsCmUsStatus> list = getCmFacade(snmpParam.getIpAddress())
                .getDocsIf3CmtsCmUsStatusList(snmpParam);
        for (DocsIf3CmtsCmUsStatus d : list) {
            String index = String.valueOf(d.getCmRegStatusId());
            if (!map.containsKey(index)) {
                List<String> strs = new ArrayList<String>();
                map.put(index, strs);
            }
            map.get(index).add(d.getCmUsStatusSignalNoiseForUnit());
        }
        return map;
    }

    /**
     * 上行snr，全部取
     * 
     * @param
     * @return Map<String,List<String>>
     */
    public Map<String, List<String>> loadUpChannelSnr(Long cmcId, List<Long> cmIndexs) {
        Long entityId = cmcService.getEntityIdByCmcId(cmcId);
        SnmpParam snmpParam = getSnmpParamByEntityId(entityId);
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (Long cmIndex : cmIndexs) {
            DocsIf3CmtsCmUsStatus dis = new DocsIf3CmtsCmUsStatus();
            dis.setCmRegStatusId(cmIndex);
            List<DocsIf3CmtsCmUsStatus> list = getCmFacade(snmpParam.getIpAddress())
                    .getDocsIf3CmtsCmUsStatusListOfCC(snmpParam, dis);
            for (DocsIf3CmtsCmUsStatus d : list) {
                String index = String.valueOf(d.getCmRegStatusId());
                if (!map.containsKey(index)) {
                    List<String> strs = new ArrayList<String>();
                    map.put(index, strs);
                }
                map.get(index).add(d.getCmUsStatusSignalNoiseForUnit());
            }
        }
        return map;
    }

    /**
     * 上行snr和信道，取单个cc
     * 
     * @param
     * @return List<RealtimeCmUpSnr>
     */
    public List<RealtimeCmUpSnr> getUpChannelSnrChIdOfCm(Long cmcId, List<Long> cmIndexs) {
        List<RealtimeCmUpSnr> realtimecus = new ArrayList<RealtimeCmUpSnr>();
        Long entityId = cmcService.getEntityIdByCmcId(cmcId);
        SnmpParam snmpParam = getSnmpParamByEntityId(entityId);
        for (Long cmIndex : cmIndexs) {
            RealtimeCmUpSnr rcur = new RealtimeCmUpSnr();
            DocsIf3CmtsCmUsStatus dis = new DocsIf3CmtsCmUsStatus();
            dis.setCmRegStatusId(cmIndex);
            List<DocsIf3CmtsCmUsStatus> list = getCmFacade(snmpParam.getIpAddress())
                    .getDocsIf3CmtsCmUsStatusListOfCC(snmpParam, dis);
            for (DocsIf3CmtsCmUsStatus d : list) {
                rcur.setCmIndex(cmIndex);
                if (d.getCmRegStatusId() != null && d.getCmRegStatusId().equals(cmIndex)) {
                    rcur.getUpChannelId().add(d.getUpChannelId());
                    rcur.getUpChannelSnr().add(d.getCmUsStatusSignalNoiseForUnit());
                }
            }
            realtimecus.add(rcur);
        }
        return realtimecus;
    }

    @Override
    public Map<String, List<String>> loadCmSignal(Integer cmType, Long cmIndex, Long cmcId) {
        // 如果是3.0的CM
        if (cmType == 3) {
            return this.loadCm30Signal(cmIndex, cmcId);
        }
        return null;
    }

    public Map<String, List<String>> loadCm30Signal(Long cmIndex, Long cmcId) {
        Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
        returnMap.put("downChannelTx", new ArrayList<String>());// 下行电平
        returnMap.put("downChannelSnr", new ArrayList<String>());// 下行SNR
        returnMap.put("upChannelTx", new ArrayList<String>());// 上行电平
        Cm3DsRemoteQuery cm3DsRemoteQuery = new Cm3DsRemoteQuery();
        cm3DsRemoteQuery.setCmIndex(cmIndex);
        String cmcIp = this.getCmcIpById(cmcId);
        snmpParam = this.getRemoteQuerySnmp(cmcIp, cmcId);
        List<Cm3DsRemoteQuery> downChannels = getCmRemoteQueryFacade(cmcIp).getCm3DsSignal(snmpParam, cm3DsRemoteQuery);
        Collections.sort(downChannels);
        for (Cm3DsRemoteQuery c : downChannels) {
            returnMap.get("downChannelTx").add(c.getCmDsRxPowerString());
            returnMap.get("downChannelSnr").add(c.getCmDsSignalNoiseString());
            logger.debug("RealtimeCmList loadCm30Signal cmIndex = " + cmIndex + ",downChannelTx = "
                    + c.getCmDsRxPowerString());
            logger.debug("RealtimeCmList loadCm30Signal cmIndex = " + cmIndex + ",downChannelSnr = "
                    + c.getCmDsSignalNoiseString());
        }
        Cm3UsRemoteQuery cm3UsRemoteQuery = new Cm3UsRemoteQuery();
        cm3UsRemoteQuery.setCmIndex(cmIndex);
        List<Cm3UsRemoteQuery> upChannels = getCmRemoteQueryFacade(cmcIp).getCm3UsSignal(snmpParam, cm3UsRemoteQuery);
        Collections.sort(upChannels);
        for (Cm3UsRemoteQuery c : upChannels) {
            returnMap.get("upChannelTx").add(c.getCmUsTxPowerString());
            logger.debug("RealtimeCmList loadCm30Signal cmIndex = " + cmIndex + ",upChannelTx = "
                    + c.getCmUsTxPowerString());
        }
        return returnMap;
    }

    /**
     * 下行snr、电平和上行电平，取单个cc
     * 
     * @param
     * @return List<RealtimeCmSignal>
     */
    public List<RealtimeCmSignal> loadCm30SignalDownAndUpByCm(Long cmcId, List<Long> cmIndexs) {
        List<RealtimeCmSignal> rcsList = new ArrayList<RealtimeCmSignal>();
        String cmcIp = this.getCmcIpById(cmcId);
        snmpParam = this.getRemoteQuerySnmp(cmcIp, cmcId);
        for (Long cmIndex : cmIndexs) {
            Cm3DsRemoteQuery cdr = new Cm3DsRemoteQuery();
            Cm3UsRemoteQuery cur = new Cm3UsRemoteQuery();
            cdr.setCmIndex(cmIndex);
            cur.setCmIndex(cmIndex);
            List<Cm3DsRemoteQuery> downChannels = getCmFacade(cmcIp).getCm3DsSignalOfCm(snmpParam, cdr);
            Collections.sort(downChannels);
            List<Cm3UsRemoteQuery> upChannels = getCmFacade(cmcIp).getCm3UsSignalOfCm(snmpParam, cur);
            Collections.sort(upChannels);
            RealtimeCmSignal rcs = new RealtimeCmSignal();
            rcs.setCmIndex(cmIndex);
            for (Cm3DsRemoteQuery downChannel : downChannels) {
                if (downChannel.getCmIndex().equals(cmIndex)) {
                    rcs.getDownChannelSnr().add(downChannel.getCmDsSignalNoiseString());// 下行信道Snr
                    rcs.getDownChannelRx().add(downChannel.getCmDsRxPowerString());// 下行信道电平
                    rcs.getCmDownChId().add(downChannel.getCmDsChanId());// 下行信道
                }
            }
            for (Cm3UsRemoteQuery upChannel : upChannels) {
                if (upChannel.getCmIndex().equals(cmIndex)) {
                    rcs.getUpChannelTx().add(upChannel.getCmUsTxPowerString());// 上行信道电平
                    rcs.getCmUpChId().add(upChannel.getCmUsChanId());// 上行信道
                }
            }
            rcsList.add(rcs);
        }
        return rcsList;
    }

    /**
     * 下行snr、电平和上行电平，取全部
     * 
     * @param
     * @return List<RealtimeCmSignal>
     */
    public List<RealtimeCmSignal> loadCm30SignalDownAndUp(Long cmcId, List<Long> cmIndexs) {// 3.0上下行信道
        List<RealtimeCmSignal> rcsList = new ArrayList<RealtimeCmSignal>();
        String cmcIp = this.getCmcIpById(cmcId);
        snmpParam = this.getRemoteQuerySnmp(cmcIp, cmcId);
        List<Cm3DsRemoteQuery> downChannels = getCmRemoteQueryFacade(cmcIp).getCm3DsSignalAll(snmpParam);
        Collections.sort(downChannels);
        List<Cm3UsRemoteQuery> upChannels = getCmRemoteQueryFacade(cmcIp).getCm3UsSignalAll(snmpParam);
        Collections.sort(upChannels);
        for (Long tempIndex : cmIndexs) {
            RealtimeCmSignal rcs = new RealtimeCmSignal();
            for (Cm3DsRemoteQuery downChannel : downChannels) {
                if (downChannel.getCmIndex().equals(tempIndex)) {
                    rcs.setCmIndex(tempIndex);
                    rcs.getDownChannelSnr().add(downChannel.getCmDsSignalNoiseString());// 下行信道Snr
                    rcs.getDownChannelRx().add(downChannel.getCmDsRxPowerString());// 下行信道电平
                    rcs.getCmDownChId().add(downChannel.getCmDsChanId());// 下行信道
                }
            }
            for (Cm3UsRemoteQuery upChannel : upChannels) {
                if (upChannel.getCmIndex().equals(tempIndex)) {
                    rcs.getUpChannelTx().add(upChannel.getCmUsTxPowerString());// 上行信道电平
                    rcs.getCmUpChId().add(upChannel.getCmUsChanId());// 上行信道
                }
            }
            rcsList.add(rcs);
        }
        return rcsList;
    }

    /**
     * 组装cm实时信号信息
     * 
     * @param cmcId,cmIndexs,cmStatus(状态参数)
     * @return List<RealtimeCm>
     */
    public List<RealtimeCm> loadCmRealtimeInfo(Long cmcId, List<Long> cmIndexs, String[] cmStatus) {
        List<RealtimeCm> cmList = new ArrayList<RealtimeCm>();
        List<Long> cmStatusIndexList = new ArrayList<Long>();
        List<RealtimeCmUpSnr> cmUpSnr = new ArrayList<RealtimeCmUpSnr>();
        List<RealtimeCmSignal> cmSignalInfo = new ArrayList<RealtimeCmSignal>();
        try {
            cmList = this.getRealtimeCmAttributeByCmcId(cmcId, cmIndexs, cmStatus);// 基本信息
        } catch (Exception e) {
            logger.debug("Load Realtime CM Basic Information failed: ", e);
        }
        for (RealtimeCm rc : cmList) {
            cmStatusIndexList.add(rc.getStatusIndex());
        }
        try {
            cmUpSnr = this.getUpChannelSnrChIdOfCm(cmcId, cmStatusIndexList);// 上行snr
        } catch (Exception e) {
            logger.debug("Load Realtime CM UpChannel SNR failed: ", e);
        }
        try {
            cmSignalInfo = this.loadCm30SignalDownAndUp(cmcId, cmStatusIndexList);// 下行snr、上下行电平
        } catch (Exception e) {
            logger.debug("Load Realtime CM Signal failed: ", e);
        }
        if (cmList != null) {
            for (RealtimeCm rtc : cmList) {
                if (cmSignalInfo != null) {
                    for (RealtimeCmSignal rcs : cmSignalInfo) {
                        if (rcs.getCmIndex() != null && rtc.getStatusIndex() != null) {
                            try {
                                if (rcs.getCmIndex().equals(rtc.getStatusIndex())) {
                                    rtc.setDownChannelSnr(rcs.getDownChannelSnr());
                                    rtc.setDownChannelRx(rcs.getDownChannelRx());
                                    rtc.setCmDownChId(rcs.getCmDownChId());
                                    rtc.setCmUpChId(rcs.getCmUpChId());
                                    rtc.setUpChannelTx(rcs.getUpChannelTx());
                                }
                            } catch (Exception e) {
                                logger.debug("CM Signal Information Get failed:", e);
                            }
                        }
                    }
                }
                if (cmUpSnr != null) {
                    for (RealtimeCmUpSnr rcur : cmUpSnr) {
                        if (rcur.getCmIndex() != null && rtc.getStatusIndex() != null) {
                            try {
                                if (rcur.getCmIndex().equals(rtc.getStatusIndex())) {
                                    rtc.setUpChannelSnr(rcur.getUpChannelSnr());
                                    rtc.setCmUpSnrChId(rcur.getUpChannelId());
                                }
                            } catch (Exception e) {
                                logger.debug("Get Realtime CM UpChannel SNR failed: ", e);
                            }
                        }
                    }
                }
            }
        }
        return cmList;
    }

    private String getCmcIpById(Long cmcId) {
        Entity cmc = perfThresholdDao.getEntityById(cmcId);
        long cmcTypeId = cmc.getTypeId();
        String cmcIp = "";
        if (entityTypeService.isCcmtsWithoutAgent(cmcTypeId)) {
            cmcIp = cmRemoteQueryDao.getOltIpByOnuId(cmcId);
        } else if (entityTypeService.isCcmtsWithAgent(cmcTypeId)) {
            cmcIp = cmc.getIp();
        }
        return cmcIp;
    }

    /**
     * 通过CmcIp获得RemoteQuery的SNMP参数
     * 
     * @param cmcIp
     * @return SnmpParam
     */

    private SnmpParam getRemoteQuerySnmp(String cmcIp, Long cmcId) {
        Long entityId = cmcService.getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        snmpParam.setMibs("TOPVISION-CCMTS-MIB");
        /*
         * SnmpParam snmpParam = new SnmpParam(); snmpParam.setIpAddress(cmcIp);
         * snmpParam.setRetry((byte) 3); snmpParam.setTimeout(5000);
         * snmpParam.setMibs("TOPVISION-CCMTS-MIB");
         */
        return snmpParam;
    }

    @Override
    public List<CmCpe> getRealtimeCpeByCmcId(Long cmcId, Integer cpeStatus, Integer cpeType) {
        Long entityId = cmcService.getEntityIdByCmcId(cmcId);
        SnmpParam snmpParam = getSnmpParamByEntityId(entityId);
        List<CmCpe> cpeList = getCmFacade(snmpParam.getIpAddress()).getCmCpe(snmpParam);
        List<CmCpe> listRerturn = new ArrayList<CmCpe>();
        if (cpeStatus != null) {
            for (CmCpe cpe : cpeList) {
                if (cpeStatus == 1) {
                    if (cpe.getTopCmCpeStatus().equals(1)) {
                        listRerturn.add(cpe);
                    }
                } else {
                    if (!cpe.getTopCmCpeStatus().equals(1)) {
                        listRerturn.add(cpe);
                    }
                }
            }
        } else if (cpeType != null) {
            for (CmCpe cpe : cpeList) {
                if (cpeType == 0) {
                    listRerturn.add(cpe);
                } else if (cpeType == cpe.getTopCmCpeType()) {
                    listRerturn.add(cpe);
                }
            }
        } else {
            listRerturn = cpeList;
        }
        return listRerturn;
    }
}
