/***********************************************************************
 * $Id: OpticalReceiverInfo.java,v1.0 2016年9月19日 下午1:59:40 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.cmc.opticalreceiver.util.OpticalReceiverUtil;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2016年9月19日-下午1:59:40
 *
 */
public class OpticalReceiverData implements AliasesSuperType {
    private static final long serialVersionUID = 1010815862022322988L;
    public static final String NO_VALUE = "-";

    private Long entityId;
    private Long cmcId;
    private Long cmcIndex;
    private String topCcmtsSysDorType;// 光机类型字符串，CFA/CFB/CFC/CFD/EF/EP06/EP09/FFA/FFB
    private Integer abSwitchState;// State of A-B switch
    private Integer channelNum;// 正向频道数量
    private List<TopCcmtsDorDCPower> dcPowers;// 直流24V输出电压（DC12V/DC24V）

    private String platSN;// 光机平台序列号
    private Integer frxNum;// 下行接收光模块数量
    private Integer optNodeTemp;// 光机平台温度
    private Integer fwdEQ0;// 正向混合信号主路均衡
    private Integer fwdATT0;// 正向混合信号主路衰减
    private Integer revEQ;// 反向混合信号主路均衡
    private Integer rtxState;// 反向光发激光器工作状态 1:OFF 2：ON
    private Integer rtxLaserPower;// 反向光发输出光功率
    private Integer revATTUS;// DOCSIS US信号衰减
    private Integer revATTRTX;// 反向光路射频衰减
    private Integer rtxLaserCurrent;// 反向光发偏置电流
    private Integer catvInLevel;// TV IN输入电平
    private Integer catvInputState;// CATV输入选择 0: TV-IN 1:FRX-IN
    private Integer ftxOptPower;// 正向光发输出光功率
    private Integer ftxLaserCurrent;// 正向光发偏置电流

    private List<TopCcmtsDorFwdAtt> fwdAtts;// 正向射频支路1-4衰减
    private List<TopCcmtsDorFwdEq> fwdEqs;// 正向射频支路1-4均衡
    private Integer linePowerVoltage1;// 同轴供电输入电压 AC 60V电压
    private List<TopCcmtsDorRevAtt> revAtts;// 反向射频支路1-4衰减
    private List<TopCcmtsDorRFPort> rfPorts;// RF1-4端口输出电平
    private List<TopCcmtsDorRRXOptPow> rrxOptPows;// 反向光收1-4输入光功率

    private Integer inputPower;// 正向光收A路光功率

    private Integer outputControl;// 正向光收模块状态 1: off 2:on
    private Integer configurationOutputRFlevelatt;// 正向光发射频衰减
    private Integer configurationAGCRg;// AGC输入光功率范围,根据光机类型显示固定值

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the topCcmtsSysDorType
     */
    public String getTopCcmtsSysDorType() {
        return topCcmtsSysDorType;
    }

    /**
     * @param topCcmtsSysDorType
     *            the topCcmtsSysDorType to set
     */
    public void setTopCcmtsSysDorType(String topCcmtsSysDorType) {
        this.topCcmtsSysDorType = topCcmtsSysDorType;
    }

    /**
     * @return the abSwitchState
     */
    public Integer getAbSwitchState() {
        return abSwitchState;
    }

    /**
     * @param abSwitchState
     *            the abSwitchState to set
     */
    public void setAbSwitchState(Integer abSwitchState) {
        this.abSwitchState = abSwitchState;
    }

    /**
     * @return the channelNum
     */
    public Integer getChannelNum() {
        return channelNum;
    }

    /**
     * @param channelNum
     *            the channelNum to set
     */
    public void setChannelNum(Integer channelNum) {
        this.channelNum = channelNum;
    }

    /**
     * @return the dcPowers
     */
    public List<TopCcmtsDorDCPower> getDcPowers() {
        return dcPowers;
    }

