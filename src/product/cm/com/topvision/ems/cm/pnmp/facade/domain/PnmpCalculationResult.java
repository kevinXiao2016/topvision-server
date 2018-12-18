package com.topvision.ems.cm.pnmp.facade.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * Created by jay on 17-8-16.
 */
public class PnmpCalculationResult implements AliasesSuperType {
    private static final long serialVersionUID = -2878039210974287090L;
	public static final int NULL = -1;
	public static final int HEALTH = 0;
	public static final int MARGINAL = 1;
	public static final int BAD = 2;
    private Long entityId;
    private Long cmcId;
    private Long cmcIndex;
    private Long cmIndex;
    private String cmIp;
    private String cmMac;
    private Integer statusValue;
    private Integer checkStatus;
    private Double mte;
    private Double preMte;
    private Double postMte;
    private Double tte;
    private Double mtc;
    private Double mtr;
    private Double mtrVariance;
    private Double upSnrVariance;
    private Double mtrToUpSnrSimilarity;
    private Integer mtrLevel;
    private Double mrLevel;
    private Double nmtter;
    private Double preMtter;
    private Double postMtter;
    private Double ppesr;
    private Double tdr;
    private Double upSnr;
    private Double upTxPower;
    private Double downSnr;
    private Double downRxPower;
    private Integer correlationGroup;
    private String tapCoefficient;//抽头系数
    private String spectrumResponse;
    private Boolean preEqualizationState = false;
    private String orginalValue;//每次采集的原始数据
    private Integer upChannelId;// 上行信道ID
    private Long upChannelFreq;// 上行信道中心频率
    private Long upChannelWidth;//上行信道带宽
    private String collectTime;

    public Double getMte() {
        return mte;
    }

    public void setMte(Double mte) {
        this.mte = mte;
    }

    public Double getTte() {
        return tte;
    }

    public void setTte(Double tte) {
        this.tte = tte;
    }

    public Double getMtc() {
        return mtc;
    }

    public void setMtc(Double mtc) {
        this.mtc = mtc;
    }

    public Double getMrLevel() {
        return mrLevel;
    }

    public void setMrLevel(Double mrLevel) {
        this.mrLevel = mrLevel;
    }

    public Double getNmtter() {
        return nmtter;
    }

    public void setNmtter(Double nmtter) {
        this.nmtter = nmtter;
    }

    public Double getPpesr() {
        return ppesr;
    }

    public void setPpesr(Double ppesr) {
        this.ppesr = ppesr;
    }

    public Double getTdr() {
        return tdr;
    }

    public void setTdr(Double tdr) {
        this.tdr = tdr;
    }

    public String getSpectrumResponse() {
        return spectrumResponse;
    }

