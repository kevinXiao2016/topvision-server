/***********************************************************************
 * $Id: CmtsDeviceListReportAction.java,v1.0 2013-11-18 下午1:44:39 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.report.cmtsdevicelist.action;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmts.report.cmtsdevicelist.service.CmtsDeviceListReportCreator;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;

/**
 * @author haojie
 * @created @2013-11-18-下午1:44:39
 * 
 */
@Controller("cmtsDeviceListReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmtsDeviceListReportAction extends BaseAction {
    private static final long serialVersionUID = 6642018355041674460L;
    @Autowired
    private CmtsDeviceListReportCreator cmtsDeviceListReportCreator;
    private List<CmcAttribute> deviceListItems;
    private String cmtsSortName = "cmcDeviceStyle";
    private Date statDate;
    private boolean nameDisplayable = true;
    private boolean ipDisplayable = true;
    private boolean typeDisplayable = true;
    private boolean locationDisplayable;
    private boolean dutyDisplayable;
    private boolean createTimeDisplayable;

    /**
     * 显示CMTS设备报表界面
     * 
     * @return String
     */
    public String showCmtsDeviceAsset() {
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sortName", cmtsSortName);
        deviceListItems = cmtsDeviceListReportCreator.getDeviceListItem(map);
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        for (CmcAttribute attribute : deviceListItems) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(attribute.getTopCcmtsSysMacAddr(), displayRule);
            attribute.setTopCcmtsSysMacAddr(formatedMac);
        }
        return SUCCESS;
    }

    /**
     * 导出CMTS设备清单到EXCEL
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String exportCmtsAssetToExcel() throws UnsupportedEncodingException {
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sortName", cmtsSortName);
        List<CmcAttribute> deviceListItems = cmtsDeviceListReportCreator.getDeviceListItem(map);
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        for (CmcAttribute attribute : deviceListItems) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(attribute.getTopCcmtsSysMacAddr(), displayRule);
            attribute.setTopCcmtsSysMacAddr(formatedMac);
        }
        Map<String, Boolean> columnDisable = new HashMap<String, Boolean>();
        columnDisable.put("locationDisplayable", locationDisplayable);
        columnDisable.put("dutyDisplayable", dutyDisplayable);
        columnDisable.put("createTimeDisplayable", createTimeDisplayable);
        cmtsDeviceListReportCreator.exportCmtsDeviceListReportToExcel(deviceListItems, columnDisable, statDate);
        return NONE;
    }

    public List<CmcAttribute> getDeviceListItems() {
        return deviceListItems;
    }

    public void setDeviceListItems(List<CmcAttribute> deviceListItems) {
        this.deviceListItems = deviceListItems;
    }

    public String getCmtsSortName() {
        return cmtsSortName;
    }

    public void setCmtsSortName(String cmtsSortName) {
        this.cmtsSortName = cmtsSortName;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public boolean isNameDisplayable() {
        return nameDisplayable;
    }

    public void setNameDisplayable(boolean nameDisplayable) {
        this.nameDisplayable = nameDisplayable;
    }

    public boolean isIpDisplayable() {
        return ipDisplayable;
    }

    public void setIpDisplayable(boolean ipDisplayable) {
        this.ipDisplayable = ipDisplayable;
    }

    public boolean isTypeDisplayable() {
        return typeDisplayable;
    }

    public void setTypeDisplayable(boolean typeDisplayable) {
        this.typeDisplayable = typeDisplayable;
    }

    public boolean isLocationDisplayable() {
        return locationDisplayable;
    }

    public void setLocationDisplayable(boolean locationDisplayable) {
        this.locationDisplayable = locationDisplayable;
    }

    public boolean isDutyDisplayable() {
        return dutyDisplayable;
    }

    public void setDutyDisplayable(boolean dutyDisplayable) {
        this.dutyDisplayable = dutyDisplayable;
    }

    public boolean isCreateTimeDisplayable() {
        return createTimeDisplayable;
    }

    public void setCreateTimeDisplayable(boolean createTimeDisplayable) {
        this.createTimeDisplayable = createTimeDisplayable;
    }

}
