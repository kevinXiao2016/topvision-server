/***********************************************************************
 * $Id: RealtimeCmListAction.java,v1.0 2014年5月10日 下午4:10:04 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.cm.domain.RealtimeCm;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.cm.service.RealtimeCmListService;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.util.RealtimeCmUtil;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.service.SystemPreferencesService;

import net.sf.json.JSONObject;

/**
 * @author YangYi
 * @created @2014年5月10日-下午4:10:04
 * 
 */
/**
 * @author admin
 * @created @2016年12月27日-上午10:07:50
 *
 */
@Controller("realtimeCmListAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RealtimeCmListAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(RealtimeCmListAction.class);
	@Resource(name = "cmService")
	private CmService cmService;
	@Resource(name = "cmcService")
	private CmcService cmcService;
	@Resource(name = "realtimeCmListService")
	private RealtimeCmListService realtimeCmListService;
	@Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
	private Long cmcId;
	private Long cmIndex;
	private Integer cpeType;
	private String cmIndexs;
	private String cmStatus;
	private Integer cpeStatus;
	private String chooseStatus;
	private Boolean isSupportRealtimeCpeQuery;
    private Integer cmPingMode; //CM PING方式， 1为从网管服务器PING， 2为从上联设备PING
	private Integer upSnrMin;
	private Integer upSnrMax;
	private Integer downSnrMin;
	private Integer downSnrMax;
	private Integer upPowerMin;
	private Integer upPowerMax;
	private Integer downPowerMin;
	private Integer downPowerMax;
	// private String jumpStatus[] = { "JUMPSTATUS_ALLSHOW" };

	public String showRealtimeCmList() {
		try {
			isSupportRealtimeCpeQuery = deviceVersionService.isFunctionSupported(cmcId, "realtimecpe");
		} catch (Exception e) {
			logger.error("", e);
		}
        Properties properties = systemPreferencesService.getModulePreferences("toolPing");
        String mode = properties.getProperty("Ping.cmping");
        if (mode != null) {
            setCmPingMode(Integer.valueOf(mode));
        } else {
            setCmPingMode(1);
        }
		return SUCCESS;
	}

	public String showRealtimeCpeList() {
		return SUCCESS;
	}

	public String loadRealtimeCpeList() {
		if (cmcId != null) {
			Map<String, Object> json = new HashMap<String, Object>();
			List<CmCpe> cpeList = realtimeCmListService.getRealtimeCpeByCmcId(cmcId, cpeStatus, cpeType);
			Integer size = cpeList.size();
			json.put("rowCount", size);
			json.put("data", cpeList);
			writeDataToAjax(JSONObject.fromObject(json));
		}
		return NONE;
	}

	/**
	 * 读取实时CM列表基本信息
	 * 
	 * @return
	 */
	// public String loadRealtimeCmList() {
	// if (cmcId != null) {
	// Map<String, Object> json = new HashMap<String, Object>();
	// List<RealtimeCm> cmList =
	// realtimeCmListService.getRealtimeCmAttributeByCmcId(cmcId,
	// this.splitCmIndexs(cmIndexs), cmStatus);
	// for(RealtimeCm cm:cmList){
	// if(cm.getStatusIpAddress()==null){
	// cm.setStatusIpAddress(cm.getStatusInetAddress());
	// }
	// }
	// Integer size = cmList.size();
	// json.put("rowCount", size);
	// json.put("data", cmList);
	// write(JSONObject.fromObject(json));
	// }
	// return NONE;
	// }

	private List<Long> splitCmIndexs(String cmIndexs) {
		List<Long> list = new ArrayList<Long>();
		if (cmIndexs != null && cmIndexs.length() > 0) {
			String[] ids = cmIndexs.split(",");
			for (int i = 0; i < ids.length; i++) {
				list.add(Long.valueOf(ids[i]));
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 实时列表载入
	 * 
	 * @return String
	 */
	public String loadRealtimeCmList() throws Exception {
		if (cmcId != null) {
			Map<String, Object> json = new HashMap<String, Object>();
			List<RealtimeCm> cmList = null;
			// Modify by ls 载入实时cm信息，将信号质量也加入，使前端查询时结果同时刷出来
			String[] status = RealtimeCmUtil.judgeStatusPara(chooseStatus, cmStatus);// 对传入的状态参数进行判断，第一个参数为查询时选择的状态参数，第二个参数为cmts实时信息跳转时的参数
			cmList = realtimeCmListService.loadCmRealtimeInfo(cmcId, this.splitCmIndexs(cmIndexs), status);
			int size = cmList.size();
			json.put("rowCount", size);
			json.put("data", cmList);
			writeDataToAjax(JSONObject.fromObject(json));
		}
		return NONE;
	}

	/**
	 * 读取CM信号质量
	 * 
	 * @return
	 */
	public String loadCmSignal() {
		Map<String, Object> json = new HashMap<String, Object>();
		Map<String, List<String>> resultMap = realtimeCmListService.loadCmSignal(3, cmIndex, cmcId);
		json.put("downChannelTx", resultMap.get("downChannelTx"));// 下行电平
		json.put("downChannelSnr", resultMap.get("downChannelSnr"));// 下行SNR
		json.put("upChannelTx", resultMap.get("upChannelTx"));// 上行电平
		json.put("cmIndex", cmIndex);
		writeDataToAjax(JSONObject.fromObject(json));
		return NONE;
	}

	/**
	 * 读取上行信道SNR
	 * 
	 * @return
	 */
	public String loadUpChannelSnr() {
		Map<String, Object> json = new HashMap<String, Object>();
		Map<String, List<String>> upChannelSnr = realtimeCmListService.loadUpChannelSnr(cmcId, null);
		json.put("upChannelSnr", upChannelSnr);
		writeDataToAjax(JSONObject.fromObject(json));
		return NONE;
	}

	public Long getCmcId() {
		return cmcId;
	}

	public void setCmcId(Long cmcId) {
		this.cmcId = cmcId;
	}

	public Long getCmIndex() {
		return cmIndex;
	}

	public void setCmIndex(Long cmIndex) {
		this.cmIndex = cmIndex;
	}

	public Integer getCpeType() {
		return cpeType;
	}

	public void setCpeType(Integer cpeType) {
		this.cpeType = cpeType;
	}

	public String getCmIndexs() {
		return cmIndexs;
	}

	public void setCmIndexs(String cmIndexs) {
		this.cmIndexs = cmIndexs;
	}

	public String getCmStatus() {
		return cmStatus;
	}

	public void setCmStatus(String cmStatus) {
		this.cmStatus = cmStatus;
	}

	public Integer getCpeStatus() {
		return cpeStatus;
	}

	public void setCpeStatus(Integer cpeStatus) {
		this.cpeStatus = cpeStatus;
	}

	public Boolean getIsSupportRealtimeCpeQuery() {
		return isSupportRealtimeCpeQuery;
	}

	public void setIsSupportRealtimeCpeQuery(Boolean isSupportRealtimeCpeQuery) {
		this.isSupportRealtimeCpeQuery = isSupportRealtimeCpeQuery;
	}

	public Integer getUpSnrMin() {
		return upSnrMin;
	}

	public void setUpSnrMin(Integer upSnrMin) {
		this.upSnrMin = upSnrMin;
	}

	public Integer getUpSnrMax() {
		return upSnrMax;
	}

	public void setUpSnrMax(Integer upSnrMax) {
		this.upSnrMax = upSnrMax;
	}

	public Integer getDownSnrMin() {
		return downSnrMin;
	}

	public void setDownSnrMin(Integer downSnrMin) {
		this.downSnrMin = downSnrMin;
	}

	public Integer getDownSnrMax() {
		return downSnrMax;
	}

	public void setDownSnrMax(Integer downSnrMax) {
		this.downSnrMax = downSnrMax;
	}

	public Integer getUpPowerMin() {
		return upPowerMin;
	}

	public void setUpPowerMin(Integer upPowerMin) {
		this.upPowerMin = upPowerMin;
	}

	public Integer getUpPowerMax() {
		return upPowerMax;
	}

	public void setUpPowerMax(Integer upPowerMax) {
		this.upPowerMax = upPowerMax;
	}

	public Integer getDownPowerMin() {
		return downPowerMin;
	}

	public void setDownPowerMin(Integer downPowerMin) {
		this.downPowerMin = downPowerMin;
	}

	public Integer getDownPowerMax() {
		return downPowerMax;
	}

	public void setDownPowerMax(Integer downPowerMax) {
		this.downPowerMax = downPowerMax;
	}

	public String getChooseStatus() {
		return chooseStatus;
	}

	public void setChooseStatus(String chooseStatus) {
		this.chooseStatus = chooseStatus;
	}

    public Integer getCmPingMode() {
        return cmPingMode;
    }

    public void setCmPingMode(Integer cmPingMode) {
        this.cmPingMode = cmPingMode;
    }

}