    public String getDcPowersStr() {
        if (dcPowers == null || dcPowers.size() == 0) {
            return "";
        } else {
            String powerStr = "";
            for (TopCcmtsDorDCPower power : dcPowers) {
                if (power.getDcPowerVoltage() == null) {
                    powerStr += NO_VALUE + "/";
                } else {
                    powerStr += Double.valueOf(power.getDcPowerVoltage()) / 10 + "/";
                }
            }
            return powerStr.substring(0, powerStr.length() - 1);
        }
    }

    /**
     * @param dcPowers
     *            the dcPowers to set
     */
    public void setDcPowers(List<TopCcmtsDorDCPower> dcPowers) {
        this.dcPowers = dcPowers;
    }

    /**
     * @return the platSN
     */
    public String getPlatSN() {
        return platSN;
    }

    /**
     * @param platSN
     *            the platSN to set
     */
    public void setPlatSN(String platSN) {
        this.platSN = platSN;
    }

    /**
     * @return the frxNum
     */
    public Integer getFrxNum() {
        return frxNum;
    }

    /**
     * @param frxNum
     *            the frxNum to set
     */
    public void setFrxNum(Integer frxNum) {
        this.frxNum = frxNum;
    }

    /**
     * @return the optNodeTemp
     */
    public Integer getOptNodeTemp() {
        return optNodeTemp;
    }

    /**
     * @return the optNodeTemp
     */
    public String getOptNodeTempStr() {
        if (optNodeTemp == null) {
            return NO_VALUE;
        }

        return String.valueOf(Double.valueOf(optNodeTemp.toString()) / 10);
    }

    /**
     * @param optNodeTemp
     *            the optNodeTemp to set
     */
    public void setOptNodeTemp(Integer optNodeTemp) {
        this.optNodeTemp = optNodeTemp;
    }

    /**
     * @return the fwdEQ0
     */
    public Integer getFwdEQ0() {
        return fwdEQ0;
    }

    /**
     * @param fwdEQ0
     *            the fwdEQ0 to set
     */
    public void setFwdEQ0(Integer fwdEQ0) {
        this.fwdEQ0 = fwdEQ0;
    }

    /**
     * @return the fwdATT0
     */
    public Integer getFwdATT0() {
        return fwdATT0;
    }

    /**
     * @param fwdATT0
     *            the fwdATT0 to set
     */
    public void setFwdATT0(Integer fwdATT0) {
        this.fwdATT0 = fwdATT0;
    }

    /**
     * @return the revEQ
     */
    public Integer getRevEQ() {
        return revEQ;
    }

    /**
     * @param revEQ
     *            the revEQ to set
     */
    public void setRevEQ(Integer revEQ) {
        this.revEQ = revEQ;
    }

    /**
     * @return the rtxState
     */
    public Integer getRtxState() {
        return rtxState;
    }

    /**
     * @param rtxState
     *            the rtxState to set
     */
    public void setRtxState(Integer rtxState) {
        this.rtxState = rtxState;
    }

    /**
     * @return the rtxLaserPower
     */
    public Integer getRtxLaserPower() {
        return rtxLaserPower;
    }

    /**
     * @param rtxLaserPower
     *            the rtxLaserPower to set
     */
    public void setRtxLaserPower(Integer rtxLaserPower) {
        this.rtxLaserPower = rtxLaserPower;
    }

    /**
     * @return the inputPower
     */
    public String getRtxLaserPowerStr() {
        if (rtxLaserPower == null) {
            return NO_VALUE;
        }
        return String.valueOf(Double.valueOf(rtxLaserPower) / 10);
    }

    /**
     * @return the revATTUS
     */
    public Integer getRevATTUS() {
        return revATTUS;
    }

    /**
     * @param revATTUS
     *            the revATTUS to set
     */
    public void setRevATTUS(Integer revATTUS) {
        this.revATTUS = revATTUS;
    }

    /**
     * @return the revATTRTX
     */
    public Integer getRevATTRTX() {
        return revATTRTX;
    }

    /**
     * @param revATTRTX
     *            the revATTRTX to set
     */
    public void setRevATTRTX(Integer revATTRTX) {
        this.revATTRTX = revATTRTX;
    }

    /**
     * @return the rtxLaserCurrent
     */
    public Integer getRtxLaserCurrent() {
        return rtxLaserCurrent;
    }

