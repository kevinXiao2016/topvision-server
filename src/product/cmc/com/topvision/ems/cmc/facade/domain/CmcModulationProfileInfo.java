/***********************************************************************
 * $Id: CmcModulationProfileInfo.java,v1.0 2011-10-26 下午02:46:18 $
 * 
 * @author: xionghao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author xionghao
 * @created @2011-10-26-下午02:46:18
 * 
 */
@Alias("cmcModulationProfileInfo")
public class CmcModulationProfileInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 45021863955420685L;
    private Long cmcModId;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.1", index = true)
    private Long modIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.2", index = true)
    private Integer modIntervalUsageCode;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.3", writable = true, type = "Integer32")
    private Integer modControl;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.4", writable = true, type = "Integer32")
    private Integer modType; // 调制类型
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.5", writable = true, type = "Integer32")
    private Integer modPreambleLen; // 前导长度
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.6", writable = true, type = "Integer32")
    private Integer modDifferentialEncoding; // 查分编码开关
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.7", writable = true, type = "Integer32")
    private Integer modFECErrorCorrection; // 可纠正最大字节数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.8", writable = true, type = "Integer32")
    private Integer modFECCodewordLength; // FEC纠错码长度
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.9", writable = true, type = "Integer32")
    private Integer modScramblerSeed;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.10", writable = true, type = "Integer32")
    private Integer modMaxBurstSize;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.11", type = "Integer32")
    private Long modGuardTimeSize; // 保护时间
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.12", writable = true, type = "Integer32")
    private Integer modLastCodewordShortened;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.13", writable = true, type = "Integer32")
    private Integer modScrambler;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.14", writable = true, type = "Integer32")
    private Long modByteInterleaverDepth; // ATDMA交织深度
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.15", writable = true, type = "Integer32")
    private Long modByteInterleaverBlockSize; // ATDMA交织块大小
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.16", writable = true, type = "Integer32")
    private Integer modPreambleType; // 前导类型
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.17", writable = true, type = "Integer32")
    private Integer modTcmErrorCorrectionOn; // 网格编码
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.18", writable = true, type = "Integer32")
    private Integer modScdmaInterleaverStepSize; // S-CDMA步长
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.19", writable = true, type = "Integer32")
    private Integer modScdmaSpreaderEnable; // CDMA扩频开关
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.20", writable = true, type = "Integer32")
    private Integer modScdmaSubframeCodes; // S-CDMA大小
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.21", writable = true, type = "Integer32")
    private Integer modChannelType; // 通道类型
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.5.1.22")
    private Integer modStorageType;
    // for unit
    private String docsIfCmtsModPreambleLenForunit; // 前导长度bits
    private String docsIfCmtsModFECErrorCorrectionForunit; // 可纠正最大字节数Bytes
    private String docsIfCmtsModFECCodewordLengthForunit; // FEC纠错码长度Bytes
    private String docsIfCmtsModMaxBurstSizeForunit;// mini-slots
    private String docsIfCmtsModGuardTimeSizeForunit; // 保护时间Symbol-times

    private String docsIfCmtsModIntervalUsageCodeName;
    private String docsIfCmtsModControlName;
    private String docsIfCmtsModTypeName;
    private String docsIfCmtsModDifferentialEncodingName;// TRUE OR FALSE
    private String docsIfCmtsModLastCodewordShortenedName;// TRUE OR FALSE
    private String docsIfCmtsModScramblerName;// TRUE OR FALSE
    private String docsIfCmtsModPreambleTypeName;
    private String docsIfCmtsModTcmErrorCorrectionOnName;// TRUE OR FALSE
    private String docsIfCmtsModScdmaSpreaderEnableName;// TRUE OR FALSE
    private String docsIfCmtsModChannelTypeName;

    public static final String[] TRUEORFALSE_COMMONTYPES = { "", "true", "false" };
    public static final String[] CODETYPES = { "", "request", "requestData", "initialRanging", "periodicRanging",
            "shortData", "longData", "", "", "advPhyShortData", "advPhyLongData", "ugs" };
    public static final String[] CONTROLNAMETYPES = { "", "active", "notInService", "notReady", "createAndGo",
            "createAndWait", "destroy" };
    public static final String[] MODTPPETYPES = { "", "other", "qpsk", "qam16", "qam8", "qam32", "qam64", "qam128",
            "qam256" };
    public static final String[] PREAMBLETYPETYPES = { "unknown", "qpsk0", "qpsk1" };
    public static final String[] CHANNELTYPETYPES = { "unknown", "tdma", "tdma", "scdma", "tdmaAndAtdma" };

    /**
     * @return the docsIfCmtsModIndex
     */
    public Long getModIndex() {
        return modIndex;
    }

    /**
     * @param modIndex
     *            the docsIfCmtsModIndex to set
     */
    public void setModIndex(Long modIndex) {
        this.modIndex = modIndex;
    }

    /**
     * @return the docsIfCmtsModIntervalUsageCode
     */
    public Integer getModIntervalUsageCode() {
        return modIntervalUsageCode;
    }

    /**
     * @param modIntervalUsageCode
     *            the docsIfCmtsModIntervalUsageCode to set
     */
    public void setModIntervalUsageCode(Integer modIntervalUsageCode) {
        this.modIntervalUsageCode = modIntervalUsageCode;
        if (this.modIntervalUsageCode != null) {
            this.docsIfCmtsModIntervalUsageCodeName = CODETYPES[modIntervalUsageCode];
        }
    }

    /**
     * @return the docsIfCmtsModControl
     */
    public Integer getModControl() {
        return modControl;
    }

    /**
     * @param modControl
     *            the docsIfCmtsModControl to set
     */
    public void setModControl(Integer modControl) {
        this.modControl = modControl;
        if (this.modControl != null) {
            this.docsIfCmtsModControlName = CONTROLNAMETYPES[modControl];
        }
    }

    /**
     * @return the docsIfCmtsModType
     */
    public Integer getModType() {
        return modType;
    }

    /**
     * @param modType
     *            the docsIfCmtsModType to set
     */
    public void setModType(Integer modType) {
        this.modType = modType;
        if (this.modType != null) {
            this.docsIfCmtsModTypeName = MODTPPETYPES[modType];
        }
    }

    /**
     * @return the docsIfCmtsModPreambleLen
     */
    public Integer getModPreambleLen() {
        return modPreambleLen;
    }

    /**
     * @param modPreambleLen
     *            the docsIfCmtsModPreambleLen to set
     */
    public void setModPreambleLen(Integer modPreambleLen) {
        this.modPreambleLen = modPreambleLen;
    }

    /**
     * @return the docsIfCmtsModDifferentialEncoding
     */
    public Integer getModDifferentialEncoding() {
        return modDifferentialEncoding;
    }

    /**
     * @param modDifferentialEncoding
     *            the docsIfCmtsModDifferentialEncoding to set
     */
    public void setModDifferentialEncoding(Integer modDifferentialEncoding) {
        this.modDifferentialEncoding = modDifferentialEncoding;
        if (this.modDifferentialEncoding != null) {
            this.docsIfCmtsModDifferentialEncodingName = TRUEORFALSE_COMMONTYPES[modDifferentialEncoding];
        }
    }

    /**
     * @return the docsIfCmtsModFECErrorCorrection
     */
    public Integer getModFECErrorCorrection() {
        return modFECErrorCorrection;
    }

    /**
     * @param modFECErrorCorrection
     *            the docsIfCmtsModFECErrorCorrection to set
     */
    public void setModFECErrorCorrection(Integer modFECErrorCorrection) {
        this.modFECErrorCorrection = modFECErrorCorrection;
    }

    /**
     * @return the docsIfCmtsModFECCodewordLength
     */
    public Integer getModFECCodewordLength() {
        return modFECCodewordLength;
    }

    /**
     * @param modFECCodewordLength
     *            the docsIfCmtsModFECCodewordLength to set
     */
    public void setModFECCodewordLength(Integer modFECCodewordLength) {
        this.modFECCodewordLength = modFECCodewordLength;
    }

    /**
     * @return the docsIfCmtsModScramblerSeed
     */
    public Integer getModScramblerSeed() {
        return modScramblerSeed;
    }

    /**
     * @param modScramblerSeed
     *            the docsIfCmtsModScramblerSeed to set
     */
    public void setModScramblerSeed(Integer modScramblerSeed) {
        this.modScramblerSeed = modScramblerSeed;
    }

    /**
     * @return the docsIfCmtsModMaxBurstSize
     */
    public Integer getModMaxBurstSize() {
        return modMaxBurstSize;
    }

    /**
     * @param modMaxBurstSize
     *            the docsIfCmtsModMaxBurstSize to set
     */
    public void setModMaxBurstSize(Integer modMaxBurstSize) {
        this.modMaxBurstSize = modMaxBurstSize;
    }

    /**
     * @return the docsIfCmtsModGuardTimeSize
     */
    public Long getModGuardTimeSize() {
        return modGuardTimeSize;
    }

    /**
     * @param modGuardTimeSize
     *            the docsIfCmtsModGuardTimeSize to set
     */
    public void setModGuardTimeSize(Long modGuardTimeSize) {
        this.modGuardTimeSize = modGuardTimeSize;
    }

    /**
     * @return the docsIfCmtsModLastCodewordShortened
     */
    public Integer getModLastCodewordShortened() {
        return modLastCodewordShortened;
    }

    /**
     * @param modLastCodewordShortened
     *            the docsIfCmtsModLastCodewordShortened to set
     */
    public void setModLastCodewordShortened(Integer modLastCodewordShortened) {
        this.modLastCodewordShortened = modLastCodewordShortened;
        if (this.modLastCodewordShortened != null) {
            this.docsIfCmtsModLastCodewordShortenedName = TRUEORFALSE_COMMONTYPES[modLastCodewordShortened];
        }
    }

    /**
     * @return the docsIfCmtsModScrambler
     */
    public Integer getModScrambler() {
        return modScrambler;
    }

    /**
     * @param modScrambler
     *            the docsIfCmtsModScrambler to set
     */
    public void setModScrambler(Integer modScrambler) {
        this.modScrambler = modScrambler;
        if (this.modScrambler != null) {
            this.docsIfCmtsModScramblerName = TRUEORFALSE_COMMONTYPES[modScrambler];
        }
    }

    /**
     * @return the docsIfCmtsModByteInterleaverDepth
     */
    public Long getModByteInterleaverDepth() {
        return modByteInterleaverDepth;
    }

    /**
     * @param modByteInterleaverDepth
     *            the docsIfCmtsModByteInterleaverDepth to set
     */
    public void setModByteInterleaverDepth(Long modByteInterleaverDepth) {
        this.modByteInterleaverDepth = modByteInterleaverDepth;
    }

    /**
     * @return the docsIfCmtsModByteInterleaverBlockSize
     */
    public Long getModByteInterleaverBlockSize() {
        return modByteInterleaverBlockSize;
    }

    /**
     * @param modByteInterleaverBlockSize
     *            the docsIfCmtsModByteInterleaverBlockSize to set
     */
    public void setModByteInterleaverBlockSize(Long modByteInterleaverBlockSize) {
        this.modByteInterleaverBlockSize = modByteInterleaverBlockSize;
    }

    /**
     * @return the docsIfCmtsModPreambleType
     */
    public Integer getModPreambleType() {
        return modPreambleType;
    }

    /**
     * @param modPreambleType
     *            the docsIfCmtsModPreambleType to set
     */
    public void setModPreambleType(Integer modPreambleType) {
        this.modPreambleType = modPreambleType;
        if (this.modPreambleType != null) {
            this.docsIfCmtsModPreambleTypeName = PREAMBLETYPETYPES[modPreambleType];
        }
    }

    /**
     * @return the docsIfCmtsModTcmErrorCorrectionOn
     */
    public Integer getModTcmErrorCorrectionOn() {
        return modTcmErrorCorrectionOn;
    }

    /**
     * @param modTcmErrorCorrectionOn
     *            the docsIfCmtsModTcmErrorCorrectionOn to set
     */
    public void setModTcmErrorCorrectionOn(Integer modTcmErrorCorrectionOn) {
        this.modTcmErrorCorrectionOn = modTcmErrorCorrectionOn;
        if (this.modTcmErrorCorrectionOn != null) {
            this.docsIfCmtsModTcmErrorCorrectionOnName = TRUEORFALSE_COMMONTYPES[modTcmErrorCorrectionOn];
        }
    }

    /**
     * @return the docsIfCmtsModScdmaInterleaverStepSize
     */
    public Integer getModScdmaInterleaverStepSize() {
        return modScdmaInterleaverStepSize;
    }

    /**
     * @param modScdmaInterleaverStepSize
     *            the docsIfCmtsModScdmaInterleaverStepSize to set
     */
    public void setModScdmaInterleaverStepSize(Integer modScdmaInterleaverStepSize) {
        this.modScdmaInterleaverStepSize = modScdmaInterleaverStepSize;
    }

    /**
     * @return the docsIfCmtsModScdmaSpreaderEnable
     */
    public Integer getModScdmaSpreaderEnable() {
        return modScdmaSpreaderEnable;
    }

    /**
     * @param modScdmaSpreaderEnable
     *            the docsIfCmtsModScdmaSpreaderEnable to set
     */
    public void setModScdmaSpreaderEnable(Integer modScdmaSpreaderEnable) {
        this.modScdmaSpreaderEnable = modScdmaSpreaderEnable;
        if (this.modScdmaSpreaderEnable != null) {
            this.docsIfCmtsModScdmaSpreaderEnableName = TRUEORFALSE_COMMONTYPES[modScdmaSpreaderEnable];
        }
    }

    /**
     * @return the docsIfCmtsModScdmaSubframeCodes
     */
    public Integer getModScdmaSubframeCodes() {
        return modScdmaSubframeCodes;
    }

    /**
     * @param modScdmaSubframeCodes
     *            the docsIfCmtsModScdmaSubframeCodes to set
     */
    public void setModScdmaSubframeCodes(Integer modScdmaSubframeCodes) {
        this.modScdmaSubframeCodes = modScdmaSubframeCodes;
    }

    /**
     * @return the docsIfCmtsModChannelType
     */
    public Integer getModChannelType() {
        return modChannelType;
    }

    /**
     * @param modChannelType
     *            the docsIfCmtsModChannelType to set
     */
    public void setModChannelType(Integer modChannelType) {
        this.modChannelType = modChannelType;
        if (this.modChannelType != null) {
            this.docsIfCmtsModChannelTypeName = CHANNELTYPETYPES[modChannelType];
        }
    }

    /**
     * @return the docsIfCmtsModStorageType
     */
    public Integer getModStorageType() {
        return modStorageType;
    }

    /**
     * @param modStorageType
     *            the docsIfCmtsModStorageType to set
     */
    public void setModStorageType(Integer modStorageType) {
        this.modStorageType = modStorageType;
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
     * @return the docsIfCmtsModIntervalUsageCodeName
     */
    public String getDocsIfCmtsModIntervalUsageCodeName() {
        return docsIfCmtsModIntervalUsageCodeName;
    }

    /**
     * @param docsIfCmtsModIntervalUsageCodeName
     *            the docsIfCmtsModIntervalUsageCodeName to set
     */
    public void setDocsIfCmtsModIntervalUsageCodeName(String docsIfCmtsModIntervalUsageCodeName) {
        this.docsIfCmtsModIntervalUsageCodeName = docsIfCmtsModIntervalUsageCodeName;
    }

    /**
     * @return the docsIfCmtsModControlName
     */
    public String getDocsIfCmtsModControlName() {
        return docsIfCmtsModControlName;
    }

    /**
     * @param docsIfCmtsModControlName
     *            the docsIfCmtsModControlName to set
     */
    public void setDocsIfCmtsModControlName(String docsIfCmtsModControlName) {
        this.docsIfCmtsModControlName = docsIfCmtsModControlName;
    }

    /**
     * @return the docsIfCmtsModTypeName
     */
    public String getDocsIfCmtsModTypeName() {
        return docsIfCmtsModTypeName;
    }

    /**
     * @param docsIfCmtsModTypeName
     *            the docsIfCmtsModTypeName to set
     */
    public void setDocsIfCmtsModTypeName(String docsIfCmtsModTypeName) {
        this.docsIfCmtsModTypeName = docsIfCmtsModTypeName;
    }

    /**
     * @return the docsIfCmtsModDifferentialEncodingName
     */
    public String getDocsIfCmtsModDifferentialEncodingName() {
        return docsIfCmtsModDifferentialEncodingName;
    }

    /**
     * @param docsIfCmtsModDifferentialEncodingName
     *            the docsIfCmtsModDifferentialEncodingName to set
     */
    public void setDocsIfCmtsModDifferentialEncodingName(String docsIfCmtsModDifferentialEncodingName) {
        this.docsIfCmtsModDifferentialEncodingName = docsIfCmtsModDifferentialEncodingName;
    }

    /**
     * @return the docsIfCmtsModLastCodewordShortenedName
     */
    public String getDocsIfCmtsModLastCodewordShortenedName() {
        return docsIfCmtsModLastCodewordShortenedName;
    }

    /**
     * @param docsIfCmtsModLastCodewordShortenedName
     *            the docsIfCmtsModLastCodewordShortenedName to set
     */
    public void setDocsIfCmtsModLastCodewordShortenedName(String docsIfCmtsModLastCodewordShortenedName) {
        this.docsIfCmtsModLastCodewordShortenedName = docsIfCmtsModLastCodewordShortenedName;
    }

    /**
     * @return the docsIfCmtsModScramblerName
     */
    public String getDocsIfCmtsModScramblerName() {
        return docsIfCmtsModScramblerName;
    }

    /**
     * @param docsIfCmtsModScramblerName
     *            the docsIfCmtsModScramblerName to set
     */
    public void setDocsIfCmtsModScramblerName(String docsIfCmtsModScramblerName) {
        this.docsIfCmtsModScramblerName = docsIfCmtsModScramblerName;
    }

    /**
     * @return the docsIfCmtsModPreambleTypeName
     */
    public String getDocsIfCmtsModPreambleTypeName() {
        return docsIfCmtsModPreambleTypeName;
    }

    /**
     * @param docsIfCmtsModPreambleTypeName
     *            the docsIfCmtsModPreambleTypeName to set
     */
    public void setDocsIfCmtsModPreambleTypeName(String docsIfCmtsModPreambleTypeName) {
        this.docsIfCmtsModPreambleTypeName = docsIfCmtsModPreambleTypeName;
    }

    /**
     * @return the docsIfCmtsModTcmErrorCorrectionOnName
     */
    public String getDocsIfCmtsModTcmErrorCorrectionOnName() {
        return docsIfCmtsModTcmErrorCorrectionOnName;
    }

    /**
     * @param docsIfCmtsModTcmErrorCorrectionOnName
     *            the docsIfCmtsModTcmErrorCorrectionOnName to set
     */
    public void setDocsIfCmtsModTcmErrorCorrectionOnName(String docsIfCmtsModTcmErrorCorrectionOnName) {
        this.docsIfCmtsModTcmErrorCorrectionOnName = docsIfCmtsModTcmErrorCorrectionOnName;
    }

    /**
     * @return the docsIfCmtsModScdmaSpreaderEnableName
     */
    public String getDocsIfCmtsModScdmaSpreaderEnableName() {
        return docsIfCmtsModScdmaSpreaderEnableName;
    }

    /**
     * @param docsIfCmtsModScdmaSpreaderEnableName
     *            the docsIfCmtsModScdmaSpreaderEnableName to set
     */
    public void setDocsIfCmtsModScdmaSpreaderEnableName(String docsIfCmtsModScdmaSpreaderEnableName) {
        this.docsIfCmtsModScdmaSpreaderEnableName = docsIfCmtsModScdmaSpreaderEnableName;
    }

    /**
     * @return the docsIfCmtsModChannelTypeName
     */
    public String getDocsIfCmtsModChannelTypeName() {
        return docsIfCmtsModChannelTypeName;
    }

    /**
     * @param docsIfCmtsModChannelTypeName
     *            the docsIfCmtsModChannelTypeName to set
     */
    public void setDocsIfCmtsModChannelTypeName(String docsIfCmtsModChannelTypeName) {
        this.docsIfCmtsModChannelTypeName = docsIfCmtsModChannelTypeName;
    }

    /**
     * @return the docsIfCmtsModPreambleLenForunit
     */
    public String getDocsIfCmtsModPreambleLenForunit() {
        if (this.getModPreambleLen() != null) {
            docsIfCmtsModPreambleLenForunit = modPreambleLen.toString() + "bits";
        }
        return docsIfCmtsModPreambleLenForunit;
    }

    /**
     * @param docsIfCmtsModPreambleLenForunit
     *            the docsIfCmtsModPreambleLenForunit to set
     */
    public void setDocsIfCmtsModPreambleLenForunit(String docsIfCmtsModPreambleLenForunit) {
        this.docsIfCmtsModPreambleLenForunit = docsIfCmtsModPreambleLenForunit;
    }

    /**
     * @return the docsIfCmtsModFECErrorCorrectionForunit
     */
    public String getDocsIfCmtsModFECErrorCorrectionForunit() {
        if (this.getModFECErrorCorrection() != null) {
            docsIfCmtsModFECErrorCorrectionForunit = modFECErrorCorrection.toString() + "Bytes";
        }
        return docsIfCmtsModFECErrorCorrectionForunit;
    }

    /**
     * @param docsIfCmtsModFECErrorCorrectionForunit
     *            the docsIfCmtsModFECErrorCorrectionForunit to set
     */
    public void setDocsIfCmtsModFECErrorCorrectionForunit(String docsIfCmtsModFECErrorCorrectionForunit) {
        this.docsIfCmtsModFECErrorCorrectionForunit = docsIfCmtsModFECErrorCorrectionForunit;
    }

    /**
     * @return the docsIfCmtsModFECCodewordLengthForunit
     */
    public String getDocsIfCmtsModFECCodewordLengthForunit() {
        if (this.getModFECCodewordLength() != null) {
            docsIfCmtsModFECCodewordLengthForunit = modFECCodewordLength.toString() + "Bytes";
        }
        return docsIfCmtsModFECCodewordLengthForunit;
    }

    /**
     * @param docsIfCmtsModFECCodewordLengthForunit
     *            the docsIfCmtsModFECCodewordLengthForunit to set
     */
    public void setDocsIfCmtsModFECCodewordLengthForunit(String docsIfCmtsModFECCodewordLengthForunit) {
        this.docsIfCmtsModFECCodewordLengthForunit = docsIfCmtsModFECCodewordLengthForunit;
    }

    /**
     * @return the docsIfCmtsModMaxBurstSizeForunit
     */
    public String getDocsIfCmtsModMaxBurstSizeForunit() {
        if (this.getModMaxBurstSize() != null) {
            docsIfCmtsModMaxBurstSizeForunit = modMaxBurstSize.toString() + "mini-slots";
        }
        return docsIfCmtsModMaxBurstSizeForunit;
    }

    /**
     * @param docsIfCmtsModMaxBurstSizeForunit
     *            the docsIfCmtsModMaxBurstSizeForunit to set
     */
    public void setDocsIfCmtsModMaxBurstSizeForunit(String docsIfCmtsModMaxBurstSizeForunit) {
        this.docsIfCmtsModMaxBurstSizeForunit = docsIfCmtsModMaxBurstSizeForunit;
    }

    /**
     * @return the docsIfCmtsModGuardTimeSizeForunit
     */
    public String getDocsIfCmtsModGuardTimeSizeForunit() {
        if (this.getModGuardTimeSize() != null) {
            docsIfCmtsModGuardTimeSizeForunit = modGuardTimeSize.toString() + "Symbol-times";
        }
        return docsIfCmtsModGuardTimeSizeForunit;
    }

    /**
     * @param docsIfCmtsModGuardTimeSizeForunit
     *            the docsIfCmtsModGuardTimeSizeForunit to set
     */
    public void setDocsIfCmtsModGuardTimeSizeForunit(String docsIfCmtsModGuardTimeSizeForunit) {
        this.docsIfCmtsModGuardTimeSizeForunit = docsIfCmtsModGuardTimeSizeForunit;
    }

    /**
     * @return the cmcModId
     */
    public Long getCmcModId() {
        return cmcModId;
    }

    /**
     * @param cmcModId
     *            the cmcModId to set
     */
    public void setCmcModId(Long cmcModId) {
        this.cmcModId = cmcModId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcModulationProfileInfo [cmcModId=");
        builder.append(cmcModId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", modIndex=");
        builder.append(modIndex);
        builder.append(", modIntervalUsageCode=");
        builder.append(modIntervalUsageCode);
        builder.append(", modControl=");
        builder.append(modControl);
        builder.append(", modType=");
        builder.append(modType);
        builder.append(", modPreambleLen=");
        builder.append(modPreambleLen);
        builder.append(", modDifferentialEncoding=");
        builder.append(modDifferentialEncoding);
        builder.append(", modFECErrorCorrection=");
        builder.append(modFECErrorCorrection);
        builder.append(", modFECCodewordLength=");
        builder.append(modFECCodewordLength);
        builder.append(", modScramblerSeed=");
        builder.append(modScramblerSeed);
        builder.append(", modMaxBurstSize=");
        builder.append(modMaxBurstSize);
        builder.append(", modGuardTimeSize=");
        builder.append(modGuardTimeSize);
        builder.append(", modLastCodewordShortened=");
        builder.append(modLastCodewordShortened);
        builder.append(", modScrambler=");
        builder.append(modScrambler);
        builder.append(", modByteInterleaverDepth=");
        builder.append(modByteInterleaverDepth);
        builder.append(", modByteInterleaverBlockSize=");
        builder.append(modByteInterleaverBlockSize);
        builder.append(", modPreambleType=");
        builder.append(modPreambleType);
        builder.append(", modTcmErrorCorrectionOn=");
        builder.append(modTcmErrorCorrectionOn);
        builder.append(", modScdmaInterleaverStepSize=");
        builder.append(modScdmaInterleaverStepSize);
        builder.append(", modScdmaSpreaderEnable=");
        builder.append(modScdmaSpreaderEnable);
        builder.append(", modScdmaSubframeCodes=");
        builder.append(modScdmaSubframeCodes);
        builder.append(", modChannelType=");
        builder.append(modChannelType);
        builder.append(", modStorageType=");
        builder.append(modStorageType);
        builder.append(", docsIfCmtsModPreambleLenForunit=");
        builder.append(docsIfCmtsModPreambleLenForunit);
        builder.append(", docsIfCmtsModFECErrorCorrectionForunit=");
        builder.append(docsIfCmtsModFECErrorCorrectionForunit);
        builder.append(", docsIfCmtsModFECCodewordLengthForunit=");
        builder.append(docsIfCmtsModFECCodewordLengthForunit);
        builder.append(", docsIfCmtsModMaxBurstSizeForunit=");
        builder.append(docsIfCmtsModMaxBurstSizeForunit);
        builder.append(", docsIfCmtsModGuardTimeSizeForunit=");
        builder.append(docsIfCmtsModGuardTimeSizeForunit);
        builder.append(", docsIfCmtsModIntervalUsageCodeName=");
        builder.append(docsIfCmtsModIntervalUsageCodeName);
        builder.append(", docsIfCmtsModControlName=");
        builder.append(docsIfCmtsModControlName);
        builder.append(", docsIfCmtsModTypeName=");
        builder.append(docsIfCmtsModTypeName);
        builder.append(", docsIfCmtsModDifferentialEncodingName=");
        builder.append(docsIfCmtsModDifferentialEncodingName);
        builder.append(", docsIfCmtsModLastCodewordShortenedName=");
        builder.append(docsIfCmtsModLastCodewordShortenedName);
        builder.append(", docsIfCmtsModScramblerName=");
        builder.append(docsIfCmtsModScramblerName);
        builder.append(", docsIfCmtsModPreambleTypeName=");
        builder.append(docsIfCmtsModPreambleTypeName);
        builder.append(", docsIfCmtsModTcmErrorCorrectionOnName=");
        builder.append(docsIfCmtsModTcmErrorCorrectionOnName);
        builder.append(", docsIfCmtsModScdmaSpreaderEnableName=");
        builder.append(docsIfCmtsModScdmaSpreaderEnableName);
        builder.append(", docsIfCmtsModChannelTypeName=");
        builder.append(docsIfCmtsModChannelTypeName);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
