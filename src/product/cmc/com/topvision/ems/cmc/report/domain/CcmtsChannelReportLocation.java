package com.topvision.ems.cmc.report.domain;

import java.util.List;

/**
 * CCMTS设备信道使用情况报表，地域层级，包含下属的OLT列表
 * 
 * @author YangYi add
 * @created @2013-9-11
 * 
 */
public class CcmtsChannelReportLocation {
    private String locationId; // 地域ID
    private String locationName; // 地域名称
    private int rowSpan; // 下辖的所有CCMTS数量，用于前台绘制和EXCEL生成
    private List<CcmtsChannelReportOLT> ccmtsChannelReportOLT; // 下辖的所有OLT列表

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public List<CcmtsChannelReportOLT> getCcmtsChannelReportOLT() {
        return ccmtsChannelReportOLT;
    }

    public void setCcmtsChannelReportOLT(List<CcmtsChannelReportOLT> ccmtsChannelReportOLT) {
        this.ccmtsChannelReportOLT = ccmtsChannelReportOLT;
    }

}
