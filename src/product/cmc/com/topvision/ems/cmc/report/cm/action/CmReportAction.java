/***********************************************************************
 * $Id: CmReportAction.java,v1.0 2013-10-29 下午3:59:21 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cm.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.report.cm.service.CmReportCreator;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;

/**
 * @author haojie
 * @created @2013-10-29-下午3:59:21
 * 
 */
@Controller("cmReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmReportAction extends BaseAction {
    private static final long serialVersionUID = -966973853387689760L;
    @Autowired
    private CmReportCreator cmReportCreator;
    private Date statDate;
    private String cmSortName = "statusValue";
    private List<CmAttribute> cmAttributes;
    private boolean ipDisable = true;
    private boolean macDisable = true;
    private boolean statusDisable = true;
    private boolean ccDisable = true;
    private boolean cmAliasDisable;
    private boolean cmClassifiedDisable;
    private JSONObject rangeList;
    private String range;
    private String rangeDetail;

    /**
     * cmDeviceAssetReprot.jsp 显示CM设备报表界面
     * 
     * @return String
     */
    public String showCmDeviceAsset() {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sortName", cmSortName);
        if ("folders".equals(range) && rangeDetail != "") {
            map.put("folderId", rangeDetail);
        } else if ("olts".equals(range) && rangeDetail != "") {
            map.put("oltId", rangeDetail);
        } else if ("cmcs".equals(range) && rangeDetail != "") {
            map.put("cmcId", rangeDetail);
        } else if ("cmts".equals(range) && rangeDetail != "") {
            map.put("cmtsId", rangeDetail);
        }
        map.put("start", 0);
        map.put("limit", 300);
        cmAttributes = cmReportCreator.statCmReport(map);
        String macRule = uc.getMacDisplayStyle();
        for (CmAttribute cmAttribute : cmAttributes) {
            if (cmAttribute.isCmOnline()) {
                cmAttribute.setStatusVlaueString("online");
            } else {
                cmAttribute.setStatusVlaueString("offline");
            }
            String formatedMac = MacUtils.convertMacToDisplayFormat(cmAttribute.getStatusMacAddress(), macRule);
            cmAttribute.setStatusMacAddress(formatedMac);
        }
        rangeList = cmReportCreator.loadFolderOltCmcLists();
        return SUCCESS;
    }

    public String loadRangeSelect() throws IOException {
        rangeList = cmReportCreator.loadFolderOltCmcLists();
        writeDataToAjax(rangeList);
        return NONE;
    }

    /**
     * cmDeviceAssetReport.jsp 导出CM设备清单到excel
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String exportCmDeviceReportToExcel() throws UnsupportedEncodingException {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        statDate = new Date();
        Map<String, Object> parMap = new HashMap<String, Object>();
        parMap.put("sortName", cmSortName);
        if ("folders".equals(range) && rangeDetail != "") {
            parMap.put("folderId", rangeDetail);
        } else if ("olts".equals(range) && rangeDetail != "") {
            parMap.put("oltId", rangeDetail);
        } else if ("cmcs".equals(range) && rangeDetail != "") {
            parMap.put("cmcId", rangeDetail);
        } else if ("cmts".equals(range) && rangeDetail != "") {
            parMap.put("cmtsId", rangeDetail);
        }
        cmAttributes = cmReportCreator.statCmReport(parMap);
        for (CmAttribute cmAttribute : cmAttributes) {
            if (cmAttribute.isCmOnline()) {
                cmAttribute.setStatusVlaueString("online");
            } else {
                cmAttribute.setStatusVlaueString("offline");
            }
            String formatedMac = MacUtils.convertMacToDisplayFormat(cmAttribute.getStatusMacAddress(), macRule);
            cmAttribute.setStatusMacAddress(formatedMac);
        }
        Map<String, Boolean> columnDisable = new HashMap<String, Boolean>();
        columnDisable.put("cmAliasDisable", cmAliasDisable);
        columnDisable.put("cmClassifiedDisable", cmClassifiedDisable);
        cmReportCreator.exportCmReportToExcel(cmAttributes, columnDisable, statDate);
        return NONE;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public String getCmSortName() {
        return cmSortName;
    }

    public void setCmSortName(String cmSortName) {
        this.cmSortName = cmSortName;
    }

    public List<CmAttribute> getCmAttributes() {
        return cmAttributes;
    }

    public void setCmAttributes(List<CmAttribute> cmAttributes) {
        this.cmAttributes = cmAttributes;
    }

    public boolean isCmAliasDisable() {
        return cmAliasDisable;
    }

    public void setCmAliasDisable(boolean cmAliasDisable) {
        this.cmAliasDisable = cmAliasDisable;
    }

    public boolean isCmClassifiedDisable() {
        return cmClassifiedDisable;
    }

    public void setCmClassifiedDisable(boolean cmClassifiedDisable) {
        this.cmClassifiedDisable = cmClassifiedDisable;
    }

    public boolean isIpDisable() {
        return ipDisable;
    }

    public void setIpDisable(boolean ipDisable) {
        this.ipDisable = ipDisable;
    }

    public boolean isMacDisable() {
        return macDisable;
    }

    public void setMacDisable(boolean macDisable) {
        this.macDisable = macDisable;
    }

    public boolean isStatusDisable() {
        return statusDisable;
    }

    public void setStatusDisable(boolean statusDisable) {
        this.statusDisable = statusDisable;
    }

    public boolean isCcDisable() {
        return ccDisable;
    }

    public void setCcDisable(boolean ccDisable) {
        this.ccDisable = ccDisable;
    }

    public JSONObject getRangeList() {
        return rangeList;
    }

    public void setRangeList(JSONObject rangeList) {
        this.rangeList = rangeList;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getRangeDetail() {
        return rangeDetail;
    }

    public void setRangeDetail(String rangeDetail) {
        this.rangeDetail = rangeDetail;
    }

}
