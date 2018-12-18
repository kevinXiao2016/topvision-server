package com.topvision.ems.cmc.syslog.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.syslog.facade.domain.CmcSyslogServerEntry;
import com.topvision.ems.cmc.syslog.service.CmcSyslogServerService;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.web.struts2.BaseAction;

@Controller("cmcSyslogServerAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcSyslogServerAction extends BaseAction {
	private static final long serialVersionUID = -4813987216309795410L;
	@Resource(name = "cmcSyslogServerService")
	private CmcSyslogServerService cmcSyslogServerService;
	
	private Long entityId;
	private int topCcmtsSyslogServerIndex;
	private String topCcmtsSyslogServerIpAddr;
	private int topCcmtsSyslogServerIpPort;
	private String indexs;
	private String cmcType;
	private Long cmcId;

    /**
     * 展示Syslog管理页面
     * 
     * @return String
     */
    public String showSyslogManagement() {
        return SUCCESS;
    }
    
    /**
     * 展示Syslog server管理页面
     * 
     * @return String
     */
    public String showSyslogServer() {
        return SUCCESS;
    }
    
    /**
     * 获取syslog服务器列表
     * @return
     */
    public String loadSyslogServerList(){
       Map<String, Object> json = new HashMap<String, Object>();
        try {
            List<CmcSyslogServerEntry> cmcSyslogServers = cmcSyslogServerService.getCmcSyslogServer(entityId);
            json.put("cmcSyslogServerNumber", cmcSyslogServers.size());
            json.put("data", cmcSyslogServers);
            json.put("success", true);
        } catch (Exception e) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }
    
    /**
     * 刷新设备数据
     * @return
     */
    public String refreshEntityData(){
        List<CmcSyslogServerEntry> cmcSyslogServerEntrys = cmcSyslogServerService.getEntitySyslogServer(entityId);
        //刷新数据库数据
        for(CmcSyslogServerEntry cmcSyslogServerEntry : cmcSyslogServerEntrys){
            cmcSyslogServerEntry.setEntityId(entityId);
        }
        cmcSyslogServerService.refreshDatabase(cmcSyslogServerEntrys, entityId);
        return NONE;
    }
    
    /**
     * 添加syslog服务器
     * @return
     * @throws Exception 
     */
    public String addSyslogServer() throws Exception{
        //Map<String, Object> json = new HashMap<String, Object>();
        int index = 0;
        if(indexs!=null && !indexs.equals("")){
            String[] indexsStr = indexs.split(",");
            int[] indexArray = new int[indexsStr.length];
            for(int i=0; i<indexsStr.length; i++){
                indexArray[i] = Integer.valueOf(indexsStr[i]);
            }
            index = CmcIndexUtils.generateIndex(indexArray, 0, 4);
        }
        CmcSyslogServerEntry cmcSyslogServerEntry = new CmcSyslogServerEntry();
        cmcSyslogServerEntry.setEntityId(entityId);
        cmcSyslogServerEntry.setTopCcmtsSyslogServerIndex(index);
        cmcSyslogServerEntry.setTopCcmtsSyslogServerIpAddr(topCcmtsSyslogServerIpAddr);
        cmcSyslogServerService.insertCmcSyslogServer(cmcSyslogServerEntry);
        return NONE;
    }
    
    public String modifySyslogServer(){
        CmcSyslogServerEntry cmcSyslogServerEntry = new CmcSyslogServerEntry();
        cmcSyslogServerEntry.setEntityId(entityId);
        cmcSyslogServerEntry.setTopCcmtsSyslogServerIndex(topCcmtsSyslogServerIndex);
        cmcSyslogServerEntry.setTopCcmtsSyslogServerIpAddr(topCcmtsSyslogServerIpAddr);
        cmcSyslogServerService.modifyCmcSyslogServer(cmcSyslogServerEntry);
        return NONE;
    }
    
    public String deleteSyslogServer(){
        CmcSyslogServerEntry cmcSyslogServerEntry = new CmcSyslogServerEntry();
        cmcSyslogServerEntry.setEntityId(entityId);
        cmcSyslogServerEntry.setTopCcmtsSyslogServerIndex(topCcmtsSyslogServerIndex);
        cmcSyslogServerService.deleteCmcSyslogServer(cmcSyslogServerEntry);
        return NONE;
    }

    public CmcSyslogServerService getCmcSyslogServerService() {
        return cmcSyslogServerService;
    }

    public void setCmcSyslogServerService(CmcSyslogServerService cmcSyslogServerService) {
        this.cmcSyslogServerService = cmcSyslogServerService;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public int getTopCcmtsSyslogServerIndex() {
        return topCcmtsSyslogServerIndex;
    }

    public void setTopCcmtsSyslogServerIndex(int topCcmtsSyslogServerIndex) {
        this.topCcmtsSyslogServerIndex = topCcmtsSyslogServerIndex;
    }

    public String getTopCcmtsSyslogServerIpAddr() {
        return topCcmtsSyslogServerIpAddr;
    }

    public void setTopCcmtsSyslogServerIpAddr(String topCcmtsSyslogServerIpAddr) {
        this.topCcmtsSyslogServerIpAddr = topCcmtsSyslogServerIpAddr;
    }

    public int getTopCcmtsSyslogServerIpPort() {
        return topCcmtsSyslogServerIpPort;
    }

    public void setTopCcmtsSyslogServerIpPort(int topCcmtsSyslogServerIpPort) {
        this.topCcmtsSyslogServerIpPort = topCcmtsSyslogServerIpPort;
    }
    
    public String getIndexs() {
        return indexs;
    }

    public void setIndexs(String indexs) {
        this.indexs = indexs;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getCmcType() {
        return cmcType;
    }

    public void setCmcType(String cmcType) {
        this.cmcType = cmcType;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }
}
