/***********************************************************************
 * $Id: OltPonPortReportAction.java,v1.0 2013-10-28 上午9:58:13 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltponport.action;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.report.oltponport.service.OltPonPortReportCreator;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author haojie
 * @created @2013-10-28-上午9:58:13
 * 
 */
@Controller("oltPonPortReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltPonPortReportAction extends BaseAction {
    private static final long serialVersionUID = 5112858047698344753L;
    protected Date statDate;
    @Autowired
    private OltPonPortReportCreator oltPonPortReportCreator;
    private Integer entityType;
    private String entityIp;
    private Integer adminState;
    private Integer operationState;
    private List<OltPonAttribute> ponlist = null;
    @Autowired
    private EntityTypeService entityTypeService;
    private List<EntityType> entityTypes;

    /**
     * 查看Pon端口统计页面
     * 
     * @return String
     */
    public String showEponPonPortReport() {
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        if (entityType != null && !entityType.equals("") && entityType > 0) {
            map.put("entityType", entityType);
        }
        if (entityIp != null && !entityIp.equals("")) {
            map.put("entityIp", entityIp);
        }
        if (adminState != null && adminState != 0) {
            map.put("adminState", adminState);
        }
        if (operationState != null && operationState != 0) {
            map.put("operationState", operationState);
        }
        ponlist = oltPonPortReportCreator.getPonPortList(map);
        for (OltPonAttribute aPonlist : ponlist) {
            if (aPonlist.getPonPortAdminStatus() == 1) {
                aPonlist.setPonPortAdminStatusString("UP");
            } else if (aPonlist.getPonPortAdminStatus() == 2) {
                aPonlist.setPonPortAdminStatusString("DOWN");
            }
            if (aPonlist.getPonOperationStatus() == 1) {
                aPonlist.setPonOperationStatusString("UP");
            } else if (aPonlist.getPonOperationStatus() == 2) {
                aPonlist.setPonOperationStatusString("DOWN");
            }
            switch (aPonlist.getPonPortType()) {
            case 1:
                aPonlist.setPonPortTypeString("ge-epon");
                break;
            case 2:
                aPonlist.setPonPortTypeString("tenge-epon");
                break;
            case 3:
                aPonlist.setPonPortTypeString("gpon");
                break;
            default:
                break;
            }
            aPonlist.setPonPort(EponIndex.getSlotNo(aPonlist.getPonIndex()) + "-"
                    + EponIndex.getPonNo(aPonlist.getPonIndex()));
            aPonlist.setPonPortName("PORT-" + EponIndex.getSlotNo(aPonlist.getPonIndex()).toString() + "-"
                    + EponIndex.getPonNo(aPonlist.getPonIndex()).toString());
        }

        Long oltType = entityTypeService.getOltType();
        entityTypes = entityTypeService.loadSubType(oltType);
        return SUCCESS;
    }

    /**
     * eponDeviceSniPortReport.jsp 导出PON端口使用情况清单到EXCEL
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String exportDevicePonToExcel() throws UnsupportedEncodingException {
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        if (entityType != null && !entityType.equals("") && entityType > 0) {
            map.put("entityType", entityType);
        }
        if (entityIp != null && !entityIp.equals("")) {
            map.put("entityIp", entityIp);
        }
        if (adminState != null && adminState != 0) {
            map.put("adminState", adminState);
        }
        if (operationState != null && operationState != 0) {
            map.put("operationState", operationState);
        }
        List<OltPonAttribute> ponlist = oltPonPortReportCreator.getPonPortList(map);
        for (OltPonAttribute aPonlist : ponlist) {
            if (aPonlist.getPonPortAdminStatus() == 1) {
                aPonlist.setPonPortAdminStatusString("UP");
            } else if (aPonlist.getPonPortAdminStatus() == 2) {
                aPonlist.setPonPortAdminStatusString("DOWN");
            }
            if (aPonlist.getPonOperationStatus() == 1) {
                aPonlist.setPonOperationStatusString("UP");
            } else if (aPonlist.getPonOperationStatus() == 2) {
                aPonlist.setPonOperationStatusString("DOWN");
            }
            switch (aPonlist.getPonPortType()) {
            case 1:
                aPonlist.setPonPortTypeString("ge-epon");
                break;
            case 2:
                aPonlist.setPonPortTypeString("tenge-epon");
                break;
            case 3:
                aPonlist.setPonPortTypeString("gpon");
                break;
            default:
                break;
            }
            aPonlist.setPonPort(EponIndex.getSlotNo(aPonlist.getPonIndex()) + "-"
                    + EponIndex.getPonNo(aPonlist.getPonIndex()));
            aPonlist.setPonPortName("PORT-" + EponIndex.getSlotNo(aPonlist.getPonIndex()).toString() + "-"
                    + EponIndex.getPonNo(aPonlist.getPonIndex()).toString());
        }
        oltPonPortReportCreator.exportOltPonPortReportToExcel(ponlist, statDate);
        return NONE;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public OltPonPortReportCreator getOltPonPortReportCreator() {
        return oltPonPortReportCreator;
    }

    public void setOltPonPortReportCreator(OltPonPortReportCreator oltPonPortReportCreator) {
        this.oltPonPortReportCreator = oltPonPortReportCreator;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public Integer getAdminState() {
        return adminState;
    }

    public void setAdminState(Integer adminState) {
        this.adminState = adminState;
    }

    public Integer getOperationState() {
        return operationState;
    }

    public void setOperationState(Integer operationState) {
        this.operationState = operationState;
    }

    public List<OltPonAttribute> getPonlist() {
        return ponlist;
    }

    public void setPonlist(List<OltPonAttribute> ponlist) {
        this.ponlist = ponlist;
    }

    public EntityTypeService getEntityTypeService() {
        return entityTypeService;
    }

    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }

    public List<EntityType> getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(List<EntityType> entityTypes) {
        this.entityTypes = entityTypes;
    }

}
