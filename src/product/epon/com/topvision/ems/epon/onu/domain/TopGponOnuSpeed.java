package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * ONU上/下行速率
 * 
 * @author xiaoyue
 * @created @2017年6月10日-下午4:39:30
 *
 */
public class TopGponOnuSpeed implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7663893263308264755L;

    public static String SERVER_URL_1 = "http://dldir1.qq.com/qqfile/qq/QQ8.3/18038/QQ8.3.exe";
    public static String SERVER_URL_2 = "http://dldir1.qq.com/qqfile/qq/QQ8.7/19091/QQ8.7.exe";
    public static String SERVER_URL_3 = "http://down.360safe.com/setup.exe";
    public static String SERVER_URL_4 = "http://download.microsoft.com/download/0/A/F/0AFB5316-3062-494A-AB78-7FB0D4461357/windows6.1-KB976932-X64.exe";
    public static String SERVER_URL_5 = "http://codown.youdao.com/note/youdaonote_163index.exe";

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.1.3.1.1", index = true)
    private Integer topGponOnuSpeedTestCardIndex; // 槽位索引
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.1.3.1.2", index = true)
    private Integer topGponOnuSpeedTestPonIndex; // 端口索引
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.1.3.1.3", index = true)
    private Integer topGponOnuSpeedTestOnuIndex; // onu索引
    private Long onuIndex;// 网管索引
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.1.3.1.4", writable = true, type = "Integer32")
    private Integer topGponOnuSpeedTestAction; // 测速激活
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.1.3.1.5", writable = true, type = "OctetString")
    private String topGponOnuSpeedTestUrl; // 测试URL
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.1.3.1.6", type = "OctetString")
    private String topGponOnuSpeedTestTimeStamp; // 测速结果时间戳
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.1.3.1.7", type = "Integer32")
    private Integer topGponOnuSpeedTestDownRate; // 下行速率kbps
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.1.3.1.8", type = "Integer32")
    private Integer topGponOnuSpeedTestUpRate; // 上行速率kbps
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.5.1.3.1.9", type = "Integer32")
    private Integer topGponOnuSpeedTestState;// 测试结果状态

    public Integer getTopGponOnuSpeedTestCardIndex() {
        return topGponOnuSpeedTestCardIndex;
    }

    public void setTopGponOnuSpeedTestCardIndex(Integer topGponOnuSpeedTestCardIndex) {
        this.topGponOnuSpeedTestCardIndex = topGponOnuSpeedTestCardIndex;
    }

    public Integer getTopGponOnuSpeedTestPonIndex() {
        return topGponOnuSpeedTestPonIndex;
    }

    public void setTopGponOnuSpeedTestPonIndex(Integer topGponOnuSpeedTestPonIndex) {
        this.topGponOnuSpeedTestPonIndex = topGponOnuSpeedTestPonIndex;
    }

    public Integer getTopGponOnuSpeedTestOnuIndex() {
        return topGponOnuSpeedTestOnuIndex;
    }

    public void setTopGponOnuSpeedTestOnuIndex(Integer topGponOnuSpeedTestOnuIndex) {
        this.topGponOnuSpeedTestOnuIndex = topGponOnuSpeedTestOnuIndex;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        this.topGponOnuSpeedTestOnuIndex = EponIndex.getOnuNo(onuIndex).intValue();
        this.topGponOnuSpeedTestCardIndex = EponIndex.getSlotNo(onuIndex).intValue();
        this.topGponOnuSpeedTestPonIndex = EponIndex.getPonNo(onuIndex).intValue();
    }

    public Integer getTopGponOnuSpeedTestAction() {
        return topGponOnuSpeedTestAction;
    }

    public void setTopGponOnuSpeedTestAction(Integer topGponOnuSpeedTestAction) {
        this.topGponOnuSpeedTestAction = topGponOnuSpeedTestAction;
    }

    public String getTopGponOnuSpeedTestUrl() {
        return topGponOnuSpeedTestUrl;
    }

    public void setTopGponOnuSpeedTestUrl(String topGponOnuSpeedTestUrl) {
        this.topGponOnuSpeedTestUrl = topGponOnuSpeedTestUrl;
    }

    public String getTopGponOnuSpeedTestTimeStamp() {
        return topGponOnuSpeedTestTimeStamp;
    }

    public void setTopGponOnuSpeedTestTimeStamp(String topGponOnuSpeedTestTimeStamp) {
        this.topGponOnuSpeedTestTimeStamp = topGponOnuSpeedTestTimeStamp;
    }

    public Integer getTopGponOnuSpeedTestDownRate() {
        return topGponOnuSpeedTestDownRate;
    }

    public void setTopGponOnuSpeedTestDownRate(Integer topGponOnuSpeedTestDownRate) {
        this.topGponOnuSpeedTestDownRate = topGponOnuSpeedTestDownRate;
    }

    public Integer getTopGponOnuSpeedTestUpRate() {
        return topGponOnuSpeedTestUpRate;
    }

    public void setTopGponOnuSpeedTestUpRate(Integer topGponOnuSpeedTestUpRate) {
        this.topGponOnuSpeedTestUpRate = topGponOnuSpeedTestUpRate;
    }

    public Integer getTopGponOnuSpeedTestState() {
        return topGponOnuSpeedTestState;
    }

    public void setTopGponOnuSpeedTestState(Integer topGponOnuSpeedTestState) {
        this.topGponOnuSpeedTestState = topGponOnuSpeedTestState;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopGponOnuSpeed [topGponOnuSpeedTestCardIndex=");
        builder.append(topGponOnuSpeedTestCardIndex);
        builder.append(", topGponOnuSpeedTestPonIndex=");
        builder.append(topGponOnuSpeedTestPonIndex);
        builder.append(", topGponOnuSpeedTestOnuIndex=");
        builder.append(topGponOnuSpeedTestOnuIndex);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", topGponOnuSpeedTestAction=");
        builder.append(topGponOnuSpeedTestAction);
        builder.append(", topGponOnuSpeedTestUrl=");
        builder.append(topGponOnuSpeedTestUrl);
        builder.append(", topGponOnuSpeedTestTimeStamp=");
        builder.append(topGponOnuSpeedTestTimeStamp);
        builder.append(", topGponOnuSpeedTestDownRate=");
        builder.append(topGponOnuSpeedTestDownRate);
        builder.append(", topGponOnuSpeedTestUpRate=");
        builder.append(topGponOnuSpeedTestUpRate);
        builder.append(", topGponOnuSpeedTestState=");
        builder.append(topGponOnuSpeedTestState);
        builder.append("]");
        return builder.toString();
    }

}
