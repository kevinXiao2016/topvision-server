/***********************************************************************
 * $Id: OltQosAction.java,v1.0 2013年10月25日 下午5:47:47 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.exception.ModifyOnuQosMapRuleException;
import com.topvision.ems.epon.exception.RefreshQosPortBaseQosMapTableException;
import com.topvision.ems.epon.exception.RefreshQosPortBaseQosPolicyTableException;
import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.qos.domain.QosDeviceBaseQosMapTable;
import com.topvision.ems.epon.qos.domain.QosDeviceBaseQosPolicyTable;
import com.topvision.ems.epon.qos.domain.QosPortBaseQosMapTable;
import com.topvision.ems.epon.qos.domain.QosPortBaseQosPolicyTable;
import com.topvision.ems.epon.qos.service.OltQosService;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.domain.OperationLog;

import net.sf.json.JSONObject;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:47:47
 *
 */
@Controller("oltQosAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltQosAction extends AbstractEponAction {
    private static final long serialVersionUID = 6630185175110448105L;
    private final Logger logger = LoggerFactory.getLogger(OltQosAction.class);
    private String qosPortBaseQosMapTable;
    private String qosPortBaseQosPolicyTable;
    private String qosDeviceBaseQosMapTable;
    private String qosDeviceBaseQosPolicyTable;
    private String portBaseQosPolicyWeightOctetList;
    private String portBaseQosPolicySpBandwidthRangeList;
    private Long portIndex;
    private Integer portBaseQosPolicyMode;
    private String portBaseQosMapOctetList;
    private Long onuIndex;
    private Integer deviceBaseQosPolicyMode;
    private String deviceBaseQosPolicyWeightOctetList;
    private String deviceBaseQosPolicySpBandwidthRangeList;
    private String deviceBaseQosMapOctetList;
    private Integer onuQosMapRuleIndex;
    // 用于操作日志国际化时表示来源的变量
    private String source;
    @Autowired
    private OltQosService oltQosService;

    /**
     * 跳转到OLT侧优先级映射页面(端口)
     */
    public String showQosPortMap() {
        QosPortBaseQosMapTable qosMap = oltQosService.getPortQosMapRule(entityId, portIndex);
        qosPortBaseQosMapTable = JSONObject.fromObject(qosMap).toString();
        return SUCCESS;
    }

    /**
     * 跳转到OLT侧优先级映射页面(OLT)
     */
    public String showQosOltMap() {
        portIndex = 0l;
        QosPortBaseQosMapTable qosMap = oltQosService.getPortQosMapRule(entityId, portIndex);
        qosPortBaseQosMapTable = JSONObject.fromObject(qosMap).toString();
        return SUCCESS;
    }

    /**
     * 跳转到QoS端口策略页面
     * 
     * @return String
     */
    public String showQosPortPolicy() {
        QosPortBaseQosPolicyTable policy = oltQosService.getPortQosPolicy(entityId, portIndex);
        qosPortBaseQosPolicyTable = JSONObject.fromObject(policy).toString();
        return SUCCESS;
    }

    /**
     * 跳转到QoS策略页面(OLT)
     * 
     * @return String
     */
    public String showQosOltPolicy() {
        portIndex = 0l;
        QosPortBaseQosPolicyTable policy = oltQosService.getPortQosPolicy(entityId, portIndex);
        qosPortBaseQosPolicyTable = JSONObject.fromObject(policy).toString();
        return SUCCESS;
    }

    /**
     * 跳转QOS onu侧优先级映射页面
     */
    public String showQosOnuMap() {
        QosDeviceBaseQosMapTable map = oltQosService.getOnuQosMapRule(entityId, onuIndex);
        qosDeviceBaseQosMapTable = JSONObject.fromObject(map).toString();
        return SUCCESS;
    }

    /**
     * 跳转到QoS ONU侧策略页面
     * @return String
     */
    public String showQosOnuPolicy() {
        QosDeviceBaseQosPolicyTable policy = oltQosService.getOnuQosPolicy(entityId, onuIndex);
        qosPortBaseQosPolicyTable = JSONObject.fromObject(policy).toString();
        return SUCCESS;
    }

    /**
     * 修改OnuQosMapRule映射
     * @return
     * @throws IOException 
     */
    @OperationLogProperty(actionName = "oltQosAction", operationName = "modifyOnuQosMapRule")
    public String modifyOnuQosMapRule() throws IOException {
        String result = "success";
        String[] mapOctet = deviceBaseQosMapOctetList.split(",");
        List<Integer> deviceBaseQosMapOctetList = new ArrayList<Integer>();
        for (String obj : mapOctet) {
            deviceBaseQosMapOctetList.add(Integer.parseInt(obj));
        }
        try {
            oltQosService.modifyOnuQosMapRule(entityId, onuIndex, onuQosMapRuleIndex, deviceBaseQosMapOctetList);
            source = EponIndex.getOnuStringByIndex(onuIndex).toString();
            operationResult = OperationLog.SUCCESS;
        } catch (ModifyOnuQosMapRuleException e) {
            result = "error";
            operationResult = OperationLog.FAILURE;
            logger.error("ModifyOnuQosMapRuleException : {}", e);
        } catch (Exception e) {
            result = "error";
            operationResult = OperationLog.FAILURE;
            logger.error("UnChecked Exception : {}", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 修改OLT端口的QOS策略
     * 
     * @return
     * @throws IOException 
     */
    @OperationLogProperty(actionName = "oltQosAction", operationName = "modifyPortQosPolicy")
    public String modifyPortQosPolicy() throws IOException {
        String result = "success";
        String[] map = portBaseQosPolicyWeightOctetList.split(",");
        List<Integer> weight = new ArrayList<Integer>();
        for (int i = 0; i < map.length; i++) {
            weight.add(Integer.parseInt(map[i]));
        }
        String[] arr = portBaseQosPolicySpBandwidthRangeList.split(",");
        List<Integer> bd = new ArrayList<Integer>();
        for (int i = 0; i < arr.length; i++) {
            bd.add(i, Integer.parseInt(arr[i]));
        }
        try {
            oltQosService.modifyPortQosPolicy(entityId, portIndex, portBaseQosPolicyMode, weight, bd);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            result = "error";
            logger.debug("modifyPortQosPolicy error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 保存QOS映射文件
     * @throws IOException 
     */
    @OperationLogProperty(actionName = "oltQosAction", operationName = "modifyPortQosMapRule")
    public String modifyPortQosMapRule() throws IOException {
        String result = "success";
        String[] map = portBaseQosMapOctetList.split(",");
        List<Integer> list = new ArrayList<Integer>();
        QosPortBaseQosMapTable qosMap = new QosPortBaseQosMapTable();
        for (String obj : map) {
            list.add(Integer.parseInt(obj));
        }
        qosMap.setEntityId(entityId);
        qosMap.setPortIndex(portIndex);
        qosMap.setPortBaseQosMapOctetList(list);
        // portQosMap的策略只有COS一种，index为1
        try {
            oltQosService.modifyPortQosMapRule(entityId, portIndex, 1, list);
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("modifyPortQosMapRule error: {}", e);
            operationResult = OperationLog.FAILURE;
            result = "error";
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 修改ONU侧QOS策略
     * 
     * @return
     * @throws IOException 
     */
    @OperationLogProperty(actionName = "oltQosAction", operationName = "modifyOnuQosPolicy")
    public String modifyOnuQosPolicy() throws IOException {
        String result = "success";
        QosDeviceBaseQosPolicyTable qos = new QosDeviceBaseQosPolicyTable();
        qos.setPolicyMode(deviceBaseQosPolicyMode);
        qos.setEntityId(entityId);
        String[] map = deviceBaseQosPolicyWeightOctetList.split(",");
        List<Integer> weight = new ArrayList<Integer>();
        for (int i = 0; i < map.length; i++) {
            weight.add(Integer.parseInt(map[i]));
        }
        String[] arr = deviceBaseQosPolicySpBandwidthRangeList.split(",");
        List<Integer> bd = new ArrayList<Integer>();
        for (int i = 0; i < arr.length; i++) {
            bd.add(Integer.parseInt(arr[i]));
        }
        // TODO 注释掉带宽，因为暂时不支持
        // qos.setDeviceBaseQosPolicySpBandwidthRangeList(bd);
        qos.setDeviceBaseQosPolicyWeightOctetList(weight);
        try {
            oltQosService.modifyOnuQosPolicy(entityId, onuIndex, deviceBaseQosPolicyMode, weight, bd);
            source = EponIndex.getOnuStringByIndex(onuIndex).toString();
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            result = "error";
            logger.debug("modifyOnuQosPolicy error: {}", e);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 从设备获取QOS队列映射信息
     * 
     * @return
     * @throws Exception
     */
    public String refreshPortMap() throws Exception {
        String result;
        try {
            oltQosService.refreshQosPortBaseQosMapTable(entityId);
            result = "success";
        } catch (RefreshQosPortBaseQosMapTableException sse) {
            result = sse.getMessage();
            logger.debug("refreshQosPortBaseQosMapTable error:{}", sse);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 从设备获取QOS队列策略信息
     * 
     * @return
     * @throws Exception
     */
    public String refreshPortPolicy() throws Exception {
        String result;
        try {
            oltQosService.refreshQosPortBaseQosPolicyTable(entityId);
            result = "success";
        } catch (RefreshQosPortBaseQosPolicyTableException sse) {
            result = sse.getMessage();
            logger.debug("refreshQosPortBaseQosPolicyTable error:{}", sse);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 从设备获取QOS ONU队列映射信息
     * 
     * @return
     * @throws Exception
     */
    public String refreshQosOnuMap() throws Exception {
        String result;
        try {
            oltQosService.refreshQosDeviceBaseQosMapTable(entityId);
            result = "success";
        } catch (Exception e) {
            result = e.getMessage();
            logger.debug("refreshQosOnuMap error:{}", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 从设备获取ONU的QoS策略信息
     * 
     * @return
     * @throws Exception
     */
    public String refreshQosOnuPolicy() throws Exception {
        String result;
        try {
            oltQosService.refreshQosDeviceBaseQosPolicyTable(entityId);
            result = "success";
        } catch (Exception e) {
            result = e.getMessage();
            logger.debug("refreshQosOnuPolicy error:{}", e);
        }
        writeDataToAjax(result);
        return NONE;
    }

    public String getQosPortBaseQosMapTable() {
        return qosPortBaseQosMapTable;
    }

    public void setQosPortBaseQosMapTable(String qosPortBaseQosMapTable) {
        this.qosPortBaseQosMapTable = qosPortBaseQosMapTable;
    }

    public String getQosPortBaseQosPolicyTable() {
        return qosPortBaseQosPolicyTable;
    }

    public void setQosPortBaseQosPolicyTable(String qosPortBaseQosPolicyTable) {
        this.qosPortBaseQosPolicyTable = qosPortBaseQosPolicyTable;
    }

    public String getQosDeviceBaseQosMapTable() {
        return qosDeviceBaseQosMapTable;
    }

    public void setQosDeviceBaseQosMapTable(String qosDeviceBaseQosMapTable) {
        this.qosDeviceBaseQosMapTable = qosDeviceBaseQosMapTable;
    }

    public String getQosDeviceBaseQosPolicyTable() {
        return qosDeviceBaseQosPolicyTable;
    }

    public void setQosDeviceBaseQosPolicyTable(String qosDeviceBaseQosPolicyTable) {
        this.qosDeviceBaseQosPolicyTable = qosDeviceBaseQosPolicyTable;
    }

    public String getPortBaseQosPolicyWeightOctetList() {
        return portBaseQosPolicyWeightOctetList;
    }

    public void setPortBaseQosPolicyWeightOctetList(String portBaseQosPolicyWeightOctetList) {
        this.portBaseQosPolicyWeightOctetList = portBaseQosPolicyWeightOctetList;
    }

    public String getPortBaseQosPolicySpBandwidthRangeList() {
        return portBaseQosPolicySpBandwidthRangeList;
    }

    public void setPortBaseQosPolicySpBandwidthRangeList(String portBaseQosPolicySpBandwidthRangeList) {
        this.portBaseQosPolicySpBandwidthRangeList = portBaseQosPolicySpBandwidthRangeList;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getPortBaseQosPolicyMode() {
        return portBaseQosPolicyMode;
    }

    public void setPortBaseQosPolicyMode(Integer portBaseQosPolicyMode) {
        this.portBaseQosPolicyMode = portBaseQosPolicyMode;
    }

    public String getPortBaseQosMapOctetList() {
        return portBaseQosMapOctetList;
    }

    public void setPortBaseQosMapOctetList(String portBaseQosMapOctetList) {
        this.portBaseQosMapOctetList = portBaseQosMapOctetList;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Integer getDeviceBaseQosPolicyMode() {
        return deviceBaseQosPolicyMode;
    }

    public void setDeviceBaseQosPolicyMode(Integer deviceBaseQosPolicyMode) {
        this.deviceBaseQosPolicyMode = deviceBaseQosPolicyMode;
    }

    public String getDeviceBaseQosPolicyWeightOctetList() {
        return deviceBaseQosPolicyWeightOctetList;
    }

    public void setDeviceBaseQosPolicyWeightOctetList(String deviceBaseQosPolicyWeightOctetList) {
        this.deviceBaseQosPolicyWeightOctetList = deviceBaseQosPolicyWeightOctetList;
    }

    public String getDeviceBaseQosPolicySpBandwidthRangeList() {
        return deviceBaseQosPolicySpBandwidthRangeList;
    }

    public void setDeviceBaseQosPolicySpBandwidthRangeList(String deviceBaseQosPolicySpBandwidthRangeList) {
        this.deviceBaseQosPolicySpBandwidthRangeList = deviceBaseQosPolicySpBandwidthRangeList;
    }

    public String getDeviceBaseQosMapOctetList() {
        return deviceBaseQosMapOctetList;
    }

    public void setDeviceBaseQosMapOctetList(String deviceBaseQosMapOctetList) {
        this.deviceBaseQosMapOctetList = deviceBaseQosMapOctetList;
    }

    public Integer getOnuQosMapRuleIndex() {
        return onuQosMapRuleIndex;
    }

    public void setOnuQosMapRuleIndex(Integer onuQosMapRuleIndex) {
        this.onuQosMapRuleIndex = onuQosMapRuleIndex;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

}
