/***********************************************************************
 * $Id: OpticalReceiverRefreshServiceImpl.java,v1.0 2016年9月18日 上午9:20:35 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.opticalreceiver.dao.OpticalReceiverRefreshDao;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorABSwitch;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorChannelNum;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorDCPower;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorDevParams;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdEq;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorLinePower;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRFPort;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRRXOptPow;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRevAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsOpRxInput;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsOpRxOutput;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsSysDorType;
import com.topvision.ems.cmc.opticalreceiver.facade.OpticalReceiverFacade;
import com.topvision.ems.cmc.opticalreceiver.service.OpticalReceiverRefreshService;
import com.topvision.ems.cmc.opticalreceiver.util.OpticalReceiverUtil;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author haojie
 * @created @2016年9月18日-上午9:20:35
 *
 */
@Service("opticalReceiverRefreshService")
public class OpticalReceiverRefreshServiceImpl extends CmcBaseCommonService implements OpticalReceiverRefreshService,
        CmcSynchronizedListener {
    @Autowired
    private MessageService messageService;
    @Autowired
    private OpticalReceiverRefreshDao opticalReceiverRefreshDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private CmcDao cmcDao;
    private static final Integer DCPOWER_SIZE = 2;
    private static final Integer FWDATT_SIZE = 4;
    private static final Integer FWDEQ_SIZE = 4;
    private static final Integer REVATT_SIZE = 4;
    private static final Integer RFPORT_SIZE = 4;
    private static final Integer RRXOPTPOW_SIZE = 4;

    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(CmcSynchronizedListener.class, this);
    }

    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(CmcSynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        Long entityId = event.getEntityId();
        List<Long> cmcIndexList = event.getCmcIndexList();
        for (Long cmcIndex : cmcIndexList) {
            String sysDorType = null;
            try {
                // 刷新光机类型字符串
                sysDorType = refreshSysDorType(entityId, cmcIndex);
                logger.info("refreshSysDorType finish");
            } catch (Exception e) {
                logger.error("refreshSysDorType wrong", e);
            }

            // 当有光机的CC才进行其余业务数据的刷新
            if (sysDorType != null && !"".equals(sysDorType)) {
                
                try {
                    //光机基本参数
                    refreshDevParams(entityId, cmcIndex);
                    logger.info("refreshDevParams finish");
                } catch (Exception e) {
                    logger.info("refreshDevParams wrong", e);
                }
                
                try {
                    // 正向光收A路光功率
                    refreshOpRxInput(entityId, cmcIndex);
                    logger.info("refreshOpRxInput finish");
                } catch (Exception e) {
                    logger.info("refreshOpRxInput wrong", e);
                }

                try {
                    // 正向光收模块状态/正向光发射频衰减/AGC输入光功率范围
                    refreshOpRxOutput(entityId, cmcIndex);
                    logger.info("refreshOpRxOutput finish");
                } catch (Exception e) {
                    logger.info("refreshOpRxOutput wrong", e);
                }
                
                try {
                    // 直流24V输出电压（DC12V/DC24V）
                    refreshDCPower(entityId, cmcIndex);
                    logger.info("refreshDCPower finish");
                } catch (Exception e) {
                    logger.info("refreshDCPower wrong", e);
                }
                
                try {
                    // 同轴供电输入电压 AC 60V电压
                    refreshLinePower(entityId, cmcIndex);
                    logger.info("refreshLinePower finish");
                } catch (Exception e) {
                    logger.info("refreshLinePower wrong", e);
                }
                
                try {
                    // 正向频道数量
                    refreshChannelNum(entityId, cmcIndex);
                    logger.info("refreshChannelNum finish");
                } catch (Exception e) {
                    logger.error("refreshChannelNum wrong", e);
                }
                
                try {
                    // RF1-4端口输出电平
                    refreshRFPort(entityId, cmcIndex);
                    logger.info("refreshRFPort finish");
                } catch (Exception e) {
                    logger.info("refreshRFPort wrong", e);
                }

                try {
                    // 正向射频支路1-4衰减
                    refreshFwdAtt(entityId, cmcIndex);
                    logger.info("refreshFwdAtt finish");
                } catch (Exception e) {
                    logger.info("refreshFwdAtt wrong", e);
                }

                try {
                    // 正向射频支路1-4均衡
                    refreshFwdEq(entityId, cmcIndex);
                    logger.info("refreshfwdEq finish");
                } catch (Exception e) {
                    logger.info("refreshfwdEq wrong", e);
                }

                try {
                    // 反向射频支路1-4衰减表
                    refreshRevAtt(entityId, cmcIndex);
                    logger.info("refreshRevAtt finish");
                } catch (Exception e) {
                    logger.info("refreshRevAtt wrong", e);
                }

                try {
                    // 反向光收1-4输入光功率
                    refreshRRXOptPow(entityId, cmcIndex);
                    logger.info("refreshRRXOptPow finish");
                } catch (Exception e) {
                    logger.info("refreshRRXOptPow wrong", e);
                }

            }
        }
    }

    @Override
    public String refreshSysDorType(Long entityId, Long cmcIndex) {
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        TopCcmtsSysDorType topCcmtsSysDorType = new TopCcmtsSysDorType();
        topCcmtsSysDorType.setCmcIndex(cmcIndex);
        topCcmtsSysDorType = facade.getSysDorType(snmpParam, topCcmtsSysDorType);
        topCcmtsSysDorType.setCmcId(cmcDao.getCmcIdByEntityIdAndCmcIndex(entityId, cmcIndex));
        opticalReceiverRefreshDao.updateSysDorType(topCcmtsSysDorType);
        return topCcmtsSysDorType.getTopCcmtsSysDorType();
    }

    @Override
    public void refreshABSwitch(Long entityId, Long cmcIndex) {
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        TopCcmtsDorABSwitch topCcmtsDorABSwitch = new TopCcmtsDorABSwitch();
        Long index = OpticalReceiverUtil.generateNextIndex(cmcIndex);
        topCcmtsDorABSwitch.setAbSwitchIndex(index);
        topCcmtsDorABSwitch = facade.getABSwitch(snmpParam, topCcmtsDorABSwitch);
        topCcmtsDorABSwitch.setCmcId(cmcDao.getCmcIdByEntityIdAndCmcIndex(entityId, cmcIndex));
        opticalReceiverRefreshDao.insertOrUpdateABSwitch(topCcmtsDorABSwitch);
    }

    @Override
    public void refreshChannelNum(Long entityId, Long cmcIndex) {
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        TopCcmtsDorChannelNum topCcmtsDorChannelNum = new TopCcmtsDorChannelNum();
        Long index = OpticalReceiverUtil.generateNextIndex(cmcIndex);
        topCcmtsDorChannelNum.setChannelNumIndex(index);
        topCcmtsDorChannelNum = facade.getChannelNum(snmpParam, topCcmtsDorChannelNum);
        topCcmtsDorChannelNum.setCmcId(cmcDao.getCmcIdByEntityIdAndCmcIndex(entityId, cmcIndex));
        opticalReceiverRefreshDao.insertOrUpdateChannelNum(topCcmtsDorChannelNum);
    }

    @Override
    public void refreshDCPower(Long entityId, Long cmcIndex) {
        List<TopCcmtsDorDCPower> powerList = new ArrayList<TopCcmtsDorDCPower>();
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        Long index = OpticalReceiverUtil.generateNextIndex(cmcIndex);
        Long cmcId = cmcDao.getCmcIdByEntityIdAndCmcIndex(entityId, cmcIndex);
        for (int i = 0; i < DCPOWER_SIZE; i++) {
            TopCcmtsDorDCPower topCcmtsDorDCPower = new TopCcmtsDorDCPower();
            topCcmtsDorDCPower.setDcPowerIndex(index);
            // 此处抓异常是为了解决例如EF光机只有12V电压
            try {
                topCcmtsDorDCPower = facade.getDCPower(snmpParam, topCcmtsDorDCPower);
            } catch (Exception e) {
                logger.debug("getDCPower error", e);
            }
            topCcmtsDorDCPower.setCmcId(cmcId);
            powerList.add(topCcmtsDorDCPower);
            index = OpticalReceiverUtil.generateNextIndex(index);
        }
        opticalReceiverRefreshDao.batchInsertOrUpdateDCPower(powerList);
    }

    @Override
    public void refreshDevParams(Long entityId, Long cmcIndex) {
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        TopCcmtsDorDevParams topCcmtsDorDevParams = new TopCcmtsDorDevParams();
        Long index = OpticalReceiverUtil.generateNextIndex(cmcIndex);
        topCcmtsDorDevParams.setDevIndex(index);
        topCcmtsDorDevParams = facade.getDevParams(snmpParam, topCcmtsDorDevParams);
        topCcmtsDorDevParams.setCmcId(cmcDao.getCmcIdByEntityIdAndCmcIndex(entityId, cmcIndex));
        opticalReceiverRefreshDao.insertOrUpdateDevParams(topCcmtsDorDevParams);
    }

    @Override
    public void refreshFwdAtt(Long entityId, Long cmcIndex) {
        List<TopCcmtsDorFwdAtt> topCcmtsDorFwdAtts = new ArrayList<TopCcmtsDorFwdAtt>();
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        Long index = OpticalReceiverUtil.generateNextIndex(cmcIndex);
        Long cmcId = cmcDao.getCmcIdByEntityIdAndCmcIndex(entityId, cmcIndex);
        for (int i = 0; i < FWDATT_SIZE; i++) {
            TopCcmtsDorFwdAtt topCcmtsDorFwdAtt = new TopCcmtsDorFwdAtt();
            topCcmtsDorFwdAtt.setFwdAttIndex(index);
            // 此处抓异常是为了解决例如CFD光机只有射频支路1-2衰减，FF光机有射频支路1-4衰减
            try {
                topCcmtsDorFwdAtt = facade.getFwdAtt(snmpParam, topCcmtsDorFwdAtt);
            } catch (Exception e) {
                logger.debug("getFwdAtt error", e);
            }
            topCcmtsDorFwdAtt.setCmcId(cmcId);
            topCcmtsDorFwdAtts.add(topCcmtsDorFwdAtt);
            index = OpticalReceiverUtil.generateNextIndex(index);
        }
        opticalReceiverRefreshDao.batchInsertOrUpdateFwdAtts(topCcmtsDorFwdAtts);
    }

    @Override
    public void refreshFwdEq(Long entityId, Long cmcIndex) {
        List<TopCcmtsDorFwdEq> topCcmtsDorFwdEqs = new ArrayList<TopCcmtsDorFwdEq>();
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        Long index = OpticalReceiverUtil.generateNextIndex(cmcIndex);
        Long cmcId = cmcDao.getCmcIdByEntityIdAndCmcIndex(entityId, cmcIndex);
        for (int i = 0; i < FWDEQ_SIZE; i++) {
            TopCcmtsDorFwdEq topCcmtsDorFwdEq = new TopCcmtsDorFwdEq();
            topCcmtsDorFwdEq.setFwdEqIndex(index);
            // 此处抓异常是为了解决例如CFD光机只有射频支路1-2均衡，FF光机有射频支路1-4均衡
            try {
                topCcmtsDorFwdEq = facade.getFwdEq(snmpParam, topCcmtsDorFwdEq);
            } catch (Exception e) {
                logger.debug("getFwdEq error", e);
            }
            topCcmtsDorFwdEq.setCmcId(cmcId);
            topCcmtsDorFwdEqs.add(topCcmtsDorFwdEq);
            index = OpticalReceiverUtil.generateNextIndex(index);
        }
        opticalReceiverRefreshDao.batchInsertOrUpdateFwdEqs(topCcmtsDorFwdEqs);
    }

    @Override
    public void refreshLinePower(Long entityId, Long cmcIndex) {
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        TopCcmtsDorLinePower topCcmtsDorLinePower = new TopCcmtsDorLinePower();
        Long index = OpticalReceiverUtil.generateNextIndex(cmcIndex);
        topCcmtsDorLinePower.setLinePowerIndex(index);
        topCcmtsDorLinePower = facade.getLinePower(snmpParam, topCcmtsDorLinePower);
        topCcmtsDorLinePower.setCmcId(cmcDao.getCmcIdByEntityIdAndCmcIndex(entityId, cmcIndex));
        opticalReceiverRefreshDao.insertOrUpdateLinePower(topCcmtsDorLinePower);
    }

    @Override
    public void refreshRevAtt(Long entityId, Long cmcIndex) {
        List<TopCcmtsDorRevAtt> topCcmtsDorRevAtts = new ArrayList<TopCcmtsDorRevAtt>();
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        Long index = OpticalReceiverUtil.generateNextIndex(cmcIndex);
        Long cmcId = cmcDao.getCmcIdByEntityIdAndCmcIndex(entityId, cmcIndex);
        for (int i = 0; i < REVATT_SIZE; i++) {
            TopCcmtsDorRevAtt topCcmtsDorRevAtt = new TopCcmtsDorRevAtt();
            topCcmtsDorRevAtt.setRevAttIndex(index);
            topCcmtsDorRevAtt = facade.getRevAtt(snmpParam, topCcmtsDorRevAtt);
            topCcmtsDorRevAtt.setCmcId(cmcId);
            topCcmtsDorRevAtts.add(topCcmtsDorRevAtt);
            index = OpticalReceiverUtil.generateNextIndex(index);
        }
        opticalReceiverRefreshDao.batchInsertOrUpdateRevAtts(topCcmtsDorRevAtts);
    }

    @Override
    public void refreshRFPort(Long entityId, Long cmcIndex) {
        List<TopCcmtsDorRFPort> topCcmtsDorRFPorts = new ArrayList<TopCcmtsDorRFPort>();
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        Long index = OpticalReceiverUtil.generateNextIndex(cmcIndex);
        Long cmcId = cmcDao.getCmcIdByEntityIdAndCmcIndex(entityId, cmcIndex);
        for (int i = 0; i < RFPORT_SIZE; i++) {
            TopCcmtsDorRFPort topCcmtsDorRFPort = new TopCcmtsDorRFPort();
            topCcmtsDorRFPort.setRfPortIndex(index);
            topCcmtsDorRFPort = facade.getRFPort(snmpParam, topCcmtsDorRFPort);
            topCcmtsDorRFPort.setCmcId(cmcId);
            topCcmtsDorRFPorts.add(topCcmtsDorRFPort);
            index = OpticalReceiverUtil.generateNextIndex(index);
        }
        opticalReceiverRefreshDao.batchInsertOrUpdateRFPorts(topCcmtsDorRFPorts);
    }

    @Override
    public void refreshRRXOptPow(Long entityId, Long cmcIndex) {
        List<TopCcmtsDorRRXOptPow> topCcmtsDorRRXOptPows = new ArrayList<TopCcmtsDorRRXOptPow>();
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        Long index = OpticalReceiverUtil.generateNextIndex(cmcIndex);
        Long cmcId = cmcDao.getCmcIdByEntityIdAndCmcIndex(entityId, cmcIndex);
        for (int i = 0; i < RRXOPTPOW_SIZE; i++) {
            TopCcmtsDorRRXOptPow topCcmtsDorRRXOptPow = new TopCcmtsDorRRXOptPow();
            topCcmtsDorRRXOptPow.setRrxOptPowIndex(index);
            topCcmtsDorRRXOptPow = facade.getRRXOptPow(snmpParam, topCcmtsDorRRXOptPow);
            topCcmtsDorRRXOptPow.setCmcId(cmcId);
            topCcmtsDorRRXOptPows.add(topCcmtsDorRRXOptPow);
            index = OpticalReceiverUtil.generateNextIndex(index);
        }
        opticalReceiverRefreshDao.batchInsertOrUpdatesRRXOptPows(topCcmtsDorRRXOptPows);
    }

    @Override
    public void refreshOpRxInput(Long entityId, Long cmcIndex) {
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        TopCcmtsOpRxInput topCcmtsOpRxInput = new TopCcmtsOpRxInput();
        Long index = OpticalReceiverUtil.generateNextIndex(cmcIndex);
        topCcmtsOpRxInput.setInputIndex(index);
        topCcmtsOpRxInput = facade.getOpRxInput(snmpParam, topCcmtsOpRxInput);
        topCcmtsOpRxInput.setCmcId(cmcDao.getCmcIdByEntityIdAndCmcIndex(entityId, cmcIndex));
        opticalReceiverRefreshDao.insertOrUpdateOpRxInput(topCcmtsOpRxInput);
    }

    @Override
    public void refreshOpRxOutput(Long entityId, Long cmcIndex) {
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        TopCcmtsOpRxOutput topCcmtsOpRxOutput = new TopCcmtsOpRxOutput();
        Long index = OpticalReceiverUtil.generateNextIndex(cmcIndex);
        topCcmtsOpRxOutput.setOutputIndex(index);
        topCcmtsOpRxOutput = facade.getOpRxOutput(snmpParam, topCcmtsOpRxOutput);
        topCcmtsOpRxOutput.setCmcId(cmcDao.getCmcIdByEntityIdAndCmcIndex(entityId, cmcIndex));
        opticalReceiverRefreshDao.insertOrUpdateOpRxOutput(topCcmtsOpRxOutput);
    }

    /**
     * 获取光机OpticalReceiverFacade
     * 
     * @param snmpParam
     * @return
     */
    private OpticalReceiverFacade getOpticalReceiverFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), OpticalReceiverFacade.class);
    }
}
