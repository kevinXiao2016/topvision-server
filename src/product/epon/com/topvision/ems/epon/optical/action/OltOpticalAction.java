/***********************************************************************
 * $Id: OltOpticalAction.java,v1.0 2013-10-26 上午09:13:55 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.optical.action;

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
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.olt.service.OltSniService;
import com.topvision.ems.epon.optical.domain.OltOnuOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.optical.domain.OltPonOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OltSniOptical;
import com.topvision.ems.epon.optical.domain.OltSysOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OnuPonOptical;
import com.topvision.ems.epon.optical.service.OltOpticalService;
import com.topvision.framework.annotation.OperationLogProperty;

/**
 * @author lizongtian
 * @created @2013-10-26-上午09:13:55
 *
 */
@Controller("oltOpticalAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltOpticalAction extends AbstractEponAction {
    private static final long serialVersionUID = -706352965398057379L;
    @Autowired
    private OltOpticalService oltOpticalService;
    @Autowired
    private OltSniService oltSniService;
    @Autowired
    private OltPonService oltPonService;
    private Long portIndex;
    private Integer alarmIndex;
    private Long portId;
    private Integer portType;
    private Integer rxMax;
    private Integer rxMin;
    private Integer txMax;
    private Integer txMin;
    private Integer onuRxMax;
    private Integer onuRxMin;
    private int portSfpRxRwr;
    private int portSfpTxRwr;
    private int onuUsTxRwr;
    private int onuDsRxRwr;
    private JSONArray oltSysOpticalList;
    private JSONArray onuOpticalList;
    private JSONArray ponOpticalList;
    private JSONArray opticalVal = new JSONArray();

    /**
    * 展示OLT光阈值告警全局配置
    * @return
    */
    public String showOltOpticalAlarm() {
        List<OltSysOpticalAlarm> sysOpticalAlarmList = oltOpticalService.getSysOpticalAlarmList(entityId);
        oltSysOpticalList = JSONArray.fromObject(sysOpticalAlarmList);
        return SUCCESS;
    }

    /**
     * 展示OLT ONUPON阈值告警配置
     * @return
     */
    public String showOltOnuOpticalAlarm() {
        OltOnuOpticalAlarm onuOptical = new OltOnuOpticalAlarm();
        onuOptical.setEntityId(entityId);
        onuOptical.setPortIndex(portIndex);
        List<OltOnuOpticalAlarm> onuOpticalAlarmList = oltOpticalService.getOnuOpticalAlarmList(onuOptical);
        onuOpticalList = JSONArray.fromObject(onuOpticalAlarmList);
        return SUCCESS;
    }

    /**
     * 展示OLT ONUPON阈值告警配置
     * @return
     */
    public String showOltOnuOpticalAlarmBack() {
        OltOnuOpticalAlarm onuOptical = new OltOnuOpticalAlarm();
        onuOptical.setEntityId(entityId);
        onuOptical.setPortIndex(portIndex);
        List<OltOnuOpticalAlarm> onuOpticalAlarmList = oltOpticalService.getOnuOpticalAlarmList(onuOptical);
        onuOpticalList = JSONArray.fromObject(onuOpticalAlarmList);
        return SUCCESS;
    }

    /**
     * 展示OLT PON阈值告警配置
     * @return
     */
    public String showOltPonOpticalAlarm() {
        OltPonOpticalAlarm ponOptical = new OltPonOpticalAlarm();
        ponOptical.setEntityId(entityId);
        ponOptical.setPortIndex(portIndex);
        List<OltPonOpticalAlarm> ponOpticalAlarmList = oltOpticalService.getPonOpticalAlarmList(ponOptical);
        ponOpticalList = JSONArray.fromObject(ponOpticalAlarmList);
        return SUCCESS;
    }

    /**
     * 配置OLT全局光阈值
     * @return
     */
    @OperationLogProperty(actionName = "oltOpticalAction", operationName = "saveOltOptAlarm")
    public String saveOltOptAlarm() {
        List<OltSysOpticalAlarm> list = new ArrayList<OltSysOpticalAlarm>();
        OltSysOpticalAlarm rxPwr = new OltSysOpticalAlarm();
        rxPwr.setTopSysOptAlarmIndex(OltSysOpticalAlarm.E_ALM_PORT_SFP_RXPWR_HIGH);
        rxPwr.setTopSysOptAlarmSoapTime(portSfpRxRwr);
        list.add(rxPwr);
        OltSysOpticalAlarm txPwr = new OltSysOpticalAlarm();
        txPwr.setTopSysOptAlarmIndex(OltSysOpticalAlarm.E_ALM_PORT_SFP_TXPWR_HIGH);
        txPwr.setTopSysOptAlarmSoapTime(portSfpTxRwr);
        list.add(txPwr);
        //ONU : 上行发送,下行接收
        OltSysOpticalAlarm usTxPwr = new OltSysOpticalAlarm();
        usTxPwr.setTopSysOptAlarmIndex(OltSysOpticalAlarm.E_ALM_ONU_US_RXPWR_HIGH);
        usTxPwr.setTopSysOptAlarmSoapTime(onuUsTxRwr);
        list.add(usTxPwr);
        OltSysOpticalAlarm dsRxPwr = new OltSysOpticalAlarm();
        dsRxPwr.setTopSysOptAlarmIndex(OltSysOpticalAlarm.E_ALM_ONU_DS_RXPWR_HIGH);
        dsRxPwr.setTopSysOptAlarmSoapTime(onuDsRxRwr);
        list.add(dsRxPwr);
        oltOpticalService.modifySysOpticalAlarm(entityId, list);
        return NONE;
    }

    /**
     * 配置OLT PON端口光阈值
     * @return
     */
    @OperationLogProperty(actionName = "oltOpticalAction", operationName = "saveOltPonOptAlarm")
    public String saveOltPonOptAlarm() {
        List<OltPonOpticalAlarm> list = new ArrayList<OltPonOpticalAlarm>();
        OltPonOpticalAlarm txMinAlarm = new OltPonOpticalAlarm();
        txMinAlarm.setTopPonOptAlarmIndex(OltSysOpticalAlarm.E_ALM_PORT_SFP_TXPWR_LOW);
        txMinAlarm.setTopPonOptThresholdValue(txMin);
        txMinAlarm.setPortIndex(portIndex);
        txMinAlarm.setEntityId(entityId);
        if (txMin == null) {
            oltOpticalService.deletePonOpticalAlarm(txMinAlarm);
        } else {
            list.add(txMinAlarm);
        }
        OltPonOpticalAlarm txMaxAlarm = new OltPonOpticalAlarm();
        txMaxAlarm.setTopPonOptAlarmIndex(OltSysOpticalAlarm.E_ALM_PORT_SFP_TXPWR_HIGH);
        txMaxAlarm.setTopPonOptThresholdValue(txMax);
        txMaxAlarm.setEntityId(entityId);
        txMaxAlarm.setPortIndex(portIndex);
        if (txMax == null) {
            oltOpticalService.deletePonOpticalAlarm(txMaxAlarm);
        } else {
            list.add(txMaxAlarm);
        }
        oltOpticalService.modifyOltPonOptAlarm(entityId, list);
        return NONE;
    }

    /**
     * 清除OLT PON端口光阈值
     * @return
     */
    @OperationLogProperty(actionName = "oltOpticalAction", operationName = "delOltPonOptAlarm")
    public String deletePonOpticalAlarm() {
        OltPonOpticalAlarm optAlarm = new OltPonOpticalAlarm();
        optAlarm.setTopPonOptAlarmIndex(alarmIndex);
        optAlarm.setPortIndex(portIndex);
        optAlarm.setEntityId(entityId);
        oltOpticalService.deletePonOpticalAlarm(optAlarm);
        return NONE;
    }

    /**
     * 配置OLT ONU光阈值
     * @return
     */
    @OperationLogProperty(actionName = "oltOpticalAction", operationName = "saveOltOnuOptAlarm")
    public String saveOltOnuOptAlarm() {
        List<OltOnuOpticalAlarm> list = new ArrayList<OltOnuOpticalAlarm>();
        //modify by lzt 2014-05-27
        //修改ONU 光功率阈值告警先修改下限 再修改上限,否则导致修改的上限小于下限的配置失败
        OltOnuOpticalAlarm rxMinAlarm = new OltOnuOpticalAlarm();
        rxMinAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_ONU_DS_RXPWR_LOW);
        rxMinAlarm.setTopOnuOptThresholdValue(rxMin);
        rxMinAlarm.setEntityId(entityId);
        rxMinAlarm.setPortIndex(portIndex);
        if (rxMin == null) {
            oltOpticalService.deleteOnuOptAlarm(rxMinAlarm);
        } else {
            list.add(rxMinAlarm);
        }

        OltOnuOpticalAlarm txMinAlarm = new OltOnuOpticalAlarm();
        txMinAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_ONU_US_RXPWR_LOW);
        txMinAlarm.setTopOnuOptThresholdValue(txMin);
        txMinAlarm.setEntityId(entityId);
        txMinAlarm.setPortIndex(portIndex);
        if (txMin == null) {
            oltOpticalService.deleteOnuOptAlarm(txMinAlarm);
        } else {
            list.add(txMinAlarm);
        }

        OltOnuOpticalAlarm onuRxMinAlarm = new OltOnuOpticalAlarm();
        onuRxMinAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_PORT_SFP_RXPWR_LOW);
        onuRxMinAlarm.setTopOnuOptThresholdValue(onuRxMin);
        onuRxMinAlarm.setEntityId(entityId);
        onuRxMinAlarm.setPortIndex(portIndex);
        if (onuRxMin == null) {
            oltOpticalService.deleteOnuOptAlarm(onuRxMinAlarm);
        } else {
            list.add(onuRxMinAlarm);
        }

        OltOnuOpticalAlarm rxMaxAlarm = new OltOnuOpticalAlarm();
        rxMaxAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_ONU_DS_RXPWR_HIGH);
        rxMaxAlarm.setTopOnuOptThresholdValue(rxMax);
        rxMaxAlarm.setEntityId(entityId);
        rxMaxAlarm.setPortIndex(portIndex);
        if (rxMax == null) {
            oltOpticalService.deleteOnuOptAlarm(rxMaxAlarm);
        } else {
            list.add(rxMaxAlarm);
        }

        OltOnuOpticalAlarm txMaxAlarm = new OltOnuOpticalAlarm();
        txMaxAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_ONU_US_RXPWR_HIGH);
        txMaxAlarm.setTopOnuOptThresholdValue(txMax);
        txMaxAlarm.setEntityId(entityId);
        txMaxAlarm.setPortIndex(portIndex);
        if (txMax == null) {
            oltOpticalService.deleteOnuOptAlarm(txMaxAlarm);
        } else {
            list.add(txMaxAlarm);
        }

        OltOnuOpticalAlarm onuRxMaxAlarm = new OltOnuOpticalAlarm();
        onuRxMaxAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_PORT_SFP_RXPWR_HIGH);
        onuRxMaxAlarm.setTopOnuOptThresholdValue(onuRxMax);
        onuRxMaxAlarm.setEntityId(entityId);
        onuRxMaxAlarm.setPortIndex(portIndex);
        if (onuRxMax == null) {
            oltOpticalService.deleteOnuOptAlarm(onuRxMaxAlarm);
        } else {
            list.add(onuRxMaxAlarm);
        }

        oltOpticalService.modifyOltOnuOptAlarm(entityId, list);
        return NONE;
    }

    /**
     * 配置OLT ONU光阈值
     * @return
     */
    @OperationLogProperty(actionName = "oltOpticalAction", operationName = "saveOltOnuOptAlarm")
    public String saveOnuOptAlarm() {
        List<OltOnuOpticalAlarm> list = new ArrayList<OltOnuOpticalAlarm>();
        //modify by lzt 2014-05-27
        //修改ONU 光功率阈值告警先修改下限 再修改上限,否则导致修改的上限小于下限的配置失败
        OltOnuOpticalAlarm rxMinAlarm = new OltOnuOpticalAlarm();
        rxMinAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_ONU_RXPWR_LOW);
        rxMinAlarm.setTopOnuOptThresholdValue(rxMin);
        rxMinAlarm.setEntityId(entityId);
        rxMinAlarm.setPortIndex(portIndex);
        if (rxMin == null) {
            oltOpticalService.deleteOnuOptAlarm(rxMinAlarm);
        } else {
            list.add(rxMinAlarm);
        }

        OltOnuOpticalAlarm txMinAlarm = new OltOnuOpticalAlarm();
        txMinAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_ONU_TXPWR_LOW);
        txMinAlarm.setTopOnuOptThresholdValue(txMin);
        txMinAlarm.setEntityId(entityId);
        txMinAlarm.setPortIndex(portIndex);
        if (txMin == null) {
            oltOpticalService.deleteOnuOptAlarm(txMinAlarm);
        } else {
            list.add(txMinAlarm);
        }

        OltOnuOpticalAlarm onuRxMinAlarm = new OltOnuOpticalAlarm();
        onuRxMinAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_PORT_RXPWR_LOW);
        onuRxMinAlarm.setTopOnuOptThresholdValue(onuRxMin);
        onuRxMinAlarm.setEntityId(entityId);
        onuRxMinAlarm.setPortIndex(portIndex);
        if (onuRxMin == null) {
            oltOpticalService.deleteOnuOptAlarm(onuRxMinAlarm);
        } else {
            list.add(onuRxMinAlarm);
        }

        OltOnuOpticalAlarm rxMaxAlarm = new OltOnuOpticalAlarm();
        rxMaxAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_ONU_RXPWR_HIGH);
        rxMaxAlarm.setTopOnuOptThresholdValue(rxMax);
        rxMaxAlarm.setEntityId(entityId);
        rxMaxAlarm.setPortIndex(portIndex);
        if (rxMax == null) {
            oltOpticalService.deleteOnuOptAlarm(rxMaxAlarm);
        } else {
            list.add(rxMaxAlarm);
        }

        OltOnuOpticalAlarm txMaxAlarm = new OltOnuOpticalAlarm();
        txMaxAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_ONU_TXPWR_HIGH);
        txMaxAlarm.setTopOnuOptThresholdValue(txMax);
        txMaxAlarm.setEntityId(entityId);
        txMaxAlarm.setPortIndex(portIndex);
        if (txMax == null) {
            oltOpticalService.deleteOnuOptAlarm(txMaxAlarm);
        } else {
            list.add(txMaxAlarm);
        }

        OltOnuOpticalAlarm onuRxMaxAlarm = new OltOnuOpticalAlarm();
        onuRxMaxAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_PORT_RXPWR_HIGH);
        onuRxMaxAlarm.setTopOnuOptThresholdValue(onuRxMax);
        onuRxMaxAlarm.setEntityId(entityId);
        onuRxMaxAlarm.setPortIndex(portIndex);
        if (onuRxMax == null) {
            oltOpticalService.deleteOnuOptAlarm(onuRxMaxAlarm);
        } else {
            list.add(onuRxMaxAlarm);
        }
        oltOpticalService.modifyOltOnuOptAlarm(entityId, list);
        return NONE;
    }

    @OperationLogProperty(actionName = "oltOpticalAction", operationName = "saveOltOnuOptAlarm")
    public String saveOnuOptAlarmBack() {
        List<OltOnuOpticalAlarm> list = new ArrayList<OltOnuOpticalAlarm>();
        //modify by lzt 2014-05-27
        //修改ONU 光功率阈值告警先修改下限 再修改上限,否则导致修改的上限小于下限的配置失败
        OltOnuOpticalAlarm rxMinAlarm = new OltOnuOpticalAlarm();
        rxMinAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_PORT_SFP_TXPWR_LOW);
        rxMinAlarm.setTopOnuOptThresholdValue(rxMin);
        rxMinAlarm.setEntityId(entityId);
        rxMinAlarm.setPortIndex(portIndex);
        if (rxMin == null) {
            oltOpticalService.deleteOnuOptAlarm(rxMinAlarm);
        } else {
            list.add(rxMinAlarm);
        }

        OltOnuOpticalAlarm txMinAlarm = new OltOnuOpticalAlarm();
        txMinAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_ONU_DS_RXPWR_LOW);
        txMinAlarm.setTopOnuOptThresholdValue(txMin);
        txMinAlarm.setEntityId(entityId);
        txMinAlarm.setPortIndex(portIndex);
        if (txMin == null) {
            oltOpticalService.deleteOnuOptAlarm(txMinAlarm);
        } else {
            list.add(txMinAlarm);
        }

        OltOnuOpticalAlarm onuRxMinAlarm = new OltOnuOpticalAlarm();
        onuRxMinAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_PORT_RXPWR_LOW);
        onuRxMinAlarm.setTopOnuOptThresholdValue(onuRxMin);
        onuRxMinAlarm.setEntityId(entityId);
        onuRxMinAlarm.setPortIndex(portIndex);
        if (onuRxMin == null) {
            oltOpticalService.deleteOnuOptAlarm(onuRxMinAlarm);
        } else {
            list.add(onuRxMinAlarm);
        }

        OltOnuOpticalAlarm rxMaxAlarm = new OltOnuOpticalAlarm();
        rxMaxAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_PORT_SFP_TXPWR_HIGH);
        rxMaxAlarm.setTopOnuOptThresholdValue(rxMax);
        rxMaxAlarm.setEntityId(entityId);
        rxMaxAlarm.setPortIndex(portIndex);
        if (rxMax == null) {
            oltOpticalService.deleteOnuOptAlarm(rxMaxAlarm);
        } else {
            list.add(rxMaxAlarm);
        }

        OltOnuOpticalAlarm txMaxAlarm = new OltOnuOpticalAlarm();
        txMaxAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_ONU_DS_RXPWR_HIGH);
        txMaxAlarm.setTopOnuOptThresholdValue(txMax);
        txMaxAlarm.setEntityId(entityId);
        txMaxAlarm.setPortIndex(portIndex);
        if (txMax == null) {
            oltOpticalService.deleteOnuOptAlarm(txMaxAlarm);
        } else {
            list.add(txMaxAlarm);
        }

        OltOnuOpticalAlarm onuRxMaxAlarm = new OltOnuOpticalAlarm();
        onuRxMaxAlarm.setTopOnuOptAlarmIndex(OltSysOpticalAlarm.E_ALM_PORT_RXPWR_HIGH);
        onuRxMaxAlarm.setTopOnuOptThresholdValue(onuRxMax);
        onuRxMaxAlarm.setEntityId(entityId);
        onuRxMaxAlarm.setPortIndex(portIndex);
        if (onuRxMax == null) {
            oltOpticalService.deleteOnuOptAlarm(onuRxMaxAlarm);
        } else {
            list.add(onuRxMaxAlarm);
        }
        oltOpticalService.modifyOltOnuOptAlarm(entityId, list);
        return NONE;
    }

    /**
     * 清除ONU光阈值
     * @return
     */
    @OperationLogProperty(actionName = "oltOpticalAction", operationName = "delOnuOptAlarm")
    public String deleteOnuOpticalAlarm() {
        OltOnuOpticalAlarm optAlarm = new OltOnuOpticalAlarm();
        optAlarm.setTopOnuOptAlarmIndex(alarmIndex);
        optAlarm.setPortIndex(portIndex);
        optAlarm.setEntityId(entityId);
        oltOpticalService.deleteOnuOptAlarm(optAlarm);
        return NONE;
    }

    /**
    * OLT 光信息页面跳转
    * 
    * @return
    */
    public String showOltSniOptical() {
        portType = 0;
        OltSniOptical s = oltOpticalService.getOltSniOptical(portId);
        List<String> v = changeSniOptical(s);
        if (v.size() > 0) {
            opticalVal = JSONArray.fromObject(v);
        } else {
            opticalVal = JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    private List<String> changeSniOptical(OltSniOptical s) {
        List<String> v = new ArrayList<String>();
        if (s != null) {
            portIndex = s.getPortIndex();
            v.add((s.getIdentifier() > 3 ? 0 : s.getIdentifier()) + "");
            v.add(s.getVendorName());
            v.add(s.getWaveLength().toString());
            v.add(s.getVendorPN());
            v.add(s.getVendorSN());
            v.add(s.getDateCode());
            v.add(s.getOltSniDisplayTemp() != null ? s.getOltSniDisplayTemp().toString() : "--");
            v.add(s.getWorkingVoltage() != null ? s.getWorkingVoltage().toString() : "--");
            v.add(s.getBiasCurrent() != null ? s.getBiasCurrent().toString() : "--");
            v.add(s.getTxPower() != null ? s.getTxPower().toString() : "--");
            v.add(s.getRxPower() != null ? s.getRxPower().toString() : "--");
            v.add(s.getBitRate() != null ? s.getBitRate().toString() : "--");
        }
        if (portIndex == null || portIndex.equals(0l)) {
            portIndex = oltSniService.getSniAttribute(portId).getSniIndex();
        }
        return v;
    }

    /**
    * OLT 光信息页面跳转
    * 
    * @return
    */
    public String showOltPonOptical() {
        portType = 1;
        OltPonOptical s = oltOpticalService.getOltPonOptical(portId);
        List<String> v = changePonOptical(s);
        if (v.size() > 0) {
            opticalVal = JSONArray.fromObject(v);
        } else {
            opticalVal = JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    private List<String> changePonOptical(OltPonOptical s) {
        List<String> v = new ArrayList<String>();
        if (s != null) {
            portIndex = s.getPortIndex();
            v.add("" + (s.getIdentifier() > 10 ? 0 : s.getIdentifier()));
            v.add(s.getVendorName());
            v.add(s.getWaveLength().toString());
            v.add(s.getVendorPN());
            v.add(s.getVendorSN());
            v.add(s.getDateCode());
            v.add(s.getOltPonDisplayTemp() != null ? s.getOltPonDisplayTemp().toString() : "--");
            v.add(s.getWorkingVoltage() != null ? s.getWorkingVoltage().toString() : "--");
            v.add(s.getBiasCurrent() != null ? s.getBiasCurrent().toString() : "--");
            v.add(s.getTxPower() != null ? s.getTxPower().toString() : "--");
            v.add(s.getRxPower() != null ? s.getRxPower().toString() : "--");
            v.add(s.getBitRate().toString());
        }
        if (portIndex == null || portIndex.equals(0l)) {
            portIndex = oltPonService.getPonIndex(portId);
        }
        return v;
    }

    /**
    * 获取OLT上的所有光口光信息
    * 
    * @return
    * @throws Exception
    */
    public String getOltAllPortOptical() throws Exception {
        List<Map<String, String>> v = oltOpticalService.getOltAllPortOptical(entityId);
        if (v.size() > 0) {
            Map<String, List<Map<String, String>>> re = new HashMap<String, List<Map<String, String>>>();
            re.put("data", v);
            writeDataToAjax(JSONObject.fromObject(re));
        } else {
            writeDataToAjax(JSONObject.fromObject(false));
        }
        return NONE;
    }

    /**
    * 获取一条PON口光信息
    * 
    * @return
    * @throws Exception
    */
    public String loadOltPonOptical() throws Exception {
        portType = 1;
        OltPonOptical s = oltOpticalService.loadOltPonOptical(entityId, portIndex);
        List<String> v = changePonOptical(s);
        if (v.size() > 0) {
            Map<String, List<String>> re = new HashMap<String, List<String>>();
            re.put("data", v);
            writeDataToAjax(JSONObject.fromObject(re));
        } else {
            writeDataToAjax(JSONObject.fromObject(false));
        }
        return NONE;
    }

    /**
    * 获取一条SNI光口光信息
    * 
    * @return
    * @throws Exception
    */
    public String loadOltSniOptical() throws Exception {
        OltSniOptical s = oltOpticalService.loadOltSniOptical(entityId, portIndex);
        List<String> v = changeSniOptical(s);
        if (v.size() > 0) {
            Map<String, List<String>> re = new HashMap<String, List<String>>();
            re.put("data", v);
            writeDataToAjax(JSONObject.fromObject(re));
        } else {
            writeDataToAjax(JSONObject.fromObject(false));
        }
        return NONE;
    }

    /**
    * ONU PON 光信息页面跳转
    * 
    * @return
    */
    public String showOnuPonOptical() {
        OnuPonOptical s = oltOpticalService.getOnuPonOptical(entityId, portIndex);
        List<Object> v = new ArrayList<Object>();
        if (s != null) {
            v.add(portIndex);
            v.add(s.getWorkingTemp());
            v.add(s.getRxPower());
            v.add(s.getTxPower());
            v.add(s.getBiasCurrent());
            v.add(s.getWorkingVoltage());
            v.add(s.getOnuPonDisplayTemp());
        }
        if (v.size() > 0) {
            opticalVal = JSONArray.fromObject(v);
        } else {
            opticalVal = JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    /**
    * 获取一条ONU PON光信息数据
    * 
    * @return
    * @throws Exception
    */
    public String loadOnuPonOptical() throws Exception {
        OnuPonOptical o = oltOpticalService.loadOnuPonOptical(entityId, portIndex);
        List<Object> v = new ArrayList<Object>();
        if (o != null) {
            v.add(portIndex);
            v.add(o.getWorkingTemp());
            v.add(o.getRxPower());
            v.add(o.getTxPower());
            v.add(o.getBiasCurrent());
            v.add(o.getWorkingVoltage());
            v.add(o.getOnuPonDisplayTemp());
        }
        if (v.size() > 0) {
            Map<String, List<Object>> re = new HashMap<String, List<Object>>();
            re.put("data", v);
            writeDataToAjax(JSONObject.fromObject(re));
        } else {
            writeDataToAjax(JSONObject.fromObject(false));
        }
        return NONE;
    }

    /**
    * 获取所有ONU上的光口光信息
    * 
    * @return
    * @throws Exception
    */
    public String getAllOnuOptical() throws Exception {
        List<OnuPonOptical> os = oltOpticalService.getAllOnuOptical(entityId);
        List<List<Object>> v = new ArrayList<List<Object>>();
        if (os.size() > 0) {
            for (OnuPonOptical o : os) {
                List<Object> tmp = new ArrayList<Object>();
                tmp.add(o.getOnuPonIndex());
                tmp.add(o.getWorkingTemp());
                tmp.add(o.getRxPower());
                tmp.add(o.getTxPower());
                tmp.add(o.getBiasCurrent());
                tmp.add(o.getWorkingVoltage());
                v.add(tmp);
            }
        }
        if (v.size() > 0) {
            Map<String, List<List<Object>>> re = new HashMap<String, List<List<Object>>>();
            re.put("data", v);
            writeDataToAjax(JSONObject.fromObject(re));
        } else {
            writeDataToAjax(JSONObject.fromObject(false));
        }
        return NONE;
    }

    /**
     * 从设备获取全局光功率阀值数据
     * @return
     */
    public String refreshSysOpticalAlarm() {
        oltOpticalService.refreshOltSysOpticalAlarm(entityId);
        return NONE;
    }

    /**
     * 从设备获取Pon光功率阀值数据
     * @return
     */
    public String refreshPonOpticalAlarm() {
        oltOpticalService.refreshOltPonOpticalAlarm(entityId);
        return NONE;
    }

    /**
     * 从设备获取Onu光功率阀值数据
     * @return
     */
    public String refreshOnuOpticalAlarm() {
        oltOpticalService.refreshOltOnuOpticalAlarm(entityId);
        return NONE;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public Integer getPortType() {
        return portType;
    }

    public void setPortType(Integer portType) {
        this.portType = portType;
    }

    public Integer getRxMax() {
        return rxMax;
    }

    public void setRxMax(Integer rxMax) {
        this.rxMax = rxMax;
    }

    public Integer getRxMin() {
        return rxMin;
    }

    public void setRxMin(Integer rxMin) {
        this.rxMin = rxMin;
    }

    public Integer getTxMax() {
        return txMax;
    }

    public void setTxMax(Integer txMax) {
        this.txMax = txMax;
    }

    public Integer getTxMin() {
        return txMin;
    }

    public void setTxMin(Integer txMin) {
        this.txMin = txMin;
    }

    public int getPortSfpRxRwr() {
        return portSfpRxRwr;
    }

    public void setPortSfpRxRwr(int portSfpRxRwr) {
        this.portSfpRxRwr = portSfpRxRwr;
    }

    public int getPortSfpTxRwr() {
        return portSfpTxRwr;
    }

    public void setPortSfpTxRwr(int portSfpTxRwr) {
        this.portSfpTxRwr = portSfpTxRwr;
    }

    public int getOnuUsTxRwr() {
        return onuUsTxRwr;
    }

    public void setOnuUsTxRwr(int onuUsTxRwr) {
        this.onuUsTxRwr = onuUsTxRwr;
    }

    public int getOnuDsRxRwr() {
        return onuDsRxRwr;
    }

    public void setOnuDsRxRwr(int onuDsRxRwr) {
        this.onuDsRxRwr = onuDsRxRwr;
    }

    public JSONArray getOltSysOpticalList() {
        return oltSysOpticalList;
    }

    public void setOltSysOpticalList(JSONArray oltSysOpticalList) {
        this.oltSysOpticalList = oltSysOpticalList;
    }

    public JSONArray getOnuOpticalList() {
        return onuOpticalList;
    }

    public void setOnuOpticalList(JSONArray onuOpticalList) {
        this.onuOpticalList = onuOpticalList;
    }

    public JSONArray getPonOpticalList() {
        return ponOpticalList;
    }

    public void setPonOpticalList(JSONArray ponOpticalList) {
        this.ponOpticalList = ponOpticalList;
    }

    public JSONArray getOpticalVal() {
        return opticalVal;
    }

    public void setOpticalVal(JSONArray opticalVal) {
        this.opticalVal = opticalVal;
    }

    public Integer getAlarmIndex() {
        return alarmIndex;
    }

    public void setAlarmIndex(Integer alarmIndex) {
        this.alarmIndex = alarmIndex;
    }

    public Integer getOnuRxMax() {
        return onuRxMax;
    }

    public void setOnuRxMax(Integer onuRxMax) {
        this.onuRxMax = onuRxMax;
    }

    public Integer getOnuRxMin() {
        return onuRxMin;
    }

    public void setOnuRxMIn(Integer onuRxMin) {
        this.onuRxMin = onuRxMin;
    }

    public void setOnuRxMin(Integer onuRxMin) {
        this.onuRxMin = onuRxMin;
    }

}
