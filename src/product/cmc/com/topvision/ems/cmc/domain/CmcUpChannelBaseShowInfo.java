/***********************************************************************
 * $Id: CmcUpChannelBaseShowInfo.java,v1.0 2011-11-3 下午03:37:35 $
 * 
 * @author: wanglichao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.cmc.util.StringUtils;

/**
 * @author wanglichao
 * @created @2011-11-3-下午03:37:35
 * 
 */
public class CmcUpChannelBaseShowInfo implements Serializable {
    private static final long serialVersionUID = -7424116553476844059L;
    private Long entityId;
    private Long cmcId;
    private Long cmcPortId;
    private Long channelIndex;
    private Integer channelId; // 上行频道Id
    private Long channelFrequency; // 上行频道频率
    private Long channelWidth; // 上行频道带宽
    private Long docsIfSigQSignalNoise; // 信噪比----信号质量表中参数
    private Long channelModulationProfile; // 上行调制配置文件
    private Double ifSpeed;// 速度--ifTable
    private Long ifMtu;// --ifTable
    private Integer ifAdminStatus;// up ( 1 ) , down ( 2 ) , testing ( 3 )--ifTable
    private Integer ifOperStatus;// up ( 1 ) , down ( 2 ) , testing ( 3 ) ,
    // unknown ( 4 ) , dormant ( 5 ) , notPresent ( 6 )--ifTable
    private Long channelSlotSize; // 上行minislot大小
    private Long channelTxTimingOffset; // 上行Tx偏移时间
    private Integer channelRangingBackoffStart; // 上行测距BackoffStart
    private Integer channelRangingBackoffEnd; // 上行测距BackoffEnd
    private Integer channelTxBackoffStart;
    private Integer channelTxBackoffEnd;
    private Integer channelScdmaActiveCodes; // ActiveCodes数目
    private Integer channelScdmaCodesPerSlot; // 每个minislotcode数
    private Integer channelScdmaFrameSize; // 上行S-CDMA帧size
    private Integer channelScdmaHoppingSeed; // 上行hop种子
    private Integer channelType; // 上行频道类型
    private Long channelCloneFrom; // 上行频道克隆
    private Integer channelUpdate; // 上行频道更新
    private Integer channelStatus; // 上行频道状态
    private Integer channelPreEqEnable; // 上行预均衡开关
    private Integer docsIf3SignalPower;// 上行接收电平
    private Integer channelExtMode;// 上行信道模式
    private String ifDescr;

    // for unit
    private String ifSpeedForunit;
    private String docsIfUpChannelFrequencyForunit; // 上行频道频率
    private String docsIfUpChannelWidthForunit; // 上行频道带宽
    private String docsIfSigQSignalNoiseForunit; // 信噪比----信号质量表中参数
    private String docsIfUpChannelSlotSizeForunit; // 上行minislot大小
    private String docsIfUpChannelTxTimingOffsetForunit; // 上行Tx偏移时间
    private String docsIfUpChannelScdmaCodesPerSlotForunit; // 每个minislotcode数
    private String docsIfUpChannelScdmaFrameSizeForunit; // 上行S-CDMA帧size
    private String docsIf3SignalPowerForunit;// 上行接收电平

    private String ifAdminStatusName;
    private String ifOperStatusName;
    private String docsIfUpChannelTypeName;
    private String docsIfUpChannelUpdateName;
    private String docsIfUpChannelStatusName;
    private String docsIfUpChannelPreEqEnableName;
    private String docsIfUpChannelModulationProfileName;
    private String channelExtModeName;
    private Integer cmcChannelTotalCmNum;
    private Integer cmcChannelOnlineCmNum;
    private String cmcPortName;
    private String channelTypeString = "US";
    public static final String[] ADMINTYPES = { "", "up", "down", "testing" };
    public static final String[] OPERTYPES = { "", "up", "down", "testing", "unknown", "dormant", "notPresent",
            "lowerLayerDown" };
    public static final String[] CHANNELTYPETYPES = { "unknown", "TDMA", "ATDMA", "SCDMA", "tdmaAndAtdma" };
    public static final String[] UPDATEYPES = { "", "true", "false" };
    public static final String[] STATUSTYPES = { "", "ACTIVE", "NOTINSERVICE", "NOTREADY", "CREATEANDGO",
            "CREATEANDWAIT", "DESTROY" };
    public static final String[] ENABLETYPES = { "", ResourcesUtil.getString("COMMON.on"),
            ResourcesUtil.getString("COMMON.close") };
    public static final String[] MODULATIONPROFILETYPES = { "", "SCDMA-QPSK-Fair", "SCDMA-QAM16-Fair",
            "SCDMA-QAM64-Fair", "SCDMA-QAM256-Fair", "SCDMA-QPSK-Good", "SCDMA-QAM16-Good", "SCDMA-QAM64-Good",
            "SCDMA-QAM256-Good", "SCDMA-QAM64-Best", "SCDMA-QAM256-Best", "ATDMA-QPSK", "ATDMA-QAM16", "ATDMA-QAM64",
            "ATDMA-QAM256", "SCDMA-QAM64-LowLatency", "SCDMA-QAM256-LowLatency", "SCDMA-QAM32-Good" };
    public static final String[] EXTTYPENAME = { "", "", "V2", "V3", "Other" };

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
     * @return the docsIfUpChannelId
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * @param channelId
     *            the docsIfUpChannelId to set
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * @return the docsIfUpChannelFrequency
     */
    public Long getChannelFrequency() {
        return channelFrequency;
    }

