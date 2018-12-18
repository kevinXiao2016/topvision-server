package com.topvision.ems.gpon.onuvoip.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * POTS列表展示 数据封装类
 * 
 * @author xiaoyue
 * @created @2017年6月19日-下午2:57:03
 * 
 */
/**
 * @author xiaoyue
 * @created @2017年6月22日-下午1:25:02
 *
 */
public class TopGponOnuPots implements AliasesSuperType {
    private static final long serialVersionUID = 4595515518155794942L;
    private Long entityId;
    private Long onuId;

    // 以下是TopSIPPstnUser表数据
    private Integer topSIPPstnUserPotsIdx;// pots口index，端口号
    private String topSIPPstnUserTelno;// 1-64 SIP PSTN用户电话号码
    private Integer topSIPPstnUserSipsrvId;// 0-64 SIP业务数据模板ID
    private Integer topSIPPstnUserDigitmapId;// 0-64 数图模板ID
    private Integer topSIPPstnUserForwardType;// 呼叫前转类型 0-disable 1-busy;2-no reply;3-unconditional
    private String topSIPPstnUserTransferNum;// 1-64 呼叫转移号码
    private Integer topSIPPstnUserForwardTime;// 5..55 s 呼叫前转无响应超时时间

    // 以下是TopVoIPLineStatus表数据
    private Integer topVoIPLineCodec;// 编码模式
    private Integer topVoIPLineServStatus;// VoIP业务服务状态
    private Integer topVoIPLineSessType;// 会话类型
    private Integer topVoIPLineState;// Voip线路状态

    // 以下是TopGponSrvPotsInfo表数据
    private Integer topGponSrvPotsInfoPotsIdx;// Pots接口索引
    private String topGponSrvPotsInfoIpIdx;// Pots口对应的IP地址

    // 以下是topOnuIfPotsInfo表数据
    private Integer topOnuIfPotsAdminState;// POTS口状态 即端口使能

    // 以下是gpon_onuiphost表数据
    private Integer onuIpHostVlanTagPriority;//VLAN优先级
    private Integer onuIpHostVlanPVid;//VLAN ID

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Integer getTopSIPPstnUserPotsIdx() {
        return topSIPPstnUserPotsIdx;
    }

    public void setTopSIPPstnUserPotsIdx(Integer topSIPPstnUserPotsIdx) {
        this.topSIPPstnUserPotsIdx = topSIPPstnUserPotsIdx;
    }

    public String getTopSIPPstnUserTelno() {
        return topSIPPstnUserTelno;
    }

    public void setTopSIPPstnUserTelno(String topSIPPstnUserTelno) {
        this.topSIPPstnUserTelno = topSIPPstnUserTelno;
    }

    public Integer getTopSIPPstnUserSipsrvId() {
        return topSIPPstnUserSipsrvId;
    }

    public void setTopSIPPstnUserSipsrvId(Integer topSIPPstnUserSipsrvId) {
        this.topSIPPstnUserSipsrvId = topSIPPstnUserSipsrvId;
    }

    public Integer getTopSIPPstnUserDigitmapId() {
        return topSIPPstnUserDigitmapId;
    }

    public void setTopSIPPstnUserDigitmapId(Integer topSIPPstnUserDigitmapId) {
        this.topSIPPstnUserDigitmapId = topSIPPstnUserDigitmapId;
    }

    public Integer getTopSIPPstnUserForwardType() {
        return topSIPPstnUserForwardType;
    }

    public void setTopSIPPstnUserForwardType(Integer topSIPPstnUserForwardType) {
        this.topSIPPstnUserForwardType = topSIPPstnUserForwardType;
    }

    public String getTopSIPPstnUserTransferNum() {
        return topSIPPstnUserTransferNum;
    }

    public void setTopSIPPstnUserTransferNum(String topSIPPstnUserTransferNum) {
        this.topSIPPstnUserTransferNum = topSIPPstnUserTransferNum;
    }

    public Integer getTopVoIPLineCodec() {
        return topVoIPLineCodec;
    }

    public void setTopVoIPLineCodec(Integer topVoIPLineCodec) {
        this.topVoIPLineCodec = topVoIPLineCodec;
    }

    public Integer getTopVoIPLineServStatus() {
        return topVoIPLineServStatus;
    }

