/***********************************************************************
 * $Id: PnmpCmData.java,v1.0 2017年8月8日 下午2:20:07 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.facade.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import org.apache.ibatis.type.Alias;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午2:20:07
 *
 */
@Alias("pnmpCmData")
public class PnmpCmData implements AliasesSuperType {

    private static final long serialVersionUID = -3631763536410111685L;
    private Long cmcId;
    private String entityIp;
    private String cmcName;
    private String cmMac;
    private String cmAddress;
    private Timestamp collectTime;
    private String collectTimeString;
    private String tapCoefficient;// 抽头系数
    private String spectrumResponse;// 频响曲线
    private Integer correlationGroup;// 故障分组
    private Double mte;// 主抽头能量
    private Double preMte;// 前抽头能量
    private Double postMte;// 后抽头能量
    private Double tte;// 总抽头能量
    private Double mtc;// 主抽头压缩
    private Double mtr;// 主抽头与非主抽头所占能量比
    private Double nmtter; // 非主抽头所占能量比
    private Double premtter; // 前抽头所占能量比
    private Double postmtter;// 后抽头所占能量比
    private Double ppesr; // 前后抽头能量比
    private Double mrLevel;// 微反射水平
    private Double tdr;// 故障点离CM距离
    private Double upSnr;// 上行SNR
    private Double upTxPower;// 上行发送电平
    private Double downSnr;// 下行SNR
    private Double downRxPower;// 下行接收电平
    private Boolean preEqualizationState;// 预均衡开启状态
    private Long upChannelWidth;// 上行信道宽度
    private Integer upChannelId;// 上行信道ID
    private Long upChannelFreq;// 上行信道中心频率
    private String cmIp;
    private Long entityId;
    private Short statusValue;
    private Double mtrVariance;
    private Double upSnrVariance;
    private Double mtrToUpSnrSimilarity;
    private String cmUserPhoneNo;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public String getCmcName() {
        return cmcName;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public String getCmAddress() {
        return cmAddress;
    }

    public void setCmAddress(String cmAddress) {
        this.cmAddress = cmAddress;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public String getTapCoefficient() {
        return tapCoefficient;
    }

    public void setTapCoefficient(String tapCoefficient) {
        this.tapCoefficient = tapCoefficient;
    }

    public String getSpectrumResponse() {
        return spectrumResponse;
    }

    public void setSpectrumResponse(String spectrumResponse) {
        this.spectrumResponse = spectrumResponse;
    }

    public Integer getCorrelationGroup() {
        return correlationGroup;
    }

    public void setCorrelationGroup(Integer correlationGroup) {
        this.correlationGroup = correlationGroup;
    }

    public Double getMte() {
        return mte;
    }

    public void setMte(Double mte) {
        this.mte = mte;
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

    public Double getMtr() {
        return mtr;
    }

    public void setMtr(Double mtr) {
        this.mtr = mtr;
    }

    public Double getNmtter() {
        return nmtter;
    }

    public void setNmtter(Double nmtter) {
        this.nmtter = nmtter;
    }

    public Double getPremtter() {
        return premtter;
    }

    public void setPremtter(Double premtter) {
        this.premtter = premtter;
    }

    public Double getPostmtter() {
        return postmtter;
    }

    public void setPostmtter(Double postmtter) {
        this.postmtter = postmtter;
    }

    public Double getPpesr() {
        return ppesr;
    }

    public void setPpesr(Double ppesr) {
        this.ppesr = ppesr;
    }

    public Double getMrLevel() {
        return mrLevel;
    }

    public void setMrLevel(Double mrLevel) {
        this.mrLevel = mrLevel;
    }

    public Double getTdr() {
        return tdr;
    }

    public void setTdr(Double tdr) {
        this.tdr = tdr;
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

    public Boolean getPreEqualizationState() {
        return preEqualizationState;
    }

    public void setPreEqualizationState(Boolean preEqualizationState) {
        this.preEqualizationState = preEqualizationState;
    }

    public String getCollectTimeString() {
        if (collectTimeString == null && collectTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            collectTimeString = sdf.format(collectTime);
        }
        return collectTimeString;
    }

    public void setCollectTimeString(String collectTimeString) {
        this.collectTimeString = collectTimeString;
    }

    public String getCmIp() {
        return cmIp;
    }

    public void setCmIp(String cmIp) {
        this.cmIp = cmIp;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Short getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Short statusValue) {
        this.statusValue = statusValue;
    }

    public Long getUpChannelWidth() {
        return upChannelWidth;
    }

    public void setUpChannelWidth(Long upChannelWidth) {
        this.upChannelWidth = upChannelWidth;
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
        StringBuilder builder = new StringBuilder();
        builder.append("PnmpCmData [cmcId=");
        builder.append(cmcId);
        builder.append(", entityIp=");
        builder.append(entityIp);
        builder.append(", cmcName=");
        builder.append(cmcName);
        builder.append(", cmMac=");
        builder.append(cmMac);
        builder.append(", cmAddress=");
        builder.append(cmAddress);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append(", tapCoefficient=");
        builder.append(tapCoefficient);
        builder.append(", spectrumResponse=");
        builder.append(spectrumResponse);
        builder.append(", correlationGroup=");
        builder.append(correlationGroup);
        builder.append(", mte=");
        builder.append(mte);
        builder.append(", preMte=");
        builder.append(preMte);
        builder.append(", postMte=");
        builder.append(postMte);
        builder.append(", tte=");
        builder.append(tte);
        builder.append(", mtc=");
        builder.append(mtc);
        builder.append(", mtr=");
        builder.append(mtr);
        builder.append(", nmtter=");
        builder.append(nmtter);
        builder.append(", premtter=");
        builder.append(premtter);
        builder.append(", postmtter=");
        builder.append(postmtter);
        builder.append(", ppesr=");
        builder.append(ppesr);
        builder.append(", mrLevel=");
        builder.append(mrLevel);
        builder.append(", tdr=");
        builder.append(tdr);
        builder.append(", upSnr=");
        builder.append(upSnr);
        builder.append(", upTxPower=");
        builder.append(upTxPower);
        builder.append(", downSnr=");
        builder.append(downSnr);
        builder.append(", downRxPower=");
        builder.append(downRxPower);
        builder.append(", statusValue=");
        builder.append(statusValue);
        builder.append(", upChannelWidth=");
        builder.append(upChannelWidth);
        builder.append(", upChannelId=");
        builder.append(upChannelId);
        builder.append(", upChannelFreq=");
        builder.append(upChannelFreq);
        builder.append("]");
        return builder.toString();
    }

    public String getCmUserPhoneNo() {
        return cmUserPhoneNo;
    }

    public void setCmUserPhoneNo(String cmUserPhoneNo) {
        this.cmUserPhoneNo = cmUserPhoneNo;
    }
}
