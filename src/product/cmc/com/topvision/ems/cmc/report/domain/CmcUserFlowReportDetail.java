/***********************************************************************
 * $Id: CmcUserFlowReportDetail.java,v1.0 2013-6-8 上午10:49:02 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import java.text.NumberFormat;
import java.util.Arrays;

import com.topvision.framework.utils.CmcIndexUtils;

/**
 * @author Bravin
 * @created @2013-6-8-上午10:49:02
 * 
 */
public class CmcUserFlowReportDetail {
    private Long ponIndex;
    private String ponDisplayName;
    private Double ponMaxSend;
    private String ponMaxSendString;
    private String ccmtsLocation;
    private String ccmtsLocationDisplay;
    private Integer cmNumTotal;
    private Long cmcIndex;
    private Long cmcId;
    private String name;
    private String typeName;
    private CcmtsChannelSnrAvg[] snrAvgs;
    private static final int MAX_UPCHANNEL_NUM = 4;
    private static final NumberFormat formater = NumberFormat.getInstance();

    // 广州新增需求
    private Double ccmtsMaxSend;
    private String ccmtsMaxSendString;
    private Integer interactiveNum;// 互动CM数
    private Integer broadbandNum;// 宽带CM数
    private Integer cpeInteractiveNum;// 互动CPE数
    private Integer cpeBroadbandNum;// 宽带CPE数
    private Integer cpeNumTotal;// CPE总数

    static {
        // 设置小数点后面最多2位
        formater.setMaximumFractionDigits(2);
    }

    /**
     * 在指定位置添加对应信道的SNR均值
     * 
     * @param channelNo
     * @param avg
     */
    public void addChannelAvg(int channelNo, CcmtsChannelSnrAvg avg) {
        snrAvgs[channelNo] = avg;
    }

    public CmcUserFlowReportDetail() {
        snrAvgs = new CcmtsChannelSnrAvg[MAX_UPCHANNEL_NUM];
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }

    public String getPonDisplayName() {
        return CmcIndexUtils.getEponPortFromIndex(this.ponIndex);
    }

    public void setPonDisplayName(String ponDisplayName) {
        this.ponDisplayName = ponDisplayName;
    }

    public Double getPonMaxSend() {
        return ponMaxSend;
    }

    public void setPonMaxSend(Double ponMaxSend) {
        this.ponMaxSend = ponMaxSend;
    }

    public String getPonMaxSendString() {
        if (this.ponMaxSend == null) {
            return "-";
        } else {
            return formater.format(this.ponMaxSend);
        }
    }

    public void setPonMaxSendString(String ponMaxSendString) {
        this.ponMaxSendString = ponMaxSendString;
    }

    public Double getCcmtsMaxSend() {
        return ccmtsMaxSend;
    }

    public void setCcmtsMaxSend(Double ccmtsMaxSend) {
        this.ccmtsMaxSend = ccmtsMaxSend;
        if (ccmtsMaxSend != null) {
            ccmtsMaxSendString = formater.format(ccmtsMaxSend);
        } else {
            ccmtsMaxSendString = "-";
        }
    }

    public String getCcmtsMaxSendString() {
        return ccmtsMaxSendString;
    }

    public void setCcmtsMaxSendString(String ccmtsMaxSendString) {
        this.ccmtsMaxSendString = ccmtsMaxSendString;
    }

    public String getCcmtsLocation() {
        return ccmtsLocation;
    }

    public void setCcmtsLocation(String ccmtsLocation) {
        this.ccmtsLocation = ccmtsLocation;
    }

    public Integer getCmNumTotal() {
        if (cmNumTotal == null) {
            return 0;
        }
        return cmNumTotal;
    }

    public void setCmNumTotal(Integer cmNumTotal) {
        this.cmNumTotal = cmNumTotal;
    }

    public CcmtsChannelSnrAvg[] getSnrAvgs() {
        return snrAvgs;
    }

    public void setSnrAvgs(CcmtsChannelSnrAvg[] snrAvgs) {
        this.snrAvgs = snrAvgs;
    }

    public String getCcmtsLocationDisplay() {
        return CmcIndexUtils.getMarkFromIndex(this.cmcIndex) + " " + ccmtsLocation;
    }

    public void setCcmtsLocationDisplay(String ccmtsLocationDisplay) {
        this.ccmtsLocationDisplay = ccmtsLocationDisplay;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInteractiveNum() {
        return (null == interactiveNum) ? 0 : interactiveNum;
    }

    public void setInteractiveNum(Integer interactiveNum) {
        if (null == interactiveNum) {
            this.interactiveNum = 0;
        } else {
            this.interactiveNum = interactiveNum;
        }
    }

    public Integer getBroadbandNum() {
        return (null == broadbandNum) ? 0 : broadbandNum;
    }

    public void setBroadbandNum(Integer broadbandNum) {
        if (null == broadbandNum) {
            this.broadbandNum = 0;
        } else {
            this.broadbandNum = broadbandNum;
        }
    }

    public Integer getCpeInteractiveNum() {
        return (null == cpeInteractiveNum) ? 0 : cpeInteractiveNum;
    }

    public void setCpeInteractiveNum(Integer cpeInteractiveNum) {
        if (null == cpeInteractiveNum) {
            this.cpeInteractiveNum = 0;
        } else {
            this.cpeInteractiveNum = cpeInteractiveNum;
        }
    }

    public Integer getCpeBroadbandNum() {
        return (null == cpeBroadbandNum) ? 0 : cpeBroadbandNum;
    }

    public void setCpeBroadbandNum(Integer cpeBroadbandNum) {
        if (null == cpeBroadbandNum) {
            this.cpeBroadbandNum = 0;
        } else {
            this.cpeBroadbandNum = cpeBroadbandNum;
        }
    }

    public Integer getCpeNumTotal() {
        return (null == cpeNumTotal) ? 0 : cpeNumTotal;
    }

    public void setCpeNumTotal(Integer cpeNumTotal) {
        if (null == cpeNumTotal) {
            this.cpeNumTotal = 0;
        } else {
            this.cpeNumTotal = cpeNumTotal;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcUserFlowReportDetail [ponIndex=");
        builder.append(ponIndex);
        builder.append(", ponDisplayName=");
        builder.append(ponDisplayName);
        builder.append(", ponMaxSend=");
        builder.append(ponMaxSend);
        builder.append(", ponMaxSendString=");
        builder.append(ponMaxSendString);
        builder.append(", ccmtsLocation=");
        builder.append(ccmtsLocation);
        builder.append(", ccmtsLocationDisplay=");
        builder.append(ccmtsLocationDisplay);
        builder.append(", cmNumTotal=");
        builder.append(cmNumTotal);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", name=");
        builder.append(name);
        builder.append(", snrAvgs=");
        builder.append(Arrays.toString(snrAvgs));
        builder.append(", ccmtsMaxSend=");
        builder.append(ccmtsMaxSend);
        builder.append(", ccmtsMaxSendString=");
        builder.append(ccmtsMaxSendString);
        builder.append(", interactiveNum=");
        builder.append(interactiveNum);
        builder.append(", broadbandNum=");
        builder.append(broadbandNum);
        builder.append(", cpeInteractiveNum=");
        builder.append(cpeInteractiveNum);
        builder.append(", cpeBroadbandNum=");
        builder.append(cpeBroadbandNum);
        builder.append(", cpeNumTotal=");
        builder.append(cpeNumTotal);
        builder.append("]");
        return builder.toString();
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
