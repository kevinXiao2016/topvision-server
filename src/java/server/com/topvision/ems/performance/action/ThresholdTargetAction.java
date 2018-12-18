/***********************************************************************
 * $Id: ThresholdTargetAction.java,v1.0 2015-1-16 上午9:34:01 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.performance.domain.PerfTarget;
import com.topvision.ems.performance.service.PerfThresholdService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author flack
 * @created @2015-1-16-上午9:34:01
 * 管理阈值指标
 */
@Controller("thresholdTargetAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ThresholdTargetAction extends BaseAction {
    private static final long serialVersionUID = -8953566304118893752L;

    private String targetId;
    private Boolean enable;
    private BigDecimal maxValue;
    private BigDecimal minValue;
    private PerfTarget perfTarget;

    @Autowired
    protected PerfThresholdService perfThresholdService;

    /**
     * 显示阈值指标管理页面
     * @return
     */
    public String showTargetManage() {
        return SUCCESS;
    }

    /**
     * 加载阈值指标
     * @return
     * @throws IOException 
     */
    public String loadTargetList() throws IOException {
        List<PerfTarget> targetList = perfThresholdService.getAllPerfTargets();
        //转换温度阈值
        String tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
        if (tempUnit != null && UnitConfigConstant.FAHR_TEMP_UNIT.equals(tempUnit)) {
            for (PerfTarget target : targetList) {
                if (UnitConfigConstant.TEMPRELATED_THRESHOLDS.contains(target.getTargetId())) {
                    target.setUnit(tempUnit);
                    target.setMaxNum(new BigDecimal(UnitConfigConstant.translateTemperature(target.getMaxNum()
                            .doubleValue())));
                    target.setMinNum(new BigDecimal(UnitConfigConstant.translateTemperature(target.getMinNum()
                            .doubleValue())));
                }
            }
        }
        //CATV ONU 的输出电平
        String powerUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
        if (powerUnit != null && UnitConfigConstant.MILLI_VOLT_UNIT.equals(powerUnit)) {
            for (PerfTarget target : targetList) {
                if (UnitConfigConstant.ONU_CATV_RF.equals(target.getTargetId())) {
                    target.setUnit(powerUnit);
                    target.setMaxNum(new BigDecimal(UnitConfigConstant
                            .transDBμVToDBmV(target.getMaxNum().doubleValue())));
                    target.setMinNum(new BigDecimal(UnitConfigConstant
                            .transDBμVToDBmV(target.getMinNum().doubleValue())));
                }
            }
        }
        writeDataToAjax(JSONArray.fromObject(targetList));
        return NONE;
    }

    /**
     * 显示指标编辑页面
     * @return
     */
    public String showTargetEdit() {
        perfTarget = perfThresholdService.getPerfTarget(targetId);
        String tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
        if (tempUnit != null && UnitConfigConstant.FAHR_TEMP_UNIT.equals(tempUnit)) {
            if (UnitConfigConstant.TEMPRELATED_THRESHOLDS.contains(perfTarget.getTargetId())) {
                perfTarget.setUnit(tempUnit);
                perfTarget.setMaxNum(new BigDecimal(UnitConfigConstant.translateTemperature(perfTarget.getMaxNum()
                        .doubleValue())));
                perfTarget.setMinNum(new BigDecimal(UnitConfigConstant.translateTemperature(perfTarget.getMinNum()
                        .doubleValue())));
            }
        }
        if (UnitConfigConstant.ONU_CATV_RF.equals(perfTarget.getTargetId())) {
            //CATV ONU 的输出电平
            String powerUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
            if (powerUnit != null && UnitConfigConstant.MILLI_VOLT_UNIT.equals(powerUnit)) {
                perfTarget.setUnit(powerUnit);
                perfTarget.setMaxNum(new BigDecimal(UnitConfigConstant.transDBμVToDBmV(perfTarget.getMaxNum()
                        .doubleValue())));
                perfTarget.setMinNum(new BigDecimal(UnitConfigConstant.transDBμVToDBmV(perfTarget.getMinNum()
                        .doubleValue())));
            }
        }
        return SUCCESS;
    }

    /**
     * 修改指标
     * @return
     */
    public String modifyTarget() {
        PerfTarget target = new PerfTarget();
        target.setTargetId(targetId);
        perfTarget = perfThresholdService.getPerfTarget(targetId);
        String tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
        if (tempUnit != null && UnitConfigConstant.FAHR_TEMP_UNIT.equals(tempUnit)) {
            if (UnitConfigConstant.TEMPRELATED_THRESHOLDS.contains(targetId)) {
                maxValue = new BigDecimal(UnitConfigConstant.translateValueToCenti(maxValue.doubleValue()));
                minValue = new BigDecimal(UnitConfigConstant.translateValueToCenti(minValue.doubleValue()));
            }
        }
        if (UnitConfigConstant.ONU_CATV_RF.equals(target.getTargetId())) {
            //CATV ONU 的输出电平
            String powerUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
            if (powerUnit != null && UnitConfigConstant.MILLI_VOLT_UNIT.equals(powerUnit)) {
                maxValue = new BigDecimal(UnitConfigConstant.transDBmVToDBμV(perfTarget.getMaxNum().doubleValue()));
                minValue = new BigDecimal(UnitConfigConstant.transDBmVToDBμV(perfTarget.getMinNum().doubleValue()));
            }
        }
        target.setMaxNum(maxValue);
        target.setMinNum(minValue);
        perfThresholdService.updateTargetValue(target);
        return NONE;
    }

    /**
     * 启用或者停用阈值指标
     * @return
     * @throws IOException 
     */
    public String enableTarget() throws IOException {
        PerfTarget target = new PerfTarget();
        target.setTargetId(targetId);
        target.setEnableStatus(enable);
        int count = perfThresholdService.getTargetReRuleCount(targetId);
        JSONObject json = new JSONObject();
        if (count > 0) {//存在包含该指标的模板
            json.put("success", false);
        } else {//没有包含该指标的模板
            perfThresholdService.updateTargetStatus(target);
            json.put("success", true);
        }
        json.write(response.getWriter());
        return NONE;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public PerfTarget getPerfTarget() {
        return perfTarget;
    }

    public void setPerfTarget(PerfTarget perfTarget) {
        this.perfTarget = perfTarget;
    }
}
