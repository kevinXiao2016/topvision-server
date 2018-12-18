package com.topvision.ems.cmc.report.domain;

import java.util.List;

/**
 * CCMTS设备信道使用情况报表，OLT层级，包含下属的PON列表
 * 
 * @author YangYi add
 * @created @2013-9-11
 * 
 */
public class CcmtsChannelReportPON {
    private String ponId; // PON口Id
    private String ponIndex;// PON口Index
    private int rowSpan;// 下辖的所有CCMTS数量，用于前台绘制和EXCEL生成
    private List<CcmtsChannelReportCCMTS> ccmtsChannelReportCCMTS;// 下辖的所有CCMTS列表

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public String getPonId() {
        return ponId;
    }

    public void setPonId(String ponId) {
        this.ponId = ponId;
    }

    public List<CcmtsChannelReportCCMTS> getCcmtsChannelReportCCMTS() {
        return ccmtsChannelReportCCMTS;
    }

    public void setCcmtsChannelReportCCMTS(List<CcmtsChannelReportCCMTS> ccmtsChannelReportCCMTS) {
        this.ccmtsChannelReportCCMTS = ccmtsChannelReportCCMTS;
    }

    public String getPonIndex() {
        return ponIndex;
    }

    public void setPonIndex(String ponIndex) {
        this.ponIndex = ponIndex;
    }

}
