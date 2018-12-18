/***********************************************************************
 * $Id: OpticalReceiverServiceImpl.java,v1.0 2016年9月13日 下午3:15:08 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.service.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.opticalreceiver.builder.OpticalReceiverConfigure;
import com.topvision.ems.cmc.opticalreceiver.dao.OpticalReceiverDao;
import com.topvision.ems.cmc.opticalreceiver.dao.OpticalReceiverRefreshDao;
import com.topvision.ems.cmc.opticalreceiver.domain.OpticalReceiverData;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorDCPower;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwUpg;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdEq;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRFPort;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRRXOptPow;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRevAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsSysDorType;
import com.topvision.ems.cmc.opticalreceiver.facade.OpticalReceiverFacade;
import com.topvision.ems.cmc.opticalreceiver.service.OpticalReceiverRefreshService;
import com.topvision.ems.cmc.opticalreceiver.service.OpticalReceiverService;
import com.topvision.ems.cmc.opticalreceiver.util.OpticalReceiverUtil;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author haojie
 * @created @2016年9月13日-下午3:15:08
 *
 */
@Service("opticalReceiverService")
public class OpticalReceiverServiceImpl extends BaseService implements OpticalReceiverService {
    @Autowired
    private OpticalReceiverDao opticalReceiverDao;
    @Autowired
    private OpticalReceiverRefreshDao opticalReceiverRefreshDao;
    @Autowired
    private CmcDao cmcDao;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private OpticalReceiverRefreshService opticalReceiverRefreshService;
    @Resource(name = "cmcService")
    private CmcService cmcService;

    @Override
    public OpticalReceiverData getOpticalReceiverInfo(Long cmcId) {
        OpticalReceiverData opticalReceiverData = opticalReceiverDao.getOpticalReceiverData(cmcId);
        // 获取直流电压信息
        List<TopCcmtsDorDCPower> dcPowers = opticalReceiverDao.getDCPowers(cmcId);
        if (dcPowers != null) {
            opticalReceiverData.setDcPowers(dcPowers);
        }
        // 获取正向射频支路1-4衰减
        List<TopCcmtsDorFwdAtt> fwdAtts = opticalReceiverDao.getFwdAtts(cmcId);
        if (fwdAtts != null) {
            opticalReceiverData.setFwdAtts(fwdAtts);
        }
        // 获取正向射频支路1-4均衡
        List<TopCcmtsDorFwdEq> fwdEqs = opticalReceiverDao.getFwdEqs(cmcId);
        if (fwdEqs != null) {
            opticalReceiverData.setFwdEqs(fwdEqs);
        }
        // 获取反向射频支路1-4衰减
        List<TopCcmtsDorRevAtt> revAtts = opticalReceiverDao.getRevAtts(cmcId);
        if (revAtts != null) {
            opticalReceiverData.setRevAtts(revAtts);
        }
        // 获取RF1-4端口输出电平
        List<TopCcmtsDorRFPort> rfPorts = opticalReceiverDao.getRfPorts(cmcId);
        if (rfPorts != null) {
            opticalReceiverData.setRfPorts(rfPorts);
        }
        // 获取反向光收1-4输入光功率
        List<TopCcmtsDorRRXOptPow> rrxOptPows = opticalReceiverDao.getRrxOptPows(cmcId);
        if (rrxOptPows != null) {
            opticalReceiverData.setRrxOptPows(rrxOptPows);
        }
        return opticalReceiverData;
    }

