package com.topvision.ems.network.action;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.LinkEx;
import com.topvision.ems.network.domain.MapNode;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.domain.PortEx;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.LinkService;
import com.topvision.ems.network.service.NetworkConstants;
import com.topvision.ems.network.service.PortService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("linkAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LinkAction extends BaseAction {
    private static final long serialVersionUID = 2581376505382102797L;
    private String name;
    private long entityId;
    private long srcEntityId;
    private long destEntityId;
    private int connectType = Link.CONNECT_STRAIGHT;
    private long linkId;
    private long folderId;
    private LinkEx linkEx;
    private Entity entity;
    private List<Long> values;
    private List<String> colors;
    @Autowired
    private PortService portService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private EntityService entityService;
    private List<Port> ports;
    private List<Port> destPorts;

    /**
     * 插入新连接.
     * 
     * @return
     * @throws Exception
     */
    public String insertNewLink() throws Exception {
        Link link = new Link();
        link.setSrcEntityId(srcEntityId);
        link.setDestEntityId(destEntityId);
        link.setName(name);
        link.setConnectType(connectType);
        if (srcEntityId < MapNode.MAPNODE_INC || destEntityId < MapNode.MAPNODE_INC) {
            link.setType(Link.LOGIC_LINK_TYPE);
        } else {
            link.setType(Link.MANUAL_LINK_TYPE);
        }

        JSONObject json = new JSONObject();
        linkService.insertLink(null, link, folderId);
        json.put("id", "edge" + link.getLinkId());
        json.put("text", link.getName());
        json.put("connectType", link.getConnectType());
        json.put("objType", NetworkConstants.TYPE_LINK);
        json.put("entityId", link.getLinkId());
        json.put("linkId", link.getLinkId());
        json.put("nodeId", link.getLinkId());
        json.put("name", link.getName());
        json.put("srcEntityId", link.getSrcEntityId());
        json.put("destEntityId", link.getDestEntityId());

        writeDataToAjax(json);

        return NONE;
    }

    /**
     * 显示连接属性.
     * 
     * @return
     */
    public String showLinkPropertyJsp() {
        linkEx = linkService.getLinkEx(linkId);
        linkEx.setIfSpeedStr(NumberUtils.getBandWidth(linkEx.getIfSpeed()));
        return SUCCESS;
    }

    /**
     * 显示设备连接表.
     * 
     * @return
     */
    public String showEntityLinkTableJsp() {
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    /**
     * 更新连接概要信息.
     * 
     * @return
     */
    public String updateLinkOutline() {
        linkEx.setLinkId(linkId);
        linkService.updateOutline(linkEx);
        return NONE;
    }

    /**
     * 显示设备的所有端口信息.
     * 
     * @return
     */
    public String showLinkSelectPortJsp() {
        ports = portService.getPortByEntity(srcEntityId);
        return SUCCESS;
    }

    /**
     * 返回设备连接表页面.
     * 
     * @return
     */
    public String getEntityLinkTableJsp() {
        return SUCCESS;
    }

    /**
     * 返回设备连接表信息.
     * 
     * @return
     * @throws Exception
     */
    public String getEntityLinkTable() throws Exception {
        // add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.network.resources");
        JSONObject json = new JSONObject();
        List<PortEx> links = linkService.getLinkTableByEntity(entityId);
        JSONArray array = new JSONArray();
        int size = links == null ? 0 : links.size();
        json.put("rowCount", size);
        if (size > 0) {
            JSONObject temp = null;
            PortEx link = null;
            Set<String> set = new HashSet<String>();
            for (int i = 0; i < size; i++) {
                temp = new JSONObject();
                link = links.get(i);
                temp.put("ifAdminStatus", link.getIfAdminStatus());
                temp.put("ifOperStatus", link.getIfOperStatus());
                temp.put("ifIndex", link.getIfIndex());
                temp.put("ifName", link.getIfName());
                String mac = MacUtils.convertMacToDisplayFormat(link.getMac(), macRule);
                temp.put("mac", mac);
                temp.put("ip", link.getIp());
                temp.put("name", link.getDestName());
                if (link.getDestType() == null) {
                    temp.put("type", "-");
                } else {
                    int t = Integer.parseInt(link.getDestType());
                    if (t == 127) {
                        if (set.contains(link.getMac())) {
                            continue;
                        }
                        temp.put("type", resourceManager.getNotNullString("type.desktop"));
                    }
                    if (t == 72) {
                        set.add(link.getMac());
                        temp.put("type", resourceManager.getNotNullString("type.server"));
                    } else if (t < 72) {
                        temp.put("type", resourceManager.getNotNullString("type.networkDevice"));
                    }
                }
                temp.put("destIfIndex", link.getDestIfIndex());
                temp.put("destIfName", link.getDestIfName());
                array.add(temp);
            }
        }
        json.put("data", array);

        writeDataToAjax(json);

        return NONE;
    }

    /**
     * 显示连接信息.
     * 
     * @return
     */
    public String showLinkInfo() {
        linkEx = linkService.getLinkEx(linkId);
        ports = portService.getPortByEntity(linkEx.getSrcEntityId());
        destPorts = portService.getPortByEntity(linkEx.getDestEntityId());

        return SUCCESS;
    }

    public String changePortOfLink() {
        long ifIndex = Long.valueOf(linkEx.getSrcPortIndex());
        Boolean isSrc = linkEx.isStartArrow();
        linkEx = linkService.getLinkEx(linkId);
        if (isSrc) {
            linkEx.setSrcIfIndex(ifIndex);
        } else {
            linkEx.setDestIfIndex(ifIndex);
        }
        linkService.updateLink(linkEx);

        return NONE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public long getSrcEntityId() {
        return srcEntityId;
    }

    public void setSrcEntityId(long srcEntityId) {
        this.srcEntityId = srcEntityId;
    }

    public long getDestEntityId() {
        return destEntityId;
    }

    public void setDestEntityId(long destEntityId) {
        this.destEntityId = destEntityId;
    }

    public int getConnectType() {
        return connectType;
    }

    public void setConnectType(int connectType) {
        this.connectType = connectType;
    }

    public long getLinkId() {
        return linkId;
    }

    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }

    public long getFolderId() {
        return folderId;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public LinkEx getLinkEx() {
        return linkEx;
    }

    public void setLinkEx(LinkEx linkEx) {
        this.linkEx = linkEx;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public List<Long> getValues() {
        return values;
    }

    public void setValues(List<Long> values) {
        this.values = values;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public List<Port> getDestPorts() {
        return destPorts;
    }

    public void setDestPorts(List<Port> destPorts) {
        this.destPorts = destPorts;
    }

    public void setPortService(PortService portService) {
        this.portService = portService;
    }

    public void setLinkService(LinkService linkService) {
        this.linkService = linkService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }
}
