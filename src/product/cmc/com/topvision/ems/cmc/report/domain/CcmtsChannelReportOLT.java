package com.topvision.ems.cmc.report.domain;

import java.util.List;

/**
 * CCMTS设备信道使用情况报表，OLT层级，包含下属的PON列表
 * 
 * @author YangYi add
 * @created @2013-9-11
 * 
 */
public class CcmtsChannelReportOLT {
    private String oltName;// OLT名称
    private String oltIp;// OLT的IP
    private String oltId;// OLT的Id
    private int rowSpan;// 下辖的所有CCMTS数量，用于前台绘制和EXCEL生成
    private List<CcmtsChannelReportPON> ccmtsChannelReportPON;// 下辖的所有PON口列表

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public String getOltName() {
        return oltName;
    }

    public void setOltName(String oltName) {
        this.oltName = oltName;
    }

    public String getOltIp() {
        return oltIp;
    }

    public void setOltIp(String oltIp) {
        this.oltIp = oltIp;
    }

    public List<CcmtsChannelReportPON> getCcmtsChannelReportPON() {
        return ccmtsChannelReportPON;
    }

    public void setCcmtsChannelReportPON(List<CcmtsChannelReportPON> ccmtsChannelReportPON) {
        this.ccmtsChannelReportPON = ccmtsChannelReportPON;
    }

    public String getOltId() {
        return oltId;
    }

    public void setOltId(String oltId) {
        this.oltId = oltId;
    }

}
