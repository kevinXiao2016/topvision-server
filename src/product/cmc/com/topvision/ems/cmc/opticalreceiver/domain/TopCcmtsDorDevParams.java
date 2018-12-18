/***********************************************************************
 * $Id: TopCcmtsDorDevParams.java,v1.0 2016年9月13日 下午2:13:10 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2016年9月13日-下午2:13:10
 *
 */
public class TopCcmtsDorDevParams implements AliasesSuperType {
    private static final long serialVersionUID = -1349777234845125396L;

    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.1", index = true)
    private Long devIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.2")
    private String platSN;// 光机平台序列号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.3")
    private Integer frxNum;// 下行接收光模块数量
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.4")
    private Integer optNodeTemp;// 光机平台温度
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.5", writable = true, type = "Integer32")
    private Integer restorFactory;// 恢复出厂设置 （设置1为恢复出厂设置）

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.6", writable = true, type = "Integer32")
    private Integer fwdEQ0;// 正向混合信号主路均衡
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.7", writable = true, type = "Integer32")
    private Integer fwdATT0;// 正向混合信号主路衰减
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.8", writable = true, type = "Integer32")
    private Integer revEQ;// 反向混合信号主路均衡
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.9", writable = true, type = "Integer32")
    private Integer rtxState;// 反向光发激光器工作状态 1:OFF 2：ON

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.10")
    private Integer rtxLaserPower;// 反向光发输出光功率

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.11", writable = true, type = "Integer32")
    private Integer revATTUS;// DOCSIS US信号衰减
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.12", writable = true, type = "Integer32")
    private Integer revATTRTX;// 反向光路射频衰减

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.13")
    private Integer rtxLaserCurrent;// 反向光发偏置电流

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.14")
    private Integer catvInLevel;// TV IN输入电平
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.15", writable = true, type = "Integer32")
    private Integer catvInputState;// CATV输入选择 0: TV-IN 1:FRX-IN

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.16")
    private Integer ftxOptPower;// 正向光发输出光功率
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.17")
    private Integer ftxLaserCurrent;// 正向光发偏置电流

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
     * @return the devIndex
     */
    public Long getDevIndex() {
        return devIndex;
    }

    /**
     * @param devIndex
     *            the devIndex to set
     */
    public void setDevIndex(Long devIndex) {
        this.devIndex = devIndex;
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
     * @param optNodeTemp
     *            the optNodeTemp to set
     */
    public void setOptNodeTemp(Integer optNodeTemp) {
        this.optNodeTemp = optNodeTemp;
    }

    /**
     * @return the restorFactory
     */
    public Integer getRestorFactory() {
        return restorFactory;
    }

    /**
     * @param restorFactory
     *            the restorFactory to set
     */
    public void setRestorFactory(Integer restorFactory) {
        this.restorFactory = restorFactory;
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

}
