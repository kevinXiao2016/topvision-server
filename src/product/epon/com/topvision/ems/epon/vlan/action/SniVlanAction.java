/***********************************************************************
 * $Id: SniVlanAction.java,v1.0 2013-10-25 下午1:50:56 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.epon.exception.SniVlanConfigException;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.ems.epon.vlan.domain.OltVlanAttribute;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.TopOltVlanVifPriIpTable;
import com.topvision.ems.epon.vlan.domain.TopOltVlanVifSubIpTable;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.ems.epon.vlan.service.SniVlanService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.domain.OperationLog;

/**
 * @author flack
 * @created @2013-10-25-下午1:50:56
 *
 */
@Controller("sniVlanAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SniVlanAction extends AbstractEponAction {
    private static final long serialVersionUID = -232489828426175470L;
    private final Logger logger = LoggerFactory.getLogger(SniVlanAction.class);

    private Long sniId;
    private String vlanTagTpid;
    private Integer vlanTagPriority;
    private Integer vlanPVid;
    private Integer vlanIndex;
    private String oltVlanName;
    private JSONObject oltVlanObject = new JSONObject();
    private String taggedPort;
    private String untaggedPort;
    private String sniName;
    private List<VlanAttribute> oltVlanConfigList;
    private Boolean modifyFlag;
    private String taggedPortIndexList;
    private String untaggedPortIndexList;
    private JSONArray oltVlanListObject = new JSONArray();
    private Integer topMcFloodMode;
    private String topOltVifPriIpAddr;
    private String topOltVifPriIpMask;
    private JSONArray vlanVifPriIpJson = new JSONArray();
    private JSONArray vlanVifSubIpJson = new JSONArray();
    private Integer topOltVifSubIpSeqIdx;
    private String topOltVifSubIpAddr;
    private String topOltVifSubIpMask;
    private String vidListStr;
    private String deleteVlans;
    private JSONArray vidListJson = new JSONArray();
    private Integer operationResult;
    private Integer vlanVifFlag;// 用于标识页面跳转：1为主ip设置，2为新增子ip，3为修改子ip
    private Integer portRealIndex;
    private Long sniIndex;
    private Integer vlanMode;
    // 用于操作日志国际化时表示来源的变量
    private String source;
    private Entity entity;
    private PortVlanAttribute portVlanAttribute;
    private OltVlanAttribute oltVlanAttribute;
    private VlanAttribute vlanAttribute;
    @Autowired
    private EntityService entityService;
    @Autowired
    private SniVlanService sniVlanService;
    @Autowired
    private OltSniService oltSniService;
    @Autowired
    private DeviceVersionService deviceVersionService;

    /**
     * 显示SNIVLAN配置管理
     * 
     * @return String
     */
    public String showSniVlanMgmt() {
        entity = entityService.getEntity(entityId);
        return SUCCESS;
    }

    /**
     * 获取SNI口基本配置信息
     * 
     * @return
     * @throws Exception
     */
    public String getSniPortVlanAttribute() throws Exception {
        Boolean isSupport = deviceVersionService.isFunctionSupported(entityId, "sniVlanEntry");
        portVlanAttribute = sniVlanService.getSniPortVlanAttribute(entityId, sniIndex);
        sniId = portVlanAttribute.getPortId();
        if (portVlanAttribute != null) {
            portVlanAttribute.setPortName(sniName);
        }
        List<VlanAttribute> oltVlanConfigList = sniVlanService.getOltVlanConfigList(entityId);
        oltVlanListObject = JSONArray.fromObject(oltVlanConfigList);
        if (isSupport) {
            return "sniPortEntry";
        } else {
            return "sniVlanConfig";
        }

    }

    /**
     * 修改SNI口基本配置信息
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "sniVlanAction", operationName = "updateSniPortVlanAttribute")
    public String updateSniPortVlanAttribute() throws Exception {
        String result;
        try {
            // 兼容页面配置“请选择”的情况,表示不进行任何修改操作
            if (vlanTagTpid != null && "0".equals(vlanTagTpid)) {
                vlanTagTpid = "0x8100";
            }
            sniVlanService.updateSniPortVlanAttribute(sniId, vlanPVid, vlanTagPriority, vlanTagTpid, vlanMode,
                    sniIndex, entityId);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SniVlanConfigException sce) {
            result = sce.getMessage();
            operationResult = OperationLog.FAILURE;
            logger.debug("sni vlan config error:{}", sce);
        } catch (SnmpException sse) {
            result = getString("Business.snmpWrong", "epon");
            operationResult = OperationLog.FAILURE;
            logger.debug("sni vlan config snmp error:{}", sse);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 从设备获取SNI口VLAN基本配置信息
     * 
     * @return
     * @throws Exception
     */
    public String refreshSniPortVlanAttribute() throws Exception {
        String result;
        try {
            sniVlanService.refreshSniPortVlanAttribute(entityId, sniIndex);
            result = "success";
        } catch (SnmpException sse) {
            result = getString("Business.snmpWrong", "epon");
            logger.error("", sse);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * VLAN视图中获取VLAN列表数据
     * 
     * @return
     * @throws Exception
     */
    public String loadOltVlanConfigList() throws Exception {
        Map<String, Object> vlanJson = new HashMap<String, Object>();
        List<VlanAttribute> oltVlanConfigList = sniVlanService.getOltVlanConfigList(entityId);
        for (VlanAttribute vlanAttribute : oltVlanConfigList) {
            try {
                TopOltVlanVifPriIpTable vlanVifPriIp = new TopOltVlanVifPriIpTable();
                vlanVifPriIp = sniVlanService.getVlanVifPriIp(entityId, vlanAttribute.getVlanIndex());
                if (vlanVifPriIp != null) {
                    vlanAttribute.setVlanVifFlag(1);
                } else {
                    vlanAttribute.setVlanVifFlag(0);
                }
                List<String> tagPortNameList = handleVlanPortName(entityId, vlanAttribute.getTaggedPortIndexList());
                vlanAttribute.setTagPortNameList(tagPortNameList);
                List<String> unTagPortNameList = handleVlanPortName(entityId, vlanAttribute.getUntaggedPortIndexList());
                vlanAttribute.setUnTagPortNameList(unTagPortNameList);
            } catch (Exception e) {
                logger.debug("get vlanAttribute pvid or portlist error", e.getMessage());
            }
        }
        logger.debug("vlanListData:{}", oltVlanConfigList);
        vlanJson.put("data", oltVlanConfigList);
        writeDataToAjax(JSONObject.fromObject(vlanJson));
        return NONE;
    }

    /**
     * 获取VLAN中的端口名称
     * @param entityId
     * @param indexList
     * @return
     */
    private List<String> handleVlanPortName(Long entityId, List<Long> indexList) {
        List<String> portNameList = new ArrayList<String>();
        for (Long portIndex : indexList) {
            OltSniAttribute sniAttr = oltSniService.getSniAttribute(entityId, portIndex);
            if (sniAttr != null) {
                portNameList.add(sniAttr.getSniDisplayName());
            } else {
                //如果没有取到SNI信息,则使用变通表示方法(如存在PON口索引)
                portNameList.add(EponIndex.getPortStringByIndex(portIndex).toString());
            }
        }
        return portNameList;
    }

    /**
     * 设置VLAN名称页面跳转
     * 
     * @return String
     */
    public String showVlanNameJsp() {
        entity = entityService.getEntity(entityId);
        VlanAttribute v = sniVlanService.getOltVlanConfig(entityId, vlanIndex);
        if (v != null) {
            oltVlanObject = JSONObject.fromObject(v);
        } else {
            oltVlanObject = JSONObject.fromObject(false);
        }
        return SUCCESS;
    }

    /**
     * 创建VLAN虚接口页面跳转
     * 
     * @return String
     */
    public String createVlanVifJsp() {
        return SUCCESS;
    }

    /**
     * 新增VLAN虚接口子IP页面跳转
     * 
     * @return String
     */
    public String addVlanVifSubIpJsp() {
        return SUCCESS;
    }

    /**
     * 修改VLAN虚接口子IP页面跳转
     * 
     * @return String
     */
    public String modifyVlanVifSubIpJsp() {
        return SUCCESS;
    }

    /**
     * 创建VLAN虚接口
     * 
     * @return String
     * @throws Exception
     */
    public String createVlanVif() throws Exception {
        String result;
        try {
            sniVlanService.setVlanPriIp(entityId, vlanIndex, topOltVifPriIpAddr, topOltVifPriIpMask);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            result = e.getErrorStatusText();
            operationResult = OperationLog.FAILURE;
            logger.debug("vlanView createVlanVif error:{}", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 修改VLAN虚接口主IP
     * 
     * @return String
     * @throws Exception
     */
    public String modifyVlanVifPriIp() throws Exception {
        String result;
        try {
            sniVlanService.modifyVlanVifPriIp(entityId, vlanIndex, topOltVifPriIpAddr, topOltVifPriIpMask);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            result = e.getErrorStatusText();
            operationResult = OperationLog.FAILURE;
            logger.debug("vlanView modifyVlanVifPriIp error:{}", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 删除VLAN虚接口
     * 
     * @return String
     * @throws Exception
     */
    public String deleteVlanVif() throws Exception {
        String result;
        try {
            sniVlanService.deleteVlanVif(entityId, vlanIndex);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            result = e.getErrorStatusText();
            operationResult = OperationLog.FAILURE;
            logger.debug("vlanView deleteVlanVif error:{}", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 新增VLAN虚接口子IP
     * 
     * @return String
     * @throws Exception
     */
    public String addVlanVifSubIp() throws Exception {
        String result;
        try {
            sniVlanService.addVlanVifSubIp(entityId, vlanIndex, topOltVifSubIpAddr, topOltVifSubIpMask);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            result = e.getErrorStatusText();
            operationResult = OperationLog.FAILURE;
            logger.debug("vlanView addVlanVifSubIp error:{}", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 修改VLAN虚接口子IP
     * 
     * @return String
     * @throws Exception
     */
    public String modifyVlanVifSubIp() throws Exception {
        String result;
        try {
            sniVlanService.modifyVlanVifSubIp(entityId, vlanIndex, topOltVifSubIpSeqIdx, topOltVifSubIpAddr,
                    topOltVifSubIpMask);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            result = e.getErrorStatusText();
            operationResult = OperationLog.FAILURE;
            logger.debug("vlanView modifyVlanVifSubIp error:{}", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 删除VLAN虚接口子IP
     * 
     * @return String
     * @throws Exception
     */
    public String deleteVlanVifSubIp() throws Exception {
        String result;
        try {
            sniVlanService.deleteVlanVifSubIp(entityId, vlanIndex, topOltVifSubIpSeqIdx);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            result = e.getErrorStatusText();
            operationResult = OperationLog.FAILURE;
            logger.debug("vlanView deleteVlanVifSubIp error:{}", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 获取VLAN虚接口子IP
     * 
     * @return String
     * @throws Exception
     */
    public String loadVlanVifSubIp() throws Exception {
        List<TopOltVlanVifSubIpTable> vlanVifSubIp = new ArrayList<TopOltVlanVifSubIpTable>();
        vlanVifSubIp = sniVlanService.getVlanVifSubIp(entityId, vlanIndex);
        writeDataToAjax(JSONArray.fromObject(vlanVifSubIp));
        return NONE;
    }

    /**
     * 刷新VLAN虚接口数据
     * 
     */
    @OperationLogProperty(actionName = "sniVlanAction", operationName = "refreshVlanVif")
    public String refreshVlanVif() throws Exception {
        String message = "success";
        try {
            sniVlanService.refreshVlanVif(entityId);
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            logger.debug("refreshVlanVif error: {}", e);
            operationResult = OperationLog.FAILURE;
            message = e.getErrorStatusText();
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 配置VLAN虚接口页面跳转
     * 
     * @return String
     */
    public String setVlanVifJsp() {
        TopOltVlanVifPriIpTable vlanVifPriIp = new TopOltVlanVifPriIpTable();
        vlanVifPriIp = sniVlanService.getVlanVifPriIp(entityId, vlanIndex);
        vlanVifPriIpJson = JSONArray.fromObject(vlanVifPriIp);
        List<TopOltVlanVifSubIpTable> vlanVifSubIp = new ArrayList<TopOltVlanVifSubIpTable>();
        vlanVifSubIp = sniVlanService.getVlanVifSubIp(entityId, vlanIndex);
        vlanVifSubIpJson = JSONArray.fromObject(vlanVifSubIp);
        return SUCCESS;
    }

    /**
     * VLAN视图中修改VLAN名称
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "sniVlanAction", operationName = "modifyVlanName")
    public String modifyVlanName() throws Exception {
        String result;
        try {
            sniVlanService.modifyVlanName(entityId, vlanIndex, oltVlanName, topMcFloodMode);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            result = e.getErrorStatusText();
            operationResult = OperationLog.FAILURE;
            logger.debug("vlanView modifyVlanName error:{}", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * VLAN视图中获取全局VLAN属性
     * 
     * @return
     * @throws Exception
     */
    public String loadOltVlanGlobalInfo() throws Exception {
        oltVlanAttribute = sniVlanService.getOltVlanGlobalInfo(entityId);
        logger.debug("OltVlanGlobalInfo:{}", oltVlanAttribute);
        writeDataToAjax(JSONObject.fromObject(oltVlanAttribute));
        return NONE;
    }

    /**
     * VLAN视图中新增VLAN
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "sniVlanAction", operationName = "addOltVlan")
    public String addOltVlan() throws Exception {
        String result;
        VlanAttribute vlanAttribute = new VlanAttribute();
        vlanAttribute.setEntityId(entityId);
        vlanAttribute.setVlanIndex(vlanIndex);
        vlanAttribute.setOltVlanName(oltVlanName);
        vlanAttribute.setTopMcFloodMode(topMcFloodMode);
        try {
            sniVlanService.addOltVlan(vlanAttribute);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            result = e.getErrorStatusText();
            operationResult = OperationLog.FAILURE;
            logger.debug("addOltVlan error:{}", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * VLAN视图中删除VLAN
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "sniVlanAction", operationName = "deleteOltVlan")
    public String deleteOltVlan() throws Exception {
        String result = "success";
        List<Integer> vidList;
        try {
            vidList = sniVlanService.deleteOltVlan(entityId, vidListStr);
            if (vidList.size() == 0) {
                // 全部删除成功
                result = "success";
            } else {
                // 删除失败的VLAN超过10
                if (vidList.size() >= 10) {
                    result = "deleteFail";
                } else {
                    // 失败的VLAN在10个以内
                    result = vidList.toString();
                }
            }
            operationResult = OperationLog.SUCCESS;
        } catch (SnmpException e) {
            result = e.getErrorStatusText();
            operationResult = OperationLog.FAILURE;
            logger.debug("deleteOltVlan error:{}", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * VLAN视图中更新某VLAN下TAGGED和UNTAGGED列表
     * 
     * @return
     * @throws Exception
     */
    @OperationLogProperty(actionName = "sniVlanAction", operationName = "updateTagStatus")
    public String updateTagStatus() throws Exception {
        List<Long> taggedPortList = new ArrayList<Long>();
        if (!taggedPortIndexList.equals("") && taggedPortIndexList.length() > 0) {
            for (String index : taggedPortIndexList.split(",")) {
                taggedPortList.add(Long.parseLong(index));
            }
        }
        List<Long> untaggedPortList = new ArrayList<Long>();
        if (!untaggedPortIndexList.equals("") && untaggedPortIndexList.length() > 0) {
            for (String index : untaggedPortIndexList.split(",")) {
                untaggedPortList.add(Long.parseLong(index));
            }
        }
        sniVlanService.updateTagStatus(entityId, vlanIndex, taggedPortList, untaggedPortList, null);
        return NONE;
    }

    /**
     * VLAN视图中获取单条VLAN列表数据
     * 
     * @return
     * @throws Exception
     */
    public String loadOltVlanConfig() throws Exception {
        VlanAttribute vlanAttribute;
        vlanAttribute = sniVlanService.getOltVlanConfig(entityId, vlanIndex);
        logger.debug("loadSniVlanConfig:{}", vlanAttribute);
        writeDataToAjax(JSONArray.fromObject(vlanAttribute));
        return NONE;
    }

    public String showVlanBatchAdd() throws Exception {
        List<VlanAttribute> vlanList = sniVlanService.getOltVlanConfigList(entityId);
        List<Integer> list = new ArrayList<Integer>();
        for (VlanAttribute v : vlanList) {
            list.add(v.getVlanIndex());
        }
        if (list.size() == 0) {
            vidListJson = JSONArray.fromObject(false);
        } else {
            vidListJson = JSONArray.fromObject(list);
        }
        return SUCCESS;
    }

    public Long getSniId() {
        return sniId;
    }

    public void setSniId(Long sniId) {
        this.sniId = sniId;
    }

    public String getVlanTagTpid() {
        return vlanTagTpid;
    }

    public void setVlanTagTpid(String vlanTagTpid) {
        this.vlanTagTpid = vlanTagTpid;
    }

    public Integer getVlanTagPriority() {
        return vlanTagPriority;
    }

    public void setVlanTagPriority(Integer vlanTagPriority) {
        this.vlanTagPriority = vlanTagPriority;
    }

    public Integer getVlanPVid() {
        return vlanPVid;
    }

    public void setVlanPVid(Integer vlanPVid) {
        this.vlanPVid = vlanPVid;
    }

    public Integer getVlanIndex() {
        return vlanIndex;
    }

    public void setVlanIndex(Integer vlanIndex) {
        this.vlanIndex = vlanIndex;
    }

    public String getOltVlanName() {
        return oltVlanName;
    }

    public void setOltVlanName(String oltVlanName) {
        this.oltVlanName = oltVlanName;
    }

    public JSONObject getOltVlanObject() {
        return oltVlanObject;
    }

    public void setOltVlanObject(JSONObject oltVlanObject) {
        this.oltVlanObject = oltVlanObject;
    }

    public String getTaggedPortIndexList() {
        return taggedPortIndexList;
    }

    public void setTaggedPortIndexList(String taggedPortIndexList) {
        this.taggedPortIndexList = taggedPortIndexList;
    }

    public String getUntaggedPortIndexList() {
        return untaggedPortIndexList;
    }

    public void setUntaggedPortIndexList(String untaggedPortIndexList) {
        this.untaggedPortIndexList = untaggedPortIndexList;
    }

    public JSONArray getOltVlanListObject() {
        return oltVlanListObject;
    }

    public void setOltVlanListObject(JSONArray oltVlanListObject) {
        this.oltVlanListObject = oltVlanListObject;
    }

    public Integer getTopMcFloodMode() {
        return topMcFloodMode;
    }

    public void setTopMcFloodMode(Integer topMcFloodMode) {
        this.topMcFloodMode = topMcFloodMode;
    }

    public String getTopOltVifPriIpAddr() {
        return topOltVifPriIpAddr;
    }

    public void setTopOltVifPriIpAddr(String topOltVifPriIpAddr) {
        this.topOltVifPriIpAddr = topOltVifPriIpAddr;
    }

    public String getTopOltVifPriIpMask() {
        return topOltVifPriIpMask;
    }

    public void setTopOltVifPriIpMask(String topOltVifPriIpMask) {
        this.topOltVifPriIpMask = topOltVifPriIpMask;
    }

    public JSONArray getVlanVifPriIpJson() {
        return vlanVifPriIpJson;
    }

    public void setVlanVifPriIpJson(JSONArray vlanVifPriIpJson) {
        this.vlanVifPriIpJson = vlanVifPriIpJson;
    }

    public JSONArray getVlanVifSubIpJson() {
        return vlanVifSubIpJson;
    }

    public void setVlanVifSubIpJson(JSONArray vlanVifSubIpJson) {
        this.vlanVifSubIpJson = vlanVifSubIpJson;
    }

    public Integer getTopOltVifSubIpSeqIdx() {
        return topOltVifSubIpSeqIdx;
    }

    public void setTopOltVifSubIpSeqIdx(Integer topOltVifSubIpSeqIdx) {
        this.topOltVifSubIpSeqIdx = topOltVifSubIpSeqIdx;
    }

    public String getTopOltVifSubIpAddr() {
        return topOltVifSubIpAddr;
    }

    public void setTopOltVifSubIpAddr(String topOltVifSubIpAddr) {
        this.topOltVifSubIpAddr = topOltVifSubIpAddr;
    }

    public String getTopOltVifSubIpMask() {
        return topOltVifSubIpMask;
    }

    public void setTopOltVifSubIpMask(String topOltVifSubIpMask) {
        this.topOltVifSubIpMask = topOltVifSubIpMask;
    }

    public String getVidListStr() {
        return vidListStr;
    }

    public void setVidListStr(String vidListStr) {
        this.vidListStr = vidListStr;
    }

    public JSONArray getVidListJson() {
        return vidListJson;
    }

    public void setVidListJson(JSONArray vidListJson) {
        this.vidListJson = vidListJson;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public PortVlanAttribute getPortVlanAttribute() {
        return portVlanAttribute;
    }

    public void setPortVlanAttribute(PortVlanAttribute portVlanAttribute) {
        this.portVlanAttribute = portVlanAttribute;
    }

    public OltVlanAttribute getOltVlanAttribute() {
        return oltVlanAttribute;
    }

    public void setOltVlanAttribute(OltVlanAttribute oltVlanAttribute) {
        this.oltVlanAttribute = oltVlanAttribute;
    }

    public String getTaggedPort() {
        return taggedPort;
    }

    public void setTaggedPort(String taggedPort) {
        this.taggedPort = taggedPort;
    }

    public String getUntaggedPort() {
        return untaggedPort;
    }

    public void setUntaggedPort(String untaggedPort) {
        this.untaggedPort = untaggedPort;
    }

    public String getSniName() {
        return sniName;
    }

    public void setSniName(String sniName) {
        this.sniName = sniName;
    }

    public List<VlanAttribute> getOltVlanConfigList() {
        return oltVlanConfigList;
    }

    public void setOltVlanConfigList(List<VlanAttribute> oltVlanConfigList) {
        this.oltVlanConfigList = oltVlanConfigList;
    }

    public Boolean getModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(Boolean modifyFlag) {
        this.modifyFlag = modifyFlag;
    }

    @Override
    public Integer getOperationResult() {
        return operationResult;
    }

    @Override
    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public Integer getVlanVifFlag() {
        return vlanVifFlag;
    }

    public void setVlanVifFlag(Integer vlanVifFlag) {
        this.vlanVifFlag = vlanVifFlag;
    }

    public Integer getPortRealIndex() {
        return portRealIndex;
    }

    public void setPortRealIndex(Integer portRealIndex) {
        this.portRealIndex = portRealIndex;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    public VlanAttribute getVlanAttribute() {
        return vlanAttribute;
    }

    public void setVlanAttribute(VlanAttribute vlanAttribute) {
        this.vlanAttribute = vlanAttribute;
    }

    public String getDeleteVlans() {
        return deleteVlans;
    }

    public void setDeleteVlans(String deleteVlans) {
        this.deleteVlans = deleteVlans;
    }

    public Long getSniIndex() {
        return sniIndex;
    }

    public void setSniIndex(Long sniIndex) {
        this.sniIndex = sniIndex;
    }

    public Integer getVlanMode() {
        return vlanMode;
    }

    public void setVlanMode(Integer vlanMode) {
        this.vlanMode = vlanMode;
    }

}
