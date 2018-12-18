/***********************************************************************
 * $Id: OltSniPortReportAction.java,v1.0 2013-10-28 下午1:57:28 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltsniport.action;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.report.oltsniport.service.OltSniPortReportCreator;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author haojie
 * @created @2013-10-28-下午1:57:28
 * 
 */
@Controller("oltSniPortReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltSniPortReportAction extends BaseAction {
    private static final long serialVersionUID = -1055999666276382606L;
    @Autowired
    private OltSniPortReportCreator oltSniPortReportCreator;
    protected Date statDate;
    private Integer entityType;
    private String entityIp;
    private Integer adminState;
    private Integer operationState;
    private List<OltSniAttribute> snilist = null;
    @Autowired
    private EntityTypeService entityTypeService;
    private List<EntityType> entityTypes;

    /**
     * 查看SNI端口统计页面
     * 
     * @return String
     */
    public String showEponSniPortReport() {
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        if (entityType != null && entityType != 0) {
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
        snilist = oltSniPortReportCreator.getSniPortList(map);
        for (OltSniAttribute aSnilist : snilist) {
            if (aSnilist.getSniAdminStatus() == 1) {
                aSnilist.setSniAdminStatusString("UP");
            } else if (aSnilist.getSniAdminStatus() == 2) {
                aSnilist.setSniAdminStatusString("DOWN");
            }

            if (aSnilist.getSniOperationStatus() == 1) {
                aSnilist.setSniOperationStatusString("UP");
            } else if (aSnilist.getSniOperationStatus() == 2) {
                aSnilist.setSniOperationStatusString("DOWN");
            }

            switch (aSnilist.getSniMediaType()) {
            case 1:
                aSnilist.setSniMediaTypeString("twistedPair");
                break;
            case 2:
                aSnilist.setSniMediaTypeString("fiber");
                break;
            default:
                break;
            }
            aSnilist.setSniPort(EponIndex.getSlotNo(aSnilist.getSniIndex()) + "-"
                    + EponIndex.getSniNo(aSnilist.getSniIndex()));
        }

        Long oltType = entityTypeService.getOltType();
        entityTypes = entityTypeService.loadSubType(oltType);
        return SUCCESS;
    }

    /**
     * eponDeviceSniPortReport.jsp 导出SNI端口使用情况清单到EXCEL
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String exportDeviceSniToExcel() throws UnsupportedEncodingException {
        statDate = new Date();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (entityType != null && entityType != 0) {
            queryMap.put("entityType", entityType);
        }
        if (entityIp != null && !entityIp.equals("")) {
            queryMap.put("entityIp", entityIp);
        }
        if (adminState != null && adminState != 0) {
            queryMap.put("adminState", adminState);
        }
        if (operationState != null && operationState != 0) {
            queryMap.put("operationState", operationState);
        }
        List<OltSniAttribute> snilist = oltSniPortReportCreator.getSniPortList(queryMap);
        for (OltSniAttribute aSnilist : snilist) {
            if (aSnilist.getSniAdminStatus() == 1) {
                aSnilist.setSniAdminStatusString("UP");
            } else if (aSnilist.getSniAdminStatus() == 2) {
                aSnilist.setSniAdminStatusString("DOWN");
            }
            if (aSnilist.getSniOperationStatus() == 1) {
                aSnilist.setSniOperationStatusString("UP");
            } else if (aSnilist.getSniOperationStatus() == 2) {
                aSnilist.setSniOperationStatusString("DOWN");
            }
            switch (aSnilist.getSniMediaType()) {
            case 1:
                aSnilist.setSniMediaTypeString("twistedPair");
                break;
            case 2:
                aSnilist.setSniMediaTypeString("fiber");
                break;
            default:
                break;
            }
            aSnilist.setSniPort(EponIndex.getSlotNo(aSnilist.getSniIndex()) + "-"
                    + EponIndex.getSniNo(aSnilist.getSniIndex()));
        }
        oltSniPortReportCreator.exportOltSniPortReportToExcel(snilist, statDate);
        return NONE;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
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

    public List<OltSniAttribute> getSnilist() {
        return snilist;
    }

    public void setSnilist(List<OltSniAttribute> snilist) {
        this.snilist = snilist;
    }

    public List<EntityType> getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(List<EntityType> entityTypes) {
        this.entityTypes = entityTypes;
    }

}
