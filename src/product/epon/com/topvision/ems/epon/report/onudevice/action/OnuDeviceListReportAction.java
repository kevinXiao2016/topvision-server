/***********************************************************************
 * $Id: OnuDeviceListReportAction.java,v1.0 2013-10-28 下午4:43:43 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.onudevice.action;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.report.onudevice.service.OnuDeviceListReportCreator;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;

/**
 * @author haojie
 * @created @2013-10-28-下午4:43:43
 * 
 */
@Controller("onuDeviceListReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OnuDeviceListReportAction extends BaseAction {
    private static final long serialVersionUID = 4262623353113888688L;
    @Autowired
    private OnuDeviceListReportCreator onuDeviceListReportCreator;
    protected Date statDate;
    private String onuSortName = "onuType";
    private List<OltOnuAttribute> deviceListItems = null;
    private JSONArray deviceListItemArray;
    private Integer onuTotalNum;
    private boolean operationStatusDisplayable;
    private boolean adminStatusDisplayable;
    private boolean nameDisplayable = true;
    private boolean positionDisplayable = true;
    private boolean macDisplayable = true;
    private boolean typeDisplayable = true;

    /**
     * 显示ONU设备报表界面
     * 
     * @return
     */
    public String showOnuDeviceAsset() {
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sortName", onuSortName);
        deviceListItems = onuDeviceListReportCreator.getOnuDeviceListItem(map);
        deviceListItemArray = new JSONArray();
        for (Iterator<OltOnuAttribute> iterator = deviceListItems.iterator(); iterator.hasNext();) {
            OltOnuAttribute oltOnuAttr = iterator.next();
            String formatedMac = MacUtils.convertMacToDisplayFormat(oltOnuAttr.getOnuMac(), macRule);
            oltOnuAttr.setOnuMac(formatedMac);
            if (oltOnuAttr.getOnuAdminStatus() == 1) {
                oltOnuAttr.setOnuAdminStatusString("UP");
            } else if (oltOnuAttr.getOnuAdminStatus() == 2) {
                oltOnuAttr.setOnuAdminStatusString("DOWN");
            }
            if (oltOnuAttr.getOnuOperationStatus() == 1) {
                oltOnuAttr.setOnuOperationStatusString("UP");
            } else if (oltOnuAttr.getOnuOperationStatus() == 2) {
                oltOnuAttr.setOnuOperationStatusString("DOWN");
            }
            String s = Long.toHexString(oltOnuAttr.getOnuIndex());
            String lid = Integer.valueOf(s.substring(3, 5), 16).toString();
            String position = oltOnuAttr.getEntityIp() + ":" + oltOnuAttr.getPonName() + ":" + lid;
            oltOnuAttr.setPosition(position);
            if (oltOnuAttr.getOnuPreType() == null) {
                oltOnuAttr.setOnuTypeString(getString("ONU.unkownType", "epon"));
                continue;
            }
            switch (oltOnuAttr.getOnuPreType()) {
            case 33:
                oltOnuAttr.setOnuTypeString("TA-PN8621");
                break;
            case 34:
                oltOnuAttr.setOnuTypeString("TA-PN8622");
                break;
            case 36:
                oltOnuAttr.setOnuTypeString("TA-PN8624");
                break;
            case 37:
                oltOnuAttr.setOnuTypeString("TA-PN8625");
                break;
            case 65:
                oltOnuAttr.setOnuTypeString("TA-PN8641");
                break;
            case 68:
                oltOnuAttr.setOnuTypeString("TA-PN8643");
                break;
            case 71:
                oltOnuAttr.setOnuTypeString("TA-PN8645");
                break;
            case 81:
                oltOnuAttr.setOnuTypeString("TA-PN8651");
                break;
            case 82:
                oltOnuAttr.setOnuTypeString("TA-PN8652");
                break;
            case 83:
                oltOnuAttr.setOnuTypeString("TA-PN8653");
                break;
            case 84:
                oltOnuAttr.setOnuTypeString("TA-PN8654");
                break;
            case 241:
                iterator.remove();
                break;
            case 242:
                iterator.remove();
                break;
            case 243:
                iterator.remove();
                break;
            default:
                oltOnuAttr.setOnuTypeString(getString("ONU.unkownType", "epon"));
                break;
            }
            JSONObject onuJson = JSONObject.fromObject(oltOnuAttr);
            onuJson.put("onuMac", formatedMac);
            deviceListItemArray.add(onuJson);
        }
        onuTotalNum = deviceListItems.size();
        return SUCCESS;
    }

    protected String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * onuDeviceAssetReport.jsp 导出ONU设备清单到EXCEL
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String exportOnuAssetToExcel() throws UnsupportedEncodingException {
        statDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sortName", onuSortName);
        List<OltOnuAttribute> deviceListItems = onuDeviceListReportCreator.getOnuDeviceListItem(map);
        for (Iterator<OltOnuAttribute> iterator = deviceListItems.iterator(); iterator.hasNext();) {
            OltOnuAttribute oltOnuAttr = iterator.next();
            if (oltOnuAttr.getOnuAdminStatus() == 1) {
                oltOnuAttr.setOnuAdminStatusString("UP");
            } else if (oltOnuAttr.getOnuAdminStatus() == 2) {
                oltOnuAttr.setOnuAdminStatusString("DOWN");
            }
            if (oltOnuAttr.getOnuOperationStatus() == 1) {
                oltOnuAttr.setOnuOperationStatusString("UP");
            } else if (oltOnuAttr.getOnuOperationStatus() == 2) {
                oltOnuAttr.setOnuOperationStatusString("DOWN");
            }
            String s = Long.toHexString(oltOnuAttr.getOnuIndex());
            String lid = Integer.valueOf(s.substring(3, 5), 16).toString();
            String position = oltOnuAttr.getEntityIp() + ":" + oltOnuAttr.getPonName() + ":" + lid;
            oltOnuAttr.setPosition(position);
            switch (oltOnuAttr.getOnuPreType()) {
            case 33:
                oltOnuAttr.setOnuTypeString("TA-PN8621");
                break;
            case 34:
                oltOnuAttr.setOnuTypeString("TA-PN8622");
                break;
            case 36:
                oltOnuAttr.setOnuTypeString("TA-PN8624");
                break;
            case 37:
                oltOnuAttr.setOnuTypeString("TA-PN8625");
                break;
            case 65:
                oltOnuAttr.setOnuTypeString("TA-PN8641");
                break;
            case 68:
                oltOnuAttr.setOnuTypeString("TA-PN8643");
                break;
            case 71:
                oltOnuAttr.setOnuTypeString("TA-PN8645");
                break;
            case 81:
                oltOnuAttr.setOnuTypeString("TA-PN8651");
                break;
            case 82:
                oltOnuAttr.setOnuTypeString("TA-PN8652");
                break;
            case 83:
                oltOnuAttr.setOnuTypeString("TA-PN8653");
                break;
            case 84:
                oltOnuAttr.setOnuTypeString("TA-PN8654");
                break;
            case 241:
                iterator.remove();
                break;
            case 242:
                iterator.remove();
                break;
            case 243:
                iterator.remove();
                break;
            default:
                oltOnuAttr.setOnuTypeString(getString("ONU.unkownType", "epon"));
                break;
            }
        }
        Map<String, Boolean> columnDisable = new HashMap<String, Boolean>();
        columnDisable.put("operationStatusDisplayable", operationStatusDisplayable);
        columnDisable.put("adminStatusDisplayable", adminStatusDisplayable);
        onuDeviceListReportCreator.exportOnuDeviceListReportToExcel(deviceListItems, columnDisable, statDate);
        return NONE;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public String getOnuSortName() {
        return onuSortName;
    }

    public void setOnuSortName(String onuSortName) {
        this.onuSortName = onuSortName;
    }

    public List<OltOnuAttribute> getDeviceListItems() {
        return deviceListItems;
    }

    public void setDeviceListItems(List<OltOnuAttribute> deviceListItems) {
        this.deviceListItems = deviceListItems;
    }

    public Integer getOnuTotalNum() {
        return onuTotalNum;
    }

    public void setOnuTotalNum(Integer onuTotalNum) {
        this.onuTotalNum = onuTotalNum;
    }

    public boolean isOperationStatusDisplayable() {
        return operationStatusDisplayable;
    }

    public void setOperationStatusDisplayable(boolean operationStatusDisplayable) {
        this.operationStatusDisplayable = operationStatusDisplayable;
    }

    public boolean isAdminStatusDisplayable() {
        return adminStatusDisplayable;
    }

    public void setAdminStatusDisplayable(boolean adminStatusDisplayable) {
        this.adminStatusDisplayable = adminStatusDisplayable;
    }

    public boolean isNameDisplayable() {
        return nameDisplayable;
    }

    public void setNameDisplayable(boolean nameDisplayable) {
        this.nameDisplayable = nameDisplayable;
    }

    public boolean isPositionDisplayable() {
        return positionDisplayable;
    }

    public void setPositionDisplayable(boolean positionDisplayable) {
        this.positionDisplayable = positionDisplayable;
    }

    public boolean isMacDisplayable() {
        return macDisplayable;
    }

    public void setMacDisplayable(boolean macDisplayable) {
        this.macDisplayable = macDisplayable;
    }

    public boolean isTypeDisplayable() {
        return typeDisplayable;
    }

    public void setTypeDisplayable(boolean typeDisplayable) {
        this.typeDisplayable = typeDisplayable;
    }

    public JSONArray getDeviceListItemArray() {
        return deviceListItemArray;
    }

    public void setDeviceListItemArray(JSONArray deviceListItemArray) {
        this.deviceListItemArray = deviceListItemArray;
    }

}