    /**
     * @param rtxLaserCurrent
     *            the rtxLaserCurrent to set
     */
    public void setRtxLaserCurrent(Integer rtxLaserCurrent) {
        this.rtxLaserCurrent = rtxLaserCurrent;
    }

    /**
     * @return the catvInLevel
     */
    public Integer getCatvInLevel() {
        return catvInLevel;
    }

    /**
     * @param catvInLevel
     *            the catvInLevel to set
     */
    public void setCatvInLevel(Integer catvInLevel) {
        this.catvInLevel = catvInLevel;
    }

    /**
     * @return the catvInputState
     */
    public Integer getCatvInputState() {
        return catvInputState;
    }

    /**
     * @param catvInputState
     *            the catvInputState to set
     */
    public void setCatvInputState(Integer catvInputState) {
        this.catvInputState = catvInputState;
    }

    /**
     * @return the ftxOptPower
     */
    public Integer getFtxOptPower() {
        return ftxOptPower;
    }

    /**
     * @param ftxOptPower
     *            the ftxOptPower to set
     */
    public void setFtxOptPower(Integer ftxOptPower) {
        this.ftxOptPower = ftxOptPower;
    }

    /**
     * @return the inputPower
     */
    public String getFtxOptPowerStr() {
        if (ftxOptPower == null) {
            return NO_VALUE;
        }
        return String.valueOf(Double.valueOf(ftxOptPower) / 10);
    }

    /**
     * @return the ftxLaserCurrent
     */
    public Integer getFtxLaserCurrent() {
        return ftxLaserCurrent;
    }

    /**
     * @param ftxLaserCurrent
     *            the ftxLaserCurrent to set
     */
    public void setFtxLaserCurrent(Integer ftxLaserCurrent) {
        this.ftxLaserCurrent = ftxLaserCurrent;
    }

    /**
     * @return the fwdAtts
     */
    public List<TopCcmtsDorFwdAtt> getFwdAtts() {
        return fwdAtts;
    }

    public String getFwdAttsStr() {
        if (fwdAtts == null || fwdAtts.size() == 0) {
            return "";
        } else {
            if(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_CFD.equals(topCcmtsSysDorType) && fwdAtts.size() >= 2) {
                fwdAtts = fwdAtts.subList(0, 2);
            }
            String fwdAttsStr = "";
            for (TopCcmtsDorFwdAtt fwdAtt : fwdAtts) {
                fwdAttsStr += formatValue(fwdAtt.getFwdAtt()) + "/";
            }
            return fwdAttsStr.substring(0, fwdAttsStr.length() - 1);
        }
    }

    public void setFwdAttsStr(String fwdAttsStr) {
        if (fwdAttsStr != null && fwdAttsStr.indexOf("/") != -1) {
            String[] values = fwdAttsStr.split("/");
            List<TopCcmtsDorFwdAtt> fwdAtts = new ArrayList<TopCcmtsDorFwdAtt>();
            Long index = cmcIndex;
            for (String value : values) {
                index = OpticalReceiverUtil.generateNextIndex(index);
                if (!"".equals(value) && !NO_VALUE.equals(value)) {
                    try {
                        Integer intValue = Integer.valueOf(value);
                        TopCcmtsDorFwdAtt fwdAtt = new TopCcmtsDorFwdAtt();
                        fwdAtt.setCmcId(cmcId);
                        fwdAtt.setFwdAttIndex(index);
                        fwdAtt.setFwdAtt(intValue);
                        fwdAtts.add(fwdAtt);
                    } catch (Exception e) {
                        // TODO 值非法，记录
                    }
                }
            }
            this.fwdAtts = fwdAtts;
        }
    }

    /**
     * @param fwdAtts
     *            the fwdAtts to set
     */
    public void setFwdAtts(List<TopCcmtsDorFwdAtt> fwdAtts) {
        this.fwdAtts = fwdAtts;
    }

    /**
     * @return the fwdEqs
     */
    public List<TopCcmtsDorFwdEq> getFwdEqs() {
        return fwdEqs;
    }

