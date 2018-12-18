/***********************************************************************
 * $Id: OnuSlaAction.java,v1.0 2013年10月25日 下午5:47:58 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.action;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.olt.action.AbstractEponAction;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.service.OltPonService;
import com.topvision.ems.epon.qos.domain.SlaTable;
import com.topvision.ems.epon.qos.service.OltQosService;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.platform.domain.OperationLog;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:47:58
 *
 */
@Controller("onuSlaAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuSlaAction extends AbstractEponAction {
    private static final long serialVersionUID = -2850637656751878926L;
    private final Logger logger = LoggerFactory.getLogger(OnuSlaAction.class);

    private Long portId;
    private Long onuIndex;
    private JSONArray slaValue;
    private Long dsCir;
    private Long dsPir;
    private Long usCir;
    private Long usFir;
    private Long usPir;
    private JSONArray otherAllDsCir;
    private JSONArray otherAllUsCir;
    // 用于操作日志国际化时表示来源的变量
    private String source;
    private Long portUpBandWidth;
    private Long portDownBandWidth;
    @Autowired
    private OltQosService oltQosService;
    @Autowired
    private OltPonService oltPonService;

    /**
     * 显示ONU的SLA管理
     * 
     * @return String
     */
    public String showOnuSlaConfig() {
        List<Long> tempList = new ArrayList<Long>();
        SlaTable slaObj = oltQosService.getOnuSlaList(entityId, onuIndex);
        Long ponIndex = EponIndex.getPonIndex(onuIndex);
        portId = oltPonService.getPonIdByIndex(entityId, ponIndex);
        OltPonAttribute oltPonAttribute = oltPonService.getPonAttribute(portId);
        Integer tempAllDs = 0;
        Integer tempAllUs = 0;
        Long fixUsBW = 0L;
        Long fixDsBw = 0L;
        if (slaObj != null) {
            tempList.add(slaObj.getSlaDsCommittedBW());
            tempList.add(slaObj.getSlaDsPeakBW());
            tempList.add(slaObj.getSlaUsFixedBW());
            tempList.add(slaObj.getSlaUsCommittedBW());
            tempList.add(slaObj.getSlaUsPeakBW());
            // ONU所在PON口的所有ONU的上、下行CIR总和
            List<SlaTable> slaList = new ArrayList<SlaTable>();
            slaList = oltQosService.getOnuSlaListInAPon(entityId, onuIndex);
            if (slaList != null && slaList.size() != 0) {
                for (SlaTable sla : slaList) {
                    fixUsBW += sla.getSlaUsFixedBW() == null ? 0 : sla.getSlaUsFixedBW();
                    fixDsBw += sla.getSlaDsFixedBW() == null ? 0 : sla.getSlaDsFixedBW();
                    tempAllDs += sla.getSlaDsCommittedBW().intValue();
                    tempAllUs += sla.getSlaUsCommittedBW().intValue();
                }
            }
            // 获得PON口下除该ONU以外的其他所有ONU的CIR的总和
            tempAllDs -= slaObj.getSlaDsCommittedBW().intValue();
            tempAllUs -= slaObj.getSlaUsCommittedBW().intValue();
        }
        if (tempList == null || tempList.size() == 0) {
            slaValue = JSONArray.fromObject(false);
        } else {
            slaValue = JSONArray.fromObject(tempList);
        }
        portUpBandWidth = Long.parseLong(oltPonAttribute.getMaxUsBandwidth().toString()) - fixUsBW;
        portDownBandWidth = Long.parseLong(oltPonAttribute.getMaxDsBandwidth().toString()) - fixDsBw;
        otherAllDsCir = JSONArray.fromObject(tempAllDs);
        otherAllUsCir = JSONArray.fromObject(tempAllUs);
        return SUCCESS;
    }

    /**
     * 显示ONU的SLA管理
     * 
     * @return String
     */
    public String showOnuSlaConfigBack() {
        List<Long> tempList = new ArrayList<Long>();
        SlaTable slaObj = oltQosService.getOnuSlaList(entityId, onuIndex);
        Long ponIndex = EponIndex.getPonIndex(onuIndex);
        portId = oltPonService.getPonIdByIndex(entityId, ponIndex);
        OltPonAttribute oltPonAttribute = oltPonService.getPonAttribute(portId);
        Integer tempAllDs = 0;
        Integer tempAllUs = 0;
        Long fixUsBW = 0L;
        Long fixDsBw = 0L;
        if (slaObj != null) {
            tempList.add(slaObj.getSlaDsCommittedBW());
            tempList.add(slaObj.getSlaDsPeakBW());
            tempList.add(slaObj.getSlaUsFixedBW());
            tempList.add(slaObj.getSlaUsCommittedBW());
            tempList.add(slaObj.getSlaUsPeakBW());
            // ONU所在PON口的所有ONU的上、下行CIR总和
            List<SlaTable> slaList = new ArrayList<SlaTable>();
            slaList = oltQosService.getOnuSlaListInAPon(entityId, onuIndex);
            if (slaList != null && slaList.size() != 0) {
                for (SlaTable sla : slaList) {
                    fixUsBW += sla.getSlaUsFixedBW() == null ? 0 : sla.getSlaUsFixedBW();
                    fixDsBw += sla.getSlaDsFixedBW() == null ? 0 : sla.getSlaDsFixedBW();
                    tempAllDs += sla.getSlaDsCommittedBW().intValue();
                    tempAllUs += sla.getSlaUsCommittedBW().intValue();
                }
            }
            // 获得PON口下除该ONU以外的其他所有ONU的CIR的总和
            tempAllDs -= slaObj.getSlaDsCommittedBW().intValue();
            tempAllUs -= slaObj.getSlaUsCommittedBW().intValue();
        }
        if (tempList == null || tempList.size() == 0) {
            slaValue = JSONArray.fromObject(false);
        } else {
            slaValue = JSONArray.fromObject(tempList);
        }
        portUpBandWidth = Long.parseLong(oltPonAttribute.getMaxUsBandwidth().toString()) - fixUsBW;
        portDownBandWidth = Long.parseLong(oltPonAttribute.getMaxDsBandwidth().toString()) - fixDsBw;
        otherAllDsCir = JSONArray.fromObject(tempAllDs);
        otherAllUsCir = JSONArray.fromObject(tempAllUs);
        return SUCCESS;
    }

    /**
     * 修改ONU的SLA参数
     * 
     * @param SlaTable
     */
    @OperationLogProperty(actionName = "oltQosAction", operationName = "modifyOnuSlaConfig")
    public String modifyOnuSlaConfig() throws Exception {
        SlaTable slaTable = new SlaTable();
        slaTable.setEntityId(entityId);
        slaTable.setOnuIndex(onuIndex);
        slaTable.setSlaDsCommittedBW(dsCir);
        slaTable.setSlaDsPeakBW(dsPir);
        slaTable.setSlaUsCommittedBW(usCir);
        slaTable.setSlaUsFixedBW(usFir);
        slaTable.setSlaUsPeakBW(usPir);
        String message = "success";
        try {
            oltQosService.modifyOnuSlaList(slaTable);
            source = EponIndex.getOnuStringByIndex(onuIndex).toString();
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.debug("modifyOnuSlaConfig error: {}", e);
            operationResult = OperationLog.FAILURE;
            message = e.getMessage();
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
     * 刷新所有ONU SLA配置信息
     * 
     * @return
     * @throws Exception
     */
    public String refrshOnuSla() throws Exception {
        String message = "success";
        try {
            oltQosService.refreshSlaTable(entityId);
        } catch (Exception e) {
            logger.debug("refrshOnuSla:{}", e.getMessage());
            message = "refrshOnuSla failed";
        }
        writeDataToAjax(message);
        return NONE;
    }

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public JSONArray getSlaValue() {
        return slaValue;
    }

    public void setSlaValue(JSONArray slaValue) {
        this.slaValue = slaValue;
    }

    public Long getDsCir() {
        return dsCir;
    }

    public void setDsCir(Long dsCir) {
        this.dsCir = dsCir;
    }

    public Long getDsPir() {
        return dsPir;
    }

    public void setDsPir(Long dsPir) {
        this.dsPir = dsPir;
    }

    public Long getUsCir() {
        return usCir;
    }

    public void setUsCir(Long usCir) {
        this.usCir = usCir;
    }

    public Long getUsFir() {
        return usFir;
    }

    public void setUsFir(Long usFir) {
        this.usFir = usFir;
    }

    public Long getUsPir() {
        return usPir;
    }

    public void setUsPir(Long usPir) {
        this.usPir = usPir;
    }

    public JSONArray getOtherAllDsCir() {
        return otherAllDsCir;
    }

    public void setOtherAllDsCir(JSONArray otherAllDsCir) {
        this.otherAllDsCir = otherAllDsCir;
    }

    public JSONArray getOtherAllUsCir() {
        return otherAllUsCir;
    }

    public void setOtherAllUsCir(JSONArray otherAllUsCir) {
        this.otherAllUsCir = otherAllUsCir;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    public Long getPortUpBandWidth() {
        return portUpBandWidth;
    }

    public void setPortUpBandWidth(Long portUpBandWidth) {
        this.portUpBandWidth = portUpBandWidth;
    }

    public Long getPortDownBandWidth() {
        return portDownBandWidth;
    }

    public void setPortDownBandWidth(Long portDownBandWidth) {
        this.portDownBandWidth = portDownBandWidth;
    }

}
