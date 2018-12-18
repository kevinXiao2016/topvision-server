/***********************************************************************
 * $Id: CmcOpticalReceiverPerfDBSaver.java,v1.0 2013-12-16 下午1:51:30 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.dbsaver;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.optical.engine.CmcOpticalReceiverDao;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverAcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverDcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverInputInfo;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfPort;
import com.topvision.ems.cmc.performance.domain.CmcOpticalReceiverPerfResult;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.framework.annotation.Engine;

/**
 * @author dosion
 * @created @2013-12-16-下午1:51:30
 * 
 */
@Engine("cmcOpticalReceiverPerfSaver")
public class CmcOpticalReceiverPerfSaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<CmcOpticalReceiverPerfResult, OperClass> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(CmcOpticalReceiverPerfResult result) {
        boolean isDebug = logger.isDebugEnabled();
        if (isDebug) {
            logger.debug("CmcOpticalReceiverPerfDBSaver identifyKey[" + result.getDomain().getIdentifyKey()
                    + "] exec start.");
        }
        CmcOpticalReceiverDao cmcOpticalReceiverDao = engineDaoFactory.getEngineDao(CmcOpticalReceiverDao.class);
        Long cmcId = result.getEntityId();
        // Input Info
        List<CmcOpReceiverInputInfo> inputInfoOld = cmcOpticalReceiverDao.selectOpReceiverInputInfoList(cmcId);
        List<CmcOpReceiverInputInfo> inputInfoList = result.getInputInfo();
        for (CmcOpReceiverInputInfo inputInfo : inputInfoList) {
            inputInfo.setCmcId(cmcId);
            if (inputInfoOld == null || !inputInfoOld.contains(inputInfo)) {
                cmcOpticalReceiverDao.insertOpReceiverInputInfo(cmcId, inputInfo);
            } else {
                cmcOpticalReceiverDao.updateOpReceiverInputInfo(cmcId, inputInfo);
            }
            try {
                redirctPerformanceData(inputInfo, result, cmcId, inputInfo.getInputIndex().longValue());
            } catch (Exception e) {
                logger.info("CmcOpticalReceiverPerfResult redirctPerformanceData info : " + result);
                logger.info("CmcOpticalReceiverPerfResult redirctPerformanceData error", e);
            }

            cmcOpticalReceiverDao.insertOpReceiverInputInfoHis(cmcId, inputInfo);
        }
        // AC Power
        CmcOpReceiverAcPower acPowerOld = cmcOpticalReceiverDao.selectOpReceiverAcPower(cmcId);
        if (acPowerOld == null) {
            cmcOpticalReceiverDao.insertOpReceiverAcPower(cmcId, result.getAcPower());
        } else {
            cmcOpticalReceiverDao.updateOpReceiverAcPower(cmcId, result.getAcPower());
        }
        // DC Power
        List<CmcOpReceiverDcPower> dcPowerList = result.getDcPowerList();
        CmcOpReceiverDcPower dcPowerOld = null;
        for (CmcOpReceiverDcPower dcPower : dcPowerList) {
            dcPowerOld = cmcOpticalReceiverDao.selectOpReceiverDcPower(cmcId, dcPower.getPowerIndex());
            if (dcPowerOld == null) {
                cmcOpticalReceiverDao.insertOpReceiverDcPower(cmcId, dcPower);
            } else {
                cmcOpticalReceiverDao.updateOpReceiverDcPower(cmcId, dcPower);
            }
        }
        // RF Port
        List<CmcOpReceiverRfPort> rfPortList = result.getRfPortList();
        List<CmcOpReceiverRfPort> rfPortListOld = cmcOpticalReceiverDao.selectOpReceiverRfPortList(cmcId);
        for (CmcOpReceiverRfPort obj : rfPortList) {
            obj.setCmcId(cmcId);
            if (rfPortListOld != null && rfPortListOld.contains(obj)) {
                cmcOpticalReceiverDao.updateOpReceiverRfPort(cmcId, obj);
            } else {
                cmcOpticalReceiverDao.insertOpReceiverRfPort(cmcId, obj);
            }
        }
        if (isDebug) {
            logger.debug("CmcOpticalReceiverPerfDBSaver identifyKey[" + result.getDomain().getIdentifyKey()
                    + "] exec end.");
        }
    }
}
