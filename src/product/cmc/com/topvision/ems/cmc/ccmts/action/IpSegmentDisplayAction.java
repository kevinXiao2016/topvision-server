/***********************************************************************
 * $Id: IpSegmentDisplayAction.java,v1.0 2014-5-10 下午4:03:33 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.ccmts.service.IpSegmentDisplayService;
import com.topvision.ems.cmc.cm.service.CcmtsCmListService;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.FolderCategory;
import com.topvision.ems.network.domain.IpSegmentInfo;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;

/**
 * @author flack
 * @created @2014-5-10-下午4:03:33
 */
@Controller("ipSegmentDisplayAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IpSegmentDisplayAction extends BaseAction {
    private static final long serialVersionUID = 6456082715064772874L;
    private static Logger logger = LoggerFactory.getLogger(IpSegmentDisplayAction.class);
    @Autowired
    private TopologyService topologyService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private IpSegmentDisplayService ipSegmentDisplayService;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private CmcPerfService cmcPerfService;
    @Autowired
    private CcmtsCmListService ccmtsCmListService;

    // 指定IP段
    private String ipSegment;
    private List<Long> cmcIds;
    private String cmcIdList;
    private Integer folderId;
    private JSONArray folderList = new JSONArray();
    private Boolean sevenDay;
    private Long cmcId;
    private String deviceName;
    private Integer loadType;
    private String cmcMac;
    private Long entityId;
    private Integer deviceType;

    /**
     * 进入IP段展示页面
     * 
     * @return
     */
    public String showIpSegmentPage() {
        return SUCCESS;
    }

    /**
     * 加载IP段并构造树结构
     * 
     * @return
     * @throws Exco
     */
    public String loadIpSegmentTree() throws Exception {
        JSONArray jsonArray = new JSONArray();
        JSONObject json = null;
        // 处理Ip段的设备统计信息
        List<IpSegmentInfo> deviceInfoList = ipSegmentDisplayService.getIpSegmentDeviceInfoList();
        StringBuilder textBuilder = null;
        for (IpSegmentInfo segmentInfo : deviceInfoList) {
            json = JSONObject.fromObject(segmentInfo);
            textBuilder = new StringBuilder();
            textBuilder.append(" (").append(getI18NString("IPTOPO.total", "cmc")).append(": ")
                    .append(segmentInfo.getTotalNum()).append(", ").append(getI18NString("IPTOPO.online", "cmc"))
                    .append(": ").append(segmentInfo.getOnlineNum()).append(", ")
                    .append(getI18NString("IPTOPO.offline", "cmc")).append(": ").append(segmentInfo.getOffineNum())
                    .append(")");
            json.put("countInfo", textBuilder.toString());
            json.put("text", segmentInfo.getIpSegment() + textBuilder.toString());
            json.put("id", segmentInfo.getIpSegment());
            json.put("ipSegment", segmentInfo.getIpSegment());
            json.put("loadType", IpSegmentInfo.LOAD_CMC_FLAG);
            json.put("leaf", false);
            jsonArray.add(json);
        }
        // 根结点的处理
        IpSegmentInfo rootDeviceInfo = ipSegmentDisplayService.getRootDeviceInfo();
        JSONObject treeJson = new JSONObject();
        treeJson.put("ipSegment", -1);
        treeJson.put("id", -1);
        treeJson.put("loadType", IpSegmentInfo.LOAD_CMC_FLAG);
        treeJson.put("children", jsonArray);
        treeJson.put("expanded", true);
        treeJson.put("leaf", false);
        textBuilder = new StringBuilder();
        textBuilder.append(getI18NString("IPTOPO.treeRoot", "cmc")).append(" (")
                .append(getI18NString("IPTOPO.total", "cmc")).append(": ").append(rootDeviceInfo.getTotalNum())
                .append(", ").append(getI18NString("IPTOPO.online", "cmc")).append(": ")
                .append(rootDeviceInfo.getOnlineNum()).append(", ").append(getI18NString("IPTOPO.offline", "cmc"))
                .append(": ").append(rootDeviceInfo.getOffineNum()).append(")");
        treeJson.put("text", textBuilder.toString());
        // 树数据
        JSONArray treeArray = new JSONArray();
        treeArray.add(treeJson);
        treeArray.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载IP段对应树节点设备列表
     * 
     * @return
     * @throws Exception
     */
    public String loadTreeRootByIpSegment() throws Exception {
        JSONArray nodeArray = new JSONArray();
        JSONObject json = null;
        StringBuilder textBuilder = null;
        if (loadType == IpSegmentInfo.LOAD_CMC_FLAG) {
            // 根据指定的IP段获取设备列表
            List<EntitySnap> entityList = ipSegmentDisplayService.getDeviceInfoByIpSegment(ipSegment);
            // 处理为树结点数据
            for (EntitySnap entity : entityList) {
                json = new JSONObject();
                textBuilder = new StringBuilder();
                textBuilder.append(entity.getMac()).append("[").append(entity.getIp()).append("]");
                if (entity.isState()) {
                    textBuilder.append(" [").append(getI18NString("IPTOPO.online", "cmc")).append("]");
                } else {
                    textBuilder.append(" [").append(getI18NString("IPTOPO.offline", "cmc")).append("]");
                }
                json.put("id", ipSegment + "_" + entity.getEntityId());
                json.put("ipSegment", ipSegment);
                json.put("text", textBuilder.toString());
                json.put("cmcMac", entity.getMac());
                json.put("name", entity.getName());
                json.put("deviceId", entity.getEntityId());
                json.put("typeId", entity.getTypeId());
                json.put("loadType", IpSegmentInfo.LOAD_CM_FLAG);
                json.put("icon", "/images/network/ccmts/ccmts_c_a_16.png");
                json.put("leaf", false);
                nodeArray.add(json);
            }
            nodeArray.write(response.getWriter());
        } else if (loadType == IpSegmentInfo.LOAD_CM_FLAG) {
            //加载CM
            List<CmAttribute> cmList = ccmtsCmListService.getCmListByCmcId(cmcId);
            // 处理为树结点数据
            for (CmAttribute cm : cmList) {
                json = new JSONObject();
                textBuilder = new StringBuilder();
                textBuilder.append(cm.getStatusMacAddress()).append("[").append(cm.getStatusInetAddress()).append("]");
                if (cm.isCmOnline()) {
                    textBuilder.append(" [").append(getI18NString("IPTOPO.online", "cmc")).append("]");
                } else {
                    textBuilder.append(" [").append(getI18NString("IPTOPO.offline", "cmc")).append("]");
                }
                json.put("text", textBuilder.toString());
                json.put("id", ipSegment + "_" + cm.getCmId());
                json.put("deviceId", cm.getCmId());
                json.put("mac", cm.getStatusMacAddress());
                json.put("loadType", CmcConstants.LOAD_TYPE_CM);
                json.put("icon", "/images/network/onu/8624Icon_16.png");
                json.put("leaf", true);
                nodeArray.add(json);
            }
            nodeArray.write(response.getWriter());
        }
        return NONE;
    }

    /**
     * 显示Ip段对应的设备列表展示页面
     * 点击IP段时进入页面
     * @return
     */
    public String showIpSegmentDeviceList() {
        return SUCCESS;
    }

    /**
     * 加载IP段对应的设备列表
     * 
     * @return
     * @throws IOException
     */
    public String loadDeviceListByIpSegment() throws IOException {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        Long tempTime = System.currentTimeMillis();
        JSONObject json = new JSONObject();
        List<JSONObject> array = new ArrayList<JSONObject>();
        List<CmcAttribute> list = ipSegmentDisplayService.getDeviceListByIpSegment(ipSegment, start, limit, sevenDay);
        int totalNum = ipSegmentDisplayService.getDeviceListNum(ipSegment, start, limit, sevenDay);
        for (CmcAttribute cmc : list) {
            JSONObject temp = new JSONObject();
            Long dt = 0L;
            if (cmc.getDt() != null) {
                dt = cmc.getDt().getTime();
            }
            temp.put("dt", dt);
            temp.put("entityId", cmc.getCmcId());
            temp.put("name", cmc.getNmName());
            temp.put("type", cmc.getCmcType());
            temp.put("ip", cmc.getIpAddress());
            temp.put("mac", cmc.getMacAddr());
            temp.put("typeId", cmc.getTypeId());
            temp.put("state", cmc.isState());
            temp.put("uplinkDevice", cmc.getUplinkDevice());
            temp.put("folderName", getDeviceTopoFolderName(cmc.getCmcId()));
            temp.put("createTime", DateUtils.format(cmc.getCreateTime()));
            if (cmc.getTopCcmtsSysUpTime() != null) {
                temp.put("lastTime", DateUtils.getTimeDesInObscure(cmc.getTopCcmtsSysUpTime() * 10 + (tempTime - dt),
                        uc.getUser().getLanguage()));
                temp.put("sysUpTime", (cmc.getTopCcmtsSysUpTime() * 10 + (tempTime - dt)) / 10);
            }
            List<CmAttribute> cmList = ccmtsCmListService.getCmListByCmcId(cmc.getCmcId());
            temp.put("totalNum", cmList.size());
            int cmNumOnline = 0;
            for (CmAttribute cmAttr : cmList) {
                if (cmAttr.isCmOnline()) {
                    cmNumOnline++;
                }
            }
            temp.put("onlineNum", cmNumOnline);
            array.add(temp);
        }
        json.put("rowCount", totalNum);
        json.put("data", array);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 显示CMC设备对应的CM设备列表展示页面
     * 点击CMC设备时进入页面
     * @return
     */
    public String showCmDeviceList() {
        return SUCCESS;
    }

    /**
     * 加载CMC对应的Cm设备
     * @return
     */
    public String loadCmDeviceList() {
        return NONE;
    }

    /**
     * 批量重启CMC
     * 
     * @return
     * @throws IOException
     */
    public String batchRestartCmc() throws IOException {
        String result = "";
        String[] cmcIds = cmcIdList.split(",");
        for (int i = 0; i < cmcIds.length; i++) {
            EntitySnap snap = entityService.getEntitySnapById(Long.valueOf(cmcIds[i]));
            if (!snap.isState()) {// 关闭了的CC显示重启失败
                continue;
            } else {
                try {
                    cmcService.resetCmcWithoutAgent(snap.getEntityId());
                    snap.setState(false);
                    entityService.updateEntitySnap(snap);
                    cmcPerfService.changeCmc8800BStatus(snap.getEntityId(), false);
                    cmcPerfService.changeCmStatusOffine(snap.getEntityId());
                    result = "success";
                } catch (Exception e) {
                    result = result.concat(snap.getName());
                }
            }
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 批量修改CMC所属地域
     * 
     * @return
     * @throws IOException
     */
    public String showModifyTopFolder() throws IOException {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        List<TopoFolder> list = topologyService.loadMyTopoFolder(uc.getUserId(), FolderCategory.CLASS_NETWORK);
        for (TopoFolder folder : list) {
            JSONObject json = new JSONObject();
            json.put("id", folder.getFolderId());
            json.put("name", getI18NString(folder.getName(), "resources"));
            folderList.add(json);
        }
        return SUCCESS;
    }

    /**
     * 批量修改CMC所属地域
     * 
     * @return
     * @throws IOException
     */
    public String batchModifyTopFolder() throws IOException {
        String result = "";
        try {
            String[] cmcIds = cmcIdList.split(",");
            for (int i = 0; i < cmcIds.length; i++) {
                topologyService.updateEntityTopoFolder(Long.valueOf(cmcIds[i]), folderId);
            }
            result = "success";
        } catch (Exception e) {
            logger.info("update folder fail {}", e);
            result = "false";
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 显示设备编辑页面
     * @return
     */
    public String showEditDevice() {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        List<TopoFolder> list = topologyService.loadMyTopoFolder(uc.getUserId(), FolderCategory.CLASS_NETWORK);
        JSONObject json = null;
        for (TopoFolder folder : list) {
            json = new JSONObject();
            json.put("id", folder.getFolderId());
            json.put("name", getI18NString(folder.getName(), "resources"));
            folderList.add(json);
        }
        return SUCCESS;
    }

    /**
     * 修改设备信息
     * @return
     */
    public String modifyDeviceInfo() {
        ipSegmentDisplayService.updateDeviceInfo(cmcId, folderId, deviceName);
        return NONE;
    }

    public static String getI18NString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    private String getDeviceTopoFolderName(Long entityId) {
        List<TopoFolder> folderList = topologyService.getTopoFolderByEntityId(entityId);
        if (folderList == null || folderList.isEmpty()) {
            //设备不在任何地域下
            return getI18NString("WorkBench.topology0", "resources");
        } else {
            StringBuilder nameBulider = new StringBuilder();
            for (TopoFolder folder : folderList) {
                nameBulider.append(",").append(getI18NString(folder.getName(), "resources"));
            }
            return nameBulider.substring(1);
        }
    }

    public String getIpSegment() {
        return ipSegment;
    }

    public void setIpSegment(String ipSegment) {
        this.ipSegment = ipSegment;
    }

    public List<Long> getCmcIds() {
        return cmcIds;
    }

    public void setCmcIds(List<Long> cmcIds) {
        this.cmcIds = cmcIds;
    }

    public JSONArray getFolderList() {
        return folderList;
    }

    public void setFolderList(JSONArray folderList) {
        this.folderList = folderList;
    }

    public String getCmcIdList() {
        return cmcIdList;
    }

    public void setCmcIdList(String cmcIdList) {
        this.cmcIdList = cmcIdList;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Boolean getSevenDay() {
        return sevenDay;
    }

    public void setSevenDay(Boolean sevenDay) {
        this.sevenDay = sevenDay;
    }

    public Integer getLoadType() {
        return loadType;
    }

    public void setLoadType(Integer loadType) {
        this.loadType = loadType;
    }

    public String getCmcMac() {
        return cmcMac;
    }

    public void setCmcMac(String cmcMac) {
        this.cmcMac = cmcMac;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

}
