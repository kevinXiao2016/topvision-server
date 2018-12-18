/***********************************************************************
 * $Id: CmTopoFolderAction.java,v1.0 2016年5月10日 上午10:12:32 $ * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.topofolder.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cm.topofolder.service.CmTopoFolderService;
import com.topvision.ems.cmc.ccmts.domain.TopoFolderDisplayInfo;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author YangYi
 * @created @2016年5月10日-上午10:12:32
 *
 */
@Controller("cmTopoFolderAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmTopoFolderAction extends BaseAction {
    private static final long serialVersionUID = 2752442627282678384L;
    @Autowired
    private CmTopoFolderService cmTopoFolderService;
    private Long folderId;

    /**
     * 根据folderId读取子地域
     * 
     * @return
     * @throws IOException
     */
    public String loadSubFolderById() throws IOException {
        JSONArray nodeArray = new JSONArray();
        if (folderId != CmcConstants.NO_AREA_FLAG) { // 无地域的情况下不需要查询子地域
            List<TopoFolderDisplayInfo> folderList = cmTopoFolderService.getTopoDisplayInfo(folderId); // 查询对应的子地域
            for (TopoFolderDisplayInfo topoFolder : folderList) { // 遍历地域，构建树的地域结点
                nodeArray.add(this.buildTreeNode(topoFolder, false, null));
            }
        }
        // nodeArray.write(response.getWriter());
        writeDataToAjax(nodeArray);
        return NONE;
    }

    /**
     * 获取树形图
     * 
     * @return
     * @throws IOException
     */
    public String loadTopoFolder() throws IOException {
        Long rootFolderId = CurrentRequest.getCurrentUser().getUser().getUserGroupId();
        TopoFolderDisplayInfo rootFolder = cmTopoFolderService.getRootFolderInfo(rootFolderId);
        List<TopoFolderDisplayInfo> topoFolderList = cmTopoFolderService.getTopoDisplayInfo(rootFolderId); // 只查询第一级地域
        JSONArray childrenNodeArray = new JSONArray();
        for (TopoFolderDisplayInfo topoFolder : topoFolderList) { // 遍历地域，构建树
            childrenNodeArray.add(this.buildTreeNode(topoFolder, false, null));
        }
        JSONObject rootNode = this.buildTreeNode(rootFolder, true, childrenNodeArray);
        JSONArray treeJSONArray = new JSONArray(); // 树数据
        treeJSONArray.add(rootNode);
        // treeJSONArray.write(response.getWriter());
        writeDataToAjax(treeJSONArray);
        return NONE;
    }

    /**
     * 读取CM列表
     * 
     * @return
     * @throws IOException
     */
    public String loadCmListByFolder() throws IOException {
        JSONObject json = new JSONObject();
        List<JSONObject> array = new ArrayList<JSONObject>();
        List<CmAttribute> list = cmTopoFolderService.loadCmListByFolder(folderId, start, limit, sort, dir);
        Long totalNum = cmTopoFolderService.loadCmListByFolderNum(folderId);
        for (CmAttribute cm : list) {
            try {
                JSONObject temp = new JSONObject();
                temp.put("displayIp", cm.getDisplayIp());
                temp.put("statusMacAddress", cm.getStatusMacAddress());
                temp.put("statusValue", cm.getStatusValue());
                temp.put("cmcName", cm.getCmcName());
                temp.put("cmAlias", cm.getCmAlias());
                temp.put("cmClassified", cm.getCmClassified());
                temp.put("cmcId", cm.getCmcId());
                temp.put("upChannelId", cm.getUpChannelId());
                temp.put("downChannelId", cm.getDownChannelId());
                temp.put("cmcDeviceStyle", cm.getCmcDeviceStyle());
                temp.put("cmcName", cm.getCmcName());
                temp.put("cmcIp", cm.getCmcIp());
                array.add(temp);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        json.put("rowCount", totalNum);
        json.put("data", array);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 打开CM列表
     * 
     * @return
     * @throws IOException
     */
    public String showTopoFolderCmList() throws IOException {
        return SUCCESS;
    }

    private JSONObject buildTreeNode(TopoFolderDisplayInfo topoFolder, boolean isRootNode, JSONArray childrenNodeArray) {
        JSONObject treeNode = new JSONObject();
        StringBuilder textBuilder = null;
        textBuilder = new StringBuilder();
        textBuilder.append(getI18NString(topoFolder.getName(), "resources")).append(" (")
                .append(getI18NString("IPTOPO.total", "cmc")).append(": ").append(topoFolder.getTotalNum())
                .append(", ").append(getI18NString("IPTOPO.online", "cmc")).append(": ")
                .append(topoFolder.getOnlineNum()).append(", ").append(getI18NString("IPTOPO.offline", "cmc"))
                .append(": ").append(topoFolder.getOffineNum()).append(")");
        treeNode.put("text", textBuilder.toString());
        treeNode.put("id", topoFolder.getFolderId());
        treeNode.put("folderId", topoFolder.getFolderId());
        treeNode.put("leaf", false);
        if (isRootNode) {
            treeNode.put("expanded", true);
            treeNode.put("children", childrenNodeArray);
        }
        return treeNode;
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

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

}
