/***********************************************************************
 * $Id: OltRealtimeServiceImpl.java,v1.0 2014-7-12 上午9:48:56 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.epon.realtime.dao.OltRealtimeDao;
import com.topvision.ems.epon.realtime.domain.ChannelCmNumInfo;
import com.topvision.ems.epon.realtime.domain.OltBaseInfo;
import com.topvision.ems.epon.realtime.domain.OltCmTotalInfo;
import com.topvision.ems.epon.realtime.domain.OltCurrentCmcInfo;
import com.topvision.ems.epon.realtime.domain.OltGponSubTotalInfo;
import com.topvision.ems.epon.realtime.domain.OltOnuBaseInfo;
import com.topvision.ems.epon.realtime.domain.OltPonInfo;
import com.topvision.ems.epon.realtime.domain.OltPonTotalInfo;
import com.topvision.ems.epon.realtime.domain.OltPortSpeedInfo;
import com.topvision.ems.epon.realtime.domain.OltSlotInfo;
import com.topvision.ems.epon.realtime.domain.OltSniInfo;
import com.topvision.ems.epon.realtime.domain.OltSubDeviceEponInfo;
import com.topvision.ems.epon.realtime.domain.OltSubDeviceGponInfo;
import com.topvision.ems.epon.realtime.domain.OltSubDeviceInfo;
import com.topvision.ems.epon.realtime.domain.OltSubOpticalInfo;
import com.topvision.ems.epon.realtime.domain.OltSubTotalInfo;
import com.topvision.ems.epon.realtime.domain.OltSummaryInfo;
import com.topvision.ems.epon.realtime.domain.OpticalStatsticsInfo;
import com.topvision.ems.epon.realtime.domain.StatisticsResult;
import com.topvision.ems.epon.realtime.facade.OltRealtimeFacade;
import com.topvision.ems.epon.realtime.service.OltRealtimeService;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.facade.domain.EntityTypeStandard;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.ems.performance.domain.PerfThresholdRule;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author flack
 * @created @2014-7-12-上午9:48:56
 *
 */
@Service("oltRealtimeService")
public class OltRealtimeServiceImpl extends BaseService implements OltRealtimeService {
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltRealtimeDao oltRealtimeDao;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;

