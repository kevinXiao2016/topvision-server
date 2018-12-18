/***********************************************************************
 * $Id: OltRealtimeAction.java,v1.0 2014-7-12 上午9:13:07 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.action;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.realtime.domain.OltPonInfo;
import com.topvision.ems.epon.realtime.domain.OltPonTotalInfo;
import com.topvision.ems.epon.realtime.domain.OltSlotInfo;
import com.topvision.ems.epon.realtime.domain.OltSniInfo;
import com.topvision.ems.epon.realtime.domain.OltSubDeviceInfo;
import com.topvision.ems.epon.realtime.domain.OltSubTotalInfo;
import com.topvision.ems.epon.realtime.domain.OltSummaryInfo;
import com.topvision.ems.epon.realtime.domain.OpticalStatsticsInfo;
import com.topvision.ems.epon.realtime.domain.StatisticsResult;
import com.topvision.ems.epon.realtime.service.OltRealtimeService;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;

/**
 * @author flack
 * @created @2014-7-12-上午9:13:07
 *
 */
@Controller("oltRealtimeAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltRealtimeAction extends BaseAction {
    private static final long serialVersionUID = -5118093970869170860L;

    private Long entityId;
    private String entityIp;
    private String entityName;
    // 是否只显示在线
    private boolean onlineFlag;
    @Autowired
    private OltRealtimeService oltRealtimeService;
    @Autowired
    private OltSlotService oltSlotService;

    private JSONObject slotTypeMapping = new JSONObject();

    /**
     * 显示OLT实时信息页面
     * 
     * @return
     */
    public String showOltRealTime() {
        // add by fanzidong, 获取设备的板卡信息
        try {
            List<OltSlotAttribute> slotList = oltSlotService.getOltSlotList(entityId);
            for (OltSlotAttribute slot : slotList) {
                slotTypeMapping.put(slot.getBmapSlotLogNo(), slot.getTopSysBdPreConfigType());
            }
        } catch (Exception e) {
            logger.debug("cannot get entity slot type info, " + e);
        }
        return SUCCESS;
    }

    /**
     * 加载OLT基本信息
     * 
     * @return
     * @throws Exception
     */
    public String loadBaseInfo() throws Exception {
        OltSummaryInfo summaryInfo = oltRealtimeService.getOltSummaryInfo(entityId);
        // 处理Pon口统计信息
        List<OltPonTotalInfo> ponList = summaryInfo.getPonTotalList();
        JSONObject ponJson = new JSONObject();
        ponJson.put("totalNum", ponList.size());
        int ponOnline = 0;
        for (OltPonTotalInfo totalInfo : ponList) {
            if (totalInfo.getOperationStatus().equals(EponConstants.PORT_STATUS_UP)) {
                ponOnline++;
            }
        }
        ponJson.put("onlineNum", ponOnline);
        // 处理下级设备统计信息
        List<OltSubTotalInfo> subList = summaryInfo.getSubTotalList();
        JSONObject subJson = new JSONObject();
        subJson.put("totalNum", subList.size());
        int subOnline = 0;
        for (OltSubTotalInfo totalInfo : subList) {
            if (totalInfo.getOperationStatus().equals(EponConstants.ONU_STATUS_UP)) {
                subOnline++;
            }
        }
        subJson.put("onlineNum", subOnline);
        // 处理SNI口统计信息
        List<OltSniInfo> sniList = summaryInfo.getSniList();
        JSONObject sniJson = new JSONObject();
        sniJson.put("totalNum", sniList.size());
        int sniOnline = 0;
        for (OltSniInfo sniInfo : sniList) {
            if (sniInfo.getOperationStatus().equals(EponConstants.PORT_STATUS_UP)) {
                sniOnline++;
            }
        }
        sniJson.put("onlineNum", sniOnline);
        // 将数据封装在一个json对象中
        JSONObject json = new JSONObject();
        json.put("baseInfo", JSONObject.fromObject(summaryInfo.getBaseInfo()));
        json.put("sniTotal", sniJson);
        json.put("ponTotal", ponJson);
        json.put("subTotal", subJson);
        if (summaryInfo.getCmTotal() != null) {
        json.put("cmTotal", JSONObject.fromObject(summaryInfo.getCmTotal()));
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 加载OLT板卡信息
     * 
     * @return
     * @throws Exception
     */
    public String loadSlotInfo() throws Exception {
        List<OltSlotInfo> slotList = oltRealtimeService.getOltSlotInfo(entityId);
        writeDataToAjax(JSONArray.fromObject(slotList));
        return NONE;
    }

    /**
     * 加载OLT SNI口信息
     * 
     * @return
     * @throws Exception
     */
    public String loadSniInfo() throws Exception {
        List<OltSniInfo> onlineSniList = oltRealtimeService.getOltSniInfo(entityId);
        writeDataToAjax(JSONArray.fromObject(onlineSniList));
        return NONE;
    }

    /**
     * 加载ONU光功率分区间统计信息
     * 
     * @return
     * @throws IOException
     */
    public String loadOpticalStatstics() throws IOException {
        StatisticsResult result = oltRealtimeService.getOpticalThreshold(entityId);
        JSONObject json = new JSONObject();
        JSONObject rx = new JSONObject();
        JSONObject tx = new JSONObject();
        rx.put("title", getI18NString("OLTREAL.subRecvOptical", "epon") + "(dBm)");
        tx.put("title", getI18NString("OLTREAL.subSendOptical", "epon") + "(dBm)");
        // 记录接收N/A数据
        JSONObject dataJson = new JSONObject();
        dataJson.put("color", "#f00");
        dataJson.put("num", result.getRecvErrorNum());
        rx.put("na", dataJson);
        // 记录发送N/A数据
        dataJson = new JSONObject();
        dataJson.put("color", "#f00");
        dataJson.put("num", result.getTransErrorNum());
        tx.put("na", dataJson);
        // 记录接收光功率分区间和发送光功率分区间
        JSONArray rxarray = new JSONArray();
        JSONArray txarray = new JSONArray();
        List<OpticalStatsticsInfo> recvList = result.getRecvStasticsList();
        List<OpticalStatsticsInfo> transList = result.getTransStasticsList();
        for (OpticalStatsticsInfo statsInfo : recvList) {
            JSONObject recvJson = new JSONObject();
            if (statsInfo.getStart() == null) {
                recvJson.put("start", "");
            } else {
                recvJson.put("start", statsInfo.getStart());
            }
            if (statsInfo.getEnd() == null) {
                recvJson.put("end", "");
            } else {
                recvJson.put("end", statsInfo.getEnd());
            }
            recvJson.put("num", statsInfo.getNum());
            recvJson.put("color", statsInfo.getColor());
            recvJson.put("level", statsInfo.getLevel());
            rxarray.add(recvJson);
        }
        for (OpticalStatsticsInfo statsInfo : transList) {
            JSONObject transJson = new JSONObject();
            if (statsInfo.getStart() == null) {
                transJson.put("start", "");
            } else {
                transJson.put("start", statsInfo.getStart());
            }
            if (statsInfo.getEnd() == null) {
                transJson.put("end", "");
            } else {
                transJson.put("end", statsInfo.getEnd());
            }
            transJson.put("num", statsInfo.getNum());
            transJson.put("color", statsInfo.getColor());
            transJson.put("level", statsInfo.getLevel());
            txarray.add(transJson);
        }
        rx.put("box", rxarray);
        tx.put("box", txarray);
        json.put("rx", rx);
        json.put("tx", tx);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 显示pon口详细信息页面
     * 
     * @return
     */
    public String showOltPonInfo() {
        return SUCCESS;
    }

    /**
     * 加载PON口详细信息
     * 
     * @return
     * @throws IOException
     */
    public String loadOltPonInfo() throws IOException {
        List<OltPonInfo> ponList = oltRealtimeService.getOltPonInfo(entityId, onlineFlag);
        writeDataToAjax(JSONArray.fromObject(ponList));
        return NONE;
    }

    /**
     * 显示Olt下级设备信息页面
     * 
     * @return
     */
    public String showOltSubInfo() {
        return SUCCESS;
    }

    /**
     * 加载Olt下级设备信息
     * 
     * @return
     * @throws IOException
     */
    public String loadOltSubInfo() throws IOException {
        try {
            List<OltSubDeviceInfo> subList = oltRealtimeService.getOltSubInfo(entityId, onlineFlag);
            JSONArray subArray = JSONArray.fromObject(subList, selfValueProcessor());
            writeDataToAjax(subArray);
        } catch (Exception e) {
            logger.debug("", e);
        }
        return NONE;
    }

    /**
     * 解决JSON处理数据时默认将null转化为0的问题
     * 
     * @return
     */
    private JsonConfig selfValueProcessor() {
        JsonConfig config = new JsonConfig();
        config.registerDefaultValueProcessor(Integer.class, new DefaultValueProcessor() {
            @SuppressWarnings("rawtypes")
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        return config;
    }

    /**
     * 国际化
     * 
     * @param key
     * @param module
     * @return
     */
    private String getI18NString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public boolean isOnlineFlag() {
        return onlineFlag;
    }

    public void setOnlineFlag(boolean onlineFlag) {
        this.onlineFlag = onlineFlag;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public JSONObject getSlotTypeMapping() {
        return slotTypeMapping;
    }

    public void setSlotTypeMapping(JSONObject slotTypeMapping) {
        this.slotTypeMapping = slotTypeMapping;
    }

}