    public void setSpectrumResponse(String spectrumResponse) {
        this.spectrumResponse = spectrumResponse;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Double getPreMte() {
        return preMte;
    }

    public void setPreMte(Double preMte) {
        this.preMte = preMte;
    }

    public Double getPostMte() {
        return postMte;
    }

    public void setPostMte(Double postMte) {
        this.postMte = postMte;
    }

    public Double getPreMtter() {
        return preMtter;
    }

    public void setPreMtter(Double preMtter) {
        this.preMtter = preMtter;
    }

    public Double getPostMtter() {
        return postMtter;
    }

    public void setPostMtter(Double postMtter) {
        this.postMtter = postMtter;
    }

    public Double getUpSnr() {
        return upSnr;
    }

    public void setUpSnr(Double upSnr) {
        this.upSnr = upSnr;
    }

    public Double getUpTxPower() {
        return upTxPower;
    }

    public void setUpTxPower(Double upTxPower) {
        this.upTxPower = upTxPower;
    }

    public Double getDownSnr() {
        return downSnr;
    }

    public void setDownSnr(Double downSnr) {
        this.downSnr = downSnr;
    }

    public Double getDownRxPower() {
        return downRxPower;
    }

    public void setDownRxPower(Double downRxPower) {
        this.downRxPower = downRxPower;
    }

    public Integer getCorrelationGroup() {
        return correlationGroup;
    }

    public void setCorrelationGroup(Integer correlationGroup) {
        this.correlationGroup = correlationGroup;
    }

    public String getTapCoefficient() {
        return tapCoefficient;
    }

    public void setTapCoefficient(String tapCoefficient) {
        this.tapCoefficient = tapCoefficient;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    public String getCmIp() {
        return cmIp;
    }

    public void setCmIp(String cmIp) {
        this.cmIp = cmIp;
    }

    public Double getMtr() {
        return mtr;
    }

    public void setMtr(Double mtr) {
        this.mtr = mtr;
    }

    public Boolean getPreEqualizationState() {
        return preEqualizationState;
    }

    public void setPreEqualizationState(Boolean preEqualizationState) {
        this.preEqualizationState = preEqualizationState;
    }

    public String getOrginalValue() {
        return orginalValue;
    }

    public void setOrginalValue(String orginalValue) {
        this.orginalValue = orginalValue;
    }

    public Long getUpChannelWidth() {
        return upChannelWidth;
    }

    public void setUpChannelWidth(Long upChannelWidth) {
        this.upChannelWidth = upChannelWidth;
    }

    public void setMtrLevel(Integer mtrLevel) {
        this.mtrLevel = mtrLevel;
    }

    public Integer getMtrLevel() {
        return mtrLevel;
    }

    public Integer getUpChannelId() {
        return upChannelId;
    }

    public void setUpChannelId(Integer upChannelId) {
        this.upChannelId = upChannelId;
    }

    public Long getUpChannelFreq() {
        return upChannelFreq;
    }

    public void setUpChannelFreq(Long upChannelFreq) {
        this.upChannelFreq = upChannelFreq;
    }

    public Double getMtrVariance() {
        return mtrVariance;
    }

    public void setMtrVariance(Double mtrVariance) {
        this.mtrVariance = mtrVariance;
    }

    public Double getUpSnrVariance() {
        return upSnrVariance;
    }

    public void setUpSnrVariance(Double upSnrVariance) {
        this.upSnrVariance = upSnrVariance;
    }

    public Double getMtrToUpSnrSimilarity() {
        return mtrToUpSnrSimilarity;
    }

    public void setMtrToUpSnrSimilarity(Double mtrToUpSnrSimilarity) {
        this.mtrToUpSnrSimilarity = mtrToUpSnrSimilarity;
    }

    @Override
    public String toString() {
        return "PnmpCalculationResult{" +
                "entityId=" + entityId +
                ", cmcId=" + cmcId +
                ", cmcIndex=" + cmcIndex +
                ", cmIndex=" + cmIndex +
                ", cmIp='" + cmIp + '\'' +
                ", cmMac='" + cmMac + '\'' +
                ", statusValue=" + statusValue +
                ", checkStatus=" + checkStatus +
                ", mte=" + mte +
                ", preMte=" + preMte +
                ", postMte=" + postMte +
                ", tte=" + tte +
                ", mtc=" + mtc +
                ", mtr=" + mtr +
                ", mtrVariance=" + mtrVariance +
                ", upSnrVariance=" + upSnrVariance +
                ", mtrToUpSnrSimilarity=" + mtrToUpSnrSimilarity +
                ", mtrLevel=" + mtrLevel +
                ", mrLevel=" + mrLevel +
                ", nmtter=" + nmtter +
                ", preMtter=" + preMtter +
                ", postMtter=" + postMtter +
                ", ppesr=" + ppesr +
                ", tdr=" + tdr +
                ", upSnr=" + upSnr +
                ", upTxPower=" + upTxPower +
                ", downSnr=" + downSnr +
                ", downRxPower=" + downRxPower +
                ", correlationGroup=" + correlationGroup +
                ", tapCoefficient='" + tapCoefficient + '\'' +
                ", spectrumResponse='" + spectrumResponse + '\'' +
                ", preEqualizationState=" + preEqualizationState +
                ", orginalValue='" + orginalValue + '\'' +
                ", upChannelId=" + upChannelId +
                ", upChannelFreq=" + upChannelFreq +
                ", upChannelWidth=" + upChannelWidth +
                ", collectTime='" + collectTime + '\'' +
                '}';
    }
}