    @Override
    public OltBaseInfo getOltBaseInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String deviceVersion = deviceVersionService.getParamValue(entityId, "topsysoltuptime", "deviceVersion");
        snmpParam.setDeviceVersion(deviceVersion);
        OltBaseInfo baseInfo = getRealtimeFacade(snmpParam.getIpAddress()).getOltBaseInfo(snmpParam);
        return baseInfo;
    }

    @Override
    public List<OltSlotInfo> getOltSlotInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 板卡信息
        List<OltSlotInfo> slotList = getRealtimeFacade(snmpParam.getIpAddress()).getOltSoltInfo(snmpParam);
        // 基于信道统计的CM信息
        List<ChannelCmNumInfo> channellist = getRealtimeFacade(snmpParam.getIpAddress()).getChannelCmNum(snmpParam);
        Map<Integer, OltSlotInfo> indexMap = new HashMap<Integer, OltSlotInfo>();
        for (OltSlotInfo slot : slotList) {
            if (slot.getOperationStatus().equals(EponConstants.OLT_BOARD_OPERATION_STATUS_UP)) {
                float usedMem = slot.getTotalMemSize() * 1024 * 1024 - slot.getFreeMemSize();
                slot.setMemUseRatio((int) (usedMem * 100 / (slot.getTotalMemSize() * 1024 * 1024)));
                float usedFlash = slot.getTotalFlashSize() - slot.getFreeFlashSize();
                slot.setFlashUseRatio((int) (usedFlash * 100 / slot.getTotalFlashSize()));
            }
            // 建立索引与对象的对应关系,在统计CM数量时使用
            indexMap.put(slot.getSlotIndex(), slot);
        }
        // 统计板卡下的CM数量
        for (ChannelCmNumInfo ch : channellist) {
            Long index = ch.getChannelIndex();
            if ((index.longValue() & 0x8000L) >> 15 == 1) {
                Long slotIndex = (index.longValue() & 0xF8000000L) >> 27;
                indexMap.get(slotIndex.intValue()).addCmNum(ch.getTotalNum());
            }
        }
        return slotList;
    }

    @Override
    public List<OltPonTotalInfo> getPonTotalInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltPonTotalInfo> ponList = getRealtimeFacade(snmpParam.getIpAddress()).getPonTotalInfo(snmpParam);
        return ponList;
    }

    @Override
    public List<OltSniInfo> getSniTotalInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltSniInfo> sniList = getRealtimeFacade(snmpParam.getIpAddress()).getOltSniInfo(snmpParam);
        return sniList;
    }

    @Override
    public List<OltSniInfo> getOltSniInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltSniInfo> sniList = getRealtimeFacade(snmpParam.getIpAddress()).getOltSniInfo(snmpParam);
        List<OltSniInfo> onlineSni = new ArrayList<OltSniInfo>();
        for (OltSniInfo sniInfo : sniList) {
            if (sniInfo.getOperationStatus().equals(EponConstants.PORT_STATUS_UP)) {
                // 只有在端口为up状态并且性能使能开启的情况下才需要去获取实时速率数据
                if (sniInfo.getPerfStatsEnable().equals(EponConstants.ABALITY_ENABLE)) {
                    OltPortSpeedInfo speedInfo = new OltPortSpeedInfo();
                    speedInfo.setEntityId(entityId);
                    // speedInfo.setDeviceIndex(parseIndex(sniInfo.getCardIndex(),
                    // sniInfo.getSniIndex()));
                    Long sniIndex = EponIndex.getSniIndex(sniInfo.getSlotIndex(), sniInfo.getSniIndex());
                    speedInfo.setDeviceIndex(EponIndex.getPortDeviceIndex(sniIndex, EponConstants.PERF_TYPE_OLT_ETH));
                    speedInfo.setCardIndex(0);
                    speedInfo.setPortIndex(0);
                    speedInfo = this.getPortSpeedInfo(speedInfo);
                    // 处理没有采集到值的情况
                    if (speedInfo.getInBindWidth() == null || speedInfo.getOutBindWidth() == null) {
                        sniInfo.setPortInSpeed(-1L);
                        sniInfo.setPortOutSpeed(-1L);
                    } else {
                        sniInfo.setPortInSpeed(speedInfo.getInBindWidth());
                        sniInfo.setPortOutSpeed(speedInfo.getOutBindWidth());
                    }
                } else {
                    sniInfo.setPortInSpeed(-1L);
                    sniInfo.setPortOutSpeed(-1L);
                }
                onlineSni.add(sniInfo);
            }
        }
        return onlineSni;
    }

    @Override
    public List<OltSubTotalInfo> getSubTotalInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltSubTotalInfo> subList = getRealtimeFacade(snmpParam.getIpAddress()).getSubTotalInfo(snmpParam);
        List<OltGponSubTotalInfo> subGponList = getRealtimeFacade(snmpParam.getIpAddress()).getGponSubTotalInfo(
                snmpParam);
        for (OltGponSubTotalInfo gponOnu : subGponList) {
            OltSubTotalInfo subTotalInfo = new OltSubTotalInfo();
            subTotalInfo.setEntityId(entityId);
            subTotalInfo.setOnuDeviceIndex(gponOnu.getOnuDeviceIndex());
            subTotalInfo.setOperationStatus(gponOnu.getOperationStatus());
            subList.add(subTotalInfo);
        }
        return subList;
    }

    @Override
    public List<OltPonInfo> getOltPonInfo(Long entityId, boolean onlineFlag) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltPonInfo> ponList = getRealtimeFacade(snmpParam.getIpAddress()).getOltPonInfo(snmpParam);
        List<ChannelCmNumInfo> channellist = getRealtimeFacade(snmpParam.getIpAddress()).getChannelCmNum(snmpParam);
        Map<String, OltPonInfo> indexMap = new HashMap<String, OltPonInfo>();
        Iterator<OltPonInfo> iterator = ponList.iterator();
        while (iterator.hasNext()) {
            OltPonInfo pon = iterator.next();
            // 建立索引与设备的对应关系
            indexMap.put(pon.getPonLocation(), pon);
            // 处理只显示在线的情况
            if (onlineFlag && !pon.getOperationStatus().equals(EponConstants.PORT_STATUS_UP)) {
                iterator.remove();
                continue;
            }
            // 只有在端口为up状态并且性能使能开启的情况下才需要去获取实时速率数据
            if (pon.getOperationStatus().equals(EponConstants.PORT_STATUS_UP)) {
                if (pon.getPerfStatsEnable().equals(EponConstants.ABALITY_ENABLE)) {
                    OltPortSpeedInfo speed = new OltPortSpeedInfo();
                    speed.setEntityId(entityId);
                    speed.setDeviceIndex(parseIndex(pon.getSlotIndex(), pon.getPonIndex()));
                    speed.setCardIndex(0);
                    speed.setPortIndex(0);
                    try {
                        speed = this.getPortSpeedInfo(speed);
                    } catch (Exception e) {
                        logger.info("port speed get fail {0}", e.getMessage());
                    }

                    // 处理没有采集到值的情况
                    if (speed.getInBindWidth() == null || speed.getOutBindWidth() == null) {
                        pon.setPortInSpeed((double) -1);
                        pon.setPortOutSpeed((double) -1);
                    } else {
                        pon.setPortInSpeed((double) speed.getInBindWidth());
                        pon.setPortOutSpeed((double) speed.getOutBindWidth());
                    }
                }
                // 计算发送光功率，特殊值置0
                if (pon.getTxPower() >= 1 && !pon.getTxPower().equals(EponConstants.TX_POWER)) {
                    pon.setTransPower(10 * Math.log10(pon.getTxPower()) - 40);
                } else {
                    pon.setTransPower(null);
                }
                // 计算接收光功率，特殊值置0
                if (pon.getRvPower() >= 1 && !pon.getRvPower().equals(EponConstants.RE_POWER)) {
                    pon.setRecvPower(10 * Math.log10(pon.getRvPower()) - 40);
                } else {
                    pon.setRecvPower(null);
                }
            } else {
                pon.setPortInSpeed((double) -1);
                pon.setPortOutSpeed((double) -1);
            }
        }
        // 处理Pon下CM数量
        for (ChannelCmNumInfo ch : channellist) {
            Long index = ch.getChannelIndex();
            if ((index.longValue() & 0x8000L) >> 15 == 1) {
                long slotNo = (index.longValue() & 0xF8000000L) >> 27;
                long ponNo = (index.longValue() & 0x7800000L) >> 23;
                indexMap.get(slotNo + "/" + ponNo).addCmNum(ch.getTotalNum());
            }
        }
        return ponList;
    }

    @Override
    public List<OltSubDeviceInfo> getOltSubInfo(Long entityId, boolean onlineFlag) {
        List<OltSubDeviceInfo> subList = getSubInfoList(entityId);
        // 将获取的CMC信息转化为以MAC地址为key的Map,避免多次循环查找
        Map<String, OltCurrentCmcInfo> cmcMap = this.transCmcMap(getOltCurrentCmcInfo(entityId));
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<ChannelCmNumInfo> channellist = getRealtimeFacade(snmpParam.getIpAddress()).getChannelCmNum(snmpParam);
        Map<String, OltSubDeviceInfo> indexMap = new HashMap<String, OltSubDeviceInfo>();
        Iterator<OltSubDeviceInfo> iterator = subList.iterator();
        while (iterator.hasNext()) {
            OltSubDeviceInfo sub = iterator.next();
            // 建立索引与设备的对应关系
            long onuIndex = EponIndex.getOnuIndexByMibIndex(sub.getCardIndex());
            long slotNo = EponIndex.getSlotNo(onuIndex);
            long ponNo = EponIndex.getPonNo(onuIndex);
            long onuNo = EponIndex.getOnuNo(onuIndex);
            String key = slotNo + "." + ponNo + "." + onuNo;
            indexMap.put(key, sub);
            // 在只显示在线的情况下处理离线的ONU
            if (!EponConstants.CC_ONUFLAG_TYPE.contains(sub.getOnuType()) && onlineFlag
                    && sub.getOperationStatus().equals(EponConstants.ONU_STATUS_DOWN)) {
                iterator.remove();
                continue;
            }
            // 处理类型为CMC的情况
            if (EponConstants.CC_ONUFLAG_TYPE.contains(sub.getOnuType())) {
                OltCurrentCmcInfo cmcInfo = cmcMap.get(sub.getMacAddress());
                // 只显示在线的情况下处理离线的CMC
                if (onlineFlag && !cmcInfo.getCmcStatus().equals(EponConstants.CMC_ONLINE_STATUS)) {
                    iterator.remove();
                    continue;
                }
                String deviceName = oltRealtimeDao.queryDeviceAlias(entityId, cmcInfo.getMacAddress());
                if (deviceName != null && deviceName != "") {
                    sub.setOnuName(deviceName);
                } else {
                    sub.setOnuName(cmcInfo.getSysName());
                }
                if (cmcInfo.getCmcStatus().equals(EponConstants.CMC_ONLINE_STATUS)) {
                    sub.setOperationStatus(EponConstants.ONU_STATUS_UP);
                } else {
                    sub.setOperationStatus(EponConstants.ONU_STATUS_DOWN);
                }
                EntityType type = null;
                if (entityTypeService.isCCMTS8800EType(sub.getOnuType())) {
                    // 强集中的E型处理方式不一样
                    type = entityTypeService.getEntityTypeBySysObjId(cmcInfo.getSysObjectId(),
                            EntityTypeStandard.STRONG_DISTRIBUTE_STANDARD);
                } else {
                    type = entityTypeService.getEntityTypeBySysObjId(cmcInfo.getSysObjectId());
                }
                // 兼容类型未取到的情况
                if (type == null) {
                    sub.setDisplayType(EponConstants.EPON_TYPE_UNKNOWN);
                    sub.setTypeId(EponConstants.UNKNOWN_ONU_TYPE.longValue());
                } else {
                    sub.setDisplayType(type.getDisplayName());
                    sub.setTypeId(type.getTypeId());
                }
            }

        }
        // 处理设备下的CM数量
        for (ChannelCmNumInfo ch : channellist) {
            Long index = ch.getChannelIndex();
            long slotNo = CmcIndexUtils.getSlotNo(index);
            long ponNo = CmcIndexUtils.getPonNo(index);
            long onuNo = CmcIndexUtils.getCmcId(index);
            long channelType = CmcIndexUtils.getChannelType(index);
            String key = slotNo + "." + ponNo + "." + onuNo;
            if (indexMap.containsKey(key) && channelType == 0) {
                OltSubDeviceInfo oltSubDeviceInfo = indexMap.get(key);
                oltSubDeviceInfo.addCmNum(ch.getTotalNum());
            }
        }
        return subList;
    }

    @Override
    public List<OltSubDeviceInfo> getSubInfoList(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltSubDeviceInfo> oltSubDeviceInfos;
        List<OltSubDeviceEponInfo> oltSubDeviceEponInfos;
        List<OltSubDeviceGponInfo> oltSubDeviceGponInfos;
        try {
            oltSubDeviceInfos = getRealtimeFacade(snmpParam.getIpAddress()).getOltSubInfo(snmpParam);
        } catch (Exception e) {
            throw e;
        }
        try {
            oltSubDeviceEponInfos = getRealtimeFacade(snmpParam.getIpAddress()).getOltSubEponInfo(snmpParam);
        } catch (Exception e) {
            logger.debug("", e);
            oltSubDeviceEponInfos = new ArrayList<>();
        }
        try {
            oltSubDeviceGponInfos = getRealtimeFacade(snmpParam.getIpAddress()).getOltSubGponInfo(snmpParam);
        } catch (Exception e) {
            logger.debug("", e);
            oltSubDeviceGponInfos = new ArrayList<>();
        }
        Map<Long, OltOnuBaseInfo> map = new HashMap<>();
        for (OltSubDeviceEponInfo oltSubDeviceEponInfo : oltSubDeviceEponInfos) {
            map.put(oltSubDeviceEponInfo.getOnuDeviceIndex(), oltSubDeviceEponInfo);
        }
        for (OltSubDeviceGponInfo oltSubDeviceGponInfo : oltSubDeviceGponInfos) {
            map.put(oltSubDeviceGponInfo.getOnuDeviceIndex(), oltSubDeviceGponInfo);
        }
        for (OltSubDeviceInfo oltSubDeviceInfo : oltSubDeviceInfos) {
            if (map.containsKey(oltSubDeviceInfo.getCardIndex())) {
                OltOnuBaseInfo oltOnuBaseInfo = map.get(oltSubDeviceInfo.getCardIndex());
                oltSubDeviceInfo.setOnuDeviceIndex(oltOnuBaseInfo.getOnuDeviceIndex());
                oltSubDeviceInfo.setOnuName(oltOnuBaseInfo.getOnuName());
                oltSubDeviceInfo.setOperationStatus(oltOnuBaseInfo.getOperationStatus());
                oltSubDeviceInfo.setMacAddress(oltOnuBaseInfo.getIdentifyKey());
                oltSubDeviceInfo.setTestDistance(oltOnuBaseInfo.getTestDistance());
                oltSubDeviceInfo.setOnuType(oltOnuBaseInfo.getOnuType());
            }
        }
        return oltSubDeviceInfos;
    }

    @Override
    public OltCmTotalInfo getCmTotalInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        return getRealtimeFacade(snmpParam.getIpAddress()).getCmTotalInfo(snmpParam);
    }

    @Override
    public OltPortSpeedInfo getPortSpeedInfo(OltPortSpeedInfo speedInfo) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(speedInfo.getEntityId());
        try {
            return getRealtimeFacade(snmpParam.getIpAddress()).getPortSpeedInfo(snmpParam, speedInfo);
        } catch (Exception e) {
            logger.info("getPortSpeedInfo error {}", e.getMessage());
        }
        return new OltPortSpeedInfo();
    }

    @Override
    public StatisticsResult getOpticalThreshold(Long entityId) {
        // 实现按照阀值大小排序
        class ValueOrder implements Comparator<OpticalStatsticsInfo> {
            @Override
            public int compare(OpticalStatsticsInfo o1, OpticalStatsticsInfo o2) {
                if (o1.getValue() > o2.getValue()) {
                    return 1;
                } else if (o1.getValue() < o2.getValue()) {
                    return -1;
                }
                return 0;
            }
        }
        /*
         * //从数据库获取光功率发送与接收的阀值 PerfThresholdRule recvRule =
         * oltRealtimeDao.queryOpticalThreshold(entityId, "OLT_PON_RE_POWER"); //TODO
         * 处理目前默认模板中没有光发送功率阈值的问题 if (recvRule == null) { recvRule = new PerfThresholdRule();
         * recvRule.setThresholds("1_-7_2#5_-24_3#5_-27_4"); } PerfThresholdRule transRule =
         * oltRealtimeDao.queryOpticalThreshold(entityId, "OLT_PON_TX_POWER");
         */
        // 鉴于onu pon口接收与发送可以配置不同的规则,此处针对OLT下ONU下使用默认的规则
        PerfThresholdRule recvRule = new PerfThresholdRule();
        recvRule.setThresholds(PerfTargetConstants.ONU_REV_RULE);
        PerfThresholdRule transRule = new PerfThresholdRule();
        transRule.setThresholds(PerfTargetConstants.ONU_TRANS_RULE);
        // 将阀值转化为对象列表
        List<OpticalStatsticsInfo> recvList = OpticalStatsticsInfo.parse(recvRule);
        List<OpticalStatsticsInfo> transList = OpticalStatsticsInfo.parse(transRule);
        // 得到按照阀值排序的列表
        ValueOrder valueOrder = new ValueOrder();
        List<OpticalStatsticsInfo> valueOrderRecvList = new ArrayList<OpticalStatsticsInfo>(recvList);
        Collections.sort(valueOrderRecvList, valueOrder);
        List<OpticalStatsticsInfo> valueOrderTransList = new ArrayList<OpticalStatsticsInfo>(transList);
        Collections.sort(valueOrderTransList, valueOrder);
        // 按照告警级别进行排序
        Collections.sort(recvList);
        Collections.sort(transList);

        List<OpticalStatsticsInfo> recvStasticsList = transStatsticsList(valueOrderRecvList, recvList);
        List<OpticalStatsticsInfo> transStasticsList = transStatsticsList(valueOrderTransList, transList);
        // 获取下级设备光功率信息
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltSubOpticalInfo> subOptcList = getRealtimeFacade(snmpParam.getIpAddress()).getOltSubOpticalInfo(
                snmpParam);
        StatisticsResult result = this.opticalAnalyse(recvStasticsList, transStasticsList, subOptcList);
        return result;
    }

    @Override
    public List<OltCurrentCmcInfo> getOltCurrentCmcInfo(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        return getRealtimeFacade(snmpParam.getIpAddress()).getOltCurrentCmcInfo(snmpParam);
    }

    @Override
    public List<ChannelCmNumInfo> getChannelCmNum(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        return getRealtimeFacade(snmpParam.getIpAddress()).getChannelCmNum(snmpParam);
    }

    @Override
    public OltSummaryInfo getOltSummaryInfo(Long entityId) {
        OltSummaryInfo summaryInfo = new OltSummaryInfo();
        // 加载基本信息
        summaryInfo.setBaseInfo(getOltBaseInfo(entityId));
        // 加载Sni口统计信息
        summaryInfo.setSniList(getSniTotalInfo(entityId));
        // 加载Olt Pon口统计信息
        summaryInfo.setPonTotalList(getPonTotalInfo(entityId));
        // 加载下级设备 统计信息
        summaryInfo.setSubTotalList(getSubTotalInfo(entityId));
        // 加载CM 统计信息
        // modify by fanzidong，如果是1.10版本，不支持CCMTS，则无需获取相关数据
        if (deviceVersionService.isFunctionSupported(entityId, "ccmts")) {
            summaryInfo.setCmTotal(getCmTotalInfo(entityId));
        }
        return summaryInfo;
    }

    private OltRealtimeFacade getRealtimeFacade(String ip) {
        return facadeFactory.getFacade(ip, OltRealtimeFacade.class);
    }

    /**
     * 在获取端口速率时组装索引
     * 
     * @param slot
     * @param port
     * @return
     */
    private long parseIndex(long slot, long port) {
        if (slot == 0) {
            return port << 8;
        } else {
            return (1L << 24) + (slot << 16) + (port << 8);
        }
    }

    /**
     * 将获取的CMC信息转化为以MAC地址为key的Map,避免多次循环查找
     * 
     * @param cmcList
     * @return
     */
    private Map<String, OltCurrentCmcInfo> transCmcMap(List<OltCurrentCmcInfo> cmcList) {
        Map<String, OltCurrentCmcInfo> cmcMap = new HashMap<String, OltCurrentCmcInfo>();
        for (OltCurrentCmcInfo cmcInfo : cmcList) {
            cmcMap.put(cmcInfo.getMacAddress(), cmcInfo);
        }
        return cmcMap;
    }

    /**
     * 将获取的阀值转化为分区间列表
     * 
     * @param valueOrderList
     * @param levelOrderList
     * @return
     */
    private List<OpticalStatsticsInfo> transStatsticsList(List<OpticalStatsticsInfo> valueOrderList,
            List<OpticalStatsticsInfo> levelOrderList) {
        List<OpticalStatsticsInfo> stasticsList = new ArrayList<OpticalStatsticsInfo>();
        OpticalStatsticsInfo stasticsInfo = null;
        Integer startValue = null;
        int recvSize = valueOrderList.size();
        boolean includeStart = false;
        for (int i = 0; i < recvSize; i++) {
            OpticalStatsticsInfo info = valueOrderList.get(i);
            Integer thresholdValue = info.getValue();
            stasticsInfo = new OpticalStatsticsInfo();
            stasticsInfo.setStart(startValue);
            stasticsInfo.setEnd(thresholdValue);
            stasticsInfo.setIncludeStart(includeStart);
            // 记录下一个区间是否包含起始值
            includeStart = OpticalStatsticsInfo.setIfInclude(info.getOperate(), stasticsInfo);
            boolean noMatch = false;
            for (OpticalStatsticsInfo levelInfo : levelOrderList) {
                if (OpticalStatsticsInfo.satisfy(levelInfo, thresholdValue)) {
                    stasticsInfo.setColor(OpticalStatsticsInfo.getColor(levelInfo.getLevel()));
                    stasticsInfo.setLevel(levelInfo.getLevel());
                    noMatch = true;
                    break;
                }
            }
            // 不满足任何阀值，认为该区间是正常的
            if (!noMatch) {
                stasticsInfo.setColor("#00CE0A");
                stasticsInfo.setLevel(OpticalStatsticsInfo.OPTICAL_NORMAL_LEVEL);
            }
            stasticsList.add(stasticsInfo);
            // 循环结束的时候将下一个区间的起始值置为当前值
            startValue = thresholdValue;
            // 当处理最后一个值的时候，需要再加上最后的区间
            if (i == recvSize - 1) {
                stasticsInfo = new OpticalStatsticsInfo();
                stasticsInfo.setStart(thresholdValue);
                stasticsInfo.setIncludeStart(includeStart);
                int endValue = ++thresholdValue;
                boolean matchFlag = false;
                for (OpticalStatsticsInfo levelInfo : levelOrderList) {
                    if (OpticalStatsticsInfo.satisfy(levelInfo, endValue)) {
                        stasticsInfo.setColor(OpticalStatsticsInfo.getColor(levelInfo.getLevel()));
                        stasticsInfo.setLevel(levelInfo.getLevel());
                        matchFlag = true;
                        break;
                    }
                }
                // 不满足任何阀值，认为该区间是正常的
                if (!matchFlag) {
                    stasticsInfo.setColor("#00CE0A");
                    stasticsInfo.setLevel(OpticalStatsticsInfo.OPTICAL_NORMAL_LEVEL);
                }
                stasticsInfo.setIncludeEnd(false);
                stasticsInfo.setEnd(null);
                stasticsList.add(stasticsInfo);
            }
        }
        return stasticsList;
    }

    /**
     * 统计下级设备光功率在各区间的值
     * 
     * @param recvStasticsList
     * @param transStasticsList
     * @param subOptcList
     * @return
     */
    private StatisticsResult opticalAnalyse(List<OpticalStatsticsInfo> recvStasticsList,
            List<OpticalStatsticsInfo> transStasticsList, List<OltSubOpticalInfo> subOptcList) {
        StatisticsResult result = new StatisticsResult();
        int rxNa = 0, txNa = 0;
        for (OltSubOpticalInfo $thd : subOptcList) {
            Integer rxPower = $thd.getRecvOpticalPower();
            Integer txPower = $thd.getTransOpticalPower();
            // 没有获取到,或者采集值为0,都认为是异常情况或者不在线
            if (txPower == null || txPower == 0) {
                txNa++;
            }
            if (rxPower == null || txPower == 0) {
                rxNa++;
            }
            if (txPower == null || rxPower == null) {
                continue;
            }
            for (OpticalStatsticsInfo statsticsInfo : recvStasticsList) {
                if (statsticsInfo.getStart() == null) {
                    // 起始值不存在(为负无穷)的情况，再考虑是否包含结束值
                    if (statsticsInfo.isIncludeEnd() && rxPower <= statsticsInfo.getEndCompare()) {
                        statsticsInfo.addMatchNum();
                    } else if (rxPower < statsticsInfo.getEndCompare()) {
                        statsticsInfo.addMatchNum();
                    }
                } else if (statsticsInfo.getEnd() == null) {
                    // 结束值不存在(为正无穷)的情况，再考虑是否包含起始值
                    if (statsticsInfo.isIncludeStart() && rxPower >= statsticsInfo.getStartCompare()) {
                        statsticsInfo.addMatchNum();
                    } else if (rxPower > statsticsInfo.getStartCompare()) {
                        statsticsInfo.addMatchNum();
                    }
                } else {
                    // 起始值和结束值都存在的情况，再考虑是否包含起始值和结束值
                    if (statsticsInfo.isIncludeStart() && rxPower >= statsticsInfo.getStartCompare()) {
                        if (statsticsInfo.isIncludeEnd() && rxPower <= statsticsInfo.getEndCompare()) {
                            statsticsInfo.addMatchNum();
                        } else if (rxPower < statsticsInfo.getEndCompare()) {
                            statsticsInfo.addMatchNum();
                        }
                    } else if (rxPower > statsticsInfo.getStartCompare()) {
                        if (statsticsInfo.isIncludeEnd() && rxPower <= statsticsInfo.getEndCompare()) {
                            statsticsInfo.addMatchNum();
                        } else if (rxPower < statsticsInfo.getEndCompare()) {
                            statsticsInfo.addMatchNum();
                        }
                    }
                }
            }
            for (OpticalStatsticsInfo statsticsInfo : transStasticsList) {
                if (statsticsInfo.getStart() == null) {
                    // 起始值不存在(为负无穷)的情况，再考虑是否包含结束值
                    if (statsticsInfo.isIncludeEnd() && txPower <= statsticsInfo.getEndCompare()) {
                        statsticsInfo.addMatchNum();
                    } else if (txPower < statsticsInfo.getEndCompare()) {
                        statsticsInfo.addMatchNum();
                    }
                } else if (statsticsInfo.getEnd() == null) {
                    // 结束值不存在(为正无穷)的情况，再考虑是否包含起始值
                    if (statsticsInfo.isIncludeStart() && txPower >= statsticsInfo.getStartCompare()) {
                        statsticsInfo.addMatchNum();
                    } else if (txPower > statsticsInfo.getStartCompare()) {
                        statsticsInfo.addMatchNum();
                    }
                } else {
                    // 起始值和结束值都存在的情况，再考虑是否包含起始值和结束值
                    if (statsticsInfo.isIncludeStart() && txPower >= statsticsInfo.getStartCompare()) {
                        if (statsticsInfo.isIncludeEnd() && txPower <= statsticsInfo.getEndCompare()) {
                            statsticsInfo.addMatchNum();
                        } else if (txPower < statsticsInfo.getEndCompare()) {
                            statsticsInfo.addMatchNum();
                        }
                    } else if (txPower > statsticsInfo.getStartCompare()) {
                        if (statsticsInfo.isIncludeEnd() && txPower <= statsticsInfo.getEndCompare()) {
                            statsticsInfo.addMatchNum();
                        } else if (txPower < statsticsInfo.getEndCompare()) {
                            statsticsInfo.addMatchNum();
                        }
                    }
                }
            }
        }
        result.setRecvStasticsList(recvStasticsList);
        result.setTransStasticsList(transStasticsList);
        result.setRecvErrorNum(rxNa);
        result.setTransErrorNum(txNa);
        return result;
    }

}
