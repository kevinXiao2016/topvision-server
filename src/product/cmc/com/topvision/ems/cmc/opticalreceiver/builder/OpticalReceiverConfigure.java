/***********************************************************************
 * $Id: OpticalReceiverConfigure.java,v1.0 2016年11月11日 下午2:47:24 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.builder;

import java.util.List;

import com.topvision.ems.cmc.opticalreceiver.dao.OpticalReceiverRefreshDao;
import com.topvision.ems.cmc.opticalreceiver.domain.OpticalReceiverData;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorChannelNum;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorDevParams;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdEq;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRevAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsOpRxOutput;
import com.topvision.ems.cmc.opticalreceiver.facade.OpticalReceiverFacade;
import com.topvision.ems.cmc.opticalreceiver.util.OpticalReceiverUtil;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author vanzand
 * @created @2016年11月11日-下午2:47:24
 *
 */
public abstract class OpticalReceiverConfigure {

    private OpticalReceiverRefreshDao opticalReceiverRefreshDao;

    private SnmpParam snmpParam;
    private OpticalReceiverFacade facade;
    private OpticalReceiverData opr;

    public OpticalReceiverConfigure(OpticalReceiverData opticalReceiverData, SnmpParam snmpParam,
            OpticalReceiverFacade facade, OpticalReceiverRefreshDao opticalReceiverRefreshDao) {
        // 引入entityDao
        this.snmpParam = snmpParam;
        this.facade = facade;
        this.opr = opticalReceiverData;
        this.opticalReceiverRefreshDao = opticalReceiverRefreshDao;
    }

    /**
     * 修改正向输出数据
     */
    protected void modifyOpRxOutput() {
        TopCcmtsOpRxOutput topCcmtsOpRxOutput = new TopCcmtsOpRxOutput();
        topCcmtsOpRxOutput.setCmcId(opr.getCmcId());
        topCcmtsOpRxOutput.setOutputIndex(OpticalReceiverUtil.generateNextIndex(opr.getCmcIndex()));
        if (opr.getConfigurationOutputRFlevelatt() != null) {
            topCcmtsOpRxOutput.setConfigurationOutputRFlevelatt(opr.getConfigurationOutputRFlevelatt());
        }
        if (opr.getOutputControl() != null) {
            topCcmtsOpRxOutput.setOutputControl(opr.getOutputControl());
        }
        facade.modifyOpRxOutput(snmpParam, topCcmtsOpRxOutput);
        opticalReceiverRefreshDao.insertOrUpdateOpRxOutput(topCcmtsOpRxOutput);
    }

    /**
     * 修改光机基本参数
     * 
     * @param snmpParam
     * @param facade
     * @param opr
     */
    protected void modifyDevParams() {
        TopCcmtsDorDevParams topCcmtsDorDevParams = new TopCcmtsDorDevParams();
        topCcmtsDorDevParams.setCmcId(opr.getCmcId());
        topCcmtsDorDevParams.setDevIndex(OpticalReceiverUtil.generateNextIndex(opr.getCmcIndex()));
        if (opr.getFwdEQ0() != null) {
            topCcmtsDorDevParams.setFwdEQ0(opr.getFwdEQ0());
        }
        if (opr.getFwdATT0() != null) {
            topCcmtsDorDevParams.setFwdATT0(opr.getFwdATT0());
        }
        if (opr.getRevATTUS() != null) {
            topCcmtsDorDevParams.setRevATTUS(opr.getRevATTUS());
        }
        if (opr.getRevATTRTX() != null) {
            topCcmtsDorDevParams.setRevATTRTX(opr.getRevATTRTX());
        }
        if (opr.getRevEQ() != null) {
            topCcmtsDorDevParams.setRevEQ(opr.getRevEQ());
        }
        if (opr.getRtxState() != null) {
            topCcmtsDorDevParams.setRtxState(opr.getRtxState());
        }
        if (opr.getCatvInputState() != null) {
            topCcmtsDorDevParams.setCatvInputState(opr.getCatvInputState());
        }
        facade.modifyDevParams(snmpParam, topCcmtsDorDevParams);
        opticalReceiverRefreshDao.insertOrUpdateDevParams(topCcmtsDorDevParams);
    }

    /**
     * 修改正向射频支路均衡
     * 
     * @param snmpParam
     * @param facade
     * @param fwdEqs
     */
    protected void modifyFwdEqs() {
        List<TopCcmtsDorFwdEq> fwdEqs = opr.getFwdEqs();
        for (TopCcmtsDorFwdEq topCcmtsDorFwdEq : fwdEqs) {
            facade.modifyFwdEq(snmpParam, topCcmtsDorFwdEq);
        }
        opticalReceiverRefreshDao.batchInsertOrUpdateFwdEqs(fwdEqs);
    }

    /**
     * 修改正向射频支路衰减
     * 
     * @param snmpParam
     * @param facade
     * @param fwdAtts
     */
    protected void modifyFwdAtts() {
        List<TopCcmtsDorFwdAtt> fwdAtts = opr.getFwdAtts();
        for (TopCcmtsDorFwdAtt topCcmtsDorFwdAtt : fwdAtts) {
            facade.modifyFwdAtt(snmpParam, topCcmtsDorFwdAtt);
        }
        opticalReceiverRefreshDao.batchInsertOrUpdateFwdAtts(fwdAtts);
    }

    /**
     * 修改反向射频支路衰减
     * 
     * @param snmpParam
     * @param facade
     * @param revAtts
     */
    protected void mofiyRevAtts() {
        List<TopCcmtsDorRevAtt> revAtts = opr.getRevAtts();
        for (TopCcmtsDorRevAtt topCcmtsDorRevAtt : revAtts) {
            facade.modifyRevAtt(snmpParam, topCcmtsDorRevAtt);
        }
        opticalReceiverRefreshDao.batchInsertOrUpdateRevAtts(revAtts);
    }

    /**
     * 修改正向频道数量
     * 
     * @param snmpParam
     * @param facade
     * @param opr
     */
    protected void modifyChannelNum() {
        TopCcmtsDorChannelNum topCcmtsDorChannelNum = new TopCcmtsDorChannelNum();
        topCcmtsDorChannelNum.setCmcId(opr.getCmcId());
        topCcmtsDorChannelNum.setChannelNumIndex(OpticalReceiverUtil.generateNextIndex(opr.getCmcIndex()));
        if (opr.getChannelNum() != null) {
            topCcmtsDorChannelNum.setChannelNum(opr.getChannelNum());
        }
        facade.modifyChannelNum(snmpParam, topCcmtsDorChannelNum);
        opticalReceiverRefreshDao.insertOrUpdateChannelNum(topCcmtsDorChannelNum);
    }

    abstract public void modifyConfig();

}
