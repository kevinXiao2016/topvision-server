package com.topvision.platform.action;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.EngineServerStatus;
import com.topvision.framework.exception.engine.EngineMgrDisconnectException;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.exception.FtpConnectException;
import com.topvision.platform.service.EngineServerService;

import net.sf.json.JSONObject;

@Controller("engineServerAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EngineServerAction extends BaseAction {

    private static final long serialVersionUID = -5011601165426426633L;
    @Autowired
    private EngineServerService engineServerService;

    private JSONObject engineServerJson;
    private JSONObject engineServerStatusJson;

    private int id;
    private Integer[] ids;
    private String name;
    private String ip;
    private int port;
    private int xmx;
    private int xms;
    private String note;
    private String type;
    private int adminStatus;
    private int linkStatus;

    /**
     * 获取所有分布式采集器的列表
     * 
     * @return
     */
    public String loadEngineServerList() {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            List<EngineServer> engineServers = engineServerService.getEngineServerList();
            json.put("engineServerNumber", engineServers.size());
            json.put("data", engineServers);
            json.put("success", true);
        } catch (FtpConnectException e) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 展示添加分布式采集器页面
     * 
     * @return
     */
    public String showAddEngineServer() {
        return SUCCESS;
    }

    /**
     * 添加分布式采集器
     * 
     * @return
     */
    public String addEngineServer() {
        EngineServer engineServer = new EngineServer(name, ip, port, xmx, xms, note, type);
        engineServer.setLinkStatus(EngineServer.DISCONNECT);
        engineServer.setAdminStatus(EngineServer.START);
        if (engineServerService.checkEngineServerExist(engineServer)) {
            writeDataToAjax("engineExists");
        } else {
            engineServerService.addEngineServer(engineServer);
        }
        return NONE;
    }

    /**
     * 展示修改分布式采集器页面
     * 
     * @return
     */
    public String showModifyEngineServer() {
        EngineServer engineServer = engineServerService.getEngineServerById(id);
        engineServerJson = JSONObject.fromObject(engineServer);
        return SUCCESS;
    }

    /**
     * 重启分布式采集器
     * 
     * @return
     */
    public String reStartEngineServer() {
        engineServerService.reStartEngineServer(id);
        return NONE;
    }

    /**
     * 修改分布式采集器参数
     * 
     * @return
     */
    public String modifyEngineServer() {
        EngineServer engineServer = new EngineServer(name, ip, port, xmx, xms, note, type);
        engineServer.setId(id);
        if (engineServerService.checkEngineServerExist(engineServer)) {
            writeDataToAjax("engineExists");
        } else {
            if (id == 1) {
                engineServerService.modifyLocalEngineServer(engineServer);
            } else {
                try {
                    engineServerService.modifyEngineServer(engineServer);
                } catch (EngineMgrDisconnectException e) {
                    logger.info("EngineMgrDisconnectException:" + ip);
                    writeDataToAjax("engineMgrDisconnect");
                }
            }
        }
        return NONE;
    }

    /**
     * 删除分布式采集器
     * 
     * @return
     * @throws IOException
     */
    public String deleteEngineServer() {
        engineServerService.deleteEngineServer(id);
        return NONE;
    }

    /**
     * 启用分布式采集器
     * 
     * @return
     * @throws IOException
     */
    public String startEngineServer() {
        engineServerService.startEngineServer(id);
        return NONE;
    }

    /**
     * 停用分布式采集器
     * 
     * @return
     * @throws IOException
     */
    public String stopEngineServer() {
        engineServerService.stopEngineServer(id);
        return NONE;
    }

    /**
     * 加载分布式采集器的状态
     * 
     * @return
     * @throws IOException
     */
    public String loadEngineServerStatuss() {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            json.put("success", true);
            List<Integer> engineServerIds = Arrays.asList(ids);
            List<EngineServerStatus> engineServerStatuss = engineServerService.getEngineServerStatuss(engineServerIds);
            json.put("statuss", engineServerStatuss);
            json.put("number", engineServerStatuss.size());
        } catch (FtpConnectException e) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public EngineServerService getEngineServerService() {
        return engineServerService;
    }

    public void setEngineServerService(EngineServerService engineServerService) {
        this.engineServerService = engineServerService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the xmx
     */
    public int getXmx() {
        return xmx;
    }

    /**
     * @param xmx the xmx to set
     */
    public void setXmx(int xmx) {
        this.xmx = xmx;
    }

    /**
     * @return the xms
     */
    public int getXms() {
        return xms;
    }

    /**
     * @param xms the xms to set
     */
    public void setXms(int xms) {
        this.xms = xms;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(int adminStatus) {
        this.adminStatus = adminStatus;
    }

    public int getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(int linkStatus) {
        this.linkStatus = linkStatus;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JSONObject getEngineServerJson() {
        return engineServerJson;
    }

    public void setEngineServerJson(JSONObject engineServerJson) {
        this.engineServerJson = engineServerJson;
    }

    public Integer[] getIds() {
        return ids;
    }

    public void setIds(Integer[] ids) {
        this.ids = ids;
    }

    public JSONObject getEngineServerStatusJson() {
        return engineServerStatusJson;
    }

    public void setEngineServerStatusJson(JSONObject engineServerStatusJson) {
        this.engineServerStatusJson = engineServerStatusJson;
    }

}
