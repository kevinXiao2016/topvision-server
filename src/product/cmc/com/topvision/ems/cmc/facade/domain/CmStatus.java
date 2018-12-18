/***********************************************************************
 * $Id: CmStatus.java,v1.0 2012-2-1 下午01:40:09 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.cmc.cpe.domain.CmIfTable;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.facade.domain.IfTable;
import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Administrator
 * @created @2012-2-1-下午01:40:09
 * 
 */
public class CmStatus implements Serializable {
    private static final long serialVersionUID = -722529209955894122L;
    private boolean ping;
    private boolean snmpCheck;
    private boolean remoteQuery;
    private boolean otherCmts;
    private boolean versionSupport;
    private Long cmId;
    private String cmIp;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.1.0")
    private String sysDescr;
    @SnmpProperty(oid = "1.3.6.1.2.1.1.3.0")
    private Long sysUpTime;
    private String sysUpTimeToString;//系统运行时间，转换成 X天X小时X分X秒 格式(874700)
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.3.2.0")
    private String docsDevSwFilename;//软件名称
//    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.3.5.0")
//    private String docsDevSoftware;//软件版本
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.4.4.0")
    private String docsDevServerTftp;//TFTP服务器
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.4.2.0")
    private String docsDevServerDhcp;//DHCP服务器
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.4.3.0")
    private String docsDevServerTime;//时钟服务器
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.1.4.0")
    private String docsDevSerialNumber;//序列号
    @SnmpProperty(oid = "1.3.6.1.2.1.69.1.4.5.0")
    private String docsDevServerConfigFile;//配置文件名
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.5.0")
    private Integer docsIfDocsisBaseCapability;// 注册模式
    private String docsIfDocsisBaseCapabilityForUnit;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.21.2.0")
    private String docsIf3CmCapabilitiesRsp;// CM能力响应消息

    // 以下四个为兼容3.0CM而做
    // 下行射频信息列表，从cm上取
    private List<DocsIfDownstreamChannel> docsIfDownstreamChannelList;
    // 上行射频信息列表，从cm上取
    private List<DocsIfUpstreamChannel> docsIfUpstreamChannelList;
    // 下行信号质量，从cm上取
    private List<DocsIfSignalQuality> docsIfSignalQualityList;
    // 上行信号质量，从CC上取
    private List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList;
    // CM上接口信息
    private List<CmIfTable> cmIfTables;

    private final static String[] OPERMODETYPES = { "", "DOC1.0", "DOC1.1" };
    private final static String[] DOCSISTYPES = { "", "DOC1.0", "DOC1.1", "DOC2.0", "DOC3.0" };
    private final static String[] DOWNMODULATIONTYPES = { "", "unknown", "QAM1024", "QAM64", "QAM256" };
    
    // 广州需求，关联cm列表中加入下行SNR、下行接收电平、上行发送电平
    private String downChannelSnr;
    private String downChannelTx;
    private String upChannelTx;
    private Timestamp collectTime;
    private String lastRefreshTime; // 最后刷新时间
    private String downChannelFrequency; // 下行信道频率
    private String upChannelFrequency; // 上行信道频率
    private String cmHardWare = "--"; // 硬件版本
    private String cmSoftWare = "--"; // 软件版本
    private Long docsIfCmStatusResets = 0L;//CM重启次数

    private Boolean remoteQueryState = false;
    private Integer statusValue;
    
    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public String getCmIp() {
        return cmIp;
    }

    public void setCmIp(String cmIp) {
        this.cmIp = cmIp;
    }

    public Long getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(Long sysUpTime) {
        this.sysUpTime = sysUpTime;
    }

    public String getDocsDevSwFilename() {
        return docsDevSwFilename;
    }

    public void setDocsDevSwFilename(String docsDevSwFilename) {
        this.docsDevSwFilename = docsDevSwFilename;
    }

    public String getDocsDevServerTftp() {
        return docsDevServerTftp;
    }

    public void setDocsDevServerTftp(String docsDevServerTftp) {
        this.docsDevServerTftp = docsDevServerTftp;
    }

    public String getDocsDevServerDhcp() {
        return docsDevServerDhcp;
    }

    public void setDocsDevServerDhcp(String docsDevServerDhcp) {
        this.docsDevServerDhcp = docsDevServerDhcp;
    }

    public String getDocsDevServerTime() {
        return docsDevServerTime;
    }

    public void setDocsDevServerTime(String docsDevServerTime) {
        this.docsDevServerTime = docsDevServerTime;
    }

    public String getDocsDevSerialNumber() {
        return docsDevSerialNumber;
    }

    public void setDocsDevSerialNumber(String docsDevSerialNumber) {
        this.docsDevSerialNumber = docsDevSerialNumber;
    }

    public String getDocsDevServerConfigFile() {
        return docsDevServerConfigFile;
    }

    public void setDocsDevServerConfigFile(String docsDevServerConfigFile) {
        this.docsDevServerConfigFile = docsDevServerConfigFile;
    }

    public Long getDocsIfCmStatusResets() {
        return docsIfCmStatusResets;
    }

