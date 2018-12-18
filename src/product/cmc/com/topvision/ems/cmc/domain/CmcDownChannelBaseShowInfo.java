/***********************************************************************
 * $Id: CmcDownChannelBaseShowInfo.java,v1.0 2011-11-3 下午03:37:57 $
 * 
 * @author: wanglichao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.topvision.ems.cmc.downchannel.domain.TxPowerLimit;
import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.cmc.util.StringUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author wanglichao
 * @created @2011-11-3-下午03:37:57
 * 
 */
@Alias("cmcDownChannelBaseShowInfo")
public class CmcDownChannelBaseShowInfo implements Serializable, AliasesSuperType  {
    private static final long serialVersionUID = 3004477793285736406L;
    private Long entityId;
    private Long cmcId;
    private Long cmcPortId;
    private Long channelIndex;
    private Integer docsIfDownChannelId; // 下行通道ID
    private String ifName;// 下行信道名    
    private Long docsIfDownChannelFrequency; // 下行通道频率HZ
    private Integer docsIfDownChannelWidth; // 下行通道带宽HZ
    private Long ifSpeed;
    private String ifDescr;
    private Long ifMtu;
    private Integer ifAdminStatus;
    private Integer ifOperStatus;
    private Integer docsIfDownChannelModulation; // 下行通道调制信息
    private Integer docsIfDownChannelInterleave; // 下行通道交织
    private Long docsIfDownChannelPower; // 下行通道电平dBmV
    private Integer docsIfDownChannelAnnex; // 下行通道标准(eo,usa)
    private Integer docsIfDownChannelStorageType; // 下行通道存储类型
    private Integer channelType;//docsis还是IPQAM
    
    private String ifSpeedForunit;
    private String docsIfDownChannelFrequencyForunit;
    private String docsIfDownChannelWidthForunit;
    private String docsIfDownChannelPowerForunit;
    private String ifAdminStatusName;
    private String ifOperStatusName;
    private String docsIfDownChannelModulationName;
    private String docsIfDownChannelInterleaveName;
    private String docsIfDownChannelAnnexName;
    private Integer cmcChannelTotalCmNum;
    private Integer cmcChannelOnlineCmNum;
    private String cmcPortName;
    private String channelTypeString = "DS";
    public static final String[] ADMINTYPES = { "", "up", "down", "testing" };
    public static final String[] OPERTYPES = { "", "up", "down", "testing", "unknown", "dormant", "notPresent",
            "lowerLayerDown" };
    /* public static final String[] MODULATIONTYPES = { "", "unknown", "other", "QAM64", "QAM256" }; */
    public static final String[] MODULATIONTYPES = { "", "unknown", "QAM1024", "QAM64", "QAM256" };
    public static final String[] INTERLEAVETYPES = { "", "unknown", "other", "(8, 16)", "(16, 8)", "(32, 4)",
            "(64, 2)", "(128, 1)", "(12, 17)" };
    public static final String[] ANNEXTYPES = { "", "unknown", "other", "annexA", "annexB", "annexC" };

    //@flackyang 用于下行信道配置使用
    private Double downChannelPower;

    private TxPowerLimit txPowerLimit = null;

    public Double getDownChannelPower() {
        if (this.getDocsIfDownChannelPower() != null) {
            double txPowerForUnit = this.getDocsIfDownChannelPower();
            downChannelPower = UnitConfigConstant.parsePowerValue(txPowerForUnit / 10);
        }
        return downChannelPower;
    }

    public void setDownChannelPower(Double downChannelPower) {
        if (downChannelPower != null) {
            this.docsIfDownChannelPower = (long) (UnitConfigConstant.transPowerToDBmV(downChannelPower) * 10);
        }
        this.downChannelPower = downChannelPower;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

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
     * @return the cmcPortId
     */
    public Long getCmcPortId() {
        return cmcPortId;
    }

    /**
     * @param cmcPortId
     *            the cmcPortId to set
     */
    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }

    /**
     * @return the channelType
     */
    public Integer getChannelType() {
        return channelType;
    }

    /**
     * @param channelType the channelType to set
     */
    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    /**
     * @return the docsIfDownChannelId
     */
    public Integer getDocsIfDownChannelId() {
        return docsIfDownChannelId;
    }

