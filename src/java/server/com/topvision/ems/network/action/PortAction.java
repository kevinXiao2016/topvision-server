package com.topvision.ems.network.action;

import java.io.Writer;
import java.sql.Timestamp;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.PortService;
import com.topvision.ems.network.util.PortUtil;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.domain.InterceptorLog;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("portAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PortAction extends BaseAction {
    private static final long serialVersionUID = 6655360504986227689L;
    private List<Port> ports;
    private Port port;
    private Entity entity;
    private long portId;
    private long entityId;
    private Long ifIndex;
    private String ip;
    @Autowired
    private EntityService entityService;
    @Autowired
    private PortService portService;
    private boolean saveLog;

    /**
     * 关闭端口.
     * 
     * @return
     */
    public String closePort() {
        InterceptorLog log = null;
        if (saveLog) {
            log = new InterceptorLog();
            log.setAuto(false);
            log.setDescription(port.getIfDescr());
            log.setEntityId(port.getEntityId());
            log.setIfIndex(port.getIfIndex());
            log.setInterceptorTime(new Timestamp(System.currentTimeMillis()));
        }

        portService.txClosePort(port, log);

        return NONE;
    }

    public String loadPortsByEntityId() throws Exception {
        ports = portService.getPortByEntity(entityId);
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        if (ports == null) {
            json.put("rowCount", 0);
        } else {
            int size = ports == null ? 0 : ports.size();
            json.put("rowCount", size);
            JSONObject temp = null;
            Port port = null;
            for (int i = 0; i < size; i++) {
                temp = new JSONObject();
                port = ports.get(i);
                temp.put("ifIndex", port.getIfIndex());
                temp.put("ifName", port.getName());
                temp.put("ifAlias", port.getIfAlias());
                temp.put("ifType", port.getIfTypeString());
                temp.put("ifMtu", port.getIfMtuString());
                temp.put("ifSpeed", port.getIfSpeedString());
                temp.put("ifPhysAddress", port.getIfPhysAddress());
                temp.put("ifAdminStatus", port.getIfAdminStatus());
                temp.put("ifOperStatus", port.getIfOperStatus());
                array.add(temp);
            }
        }
        json.put("data", array);

        writeDataToAjax(json);

        return NONE;
    }

    public String loadPortTip() throws Exception {
        if (portId == 0) {
            return NONE;
        }
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        Port p = portService.getPort(portId);
        writer.append("<table cellspacing=0 cellpadding=0>");
        writer.append("<tr><td width=80px>");
        writer.append(String.valueOf(p.getIfIndex()));
        writer.append("<td><Td>");
        writer.append(String.valueOf(p.getIfIndex()));
        writer.append("</td><Tr>");
        writer.append("<tr><td>");
        writer.append(String.valueOf(p.getIfIndex()));
        writer.append("<td><Td>");
        writer.append(String.valueOf(p.getIfDescr()));
        writer.append("</td><Tr>");
        writer.append("</td></tr></table>");
        writer.flush();

        return NONE;
    }

    /**
     * 打开端口.
     * 
     * @return
     */
    public String openPort() {
        portService.txOpenPort(port, null);
        return NONE;
    }

    public String showEntityInterfacesJsp() {
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    public String showPortPropertyJsp() {
        port = portService.getPort(entityId, ifIndex);
        if (port.getIfLastChange() != null) {
            port.setLastChangeTime(PortUtil.getLastChangeTime(port.getIfLastChange()));
        }
        return SUCCESS;
    }

    public String showPortsJsp() {
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    public String updatePortInfo() {
        portService.updatePort(port);
        return NONE;
    }

    public Entity getEntity() {
        return entity;
    }

    public long getEntityId() {
        return entityId;
    }

    public Long getIfIndex() {
        return ifIndex;
    }

    public String getIp() {
        return ip;
    }

    public Port getPort() {
        return port;
    }

    public long getPortId() {
        return portId;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public final boolean isSaveLog() {
        return saveLog;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public void setPortId(long portId) {
        this.portId = portId;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public void setPortService(PortService portService) {
        this.portService = portService;
    }

    public final void setSaveLog(boolean saveLog) {
        this.saveLog = saveLog;
    }
}
