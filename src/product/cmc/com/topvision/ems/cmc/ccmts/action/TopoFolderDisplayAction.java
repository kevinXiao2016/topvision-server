/***********************************************************************
 * $Id: IpSegmentDisplayAction.java,v1.0 2014-5-28-上午9:37:22 $
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

import com.topvision.ems.cmc.ccmts.domain.TopoFolderDisplayInfo;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.IpSegmentDisplayService;
import com.topvision.ems.cmc.cm.service.CcmtsCmListService;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.util.CurrentRequest;

/**
 * 
 * @author flack
 * @created @2014-5-28-上午9:37:22
 *
 */
@Controller("topoFolderDisplayAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TopoFolderDisplayAction extends BaseAction {
    private static final long serialVersionUID = 6456082715064772874L;
    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(TopoFolderDisplayAction.class);

    @Autowired
    private IpSegmentDisplayService ipSegmentDisplayService;
    @Autowired
    private CcmtsCmListService ccmtsCmListService;
    @Autowired
    private TopologyService topologyService;
    private Boolean sevenDay;
    private Long folderId;
    private Long cmcId;
    private Integer loadType;

    /**
     * 进入分地域展示页面
     */
    public String showTopoFolderPage() {
        folderId = CurrentRequest.getCurrentUser().getUser().getUserGroupId();
        return SUCCESS;
    }
    
    /**
     * 进入CM分地域展示页面
     */
    public String showCmView() {
        folderId = CurrentRequest.getCurrentUser().getUser().getUserGroupId();
        return SUCCESS;
    }

    /**
     * 加载所有的地域
     * @return
     * @throws IOException 
     */
    public String loadTopoFolder() throws IOException {
        Long currentFolderId = CurrentRequest.getCurrentUser().getUser().getUserGroupId();
        TopoFolderDisplayInfo currentFolder = ipSegmentDisplayService.getCurrentFolderInfo(currentFolderId);
        //查询对应的设备
        List<EntitySnap> entityList = ipSegmentDisplayService.getDeviceByFolderList(currentFolderId);
        //只查询第一级地域
        List<TopoFolderDisplayInfo> list = ipSegmentDisplayService.getTopoDisplayInfo(currentFolderId);
        JSONObject treeNode = null;
        JSONArray nodeArray = new JSONArray();
        StringBuilder textBuilder = null;
        //遍历地域，构建树
        for (TopoFolderDisplayInfo topoFolder : list) {
            textBuilder = new StringBuilder();
            textBuilder.append(getI18NString(topoFolder.getName(), "resources")).append(" (")
                    .append(getI18NString("IPTOPO.total", "cmc")).append(": ").append(topoFolder.getTotalNum())
                    .append(", ").append(getI18NString("IPTOPO.online", "cmc")).append(": ")
                    .append(topoFolder.getOnlineNum()).append(", ").append(getI18NString("IPTOPO.offline", "cmc"))
                    .append(": ").append(topoFolder.getOffineNum()).append(")");
            treeNode = new JSONObject();
            treeNode.put("text", textBuilder.toString());
            treeNode.put("id", topoFolder.getFolderId());
            treeNode.put("folderId", topoFolder.getFolderId());
            //treeNode.put("parentId", topoFolder.getSuperiorId());
            //treeNode.put("icon", "topoFolderIcon20");
            treeNode.put("leaf", false);
            treeNode.put("loadType", CmcConstants.LOAD_TYPE_FOLDER);
            nodeArray.add(treeNode);
        }
        // 处理为树设备结点数据
        JSONObject deviceJson = null;
        for (EntitySnap entity : entityList) {
            deviceJson = new JSONObject();
            textBuilder = new StringBuilder();
            textBuilder.append(entity.getMac()).append("[").append(entity.getIp()).append("]");
            if (entity.isState()) {
                textBuilder.append(" [").append(getI18NString("IPTOPO.online", "cmc")).append("]");
            } else {
                textBuilder.append(" [").append(getI18NString("IPTOPO.offline", "cmc")).append("]");
            }
            deviceJson.put("text", textBuilder.toString());
            deviceJson.put("name", entity.getName());
            //当设备在多地域下时,以entityId作为结点ID时存在问题,所以作以下处理
            deviceJson.put("id", currentFolder.getFolderId() + "_" + entity.getEntityId());
            deviceJson.put("folderId", currentFolder.getFolderId());
            deviceJson.put("deviceId", entity.getEntityId());
            deviceJson.put("typeId", entity.getTypeId());
            deviceJson.put("loadType", CmcConstants.LOAD_TYPE_CMC);
            deviceJson.put("icon", "/images/network/ccmts/ccmts_c_a_16.png");
            deviceJson.put("leaf", false);
            nodeArray.add(deviceJson);
        }
        // 加入拓扑管理,并将跟设为其子节点
        JSONObject topoManageNode = new JSONObject();
        textBuilder = new StringBuilder();
        textBuilder.append(getI18NString(currentFolder.getName(), "resources")).append(" (")
                .append(getI18NString("IPTOPO.total", "cmc")).append(": ").append(currentFolder.getTotalNum())
                .append(", ").append(getI18NString("IPTOPO.online", "cmc")).append(": ")
                .append(currentFolder.getOnlineNum()).append(", ").append(getI18NString("IPTOPO.offline", "cmc"))
                .append(": ").append(currentFolder.getOffineNum()).append(")");
        topoManageNode.put("text", textBuilder.toString());
        topoManageNode.put("expanded", true);
        topoManageNode.put("id", currentFolder.getFolderId());
        topoManageNode.put("folderId", currentFolder.getFolderId());
        //topoManageNode.put("icon", "folder");
        topoManageNode.put("leaf", false);
        topoManageNode.put("loadType", CmcConstants.LOAD_TYPE_FOLDER);
        topoManageNode.put("children", nodeArray);

        // 树数据
        JSONArray treeArray = new JSONArray();
        treeArray.add(topoManageNode);
        treeArray.write(response.getWriter());
        return NONE;
    }

    public String loadDeviceByFolderId() throws IOException {
        JSONArray nodeArray = new JSONArray();
        JSONObject deviceJson = null;
        StringBuilder textBuilder = null;
        if (loadType == CmcConstants.LOAD_TYPE_FOLDER) { //点击地域时加载该地域下设备以及子地域
            //无地域的情况下不需要查询子地域
            if (folderId != CmcConstants.NO_AREA_FLAG) {
                //查询对应的子地域
                //List<TopoFolder> folderList = topologyService.getChildTopoFolder(folderId);
                List<TopoFolderDisplayInfo> folderList = ipSegmentDisplayService.getTopoDisplayInfo(folderId);
                JSONObject treeNode = null;
                //遍历地域，构建树的地域结点
                for (TopoFolderDisplayInfo topoFolder : folderList) {
                    treeNode = new JSONObject();
                    textBuilder = new StringBuilder();
                    textBuilder.append(getI18NString(topoFolder.getName(), "resources")).append(" (")
                            .append(getI18NString("IPTOPO.total", "cmc")).append(": ").append(topoFolder.getTotalNum())
                            .append(", ").append(getI18NString("IPTOPO.online", "cmc")).append(": ")
                            .append(topoFolder.getOnlineNum()).append(", ")
                            .append(getI18NString("IPTOPO.offline", "cmc")).append(": ")
                            .append(topoFolder.getOffineNum()).append(")");
                    treeNode.put("text", textBuilder.toString());
                    treeNode.put("id", topoFolder.getFolderId());
                    treeNode.put("folderId", topoFolder.getFolderId());
                    //treeNode.put("parentId", topoFolder.getSuperiorId());
                    //treeNode.put("icon", "topoFolderIcon20");
                    treeNode.put("leaf", false);
                    treeNode.put("loadType", CmcConstants.LOAD_TYPE_FOLDER);
                    nodeArray.add(treeNode);
                }
            }
            //查询对应的设备
            List<EntitySnap> entityList = ipSegmentDisplayService.getDeviceByFolderList(folderId);
            // 处理为树设备结点数据
            for (EntitySnap entity : entityList) {
                deviceJson = new JSONObject();
                textBuilder = new StringBuilder();
                textBuilder.append(entity.getMac()).append("[").append(entity.getIp()).append("]");
                if (entity.isState()) {
                    textBuilder.append(" [").append(getI18NString("IPTOPO.online", "cmc")).append("]");
                } else {
                    textBuilder.append(" [").append(getI18NString("IPTOPO.offline", "cmc")).append("]");
                }
                deviceJson.put("text", textBuilder.toString());
                deviceJson.put("name", entity.getName());
                //当设备在多地域下时,以entityId作为结点ID时存在问题,所以作以下处理
                deviceJson.put("id", folderId + "_" + entity.getEntityId());
                deviceJson.put("folderId", folderId);
                deviceJson.put("deviceId", entity.getEntityId());
                deviceJson.put("typeId", entity.getTypeId());
                deviceJson.put("loadType", CmcConstants.LOAD_TYPE_CMC);
                deviceJson.put("icon", "/images/network/ccmts/ccmts_c_a_16.png");
                deviceJson.put("leaf", false);
                nodeArray.add(deviceJson);
            }
        } else if (loadType == CmcConstants.LOAD_TYPE_CMC) { //点击CCMTS设备,加载设备下的CM
            //加载CM
            List<CmAttribute> cmList = ccmtsCmListService.getCmListByCmcId(cmcId);
            // 处理为树结点数据
            for (CmAttribute cm : cmList) {
                deviceJson = new JSONObject();
                textBuilder = new StringBuilder();
                textBuilder.append(cm.getStatusMacAddress()).append("[").append(cm.getStatusInetAddress()).append("]");
                if (cm.isCmOnline()) {
                    textBuilder.append(" [").append(getI18NString("IPTOPO.online", "cmc")).append("]");
                } else {
                    textBuilder.append(" [").append(getI18NString("IPTOPO.offline", "cmc")).append("]");
                }
                deviceJson.put("text", textBuilder.toString());
                //当设备在多地域下时,以entityId作为结点ID时存在问题,所以作以下处理
                deviceJson.put("id", folderId + "_" + cm.getCmId());
                deviceJson.put("deviceId", cm.getCmId());
                deviceJson.put("mac", cm.getStatusMacAddress());
                deviceJson.put("loadType", CmcConstants.LOAD_TYPE_CM);
                deviceJson.put("icon", "/images/network/onu/8624Icon_16.png");
                deviceJson.put("leaf", true);
                nodeArray.add(deviceJson);
            }
        }
        nodeArray.write(response.getWriter());
        return NONE;
    }

    /**
     * 显示地域下的cmc列表页面
     * @return
     */
    public String showTopoFolderCmcList() {
        return SUCCESS;
    }

    /**
     * 加载地域对应的设备列表
     * 
     * @return
     * @throws IOException
     */
    public String loadDeviceListByFolder() throws IOException {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        Long tempTime = System.currentTimeMillis();
        JSONObject json = new JSONObject();
        List<JSONObject> array = new ArrayList<JSONObject>();
        List<CmcAttribute> list = ipSegmentDisplayService.getDeviceListByFolder(folderId, start, limit, sevenDay);
        int totalNum = ipSegmentDisplayService.getDeviceListNumByFolder(folderId, start, limit, sevenDay);
        for (CmcAttribute cmc : list) {
            JSONObject temp = new JSONObject();
            Long dt = 0L;
            if (cmc.getDt() != null) {
                dt = cmc.getDt().getTime();
            }
            temp.put("dt", dt);
            temp.put("entityId", cmc.getCmcId());
            temp.put("name", cmc.getManageName());
            temp.put("type", cmc.getCmcType());
            temp.put("ip", cmc.getIpAddress());
            temp.put("mac", cmc.getMacAddr());
            temp.put("state", cmc.isState());
            temp.put("uplinkDevice", cmc.getUplinkDevice());
            temp.put("folderName", getDeviceTopoFolderName(cmc.getCmcId()));
            temp.put("createTime", DateUtils.format(cmc.getCreateTime()));
            if (cmc.getTopCcmtsSysUpTime() != null) {
                temp.put("lastTime",
                        DateUtils.getTimeDesInObscure(cmc.getTopCcmtsSysUpTime() * 10, uc.getUser().getLanguage()));
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
     * 显示cmc下的cm列表页面
     * @return
     */
    public String showCmcSubCmList() {
        return SUCCESS;
    }

    private static String getI18NString(String key, String module) {
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

    public Boolean getSevenDay() {
        return sevenDay;
    }

    public void setSevenDay(Boolean sevenDay) {
        this.sevenDay = sevenDay;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getLoadType() {
        return loadType;
    }

    public void setLoadType(Integer loadType) {
        this.loadType = loadType;
    }

}
