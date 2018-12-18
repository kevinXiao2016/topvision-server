/***********************************************************************
 * $Id: OltDeviceListReportAction.java,v1.0 2013-10-26 下午2:49:33 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltdevicelist.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.domain.DeviceListItem;
import com.topvision.ems.epon.report.oltdevicelist.service.OltDeviceListReportCreator;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author haojie
 * @created @2013-10-26-下午2:49:33
 * 
 */
@Controller("oltDeviceListReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltDeviceListReportAction extends BaseAction {
    private static final long serialVersionUID = 4696256863790553829L;
    private static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    protected Date statDate;
    private String oltSortName = "typeName";
    private List<DeviceListItem> deviceListItems = null;
    @Autowired
    private OltDeviceListReportCreator oltDeviceListReportCreator;
    private boolean nameDisplayable = true;
    private boolean ipDisplayable = true;
    private boolean typeDisplayable = true;
    private boolean locationDisplayable;
    private boolean createTimeDisplayable;

    /**
     * 显示epon设备报表界面
     * 
     * @return String
     */
    public String showEponDeviceAsset() {
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sortName", oltSortName);
        deviceListItems = oltDeviceListReportCreator.getDeviceListItem(map);
        return SUCCESS;
    }

    /**
     * eponDeviceAssetReport.jsp 导出OLT设备清单到EXCEL
     * 
     * @return
     * @throws IOException
     */
    public String exportEponDeviceAssetToExcel() throws IOException {
        statDate = new Date();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("sortName", oltSortName);
        queryMap.put("locationDisplayable", locationDisplayable);
        queryMap.put("createTimeDisplayable", createTimeDisplayable);
        OutputStream out = response.getOutputStream();
        ServletActionContext.getResponse().setContentType("application/msexcel");
        String fileName = URLEncoder.encode(ReportTaskUtil.getString("report.oltDeviceList", "report") + "-"
                + fileNameFormatter.format(statDate) + ".xls", "UTF-8");
        ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
        oltDeviceListReportCreator.exportAsExcelFromRequest(queryMap, statDate, out);
        return NONE;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public String getOltSortName() {
        return oltSortName;
    }

    public void setOltSortName(String oltSortName) {
        this.oltSortName = oltSortName;
    }

    public List<DeviceListItem> getDeviceListItems() {
        return deviceListItems;
    }

    public void setDeviceListItems(List<DeviceListItem> deviceListItems) {
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

    public boolean isLocationDisplayable() {
        return locationDisplayable;
    }

    public void setLocationDisplayable(boolean locationDisplayable) {
        this.locationDisplayable = locationDisplayable;
    }

    public boolean isCreateTimeDisplayable() {
        return createTimeDisplayable;
    }

    public void setCreateTimeDisplayable(boolean createTimeDisplayable) {
        this.createTimeDisplayable = createTimeDisplayable;
    }

}
