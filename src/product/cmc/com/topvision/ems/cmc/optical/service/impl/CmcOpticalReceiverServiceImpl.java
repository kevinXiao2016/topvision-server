/***********************************************************************
 * $Id: CmcOpticalReceiverServiceImpl.java,v1.0 2013-12-2 下午4:55:04 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.optical.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.optical.dao.CmcOpticalReceiverDao;
import com.topvision.ems.cmc.optical.domain.CmcOpReceiverCfg;
import com.topvision.ems.cmc.optical.domain.CmcOpReceiverStatus;
import com.topvision.ems.cmc.optical.facade.CmcOpticalReceiverFacade;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverAcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverChannelNum;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverDcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverInputInfo;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfCfg;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfPort;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverSwitchCfg;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverType;
import com.topvision.ems.cmc.optical.service.CmcOpticalReceiverService;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.framework.exception.engine.SnmpNoSuchInstanceException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author dosion
 * @created @2013-12-2-下午4:55:04
 * 
 */
@Service("cmcOpticalReceiverService")
public class CmcOpticalReceiverServiceImpl extends BaseService implements CmcOpticalReceiverService {
    private static final Integer INFO_SIZE = 2;
    private static final Integer DCPOWER_SIZE = 3;
    private static final Integer RFPORT_SIZE = 4;
    @Resource(name = "cmcOpticalReceiverDao")
    private CmcOpticalReceiverDao opticalReceiverDao;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;
    @Resource(name = "cmcDao")
    protected CmcDao cmcDao;
    @Resource(name = "entityDao")
    protected EntityDao entityDao;

    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    @PostConstruct
    public void initialize() {
        super.initialize();
    }

    @Override
    public CmcOpReceiverStatus getOpticalReceiverStatus(Long cmcId) {
        CmcOpReceiverStatus status = new CmcOpReceiverStatus();
        // TODO 光机类型
        // modify by fanzidong, 光机类型改用cmcattribute的topCcmtsSysDorType
        CmcAttribute cmcAttribute = cmcDao.getCmcAttributeByCmcId(cmcId);
        status.setDorType(cmcAttribute.getTopCcmtsSysDorType());
        /*
         * CmcOpReceiverType opType = opticalReceiverDao.selectOpReceiverType(cmcId); if (opType !=
         * null) { status.setDorType(opType.getDorDevType()); }
         */
        // Input Info
        List<CmcOpReceiverInputInfo> inputInfo = opticalReceiverDao.selectOpReceiverInputInfoList(cmcId);
        status.setInputInfoList(inputInfo);
        // 下行输出电平，开光状态暂不支持
        // CmcOpReceiverRfCfg rfCfg = opticalReceiverDao.selectOpReceiverRfCfg(cmcId);
        // status.setOutputLevel(rfCfg.getOutputLevel());
        // CmcOpReceiverSwitchCfg switchCfg = opticalReceiverDao.selectOpReceiverSwitchCfg(cmcId);
        // status.setSwitchState(switchCfg.getSwitchState());
        // AC Power
        CmcOpReceiverAcPower acPower = opticalReceiverDao.selectOpReceiverAcPower(cmcId);
        if (acPower != null) {
            status.setAcPowerVoltage(acPower.getPowerVoltage1());
        }
        // DC Power
        List<CmcOpReceiverDcPower> dcPower = opticalReceiverDao.selectOpReceiverDcPowerList(cmcId);
        status.setDcPower(dcPower);
        // Channel Num
        CmcOpReceiverChannelNum channelNum = opticalReceiverDao.selectOpReceiverChannelNum(cmcId);
        if (channelNum != null) {
            status.setChannelNum(channelNum.getChannelNum());
        }
        // RF Port
        List<CmcOpReceiverRfPort> rfPortList = opticalReceiverDao.selectOpReceiverRfPortList(cmcId);
        status.setRfPort(rfPortList);
        // RF config
        CmcOpReceiverRfCfg rfCfg = opticalReceiverDao.selectOpReceiverRfCfg(cmcId);
        status.setRfCfg(rfCfg);
        // AB Switch
        CmcOpReceiverSwitchCfg switchCfg = opticalReceiverDao.selectOpReceiverSwitchCfg(cmcId);
        status.setSwitchCfg(switchCfg);
        return status;
    }

