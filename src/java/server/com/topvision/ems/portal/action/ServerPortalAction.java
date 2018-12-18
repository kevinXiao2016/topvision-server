/**
 * 
 */
package com.topvision.ems.portal.action;

import java.io.PrintWriter;
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
 * @author kelers
 * 
 */
@Controller("serverPortalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ServerPortalAction extends BaseAction {
    private static final long serialVersionUID = -8523478225705888624L;
    private static Logger logger = LoggerFactory.getLogger(ServerPortalAction.class);

    public String getServerAvailableDist() throws Exception {
        StringBuilder sb = new StringBuilder("");
        try {
        } catch (Exception ex) {
            logger.error("Get Server Available Dist.", ex);
            throw ex;
        } finally {
            writeDataToAjax(sb.toString());
        }
        return NONE;
    }

    public String getServerTypeDist() throws Exception {
        StringBuilder sb = new StringBuilder("");
        try {
        } catch (Exception ex) {
            logger.error("Get Server Type Dist.", ex);
            throw ex;
        } finally {
            writeDataToAjax(sb.toString());
        }
        return NONE;
    }

    public String getTopServerCpuLoading() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        writer.write("<table width=100% class=portletTable><tr><th width=50px style=\"text-align:left\">"
                + getResourceString("WorkBench.Ranking", "com.topvision.ems.resources.resources")
                + "</th><th>"
                + getResourceString("Config.oltConfigFileImported.deviceName", "com.topvision.ems.resources.resources")
                + "</th><Th>"
                + getResourceString("COMMON.ip", "com.topvision.ems.resources.resources")
                + "</th><th style=\"text-align:right\"><img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>"
                + getResourceString("WorkBench.CpuRanking", "com.topvision.ems.resources.resources")
                + "(%)</th><th style=\"text-align:right\">" 
                + getResourceString("WorkBench.MonitorTime", "com.topvision.ems.resources.resources")
                + "</th></tr>");
        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            writer.write("</table>");
            writer.flush();
        }
        return NONE;
    }

    public String getTopServerDelaying() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        writer.write("<table width=100% class=portletTable><tr><th width=50px style=\"text-align:left\">"
                + getResourceString("WorkBench.Ranking", "com.topvision.ems.resources.resources")
                + "</th><th>"
                + getResourceString("Config.oltConfigFileImported.deviceName", "com.topvision.ems.resources.resources")
                + "</th><Th>"
                + getResourceString("COMMON.ip", "com.topvision.ems.resources.resources")
                + "</th><th style=\"text-align:right\"><img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>"
                + getResourceString("PortPortalAction.Delay", "com.topvision.ems.resources.resources")
                + "(ms)</th><th style=\"text-align:right\">" 
                + getResourceString("WorkBench.MonitorTime", "com.topvision.ems.resources.resources")
                + "</th></tr>");
        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            writer.write("</table>");
            writer.flush();
        }
        return NONE;
    }

    public String getTopServerMemLoading() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        writer.write("<table width=100% class=portletTable><tr><th width=50px style=\"text-align:left\">"
                + getResourceString("WorkBench.Ranking", "com.topvision.ems.resources.resources")
                + "</th><th>"
                + getResourceString("Config.oltConfigFileImported.deviceName", "com.topvision.ems.resources.resources")
                + "</th><Th>"
                + getResourceString("COMMON.ip", "com.topvision.ems.resources.resources")
                + "</th><th style=\"text-align:right\"><img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>"
                + getResourceString("WorkBench.MemRanking", "com.topvision.ems.resources.resources")
                + "(%)</th><th style=\"text-align:right\">" 
                + getResourceString("PortPortalAction.Used", "com.topvision.ems.resources.resources")
                + "(M)</th><th style=\"text-align:right\">" 
                + getResourceString("WorkBench.MonitorTime", "com.topvision.ems.resources.resources")
                + "</th></tr>");
        try {
        } catch (Exception ex) {
            ex.printStackTrace();
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
