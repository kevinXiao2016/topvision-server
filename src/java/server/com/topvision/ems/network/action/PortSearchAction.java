package com.topvision.ems.network.action;

import java.io.Writer;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.service.PortService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("portSearchAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PortSearchAction extends BaseAction {
    private static final long serialVersionUID = 2889021235154944659L;
    private long entityId;
    @Autowired
    private PortService portService;

    public String getPortsByEntityId() throws Exception {
        JSONArray array = new JSONArray();
        JSONObject json = null;
        Port port = null;
        List<Port> ports = portService.getPortByEntity(entityId);
        int size = ports == null ? 0 : ports.size();
        for (int i = 0; i < size; i++) {
            port = ports.get(i);
            json = new JSONObject();
            json.put("ifIndex", port.getIfIndex());
            json.put("ifDescr", port.getIfName());
            json.put("ifName", port.getIfName());
            array.add(json);
        }

        writeDataToAjax(array);

        return NONE;
    }

    public String loadPortForCombox() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        Port port = null;
        List<Port> ports = portService.getPortByEntity(entityId);
        int size = ports == null ? 0 : ports.size();
        writer.write("<?xml version=\"1.0\" ?><complete>");
        for (int i = 0; i < size; i++) {
            port = ports.get(i);
            writer.write("<option value=\"");
            writer.write(String.valueOf(port.getIfIndex()));
            writer.write("\" img_src=\"");
            writer.write(port.getIfOperStatus() == 1 ? "../images/network/port/up.png"
                    : (port.getIfOperStatus() == 2 ? "../images/network/port/down.png" : "../images/s.gif"));
            writer.write("\">");
            writer.write(String.valueOf(port.getIfIndex()));
            writer.write(" [");
            writer.write(port.getIfDescr());
            writer.write("]");
            writer.write("</option>");
        }
        writer.write("</complete>");
        writer.flush();

        return NONE;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setPortService(PortService portService) {
        this.portService = portService;
    }

    public long getEntityId() {
        return entityId;
    }
}
