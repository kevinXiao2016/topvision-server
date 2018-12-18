/***********************************************************************
 * $Id: CmcPerfGraphAction.java,v1.0 2013-8-8 上午09:57:38 $
 *
 * @author: lizongtian
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.ems.cmc.domain.CmcChannelBasic;
import com.topvision.ems.cmc.optical.service.CmcOpticalReceiverService;
import com.topvision.ems.cmc.perf.service.CmcPerfGraphService;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.action.PerfGraphAction;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.ems.performance.service.PerfTargetService;
import com.topvision.ems.performance.utils.PerfTargetUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.util.highcharts.domain.Point;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author lizongtian
 * @created @2013-8-8-上午09:57:38
 * 
 */
@Controller("cmcPerfGraphAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcPerfGraphAction extends PerfGraphAction {

    private static final long serialVersionUID = -7617508213475425455L;
    private static final Logger LOGGER = LoggerFactory.getLogger(CmcPerfGraphAction.class);

    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "cmcPerfGraphService")
    private CmcPerfGraphService cmcPerfGraphService;
    @Resource(name = "perfTargetService")
    private PerfTargetService perfTargetService;
    @Resource(name = "cmcPerfService")
    private CmcPerfService cmcPerfService;
    @Resource(name = "cpeService")
    private CpeService cpeService;
    @Resource(name = "cmcOpticalReceiverService")
    private CmcOpticalReceiverService cmcOpticalReceiverService;

    private String channelType;
    private Long channelIndex;

    private JSONArray cmcChanelNames;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    private Long cmcId;
    private Long cmcIndex;
    private String perfTarget;
    private String flag;
    @Resource(name = "entityService")
    private EntityService entityService;

    private Integer productType;
    private CmcAttribute cmcAttribute;

    /**
     * 显示性能展示页面
     * 
     * @return String
     */
    public String showCmcCurPerf() {
    	cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        cmcChanelNames = JSONArray.fromObject(cmcPerfService.getCmcChanelNames(cmcId));
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        Entity entity = entityService.getEntity(cmcId);
        entityName = entity.getName();
        cmcIndex = cmcService.getCmcAttributeByCmcId(cmcId).getCmcIndex();
        deviceType = entity.getTypeId();
        productType = Integer.valueOf(String.valueOf(deviceType));
        return SUCCESS;
    }

    /**
     * 获取CCMTS的所有指标分组
     * 
     * @return
     * @throws IOException
     */
    public String loadCmcPerfTargetGroups() throws IOException {
        JSONArray array = new JSONArray();
        CmcAttribute cmcAttr = cmcService.getCmcAttributeByCmcId(cmcId);
        List<String> groups = perfTargetService.getPerfTargetGroupsByDeviceType(cmcId);
        // Modified by huangdongsheng 增加光机接收功率性能展示的设备版本控制
        for (String group : groups) {
            if (PerfTargetConstants.CMC_BUSINESSQUALITY.equals(group)) {
                if (cmcAttr.getTopCcmtsSysDorType() == null) {
                    continue;
                }
            }
            JSONObject o = new JSONObject();
            o.put("value", group);
            o.put("text", PerfTargetUtil.getString("Performance." + group, "performance"));
            array.add(o);
        }
        writeDataToAjax(array);
        return NONE;
    }

    /**
     * 加载指定分组下的指标列表
     * 
     * @return
     * @throws IOException
     */
    public String loadPerfTargetsByGroup() throws IOException {
        JSONArray array = new JSONArray();
        List<String> targets = perfTargetService.getPerfTargetNamesByGroup(groupName, cmcId);
        Long entityType = entityService.getEntity(cmcId).getTypeId();
        for (Iterator<String> iters = targets.iterator(); iters.hasNext();) {
            String targetName = iters.next();
            if (PerfTargetConstants.CMC_CMFLAP.equals(targetName)) {
                iters.remove();
            }
        }
        for (String target : targets) {
            JSONObject o = new JSONObject();
            if (target.equals(PerfTargetConstants.CMC_UPLINKFLOW)) {
                o.put("value", PerfTargetConstants.UPLINK_IN_FLOW);
                o.put("text",
                        PerfTargetUtil.getString("Performance." + PerfTargetConstants.UPLINK_IN_FLOW, "performance"));
                array.add(o);
                o = new JSONObject();
                o.put("value", PerfTargetConstants.UPLINK_OUT_FLOW);
                o.put("text",
                        PerfTargetUtil.getString("Performance." + PerfTargetConstants.UPLINK_OUT_FLOW, "performance"));
                array.add(o);
                o = new JSONObject();
                o.put("value", PerfTargetConstants.UPLINK_IN_USED);
                o.put("text",
                        PerfTargetUtil.getString("Performance." + PerfTargetConstants.UPLINK_IN_USED, "performance"));
                array.add(o);
                o = new JSONObject();
                o.put("value", PerfTargetConstants.UPLINK_OUT_USED);
                o.put("text",
                        PerfTargetUtil.getString("Performance." + PerfTargetConstants.UPLINK_OUT_USED, "performance"));
                array.add(o);
            } else if (PerfTargetConstants.CMC_ONLINESTATUS.equals(target)) {
                o.put("value", target);
                o.put("text", PerfTargetUtil.getString("Performance.deviceRelay", "performance"));
                array.add(o);
            } else if (target.equals(PerfTargetConstants.CMC_MACFLOW)) {
                o.put("value", PerfTargetConstants.MAC_IN_FLOW);
                o.put("text", PerfTargetUtil.getString("Performance." + PerfTargetConstants.MAC_IN_FLOW, "performance"));
                array.add(o);
                o = new JSONObject();
                o.put("value", PerfTargetConstants.MAC_OUT_FLOW);
                o.put("text",
                        PerfTargetUtil.getString("Performance." + PerfTargetConstants.MAC_OUT_FLOW, "performance"));
                array.add(o);
                o = new JSONObject();
                o.put("value", PerfTargetConstants.MAC_USED);
                o.put("text", PerfTargetUtil.getString("Performance." + PerfTargetConstants.MAC_USED, "performance"));
                array.add(o);
            } else if (target.equals(PerfTargetConstants.CMC_CHANNELSPEED)) {
                o.put("value", PerfTargetConstants.CMC_CHANNELSPEED);
                o.put("text",
                        PerfTargetUtil.getString("Performance." + PerfTargetConstants.CMC_CHANNELSPEED, "performance"));
                array.add(o);
                o = new JSONObject();
                o.put("value", PerfTargetConstants.CHANNEL_USED);
                o.put("text",
                        PerfTargetUtil.getString("Performance." + PerfTargetConstants.CHANNEL_USED, "performance"));
                array.add(o);
            } else if (target.equals(PerfTargetConstants.CMC_MODULETEMP)) {
                if (!entityTypeService.isCCMTSAOrBType(entityType)) {
                    o.put("value", target);
                    o.put("text", PerfTargetUtil.getString("Performance." + target, "performance"));
                    array.add(o);
                }
            } else {
                o.put("value", target);
                o.put("text", PerfTargetUtil.getString("Performance." + target, "performance"));
                array.add(o);
            }
        }
        writeDataToAjax(array);
        return NONE;
    }

    /**
     * 加载指定指标的子指标
     * 
     * @return
     * @throws IOException
     */
    public String loadCmcSubPerfTargets() throws IOException {
        JSONArray array = new JSONArray();
        List<String> groups = new ArrayList<String>();
        Long entityType = entityService.getEntity(cmcId).getTypeId();
        if (PerfTargetConstants.CMC_OPTLINK.equals(perfTarget)) {
            groups = Arrays.asList(PerfTargetConstants.OPTLINK_PERFTARGETS);
        } else if (PerfTargetConstants.CMC_MODULETEMP.equals(perfTarget)) {
            if (entityTypeService.isCCMTS8800SType(entityType)) {
                groups = Arrays.asList(PerfTargetConstants.OUTSIDE_TEMP);
            } else {
                groups = Arrays.asList(PerfTargetConstants.MODULETEMP_PERFTARGETS);
            }
        } else if (PerfTargetConstants.CMC_BER.equals(perfTarget)) {
            groups = Arrays.asList(PerfTargetConstants.BER_PERFTARGETS);
        }
        for (String group : groups) {
            JSONObject o = new JSONObject();
            o.put("value", group);
            o.put("text", PerfTargetUtil.getString("Performance." + group, "performance"));
            array.add(o);
        }
        writeDataToAjax(array);
        return NONE;
    }

    /**
     * 获得CMC设备信道列表
     * 
     * @return
     * @throws IOException
     */
    public String loadCmcChannelList() throws IOException {
        JSONArray array = new JSONArray();
        List<CmcChannelBasic> cmcChannelBasics = cmcPerfGraphService.queryCmcChannelList(entityId, channelType);
        for (CmcChannelBasic cmcChannelBasic : cmcChannelBasics) {
            JSONObject o = new JSONObject();
            o.put("value", cmcChannelBasic.getChannelIndex());
            o.put("text",
                    cmcChannelBasic.getChannelType() + CmcIndexUtils.getChannelId(cmcChannelBasic.getChannelIndex()));
            o.put("status", cmcChannelBasic.getChannelStatus() == 1);
            array.add(o);
        }
        writeDataToAjax(array);
        return NONE;
    }

    /**
     * 加载板卡数据
     * 
     * @return
     * @throws IOException
     */
    public String loadBoardUsedData() throws IOException {
        List<Point> points = cmcPerfGraphService.queryCmcServicePerfPoints(entityId, perfTarget, startTime, endTime);
        points = PerfTargetUtil.samplePoints(points);
        JSONArray jsonArray = new JSONArray();
        for (Point p : points) {
            JSONArray point = new JSONArray();
            point.add(p.getXTime().getTime());
            point.add(p.getY());
            jsonArray.add(point);
        }
        JSONObject json = new JSONObject();
        json.put("", jsonArray);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 加载光链路信息数据
     * 
     * @return
     * @throws IOException
     */
    public String loadOptLinkData() throws IOException {
        List<Point> points = cmcPerfGraphService.queryCmcOptLinkPerfPoints(entityId, perfTarget, startTime, endTime);
        points = PerfTargetUtil.samplePoints(points);
        JSONArray jsonArray = new JSONArray();
        for (Point p : points) {
            JSONArray point = new JSONArray();
            point.add(p.getXTime().getTime());
            point.add(p.getY());
            jsonArray.add(point);
        }
        JSONObject json = new JSONObject();
        json.put("", jsonArray);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 加载温度数据
     * 
     * @return
     * @throws IOException
     */
    public String loadTempData() throws IOException {
        List<Point> points = new ArrayList<Point>();
        if (perfTarget == null) {
            LOGGER.error("loadTempData error, no perfTarget!");
        } else {
            points = cmcPerfGraphService.queryCmcTempPoints(entityId, perfTarget, startTime, endTime);
        }
        points = PerfTargetUtil.samplePoints(points);
        JSONArray jsonArray = new JSONArray();
        for (Point p : points) {
            JSONArray point = new JSONArray();
            point.add(p.getXTime().getTime());
            // 进行温度转换
            Double tempValue = p.getY();
            if (tempValue != null && tempValue != -1) {
                point.add(UnitConfigConstant.translateTemperature(tempValue.intValue()));
            } else {
                point.add(p.getY());
            }
            jsonArray.add(point);
        }
        JSONObject json = new JSONObject();
        json.put("", jsonArray);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 加载流量数据
     * 
     * @return
     * @throws IOException
     */
    public String loadFlowData() throws IOException {
        JSONObject json = new JSONObject();
        if (PerfTargetConstants.CMC_UPLINKFLOW.equals(perfTarget) || PerfTargetConstants.UPLINK_USED.equals(perfTarget)
                || PerfTargetConstants.UPLINK_IN_USED.equals(perfTarget)
                || PerfTargetConstants.UPLINK_OUT_USED.equals(perfTarget)) {
            List<CmcPhyConfig> portList = cmcPerfGraphService.queryCmcUpLinkPorts(entityId);
            for (CmcPhyConfig cmcPhyConfig : portList) {
                Integer index = cmcPhyConfig.getPhyIndex();
                List<Point> points = cmcPerfGraphService.queryCmcUpLinkFlowPoints(entityId, index, direction,
                        startTime, endTime, perfTarget);
                points = PerfTargetUtil.samplePoints(points);
                JSONArray jsonArray = new JSONArray();
                for (Point p : points) {
                    JSONArray point = new JSONArray();
                    point.add(p.getXTime().getTime());
                    point.add(p.getY());
                    jsonArray.add(point);
                }
                json.put(index, jsonArray);
            }

        } else if (PerfTargetConstants.CMC_MACFLOW.equals(perfTarget)
                || PerfTargetConstants.MAC_USED.equals(perfTarget)) {
            List<Point> points = cmcPerfGraphService.queryCmcMacFlowPoints(entityId, channelIndex, direction,
                    perfTarget, startTime, endTime);
            points = PerfTargetUtil.samplePoints(points);
            JSONArray jsonArray = new JSONArray();
            for (Point p : points) {
                JSONArray point = new JSONArray();
                point.add(p.getXTime().getTime());
                point.add(p.getY());
                jsonArray.add(point);
            }
            json.put("", jsonArray);
        } else if (PerfTargetConstants.CMC_CHANNELSPEED.equals(perfTarget)
                || PerfTargetConstants.CHANNEL_USED.equals(perfTarget)) {
            List<Long> indexList = new ArrayList<Long>();
            String[] strs = indexs.split(",");
            for (int i = 0; i < strs.length; i++) {
                indexList.add(Long.valueOf(strs[i]));
            }
            for (Long index : indexList) {
                List<Point> points = cmcPerfGraphService.queryCmcChannelSpeedPoints(entityId, index, perfTarget,
                        startTime, endTime);
                points = PerfTargetUtil.samplePoints(points);
                JSONArray pointArray = new JSONArray();
                for (Point p : points) {
                    JSONArray point = new JSONArray();
                    point.add(p.getXTime().getTime());
                    point.add(p.getY());
                    pointArray.add(point);
                }
                json.put(CmcIndexUtils.getCmcSourceString(index) + CmcIndexUtils.getChannelId(index), pointArray);
            }
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 加载信道信号质量数据
     * 
     * @return
     * @throws IOException
     */
    public String loadChannelSingalQuality() throws IOException {
        JSONObject json = new JSONObject();
        List<Long> indexList = new ArrayList<Long>();
        String[] strs = indexs.split(",");
        for (int i = 0; i < strs.length; i++) {
            indexList.add(Long.valueOf(strs[i]));
        }
        for (Long index : indexList) {
            List<Point> points = cmcPerfGraphService.queryCmcSignalQualityPoints(entityId, perfTarget, index,
                    startTime, endTime);
            points = PerfTargetUtil.samplePoints(points);
            JSONArray pointArray = new JSONArray();
            for (Point p : points) {
                JSONArray point = new JSONArray();
                point.add(p.getXTime().getTime());
                point.add(p.getY());
                point.add(p.getTipStr());
                pointArray.add(point);
            }
            json.put(CmcIndexUtils.getCmcSourceString(index) + CmcIndexUtils.getChannelId(index), pointArray);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 加载信道CM数量
     * 
     * @return
     * @throws IOException
     */
    public String loadChannelCmNumber() throws IOException {
        JSONObject json = new JSONObject();
        List<Long> indexList = new ArrayList<Long>();
        String[] strs = indexs.split(",");
        for (int i = 0; i < strs.length; i++) {
            indexList.add(Long.valueOf(strs[i]));
        }
        for (Long index : indexList) {
            List<Point> points = cmcPerfGraphService.queryCmcChannelCmNumberPoints(entityId, perfTarget, index,
                    startTime, endTime);
            points = PerfTargetUtil.samplePoints(points);
            JSONArray pointArray = new JSONArray();
            for (Point p : points) {
                JSONArray point = new JSONArray();
                point.add(p.getXTime().getTime());
                point.add(p.getY());
                pointArray.add(point);
            }
            json.put("US" + CmcIndexUtils.getMarkFromIndex(index) + ":" + CmcIndexUtils.getChannelId(index), pointArray);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 加载光机接收功率信息
     * 
     * @author dosion
     * @date 2013-12-17
     * @return
     * @throws IOException
     */
    public String loadOpticalReceiverReceivedPowerData() throws IOException {
        JSONObject json = new JSONObject();
        long index = cmcService.getCmcIndexByCmcId(entityId);
        String s = "";
        for (int i = 0; i < 2; i++) {
            index = index + 2;
            List<Point> points = cmcOpticalReceiverService.readOpticalReceivedPowerData(entityId, index, startTime,
                    endTime);
            points = PerfTargetUtil.samplePoints(points);
            JSONArray jsonArray = new JSONArray();
            for (Point p : points) {
                JSONArray point = new JSONArray();
                point.add(p.getXTime().getTime());
                point.add(p.getY());
                jsonArray.add(point);
            }
            if (i == 0) {
                s = "A";
            } else {
                s = "B";
            }
            json.put(s, jsonArray);
        }
        writeDataToAjax(json);
        return NONE;
    }

    public String loadCmcRelayPerfData() throws IOException {
        JSONObject json = new JSONObject();
        Entity entity = entityService.getEntity(entityId);
        List<Point> points = cmcPerfGraphService.queryCmcRelayPerfPoints(entityId, startTime, endTime);
        points = PerfTargetUtil.samplePoints(points);
        JSONArray pointArray = new JSONArray();
        for (Point p : points) {
            JSONArray point = new JSONArray();
            point.add(p.getXTime().getTime());
            point.add(p.getY());
            pointArray.add(point);
        }
        json.put(entity.getName(), pointArray);
        writeDataToAjax(json);
        return NONE;
    }

    public String loadcmcNetInfo() throws IOException {
        JSONObject json = new JSONObject();
        List<Point> points = cmcPerfGraphService.queryCmcNetInfoPoints(entityId, perfTarget, channelIndex, startTime,
                endTime);
        points = PerfTargetUtil.samplePoints(points);
        JSONArray pointArray = new JSONArray();
        for (Point p : points) {
            JSONArray point = new JSONArray();
            point.add(p.getXTime().getTime());
            point.add(p.getY());
            pointArray.add(point);
        }
        json.put("", pointArray);
        writeDataToAjax(json);
        return NONE;
    }

    public String loadcmcCmNum() throws IOException {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.cmc.resources");
        JSONObject json = new JSONObject();
        Long cmcIndex = cpeService.getCmcIndexByCmcId(entityId);
        Long entity = cmcService.getEntityIdByCmcId(entityId);
        List<Point> points1 = cmcPerfGraphService.queryCmcOnlineCmPoints(entity, cmcIndex, startTime, endTime);
        List<Point> points2 = cmcPerfGraphService.queryCmcAllCmPoints(entity, cmcIndex, startTime, endTime);
        points1 = PerfTargetUtil.samplePoints(points1);
        points2 = PerfTargetUtil.samplePoints(points2);
        JSONArray pointArray1 = new JSONArray();
        JSONArray pointArray2 = new JSONArray();
        for (Point p : points1) {
            JSONArray point = new JSONArray();
            point.add(p.getXTime().getTime());
            point.add(p.getY());
            pointArray1.add(point);
        }
        for (Point p : points2) {
            JSONArray point = new JSONArray();
            point.add(p.getXTime().getTime());
            point.add(p.getY());
            pointArray2.add(point);
        }
        json.put(resourceManager.getString("Perf.OnlineCmNum"), pointArray1);
        json.put(resourceManager.getString("Perf.allCmNum"), pointArray2);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 加载60V电压
     * 
     * @return
     */
    public String loadCmcDorVoltage() throws IOException {
        List<Point> points = new ArrayList<Point>();
        if (perfTarget == null) {
            LOGGER.error("loadTempData error, no perfTarget!");
        } else {
            points = cmcPerfGraphService.queryCmcVoltagePoints(entityId, perfTarget, startTime, endTime);
        }
        JSONObject json = new JSONObject();
        points = PerfTargetUtil.samplePoints(points);
        JSONArray jsonArray = new JSONArray();
        for (Point p : points) {
            JSONArray point = new JSONArray();
            point.add(p.getXTime().getTime());
            point.add(p.getY());
            jsonArray.add(point);
        }
        json.put("", jsonArray);
        writeDataToAjax(json);
        return NONE;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public JSONArray getCmcChanelNames() {
        return cmcChanelNames;
    }

    public void setCmcChanelNames(JSONArray cmcChanelNames) {
        this.cmcChanelNames = cmcChanelNames;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public String getPerfTarget() {
        return perfTarget;
    }

    public void setPerfTarget(String perfTarget) {
        this.perfTarget = perfTarget;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

	public CmcAttribute getCmcAttribute() {
		return cmcAttribute;
	}

	public void setCmcAttribute(CmcAttribute cmcAttribute) {
		this.cmcAttribute = cmcAttribute;
	}

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
