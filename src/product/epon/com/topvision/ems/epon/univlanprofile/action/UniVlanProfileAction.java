/***********************************************************************
 * $Id: UniVlanProfileAction.java,v1.0 2013-11-28 上午10:08:50 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.univlanprofile.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.batchdeploy.domain.Type;
import com.topvision.ems.epon.batchdeploy.domain.Target;
import com.topvision.ems.epon.batchdeploy.service.EponBatchDeployService;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanProfileTable;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanRuleTable;
import com.topvision.ems.epon.univlanprofile.service.UniVlanProfileService;
import com.topvision.ems.epon.vlan.service.UniVlanService;
import com.topvision.ems.facade.batchdeploy.domain.Result;
import com.topvision.ems.facade.batchdeploy.domain.ResultBundle;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;

/**
 * @author flack
 * @created @2013-11-28-上午10:08:50
 *
 */
@Controller("uniVlanProfileAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UniVlanProfileAction extends BaseAction {
    private static final long serialVersionUID = 4261885679305802111L;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Long entityId;
    private Integer profileIndex;
    private String profileName;
    private Integer profileMode;
    //在模板修改时标记Mode是否修改
    private Boolean modeChange;
    private Integer ruleIndex;
    private Integer ruleMode;
    private String ruleCvlan;
    private Integer ruleSvlan;
    //需要绑定的端口表达式
    private String needBinds;
    //需要排除绑定的端口表达式
    private String excudeBinds;
    //模板引用次数
    private int profileRefCnt;
    private Long onuId;
    //ONU UNI id
    private Long uniId;
    private Long uniIndex;
    private Integer uniPvid;
    private Integer bindProfAttr;
    private UniVlanBindTable uniBindInfo;
    private UniVlanProfileTable uniVlanProfile;

    @Autowired
    private UniVlanProfileService uniVlanProfileService;
    @Autowired
    private EponBatchDeployService eponBatchDeployService;
    @Autowired
    private UniVlanService uniVlanService;

    /**
     * 显示UNI VLAN模板配置页面
     * @return
     */
    public String showUniVlanProfile() {
        return SUCCESS;
    }

    /**
     * 加载UNI　VLAN模板列表
     * @return
     * @throws IOException 
     */
    public String loadUniVlanProfileList() throws IOException {
        List<UniVlanProfileTable> profileList = uniVlanProfileService.getProfileList(entityId);
        JSONArray array = JSONArray.fromObject(profileList);
        array.write(response.getWriter());
        return NONE;
    }

    /**
     * 添加模板
     * @return
     */
    public String addUniVlanProfile() {
        UniVlanProfileTable profile = new UniVlanProfileTable();
        profile.setEntityId(entityId);
        profile.setProfileId(profileIndex);
        profile.setProfileName(profileName);
        profile.setProfileMode(profileMode);
        uniVlanProfileService.addProfile(profile);
        return NONE;
    }

    /**
     * 更新模板
     * @return
     */
    public String modifyProfile() {
        UniVlanProfileTable profile = new UniVlanProfileTable();
        profile.setEntityId(entityId);
        profile.setProfileId(profileIndex);
        profile.setProfileName(profileName);
        profile.setProfileMode(profileMode);
        uniVlanProfileService.updateProfile(profile, modeChange);
        return NONE;
    }

    /**
     * 删除模板
     * @return
     */
    public String deleteProfile() {
        UniVlanProfileTable profile = new UniVlanProfileTable();
        profile.setEntityId(entityId);
        profile.setProfileId(profileIndex);
        uniVlanProfileService.deleteProfile(profile);
        return NONE;
    }

    /**
     * 从设备刷新模板数据
     * @return
     */
    public String refreshProfileData() {
        uniVlanProfileService.refreshProfileAndRule(entityId);
        uniVlanProfileService.refreshUniVlanData(entityId);
        return NONE;
    }

    /**
     * 显示模板规则配置页面
     * @return
     */
    public String showProfileRule() {
        return SUCCESS;
    }

    /**
     * 加载模板对应的规则列表
     * @return
     * @throws IOException 
     */
    public String loadProfileRuleList() throws IOException {
        List<UniVlanRuleTable> ruleList = uniVlanProfileService.getRuleList(entityId, profileIndex);
        JSONArray ruleArray = JSONArray.fromObject(ruleList);
        ruleArray.write(response.getWriter());
        return NONE;
    }

    /**
     * 添加模板规则
     * @return
     */
    public String addProfileRule() {
        UniVlanRuleTable rule = new UniVlanRuleTable();
        rule.setEntityId(entityId);
        rule.setRuleProfileIndex(profileIndex);
        rule.setRuleIndex(ruleIndex);
        rule.setRuleMode(ruleMode);
        //需要将cVlan转为MIB匹配的格式
        String[] vlanData = ruleCvlan.split(",");
        List<Integer> list = new ArrayList<Integer>();
        for (String vlan : vlanData) {
            list.add(Integer.parseInt(vlan));
        }
        rule.setcVlanList(list);
        rule.setRuleSvlan(ruleSvlan);
        uniVlanProfileService.addRule(rule);
        return NONE;
    }

    /**
     * 删除规则
     * @return
     */
    public String deleteProfileRule() {
        UniVlanRuleTable rule = new UniVlanRuleTable();
        rule.setEntityId(entityId);
        rule.setRuleProfileIndex(profileIndex);
        rule.setRuleIndex(ruleIndex);
        uniVlanProfileService.deleteRule(rule);
        return NONE;
    }

    /**
     * 从设备刷新规则数据
     * @return
     */
    public String refershRuleData() {
        uniVlanProfileService.refreshRuleData(entityId);
        return NONE;
    }

    /**
     * 显示聚合规则添加页面
     * @return
     */
    public String showAggRule() {
        return SUCCESS;
    }

    /**
     * 显示模板绑定页面
     * @return
     */
    public String showProfileBind() {
        return SUCCESS;
    }

    /**
     * 保存模板绑定配置并提交执行绑定动作
     * @return
     * @throws IOException 
     */
    @SuppressWarnings("unchecked")
    public String saveProfileBind() throws IOException {
        UniVlanProfileTable profile = new UniVlanProfileTable();
        profile.setEntityId(entityId);
        profile.setProfileId(profileIndex);
        profile.setPvid(uniPvid);
        List<String> needList = new ArrayList<String>();
        if (needBinds != null && !"".equals(needBinds)) {
            needList = Arrays.asList(needBinds.split("_"));
        }
        List<String> excludeList = new ArrayList<String>();
        if (excudeBinds != null && !"".equals(excudeBinds)) {
            excludeList = Arrays.asList(excudeBinds.split("_"));
        }
        //调用接口提交绑定
        ResultBundle<Target> bindResult = eponBatchDeployService.batchDeploy(needList, excludeList, entityId, profile,
                "uniVlanProfileBindExecutor", Type.UNIVLAN_PROFILE_BIND,
                getString("RECORD.bindProfile", "univlanprofile") + profileIndex + "  PVID:" + uniPvid);
        //获得绑定结果
        List<UniVlanBindTable> bindList = new ArrayList<UniVlanBindTable>();
        UniVlanBindTable bindTable = null;
        JSONObject resultJson = new JSONObject();
        if (bindResult.getData().isEmpty()) {
            resultJson.put("successCount", 0);
            resultJson.put("failedCount", 0);
        } else {
            for (Result<Target> result : bindResult.getData()) {
                resultJson.put("successCount", result.getSuccessList().size());
                resultJson.put("failedCount", result.getFailureList().size());
                for (Target bindTarget : result.getSuccessList()) {
                    bindTable = new UniVlanBindTable();
                    bindTable.setEntityId(entityId);
                    bindTable.setUniIndex(bindTarget.getTargetIndex());
                    bindTable.setBindPvid(uniPvid);
                    bindTable.setBindProfileId(profileIndex);
                    bindList.add(bindTable);
                }
            }
        }
        //保存绑定结果到数据库
        if (!bindList.isEmpty()) {
            uniVlanProfileService.updateProfileBind(bindList, profile);
            //批量解绑定成功后刷新UNI VLAN模板相关数据,主要是同步模板引用次数
            uniVlanProfileService.refreshProfileAndRule(entityId);
            //同步端口VLAN配置信息
            uniVlanService.refreshUniPortVlanAttribute(entityId);
        }
        resultJson.write(response.getWriter());
        return NONE;
    }

    /**
     * 解除特定模板的UNI端口绑定
     * @return
     * @throws IOException 
     */
    public String unBindProfile() throws IOException {
        //根据模板id去模板绑定表查找模板所绑定的所有uni端口列表
        List<Long> uniIndexList = uniVlanProfileService.getProfileBindList(profileIndex, entityId);
        List<Target> targetList = new ArrayList<Target>();
        Target target = null;
        for (Long uniIndex : uniIndexList) {
            target = new Target();
            target.setTargetIndex(uniIndex);
            targetList.add(target);
        }
        UniVlanProfileTable profile = new UniVlanProfileTable();
        profile.setEntityId(entityId);
        profile.setProfileId(EponConstants.UNIVALN_UNBIND_PROFILEID);
        //调用解绑定的接口进行解绑定
        /* ResultBundle<Target> bindResult = batchDeployService.batchDeploy(targetList, entityId, profile,
                 "uniVlanProfileBindExecutor");*/
        ResultBundle<Target> bindResult = eponBatchDeployService.batchDeploy(targetList, entityId, profile,
                "uniVlanProfileBindExecutor", Type.UNIVLAN_PROFILE_UNBIND,
                getString("RECORD.unBindProfile", "univlanprofile") + profileIndex);
        //获得绑定结果
        List<UniVlanBindTable> unbindList = new ArrayList<UniVlanBindTable>();
        UniVlanBindTable bindTable = null;
        JSONObject resultJson = new JSONObject();
        if (bindResult.getData().isEmpty()) {
            resultJson.put("successCount", 0);
            resultJson.put("failedCount", 0);
        } else {
            for (Result<Target> result : bindResult.getData()) {
                resultJson.put("successCount", result.getSuccessList().size());
                resultJson.put("failedCount", result.getFailureList().size());
                for (Target bindTarget : result.getSuccessList()) {
                    bindTable = new UniVlanBindTable();
                    bindTable.setEntityId(entityId);
                    bindTable.setUniIndex(bindTarget.getTargetIndex());
                    //bindTable.setBindPvid(1);
                    bindTable.setBindProfileId(EponConstants.UNIVALN_UNBIND_PROFILEID);
                    unbindList.add(bindTable);
                }
            }
        }
        //保存解绑定绑定结果到数据库
        if (!unbindList.isEmpty()) {
            uniVlanProfileService.updateUnBind(unbindList);
            //批量解绑定成功后刷新UNI VLAN模板相关数据,主要是同步模板引用次数
            uniVlanProfileService.refreshProfileAndRule(entityId);
            //同步端口VLAN配置信息
            uniVlanService.refreshUniPortVlanAttribute(entityId);
        }
        resultJson.write(response.getWriter());
        return NONE;
    }

    /**
     * 显示批量解除端口绑定的UNI VLAN模板页面
     * @return
     */
    public String showBatchUnBind() {
        return SUCCESS;
    }

    /**
     * 批量解除端口的UNI VLAN 模板绑定
     * @return
     * @throws IOException 
     */
    @SuppressWarnings("unchecked")
    public String batchUnBindProfile() throws IOException {
        JSONObject resultJson = new JSONObject();
        //判断该设备下是否有需要解绑定的记录
        List<UniVlanBindTable> bindList = uniVlanProfileService.getEntityBindList(entityId);
        if (bindList.isEmpty()) {
            resultJson.put("successCount", 0);
            resultJson.put("failedCount", 0);
        } else {
            UniVlanProfileTable profile = new UniVlanProfileTable();
            profile.setEntityId(entityId);
            profile.setProfileId(EponConstants.UNIVALN_UNBIND_PROFILEID);
            //待解除绑定表达式列表
            List<String> needList = new ArrayList<String>();
            if (needBinds != null && !"".equals(needBinds)) {
                needList = Arrays.asList(needBinds.split("_"));
            }
            //调用接口提交解除绑定
            ResultBundle<Target> unbindResult = eponBatchDeployService.batchDeploy(needList, null, entityId, profile,
                    "uniVlanProfileBindExecutor", Type.UNIVLAN_PROFILE_UNBIND,
                    getString("RECORD.batchUnbind", "univlanprofile"));
            //获得解除绑定结果
            List<UniVlanBindTable> unbindList = new ArrayList<UniVlanBindTable>();
            UniVlanBindTable bindTable = null;

            if (unbindResult.getData().isEmpty()) {
                resultJson.put("successCount", 0);
                resultJson.put("failedCount", 0);
            } else {
                for (Result<Target> result : unbindResult.getData()) {
                    resultJson.put("successCount", result.getSuccessList().size());
                    resultJson.put("failedCount", result.getFailureList().size());
                    for (Target bindTarget : result.getSuccessList()) {
                        bindTable = new UniVlanBindTable();
                        bindTable.setEntityId(entityId);
                        bindTable.setUniIndex(bindTarget.getTargetIndex());
                        //bindTable.setBindPvid(1);
                        bindTable.setBindProfileId(EponConstants.UNIVALN_UNBIND_PROFILEID);
                        unbindList.add(bindTable);
                    }
                }
            }
            //保存解绑定绑定结果到数据库
            if (!unbindList.isEmpty()) {
                uniVlanProfileService.updateUnBind(unbindList);
                //批量解绑定成功后刷新UNI VLAN模板相关数据,主要是同步模板引用次数
                uniVlanProfileService.refreshProfileAndRule(entityId);
                //同步端口VLAN配置信息
                uniVlanService.refreshUniPortVlanAttribute(entityId);
            }
        }
        resultJson.write(response.getWriter());
        return NONE;
    }

    /**
     * 显示UNI端口的VLAN模板信息
     * @return
     */
    public String showUniBindInfo() {
        uniBindInfo = uniVlanProfileService.getUniBindInfo(uniId, entityId);
        if (uniBindInfo != null && uniBindInfo.getBindProfileId() > EponConstants.UNIVALN_UNBIND_PROFILEID) {
            uniVlanProfile = uniVlanProfileService.getUniVlanProfile(entityId, uniBindInfo.getBindProfileId());
        }
        return SUCCESS;
    }

    /**
     * 更改uni端口的Pvid信息
     * @return
     */
    public String modifyUniPvid() {
        UniVlanBindTable uniBindInfo = new UniVlanBindTable();
        uniBindInfo.setEntityId(entityId);
        uniBindInfo.setUniIndex(uniIndex);
        uniBindInfo.setUniId(uniId);
        uniBindInfo.setBindPvid(uniPvid);
        uniVlanProfileService.updateUniPvid(uniBindInfo);
        return NONE;
    }

    /**
     * 刷新UNI口的绑定信息
     * @return
     */
    public String refreshBindInfo() {
        uniVlanProfileService.refreshUniVlanInfo(entityId, uniIndex);
        return NONE;
    }

    private String getString(String key, String module) {
        module = String.format("com.topvision.ems.epon.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * 查看关联模板
     * @return
     */
    public String showRelateProfile() {
        uniBindInfo = uniVlanProfileService.getUniVlanBindInfo(entityId, uniIndex);
        return SUCCESS;
    }

    /**
     * 替换uni口的模板绑定
     * @return
     */
    public String replaceBindProfile() {
        UniVlanBindTable uniBindInfo = new UniVlanBindTable();
        uniBindInfo.setEntityId(entityId);
        uniBindInfo.setUniIndex(uniIndex);
        uniBindInfo.setBindProfileId(profileIndex);
        uniBindInfo.setBindProfAttr(bindProfAttr);
        uniVlanProfileService.replaceBindProfile(uniBindInfo);
        //修改绑定成功后刷新UNI绑定信息
        uniVlanProfileService.refreshUniVlanInfo(entityId, uniIndex);
        try {
            //TODO 同步UNI端口数据
            uniVlanService.refreshSingleUniVlanAttribute(entityId, uniId);
        } catch (Exception e) {
            logger.error("Sync Uni[{}] Vlan Data failed:{}", uniId, e);
        }
        return NONE;
    }

    /**
     * 解除UNI口模板的绑定
     * @return
     */
    public String unBindUniProfile() {
        UniVlanBindTable bindTable = new UniVlanBindTable();
        bindTable.setEntityId(entityId);
        bindTable.setUniIndex(uniIndex);
        bindTable.setBindProfileId(EponConstants.UNIVALN_UNBIND_PROFILEID);
        uniVlanProfileService.unBindUniProfile(bindTable);
        //修改绑定成功后刷新UNI绑定信息
        uniVlanProfileService.refreshUniVlanInfo(entityId, uniIndex);
        try {
            //TODO 同步UNI端口数据
            uniVlanService.refreshSingleUniVlanAttribute(entityId, uniId);
        } catch (Exception e) {
            logger.error("Sync Uni[{}] Vlan Data failed:{}", uniId, e);
        }
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getProfileIndex() {
        return profileIndex;
    }

    public void setProfileIndex(Integer profileIndex) {
        this.profileIndex = profileIndex;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Integer getProfileMode() {
        return profileMode;
    }

    public void setProfileMode(Integer profileMode) {
        this.profileMode = profileMode;
    }

    public Boolean getModeChange() {
        return modeChange;
    }

    public void setModeChange(Boolean modeChange) {
        this.modeChange = modeChange;
    }

    public Integer getRuleIndex() {
        return ruleIndex;
    }

    public void setRuleIndex(Integer ruleIndex) {
        this.ruleIndex = ruleIndex;
    }

    public Integer getRuleMode() {
        return ruleMode;
    }

    public void setRuleMode(Integer ruleMode) {
        this.ruleMode = ruleMode;
    }

    public String getRuleCvlan() {
        return ruleCvlan;
    }

    public void setRuleCvlan(String ruleCvlan) {
        this.ruleCvlan = ruleCvlan;
    }

    public Integer getRuleSvlan() {
        return ruleSvlan;
    }

    public void setRuleSvlan(Integer ruleSvlan) {
        this.ruleSvlan = ruleSvlan;
    }

    public String getNeedBinds() {
        return needBinds;
    }

    public void setNeedBinds(String needBinds) {
        this.needBinds = needBinds;
    }

    public String getExcudeBinds() {
        return excudeBinds;
    }

    public void setExcudeBinds(String excudeBinds) {
        this.excudeBinds = excudeBinds;
    }

    public int getProfileRefCnt() {
        return profileRefCnt;
    }

    public void setProfileRefCnt(int profileRefCnt) {
        this.profileRefCnt = profileRefCnt;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public Integer getUniPvid() {
        return uniPvid;
    }

    public void setUniPvid(Integer uniPvid) {
        this.uniPvid = uniPvid;
    }

    public UniVlanBindTable getUniBindInfo() {
        return uniBindInfo;
    }

    public void setUniBindInfo(UniVlanBindTable uniBindInfo) {
        this.uniBindInfo = uniBindInfo;
    }

    public UniVlanProfileTable getUniVlanProfile() {
        return uniVlanProfile;
    }

    public void setUniVlanProfile(UniVlanProfileTable uniVlanProfile) {
        this.uniVlanProfile = uniVlanProfile;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Integer getBindProfAttr() {
        return bindProfAttr;
    }

    public void setBindProfAttr(Integer bindProfAttr) {
        this.bindProfAttr = bindProfAttr;
    }

}
