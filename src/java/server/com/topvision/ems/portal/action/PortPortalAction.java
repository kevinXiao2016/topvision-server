package com.topvision.ems.portal.action;

import java.io.Writer;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.ResourceManager;

/**
 * 
 * @Create Date Nov 4, 2009 6:11:35 PM
 * 
 * @author kelers
 * 
 */
@Controller("portPortalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PortPortalAction extends BaseAction {
    private static final long serialVersionUID = -1795409897308745728L;
    protected static final Logger logger = LoggerFactory.getLogger(PortPortalAction.class);

    public String getPortFlowTop() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        writer.write("<table width=100% class=portletTable><tr><th width=50px style=\"text-align:left\">"
                + getResourceString("WorkBench.Ranking", "com.topvision.ems.resources.resources")
                + "</th><Th>"
                + getResourceString("Config.oltConfigFileImported.deviceName", "com.topvision.ems.resources.resources")
                + "</th><th>"
                + getResourceString("WorkBench.PortName", "com.topvision.ems.resources.resources")
                + "</th><th style=\"text-align:right\"><img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>"
                + getResourceString("PortReportCreator.TotalFlow", "com.topvision.ems.resources.resources")
                + "</th></tr>");
        try {
        } catch (Exception ex) {
            logger.error("GetTop Link Flow.", ex);
        } finally {
            writer.write("</table>");
            writer.flush();
        }
        return NONE;
    }

    public String getPortRateTop() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        writer.write("<table width=100% class=portletTable><tr><th width=50px style=\"text-align:left\">"
                + getResourceString("WorkBench.Ranking", "com.topvision.ems.resources.resources")
                + "</th><Th>"
                + getResourceString("Config.oltConfigFileImported.deviceName", "com.topvision.ems.resources.resources")
                + "</th><th>"
                + getResourceString("WorkBench.PortName", "com.topvision.ems.resources.resources")
                + "</th><th style=\"text-align:right\"><img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>"
                + getResourceString("PortPortalAction.TotalRate", "com.topvision.ems.resources.resources")
                + "</th></tr>");
        try {
        } catch (Exception ex) {
            logger.error("GetTop Link Flow.", ex);
        } finally {
            writer.write("</table>");
            writer.flush();
        }
        return NONE;
    }

    /**
     * key：properties文件的keymodule：资源文件
     */
    protected String getResourceString(String key, String module) {
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }
}
