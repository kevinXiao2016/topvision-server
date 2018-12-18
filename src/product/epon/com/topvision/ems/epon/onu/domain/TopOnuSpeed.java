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
public class TopOnuSpeed implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7663893263308264755L;

    public static String SERVER_URL_1 = "http://dldir1.qq.com/qqfile/qq/QQ8.3/18038/QQ8.3.exe";
    public static String SERVER_URL_2 = "http://dldir1.qq.com/qqfile/qq/QQ8.7/19091/QQ8.7.exe";
    public static String SERVER_URL_3 = "http://down.360safe.com/setup.exe";
    public static String SERVER_URL_4 = "http://download.microsoft.com/download/0/A/F/0AFB5316-3062-494A-AB78-7FB0D4461357/windows6.1-KB976932-X64.exe";
    public static String SERVER_URL_5 = "http://codown.youdao.com/note/youdaonote_163index.exe";

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.19.1.1", index = true)
    private Integer topOnuSpeedTestCardIndex; // 槽位索引
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.19.1.2", index = true)
    private Integer topOnuSpeedTestPonIndex; // 端口索引
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.19.1.3", index = true)
    private Integer topOnuSpeedTestOnuIndex; // onu索引
    private Long onuIndex;// 网管用onuIndex
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.19.1.4", writable = true, type = "Integer32")
    private Integer topOnuSpeedTestAction; // 测速激活
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.19.1.5", writable = true, type = "OctetString")
    private String topOnuSpeedTestUrl; // 测试URL
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.19.1.6", type = "OctetString")
    private String topOnuSpeedTestTimeStamp; // 测速结果时间戳
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.19.1.7", type = "Integer32")
    private Integer topOnuSpeedTestDownRate; // 下行速率kbps
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.19.1.8", type = "Integer32")
    private Integer topOnuSpeedTestUpRate; // 上行速率kbps
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.19.1.9", type = "Integer32")
    private Integer topOnuSpeedTestState;// 测试结果状态

    public Integer getTopOnuSpeedTestCardIndex() {
        return topOnuSpeedTestCardIndex;
    }

    public void setTopOnuSpeedTestCardIndex(Integer topOnuSpeedTestCardIndex) {
        this.topOnuSpeedTestCardIndex = topOnuSpeedTestCardIndex;
    }

    public Integer getTopOnuSpeedTestPonIndex() {
        return topOnuSpeedTestPonIndex;
    }

    public void setTopOnuSpeedTestPonIndex(Integer topOnuSpeedTestPonIndex) {
        this.topOnuSpeedTestPonIndex = topOnuSpeedTestPonIndex;
    }

    public Integer getTopOnuSpeedTestOnuIndex() {
        return topOnuSpeedTestOnuIndex;
    }

    public void setTopOnuSpeedTestOnuIndex(Integer topOnuSpeedTestOnuIndex) {
        this.topOnuSpeedTestOnuIndex = topOnuSpeedTestOnuIndex;
    }

    public Integer getTopOnuSpeedTestAction() {
        return topOnuSpeedTestAction;
    }

    public void setTopOnuSpeedTestAction(Integer topOnuSpeedTestAction) {
        this.topOnuSpeedTestAction = topOnuSpeedTestAction;
    }

    public String getTopOnuSpeedTestUrl() {
        return topOnuSpeedTestUrl;
    }

    public void setTopOnuSpeedTestUrl(String topOnuSpeedTestUrl) {
        this.topOnuSpeedTestUrl = topOnuSpeedTestUrl;
    }

    public String getTopOnuSpeedTestTimeStamp() {
        return topOnuSpeedTestTimeStamp;
    }

    public void setTopOnuSpeedTestTimeStamp(String topOnuSpeedTestTimeStamp) {
        this.topOnuSpeedTestTimeStamp = topOnuSpeedTestTimeStamp;
    }

    public Integer getTopOnuSpeedTestDownRate() {
        return topOnuSpeedTestDownRate;
    }

    public void setTopOnuSpeedTestDownRate(Integer topOnuSpeedTestDownRate) {
        this.topOnuSpeedTestDownRate = topOnuSpeedTestDownRate;
    }

    public Integer getTopOnuSpeedTestUpRate() {
        return topOnuSpeedTestUpRate;
    }

    public void setTopOnuSpeedTestUpRate(Integer topOnuSpeedTestUpRate) {
        this.topOnuSpeedTestUpRate = topOnuSpeedTestUpRate;
    }

    public Integer getTopOnuSpeedTestState() {
        return topOnuSpeedTestState;
    }

    public void setTopOnuSpeedTestState(Integer topOnuSpeedTestState) {
        this.topOnuSpeedTestState = topOnuSpeedTestState;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        this.topOnuSpeedTestOnuIndex = EponIndex.getOnuNo(onuIndex).intValue();
        this.topOnuSpeedTestCardIndex = EponIndex.getSlotNo(onuIndex).intValue();
        this.topOnuSpeedTestPonIndex = EponIndex.getPonNo(onuIndex).intValue();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopOnuSpeed [topOnuSpeedTestCardIndex=");
        builder.append(topOnuSpeedTestCardIndex);
        builder.append(", topOnuSpeedTestPonIndex=");
        builder.append(topOnuSpeedTestPonIndex);
        builder.append(", topOnuSpeedTestOnuIndex=");
        builder.append(topOnuSpeedTestOnuIndex);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", topOnuSpeedTestAction=");
        builder.append(topOnuSpeedTestAction);
        builder.append(", topOnuSpeedTestUrl=");
        builder.append(topOnuSpeedTestUrl);
        builder.append(", topOnuSpeedTestTimeStamp=");
        builder.append(topOnuSpeedTestTimeStamp);
        builder.append(", topOnuSpeedTestDownRate=");
        builder.append(topOnuSpeedTestDownRate);
        builder.append(", topOnuSpeedTestUpRate=");
        builder.append(topOnuSpeedTestUpRate);
        builder.append(", topOnuSpeedTestState=");
        builder.append(topOnuSpeedTestState);
        builder.append("]");
        return builder.toString();
    }

}
