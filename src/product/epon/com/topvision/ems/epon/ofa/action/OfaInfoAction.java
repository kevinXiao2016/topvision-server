package com.topvision.ems.epon.ofa.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.topvision.ems.epon.ofa.facade.domain.CommonOptiTrans;
import com.topvision.ems.epon.ofa.facade.domain.OfaAlarmThreshold;
import com.topvision.ems.epon.ofa.facade.domain.OfaBasicInfo;
import com.topvision.ems.epon.ofa.service.CommonOptiTransService;
import com.topvision.ems.epon.ofa.service.OfaAlarmThresholdService;
import com.topvision.ems.epon.ofa.service.OfaBasicInfoService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.util.CurrentRequest;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * 
 * @author w1992wishes
 * @created @2017年10月12日-上午10:47:18
 *
 */
@Controller("ofaInfoAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OfaInfoAction extends BaseAction {

    private static final long serialVersionUID = 5151790736451369690L;
    private Long entityId;
    private Integer outputAtt;
    private Entity entity;
    private OfaAlarmThreshold ofaAlarmThreshold;
    private String ofaAlarmThresholdJson;
    private OfaBasicInfo ofaBasicInfo;
    private CommonOptiTrans commonOptiTrans;
    private String ofaBasicInfoJson;
    private String commonOptiTransJson;
    private String tempUnit;
    @Autowired
    private OfaBasicInfoService ofaBasicInfoService;
    @Autowired
    private CommonOptiTransService commonOptiTransService;
    @Autowired
    private OfaAlarmThresholdService ofaAlarmThresholdService;
    @Autowired
    private EntityService entityService;

    /**
     * 展示ofa信息页面
     * 
     * @return
     */
    public String showOfaInfo() {
        // 温度单位
        tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
        entity = entityService.getEntity(entityId);
        ofaAlarmThreshold = ofaAlarmThresholdService.getOfaAlarmThresholdById(entityId);
        if (ofaAlarmThreshold != null) {
            ofaAlarmThresholdJson = JSON.toJSONString(ofaAlarmThreshold);
        }
        ofaBasicInfo = ofaBasicInfoService.getOfaBasicInfo(entityId);
        if (ofaBasicInfo != null) {
            ofaBasicInfoJson = JSON.toJSONString(ofaBasicInfo);
        }
        commonOptiTrans = commonOptiTransService.getCommonOptiTrans(entityId);
        if (commonOptiTrans != null) {
            UserContext uc = CurrentRequest.getCurrentUser();
            if (commonOptiTrans.getDeviceAcct() != null) {
                commonOptiTrans.setDeviceAcctStr(DateUtils.getTimePeriod(commonOptiTrans.getDeviceAcct() * 1000, uc
                        .getUser().getLanguage()));
            }
            // mac地址格式化
            try {
                String macRule = uc.getMacDisplayStyle();
                commonOptiTrans.setMacAddress(MacUtils.convertMacToDisplayFormat(commonOptiTrans.getMacAddress(),
                        macRule));
                logger.info("commonOptiTrans setMacAddress success");
            } catch (Exception e) {
                logger.error("commonOptiTrans setMacAddress wrong", e);
            }
            commonOptiTransJson = JSON.toJSONString(commonOptiTrans);
        }
        return SUCCESS;
    }

    /**
     * Ofa告警阈值设置
     * 
     * @return
     */
    public String modifyOfaAlarmThreshold() {
        String msg = "success";
        OfaAlarmThreshold newOfaAlarmThreshold = null;
        JSONObject json = new JSONObject();
        try {
            newOfaAlarmThreshold = ofaAlarmThresholdService.modifyOfaAlarmThreshold(ofaAlarmThreshold);
            json.put("data", newOfaAlarmThreshold);
        } catch (Exception e) {
            msg = "error";
            logger.error("modify OfaAlarmThreshold failure", e);
        }
        json.put("msg", msg);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 修改基本信息
     * 
     * @return
     */
    public String modifyOfaBasicInfo() {
        OfaBasicInfo ofaBasicInfo = new OfaBasicInfo();
        ofaBasicInfo.setOutputAtt(outputAtt);
        ofaBasicInfo.setEntityId(entityId);
        ofaBasicInfoService.modifyOfaBasicInfo(ofaBasicInfo, ofaBasicInfo.getEntityId());
        return NONE;
    }

    /**
     * 刷新Ofa告警阈值配置
     * 
     * @return
     */
    public String refreshOfaAlarmThreshold() {
        String msg = "success";
        OfaAlarmThreshold ofaAlarmThreshold = null;
        JSONObject json = new JSONObject();
        try {
            ofaAlarmThreshold = ofaAlarmThresholdService.fetchOfaAlarmThreshold(entityId).get(0);
            json.put("data", ofaAlarmThreshold);
        } catch (Exception e) {
            msg = "error";
            logger.error("refresh OfaAlarmThreshold failure", e);
        }
        json.put("msg", msg);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 刷新基本信息
     * 
     * @return
     */
    public String refreshOfaBasicInfo() {
        ofaBasicInfoService.refreshOfaBasicInfo(entityId);
        commonOptiTransService.refreshCommonOptiTrans(entityId);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public OfaAlarmThreshold getOfaAlarmThreshold() {
        return ofaAlarmThreshold;
    }

    public void setOfaAlarmThreshold(OfaAlarmThreshold ofaAlarmThreshold) {
        this.ofaAlarmThreshold = ofaAlarmThreshold;
    }

    public String getOfaAlarmThresholdJson() {
        return ofaAlarmThresholdJson;
    }

    public void setOfaAlarmThresholdJson(String ofaAlarmThresholdJson) {
        this.ofaAlarmThresholdJson = ofaAlarmThresholdJson;
    }

    public OfaBasicInfo getOfaBasicInfo() {
        return ofaBasicInfo;
    }

    public void setOfaBasicInfo(OfaBasicInfo ofaBasicInfo) {
        this.ofaBasicInfo = ofaBasicInfo;
    }

    public CommonOptiTrans getCommonOptiTransDeviceEntry() {
        return commonOptiTrans;
    }

    public void setCommonOptiTransDeviceEntry(CommonOptiTrans commonOptiTrans) {
        this.commonOptiTrans = commonOptiTrans;
    }

    public String getOfaBasicInfoJson() {
        return ofaBasicInfoJson;
    }

    public void setOfaBasicInfoJson(String ofaBasicInfoJson) {
        this.ofaBasicInfoJson = ofaBasicInfoJson;
    }

    public String getCommonOptiTransJson() {
        return commonOptiTransJson;
    }

    public void setCommonOptiTransJson(String commonOptiTransJson) {
        this.commonOptiTransJson = commonOptiTransJson;
    }

    public String getTempUnit() {
        return tempUnit;
    }

    public void setTempUnit(String tempUnit) {
        this.tempUnit = tempUnit;
    }

    public Integer getOutputAtt() {
        return outputAtt;
    }

    public void setOutputAtt(Integer outputAtt) {
        this.outputAtt = outputAtt;
    }

}
