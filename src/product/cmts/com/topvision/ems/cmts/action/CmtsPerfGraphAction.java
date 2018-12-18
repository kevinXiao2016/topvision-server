/***********************************************************************
 * $ CmtsPerfGraphAction.java,v1.0 2013-8-26 13:54:22 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.service.CmcChannelService;
import com.topvision.ems.cmc.domain.CmcDownChannelBaseShowInfo;
import com.topvision.ems.cmc.downchannel.service.CmcDownChannelService;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.ems.cmts.domain.CmtsUpLinkPort;
import com.topvision.ems.cmts.service.CmtsChannelService;
import com.topvision.ems.cmts.service.CmtsPerfService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.ems.performance.service.PerfTargetService;
import com.topvision.ems.performance.utils.PerfTargetUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.util.highcharts.domain.Point;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author jay
 * @created @2013-8-26-13:54:22
 */
@Controller("cmtsPerfGraphAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmtsPerfGraphAction extends BaseAction {
    private static final long serialVersionUID = 7342180206202447869L;
    @Resource(name = "perfTargetService")
    private PerfTargetService perfTargetService;
    @Resource(name = "cmtsPerfService")
    private CmtsPerfService cmtsPerfService;
    @Resource(name = "cmcChannelService")
    private CmcChannelService cmcChannelService;
    @Resource(name = "cmcUpChannelService")
    private CmcUpChannelService cmcUpChannelService;
    @Resource(name = "cmcDownChannelService")
    private CmcDownChannelService cmcDownChannelService;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Resource(name = "cmtsChannelService")
    private CmtsChannelService cmtsChannelService;
    private Long entityId;
    private Integer upLinkFlow;
    private Integer channelSpeed;
    private Integer snr;
    private Integer ber;
    private Integer sysUptime;
    private Long deviceType;
    private String groupName;
    private String channelType;
    private String indexs;
    private String perfTarget;
    private String startTime;
    private String endTime;

    /**
     * 获取CMTS的所有指标分组
     * 
     * @return
     * @throws IOException
     */
    public String loadCmtsPerfTargetGroups() throws IOException {
        JSONArray array = new JSONArray();
        List<String> groups = perfTargetService.getPerfTargetGroupsByDeviceType(entityId);
        for (String group : groups) {
            JSONObject o = new JSONObject();
            /*
             * if (PerfTargetConstants.CMC_SERVICE.equals(group)) {// 服务质量包含在线时长，不做性能展示 continue; }
             */
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
        List<String> targets = perfTargetService.getPerfTargetNamesByGroup(groupName, entityId);
        for (String target : targets) {
            JSONObject o = new JSONObject();
            if (target.equals(PerfTargetConstants.CMC_CHANNELSPEED)) {
                o.put("value", PerfTargetConstants.CMC_CHANNELSPEED);
                o.put("text",
                        PerfTargetUtil.getString("Performance." + PerfTargetConstants.CMC_CHANNELSPEED, "performance"));
                array.add(o);
                o = new JSONObject();
                o.put("value", PerfTargetConstants.CHANNEL_USED);
                o.put("text",
                        PerfTargetUtil.getString("Performance." + PerfTargetConstants.CHANNEL_USED, "performance"));
                array.add(o);
            } else if (PerfTargetConstants.CMC_ONLINESTATUS.equals(target)) {
                o.put("value", target);
                o.put("text", PerfTargetUtil.getString("Performance.deviceRelay", "performance"));
                array.add(o);
            } else if (target.equals(PerfTargetConstants.CMC_UPLINKFLOW)) {
                o.put("value", PerfTargetConstants.CMC_UPLINKFLOW);
                o.put("text",
                        PerfTargetUtil.getString("Performance." + PerfTargetConstants.CMC_UPLINKFLOW, "performance"));
                array.add(o);
                o = new JSONObject();
                o.put("value", PerfTargetConstants.UPLINK_USED);
                o.put("text",
                        PerfTargetUtil.getString("Performance." + PerfTargetConstants.UPLINK_USED, "performance"));
                array.add(o);
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
     * 获取信道列表
     * 
     * @return
     * @throws IOException
     */
    public String loadCmtsChannelList() throws IOException {
        JSONArray array = new JSONArray();
        if (PerfTargetConstants.CHANNEL_TYPE_ALL.equals(channelType)) {
            List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                    .getUpChannelBaseShowInfoList(entityId);
            List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList = cmcDownChannelService
                    .getDownChannelBaseShowInfoList(entityId);

            for (CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo : cmcUpChannelBaseShowInfoList) {
                JSONObject o = new JSONObject();
                o.put("value", cmcUpChannelBaseShowInfo.getChannelIndex());
                o.put("text", cmcUpChannelBaseShowInfo.getIfName());
                o.put("status", cmcUpChannelBaseShowInfo.getIfAdminStatus() == 1);
                array.add(o);
            }

            for (CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo : cmcDownChannelBaseShowInfoList) {
                JSONObject o = new JSONObject();
                o.put("value", cmcDownChannelBaseShowInfo.getChannelIndex());
                o.put("text",cmcDownChannelBaseShowInfo.getIfName());
                o.put("status", cmcDownChannelBaseShowInfo.getIfAdminStatus() == 1);
                array.add(o);
            }
        } else if (PerfTargetConstants.CHANNEL_TYPE_US.equals(channelType)) {
            List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                    .getUpChannelBaseShowInfoList(entityId);
            for (CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo : cmcUpChannelBaseShowInfoList) {
                JSONObject o = new JSONObject();
                o.put("value", cmcUpChannelBaseShowInfo.getChannelIndex());
                o.put("text", cmcUpChannelBaseShowInfo.getIfName());
                o.put("status", cmcUpChannelBaseShowInfo.getIfAdminStatus() == 1);
                array.add(o);
            }
        } else if (PerfTargetConstants.CHANNEL_TYPE_DS.equals(channelType)) {
            List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList = cmcDownChannelService
                    .getDownChannelBaseShowInfoList(entityId);
            for (CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo : cmcDownChannelBaseShowInfoList) {
                JSONObject o = new JSONObject();
                o.put("value", cmcDownChannelBaseShowInfo.getChannelIndex());
                o.put("text", cmcDownChannelBaseShowInfo.getIfName());
                o.put("status", cmcDownChannelBaseShowInfo.getIfAdminStatus() == 1);
                array.add(o);
            }
        } else if (PerfTargetConstants.CHANNEL_TYPE_UPLINK.equals(channelType)) {
            List<CmtsUpLinkPort> ports = cmtsChannelService.getUpLinkPortList(entityId);
            for (CmtsUpLinkPort port : ports) {
                JSONObject o = new JSONObject();
                o.put("value", port.getIfIndex());
                o.put("text", port.getIfDescr());
                o.put("status", port.getIfAdminStatus() == 1);
                array.add(o);
            }
        }
        writeDataToAjax(array);
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

        Map<Long, String> map = new HashMap<Long, String>();
        List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                .getUpChannelBaseShowInfoList(entityId);
        List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList = cmcDownChannelService
                .getDownChannelBaseShowInfoList(entityId);
        for (CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo : cmcUpChannelBaseShowInfoList) {
            map.put(cmcUpChannelBaseShowInfo.getChannelIndex(), cmcUpChannelBaseShowInfo.getIfName());
        }
        for (CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo : cmcDownChannelBaseShowInfoList) {
            map.put(cmcDownChannelBaseShowInfo.getChannelIndex(), cmcDownChannelBaseShowInfo.getIfName());
        }
        for (int i = 0; i < strs.length; i++) {
            indexList.add(Long.valueOf(strs[i]));
        }
        for (Long index : indexList) {
            List<Point> points = cmtsPerfService.queryCmtsSignalQualityPoints(entityId, perfTarget, index, startTime,
                    endTime);
            points = PerfTargetUtil.samplePoints(points);
            JSONArray pointArray = new JSONArray();
            for (Point p : points) {
                JSONArray point = new JSONArray();
                point.add(p.getXTime().getTime());
                point.add(p.getY());
                point.add(p.getTipStr());
                pointArray.add(point);
            }
            json.put(map.get(index), pointArray);
        }
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
        List<Long> indexList = new ArrayList<Long>();
        String[] strs = indexs.split(",");

        Map<Long, String> map = new HashMap<Long, String>();
        List<CmcUpChannelBaseShowInfo> cmcUpChannelBaseShowInfoList = cmcUpChannelService
                .getUpChannelBaseShowInfoList(entityId);
        List<CmcDownChannelBaseShowInfo> cmcDownChannelBaseShowInfoList = cmcDownChannelService
                .getDownChannelBaseShowInfoList(entityId);
        List<CmtsUpLinkPort> ports = cmtsChannelService.getUpLinkPortList(entityId);

        for (CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo : cmcUpChannelBaseShowInfoList) {
            map.put(cmcUpChannelBaseShowInfo.getChannelIndex(), cmcUpChannelBaseShowInfo.getIfName());
        }
        for (CmcDownChannelBaseShowInfo cmcDownChannelBaseShowInfo : cmcDownChannelBaseShowInfoList) {
            map.put(cmcDownChannelBaseShowInfo.getChannelIndex(), cmcDownChannelBaseShowInfo.getIfName());
        }
        for (Port port : ports) {
            map.put(new Long(port.getIfIndex()), port.getIfDescr());
        }

        for (int i = 0; i < strs.length; i++) {
            indexList.add(Long.valueOf(strs[i]));
        }
        for (Long index : indexList) {
            List<Point> points = cmtsPerfService.queryCmtsFlowPoints(entityId, perfTarget, index, startTime, endTime);
            points = PerfTargetUtil.samplePoints(points);
            JSONArray pointArray = new JSONArray();
            for (Point p : points) {
                JSONArray point = new JSONArray();
                point.add(p.getXTime().getTime());
                point.add(p.getY());
                pointArray.add(point);
            }
            json.put(map.get(index), pointArray);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 加载指定指标的子指标
     * 
     * @return
     * @throws IOException
     */
    public String loadCmtsSubPerfTargets() throws IOException {
        JSONArray array = new JSONArray();
        List<String> groups = new ArrayList<String>();
        if (PerfTargetConstants.CMC_BER.equals(perfTarget)) {
            groups = Arrays.asList(PerfTargetConstants.BER_PERFTARGETS);
        } else if (PerfTargetConstants.CMC_CHANNELSPEED.equals(perfTarget)) {
            groups = Arrays.asList(PerfTargetConstants.CMTSCHANNELSPEED_PERFTARGETS);
        } else if (PerfTargetConstants.CMC_UPLINKFLOW.equals(perfTarget)) {
            groups = Arrays.asList(PerfTargetConstants.CMC_UPLINKFLOW_INOUT);
        } else if (PerfTargetConstants.CHANNEL_USED.equals(perfTarget)) {
            groups = Arrays.asList(PerfTargetConstants.CMTSCHANNELUSED_PERFTARGETS);
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
     * 加载设备响应延时
     * 
     * @return
     * @throws IOException
     */
    public String loadCmtsRelayPerfData() throws IOException {
        JSONObject json = new JSONObject();
        Entity entity = entityService.getEntity(entityId);
        List<Point> points = cmtsPerfService.queryCmtsRelayPerfPoints(entityId, startTime, endTime);
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

    /**
     * 加载CMTS服务质量数据,CPU和Mem
     * 
     * @return
     * @throws Exception
     */
    public String loadCmtsServicePerfData() throws Exception {
        List<Point> points = cmtsPerfService.getCmtsServicePerfPoints(entityId, perfTarget, startTime, endTime);
        points = PerfTargetUtil.samplePoints(points);
        JSONArray jsonArray = new JSONArray();
        for (Point p : points) {
            JSONArray point = new JSONArray();
            point.add(p.getXTime().getTime());
            point.add(p.getY().floatValue() * 100);
            jsonArray.add(point);
        }
        JSONObject json = new JSONObject();
        json.put("", jsonArray);
        writeDataToAjax(json);
        return NONE;
    }

    public Integer getBer() {
        return ber;
    }

    public void setBer(Integer ber) {
        this.ber = ber;
    }

    public Integer getChannelSpeed() {
        return channelSpeed;
    }

    public void setChannelSpeed(Integer channelSpeed) {
        this.channelSpeed = channelSpeed;
    }

    public CmtsPerfService getCmtsPerfService() {
        return cmtsPerfService;
    }

    public void setCmtsPerfService(CmtsPerfService cmtsPerfService) {
        this.cmtsPerfService = cmtsPerfService;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public PerfTargetService getPerfTargetService() {
        return perfTargetService;
    }

    public void setPerfTargetService(PerfTargetService perfTargetService) {
        this.perfTargetService = perfTargetService;
    }

    public Integer getSnr() {
        return snr;
    }

    public void setSnr(Integer snr) {
        this.snr = snr;
    }

    public Integer getSysUptime() {
        return sysUptime;
    }

    public void setSysUptime(Integer sysUptime) {
        this.sysUptime = sysUptime;
    }

    public Integer getUpLinkFlow() {
        return upLinkFlow;
    }

    public void setUpLinkFlow(Integer upLinkFlow) {
        this.upLinkFlow = upLinkFlow;
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public CmcChannelService getCmcChannelService() {
        return cmcChannelService;
    }

    public void setCmcChannelService(CmcChannelService cmcChannelService) {
        this.cmcChannelService = cmcChannelService;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public String getIndexs() {
        return indexs;
    }

    public void setIndexs(String indexs) {
        this.indexs = indexs;
    }

    public String getPerfTarget() {
        return perfTarget;
    }

    public void setPerfTarget(String perfTarget) {
        this.perfTarget = perfTarget;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public CmtsChannelService getCmtsChannelService() {
        return cmtsChannelService;
    }

    public void setCmtsChannelService(CmtsChannelService cmtsChannelService) {
        this.cmtsChannelService = cmtsChannelService;
    }
}