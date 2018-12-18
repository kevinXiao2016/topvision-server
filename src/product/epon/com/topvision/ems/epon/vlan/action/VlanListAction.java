/***********************************************************************
 * $Id: VlanListAction.java,v1.0 2016年6月6日 下午2:51:19 $
 * 
 * @author: Bravin
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onu.util.OnuUtil;
import com.topvision.ems.epon.vlan.domain.OltPortVlan;
import com.topvision.ems.epon.vlan.domain.PonVlanPortLocation;
import com.topvision.ems.epon.vlan.domain.UniPortVlan;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.ems.epon.vlan.service.SniVlanService;
import com.topvision.ems.epon.vlan.service.VlanListService;
import com.topvision.ems.message.CallbackableRequest;
import com.topvision.ems.message.LongRequestExecutorService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.platform.util.CurrentRequest;
import com.topvision.platform.util.StringUtil;

/**
 * @author Bravin
 * @created @2016年6月6日-下午2:51:19
 *
 */
@Controller("vlanListAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VlanListAction extends AbstractEponAction {
    private static final long serialVersionUID = 4473612236117745132L;
    @Autowired
    private SniVlanService sniVlanService;
    @Autowired
    private OnuService onuService;
    @Autowired
    private VlanListService vlanListService;
    @Autowired
    private LongRequestExecutorService longRequestExecutorService;
    @Autowired
    private EntityTypeService entityTypeService;
    private String vlanIdsStr;
    private String taggedVlanStr;
    private String unTaggedVlanStr;
    private String portIndexsStr;
    private Long vlanIndex;
    private VlanAttribute vlanAttribute;
    private Integer topMcFloodMode;
    private String vlanName;
    private String jconnectionId;
    private Long onuId;
    private Long ponId;
    private String targetPonIds;
    private String services;
    private Long portIndex;

    /**
     * 加载指定OLT的VLAN列表
     * 
     * @return
     */
    public String loadVlanList() {
        List<VlanAttribute> oltVlanConfigList = sniVlanService.getOltVlanConfigList(entityId);
        writeDataToAjax(oltVlanConfigList);
        return NONE;
    }

    /**
     * 删除VLAN，支持单条和多条
     * 
     * @return
     */
    public String deleteVlan() {
        List<Integer> vidList = sniVlanService.deleteOltVlan(entityId, vlanIdsStr);
        writeDataToAjax(vidList);
        return NONE;
    }

    /**
     * 加载SNI VLAN信息列表
     * 
     * @return
     */
    public String loadSniVlanList() {
        return NONE;
    }

    /**
     * 展示编辑SNI/PON端口 VLAN页面
     * 
     * @return
     */
    public String showEditPortVlans() {
        return SUCCESS;
    }

    /**
     * 修改SNI/PON口的所属tagged/untagged VLAN
     * 
     * @return
     */
    public String modifyPortVlans() {
        final List<Integer> taggedList = new ArrayList<Integer>();
        if (!taggedVlanStr.equals("") && taggedVlanStr.length() > 0) {
            for (String vlanIndex : taggedVlanStr.split(",")) {
                taggedList.add(Integer.parseInt(vlanIndex));
            }
        }
        final List<Integer> untaggedList = new ArrayList<Integer>();
        if (!unTaggedVlanStr.equals("") && unTaggedVlanStr.length() > 0) {
            for (String vlanIndex : unTaggedVlanStr.split(",")) {
                untaggedList.add(Integer.parseInt(vlanIndex));
            }
        }
        final List<Long> sniIndexs = new ArrayList<Long>();
        if (!portIndexsStr.equals("") && portIndexsStr.length() > 0) {
            for (String portIndex : portIndexsStr.split(",")) {
                sniIndexs.add(Long.parseLong(portIndex));
            }
        }

        longRequestExecutorService.executeRequest(new CallbackableRequest(jconnectionId) {

            @Override
            protected void execute() {
                vlanListService.batchApplyPortVlanConfig(entityId, sniIndexs, taggedList, untaggedList);
            }
        });

        return NONE;
    }

    public String loadOnuList() {
        String displayStyle = CurrentRequest.getCurrentUser().getMacDisplayStyle();
        // 得到ONU列表信息
        List<OltOnuAttribute> oltOnuList = onuService.getOnuListByEntity(entityId);
        List<OltOnuAttribute> onuList = new ArrayList<OltOnuAttribute>();
        for (OltOnuAttribute onu : oltOnuList) {
            if (entityTypeService.isOnu(onu.getTypeId())) {
                onuList.add(onu);
            }
        }
        JSONArray jsonArray = OnuUtil.getOnuListTree(onuList, displayStyle).getJSONArray("tree");
        writeDataToAjax(jsonArray);
        return NONE;
    }

    /**
     * 加载基于端口视角的SNI VLAN的列表
     * 
     * @return
     */
    public String loadSniPortVlanList() {
        List<OltPortVlan> list = vlanListService.loadSniPortVlanList(entityId);
        writeDataToAjax(list);
        return NONE;
    }

    /**
     * 加载基于端口视角的PON VLAN的列表
     * 
     * @return
     */
    public String loadPonPortVlanList() {
        List<OltPortVlan> list = vlanListService.loadPonPortVlanList(entityId);
        writeDataToAjax(list);
        return NONE;
    }

    /**
     * 加载UNI VLAN 
     * @return
     */
    public String loadUniPortVlan() {
        List<UniPortVlan> list = vlanListService.loadUniPortVlan(onuId);
        writeDataToAjax(list);
        return NONE;
    }

    public String showPonVlanConfig() {
        return SUCCESS;
    }

    /**
     * 刷新VLAN数据
     * 
     * @return
     */
    public String refreshVlan() {
        vlanListService.refreshVlan(entityId, jconnectionId);
        return NONE;
    }

    public String showApplyToOtherPon() {
        return SUCCESS;
    }

    /**
     * 批量复制PON业务VLAN到其他端口
     * @return
     */
    public String batchCopyPonServiceVlanConfig() {
        final List<Long> targetPonIdList = new ArrayList<Long>();
        if (!StringUtil.isEmpty(targetPonIds)) {
            for (String vlanIndex : targetPonIds.split(",")) {
                targetPonIdList.add(Long.parseLong(vlanIndex));
            }
        }
        targetPonIdList.remove(ponId);
        vlanListService.batchCopyPonServiceVlanConfig(ponId, targetPonIdList, services, jconnectionId);
        return NONE;
    }

    public String loadSlotPonList() {
        List<PonVlanPortLocation> list = vlanListService.loadSlotPonList(entityId);
        Map<Long, JSONObject> map = new HashMap<Long, JSONObject>();
        for (PonVlanPortLocation port : list) {
            Long slotNo = port.getSlotNo();
            JSONObject jsonObject = map.get(slotNo);
            if (jsonObject == null) {
                jsonObject = new JSONObject();
                jsonObject.put("slotNo", slotNo);
                jsonObject.put("slotType", port.getTopsysbdpreconfigtype());
                jsonObject.put("bname", port.getBname());
                jsonObject.put("pons", new JSONArray());
                map.put(slotNo, jsonObject);
            }
            JSONObject $port = new JSONObject();
            $port.put("portId", port.getPonId());
            $port.put("portInex", port.getPonIndex());
            jsonObject.getJSONArray("pons").add($port);
        }
        writeDataToAjax(map.values());
        return NONE;
    }

    public String getVlanIdsStr() {
        return vlanIdsStr;
    }

    public void setVlanIdsStr(String vlanIdsStr) {
        this.vlanIdsStr = vlanIdsStr;
    }

    public Long getVlanIndex() {
        return vlanIndex;
    }

    public void setVlanIndex(Long vlanIndex) {
        this.vlanIndex = vlanIndex;
    }

    public SniVlanService getSniVlanService() {
        return sniVlanService;
    }

    public void setSniVlanService(SniVlanService sniVlanService) {
        this.sniVlanService = sniVlanService;
    }

    public VlanAttribute getVlanAttribute() {
        return vlanAttribute;
    }

    public void setVlanAttribute(VlanAttribute vlanAttribute) {
        this.vlanAttribute = vlanAttribute;
    }

    public Integer getTopMcFloodMode() {
        return topMcFloodMode;
    }

    public void setTopMcFloodMode(Integer topMcFloodMode) {
        this.topMcFloodMode = topMcFloodMode;
    }

    public String getVlanName() {
        return vlanName;
    }

    public void setVlanName(String vlanName) {
        this.vlanName = vlanName;
    }

    public String getTaggedVlanStr() {
        return taggedVlanStr;
    }

    public void setTaggedVlanStr(String taggedVlanStr) {
        this.taggedVlanStr = taggedVlanStr;
    }

    public String getUnTaggedVlanStr() {
        return unTaggedVlanStr;
    }

    public void setUnTaggedVlanStr(String unTaggedVlanStr) {
        this.unTaggedVlanStr = unTaggedVlanStr;
    }

    public String getJconnectionId() {
        return jconnectionId;
    }

    public void setJconnectionId(String jconnectionId) {
        this.jconnectionId = jconnectionId;
    }

    public String getPortIndexsStr() {
        return portIndexsStr;
    }

    public void setPortIndexsStr(String portIndexsStr) {
        this.portIndexsStr = portIndexsStr;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public String getTargetPonIds() {
        return targetPonIds;
    }

    public void setTargetPonIds(String targetPonIds) {
        this.targetPonIds = targetPonIds;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

}
