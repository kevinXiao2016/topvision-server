/**
 * 
 */
package com.topvision.ems.portal.action;

import java.io.Writer;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.network.domain.LinkEx;
import com.topvision.ems.network.service.LinkService;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.ResourceManager;

/**
 * @author kelers
 * 
 */
@Controller("linkPortalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LinkPortalAction extends BaseAction {
    private static final long serialVersionUID = 9167090192714880880L;
    private static Logger logger = LoggerFactory.getLogger(LinkPortalAction.class);
    @Autowired
    private LinkService linkService;

    public String getLinkRateTop() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        writer.write("<table width=100% class=portletTable><tr><th width=50px style=\"text-align:left\">"
                + getResourceString("WorkBench.Ranking", "com.topvision.ems.resources.resources")
                + "</th><th>"
                + getResourceString("LinkPortalAction.LineName", "com.topvision.ems.resources.resources")
                + "</th><th style=\"text-align:right\"><img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>"
                + getResourceString("PortPortalAction.TotalRate", "com.topvision.ems.resources.resources")
                + "</th></tr>");
        try {
            List<LinkEx> links = linkService.getLinkRateTop(null);
            int size = links == null ? 0 : links.size();
            if (size > 0) {
                LinkEx link = null;
                for (int i = 0; i < size; i++) {
                    link = links.get(i);
                    writer.write("<tr><Td><div class=topCls");
                    writer.write(String.valueOf(i + 1));
                    writer.write("></div></Td><Td><a class=my-link");
                    writer.write(" href=\"javascript:showLinkSnap(");
                    writer.write(String.valueOf(link.getLinkId()));
                    writer.write(")\">");
                    writer.write(link.getName());
                    writer.write("</a></Td>");
                    writer.write("<Td align=right>");
                    writer.write(NumberUtils.getIfSpeedStr(link.getSrcIfOctetsRate()));
                    writer.write("</Td>");
                    writer.write("</tr>");
                }
            }
        } catch (Exception ex) {
            logger.error("GetTop Link Rate.", ex);
        } finally {
            writer.write("</table>");
            writer.flush();
        }
        return NONE;
    }

    public String getTopLinkFlow() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        writer.write("<table width=100% class=portletTable><tr><th width=50px style=\"text-align:left\">"
                + getResourceString("WorkBench.Ranking", "com.topvision.ems.resources.resources")
                + "</th><th>"
                + getResourceString("LinkPortalAction.LineName", "com.topvision.ems.resources.resources")
                + "</th><th style=\"text-align:right\"><img src=\"../images/desc.gif\" border=0 align=absmiddle hspace=5>"
                + getResourceString("PortPortalAction.TotalRate", "com.topvision.ems.resources.resources")
                + "</th></tr>");
        try {
            List<LinkEx> links = linkService.getLinkFlowTop(null);
            int size = links == null ? 0 : links.size();
            if (size > 0) {
                LinkEx link = null;
                for (int i = 0; i < size; i++) {
                    link = links.get(i);
                    writer.write("<tr><Td><div class=topCls");
                    writer.write(String.valueOf(i + 1));
                    writer.write("></div></Td><Td><a class=my-link");
                    writer.write(" href=\"javascript:showLinkSnap(");
                    writer.write(String.valueOf(link.getLinkId()));
                    writer.write(")\">");
                    writer.write(link.getName());
                    writer.write("</a></Td>");
                    writer.write("<Td align=right>");
                    writer.write(NumberUtils.getByteLength(link.getSrcIfOctets()));
                    writer.write("</Td>");
                    writer.write("</tr>");
                }
            }
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

    public void setLinkService(LinkService linkService) {
        this.linkService = linkService;
    }
}