    /**
     * @param docsIfDownChannelId
     *            the docsIfDownChannelId to set
     */
    public void setDocsIfDownChannelId(Integer docsIfDownChannelId) {
        this.docsIfDownChannelId = docsIfDownChannelId;
    }

    /**
     * @return the ifName
     */
    public String getIfName() {
        return ifName;
    }

    /**
     * @param ifName
     *            the ifName to set
     */
    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

    /**
     * @return the docsIfDownChannelFrequency
     */
    public Long getDocsIfDownChannelFrequency() {
        return docsIfDownChannelFrequency;
    }

    /**
     * @param docsIfDownChannelFrequency
     *            the docsIfDownChannelFrequency to set
     */
    public void setDocsIfDownChannelFrequency(Long docsIfDownChannelFrequency) {
        this.docsIfDownChannelFrequency = docsIfDownChannelFrequency;
        if (this.docsIfDownChannelFrequency != null) {
            double downChannelFrequency = this.docsIfDownChannelFrequency;
            String freInfo = downChannelFrequency / 1000000 + "";
            if (freInfo.matches("^(([0-9]+)[.]+([0-9]{0,6}))$")) {
                freInfo = freInfo + " MHz";
            } else {
                DecimalFormat df = new DecimalFormat("0.000000");
                double info = Double.parseDouble(df.format(downChannelFrequency / 1000000));
                freInfo = info + " MHz";
            }
            this.setDocsIfDownChannelFrequencyForunit(freInfo);
        }
    }

    /**
     * @return the docsIfDownChannelWidth
     */
    public Integer getDocsIfDownChannelWidth() {
        return docsIfDownChannelWidth;
    }

    /**
     * @param docsIfDownChannelWidth
     *            the docsIfDownChannelWidth to set
     */
    public void setDocsIfDownChannelWidth(Integer docsIfDownChannelWidth) {
        this.docsIfDownChannelWidth = docsIfDownChannelWidth;
        if (this.docsIfDownChannelWidth != null) {
            DecimalFormat df = new DecimalFormat("0.0");
            double downChannelWidth = this.docsIfDownChannelWidth;
            this.setDocsIfDownChannelWidthForunit(df.format(downChannelWidth / 1000000) + " MHz");
        }
    }

    /**
     * @return the ifSpeed
     */
    public Long getIfSpeed() {
        return ifSpeed;
    }

    /**
     * @param ifSpeed
     *            the ifSpeed to set
     */
    public void setIfSpeed(Long ifSpeed) {
        this.ifSpeed = ifSpeed;
    }

    /**
     * @return the ifDescr
     */
    public String getIfDescr() {
        return ifDescr;
    }

