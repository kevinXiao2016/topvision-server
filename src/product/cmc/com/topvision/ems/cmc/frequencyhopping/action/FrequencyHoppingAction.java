/***********************************************************************
 * $Id: CcmtsSpectrumGpAction.java,v1.0 2013-8-2 上午10:27:54 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGp;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpChnl;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpFreq;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpGlobal;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpHopHis;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpTemplate;
import com.topvision.ems.cmc.frequencyhopping.domain.EmsCcmtsSpectrumGp;
import com.topvision.ems.cmc.frequencyhopping.domain.EmsCcmtsSpectrumGpFreq;
import com.topvision.ems.cmc.frequencyhopping.domain.SpectrumTempConfigLog;
import com.topvision.ems.cmc.frequencyhopping.domain.SpectrumTempConfigLogDetail;
import com.topvision.ems.cmc.frequencyhopping.service.FrequencyHoppingService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.domain.PhysAddress;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;

/**
 * @author haojie
 * @created @2013-8-2-上午10:27:54
 * @renamed by YangYi @2013-10-28 原名CcmtsSpectrumGpAction
 */
@Controller("frequencyHoppingAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FrequencyHoppingAction extends BaseAction {
    private static final long serialVersionUID = -6051062670260161950L;
    private final Logger logger = LoggerFactory.getLogger(FrequencyHoppingAction.class);
    private static final int GROUP_MODIFY_FLAG = 1;// 1表示为修改
    @Resource(name = "frequencyHoppingService")
    private FrequencyHoppingService frequencyHoppingService;
    private CcmtsSpectrumGpGlobal ccmtsSpectrumGpGlobal;
    private JSONObject ccmtsSpectrumGpGlobalJson;
    private Long entityId;
    private Long channelIndex;
    // 判断新增还是修改跳频组
    private Integer groupModifyFlag;
    private List<String> groupIds;
    private Long emsGroupId;
    private Long configLogId;
    private int start;
    private int limit;
    private Long tempLateId;
    private Integer globalAdminStatus;
    private Integer snrQueryPeriod;
    private Integer hopHisMaxCount;
    private Integer groupId;
    private Integer channelId;
    private Integer chnlTertiaryProf;
    private Integer chnlSecondaryProf;
    private Integer gpForUpChannel1;
    private Integer gpForUpChannel2;
    private Integer gpForUpChannel3;
    private Integer gpForUpChannel4;
    private Integer hopPeriod;
    private Integer snrThres1;
    private Integer snrThres2;
    private Integer maxHopLimit;
    private Integer adminStatus;
    private Integer groupPolicy;
    private Integer fecThresCorrect1;
    private Integer fecThresCorrect2;
    private Integer fecThresUnCorrect1;
    private Integer fecThresUnCorrect2;
    private Integer groupPriority1st;
    private Integer groupPriority2st;
    private Integer groupPriority3st;
    private String cmcMac;
    private String templateName;
    private String selectGpString;
    private String entityIds;
    private String emsGroupName;
    private String freqListString;
    private JSONObject ccmtsSpectrumGpJson;
    private JSONObject groupTemplateJson;
    private JSONObject emsCcmtsSpectrumGpJson;
    private JSONArray emsCcmtsSpectrumGpListJson;
    private JSONArray ccmtsSpectrumGpListJson;

    /**
     * showSpectrumGpGlobal.jsp 显示CCMTS自动跳频全局配置页面
     * 
     * @return String
     */
    public String showDeviceGpGlobal() {
        ccmtsSpectrumGpGlobal = frequencyHoppingService.getDeviceGpGlobal(entityId);
        ccmtsSpectrumGpGlobalJson = JSONObject.fromObject(ccmtsSpectrumGpGlobal);
        return SUCCESS;
    }

    /**
     * showSpectrumGpGlobal.jsp 修改CCMTS自动跳频全局配置
     * 
     * @return
     */
    public String modifyDeviceGpGlobal() {
        String message;
        CcmtsSpectrumGpGlobal ccmtsSpectrumGpGlobal = new CcmtsSpectrumGpGlobal();
        ccmtsSpectrumGpGlobal.setEntityId(entityId);
        ccmtsSpectrumGpGlobal.setGlobalAdminStatus(globalAdminStatus);
        ccmtsSpectrumGpGlobal.setHopHisMaxCount(hopHisMaxCount);
        ccmtsSpectrumGpGlobal.setSnrQueryPeriod(snrQueryPeriod);
        try {
            frequencyHoppingService.modifyDeviceGpGlobal(ccmtsSpectrumGpGlobal);
            message = "success";
        } catch (Exception e) {
            logger.debug("modifyDeviceGpGlobal errer", e);
            message = e.getMessage();
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * showSpectrumGpGlobal.jsp 刷新CCMTS自动跳频全局配置
     * 
     * @return
     */
    public String refreshGpGlobalFromDevice() {
        String message;
        try {
            frequencyHoppingService.refreshGpGlobalFromDevice(entityId);
            message = "success";
        } catch (Exception e) {
            logger.debug("refreshGpGlobalFromDevice errer", e);
            message = e.getMessage();
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * showGroupHopHis.jsp 加载信道历史查看页面
     * 
     * @return
     */
    public String showGroupHopHisPage() {
        return SUCCESS;
    }

    /**
     * showGroupHopHis.jsp 获取信道跳频历史记录
     * 
     * @return
     * @throws IOException
     */
    public String loadGroupHopHisList() throws IOException {
        List<CcmtsSpectrumGpHopHis> hopHisList = frequencyHoppingService.getGroupHopHisList(entityId, channelIndex);
        // add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (CcmtsSpectrumGpHopHis gpHopHis : hopHisList) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(gpHopHis.getCmcMac(), macRule);
            gpHopHis.setCmcMac(formatedMac);
        }
        JSONArray jArray = JSONArray.fromObject(hopHisList);
        writeDataToAjax(jArray);
        return NONE;
    }

    /**
     * showGroupHopHis.jsp 清空信道跳频历史记录
     * 
     * @return
     */
    public String deleteGroupHopHis() {
        CcmtsSpectrumGpChnl chnlGroup = new CcmtsSpectrumGpChnl();
        chnlGroup.setEntityId(entityId);
        chnlGroup.setChannelIndex(channelIndex);
        chnlGroup.setChnlCmcMac(cmcMac);
        chnlGroup.setCmcMacForDevice(new PhysAddress(cmcMac));
        chnlGroup.setChnlId(channelId);
        chnlGroup.setChnlClearHistory(CcmtsSpectrumGpChnl.CHNL_CLEAR_HISTORY);
        frequencyHoppingService.deleteGroupHopHis(chnlGroup);
        return NONE;
    }

    /**
     * showGroupHopHis.jsp 从设备上获取信道历史
     * 
     * @return
     */
    public String refreshGpHopHisFromDevice() {
        CcmtsSpectrumGpHopHis hopHis = new CcmtsSpectrumGpHopHis();
        hopHis.setEntityId(entityId);
        hopHis.setChannelIndex(channelIndex);
        hopHis.setCmcMac(cmcMac);
        hopHis.setChnlId(channelId);
        frequencyHoppingService.refreshGpHopHisFromDevice(hopHis);
        return NONE;
    }

    /**
     * upStreamBaseInfoConfig.jsp 获取当前设备上的所有跳频组
     * 
     * @return
     * @throws IOException
     */
    public String loadAllDeviceGroup() throws IOException {
        List<CcmtsSpectrumGp> goupList = frequencyHoppingService.getAllDeviceGroup(entityId);
        JSONArray typeJson = JSONArray.fromObject(goupList);
        writeDataToAjax(typeJson);
        return NONE;
    }

    /**
     * showGlobalSpectrumGpMgmt.jsp 跳转全局自动跳频组管理页面
     * 
     * @return
     */
    public String showGlobalSpectrumGp() {
        return SUCCESS;
    }

    /**
     * showGlobalSpectrumGpMgmt.jsp,showGlobalGroupList.jsp 加载全局跳频组列表信息
     * 
     * @return
     */
    public String loadGlobalSpectrumGpList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<EmsCcmtsSpectrumGp> emsCcmtsSpectrumGpList = new ArrayList<EmsCcmtsSpectrumGp>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", start);
        map.put("limit", limit);
        emsCcmtsSpectrumGpList = frequencyHoppingService.getGlobalSpectrumGpList(map);
        Integer emsCcmtsSpectrumGpNum = frequencyHoppingService.getGlobalSpectrumGpNum();
        json.put("data", emsCcmtsSpectrumGpList);
        json.put("rowCount", emsCcmtsSpectrumGpNum);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * showGlobalSpectrumGpMgmt.jsp 删除全局自动跳频组
     * 
     * @return
     */
    public String deleteGlobalSpectrumGp() {
        String message;
        String groupIdList = groupIds.get(0);
        String[] idList = groupIdList.split(Symbol.COMMA);
        List<Long> groupIds = new ArrayList<Long>();
        for (int j = 0; j < idList.length; j++) {
            groupIds.add(Long.parseLong(idList[j]));
        }
        try {
            frequencyHoppingService.deleteGlobalSpectrumGp(groupIds);
            message = "success";
        } catch (Exception e) {
            logger.debug("deleteGlobalSpectrumGp errer", e);
            message = e.getMessage();
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * showGlobalSpectrumGpMgmt.jsp,showNewGlobalSpectrumGp.jsp 显示新增全局跳频组页面
     * 
     * @return
     */
    public String showNewGlobalSpectrumGp() {
        EmsCcmtsSpectrumGp emsCcmtsSpectrumGp = new EmsCcmtsSpectrumGp();
        List<EmsCcmtsSpectrumGp> emsCcmtsSpectrumGpList = new ArrayList<EmsCcmtsSpectrumGp>();
        emsCcmtsSpectrumGpList = frequencyHoppingService.getGlobalSpectrumGpList();
        if (groupModifyFlag == GROUP_MODIFY_FLAG) {
            emsCcmtsSpectrumGp = frequencyHoppingService.getGlobalSpectrumGpById(emsGroupId);
        }
        emsCcmtsSpectrumGpJson = JSONObject.fromObject(emsCcmtsSpectrumGp);
        emsCcmtsSpectrumGpListJson = JSONArray.fromObject(emsCcmtsSpectrumGpList);
        return SUCCESS;
    }

    /**
     * showSpectrumGpMgmt.jsp,showGlobalGroupList.jsp 跳转选择全局跳频组界面
     * 
     * @return
     */
    public String showGlobalGroupList() {
        List<CcmtsSpectrumGp> ccmtsSpectrumGpList = new ArrayList<CcmtsSpectrumGp>();
        // 用于分配全局跳频组设置到设备时的groupId
        ccmtsSpectrumGpList = frequencyHoppingService.getDeviceGroupList(entityId);
        ccmtsSpectrumGpListJson = JSONArray.fromObject(ccmtsSpectrumGpList);
        return SUCCESS;
    }

    /**
     * showGlobalGroupList.jsp 下发全局跳频组到设备
     * 
     * @return
     */
    public String addDeviceGroupFromGlobal() {
        String message;
        try {
            frequencyHoppingService.addDeviceGroupFromGlobal(entityId, emsGroupId, groupId);
            message = "success";
        } catch (Exception e) {
            logger.debug("addDeviceGroupFromGlobal errer", e);
            message = e.getMessage();
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * showConfigLogDetail.jsp 获取自动跳频模板批量配置详细记录
     * 
     * @return
     */
    public String loadLogDetailList() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("configLogId", configLogId);
        param.put("start", start);
        param.put("limit", limit);
        List<SpectrumTempConfigLogDetail> detailList = frequencyHoppingService.getLogDetailList(param);
        int totalCount = frequencyHoppingService.getDetailCount(configLogId);
        JSONObject json = new JSONObject();
        json.put("rowCount", totalCount);
        json.put("data", detailList);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * showSpectrumGpMgnt.jsp 获取某一个信道使用到信道的数目
     * 
     * @return
     */
    public String loadGroupChlNum() {
        String result = "";
        try {
            result = frequencyHoppingService.getGroupChlNum(entityId, groupId);
        } catch (Exception e) {
            logger.debug("loadGroupChlNum errer", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * showSpectrumGpTempLate.jsp 加载自动跳频功能模板创建的页面
     * 
     * @return
     */
    public String showSpectrumGpTempLate() {
        return SUCCESS;
    }

    /**
     * showSpectrumGpTempLate.jsp 获取自动跳频模板列表
     * 
     * @return
     * @throws IOException
     */
    public String loadSpectrumGpTempLateList() throws IOException {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("start", start);
        param.put("limit", limit);
        List<CcmtsSpectrumGpTemplate> gpTemplateList = frequencyHoppingService.getSpectrumGpTempLateList(param);
        int totalTemp = frequencyHoppingService.getTempCount();
        JSONObject json = new JSONObject();
        json.put("rowCount", totalTemp);
        json.put("data", gpTemplateList);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * showSpectrumGpTempLate.jsp 删除自动跳频模板
     * 
     * @return
     */
    public String deleteSpectrumGpTempLate() {
        frequencyHoppingService.deleteSpectrumGpTempLate(tempLateId);
        return NONE;
    }

    /**
     * showSpectrumGpTempLate.jsp,showNewGpTempLate.jsp 加载自动跳频模板新建页面
     * 
     * @return
     */
    public String showNewGpTempLate() {
        CcmtsSpectrumGpTemplate gpTemplate = new CcmtsSpectrumGpTemplate();
        List<EmsCcmtsSpectrumGp> tempRelationGp = new ArrayList<EmsCcmtsSpectrumGp>();
        if (groupModifyFlag == 1) { // modify Template
            gpTemplate = frequencyHoppingService.getSpectrumGpTempLateById(tempLateId);
            tempRelationGp = frequencyHoppingService.getTempRelationGp(tempLateId);
        }
        groupTemplateJson = JSONObject.fromObject(gpTemplate);
        emsCcmtsSpectrumGpListJson = JSONArray.fromObject(tempRelationGp);
        return SUCCESS;
    }

    /**
     * showNewGpTempLate.jsp 添加自动跳频模板
     * 
     * @return
     */
    public String addSpectrumGpTempLate() {
        CcmtsSpectrumGpTemplate gpTemplate = new CcmtsSpectrumGpTemplate();
        gpTemplate.setTemplateName(templateName);
        gpTemplate.setGlobalAdminStatus(globalAdminStatus);
        gpTemplate.setSnrQueryPeriod(snrQueryPeriod);
        gpTemplate.setHopHisMaxCount(hopHisMaxCount);
        gpTemplate.setChnlTertiaryProf(chnlTertiaryProf);
        gpTemplate.setChnlSecondaryProf(chnlSecondaryProf);
        frequencyHoppingService.addSpectrumGpTempLate(gpTemplate, selectGpString);
        return NONE;
    }

    /**
     * showNewGpTempLate.jsp 修改自动跳频模板
     * 
     * @return
     */
    public String modifySpectrumGpTempLate() {
        CcmtsSpectrumGpTemplate gpTemplate = new CcmtsSpectrumGpTemplate();
        gpTemplate.setTempLateId(tempLateId);
        gpTemplate.setTemplateName(templateName);
        gpTemplate.setGlobalAdminStatus(globalAdminStatus);
        gpTemplate.setSnrQueryPeriod(snrQueryPeriod);
        gpTemplate.setHopHisMaxCount(hopHisMaxCount);
        gpTemplate.setChnlTertiaryProf(chnlTertiaryProf);
        gpTemplate.setChnlSecondaryProf(chnlSecondaryProf);
        frequencyHoppingService.txModifySpectrumGpTempLate(gpTemplate, selectGpString);
        return NONE;
    }

    /**
     * showNewGpTempLate.jsp 获取所有的全局跳频组
     * 
     * @return
     * @throws IOException
     */
    public String loadAllGlobalGroup() {
        List<EmsCcmtsSpectrumGp> globalGpList = frequencyHoppingService.getAllGlobalGroup();
        JSONArray globalGpJson = JSONArray.fromObject(globalGpList);
        writeDataToAjax(globalGpJson);
        return NONE;
    }

    /**
     * showGpTempLateConfig.jsp 加载自动跳频模板下发页面
     * 
     * @return
     */
    public String showTempLateConfig() {
        return SUCCESS;
    }

    /**
     * showGpTempLateConfig.jsp 获取自动跳频模板相关联的所有全局跳频组
     * 
     * @return
     */
    public String loadTempRelationGp() {
        List<EmsCcmtsSpectrumGp> tempRelationGp = frequencyHoppingService.getTempRelationGp(tempLateId);
        emsCcmtsSpectrumGpListJson = JSONArray.fromObject(tempRelationGp);
        writeDataToAjax(emsCcmtsSpectrumGpListJson);
        return NONE;
    }

    /**
     * showGpTempLateConfig.jsp 下发跳频模板
     * 
     * @return
     */
    public String confSpectrumGpTempLate() {
        String message;
        String[] entityIdArray = entityIds.split(Symbol.COMMA);
        List<Long> entityIdList = new ArrayList<Long>();
        for (String entityId : entityIdArray) {
            entityIdList.add(Long.parseLong(entityId));
        }
        CcmtsSpectrumGpTemplate ccmtsSpectrumGpTemplate = frequencyHoppingService.getSpectrumGpTempLateById(tempLateId);
        ccmtsSpectrumGpTemplate.setGpForUpChannel1(gpForUpChannel1);
        ccmtsSpectrumGpTemplate.setGpForUpChannel2(gpForUpChannel2);
        ccmtsSpectrumGpTemplate.setGpForUpChannel3(gpForUpChannel3);
        ccmtsSpectrumGpTemplate.setGpForUpChannel4(gpForUpChannel4);
        try {
            frequencyHoppingService.executeSpectrumGroupConfig(entityIdList, ccmtsSpectrumGpTemplate);
            message = "success";
        } catch (Exception e) {
            logger.debug("confSpectrumGpTempLate errer", e);
            message = e.getMessage();
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * showTemplateConfigLog.jsp 加载自动跳频模板批量配置日志记录页面
     * 
     * @return
     */
    public String showTemplateConfigLog() {
        return SUCCESS;
    }

    /**
     * showTemplateConfigLog.jsp 获取自动跳频模板批量配置日志记录
     * 
     * @return
     */
    public String loadTempConfigLogList() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("start", start);
        param.put("limit", limit);
        List<SpectrumTempConfigLog> logList = frequencyHoppingService.getConfigLogList(param);
        int totalCount = frequencyHoppingService.getLogCount();
        JSONObject json = new JSONObject();
        json.put("rowCount", totalCount);
        json.put("data", logList);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * showTemplateConfigLog.jsp,showConfigLogDetail.jsp 加载自动跳频模板批量配置详细记录页面
     * 
     * @return
     */
    public String showConfigLogDetail() {
        return SUCCESS;
    }

    /**
     * showSpectrumGpMgmt.jsp 显示CCMTS自动跳频组管理页面
     * 
     * @return
     */
    public String showGroupManagePage() {
        return SUCCESS;
    }

    /**
     * showSpectrumGpMgmt.jsp 获取设备侧自动跳频组列表信息
     * 
     * @return
     */
    public String loadDeviceGroupList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<CcmtsSpectrumGp> ccmtsSpectrumGpList = new ArrayList<CcmtsSpectrumGp>();
        ccmtsSpectrumGpList = frequencyHoppingService.getDeviceGroupList(entityId);
        json.put("data", ccmtsSpectrumGpList);
        json.put("rowCount", ccmtsSpectrumGpList.size());
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * showSpectrumGpMgmt.jsp 删除设备侧自动跳频组
     * 
     * @return
     */
    public String deleteDeviceGroup() {
        String message;
        try {
            frequencyHoppingService.deleteDeviceGroup(entityId, groupId);
            message = "success";
        } catch (Exception e) {
            logger.debug("deleteDeviceGroup errer", e);
            message = e.getMessage();
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * showSpectrumGpMgmt.jsp 刷新设备的跳频组信息
     * 
     * @return
     */
    public String refreshGroupFromDevice() {
        String message;
        try {
            frequencyHoppingService.refreshGroupFromDevice(entityId);
            message = "success";
        } catch (Exception e) {
            logger.debug("refreshGroupFromDevice errer", e);
            message = e.getMessage();
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * showSpectrumGpMgmt.jsp,showNewDeviceGroup.jsp 显示新增跳频组页面
     * 
     * @return
     */
    public String showNewDeviceGroup() {
        CcmtsSpectrumGp ccmtsSpectrumGp = new CcmtsSpectrumGp();
        List<CcmtsSpectrumGp> ccmtsSpectrumGpList = new ArrayList<CcmtsSpectrumGp>();
        if (groupModifyFlag == GROUP_MODIFY_FLAG) {
            // 修改跳频组
            ccmtsSpectrumGp = frequencyHoppingService.getDeviceGroup(entityId, groupId);
        } else {
            // 获取已存在的跳频组信息，便于初始化可选跳频组groupId下拉框
            ccmtsSpectrumGpList = frequencyHoppingService.getDeviceGroupList(entityId);
        }
        ccmtsSpectrumGpJson = JSONObject.fromObject(ccmtsSpectrumGp);
        ccmtsSpectrumGpListJson = JSONArray.fromObject(ccmtsSpectrumGpList);
        return SUCCESS;
    }

    /**
     * showSpectrumGpMgmt.jsp,showNewDeviceGroup.jsp 新增跳频组
     * 
     * @return
     */
    public String addDeviceGroup() {
        String message;
        // 封装GROUP
        CcmtsSpectrumGp ccmtsSpectrumGp = new CcmtsSpectrumGp();
        ccmtsSpectrumGp.setEntityId(entityId);
        ccmtsSpectrumGp.setAdminStatus(adminStatus);
        ccmtsSpectrumGp.setFecThresCorrect1(fecThresCorrect1);
        ccmtsSpectrumGp.setFecThresCorrect2(fecThresCorrect2);
        ccmtsSpectrumGp.setFecThresUnCorrect1(fecThresUnCorrect1);
        ccmtsSpectrumGp.setFecThresUnCorrect2(fecThresUnCorrect2);
        ccmtsSpectrumGp.setGroupId(groupId);
        ccmtsSpectrumGp.setGroupPolicy(groupPolicy);
        ccmtsSpectrumGp.setGroupPriority1st(groupPriority1st);
        ccmtsSpectrumGp.setGroupPriority2st(groupPriority2st);
        ccmtsSpectrumGp.setGroupPriority3st(groupPriority3st);
        ccmtsSpectrumGp.setHopPeriod(hopPeriod);
        ccmtsSpectrumGp.setMaxHopLimit(maxHopLimit);
        ccmtsSpectrumGp.setSnrThres1(snrThres1);
        ccmtsSpectrumGp.setSnrThres2(snrThres2);
        // 封装跳频组
        List<CcmtsSpectrumGpFreq> ccmtsSpectrumGpFreqList = new ArrayList<CcmtsSpectrumGpFreq>();
        String[] freqList = freqListString.split(Symbol.COMMA);
        for (int i = 0; i < freqList.length; i++) {
            String[] params = freqList[i].split(Symbol.UNDERLINE);
            if (!Symbol.EMPTY_STRING.equals(params[0])) {
                CcmtsSpectrumGpFreq ccmtsSpectrumGpFreq = new CcmtsSpectrumGpFreq();
                ccmtsSpectrumGpFreq.setEntityId(entityId);
                ccmtsSpectrumGpFreq.setGroupId(groupId);
                ccmtsSpectrumGpFreq.setFreqIndex(Integer.parseInt(params[0]));
                ccmtsSpectrumGpFreq.setFreqFrequency(Integer.parseInt(params[1]));
                ccmtsSpectrumGpFreq.setFreqMaxWidth(Integer.parseInt(params[2]));
                ccmtsSpectrumGpFreq.setFreqPower(Integer.parseInt(params[3]));
                ccmtsSpectrumGpFreqList.add(ccmtsSpectrumGpFreq);
            }
        }
        ccmtsSpectrumGp.setCcmtsSpectrumGpFreqList(ccmtsSpectrumGpFreqList);
        try {
            frequencyHoppingService.addDeviceGroup(ccmtsSpectrumGp);
            message = "success";
        } catch (Exception e) {
            logger.debug("addDeviceGroup errer", e);
            message = e.getMessage();
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * showSpectrumGpMgmt.jsp 保存设备跳频组到网管全局跳频组
     * 
     * @return
     */
    public String transGroupFromDeviceToGlobal() {
        String message;
        String groupIdList = groupIds.get(0);
        String[] idList = groupIdList.split(Symbol.COMMA);
        List<Long> groupIds = new ArrayList<Long>();
        for (int j = 0; j < idList.length; j++) {
            groupIds.add(Long.parseLong(idList[j]));
        }
        try {
            frequencyHoppingService.transGroupFromDeviceToGlobal(entityId, groupIds);
            message = "success";
        } catch (Exception e) {
            logger.debug("transGroupFromDeviceToGlobal errer", e);
            message = e.getMessage();
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * showNewGlobalSpectrumGp.jsp 修改全局跳频组
     * 
     * @return
     */
    public String modifyGlobalSpectrumGp() {
        String message;
        // 封装GROUP
        EmsCcmtsSpectrumGp emsCcmtsSpectrumGp = new EmsCcmtsSpectrumGp();
        emsCcmtsSpectrumGp.setEmsGroupId(emsGroupId);
        emsCcmtsSpectrumGp.setEmsGroupName(emsGroupName);
        emsCcmtsSpectrumGp.setAdminStatus(adminStatus);
        emsCcmtsSpectrumGp.setFecThresCorrect1(fecThresCorrect1);
        emsCcmtsSpectrumGp.setFecThresCorrect2(fecThresCorrect2);
        emsCcmtsSpectrumGp.setFecThresUnCorrect1(fecThresUnCorrect1);
        emsCcmtsSpectrumGp.setFecThresUnCorrect2(fecThresUnCorrect2);
        emsCcmtsSpectrumGp.setGroupPolicy(groupPolicy);
        emsCcmtsSpectrumGp.setGroupPriority1st(groupPriority1st);
        emsCcmtsSpectrumGp.setGroupPriority2st(groupPriority2st);
        emsCcmtsSpectrumGp.setGroupPriority3st(groupPriority3st);
        emsCcmtsSpectrumGp.setHopPeriod(hopPeriod);
        emsCcmtsSpectrumGp.setMaxHopLimit(maxHopLimit);
        emsCcmtsSpectrumGp.setSnrThres1(snrThres1);
        emsCcmtsSpectrumGp.setSnrThres2(snrThres2);
        // 封装跳频组
        List<EmsCcmtsSpectrumGpFreq> emsCcmtsSpectrumGpFreqList = new ArrayList<EmsCcmtsSpectrumGpFreq>();
        String[] freqList = freqListString.split(Symbol.COMMA);
        for (int i = 0; i < freqList.length; i++) {
            String[] params = freqList[i].split(Symbol.UNDERLINE);
            if (!Symbol.EMPTY_STRING.equals(params[0])) {
                EmsCcmtsSpectrumGpFreq emsCcmtsSpectrumGpFreq = new EmsCcmtsSpectrumGpFreq();
                emsCcmtsSpectrumGpFreq.setEmsGroupId(emsGroupId);
                emsCcmtsSpectrumGpFreq.setFreqIndex(Integer.parseInt(params[0]));
                emsCcmtsSpectrumGpFreq.setFreqFrequency(Integer.parseInt(params[1]));
                emsCcmtsSpectrumGpFreq.setFreqMaxWidth(Integer.parseInt(params[2]));
                emsCcmtsSpectrumGpFreq.setFreqPower(Integer.parseInt(params[3]));
                emsCcmtsSpectrumGpFreqList.add(emsCcmtsSpectrumGpFreq);
            }
        }
        emsCcmtsSpectrumGp.setEmsCcmtsSpectrumGpFreqList(emsCcmtsSpectrumGpFreqList);
        try {
            frequencyHoppingService.modifyGlobalSpectrumGp(emsCcmtsSpectrumGp);
            message = "success";
        } catch (Exception e) {
            logger.debug("modifyGlobalSpectrumGp errer", e);
            message = e.getMessage();
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * showNewDeviceGroup.jsp 修改跳频组
     * 
     * @return
     */
    public String modifyDeviceGroup() {
        String message;
        // 封装GROUP
        CcmtsSpectrumGp ccmtsSpectrumGp = new CcmtsSpectrumGp();
        ccmtsSpectrumGp.setEntityId(entityId);
        ccmtsSpectrumGp.setAdminStatus(adminStatus);
        ccmtsSpectrumGp.setFecThresCorrect1(fecThresCorrect1);
        ccmtsSpectrumGp.setFecThresCorrect2(fecThresCorrect2);
        ccmtsSpectrumGp.setFecThresUnCorrect1(fecThresUnCorrect1);
        ccmtsSpectrumGp.setFecThresUnCorrect2(fecThresUnCorrect2);
        ccmtsSpectrumGp.setGroupId(groupId);
        ccmtsSpectrumGp.setGroupPolicy(groupPolicy);
        ccmtsSpectrumGp.setGroupPriority1st(groupPriority1st);
        ccmtsSpectrumGp.setGroupPriority2st(groupPriority2st);
        ccmtsSpectrumGp.setGroupPriority3st(groupPriority3st);
        ccmtsSpectrumGp.setHopPeriod(hopPeriod);
        ccmtsSpectrumGp.setMaxHopLimit(maxHopLimit);
        ccmtsSpectrumGp.setSnrThres1(snrThres1);
        ccmtsSpectrumGp.setSnrThres2(snrThres2);
        // 封装跳频组
        List<CcmtsSpectrumGpFreq> ccmtsSpectrumGpFreqList = new ArrayList<CcmtsSpectrumGpFreq>();
        String[] freqList = freqListString.split(Symbol.COMMA);
        for (int i = 0; i < freqList.length; i++) {
            String[] params = freqList[i].split(Symbol.UNDERLINE);
            if (!Symbol.EMPTY_STRING.equals(params[0])) {
                CcmtsSpectrumGpFreq ccmtsSpectrumGpFreq = new CcmtsSpectrumGpFreq();
                ccmtsSpectrumGpFreq.setEntityId(entityId);
                ccmtsSpectrumGpFreq.setGroupId(groupId);
                ccmtsSpectrumGpFreq.setFreqIndex(Integer.parseInt(params[0]));
                ccmtsSpectrumGpFreq.setFreqFrequency(Integer.parseInt(params[1]));
                ccmtsSpectrumGpFreq.setFreqMaxWidth(Integer.parseInt(params[2]));
                ccmtsSpectrumGpFreq.setFreqPower(Integer.parseInt(params[3]));
                ccmtsSpectrumGpFreqList.add(ccmtsSpectrumGpFreq);
            }
        }
        ccmtsSpectrumGp.setCcmtsSpectrumGpFreqList(ccmtsSpectrumGpFreqList);
        try {
            frequencyHoppingService.modifyDeviceGroup(ccmtsSpectrumGp);
            message = "success";
        } catch (Exception e) {
            logger.debug("modifyDeviceGroup errer", e);
            message = e.getMessage();
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * showNewGlobalSpectrumGp.jsp 新增全局跳频组
     * 
     * @return
     */
    public String addGlobalSpectrumGp() {
        String message;
        // 封装GROUP
        EmsCcmtsSpectrumGp emsCcmtsSpectrumGp = new EmsCcmtsSpectrumGp();
        emsCcmtsSpectrumGp.setEmsGroupName(emsGroupName);
        emsCcmtsSpectrumGp.setAdminStatus(adminStatus);
        emsCcmtsSpectrumGp.setFecThresCorrect1(fecThresCorrect1);
        emsCcmtsSpectrumGp.setFecThresCorrect2(fecThresCorrect2);
        emsCcmtsSpectrumGp.setFecThresUnCorrect1(fecThresUnCorrect1);
        emsCcmtsSpectrumGp.setFecThresUnCorrect2(fecThresUnCorrect2);
        emsCcmtsSpectrumGp.setGroupPolicy(groupPolicy);
        emsCcmtsSpectrumGp.setGroupPriority1st(groupPriority1st);
        emsCcmtsSpectrumGp.setGroupPriority2st(groupPriority2st);
        emsCcmtsSpectrumGp.setGroupPriority3st(groupPriority3st);
        emsCcmtsSpectrumGp.setHopPeriod(hopPeriod);
        emsCcmtsSpectrumGp.setMaxHopLimit(maxHopLimit);
        emsCcmtsSpectrumGp.setSnrThres1(snrThres1);
        emsCcmtsSpectrumGp.setSnrThres2(snrThres2);
        // 封装跳频组
        List<EmsCcmtsSpectrumGpFreq> emsCcmtsSpectrumGpFreqList = new ArrayList<EmsCcmtsSpectrumGpFreq>();
        String[] freqList = freqListString.split(Symbol.COMMA);
        for (int i = 0; i < freqList.length; i++) {
            String[] params = freqList[i].split(Symbol.UNDERLINE);
            if (!Symbol.EMPTY_STRING.equals(params[0])) {
                EmsCcmtsSpectrumGpFreq emsCcmtsSpectrumGpFreq = new EmsCcmtsSpectrumGpFreq();
                emsCcmtsSpectrumGpFreq.setFreqIndex(Integer.parseInt(params[0]));
                emsCcmtsSpectrumGpFreq.setFreqFrequency(Integer.parseInt(params[1]));
                emsCcmtsSpectrumGpFreq.setFreqMaxWidth(Integer.parseInt(params[2]));
                emsCcmtsSpectrumGpFreq.setFreqPower(Integer.parseInt(params[3]));
                emsCcmtsSpectrumGpFreqList.add(emsCcmtsSpectrumGpFreq);
            }
        }
        emsCcmtsSpectrumGp.setEmsCcmtsSpectrumGpFreqList(emsCcmtsSpectrumGpFreqList);
        try {
            frequencyHoppingService.addGlobalSpectrumGp(emsCcmtsSpectrumGp);
            message = "success";
        } catch (Exception e) {
            logger.debug("addGlobalSpectrumGp errer", e);
            message = e.getMessage();
        }
        writeDataToAjax(message);
        return NONE;
    }

    public CcmtsSpectrumGpGlobal getCcmtsSpectrumGpGlobal() {
        return ccmtsSpectrumGpGlobal;
    }

    public void setCcmtsSpectrumGpGlobal(CcmtsSpectrumGpGlobal ccmtsSpectrumGpGlobal) {
        this.ccmtsSpectrumGpGlobal = ccmtsSpectrumGpGlobal;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public JSONObject getCcmtsSpectrumGpGlobalJson() {
        return ccmtsSpectrumGpGlobalJson;
    }

    public void setCcmtsSpectrumGpGlobalJson(JSONObject ccmtsSpectrumGpGlobalJson) {
        this.ccmtsSpectrumGpGlobalJson = ccmtsSpectrumGpGlobalJson;
    }

    public Integer getGlobalAdminStatus() {
        return globalAdminStatus;
    }

    public void setGlobalAdminStatus(Integer globalAdminStatus) {
        this.globalAdminStatus = globalAdminStatus;
    }

    public Integer getSnrQueryPeriod() {
        return snrQueryPeriod;
    }

    public void setSnrQueryPeriod(Integer snrQueryPeriod) {
        this.snrQueryPeriod = snrQueryPeriod;
    }

    public Integer getHopHisMaxCount() {
        return hopHisMaxCount;
    }

    public void setHopHisMaxCount(Integer hopHisMaxCount) {
        this.hopHisMaxCount = hopHisMaxCount;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getCmcMac() {
        return cmcMac;
    }

    public void setCmcMac(String cmcMac) {
        this.cmcMac = cmcMac;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public JSONArray getCcmtsSpectrumGpListJson() {
        return ccmtsSpectrumGpListJson;
    }

    public void setCcmtsSpectrumGpListJson(JSONArray ccmtsSpectrumGpListJson) {
        this.ccmtsSpectrumGpListJson = ccmtsSpectrumGpListJson;
    }

    public Logger getLogger() {
        return logger;
    }

    public Integer getGroupModifyFlag() {
        return groupModifyFlag;
    }

    public void setGroupModifyFlag(Integer groupModifyFlag) {
        this.groupModifyFlag = groupModifyFlag;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }

    public Long getEmsGroupId() {
        return emsGroupId;
    }

    public void setEmsGroupId(Long emsGroupId) {
        this.emsGroupId = emsGroupId;
    }

    public JSONObject getEmsCcmtsSpectrumGpJson() {
        return emsCcmtsSpectrumGpJson;
    }

    public void setEmsCcmtsSpectrumGpJson(JSONObject emsCcmtsSpectrumGpJson) {
        this.emsCcmtsSpectrumGpJson = emsCcmtsSpectrumGpJson;
    }

    public JSONArray getEmsCcmtsSpectrumGpListJson() {
        return emsCcmtsSpectrumGpListJson;
    }

    public void setEmsCcmtsSpectrumGpListJson(JSONArray emsCcmtsSpectrumGpListJson) {
        this.emsCcmtsSpectrumGpListJson = emsCcmtsSpectrumGpListJson;
    }

    public Long getConfigLogId() {
        return configLogId;
    }

    public void setConfigLogId(Long configLogId) {
        this.configLogId = configLogId;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Long getTempLateId() {
        return tempLateId;
    }

    public void setTempLateId(Long tempLateId) {
        this.tempLateId = tempLateId;
    }

    public Integer getChnlTertiaryProf() {
        return chnlTertiaryProf;
    }

    public void setChnlTertiaryProf(Integer chnlTertiaryProf) {
        this.chnlTertiaryProf = chnlTertiaryProf;
    }

    public Integer getChnlSecondaryProf() {
        return chnlSecondaryProf;
    }

    public void setChnlSecondaryProf(Integer chnlSecondaryProf) {
        this.chnlSecondaryProf = chnlSecondaryProf;
    }

    public Integer getGpForUpChannel1() {
        return gpForUpChannel1;
    }

    public void setGpForUpChannel1(Integer gpForUpChannel1) {
        this.gpForUpChannel1 = gpForUpChannel1;
    }

    public Integer getGpForUpChannel2() {
        return gpForUpChannel2;
    }

    public void setGpForUpChannel2(Integer gpForUpChannel2) {
        this.gpForUpChannel2 = gpForUpChannel2;
    }

    public Integer getGpForUpChannel3() {
        return gpForUpChannel3;
    }

    public void setGpForUpChannel3(Integer gpForUpChannel3) {
        this.gpForUpChannel3 = gpForUpChannel3;
    }

    public Integer getGpForUpChannel4() {
        return gpForUpChannel4;
    }

    public void setGpForUpChannel4(Integer gpForUpChannel4) {
        this.gpForUpChannel4 = gpForUpChannel4;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getSelectGpString() {
        return selectGpString;
    }

    public void setSelectGpString(String selectGpString) {
        this.selectGpString = selectGpString;
    }

    public String getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(String entityIds) {
        this.entityIds = entityIds;
    }

    public JSONObject getGroupTemplateJson() {
        return groupTemplateJson;
    }

    public void setGroupTemplateJson(JSONObject groupTemplateJson) {
        this.groupTemplateJson = groupTemplateJson;
    }

    public Integer getHopPeriod() {
        return hopPeriod;
    }

    public void setHopPeriod(Integer hopPeriod) {
        this.hopPeriod = hopPeriod;
    }

    public Integer getSnrThres1() {
        return snrThres1;
    }

    public void setSnrThres1(Integer snrThres1) {
        this.snrThres1 = snrThres1;
    }

    public Integer getSnrThres2() {
        return snrThres2;
    }

    public void setSnrThres2(Integer snrThres2) {
        this.snrThres2 = snrThres2;
    }

    public Integer getMaxHopLimit() {
        return maxHopLimit;
    }

    public void setMaxHopLimit(Integer maxHopLimit) {
        this.maxHopLimit = maxHopLimit;
    }

    public Integer getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(Integer adminStatus) {
        this.adminStatus = adminStatus;
    }

    public Integer getGroupPolicy() {
        return groupPolicy;
    }

    public void setGroupPolicy(Integer groupPolicy) {
        this.groupPolicy = groupPolicy;
    }

    public Integer getFecThresCorrect1() {
        return fecThresCorrect1;
    }

    public void setFecThresCorrect1(Integer fecThresCorrect1) {
        this.fecThresCorrect1 = fecThresCorrect1;
    }

    public Integer getFecThresCorrect2() {
        return fecThresCorrect2;
    }

    public void setFecThresCorrect2(Integer fecThresCorrect2) {
        this.fecThresCorrect2 = fecThresCorrect2;
    }

    public Integer getFecThresUnCorrect1() {
        return fecThresUnCorrect1;
    }

    public void setFecThresUnCorrect1(Integer fecThresUnCorrect1) {
        this.fecThresUnCorrect1 = fecThresUnCorrect1;
    }

    public Integer getFecThresUnCorrect2() {
        return fecThresUnCorrect2;
    }

    public void setFecThresUnCorrect2(Integer fecThresUnCorrect2) {
        this.fecThresUnCorrect2 = fecThresUnCorrect2;
    }

    public Integer getGroupPriority1st() {
        return groupPriority1st;
    }

    public void setGroupPriority1st(Integer groupPriority1st) {
        this.groupPriority1st = groupPriority1st;
    }

    public Integer getGroupPriority2st() {
        return groupPriority2st;
    }

    public void setGroupPriority2st(Integer groupPriority2st) {
        this.groupPriority2st = groupPriority2st;
    }

    public Integer getGroupPriority3st() {
        return groupPriority3st;
    }

    public void setGroupPriority3st(Integer groupPriority3st) {
        this.groupPriority3st = groupPriority3st;
    }

    public String getEmsGroupName() {
        return emsGroupName;
    }

    public void setEmsGroupName(String emsGroupName) {
        this.emsGroupName = emsGroupName;
    }

    public String getFreqListString() {
        return freqListString;
    }

    public void setFreqListString(String freqListString) {
        this.freqListString = freqListString;
    }

    public JSONObject getCcmtsSpectrumGpJson() {
        return ccmtsSpectrumGpJson;
    }

    public void setCcmtsSpectrumGpJson(JSONObject ccmtsSpectrumGpJson) {
        this.ccmtsSpectrumGpJson = ccmtsSpectrumGpJson;
    }

}