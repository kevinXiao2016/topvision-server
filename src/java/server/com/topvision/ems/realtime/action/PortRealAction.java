/**
 * 
 */
package com.topvision.ems.realtime.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.service.PortService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.service.SystemPreferencesService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author niejun
 * 
 */
@Controller("portRealAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PortRealAction extends BaseAction {
    private static final long serialVersionUID = 3601694655976848582L;
    private static Logger logger = LoggerFactory.getLogger(PortRealAction.class);
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private PortService portService;
    private List<Port> ports = null;
    private Port port = null;
    private long entityId;
    private String ip;
    private long synTime;
    private long portIndex;
    private List<Long> portIndexs;

    public String getPortRealDataByEntity() throws Exception {
        JSONObject json = new JSONObject();
        try {
            ports = portService.getPortByEntity(entityId);
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
                    temp.put("ifDescr", port.getIfDescr());
                    temp.put("ifType", port.getIfTypeString());
                    temp.put("ifMtu", port.getIfMtuString());
                    temp.put("ifSpeed", port.getIfSpeedString());
                    temp.put("ifPhysAddress", port.getIfPhysAddress());
                    temp.put("ifAdminStatus", port.getIfAdminStatusString());
                    temp.put("ifOperStatus", port.getIfOperStatusString());
                    array.add(temp);
                }
            }
            json.put("data", array);
        } catch (Exception ex) {
            logger.error("Load Entity Link Table.", ex);
            throw ex;
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    public String showPortFlowInfo() throws Exception {
        if (systemPreferencesService.isUsedApplet()) {
            return "applet";
        }
        try {
            synTime = System.currentTimeMillis();
            ports = portService.getPortByEntity(entityId);
        } catch (Exception ex) {
            logger.error("show Ports list.", ex);
        }
        return SUCCESS;
    }

    public String showPortRealCompareInfo() throws Exception {
        try {
            synTime = System.currentTimeMillis();
            ports = portService.getPortByEntity(entityId);
        } catch (Exception ex) {
            logger.error("show Ports list.", ex);
        }
        return SUCCESS;
    }

    public String showPortRealSingleInfo() throws Exception {
        try {
            synTime = System.currentTimeMillis();
            ports = portService.getPortByEntity(entityId);
        } catch (Exception ex) {
            logger.error("show Ports list.", ex);
        }
        return SUCCESS;
    }

    public long getEntityId() {
        return entityId;
    }

    public String getIp() {
        return ip;
    }

    public Port getPort() {
        return port;
    }

    public long getPortIndex() {
        return portIndex;
    }

    public List<Long> getPortIndexs() {
        return portIndexs;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public long getSynTime() {
        return synTime;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public void setPortIndex(long portIndex) {
        this.portIndex = portIndex;
    }

    public void setPortIndexs(List<Long> portIndexs) {
        this.portIndexs = portIndexs;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public void setPortService(PortService portService) {
        this.portService = portService;
    }

    public void setSynTime(long synTime) {
        this.synTime = synTime;
    }

    public void setSystemPreferencesService(SystemPreferencesService systemPreferencesService) {
        this.systemPreferencesService = systemPreferencesService;
    }

}