    public String getFwdEqsStr() {
        if (fwdEqs == null || fwdEqs.size() == 0) {
            return "";
        } else {
            if(TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_CFD.equals(topCcmtsSysDorType) && fwdEqs.size() >= 2) {
                fwdEqs = fwdEqs.subList(0, 2);
            }
            
            String fwdEqsStr = "";
            for (TopCcmtsDorFwdEq fwdEq : fwdEqs) {
                fwdEqsStr += formatValue(fwdEq.getFwdEq()) + "/";
            }
            return fwdEqsStr.substring(0, fwdEqsStr.length() - 1);
        }
    }

    public void setFwdEqsStr(String fwdEqsStr) {
        if (fwdEqsStr != null && fwdEqsStr.indexOf("/") != -1) {
            String[] values = fwdEqsStr.split("/");
            List<TopCcmtsDorFwdEq> fwdEqs = new ArrayList<TopCcmtsDorFwdEq>();
            Long index = cmcIndex;
            for (String value : values) {
                index = OpticalReceiverUtil.generateNextIndex(index);
                if (!"".equals(value) && !NO_VALUE.equals(value)) {
                    try {
                        Integer intValue = Integer.valueOf(value);
                        TopCcmtsDorFwdEq fwdEq = new TopCcmtsDorFwdEq();
                        fwdEq.setCmcId(cmcId);
                        fwdEq.setFwdEqIndex(index);
                        fwdEq.setFwdEq(intValue);
                        fwdEqs.add(fwdEq);
                    } catch (Exception e) {
                        // TODO 值非法，记录
                    }
                }
            }
            this.fwdEqs = fwdEqs;
        }
    }

    /**
     * @param fwdEqs
     *            the fwdEqs to set
     */
    public void setFwdEqs(List<TopCcmtsDorFwdEq> fwdEqs) {
        this.fwdEqs = fwdEqs;
    }

    /**
     * @return the linePowerVoltage1
     */
    public Integer getLinePowerVoltage1() {
        return linePowerVoltage1;
    }

    public String getLinePowerVoltage1Str() {
        if (linePowerVoltage1 == null) {
            return NO_VALUE;
        }
        return String.valueOf(Double.valueOf(linePowerVoltage1) / 10);
    }

    /**
     * @param linePowerVoltage1
     *            the linePowerVoltage1 to set
     */
    public void setLinePowerVoltage1(Integer linePowerVoltage1) {
        this.linePowerVoltage1 = linePowerVoltage1;
    }

    /**
     * @return the revAtts
     */
    public List<TopCcmtsDorRevAtt> getRevAtts() {
        return revAtts;
    }

    public String getRevAttsStr() {
        if (revAtts == null || revAtts.size() == 0) {
            return "";
        } else {
            String revAttsStr = "";
            for (TopCcmtsDorRevAtt revAtt : revAtts) {
                revAttsStr += formatValue(revAtt.getRevAtt()) + "/";
            }
            return revAttsStr.substring(0, revAttsStr.length() - 1);
        }
    }

    public void setRevAttsStr(String revAttsStr) {
        if (revAttsStr != null && revAttsStr.indexOf("/") != -1) {
            String[] values = revAttsStr.split("/");
            List<TopCcmtsDorRevAtt> revAtts = new ArrayList<TopCcmtsDorRevAtt>();
            Long index = cmcIndex;
            for (String value : values) {
                index = OpticalReceiverUtil.generateNextIndex(index);
                if (!"".equals(value) && !NO_VALUE.equals(value)) {
                    try {
                        Integer intValue = Integer.valueOf(value);
                        TopCcmtsDorRevAtt revAtt = new TopCcmtsDorRevAtt();
                        revAtt.setCmcId(cmcId);
                        revAtt.setRevAttIndex(index);
                        revAtt.setRevAtt(intValue);
                        revAtts.add(revAtt);
                    } catch (Exception e) {
                        // TODO 值非法，记录
                    }
                }
            }
            this.revAtts = revAtts;
        }
    }

