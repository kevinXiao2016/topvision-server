/***********************************************************************
 * $Id: OnuAuthAction.java,v1.0 2013年10月25日 下午5:58:09 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.domain.OltOnuBlockAuthen;
import com.topvision.ems.epon.onuauth.domain.OltPonOnuAuthModeTable;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:58:09
 *
 */
@Controller("onuAuthAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuAuthAction extends AbstractEponAction {
    private static final long serialVersionUID = -2979802558026029359L;
    private final Logger logger = LoggerFactory.getLogger(OnuAuthAction.class);
    private JSONArray ponListObject = new JSONArray();
    private JSONArray slotPonObject = new JSONArray();
    private JSONArray onuMaxNumList = new JSONArray();
    private JSONArray ponAuthEnableList = new JSONArray();
    private JSONArray autoModePonIdList = new JSONArray();
    private JSONArray macModePonIdList = new JSONArray();
    private JSONArray mixModePonIdList = new JSONArray();
    private JSONArray snModePonIdList = new JSONArray();
    private JSONArray snPwdModePonIdList = new JSONArray();
    private JSONArray onuAuthMacListObject = new JSONArray();
    private JSONArray onuAuthSnListObject = new JSONArray();
    private JSONArray onuTypes;
    private Integer authEnable;
    private Integer authMode;
    private Integer authType;
    private Integer authSnMode;
    private String source;
    private Integer onuAuthenticationPolicy;
    private Integer slotNo;
    private Integer ponNo;
    private Long ponId;
    private Integer onuNo;
    private String macAddress;
    private String sn;
    private String password;
    private Long slotId;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private OnuAuthService onuAuthService;
    @Autowired
    private OltPonService oltPonService;
    @Autowired
    private OltSniService oltSniService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private EntityService entityService;

    /**
     * 显示ONU认证管理
     * 
     * @return String
     */
    public String showOnuAuthConfig() {
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        // PON口最大ONU个数
        List<List<Integer>> tmpMaxNum = new ArrayList<List<Integer>>();
        for (int a = 0; a < EponConstants.OLT_SLOT_MAXNUM + 1; a++) {
            List<Integer> tmpNum = new ArrayList<Integer>();
            for (int b = 0; b < EponConstants.OLT_SLOT_PORT_MAXNUM + 1; b++) {
                tmpNum.add(0);
            }
            tmpMaxNum.add(tmpNum);
        }
        // PON板
        List<OltSlotAttribute> oltPonList = oltSlotService.getOltEponSlotList(entityId);
        Entity entity = entityService.getEntity(entityId);
        if (entityTypeService.isPN8602_EType(entity.getTypeId())
                || entityTypeService.isPN8602_EFType(entity.getTypeId())) {
            for (OltSlotAttribute slotAttr : oltPonList) {
                slotAttr.setSlotNo(0L);
            }
        }
        if (oltPonList.size() > 0) {
            ponListObject = JSONArray.fromObject(oltPonList);
            // PON口
            List<OltPonAttribute> oltPonAttributes = new ArrayList<OltPonAttribute>();
            for (OltSlotAttribute oltSlotAttribute : oltPonList) {
                List<OltPonAttribute> tempPon = oltSlotService.getSlotPonList(oltSlotAttribute.getSlotId());
                oltPonAttributes.addAll(tempPon);
                // PON口最大ONU个数
                if (tempPon.size() > 0) {
                    for (OltPonAttribute pon : tempPon) {
                        if (pon.getPonPortMaxOnuNumSupport() != null) {
                            tmpMaxNum.get(oltSlotAttribute.getSlotNo().intValue()).set(pon.getPonNo().intValue(),
                                    pon.getPonPortMaxOnuNumSupport());
                        }
                    }
                }
            }
            if (oltPonAttributes.size() > 0) {
                slotPonObject = JSONArray.fromObject(oltPonAttributes);
            } else {
                slotPonObject = JSONArray.fromObject(false);
            }
        } else {
            ponListObject = JSONArray.fromObject(false);
            slotPonObject = JSONArray.fromObject(false);
        }
        // PON口最大ONU个数
        onuMaxNumList = JSONArray.fromObject(tmpMaxNum);
        // PON口认证使能
        List<OltPonOnuAuthModeTable> status = onuAuthService.getOnuAuthEnable(entityId);
        if (status.size() > 0) {
            List<Long> tmpStatus = new ArrayList<Long>();
            for (OltPonOnuAuthModeTable stat : status) {
                if (stat.getTopPonOnuAuthMode() != null && stat.getPonId() != null) {
                    if (stat.getTopPonOnuAuthMode().equals(EponConstants.OLT_AUTHEN_DISABLE)) {
                        tmpStatus.add(stat.getPonId());
                    }
                }
            }
            if (tmpStatus.size() > 0) {
                ponAuthEnableList = JSONArray.fromObject(tmpStatus);
            } else {
                ponAuthEnableList = JSONArray.fromObject(false);
            }
        } else {
            ponAuthEnableList = JSONArray.fromObject(false);
        }
        // PON口认证模式
        List<OltPonOnuAuthModeTable> modeList = onuAuthService.getOnuAuthMode(entityId);
        if (modeList.size() > 0) {
            List<Long> tmpAutoPonId = new ArrayList<Long>();
            List<Long> tmpMacPonId = new ArrayList<Long>();
            List<Long> tmpMixPonId = new ArrayList<Long>();
            List<Long> tmpSnPonId = new ArrayList<Long>();
            List<Long> tmpSnPwdPonId = new ArrayList<Long>();
            for (OltPonOnuAuthModeTable tmp : modeList) {
                if (tmp.getTopPonOnuAuthMode() != null && tmp.getPonId() != null) {
                    if (tmp.getTopPonOnuAuthMode().equals(EponConstants.OLT_AUTHEN_PONMODE_AUTO)) {
                        tmpAutoPonId.add(tmp.getPonId());
                    } else if (tmp.getTopPonOnuAuthMode().equals(EponConstants.OLT_AUTHEN_PONMODE_MAC)) {
                        tmpMacPonId.add(tmp.getPonId());
                    } else if (tmp.getTopPonOnuAuthMode().equals(EponConstants.OLT_AUTHEN_PONMODE_MIX)) {
                        tmpMixPonId.add(tmp.getPonId());
                    } else if (tmp.getTopPonOnuAuthMode().equals(EponConstants.OLT_AUTHEN_PONMODE_SN)) {
                        tmpSnPonId.add(tmp.getPonId());
                    } else if (tmp.getTopPonOnuAuthMode().equals(EponConstants.OLT_AUTHEN_PONMODE_SNPWD)) {
                        tmpSnPwdPonId.add(tmp.getPonId());
                    }
                }
            }
            if (tmpAutoPonId.size() > 0) {
                autoModePonIdList = JSONArray.fromObject(tmpAutoPonId);
            } else {
                autoModePonIdList = JSONArray.fromObject(false);
            }
            if (tmpMacPonId.size() > 0) {
                macModePonIdList = JSONArray.fromObject(tmpMacPonId);
            } else {
                macModePonIdList = JSONArray.fromObject(false);
            }
            if (tmpMixPonId.size() > 0) {
                mixModePonIdList = JSONArray.fromObject(tmpMixPonId);
            } else {
                mixModePonIdList = JSONArray.fromObject(false);
            }
            if (tmpSnPonId.size() > 0) {
                snModePonIdList = JSONArray.fromObject(tmpSnPonId);
            } else {
                snModePonIdList = JSONArray.fromObject(false);
            }
            if (tmpSnPwdPonId.size() > 0) {
                snPwdModePonIdList = JSONArray.fromObject(tmpSnPwdPonId);
            } else {
                snPwdModePonIdList = JSONArray.fromObject(false);
            }
        } else {
            autoModePonIdList = JSONArray.fromObject(false);
            macModePonIdList = JSONArray.fromObject(false);
            mixModePonIdList = JSONArray.fromObject(false);
            snModePonIdList = JSONArray.fromObject(false);
            snPwdModePonIdList = JSONArray.fromObject(false);
        }
        // MAC认证规则
        List<String> onuMacList = new ArrayList<String>();
        List<OltAuthentication> onuMacObjList = onuAuthService.getOnuAuthMacList(entityId);
        for (OltAuthentication anOnuMacObjList : onuMacObjList) {
            String tmpMac = new MacUtils(anOnuMacObjList.getOnuAuthenMacAddress()).toString(MacUtils.MAOHAO)
                    .toUpperCase();
            //格式化MAC地址
            tmpMac = MacUtils.convertMacToDisplayFormat(tmpMac, displayRule);
            onuMacList.add(tmpMac);
            Long onuAuthIndex = anOnuMacObjList.getOnuIndex();
            Long slotNum = EponIndex.getSlotNo(onuAuthIndex);
            Long ponNum = EponIndex.getPonNo(onuAuthIndex);
            Long onuNum = EponIndex.getOnuNo(onuAuthIndex);
            StringBuilder onuLocation = new StringBuilder();
            onuLocation.append(slotNum.toString()).append("/").append(ponNum.toString()).append(":")
                    .append(onuNum.toString());
            onuMacList.add(onuLocation.toString());
        }
        if (onuMacList.size() > 0) {
            onuAuthMacListObject = JSONArray.fromObject(onuMacList);
        } else {
            onuAuthMacListObject = JSONArray.fromObject(false);
        }
        // SN认证规则
        List<String> onuSnList = new ArrayList<String>();
        List<OltAuthentication> onuSnObjList = onuAuthService.getOnuAuthSnList(entityId);
        for (OltAuthentication anOnuSnObjList : onuSnObjList) {
            onuSnList.add(anOnuSnObjList.getTopOnuAuthLogicSn());
            Long onuAuthIndex = anOnuSnObjList.getOnuIndex();
            Long slotNum = EponIndex.getSlotNo(onuAuthIndex);
            Long ponNum = EponIndex.getPonNo(onuAuthIndex);
            Long onuNum = EponIndex.getOnuNo(onuAuthIndex);
            StringBuilder onuLocation = new StringBuilder();
            onuLocation.append(slotNum.toString()).append("/").append(ponNum.toString()).append(":")
                    .append(onuNum.toString());
            onuSnList.add(onuLocation.toString());
        }
        if (onuSnList.size() > 0) {
            onuAuthSnListObject = JSONArray.fromObject(onuSnList);
        } else {
            onuAuthSnListObject = JSONArray.fromObject(false);
        }
        Long onuType = entityTypeService.getOnuType();
        onuTypes = JSONArray.fromObject(entityTypeService.loadSubType(onuType));
        return SUCCESS;
    }

    /**
     * 设置ONU认证使能（acceptAll/rejectNoConfig）
     * 
     * @return String
     * @throws Exception
     */
    @Deprecated
    public String setOnuAuthPolicy() throws Exception {
        String message = "success";
        try {
            onuAuthService.setOnuAuthPolicy(entityId, onuAuthenticationPolicy);
        } catch (SetValueConflictException svce) {
            message = getString(svce.getMessage(), "epon");
            logger.debug("setOnuAuthPolicy error:{}", svce);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 修改PON口的ONU认证模式
     */
    @OperationLogProperty(actionName = "onuAuthAction", operationName = "modifyOnuAuthMode")
    public String modifyOnuAuthMode() throws Exception {
        Entity entity = entityService.getEntity(entityId);
        Long ponIndex = EponIndex.getPonIndex(slotNo, ponNo);
        if (entity != null && entityTypeService.isPN8602_EType(entity.getTypeId())) {
            ponIndex = EponIndex.getPonIndex(0, ponNo);
        }
        source = getPortLoc(ponIndex, 7);
        onuAuthService.modifyOnuAuthMode(entityId, ponIndex, authMode);
        return NONE;
    }

    /**
     * 修改PON口的ONU认证使能
     */
    @OperationLogProperty(actionName = "onuAuthAction", operationName = "modifyOnuAuthEnable")
    public String modifyOnuAuthEnable() throws Exception {
        String message = "success";
        try {
            onuAuthService.modifyOnuAuthEnable(entityId, ponId, authEnable);
            source = getPortLoc(ponId, 0);
        } catch (SetValueConflictException svce) {
            message = svce.getMessage();
            logger.debug("modifyOnuAuthMode error:{}", svce);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 获取PON口或SNI口的loc
     * 
     * @param id
     * @param type
     *            : 0:ponId, 1:sniId
     * @return
     */
    private String getPortLoc(Long id, Integer type) {
        String loc = "";
        if (type.equals(0)) {// ponId
            id = oltPonService.getPonIndex(id);
        } else if (type.equals(1)) {// sniId
            id = oltSniService.getSniIndex(id);
        }
        loc = EponIndex.getSlotNo(id) + "/" + EponIndex.getSniNo(id);
        return loc;
    }

    /**
     * ONU替换
     * 
     */
    @OperationLogProperty(actionName = "onuAuthAction", operationName = "onuAuthInstead")
    public String onuAuthInstead() throws Exception {
        String message = "success";
        Long onuIndex = EponIndex.getOnuIndex(slotNo, ponNo, onuNo);
        if (authType.equals(EponConstants.OLT_AUTHEN_MAC)) {
            try {
                onuAuthService.onuAuthMacInstead(entityId, onuIndex, macAddress);
            } catch (Exception e) {
                message = e.getMessage();
                logger.debug("onuAuthInstead error: {}", e);
                operationResult = OperationLog.FAILURE;
            }
        } else if (authType.equals(EponConstants.OLT_AUTHEN_SN)) {
            try {
                onuAuthService.onuAuthSnInstead(entityId, onuIndex, sn, password);
            } catch (Exception e) {
                message = e.getMessage();
                logger.debug("onuAuthInstead error: {}", e);
                operationResult = OperationLog.FAILURE;
            }
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 获得某个PON口的MAC认证拒绝列表
     * @return
     * @throws IOException 
     */
    public String loadRejectedMacList() throws IOException {
        List<OltAuthentication> list = onuAuthService.loadRejectedMacList(entityId, ponId);
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        JSONArray json = new JSONArray();
        for (OltAuthentication auth : list) {
            String mac = new MacUtils(auth.getOnuAuthenMacAddress()).toString(MacUtils.MAOHAO).toUpperCase();
            mac = MacUtils.convertMacToDisplayFormat(mac, macRule);
            json.add(mac);
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 显示查看ONU认证BLOCK策略
     * 
     * @return String
     * @throws Exception
     */
    public String loadOnuAuthBlock() throws Exception {
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        List<OltOnuBlockAuthen> onuBlockList = onuAuthService.getOnuAuthenBlockList(entityId, slotId, ponId);
        List<List<String>> onuBlockInfoList = new ArrayList<List<String>>();
        for (OltOnuBlockAuthen anOnuBlockList : onuBlockList) {
            List<String> blockInfo = new ArrayList<String>();
            String macStr = MacUtils.convertMacToDisplayFormat(anOnuBlockList.getMacAddress(), macRule);
            // ONU位置
            Long onuIndex = anOnuBlockList.getOnuIndex();
            Long slotNo = EponIndex.getSlotNo(onuIndex);
            Long ponNo = EponIndex.getPonNo(onuIndex);
            Long onuNo = EponIndex.getOnuNo(onuIndex);
            StringBuilder sb = new StringBuilder();
            sb.append(slotNo.toString()).append("/").append(ponNo.toString()).append(":").append(onuNo.toString());
            String onuLocation = sb.toString();
            // 最近认证时间
            Long authTime = anOnuBlockList.getAuthTime();
            authTime = authTime / 100;// 单位0.01秒，故转换为秒
            StringBuilder time = new StringBuilder();
            Long miao = authTime % 60;
            Long fen = (authTime / 60) % 60;
            Long xiaoshi = (authTime / 3600) % 24;
            Long tian = authTime / 86400;
            if (authTime == 0) {
                time.append(getString("onuAuth.lastNoAppRegist", "epon"));
            } else if (authTime < 60) {
                time.append(authTime.toString()).append(getString("COMMON.S", "epon"));
            } else if (authTime < 3600) {
                time.append(fen.toString()).append(getString("COMMON.M", "epon")).append(miao.toString())
                        .append(getString("COMMON.S", "epon"));
            } else if (authTime < 86400) {
                time.append(xiaoshi.toString()).append(getString("COMMON.H", "epon")).append(fen.toString())
                        .append(getString("COMMON.M", "epon")).append(miao.toString())
                        .append(getString("COMMON.S", "epon"));
            } else {
                time.append(tian.toString()).append(getString("COMMON.D", "epon")).append(xiaoshi.toString())
                        .append(getString("COMMON.H", "epon")).append(fen.toString())
                        .append(getString("COMMON.M", "epon")).append(miao.toString())
                        .append(getString("COMMON.S", "epon"));
            }
            if (authTime != 0) {
                time.append(getString("COMMON.before", "epon"));
            }
            String authTimeStr = time.toString();
            String snStr = anOnuBlockList.getTopOnuAuthBlockedExtLogicSn();
            String pwStr = anOnuBlockList.getTopOnuAuthBlockedExtPwd();
            blockInfo.add(onuLocation);
            blockInfo.add(macStr);
            blockInfo.add(authTimeStr);
            blockInfo.add(snStr);
            blockInfo.add(pwStr);
            blockInfo.add(anOnuBlockList.getPonId().toString());
            onuBlockInfoList.add(blockInfo);
        }
        logger.debug("authBlockData:{}", onuBlockInfoList);
        writeDataToAjax(JSONArray.fromObject(onuBlockInfoList));
        return NONE;
    }

    /**
     * 刷新ONU认证BLOCK表
     * 
     * @param entityId
     *            设备ID
     * 
     * @param ponId
     *            PON口ID
     * 
     * @return String List<OltOnuBlockAuthen>
     */
    public String refreshOnuAuthBlock() throws Exception {
        String message = "success";
        try {
            onuAuthService.refreshOnuAuthenBlockList(entityId);
        } catch (Exception e) {
            message = "failed";
            logger.error("refreshOnuAuthBlock:{}", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 刷新ONU认证表
     * 
     * @param entityId
     *            设备ID
     */
    @OperationLogProperty(actionName = "onuAuthAction", operationName = "refreshOnuAuthInfo")
    public String refreshOnuAuthInfo() throws Exception {
        // 刷新ONU认证表
        onuAuthService.refreshOnuAuthInfo(entityId);
        onuAuthService.refreshOnuAuthMode(entityId);
        onuAuthService.refreshOnuAuthEnable(entityId);
        onuAuthService.refreshOnuAuthPreType(entityId);
        return NONE;
    }

    public JSONArray getPonListObject() {
        return ponListObject;
    }

    public void setPonListObject(JSONArray ponListObject) {
        this.ponListObject = ponListObject;
    }

    public JSONArray getSlotPonObject() {
        return slotPonObject;
    }

    public void setSlotPonObject(JSONArray slotPonObject) {
        this.slotPonObject = slotPonObject;
    }

    public JSONArray getOnuMaxNumList() {
        return onuMaxNumList;
    }

    public void setOnuMaxNumList(JSONArray onuMaxNumList) {
        this.onuMaxNumList = onuMaxNumList;
    }

    public JSONArray getPonAuthEnableList() {
        return ponAuthEnableList;
    }

    public void setPonAuthEnableList(JSONArray ponAuthEnableList) {
        this.ponAuthEnableList = ponAuthEnableList;
    }

    public JSONArray getMacModePonIdList() {
        return macModePonIdList;
    }

    public void setMacModePonIdList(JSONArray macModePonIdList) {
        this.macModePonIdList = macModePonIdList;
    }

    public JSONArray getSnModePonIdList() {
        return snModePonIdList;
    }

    public void setSnModePonIdList(JSONArray snModePonIdList) {
        this.snModePonIdList = snModePonIdList;
    }

    public JSONArray getOnuAuthMacListObject() {
        return onuAuthMacListObject;
    }

    public void setOnuAuthMacListObject(JSONArray onuAuthMacListObject) {
        this.onuAuthMacListObject = onuAuthMacListObject;
    }

    public JSONArray getOnuAuthSnListObject() {
        return onuAuthSnListObject;
    }

    public void setOnuAuthSnListObject(JSONArray onuAuthSnListObject) {
        this.onuAuthSnListObject = onuAuthSnListObject;
    }

    public Integer getAuthEnable() {
        return authEnable;
    }

    public void setAuthEnable(Integer authEnable) {
        this.authEnable = authEnable;
    }

    public Integer getAuthMode() {
        return authMode;
    }

    public void setAuthMode(Integer authMode) {
        this.authMode = authMode;
    }

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    public Integer getAuthSnMode() {
        return authSnMode;
    }

    public void setAuthSnMode(Integer authSnMode) {
        this.authSnMode = authSnMode;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    public Integer getOnuAuthenticationPolicy() {
        return onuAuthenticationPolicy;
    }

    public void setOnuAuthenticationPolicy(Integer onuAuthenticationPolicy) {
        this.onuAuthenticationPolicy = onuAuthenticationPolicy;
    }

    public Integer getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public Integer getPonNo() {
        return ponNo;
    }

    public void setPonNo(Integer ponNo) {
        this.ponNo = ponNo;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Integer getOnuNo() {
        return onuNo;
    }

    public void setOnuNo(Integer onuNo) {
        this.onuNo = onuNo;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public JSONArray getAutoModePonIdList() {
        return autoModePonIdList;
    }

    public void setAutoModePonIdList(JSONArray autoModePonIdList) {
        this.autoModePonIdList = autoModePonIdList;
    }

    public JSONArray getMixModePonIdList() {
        return mixModePonIdList;
    }

    public void setMixModePonIdList(JSONArray mixModePonIdList) {
        this.mixModePonIdList = mixModePonIdList;
    }

    public JSONArray getSnPwdModePonIdList() {
        return snPwdModePonIdList;
    }

    public void setSnPwdModePonIdList(JSONArray snPwdModePonIdList) {
        this.snPwdModePonIdList = snPwdModePonIdList;
    }

    public JSONArray getOnuTypes() {
        return onuTypes;
    }

    public void setOnuTypes(JSONArray onuTypes) {
        this.onuTypes = onuTypes;
    }

}
