package com.topvision.ems.mobile.action;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.domain.QualityRange;
import com.topvision.ems.cmc.ccmts.domain.QualityRangeResult;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcChannelService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.service.CmListService;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamBaseInfo;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.exception.RefreshDataException;
import com.topvision.ems.cmc.optical.domain.CmcOpReceiverStatus;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverDcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverInputInfo;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfPort;
import com.topvision.ems.cmc.optical.service.CmcOpticalReceiverService;
import com.topvision.ems.cmc.opticalreceiver.domain.OpticalReceiverData;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsSysDorType;
import com.topvision.ems.cmc.opticalreceiver.service.OpticalReceiverService;
import com.topvision.ems.cmc.spectrum.domain.UpChannelSpectrum;
import com.topvision.ems.cmc.spectrum.service.SpectrumHeartbeatService;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.congfigbackup.service.ConfigBackupService;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.mobile.domain.BaiduMapInfo;
import com.topvision.ems.mobile.domain.CmtsDownChannel;
import com.topvision.ems.mobile.domain.CmtsDownChannelWithPortId;
import com.topvision.ems.mobile.domain.CmtsInCmtsList;
import com.topvision.ems.mobile.domain.CmtsInfo;
import com.topvision.ems.mobile.domain.CmtsUpChannel;
import com.topvision.ems.mobile.domain.CmtsUpChannelWithPortId;
import com.topvision.ems.mobile.domain.OpticalReceiverInfo;
import com.topvision.ems.mobile.service.MCmtsService;
import com.topvision.ems.mobile.util.MobileUtil;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.util.FtpClientUtil;
import com.topvision.platform.util.StringUtil;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("mCmtsAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MCmtsAction extends BaseAction {
    private static final long serialVersionUID = 5269661531075934657L;
    private static final String STRING_TEMPLATE_NEWFILE = "{0}META-INF{1}startConfig{1}{2}{1}{3}";
    @Resource(name = "mCmtsService")
    private MCmtsService mCmtsService;
    @Autowired
    private CmcService cmcService;
    @Resource(name = "cmcOpticalReceiverService")
    private CmcOpticalReceiverService cmcOpservice;
    @Resource(name = "entityTypeService")
    private EntityTypeService entityTypeService;
    @Autowired
    private CmcUpChannelService cmcUpChannelService;
    @Autowired
    private DiscoveryService<DiscoveryData> discoveryService;
    @Autowired
    private OpticalReceiverService opticalReceiverService;
    @Autowired
    private ConfigBackupService configBackupService;
    @Resource(name = "cmcDownChannelService")
    private CmcDownChannelService cmcDownChannelService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    @Resource(name = "cmListService")
    private CmListService cmListService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private CmcChannelService cmcChannelService;
    
    @Value("${fileautosave.cc.config:config}")
    private String CC_CONFIG;
    private Long cmtsId;
    private Long entityId;
    private Long cmcPortId;
    private String cmtsNameOrMac;
    private CmcAttribute cmcAttribute;
    private String spectrumWidth;
    private CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo;
    private CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo;
    private Long channelFrequency;
    private Long channelWidth;
    private Integer ifAdminStatus;
    private Integer recPower;
    private Long downChannelPower;
    private Long module;
    private Boolean isCcWithoutAgent;
    // private Integer typeId;
    private String address;
    private Double latitude;
    private Double longitude;
    private String remoteFilePathName;
    private String ftpServer;
    private Integer ftpPort;
    private String ftpUserName;
    private String ftpPassword;
    private String ftpFile;
    private String aliasName;
    private Integer channelAdminstatus;
    private Integer paraChannelId;
    private List<Long> channelIndexs;

    public String getCmtsList() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (cmtsNameOrMac != null && !"".equals(cmtsNameOrMac.trim())) {
            // mysql中下划线是特殊的，like的时候必须转义
            if (cmtsNameOrMac.contains("_")) {
                cmtsNameOrMac = cmtsNameOrMac.replace("_", "\\_");
        }
            map.put("cmtsNameOrMac", cmtsNameOrMac.trim());
            String formatQueryMac = MacUtils.formatQueryMac(cmtsNameOrMac);
        if (formatQueryMac.indexOf(":") == -1) {
            map.put("queryMacWithoutSplit", formatQueryMac);
        }
        map.put("queryContentMac", formatQueryMac);
        }
        // 将输入的特殊符号直接替换了，查询不准确
        /*
         * if (cmtsNameOrMac != null) { map.put("cmtsNameOrMac",
         * MobileUtil.convertQueryContext(cmtsNameOrMac)); } String formatQueryMac =
         * MacUtils.formatQueryMac(MobileUtil.convertQueryContext(cmtsNameOrMac)); if
         * (formatQueryMac.indexOf(":") == -1) { map.put("queryMacWithoutSplit", formatQueryMac); }
         * map.put("queryContentMac", formatQueryMac);
         */

        map.put("start", start);
        map.put("limit", limit);
        List<CmtsInCmtsList> list = mCmtsService.getCmtsList(map);
        for (CmtsInCmtsList cmts : list) {
            Boolean versionRead = deviceVersionService.isFunctionSupported(cmts.getCmtsId(), "opticalReceiverRead");
            Boolean versionNew = deviceVersionService.isFunctionSupported(cmts.getCmtsId(), "opticalReceiverNew");
            Boolean isSuppertOptical = false;
            if (versionRead || versionNew) {
                if (cmts.getTopCcmtsSysDorType() != null && !"".equalsIgnoreCase(cmts.getTopCcmtsSysDorType())) {
                    isSuppertOptical = true;
                }
            }
            cmts.setIsSupportOptical(isSuppertOptical);// 是否支持光机

        }
        Long totalCount = mCmtsService.getCmtsListCount(map);
        JSONObject json = new JSONObject();
        json.put("totalCount", totalCount);
        json.put("data", list);
        json.write(response.getWriter());
        return NONE;
    }

    public String getCmtsListWithRegion() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        if (cmtsNameOrMac != null && !"".equals(cmtsNameOrMac.trim())) {
            // mysql中下划线是特殊的，like的时候必须转义
            if (cmtsNameOrMac.contains("_")) {
                cmtsNameOrMac = cmtsNameOrMac.replace("_", "\\_");
            }
            map.put("cmtsNameOrMac", cmtsNameOrMac.trim());
            String formatQueryMac = MacUtils.formatQueryMac(cmtsNameOrMac);
            if (formatQueryMac.indexOf(":") == -1) {
                map.put("queryMacWithoutSplit", formatQueryMac);
            }
            map.put("queryContentMac", formatQueryMac);
        }

        map.put("start", start);
        map.put("limit", limit);
        List<CmtsInCmtsList> list = mCmtsService.getCmtsListWithRegion(map);
        for (CmtsInCmtsList cmts : list) {
            Boolean versionRead = deviceVersionService.isFunctionSupported(cmts.getCmtsId(),"opticalReceiverRead");
            Boolean versionNew = deviceVersionService.isFunctionSupported(cmts.getCmtsId(),"opticalReceiverNew");
            Boolean isSuppertOptical = false;
            Boolean isSupportFOptical = false;
            if (versionRead || versionNew) {
                if (cmts.getTopCcmtsSysDorType() != null && !"".equalsIgnoreCase(cmts.getTopCcmtsSysDorType())) {
                    isSuppertOptical = true;
                    if (TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_FFA.equalsIgnoreCase(cmts.getTopCcmtsSysDorType())
                            || TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_FFB
                                    .equalsIgnoreCase(cmts.getTopCcmtsSysDorType())) {
                        isSupportFOptical = true;
                    }
                }
            }
            cmts.setIsSupportOptical(isSuppertOptical);// 是否支持光机
            cmts.setIsFOptical(isSupportFOptical);// 是否支持F型光机
            if (entityTypeService.isCcmtsWithoutAgent(cmts.getTypeId())) {
                cmts.setIsCcWithoutAgent(true);
            } else {
                cmts.setIsCcWithoutAgent(false);
            }
            if (entityTypeService.isCcmtsWithAgent(cmts.getTypeId())) {
                cmts.setIsCcWithAgent(true);
            } else {
                cmts.setIsCcWithAgent(false);
            }
        }
        Long totalCount = mCmtsService.getCmtsListCountWithRegion(map);
        JSONObject json = new JSONObject();
        json.put("totalCount", totalCount);
        json.put("data", list);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取光机的信息
     * 
     * @return
     * @throws IOException
     */
    public String getOpticalReceiverInfo() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        String result = null;
        try {
            cmcOpservice.refreshOpReceiverInfo(cmtsId);
            CmcOpReceiverStatus status = cmcOpservice.getOpticalReceiverStatus(cmtsId);
            OpticalReceiverInfo opticalInfo = new OpticalReceiverInfo();// 返回结果
            List<CmcOpReceiverInputInfo> inputInfoList = status.getInputInfoList();
            if (inputInfoList != null && inputInfoList.size() > 0) {
                opticalInfo.setReceiverPowerA(inputInfoList.get(0).getInputPower());// 接收光功率A
                if (inputInfoList.size() > 1) {
                    opticalInfo.setReceiverPowerB(inputInfoList.get(1).getInputPower());// 接收光功率B
                }
            }
            opticalInfo.setSwitchControl(Integer.valueOf(status.getSwitchCfg().getSwitchControl()));// 光输入切换
            opticalInfo.setSwitchThres(status.getSwitchCfg().getSwitchThres());// 转换门限
            List<CmcOpReceiverDcPower> dcPowers = status.getDcPower();
            if (dcPowers != null && dcPowers.size() > 0) {
                opticalInfo.setDcPower1(dcPowers.get(0).getPowerVoltage()); // 直流电源(5V)
                if (dcPowers.size() > 1) {
                    opticalInfo.setDcPower2(dcPowers.get(1).getPowerVoltage());// 直流电源(12V)
                    if (dcPowers.size() > 2) {
                        opticalInfo.setDcPower3(dcPowers.get(2).getPowerVoltage());// 直流电源(24V)
                    }
                }
            }
            opticalInfo.setAcPower(status.getAcPowerVoltage());// 交流电源;
            opticalInfo.setOutputControl(Integer.valueOf(status.getRfCfg().getOutputControl()));// 下行链路开关
            List<CmcOpReceiverRfPort> rfPorts = status.getRfPort();
            if (rfPorts != null && rfPorts.size() > 0) {
                opticalInfo.setRfOutput1(rfPorts.get(0).getRfOutputLevel()); // 射频输出1
                if (rfPorts.size() > 1) {
                    opticalInfo.setRfOutput2(rfPorts.get(1).getRfOutputLevel());// 射频输出2
                    if (rfPorts.size() > 2) {
                        opticalInfo.setRfOutput3(rfPorts.get(2).getRfOutputLevel());// 射频输出3
                        if (rfPorts.size() > 3) {
                            opticalInfo.setRfOutput4(rfPorts.get(3).getRfOutputLevel());// 射频输出4
                        }
                    }
                }
            }
            opticalInfo.setOutputGainType(Integer.valueOf(status.getRfCfg().getOutputGainType()));// 增益类型
            opticalInfo.setOutputAGCOrigin(status.getRfCfg().getOutputAGCOrigin());// AGC起控光功率
            opticalInfo.setOutputRFlevelatt(status.getRfCfg().getOutputRFlevelatt());// 射频衰减量
            opticalInfo.setChannelNum(status.getChannelNum());// 载波频道数
            map.put("data", opticalInfo);
            result = "success";
        } catch (Exception e) {
            result = "fail";
        }
        map.put("result", result);
        writeDataToAjax(JSONObject.fromObject(map));
        return NONE;
    }

    public String getOpticalReceiverInfoOfFF() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        String result = null;
        try {
            OpticalReceiverData data = opticalReceiverService.getOpticalReceiverInfo(cmtsId);
            map.put("data", data);
            result = "success";
        } catch (Exception e) {
            result = "fail";
        }
        map.put("result", result);
        writeDataToAjax(JSONObject.fromObject(map));
        return NONE;
    }

    /**
     * 通过CMTS的ID获取其基本信息
     * 
     * @return
     * @throws IOException
     */
    public String getCmtsInfoById() throws IOException {

        CmtsInfo cmtsInfo = mCmtsService.getCmtsInfoById(cmtsId);
        JSONObject json = new JSONObject();
        if (cmtsInfo != null) {
            cmtsInfo = processCmtsInfo(cmtsInfo);
            Boolean versionRead = deviceVersionService.isFunctionSupported(cmtsId, "opticalReceiverRead");
            Boolean versionNew = deviceVersionService.isFunctionSupported(cmtsId, "opticalReceiverNew");
            Boolean isSuppertOptical = false;
            Boolean isSupportFOptical = false;
            if (versionRead || versionNew) {
                if (cmtsInfo.getTopCcmtsSysDorType() != null
                        && !"".equalsIgnoreCase(cmtsInfo.getTopCcmtsSysDorType())) {
                    isSuppertOptical = true;
                    if (TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_FFA.equalsIgnoreCase(cmtsInfo.getTopCcmtsSysDorType())
                            || TopCcmtsSysDorType.OPTICALRECEIVER_TYPE_FFB
                                    .equalsIgnoreCase(cmtsInfo.getTopCcmtsSysDorType())) {
                        isSupportFOptical = true;
                }
            }
            }
            cmtsInfo.setIsSupportOptical(isSuppertOptical);// 是否支持光机
            cmtsInfo.setIsFOptical(isSupportFOptical);
            if (entityTypeService.isCcmtsWithoutAgent(cmtsInfo.getTypeId())) {
                cmtsInfo.setIsCcWithoutAgent(true);
            } else {
                cmtsInfo.setIsCcWithoutAgent(false);
            }
            if (entityTypeService.isCcmtsWithAgent(cmtsInfo.getTypeId())) {
                cmtsInfo.setIsCcWithAgent(true);
            } else {
                cmtsInfo.setIsCcWithAgent(false);
            }
            Double cmcOptRecPower = mCmtsService.getCmcOptRecPower(cmtsId);
            json = JSONObject.fromObject(cmtsInfo);
            json.put("cmcOptRecPower", cmcOptRecPower);
            json.put("deviceExist", true);
        } else {
            json.put("deviceExist", false);
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 重新拓扑cc
     * 
     * @return
     */
    public String reTopoCmtsWithoutAgent() {
        JSONObject json = new JSONObject();
        Long entityId = cmcService.getEntityIdByCmcId(cmtsId);
        CmcAttribute ca = cmcService.getCmcAttributeByCmcId(cmtsId);
        try {
            cmcService.refreshCC(entityId, cmtsId, ca.getCmcDeviceStyle().intValue());
            json.put("result", 1);
        } catch (Exception e) {
            json.put("result", 0);
        }
        writeDataToAjax(json);
        return NONE;
    }

    public String reTopoCmtsAndCC() {
        JSONObject json = new JSONObject();
        Long entityId = cmcService.getEntityIdByCmcId(cmtsId);
        try {
            discoveryService.refresh(entityId);
            json.put("result", 1);
        } catch (Exception e) {
            json.put("result", 0);
        }
        writeDataToAjax(json);
        return NONE;
    }

    private CmtsInfo processCmtsInfo(CmtsInfo cmtsInfo) {
        // 处理在线状态
        if (cmtsInfo.getState().intValue() == 1) {
            cmtsInfo.setOnStatus(ResourcesUtil.getString("mobile.online"));
        } else {
            cmtsInfo.setOnStatus(ResourcesUtil.getString("mobile.offline"));
        }
        // 处理在线时长
        Long sysUpTime = 0L;
        if (cmtsInfo.getSysUpTime() != null) {
            sysUpTime = cmtsInfo.getSysUpTime() / 100
                    + (System.currentTimeMillis() - cmtsInfo.getDt().getTime()) / 1000;
            cmtsInfo.setSysUpTime(sysUpTime);
            cmtsInfo.setOnTime(CmcUtil.timeFormatToZh(sysUpTime));
        } else {
            // 防止未采集到在线时间
            cmtsInfo.setSysUpTime(-1L);
            cmtsInfo.setOnTime("--");
        }
        return cmtsInfo;
    }

    /**
     * 通过CMTS的ID获取其下行信道列表
     * 
     * @return
     * @throws IOException
     */
    public String getDownChannelsById() throws IOException {
        CmcAttribute cmcAttribute = cmcService.getCmcAttributeByCmcId(cmtsId);
        List<CmcDSIpqamBaseInfo> downChannelIPQAMList = new ArrayList<CmcDSIpqamBaseInfo>();
        if (entityTypeService.isCcmtsWithAgent(cmcAttribute.getCmcDeviceStyle())
                && cmcAttribute.compareVersion(CmcConstants.CMC_WEB_DOWNCHANNEL_VER_MIN) >= 0) {
            downChannelIPQAMList = cmcDownChannelService.getDownChannelIPQAMInfoList(cmtsId);
        }
        List<CmtsDownChannel> channels = mCmtsService.getDownChannelsById(cmtsId);
        for (CmtsDownChannel cmtsDownChannel : channels) {
            for (CmcDSIpqamBaseInfo cmcDSIpqamBaseInfo : downChannelIPQAMList) {
                if (cmcDSIpqamBaseInfo.getDocsIfDownChannelId() == cmtsDownChannel.getChanId()) {
                    cmtsDownChannel.setIfAdminStatus(cmcDSIpqamBaseInfo.getIfAdminStatus());
                    cmtsDownChannel.setEqam(true);
                }
            }
            cmtsDownChannel = processDownChannelData(cmtsDownChannel);
        }
        JSONArray json = JSONArray.fromObject(channels);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取含portid的下行信道列表信息
     * 
     * @param
     * @return String
     */
    public String getDownChannelsInfoById() throws IOException {
        CmcAttribute cmcAttribute = cmcService.getCmcAttributeByCmcId(cmtsId);
        List<CmcDSIpqamBaseInfo> downChannelIPQAMList = new ArrayList<CmcDSIpqamBaseInfo>();
        if (entityTypeService.isCcmtsWithAgent(cmcAttribute.getCmcDeviceStyle())
                && cmcAttribute.compareVersion(CmcConstants.CMC_WEB_DOWNCHANNEL_VER_MIN) >= 0) {
            downChannelIPQAMList = cmcDownChannelService.getDownChannelIPQAMInfoList(cmtsId);
        }
        List<CmtsDownChannelWithPortId> channels = mCmtsService.getDownChannelsInfoById(cmtsId);
        for (CmtsDownChannelWithPortId cmtsDownChannel : channels) {
            for (CmcDSIpqamBaseInfo cmcDSIpqamBaseInfo : downChannelIPQAMList) {
                if (cmcDSIpqamBaseInfo.getDocsIfDownChannelId() == cmtsDownChannel.getChanId()) {
                    cmtsDownChannel.setIfAdminStatus(cmcDSIpqamBaseInfo.getIfAdminStatus());
                    cmtsDownChannel.setEqam(true);
                }
            }
            cmtsDownChannel = processDownChannelData2(cmtsDownChannel);
        }
        Boolean supportIPQAM=false;
        if(entityTypeService.isCcmtsWithAgent(cmcAttribute.getCmcDeviceStyle())&&
                cmcAttribute.compareVersion(CmcConstants.CMC_WEB_DOWNCHANNEL_VER_MIN) >= 0){
            supportIPQAM=true;
        }
        JSONObject json = new JSONObject();
        json.put("channels", channels);
        json.put("supportIPQAM", supportIPQAM);
        writeDataToAjax(json);
        return NONE;
    }
    
    public String openDownChannel(){
        Map<String, Object> json = new HashMap<String, Object>();
        int status = 1;
        if (channelAdminstatus != null && channelAdminstatus == 3) {
            status = 3;//IPQAM
            String message = cmcDownChannelService.setChannelsAdminStatus(cmtsId, paraChannelId, status);
            json.put("message", message);
        } else {
            // 批量开启DOCSIS信道
            List<Long> failerList = cmcChannelService.batchChangeChannelAdminstatus(cmtsId, channelIndexs, status);
            if (failerList == null || failerList.size() == 0) {
                json.put("message", "success");
            } else {
                json.put("message", "fail");
                json.put("failureList", failerList);
            }
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }
    
    public String closeChannels() {
        // 批量关闭信道
        List<Long> failerList = cmcChannelService.batchChangeChannelAdminstatus(cmtsId, channelIndexs, 2);
        Map<String, Object> json = new HashMap<String, Object>();
        if (failerList == null || failerList.size() == 0) {
            json.put("message", "success");
        } else {
            json.put("message", "fail");
            json.put("failureList", failerList);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 在服务器端处理下行信道数据
     * 
     * @param downChannel
     * @return
     */
    private CmtsDownChannel processDownChannelData(CmtsDownChannel downChannel) {
        downChannel.setChannelId(String.valueOf(downChannel.getChanId()));
        if (downChannel.getFreq() != null) {
            downChannel.setChannelFreq(String.valueOf((float) downChannel.getFreq() / 1000000) + " MHz");
        } else {
            downChannel.setChannelFreq("--");
        }
        if (downChannel.getWidth() != null) {
            downChannel.setChannelWidth(String.valueOf((float) downChannel.getWidth() / 1000000) + " MHz");
        } else {
            downChannel.setChannelWidth("--");
        }
        if (downChannel.getDocsIfDownChannelPower() != null) {
            String powerUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
            Double transPower = UnitConfigConstant
                    .parsePowerValue((double) downChannel.getDocsIfDownChannelPower() / 10);
            downChannel.setTransPower(String.valueOf(transPower) + " " + powerUnit);
        }
        Integer mode = downChannel.getModel();
        if (mode != null && mode.intValue() < EponConstants.DOWN_MODULATION_TYPES.length) {
            downChannel.setChannelMode(EponConstants.DOWN_MODULATION_TYPES[mode]);
        } else {
            downChannel.setChannelMode("--");
        }
        Integer annex = downChannel.getAnnex();
        if (annex != null && annex.intValue() < EponConstants.ANNEXTYPES.length) {
            downChannel.setChannelAnnex(EponConstants.ANNEXTYPES[annex]);
        } else {
            downChannel.setChannelAnnex("--");
        }
        return downChannel;
    }

    private CmtsDownChannelWithPortId processDownChannelData2(CmtsDownChannelWithPortId downChannel) {
        downChannel.setChannelId(String.valueOf(downChannel.getChanId()));
        if (downChannel.getFreq() != null) {
            downChannel.setChannelFreq(String.valueOf((float) downChannel.getFreq() / 1000000) + " MHz");
        } else {
            downChannel.setChannelFreq("--");
        }
        if (downChannel.getWidth() != null) {
            downChannel.setChannelWidth(String.valueOf((float) downChannel.getWidth() / 1000000) + " MHz");
        } else {
            downChannel.setChannelWidth("--");
        }
        if (downChannel.getDocsIfDownChannelPower() != null) {
            String powerUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
            Double transPower = UnitConfigConstant
                    .parsePowerValue((double) downChannel.getDocsIfDownChannelPower() / 10);
            downChannel.setTransPower(String.valueOf(transPower) + " " + powerUnit);
        }
        Integer mode = downChannel.getModel();
        if (mode != null && mode.intValue() < EponConstants.DOWN_MODULATION_TYPES.length) {
            downChannel.setChannelMode(EponConstants.DOWN_MODULATION_TYPES[mode]);
        } else {
            downChannel.setChannelMode("--");
        }
        Integer annex = downChannel.getAnnex();
        if (annex != null && annex.intValue() < EponConstants.ANNEXTYPES.length) {
            downChannel.setChannelAnnex(EponConstants.ANNEXTYPES[annex]);
        } else {
            downChannel.setChannelAnnex("--");
        }
        return downChannel;
    }

    /**
     * 通过CMTS的ID获取其上行信道
     * 
     * @return
     */
    public String getUpChannelsById() throws IOException {
        List<CmtsUpChannel> channels = mCmtsService.getUpChannelsById(cmtsId);
        Long interval = mCmtsService.getErrorRateInterval(cmtsId);
        for (CmtsUpChannel upChannel : channels) {
            upChannel.setCollectInterval(interval);
            upChannel = this.processUpChannelData(upChannel);
        }
        JSONArray json = JSONArray.fromObject(channels);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取含portid的上行信道信息
     * 
     * @param
     * @return String
     */
    public String getUpChannelsInfoById() throws IOException {
        List<CmtsUpChannelWithPortId> channels = mCmtsService.getUpChannelsInfoById(cmtsId);
        Long interval = mCmtsService.getErrorRateInterval(cmtsId);
        for (CmtsUpChannelWithPortId upChannel : channels) {
            upChannel.setCollectInterval(interval);
            upChannel = this.processUpChannelData2(upChannel);
        }
        JSONArray json = JSONArray.fromObject(channels);
        json.write(response.getWriter());
        return NONE;
    }
    
    public String refreshUpChannel() {
        Map<String, String> message = new HashMap<String, String>();
        String result = "success";
        try {
            mCmtsService.refreshUpChannel(cmtsId);
        } catch (Exception e) {
            logger.debug("", e);
            result = "fail";
        }
        message.put("message", result);
        writeDataToAjax(message);
        return NONE;
    }
    
    public String refreshDownChannel() {
        Map<String, String> message = new HashMap<String, String>();
        String result = "success";
        try {
            mCmtsService.refreshDownChannel(cmtsId);
        } catch (Exception e) {
            logger.debug("", e);
            result = "fail";
        }
        message.put("message", result);
        writeDataToAjax(message);
        return NONE;
    }
    

    /**
     * 修改上行信道信息
     * 
     * @param
     * @return String
     */
    public String modifyUpChannel() {
        cmcUpChannelBaseShowInfo = cmcUpChannelService.getUpChannelBaseShowInfo(cmcPortId);
        String result = null;
        Long entityId = cmcService.getEntityIdByCmcId(cmtsId);
        Map<String, String> message = new HashMap<String, String>();
        cmcUpChannelBaseShowInfo.setCmcId(cmtsId);
        cmcUpChannelBaseShowInfo.setCmcPortId(cmcPortId);
        cmcUpChannelBaseShowInfo.setEntityId(entityId);
        cmcAttribute = cmcService.getCmcAttributeByCmcId(cmtsId);
        int cmcType = cmcAttribute.getCmcDeviceStyle().intValue();
        if (channelFrequency != null) {
            cmcUpChannelBaseShowInfo.setChannelFrequency(channelFrequency);
        }
        if (channelWidth != null) {
            cmcUpChannelBaseShowInfo.setChannelWidth(channelWidth);
        }
        if (module != null) {
            cmcUpChannelBaseShowInfo.setChannelModulationProfile(module);
        }
        if (ifAdminStatus != null) {
            cmcUpChannelBaseShowInfo.setIfAdminStatus(ifAdminStatus);
        }
        if (recPower != null) {
            cmcUpChannelBaseShowInfo.setDocsIf3SignalPower(recPower);
        }

        try {
            // 修改单条上行信道基本信息
            cmcUpChannelService.modifyUpChannelBaseShowInfo(cmcUpChannelBaseShowInfo, cmcType, null);
            result = "success";
        } catch (RefreshDataException rde) {
            result = "refreshError";
        } catch (Exception e) {
            logger.error("", e);
            result = "error";
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    /**
     * 修改下行信道信息
     * 
     * @param
     * @return String
     */
    public String modifyDownChannel() {
        String result = null;
        Long entityId = cmcService.getEntityIdByCmcId(cmtsId);
        Integer cmcType = cmcService.getCmcTypeByCmcId(cmtsId);
        Map<String, String> message = new HashMap<String, String>();
        CmcAttribute cmc = cmcService.getCmcAttributeByCmcId(cmtsId);
        cmcDownChannelBaseShowInfo = cmcDownChannelService.getDownChannelBaseShowInfo(cmc, cmcPortId);
        cmcDownChannelBaseShowInfo.setCmcId(cmtsId);
        cmcDownChannelBaseShowInfo.setCmcPortId(cmcPortId);
        cmcDownChannelBaseShowInfo.setEntityId(entityId);
        if (channelFrequency != null) {
            cmcDownChannelBaseShowInfo.setDocsIfDownChannelFrequency(channelFrequency);
        }
        if (channelWidth != null) {
            cmcDownChannelBaseShowInfo.setDocsIfDownChannelWidth(channelWidth.intValue());
        }
        if (module != null) {
            cmcDownChannelBaseShowInfo.setDocsIfDownChannelModulation(module.intValue());
        }
        if (downChannelPower != null) {
            cmcDownChannelBaseShowInfo.setDocsIfDownChannelPower(downChannelPower);
        }
        if (ifAdminStatus != null) {
            cmcDownChannelBaseShowInfo.setIfAdminStatus(ifAdminStatus);
        }
        try {
            cmcDownChannelService.modifyDownChannelBaseShowInfo(cmcDownChannelBaseShowInfo, cmcType);
            result = "success";
        } catch (Exception e) {
            result = "error";
            logger.debug("", e);
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    /**
     * 在服务器端处理上行信道信息
     * 
     * @param upChannel
     * @return
     */
    private CmtsUpChannel processUpChannelData(CmtsUpChannel upChannel) {
        upChannel.setChannelId(String.valueOf(upChannel.getChanId()));
        if (upChannel.getFreq() != null) {
            upChannel.setChannelFreq(String.valueOf((float) upChannel.getFreq() / 1000000) + " MHz");
        } else {
            upChannel.setChannelFreq("--");
        }
        if (upChannel.getWidth() != null) {
            upChannel.setChannelWidth(String.valueOf((float) upChannel.getWidth() / 1000000) + " MHz");
        } else {
            upChannel.setChannelWidth("--");
        }
        if (upChannel.getSnr() != null) {
            upChannel.setChannelSnr(String.valueOf((float) upChannel.getSnr() / 10) + " dB");
        } else {
            upChannel.setChannelSnr("--");
        }
        if (upChannel.getDocsIf3SignalPower() != null) {
            String powerUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
            Double recvPower = UnitConfigConstant.parsePowerValue((double) upChannel.getDocsIf3SignalPower() / 10);
            upChannel.setRecvPower(String.valueOf(recvPower) + " " + powerUnit);
        } else {
            upChannel.setRecvPower("--");
        }
        Integer mode = upChannel.getModel();
        if (mode != null && mode.intValue() < EponConstants.UP_MODULATION_PROFILETYPES.length) {
            upChannel.setChannelMode(EponConstants.UP_MODULATION_PROFILETYPES[mode]);
        }
        // 处理误码率,需要处理未采集到标识为-1的情况
        StringBuilder errorRate = new StringBuilder();
        if (upChannel.getCcerRate() != null && upChannel.getCcerRate() != -1) {
            errorRate.append(upChannel.getCcerRate() + "%");
        } else {
            errorRate.append("--");
        }
        errorRate.append("/");
        if (upChannel.getUcerRate() != null && upChannel.getUcerRate() != -1) {
            errorRate.append(upChannel.getUcerRate() + "%");
        } else {
            errorRate.append("--");
        }
        upChannel.setErrorRateString(errorRate.toString());
        return upChannel;
    }

    private CmtsUpChannelWithPortId processUpChannelData2(CmtsUpChannelWithPortId upChannel) {
        upChannel.setChannelId(String.valueOf(upChannel.getChanId()));
        if (upChannel.getFreq() != null) {
            upChannel.setChannelFreq(String.valueOf((float) upChannel.getFreq() / 1000000) + " MHz");
        } else {
            upChannel.setChannelFreq("--");
        }
        if (upChannel.getWidth() != null) {
            upChannel.setChannelWidth(String.valueOf((float) upChannel.getWidth() / 1000000) + " MHz");
        } else {
            upChannel.setChannelWidth("--");
        }
        if (upChannel.getSnr() != null) {
            upChannel.setChannelSnr(String.valueOf((float) upChannel.getSnr() / 10) + " dB");
        } else {
            upChannel.setChannelSnr("--");
        }
        if (upChannel.getDocsIf3SignalPower() != null) {
            String powerUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
            Double recvPower = UnitConfigConstant.parsePowerValue((double) upChannel.getDocsIf3SignalPower() / 10);
            upChannel.setRecvPower(String.valueOf(recvPower) + " " + powerUnit);
        } else {
            upChannel.setRecvPower("--");
        }
        Integer mode = upChannel.getModel();
        if (mode != null && mode.intValue() < EponConstants.UP_MODULATION_PROFILETYPES.length) {
            upChannel.setChannelMode(EponConstants.UP_MODULATION_PROFILETYPES[mode]);
        }
        // 处理误码率,需要处理未采集到标识为-1的情况
        StringBuilder errorRate = new StringBuilder();
        if (upChannel.getCcerRate() != null && upChannel.getCcerRate() != -1) {
            errorRate.append(upChannel.getCcerRate() + "%");
        } else {
            errorRate.append("--");
        }
        errorRate.append("/");
        if (upChannel.getUcerRate() != null && upChannel.getUcerRate() != -1) {
            errorRate.append(upChannel.getUcerRate() + "%");
        } else {
            errorRate.append("--");
        }
        upChannel.setErrorRateString(errorRate.toString());
        return upChannel;
    }

    public String getCmtsSignalQuality() throws IOException {
        QualityRangeResult qualityRangeResult = mCmtsService.getSignalQuality(cmtsId);
        JSONObject json = processSQ(qualityRangeResult);
        json.write(response.getWriter());
        return NONE;
    }

    public String getCmtsSpectrum() throws IOException, ParseException {
        List<UpChannelSpectrum> upChannelSpectrumList = mCmtsService.getCmtsSpectrum(cmtsId, spectrumWidth);
        JSONArray json = JSONArray.fromObject(upChannelSpectrumList);
        json.write(response.getWriter());
        return NONE;
    }

    public String getCmtsSpectrum2() throws IOException, ParseException {
        String powerLevelUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
        List<UpChannelSpectrum> upChannelSpectrumList = mCmtsService.getCmtsSpectrum(cmtsId, spectrumWidth);

        // for(UpChannelSpectrum us:upChannelSpectrumList){
        // String tempMin[]=us.getMin().split("/");
        // Float freqMin=Float.parseFloat(tempMin[1])/1000000;
        // String tempMax[]=us.getMax().split("/");
        // Float freqMax=Float.parseFloat(tempMax[1])/1000000;
        // if(powerLevelUnit.equals("dBmV")){
        // us.setMin(tempMin[0]+"/"+freqMin.toString());
        // us.setMax(tempMax[0]+"/"+freqMax.toString());
        // }else{
        // Float power=Float.parseFloat(tempMin[0])+60;
        // us.setMin(power+"/"+freqMin.toString());
        // Float power2=Float.parseFloat(tempMax[0])+60;
        // us.setMax(power2+"/"+freqMax.toString());
        // us.setAvg(""+(Float.parseFloat(us.getAvg())+60));
        // }
        // }
        JSONArray json = JSONArray.fromObject(upChannelSpectrumList);
        json.write(response.getWriter());
        return NONE;
    }

    private JSONObject processSQ(QualityRangeResult qualityRangeResult) {
        List<QualityRange> upPower = qualityRangeResult.getUsTxPowerQualityRange();
        List<QualityRange> upSnr = qualityRangeResult.getUsSnrQualityRange();
        List<QualityRange> downPower = qualityRangeResult.getDsRxPowerQualityRange();
        List<QualityRange> downSnr = qualityRangeResult.getDsSnrQualityRange();
        JSONArray downPowerArray = new JSONArray();
        JSONArray downSnrArray = new JSONArray();
        JSONArray upPowerArray = new JSONArray();
        JSONArray upSnrArray = new JSONArray();
        for (int i = 1; i < 8; i++) {
            upPowerArray.add(upPower.get(i).getCount());
            upSnrArray.add(upSnr.get(i).getCount());
            downPowerArray.add(downPower.get(i).getCount());
            downSnrArray.add(downSnr.get(i).getCount());
        }
        upPowerArray.add(upPower.get(0).getCount());
        upSnrArray.add(upSnr.get(0).getCount());
        downPowerArray.add(downPower.get(0).getCount());
        downSnrArray.add(downSnr.get(0).getCount());
        JSONObject json = new JSONObject();
        json.put("usTxPowerQualityRange", upPowerArray);
        json.put("dsRxPowerQualityRange", downPowerArray);
        json.put("usSnrQualityRange", upSnrArray);
        json.put("dsSnrQualityRange", downSnrArray);
        return json;
    }

    /**
     * 刷新cc的cm列表
     * 
     * @param
     * @return String
     */
    public String refreshCmList() {
        Map<String, String> message = new HashMap<String, String>();
        String result = "success";
        Long entityId = cmcService.getEntityIdByCmcId(cmtsId);
        try {
            cmListService.refreshCmList(entityId, cmtsId);
        } catch (Exception e) {
            logger.debug("", e);
            result = "fail";
        }
        message.put("message", result);
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    public String transMaptoDB() {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, String> message = new HashMap<String, String>();
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.mobile.resources");
        CmcAttribute ca = cmcService.getCmcAttributeByCmcId(cmtsId);
        String result = "success";
        map.put("entityId", cmtsId);
        map.put("typeId", ca.getCmcDeviceStyle());
        map.put("address", address);
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        String fileName = ca.getTopCcmtsSysMacAddr().replaceAll(":", "-") + "_dingdian.txt";
        FileWriter fw = null;
        File file = new File(fileName);
        try {
            mCmtsService.saveMapDataToDB(map);
            fw = new FileWriter(fileName);
            fw.write(resourceManager.getString("mobile.alias") + ":" + ca.getNmName() + "\r\n");
            fw.write(resourceManager.getString("mobile.manageIp") + ":" + ca.getManageIp() + "\r\n");
            fw.write("mac:" + ca.getTopCcmtsSysMacAddr() + "\r\n");
            fw.write(resourceManager.getString("mobile.address") + ":" + address + "\r\n");
            fw.write(resourceManager.getString("mobile.latitude") + ":" + latitude + "\r\n");
            fw.write(resourceManager.getString("mobile.longitude") + ":" + longitude + "\r\n");
            fw.close();
            boolean uploadFlag = false;
            FtpClientUtil ftpClientUtil = new FtpClientUtil();
            // boolean connected = ftpClientUtil.connect("172.17.2.14", 21, "admin","admin");
            boolean connected = ftpClientUtil.connect(ftpServer, ftpPort, ftpUserName, ftpPassword);
            if (connected) {
                uploadFlag = ftpClientUtil.uploadFile(file, ftpFile + "/" + fileName);
            }
            ftpClientUtil.disconnect();
        } catch (Exception e) {
            logger.debug("", e);
            result = "fail";
        }
        if (file.exists()) {
            file.delete();
        }
        message.put("message", result);
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    public String modifyCmtsLocation() {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, String> message = new HashMap<String, String>();
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.mobile.resources");
        CmcAttribute ca = cmcService.getCmcAttributeByCmcId(cmtsId);
        String result = "success";
        BaiduMapInfo bmi = mCmtsService.getBaiduMapInfo(cmtsId);
        map.put("entityId", cmtsId);
        map.put("address", address);
        if (bmi != null) {
            map.put("latitude", bmi.getLatitude());
            map.put("longitude", bmi.getLongitude());
        } else {
            map.put("latitude", 0.0);
            map.put("longitude", 0.0);
        }
        String fileName = ca.getTopCcmtsSysMacAddr().replaceAll(":", "-") + "_dingdian.txt";
        FileWriter fw = null;
        File file = new File(fileName);
        try {
            mCmtsService.modifyCmcLocation(map);
            fw = new FileWriter(fileName);
            fw.write(resourceManager.getString("mobile.alias") + ":" + ca.getNmName() + "\r\n");
            fw.write(resourceManager.getString("mobile.manageIp") + ":" + ca.getManageIp() + "\r\n");
            fw.write("mac:" + ca.getTopCcmtsSysMacAddr() + "\r\n");
            fw.write(resourceManager.getString("mobile.address") + ":" + address + "\r\n");
            if (bmi != null) {
                fw.write(resourceManager.getString("mobile.latitude") + ":" + bmi.getLatitude() + "\r\n");
                fw.write(resourceManager.getString("mobile.longitude") + ":" + bmi.getLongitude() + "\r\n");
            } else {
                fw.write(resourceManager.getString("mobile.latitude") + ":" + 0.0 + "\r\n");
                fw.write(resourceManager.getString("mobile.longitude") + ":" + 0.0 + "\r\n");
            }
            fw.close();
            boolean uploadFlag = false;
            FtpClientUtil ftpClientUtil = new FtpClientUtil();
            // boolean connected = ftpClientUtil.connect("172.17.2.14", 21, "admin","admin");
            boolean connected = ftpClientUtil.connect(ftpServer, ftpPort, ftpUserName, ftpPassword);
            if (connected) {
                uploadFlag = ftpClientUtil.uploadFile(file, ftpFile + "/" + fileName);
            }
            ftpClientUtil.disconnect();
        } catch (Exception e) {
            logger.debug("", e);
            result = "fail";
        }
        if (file.exists()) {
            file.delete();
        }
        message.put("message", result);
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    public String uploadCmtsConfig() {
        String result = "success";
        Map<String, String> message = new HashMap<String, String>();
        CmcAttribute ca = cmcService.getCmcAttributeByCmcId(cmtsId);
        try {
            configBackupService.downloadConfigFile(cmtsId, ca.getCmcDeviceStyle(), ca.getManageIp());
            String fileName = ca.getTopCcmtsSysMacAddr().replaceAll(":", "-") + "_config";
            String path = StringUtil.format(STRING_TEMPLATE_NEWFILE, SystemConstants.ROOT_REAL_PATH, File.separator,
                    cmtsId, CC_CONFIG);
            File newFile = new File(path);
            boolean uploadFlag = false;
            FtpClientUtil ftpClientUtil = new FtpClientUtil();
            boolean connected = ftpClientUtil.connect(ftpServer, ftpPort, ftpUserName, ftpPassword);
            if (connected) {
                uploadFlag = ftpClientUtil.uploadFile(newFile, ftpFile + "/" + fileName);
            }
            if (uploadFlag == false) {
                result = "fail";
            }
            ftpClientUtil.disconnect();
        } catch (Exception e) {
            result = "fail";
            logger.error("", e);
        }
        message.put("message", result);
        writeDataToAjax(JSONObject.fromObject(message));
        return NONE;
    }

    /**
     * 修改设备别名
     * 
     * @param
     * @return String
     */
    public String modifyEntityName() {
        entityService.renameEntity(entityId, aliasName);
        return NONE;
    }

    public Long getCmtsId() {
        return cmtsId;
    }

    public void setCmtsId(Long cmtsId) {
        this.cmtsId = cmtsId;
    }

    public String getSpectrumWidth() {
        return spectrumWidth;
    }

    public void setSpectrumWidth(String spectrumWidth) {
        this.spectrumWidth = spectrumWidth;
    }

    public String getCmtsNameOrMac() {
        return cmtsNameOrMac;
    }

    public void setCmtsNameOrMac(String cmtsNameOrMac) {
        this.cmtsNameOrMac = cmtsNameOrMac;
    }

    public Long getCmcPortId() {
        return cmcPortId;
    }

    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }

    public CmcUpChannelBaseShowInfo getCmcUpChannelBaseShowInfo() {
        return cmcUpChannelBaseShowInfo;
    }

    public void setCmcUpChannelBaseShowInfo(CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo) {
        this.cmcUpChannelBaseShowInfo = cmcUpChannelBaseShowInfo;
    }

    public Long getChannelFrequency() {
        return channelFrequency;
    }

    public void setChannelFrequency(Long channelFrequency) {
        this.channelFrequency = channelFrequency;
    }

    public Long getChannelWidth() {
        return channelWidth;
    }

    public void setChannelWidth(Long channelWidth) {
        this.channelWidth = channelWidth;
    }

    public Integer getIfAdminStatus() {
        return ifAdminStatus;
    }

    public void setIfAdminStatus(Integer ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }

    public Long getModule() {
        return module;
    }

    public void setModule(Long module) {
        this.module = module;
    }

    public Boolean getIsCcWithoutAgent() {
        return isCcWithoutAgent;
    }

    public void setIsCcWithoutAgent(Boolean isCcWithoutAgent) {
        this.isCcWithoutAgent = isCcWithoutAgent;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getRecPower() {
        return recPower;
    }

    public void setRecPower(Integer recPower) {
        this.recPower = recPower;
    }

    public Long getDownChannelPower() {
        return downChannelPower;
    }

    public void setDownChannelPower(Long downChannelPower) {
        this.downChannelPower = downChannelPower;
    }

    public String getFtpServer() {
        return ftpServer;
    }

    public void setFtpServer(String ftpServer) {
        this.ftpServer = ftpServer;
    }

    public Integer getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(Integer ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getRemoteFilePathName() {
        return remoteFilePathName;
    }

    public void setRemoteFilePathName(String remoteFilePathName) {
        this.remoteFilePathName = remoteFilePathName;
    }

    public String getFtpFile() {
        return ftpFile;
    }

    public void setFtpFile(String ftpFile) {
        this.ftpFile = ftpFile;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getChannelAdminstatus() {
        return channelAdminstatus;
    }

    public void setChannelAdminstatus(Integer channelAdminstatus) {
        this.channelAdminstatus = channelAdminstatus;
    }

    public Integer getParaChannelId() {
        return paraChannelId;
    }

    public void setParaChannelId(Integer paraChannelId) {
        this.paraChannelId = paraChannelId;
    }

    public List<Long> getChannelIndexs() {
        return channelIndexs;
    }

    public void setChannelIndexs(List<Long> channelIndexs) {
        this.channelIndexs = channelIndexs;
    }

}
