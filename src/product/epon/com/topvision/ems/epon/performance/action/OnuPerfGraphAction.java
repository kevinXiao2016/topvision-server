/***********************************************************************
 * $Id: OnuPerfGraphAction.java,v1.0 2015-4-22 下午5:33:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.action;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.performance.service.OnuPerfService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onu.service.GponOnuService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.action.PerfGraphAction;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.ems.performance.service.PerfTargetService;
import com.topvision.ems.performance.utils.PerfTargetUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author flack
 * @created @2015-4-22-下午5:33:47
 *
 */
@Controller("onuPerfGraphAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuPerfGraphAction extends PerfGraphAction {
    private static final long serialVersionUID = 1833968757752509732L;
    // 与其它性能展示相同,获取性能数据时统一使用entityId传递数据
    private Long entityId;
    private Long onuId;
    private String groupName;
    private Entity entity;
    private String perfTarget;
    // 标识入方向还是出方向
    private Integer direction;
    // gpon能力集
    private GponOnuCapability gponOnuCapability;
    // 流量经过计算后保留两位小数
    private static DecimalFormat format = new DecimalFormat("#.00");

    @Autowired
    private PerfTargetService perfTargetService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuPerfService onuPerfService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private GponOnuService gponOnuService;

    /**
     * 显示Onu性能展示页面
     * 
     * @return
     */
    public String showOnuPerfView() {
        return SUCCESS;
    }

    /**
     * 加载指定性能指标组中的指标
     * 
     * @return
     * @throws IOException
     */
    public String loadOnuPerfTargetGroups() throws IOException {
        JSONArray array = new JSONArray();
        List<String> groups = perfTargetService.getOnuPerfTargetGroupsByDeviceType(entityTypeService.getOnuType());
        for (String group : groups) {
            // 在线状态不用展示
            if (!PerfTargetConstants.ONU_DEVICESTATUS.equals(group)
                    && !PerfTargetConstants.ONU_CATV_TARGET.equals(group)) {
                JSONObject o = new JSONObject();
                o.put("value", group);
                o.put("text", PerfTargetUtil.getString("Performance." + group, "performance"));
                array.add(o);
            }
        }
        writeDataToAjax(array);
        return NONE;
    }

    /**
     * 加载指定Onu的性能指标数据
     * 
     * @return
     * @throws IOException
     */
    public String loadOnuTargetsByGroup() throws IOException {
        JSONArray array = new JSONArray();
        List<String> targets = perfTargetService.getOnuPerfTargetNamesByGroup(groupName,
                entityTypeService.getOnuType());
        JSONObject targetJson = null;
        for (String target : targets) {
            // 速率指标单独处理
            if (PerfTargetConstants.ONU_PORTFLOW.equals(target)) {
                List<String> flowTargets = Arrays.asList(PerfTargetConstants.ONU_FLOW_PERFTARGETS);
                for (String subTarget : flowTargets) {
                    targetJson = new JSONObject();
                    targetJson.put("value", subTarget);
                    targetJson.put("text", PerfTargetUtil.getString("Performance." + subTarget, "performance"));
                    array.add(targetJson);
                }
            } else {
                targetJson = new JSONObject();
                targetJson.put("value", target);
                targetJson.put("text", PerfTargetUtil.getString("Performance." + target, "performance"));
                array.add(targetJson);
            }
        }
        writeDataToAjax(array);
        return NONE;
    }

    public String loadOnuOptSubPerfTargets() throws IOException {
        JSONArray array = new JSONArray();
        List<String> subTargets = new ArrayList<String>();
        OltOnuAttribute onuAttribute = onuService.getOnuAttribute(onuId);
        if (onuAttribute.getOnuEorG().equals("G")) {
            gponOnuCapability = gponOnuService.queryForGponOnuCapability(onuId);
            if (gponOnuCapability.getOnuTotalCatvNum() != null && gponOnuCapability.getOnuTotalCatvNum() > 0) {
                subTargets = Arrays.asList(PerfTargetConstants.ONU_OPTLINK_PERFTARGETSWithCATV);
            } else {
                subTargets = Arrays.asList(PerfTargetConstants.ONU_OPTLINK_PERFTARGETS);
            }
        } else {
            if (onuAttribute.getCatvCapability() != null) {
                if (onuAttribute.getCatvCapability() == 1) {
                    subTargets = Arrays.asList(PerfTargetConstants.ONU_OPTLINK_PERFTARGETSWithCATV);
                } else {
                    subTargets = Arrays.asList(PerfTargetConstants.ONU_OPTLINK_PERFTARGETS);
                }
            } else {
                subTargets = Arrays.asList(PerfTargetConstants.ONU_OPTLINK_PERFTARGETS);
            }
        }
        for (String subTarget : subTargets) {
            //EMS-15587 gpon不支持PON口基于LLID接收功率
            if ("G".equalsIgnoreCase(onuAttribute.getOnuEorG()) && "oltPonRePower".equalsIgnoreCase(subTarget)) {
                continue;
            }
            JSONObject o = new JSONObject();
            o.put("value", subTarget);
            o.put("text", PerfTargetUtil.getString("Performance." + subTarget, "performance"));
            array.add(o);
        }
        writeDataToAjax(array);
        return NONE;
    }

    /**
     * 加载指定指标的性能数据
     * 
     * @return
     * @throws IOException
     */
    public String loadOnuOptPerfData() throws IOException {
        JSONObject json = new JSONObject();
        entity = entityService.getEntity(entityId);
        List<Point> points = onuPerfService.queryOnuOptLinkPerfPoints(entityId, perfTarget, startTime, endTime);
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
     * 加载ONU PON端口
     * 
     * @return
     * @throws IOException
     */
    public String loadOnuPonList() throws IOException {
        List<Long> ponList = onuPerfService.getOnuPonIndexList(onuId);
        JSONArray ponArray = new JSONArray();
        JSONObject ponJson = null;
        for (Long ponIndex : ponList) {
            ponJson = new JSONObject();
            ponJson.put("value", ponIndex);
            ponJson.put("text", formatOnuPortStr(EponIndex.getOnuPonIndex(ponIndex)));
            ponArray.add(ponJson);
        }
        ponArray.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载ONU UNI端口
     * 
     * @return
     * @throws IOException
     */
    public String loadOnuUniList() throws IOException {
        List<Long> uniList = onuPerfService.getOnuUniIndexList(onuId);
        JSONArray uniArray = new JSONArray();
        JSONObject uniJson = null;
        for (Long uniIndex : uniList) {
            uniJson = new JSONObject();
            uniJson.put("value", uniIndex);
            uniJson.put("text", formatOnuPortStr(EponIndex.getOnuPonIndex(uniIndex)));
            uniArray.add(uniJson);
        }
        uniArray.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载onu流量性能数据
     * 
     * @return
     * @throws IOException
     */
    public String loadOnuFlowPerfData() throws IOException {
        List<Long> indexList = new ArrayList<Long>();
        if (indexs != null && !"".equals(indexs)) {
            String[] strs = indexs.split(",");
            for (int i = 0; i < strs.length; i++) {
                indexList.add(Long.valueOf(strs[i]));
            }
        }
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("onuId", entityId);
        paraMap.put("direction", direction);
        paraMap.put("startTime", startTime);
        paraMap.put("endTime", endTime);
        JSONObject flowJson = new JSONObject();
        JSONArray pointArray = null;
        List<Point> points = null;
        JSONArray point = null;
        for (Long portIndex : indexList) {
            paraMap.put("portIndex", portIndex);
            points = onuPerfService.getOnuFlowPerfPoints(paraMap);
            points = PerfTargetUtil.samplePoints(points);
            pointArray = new JSONArray();
            for (Point p : points) {
                point = new JSONArray();
                point.add(p.getXTime().getTime());
                // point.add(Float.parseFloat(format.format(p.getY() / 8 / 1000)));
                point.add(p.getY());
                pointArray.add(point);
            }
            flowJson.put(formatOnuPortStr(EponIndex.getOnuPonIndex(portIndex)), pointArray);
        }
        flowJson.write(response.getWriter());
        return NONE;
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
        if (strs[4].toLowerCase().equals("ff")) {
            handledStr.append(1);
        } else {
            handledStr.append(Integer.parseInt((strs[4]), 16));
        }
        return handledStr.toString();
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getPerfTarget() {
        return perfTarget;
    }

    public void setPerfTarget(String perfTarget) {
        this.perfTarget = perfTarget;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public GponOnuCapability getGponOnuCapability() {
        return gponOnuCapability;
    }

    public void setGponOnuCapability(GponOnuCapability gponOnuCapability) {
        this.gponOnuCapability = gponOnuCapability;
    }

}
