/***********************************************************************
 * $Id: CcmtsDeviceListReportAction.java,v1.0 2013-10-29 上午9:23:47 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtsdevicelist.action;

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
import com.topvision.ems.cmc.report.ccmtsdevicelist.service.CcmtsDeviceListReportCreator;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;

/**
 * @author haojie
 * @created @2013-10-29-上午9:23:47
 * 
 */
@Controller("ccmtsDeviceListReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CcmtsDeviceListReportAction extends BaseAction {
    private static final long serialVersionUID = -1157661174714645381L;
    @Autowired
    private CcmtsDeviceListReportCreator ccmtsDeviceListReportCreator;
    private List<CmcAttribute> deviceListItems = null;
    private String ccSortName = "cmcDeviceStyle";
    private Date statDate;
    private boolean nameDisplayable = true;
    private boolean ipDisplayable = true;
    private boolean typeDisplayable = true;
    private boolean macDisplayable = true;
    private boolean folderDisplayable = true;
    private boolean locationDisplayable;
    private boolean dutyDisplayable;
    private boolean createTimeDisplayable;

    /**
     * 显示C-CMTS设备报表界面
     * 
     * @return String
     */
    public String showCcmtsDeviceAsset() {
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sortName", ccSortName);
        /**
         * Modified By huangdongsheng 2013-3-6 删除单独查询8800A的管理IP的代码 通过数据库统一查询8800A/B的IP地址
         */
        deviceListItems = ccmtsDeviceListReportCreator.getDeviceListItem(map);
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        for (CmcAttribute attribute : deviceListItems) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(attribute.getTopCcmtsSysMacAddr(), displayRule);
            if (formatedMac == null) {
                attribute.setTopCcmtsSysMacAddr("-");
            } else {
                attribute.setTopCcmtsSysMacAddr(formatedMac);
            }
            //地域名称国际化
            attribute.setFolderName(ReportTaskUtil.getString(attribute.getFolderName(), "resources"));
        }
        return SUCCESS;
    }

    /**
     * ccDeviceAssetReport.jsp 导出CCMTS设备清单到EXCEL
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String exportCcmtsAssetToExcel() throws UnsupportedEncodingException {
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sortName", ccSortName);
        List<CmcAttribute> deviceListItems = ccmtsDeviceListReportCreator.getDeviceListItem(map);
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
        ccmtsDeviceListReportCreator.exportCcmtsDeviceListReportToExcel(deviceListItems, columnDisable, statDate);
        return NONE;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public String getCcSortName() {
        return ccSortName;
    }

    public void setCcSortName(String ccSortName) {
        this.ccSortName = ccSortName;
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

    public List<CmcAttribute> getDeviceListItems() {
        return deviceListItems;
    }

    public void setDeviceListItems(List<CmcAttribute> deviceListItems) {
        this.deviceListItems = deviceListItems;
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

    public boolean isMacDisplayable() {
        return macDisplayable;
    }

    public void setMacDisplayable(boolean macDisplayable) {
        this.macDisplayable = macDisplayable;
    }

    public boolean isCreateTimeDisplayable() {
        return createTimeDisplayable;
    }

    public void setCreateTimeDisplayable(boolean createTimeDisplayable) {
        this.createTimeDisplayable = createTimeDisplayable;
    }

    public boolean isFolderDisplayable() {
        return folderDisplayable;
    }

    public void setFolderDisplayable(boolean folderDisplayable) {
        this.folderDisplayable = folderDisplayable;
    }
 
}