    /**
     * @param channelFrequency
     *            the docsIfUpChannelFrequency to set
     */
    public void setChannelFrequency(Long channelFrequency) {
        this.channelFrequency = channelFrequency;
    }

    /**
     * @return the docsIfUpChannelWidth
     */
    public Long getChannelWidth() {
        return channelWidth;
    }

    /**
     * @param channelWidth
     *            the docsIfUpChannelWidth to set
     */
    public void setChannelWidth(Long channelWidth) {
        this.channelWidth = channelWidth;
    }

    /**
     * @return the docsIfSigQSignalNoise
     */
    public Long getDocsIfSigQSignalNoise() {
        return docsIfSigQSignalNoise;
    }

    /**
     * @param docsIfSigQSignalNoise
     *            the docsIfSigQSignalNoise to set
     */
    public void setDocsIfSigQSignalNoise(Long docsIfSigQSignalNoise) {
        this.docsIfSigQSignalNoise = docsIfSigQSignalNoise;
    }

    /**
     * @return the docsIfUpChannelModulationProfile
     */
    public Long getChannelModulationProfile() {
        return channelModulationProfile;
    }

    /**
     * @param channelModulationProfile
     *            the docsIfUpChannelModulationProfile to set
     */
    public void setChannelModulationProfile(Long channelModulationProfile) {
        this.channelModulationProfile = channelModulationProfile;
        if (this.channelModulationProfile != null && channelModulationProfile < MODULATIONPROFILETYPES.length) {
            this.docsIfUpChannelModulationProfileName = MODULATIONPROFILETYPES[channelModulationProfile.intValue()];
        } else {
            this.docsIfUpChannelModulationProfileName = "Other";
        }
    }

    /**
     * @return the ifSpeed
     */
    public Double getIfSpeed() {
        return ifSpeed;
    }

    /**
     * @param ifSpeed
     *            the ifSpeed to set
     */
    public void setIfSpeed(Double ifSpeed) {
        this.ifSpeed = ifSpeed;
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
        // modify by loyal 添加this.ifAdminStatus < 4,防止数组越界
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
        // modify by loyal 添加this.ifOperStatus < 7,防止数组越界
        if (this.ifOperStatus != null && this.ifOperStatus < 7) {
            this.ifOperStatusName = OPERTYPES[ifOperStatus];
        }
    }

    /**
     * @return the docsIfUpChannelSlotSize
     */
    public Long getChannelSlotSize() {
        return channelSlotSize;
    }

    /**
     * @param channelSlotSize
     *            the docsIfUpChannelSlotSize to set
     */
    public void setChannelSlotSize(Long channelSlotSize) {
        this.channelSlotSize = channelSlotSize;
    }

    /**
     * @return the docsIfUpChannelTxTimingOffset
     */
    public Long getChannelTxTimingOffset() {
        return channelTxTimingOffset;
    }

    /**
     * @param channelTxTimingOffset
     *            the docsIfUpChannelTxTimingOffset to set
     */
    public void setChannelTxTimingOffset(Long channelTxTimingOffset) {
        this.channelTxTimingOffset = channelTxTimingOffset;
    }

    /**
     * @return the docsIfUpChannelRangingBackoffStart
     */
    public Integer getChannelRangingBackoffStart() {
        return channelRangingBackoffStart;
    }

