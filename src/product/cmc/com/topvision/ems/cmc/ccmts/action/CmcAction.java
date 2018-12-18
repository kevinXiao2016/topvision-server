package com.topvision.ems.cmc.ccmts.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcBfsxSnapInfo;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.domain.CmcCmNumStatic;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;

/**
 * @author dosion
 * @created @2013-8-6-下午4:40:27
 * 
 */
@Controller("cmcAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcAction extends BaseAction {
    private static final long serialVersionUID = 4259869100676362331L;
    private final Logger logger = LoggerFactory.getLogger(CmcAction.class);
    @Autowired
    private CmcService cmcService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name="entityService")
    private EntityService entityService;
    @Resource(name = "cmcPerfService")
    private CmcPerfService cmcPerfService;
    private Long entityId;
    private Long cmcId;
    private Long onuId;
    private Integer operationResult;
    private Long folderId;
    private Integer cmcType;
    private String source;
    /* 网管别名-for CC */
    private String nmName;
    private int renameFlag = 0;// CC列表页面
    private boolean hasSupportEpon;

    /**
     * 刷新CC设备和设备上的相关信息
     * 
     * @return String
     */
    public String refreshCC() {
        String message;
        try {
            cmcService.refreshCC(entityId, cmcId, cmcType);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 重启不带Agent的CMC TODO 旧名称为resetCmcWithoutAgent
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "cmcAction", operationName = "resetCmc${source}")
    public String resetCmc() {
        String result = null;
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        Map<String, String> message = new HashMap<String, String>();
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
        CmcAttribute cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        Entity entity = entityService.getEntity(entityId);
        if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
            source = "";
        } else {
            source = Symbol.BRACKET_LEFT + CmcIndexUtils.getSlotNo(cmcIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getPonNo(cmcIndex).toString() + Symbol.BRACKET_RIGHT;
        }

        try {
            if (CmcConstants.TOPCCMTSSYSSTATUS_OFFLINE.equals(cmcAttribute.getTopCcmtsSysStatus())) {// 关闭了的CC显示重启失败
                result = "error";
                operationResult = OperationLog.FAILURE;
            } else {
                cmcService.resetCmcWithoutAgent(cmcId);
                result = "success";
                operationResult = OperationLog.SUCCESS;
            }
        } catch (Exception e) {
            result = "error";
            operationResult = OperationLog.FAILURE;
            logger.error("", e);
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }

        return NONE;
    }

    /**
     * 在ONU视图中添加CMC并关联到某个ONU
     * 
     * @return String
     */
    public String addAndRelateCmcToOnu() {
        cmcService.relateCmcToOnu(onuId, cmcId);
        return NONE;

    }

    /**
     * 将CMC关联到某个ONU
     * 
     * @return String
     */
    public String relateCmcToOnu() {
        cmcService.relateCmcToOnu(onuId, cmcId);
        return NONE;
    }

    public String showCmcRename_B() {
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        return SUCCESS;
    }

    /**
     * 刷新设备
     * 
     * @return String
     */
    public String discoveryCmcAgain() {
        cmcService.discoveryCmcAgain(cmcId, cmcType.longValue());
        return NONE;
    }

    /**
     * 解绑定指定设备的MAC
     * 
     * @return String
     */
    @OperationLogProperty(actionName = "cmcAction", operationName = "cmcNoMacBind${source}")
    public String cmcNoMacBind() {
        String result = null;
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        Entity entity = entityService.getEntity(entityId);
        Map<String, String> message = new HashMap<String, String>();
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
        if (entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
            source = "";
        } else {
            source = Symbol.BRACKET_LEFT + CmcIndexUtils.getSlotNo(cmcIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getPonNo(cmcIndex).toString() + Symbol.BRACKET_RIGHT;
        }
        try {
            cmcService.cmcNoMacBind(cmcId);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            result = "error";
            operationResult = OperationLog.FAILURE;
            logger.debug("", e);
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    /**
     * 显示cc重命名页面
     * 
     * @return String
     */
    public String showRenameCmc() {
        if (cmcId == null) {
            renameFlag = 1;// 面板图页面
            cmcId = cmcService.getCmcIdByOnuId(onuId);
        }
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        return SUCCESS;
    }

    /**
     * cc重命名
     * 
     * @return String
     */
    public String renameCc() {
        String result = null;
        Map<String, String> message = new HashMap<String, String>();
        try {
            if (cmcId == null) {
                cmcId = cmcService.getCmcIdByOnuId(onuId);
            } else {
                // 获取onu id
                onuId = cmcService.getOnuIdByCmcId(cmcId);
            }
            entityId = cmcService.getEntityIdByCmcId(cmcId);
            cmcService.renameCc(entityId, cmcId, nmName);
            UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
            hasSupportEpon = uc.hasSupportModule("olt");
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            result = "false";
            logger.debug("", e);
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }

        return NONE;
    }

    /**
     * 添加onu到拓扑图
     * 
     * @return String
     */
    public String moveToTopoFromOnuView() {
        Map<String, Object> json = new HashMap<String, Object>();
        if (cmcId == null) {
            json.put("success", false);
            writeDataToAjax(JSONObject.fromObject(json));
            return NONE;
        } else {
            try {
                Entity entity = entityService.getEntity(cmcId);
                entity.setFolderId(folderId);
                // 添加设备的子网信息,子网的ID
                // 获取SnmpParam参数
                if (cmcService.isCmcExistsInTopo(cmcId, folderId)) {
                    json.put("success", "exists");
                    writeDataToAjax(JSONObject.fromObject(json));
                    return NONE;
                } else {
                    Long newEntityId = cmcService.moveToTopoFromOnuView(cmcId, entity);
                    json.put("entityId", newEntityId);
                    json.put("success", true);
                }
            } catch (Exception ex) {
                logger.error("", ex);
                json.put("success", false);
                writeDataToAjax(JSONObject.fromObject(json));
                return NONE;
            }
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 显示选择拓扑图页面
     * 
     * @return String
     */
    public String moveToTopoJsp() {
        return SUCCESS;
    }

    /**
     * 刷新CM信息
     * 
     * @return String
     */
    public String refreshCmInfo() {
        // TODO 需要考虑该方法位置
        CmcCmNumStatic cmcCmNumStatic = cmcPerfService.getCmcCmNumStatic(cmcId);
        if (cmcCmNumStatic == null) {
            cmcCmNumStatic = new CmcCmNumStatic();
            cmcCmNumStatic.setCmcId(cmcId);
            cmcCmNumStatic.setEntityId(entityId);
            cmcCmNumStatic.setCmNumActive(0);
            cmcCmNumStatic.setCmNumOffline(0);
            cmcCmNumStatic.setCmNumOnline(0);
            cmcCmNumStatic.setCmNumTotal(0);
        }
        writeDataToAjax(JSONObject.fromObject(cmcCmNumStatic));
        return NONE;
    }

    /**
     * 刷新类A型设备
     * @return
     */
    public String refreshCC8800A() {
        CmcAttribute cmcAttri = cmcService.getCmcAttributeByCmcId(cmcId);
        CmcBfsxSnapInfo cmcBaseInfo = cmcService.refreshCC8800A(entityId, cmcId, cmcAttri.getCmcIndex());
        JSONObject json = new JSONObject();
        if (!cmcAttri.getTopCcmtsSysObjectId().equals(cmcBaseInfo.getCmcSysObjectId())) {
            json.put("typeChange", true);
        } else {
            json.put("typeChange", false);
        }
        if (!cmcAttri.getTopCcmtsSysMacAddr().equals(cmcBaseInfo.getCmcSysMacAddr())) {
            json.put("macChange", true);
        } else {
            json.put("macChange", false);
        }
        writeDataToAjax(json);
        return NONE;
    }

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Integer getCmcType() {
        return cmcType;
    }

    public void setCmcType(Integer cmcType) {
        this.cmcType = cmcType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNmName() {
        return nmName;
    }

    public void setNmName(String nmName) {
        this.nmName = nmName;
    }

    public int getRenameFlag() {
        return renameFlag;
    }

    public void setRenameFlag(int renameFlag) {
        this.renameFlag = renameFlag;
    }

    public boolean isHasSupportEpon() {
        return hasSupportEpon;
    }

    public void setHasSupportEpon(boolean hasSupportEpon) {
        this.hasSupportEpon = hasSupportEpon;
    }

}
