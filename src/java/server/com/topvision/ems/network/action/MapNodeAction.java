package com.topvision.ems.network.action;

import java.io.Writer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.LinkEx;
import com.topvision.ems.network.domain.LinkSnap;
import com.topvision.ems.network.domain.MapNode;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.domain.TopoLabel;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.LinkService;
import com.topvision.ems.network.service.NetworkConstants;
import com.topvision.ems.network.service.NetworkSnapManager;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;

import net.sf.json.JSONObject;

@Controller("mapNodeAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MapNodeAction extends BaseAction {
    private static final long serialVersionUID = -9051892996078132452L;
    private static Logger logger = LoggerFactory.getLogger(MapNodeAction.class);
    private MapNode mapNode;
    private int userObjType;
    private Long userObjId;
    private Long nodeId;
    private String text;
    private List<Long> nodeIds;
    private long folderId;
    private long groupId;
    private final NumberFormat PERCENT_FORMAT = NumberFormat.getPercentInstance();
    @Autowired

    private LinkService linkService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private TopologyService topologyService;
    @Autowired
    private EntityTypeService entityTypeService;
    private boolean fixed;

    /**
     * 返回结点信息.
     * 
     * @return
     * @throws Exception
     */
    public String loadNodeTip() throws Exception {
        if (userObjType == NetworkConstants.TYPE_ENTITY) {
            return loadEntityTip();
        } else if (userObjType == NetworkConstants.TYPE_FOLDER) {
            return loadFolderTip();
        } else if (userObjType == NetworkConstants.TYPE_LINK) {
            return loadLinkTip();
        }

        return NONE;
    }

    /**
     * 返回拓扑文件夹信息.
     * 
     * @return
     * @throws Exception
     */
    public String loadFolderTip() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        writer.append("<div style=\"padding:0 10px 5px 10px;\"><table cellspacing=2 cellpadding=0>");
        writer.append("<tr><td width=70px>");
        TopoFolder topoFolder = topologyService.getTopoFolder(userObjId);
        writer.append(getResourceManager().getString("label.type"));
        writer.append("</td><td>");
        writer.append(getResourceManager().getString("folder.type" + topoFolder.getType()));
        writer.append("</td></tr><tr><td>");
        writer.append(getResourceManager().getString("label.name"));
        writer.append("</td><td>");
        writer.append(topoFolder.getName());
        writer.append("</td></tr><tr><td>");
        if (topoFolder.getType() == TopoFolder.TYPE_SUBNET) {
            writer.append(getResourceManager().getString("subnet.ip"));
            writer.append("</td><td>");
            writer.append(topoFolder.getSubnetIp() == null ? "" : topoFolder.getSubnetIp());
            writer.append("</td></tr><tr><td>");
            writer.append(getResourceManager().getString("subnet.mask"));
            writer.append("</td><td>");
            writer.append(topoFolder.getSubnetMask() == null ? "" : topoFolder.getSubnetMask());
            writer.append("</td></tr><tr><td>");
        }
        writer.append("URL:");
        writer.append("</td><td>");
        writer.append(topoFolder.getUrl() == null ? "" : topoFolder.getUrl());
        writer.append("</td></tr></table></div>");
        writer.flush();

        return NONE;
    }

    /**
     * 返回设备信息.
     * 
     * @return
     * @throws Exception
     */
    public String loadEntityTip() throws Exception {
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        if (userObjId == 0) {
            return NONE;
        }
        Writer writer = ServletActionContext.getResponse().getWriter();
        writer.append("<div style=\"padding:0 10px 5px 10px;\"><table cellspacing=2 cellpadding=0>");
        writer.append("<tr><td width=100px>");
        try {
            Entity entity = entityService.getEntity(userObjId);
            writer.write(getResourceManager().getString("label.entityName"));
            writer.append("</td><td>");
            writer.write(entity.getName() == null ? "" : entity.getName());
            writer.append("</td></tr><tr><td>");
            writer.write(getResourceManager().getString("label.typeName"));
            writer.append("</td><td>");
            writer.write(entity.getTypeName());
            if (entity.getModelName() != null) {
                writer.append(" - ");
                writer.write(entity.getModelName());
            }
            writer.append("</td></tr>");
            if (entity.getIp() != null) {
                writer.append("<tr><td>");
                writer.write(getResourceManager().getString("label.ipv4"));
                writer.append("</td><td>");
                writer.write(entity.getIp());
                writer.append("</td></tr>");
            }
            writer.append("<tr><td>");
            writer.write(getResourceManager().getString("label.mac"));
            writer.append("</td><td>");
            // add by fanzidong,展示之前需要格式化MAC地址
            String formatedMac = "";
            if (entity.getMac() != null) {
                formatedMac = MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule);
            }
            writer.write(formatedMac);
            writer.append("</td></tr>");
            writer.append("</td><tr>");
            writer.append("<tr><td>");
            writer.write(getResourceManager().getString("label.location"));
            writer.append("</td><td>");
            writer.write(entity.getLocation() == null ? "" : entity.getLocation());
            writer.append("</td></tr>");

            // EntitySnap snap = NetworkSnapManager.getInstance().getEntitySnap(userObjId);
            // 应李纵天要求，修改从数据库中去设备snap
            EntitySnap snap = entityService.getEntitySnapById(userObjId);
            if (snap != null) {
                if (snap != null && snap.isState() != null) {
                    writer.append("</td></tr><tr><td>");
                    writer.write(getResourceManager().getString("label.currentStatus"));
                    writer.append("</td><td>");
                    if (snap.isState() != null && snap.isState()) {
                        writer.append(getResourceManager().getString("label.online"));
                    } else {
                        writer.append(getResourceManager().getString("label.deviceLinkDown"));
                    }
                    writer.append("</td></tr><tr><td>");
                    writer.write(getResourceManager().getString("label.upTime"));
                    writer.append("</td><td>");

                    // modify by bravin: 不再通过设备类型来区分到底是采用哪种方式处理设备启动时长，而是根据值是否为数字为判断。根本的解决版本是设置
                    // sysuptime字段为 NUMBER类型而不是VARCHAR类型
                    String sysUptime = snap.getSysUpTime();
                    if (sysUptime == null || "".equals(sysUptime) || "-1".equals(sysUptime)) {
                        writer.write("");
                    } else if (sysUptime.matches("[0-9]+")) {
                        long $sysUptime = Long.parseLong(snap.getSysUpTime()) * 10;
                        long $snapTime = snap.getSnapTimeMillis();
                        $sysUptime += System.currentTimeMillis() - $snapTime;
                        writer.write(DateUtils.getTimePeriod($sysUptime, uc.getUser().getLanguage()));
                    } else {
                        writer.write(
                                snap.getSysUpTime() == null ? ""
                                        : snap.getSysUpTime()
                                                .replaceAll("days", getResourceManager().getString("label.days"))
                                                .replaceAll("day", getResourceManager().getString("label.day")));
                    }

                    writer.append("</td></tr>");

                    writer.append("<tr><td>");
                    writer.write(getResourceManager().getString("label.cpu"));
                    if (snap.getCpu() != null && snap.getCpu() >= 0) {
                        double value = snap.getCpu() * 100;
                        String color = "#00ff00";
                        if (value > 0) {
                            List<TopoLabel> labels = topologyService.getTopoLabel(folderId, TopoLabel.TYPE_CPU);
                            int labelSize = (labels == null ? 0 : labels.size());
                            for (int j = 0; j < labelSize; j++) {
                                if (value > labels.get(j).getValue()) {
                                    color = labels.get(j).getColor();
                                    break;
                                }
                            }
                            if (color.equals("transparent")) {
                                color = "#5be516";
                            }
                        }
                        writer.append("</td><td>");
                        writer.append(
                                "<div style=\"padding-top:3px;\"><div style=\"float:left;width:100px;height:10px;border:1px solid yellow;background-color:gray;font-size:1px;\"><div style=\"float:left;width:");
                        writer.append(String.valueOf(100 * snap.getCpu()));
                        writer.append("px;height:10px;font-size:1px;background-color:");
                        writer.append(color);
                        writer.append("\"></div></div><div style=\"float:left;margin-left:3px;color:");
                        writer.append(color);
                        writer.append("\">");
                        writer.append(PERCENT_FORMAT.format(snap.getCpu()));
                        writer.append("</div></div>");
                    } else {
                        writer.append("</td><td>--");
                    }
                    writer.append("</td></tr>");
                    writer.append("<tr><td>");
                    writer.write(getResourceManager().getString("label.mem"));
                    if (snap.getMem() != null && snap.getMem() >= 0) {
                        double value = snap.getMem() * 100;
                        String color = "#00ff00";
                        if (value > 0) {
                            List<TopoLabel> labels = topologyService.getTopoLabel(folderId, TopoLabel.TYPE_MEM);
                            int labelSize = (labels == null ? 0 : labels.size());
                            for (int j = 0; j < labelSize; j++) {
                                if (value > labels.get(j).getValue()) {
                                    color = labels.get(j).getColor();
                                    break;
                                }
                            }
                            if (color.equals("transparent")) {
                                color = "#5be516";
                            }
                        }
                        writer.append("</td><td>");
                        writer.append(
                                "<div style=\"padding-top:3px;\"><div style=\"float:left;width:100px;height:10px;border:1px solid yellow;background-color:gray;font-size:1px;\"><div style=\"float:left;width:");
                        writer.append(String.valueOf(100 * snap.getMem()));
                        writer.append("px;height:10px;font-size:1px;background-color:");
                        writer.append(color);
                        writer.append(
                                "\"></div></div><span style=\"float:left;margin-left:3px;margin-bottom:3px;color:");
                        writer.append(color);
                        writer.append("\"></div></div><div style=\"float:left;margin-left:3px;color:");
                        writer.append(color);
                        writer.append("\">");
                        writer.append(PERCENT_FORMAT.format(snap.getMem()));
                        writer.append("</div></div>");
                    } else {
                        writer.append("</td><td>--");
                    }
                } else {
                    writer.append("</td></tr><tr><td>");
                    writer.write(getResourceManager().getString("label.currentStatus"));
                    writer.append("</td><td>");
                    writer.append(getResourceManager().getString("label.offline"));
                    writer.append("</td></tr>");
                    if (!entityTypeService.isOnu(entity.getTypeId())) {
                        writer.append("<tr><td>");
                        writer.write(getResourceManager().getString("label.cpu"));
                        writer.append("</td><td>--<td></tr>");
                        writer.append("<tr><td>");
                        writer.write(getResourceManager().getString("label.mem"));
                        writer.append("</td><td>--<td></tr>");
                    }
                }
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
        writer.append("</td></tr></table><div>");
        writer.flush();

        return NONE;
    }

    /**
     * 返回连接信息.
     * 
     * @return
     * @throws Exception
     */
    public String loadLinkTip() throws Exception {
        if (userObjId == 0) {
            return NONE;
        }
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        writer.append("<div style=\"padding:0 10px 5px 10px;\"><table cellspacing=2 cellpadding=0>");
        writer.append("<tr><td width=100px>");
        LinkEx linkEx = linkService.getLinkEx(userObjId);
        writer.write(getResourceManager().getString("label.src.ip"));
        writer.append("</td><td>");
        writer.write(linkEx.getSrcIp() == null ? "" : linkEx.getSrcIp());
        writer.append("</td></tr><tr><td>");
        writer.write(getResourceManager().getString("label.src.port.index"));
        writer.append("</td><td>");
        writer.write(linkEx.getSrcPortIndex() == null ? "" : String.valueOf(linkEx.getSrcPortIndex()));
        writer.append("</td></tr><tr><td>");
        writer.write(getResourceManager().getString("label.src.port.name"));
        writer.append("</td><td>");
        writer.write(linkEx.getSrcPortName() == null ? "" : linkEx.getSrcPortName());
        writer.append("</td></tr><tr><td>");
        writer.write(getResourceManager().getString("label.dest.ip"));
        writer.append("</td><td>");
        writer.write(linkEx.getDestIp() == null ? "" : linkEx.getDestIp());
        writer.append("</td></tr><tr><td>");
        writer.write(getResourceManager().getString("label.dest.port.index"));
        writer.append("</td><td>");
        writer.write(linkEx.getDestPortIndex() == null ? "" : linkEx.getDestPortIndex());
        writer.append("</td></tr><tr><td>");
        writer.write(getResourceManager().getString("label.dest.port.name"));
        writer.append("</td><td>");
        writer.write(linkEx.getDestPortName() == null ? "" : linkEx.getDestPortName());
        writer.append("</td></tr><tr><td>");
        writer.write(getResourceManager().getString("label.bandth"));
        writer.append("</td><td>");
        writer.write(NumberUtils.getBandWidth(linkEx.getIfSpeed()));

        if (NetworkSnapManager.getInstance().containsLink(userObjId)) {
            LinkSnap snap = NetworkSnapManager.getInstance().getLinkSnapByLinkId(userObjId);
            if (snap.getFlow() > 0) {
                String linkLabel = topologyService.getLinkLabelType(folderId);
                boolean flowLabelFlag = TopoLabel.TYPE_LINKFLOW.equals(linkLabel);
                List<TopoLabel> labels = topologyService.getTopoLabel(folderId, linkLabel);
                int labelSize = labels == null ? 0 : labels.size();
                double value = flowLabelFlag ? 100 * snap.getUsage() : snap.getRate();
                String color = null;
                for (int j = 0; j < labelSize; j++) {
                    if (value > labels.get(j).getValue()) {
                        color = labels.get(j).getColor();
                        break;
                    }
                }
                writer.append("</td></tr><tr><td>");
                writer.write(getResourceManager().getString("label.flowUtility"));
                writer.append("</td><td>");
                writer.append(
                        "<div style=\"padding-top:3px;\"><div style=\"float:left;width:100px;height:10px;border:1px solid yellow;background-color:gray;font-size:1px;\"><div style=\"float:left;width:");
                writer.append(String.valueOf(100 * snap.getUsage()));
                writer.append("px;height:10px;font-size:1px;background-color:");
                writer.append(color);
                writer.append("\"></div></div><div style=\"float:left;margin-left:3px;color:");
                writer.append(color);
                writer.append("\">");
                writer.append(NumberUtils.getPercentStr(snap.getUsage(), 1));
                writer.append("</div></div>");
                writer.append("</td></tr>");
                writer.append("<tr><td>");
                writer.write(getResourceManager().getString("label.rateUtility"));
                writer.append("</td><td style=\"color:");
                writer.append(color);
                writer.append("\">");
                writer.append(NumberUtils.getIfSpeedStr(snap.getRate()));
                writer.append("</td></tr><tr><td>");
                writer.write(getResourceManager().getString("label.linkflow"));
                writer.append("</td><td>");
                writer.write(NumberUtils.getByteLength(snap.getFlow()));
            }
        } else {
            writer.append("</td></tr><tr><td>");
            writer.write(getResourceManager().getString("label.flowUtility"));
            writer.append("</td><td>");
            writer.append("</td></tr><tr><td>");
            writer.write(getResourceManager().getString("label.rateUtility"));
            writer.append("</td><td>");
            writer.append("</td></tr><tr><td>");
            writer.write(getResourceManager().getString("label.linkflow"));
            writer.append("</td><td>");
        }
        writer.append("</td></tr></table></div>");
        writer.flush();

        return NONE;
    }

    /**
     * 获取结点实体信息.
     * 
     * @return
     */
    public String showVmlProperty() {
        mapNode = topologyService.getMapNode(nodeId);
        if (mapNode.getType() == MapNode.PICTURE) {
            return "picture";
        }
        return SUCCESS;
    }

    /**
     * 插入图形结点信息.
     * 
     * @return
     * @throws Exception
     */
    public String insertMapNode() throws Exception {
        JSONObject json = new JSONObject();
        topologyService.insertMapNode(mapNode);
        json.put("id", "cell" + mapNode.getNodeId());
        json.put("nodeId", mapNode.getNodeId());
        json.put("text", mapNode.getText());
        json.put("url", mapNode.getUrl());
        json.put("x", mapNode.getX());
        json.put("y", mapNode.getY());
        json.put("icon", mapNode.getIcon());
        json.put("width", mapNode.getWidth());
        json.put("height", mapNode.getHeight());
        json.put("groupId", mapNode.getGroupId());
        json.put("fixed", mapNode.getFixed());
        json.put("name", mapNode.getText());
        json.put("objType", mapNode.getUserObjType());
        json.put("userObjId", mapNode.getUserObjId());
        json.put("type", mapNode.getType());
        json.put("strokeWeight", mapNode.getStrokeWeight());
        json.put("strokeColor", mapNode.getStrokeColor());
        json.put("fillColor", mapNode.getFillColor());
        json.put("gradient", mapNode.getGradient());
        json.put("gradientColor", mapNode.getGradientColor());
        json.put("fontColor", mapNode.getTextColor());
        json.put("fontSize", mapNode.getFontSize());
        json.put("shadow", mapNode.getShadow());
        json.put("dashed", mapNode.getDashed());
        json.put("collapsed", !mapNode.getExpanded().booleanValue());
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 修改指定图形结点的Text信息.
     * 
     * @return
     */
    public String setMapNodeText() {
        MapNode node = new MapNode();
        node.setNodeId(nodeId);
        node.setText(text);
        node.setFolderId(folderId);
        topologyService.updateMapNodeText(node);
        return NONE;
    }

    /**
     * 修改指定图形结点的字体大小信息.
     * 
     * @return
     */
    public String setMapNodeFontSize() {
        topologyService.updateMapNodeFontSize(mapNode);

        return NONE;
    }

    /**
     * 修改指定图形结点的字体颜色信息.
     * 
     * @return
     */
    public String setMapNodeFontColor() {
        topologyService.updateMapNodeFontColor(mapNode);

        return NONE;
    }

    /**
     * 修改指定图形结点的笔划粗细信息.
     * 
     * @return
     */
    public String setMapNodeStrokeWeight() {
        topologyService.updateMapNodeStrokeWeight(mapNode);

        return NONE;
    }

    /**
     * 修改指定图形结点的Text信息.
     * 
     * @return
     * @throws Exception
     */
    public String setMapNodeInfo() {
        topologyService.updateMapNodeText(mapNode);
        return NONE;
    }

    /**
     * 修改超链的URL属性.
     * 
     * @return
     * @throws Exception
     */
    public String setMapNodeUrl() {
        topologyService.updateMapNodeUrl(mapNode);
        return NONE;
    }

    /**
     * 修改指定图形结点的阴影信息.
     * 
     * @return
     */
    public String setMapNodeShadow() {
        topologyService.updateMapNodeShadow(mapNode);
        return NONE;
    }

    /**
     * 修改指定图形结点的虚线边界信息.
     * 
     * @return
     */
    public String setMapNodeDashedBorder() {
        topologyService.updateMapNodeDashedBorder(mapNode);
        return NONE;
    }

    /**
     * 修改fixed信息.
     * 
     * @return
     */
    public String updateVertexFixed() {
        List<Entity> entities = new ArrayList<Entity>();
        List<MapNode> mapNodes = new ArrayList<MapNode>();
        for (int i = 0; i < nodeIds.size(); i++) {
            if (nodeIds.get(i) < Entity.ENTITY_INC) {
                MapNode node = new MapNode();
                node.setNodeId(nodeIds.get(i));
                node.setFixed(fixed);
                mapNodes.add(node);
            } else {
                Entity entity = new Entity();
                entity.setEntityId(nodeIds.get(i));
                entity.setFolderId(folderId);
                entity.setFixed(fixed);
                entities.add(entity);
            }
        }
        if (entities.size() > 0) {
            entityService.updateEntityFixed(entities);
        }
        if (mapNodes.size() > 0) {
            topologyService.updateMapNodeFixed(mapNodes);
        }

        return NONE;
    }

    /**
     * 修改指定图形结点的fixed信息.
     * 
     * @return
     */
    public String setMapNodeFixed() {
        MapNode mn = topologyService.getMapNode(mapNode.getNodeId());
        if (mn == null) {
            TopoFolder tf = topologyService.getTopoFolder(mapNode.getNodeId());
            if (tf != null) {
                // 更新Topfolder
                tf.setFixed(mapNode.getFixed());
                topologyService.updateTopoFolderFixed(tf);
            }
        } else {
            // 更新MapNode
            mn.setFixed(mapNode.getFixed());
            topologyService.updateMapNodeFixed(mn);
        }

        return NONE;
    }

    /**
     * 修改指定图形结点的图标信息.
     * 
     * @return
     * @throws Exception
     */
    public String setMapNodeIcon() {
        topologyService.updateMapNodeIcon(mapNode);
        return NONE;
    }

    /**
     * 修改指定图形结点的宽高信息.
     * 
     * @return
     */
    public String setMapNodeSize() {
        topologyService.updateMapNodeSize(mapNode);
        return NONE;
    }

    /**
     * 修改指定图形结点的组信息.
     * 
     * @return
     */
    public String setMapNodeGroup() {
        if (nodeId >= Entity.ENTITY_INC) {
            Entity entity = new Entity();
            entity.setFolderId(folderId);
            entity.setEntityId(nodeId);
            entity.setGroupId(groupId);
            topologyService.updateEntityGroup(entity);
        } else if (nodeId >= MapNode.MAPNODE_INC) {
            mapNode = new MapNode();
            mapNode.setFolderId(folderId);
            mapNode.setNodeId(nodeId);
            mapNode.setGroupId(groupId);
            topologyService.updateMapNodeGroup(mapNode);
        }

        return NONE;
    }

    /**
     * 修改指定图形结点的扩展信息.
     * 
     * @return
     */
    public String setMapNodeExpanded() {
        topologyService.updateMapNodeExpanded(mapNode);
        return NONE;
    }

    /**
     * 修改指定图形结点的填充颜色信息.
     * 
     * @return
     */
    public String setMapNodeFillColor() {
        topologyService.updateMapNodeFillColor(mapNode);
        return NONE;
    }

    /**
     * 修改指定图形结点的笔划颜色信息.
     * 
     * @return
     */
    public String setMapNodeStrokeColor() {
        topologyService.updateMapNodeStrokeColor(mapNode);
        return NONE;
    }

    private ResourceManager getResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.network.resources");
    }

    public MapNode getMapNode() {
        return mapNode;
    }

    public void setMapNode(MapNode mapNode) {
        this.mapNode = mapNode;
    }

    public int getUserObjType() {
        return userObjType;
    }

    public void setUserObjType(int userObjType) {
        this.userObjType = userObjType;
    }

    public long getUserObjId() {
        return userObjId;
    }

    public void setUserObjId(long userObjId) {
        this.userObjId = userObjId;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public List<Long> getNodeIds() {
        return nodeIds;
    }

    public void setNodeIds(List<Long> nodeIds) {
        this.nodeIds = nodeIds;
    }

    public long getFolderId() {
        return folderId;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public void setLinkService(LinkService linkService) {
        this.linkService = linkService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public void setTopologyService(TopologyService topologyService) {
        this.topologyService = topologyService;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