    /**
     * @param channelRangingBackoffStart
     *            the docsIfUpChannelRangingBackoffStart to set
     */
    public void setChannelRangingBackoffStart(Integer channelRangingBackoffStart) {
        this.channelRangingBackoffStart = channelRangingBackoffStart;
    }

    /**
     * @return the docsIfUpChannelRangingBackoffEnd
     */
    public Integer getChannelRangingBackoffEnd() {
        return channelRangingBackoffEnd;
    }

    /**
     * @param channelRangingBackoffEnd
     *            the docsIfUpChannelRangingBackoffEnd to set
     */
    public void setChannelRangingBackoffEnd(Integer channelRangingBackoffEnd) {
        this.channelRangingBackoffEnd = channelRangingBackoffEnd;
    }

    /**
     * @return the docsIfUpChannelTxBackoffStart
     */
    public Integer getChannelTxBackoffStart() {
        return channelTxBackoffStart;
    }

    /**
     * @param channelTxBackoffStart
     *            the docsIfUpChannelTxBackoffStart to set
     */
    public void setChannelTxBackoffStart(Integer channelTxBackoffStart) {
        this.channelTxBackoffStart = channelTxBackoffStart;
    }

    /**
     * @return the docsIfUpChannelTxBackoffEnd
     */
    public Integer getChannelTxBackoffEnd() {
        return channelTxBackoffEnd;
    }

    /**
     * @param channelTxBackoffEnd
     *            the docsIfUpChannelTxBackoffEnd to set
     */
    public void setChannelTxBackoffEnd(Integer channelTxBackoffEnd) {
        this.channelTxBackoffEnd = channelTxBackoffEnd;
    }

    /**
     * @return the docsIfUpChannelScdmaActiveCodes
     */
    public Integer getChannelScdmaActiveCodes() {
        return channelScdmaActiveCodes;
    }

    /**
     * @param channelScdmaActiveCodes
     *            the docsIfUpChannelScdmaActiveCodes to set
     */
    public void setChannelScdmaActiveCodes(Integer channelScdmaActiveCodes) {
        this.channelScdmaActiveCodes = channelScdmaActiveCodes;
    }

    /**
     * @return the docsIfUpChannelScdmaCodesPerSlot
     */
    public Integer getChannelScdmaCodesPerSlot() {
        return channelScdmaCodesPerSlot;
    }

    /**
     * @param channelScdmaCodesPerSlot
     *            the docsIfUpChannelScdmaCodesPerSlot to set
     */
    public void setChannelScdmaCodesPerSlot(Integer channelScdmaCodesPerSlot) {
        this.channelScdmaCodesPerSlot = channelScdmaCodesPerSlot;
    }

    /**
     * @return the docsIfUpChannelScdmaFrameSize
     */
    public Integer getChannelScdmaFrameSize() {
        return channelScdmaFrameSize;
    }

    /**
     * @param channelScdmaFrameSize
     *            the docsIfUpChannelScdmaFrameSize to set
     */
    public void setChannelScdmaFrameSize(Integer channelScdmaFrameSize) {
        this.channelScdmaFrameSize = channelScdmaFrameSize;
    }

    /**
     * @return the docsIfUpChannelScdmaHoppingSeed
     */
    public Integer getChannelScdmaHoppingSeed() {
        return channelScdmaHoppingSeed;
    }

    /**
     * @param channelScdmaHoppingSeed
     *            the docsIfUpChannelScdmaHoppingSeed to set
     */
    public void setChannelScdmaHoppingSeed(Integer channelScdmaHoppingSeed) {
        this.channelScdmaHoppingSeed = channelScdmaHoppingSeed;
    }

    /**
     * @return the docsIfUpChannelType
     */
    public Integer getChannelType() {
        return channelType;
    }

