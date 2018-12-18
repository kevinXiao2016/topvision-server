/***********************************************************************
 * $Id: CmcPerfAction.java,v1.0 2012-5-6 下午01:45:41 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.flap.action;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.flap.service.CmcFlapService;
import com.topvision.ems.cmc.performance.domain.CMFlapHis;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author dosion
 * @created @2012-5-6-下午01:45:41
 * 
 */
@Controller("cmcFlapAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcFlapAction extends BaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 6575467055183260940L;

    private final Logger logger = LoggerFactory.getLogger(CmcFlapAction.class);

    private Integer topCmFlapInterval;
    @Resource(name = "cmcFlapService")
    private CmcFlapService cmcFlapService;
    private Long entityId;
    private Long cmcId;
    private String cmMac;

    /**
     * 显示DOL Flap配置页面
     * 
     * @return String
     */
    public String showDolflapConf() {
        //
        topCmFlapInterval = cmcFlapService.getTopCmFlapInterval(cmcId);
        // 采集器采集周期，单位：s
        // flapMonitorInterval = cmcFlapService.queryCmcFlapMonitorInterval(cmcId);
        // flapMonitorInterval = flapMonitorInterval/1000;
        return SUCCESS;
    }

    /**
     * 修改FLAP时间间隔
     * 
     * @return String
     */
    public String cmFlapConfig() {
        String message;
        try {
            cmcFlapService.cmFlapConfig(cmcId, topCmFlapInterval);
            message = "success";
        } catch (Exception e) {
            message = e.getMessage();
            logger.debug("", e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    public String resetFlapCounters() {
        String message = "success";
        try {
            cmcFlapService.resetFlapCounters(cmcId);

        } catch (Exception e) {

            message = e.getMessage();
            logger.error("error:" + e);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 只负责跳转页面使用，默认会传递一个参数：cmMac
     * 
     * @return
     */
    public String showOneCMFlapHisChart() {

        return SUCCESS;
    }

    /**
     * 拼接三个曲线指标： 0.上线次数 1. 异常上线次数 2. 测距命中率 3. 电平调整次数
     * 
     * @return
     */
    public String queryOneCMFlapHisData() {
        Calendar cur = Calendar.getInstance();
        Date endDate = new Date(cur.getTimeInMillis());
        cur.add(Calendar.DATE, -7);
        Date startDate = new Date(cur.getTimeInMillis());
        JSONArray insReal = new JSONArray();
        JSONArray insFail = new JSONArray();
        JSONArray range = new JSONArray();
        JSONArray power = new JSONArray();
        List<CMFlapHis> allFlap = cmcFlapService.queryOneCMFlapHis(MacUtils.convertToMaohaoFormat(cmMac),
                startDate.getTime(), endDate.getTime());
        Integer lastInsIns = 0;
        long lastFlapTime = 0L;
        for (int i = 0; i < allFlap.size(); i++) {
            CMFlapHis flap = allFlap.get(i);
            long time = flap.getCollectTime().getTime();
            if (time == lastFlapTime) { // 如果上一条数据与这一条采集相同，则直接跳过
                continue;
            }
            lastFlapTime = time;
            // 正常ins数统计
            Integer ins = flap.getInsFailNum();
            if (ins != null) {
                JSONArray pointArray = new JSONArray();
                pointArray.add(time);
                pointArray.add(ins);
                insReal.add(pointArray);
            }
            // 异常上线增长次数
            Integer insInc = 0;
            if (i != 0) {
                insInc = ins - lastInsIns;
            }
            lastInsIns = ins;
            JSONArray insPointArray = new JSONArray();
            insPointArray.add(time);
            insPointArray.add(insInc);
            insFail.add(insPointArray);
            // 电平调整增长次数
            Integer powAdj = flap.getIncreasePowerAdjNum();
            JSONArray powerPointArray = new JSONArray();
            powerPointArray.add(time);
            powerPointArray.add(powAdj);
            power.add(powerPointArray);
            // 测距命中率
            Float f = flap.getIncreaseHitPercent();
            JSONArray hitPointArray = new JSONArray();
            hitPointArray.add(time);
            hitPointArray.add(f);
            range.add(hitPointArray);
        }
        JSONObject allData = new JSONObject();
        allData.put("insRealNum", insReal);
        allData.put("insFailNum", insFail);
        allData.put("rangePercent", range);
        allData.put("poweAdjNum", power);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        allData.put("startTime", dateFormat.format(startDate));
        allData.put("endTime", dateFormat.format(endDate));
        writeDataToAjax(allData);
        return NONE;
    }

    public Integer getTopCmFlapInterval() {
        return topCmFlapInterval;
    }

    public void setTopCmFlapInterval(Integer topCmFlapInterval) {
        this.topCmFlapInterval = topCmFlapInterval;
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

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public String getCmMac() {
        return cmMac;
    }

}