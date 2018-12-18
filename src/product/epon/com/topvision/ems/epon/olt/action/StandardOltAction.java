/***********************************************************************
 * $Id: StandardOltAction.java,v1.0 2015-9-9 下午4:52:05 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.domain.Onu;
import com.topvision.ems.epon.igmp.domain.IgmpMcOnuTable;
import com.topvision.ems.epon.igmp.service.OltIgmpService;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.utils.OnuTypeConvertor;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.service.UserPreferencesService;

/**
 * @author flack
 * @created @2015-9-9-下午4:52:05
 * 处理标准OLT的展示和业务
 */
@Controller("standardOltAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StandardOltAction extends BaseAction {
    private static final long serialVersionUID = 7576857363786316670L;
    private Long entityId;
    private OltAttribute oltAttribute;
    private Entity entity;
    private SnmpParam snmpParam;
    private String displayStyle;
    private String oltSoftVersion;
    private JSONArray ponListObject;
    private JSONArray slotPonObject;
    private List<OltOnuAttribute> oltOnuList;
    private JSONArray entityList;
    //用于下级设备视图展示下级设备树
    private JSONArray subListArray;
    private JSONArray onuIgmpMode;
    private JSONObject layout;

    private Long ponId;
    private Long slotId;
    private Integer llid;
    private String onuName;
    private String macAddress;
    private String ip;
    private Integer type = 1;
    private String entityName;
    private Long onuId;

    @Autowired
    protected OltService oltService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltSlotService oltSlotService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private OltIgmpService oltIgmpService;
    @Autowired
    private UserPreferencesService userPreferencesService;
    @Autowired
    private OltSniService oltSniService;
    @Autowired
    private OltPonService oltPonService;

    /**
     * 展示标准OLT的配置页面
     * @return
     */
    public String showStandardOltConfig() {
        oltAttribute = oltService.getOltAttribute(entityId);

        entity = entityService.getEntity(entityId);
        snmpParam = entityService.getSnmpParamByEntity(entityId);
        //处理读写共同体中的特殊字符'和"
        snmpParam.setCommunity(snmpParam.getCommunity().replace("\"", "&quot;"));
        snmpParam.setWriteCommunity(snmpParam.getWriteCommunity().replace("\"", "&quot;"));
        return SUCCESS;
    }

    /**
     * 展示端口列表页面
     * @return
     */
    public String showOltPortList() {
        //entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    /**
     * 展示标准OLT的下级设备视图页面
     * @return
     */
    public String showStandardOltOnuView() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        displayStyle = uc.getMacDisplayStyle();
        // /得到该OLT的信息
        entity = entityService.getEntity(entityId);
        oltSoftVersion = entityService.getDeviceVersion(entityId);
        // 得到PON板信息？@bravin
        List<OltSlotAttribute> oltPonList = oltSlotService.getOltPonSlotList(entityId);
        ponListObject = JSONArray.fromObject(oltPonList);
        // 得到PON口信息
        List<OltPonAttribute> oltPonAttributes = new ArrayList<OltPonAttribute>();
        for (OltSlotAttribute oltSlotAttribute : oltPonList) {
            oltPonAttributes.addAll(oltSlotService.getSlotPonList(oltSlotAttribute.getSlotId()));
        }
        slotPonObject = JSONArray.fromObject(oltPonAttributes);
        // 得到ONU列表信息
        oltOnuList = onuService.getOnuList(entityId, slotId, ponId, llid, macAddress, onuName);
        // initialize entityList
        entityList = new JSONArray();
        Map<Long, Map<String, JSONArray>> dataMap = new TreeMap<Long, Map<String, JSONArray>>();
        for (OltOnuAttribute o : oltOnuList) {
            JSONObject json = new JSONObject();
            json.put("entityId", o.getOnuId());
            json.put("parentId", o.getEntityId());
            String mac = MacUtils.convertMacToDisplayFormat(o.getOnuMac(), uc.getMacDisplayStyle());
            json.put("onuMac", mac);
            if (o.getOnuPreType() != null) {
                json.put("entityType", o.getOnuPreType());
            } else {
                json.put("entityType", EponConstants.UNKNOWN_ONU_TYPE);
            }
            json.put("typeId", o.getTypeId());
            json.put("adminStatus", o.getOnuAdminStatus());
            json.put("entityName", o.getOnuName());
            json.put("oprationStatus", o.getOnuOperationStatus());
            entityList.add(json);

            Long onuIndex = o.getOnuIndex();
            Long slotNo = EponIndex.getSlotNo(onuIndex);
            String portString = EponIndex.getPortStringByIndex(onuIndex).toString();
            if (dataMap.containsKey(slotNo)) {
                Map<String, JSONArray> portData = dataMap.get(slotNo);
                if (portData.containsKey(portString)) {
                    portData.get(portString).add(json);
                } else {
                    JSONArray portJson = new JSONArray();
                    portJson.add(json);
                    portData.put(portString, portJson);
                }
            } else {
                Map<String, JSONArray> portData = new LinkedHashMap<String, JSONArray>();
                JSONArray portJson = new JSONArray();
                portJson.add(json);
                portData.put(portString, portJson);
                dataMap.put(slotNo, portData);
            }
        }
        subListArray = new JSONArray();
        for (Entry<Long, Map<String, JSONArray>> slotEntry : dataMap.entrySet()) {
            JSONObject slotObject = new JSONObject();
            Map<String, JSONArray> portMap = slotEntry.getValue();
            JSONArray portArray = new JSONArray();
            for (Entry<String, JSONArray> portEntry : portMap.entrySet()) {
                JSONObject portObject = new JSONObject();
                portObject.put("portNo", portEntry.getKey());
                portObject.put("children", portEntry.getValue());
                portArray.add(portObject);
            }
            slotObject.put("slotNo", slotEntry.getKey());
            slotObject.put("children", portArray);
            subListArray.add(slotObject);
        }

        // 获取所有ONU的IGMP模式
        List<IgmpMcOnuTable> tempOnuIgmpList = oltIgmpService.getIgmpMcOnuInfoList(entityId);
        List<Long> tempOnuIgmpMode = new ArrayList<Long>();
        if (tempOnuIgmpList != null && tempOnuIgmpList.size() != 0) {
            for (IgmpMcOnuTable anOnuIgmp : tempOnuIgmpList) {
                tempOnuIgmpMode.add(anOnuIgmp.getOnuIndex());
                tempOnuIgmpMode.add(anOnuIgmp.getTopMcOnuMode().longValue());
            }
            onuIgmpMode = JSONArray.fromObject(tempOnuIgmpMode);
        } else {
            onuIgmpMode = JSONArray.fromObject(false);
        }
        //加载用户视图信息
        layout = this.getUserView();
        return SUCCESS;
    }
    
    public String getOltPonSlotList(){
    	List<OltSlotAttribute> oltPonList = oltSlotService.getOltPonSlotList(entityId);
    	writeDataToAjax(oltPonList);
    	return NONE;
    }

    /**
     * 获取用户视图信息
     * @return
     */
    private JSONObject getUserView() {
        JSONObject view = new JSONObject();
        UserContext uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute("UserContext");
        UserPreferences up = new UserPreferences();
        up.setModule("oltFaceplateView");
        up.setUserId(uc.getUserId());
        Properties oltView = userPreferencesService.getModulePreferences(up);
        Map<String, Object> viewMap = new HashMap<String, Object>();
        viewMap.put("rightTopHeight", oltView.get("rightTopHeight"));
        viewMap.put("middleBottomHeight", oltView.get("middleBottomHeight"));
        viewMap.put("leftWidth", oltView.get("leftWidth"));
        viewMap.put("rightWidth", oltView.get("rightWidth"));
        viewMap.put("rightTopOpen", oltView.get("rightTopOpen"));
        viewMap.put("rightBottomOpen", oltView.get("rightBottomOpen"));
        viewMap.put("middleBottomOpen", oltView.get("middleBottomOpen"));
        viewMap.put("layoutToMiddle", oltView.get("layoutToMiddle"));
        viewMap.put("tabBtnSelectedIndex", oltView.get("tabBtnSelectedIndex"));
        view = JSONObject.fromObject(viewMap);
        return view;
    }

    /**
     * 显示标准OLT告警信息
     * 
     * @return String
     */
    public String showStandardOltAlert() {
        if (entityId != null && entityId > 0) {
            entity = entityService.getEntity(entityId);
        } else {
            entity = entityService.getEntityByIp(ip);
            if (entity != null) {
                // add by fanzidong,需要在展示前格式化MAC地址
                UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
                String macRule = uc.getMacDisplayStyle();
                String formatedMac = MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule);
                entity.setMac(formatedMac);
            }
            entityId = entity.getEntityId();
        }
        return SUCCESS;
    }

    /**
     * 显示标准OLT历史告警
     * 
     * @return
     */
    public String showStandardOltAlertHistory() {
        if (entityId != null && entityId > 0) {
            entity = entityService.getEntity(entityId);
        } else {
            entity = entityService.getEntityByIp(ip);
            // add by fanzidong,需要在展示前格式化MAC地址
            UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
            String macRule = uc.getMacDisplayStyle();
            String formatedMac = MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule);
            entity.setMac(formatedMac);
            entityId = entity.getEntityId();
        }
        return SUCCESS;
    }

    /**
     * 显示标准OLT性能
     * 
     * @return String
     */
    public String showStandardOltPerfView() {
        entity = entityService.getEntity(entityId);
        entityName = entity.getName();
        return SUCCESS;
    }

    /**
     * 加载SNI口信息
     * @return
     */
    public String loadSniList() {
        List<OltSniAttribute> sniList = oltSniService.getAllSniList(entityId);
        writeDataToAjax(JSONArray.fromObject(sniList));
        return NONE;
    }

    /**
     * 加载PON口信息
     * @return
     */
    public String loadPonList() {
        List<OltPonAttribute> ponList = oltPonService.getOltPonList(entityId);
        writeDataToAjax(JSONArray.fromObject(ponList));
        return NONE;
    }

    /**
     * 获取标准OLT下ONU信息
     * @return
     */
    public String loadStandardOnuInfo() {
        JSONObject json = new JSONObject();
        Onu onu = onuService.getStandardOnuInfo(onuId);
        if (onu.getOnuPreType() != null) {
            Integer onuPretype = Integer.parseInt(onu.getOnuPreType());
            if (onuPretype != null && onuPretype > 255) {
                onu.setTypeName(OnuTypeConvertor.convertTypeName(onuPretype));
            }
        }
        json.put("onu", onu);
        writeDataToAjax(json);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public OltAttribute getOltAttribute() {
        return oltAttribute;
    }

    public void setOltAttribute(OltAttribute oltAttribute) {
        this.oltAttribute = oltAttribute;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    public String getDisplayStyle() {
        return displayStyle;
    }

    public void setDisplayStyle(String displayStyle) {
        this.displayStyle = displayStyle;
    }

    public String getOltSoftVersion() {
        return oltSoftVersion;
    }

    public void setOltSoftVersion(String oltSoftVersion) {
        this.oltSoftVersion = oltSoftVersion;
    }

    public JSONArray getPonListObject() {
        return ponListObject;
    }

    public void setPonListObject(JSONArray ponListObject) {
        this.ponListObject = ponListObject;
    }

    public JSONArray getSlotPonObject() {
        return slotPonObject;
    }

    public void setSlotPonObject(JSONArray slotPonObject) {
        this.slotPonObject = slotPonObject;
    }

    public List<OltOnuAttribute> getOltOnuList() {
        return oltOnuList;
    }

    public void setOltOnuList(List<OltOnuAttribute> oltOnuList) {
        this.oltOnuList = oltOnuList;
    }

    public JSONArray getEntityList() {
        return entityList;
    }

    public void setEntityList(JSONArray entityList) {
        this.entityList = entityList;
    }

    public JSONArray getSubListArray() {
        return subListArray;
    }

    public void setSubListArray(JSONArray subListArray) {
        this.subListArray = subListArray;
    }

    public JSONArray getOnuIgmpMode() {
        return onuIgmpMode;
    }

    public void setOnuIgmpMode(JSONArray onuIgmpMode) {
        this.onuIgmpMode = onuIgmpMode;
    }

    public JSONObject getLayout() {
        return layout;
    }

    public void setLayout(JSONObject layout) {
        this.layout = layout;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Integer getLlid() {
        return llid;
    }

    public void setLlid(Integer llid) {
        this.llid = llid;
    }

    public String getOnuName() {
        return onuName;
    }

    public void setOnuName(String onuName) {
        this.onuName = onuName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

}