    @Override
    public CmcOpReceiverCfg getOpticalReceiverCfg(Long cmcId) {
        CmcOpReceiverCfg cfg = new CmcOpReceiverCfg();
        // 获取光机类型
        CmcOpReceiverType dorType = opticalReceiverDao.selectOpReceiverType(cmcId);
        if (dorType != null) {
            cfg.setDorType(dorType.getDorDevType());
        }
        CmcOpReceiverRfCfg rfCfg = opticalReceiverDao.selectOpReceiverRfCfg(cmcId);
        CmcOpReceiverSwitchCfg switchCfg = opticalReceiverDao.selectOpReceiverSwitchCfg(cmcId);
        cfg.setCmcId(cmcId);
        // 设置RF配置
        if (rfCfg != null) {
            cfg.setOutputAGCOrigin(rfCfg.getOutputAGCOrigin());
            cfg.setOutputControl(rfCfg.getOutputControl());
            cfg.setOutputGainType(rfCfg.getOutputGainType());
            cfg.setOutputIndex(rfCfg.getOutputIndex());
            cfg.setOutputLevel(rfCfg.getOutputLevel());
            cfg.setOutputRFlevelatt(rfCfg.getOutputRFlevelatt());
        }
        // 设置光机转换开关配置
        if (switchCfg != null) {
            cfg.setSwitchControl(switchCfg.getSwitchControl());
            cfg.setSwitchIndex(switchCfg.getSwitchIndex());
            cfg.setSwitchState(switchCfg.getSwitchState());
            cfg.setSwitchThres(switchCfg.getSwitchThres());
        }
        return cfg;
    }

    @Override
    public void modifyOpReceiverCfg(Long cmcId, CmcOpReceiverCfg cmcOpReceiverCfg) {
        Long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        CmcOpticalReceiverFacade facade = getCmcOpFacade(snmpParam);
        // 设置射频状态
        CmcOpReceiverRfCfg rfCfg = new CmcOpReceiverRfCfg();
        int rfIndex = cmcOpReceiverCfg.getOutputIndex();
        rfCfg.setOutputIndex(rfIndex);
        rfCfg.setOutputControl(cmcOpReceiverCfg.getOutputControl());
        // rfCfg.setOutputGainType(cmcOpReceiverCfg.getOutputGainType());
        rfCfg.setOutputAGCOrigin(cmcOpReceiverCfg.getOutputAGCOrigin());
        rfCfg.setOutputRFlevelatt(cmcOpReceiverCfg.getOutputRFlevelatt());
        facade.setOpReceiverRfCfg(snmpParam, rfCfg);
        // 设置开关状态
        // CmcOpReceiverSwitchCfg sCfg = new CmcOpReceiverSwitchCfg();
        // Integer sIndex = cmcOpReceiverCfg.getSwitchIndex();
        // sCfg.setSwitchIndex(sIndex);
        // sCfg.setSwitchControl(cmcOpReceiverCfg.getSwitchControl());
        // sCfg.setSwitchThres(cmcOpReceiverCfg.getSwitchThres());
        // facade.setOpReceiverSwitchCfg(snmpParam, sCfg);
        // 重新从设备获取射频状态配置
        rfCfg = facade.getOpReceiverRfCfg(snmpParam, rfIndex);
        // 重新从设备获取开关状态
        // sCfg = facade.getOpReceiverSwitchCfg(snmpParam, sIndex);
        // 更新数据库
        opticalReceiverDao.updateOpReceiverRfCfg(cmcId, rfCfg);
        // opticalReceiverDao.updateOpReceiverSwitchCfg(cmcId, sCfg);
    }