    public void setTopVoIPLineServStatus(Integer topVoIPLineServStatus) {
        this.topVoIPLineServStatus = topVoIPLineServStatus;
    }

    public Integer getTopVoIPLineSessType() {
        return topVoIPLineSessType;
    }

    public void setTopVoIPLineSessType(Integer topVoIPLineSessType) {
        this.topVoIPLineSessType = topVoIPLineSessType;
    }

    public Integer getTopVoIPLineState() {
        return topVoIPLineState;
    }

    public void setTopVoIPLineState(Integer topVoIPLineState) {
        this.topVoIPLineState = topVoIPLineState;
    }

    public Integer getTopGponSrvPotsInfoPotsIdx() {
        return topGponSrvPotsInfoPotsIdx;
    }

    public void setTopGponSrvPotsInfoPotsIdx(Integer topGponSrvPotsInfoPotsIdx) {
        this.topGponSrvPotsInfoPotsIdx = topGponSrvPotsInfoPotsIdx;
    }

    public String getTopGponSrvPotsInfoIpIdx() {
        return topGponSrvPotsInfoIpIdx;
    }

    public void setTopGponSrvPotsInfoIpIdx(String topGponSrvPotsInfoIpIdx) {
        this.topGponSrvPotsInfoIpIdx = topGponSrvPotsInfoIpIdx;
    }

    public Integer getTopOnuIfPotsAdminState() {
        return topOnuIfPotsAdminState;
    }

    public void setTopOnuIfPotsAdminState(Integer topOnuIfPotsAdminState) {
        this.topOnuIfPotsAdminState = topOnuIfPotsAdminState;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getTopSIPPstnUserForwardTime() {
        return topSIPPstnUserForwardTime;
    }

    public void setTopSIPPstnUserForwardTime(Integer topSIPPstnUserForwardTime) {
        this.topSIPPstnUserForwardTime = topSIPPstnUserForwardTime;
    }

    public Integer getOnuIpHostVlanTagPriority() {
        return onuIpHostVlanTagPriority;
    }

    public void setOnuIpHostVlanTagPriority(Integer onuIpHostVlanTagPriority) {
        this.onuIpHostVlanTagPriority = onuIpHostVlanTagPriority;
    }

    public Integer getOnuIpHostVlanPVid() {
        return onuIpHostVlanPVid;
    }

    public void setOnuIpHostVlanPVid(Integer onuIpHostVlanPVid) {
        this.onuIpHostVlanPVid = onuIpHostVlanPVid;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopGponOnuPots [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", topSIPPstnUserPotsIdx=");
        builder.append(topSIPPstnUserPotsIdx);
        builder.append(", topSIPPstnUserTelno=");
        builder.append(topSIPPstnUserTelno);
        builder.append(", topSIPPstnUserSipsrvId=");
        builder.append(topSIPPstnUserSipsrvId);
        builder.append(", topSIPPstnUserDigitmapId=");
        builder.append(topSIPPstnUserDigitmapId);
        builder.append(", topSIPPstnUserForwardType=");
        builder.append(topSIPPstnUserForwardType);
        builder.append(", topSIPPstnUserTransferNum=");
        builder.append(topSIPPstnUserTransferNum);
        builder.append(", topSIPPstnUserForwardTime=");
        builder.append(topSIPPstnUserForwardTime);
        builder.append(", topVoIPLineCodec=");
        builder.append(topVoIPLineCodec);
        builder.append(", topVoIPLineServStatus=");
        builder.append(topVoIPLineServStatus);
        builder.append(", topVoIPLineSessType=");
        builder.append(topVoIPLineSessType);
        builder.append(", topVoIPLineState=");
        builder.append(topVoIPLineState);
        builder.append(", topGponSrvPotsInfoPotsIdx=");
        builder.append(topGponSrvPotsInfoPotsIdx);
        builder.append(", topGponSrvPotsInfoIpIdx=");
        builder.append(topGponSrvPotsInfoIpIdx);
        builder.append(", topOnuIfPotsAdminState=");
        builder.append(topOnuIfPotsAdminState);
        builder.append(", onuIpHostVlanTagPriority=");
        builder.append(onuIpHostVlanTagPriority);
        builder.append(", onuIpHostVlanPVid=");
        builder.append(onuIpHostVlanPVid);
        builder.append("]");
        return builder.toString();
    }

}
