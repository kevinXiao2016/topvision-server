/***********************************************************************
 * $Id: CmAttribute.java,v1.0 2011-10-26 下午04:38:38 $
 *
 * @author: xionghao
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.topvision.ems.cmc.cm.domain.Cm3Signal;
import com.topvision.ems.cmc.cm.domain.CmSignal;
import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author xionghao
 * @created @2011-10-26-下午04:38:38
 */
@Alias("cmAttribute")
public class CmAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -830134461940934671L;
    private Long entityId;
    private Long cmId;
    // 离线状态
    public static final Integer OFFLINESTATUS = 1;// offline
    // EAE阶段
    public static final Integer INIT_R2 = 2;// init(r2)
    // 测距状态
    public static final Integer EXPIRE_EPK = 28;// expire(epk)
    public static final Integer EXPIRE_EPT = 29;// expire(ept)
    // DHCP阶段
    public static final Integer INIT_D = 10;// init(d)
    public static final Integer INIT6_S = 22;// init6(s)
    public static final Integer INIT_IO = 11;// init(io)
    public static final Integer INIT6_A = 23;// init6(a)
    public static final Integer INIT_DR = 12;// init(dr)
    public static final Integer INIT6_R = 24;// init6(r)
    public static final Integer INIT_I = 5;// init(i)
    public static final Integer INIT6_I = 25;// init6(i)
    // reject
    public static final Integer REJECT_EPK = 32;// reject(epk)
    public static final Integer REJECT_EPT = 33;// reject(ept)
    public static final Integer REJECT_PK = 50;// reject(pk)
    public static final Integer REJECT_PT = 51;// reject(pt)
    public static final Integer REJECT_PKD = 52;// reject(pkd)
    public static final Integer REJECT_PTD = 53;// reject(ptd)
    // 无BPI+在线状态
    public static final Integer ONLINESTATUS = 6;// online
    public static final Integer ONLINE_D = 21;// online(d)
    public static final Integer P_ONLINE = 26;// p-online
    public static final Integer W_ONLINE = 27;// w-online
    public static final Integer P_ONLINE_D = 30;// p-online(d)
    public static final Integer W_ONLINE_D = 31;// w-online(d)
    // BPI+在线状态
    public static final Integer ONLINE_PK = 34;// online(pk)
    public static final Integer ONLINE_PT = 35;// online(pt)
    public static final Integer ONLINE_PKD = 36;// online(pkd)
    public static final Integer ONLINE_PTD = 37;// online(ptd)
    public static final Integer W_ONLINE_PK = 38;// w-online(pk)
    public static final Integer W_ONLINE_PT = 39;// w-online(pt)
    public static final Integer W_ONLINE_PKD = 40;// w-online(pkd)
    public static final Integer W_ONLINE_PTD = 41;// w-online(ptd)
    public static final Integer P_ONLINE_PK = 42;// p-online(pk)
    public static final Integer P_ONLINE_PT = 43;// p-online(pt)
    public static final Integer P_ONLINE_PKD = 44;// p-online(pkd)
    public static final Integer P_ONLINE_PTD = 45;// p-online(ptd)
    // expire在线状态
    public static final Integer EXPIRE_PK = 46;// expire(pk)
    public static final Integer EXPIRE_PT = 47;// expire(pt)
    public static final Integer EXPIRE_PKD = 48;// expire(pkd)
    public static final Integer EXPIRE_PTD = 49;// expire(ptd)

    // CM状态mib获取值与命令行字符串对应关系
    public static final Map<Integer, String> CM_STATUS_VALUE = new HashMap<Integer, String>();
    static {
        CM_STATUS_VALUE.put(OFFLINESTATUS, "offline");
        
        CM_STATUS_VALUE.put(INIT_R2, "init(r2)");
        
        CM_STATUS_VALUE.put(EXPIRE_EPK, "expire(epk)");
        CM_STATUS_VALUE.put(EXPIRE_EPT, "expire(ept)");
        
        CM_STATUS_VALUE.put(INIT_D, "init(d)");
        CM_STATUS_VALUE.put(INIT6_S, "init6(s)");
        CM_STATUS_VALUE.put(INIT_IO, "init(io)");
        CM_STATUS_VALUE.put(INIT6_A, "init6(a)");
        CM_STATUS_VALUE.put(INIT_DR, "init(dr)");
        CM_STATUS_VALUE.put(INIT6_R, "init6(r)");
        CM_STATUS_VALUE.put(INIT_I, "init(i)");
        CM_STATUS_VALUE.put(INIT6_I, "init6(i)");
        
        CM_STATUS_VALUE.put(REJECT_EPK, "reject(epk)");
        CM_STATUS_VALUE.put(REJECT_EPT, "reject(ept)");
        CM_STATUS_VALUE.put(REJECT_PK, "reject(pk)");
        CM_STATUS_VALUE.put(REJECT_PT, "reject(pt)");
        CM_STATUS_VALUE.put(REJECT_PKD, "reject(pkd)");
        CM_STATUS_VALUE.put(REJECT_PTD, "reject(ptd)");
        
        CM_STATUS_VALUE.put(ONLINESTATUS, "online");
        CM_STATUS_VALUE.put(ONLINE_D, "online(d)");
        CM_STATUS_VALUE.put(P_ONLINE, "p-online");
        CM_STATUS_VALUE.put(W_ONLINE, "w-online");
        CM_STATUS_VALUE.put(P_ONLINE_D, "p-online(d)");
        CM_STATUS_VALUE.put(W_ONLINE_D, "w-online(d)");
        
        CM_STATUS_VALUE.put(ONLINE_PK, "online(pk)");
        CM_STATUS_VALUE.put(ONLINE_PT, "online(pt)");
        CM_STATUS_VALUE.put(ONLINE_PKD, "online(pkd)");
        CM_STATUS_VALUE.put(ONLINE_PTD, "online(ptd)");
        CM_STATUS_VALUE.put(W_ONLINE_PK, "w-online(pk)");
        CM_STATUS_VALUE.put(W_ONLINE_PT, "w-online(pt)");
        CM_STATUS_VALUE.put(W_ONLINE_PKD, "w-online(pkd)");
        CM_STATUS_VALUE.put(W_ONLINE_PTD, "w-online(ptd)");
        CM_STATUS_VALUE.put(P_ONLINE_PK, "p-online(pk)");
        CM_STATUS_VALUE.put(P_ONLINE_PT, "p-online(pt)");
        CM_STATUS_VALUE.put(P_ONLINE_PKD, "p-online(pkd)");
        CM_STATUS_VALUE.put(P_ONLINE_PTD, "p-online(ptd)");
        
        CM_STATUS_VALUE.put(EXPIRE_PK, "expire(pk)");
        CM_STATUS_VALUE.put(EXPIRE_PT, "expire(pt)");
        CM_STATUS_VALUE.put(EXPIRE_PKD, "expire(pkd)");
        CM_STATUS_VALUE.put(EXPIRE_PTD, "expire(ptd)");
    }
    
    // 所有CM在线状态，包含pre3.0，3.0,3.1
    public static List<Integer> CM_ONLINE_STATUS = new ArrayList<Integer>();
    static {
        CM_ONLINE_STATUS.add(ONLINESTATUS);
        CM_ONLINE_STATUS.add(ONLINE_D);
        CM_ONLINE_STATUS.add(P_ONLINE);
        CM_ONLINE_STATUS.add(W_ONLINE);
        CM_ONLINE_STATUS.add(P_ONLINE_D);
        CM_ONLINE_STATUS.add(W_ONLINE_D);
        CM_ONLINE_STATUS.add(ONLINE_PK);
        CM_ONLINE_STATUS.add(ONLINE_PT);
        CM_ONLINE_STATUS.add(ONLINE_PKD);
        CM_ONLINE_STATUS.add(ONLINE_PTD);
        CM_ONLINE_STATUS.add(W_ONLINE_PK);
        CM_ONLINE_STATUS.add(W_ONLINE_PT);
        CM_ONLINE_STATUS.add(W_ONLINE_PKD);
        CM_ONLINE_STATUS.add(W_ONLINE_PTD);
        CM_ONLINE_STATUS.add(P_ONLINE_PK);
        CM_ONLINE_STATUS.add(P_ONLINE_PT);
        CM_ONLINE_STATUS.add(P_ONLINE_PKD);
        CM_ONLINE_STATUS.add(P_ONLINE_PTD);
        CM_ONLINE_STATUS.add(EXPIRE_PK);
        CM_ONLINE_STATUS.add(EXPIRE_PT);
        CM_ONLINE_STATUS.add(EXPIRE_PKD);
        CM_ONLINE_STATUS.add(EXPIRE_PTD);
    }
    
    public static final String[] cmtsCmStatusDocsisRegMode = { "", "DOC1.1", "DOC1.1" };
    public static final String[] cmtsCmStatusModulationType = { "UNKNOWN", "TDMA", "ATDMA", "SCDMA", "tdmaAndAtdma" };
    public static final String[] cmtsCmStatusInetAddressType = { "UNKNOWN", "ipv4", "ipv6", "ipv4z", "ipv6z", "", "",
            "", "", "", "", "", "", "", "", "", "dns" };
    private String docsIfCmtsCmStatusValueString;
    private String docsIfCmtsCmStatusDocsisRegModeString;
    private String docsIfCmtsCmStatusModulationTypeString;
    private String docsIfCmtsCmStatusInetAddressTypeString;
    private String docsIfCmtsCmStatusRxPowerString;
    private String docsIfCmtsCmStatusMicroreflectionsString;
    private String docsIfCmtsCmStatusSignalNoiseString;
    private String docsIfCmtsCmStatusValueLastUpdateString;
    private String docsIfCmtsCmStatusUnerroredsString;
    private String docsIfCmtsCmStatusCorrectedsString;
    private String docsIfCmtsCmStatusUncorrectablesString;
    private String docsIfCmtsCmStatusExtUnerroredsString;
    private String docsIfCmtsCmStatusExtCorrectedsString;
    private String docsIfCmtsCmStatusExtUncorrectablesString;
    private String cmAlias;
    private String cmClassified;
    private Long cmcId;
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.1", index = true)
    private Long statusIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.2")
    private String statusMacAddress; // MAC地址
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.3")
    private String statusIpAddress; // IP地址
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.4")
    private Long statusDownChannelIfIndex; // 下行通道索引
    private Long downChannelId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.5")
    private Long statusUpChannelIfIndex; // 上行通道索引
    private Long upChannelId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.6")
    private Long statusRxPower; // CMTS侧接收电平
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.7")
    private Long statusTimingOffset; // 时间偏移
    // @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.8")
    private String statusEqualizationData;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.9")
    private Integer statusValue; // 当前状态
    // 1: other
    // 2: ranging 测距
    // 3: rangingAborted 测距失败
    // 4: rangingComplete 测距完成
    // 5: ipComplete 获取IP成功
    // 6: registrationComplete 注册成功，上线
    // 7: accessDenied 权限拒绝
    // 8: operational
    // 9: registeredBPIInitializing
    private String statusVlaueString;
    // @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.10")
    private Long statusUnerroreds = 0L; // 无错字码数
    // @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.11")
    private Long statusCorrecteds = 0L; // 可纠错字码数
    // @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.12")
    private Long statusUncorrectables = 0L; // 不可纠错字码数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.13")
    private Long statusSignalNoise; // CMTS侧信噪比
    // @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.14")
    private Long statusMicroreflections; // 反射值
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.15")
    private Long statusExtUnerroreds = 0L; // 64位无错字码数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.16")
    private Long statusExtCorrecteds = 0L; // 64位可纠错字码数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.17")
    private Long statusExtUncorrectables = 0L; // 64位不可纠错字码数
    // @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.18")
    private Long statusDocsisRegMode; // 注册模式 1:DOC1.0 2:DOC1.1
    // @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.19")
    private Long statusModulationType; // 调制类型 0:unknown 1:tdma
    // 2:atdma 3:scdma
    // 4:tdmaAndAtdma
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.20")
    private Long statusInetAddressType; // 因特网地址类型 0:unknown 1:ipv4
    // 2:ipv6 3:ipv4z
    // 4:ipv6z 5:dns
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.21")
    private String statusInetAddress; // 因特网地址
    private String statusInetAddressIpString; // 因特网地址
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.22")
    private Long statusValueLastUpdate; // 状态更新时间
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.23")
    private Long statusHighResolutionTO; // 时间偏移
    private String cmcAlias;
    private Integer topCmFlapInsertionFailNum;
    private List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList;

    private String docsIfSigQSignalNoiseForUnit;// primary下行信道的信噪值

    // 广州需求，关联cm列表中加入下行SNR、下行接收电平、上行发送电平
    private String downChannelSnr; // 下行SNR
    private String downChannelTx; // 下行接收电平
    private String upChannelTx;// 上行发送电平
    private Timestamp collectTime;
    private String lastRefreshTime; // 最后刷新时间
    private String downChannelFrequency; // 下行信道频率
    private String upChannelFrequency; // 上行信道频率

    private String partialUpChannels;
    private String partialDownChannels;

    private CmSignal cmSignal;
    private List<Cm3Signal> upChannelCm3Signal;
    private List<Cm3Signal> downChannelCm3Signal;

    // 用于存储cmts索引解析结果 modify by loyal
    private String upChannelIndexString;
    private String downChannelIndexString;
    private String upChannelSnr;// YangYi添加@20130824,上行信道SNR

    private String upChannelIfDescr;
    private String downChannelIfDescr;

    // 用于显示所连CC/CMTS的别名
    private String cmcName;
    private String cmcIp;
    // 用于表示所连CC/CMTS的类型
    private Long cmcDeviceStyle;
    // 用于显示连在8800A下的上联OLT的ID
    private Long oltId;
    // 用于显示连在8800A下的上联OLT的对应PON口的ID
    private Long ponId;
    // 判断是否支持大客户IP显示
    private Boolean supportStaticIp;
    // 判断是否支持CPE信息查看
    private Boolean supportCpeInfo;
    // 判断是否支持重启
    private Boolean supportReset;
    // 判断是否支持CM批量升级
    private Boolean supportCmUpgrade;
    // @flackyang 20140917添加,解决字段别名导致的排序问题
    private String name;
    // 判断是否重启
    private Integer cpeNum = 0;
    // 是否支持单个离线cm清除
    private Boolean supportClearSingleCm;

    private Integer displayStatus;
    private String displayIp;

    // @flackyang 20141106添加,用作电平展示
    private Double downChannelRecvPower;
    private Double upChannelTransPower;

    // cm boss 信息
    private String userId;
    private String userName;
    private String userAddr;
    private String userPhoneNo;
    private String offerName;
    private String effDate;
    private String expDate;
    private String configFile;
    private String extension;

    private Timestamp cmExpDate;// add by loyal for neimemng report
    private String cmExpDateString;// add by loyal for neimemng report
    private String upChannelIfName;
    private String downChannelIfName;

    // 1: other(1)
    // 2: ranging(2)
    // 3: rangingAborted(3)
    // 4: rangingComplete(4)
    // 5: ipComplete(5)
    // 6: registrationComplete(6)
    // 7: accessDenied(7)
    // 8: operational(8)
    // 9: registeredBPIInitializing(9)
    // 10: DHCPv4Discover(10)
    // 11: DHCPv4Offer(11)
    // 12: DHCPv4Request(12)
    // 13: ForwardingDisabled(21)
    // 14: DHCPv6Solicit(22)
    // 15: DHCPv6Advertise(23)
    // 16: DHCPv6Request(24)
    // 17: DHCPv6Reply(25)
    // 18: 设备版本不支持 -2
    // 19: 未采集到值 -1
    private Integer preStatus = null;// previous state

    // DOCSIS 1.0 (1), DOCSIS 1.1 (2), DOCSIS 2.0 (3), DOCSIS 3.0 (4)
    private Integer docsisMode = null;// 支持2.0还是3.0

    public static final int DOCSIS_1_0 = 1;
    public static final int DOCSIS_1_1 = 2;
    public static final int DOCSIS_2 = 3;
    public static final int DOCSIS_3 = 4;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.5.1.3")
    private String fileName;// CM配置文件名
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.5.1.5")
    private String v6FileName;// CM配置文件名

    private String cmServiceType;
    private Boolean remoteQueryState = false;
    private Boolean supportWebProxy = false;

    // 支持功能
    public boolean isCmOnline() {
        return this.getStatusValue() != null && CM_ONLINE_STATUS.contains(this.getStatusValue());
    }

    public static boolean isCmOnline(Integer statusValue) {
        return statusValue != null && CM_ONLINE_STATUS.contains(statusValue);
    }

    public boolean isCmOffline() {
        return this.getStatusValue() != null && getStatusValue().equals(OFFLINESTATUS);
    }

    public static boolean isCmOffline(Integer statusValue) {
        return statusValue != null && statusValue.equals(OFFLINESTATUS);
    }

    public void copyBy(CmAttribute newCmAttribute) {
        this.statusIndex = newCmAttribute.getStatusIndex();
        this.statusMacAddress = newCmAttribute.getStatusMacAddress();
        this.statusIpAddress = newCmAttribute.getStatusIpAddress();
        this.statusDownChannelIfIndex = newCmAttribute.getStatusDownChannelIfIndex();
        this.statusUpChannelIfIndex = newCmAttribute.getStatusUpChannelIfIndex();
        this.statusRxPower = newCmAttribute.getStatusRxPower();
        this.statusTimingOffset = newCmAttribute.getStatusTimingOffset();
        this.statusEqualizationData = newCmAttribute.getStatusEqualizationData();
        this.statusValue = newCmAttribute.getStatusValue();
        this.statusUnerroreds = newCmAttribute.getStatusUnerroreds();
        this.statusCorrecteds = newCmAttribute.getStatusCorrecteds();
        this.statusUncorrectables = newCmAttribute.getStatusUncorrectables();
        this.statusExtUnerroreds = newCmAttribute.getStatusExtUnerroreds();
        this.statusExtCorrecteds = newCmAttribute.getStatusExtCorrecteds();
        this.statusExtUncorrectables = newCmAttribute.getStatusExtUncorrectables();
        this.statusSignalNoise = newCmAttribute.getStatusSignalNoise();
        this.statusMicroreflections = newCmAttribute.getStatusMicroreflections();
        this.statusDocsisRegMode = newCmAttribute.getStatusDocsisRegMode();
        this.statusModulationType = newCmAttribute.getStatusModulationType();
        this.statusInetAddressType = newCmAttribute.getStatusInetAddressType();
        this.statusInetAddress = newCmAttribute.getStatusInetAddress();
        this.statusValueLastUpdate = newCmAttribute.getStatusValueLastUpdate();
        this.statusHighResolutionTO = newCmAttribute.getStatusHighResolutionTO();
        this.preStatus = newCmAttribute.getPreStatus();
        this.docsisMode = newCmAttribute.getDocsisMode();
        this.fileName = newCmAttribute.getFileName();
        this.v6FileName = newCmAttribute.getV6FileName();
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

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Long getStatusIndex() {
        return statusIndex;
    }

    public void setStatusIndex(Long statusIndex) {
        this.statusIndex = statusIndex;
    }

    public String getStatusMacAddress() {
        return statusMacAddress;
    }

    public void setStatusMacAddress(String statusMacAddress) {
        this.statusMacAddress = statusMacAddress;
    }

    public String getStatusIpAddress() {
        return statusIpAddress;
    }

    public void setStatusIpAddress(String statusIpAddress) {
        this.statusIpAddress = statusIpAddress;
    }

    public Long getStatusDownChannelIfIndex() {
        return statusDownChannelIfIndex;
    }

    public void setStatusDownChannelIfIndex(Long statusDownChannelIfIndex) {
        this.statusDownChannelIfIndex = statusDownChannelIfIndex;
        if (statusDownChannelIfIndex != null) {
            this.downChannelId = CmcIndexUtils.getChannelId(statusDownChannelIfIndex);
        }
    }

    public Long getStatusUpChannelIfIndex() {
        return statusUpChannelIfIndex;
    }

    public void setStatusUpChannelIfIndex(Long statusUpChannelIfIndex) {
        this.statusUpChannelIfIndex = statusUpChannelIfIndex;
        if (statusUpChannelIfIndex != null) {
            this.upChannelId = CmcIndexUtils.getChannelId(statusUpChannelIfIndex);
        }
    }

    public Long getStatusRxPower() {
        return statusRxPower;
    }

    public void setStatusRxPower(Long statusRxPower) {
        this.statusRxPower = statusRxPower;
        if (statusRxPower != null) {
            this.docsIfCmtsCmStatusRxPowerString = (float) statusRxPower / 10 + " dBmV";
        }
    }

    public Long getStatusTimingOffset() {
        return statusTimingOffset;
    }

    public void setStatusTimingOffset(Long statusTimingOffset) {
        this.statusTimingOffset = statusTimingOffset;
    }

    public String getStatusEqualizationData() {
        return statusEqualizationData;
    }

    public void setStatusEqualizationData(String statusEqualizationData) {
        this.statusEqualizationData = statusEqualizationData;
    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
    }

    public Long getStatusUnerroreds() {
        return statusUnerroreds;
    }

    public void setStatusUnerroreds(Long statusUnerroreds) {
        this.statusUnerroreds = statusUnerroreds;
    }

    public Long getStatusCorrecteds() {
        return statusCorrecteds;
    }

    public void setStatusCorrecteds(Long statusCorrecteds) {
        this.statusCorrecteds = statusCorrecteds;
    }

    public Long getStatusUncorrectables() {
        return statusUncorrectables;
    }

    public void setStatusUncorrectables(Long statusUncorrectables) {
        this.statusUncorrectables = statusUncorrectables;
    }

    public Long getStatusExtUnerroreds() {
        return statusExtUnerroreds;
    }

    public void setStatusExtUnerroreds(Long statusExtUnerroreds) {
        this.statusExtUnerroreds = statusExtUnerroreds;
    }

    public Long getStatusExtCorrecteds() {
        return statusExtCorrecteds;
    }

    public void setStatusExtCorrecteds(Long statusExtCorrecteds) {
        this.statusExtCorrecteds = statusExtCorrecteds;
    }

    public Long getStatusExtUncorrectables() {
        return statusExtUncorrectables;
    }

    public void setStatusExtUncorrectables(Long statusExtUncorrectables) {
        this.statusExtUncorrectables = statusExtUncorrectables;
    }

    public Long getStatusSignalNoise() {
        return statusSignalNoise;
    }

    public void setStatusSignalNoise(Long statusSignalNoise) {
        this.statusSignalNoise = statusSignalNoise;
        if (statusSignalNoise != null) {
            this.docsIfCmtsCmStatusSignalNoiseString = (float) statusSignalNoise / 10 + " dB";
        }
    }

    public Long getStatusMicroreflections() {
        return statusMicroreflections;
    }

    public void setStatusMicroreflections(Long statusMicroreflections) {
        this.statusMicroreflections = statusMicroreflections;
        if (statusMicroreflections != null) {
            this.docsIfCmtsCmStatusMicroreflectionsString = statusMicroreflections + " -dBc";
        }
    }

    public Long getStatusDocsisRegMode() {
        return statusDocsisRegMode;
    }

    public void setStatusDocsisRegMode(Long statusDocsisRegMode) {
        this.statusDocsisRegMode = statusDocsisRegMode;
        if (statusDocsisRegMode != null && statusDocsisRegMode <= 2) {
            this.docsIfCmtsCmStatusDocsisRegModeString = cmtsCmStatusDocsisRegMode[statusDocsisRegMode.intValue()];
        }
    }

    public Long getStatusModulationType() {
        return statusModulationType;
    }

    public void setStatusModulationType(Long statusModulationType) {
        this.statusModulationType = statusModulationType;
        if (statusModulationType != null && statusModulationType.longValue() < cmtsCmStatusModulationType.length) {
            this.docsIfCmtsCmStatusModulationTypeString = cmtsCmStatusModulationType[statusModulationType.intValue()];
        }
    }

    public Long getStatusInetAddressType() {
        return statusInetAddressType;
    }

    public void setStatusInetAddressType(Long statusInetAddressType) {
        this.statusInetAddressType = statusInetAddressType;
        if (statusInetAddressType != null && statusInetAddressType.longValue() < cmtsCmStatusInetAddressType.length) {
            this.docsIfCmtsCmStatusInetAddressTypeString = cmtsCmStatusInetAddressType[statusInetAddressType.intValue()];
        }
    }

    public static String[] getCmtscmstatusinetaddresstype() {
        return cmtsCmStatusInetAddressType;
    }

    public Long getStatusValueLastUpdate() {
        return statusValueLastUpdate;
    }

    public void setStatusValueLastUpdate(Long statusValueLastUpdate) {
        this.statusValueLastUpdate = statusValueLastUpdate;
        if (statusValueLastUpdate != null) {
            this.docsIfCmtsCmStatusValueLastUpdateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(
                    statusValueLastUpdate * 10));
        }
    }

    public Long getStatusHighResolutionTO() {
        return statusHighResolutionTO;
    }

    public void setStatusHighResolutionTO(Long statusHighResolutionTO) {
        this.statusHighResolutionTO = statusHighResolutionTO;
    }

    public String getDocsIfCmtsCmStatusValueString() {
        if (statusValue != null) {
            this.docsIfCmtsCmStatusValueString = ResourcesUtil.getString("CM.status." + statusValue.intValue());
        }
        return docsIfCmtsCmStatusValueString;
    }

    public void setDocsIfCmtsCmStatusValueString(String docsIfCmtsCmStatusValueString) {
        this.docsIfCmtsCmStatusValueString = docsIfCmtsCmStatusValueString;
    }

    public String getDocsIfCmtsCmStatusDocsisRegModeString() {
        return docsIfCmtsCmStatusDocsisRegModeString;
    }

    public void setDocsIfCmtsCmStatusDocsisRegModeString(String docsIfCmtsCmStatusDocsisRegModeString) {
        this.docsIfCmtsCmStatusDocsisRegModeString = docsIfCmtsCmStatusDocsisRegModeString;
    }

    public String getDocsIfCmtsCmStatusInetAddressTypeString() {
        return docsIfCmtsCmStatusInetAddressTypeString;
    }

    public void setDocsIfCmtsCmStatusInetAddressTypeString(String docsIfCmtsCmStatusInetAddressTypeString) {
        this.docsIfCmtsCmStatusInetAddressTypeString = docsIfCmtsCmStatusInetAddressTypeString;
    }

    public String getDocsIfCmtsCmStatusModulationTypeString() {
        return docsIfCmtsCmStatusModulationTypeString;
    }

    public String getStatusInetAddress() {
        if (statusInetAddress == null) {
            return statusInetAddress;
        }
        String[] sug = statusInetAddress.trim().split(".");
        if (sug.length == 4) {
            // 老版本的ip 10.10.10.1
            return statusInetAddress;
        } else {
            sug = statusInetAddress.split(":");
            if (sug.length == 4) {
                int[] ipInt = { 0, 0, 0, 0 };
                for (int i = 0; i < sug.length; i++) {
                    ipInt[i] = Integer.parseInt(sug[i], 16);
                }
                // 新版本的ip无法被转换成字符的 6e:12:34:01
                return IpUtils.intArrayToIp(ipInt);
            } else {
                int[] ipInt = { 0, 0, 0, 0 };
                char[] ipChar = statusInetAddress.toCharArray();
                if (ipChar.length == 4) {
                    for (int i = 0; i < ipChar.length; i++) {
                        ipInt[i] = (int) ipChar[i];
                    }
                    // 新版本的ip已经转换成字符的 d a
                    return IpUtils.intArrayToIp(ipInt);
                } else {
                    // ipv6
                    return statusInetAddress;
                }
            }
        }
    }

    public void setStatusInetAddress(String statusInetAddress) {
        this.statusInetAddress = statusInetAddress;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static String[] getCmtscmstatusdocsisregmode() {
        return cmtsCmStatusDocsisRegMode;
    }

    public static String[] getCmtscmstatusmodulationtype() {
        return cmtsCmStatusModulationType;
    }

    public void setDocsIfCmtsCmStatusRxPowerString(String docsIfCmtsCmStatusRxPowerString) {
        this.docsIfCmtsCmStatusRxPowerString = docsIfCmtsCmStatusRxPowerString;
    }

    public void setDocsIfCmtsCmStatusMicroreflectionsString(String docsIfCmtsCmStatusMicroreflectionsString) {
        this.docsIfCmtsCmStatusMicroreflectionsString = docsIfCmtsCmStatusMicroreflectionsString;
    }

    public void setDocsIfCmtsCmStatusSignalNoiseString(String docsIfCmtsCmStatusSignalNoiseString) {
        this.docsIfCmtsCmStatusSignalNoiseString = docsIfCmtsCmStatusSignalNoiseString;
    }

    public void setDocsIfCmtsCmStatusValueLastUpdateString(String docsIfCmtsCmStatusValueLastUpdateString) {
        this.docsIfCmtsCmStatusValueLastUpdateString = docsIfCmtsCmStatusValueLastUpdateString;
    }

    public void setDocsIfCmtsCmStatusModulationTypeString(String docsIfCmtsCmStatusModulationTypeString) {
        this.docsIfCmtsCmStatusModulationTypeString = docsIfCmtsCmStatusModulationTypeString;
    }

    public String getDocsIfCmtsCmStatusRxPowerString() {
        return docsIfCmtsCmStatusRxPowerString;
    }

    public String getDocsIfCmtsCmStatusSignalNoiseString() {
        return docsIfCmtsCmStatusSignalNoiseString;
    }

    public String getDocsIfCmtsCmStatusMicroreflectionsString() {
        return docsIfCmtsCmStatusMicroreflectionsString;
    }

    public String getDocsIfCmtsCmStatusValueLastUpdateString() {
        return docsIfCmtsCmStatusValueLastUpdateString;
    }

    public String getDocsIfCmtsCmStatusUnerroredsString() {
        if (this.statusUnerroreds == null || this.statusCorrecteds == null || this.statusUncorrectables == null) {
            return null;
        }
        long total = statusUnerroreds.intValue() + statusCorrecteds.intValue() + statusUncorrectables.intValue();
        this.docsIfCmtsCmStatusUnerroredsString = statusUnerroreds + Symbol.PARENTHESIS_LEFT
                + CmcUtil.turnToPercent(statusUnerroreds.intValue(), total) + Symbol.PARENTHESIS_RIGHT;
        return docsIfCmtsCmStatusUnerroredsString;
    }

    public void setDocsIfCmtsCmStatusUnerroredsString(String docsIfCmtsCmStatusUnerroredsString) {
        this.docsIfCmtsCmStatusUnerroredsString = docsIfCmtsCmStatusUnerroredsString;
    }

    public String getDocsIfCmtsCmStatusCorrectedsString() {
        if (this.statusUnerroreds == null || this.statusCorrecteds == null || this.statusUncorrectables == null) {
            return null;
        }
        long total = statusUnerroreds.intValue() + statusCorrecteds.intValue() + statusUncorrectables.intValue();
        this.docsIfCmtsCmStatusCorrectedsString = statusUnerroreds + Symbol.PARENTHESIS_LEFT
                + CmcUtil.turnToPercent(statusCorrecteds.intValue(), total) + Symbol.PARENTHESIS_RIGHT;
        return docsIfCmtsCmStatusCorrectedsString;
    }

    public void setDocsIfCmtsCmStatusCorrectedsString(String docsIfCmtsCmStatusCorrectedsString) {
        this.docsIfCmtsCmStatusCorrectedsString = docsIfCmtsCmStatusCorrectedsString;
    }

    public String getDocsIfCmtsCmStatusUncorrectablesString() {
        if (this.statusUnerroreds == null || this.statusCorrecteds == null || this.statusUncorrectables == null) {
            return null;
        }
        long total = statusUnerroreds.intValue() + statusCorrecteds.intValue() + statusUncorrectables.intValue();
        this.docsIfCmtsCmStatusUncorrectablesString = statusUnerroreds + Symbol.PARENTHESIS_LEFT
                + CmcUtil.turnToPercent(statusUncorrectables.intValue(), total) + Symbol.PARENTHESIS_RIGHT;
        return docsIfCmtsCmStatusUncorrectablesString;
    }

    public void setDocsIfCmtsCmStatusUncorrectablesString(String docsIfCmtsCmStatusUncorrectablesString) {
        this.docsIfCmtsCmStatusUncorrectablesString = docsIfCmtsCmStatusUncorrectablesString;
    }

    public String getDocsIfCmtsCmStatusExtUnerroredsString() {
        if (this.statusExtUnerroreds == null || this.statusExtCorrecteds == null
                || this.statusExtUncorrectables == null) {
            return null;
        }
        long total = statusExtUnerroreds.intValue() + statusExtCorrecteds.intValue()
                + statusExtUncorrectables.intValue();
        this.docsIfCmtsCmStatusExtUnerroredsString = statusExtUncorrectables + Symbol.PARENTHESIS_LEFT
                + CmcUtil.turnToPercent(statusExtUncorrectables.intValue(), total) + Symbol.PARENTHESIS_RIGHT;
        return docsIfCmtsCmStatusExtUnerroredsString;
    }

    public void setDocsIfCmtsCmStatusExtUnerroredsString(String docsIfCmtsCmStatusExtUnerroredsString) {
        this.docsIfCmtsCmStatusExtUnerroredsString = docsIfCmtsCmStatusExtUnerroredsString;
    }

    public String getDocsIfCmtsCmStatusExtCorrectedsString() {
        if (this.statusExtUnerroreds == null || this.statusExtCorrecteds == null
                || this.statusExtUncorrectables == null) {
            return null;
        }
        long total = statusExtUnerroreds.intValue() + statusExtCorrecteds.intValue()
                + statusExtUncorrectables.intValue();
        this.docsIfCmtsCmStatusExtCorrectedsString = statusExtUncorrectables + Symbol.PARENTHESIS_LEFT
                + CmcUtil.turnToPercent(statusExtCorrecteds.intValue(), total) + Symbol.PARENTHESIS_RIGHT;
        return docsIfCmtsCmStatusExtCorrectedsString;
    }

    public void setDocsIfCmtsCmStatusExtCorrectedsString(String docsIfCmtsCmStatusExtCorrectedsString) {
        this.docsIfCmtsCmStatusExtCorrectedsString = docsIfCmtsCmStatusExtCorrectedsString;
    }

    public String getDocsIfCmtsCmStatusExtUncorrectablesString() {
        if (this.statusExtUnerroreds == null || this.statusExtCorrecteds == null
                || this.statusExtUncorrectables == null) {
            return null;
        }
        long total = statusExtUnerroreds.intValue() + statusExtCorrecteds.intValue()
                + statusExtUncorrectables.intValue();
        this.docsIfCmtsCmStatusExtUncorrectablesString = statusExtUncorrectables + Symbol.PARENTHESIS_LEFT
                + CmcUtil.turnToPercent(statusExtUncorrectables.intValue(), total) + Symbol.PARENTHESIS_RIGHT;
        return docsIfCmtsCmStatusExtUncorrectablesString;
    }

    public void setDocsIfCmtsCmStatusExtUncorrectablesString(String docsIfCmtsCmStatusExtUncorrectablesString) {
        this.docsIfCmtsCmStatusExtUncorrectablesString = docsIfCmtsCmStatusExtUncorrectablesString;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getCmAlias() {
        return cmAlias;
    }

    public void setCmAlias(String cmAlias) {
        this.cmAlias = cmAlias;
    }

    public String getCmClassified() {
        return cmClassified;
    }

    public void setCmClassified(String cmClassified) {
        this.cmClassified = cmClassified;
    }

    public Integer getTopCmFlapInsertionFailNum() {
        return topCmFlapInsertionFailNum;
    }

    public void setTopCmFlapInsertionFailNum(Integer topCmFlapInsertionFailNum) {
        if (null == topCmFlapInsertionFailNum || topCmFlapInsertionFailNum == 0) {
            topCmFlapInsertionFailNum = 0;
        }
        this.topCmFlapInsertionFailNum = topCmFlapInsertionFailNum;
    }

    public List<DocsIf3CmtsCmUsStatus> getDocsIf3CmtsCmUsStatusList() {
        return docsIf3CmtsCmUsStatusList;
    }

    public void setDocsIf3CmtsCmUsStatusList(List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatusList) {
        this.docsIf3CmtsCmUsStatusList = docsIf3CmtsCmUsStatusList;
    }

    public Long getDownChannelId() {
        return downChannelId;
    }

    public void setDownChannelId(Long downChannelId) {
        this.downChannelId = downChannelId;
    }

    public String getDocsIfSigQSignalNoiseForUnit() {
        return docsIfSigQSignalNoiseForUnit;
    }

    public void setDocsIfSigQSignalNoiseForUnit(String docsIfSigQSignalNoiseForUnit) {
        this.docsIfSigQSignalNoiseForUnit = docsIfSigQSignalNoiseForUnit;
    }

    public Long getUpChannelId() {
        return upChannelId;
    }

    public void setUpChannelId(Long upChannelId) {
        this.upChannelId = upChannelId;
    }

    public String getCmcAlias() {
        return cmcAlias;
    }

    public void setCmcAlias(String cmcAlias) {
        this.cmcAlias = cmcAlias;
    }

    public String getStatusVlaueString() {
        return statusVlaueString;
    }

    public void setStatusVlaueString(String statusVlaueString) {
        this.statusVlaueString = statusVlaueString;
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
        if (downChannelTx != null) {
            this.downChannelRecvPower = UnitConfigConstant.parsePowerValue(Double.parseDouble(downChannelTx));
        }
    }

    public String getUpChannelTx() {
        return upChannelTx;
    }

    public void setUpChannelTx(String upChannelTx) {
        this.upChannelTx = upChannelTx;
        if (upChannelTx != null) {
            this.upChannelTransPower = UnitConfigConstant.parsePowerValue(Double.parseDouble(upChannelTx));
        }
    }

    public String getUpChannelIndexString() {
        return upChannelIndexString;
    }

    public void setUpChannelIndexString(String upChannelIndexString) {
        this.upChannelIndexString = upChannelIndexString;
    }

    public String getDownChannelIndexString() {
        return downChannelIndexString;
    }

    public void setDownChannelIndexString(String downChannelIndexString) {
        this.downChannelIndexString = downChannelIndexString;
    }

    public String getUpChannelSnr() {
        return upChannelSnr;
    }

    public void setUpChannelSnr(String upChannelSnr) {
        this.upChannelSnr = upChannelSnr;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.lastRefreshTime = df.format(collectTime);
    }

    public String getLastRefreshTime() {
        return lastRefreshTime;
    }

    public void setLastRefreshTime(String lastRefreshTime) {
        this.lastRefreshTime = lastRefreshTime;
    }

    public String getCmcName() {
        return cmcName;
    }

    public void setCmcName(String cmcName) {
        this.cmcName = cmcName;
    }

    public Long getCmcDeviceStyle() {
        return cmcDeviceStyle;
    }

    public void setCmcDeviceStyle(Long cmcDeviceStyle) {
        this.cmcDeviceStyle = cmcDeviceStyle;
    }

    public Long getOltId() {
        return oltId;
    }

    public void setOltId(Long oltId) {
        this.oltId = oltId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Boolean getSupportStaticIp() {
        return supportStaticIp;
    }

    public void setSupportStaticIp(Boolean supportStaticIp) {
        this.supportStaticIp = supportStaticIp;
    }

    public Boolean getSupportCpeInfo() {
        return supportCpeInfo;
    }

    public void setSupportCpeInfo(Boolean supportCpeInfo) {
        this.supportCpeInfo = supportCpeInfo;
    }

    public Boolean getSupportReset() {
        return supportReset;
    }

    public void setSupportReset(Boolean supportReset) {
        this.supportReset = supportReset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.cmcName = name;
    }

    public Double getDownChannelRecvPower() {
        return downChannelRecvPower;
    }

    public void setDownChannelRecvPower(Double downChannelRecvPower) {
        this.downChannelRecvPower = downChannelRecvPower;
    }

    public Double getUpChannelTransPower() {
        return upChannelTransPower;
    }

    public void setUpChannelTransPower(Double upChannelTransPower) {
        this.upChannelTransPower = upChannelTransPower;
    }

    public Integer getCpeNum() {
        return cpeNum;
    }

    public void setCpeNum(Integer cpeNum) {
        this.cpeNum = cpeNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUpChannelIfDescr() {
        return upChannelIfDescr;
    }

    public void setUpChannelIfDescr(String upChannelIfDescr) {
        this.upChannelIfDescr = upChannelIfDescr;
    }

    public String getDownChannelIfDescr() {
        return downChannelIfDescr;
    }

    public void setDownChannelIfDescr(String downChannelIfDescr) {
        this.downChannelIfDescr = downChannelIfDescr;
    }

    public Integer getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(Integer displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisplayIp() {
        return displayIp;
    }

    public void setDisplayIp(String displayIp) {
        this.displayIp = displayIp;
    }

    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Timestamp getCmExpDate() {
        return cmExpDate;
    }

    public void setCmExpDate(Timestamp cmExpDate) {
        this.cmExpDate = cmExpDate;
    }

    public String getCmExpDateString() {
        return cmExpDateString;
    }

    public void setCmExpDateString(String cmExpDateString) {
        this.cmExpDateString = cmExpDateString;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getEffDate() {
        return effDate;
    }

    public void setEffDate(String effDate) {
        this.effDate = effDate;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getDownChannelIfName() {
        return downChannelIfName;
    }

    public void setDownChannelIfName(String downChannelIfName) {
        this.downChannelIfName = downChannelIfName;
    }

    public String getUpChannelIfName() {
        return upChannelIfName;
    }

    public void setUpChannelIfName(String upChannelIfName) {
        this.upChannelIfName = upChannelIfName;
    }

    public Integer getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(Integer preStatus) {
        this.preStatus = preStatus;
    }

    public Integer getDocsisMode() {
        return docsisMode;
    }

    public void setDocsisMode(Integer docsisMode) {
        this.docsisMode = docsisMode;
    }

    public String getCmcIp() {
        return cmcIp;
    }

    public void setCmcIp(String cmcIp) {
        this.cmcIp = cmcIp;
    }

    public CmSignal getCmSignal() {
        return cmSignal;

    }

    public void setCmSignal(CmSignal cmSignal) {
        this.cmSignal = cmSignal;
    }

    public List<Cm3Signal> getUpChannelCm3Signal() {
        return upChannelCm3Signal;
    }

    public void setUpChannelCm3Signal(List<Cm3Signal> upChannelCm3Signal) {
        this.upChannelCm3Signal = upChannelCm3Signal;
    }

    public List<Cm3Signal> getDownChannelCm3Signal() {
        return downChannelCm3Signal;
    }

    public void setDownChannelCm3Signal(List<Cm3Signal> downChannelCm3Signal) {
        this.downChannelCm3Signal = downChannelCm3Signal;
    }

    public String getStatusInetAddressIpString() {
        return statusInetAddressIpString;
    }

    public void setStatusInetAddressIpString(String statusInetAddressIpString) {
        this.statusInetAddressIpString = statusInetAddressIpString;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getV6FileName() {
        return v6FileName;
    }

    public void setV6FileName(String v6FileName) {
        this.v6FileName = v6FileName;
    }

    public String getCmServiceType() {
        if (cmServiceType != null && !"".equals(cmServiceType)) {
            cmServiceType = fileName + "(" + cmServiceType + ")";
        } else {
            cmServiceType = fileName;
        }
        return cmServiceType;
    }

    public void setCmServiceType(String cmServiceType) {
        this.cmServiceType = cmServiceType;
    }

    public Boolean getRemoteQueryState() {
        return remoteQueryState;
    }

    public void setRemoteQueryState(Boolean remoteQueryState) {
        this.remoteQueryState = remoteQueryState;
    }

    public String getPartialUpChannels() {
        return partialUpChannels;
    }

    public void setPartialUpChannels(String partialUpChannels) {
        this.partialUpChannels = partialUpChannels;
    }

    public String getPartialDownChannels() {
        return partialDownChannels;
    }

    public void setPartialDownChannels(String partialDownChannels) {
        this.partialDownChannels = partialDownChannels;
    }

    public Boolean getSupportCmUpgrade() {
        return supportCmUpgrade;
    }

    public void setSupportCmUpgrade(Boolean supportCmUpgrade) {
        this.supportCmUpgrade = supportCmUpgrade;
    }

    public Boolean getSupportWebProxy() {
        return supportWebProxy;
    }

    public void setSupportWebProxy(Boolean supportWebProxy) {
        this.supportWebProxy = supportWebProxy;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmAttribute [entityId=");
        builder.append(entityId);
        builder.append(", cmId=");
        builder.append(cmId);
        builder.append(", docsIfCmtsCmStatusValueString=");
        builder.append(docsIfCmtsCmStatusValueString);
        builder.append(", docsIfCmtsCmStatusDocsisRegModeString=");
        builder.append(docsIfCmtsCmStatusDocsisRegModeString);
        builder.append(", docsIfCmtsCmStatusModulationTypeString=");
        builder.append(docsIfCmtsCmStatusModulationTypeString);
        builder.append(", docsIfCmtsCmStatusInetAddressTypeString=");
        builder.append(docsIfCmtsCmStatusInetAddressTypeString);
        builder.append(", docsIfCmtsCmStatusRxPowerString=");
        builder.append(docsIfCmtsCmStatusRxPowerString);
        builder.append(", docsIfCmtsCmStatusMicroreflectionsString=");
        builder.append(docsIfCmtsCmStatusMicroreflectionsString);
        builder.append(", docsIfCmtsCmStatusSignalNoiseString=");
        builder.append(docsIfCmtsCmStatusSignalNoiseString);
        builder.append(", docsIfCmtsCmStatusValueLastUpdateString=");
        builder.append(docsIfCmtsCmStatusValueLastUpdateString);
        builder.append(", docsIfCmtsCmStatusUnerroredsString=");
        builder.append(docsIfCmtsCmStatusUnerroredsString);
        builder.append(", docsIfCmtsCmStatusCorrectedsString=");
        builder.append(docsIfCmtsCmStatusCorrectedsString);
        builder.append(", docsIfCmtsCmStatusUncorrectablesString=");
        builder.append(docsIfCmtsCmStatusUncorrectablesString);
        builder.append(", docsIfCmtsCmStatusExtUnerroredsString=");
        builder.append(docsIfCmtsCmStatusExtUnerroredsString);
        builder.append(", docsIfCmtsCmStatusExtCorrectedsString=");
        builder.append(docsIfCmtsCmStatusExtCorrectedsString);
        builder.append(", docsIfCmtsCmStatusExtUncorrectablesString=");
        builder.append(docsIfCmtsCmStatusExtUncorrectablesString);
        builder.append(", cmAlias=");
        builder.append(cmAlias);
        builder.append(", cmClassified=");
        builder.append(cmClassified);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", statusIndex=");
        builder.append(statusIndex);
        builder.append(", statusMacAddress=");
        builder.append(statusMacAddress);
        builder.append(", statusIpAddress=");
        builder.append(statusIpAddress);
        builder.append(", statusDownChannelIfIndex=");
        builder.append(statusDownChannelIfIndex);
        builder.append(", downChannelId=");
        builder.append(downChannelId);
        builder.append(", statusUpChannelIfIndex=");
        builder.append(statusUpChannelIfIndex);
        builder.append(", upChannelId=");
        builder.append(upChannelId);
        builder.append(", statusRxPower=");
        builder.append(statusRxPower);
        builder.append(", statusTimingOffset=");
        builder.append(statusTimingOffset);
        builder.append(", statusEqualizationData=");
        builder.append(statusEqualizationData);
        builder.append(", statusValue=");
        builder.append(statusValue);
        builder.append(", statusVlaueString=");
        builder.append(statusVlaueString);
        builder.append(", statusUnerroreds=");
        builder.append(statusUnerroreds);
        builder.append(", statusCorrecteds=");
        builder.append(statusCorrecteds);
        builder.append(", statusUncorrectables=");
        builder.append(statusUncorrectables);
        builder.append(", statusSignalNoise=");
        builder.append(statusSignalNoise);
        builder.append(", statusMicroreflections=");
        builder.append(statusMicroreflections);
        builder.append(", statusExtUnerroreds=");
        builder.append(statusExtUnerroreds);
        builder.append(", statusExtCorrecteds=");
        builder.append(statusExtCorrecteds);
        builder.append(", statusExtUncorrectables=");
        builder.append(statusExtUncorrectables);
        builder.append(", statusDocsisRegMode=");
        builder.append(statusDocsisRegMode);
        builder.append(", statusModulationType=");
        builder.append(statusModulationType);
        builder.append(", statusInetAddressType=");
        builder.append(statusInetAddressType);
        builder.append(", statusInetAddress=");
        builder.append(statusInetAddress);
        builder.append(", statusInetAddressIpString=");
        builder.append(statusInetAddressIpString);
        builder.append(", statusValueLastUpdate=");
        builder.append(statusValueLastUpdate);
        builder.append(", statusHighResolutionTO=");
        builder.append(statusHighResolutionTO);
        builder.append(", cmcAlias=");
        builder.append(cmcAlias);
        builder.append(", topCmFlapInsertionFailNum=");
        builder.append(topCmFlapInsertionFailNum);
        builder.append(", docsIf3CmtsCmUsStatusList=");
        builder.append(docsIf3CmtsCmUsStatusList);
        builder.append(", docsIfSigQSignalNoiseForUnit=");
        builder.append(docsIfSigQSignalNoiseForUnit);
        builder.append(", downChannelSnr=");
        builder.append(downChannelSnr);
        builder.append(", downChannelTx=");
        builder.append(downChannelTx);
        builder.append(", upChannelTx=");
        builder.append(upChannelTx);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append(", lastRefreshTime=");
        builder.append(lastRefreshTime);
        builder.append(", downChannelFrequency=");
        builder.append(downChannelFrequency);
        builder.append(", upChannelFrequency=");
        builder.append(upChannelFrequency);
        builder.append(", partialUpChannels=");
        builder.append(partialUpChannels);
        builder.append(", partialDownChannels=");
        builder.append(partialDownChannels);
        builder.append(", cmSignal=");
        builder.append(cmSignal);
        builder.append(", upChannelCm3Signal=");
        builder.append(upChannelCm3Signal);
        builder.append(", downChannelCm3Signal=");
        builder.append(downChannelCm3Signal);
        builder.append(", upChannelIndexString=");
        builder.append(upChannelIndexString);
        builder.append(", downChannelIndexString=");
        builder.append(downChannelIndexString);
        builder.append(", upChannelSnr=");
        builder.append(upChannelSnr);
        builder.append(", upChannelIfDescr=");
        builder.append(upChannelIfDescr);
        builder.append(", downChannelIfDescr=");
        builder.append(downChannelIfDescr);
        builder.append(", cmcName=");
        builder.append(cmcName);
        builder.append(", cmcIp=");
        builder.append(cmcIp);
        builder.append(", cmcDeviceStyle=");
        builder.append(cmcDeviceStyle);
        builder.append(", oltId=");
        builder.append(oltId);
        builder.append(", ponId=");
        builder.append(ponId);
        builder.append(", supportStaticIp=");
        builder.append(supportStaticIp);
        builder.append(", supportCpeInfo=");
        builder.append(supportCpeInfo);
        builder.append(", supportReset=");
        builder.append(supportReset);
        builder.append(", supportCmUpgrade=");
        builder.append(supportCmUpgrade);
        builder.append(", name=");
        builder.append(name);
        builder.append(", cpeNum=");
        builder.append(cpeNum);
        builder.append(", displayStatus=");
        builder.append(displayStatus);
        builder.append(", displayIp=");
        builder.append(displayIp);
        builder.append(", downChannelRecvPower=");
        builder.append(downChannelRecvPower);
        builder.append(", upChannelTransPower=");
        builder.append(upChannelTransPower);
        builder.append(", userId=");
        builder.append(userId);
        builder.append(", userName=");
        builder.append(userName);
        builder.append(", userAddr=");
        builder.append(userAddr);
        builder.append(", userPhoneNo=");
        builder.append(userPhoneNo);
        builder.append(", offerName=");
        builder.append(offerName);
        builder.append(", effDate=");
        builder.append(effDate);
        builder.append(", expDate=");
        builder.append(expDate);
        builder.append(", configFile=");
        builder.append(configFile);
        builder.append(", extension=");
        builder.append(extension);
        builder.append(", cmExpDate=");
        builder.append(cmExpDate);
        builder.append(", cmExpDateString=");
        builder.append(cmExpDateString);
        builder.append(", upChannelIfName=");
        builder.append(upChannelIfName);
        builder.append(", downChannelIfName=");
        builder.append(downChannelIfName);
        builder.append(", preStatus=");
        builder.append(preStatus);
        builder.append(", docsisMode=");
        builder.append(docsisMode);
        builder.append(", fileName=");
        builder.append(fileName);
        builder.append(", v6FileName=");
        builder.append(v6FileName);
        builder.append(", cmServiceType=");
        builder.append(cmServiceType);
        builder.append(", remoteQueryState=");
        builder.append(remoteQueryState);
        builder.append("]");
        return builder.toString();
    }

    public Boolean getSupportClearSingleCm() {
        return supportClearSingleCm;
    }

    public void setSupportClearSingleCm(Boolean supportClearSingleCm) {
        this.supportClearSingleCm = supportClearSingleCm;
    }

}
