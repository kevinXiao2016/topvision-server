/***********************************************************************
 * $Id: OnuAction.java,v1.0 2013-10-25 上午11:18:33 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.config.domain.OltControlFileCommand;
import com.topvision.ems.epon.domain.EponPort;
import com.topvision.ems.epon.exception.OltFileControlException;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.olt.domain.OltFileAttribute;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.olt.service.OltUploadAndUpdateService;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgBand;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgProfile;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgRecord;
import com.topvision.ems.epon.onu.domain.OltOnuUpgrade;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onu.service.OnuUpdateService;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.ems.message.LongRequestExecutorService;
import com.topvision.ems.message.MessagePusher;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.domain.FtpConnectInfo;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.service.FtpConnectService;

/**
 * @author flack
 * @created @2013-10-25-上午11:18:33
 *
 */
@Controller("onuUpdateAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuUpdateAction extends AbstractEponAction {
    private static final long serialVersionUID = 520636647153437441L;
    private final Logger logger = LoggerFactory.getLogger(OnuUpdateAction.class);
    @Autowired
    private MessagePusher messagePusher;
    @Autowired
    private FtpConnectService ftpConnectService;
    @Autowired
    private OnuUpdateService onuUpdateService;
    @Autowired
    private OltPonService oltPonService;
    @Autowired
    private OltUploadAndUpdateService oltUploadAndUpdateService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private OltService oltService;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private LongRequestExecutorService longRequestExecutorService;

    private Integer topOnuUpgradeSlotNum;
    private Integer topOnuUpgradeOnuType;
    private String topOnuUpgradeFileName;
    // private Integer topOnuUpgradeFileType;
    private Integer topOnuUpgradeOption;
    private Integer topOnuUpgradeMode;
    private String topOnuHwVersion;
    private String topOnuUpgradeOnuList;
    private Integer topOnuUpgradeOperAction;
    private String topOnuUpgradeStatus;
    private String ponIndexs;
    private Integer onuType;
    private JSONArray onuHardwareVersionList;
    private JSONArray onuUpgradeFileList;
    private JSONArray ponOnuProfileList = new JSONArray();
    private JSONArray profileList = new JSONArray();
    private List<Integer> profileIdList;
    private Integer profileId;
    private String profileName;
    private Integer proOnuType;
    private String proHwVersion;
    private Integer proMode;
    private String proNewVersion;
    private String proBoot;
    private String proApp;
    private String proWebs;
    private String proOther;
    private String proPers;
    private String proBandStat;
    private JSONObject onuTypeMap;
    private Long ponId;
    private Integer ponNo;
    private Integer slotNo;
    private Integer onuAutoUpgFlag;
    private String messageList;
    private Integer recordId;
    private String jconnectionId;
    private String proUpgTime;
    private Boolean boardGponType;

    /**
     * ONU升级
     * 
     * @return String
     * @throws Exception
     */
    // @OperationLogProperty(actionName = "onuUpdateAction", operationName = "onuUpdate")
    public String onuUpdate() throws Exception {
        JSONObject json = new JSONObject();
        final OltOnuUpgrade oltOnuUpgrade = new OltOnuUpgrade();
        oltOnuUpgrade.setEntityId(entityId);
        oltOnuUpgrade.setTopOnuUpgradeTransactionIndex(1070);
        oltOnuUpgrade.setTopOnuUpgradeSlotNum(topOnuUpgradeSlotNum);
        oltOnuUpgrade.setTopOnuUpgradeOnuType(topOnuUpgradeOnuType);
        if (oltService.oltVersionCompare(entityId) >= 0) {
            topOnuUpgradeFileName = "/tffs0/onuapp/" + topOnuUpgradeFileName;
        } else {
            topOnuUpgradeFileName = oltService.getOltFileDirEntry(entityId, EponConstants.DIR_ONU).getFileDirPath()
                    + topOnuUpgradeFileName;
        }
        oltOnuUpgrade.setTopOnuUpgradeFileName(topOnuUpgradeFileName);
        // oltOnuUpgrade.setTopOnuUpgradeFileType(topOnuUpgradeFileType);
        oltOnuUpgrade.setTopOnuUpgradeOption(topOnuUpgradeOption);
        oltOnuUpgrade.setTopOnuUpgradeMode(topOnuUpgradeMode);
        oltOnuUpgrade.setTopOnuUpgradeHwVersion(topOnuHwVersion);
        List<Integer> onuNoList = new ArrayList<Integer>();
        List<Long> hwOnuList = onuUpdateService.getOnuIndexListByHwVeList(entityId, topOnuHwVersion);
        if (!topOnuUpgradeOnuList.equals("")) {
            String[] onuIndexs = topOnuUpgradeOnuList.split(",");
            // modify by lzt
            // topOnuUpgradeOnuList由1024个字节改为128个字节
            for (String onuIndex : onuIndexs) {
                if ("any".equals(topOnuHwVersion)) {
                    onuNoList.add((EponIndex.getPonNo(Long.parseLong(onuIndex)).intValue() - 1) * 128
                            + EponIndex.getOnuNo(Long.parseLong(onuIndex)).intValue());
                } else {
                    if (hwOnuList.indexOf(Long.parseLong(onuIndex)) != -1) {
                        onuNoList.add((EponIndex.getPonNo(Long.parseLong(onuIndex)).intValue() - 1) * 128
                                + EponIndex.getOnuNo(Long.parseLong(onuIndex)).intValue());
                    }
                }
            }
        }
        oltOnuUpgrade.setTopOnuUpgradeOnuListList(onuNoList);
        oltOnuUpgrade.setTopOnuUpgradeOperAction(topOnuUpgradeOperAction);
        oltOnuUpgrade.setTopOnuUpgradeStatus(topOnuUpgradeStatus);
        String result = null;
        try {
            onuUpdateService.upgradeOnu(oltOnuUpgrade);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            result = "onuUpdateFail";
            operationResult = OperationLog.FAILURE;
            logger.debug("onuUpdate error: {}", e);
        } finally {
            /*
             * Message message = new Message("onuUpgrade"); message.setJconnectID(jconnectionId);
             * message.setData(result); messagePusher.sendMessage(message);
             */
        }
        json.put("result", result);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获取槽位和PON口关系
     * 
     * @return String
     * @throws Exception
     */
    public String loadSlotPonRelation() throws Exception {
        List<Long> ponIndexList = oltPonService.getAllPonIndex(entityId);
        List<EponPort> ponList = new ArrayList<EponPort>();
        EponPort pon;
        for (Long ponIndex : ponIndexList) {
            pon = new EponPort();
            Long slotRealIndex = EponIndex.getSlotNo(ponIndex) == 0 ? (EponIndex.getSlotNo(ponIndex) + 1) : EponIndex
                    .getSlotNo(ponIndex);
            pon.setSlotRealIndex(slotRealIndex);
            pon.setPortRealIndex(EponIndex.getPonNo(ponIndex));
            pon.setPortIndex(ponIndex);
            ponList.add(pon);
        }
        logger.debug("loadSlotPonRelation:{}", ponList);
        writeDataToAjax(JSONArray.fromObject(ponList));
        return NONE;
    }

    /**
     * 获取需要进行升级的ONU
     * 
     * @return String
     * @throws Exception
     */
    public String loadAvailableUpgradeOnuList() throws Exception {
        List<Long> ponIndexList = new ArrayList<Long>();
        if (!ponIndexs.equals("")) {
            String[] ponIndexArray = ponIndexs.split(",");
            for (String ponIndexString : ponIndexArray) {
                ponIndexList.add(Long.parseLong(ponIndexString));
            }
        }
        List<OltOnuAttribute> oltOnuAttributeList = oltPonService.getOnuAttributeByPonIndexs(entityId, ponIndexList,
                onuType);
        logger.debug("loadAvailableUpgradeOnuList:{}", oltOnuAttributeList);
        writeDataToAjax(JSONArray.fromObject(oltOnuAttributeList));
        return NONE;
    }

    /**
     * 获取ONU升级历史记录
     * 
     * @return String
     * @throws Exception
     */
    public String loadOnuUpgradeHistory() throws Exception {
        List<OltOnuUpgrade> oltOnuUpgradeList = onuUpdateService.getOnuUpgradeHistory(entityId);
        logger.debug("loadOnuUpgradeHistory:{}", oltOnuUpgradeList);
        writeDataToAjax(JSONArray.fromObject(oltOnuUpgradeList));
        return NONE;
    }

    /**
     * 显示ONU升级页面
     * 
     * @return String
     */
    public String onuUpgradeFileUpload() {
        List<OltFileAttribute> setList = new ArrayList<OltFileAttribute>();
        List<OltFileAttribute> ugfileList = new ArrayList<OltFileAttribute>();
        List<String> hwVersionList = new ArrayList<String>();
        String onuDir = "";
        // boolean flag = true;
        try {
            setList = oltUploadAndUpdateService.getOltFilePath(entityId);
            hwVersionList = onuService.getOnuHwList(entityId);
        } catch (SnmpException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("FileLoad Failure error {}", e);
            }
        }
        if (oltService.oltVersionCompare(entityId) >= 0) {
            onuDir = "/tffs0/onuapp/";
        } else {
            onuDir = oltService.getOltFileDirEntry(entityId, EponConstants.DIR_ONU).getFileDirPath();
        }
        for (OltFileAttribute oltFileAttribute : setList) {
            if (setList.size() > 0 && oltFileAttribute.getFilePath().equals(onuDir)
                    && !oltFileAttribute.getFileAttribute().equals(EponConstants.FILE_TYPE_DIR)) {
                ugfileList.add(oltFileAttribute);
            }
        }
        boardGponType = oltSlotService.slotIsGponBoard(entityId, topOnuUpgradeSlotNum);
        onuHardwareVersionList = JSONArray.fromObject(hwVersionList);
        onuUpgradeFileList = JSONArray.fromObject(ugfileList);
        return SUCCESS;
    }

    /**
     * ONU自动升级页面跳转
     */
    public String showOnuAutoUpg() throws Exception {
        // PON口列表数据
        List<Map<String, Object>> ponList = new ArrayList<Map<String, Object>>();
        List<OltSlotAttribute> oltPonList = oltSlotService.getOltPonSlotList(entityId);
        List<OltOnuAutoUpgBand> bandList = onuUpdateService.getOnuAutoUpgBand(entityId);
        if (oltPonList.size() > 0) {
            for (OltSlotAttribute oltSlotAttribute : oltPonList) {
                List<OltPonAttribute> tempPon = oltSlotService.getSlotPonList(oltSlotAttribute.getSlotId());
                for (OltPonAttribute p : tempPon) {
                    Map<String, Object> tmpP = new HashMap<String, Object>();
                    tmpP.put("slotId", p.getSlotId());
                    tmpP.put("slotNo", p.getSlotNo());
                    tmpP.put("ponId", p.getPonId());
                    tmpP.put("ponNo", p.getPonNo());
                    List<Integer> tmpPro = new ArrayList<Integer>();
                    if (bandList.size() > 0) {
                        for (OltOnuAutoUpgBand b : bandList) {
                            if (b.getSlotNo().equals(p.getSlotNo().intValue())
                                    && b.getPonNo().equals(p.getPonNo().intValue())) {
                                tmpPro.add(b.getProfileId());
                            }
                        }
                    }
                    tmpP.put("proList", tmpPro);
                    ponList.add(tmpP);
                }
            }
        }
        if (ponList.size() > 0) {
            ponOnuProfileList = JSONArray.fromObject(ponList);
        } else {
            ponOnuProfileList = JSONArray.fromObject(false);
        }
        // profile 数据
        List<Object> profileListTmp = getOnuAutoUpgProfileList(bandList);
        if (profileListTmp.size() > 0) {
            profileList = JSONArray.fromObject(profileListTmp);
        } else {
            profileList = JSONArray.fromObject(false);
        }
        onuTypeMap = JSONObject.fromObject(EponConstants.EPON_ONU_PRETYPE);
        return SUCCESS;
    }

    /**
     * 组装页面profile列表展示的数据的方法，供showOnuAutoUpg()和showOnuAutoUpgProfile()使用
     * 
     * @param bandList
     * @return
     * @throws Exception
     */
    private List<Object> getOnuAutoUpgProfileList(List<OltOnuAutoUpgBand> bandList) throws Exception {
        List<Object> profileListTmp = new ArrayList<Object>();
        List<OltOnuAutoUpgProfile> profileObjec = onuUpdateService.getOnuAutoUpgProfile(entityId);
        if (profileObjec.size() > 0) {
            for (OltOnuAutoUpgProfile p : profileObjec) {
                List<Object> tmp = new ArrayList<Object>();
                tmp.add(p.getProfileId());
                tmp.add(p.getProName());
                tmp.add(p.getProOnuType());
                tmp.add(p.getProHwVersion());
                tmp.add(p.getProNewVersion());
                tmp.add(p.getProMode());
                tmp.add(p.getBoot());
                tmp.add(p.getApp());
                tmp.add(p.getWebs());
                tmp.add(p.getOther());
                tmp.add(p.getPers());
                tmp.add(p.getProBandStat());
                List<Object> tt = new ArrayList<Object>();
                if (bandList.size() > 0) {
                    for (OltOnuAutoUpgBand b : bandList) {
                        if (b.getProfileId().equals(p.getProfileId())) {
                            Map<String, Integer> m = new HashMap<String, Integer>();
                            m.put("slotNo", b.getSlotNo());
                            m.put("ponNo", b.getPonNo());
                            tt.add(m);
                        }
                    }
                }
                tmp.add(tt);
                tmp.add(p.getProUpgTime());
                profileListTmp.add(tmp);
            }
        }
        return profileListTmp;
    }

    /**
     * 通过ponId来load ONU数据
     */
    public String loadOnuAutoUpgInfo() throws Exception {
        List<Map<String, Object>> onuList = new ArrayList<Map<String, Object>>();
        List<OltOnuAttribute> onuObject = onuService.getOnuListByPonId(ponId);
        if (onuObject.size() > 0) {
            for (OltOnuAttribute o : onuObject) {
                Map<String, Object> onu = new HashMap<String, Object>();
                onu.put("onuId", o.getOnuId());
                onu.put("onuNo", EponIndex.getOnuNoByPortIndex(o.getOnuIndex()));
                // 为了能展示出CC的类型来
                if (o.getOnuPreType() != null && o.getOnuPreType() != 255 && o.getOnuPreType() != 241) {
                    String onuTypeString = "NONE";
                    if (EponConstants.EPON_ONU_PRETYPE.containsValue(o.getOnuPreType())) {
                        for (String key : EponConstants.EPON_ONU_PRETYPE.keySet()) {
                            if (EponConstants.EPON_ONU_PRETYPE.get(key).equals(o.getOnuPreType())) {
                                onuTypeString = "PN" + key;
                                break;
                            }
                        }
                    } else if (o.getOnuType().equals(EponConstants.EPON_CC_TYPE_INTEGER_A)) {
                        onuTypeString = EponConstants.EPON_CC_TYPE_STRING_A;
                    } else if (o.getOnuType().equals(EponConstants.EPON_CC_TYPE_INTEGER_B)) {
                        onuTypeString = EponConstants.EPON_CC_TYPE_STRING_B;
                    }
                    onu.put("onuType", onuTypeString);
                } else if (o.getOnuPreType() == 241) {
                    // modify by bravin@20140625:EMS-9111.ONU
                    // onu.put("onuType", "CC8800");
                    continue;
                } else {
                    onu.put("onuType", "NONE");
                }
                onu.put("onuHwVersion", o.getTopOnuHardwareVersion());
                onu.put("onuSoftwareVersion", o.getOnuSoftwareVersion());
                onu.put("onlineStat", o.getOnuOperationStatus());
                onuList.add(onu);
            }
        }
        Map<String, Object> onu = new HashMap<String, Object>();
        if (onuList.size() > 0) {
            onu.put("data", onuList);
        } else {
            onu.put("data", false);
        }
        writeDataToAjax(JSONObject.fromObject(onu));
        return NONE;
    }

    /**
     * ONU自动升级模板页面跳转
     */
    public String showOnuAutoUpgProfile() throws Exception {
        List<OltOnuAutoUpgBand> bandList = onuUpdateService.getOnuAutoUpgBand(entityId);
        // profile 数据
        List<Object> profileListTmp = getOnuAutoUpgProfileList(bandList);
        if (profileListTmp.size() > 0) {
            profileList = JSONArray.fromObject(profileListTmp);
        } else {
            profileList = JSONArray.fromObject(false);
        }

        List<String> hwVersionList = new ArrayList<String>();
        try {
            hwVersionList = onuService.getOnuHwList(entityId);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Load HwVersion Failure error {}", e);
            }
        }
        if (hwVersionList.size() > 0) {
            onuHardwareVersionList = JSONArray.fromObject(hwVersionList);
        } else {
            onuHardwareVersionList = JSONArray.fromObject(false);
        }
        onuTypeMap = JSONObject.fromObject(EponConstants.EPON_ONU_PRETYPE);
        return SUCCESS;
    }

    /**
     * 从设备上获取ONU升级（onuapp文件夹下）的文件列表
     * 
     * @return
     * @throws Exception
     */
    public String loadOnuUpgFileList() throws Exception {
        List<OltFileAttribute> setList = new ArrayList<OltFileAttribute>();
        List<OltFileAttribute> ugfileList = new ArrayList<OltFileAttribute>();
        try {
            setList = oltUploadAndUpdateService.getOltFilePath(entityId);
        } catch (SnmpException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("FileLoad Failure error {}", e);
            }
        }
        String onuDir = "";
        if (oltService.oltVersionCompare(entityId) >= 0) {
            onuDir = "/tffs0/onuapp/";
        } else {
            onuDir = oltService.getOltFileDirEntry(entityId, EponConstants.DIR_ONU).getFileDirPath();
        }
        for (OltFileAttribute oltFileAttribute : setList) {
            if (setList.size() > 0 && oltFileAttribute.getFilePath().equals(onuDir)
                    && !oltFileAttribute.getFileAttribute().equals(EponConstants.FILE_TYPE_DIR)) {
                ugfileList.add(oltFileAttribute);
            }
        }
        Map<String, List<OltFileAttribute>> re = new HashMap<String, List<OltFileAttribute>>();
        if (ugfileList.size() > 0) {
            re.put("data", ugfileList);
        }
        writeDataToAjax(JSONObject.fromObject(re));
        return NONE;
    }

    /**
     * 新增ONU自动升级模板页面跳转
     */
    @SuppressWarnings("unchecked")
    public String showOnuAutoUpgAddPro() throws Exception {
        List<OltOnuAutoUpgProfile> profileObjec = onuUpdateService.getOnuAutoUpgProfile(entityId);
        List<Integer> re = new ArrayList<Integer>();
        if (profileObjec.size() > 0) {
            for (OltOnuAutoUpgProfile o : profileObjec) {
                re.add(o.getProfileId());
            }
        }
        if (re.size() > 0) {
            profileIdList = JSONArray.fromObject(re);
        } else {
            profileIdList = JSONArray.fromObject(false);
        }

        List<String> hwVersionList = new ArrayList<String>();
        try {
            hwVersionList = onuService.getOnuHwList(entityId);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Load HwVersion Failure error {}", e);
            }
        }
        if (hwVersionList.size() > 0) {
            onuHardwareVersionList = JSONArray.fromObject(hwVersionList);
        } else {
            onuHardwareVersionList = JSONArray.fromObject(false);
        }
        onuTypeMap = JSONObject.fromObject(EponConstants.EPON_ONU_PRETYPE);
        return SUCCESS;
    }

    /**
     * 正在升级的ONU页面跳转
     */
    @SuppressWarnings("unchecked")
    public String showOnuAutoUpgRecord() throws Exception {
        List<Integer> slotNoList = new ArrayList<Integer>();
        List<OltSlotAttribute> oltPonList = oltSlotService.getOltPonSlotList(entityId);
        if (oltPonList.size() > 0) {
            for (OltSlotAttribute oltSlotAttribute : oltPonList) {
                slotNoList.add(oltSlotAttribute.getSlotNo().intValue());
            }
        }
        if (slotNoList.size() > 0) {
            ponOnuProfileList = JSONArray.fromObject(slotNoList);
        } else {
            ponOnuProfileList = JSONArray.fromObject(false);
        }
        // profileIdList
        List<OltOnuAutoUpgProfile> profileObjec = onuUpdateService.getOnuAutoUpgProfile(entityId);
        List<Integer> re = new ArrayList<Integer>();
        if (profileObjec.size() > 0) {
            for (OltOnuAutoUpgProfile o : profileObjec) {
                re.add(o.getProfileId());
            }
        }
        if (re.size() > 0) {
            profileIdList = JSONArray.fromObject(re);
        } else {
            profileIdList = JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    /******************************
     * ONU自动升级的操作对应的方法**
     ****************************/
    /**
     * 在新建或修改ONU自动升级模板时，需要上传文件到onuapp中
     */
    @OperationLogProperty(actionName = "onuUpdateAction", operationName = "uploadFileInOnuAutoUpg")
    public String uploadFileInOnuAutoUpg() throws Exception {
        operationResult = OperationLog.SUCCESS;
        OltOnuAutoUpgProfile pro = new OltOnuAutoUpgProfile();
        pro.setEntityId(entityId);
        pro.setProfileId(profileId);
        pro.setProName(profileName);
        if (proOnuType != null) {
            pro.setProOnuType(proOnuType);
        }
        if (proNewVersion != null) {
            pro.setProNewVersion(proNewVersion);
        }
        if (proHwVersion != null) {
            pro.setProHwVersion(proHwVersion);
        }
        pro.setProMode(proMode);
        Map<String, String> fileMap = new HashMap<String, String>();
        if (proBoot != null) {
            pro.setBoot(proBoot);
            fileMap.put("fileBoot", proBoot);
        }
        if (proApp != null) {
            pro.setApp(proApp);
            fileMap.put("fileApp", proApp);
        }
        if (proWebs != null) {
            pro.setWebs(proWebs);
            fileMap.put("fileWebs", proWebs);
        }
        if (proOther != null) {
            pro.setOther(proOther);
            fileMap.put("fileOther", proOther);
        }
        pro.setPers(proPers);
        // 自动升级时间处理
        if (proUpgTime != null && !"".equals(proUpgTime)) {
            // proUpgTime格式为Y-m-d H:i
            String datatimeFormat = proUpgTime + ":00";
            try {
                pro.setProUpgTime(EponUtil.parseToDateAndTimeFormat(DateUtils.parseLong(datatimeFormat)));
            } catch (Exception e) {
                logger.error("", e);
            }
        } else {
            pro.setProUpgTime("00:00:00:00:00:00:00:00");
        }
        pro.setUpgradeTimeStr(proUpgTime);
        fileMap.put("filePers", proPers);
        List<String> result = new ArrayList<String>();
        boolean uploadFileResult = false;
        FtpConnectInfo connectInfo = ftpConnectService.getFtpConnectAttr();
        MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();
        Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();
        while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
            String inputName = fileParameterNames.nextElement();
            String[] contentType = multiWrapper.getContentTypes(inputName);
            if (isNonEmpty(contentType)) {
                String[] fileName = multiWrapper.getFileNames(inputName);
                if (isNonEmpty(fileName)) {
                    File[] files = multiWrapper.getFiles(inputName);
                    if (files != null) {
                        for (File tmpFile : files) {
                            /*
                             * File toFile = new File(SystemConstants.ROOT_REAL_PATH +
                             * "META-INF/ftpTemp/" + fileMap.get(inputName).toString());
                             */
                            if (tmpFile.length() > 10000000) {
                                result.add("failed");
                                result.add("file: " + fileMap.get(inputName).toString()
                                        + " is too large, cannot be uploaded to device!");
                                break;
                            }
                            // String tmpRe = EponUtil.writeFile(tmpFile, toFile);
                            uploadFileResult = ftpConnectService.uploadFile(tmpFile, fileMap.get(inputName).toString());
                            if (!uploadFileResult) {
                                result.add("failed");
                                /*
                                 * if (tmpRe.equals("fileNotExists")) { result.add("file: " +
                                 * fileMap.get(inputName).toString() +
                                 * " not exists or cannot be used!"); } else { result.add(tmpRe); }
                                 */
                            } else {
                                OltControlFileCommand oltControlFileCommand = new OltControlFileCommand();
                                oltControlFileCommand.setFileTransferProtocolType(EponConstants.FILE_TRANS_PRO_FTP);
                                /*
                                 * Properties prop = new Properties(); InputStream in = new
                                 * FileInputStream(new File(SystemConstants.WEB_INF_REAL_PATH +
                                 * "/conf/ftp.properties")); prop.load(in); String ftpServer =
                                 * prop.getProperty("FtpServer.Ip"); if (ftpServer == null ||
                                 * ftpServer.equalsIgnoreCase("") ||
                                 * ftpServer.equalsIgnoreCase("127.0.0.1") ||
                                 * ftpServer.equalsIgnoreCase("localhost")) { ftpServer =
                                 * InetAddress.getLocalHost().getHostAddress(); }
                                 */
                                oltControlFileCommand.setServerIpAddress(connectInfo.getIp());
                                oltControlFileCommand.setFtpUserName(connectInfo.getUserName());
                                oltControlFileCommand.setFtpUserPassword(connectInfo.getPwd());

                                if (oltService.oltVersionCompare(entityId) >= 0) {
                                    oltControlFileCommand.setTransferFileDstNamePath("/tffs0/onuapp/"
                                            + fileMap.get(inputName).toString());
                                } else {
                                    String fileUploadPath = oltService.getOltFileDirEntry(entityId,
                                            EponConstants.DIR_ONU).getFileDirPath();
                                    oltControlFileCommand.setTransferFileDstNamePath(fileUploadPath
                                            + fileMap.get(inputName).toString());
                                }
                                oltControlFileCommand.setTransferFileSrcNamePath(fileMap.get(inputName).toString());
                                oltControlFileCommand.setTransferAction(EponConstants.FILE_OLT_DOWNLOAD);
                                try {
                                    oltUploadAndUpdateService.contorlOltFile(entityId, oltControlFileCommand);
                                    int fileUpdateStatusResult = oltUploadAndUpdateService.getOltTranslationStatus(
                                            entityId, tmpFile.length());
                                    if (fileUpdateStatusResult == EponConstants.FILE_TRANS_SUCCESS) {
                                        result.add("file: " + fileMap.get(inputName).toString() + " upload success!");
                                    } else if (fileUpdateStatusResult == EponConstants.FILE_TRANS_TIMELIMIT) {
                                        result.add("failed");
                                        result.add("file: " + fileMap.get(inputName).toString()
                                                + " is transmission overtime!");
                                        operationResult = OperationLog.FAILURE;
                                    } else if (fileUpdateStatusResult == EponConstants.FILE_TRANS_FAILURE) {
                                        result.add("failed");
                                        result.add("file: " + fileMap.get(inputName).toString()
                                                + " is transmission failed!");
                                        operationResult = OperationLog.FAILURE;
                                    }
                                } catch (OltFileControlException ofce) {
                                    result.add("failed");
                                    result.add("OltFileControlException");
                                    // result.add(ofce.getMessage());
                                    operationResult = OperationLog.FAILURE;
                                } catch (SnmpException se) {
                                    result.add("failed");
                                    result.add(getString("Business.snmpWrong", "epon"));
                                    operationResult = OperationLog.FAILURE;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (!result.contains("failed")) {
            try {
                if (onuAutoUpgFlag == 0) {// add
                    onuUpdateService.addOnuAutoUpgProfile(pro);
                } else if (onuAutoUpgFlag == 1) {// modify
                    onuUpdateService.modifyOnuAutoUpgProfile(pro);
                }
            } catch (Exception e) {
                result.add("failed");
                if (onuAutoUpgFlag == 0) {// add
                    result.add("addOnuAutoUpgProfile failed: " + e.getMessage());
                } else if (onuAutoUpgFlag == 1) {// modify
                    result.add("modifyOnuAutoUpgProfile failed: " + e.getMessage());
                }
                operationResult = OperationLog.FAILURE;
            }
        }
        if (!result.contains("failed")) {
            result.add("success");
        }
        StringBuilder ss = new StringBuilder();
        for (String s : result) {
            if (!s.equalsIgnoreCase("failed")) {
                ss.append("@");
                ss.append(s);
            }
        }
        messageList = ss.substring(1, ss.length());
        return SUCCESS;
    }

    /**
     * 跳转到信息提示页面
     * 
     * @return
     */
    public String showOnuAutoUpgMessage() {
        return SUCCESS;
    }

    /**
     * 新建模板
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "onuUpdateAction", operationName = "addOnuAutoUpgProfile")
    public String addOnuAutoUpgProfile() throws Exception {
        OltOnuAutoUpgProfile ad = new OltOnuAutoUpgProfile();
        ad.setEntityId(entityId);
        ad.setProfileId(profileId);
        ad.setProName(profileName);
        if (proOnuType != null) {
            ad.setProOnuType(proOnuType);
        }
        if (proHwVersion != null) {
            ad.setProHwVersion(proHwVersion);
        }
        if (proNewVersion != null) {
            ad.setProNewVersion(proNewVersion);
        }
        ad.setProMode(proMode);
        if (proBoot != null) {
            ad.setBoot(proBoot);
        }
        if (proApp != null) {
            ad.setApp(proApp);
        }
        if (proWebs != null) {
            ad.setWebs(proWebs);
        }
        if (proOther != null) {
            ad.setOther(proOther);
        }
        ad.setPers(proPers);
        // 自动升级时间处理
        if (proUpgTime != null && !"".equals(proUpgTime)) {
            // proUpgTime格式为Y-m-d H:i
            String datatimeFormat = proUpgTime + ":00";
            try {
                ad.setProUpgTime(EponUtil.parseToDateAndTimeFormat(DateUtils.parseLong(datatimeFormat)));
            } catch (Exception e) {
                logger.error("", e);
            }
        } else {
            ad.setProUpgTime("00:00:00:00:00:00:00:00");
        }
        ad.setUpgradeTimeStr(proUpgTime);
        onuUpdateService.addOnuAutoUpgProfile(ad);
        return NONE;
    }

    /**
     * 修改模板
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "onuUpdateAction", operationName = "modifyOnuAutoUpgProfile")
    public String modifyOnuAutoUpgProfile() throws Exception {
        operationResult = OperationLog.SUCCESS;
        OltOnuAutoUpgProfile mo = new OltOnuAutoUpgProfile();
        mo.setEntityId(entityId);
        mo.setProfileId(profileId);
        mo.setProName(profileName);
        if (proOnuType != null) {
            mo.setProOnuType(proOnuType);
        }
        if (proHwVersion != null) {
            mo.setProHwVersion(proHwVersion);
        }
        if (proNewVersion != null) {
            mo.setProNewVersion(proNewVersion);
        }
        mo.setProMode(proMode);
        if (proBoot != null) {
            mo.setBoot(proBoot);
        }
        if (proApp != null) {
            mo.setApp(proApp);
        }
        if (proWebs != null) {
            mo.setWebs(proWebs);
        }
        if (proOther != null) {
            mo.setOther(proOther);
        }
        mo.setPers(proPers);
        // 自动升级时间处理
        if (proUpgTime != null && !"".equals(proUpgTime)) {
            // proUpgTime格式为Y-m-d H:i
            String datatimeFormat = proUpgTime + ":00";
            try {
                mo.setProUpgTime(EponUtil.parseToDateAndTimeFormat(DateUtils.parseLong(datatimeFormat)));
            } catch (Exception e) {
                logger.error("", e);
            }
        } else {
            mo.setProUpgTime("00:00:00:00:00:00:00:00");
        }
        mo.setUpgradeTimeStr(proUpgTime);
        String msg = "";
        try {
            onuUpdateService.modifyOnuAutoUpgProfile(mo);
        } catch (Exception e) {
            logger.debug("modify onuAutoUpgProfile failed:{}", e);
            msg = e.getMessage();
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(msg);
        return NONE;
    }

    /**
     * 删除模板
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "onuUpdateAction", operationName = "delOnuAutoUpgProfile")
    public String delOnuAutoUpgProfile() throws Exception {
        operationResult = OperationLog.SUCCESS;
        String msg = "";
        try {
            onuUpdateService.delOnuAutoUpgProfile(entityId, profileId);
        } catch (Exception e) {
            logger.debug("modify onuAutoUpgProfile failed:{}", e);
            msg = e.getMessage();
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(msg);
        return NONE;
    }

    /**
     * 绑定模板
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "onuUpdateAction", operationName = "bandOnuAutoUpgProfile")
    public String bandOnuAutoUpgProfile() throws Exception {
        operationResult = OperationLog.SUCCESS;
        OltOnuAutoUpgBand band = new OltOnuAutoUpgBand();
        band.setEntityId(entityId);
        band.setProfileId(profileId);
        Long ponIndex = EponIndex.getPonIndex(slotNo, ponNo);
        band.setPonIndex(ponIndex);
        String msg = "";
        try {
            onuUpdateService.bandOnuAutoUpgProfile(band);
        } catch (Exception e) {
            logger.debug("modify onuAutoUpgProfile failed:{}", e);
            msg = e.getMessage();
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(msg);
        return NONE;
    }

    /**
     * 解绑定模板
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "onuUpdateAction", operationName = "unbandOnuAutoUpgProfile")
    public String unbandOnuAutoUpgProfile() throws Exception {
        operationResult = OperationLog.SUCCESS;
        OltOnuAutoUpgBand band = new OltOnuAutoUpgBand();
        band.setEntityId(entityId);
        band.setProfileId(profileId);
        Long ponIndex = EponIndex.getPonIndex(slotNo, ponNo);
        band.setPonIndex(ponIndex);
        String msg = "";
        try {
            onuUpdateService.unbandOnuAutoUpgProfile(band);
        } catch (Exception e) {
            logger.debug("modify onuAutoUpgProfile failed:{}", e);
            msg = e.getMessage();
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(msg);
        return NONE;
    }

    /**
     * 重启自动升级
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "onuUpdateAction", operationName = "restartOnuAutoUpg")
    public String restartOnuAutoUpg() throws Exception {
        operationResult = OperationLog.SUCCESS;
        OltOnuAutoUpgBand band = new OltOnuAutoUpgBand();
        band.setEntityId(entityId);
        band.setProfileId(profileId);
        band.setTopOnuAutoUpgInstallAction(1);
        if (slotNo != null) {
            band.setSlotNo(slotNo);
            if (ponNo != null) {
                band.setPonNo(ponNo);
            } else {
                band.setPonNo(EponConstants.EPON_ONU_AUTOUPG_ALLNO_RESTART);
            }
        } else {
            band.setSlotNo(EponConstants.EPON_ONU_AUTOUPG_ALLNO_RESTART);
            band.setPonNo(EponConstants.EPON_ONU_AUTOUPG_ALLNO_RESTART);
        }
        band.setOnuNo(EponConstants.EPON_ONU_AUTOUPG_ALLNO_RESTART);
        String msg = "";
        try {
            onuUpdateService.restartOnuAutoUpg(band);
        } catch (Exception e) {
            logger.debug("modify onuAutoUpgProfile failed:{}", e);
            msg = e.getMessage();
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(msg);
        return NONE;
    }

    /**
     * 取消自动升级
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "onuUpdateAction", operationName = "cancelOnuAutoUpg")
    public String cancelOnuAutoUpg() throws Exception {
        OltOnuAutoUpgRecord record = new OltOnuAutoUpgRecord();
        record.setEntityId(entityId);
        record.setSlotNo(slotNo);
        record.setRecordId(recordId);
        onuUpdateService.cancelOnuAutoUpg(record);
        return NONE;
    }

    /**
     * 获取ONU自动升级的模板及绑定数据
     * 
     * @return
     * @throws Exception
     */
    public String refreshOnuAutoUpg() throws Exception {
        onuUpdateService.refreshOnuAutoUpg(entityId);
        return NONE;
    }

    /**
     * 获取正在升级的ONU的数据
     * 
     * @return
     * @throws Exception
     */
    public String refreshOnuAutoUpging() throws Exception {
        List<OltOnuAutoUpgRecord> re = onuUpdateService.refreshOnuAutoUpging(entityId);
        writeDataToAjax(re);
        return NONE;
    }

    /**
     * 读取文件信息的时候判断数组是否有可用对象
     * 
     * @param objArray
     *            数组列表
     * @return String
     */
    private boolean isNonEmpty(Object[] objArray) {
        boolean result = false;
        for (int index = 0; index < objArray.length && !result; index++) {
            if (objArray[index] != null) {
                result = true;
            }
        }
        return result;
    }

    public MessagePusher getMessagePusher() {
        return messagePusher;
    }

    public void setMessagePusher(MessagePusher messagePusher) {
        this.messagePusher = messagePusher;
    }

    public FtpConnectService getFtpConnectService() {
        return ftpConnectService;
    }

    public void setFtpConnectService(FtpConnectService ftpConnectService) {
        this.ftpConnectService = ftpConnectService;
    }

    public Integer getTopOnuUpgradeSlotNum() {
        return topOnuUpgradeSlotNum;
    }

    public void setTopOnuUpgradeSlotNum(Integer topOnuUpgradeSlotNum) {
        this.topOnuUpgradeSlotNum = topOnuUpgradeSlotNum;
    }

    public Integer getTopOnuUpgradeOnuType() {
        return topOnuUpgradeOnuType;
    }

    public void setTopOnuUpgradeOnuType(Integer topOnuUpgradeOnuType) {
        this.topOnuUpgradeOnuType = topOnuUpgradeOnuType;
    }

    public String getTopOnuUpgradeFileName() {
        return topOnuUpgradeFileName;
    }

    public void setTopOnuUpgradeFileName(String topOnuUpgradeFileName) {
        this.topOnuUpgradeFileName = topOnuUpgradeFileName;
    }

    public Integer getTopOnuUpgradeOption() {
        return topOnuUpgradeOption;
    }

    public void setTopOnuUpgradeOption(Integer topOnuUpgradeOption) {
        this.topOnuUpgradeOption = topOnuUpgradeOption;
    }

    public Integer getTopOnuUpgradeMode() {
        return topOnuUpgradeMode;
    }

    public void setTopOnuUpgradeMode(Integer topOnuUpgradeMode) {
        this.topOnuUpgradeMode = topOnuUpgradeMode;
    }

    public String getTopOnuHwVersion() {
        return topOnuHwVersion;
    }

    public void setTopOnuHwVersion(String topOnuHwVersion) {
        this.topOnuHwVersion = topOnuHwVersion;
    }

    public String getTopOnuUpgradeOnuList() {
        return topOnuUpgradeOnuList;
    }

    public void setTopOnuUpgradeOnuList(String topOnuUpgradeOnuList) {
        this.topOnuUpgradeOnuList = topOnuUpgradeOnuList;
    }

    public Integer getTopOnuUpgradeOperAction() {
        return topOnuUpgradeOperAction;
    }

    public void setTopOnuUpgradeOperAction(Integer topOnuUpgradeOperAction) {
        this.topOnuUpgradeOperAction = topOnuUpgradeOperAction;
    }

    public String getTopOnuUpgradeStatus() {
        return topOnuUpgradeStatus;
    }

    public void setTopOnuUpgradeStatus(String topOnuUpgradeStatus) {
        this.topOnuUpgradeStatus = topOnuUpgradeStatus;
    }

    public String getPonIndexs() {
        return ponIndexs;
    }

    public void setPonIndexs(String ponIndexs) {
        this.ponIndexs = ponIndexs;
    }

    public Integer getOnuType() {
        return onuType;
    }

    public void setOnuType(Integer onuType) {
        this.onuType = onuType;
    }

    public JSONArray getOnuHardwareVersionList() {
        return onuHardwareVersionList;
    }

    public void setOnuHardwareVersionList(JSONArray onuHardwareVersionList) {
        this.onuHardwareVersionList = onuHardwareVersionList;
    }

    public JSONArray getOnuUpgradeFileList() {
        return onuUpgradeFileList;
    }

    public void setOnuUpgradeFileList(JSONArray onuUpgradeFileList) {
        this.onuUpgradeFileList = onuUpgradeFileList;
    }

    public JSONArray getPonOnuProfileList() {
        return ponOnuProfileList;
    }

    public void setPonOnuProfileList(JSONArray ponOnuProfileList) {
        this.ponOnuProfileList = ponOnuProfileList;
    }

    public JSONArray getProfileList() {
        return profileList;
    }

    public void setProfileList(JSONArray profileList) {
        this.profileList = profileList;
    }

    public List<Integer> getProfileIdList() {
        return profileIdList;
    }

    public void setProfileIdList(List<Integer> profileIdList) {
        this.profileIdList = profileIdList;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Integer getProOnuType() {
        return proOnuType;
    }

    public void setProOnuType(Integer proOnuType) {
        this.proOnuType = proOnuType;
    }

    public String getProHwVersion() {
        return proHwVersion;
    }

    public void setProHwVersion(String proHwVersion) {
        this.proHwVersion = proHwVersion;
    }

    public Integer getProMode() {
        return proMode;
    }

    public void setProMode(Integer proMode) {
        this.proMode = proMode;
    }

    public String getProNewVersion() {
        return proNewVersion;
    }

    public void setProNewVersion(String proNewVersion) {
        this.proNewVersion = proNewVersion;
    }

    public String getProBoot() {
        return proBoot;
    }

    public void setProBoot(String proBoot) {
        this.proBoot = proBoot;
    }

    public String getProApp() {
        return proApp;
    }

    public void setProApp(String proApp) {
        this.proApp = proApp;
    }

    public String getProWebs() {
        return proWebs;
    }

    public void setProWebs(String proWebs) {
        this.proWebs = proWebs;
    }

    public String getProOther() {
        return proOther;
    }

    public void setProOther(String proOther) {
        this.proOther = proOther;
    }

    public String getProPers() {
        return proPers;
    }

    public void setProPers(String proPers) {
        this.proPers = proPers;
    }

    public String getProBandStat() {
        return proBandStat;
    }

    public void setProBandStat(String proBandStat) {
        this.proBandStat = proBandStat;
    }

    public JSONObject getOnuTypeMap() {
        return onuTypeMap;
    }

    public void setOnuTypeMap(JSONObject onuTypeMap) {
        this.onuTypeMap = onuTypeMap;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Integer getPonNo() {
        return ponNo;
    }

    public void setPonNo(Integer ponNo) {
        this.ponNo = ponNo;
    }

    public Integer getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public Integer getOnuAutoUpgFlag() {
        return onuAutoUpgFlag;
    }

    public void setOnuAutoUpgFlag(Integer onuAutoUpgFlag) {
        this.onuAutoUpgFlag = onuAutoUpgFlag;
    }

    public String getMessageList() {
        return messageList;
    }

    public void setMessageList(String messageList) {
        this.messageList = messageList;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getJconnectionId() {
        return jconnectionId;
    }

    public void setJconnectionId(String jconnectionId) {
        this.jconnectionId = jconnectionId;
    }

    public String getProUpgTime() {
        return proUpgTime;
    }

    public void setProUpgTime(String proUpgTime) {
        this.proUpgTime = proUpgTime;
    }

    public Boolean getBoardGponType() {
        return boardGponType;
    }

    public void setBoardGponType(Boolean boardGponType) {
        this.boardGponType = boardGponType;
    }

}
