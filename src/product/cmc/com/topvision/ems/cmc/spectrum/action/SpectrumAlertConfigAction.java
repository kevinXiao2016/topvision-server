/***********************************************************************
 * $Id: SpectrumAlertConfigAction.java,v1.0 2015年3月12日 上午10:34:12 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.spectrum.service.SpectrumAlertService;
import com.topvision.ems.cmc.spectrum.utils.SpectrumTestUtil;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author YangYi
 * @created @2015年3月12日-上午10:34:12
 * 
 */
@Controller("spectrumAlertConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SpectrumAlertConfigAction extends BaseAction {
    private static final long serialVersionUID = -2579291968033450977L;
    private final Logger logger = LoggerFactory.getLogger(SpectrumAlertConfigAction.class);
    @Resource(name = "spectrumAlertService")
    private SpectrumAlertService spectrumAlertService;

    private Integer overThresholdTimes;// 累计超过阈值次数Y，默认为4
    private Integer notOverThresholdTimes; // 累计不超过阈值次数Z，默认为4
    private Integer overThresholdPercent;// 超过阈值N%个点的N，默认为20%
    private Integer notOverThresholdPercent;// 未超过阈值M%个点的N，默认为10%

    private Double freq;
    private Double power;

    /**
     * 显示频谱告警配置页面
     * 
     * @return
     */
    public String showSpectrumAlertConfig() {
        overThresholdTimes = spectrumAlertService.getOverThresholdTimes();// 累计超过阈值次数Y，默认为4
        notOverThresholdTimes = spectrumAlertService.getNotOverThresholdTimes();// 累计不超过阈值次数Z，默认为4
        overThresholdPercent = spectrumAlertService.getOverThresholdPercent();// 超过阈值N%个点的N，默认为20%
        notOverThresholdPercent = spectrumAlertService.getNotOverThresholdPercent();// 未超过阈值M%个点的N，默认为10%
        return SUCCESS;
    }

    /**
     * 读取频谱告警配置
     * 
     * @return
     */
    public String loadSpectrumAlertConfig() {
        Map<String, Object> json = new HashMap<String, Object>();
        overThresholdTimes = spectrumAlertService.getOverThresholdTimes();// 累计超过阈值次数Y，默认为4
        notOverThresholdTimes = spectrumAlertService.getNotOverThresholdTimes();// 累计不超过阈值次数Z，默认为4
        overThresholdPercent = spectrumAlertService.getOverThresholdPercent();// 超过阈值N%个点的N，默认为20%
        notOverThresholdPercent = spectrumAlertService.getNotOverThresholdPercent();// 未超过阈值M%个点的N，默认为10%
        json.put("overThresholdTimes", overThresholdTimes);
        json.put("notOverThresholdTimes", notOverThresholdTimes);
        json.put("overThresholdPercent", overThresholdPercent);
        json.put("notOverThresholdPercent", notOverThresholdPercent);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 修改频谱告警配置
     * 
     * @return
     */
    public String modifySpectrumAlertConfig() {
        String message;
        try {
            spectrumAlertService.modifySpectrumAlertConfig(overThresholdTimes, notOverThresholdTimes,
                    overThresholdPercent, notOverThresholdPercent);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.error("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 显示频谱测试页面
     * 
     * @return
     */
    public String showTestSpectrumAlert() {
        return SUCCESS;
    }

    /**
     * 读取测试频点
     * 
     * @return
     */
    public String loadSpecialPoints() {
        HashMap<Double, Double> pointsMap = SpectrumTestUtil.getSpeactionPoints();
        JSONArray data = new JSONArray();
        Set<Double> keySet = pointsMap.keySet();
        for (Double freq : keySet) {
            JSONObject json = new JSONObject();
            json.put("freq", freq);
            json.put("power", pointsMap.get(freq) - 60);
            data.add(json);
        }
        JSONObject re = new JSONObject();
        re.put("data", data);
        re.put("rowCount", data.size());
        writeDataToAjax(re);
        return NONE;
    }

    /**
     * 添加特殊频点
     * 
     * @return
     */
    public String addTestFreq() {
        SpectrumTestUtil.addSpeactionPoints(freq, power);
        String message = "success";
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 删除特殊频点
     * 
     * @return
     */
    public String deleteTestFreq() {
        SpectrumTestUtil.deleteTestPoints(freq);
        String message = "success";
        writeDataToAjax(message);
        return NONE;
    }

    public Integer getOverThresholdTimes() {
        return overThresholdTimes;
    }

    public void setOverThresholdTimes(Integer overThresholdTimes) {
        this.overThresholdTimes = overThresholdTimes;
    }

    public Integer getOverThresholdPercent() {
        return overThresholdPercent;
    }

    public void setOverThresholdPercent(Integer overThresholdPercent) {
        this.overThresholdPercent = overThresholdPercent;
    }

    public Integer getNotOverThresholdPercent() {
        return notOverThresholdPercent;
    }

    public void setNotOverThresholdPercent(Integer notOverThresholdPercent) {
        this.notOverThresholdPercent = notOverThresholdPercent;
    }

    public Integer getNotOverThresholdTimes() {
        return notOverThresholdTimes;
    }

    public void setNotOverThresholdTimes(Integer notOverThresholdTimes) {
        this.notOverThresholdTimes = notOverThresholdTimes;
    }

    public Double getFreq() {
        return freq;
    }

    public void setFreq(Double freq) {
        this.freq = freq;
    }

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
    }

}