    /**
     * @param ifDescr
     *            the ifDescr to set
     */
    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }

    /**
     * @return the ifMtu
     */
    public Long getIfMtu() {
        return ifMtu;
    }

    /**
     * @param ifMtu
     *            the ifMtu to set
     */
    public void setIfMtu(Long ifMtu) {
        this.ifMtu = ifMtu;
    }

    /**
     * @return the ifAdminStatus
     */
    public Integer getIfAdminStatus() {
        return ifAdminStatus;
    }

    /**
     * @param ifAdminStatus
     *            the ifAdminStatus to set
     */
    public void setIfAdminStatus(Integer ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
        // modify by loyal 防止数组越界
        if (this.ifAdminStatus != null && this.ifAdminStatus < 4) {
            this.ifAdminStatusName = ADMINTYPES[ifAdminStatus];
        }
    }

    /**
     * @return the ifOperStatus
     */
    public Integer getIfOperStatus() {
        return ifOperStatus;
    }

    /**
     * @param ifOperStatus
     *            the ifOperStatus to set
     */
    public void setIfOperStatus(Integer ifOperStatus) {
        this.ifOperStatus = ifOperStatus;
        // modify by loyal 防止数组越界
        if (this.ifOperStatus != null && this.ifOperStatus < 7) {
            this.ifOperStatusName = OPERTYPES[ifOperStatus];
        }
    }

    /**
     * @return the docsIfDownChannelModulation
     */
    public Integer getDocsIfDownChannelModulation() {
        return docsIfDownChannelModulation;
    }

    /**
     * @param docsIfDownChannelModulation
     *            the docsIfDownChannelModulation to set
     */
    public void setDocsIfDownChannelModulation(Integer docsIfDownChannelModulation) {
        this.docsIfDownChannelModulation = docsIfDownChannelModulation;
        if (docsIfDownChannelModulation != null) {
            this.docsIfDownChannelModulationName = MODULATIONTYPES[docsIfDownChannelModulation];
        }
    }

    /**
     * @return the docsIfDownChannelInterleave
     */
    public Integer getDocsIfDownChannelInterleave() {
        return docsIfDownChannelInterleave;
    }

    /**
     * @param docsIfDownChannelInterleave
     *            the docsIfDownChannelInterleave to set
     */
    public void setDocsIfDownChannelInterleave(Integer docsIfDownChannelInterleave) {
        this.docsIfDownChannelInterleave = docsIfDownChannelInterleave;
        if (docsIfDownChannelInterleave != null) {
            this.docsIfDownChannelInterleaveName = INTERLEAVETYPES[docsIfDownChannelInterleave];
        }
    }

    /**
     * @return the docsIfDownChannelPower
     */
    public Long getDocsIfDownChannelPower() {
        return docsIfDownChannelPower;
    }

    /**
     * @param docsIfDownChannelPower
     *            the docsIfDownChannelPower to set
     */
    public void setDocsIfDownChannelPower(Long docsIfDownChannelPower) {
        if (docsIfDownChannelPower != null) {
            this.docsIfDownChannelPower = docsIfDownChannelPower;
            //this.setDocsIfDownChannelPowerForunit(downChannelPower / 10 + " dBmV");
            double powerValue = UnitConfigConstant.parsePowerValue((double) docsIfDownChannelPower / 10);
            this.docsIfDownChannelPowerForunit = powerValue + " " + UnitConfigConstant.get("elecLevelUnit");
        }
    }

    /**
     * @return the docsIfDownChannelAnnex
     */
    public Integer getDocsIfDownChannelAnnex() {
        return docsIfDownChannelAnnex;
    }

    /**
     * @param docsIfDownChannelAnnex
     *            the docsIfDownChannelAnnex to set
     */
    public void setDocsIfDownChannelAnnex(Integer docsIfDownChannelAnnex) {
        this.docsIfDownChannelAnnex = docsIfDownChannelAnnex;
    }

    /**
     * @return the docsIfDownChannelStorageType
     */
    public Integer getDocsIfDownChannelStorageType() {
        return docsIfDownChannelStorageType;
    }

    /**
     * @param docsIfDownChannelStorageType
     *            the docsIfDownChannelStorageType to set
     */
    public void setDocsIfDownChannelStorageType(Integer docsIfDownChannelStorageType) {
        this.docsIfDownChannelStorageType = docsIfDownChannelStorageType;
    }

    /**
     * @return the ifAdminStatusName
     */
    public String getIfAdminStatusName() {
        return ifAdminStatusName;
    }

    /**
     * @param ifAdminStatusName
     *            the ifAdminStatusName to set
     */
    public void setIfAdminStatusName(String ifAdminStatusName) {
        this.ifAdminStatusName = ifAdminStatusName;
    }

    /**
     * @return the ifOperStatusName
     */
    public String getIfOperStatusName() {
        return ifOperStatusName;
    }

    /**
     * @param ifOperStatusName
     *            the ifOperStatusName to set
     */
    public void setIfOperStatusName(String ifOperStatusName) {
        this.ifOperStatusName = ifOperStatusName;
    }

    /**
     * @return the docsIfDownChannelModulationName
     */
    public String getDocsIfDownChannelModulationName() {
        return docsIfDownChannelModulationName;
    }

    /**
     * @param docsIfDownChannelModulationName
     *            the docsIfDownChannelModulationName to set
     */
    public void setDocsIfDownChannelModulationName(String docsIfDownChannelModulationName) {
        this.docsIfDownChannelModulationName = docsIfDownChannelModulationName;
    }

    /**
     * @return the docsIfDownChannelInterleaveName
     */
    public String getDocsIfDownChannelInterleaveName() {
        return docsIfDownChannelInterleaveName;
    }

    /**
     * @param docsIfDownChannelInterleaveName
     *            the docsIfDownChannelInterleaveName to set
     */
    public void setDocsIfDownChannelInterleaveName(String docsIfDownChannelInterleaveName) {
        this.docsIfDownChannelInterleaveName = docsIfDownChannelInterleaveName;
    }

    /**
     * @return the docsIfDownChannelAnnexName
     */
    public String getDocsIfDownChannelAnnexName() {
        if (docsIfDownChannelAnnex != null) {
            this.docsIfDownChannelAnnexName = ANNEXTYPES[docsIfDownChannelAnnex];
        }
        return docsIfDownChannelAnnexName;
    }

    /**
     * @param docsIfDownChannelAnnexName
     *            the docsIfDownChannelAnnexName to set
     */
    public void setDocsIfDownChannelAnnexName(String docsIfDownChannelAnnexName) {
        this.docsIfDownChannelAnnexName = docsIfDownChannelAnnexName;
    }

    /**
     * @return the ifSpeedForunit
     */
    public String getIfSpeedForunit() {
        if (this.getIfSpeed() != null) {
            DecimalFormat df = new DecimalFormat("0.00");
            double speed = this.ifSpeed;
            this.ifSpeedForunit = df.format(speed / (1024 * 1024)) + " Mbps";
        }
        return ifSpeedForunit;
    }

    /**
     * @param ifSpeedForunit
     *            the ifSpeedForunit to set
     */
    public void setIfSpeedForunit(String ifSpeedForunit) {
        this.ifSpeedForunit = ifSpeedForunit;
    }

    /**
     * @return the docsIfDownChannelFrequencyForunit
     */
    public String getDocsIfDownChannelFrequencyForunit() {
        return docsIfDownChannelFrequencyForunit;
    }

    /**
     * @param docsIfDownChannelFrequencyForunit
     *            the docsIfDownChannelFrequencyForunit to set
     */
    public void setDocsIfDownChannelFrequencyForunit(String docsIfDownChannelFrequencyForunit) {
        if (StringUtils.isEmpty(docsIfDownChannelFrequencyForunit)) {
            this.docsIfDownChannelFrequency = 0L;
        } else if (docsIfDownChannelFrequencyForunit.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
            BigDecimal freqB = new BigDecimal(docsIfDownChannelFrequencyForunit.trim());
            this.docsIfDownChannelFrequency = freqB.multiply(new BigDecimal("1000000")).longValue();
        }
        this.docsIfDownChannelFrequencyForunit = docsIfDownChannelFrequencyForunit;
    }

    /**
     * @return the docsIfDownChannelWidthForunit
     */
    public String getDocsIfDownChannelWidthForunit() {
        return docsIfDownChannelWidthForunit;
    }

    /**
     * @param docsIfDownChannelWidthForunit
     *            the docsIfDownChannelWidthForunit to set
     */
    public void setDocsIfDownChannelWidthForunit(String docsIfDownChannelWidthForunit) {
        this.docsIfDownChannelWidthForunit = docsIfDownChannelWidthForunit;
    }

    /**
     * @return the docsIfDownChannelPowerForunit
     */
    public String getDocsIfDownChannelPowerForunit() {
        return docsIfDownChannelPowerForunit;
    }

    /**
     * @param docsIfDownChannelPowerForunit
     *            the docsIfDownChannelPowerForunit to set
     */
    public void setDocsIfDownChannelPowerForunit(String docsIfDownChannelPowerForunit) {
        if (StringUtils.isEmpty(docsIfDownChannelPowerForunit)) {
            this.docsIfDownChannelPower = 0L;
        } else if (docsIfDownChannelPowerForunit.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
            this.docsIfDownChannelPower = (long) (Double.valueOf(docsIfDownChannelPowerForunit.trim()) * 10);
        } else {
            this.docsIfDownChannelPowerForunit = docsIfDownChannelPowerForunit;
        }
    }

    /**
     * @return the channelIndex
     */
    public Long getChannelIndex() {
        return channelIndex;
    }

    /**
     * @param channelIndex
     *            the channelIndex to set
     */
    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    /**
     * @return the cmcChannelTotalCmNum
     */
    public Integer getCmcChannelTotalCmNum() {
        return cmcChannelTotalCmNum;
    }

    /**
     * @param cmcChannelTotalCmNum
     *            the cmcChannelTotalCmNum to set
     */
    public void setCmcChannelTotalCmNum(Integer cmcChannelTotalCmNum) {
        this.cmcChannelTotalCmNum = cmcChannelTotalCmNum;
    }

    /**
     * @return the cmcChannelOnlineCmNum
     */
    public Integer getCmcChannelOnlineCmNum() {
        return cmcChannelOnlineCmNum;
    }

    /**
     * @param cmcChannelOnlineCmNum
     *            the cmcChannelOnlineCmNum to set
     */
    public void setCmcChannelOnlineCmNum(Integer cmcChannelOnlineCmNum) {
        this.cmcChannelOnlineCmNum = cmcChannelOnlineCmNum;
    }

    public String getCmcPortName() {
    	Long index = getChannelIndex();
    	String Slot_Pon_cmcId = "";
		if(index != null){
			Slot_Pon_cmcId = CmcIndexUtils.getSlotNo(index) + Symbol.SLASH + CmcIndexUtils.getPonNo(index) + Symbol.SLASH
	                + CmcIndexUtils.getCmcId(index);
		}
        cmcPortName = channelTypeString + " " + Slot_Pon_cmcId + Symbol.SLASH + getDocsIfDownChannelId();
        return cmcPortName;
	}

	public void setCmcPortName(String cmcPortName) {
		this.cmcPortName = cmcPortName;
	}

    public TxPowerLimit getTxPowerLimit() {
        return txPowerLimit;
    }

    public void setTxPowerLimit(TxPowerLimit txPowerLimit) {
        this.txPowerLimit = txPowerLimit;
    }

    /*
             * (non-Javadoc)
             *
             * @see java.lang.Object#toString()
             */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcDownChannelBaseShowInfo [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", docsIfDownChannelId=");
        builder.append(docsIfDownChannelId);
        builder.append(", ifName=");
        builder.append(ifName);
        builder.append(", docsIfDownChannelFrequency=");
        builder.append(docsIfDownChannelFrequency);
        builder.append(", docsIfDownChannelWidth=");
        builder.append(docsIfDownChannelWidth);
        builder.append(", ifSpeed=");
        builder.append(ifSpeed);
        builder.append(", ifDescr=");
        builder.append(ifDescr);
        builder.append(", ifMtu=");
        builder.append(ifMtu);
        builder.append(", ifAdminStatus=");
        builder.append(ifAdminStatus);
        builder.append(", ifOperStatus=");
        builder.append(ifOperStatus);
        builder.append(", docsIfDownChannelModulation=");
        builder.append(docsIfDownChannelModulation);
        builder.append(", docsIfDownChannelInterleave=");
        builder.append(docsIfDownChannelInterleave);
        builder.append(", docsIfDownChannelPower=");
        builder.append(docsIfDownChannelPower);
        builder.append(", docsIfDownChannelAnnex=");
        builder.append(docsIfDownChannelAnnex);
        builder.append(", docsIfDownChannelStorageType=");
        builder.append(docsIfDownChannelStorageType);
        builder.append(", ifSpeedForunit=");
        builder.append(ifSpeedForunit);
        builder.append(", docsIfDownChannelFrequencyForunit=");
        builder.append(docsIfDownChannelFrequencyForunit);
        builder.append(", docsIfDownChannelWidthForunit=");
        builder.append(docsIfDownChannelWidthForunit);
        builder.append(", docsIfDownChannelPowerForunit=");
        builder.append(docsIfDownChannelPowerForunit);
        builder.append(", ifAdminStatusName=");
        builder.append(ifAdminStatusName);
        builder.append(", ifOperStatusName=");
        builder.append(ifOperStatusName);
        builder.append(", docsIfDownChannelModulationName=");
        builder.append(docsIfDownChannelModulationName);
        builder.append(", docsIfDownChannelInterleaveName=");
        builder.append(docsIfDownChannelInterleaveName);
        builder.append(", docsIfDownChannelAnnexName=");
        builder.append(docsIfDownChannelAnnexName);
        builder.append(", cmcChannelTotalCmNum=");
        builder.append(cmcChannelTotalCmNum);
        builder.append(", cmcChannelOnlineCmNum=");
        builder.append(cmcChannelOnlineCmNum);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