    @Override
    public void refreshOpReceiverInfo(Long cmcId) {
        Long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        int cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId).intValue();
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        CmcOpticalReceiverFacade facade = getCmcOpFacade(snmpParam);
        int index = generateNextIndex(cmcIndex);
        long dt = System.currentTimeMillis();
        Timestamp ts = new Timestamp(dt);
        if (isSupportOptical(snmpParam, index)) {
            // 获取接收光功率信息
            List<CmcOpReceiverInputInfo> inputInfoList = new ArrayList<CmcOpReceiverInputInfo>();
            int infoIndex = index;
            for (int i = 0; i < INFO_SIZE; i++) {
                CmcOpReceiverInputInfo inputInfo = facade.getOpReceiverInputInfo(snmpParam, infoIndex);
                inputInfo.setDt(dt);
                infoIndex = generateNextIndex(infoIndex);
                inputInfoList.add(inputInfo);
            }
            // TODO 获取光机类型信息
            CmcOpReceiverType opType = new CmcOpReceiverType();
            try {
                opType = facade.getOpReceiverType(snmpParam, index);
            } catch (Exception e) {
                logger.debug("refresh CmcOpReceiverType error", e);
            }

            // 获取RF配置信息
            CmcOpReceiverRfCfg rfCfg = facade.getOpReceiverRfCfg(snmpParam, index);
            rfCfg.setDt(dt);
            // 获取开关选择配置信息
            CmcOpReceiverSwitchCfg switchCfg = facade.getOpReceiverSwitchCfg(snmpParam, index);
            // 获取交流电源信息
            CmcOpReceiverAcPower acPower = facade.getOpReceiverAcPower(snmpParam, index);
            acPower.setDt(dt);
            // 获取直流电源信息
            List<CmcOpReceiverDcPower> dcPowerList = new ArrayList<CmcOpReceiverDcPower>();
            int powerIndex = index;
            CmcOpReceiverDcPower power = null;
            for (int i = 0; i < DCPOWER_SIZE; i++) {
                try {
                    power = facade.getOpReceiverDcPower(snmpParam, powerIndex);
                } catch (Exception e) {
                    logger.debug("refresh dcpower error", e);
                }
                power.setDt(dt);
                dcPowerList.add(power);
                powerIndex = generateNextIndex(powerIndex);
            }
            // 获取射频输出电平
            int rfIndex = index;
            List<CmcOpReceiverRfPort> rfPortList = new ArrayList<CmcOpReceiverRfPort>();
            CmcOpReceiverRfPort rfPort = null;
            for (int i = 0; i < RFPORT_SIZE; i++) {
                try {
                    rfPort = facade.getOpReceiverRfPort(snmpParam, rfIndex);
                } catch (Exception e) {
                    logger.debug("refresh RfPort error", e);
                }
                rfPort.setCollectTime(ts);
                rfPortList.add(rfPort);
                rfIndex = generateNextIndex(rfIndex);
            }
            // 获取载波频道数量
            CmcOpReceiverChannelNum channelNum = facade.getOpReceiverChannelNum(snmpParam, index);
            channelNum.setCollectTime(ts);
            // 更新数据库
            List<CmcOpReceiverInputInfo> inputInfoOld = opticalReceiverDao.selectOpReceiverInputInfoList(cmcId);
            CmcOpReceiverRfCfg rfCfgOld = opticalReceiverDao.selectOpReceiverRfCfg(cmcId);
            CmcOpReceiverType opTypeOld = opticalReceiverDao.selectOpReceiverType(cmcId);
            CmcOpReceiverSwitchCfg switchCfgOld = opticalReceiverDao.selectOpReceiverSwitchCfg(cmcId);
            CmcOpReceiverAcPower acPowerOld = opticalReceiverDao.selectOpReceiverAcPower(cmcId);
            CmcOpReceiverChannelNum channelNumOld = opticalReceiverDao.selectOpReceiverChannelNum(cmcId);
            List<CmcOpReceiverRfPort> rfPortListOld = opticalReceiverDao.selectOpReceiverRfPortList(cmcId);
            List<CmcOpReceiverDcPower> dcPowerListOld = opticalReceiverDao.selectOpReceiverDcPowerList(cmcId);
            // 仅有两条光机接收功率信息，不通过批量插入
            for (CmcOpReceiverInputInfo inputInfo : inputInfoList) {
                inputInfo.setCmcId(cmcId);
                if (inputInfoOld != null && inputInfoOld.contains(inputInfo)) {
                    opticalReceiverDao.updateOpReceiverInputInfo(cmcId, inputInfo);
                } else {
                    opticalReceiverDao.insertOpReceiverInputInfo(cmcId, inputInfo);
                }
            }

            if (rfCfgOld == null) {
                opticalReceiverDao.insertOpReceiverRfCfg(cmcId, rfCfg);
            } else {
                opticalReceiverDao.updateOpReceiverRfCfg(cmcId, rfCfg);
            }
            if (switchCfgOld == null) {
                opticalReceiverDao.insertOpReceiverSwitchCfg(cmcId, switchCfg);
            } else {
                opticalReceiverDao.updateOpReceiverSwitchCfg(cmcId, switchCfg);
            }
            if (opTypeOld == null) {
                opticalReceiverDao.insertOpReceiverType(cmcId, opType);
            } else {
                opticalReceiverDao.updateOpReceiverType(cmcId, opType);
            }
            if (acPowerOld == null) {
                opticalReceiverDao.insertOpReceiverAcPower(cmcId, acPower);
            } else {
                opticalReceiverDao.updateOpReceiverAcPower(cmcId, acPower);
            }
            if (channelNumOld == null) {
                opticalReceiverDao.insertOpReceiverChannelNum(cmcId, channelNum);
            } else {
                opticalReceiverDao.updateOpReceiverChannelNum(cmcId, channelNum);
            }
            // 仅有3条直流电源信息，不通过批量插入
            for (CmcOpReceiverDcPower dcPower : dcPowerList) {
                dcPower.setCmcId(cmcId);
                if (dcPowerListOld != null && dcPowerListOld.contains(dcPower)) {
                    opticalReceiverDao.updateOpReceiverDcPower(cmcId, dcPower);
                } else {
                    opticalReceiverDao.insertOpReceiverDcPower(cmcId, dcPower);
                }
            }
            // 仅有4条载波频道信息，不通过批量插入
            for (CmcOpReceiverRfPort obj : rfPortList) {
                obj.setCmcId(cmcId);
                if (rfPortListOld != null && rfPortListOld.contains(obj)) {
                    opticalReceiverDao.updateOpReceiverRfPort(cmcId, obj);
                } else {
                    opticalReceiverDao.insertOpReceiverRfPort(cmcId, obj);
                }
            }
        }
    }

    /**
     * 从CC获取数据，判断CC是否支持光机功能
     * 
     * @param snmpParam
     * @param index
     * @return
     */
    private Boolean isSupportOptical(SnmpParam snmpParam, Integer index) {
        try {
            getCmcOpFacade(snmpParam).getOpReceiverInputInfo(snmpParam, index);
            return true;
        } catch (SnmpNoSuchInstanceException e) {
            logger.error("CMC not support Optical Receiver!", e);
            return false;
        }

    }

    /**
     * 获取光机CmcOpticalReceiverFacade
     * 
     * @param snmpParam
     * @return
     */
    private CmcOpticalReceiverFacade getCmcOpFacade(SnmpParam snmpParam) {
        return facadeFactory.getFacade(snmpParam.getIpAddress(), CmcOpticalReceiverFacade.class);
    }

    /**
     * 按照光机Table的Index的规则，通过给定的index生成下一个index，光机的index使用低位开始的第二位为1开始编号，
     * 例如，第一个index为cmcIndex+2，第二个索引为cmcIndex + 2 +2。
     * 
     * @param index
     *            一个存在的index
     * @return 该index的下一个index
     */
    private Integer generateNextIndex(Integer index) {
        return index + 2;
    }

    @Override
    public List<Point> readOpticalReceivedPowerData(Long cmcId, Long index, String startTime, String endTime) {
        return opticalReceiverDao.selectOpReceiverInputInfoHis(cmcId, index, startTime, endTime);
    }

    @Override
    public Boolean isSupportOpticalReceiver(Long cmcId) {
        try {
            // 版本支持情况
            Boolean suppoerOpticalRead = deviceVersionService.isFunctionSupported(cmcId, "opticalReceiverRead");
            if (!suppoerOpticalRead) {
                return false;
            }
            // 是否有光机类型值
            CmcAttribute cmcAttribute = cmcDao.getCmcAttributeByCmcId(cmcId);
            String dorType = cmcAttribute.getTopCcmtsSysDorType();
            return dorType != null && !"".equals(dorType);
        } catch (Exception e) {
            logger.debug("isSupportOpticalReceiver error: " + e.getMessage());
            return false;
        }
    }

}