    public void setDocsIfCmStatusResets(Long docsIfCmStatusResets) {
        this.docsIfCmStatusResets = docsIfCmStatusResets;
    }

    public String getSysUpTimeToString() {
        if (sysUpTime != null && sysUpTime > 0) {
            sysUpTimeToString = CmcUtil.timeFormatToZh(sysUpTime / 100);
        }
        return sysUpTimeToString;
    }

    public void setSysUpTimeToString(String sysUpTimeToString) {
        this.sysUpTimeToString = sysUpTimeToString;
    }

    public static String[] getOpermodetypes() {
        return OPERMODETYPES;
    }

    public static String[] getDownmodulationtypes() {
        return DOWNMODULATIONTYPES;
    }

    public Integer getDocsIfDocsisBaseCapability() {
        return docsIfDocsisBaseCapability;
    }

    public void setDocsIfDocsisBaseCapability(Integer docsIfDocsisBaseCapability) {
        this.docsIfDocsisBaseCapability = docsIfDocsisBaseCapability;
    }

    public String getDocsIfDocsisBaseCapabilityForUnit() {
        if (docsIfDocsisBaseCapability != null && docsIfDocsisBaseCapability < DOCSISTYPES.length) {
            if (docsIfDocsisBaseCapability == 3) {//2.0cm
                docsIfDocsisBaseCapabilityForUnit = DOCSISTYPES[3];
            } else if (docsIfDocsisBaseCapability == 4) {//3.0cm
                if (getCmDocsisRegVersion(docsIf3CmCapabilitiesRsp) == 2) {//注册模式是2.0
                    docsIfDocsisBaseCapabilityForUnit = DOCSISTYPES[3];
                } else if (getCmDocsisRegVersion(docsIf3CmCapabilitiesRsp) == 3) {//注册模式是3.0
                    docsIfDocsisBaseCapabilityForUnit = DOCSISTYPES[4];
                }
            }
        }
        return docsIfDocsisBaseCapabilityForUnit;
    }

    public void setDocsIfDocsisBaseCapabilityForUnit(String docsIfDocsisBaseCapabilityForUnit) {
        this.docsIfDocsisBaseCapabilityForUnit = docsIfDocsisBaseCapabilityForUnit;
    }

    public List<DocsIfDownstreamChannel> getDocsIfDownstreamChannelList() {
        return docsIfDownstreamChannelList;
    }

    public void setDocsIfDownstreamChannelList(List<DocsIfDownstreamChannel> docsIfDownstreamChannelList) {
        this.docsIfDownstreamChannelList = docsIfDownstreamChannelList;
    }

    public List<DocsIfUpstreamChannel> getDocsIfUpstreamChannelList() {
        return docsIfUpstreamChannelList;
    }

    public void setDocsIfUpstreamChannelList(List<DocsIfUpstreamChannel> docsIfUpstreamChannelList) {
        this.docsIfUpstreamChannelList = docsIfUpstreamChannelList;
    }

    public List<DocsIfSignalQuality> getDocsIfSignalQualityList() {
        return docsIfSignalQualityList;
    }

    public void setDocsIfSignalQualityList(List<DocsIfSignalQuality> docsIfSignalQualityList) {
        this.docsIfSignalQualityList = docsIfSignalQualityList;
    }

    public List<DocsIf3CmtsCmUsStatus> getDocsIf3CmtsCmUsStatusList() {
        return docsIf3CmtsCmUsStatusList;
    }

    public void setDocsIf3CmtsCmUsStatusList(List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList) {
        this.docsIf3CmtsCmUsStatusList = docsIf3CmtsCmUsStatusList;
    }

    public boolean isPing() {
        return ping;
    }

    public void setPing(boolean ping) {
        this.ping = ping;
    }

    public String getDocsIf3CmCapabilitiesRsp() {
        return docsIf3CmCapabilitiesRsp;
    }

    public void setDocsIf3CmCapabilitiesRsp(String docsIf3CmCapabilitiesRsp) {
        this.docsIf3CmCapabilitiesRsp = docsIf3CmCapabilitiesRsp;
    }

    /**
     * 解析TLV
     * @param tlv 以冒号隔开的十六进制数“01:01:01”
     * @return
     *          Sting[0]:类型，String[1]值
     */
    public List<String[]> getValueFromTlv(String tlv) {
        List<String[]> list = new ArrayList<String[]>();
        String[] tlvArry = tlv.split(":");
        for (int i = 0; i < tlvArry.length;) {
            String[] s = new String[2];
            s[0] = tlvArry[i];
            int length = Integer.parseInt(tlvArry[i + 1], 16);
            StringBuilder sb = new StringBuilder();
            for (int j = i + 2; j < i + 2 + length; j++) {
                sb.append(tlvArry[j]).append(":");
            }
            s[1] = sb.substring(0, sb.length() - 1);
            list.add(s);
            i = i + 2 + length;
        }
        return list;
    }