    /**
     * @param channelType
     *            the docsIfUpChannelType to set
     */
    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
        if (this.channelType != null) {
            this.docsIfUpChannelTypeName = CHANNELTYPETYPES[channelType];
        }
    }

    /**
     * @return the docsIfUpChannelCloneFrom
     */
    public Long getChannelCloneFrom() {
        return channelCloneFrom;
    }

    /**
     * @param channelCloneFrom
     *            the docsIfUpChannelCloneFrom to set
     */
    public void setChannelCloneFrom(Long channelCloneFrom) {
        this.channelCloneFrom = channelCloneFrom;
    }

    /**
     * @return the docsIfUpChannelUpdate
     */
    public Integer getChannelUpdate() {
        return channelUpdate;
    }

    /**
     * @param channelUpdate
     *            the docsIfUpChannelUpdate to set
     */
    public void setChannelUpdate(Integer channelUpdate) {
        this.channelUpdate = channelUpdate;
        if (this.channelUpdate != null) {
            this.docsIfUpChannelUpdateName = UPDATEYPES[channelUpdate];
        }
    }

    /**
     * @return the docsIfUpChannelStatus
     */
    public Integer getChannelStatus() {
        return channelStatus;
    }

    /**
     * @param channelStatus
     *            the docsIfUpChannelStatus to set
     */
    public void setChannelStatus(Integer channelStatus) {
        this.channelStatus = channelStatus;
        if (this.channelStatus != null) {
            this.docsIfUpChannelStatusName = STATUSTYPES[channelStatus];
        }
    }

    /**
     * @return the docsIfUpChannelPreEqEnable
     */
    public Integer getChannelPreEqEnable() {
        return channelPreEqEnable;
    }

    /**
     * @param channelPreEqEnable
     *            the docsIfUpChannelPreEqEnable to set
     */
    public void setChannelPreEqEnable(Integer channelPreEqEnable) {
        this.channelPreEqEnable = channelPreEqEnable;
        if (this.channelPreEqEnable != null) {
            this.docsIfUpChannelPreEqEnableName = ENABLETYPES[channelPreEqEnable];
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
     * @return the docsIfUpChannelTypeName
     */
    public String getDocsIfUpChannelTypeName() {
        return docsIfUpChannelTypeName;
    }

    /**
     * @param docsIfUpChannelTypeName
     *            the docsIfUpChannelTypeName to set
     */
    public void setDocsIfUpChannelTypeName(String docsIfUpChannelTypeName) {
        this.docsIfUpChannelTypeName = docsIfUpChannelTypeName;
    }

    /**
     * @return the docsIfUpChannelUpdateName
     */
    public String getDocsIfUpChannelUpdateName() {
        return docsIfUpChannelUpdateName;
    }

    /**
     * @param docsIfUpChannelUpdateName
     *            the docsIfUpChannelUpdateName to set
     */
    public void setDocsIfUpChannelUpdateName(String docsIfUpChannelUpdateName) {
        this.docsIfUpChannelUpdateName = docsIfUpChannelUpdateName;
    }

    /**
     * @return the docsIfUpChannelStatusName
     */
    public String getDocsIfUpChannelStatusName() {
        return docsIfUpChannelStatusName;
    }

    /**
     * @param docsIfUpChannelStatusName
     *            the docsIfUpChannelStatusName to set
     */
    public void setDocsIfUpChannelStatusName(String docsIfUpChannelStatusName) {
        this.docsIfUpChannelStatusName = docsIfUpChannelStatusName;
    }

    /**
     * @return the docsIfUpChannelPreEqEnableName
     */
    public String getDocsIfUpChannelPreEqEnableName() {
        return docsIfUpChannelPreEqEnableName;
    }

    /**
     * @param docsIfUpChannelPreEqEnableName
     *            the docsIfUpChannelPreEqEnableName to set
     */
    public void setDocsIfUpChannelPreEqEnableName(String docsIfUpChannelPreEqEnableName) {
        this.docsIfUpChannelPreEqEnableName = docsIfUpChannelPreEqEnableName;
    }

    public String getDocsIfUpChannelModulationProfileName() {
        return docsIfUpChannelModulationProfileName;
    }

    public void setDocsIfUpChannelModulationProfileName(String docsIfUpChannelModulationProfileName) {
        this.docsIfUpChannelModulationProfileName = docsIfUpChannelModulationProfileName;
    }

    /**
     * @return the docsIfUpChannelFrequencyForunit
     */
    public String getDocsIfUpChannelFrequencyForunit() {
        if (this.getChannelFrequency() != null) {
            double FrequencyForunit = this.getChannelFrequency();
            String freInfo = FrequencyForunit / 1000000 + "";
            if (freInfo.matches("^(([0-9]+)[.]+([0-9]{0,6}))$")) {
                docsIfUpChannelFrequencyForunit = freInfo + " MHz";
            } else {
                DecimalFormat df = new DecimalFormat("0.000000");
                double info = Double.parseDouble(df.format(FrequencyForunit / 1000000));
                docsIfUpChannelFrequencyForunit = info + " MHz";
            }
        }
        return docsIfUpChannelFrequencyForunit;
    }

    /**
     * @param docsIfUpChannelFrequencyForunit
     *            the docsIfUpChannelFrequencyForunit to set
     */
    public void setDocsIfUpChannelFrequencyForunit(String docsIfUpChannelFrequencyForunit) {
        if (StringUtils.isEmpty(docsIfUpChannelFrequencyForunit)) {
            this.channelFrequency = 0L;
        } else if (docsIfUpChannelFrequencyForunit.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
            BigDecimal freqB = new BigDecimal(docsIfUpChannelFrequencyForunit.trim());
            this.channelFrequency = freqB.multiply(new BigDecimal("1000000")).longValue();
        }
        this.docsIfUpChannelFrequencyForunit = docsIfUpChannelFrequencyForunit;
    }

    /**
     * @return the docsIfUpChannelWidthForunit
     */
    public String getDocsIfUpChannelWidthForunit() {
        if (this.getChannelWidth() != null) {
            DecimalFormat df = new DecimalFormat("0.0");
            double ChannelWidth = channelWidth;
            docsIfUpChannelWidthForunit = df.format(ChannelWidth / 1000000) + " MHz";
        }
        return docsIfUpChannelWidthForunit;
    }

    /**
     * @param docsIfUpChannelWidthForunit
     *            the docsIfUpChannelWidthForunit to set
     */
    public void setDocsIfUpChannelWidthForunit(String docsIfUpChannelWidthForunit) {
        this.docsIfUpChannelWidthForunit = docsIfUpChannelWidthForunit;
    }

    /**
     * @return the docsIfSigQSignalNoiseForunit
     */
    public String getDocsIfSigQSignalNoiseForunit() {
        String str = " dB";
        Float l = 0f;
        if (this.getDocsIfSigQSignalNoise() != null) {
            l = (float) this.getDocsIfSigQSignalNoise() / 10;
            docsIfSigQSignalNoiseForunit = l.toString();
        }
        return docsIfSigQSignalNoiseForunit + str;
    }

    /**
     * @param docsIfSigQSignalNoiseForunit
     *            the docsIfSigQSignalNoiseForunit to set
     */
    public void setDocsIfSigQSignalNoiseForunit(String docsIfSigQSignalNoiseForunit) {
        this.docsIfSigQSignalNoiseForunit = docsIfSigQSignalNoiseForunit;
    }

    /**
     * @return the docsIfUpChannelSlotSizeForunit
     */
    public String getDocsIfUpChannelSlotSizeForunit() {
        if (this.getChannelSlotSize() != null) {
            docsIfUpChannelSlotSizeForunit = channelSlotSize.toString() + " ticks";
        }
        return docsIfUpChannelSlotSizeForunit;
    }

    /**
     * @param docsIfUpChannelSlotSizeForunit
     *            the docsIfUpChannelSlotSizeForunit to set
     */
    public void setDocsIfUpChannelSlotSizeForunit(String docsIfUpChannelSlotSizeForunit) {
        this.docsIfUpChannelSlotSizeForunit = docsIfUpChannelSlotSizeForunit;
    }

    /**
     * @return the docsIfUpChannelTxTimingOffsetForunit
     */
    public String getDocsIfUpChannelTxTimingOffsetForunit() {
        if (this.getChannelTxTimingOffset() != null) {
            docsIfUpChannelTxTimingOffsetForunit = channelTxTimingOffset.toString() + " ticks/64";
        }
        return docsIfUpChannelTxTimingOffsetForunit;
    }

    /**
     * @param docsIfUpChannelTxTimingOffsetForunit
     *            the docsIfUpChannelTxTimingOffsetForunit to set
     */
    public void setDocsIfUpChannelTxTimingOffsetForunit(String docsIfUpChannelTxTimingOffsetForunit) {
        this.docsIfUpChannelTxTimingOffsetForunit = docsIfUpChannelTxTimingOffsetForunit;
    }

    /**
     * @return the docsIfUpChannelScdmaCodesPerSlotForunit
     */
    public String getDocsIfUpChannelScdmaCodesPerSlotForunit() {
        if (this.getChannelScdmaCodesPerSlot() != null) {
            docsIfUpChannelScdmaCodesPerSlotForunit = channelScdmaCodesPerSlot.toString() + " codesperMinislots";
        }
        return docsIfUpChannelScdmaCodesPerSlotForunit;
    }

    /**
     * @param docsIfUpChannelScdmaCodesPerSlotForunit
     *            the docsIfUpChannelScdmaCodesPerSlotForunit to set
     */
    public void setDocsIfUpChannelScdmaCodesPerSlotForunit(String docsIfUpChannelScdmaCodesPerSlotForunit) {
        this.docsIfUpChannelScdmaCodesPerSlotForunit = docsIfUpChannelScdmaCodesPerSlotForunit;
    }

    /**
     * @return the docsIfUpChannelScdmaFrameSizeForunit
     */
    public String getDocsIfUpChannelScdmaFrameSizeForunit() {
        if (this.getChannelScdmaFrameSize() != null) {
            docsIfUpChannelScdmaFrameSizeForunit = channelScdmaFrameSize.toString() + " spreadIntervals";
        }
        return docsIfUpChannelScdmaFrameSizeForunit;
    }

    /**
     * @param docsIfUpChannelScdmaFrameSizeForunit
     *            the docsIfUpChannelScdmaFrameSizeForunit to set
     */
    public void setDocsIfUpChannelScdmaFrameSizeForunit(String docsIfUpChannelScdmaFrameSizeForunit) {
        this.docsIfUpChannelScdmaFrameSizeForunit = docsIfUpChannelScdmaFrameSizeForunit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

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

    public Integer getDocsIf3SignalPower() {
        return docsIf3SignalPower;
    }

    public void setDocsIf3SignalPower(Integer docsIf3SignalPower) {
        this.docsIf3SignalPower = docsIf3SignalPower;
    }

    public String getDocsIf3SignalPowerForunit() {
        if (docsIf3SignalPower != null) {
            float SignalPower = this.docsIf3SignalPower;
            docsIf3SignalPowerForunit = SignalPower / 10 + " dBmV";
        }
        return docsIf3SignalPowerForunit;
    }

    public void setDocsIf3SignalPowerForunit(String docsIf3SignalPowerForunit) {
        if (StringUtils.isEmpty(docsIf3SignalPowerForunit)) {
            this.docsIf3SignalPower = 0;
        } else if (docsIf3SignalPowerForunit.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
            this.docsIf3SignalPower = (int) (Double.valueOf(docsIf3SignalPowerForunit.trim()) * 10);
        } else {
            this.docsIf3SignalPowerForunit = docsIf3SignalPowerForunit;
        }
        this.docsIf3SignalPowerForunit = docsIf3SignalPowerForunit;
    }

    public String getIfDescr() {
        return ifDescr;
    }

    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }

    /**
     * @return the channelExtMode
     */
    public Integer getChannelExtMode() {
        return channelExtMode;
    }

    /**
     * @param channelExtMode
     *            the channelExtMode to set
     */
    public void setChannelExtMode(Integer channelExtMode) {
        this.channelExtMode = channelExtMode;
    }

    /**
     * @return the channelExtModeName
     */
    public String getChannelExtModeName() {
        if (channelExtMode != null && channelExtMode < EXTTYPENAME.length) {
            channelExtModeName = EXTTYPENAME[channelExtMode];
        }
        return channelExtModeName;
    }

    /**
     * @param channelExtModeName
     *            the channelExtModeName to set
     */
    public void setchannelExtModeName(String channelExtModeName) {
        this.channelExtModeName = channelExtModeName;
    }

    public String getCmcPortName() {
        Long index = getChannelIndex();
        String Slot_Pon_cmcId = "";
        if (index != null) {
            Slot_Pon_cmcId = CmcIndexUtils.getSlotNo(index) + Symbol.SLASH + CmcIndexUtils.getPonNo(index)
                    + Symbol.SLASH + CmcIndexUtils.getCmcId(index);
        }
        cmcPortName = channelTypeString + " " + Slot_Pon_cmcId + Symbol.SLASH + getChannelId();
        return cmcPortName;
    }

    public void setCmcPortName(String cmcPortName) {
        this.cmcPortName = cmcPortName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcUpChannelBaseShowInfo [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", channelId=");
        builder.append(channelId);
        builder.append(", channelFrequency=");
        builder.append(channelFrequency);
        builder.append(", channelWidth=");
        builder.append(channelWidth);
        builder.append(", docsIfSigQSignalNoise=");
        builder.append(docsIfSigQSignalNoise);
        builder.append(", channelModulationProfile=");
        builder.append(channelModulationProfile);
        builder.append(", ifSpeed=");
        builder.append(ifSpeed);
        builder.append(", ifMtu=");
        builder.append(ifMtu);
        builder.append(", ifAdminStatus=");
        builder.append(ifAdminStatus);
        builder.append(", ifOperStatus=");
        builder.append(ifOperStatus);
        builder.append(", channelSlotSize=");
        builder.append(channelSlotSize);
        builder.append(", channelTxTimingOffset=");
        builder.append(channelTxTimingOffset);
        builder.append(", channelRangingBackoffStart=");
        builder.append(channelRangingBackoffStart);
        builder.append(", channelRangingBackoffEnd=");
        builder.append(channelRangingBackoffEnd);
        builder.append(", channelTxBackoffStart=");
        builder.append(channelTxBackoffStart);
        builder.append(", channelTxBackoffEnd=");
        builder.append(channelTxBackoffEnd);
        builder.append(", channelScdmaActiveCodes=");
        builder.append(channelScdmaActiveCodes);
        builder.append(", channelScdmaCodesPerSlot=");
        builder.append(channelScdmaCodesPerSlot);
        builder.append(", channelScdmaFrameSize=");
        builder.append(channelScdmaFrameSize);
        builder.append(", channelScdmaHoppingSeed=");
        builder.append(channelScdmaHoppingSeed);
        builder.append(", channelType=");
        builder.append(channelType);
        builder.append(", channelCloneFrom=");
        builder.append(channelCloneFrom);
        builder.append(", channelUpdate=");
        builder.append(channelUpdate);
        builder.append(", channelStatus=");
        builder.append(channelStatus);
        builder.append(", channelPreEqEnable=");
        builder.append(channelPreEqEnable);
        builder.append(", docsIf3SignalPower=");
        builder.append(docsIf3SignalPower);
        builder.append(", channelExtMode=");
        builder.append(channelExtMode);
        builder.append(", ifDescr=");
        builder.append(ifDescr);
        builder.append(", ifSpeedForunit=");
        builder.append(ifSpeedForunit);
        builder.append(", docsIfUpChannelFrequencyForunit=");
        builder.append(docsIfUpChannelFrequencyForunit);
        builder.append(", docsIfUpChannelWidthForunit=");
        builder.append(docsIfUpChannelWidthForunit);
        builder.append(", docsIfSigQSignalNoiseForunit=");
        builder.append(docsIfSigQSignalNoiseForunit);
        builder.append(", docsIfUpChannelSlotSizeForunit=");
        builder.append(docsIfUpChannelSlotSizeForunit);
        builder.append(", docsIfUpChannelTxTimingOffsetForunit=");
        builder.append(docsIfUpChannelTxTimingOffsetForunit);
        builder.append(", docsIfUpChannelScdmaCodesPerSlotForunit=");
        builder.append(docsIfUpChannelScdmaCodesPerSlotForunit);
        builder.append(", docsIfUpChannelScdmaFrameSizeForunit=");
        builder.append(docsIfUpChannelScdmaFrameSizeForunit);
        builder.append(", docsIf3SignalPowerForunit=");
        builder.append(docsIf3SignalPowerForunit);
        builder.append(", ifAdminStatusName=");
        builder.append(ifAdminStatusName);
        builder.append(", ifOperStatusName=");
        builder.append(ifOperStatusName);
        builder.append(", docsIfUpChannelTypeName=");
        builder.append(docsIfUpChannelTypeName);
        builder.append(", docsIfUpChannelUpdateName=");
        builder.append(docsIfUpChannelUpdateName);
        builder.append(", docsIfUpChannelStatusName=");
        builder.append(docsIfUpChannelStatusName);
        builder.append(", docsIfUpChannelPreEqEnableName=");
        builder.append(docsIfUpChannelPreEqEnableName);
        builder.append(", docsIfUpChannelModulationProfileName=");
        builder.append(docsIfUpChannelModulationProfileName);
        builder.append(", channelExtModeName=");
        builder.append(channelExtModeName);
        builder.append(", cmcChannelTotalCmNum=");
        builder.append(cmcChannelTotalCmNum);
        builder.append(", cmcChannelOnlineCmNum=");
        builder.append(cmcChannelOnlineCmNum);
        builder.append("]");
        return builder.toString();
    }

}
