/***********************************************************************
 * $Id: OpticalReceiverFacade.java,v1.0 2016年9月13日 下午3:20:09 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.facade;

import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorABSwitch;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorChannelNum;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorDCPower;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorDevParams;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwUpg;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdEq;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorLinePower;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRFPort;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRRXOptPow;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRevAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsOpRxInput;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsOpRxOutput;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsSysDorType;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author haojie
 * @created @2016年9月13日-下午3:20:09
 *
 */
@EngineFacade(serviceName = "OpticalReceiverFacade", beanName = "opticalReceiverFacade")
public interface OpticalReceiverFacade extends Facade {

    /**
     * 获取光机类型字符串
     * 
     * @param snmpParam
     * @param topCcmtsSysDorType
     * @return
     */
    TopCcmtsSysDorType getSysDorType(SnmpParam snmpParam, TopCcmtsSysDorType topCcmtsSysDorType);

    /**
     * State of A-B switch
     * 
     * @param snmpParam
     * @param topCcmtsDorABSwitch
     * @return
     */
    TopCcmtsDorABSwitch getABSwitch(SnmpParam snmpParam, TopCcmtsDorABSwitch topCcmtsDorABSwitch);

    /**
     * 正向频道数量
     * 
     * @param snmpParam
     * @param topCcmtsDorChannelNum
     * @return
     */
    TopCcmtsDorChannelNum getChannelNum(SnmpParam snmpParam, TopCcmtsDorChannelNum topCcmtsDorChannelNum);

    /**
     * DC Power
     * 
     * @param snmpParam
     * @param topCcmtsDorDCPower
     * @return
     */
    TopCcmtsDorDCPower getDCPower(SnmpParam snmpParam, TopCcmtsDorDCPower topCcmtsDorDCPower);

    /**
     * 光机基本参数表
     * 
     * @param snmpParam
     * @param topCcmtsDorDevParams
     * @return
     */
    TopCcmtsDorDevParams getDevParams(SnmpParam snmpParam, TopCcmtsDorDevParams topCcmtsDorDevParams);

    /**
     * 正向射频支路1-4衰减
     * 
     * @param snmpParam
     * @param topCcmtsDorFwdAtt
     * @return
     */
    TopCcmtsDorFwdAtt getFwdAtt(SnmpParam snmpParam, TopCcmtsDorFwdAtt topCcmtsDorFwdAtt);

    /**
     * 正向射频支路1-4均衡
     * 
     * @param snmpParam
     * @param topCcmtsDorFwdEq
     * @return
     */
    TopCcmtsDorFwdEq getFwdEq(SnmpParam snmpParam, TopCcmtsDorFwdEq topCcmtsDorFwdEq);

    /**
     * 同轴供电输入电压 AC 60V电压
     * 
     * @param snmpParam
     * @param topCcmtsDorLinePower
     * @return
     */
    TopCcmtsDorLinePower getLinePower(SnmpParam snmpParam, TopCcmtsDorLinePower topCcmtsDorLinePower);

    /**
     * 反向射频支路1-4衰减表
     * 
     * @param snmpParam
     * @param topCcmtsDorRevAtt
     * @return
     */
    TopCcmtsDorRevAtt getRevAtt(SnmpParam snmpParam, TopCcmtsDorRevAtt topCcmtsDorRevAtt);

    /**
     * RF1-4端口输出电平
     * 
     * @param snmpParam
     * @param topCcmtsDorRFPort
     * @return
     */
    TopCcmtsDorRFPort getRFPort(SnmpParam snmpParam, TopCcmtsDorRFPort topCcmtsDorRFPort);

    /**
     * 反向光收1-4输入光功率
     * 
     * @param snmpParam
     * @param topCcmtsDorRRXOptPow
     * @return
     */
    TopCcmtsDorRRXOptPow getRRXOptPow(SnmpParam snmpParam, TopCcmtsDorRRXOptPow topCcmtsDorRRXOptPow);

    /**
     * 正向光收A路光功率
     * 
     * @param snmpParam
     * @param topCcmtsOpRxInput
     * @return
     */
    TopCcmtsOpRxInput getOpRxInput(SnmpParam snmpParam, TopCcmtsOpRxInput topCcmtsOpRxInput);

    /**
     * 正向光发参数
     * 
     * @param snmpParam
     * @param topCcmtsOpRxOutput
     * @return
     */
    TopCcmtsOpRxOutput getOpRxOutput(SnmpParam snmpParam, TopCcmtsOpRxOutput topCcmtsOpRxOutput);

    /**
     * 修改正向光发参数
     * 
     * @param snmpParam
     * @param topCcmtsOpRxOutput
     */
    void modifyOpRxOutput(SnmpParam snmpParam, TopCcmtsOpRxOutput topCcmtsOpRxOutput);

    /**
     * 修改光机基本参数
     * 
     * @param snmpParam
     * @param topCcmtsDorDevParams
     */
    void modifyDevParams(SnmpParam snmpParam, TopCcmtsDorDevParams topCcmtsDorDevParams);

    /**
     * 修改正向射频支路衰减
     * 
     * @param snmpParam
     * @param topCcmtsDorFwdAtt
     */
    void modifyFwdAtt(SnmpParam snmpParam, TopCcmtsDorFwdAtt topCcmtsDorFwdAtt);

    /**
     * 修改正向射频支路均衡
     * 
     * @param snmpParam
     * @param topCcmtsDorFwdEq
     */
    void modifyFwdEq(SnmpParam snmpParam, TopCcmtsDorFwdEq topCcmtsDorFwdEq);

    /**
     * 修改反向射频支路衰减
     * 
     * @param snmpParam
     * @param topCcmtsDorRevAtt
     */
    void modifyRevAtt(SnmpParam snmpParam, TopCcmtsDorRevAtt topCcmtsDorRevAtt);

    /**
     * 修改正向频道数量
     * 
     * @param snmpParam
     * @param topCcmtsDorChannelNum
     */
    void modifyChannelNum(SnmpParam snmpParam, TopCcmtsDorChannelNum topCcmtsDorChannelNum);

    /**
     * 光机恢复出厂设置
     * 
     * @param snmpParam
     * @param generateNextIndex
     */
    void restorFactory(SnmpParam snmpParam, Long generateNextIndex);

    /**
     * 光机升级
     * 
     * @param snmpParam
     * @param topCcmtsDorFirmWare
     */
    void upgradeOpticalReceiver(SnmpParam snmpParam, TopCcmtsDorFwUpg topCcmtsDorFirmWare);

    /**
     * 获取升级进度
     * 
     * @param snmpParam
     * @param generateNextIndex
     * @return
     */
    Integer getUpdateProgress(SnmpParam snmpParam, Long generateNextIndex);

    /**
     * 实时刷新时, 刷新前改成实时模式 再去刷新 完成后再改成轮询模式
     * 
     * @param snmpParam
     * @param state
     */
    void setRealTimeSnmpDataStatus(SnmpParam snmpParam, String state);
}
