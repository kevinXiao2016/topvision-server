/***********************************************************************
 * $Id: OltPerfAction.java,v1.0 2013-10-25 下午3:41:18 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPortInfo;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.performance.service.OltPerfGraphService;
import com.topvision.ems.epon.utils.BoardType;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.action.PerfGraphAction;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.ems.performance.service.PerfTargetService;
import com.topvision.ems.performance.utils.PerfTargetUtil;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.util.highcharts.domain.Point;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author flack
 * @created @2013-10-25-下午3:41:18
 * 
 */
@Controller("oltPerfGraphAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltPerfGraphAction extends PerfGraphAction {
    private static final long serialVersionUID = -4089460977563770143L;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltPerfGraphService oltPerfGraphService;
    @Autowired
    private PerfTargetService perfTargetService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private OltSniService oltSniService;
    private Entity entity;
    private String perfTarget;
    private Long portIndex;
    private String portType;
    private int start;
    private int limit;
    private String cameraSwitch;
    // 默认只选中4个
    private static final int MAX_CHECKED_NUM = 4;

    /**
     * 加载OLT性能指标的分组信息
     * 
     * @return
     * @throws IOException
     */
    public String loadOltPerfTargetGroups() throws IOException {
        JSONArray array = new JSONArray();
        List<String> groups = perfTargetService.getPerfTargetGroupsByDeviceType(entityId);
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
            if (target.equals(PerfTargetConstants.OLT_SNIFLOW)) {
                o.put("value", PerfTargetConstants.SNI_IN_FLOW);
                o.put("text", PerfTargetUtil.getString("Performance." + PerfTargetConstants.SNI_IN_FLOW, "performance"));
                array.add(o);
                o = new JSONObject();
                o.put("value", PerfTargetConstants.SNI_OUT_FLOW);
                o.put("text",
                        PerfTargetUtil.getString("Performance." + PerfTargetConstants.SNI_OUT_FLOW, "performance"));
                array.add(o);
                o = new JSONObject();
                o.put("value", PerfTargetConstants.SNI_IN_USED);
                o.put("text", PerfTargetUtil.getString("Performance." + PerfTargetConstants.SNI_IN_USED, "performance"));
                array.add(o);
                o = new JSONObject();
                o.put("value", PerfTargetConstants.SNI_OUT_USED);
                o.put("text",
                        PerfTargetUtil.getString("Performance." + PerfTargetConstants.SNI_OUT_USED, "performance"));
                array.add(o);
            } else if (target.equals(PerfTargetConstants.OLT_PONFLOW)) {
                o.put("value", PerfTargetConstants.PON_IN_FLOW);
                o.put("text", PerfTargetUtil.getString("Performance." + PerfTargetConstants.PON_IN_FLOW, "performance"));
                array.add(o);
                o = new JSONObject();
                o.put("value", PerfTargetConstants.PON_OUT_FLOW);
                o.put("text",
                        PerfTargetUtil.getString("Performance." + PerfTargetConstants.PON_OUT_FLOW, "performance"));
                array.add(o);
                o = new JSONObject();
                o.put("value", PerfTargetConstants.PON_IN_USED);
                o.put("text", PerfTargetUtil.getString("Performance." + PerfTargetConstants.PON_IN_USED, "performance"));
                array.add(o);
                o = new JSONObject();
                o.put("value", PerfTargetConstants.PON_OUT_USED);
                o.put("text",
                        PerfTargetUtil.getString("Performance." + PerfTargetConstants.PON_OUT_USED, "performance"));
                array.add(o);
            } else if (target.equals(PerfTargetConstants.OLT_ONLINESTATUS)) {
                o.put("value", PerfTargetConstants.OLT_ONLINESTATUS);
                o.put("text", PerfTargetUtil.getString("Performance.deviceRelay", "performance"));
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
     * 加载OLT的光链路的性能指标
     * 
     * @return
     * @throws IOException
     */
    public String loadOltOptSubPerfTargets() throws IOException {
        JSONArray array = new JSONArray();
        List<String> subTargets = Arrays.asList(PerfTargetConstants.OPTLINK_PERFTARGETS);
        for (String subTarget : subTargets) {
            JSONObject o = new JSONObject();
            o.put("value", subTarget);
            o.put("text", PerfTargetUtil.getString("Performance." + subTarget, "performance"));
            array.add(o);
        }
        writeDataToAjax(array);
        return NONE;
    }

    /**
     * 显示OLT性能
     * 
     * @return String
     */
    public String showOltPerfViewJsp() {
        Properties properties = systemPreferencesService.getModulePreferences("camera");
        cameraSwitch = properties.getProperty("camera.switch");
        entity = entityService.getEntity(entityId);
        entityName = entity.getName();
        return SUCCESS;
    }

    /**
     * 获得设备板卡列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadOltSlotList() throws Exception {
        List<OltSlotAttribute> oltSlotAttributes = oltPerfGraphService.queryOltSlotListByEntityId(entityId);
        JSONArray json = new JSONArray();
        @SuppressWarnings("unused")
        int count = 0;
        for (OltSlotAttribute oltSlot : oltSlotAttributes) {
            Long slotIndex = oltSlot.getSlotIndex();
            JSONObject $slot = new JSONObject();
            $slot.put("id", slotIndex);
            $slot.put("name", BoardType.getName(oltSlot.getTopSysBdPreConfigType()) + " : " + oltSlot.getSlotNo());
            if (oltSlot.getbOperationStatus() == EponConstants.OLT_BOARD_OPERATION_STATUS_UP.intValue()) {
                // $slot.put("checked", count++ < MAX_CHECKED_NUM);
                $slot.put("icon", "/images/fault/trap_on.png");
                $slot.put("nm3kTip", "UP");
            } else {
                $slot.put("icon", "/images/fault/trap_off.png");
                $slot.put("nm3kTip", "DOWN");
            }
            json.add($slot);
        }
        // 向页面传值
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获得设备风扇列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadOltFanList() throws Exception {
        List<OltFanAttribute> oltFanAttributes = oltPerfGraphService.queryOltFanListByEntityId(entityId);
        JSONArray json = new JSONArray();
        for (OltFanAttribute oltFanAttribute : oltFanAttributes) {
            JSONObject o = new JSONObject();
            o.put("id", oltFanAttribute.getFanCardIndex());
            o.put("name", PerfTargetUtil.getString("Performance.fan", "performance")
                    + (oltFanAttribute.getFanNo() - OltFanAttribute.STARTNO_INTEGER + 1));
            // o.put("checked", true);
            json.add(o);
        }
        // 向页面传值
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获得设备SNI端口列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadSniPortList() throws Exception {
        List<OltSniAttribute> sniAttributes = oltPerfGraphService.queryOltSniListByEntityId(entityId);
        List<OltSlotAttribute> oltSlotList = oltSlotService.getOltSlotList(entityId);
        JSONArray nodeArray = new JSONArray();
        Map<Long, JSONObject> slotMap = new HashMap<Long, JSONObject>();
        int count = 0;
        for (OltSniAttribute oltSniAttribute : sniAttributes) {
            Long slotId = oltSniAttribute.getSlotId();
            // 处理板卡
            if (!slotMap.containsKey(slotId)) {
                JSONObject slot = new JSONObject();
                slot.put("id", slotId);
                slot.put("pId", -1);
                for (OltSlotAttribute $slot : oltSlotList) {
                    if ($slot.getSlotId().equals(slotId)) {
                        slot.put("name",
                                BoardType.getName($slot.getTopSysBdPreConfigType()) + " : " + $slot.getSlotNo());
                    }
                }
                slot.put("open", true);
                slot.put("nocheck", true);
                slotMap.put(slotId, slot);
                // 将板卡加入到节点中
                nodeArray.add(slot);
            }
            JSONObject port = new JSONObject();
            port.put("id", oltSniAttribute.getSniIndex());
            port.put("pId", slotId);
            port.put("name", oltSniAttribute.getSniDisplayName());
            port.put("open", true);
            if (oltSniAttribute.getSniOperationStatus() == EponConstants.PORT_STATUS_UP.intValue()) {
                port.put("online", true);
                port.put("checked", count++ < MAX_CHECKED_NUM);
                port.put("icon", "/images/fault/trap_on.png");
                port.put("nm3kTip", "LINK_UP");
            } else {
                port.put("online", false);
                port.put("icon", "/images/fault/trap_off.png");
                port.put("nm3kTip", "LINK_DOWN");
            }
            nodeArray.add(port);
        }
        // 向页面传值
        writeDataToAjax(nodeArray);
        return NONE;
    }

    /**
     * 获得设备PON端口列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadPonPortList() throws Exception {
        List<OltPonAttribute> ponAttributes = oltPerfGraphService.queryOltPonListByEntityId(entityId);
        List<OltSlotAttribute> oltSlotList = oltSlotService.getOltSlotList(entityId);
        JSONArray nodeArray = new JSONArray();
        Map<Long, JSONObject> slotMap = new HashMap<Long, JSONObject>();
        int count = 0;
        for (OltPonAttribute oltPonAttribute : ponAttributes) {
            Long slotId = oltPonAttribute.getSlotId();
            // 处理板卡
            if (!slotMap.containsKey(slotId)) {
                JSONObject slot = new JSONObject();
                slot.put("id", slotId);
                slot.put("pId", -1);
                for (OltSlotAttribute $slot : oltSlotList) {
                    if ($slot.getSlotId().equals(slotId)) {
                        slot.put("name",
                                BoardType.getName($slot.getTopSysBdPreConfigType()) + " : " + $slot.getSlotNo());
                    }
                }
                slot.put("open", true);
                slot.put("nocheck", true);
                slotMap.put(slotId, slot);
                // 将板卡加入到节点中
                nodeArray.add(slot);
            }
            JSONObject port = new JSONObject();
            port.put("id", oltPonAttribute.getPonIndex());
            port.put("ponId", oltPonAttribute.getPonId());
            port.put("pId", slotId);
            port.put("open", true);
            String $disp = EponIndex.getPortStringByIndex(oltPonAttribute.getPonIndex()).toString();
            if (oltPonAttribute.getPonOperationStatus() == EponConstants.PORT_STATUS_UP.intValue()) {
                port.put("online", true);
                port.put("checked", count++ < MAX_CHECKED_NUM);
                port.put("icon", "/images/fault/trap_on.png");
                port.put("nm3kTip", "LINK_UP");
            } else {
                port.put("online", false);
                port.put("icon", "/images/fault/trap_off.png");
                port.put("nm3kTip", "LINK_DOWN");
            }
            port.put("name", $disp);
            nodeArray.add(port);
        }
        // 向页面传值
        writeDataToAjax(nodeArray);
        return NONE;
    }

    /**
     * 获得设备ONU PON端口列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadOnuPonPortList() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("start", start);
        params.put("limit", limit);
        List<OltOnuPonAttribute> onuPonAttributes = oltPerfGraphService.getOltOnuPonList(params);
        int totalCount = oltPerfGraphService.getOltOntPonCount(params);
        JSONArray json = new JSONArray();
        for (OltOnuPonAttribute onuPonAttribute : onuPonAttributes) {
            JSONObject o = new JSONObject();
            o.put("value", onuPonAttribute.getOnuPonIndex());
            o.put("text", formatOnuPortStr(EponIndex.getOnuPonIndex(onuPonAttribute.getOnuPonIndex())));
            json.add(o);
        }
        JSONObject onuPonJson = new JSONObject();
        onuPonJson.put("rowCount", totalCount);
        onuPonJson.put("data", json);
        // 向页面传值
        writeDataToAjax(onuPonJson);
        return NONE;
    }

    /**
     * 获得设备UNI端口列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadOnuUniPortList() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("start", start);
        params.put("limit", limit);
        List<OltUniAttribute> uniAttributes = oltPerfGraphService.getOltUniList(params);
        int totalCount = oltPerfGraphService.getOltUniCount(params);
        JSONArray json = new JSONArray();
        for (OltUniAttribute uniAttribute : uniAttributes) {
            JSONObject o = new JSONObject();
            o.put("value", uniAttribute.getUniIndex());
            o.put("text", formatOnuPortStr(EponIndex.getOnuPonIndex(uniAttribute.getUniIndex())));
            json.add(o);
        }
        JSONObject uniJson = new JSONObject();
        uniJson.put("rowCount", totalCount);
        uniJson.put("data", json);
        // 向页面传值
        writeDataToAjax(uniJson);
        return NONE;
    }

    /**
     * 获得光端口列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadFirberPortList() throws Exception {
        List<OltPortInfo> ePorts = oltPerfGraphService.queryEponPortListByEntityId(entityId);
        List<OltSlotAttribute> oltSlotList = oltSlotService.getOltSlotList(entityId);
        JSONArray nodeArray = new JSONArray();
        Map<Long, JSONObject> slotMap = new HashMap<Long, JSONObject>();
        int count = 0;
        for (OltPortInfo ePort : ePorts) {
            Long slotId = ePort.getSlotId();
            // 处理板卡
            if (!slotMap.containsKey(slotId)) {
                JSONObject slot = new JSONObject();
                slot.put("id", slotId);
                slot.put("pId", -1);
                for (OltSlotAttribute $slot : oltSlotList) {
                    if ($slot.getSlotId().equals(slotId)) {
                        slot.put("name",
                                BoardType.getName($slot.getTopSysBdPreConfigType()) + " : " + $slot.getSlotNo());
                    }
                }
                slot.put("open", true);
                slot.put("nocheck", true);
                slotMap.put(slotId, slot);
                // 将板卡加入到节点中
                nodeArray.add(slot);
            }
            JSONObject port = new JSONObject();
            port.put("id", ePort.getPortIndex());
            port.put("pId", slotId);
            port.put(
                    "name",
                    ePort.getDisplayName() != null ? ePort.getDisplayName() : EponIndex.getPortStringByIndex(
                            ePort.getPortIndex()).toString());
            port.put("open", true);
            if (ePort.getPortStatus() == EponConstants.PORT_STATUS_UP.intValue()) {
                port.put("online", true);
                port.put("checked", count++ < MAX_CHECKED_NUM);
                port.put("icon", "/images/fault/trap_on.png");
                port.put("nm3kTip", "LINK_UP");
            } else {
                port.put("online", false);
                port.put("icon", "/images/fault/trap_off.png");
                port.put("nm3kTip", "LINK_DOWN");
            }
            nodeArray.add(port);
        }
        // 向页面传值
        writeDataToAjax(nodeArray);
        return NONE;
    }

    /**
     * 获得设备服务质量性能指标数据列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadOltServicePerfData() throws Exception {
        JSONObject json = new JSONObject();
        List<Long> indexList = new ArrayList<Long>();
        String[] strs = indexs.split(",");
        for (int i = 0; i < strs.length; i++) {
            indexList.add(Long.valueOf(strs[i]));
        }
        for (Long index : indexList) {
            List<Point> points = oltPerfGraphService.queryOltServicePerfPoints(entityId, index, perfTarget, startTime,
                    endTime);
            points = PerfTargetUtil.samplePoints(points);

            JSONArray pointArray = new JSONArray();
            for (Point p : points) {
                JSONArray point = new JSONArray();
                point.add(p.getXTime().getTime());
                // 进行温度转换
                if (PerfTargetConstants.OLT_BOARDTEMP.equals(perfTarget)) {
                    Double tempValue = p.getY();
                    if (tempValue != null && tempValue != -1) {
                        point.add(UnitConfigConstant.translateTemperature(tempValue.intValue()));
                    } else {
                        point.add(p.getY());
                    }
                } else {
                    point.add(p.getY());
                }
                pointArray.add(point);
            }
            if (PerfTargetConstants.OLT_FANSPEED.equals(perfTarget)) {
                json.put(EponIndex.getSlotNo(index) - OltFanAttribute.STARTNO_INTEGER + 1, pointArray);
            } else {
                Long slotNo = oltSlotService.getSlotNoByIndex(index, entityId);
                json.put(slotNo, pointArray);
            }
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获得设备光链路质量性能指标数据列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadFirberPortPerfData() throws Exception {
        JSONObject json = new JSONObject();
        List<Long> indexList = new ArrayList<Long>();

        String[] strs = indexs.split(",");
        for (int i = 0; i < strs.length; i++) {
            indexList.add(Long.valueOf(strs[i]));
        }
        for (Long index : indexList) {
            List<Point> points = oltPerfGraphService.queryOltOptLinkPerfPoints(entityId, index, perfTarget, startTime,
                    endTime);
            points = PerfTargetUtil.samplePoints(points);
            JSONArray pointArray = new JSONArray();
            for (Point p : points) {
                JSONArray point = new JSONArray();
                point.add(p.getXTime().getTime());
                point.add(p.getY());
                pointArray.add(point);
            }
            OltSniAttribute sniAttr = oltSniService.getSniAttribute(entityId, index);
            if (sniAttr != null) {
                json.put(sniAttr.getSniDisplayName(), pointArray);
            } else {
                json.put(formatPortStr(EponIndex.getPortIndex(index)), pointArray);
            }
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获得设备端口流量性能数据列表
     * 
     * @return String
     * @throws Exception
     */
    public String loadPortFlowPerfData() throws Exception {
        JSONObject json = new JSONObject();
        List<Long> indexList = new ArrayList<Long>();
        String[] strs = indexs.split(",");
        for (int i = 0; i < strs.length; i++) {
            indexList.add(Long.valueOf(strs[i]));
        }
        for (Long index : indexList) {
            List<Point> points = oltPerfGraphService.queryPortFlowPerfPoints(entityId, index, perfTarget, startTime,
                    endTime, direction);
            points = PerfTargetUtil.samplePoints(points);
            JSONArray pointArray = new JSONArray();
            for (Point p : points) {
                JSONArray point = new JSONArray();
                point.add(p.getXTime().getTime());
                point.add(p.getY());
                pointArray.add(point);
            }
            if (perfTarget.equals(PerfTargetConstants.OLT_SNIFLOW)
                    || perfTarget.equals(PerfTargetConstants.OLT_SNIUSED)) {
                OltSniAttribute sniAttr = oltSniService.getSniAttribute(entityId, index);
                if (sniAttr != null) {
                    json.put(sniAttr.getSniDisplayName(), pointArray);
                } else {
                    json.put(formatPortStr(EponIndex.getPortIndex(index)), pointArray);
                }
            } else if (perfTarget.equals(PerfTargetConstants.OLT_PONFLOW)
                    || perfTarget.equals(PerfTargetConstants.OLT_PONUSED)) {
                json.put(formatPortStr(EponIndex.getPortIndex(index)), pointArray);
            } else if (perfTarget.equals(PerfTargetConstants.ONUPON_FLOW)
                    || perfTarget.equals(PerfTargetConstants.UNI_FLOW)) {
                json.put(formatOnuPortStr(EponIndex.getOnuPonIndex(index)), pointArray);
            }
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获得设备响应延时
     * 
     * @return String
     * @throws Exception
     */
    public String loadOltRelayPerfData() throws Exception {
        JSONObject json = new JSONObject();
        entity = entityService.getEntity(entityId);
        List<Point> points = oltPerfGraphService.queryOltRelayPerfPoints(entityId, startTime, endTime);
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
     * 格式化端口
     * 
     * @param originalStr
     * @return
     */
    private static String formatPortStr(String originalStr) {
        StringBuilder handledStr = new StringBuilder();
        // 取到槽位号和端口号
        String[] strs = originalStr.split(":");
        if (Integer.parseInt(strs[2], 16) > 16) {
            handledStr.append(Integer.parseInt(strs[1], 16)).append("/").append(Integer.parseInt(strs[2], 16) - 16);
        } else {
            handledStr.append(Integer.parseInt(strs[1], 16)).append("/").append(Integer.parseInt(strs[2], 16));
        }
        return handledStr.toString();
    }

    /**
     * 格式化ONU端口
     * 
     * @param originalStr
     * @return
     */
    private static String formatOnuPortStr(String originalStr) {
        StringBuilder handledStr = new StringBuilder();
        String[] strs = originalStr.split(":");
        handledStr.append(Integer.parseInt((strs[0]), 16)).append("/").append(Integer.parseInt((strs[1]), 16))
                .append("/").append(Integer.parseInt((strs[2]), 16)).append(":");
        if (strs[4].equals("ff")) {
            handledStr.append(1);
        } else {
            handledStr.append(Integer.parseInt((strs[4]), 16));
        }
        return handledStr.toString();
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String getGroupName() {
        return groupName;
    }

    @Override
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPerfTarget() {
        return perfTarget;
    }

    public void setPerfTarget(String perfTarget) {
        this.perfTarget = perfTarget;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getCameraSwitch() {
        return cameraSwitch;
    }

    public void setCameraSwitch(String cameraSwitch) {
        this.cameraSwitch = cameraSwitch;
    }

}