    /**
     * 返回3.0CM的注册模式
     * @param tlv
     * @return
     *          -1：未找到注册模式的TLV，0：DOCSIS 1.0， 1：DOCSIS1.1
     *          2：DOCSIS 2.0， 3：DOCSIS 3.0，4-255保留位
     */
    public int getCmDocsisRegVersion(String tlv) {
        List<String[]> list = getValueFromTlv(tlv);
        //如果最外层不是只有一个TLV，并且该类型不是‘05’则返回异常数据
        if (list.size() != 1 || !list.get(0)[0].equals("05")) {
            return -1;
        }
        List<String[]> tlvList = getValueFromTlv(list.get(0)[1]);
        //查找DOCSIS 注册模式type = '02'
        for (int i = 0; i < tlvList.size(); i++) {
            if (tlvList.get(i)[0].equals("02")) {
                return Integer.parseInt(tlvList.get(i)[1]);
            }
        }
        return -1;
    }

	public boolean isSnmpCheck() {
        return snmpCheck;
    }

    public void setSnmpCheck(boolean snmpCheck) {
        this.snmpCheck = snmpCheck;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }
    
    public String getDownChannelSnr() {
        return downChannelSnr;
    }

    public void setDownChannelSnr(String downChannelSnr) {
        this.downChannelSnr = downChannelSnr;
    }

    public String getDownChannelTx() {
        return downChannelTx;
    }

    public void setDownChannelTx(String downChannelTx) {
        this.downChannelTx = downChannelTx;
    }

    public String getUpChannelTx() {
        return upChannelTx;
    }

    public void setUpChannelTx(String upChannelTx) {
        this.upChannelTx = upChannelTx;
    }

    public String getDownChannelFrequency() {
        return downChannelFrequency;
    }

    public void setDownChannelFrequency(String downChannelFrequency) {
        this.downChannelFrequency = downChannelFrequency;
    }

    public String getUpChannelFrequency() {
        return upChannelFrequency;
    }

    public void setUpChannelFrequency(String upChannelFrequency) {
        this.upChannelFrequency = upChannelFrequency;
    }

    public String getSysDescr() {
        return sysDescr;
    }

    public void setSysDescr(String sysDescr) {
        this.sysDescr = sysDescr;
    }

    public String getCmHardWare() {
        return cmHardWare;
    }

    public void setCmHardWare(String cmHardWare) {
        this.cmHardWare = cmHardWare;
    }

    public String getCmSoftWare() {
        return cmSoftWare;
    }

    public void setCmSoftWare(String cmSoftWare) {
        this.cmSoftWare = cmSoftWare;
    }

    public List<CmIfTable> getCmIfTables() {
        return cmIfTables;
    }

    public void setCmIfTables(List<CmIfTable> cmIfTables) {
        this.cmIfTables = cmIfTables;
    }

    public Boolean getRemoteQueryState() {
        return remoteQueryState;
    }

    public void setRemoteQueryState(Boolean remoteQueryState) {
        this.remoteQueryState = remoteQueryState;
    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
    }

    public boolean isRemoteQuery() {
        return remoteQuery;
    }

    public void setRemoteQuery(boolean remoteQuery) {
        this.remoteQuery = remoteQuery;
    }

    public boolean isVersionSupport() {
        return versionSupport;
    }

    public void setVersionSupport(boolean versionSupport) {
        this.versionSupport = versionSupport;
    }

    public boolean isOtherCmts() {
        return otherCmts;
    }

    public void setOtherCmts(boolean otherCmts) {
        this.otherCmts = otherCmts;
    }

    @Override
    public String toString() {
        return "CmStatus [ping=" + ping + ", snmpCheck=" + snmpCheck + ", cmId=" + cmId + ", cmIp=" + cmIp
                + ", sysUpTime=" + sysUpTime + ", sysUpTimeToString=" + sysUpTimeToString + ", docsDevSwFilename="
                + docsDevSwFilename  + ", docsDevServerTftp="
                + docsDevServerTftp + ", docsDevServerDhcp=" + docsDevServerDhcp + ", docsDevServerTime="
                + docsDevServerTime + ", docsDevSerialNumber=" + docsDevSerialNumber + ", docsDevServerConfigFile="
                + docsDevServerConfigFile + ", docsIfCmStatusResets=" + docsIfCmStatusResets
                + ", docsIfDocsisBaseCapability=" + docsIfDocsisBaseCapability + ", docsIfDocsisBaseCapabilityForUnit="
                + docsIfDocsisBaseCapabilityForUnit + ", docsIf3CmCapabilitiesRsp=" + docsIf3CmCapabilitiesRsp
                + ", docsIfDownstreamChannelList=" + docsIfDownstreamChannelList + ", docsIfUpstreamChannelList="
                + docsIfUpstreamChannelList + ", docsIfSignalQualityList=" + docsIfSignalQualityList
                + ", docsIf3CmtsCmUsStatusList=" + docsIf3CmtsCmUsStatusList + ", downChannelSnr=" + downChannelSnr
                + ", downChannelTx=" + downChannelTx + ", upChannelTx=" + upChannelTx + ", collectTime=" + collectTime
                + ", lastRefreshTime=" + lastRefreshTime + ", downChannelFrequency=" + downChannelFrequency
                + ", upChannelFrequency=" + upChannelFrequency + "]";
    }

}