    @Override
    public void modifyOpticalReceiver(OpticalReceiverData opticalReceiverData) {
        // 封装参数
        Long cmcId = opticalReceiverData.getCmcId();
        opticalReceiverData.setEntityId(cmcDao.getEntityIdByCmcId(cmcId));
        opticalReceiverData.setCmcIndex(cmcDao.getCmcIndexByCmcId(cmcId));
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(opticalReceiverData.getEntityId());
        OpticalReceiverFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), OpticalReceiverFacade.class);

        // 构造出合适的光机类型配置对象
        OpticalReceiverConfigure configure = initOpticalReceiverConfigure(opticalReceiverData, snmpParam, facade);
        // 调用配置方法
        configure.modifyConfig();
    }

    /**
     * 构建对应的设置配置对象
     * 
     * @param opticalReceiverData
     * @param snmpParam
     * @param facade
     * @return
     */
    private OpticalReceiverConfigure initOpticalReceiverConfigure(OpticalReceiverData opticalReceiverData,
            SnmpParam snmpParam, OpticalReceiverFacade facade) {
        String dorType = opticalReceiverData.getTopCcmtsSysDorType();
        String className = "com.topvision.ems.cmc.opticalreceiver.builder." + dorType.toUpperCase()
                + "OpticalReceiverConfigure";
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> con = clazz.getConstructor(new Class[] { OpticalReceiverData.class, SnmpParam.class,
                    OpticalReceiverFacade.class, OpticalReceiverRefreshDao.class });
            OpticalReceiverConfigure configure = (OpticalReceiverConfigure) con.newInstance(new Object[] {
                    opticalReceiverData, snmpParam, facade, opticalReceiverRefreshDao });
            return configure;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            logger.error("", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void restorFactory(Long cmcId) {
        Long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        facade.restorFactory(snmpParam, OpticalReceiverUtil.generateNextIndex(cmcIndex));
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

    @Override
    public void refreshOpticalReceiver(Long cmcId) {
        Long entityId = cmcDao.getEntityIdByCmcId(cmcId);
        Long cmcIndex = cmcDao.getCmcIndexByCmcId(cmcId);
        String dorType = opticalReceiverRefreshService.refreshSysDorType(entityId, cmcIndex);
        // 当有光机的CC才进行其余业务数据的刷新
        if (dorType != null && !"".equals(dorType)) {
            try {
                // 实时采集数据前,先改成实时模式 再去刷新
                setRealTimeSnmpDataStatus(entityId, "1");

                // 光机基本参数，所有类型都支持
                opticalReceiverRefreshService.refreshDevParams(entityId, cmcIndex);
                logger.info("refreshDevParams finish");

                // 直流24V输出电压（DC12V/DC24V），所有类型都支持
                opticalReceiverRefreshService.refreshDCPower(entityId, cmcIndex);
                logger.info("refreshDCPower finish");

                if (!dorType.equals(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_EP06)
                        && !dorType.equals(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_EP09)) {
                    // 正向光收A路光功率，EP06/EP09除外
                    opticalReceiverRefreshService.refreshOpRxInput(entityId, cmcIndex);
                    logger.info("refreshOpRxInput finish");

                    // 正向光收模块状态/正向光发射频衰减/AGC输入光功率范围 ，EP06/EP09除外
                    opticalReceiverRefreshService.refreshOpRxOutput(entityId, cmcIndex);
                    logger.info("refreshOpRxOutput finish");

                    // 同轴供电输入电压 AC 60V电压，EP06/EP09除外
                    opticalReceiverRefreshService.refreshLinePower(entityId, cmcIndex);
                    logger.info("refreshLinePower finish");
                }

                // 正向频道数量，EP06/EP09/CFA/EF除外
                if (!dorType.equals(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_EP06)
                        && !dorType.equals(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_EP09)
                        && !dorType.equals(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_CFA)
                        && !dorType.equals(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_EF)) {
                    opticalReceiverRefreshService.refreshChannelNum(entityId, cmcIndex);
                    logger.info("refreshChannelNum finish");
                }

                // RF1-4端口输出电平，目前只有CFB/FFA/FFB支持
                if (dorType.equals(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_CFB)
                        || dorType.equals(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_FFA)
                        || dorType.equals(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_FFB)) {
                    opticalReceiverRefreshService.refreshRFPort(entityId, cmcIndex);
                    logger.info("refreshRFPort finish");
                }

                if (dorType.equals(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_CFD)
                        || dorType.equals(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_FFA)
                        || dorType.equals(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_FFB)) {

                    // 正向射频支路1-4衰减，目前只有CFD/FFA/FFB支持
                    opticalReceiverRefreshService.refreshFwdAtt(entityId, cmcIndex);
                    logger.info("refreshFwdAtt finish");

                    // 正向射频支路1-4均衡，目前只有CFD/FFA/FFB支持
                    opticalReceiverRefreshService.refreshFwdEq(entityId, cmcIndex);
                    logger.info("refreshfwdEq finish");

                    // 反向射频支路1-4衰减表，目前只有CFD/FFA/FFB支持
                    opticalReceiverRefreshService.refreshRevAtt(entityId, cmcIndex);
                    logger.info("refreshRevAtt finish");
                }

                // 反向光收1-4输入光功率，目前只有EP06/EP09支持
                if (dorType.equals(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_EP06)
                        || dorType.equals(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_EP09)) {
                    opticalReceiverRefreshService.refreshRRXOptPow(entityId, cmcIndex);
                    logger.info("refreshRRXOptPow finish");
                }
            } finally {
                // 实时采集数据完成后,再改成轮询模式
                setRealTimeSnmpDataStatus(entityId, "2");
            }

        }
    }

    @Override
    public void upgradeOpticalReceiver(TopCcmtsDorFwUpg topCcmtsDorFwUpg) {
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(topCcmtsDorFwUpg.getEntityId());
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        facade.upgradeOpticalReceiver(snmpParam, topCcmtsDorFwUpg);
    }

    @Override
    public Integer getUpdateProgress(Long entityId) {
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        Long cmcIndex = cmcService.getCmcIndexByCmcId(entityId);
        Long fwUpgDevIndex = OpticalReceiverUtil.generateNextIndex(cmcIndex);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        int result = facade.getUpdateProgress(snmpParam, fwUpgDevIndex);
        return result;
    }

    /**
     * 实时刷新时, 刷新前改成实时模式 再去刷新 完成后再改成轮询模式
     * 
     * @param entityId
     * @param state
     */
    private void setRealTimeSnmpDataStatus(Long entityId, String state) {
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        OpticalReceiverFacade facade = getOpticalReceiverFacade(snmpParam);
        facade.setRealTimeSnmpDataStatus(snmpParam, state);
    }

}
