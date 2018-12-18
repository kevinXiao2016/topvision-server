/***********************************************************************
 * $Id: GponOnuAction.java,v1.0 2016年12月19日 下午1:11:37 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuauth.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.olt.service.OltSlotService;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.epon.performance.service.OltPerfGraphService;
import com.topvision.ems.epon.utils.BoardType;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.gpon.onuauth.domain.GponAutoAuthConfig;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthConfig;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAuthMode;
import com.topvision.ems.gpon.onuauth.domain.GponOnuAutoFind;
import com.topvision.ems.gpon.onuauth.service.GponOnuAuthService;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Bravin
 * @created @2016年12月19日-下午1:11:37
 *
 */
@Controller("gponOnuAuthAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GponOnuAuthAction extends BaseAction {
	private static final long serialVersionUID = -3512042275829799911L;
	@Autowired
	private OltSlotService oltSlotService;
	@Autowired
	private GponOnuAuthService gponOnuAuthService;
	@Autowired
	private OltPerfGraphService oltPerfGraphService;
	@Autowired
	private OltPonService oltPonService;
	@Autowired
	private DiscoveryService<DiscoveryData> discoveryService;
	@Autowired
	private EntityService entityService;
	@Autowired
	private EntityTypeService entityTypeService;
	@Autowired
	private OnuAuthService onuAuthService;
	private Long entityId;
	private Long ponIndex;
	private Long onuIndex;
	private Long ponId;
	private Long slotId;
	private Integer authMode;
	private String ponIndexs;
	private Integer autoFindConfig;
	private Integer portType;
	private String sn;
	private List<OltPonAttribute> oltPonAttributes = new ArrayList<OltPonAttribute>();
	private List<OltSlotAttribute> oltSlotList;
	private List<GponOnuAuthMode> authModeList;
	private GponOnuAuthConfig gponOnuAuthConfig;
	private GponAutoAuthConfig gponAutoAuthConfig;
	private GponOnuAutoFind gponOnuAutoFind;
	private JSONArray onuMaxNumList = new JSONArray();
	private Long onuNo;
	private String entityIds;
	private String onuIndexs;
	private Integer onuNumber;
	private String onuType;
	private List<GponOnuAutoFind> gponOnuAutoList = new ArrayList<GponOnuAutoFind>();
	private List<Long> ponIds = new ArrayList<Long>();
	private List<Long> onuNoList = new ArrayList<Long>();
	private Integer lineProfileId;
	private Integer srvProfileId;

	/**
	 * PON口最大ONU个数
	 * 
	 * @return
	 */
	private List<List<Integer>> getOnuMaxNums(Long entityId) {
		List<List<Integer>> tmpMaxNum = new ArrayList<List<Integer>>();
		for (int a = 0; a < EponConstants.OLT_SLOT_MAXNUM + 1; a++) {
			List<Integer> tmpNum = new ArrayList<Integer>();
			for (int b = 0; b < EponConstants.OLT_SLOT_PORT_MAXNUM + 1; b++) {
				tmpNum.add(0);
			}
			tmpMaxNum.add(tmpNum);
		}
		// PON板
		List<OltSlotAttribute> oltPonList = oltSlotService.getOltGponSlotList(entityId);
		Entity entity = entityService.getEntity(entityId);
		if (entityTypeService.isPN8602_EType(entity.getTypeId())
				|| entityTypeService.isPN8602_EFType(entity.getTypeId())
				|| entityTypeService.isPN8602_GType(entity.getTypeId())) {
			for (OltSlotAttribute slotAttr : oltPonList) {
				slotAttr.setSlotNo(0L);
			}
		}
		// PON口
		for (OltSlotAttribute oltSlotAttribute : oltPonList) {
			List<OltPonAttribute> tempPon = oltSlotService.getSlotPonList(oltSlotAttribute.getSlotId());
			// PON口最大ONU个数
			if (tempPon.size() > 0) {
				for (OltPonAttribute pon : tempPon) {
					if (pon.getPonPortMaxOnuNumSupport() != null) {
						tmpMaxNum.get(oltSlotAttribute.getSlotNo().intValue()).set(pon.getPonNo().intValue(),
								pon.getPonPortMaxOnuNumSupport());
					}
				}
			}
		}
		return tmpMaxNum;
	}

	public String showOnuAuthen() {
		// PON口最大ONU个数
		onuMaxNumList = JSONArray.fromObject(getOnuMaxNums(entityId));
		// PON板
		oltSlotList = oltSlotService.getOltGponSlotList(entityId);
		if (oltSlotList.size() > 0) {
			// PON口
			for (OltSlotAttribute oltSlotAttribute : oltSlotList) {
				List<OltPonAttribute> $pons = oltSlotService.getSlotPonList(oltSlotAttribute.getSlotId());
				oltPonAttributes.addAll($pons);
			}
		}
		authModeList = gponOnuAuthService.loadPonAuthMode(entityId);
		return SUCCESS;
	}

	public String loadGponPorts() {
		List<OltPonAttribute> ponAttributes = oltPonService.getOltPonList(entityId);
		List<OltSlotAttribute> oltSlotList = oltSlotService.getOltSlotList(entityId);
		JSONArray nodeArray = new JSONArray();
		Map<Long, JSONObject> slotMap = new HashMap<Long, JSONObject>();
		for (OltPonAttribute oltPonAttribute : ponAttributes) {
			if (!portType.equals(oltPonAttribute.getPonPortType())) {
				continue;
			}
			Long slotId = oltPonAttribute.getSlotId();
			// 处理板卡
			if (!slotMap.containsKey(slotId)) {
				JSONObject slot = new JSONObject();
				slot.put("id", slotId);
				slot.put("pId", -1);
				for (OltSlotAttribute $slot : oltSlotList) {
					if ($slot.getSlotId().equals(slotId)) {
						slot.put("name",
								BoardType.getName($slot.getTopSysBdPreConfigType()) + " : " + $slot.getSlotNo());
					}
				}
				slot.put("open", true);
				slot.put("nocheck", true);
				slotMap.put(slotId, slot);
				// 将板卡加入到节点中
				nodeArray.add(slot);
			}
			JSONObject port = new JSONObject();
			port.put("id", oltPonAttribute.getPonIndex());
			port.put("ponId", oltPonAttribute.getPonId());
			port.put("pId", slotId);
			port.put("open", true);
			String $disp = EponIndex.getPortStringByIndex(oltPonAttribute.getPonIndex()).toString();
			if (oltPonAttribute.getPonOperationStatus() == EponConstants.PORT_STATUS_UP.intValue()) {
				port.put("online", true);
				port.put("icon", "/images/fault/trap_on.png");
				port.put("nm3kTip", "LINK_UP");
			} else {
				port.put("online", false);
				port.put("icon", "/images/fault/trap_off.png");
				port.put("nm3kTip", "LINK_DOWN");
			}
			port.put("name", $disp);
			nodeArray.add(port);
		}
		// 向页面传值
        writeDataToAjax(nodeArray);
		return NONE;
	}

	public String loadGponPortList() {
		List<OltPonAttribute> ponAttributes = oltPonService.getOltPonList(entityId);
		JSONArray nodeArray = new JSONArray();
		for (OltPonAttribute oltPonAttribute : ponAttributes) {
			if (!portType.equals(oltPonAttribute.getPonPortType())) {
				continue;
			}
			JSONObject port = new JSONObject();
			port.put("id", oltPonAttribute.getPonIndex());
			port.put("ponId", oltPonAttribute.getPonId());
			port.put("pId", slotId);
			port.put("open", true);
			String $disp = EponIndex.getPortStringByIndex(oltPonAttribute.getPonIndex()).toString();
			port.put("name", $disp);
			nodeArray.add(port);
		}
		// 向页面传值
        writeDataToAjax(nodeArray);
		return NONE;
	}

	public String loadOnuAuthConfigList() {
		List<GponOnuAuthConfig> list = gponOnuAuthService.loadOnuAuthConfigList(entityId, ponIndex);
        writeDataToAjax(list);
		return NONE;
	}

	/*
	 * public String loadAutoFindOnuList() { List<GponOnuAutoFind> list =
	 * gponOnuAuthService.loadAutoFindOnuList(entityId, ponIndex); write(list);
	 * return NONE; }
	 */
	public String loadAutoFindOnuList() {
		Map<String, Object> conditions = new HashMap<String, Object>();
		if (ponIndex != null && ponIndex != -1) {
			conditions.put("ponIndex", ponIndex);
		}
		if (onuType != null && !("-1").equals(onuType)) {
			conditions.put("onuType", onuType);
		}
		if (authMode != null && authMode != -1) {
			conditions.put("authMode", authMode);
		}
		if (sn != null && !"".equals(sn)) {
			conditions.put("sn", sn);
		}
		conditions.put("start", start);
		conditions.put("limit", limit);
		conditions.put("entityId", entityId);
		List<GponOnuAutoFind> list = gponOnuAuthService.loadAutoFindOnuList(conditions);
		Integer rowCount = gponOnuAuthService.getAutoFindOnuNum(conditions);
		JSONObject json = new JSONObject();
		json.put("data", list);
		json.put("rowCount", rowCount);
		writeDataToAjax(json);
		return NONE;
	}

	public String loadAutoAuthConfigList() {
		List<GponAutoAuthConfig> list = gponOnuAuthService.loadAutoAuthConfigList(entityId);
        writeDataToAjax(list);
		return NONE;
	}

	public String refreshGponOnuAuth() {
		gponOnuAuthService.refreshGponOnuAuth(entityId);
		return NONE;
	}

	public String addGponOnuAuth() {
		gponOnuAuthService.addGponOnuAuth(gponOnuAuthConfig, onuNo);
		discoveryService.refresh(gponOnuAuthConfig.getEntityId());
		return NONE;
	}

	public String batchAddGponOnuAuth() {
		Integer successNum = 0;
		Integer failNum = 0;
		Long entityId = 0L;
		JSONObject json = new JSONObject();
		Map<Long, List<Long>> oltPon = new HashMap<>();
		String[] entityIdArray = entityIds.split(",");
		String[] onuIndexArray = onuIndexs.split(",");
		if (entityIdArray[0] != null) {
			entityId = Long.parseLong(entityIdArray[0]);
		}
		// 获取PON口下所有占用的位置
		List<OltPonAttribute> ponAttributes = oltPonService.getOltPonList(entityId);
		for (OltPonAttribute oltPonAttribute : ponAttributes) {
			List<Long> onuNoList = gponOnuAuthService.getOnuAuthIdList(oltPonAttribute.getPonId());
			Collections.sort(onuNoList);
			oltPon.put(oltPonAttribute.getPonId(), onuNoList);
		}
		for (int i = 0, size = entityIdArray.length; i < size; i++) {
			try {
				if (entityIdArray[i] != null && onuIndexArray[i] != null) {
					GponOnuAuthConfig gponOnuAuthConfig = new GponOnuAuthConfig();
					gponOnuAuthConfig.setEntityId(Long.parseLong(entityIdArray[i]));
					GponOnuAutoFind temp = gponOnuAuthService.selectGponOnuAutoFind(entityId,
							Long.parseLong(onuIndexArray[i]));
					gponOnuAuthConfig.setPonIndex(temp.getPonIndex());
					gponOnuAuthConfig.setSn(temp.getSerialNumber());
					gponOnuAuthConfig.setLineProfileId(lineProfileId);
					gponOnuAuthConfig.setSrvProfileId(srvProfileId);
					gponOnuAuthConfig.setPassword(temp.getPassword());
					gponOnuAuthConfig.setAuthMode(1);
					if (temp != null) {
						Long ponId = gponOnuAuthService.selectPonId(entityId, temp.getPonIndex());
						gponOnuAuthConfig.setPonId(ponId);
					}
					onuNo = temp.getOnuIndex() & 0x000000FFL;
					List<Long> onuNoList = oltPon.get(gponOnuAuthConfig.getPonId());
					if (onuNoList != null && onuNoList.size() > 0) {
						onuNo = onuNo + onuNoList.get(onuNoList.size() - 1);
					}
					gponOnuAuthService.addGponOnuAuth(gponOnuAuthConfig, onuNo);
					successNum++;
				}
			} catch (Exception e) {
				failNum++;
				logger.error("", e);
			}
		}
		discoveryService.refresh(entityId);
		json.put("successNum", successNum);
		json.put("failNum", failNum);
		writeDataToAjax(json);
		return NONE;
	}

	public String modifyOnuAuthMode() {
		GponOnuAuthMode gponOnuAuthMode = new GponOnuAuthMode();
		gponOnuAuthMode.setEntityId(entityId);
		gponOnuAuthMode.setPonIndex(ponIndex);
		gponOnuAuthMode.setPonOnuAuthenMode(authMode);
		gponOnuAuthService.modifyOnuAuthMode(gponOnuAuthMode);
		return NONE;
	}

	public String modifyOnuAutoFind() {
		GponOnuAuthMode gponOnuAuthMode = new GponOnuAuthMode();
		gponOnuAuthMode.setEntityId(entityId);
		gponOnuAuthMode.setPonIndex(ponIndex);
		gponOnuAuthMode.setPonAutoFindEnable(autoFindConfig);
		gponOnuAuthService.modifyOnuAuthMode(gponOnuAuthMode);
		return NONE;
	}

	public String showGponOnuAutoFindList() {
		return SUCCESS;
	}

	public String loadGponONuAutoFindList() {
		return NONE;
	}

	public String showGponAutoAuthConfig() {
		return SUCCESS;
	}

	public String loadGponAutoAuthConfig() {
		List<GponAutoAuthConfig> list = gponOnuAuthService.loadAutoAuthConfigList(entityId);
        writeDataToAjax(list);
		return NONE;
	}

	public String addGponAutoAuth() {
		String[] list = ponIndexs.split(",");
		List<Long> ports = new ArrayList<Long>();
		for (String index : list) {
			if (!StringUtil.isEmpty(index)) {
				ports.add(Long.parseLong(index));
			}
		}
		gponAutoAuthConfig.setAutoAuthPortList(ports);
		gponOnuAuthService.addGponAutoAuth(gponAutoAuthConfig);
		return NONE;
	}

	public String updateGponAutoAuth() {
		String[] list = ponIndexs.split(",");
		List<Long> ports = new ArrayList<Long>();
		for (String index : list) {
			if (!StringUtil.isEmpty(index)) {
				ports.add(Long.parseLong(index));
			}
		}
		gponAutoAuthConfig.setAutoAuthPortList(ports);
		gponOnuAuthService.updateGponAutoAuth(gponAutoAuthConfig);
		return NONE;
	}

	public String showAddGponOnuAuth() {
		// PON口最大ONU个数
		onuMaxNumList = JSONArray.fromObject(getOnuMaxNums(entityId));
		authModeList = gponOnuAuthService.loadPonAuthMode(entityId);
		if (onuIndex != null) {
			gponOnuAutoFind = gponOnuAuthService.queryGponOnuAutoFind(entityId, onuIndex);
		}
		return SUCCESS;
	}

	/**
	 * 获取onuNo的列表
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String loadOnuNoList() throws Exception {
		// 获取onuNo列表
		List<Long> onuNoList = gponOnuAuthService.getOnuAuthIdList(ponId);
		logger.debug("onuNoList:{}", onuNoList);
        writeDataToAjax(JSONArray.fromObject(onuNoList));
		return NONE;
	}

	public String showModifyPonAuthMode() {
		return SUCCESS;
	}

	public String deleteGponOnuAuth() {
		gponOnuAuthService.deleteGponOnuAuth(gponOnuAuthConfig);
		return NONE;
	}

	public String queryGponOnuAutoFind() {
		JSONObject json = new JSONObject();
		Map<String, Object> conditions = new HashMap<String, Object>();
		if (entityId != null) {
			conditions.put("entityId", entityId);
		}
		if (slotId != null) {
			conditions.put("slotId", slotId);
		}
		if (ponId != null) {
			conditions.put("ponId", ponId);
		}
		if (sn != null && !"".equals(sn)) {
			conditions.put("sn", sn);
		}
		conditions.put("start", start);
		conditions.put("limit", limit);
		conditions.put("sort", sort);
		conditions.put("dir", dir);
		List<GponOnuAutoFind> list = gponOnuAuthService.queryGponOnuAutoFind(conditions);
		int count = gponOnuAuthService.queryGponOnuAutoFindCount(conditions);
		json.put("data", list);
		json.put("rowCount", count);
        writeDataToAjax(json);
		return NONE;
	}

	public String batchModifyGponOnuAuthMode() {
		String[] $indexs = ponIndexs.split(",");
		List<GponOnuAuthMode> gponOnuAuthModes = new ArrayList<GponOnuAuthMode>();
		List<Long> ponIndexs = new ArrayList<Long>();
		for (String $index : $indexs) {
			GponOnuAuthMode gponOnuAuthMode = new GponOnuAuthMode();
			gponOnuAuthMode.setEntityId(entityId);
			gponOnuAuthMode.setPonIndex(Long.parseLong($index));
			gponOnuAuthMode.setPonOnuAuthenMode(authMode);
			gponOnuAuthModes.add(gponOnuAuthMode);
			ponIndexs.add(Long.parseLong($index));
			// gponOnuAuthService.modifySingleOnuAuthMode(gponOnuAuthMode);
		}
		// EMS-14320 GPON onu切换认证模式改为同epon onu一样，不重新拓扑
		// 但因为切换认证模式会将该端口下ONU全部删除 ，所以需删除数据库相关信息
		// gponOnuAuthService.refreshGponOnuAuth(entityId);
		// discoveryService.refresh(entityId);
		String result = gponOnuAuthService.batchModifyGponOnuAuthMode(entityId, ponIndexs, gponOnuAuthModes);
        writeDataToAjax(result);
		return NONE;
	}

	public String deleteGponAutoAuth() {
		gponOnuAuthService.deleteGponAutoAuth(gponAutoAuthConfig);
		return NONE;
	}

	public String showAddGponOnuAuthList() {
		return SUCCESS;
	}

	public String getGponOnuAutoFindByIds() {
		List<GponOnuAutoFind> gponOnuAutoFindList = new ArrayList<GponOnuAutoFind>();
		String[] entityIdArray = entityIds.split(",");
		String[] onuIndexArray = onuIndexs.split(",");
		for (int i = 0, size = entityIdArray.length; i < size; i++) {
			GponOnuAutoFind temp = gponOnuAuthService.selectGponOnuAutoFind(Long.parseLong(entityIdArray[i]),
					Long.parseLong(onuIndexArray[i]));
			gponOnuAutoFindList.add(temp);
		}
		JSONObject gponOnuAutoFindJson = new JSONObject();
		gponOnuAutoFindJson.put("data", gponOnuAutoFindList);
        writeDataToAjax(gponOnuAutoFindJson);
		return NONE;
	}

	public String showBatchAddGponOnuAuth() {
		return SUCCESS;
	}

	public String showOnuType() {
		List<EntityType> entityTypeList = new ArrayList<EntityType>();
		Long onuType = entityTypeService.getOnuType();
		entityTypeList = entityTypeService.loadSubType(onuType);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("entityTypeList", entityTypeList);
		writeDataToAjax(jsonObject);
		return NONE;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public List<OltPonAttribute> getOltPonAttributes() {
		return oltPonAttributes;
	}

	public void setOltPonAttributes(List<OltPonAttribute> oltPonAttributes) {
		this.oltPonAttributes = oltPonAttributes;
	}

	public List<OltSlotAttribute> getOltSlotList() {
		return oltSlotList;
	}

	public void setOltSlotList(List<OltSlotAttribute> oltSlotList) {
		this.oltSlotList = oltSlotList;
	}

	public List<GponOnuAuthMode> getAuthModeList() {
		return authModeList;
	}

	public void setAuthModeList(List<GponOnuAuthMode> authModeList) {
		this.authModeList = authModeList;
	}

	public Long getPonIndex() {
		return ponIndex;
	}

	public void setPonIndex(Long ponIndex) {
		this.ponIndex = ponIndex;
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

	public GponOnuAuthConfig getGponOnuAuthConfig() {
		return gponOnuAuthConfig;
	}

	public void setGponOnuAuthConfig(GponOnuAuthConfig gponOnuAuthConfig) {
		this.gponOnuAuthConfig = gponOnuAuthConfig;
	}

	public Integer getAuthMode() {
		return authMode;
	}

	public void setAuthMode(Integer authMode) {
		this.authMode = authMode;
	}

	public Integer getAutoFindConfig() {
		return autoFindConfig;
	}

	public void setAutoFindConfig(Integer autoFindConfig) {
		this.autoFindConfig = autoFindConfig;
	}

	public GponAutoAuthConfig getGponAutoAuthConfig() {
		return gponAutoAuthConfig;
	}

	public void setGponAutoAuthConfig(GponAutoAuthConfig gponAutoAuthConfig) {
		this.gponAutoAuthConfig = gponAutoAuthConfig;
	}

	public Integer getPortType() {
		return portType;
	}

	public void setPortType(Integer portType) {
		this.portType = portType;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getPonIndexs() {
		return ponIndexs;
	}

	public void setPonIndexs(String ponIndexs) {
		this.ponIndexs = ponIndexs;
	}

	public Long getOnuIndex() {
		return onuIndex;
	}

	public void setOnuIndex(Long onuIndex) {
		this.onuIndex = onuIndex;
	}

	public GponOnuAutoFind getGponOnuAutoFind() {
		return gponOnuAutoFind;
	}

	public void setGponOnuAutoFind(GponOnuAutoFind gponOnuAutoFind) {
		this.gponOnuAutoFind = gponOnuAutoFind;
	}

	public JSONArray getOnuMaxNumList() {
		return onuMaxNumList;
	}

	public void setOnuMaxNumList(JSONArray onuMaxNumList) {
		this.onuMaxNumList = onuMaxNumList;
	}

	public Long getOnuNo() {
		return onuNo;
	}

	public void setOnuNo(Long onuNo) {
		this.onuNo = onuNo;
	}

	public String getEntityIds() {
		return entityIds;
	}

	public String getOnuIndexs() {
		return onuIndexs;
	}

	public void setEntityIds(String entityIds) {
		this.entityIds = entityIds;
	}

	public void setOnuIndexs(String onuIndexs) {
		this.onuIndexs = onuIndexs;
	}

	public Integer getOnuNumber() {
		return onuNumber;
	}

	public void setOnuNumber(Integer onuNumber) {
		this.onuNumber = onuNumber;
	}

	public String getOnuType() {
		return onuType;
	}

	public void setOnuType(String onuType) {
		this.onuType = onuType;
	}

	public List<GponOnuAutoFind> getGponOnuAutoList() {
		return gponOnuAutoList;
	}

	public void setGponOnuAutoList(List<GponOnuAutoFind> gponOnuAutoList) {
		this.gponOnuAutoList = gponOnuAutoList;
	}

	public List<Long> getPonIds() {
		return ponIds;
	}

	public void setPonIds(List<Long> ponIds) {
		this.ponIds = ponIds;
	}

	public List<Long> getOnuNoList() {
		return onuNoList;
	}

	public void setOnuNoList(List<Long> onuNoList) {
		this.onuNoList = onuNoList;
	}

	public Integer getLineProfileId() {
		return lineProfileId;
	}

	public void setLineProfileId(Integer lineProfileId) {
		this.lineProfileId = lineProfileId;
	}

	public Integer getSrvProfileId() {
		return srvProfileId;
	}

	public void setSrvProfileId(Integer srvProfileId) {
		this.srvProfileId = srvProfileId;
	}
}
