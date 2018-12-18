package com.topvision.ems.network.action;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.fault.service.LevelManager;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.LinkSnap;
import com.topvision.ems.network.domain.TopoFolderEx;
import com.topvision.ems.network.domain.TopoLabel;
import com.topvision.ems.network.service.NetworkSnapManager;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("topologyStateAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TopologyStateAction extends BaseAction {
    private final ResourceManager faultRM = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
    private static final long serialVersionUID = -4606793574620114266L;
    private static Logger logger = LoggerFactory.getLogger(TopologyStateAction.class);
    @Autowired
    private AlertService alertService;
    @Autowired
    private TopologyService topologyService;
    private long folderId;
    private String folderPath;
    private String entityLabel = TopoLabel.TYPE_CPU;
    private String linkLabel = TopoLabel.TYPE_LINKFLOW;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 拓扑图打开的时候获取拓扑图的状态, 用于还原当前拓扑图中所有节点的状态.
     * 
     * @return
     * @throws Exception
     */
    public String getTopologyStateFirstly() throws Exception {
        JSONObject json = new JSONObject();
        JSONObject temp = null;
        NetworkSnapManager topologyState = NetworkSnapManager.getInstance();
        LevelManager levelManager = LevelManager.getInstance();
        List<Entity> entityIds = topologyService.getVertexByFolderId(folderId);
        List<Link> linkIds = topologyService.getEdgeByFolderId(folderId);
        List<TopoFolderEx> folders = alertService.getAlertLevelInChildFolder(folderPath);
        int size = (entityIds == null ? 0 : entityIds.size());
        JSONArray arr = new JSONArray();
        if (size > 0) {
            EntitySnap snap = null;
            long entityId = 0;
            double value = 0;
            boolean cpuLabelFlag = TopoLabel.TYPE_CPU.equals(entityLabel);
            List<TopoLabel> labels = topologyService.getTopoLabel(folderId, entityLabel);
            int labelSize = (labels == null ? 0 : labels.size());
            for (int i = 0; i < size; i++) {
                entityId = entityIds.get(i).getEntityId();
                snap = topologyState.getEntitySnap(entityId);
                if (snap == null) {
                    continue;
                }
                temp = new JSONObject();
                temp.put("id", "cell" + entityId);
                temp.put("entityId", entityId);
                if (snap.isState() != null && snap.isState()) {
                    temp.put("online", true);
                    // CPU和MEM负载显示
                    try {
                        value = (cpuLabelFlag ? snap.getCpu() : snap.getMem()) * 100;
                    } catch (Exception e) {
                        logger.debug("get Snap Information errer", e);
                    }
                    temp.put("value", NumberUtils.TWODOT_FORMAT.format(value));
                    if (value > 0) {
                        for (int j = 0; j < labelSize; j++) {
                            if (value > labels.get(j).getValue()) {
                                temp.put("backgroundColor", labels.get(j).getColor());
                                break;
                            }
                        }
                    }
                } else {
                    temp.put("online", false);
                }

                temp.put("alert", snap.getAlertLevel());
                temp.put("alertId", snap.getAlertId());
                temp.put("alertDesc", snap.getAlertDesc());
                if (snap.getAlertTime() != null) {
                    temp.put("alertTime", dateFormat.format(snap.getAlertTime()));
                }
                // temp.put("alt",
                // faultRM.getString(levelManager.getLevelName(snap.getAlertLevel())));
                arr.add(temp);
            }
        }
        json.put("entities", arr);
        if (logger.isDebugEnabled()) {
            logger.debug("StateFirstly:" + json);
        }
        arr = new JSONArray();
        size = linkIds == null ? 0 : linkIds.size();
        if (size > 0) {
            List<TopoLabel> labels = topologyService.getTopoLabel(folderId, linkLabel);
            int labelSize = labels == null ? 0 : labels.size();
            if (labelSize > 0) {
                LinkSnap snap = null;
                long linkId = 0;
                double value;
                TopoLabel threshold = null;
                boolean flowLabelFlag = TopoLabel.TYPE_LINKFLOW.equals(linkLabel);
                for (int i = 0; i < size; i++) {
                    linkId = linkIds.get(i).getLinkId();
                    snap = topologyState.getLinkSnapByLinkId(linkId);
                    if (snap == null) {
                        continue;
                    }
                    temp = new JSONObject();
                    temp.put("id", "edge" + linkId);
                    temp.put("linkId", linkId);
                    if (flowLabelFlag) {
                        // linkflow
                        if (snap.getUsage() > 0) {
                            value = 100 * snap.getUsage();
                            for (int j = 0; j < labelSize; j++) {
                                threshold = labels.get(j);
                                if (value > threshold.getValue()) {
                                    temp.put("strokeColor", threshold.getColor());
                                    break;
                                }
                            }
                            temp.put("linklabel", NumberUtils.getPercentStr(snap.getUsage(), 1));
                        } else {
                            temp.put("linklabel", "");
                        }
                    } else {
                        // link rate
                        value = snap.getRate();
                        if (value > 0) {
                            for (int j = 0; j < labelSize; j++) {
                                threshold = labels.get(j);
                                if (value > threshold.getValue()) {
                                    temp.put("strokeColor", threshold.getColor());
                                    break;
                                }
                            }
                            temp.put("linklabel", NumberUtils.getIfSpeedStr(value));
                        } else {
                            temp.put("linklabel", "");
                        }
                    }
                    arr.add(temp);
                }
            }
        }
        json.put("flows", arr);

        size = folders == null ? 0 : folders.size();
        arr = new JSONArray();
        if (size > 0) {
            TopoFolderEx folder = null;
            for (int i = 0; i < size; i++) {
                folder = folders.get(i);
                temp = new JSONObject();
                temp.put("id", "cell" + folder.getFolderId());
                temp.put("folderId", folder.getFolderId());
                temp.put("alert", folder.getAlertLevel());
                temp.put("alt", faultRM.getString(levelManager.getLevelName(folder.getAlertLevel())));
                arr.add(temp);
            }
        }
        json.put("folders", arr);

        writeDataToAjax(json);

        return NONE;
    }

    /**
     * 获取某个拓扑文件夹下设备和线路的最新状态, 只得到发生变化的节点状态.
     * 
     * @return
     * @throws Exception
     */
    public String getTopologyStateNewly() throws Exception {
        JSONObject json = new JSONObject();
        JSONObject temp = null;
        NetworkSnapManager topologyState = NetworkSnapManager.getInstance();
        LevelManager levelManager = LevelManager.getInstance();

        List<Entity> entityIds = topologyService.getVertexByFolderId(folderId);
        List<Link> linkIds = topologyService.getEdgeByFolderId(folderId);
        List<TopoFolderEx> folders = alertService.getAlertLevelInChildFolder(folderPath);

        int size = (entityIds == null ? 0 : entityIds.size());
        JSONArray arr = new JSONArray();
        if (size > 0) {
            EntitySnap snap = null;
            long entityId = 0;
            double value = 0;
            boolean cpuLabelFlag = TopoLabel.TYPE_CPU.equals(entityLabel);
            List<TopoLabel> labels = topologyService.getTopoLabel(folderId, entityLabel);
            int labelSize = (labels == null ? 0 : labels.size());
            for (int i = 0; i < size; i++) {
                entityId = entityIds.get(i).getEntityId();
                snap = topologyState.getEntitySnap(entityId);
                if (snap == null) {
                    continue;
                }
                temp = new JSONObject();
                temp.put("id", "cell" + entityId);
                temp.put("entityId", entityId);
                temp.put("status", snap.isStatus());

                if (snap.isState() != null && snap.isState()) {
                    temp.put("online", true);
                    // CPU和MEM负载显示
                    Double d = cpuLabelFlag ? snap.getCpu() : snap.getMem();
                    value = d != null ? d * 100 : 0;
                    temp.put("value", NumberUtils.TWODOT_FORMAT.format(value));
                    if (value > 0) {
                        for (int j = 0; j < labelSize; j++) {
                            if (value > labels.get(j).getValue()) {
                                temp.put("backgroundColor", labels.get(j).getColor());
                                break;
                            }
                        }
                    }
                } else {
                    temp.put("online", false);
                }
                temp.put("alertId", snap.getAlertId());
                temp.put("alert", snap.getAlertLevel());
                temp.put("alertDesc", snap.getAlertDesc());
                if (snap.getAlertTime() != null) {
                    temp.put("alertTime", dateFormat.format(snap.getAlertTime()));
                }
                // temp.put("alt",
                // faultRM.getString(levelManager.getLevelName(snap.getAlertLevel())));
                // temp.put("alt", levelManager.getLevelName(snap.getAlertLevel()));
                arr.add(temp);
            }
        }
        json.put("entities", arr);

        size = linkIds == null ? 0 : linkIds.size();
        arr = new JSONArray();
        if (size > 0) {
            List<TopoLabel> labels = topologyService.getTopoLabel(folderId, linkLabel);
            int labelSize = labels == null ? 0 : labels.size();
            if (labelSize > 0) {
                LinkSnap snap = null;
                long linkId = 0;
                double value;
                TopoLabel threshold = null;
                boolean flowLabelFlag = TopoLabel.TYPE_LINKFLOW.equals(linkLabel);
                for (int i = 0; i < size; i++) {
                    linkId = linkIds.get(i).getLinkId();
                    snap = topologyState.getLinkSnapByLinkId(linkId);
                    if (snap == null) {
                        continue;
                    }
                    temp = new JSONObject();
                    temp.put("id", "edge" + linkId);
                    temp.put("linkId", linkId);
                    if (flowLabelFlag) {
                        // linkflow
                        if (snap.getUsage() > 0) {
                            value = 100 * snap.getUsage();
                            for (int j = 0; j < labelSize; j++) {
                                threshold = labels.get(j);
                                if (value > threshold.getValue()) {
                                    temp.put("strokeColor", threshold.getColor());
                                    break;
                                }
                            }
                            temp.put("linklabel", NumberUtils.getPercentStr(snap.getUsage(), 1));
                        } else {
                            temp.put("linklabel", "");
                        }
                    } else {
                        // link rate
                        value = snap.getRate();
                        if (value > 0) {
                            for (int j = 0; j < labelSize; j++) {
                                threshold = labels.get(j);
                                if (value > threshold.getValue()) {
                                    temp.put("strokeColor", threshold.getColor());
                                    break;
                                }
                            }
                            temp.put("linklabel", NumberUtils.getIfSpeedStr(value));
                        } else {
                            temp.put("linklabel", "");
                        }
                    }
                    arr.add(temp);
                }
            }
        }
        json.put("flows", arr);

        size = folders == null ? 0 : folders.size();
        arr = new JSONArray();
        if (size > 0) {
            TopoFolderEx folder = null;
            for (int i = 0; i < size; i++) {
                folder = folders.get(i);
                temp = new JSONObject();
                temp.put("id", "cell" + folder.getFolderId());
                temp.put("folderId", folder.getFolderId());
                temp.put("alert", folder.getAlertLevel());
                temp.put("alt", levelManager.getLevelName((folder.getAlertLevel())));
                arr.add(temp);
            }
        }
        json.put("folders", arr);
        writeDataToAjax(json);

        return NONE;
    }

    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    public void setEntityLabel(String entityLabel) {
        this.entityLabel = entityLabel;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public void setLinkLabel(String linkLabel) {
        this.linkLabel = linkLabel;
    }

    public void setTopologyService(TopologyService topologyService) {
        this.topologyService = topologyService;
    }

    public String getEntityLabel() {
        return entityLabel;
    }

    public long getFolderId() {
        return folderId;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public String getLinkLabel() {
        return linkLabel;
    }
}