    /**
     * @param revAtts
     *            the revAtts to set
     */
    public void setRevAtts(List<TopCcmtsDorRevAtt> revAtts) {
        this.revAtts = revAtts;
    }

    /**
     * @return the rfPorts
     */
    public List<TopCcmtsDorRFPort> getRfPorts() {
        return rfPorts;
    }

    public String getRfPortsStr() {
        if (rfPorts == null || rfPorts.size() == 0) {
            return "";
        } else {
            String portStr = "";
            for (TopCcmtsDorRFPort port : rfPorts) {
                portStr += formatValue(port.getRfPortOutputRFLevel()) + "/";
            }
            return portStr.substring(0, portStr.length() - 1);
        }
    }

    /**
     * @param rfPorts
     *            the rfPorts to set
     */
    public void setRfPorts(List<TopCcmtsDorRFPort> rfPorts) {
        this.rfPorts = rfPorts;
    }

    /**
     * @return the rrxOptPows
     */
    public List<TopCcmtsDorRRXOptPow> getRrxOptPows() {
        return rrxOptPows;
    }

    public String getRrxOptPowsStr() {
        if (rrxOptPows == null || rrxOptPows.size() == 0) {
            return "";
        } else {
            String rrxOptPowsStr = "";
            for (TopCcmtsDorRRXOptPow rrxOptPows : rrxOptPows) {
                Integer optPow = rrxOptPows.getRrxOptPow();
                if (optPow == null) {
                    rrxOptPowsStr += NO_VALUE + "/";
                } else {
                    rrxOptPowsStr += Double.valueOf(rrxOptPows.getRrxOptPow()) / 10 + "/";
                }
            }
            return rrxOptPowsStr.substring(0, rrxOptPowsStr.length() - 1);
        }
    }

    /**
     * @param rrxOptPows
     *            the rrxOptPows to set
     */
    public void setRrxOptPows(List<TopCcmtsDorRRXOptPow> rrxOptPows) {
        this.rrxOptPows = rrxOptPows;
    }

    /**
     * @return the inputPower
     */
    public Integer getInputPower() {
        return inputPower;
    }

    /**
     * @param inputPower
     *            the inputPower to set
     */
    public void setInputPower(Integer inputPower) {
        this.inputPower = inputPower;
    }

    /**
     * @return the inputPower
     */
    public String getInputPowerStr() {
        if (inputPower == null) {
            return NO_VALUE;
        }
        return String.valueOf(Double.valueOf(inputPower) / 10);
    }

    /**
     * @return the outputControl
     */
    public Integer getOutputControl() {
        return outputControl;
    }

    /**
     * @param outputControl
     *            the outputControl to set
     */
    public void setOutputControl(Integer outputControl) {
        this.outputControl = outputControl;
    }

    /**
     * @return the configurationOutputRFlevelatt
     */
    public Integer getConfigurationOutputRFlevelatt() {
        return configurationOutputRFlevelatt;
    }

    /**
     * @param configurationOutputRFlevelatt
     *            the configurationOutputRFlevelatt to set
     */
    public void setConfigurationOutputRFlevelatt(Integer configurationOutputRFlevelatt) {
        this.configurationOutputRFlevelatt = configurationOutputRFlevelatt;
    }

    /**
     * @return the configurationAGCRg
     */
    public Integer getConfigurationAGCRg() {
        return configurationAGCRg;
    }
    
    /**
     * @return the configurationAGCRg
     */
    public String getConfigurationAGCRgStr() {
        if(configurationAGCRg != null && configurationAGCRg.equals(1)) {
            return "[-7,+2]";
        } else {
            
            return NO_VALUE;
        }
    }

    /**
     * @param configurationAGCRg
     *            the configurationAGCRg to set
     */
    public void setConfigurationAGCRg(Integer configurationAGCRg) {
        this.configurationAGCRg = configurationAGCRg;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex
     *            the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    /**
     * 如果没有数值，统一显示 -
     * 
     * @param value
     * @return
     */
    private Object formatValue(Object value) {
        if (value == null) {
            return NO_VALUE;
        }
        return value;
    }

}
