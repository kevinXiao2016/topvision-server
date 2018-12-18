/**
 * 
 */
package com.topvision.ems.portal.action;

import java.awt.Color;
import java.io.Writer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.facade.domain.HostService;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.NetworkSnapManager;
import com.topvision.ems.network.service.PortService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.FileUtils;
import com.topvision.framework.exception.ResourceNotFoundException;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.ResourceManager;

import net.sf.json.JSONObject;

/**
 * 以 Portal 方式显示设备信息.
 * 
 * @author niejun
 * 
 */
@Controller("entityPortalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityPortalAction extends BaseAction {
    private static final long serialVersionUID = -3186924787341629459L;
    private static Logger logger = LoggerFactory.getLogger(EntityPortalAction.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private int width = 150;
    private int height = 150;
    private Long entityId;
    private Long productType;
    private Long productId;
    private String ip = null;
    private boolean refresh = false;
    private Entity entity;
    private List<HostService> hostServices;
    @Autowired
    private EntityService entityService;
    @Autowired
    private AlertService alertService;
    @Autowired
    private PortService portService;
    @Autowired
    private EntityTypeService entityTypeService;
    private SnmpParam snmpParam;

    private static JFreeChart createChart(String title, ValueDataset valuedataset) {
        MeterPlot meterplot = new MeterPlot(valuedataset);
        meterplot.addInterval(new MeterInterval("High", new Range(0.0D, 1.0D)));
        meterplot.setDialOutlinePaint(Color.white);
        meterplot.setUnits("%");
        meterplot.setTickLabelFormat(NumberFormat.getIntegerInstance());
        JFreeChart jfreechart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, meterplot, false);
        jfreechart.setBackgroundPaint(Color.WHITE);
        return jfreechart;
    }

    public String getActiveProcessByEntityId() throws Exception {
        return NONE;
    }

    /**
     * 得到设备当前的CPU使用率.
     * 
     * @return
     * @throws Exception
     */
    public String getCpuUsageByEntityId() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        EntitySnap snap = NetworkSnapManager.getInstance().getEntitySnap(entityId);
        try {
            double cpu = 0.0;
            if (snap != null && snap.getCpu() > 0) {
                cpu = snap.getCpu() * 100;
                ChartUtilities.writeChartAsPNG(response.getOutputStream(),
                        createChart(getString("cpuUtility", "CPU Usage"), new DefaultValueDataset(cpu)), 150, 150);
            } else {
                FileUtils.copy(ServletActionContext.getServletContext().getResourceAsStream("/images/NA.gif"),
                        response.getOutputStream());
            }
        } catch (Exception ex) {
            FileUtils.copy(ServletActionContext.getServletContext().getResourceAsStream("/images/NA.gif"),
                    response.getOutputStream());
        }
        return NONE;
    }

    public String getDiskByEntityId() throws Exception {
        return NONE;
    }

    /**
     * 获取当前机器的磁盘使用率.
     * 
     * @return
     * @throws Exception
     */
    public String getDiskUsageByEntityId() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        EntitySnap snap = NetworkSnapManager.getInstance().getEntitySnap(entityId);
        try {
            double disk = 0.0;
            if (snap != null && snap.getDisk() > 0) {
                disk = snap.getDisk() * 100;
                ChartUtilities.writeChartAsPNG(response.getOutputStream(),
                        createChart(getString("diskUtility", "Disk Usage"), new DefaultValueDataset(disk)), 150, 150);
            } else {
                FileUtils.copy(ServletActionContext.getServletContext().getResourceAsStream("/images/NA.gif"),
                        response.getOutputStream());
            }

        } catch (Exception ex) {
            FileUtils.copy(ServletActionContext.getServletContext().getResourceAsStream("/images/NA.gif"),
                    response.getOutputStream());
        }
        return NONE;
    }

    public String getMemUsageByEntityId() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        EntitySnap snap = NetworkSnapManager.getInstance().getEntitySnap(entityId);
        try {
            double mem = 0.0;
            if (snap != null && snap.getMem() > 0) {
                mem = snap.getMem() * 100;
                ChartUtilities.writeChartAsPNG(response.getOutputStream(),
                        createChart(getString("memUtility", "MEM Usage"), new DefaultValueDataset(mem)), 150, 150);
            } else {
                FileUtils.copy(ServletActionContext.getServletContext().getResourceAsStream("/images/NA.gif"),
                        response.getOutputStream());
            }
        } catch (Exception ex) {
            FileUtils.copy(ServletActionContext.getServletContext().getResourceAsStream("/images/NA.gif"),
                    response.getOutputStream());
        }
        return NONE;
    }

    public String getPortsByEntityId() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        try {
            List<Port> ports = portService.getPortByEntity(entityId);
            Port port = null;
            writer.write("<table width=100% cellpacing=5 cellpadding=0><tr><td width=70>"
                    + getResourceString("EntityPortalAction.SerialNumber", "com.topvision.ems.resources.resources")
                    + "</td><td>" + getResourceString("COMMON.description", "com.topvision.ems.resources.resources")
                    + "</td><td width=70>" + getResourceString("COMMON.type", "com.topvision.ems.resources.resources")
                    + "</td><td width=80 align=center>"
                    + getResourceString("EntityPortalAction.WorkStatus", "com.topvision.ems.resources.resources")
                    + "</td><td width=80 align=right>"
                    + getResourceString("EntityPortalAction.Rate", "com.topvision.ems.resources.resources")
                    + "</td></tr>");
            for (int i = 0; i < ports.size(); i++) {
                port = ports.get(i);
                writer.write("<tr><td>");
                writer.write(String.valueOf(port.getIfIndex()));
                writer.write("</td><td>");
                writer.write(port.getIfDescr());
                writer.write("</td><td>");
                writer.write(port.getIfTypeString());
                writer.write("</td><td align=center>");
                writer.write(port.getIfOperStatusString());
                writer.write("</td><td align=right>");
                writer.write(port.getIfSpeedString());
                writer.write("</td><td></tr>");
            }
            writer.write("</table>");
        } catch (Exception ex) {
            logger.error("Get Response Time for entity.", ex);
            throw ex;
        }
        return NONE;
    }

    public String getRecentAlarmByEntityId() throws Exception {
        try {
            Map<String, String> map = new HashMap<String, String>(1);
            map.put("entityId", String.valueOf(entityId));
            map.put("host", ip);
            map.put("limit", "5");
            List<Alert> alerts = alertService.selectByMap(map);
            HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
            int size = alerts == null ? 0 : alerts.size();
            Writer writer = ServletActionContext.getResponse().getWriter();
            if (size == 0) {
                writer.write(
                        "<table width=100% height=40><tr><td align=center><img src=\"../images/fault/normal.gif\" hspace=5 border=0 align=absmiddle>");
                writer.write(getString("no.alarm"));
                writer.write("</td></tr></table>");
            } else {
                writer.write("<table width=100%>");
                writer.write("<tr><th>"
                        + getResourceString("EntityPortalAction.Level", "com.topvision.ems.resources.resources")
                        + "</th><th>" + getResourceString("COMMON.type", "com.topvision.ems.resources.resources")
                        + "</th><th>"
                        + getResourceString("EntityPortalAction.Content", "com.topvision.ems.resources.resources")
                        + "</th><th>" + getResourceString("my97.timeStr", "com.topvision.ems.resources.resources")
                        + "</th></tr>");
                for (Alert alert : alerts) {
                    writer.write("<tr><td align=left>");
                    writer.write(getString(Level.getNameByLevel(alert.getLevelId())));
                    writer.write("</td><td>");
                    writer.write(alert.getTypeName());
                    writer.write("</td><td>");
                    writer.write(alert.getMessage());
                    writer.write("</td><td>");
                    writer.write(DATE_FORMAT.format(alert.getLastTime()));
                    writer.write("</td></tr>");
                }
                if (alerts.size() >= 5) {
                    writer.write("<tr><td colspan=4 align=right>");
                    writer.write("<a href=\"#\" onclick=\"viewMoreAlarm()\">");
                    writer.write(getString("more.alarm"));
                    writer.write("</a></td></tr>");
                }
                writer.write("</table>");
            }
        } catch (Exception ex) {
            logger.error("Get Recent Entity Alarm.", ex);
            throw ex;
        }
        return NONE;
    }

    public String getResponseByAddress() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        try {
            int time = -1;
            if (refresh) {
            } else {
                EntitySnap snap = NetworkSnapManager.getInstance().getEntitySnap(entityId);
                if (snap != null) {
                    time = snap.getDelay();
                }
            }
            writer.write("<center><table cellpacing=5 cellpadding=0>");
            if (time < 0) {
                writer.write(
                        "<tr><td height=30><img src=\"..\\images\\event\\offline.gif\" border=0 hspace=3 align=absmiddle>"
                                + getResourceString("EntityPortalAction.DeviceNotRespond",
                                        "com.topvision.ems.resources.resources")
                                + "</td>");
            } else {
                // 小于一毫秒显示一毫秒
                if (time < 2) {
                    time = 1;
                }
                writer.write("<tr><td><img src=\"..\\images\\");
                if (time >= 1000) {
                    writer.write(
                            time / 1000 + ".gif\" hspace=3 border=0 align=asbmiddle></td><td><img src=\"..\\images\\");
                    time = time - 1000 * (time / 1000);
                }
                writer.write(time / 100 + ".gif\" hspace=3 border=0 align=asbmiddle></td><td><img src=\"..\\images\\");
                time = time - 100 * (time / 100);
                writer.write(time / 10 + ".gif\" hspace=3  border=0 align=asbmiddle></td><td><img src=\"..\\images\\");
                time = time - 10 * (time / 10);
                writer.write(time + ".gif\" hspace=3 border=0 align=asbmiddle></td>");
                writer.write("<td style=\"padding-top:16px;font-size:16pt;font-weight:bold;color:#666666\">ms</td>");
            }
            writer.write("</table></center>");
        } catch (Exception ex) {
            logger.error("Get Response Time for entity.", ex);
            writer.write("<center><table cellpacing=5 cellpadding=0><tr><td>"
                    + getResourceString("EntityPortalAction.DeviceNotRespond", "com.topvision.ems.resources.resources")
                    + "</td></tr></table></center>");
            ;
        }
        return NONE;
    }

    public String getSoftwareByEntityId() throws Exception {
        return NONE;
    }

    private String getString(String key, String... strings) {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.network.resources");
        try {
            return resourceManager.getNotNullString(key, strings);
        } catch (ResourceNotFoundException e) {
            return key;
        }
    }

    public String getSysuptimeByEntity() throws Exception {
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        Writer writer = ServletActionContext.getResponse().getWriter();
        String sysUpTime = null;
        EntitySnap snap = NetworkSnapManager.getInstance().getEntitySnap(entityId);
        if (snap != null) {
            sysUpTime = snap.getSysUpTime();
        }
        writer.write("<div align=center>");
        if (sysUpTime != null) {
            sysUpTime = sysUpTime.replaceAll("days", getString("label.days")).replaceAll("day", getString("label.day"));
            for (int i = 0; i < sysUpTime.length(); i++) {
                if (Character.isDigit(sysUpTime.charAt(i))) {
                    writer.write("<img border=0 hspace=3 src=\"../images/");
                    writer.write(sysUpTime.charAt(i));
                    writer.write(".gif\">");
                } else {
                    writer.write("<font style=\"font-color:#666666;font-weight:bold;font-size:20px;\">");
                    writer.write(sysUpTime.charAt(i));
                    writer.write("</font>");
                }
            }
        } else {
            writer.write("<img border=0 src=\"../images/NA.gif>");
        }
        writer.write("</div>");
        writer.flush();
        return NONE;
    }

    public String showEntitySnapJsp() throws Exception {
        if (productType != null) {
            EntityType et = entityTypeService.getEntityType(productType);
            response.sendRedirect(
                    String.format("/%s/entityPortal.tv?module=1&productId=" + productId + "&productType=" + productType,
                            et.getModule()));
        } else {
            entity = entityService.getEntity(entityId);
            if (entityTypeService.isOnu(entity.getTypeId())) {
                response.sendRedirect(String.format("/onu/showOnuPortal.tv?module=1&onuId=%d", entity.getEntityId()));
            } else {
                if (!entity.isStatus()) {
                    response.sendRedirect(String.format("%s/entityPortalCancel.tv?module=1&entityId=%d",
                            entity.getModulePath(), entityId));
                } else {
                    response.sendRedirect(
                            String.format("%s/entityPortal.tv?module=1&entityId=%d", entity.getModulePath(), entityId));
                }
            }
        }
        return NONE;
    }

    public String showUnknownEntityJsp() {
        entity = entityService.getEntity(entityId);
        snmpParam = entityService.getSnmpParamByEntity(entityId);
        return SUCCESS;
    }

    public String isEntityExist() throws Exception {
        JSONObject json = new JSONObject();
        try {
            entity = entityService.getEntity(entityId);
            if (entity == null) {
                json.put("result", false);
            } else {
                json.put("result", true);
            }

        } catch (Exception ex) {
            logger.error("Show Entity detail.", ex);
        }

        writeDataToAjax(json);

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

    public int getWidth() {
        return width;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setHostServices(List<HostService> hostServices) {
        this.hostServices = hostServices;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPortService(PortService portService) {
        this.portService = portService;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getProductType() {
        return productType;
    }

    public void setProductType(Long productType) {
        this.productType = productType;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public EntityTypeService getEntityTypeService() {
        return entityTypeService;
    }

    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    public Entity getEntity() {
        return entity;
    }

    public int getHeight() {
        return height;
    }

    public List<HostService> getHostServices() {
        return hostServices;
    }

    public String getIp() {
        return ip;
    }
}
