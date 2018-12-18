package com.topvision.ems.network.action;

import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.network.domain.LinkEx;
import com.topvision.ems.network.service.LinkService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("linkSearchAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LinkSearchAction extends BaseAction {
    private static final long serialVersionUID = -6174988244189696523L;
    private String query;
    private List<LinkEx> links;
    private String q;
    private int limit;
    private long entityId;
    @Autowired
    private LinkService linkService;

    /**
     * 返回连接信息.
     * 
     * @return
     * @throws Exception
     */
    public String queryLinkForAc() throws Exception {
        JSONArray json = new JSONArray();
        query = new String(q.getBytes("ISO-8859-1"), "UTF-8");
        links = linkService.queryLink(query);
        int size = links.size();
        LinkEx linkEx = null;
        JSONObject temp = null;
        for (int i = 0; i < size; i++) {
            linkEx = links.get(i);
            temp = new JSONObject();
            temp.put("linkId", linkEx.getLinkId());
            temp.put("name",
                    linkEx.getSrcIp() + ": " + linkEx.getSrcPortIndex() + " [" + linkEx.getSrcPortName() + "] - "
                            + linkEx.getDestIp() + ": " + linkEx.getDestPortIndex() + " [" + linkEx.getDestPortName()
                            + "]");
            json.add(temp);
        }

        writeDataToAjax(json);

        return NONE;
    }

    /**
     * 查询.
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String search() throws UnsupportedEncodingException {
        query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
        return SUCCESS;
    }

    /**
     * 显示连接弹出窗口.
     * 
     * @return
     */
    public String showPopLinkDlg() {
        return SUCCESS;
    }

    /**
     * 返回所以连接信息.
     * 
     * @return
     * @throws Exception
     */
    public String loadAllLink() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        if (entityId > 0) {
            links = linkService.getAllLink(entityId);
        } else {
            links = linkService.getAllLink();
        }
        if (links != null) {
            int size = links.size();
            LinkEx link = null;
            writer.write("<?xml version=\"1.0\" ?><complete>");
            for (int i = 0; i < size; i++) {
                link = links.get(i);
                writer.write("<option value=\"");
                writer.write(String.valueOf(link.getLinkId()));
                writer.write("\">");
                writer.write(link.getSrcIp());
                writer.write(": [");
                writer.write(link.getSrcPortName());
                writer.write("] - ");
                writer.write(link.getDestIp());
                writer.write(": [");
                writer.write(link.getDestPortName());
                writer.write("]");
                writer.write("</option>");
            }
            writer.write("</complete>");
            writer.flush();
        }
        return NONE;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<LinkEx> getLinks() {
        return links;
    }

    public void setLinks(List<LinkEx> links) {
        this.links = links;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setLinkService(LinkService linkService) {
        this.linkService = linkService;
    }

}
