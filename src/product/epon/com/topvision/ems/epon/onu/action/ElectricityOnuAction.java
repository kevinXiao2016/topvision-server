/***********************************************************************
 * $Id: OnuAction.java,v1.0 2013-10-25 上午11:18:33 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.onu.domain.OltOnuComAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuComStatics;
import com.topvision.ems.epon.onu.domain.OltOnuMacMgmt;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.service.ElectricityOnuService;
import com.topvision.ems.epon.vlan.domain.VlanAttribute;
import com.topvision.ems.epon.vlan.service.SniVlanService;
import com.topvision.framework.exception.engine.SnmpNoResponseException;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2013-10-25-上午11:18:33
 *
 */
@Controller("electricityOnuAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ElectricityOnuAction extends AbstractEponAction {
    private static final long serialVersionUID = 8315737391250188736L;
    private final Logger logger = LoggerFactory.getLogger(ElectricityOnuAction.class);
    @Autowired
    private ElectricityOnuService electricityOnuService;
    @Autowired
    private SniVlanService sniVlanService;
    private Long comIndex;
    private Long onuIndex;
    private String comInfoDes;
    private Integer comInfoMode;
    private Integer comInfoType;
    private Integer comInfoBuad;
    private Integer comInfoDataBit;
    private Integer comInfoStartBit;
    private Integer comInfoEndBit;
    private String mainRemoteIp;
    private Integer mainRemotePort;
    private String backRemoteIp;
    private Integer backRemotePort;
    private Integer serverType;
    private Integer serverPort;
    private String inbandIp;
    private String inbandMask;
    private String inbandGateway;
    private Integer onuComVlan;
    private Long ponCutOverPortIndex;
    private Long srcPonIndex;
    private Long dstPonIndex;
    private JSONObject elecOnuCapability;
    private Integer mgmtMode;
    private net.sf.json.JSONArray mgmtMacList;
    private String topOnuMacList;
    private Integer mgmtEnable;
    private Long uniId;

    /**
     * 跳转到电力ONU8COM服务器VLAN配置界面
     * 
     * @return
     */
    public String showElecComVlan() {
        onuComVlan = electricityOnuService.getOnuComVlan(entityId).getOnuComVlan();
        return SUCCESS;
    }

    /**
     * 刷新ONU的串口VLAN信息
     * 
     * @return
     */
    public String refreshOnuComVlan() {
        electricityOnuService.refreshOnuComVlan(entityId);
        return NONE;
    }

    /**
     * 加载VLAN List
     * 
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public String loadVlanList() throws JSONException, IOException {
        List<VlanAttribute> oltVlanConfigList = sniVlanService.getOltVlanConfigList(entityId);
        JSONArray json = new JSONArray();
        JSONObject basic = new JSONObject();
        basic.put("vlan", 0);
        basic.put("vlanName", getString("VLAN.vlanConfigStatus", "epon"));
        json.add(basic);
        for (VlanAttribute vlan : oltVlanConfigList) {
            JSONObject o = new JSONObject();
            o.put("vlan", vlan.getVlanIndex());
            o.put("vlanName", vlan.getOltVlanName());
            json.add(o);
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 跳转到ONU MAC 地址管理页面
     * 
     * @return
     */
    public String showOnuMacMgmt() {
        OltOnuMacMgmt o = electricityOnuService.getOnuMacMgmt(entityId, onuIndex);
        if (o != null) {
            mgmtMode = o.getTopOnuMacMark();
            mgmtEnable = o.getMgmtEnable();
            if (o.getTopOnuMac().size() > 0) {
                mgmtMacList = net.sf.json.JSONArray.fromObject(o.getTopOnuMac());
            } else {
                mgmtMacList = net.sf.json.JSONArray.fromObject(false);
            }
        } else {
            mgmtMode = 0;
            mgmtEnable = 2;
            mgmtMacList = net.sf.json.JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    /**
     * 修改ONU MAC地址管理
     * 
     * @return
     * @throws Exception
     */
    public String modifyOnuMacMgmt() throws Exception {
        String mes = "success";
        OltOnuMacMgmt o = new OltOnuMacMgmt();
        o.setEntityId(entityId);
        o.setOnuIndex(onuIndex);
        o.setTopOnuMacList(topOnuMacList);
        o.setTopOnuMacMark(mgmtMode);
        o.setMgmtEnable(mgmtEnable);
        try {
            electricityOnuService.setOnuMacMgmt(o);
        } catch (Exception e) {
            logger.debug("modifyOnuMacMgmt failed: " + e.getMessage());
            mes = e.getMessage();
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 刷新ONU MAC地址管理
     * 
     * @return
     * @throws Exception
     */
    public String refreshOnuMacMgmt() throws Exception {
        String mes = "success";
        try {
            electricityOnuService.refreshOnuMacMgmt(entityId, onuIndex);
        } catch (Exception e) {
            logger.debug("refreshOnuMacMgmt failed: " + e.getMessage());
            mes = e.getMessage();
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 跳转到串口基本配置页面
     * 
     * @return
     */
    public String showComBaseConfig() {
        OltOnuComAttribute o = electricityOnuService.getOnuComAttribute(entityId, comIndex);
        comInfoDes = "";
        if (o != null) {
            if (o.getOnuComInfoComDesc() != null) {
                comInfoDes = o.getOnuComInfoComDesc();
            }
            if (o.getOnuComInfoBuad() != null) {
                comInfoBuad = o.getOnuComInfoBuad();
            }
            if (o.getOnuComInfoComType() != null) {
                comInfoType = o.getOnuComInfoComType();
            }
            if (o.getOnuComInfoParityType() != null) {
                comInfoMode = o.getOnuComInfoParityType();
            }
            if (o.getOnuComInfoDataBits() != null) {
                comInfoDataBit = o.getOnuComInfoDataBits();
            }
            if (o.getOnuComInfoStartBits() != null) {
                comInfoStartBit = o.getOnuComInfoStartBits();
            }
            if (o.getOnuComInfoStopBits() != null) {
                comInfoEndBit = o.getOnuComInfoStopBits();
            }
        }
        return SUCCESS;
    }

    /**
     * 修改串口基本信息
     * 
     * @return
     */
    public String modifyComBase() throws Exception {
        String mes = "success";
        OltOnuComAttribute v = new OltOnuComAttribute();
        v.setEntityId(entityId);
        v.setOnuComIndex(comIndex);
        v.setOnuComInfoComDesc(comInfoDes);
        v.setOnuComInfoBuad(comInfoBuad);
        v.setOnuComInfoComType(comInfoType);
        v.setOnuComInfoParityType(comInfoMode);
        v.setOnuComInfoDataBits(comInfoDataBit);
        v.setOnuComInfoStopBits(comInfoEndBit);
        try {
            electricityOnuService.setOnuComAttribute(entityId, v);
        } catch (Exception e) {
            logger.debug("modifyComBase faield: " + e.getMessage());
            mes = e.getMessage();
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 刷新串口基本配置信息
     * 
     * @return
     * @throws Exception
     */
    public String refreshComBase() throws Exception {
        String mes = "success";
        try {
            electricityOnuService.refreshOnuComAttribute(entityId, comIndex);
        } catch (Exception e) {
            logger.debug("refreshComBase failed: " + e.getMessage());
            mes = e.getMessage();
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 跳转到ONU 带内管理地址页面
     * 
     * @return
     */
    public String showOnuInbandMgmt() {
        onuComVlan = electricityOnuService.getOnuComVlan(entityId).getOnuComVlan();
        OltTopOnuCapability onuCapability = electricityOnuService.getElecOnuCapability(entityId, onuIndex);
        elecOnuCapability = new JSONObject();
        elecOnuCapability.put("topOnuMgmtIp", onuCapability.getTopOnuMgmtIp());
        elecOnuCapability.put("topOnuNetMask", onuCapability.getTopOnuNetMask());
        elecOnuCapability.put("topOnuGateway", onuCapability.getTopOnuGateway());
        return SUCCESS;
    }

    /**
     * 配置电力ONU带内信息
     * 
     * @return
     * @throws Exception
     */
    public String modifyOnuInbandMgmt() throws Exception {
        electricityOnuService.setOnuIpMaskInfo(entityId, onuIndex, inbandIp, inbandMask, inbandGateway);
        return NONE;
    }

    /**
     * 刷新电力ONU带内信息
     * 
     * @return
     * @throws Exception
     */
    @Deprecated
    public String refreshOnuInbandMgmt() throws Exception {
        return NONE;
    }

    /**
     * 跳转到串口远端服务器配置页面
     * 
     * @return
     */
    public String showComRemoteConfig() {
        OltOnuComAttribute v = electricityOnuService.getOnuComAttribute(entityId, comIndex);
        mainRemoteIp = v.getOnuComInfoMainRemoteIp();
        mainRemotePort = v.getOnuComInfoMainRemotePort();
        backRemoteIp = v.getOnuComInfoBackRemoteIp();
        backRemotePort = v.getOnuComInfoBackRemotePort();
        serverType = v.getOnuComInfoSrvType();
        serverPort = v.getOnuComInfoSrvPort();
        return SUCCESS;
    }

    /**
     * 修改串口远端服务器配置
     * 
     * @return
     * @throws Exception
     */
    public String modifyComRemote() throws Exception {
        String mes = "success";
        OltOnuComAttribute v = new OltOnuComAttribute();
        v.setEntityId(entityId);
        v.setOnuComIndex(comIndex);
        v.setOnuComInfoBackRemoteIp(backRemoteIp);
        if (backRemotePort > 0) {
            v.setOnuComInfoBackRemotePort(backRemotePort);
        } else {
            v.setOnuComInfoBackRemotePort(1025);
        }
        v.setOnuComInfoMainRemoteIp(mainRemoteIp);
        if (mainRemotePort > 0) {
            v.setOnuComInfoMainRemotePort(mainRemotePort);
        } else {
            v.setOnuComInfoMainRemotePort(1025);
        }
        v.setOnuComInfoSrvType(serverType);
        if (serverPort > 0) {
            v.setOnuComInfoSrvPort(serverPort);
        } else {
            v.setOnuComInfoSrvPort(1025);
        }
        try {
            electricityOnuService.setOnuComAttribute(entityId, v);
        } catch (Exception e) {
            logger.debug("modifyComRemote faield: " + e.getMessage());
            mes = e.getMessage();
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 刷新串口远端服务器配置
     * 
     * @return
     * @throws Exception
     */
    public String refreshComRemote() throws Exception {
        String mes = "success";
        try {
            electricityOnuService.refreshOnuComAttribute(entityId, comIndex);
        } catch (Exception e) {
            logger.debug("refreshComRemote failed: " + e.getMessage());
            mes = e.getMessage();
        }
        writeDataToAjax(mes);
        return NONE;
    }

    /**
     * 从设备得到性能统计数据
     * 
     * @return
     * @throws IOException
     */
    public String fetchElecComStaticData() throws IOException {
        try {
            OltOnuComStatics statics = electricityOnuService.getOnuComStatisc(entityId, comIndex);
            List<OltOnuComStatics> list = new ArrayList<OltOnuComStatics>();
            list.add(statics);
            JSONObject json = new JSONObject();
            json.put("data", list);
            writeDataToAjax(json);
        } catch (SnmpNoResponseException e) {
            List<OltOnuComStatics> list = new ArrayList<OltOnuComStatics>();
            OltOnuComStatics statics = new OltOnuComStatics();
            statics.setEntityId(entityId);
            statics.setOnuComIndex(comIndex);
            list.add(statics);
            JSONObject json = new JSONObject();
            json.put("data", list);
            writeDataToAjax(json);
            logger.error("", e);
        }
        return NONE;
    }

    /**
     * 加载可用的PON口割接端口列表
     * 
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public String loadPonCutOverPort() throws IOException, JSONException {
        List<Long> list = electricityOnuService.loadPonCutOverPort(entityId, ponCutOverPortIndex);
        if (ponCutOverPortIndex != null) {
            long sameChipPonIndex = EponIndex.getChipPortIndex(ponCutOverPortIndex);
            list.remove(sameChipPonIndex);
        }
        JSONArray json = new JSONArray();
        for (Long port : list) {
            JSONObject o = new JSONObject();
            o.put("portIndex", port.longValue());
            o.put("portName", EponIndex.getPortStringByIndex(port).toString());
            json.add(o);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 展示电力ONU COM口性能统计页面
     * 
     * @return
     */
    public String showElecComStatic() {
        return SUCCESS;
    }

    /**
     * 保存串口服务器COM口VLAN配置
     * 
     * @return
     */
    public String cfgElecComVlan() {
        electricityOnuService.setOnuComVlan(entityId, onuComVlan);
        return NONE;
    }

    /**
     * 展示PON口割接界面
     * 
     * @return
     */
    public String showElecPonCutover() {
        return SUCCESS;
    }

    /**
     * PON端口业务割接
     * 
     * @return
     */
    public String cfgElecPonCutover() {
        electricityOnuService.switchPonInfo(entityId, srcPonIndex, dstPonIndex);
        return NONE;
    }

    /**
     * 清除串口业务数据统计，重新计数
     * 
     * @return
     */
    public String clearElecComStatic() {
        electricityOnuService.cleanOnuComStatisc(entityId, comIndex);
        return NONE;
    }

    /**
     * 刷新UNI口的上行untag报文的指定优先级
     * 
     * @param entityId
     *            ,uniId
     */
    public String refreshUniUSUtgPri() throws Exception {
        String mes = "success";
        try {
            electricityOnuService.refreshUniUSUtgPri(entityId, uniId);
        } catch (Exception e) {
            mes = "refreshUniUSUtgPri failed:<br>" + e.getMessage();
        }
        writeDataToAjax(mes);
        return NONE;
    }

    @Override
    public Integer getOperationResult() {
        return operationResult;
    }

    @Override
    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    @Override
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Long getComIndex() {
        return comIndex;
    }

    public void setComIndex(Long comIndex) {
        this.comIndex = comIndex;
    }

    public String getComInfoDes() {
        return comInfoDes;
    }

    public void setComInfoDes(String comInfoDes) {
        this.comInfoDes = comInfoDes;
    }

    public Integer getComInfoMode() {
        return comInfoMode;
    }

    public void setComInfoMode(Integer comInfoMode) {
        this.comInfoMode = comInfoMode;
    }

    public Integer getComInfoType() {
        return comInfoType;
    }

    public void setComInfoType(Integer comInfoType) {
        this.comInfoType = comInfoType;
    }

    public Integer getComInfoBuad() {
        return comInfoBuad;
    }

    public void setComInfoBuad(Integer comInfoBuad) {
        this.comInfoBuad = comInfoBuad;
    }

    public Integer getComInfoDataBit() {
        return comInfoDataBit;
    }

    public void setComInfoDataBit(Integer comInfoDataBit) {
        this.comInfoDataBit = comInfoDataBit;
    }

    public Integer getComInfoStartBit() {
        return comInfoStartBit;
    }

    public void setComInfoStartBit(Integer comInfoStartBit) {
        this.comInfoStartBit = comInfoStartBit;
    }

    public Integer getComInfoEndBit() {
        return comInfoEndBit;
    }

    public void setComInfoEndBit(Integer comInfoEndBit) {
        this.comInfoEndBit = comInfoEndBit;
    }

    public String getMainRemoteIp() {
        return mainRemoteIp;
    }

    public void setMainRemoteIp(String mainRemoteIp) {
        this.mainRemoteIp = mainRemoteIp;
    }

    public Integer getMainRemotePort() {
        return mainRemotePort;
    }

    public void setMainRemotePort(Integer mainRemotePort) {
        this.mainRemotePort = mainRemotePort;
    }

    public String getBackRemoteIp() {
        return backRemoteIp;
    }

    public void setBackRemoteIp(String backRemoteIp) {
        this.backRemoteIp = backRemoteIp;
    }

    public Integer getBackRemotePort() {
        return backRemotePort;
    }

    public void setBackRemotePort(Integer backRemotePort) {
        this.backRemotePort = backRemotePort;
    }

    public Integer getServerType() {
        return serverType;
    }

    public void setServerType(Integer serverType) {
        this.serverType = serverType;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getInbandIp() {
        return inbandIp;
    }

    public void setInbandIp(String inbandIp) {
        this.inbandIp = inbandIp;
    }

    public String getInbandMask() {
        return inbandMask;
    }

    public void setInbandMask(String inbandMask) {
        this.inbandMask = inbandMask;
    }

    public String getInbandGateway() {
        return inbandGateway;
    }

    public void setInbandGateway(String inbandGateway) {
        this.inbandGateway = inbandGateway;
    }

    public Long getPonCutOverPortIndex() {
        return ponCutOverPortIndex;
    }

    public void setPonCutOverPortIndex(Long ponCutOverPortIndex) {
        this.ponCutOverPortIndex = ponCutOverPortIndex;
    }

    public Long getSrcPonIndex() {
        return srcPonIndex;
    }

    public void setSrcPonIndex(Long srcPonIndex) {
        this.srcPonIndex = srcPonIndex;
    }

    public Long getDstPonIndex() {
        return dstPonIndex;
    }

    public void setDstPonIndex(Long dstPonIndex) {
        this.dstPonIndex = dstPonIndex;
    }

    public Integer getOnuComVlan() {
        return onuComVlan;
    }

    public JSONObject getElecOnuCapability() {
        return elecOnuCapability;
    }

    public Integer getMgmtMode() {
        return mgmtMode;
    }

    public void setMgmtMode(Integer mgmtMode) {
        this.mgmtMode = mgmtMode;
    }

    public net.sf.json.JSONArray getMgmtMacList() {
        return mgmtMacList;
    }

    public void setMgmtMacList(net.sf.json.JSONArray mgmtMacList) {
        this.mgmtMacList = mgmtMacList;
    }

    public String getTopOnuMacList() {
        return topOnuMacList;
    }

    public void setTopOnuMacList(String topOnuMacList) {
        this.topOnuMacList = topOnuMacList;
    }

    public Integer getMgmtEnable() {
        return mgmtEnable;
    }

    public void setMgmtEnable(Integer mgmtEnable) {
        this.mgmtEnable = mgmtEnable;
    }

    public void setElecOnuCapability(JSONObject elecOnuCapability) {
        this.elecOnuCapability = elecOnuCapability;
    }

    public void setOnuComVlan(Integer onuComVlan) {
        this.onuComVlan = onuComVlan;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

}
