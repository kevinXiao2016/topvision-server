/***********************************************************************
 * $Id: OnuAuthRuleAction.java,v1.0 2013年10月25日 下午5:58:18 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:58:18
 *
 */
@Controller("onuAuthRuleAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuAuthRuleAction extends AbstractEponAction {
    private static final long serialVersionUID = -3226533653428802489L;
    private final Logger logger = LoggerFactory.getLogger(OnuAuthRuleAction.class);
    private String macAddress;
    private Long ponId;
    private Long mac;
    private Long slotId;
    private List<OltAuthentication> onuAuthRuleList;
    private String sn;
    private Integer slotNo;
    private Integer ponNo;
    private Integer onuNo;
    private Integer authType;
    private Integer authSnMode;
    private Integer authAction;
    private String password;
    private String onuPreType;
    @Autowired
    private OnuAuthService onuAuthService;
    @Autowired
    private DiscoveryService<DiscoveryData> discoveryService;

    /**
     * 根据查询条件获得ONU认证规则列表
     * 
     * @return String
     * @throws IOException 
     */
    public String getOnuAuthRule() throws IOException {
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        List<OltOnuAttribute> preTypeList = new ArrayList<OltOnuAttribute>();
        if (ponId == -1L) {
            if (slotId == -1L) {
                if (macAddress != null) {
                    // 通过MAC进行查询
                    onuAuthRuleList = onuAuthService.getOnuAuthenListByMac(entityId, macAddress);
                } else if (sn != null && !"".equals(sn)) {
                    // 通过SN进行查询
                    onuAuthRuleList = onuAuthService.getOnuAuthenListBySn(entityId, sn);
                } else if (entityId != null) {
                    // 四个查询条件都为空
                    onuAuthRuleList = onuAuthService.getOnuAuthenListByEntity(entityId);
                }
            } else {
                OltAuthentication oltAuthen = new OltAuthentication();
                oltAuthen.setSlotId(slotId);
                if (macAddress != null) {
                    oltAuthen.setOnuAuthenMacAddress(macAddress);
                } else if (sn != null && !"".equals(sn)) {
                    oltAuthen.setTopOnuAuthLogicSn(sn);
                }
                onuAuthRuleList = onuAuthService.getOnuAuthenListBySlot(oltAuthen);
            }
        } else {
            OltAuthentication oltAuthen = new OltAuthentication();
            oltAuthen.setPonId(ponId);
            if (macAddress != null) {
                oltAuthen.setOnuAuthenMacAddress(macAddress);
            } else if (sn != null) {
                oltAuthen.setTopOnuAuthLogicSn(sn);
            }
            onuAuthRuleList = onuAuthService.getOnuAuthenPreConfigList(oltAuthen);
            preTypeList = onuAuthService.getOnuPreTypeByPonId(ponId);
        }
        // 获取preType及其处理
        Map<Long, Integer> preType = new HashMap<Long, Integer>();
        List<Long> preIndexL = new ArrayList<Long>();
        if (preTypeList.size() > 0) {
            for (OltOnuAttribute pr : preTypeList) {
                if (pr.getOnuPreType() != null) {
                    Integer tmpS = pr.getOnuPreType();
                    preType.put(pr.getOnuIndex(), tmpS);
                    preIndexL.add(pr.getOnuIndex());
                }
            }
        }
        // 将数据库中获取的OltAuthentication数据转换为页面使用的authData列表数据
        List<List<String>> result = new ArrayList<List<String>>();
        if (onuAuthRuleList != null) {
            List<String> onuAuthInfo = new ArrayList<String>();
            for (OltAuthentication anOnuAuthRuleList : onuAuthRuleList) {
                Long onuAuthIndex = anOnuAuthRuleList.getOnuIndex();
                Long slotNum = EponIndex.getSlotNo(onuAuthIndex);
                Long ponNum = EponIndex.getPonNo(onuAuthIndex);
                Long onuNum = EponIndex.getOnuNo(onuAuthIndex);
                StringBuilder onuLocation = new StringBuilder();
                onuLocation.append(slotNum.toString()).append("/").append(ponNum.toString()).append(":")
                        .append(onuNum.toString());
                onuAuthInfo.add(onuLocation.toString());// ONU位置
                if (anOnuAuthRuleList.getAuthType().equals(EponConstants.OLT_AUTHEN_MAC)) {
                    onuAuthInfo.add("MAC");// 认证类型authType
                    String onuMac = new MacUtils(anOnuAuthRuleList.getOnuAuthenMacAddress()).toString(MacUtils.MAOHAO)
                            .toUpperCase();
                    //add by fanzidong,展示之前格式化MAC地址
                    onuMac = MacUtils.convertMacToDisplayFormat(onuMac, displayRule);
                    onuAuthInfo.add(onuMac);
                    if (anOnuAuthRuleList.getAuthAction().equals(EponConstants.OLT_AUTHEN_MACACTION_ACCEPT)) {
                        // onuAuthInfo.add(getString("Business.authAccept","epon"));
                        onuAuthInfo.add(EponConstants.OLT_AUTHEN_MACACTION_ACCEPT.toString());
                    } else if (anOnuAuthRuleList.getAuthAction().equals(EponConstants.OLT_AUTHEN_MACACTION_REJECT)) {
                        // onuAuthInfo.add(getString("Business.authReject","epon"));
                        onuAuthInfo.add(EponConstants.OLT_AUTHEN_MACACTION_REJECT.toString());
                    } else {
                        // onuAuthInfo.add(getString("Business.authReject","epon"));
                        onuAuthInfo.add(EponConstants.OLT_AUTHEN_MACACTION_REJECT.toString());
                    }
                } else if (anOnuAuthRuleList.getAuthType().equals(EponConstants.OLT_AUTHEN_SN)) {
                    onuAuthInfo.add("SN");// 认证类型authType
                    onuAuthInfo.add(anOnuAuthRuleList.getTopOnuAuthLogicSn());
                    if (anOnuAuthRuleList.getOnuSnMode().equals(EponConstants.OLT_AUTHEN_SNMODE_SP)) {
                        onuAuthInfo.add(anOnuAuthRuleList.getTopOnuAuthPassword());
                    } else if (anOnuAuthRuleList.getOnuSnMode().equals(EponConstants.OLT_AUTHEN_SNMODE_SN)) {
                        onuAuthInfo.add("");
                    }
                } else {
                    // TODO 觉得这么做不太合适
                    onuAuthInfo.add("");
                    onuAuthInfo.add("");
                    onuAuthInfo.add("");
                }
                onuAuthInfo.add(anOnuAuthRuleList.getPonId().toString());
                if (anOnuAuthRuleList.getOnuPreType() == null) {
                    if (preIndexL.contains(onuAuthIndex)) {
                        // onuAuthInfo.add(preType.get(onuAuthIndex));
                    } else {
                        onuAuthInfo.add("");
                    }
                } else {
                    onuAuthInfo.add(anOnuAuthRuleList.getOnuPreType().toString());
                }
                result.add(onuAuthInfo);
            }
        }
        logger.debug("authData:{}", result);
        writeDataToAjax(JSONArray.fromObject(result));
        return NONE;
    }

    /**
     * 删除认证规则
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "onuAuthRuleAction", operationName = "deleteOnuAuthRule")
    public String deleteOnuAuthRule() throws Exception {
        Long onuIndex = EponIndex.getOnuIndex(slotNo, ponNo, onuNo);
        String message = "success";
        try {
            onuAuthService.deleteAuthenPreConfig(entityId, ponId, onuIndex, authType);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("deleteOnuAuthRule error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 添加认证规则
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "onuAuthRuleAction", operationName = "addOnuAuthRule")
    public String addOnuAuthRule() throws Exception {
        Long onuIndex = EponIndex.getOnuIndex(slotNo, ponNo, onuNo);
        OltAuthentication oltAuthen = new OltAuthentication();
        oltAuthen.setOnuIndex(onuIndex);
        oltAuthen.setAuthType(authType);
        oltAuthen.setEntityId(entityId);
        oltAuthen.setPonId(ponId);
        oltAuthen.setOnuPreType(Integer.parseInt(onuPreType));
        if (authType.equals(EponConstants.OLT_AUTHEN_MAC)) {
            oltAuthen.setAuthAction(authAction);
            oltAuthen.setOnuAuthenMacAddress(macAddress);
        } else if (authType.equals(EponConstants.OLT_AUTHEN_SN)) {
            oltAuthen.setAuthAction(1);
            oltAuthen.setOnuSnMode(authSnMode);
            oltAuthen.setTopOnuAuthLogicSn(sn);
            if (authSnMode.equals(EponConstants.OLT_AUTHEN_SNMODE_SP)) {
                oltAuthen.setTopOnuAuthPassword(password);
            }
        }
        String message = "success";
        try {
            onuAuthService.addOnuAuthenPreConfig(oltAuthen);
            if (macAddress != null && !"".equals(macAddress) && authAction.equals(EponConstants.OLT_AUTHEN_MACACTION_ACCEPT)) {
                onuAuthService.deleteOnuAuthBlock(ponId, onuIndex);
            } else if (!sn.equalsIgnoreCase("")) {
                onuAuthService.deleteOnuAuthBlock(ponId, onuIndex);
            }
            if (!(onuPreType.equalsIgnoreCase("") || (authType.equals(EponConstants.OLT_AUTHEN_MAC) && authAction
                    .equals(EponConstants.OLT_AUTHEN_MACACTION_REJECT)))) {
                onuAuthService.modifyOnuPreType(entityId, onuIndex, onuPreType);
            }
            discoveryService.refresh(entityId);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("addOnuAuthRule error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(message);
        return NONE;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getMac() {
        return mac;
    }

    public void setMac(Long mac) {
        this.mac = mac;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public List<OltAuthentication> getOnuAuthRuleList() {
        return onuAuthRuleList;
    }

    public void setOnuAuthRuleList(List<OltAuthentication> onuAuthRuleList) {
        this.onuAuthRuleList = onuAuthRuleList;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
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

    public Integer getOnuNo() {
        return onuNo;
    }

    public void setOnuNo(Integer onuNo) {
        this.onuNo = onuNo;
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

    public Integer getAuthAction() {
        return authAction;
    }

    public void setAuthAction(Integer authAction) {
        this.authAction = authAction;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOnuPreType() {
        return onuPreType;
    }

    public void setOnuPreType(String onuPreType) {
        this.onuPreType = onuPreType;
    }

}
