/***********************************************************************
 * $Id: CmSignalServiceImpl.java,v1.0 2016年6月28日 上午11:44:13 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.dao.CmDao;
import com.topvision.ems.cmc.cm.domain.Cm3Signal;
import com.topvision.ems.cmc.cm.domain.CmSignal;
import com.topvision.ems.cmc.cm.exception.CmRefreshSignalException;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.cm.service.CmSignalService;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmStatusForContactCmc;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.facade.domain.DocsIfDownstreamChannelForContactCmc;
import com.topvision.ems.cmc.facade.domain.DocsIfSignalQualityForContactCmc;
import com.topvision.ems.cmc.facade.domain.DocsIfUpstreamChannelForContactCmc;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3DsRemoteQuery;
import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3UsRemoteQuery;
import com.topvision.ems.cmc.remotequerycm.service.CmRemoteQueryService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.ping.CmdPing;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.SystemPreferencesService;

import net.sf.json.JSONObject;

/**
 * @author YangYi
 * @created @2016年6月28日-上午11:44:13
 *
 */
@Service("cmSignalService")
public class CmSignalServiceImpl extends CmcBaseCommonService implements CmSignalService {
    @Resource(name = "systemPreferencesService")
    private SystemPreferencesService systemPreferencesService;
    @Resource(name = "cmDao")
    private CmDao cmDao;
    @Resource(name = "cmService")
    private CmService cmService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "cmRemoteQueryService")
    private CmRemoteQueryService cmRemoteQueryService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    @Resource(name = "messagePusher")
    private MessagePusher messagePusher;// YangYi添加@20130824,消息推送

    @Override
    public void getSignalAndPush(String cmIds, final String jConnectedId, final String operationId,
            final UserContext uc) {
        final String seesionId = ServletActionContext.getRequest().getSession().getId();
        ExecutorService threadPool = Executors.newCachedThreadPool();
        String[] cmIdArray = cmIds.split(",");
        for (int i = 0; i < cmIdArray.length; i++) {
            final Long cmId = Long.parseLong(cmIdArray[i]);
            final Long sleepTime = 200L * i;
            threadPool.execute(new Runnable() {
                public void run() {
                    Boolean result = refreshSignalWithSave(cmId);
                    CmAttribute cmAttribute = getCmSignal(cmId);
                    // 格式化MAC地址，再推送到前台
                    String macRule = uc.getMacDisplayStyle();
                    String formatedMac = MacUtils.convertMacToDisplayFormat(cmAttribute.getStatusMacAddress(), macRule);
                    cmAttribute.setStatusMacAddress(formatedMac);
                    logger.debug("Push before print + " + cmAttribute.getCmId());
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pushCmAttributeMessage(result, cmAttribute, jConnectedId, seesionId, operationId);
                }
            });
        }
        threadPool.shutdown();// 任务执行完毕，关闭线程池
    }

    @Override
    public CmAttribute getCmSignal(Long cmId) {
        CmAttribute cmAttribute = cmDao.selectCmAttributeByCmId(cmId);
        CmSignal cmSignal = cmDao.selectCmSignal(cmId);
        List<Cm3Signal> upChannelCm3Signal = cmDao.selectUpChannelCm3Signal(cmId, Cm3Signal.CHANNEL_TYPE_UP);
        List<Cm3Signal> downChannelCm3Signal = cmDao.selectUpChannelCm3Signal(cmId, Cm3Signal.CHANNEL_TYPE_DOWN);
        cmAttribute.setCmSignal(cmSignal);
        if (cmSignal != null) {
            cmAttribute.setCollectTime(cmSignal.getCollectTime());
        }
        cmAttribute.setUpChannelCm3Signal(upChannelCm3Signal);
        cmAttribute.setDownChannelCm3Signal(downChannelCm3Signal);
        return cmAttribute;
    }

    @Override
    public boolean refreshSignalWithSave(Long cmId) {
        boolean result = false;
        CmAttribute cmAttribute = cmDao.selectCmAttributeByCmId(cmId);
        if (cmAttribute == null) {
            logger.error("refreshSignal error,cann't find CM cmId = " + cmId);
        }
        logger.info("refreshSignal,cmId = " + cmId);
        if (this.needRefreshCmSignal(cmAttribute)) {
            try {
                cmDao.deleteCmSignal(cmAttribute.getCmId());// 先将原有的记录删除
                cmDao.deleteCm3Signal(cmAttribute.getCmId());// 先将原有的记录删除
                this.refreshSignal(cmAttribute);
                result = true;
            } catch (Exception e) {
                logger.error("refreshSignal fail cmId = " + cmId, e);
            }
        }
        return result;
    }

    /**
     * 是否需要刷新，离线和IP为0.0.0.0的不刷新
     * 
     * @param cmAttribute
     * @return
     */
    private boolean needRefreshCmSignal(CmAttribute cmAttribute) {
        if (cmAttribute == null) {
            return false;
        }
        String cmIp = cmAttribute.getDisplayIp();
        if ("0.0.0.0".equals(cmIp) || !cmAttribute.isCmOnline()) {
            logger.info("refreshSignal,CM offline,ignore CM,CM = " + cmAttribute.toString());
            return false;
        }
        return true;
    }

    /**
     * 查询Cm实时信号,根据用户设置的,直接从CM获取或者使用RemoteQuery方式获取
     * 
     * @param cmId
     * @return
     */
    private void refreshSignal(CmAttribute cmAttribute) {
        if (cmService.getCmQueryMode().equals(CmcConstants.CM_REMOTE_QUERY)) {
            this.remoteQueryCmSignal(cmAttribute);// REMOTEQUERY
        } else {
            this.getSignalFromCm(cmAttribute);// 直接从CM获取
        }
    }

    /**
     * 从CM获取信号质量
     * 
     * @param cmAttribute
     */
    private void getSignalFromCm(CmAttribute cmAttribute) {
        logger.debug("getSignalFromCm start  cm = " + cmAttribute);
        List<Cm3Signal> cm3SignalList = new ArrayList<Cm3Signal>();
        CmStatusForContactCmc cmStatus = getCmStatusForContactCmc(cmAttribute.getDisplayIp(), cmAttribute.getCmId());

        // 获取3.0 CM信息
        for (DocsIfUpstreamChannelForContactCmc upChannelTx : cmStatus.getDocsIfUpstreamChannelList()) {
            Cm3Signal cm3Signal = this.buildCm3Signal(cm3SignalList, cmAttribute.getCmId(),
                    Long.valueOf(upChannelTx.getDocsIfUpChannelId()), Cm3Signal.CHANNEL_TYPE_UP);
            if (upChannelTx.getDocsIf3CmStatusUsTxPowerForUnit() != null) {
                cm3Signal.setUpChannelTx(upChannelTx.getDocsIf3CmStatusUsTxPowerForUnit());// 上行发送电平
            } else {
                cm3Signal.setUpChannelTx(upChannelTx.getDocsIfCmStatusTxPowerForUnit());// 如果拿不到CM3.0节点，则使用2.0节点填入上行发送电平
            }
            if (upChannelTx.getDocsIfUpChannelFrequencyForUnit() != null) {
                cm3Signal.setUpChannelFrequency(upChannelTx.getDocsIfUpChannelFrequencyForUnit());// 上行频率
            }
        }
        for (DocsIfDownstreamChannelForContactCmc downChannelTx : cmStatus.getDocsIfDownstreamChannelList()) {
            Cm3Signal cm3Signal = this.buildCm3Signal(cm3SignalList, cmAttribute.getCmId(),
                    downChannelTx.getDocsIfDownChannelId(), Cm3Signal.CHANNEL_TYPE_DOWN);
            cm3Signal.setDownChannelTx(downChannelTx.getDocsIfDownChannelPowerForUnit());// 下行接收电平
            if (downChannelTx.getDocsIfDownChannelFrequencyForUnit() != null) {
                cm3Signal.setDownChannelFrequency(downChannelTx.getDocsIfDownChannelFrequencyForUnit());// 下行频率
            }
        }
        for (DocsIfSignalQualityForContactCmc downChannelSnr : cmStatus.getDocsIfSignalQualityList()) {
            Cm3Signal cm3Signal = this.buildCm3Signal(cm3SignalList, cmAttribute.getCmId(),
                    downChannelSnr.getDownChanelId(), Cm3Signal.CHANNEL_TYPE_DOWN);
            cm3Signal.setDownChannelSnr(downChannelSnr.getDocsIfSigQSignalNoiseForUnit());// 下行信道SNR
        }
        List<DocsIf3CmtsCmUsStatus> cm3UpChannelList = getUpChannelSnr3(cmAttribute);
        for (DocsIf3CmtsCmUsStatus cm3UpChannel : cm3UpChannelList) {
            Cm3Signal cm3Signal = this.getCm3Signal(cm3SignalList, cmAttribute.getCmId(),
                    CmcIndexUtils.getChannelId(cm3UpChannel.getCmUsStatusChIfIndex()), Cm3Signal.CHANNEL_TYPE_UP);
            if (cm3Signal != null) {
                cm3Signal.setUpChannelSnr(cm3UpChannel.getCmUsStatusSignalNoiseForUnit());// 上行信道SNR
            }
        }
        for (Cm3Signal cm3Signal : cm3SignalList) {
            cmDao.insertOrUpdateCm3Signal(cm3Signal);
        }

        // 获取2.0 CM信息
        // modify by fanzidong,应该是使用3.0CM信息的第一条作为2.0CM的数据
        CmSignal cmSingal = new CmSignal();
        cmSingal.setCmId(cmAttribute.getCmId());
        if (cmStatus.getDocsIfUpstreamChannelList() != null && cmStatus.getDocsIfUpstreamChannelList().size() > 0) {
            cmSingal.setUpChannelTx(cmStatus.getDocsIfUpstreamChannelList().get(0).getDocsIfCmStatusTxPowerForUnit());// 上行发送电平
            cmSingal.setUpChannelFrequency(
                    cmStatus.getDocsIfUpstreamChannelList().get(0).getDocsIfUpChannelFrequencyForUnit());// 上行频率
        }
        if (cmStatus.getDocsIfDownstreamChannelList() != null && cmStatus.getDocsIfDownstreamChannelList().size() > 0) {
            cmSingal.setDownChannelTx(
                    cmStatus.getDocsIfDownstreamChannelList().get(0).getDocsIfDownChannelPowerForUnit());// 下行接收电平
            cmSingal.setDownChannelFrequency(
                    cmStatus.getDocsIfDownstreamChannelList().get(0).getDocsIfDownChannelFrequencyForUnit());// 下行频率
        }

        cmSingal.setDownChannelSnr(cmStatus.getDocsIfSignalQualityList().get(0).getDocsIfSigQSignalNoiseForUnit());// 下行信道SNR

        // modify by fanzidong,直接使用3.0CM的上行SNR值
        // cmSingal.setUpChannelSnr(getUpChannelSnr2(cmAttribute));// 上行信道SNR
        if (cm3UpChannelList != null && cm3UpChannelList.size() > 0) {
            cmSingal.setUpChannelSnr(cm3UpChannelList.get(0).getCmUsStatusSignalNoiseForUnit());
        }
        cmDao.insertOrUpdateCmSignal(cmSingal);
    }

    /**
     * 使用Remote Query方式查询CM实时信号
     * 
     * @param cmId
     * @return
     */
    private void remoteQueryCmSignal(CmAttribute cmAttribute) {
        Long entityId = getEntityIdByCmcId(cmAttribute.getCmcId());
        SnmpParam entitySnmpParam = getSnmpParamByEntityId(entityId);
        entitySnmpParam.setMibs("TOPVISION-CCMTS-MIB");

        List<Cm3Signal> cm3SignalList = new ArrayList<Cm3Signal>();
        List<Cm3UsRemoteQuery> cm3UpList = cmRemoteQueryService.getCm3UsRemoteQuery(entitySnmpParam, cmAttribute);

        // add by fanzidong,排序
        try {
            Collections.sort(cm3UpList);
        } catch (Exception e) {
            logger.debug("docsIfUpstreamChannelList sort error" + e);
        }
        List<Cm3DsRemoteQuery> cm3DownList = cmRemoteQueryService.getCm3DsRemoteQuery(entitySnmpParam, cmAttribute);
        // add by fanzidong,排序
        try {
            Collections.sort(cm3DownList);
        } catch (Exception e) {
            logger.debug("docsIfUpstreamChannelList sort error" + e);
        }
        List<DocsIf3CmtsCmUsStatus> cm3UpChannelList = getUpChannelSnr3(cmAttribute);
        // add by fanzidong,排序
        try {
            Collections.sort(cm3UpChannelList);
        } catch (Exception e) {
            logger.debug("docsIfUpstreamChannelList sort error" + e);
        }
        for (Cm3UsRemoteQuery cm3UsRemoteQuery : cm3UpList) {
            Cm3Signal cm3Signal = this.buildCm3Signal(cm3SignalList, cmAttribute.getCmId(),
                    Long.valueOf(cm3UsRemoteQuery.getCmUsChanId()), Cm3Signal.CHANNEL_TYPE_UP);
            cm3Signal.setUpChannelTx(cm3UsRemoteQuery.getCmUsTxPowerStringFordBmV());// 上行发送电平
        }
        for (Cm3DsRemoteQuery cm3DsRemoteQuery : cm3DownList) {
            Cm3Signal cm3Signal = this.buildCm3Signal(cm3SignalList, cmAttribute.getCmId(),
                    cm3DsRemoteQuery.getCmDsChanId(), Cm3Signal.CHANNEL_TYPE_DOWN);
            cm3Signal.setDownChannelTx(cm3DsRemoteQuery.getCmDsRxPowerStringFordBmV());// 下行接收电平
            cm3Signal.setDownChannelSnr(cm3DsRemoteQuery.getCmDsSignalNoiseString());// 下行信道SNR
        }
        for (DocsIf3CmtsCmUsStatus cm3UpChannel : cm3UpChannelList) {
            Cm3Signal cm3Signal = this.getCm3Signal(cm3SignalList, cmAttribute.getCmId(),
                    CmcIndexUtils.getChannelId(cm3UpChannel.getCmUsStatusChIfIndex()), Cm3Signal.CHANNEL_TYPE_UP);
            if (cm3Signal != null) {
                cm3Signal.setUpChannelSnr(cm3UpChannel.getCmUsStatusSignalNoiseForUnit());// 上行信道SNR
            }
        }
        for (Cm3Signal cm3Signal : cm3SignalList) {
            cmDao.insertOrUpdateCm3Signal(cm3Signal);
        }

        CmSignal cmSignal = new CmSignal();
        cmSignal.setCmId(cmAttribute.getCmId());
        if (cm3DownList != null && cm3DownList.size() > 0) {
            cmSignal.setDownChannelTx(cm3DownList.get(0).getCmDsRxPowerStringFordBmV());// 下行接收电平
            cmSignal.setDownChannelSnr(cm3DownList.get(0).getCmDsSignalNoiseString());// 下行信道SNR
        }
        if (cm3UpChannelList != null && cm3UpChannelList.size() > 0) {
            cmSignal.setUpChannelSnr(cm3UpChannelList.get(0).getCmUsStatusSignalNoiseForUnit());// 上行信道SNR
        }
        if (cm3UpList != null && cm3UpList.size() > 0) {
            cmSignal.setUpChannelTx(cm3UpList.get(0).getCmUsTxPowerStringFordBmV()); // 上行发送电平
        }
        cmDao.insertOrUpdateCmSignal(cmSignal);
    }

    private Cm3Signal buildCm3Signal(List<Cm3Signal> cm3List, Long cmId, Long cmChannelId, Long cmChannelType) {
        Cm3Signal cm3Signal = new Cm3Signal(cmId, cmChannelId, cmChannelType);
        int index = cm3List.indexOf(cm3Signal);
        if (index >= 0) {
            cm3Signal = cm3List.get(index);
        } else {
            cm3List.add(cm3Signal);
        }
        return cm3Signal;
    }

    private Cm3Signal getCm3Signal(List<Cm3Signal> cm3List, Long cmId, Long cmChannelId, Long cmChannelType) {
        Cm3Signal cm3Signal = new Cm3Signal(cmId, cmChannelId, cmChannelType);
        int index = cm3List.indexOf(cm3Signal);
        if (index >= 0) {
            return cm3List.get(index);
        } else {
            return null;
        }
    }

    /**
     * 获取CM2.0 上行信道SNR
     * 
     * @param cmAttribute
     * @return
     */
    private String getUpChannelSnr2(CmAttribute cmAttribute) {
        Long entityId = getEntityIdByCmcId(cmAttribute.getCmcId());
        SnmpParam snmpParam = getSnmpParamByEntityId(entityId);
        Long upChannelSnr = getCmcFacade(snmpParam.getIpAddress()).getUpChannelSnr(snmpParam,
                cmAttribute.getStatusIndex());
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(upChannelSnr / 10);
    }

    /**
     * 获取CM3.0 上行信道SNR
     * 
     * @param cmAttribute
     * @return
     */
    private List<DocsIf3CmtsCmUsStatus> getUpChannelSnr3(CmAttribute cmAttribute) {
        Long entityId = getEntityIdByCmcId(cmAttribute.getCmcId());
        SnmpParam snmpParam = getSnmpParamByEntityId(entityId);
        // Modify by Victor@20170104增加版本控制，对于不支持的版本采用老的全表获取方式
        // 版本控制的方式只对E/CE/DE独立型在V2.2.9.0之前采用全表采集
        List<DocsIf3CmtsCmUsStatus> list = null;
        if (deviceVersionService.isFunctionSupported(cmAttribute.getCmcId(), "getDocsIf3CmtsCmUsStatus")) {
            list = getCmcFacade(snmpParam.getIpAddress()).getDocsIf3CmtsCmUsStatus(snmpParam);
        } else {
            list = getCmcFacade(snmpParam.getIpAddress()).getDocsIf3CmtsCmUsStatusByCmIndex(snmpParam,
                    cmAttribute.getStatusIndex());
        }
        return list;
    }

    /**
     * 从CM获取CM 发射接收电平 下行SNR
     * 
     * @param cmIp
     * @param cmId
     * @return
     */
    private CmStatusForContactCmc getCmStatusForContactCmc(String cmIp, Long cmId) {
        if (!checkCmReachableTimeOut(cmIp)) {
            logger.debug("getCmStatusForContactCmc ping timeout cmIp:" + cmIp);
            throw new CmRefreshSignalException("getCmStatusForContactCmc ping timeout cmIp:" + cmIp);
        }
        SnmpParam snmpParam = cmService.getCmSnmpParam();
        snmpParam.setIpAddress(cmIp);
        String cmcmib = "RFC1213-MIB,DOCS-IF-MIB";
        snmpParam.setMibs(cmcmib);
        // cm基本属性信息
        CmStatusForContactCmc cmStatus = new CmStatusForContactCmc();
        // cm上行射频信息
        List<DocsIfUpstreamChannelForContactCmc> docsIfUpstreamChannelList = getCcmtsCmListFacade(cmIp)
                .getDocsIfUpstreamForContactCmc(snmpParam, cmIp);
        // modify by fanzidong,信道排序
        try {
            Collections.sort(docsIfUpstreamChannelList);
        } catch (Exception e) {
            logger.debug("docsIfUpstreamChannelList sort error" + e);
        }
        cmStatus.setDocsIfUpstreamChannelList(docsIfUpstreamChannelList);
        // cm下行射频信息
        List<DocsIfDownstreamChannelForContactCmc> docsIfDownstreamChannelList = getCcmtsCmListFacade(cmIp)
                .getDocsIfDownstreamForContactCmc(snmpParam, cmIp);
        // modify by fanzidong,信道排序
        try {
            Collections.sort(docsIfDownstreamChannelList);
        } catch (Exception e) {
            logger.debug("docsIfDownstreamChannelList sort error" + e);
        }
        cmStatus.setDocsIfDownstreamChannelList(docsIfDownstreamChannelList);
        // cm下行信号质量信息
        List<DocsIfSignalQualityForContactCmc> docsIfSignalQualityList = getCcmtsCmListFacade(cmIp)
                .getDocsIfSignalQualityForContactCmc(snmpParam, cmIp);
        if (docsIfSignalQualityList != null) {
            // 映射信道Id
            for (int i = 0; i < docsIfSignalQualityList.size(); i++) {
                for (int j = 0; j < docsIfDownstreamChannelList.size(); j++) {
                    if (docsIfSignalQualityList.get(i).getIfIndex()
                            .equals(docsIfDownstreamChannelList.get(j).getIfIndex())) {
                        docsIfSignalQualityList.get(i)
                                .setDownChanelId(docsIfDownstreamChannelList.get(j).getDocsIfDownChannelId());
                    }
                }
            }
            // modify by fanzidong,信道排序
            try {
                Collections.sort(docsIfSignalQualityList);
            } catch (Exception e) {
                logger.debug("docsIfSignalQualityList sort error" + e);
            }
            cmStatus.setDocsIfSignalQualityList(docsIfSignalQualityList);
        }
        return cmStatus;
    }

    /**
     * CM是否可达
     * 
     * @param ip
     * @return
     */
    private boolean checkCmReachableTimeOut(String ip) {
        CmdPing cmdPing = new CmdPing();
        return cmdPing.online(ip,
                Integer.parseInt(
                        systemPreferencesService.getModulePreferences("Ping").getProperty("Ping.timeout", "4000")),
                Integer.parseInt(systemPreferencesService.getModulePreferences("Ping").getProperty("Ping.count", "1")));
    }

    /**
     * 推送CM信号质量到前台
     * 
     * @param cmAttribute
     * @param jConnectedId
     * @param seesionId
     */
    private void pushCmAttributeMessage(boolean result, CmAttribute cmAttribute, String jConnectedId, String seesionId,
            String operationId) {
        if (jConnectedId != null) {
            Message message = new Message(operationId);
            message.addSessionId(seesionId);
            message.setJconnectID(jConnectedId);
            logger.debug("at pushCmAttributeMessage print:" + cmAttribute.getCmId() + " " + cmAttribute.getDisplayIp());
            JSONObject json = new JSONObject();
            json.put("result", result);
            json.put("cmAttribute", cmAttribute);
            message.setData(json);
            messagePusher.sendMessage(message);
        }
    }

    /**
     * 获取是CM2.0 还是3.0
     * 
     * @param cmIp
     * @return 返回结果3是2.0 4是3.0
     */
    private Integer getCmDocsisCapability(String cmIp) {
        SnmpParam snmpParam = cmService.getCmSnmpParam();
        snmpParam.setIpAddress(cmIp);
        String cmcmib = "RFC1213-MIB,DOCS-IF-MIB";
        snmpParam.setMibs(cmcmib);
        // cm基本属性信息
        try {
            Integer docsis = getCcmtsCmListFacade(cmIp).showCmStatusForContactCmc(snmpParam, cmIp)
                    .getDocsIfDocsisBaseCapability();
            if (docsis == 4) {
                return 4;
            } else {
                return 3;
            }
        } catch (Exception e) {
            return 3;
        }
    }
}
