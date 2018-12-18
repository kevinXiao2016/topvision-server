/***********************************************************************
 * $Id: CcmtsChannelListReportAction.java,v1.0 2013-10-29 上午8:45:39 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtschannellist.action;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.report.ccmtschannellist.service.CcmtsChannelListReportCreator;
import com.topvision.ems.cmc.report.domain.CcmtsChannelReportCCMTS;
import com.topvision.ems.cmc.report.domain.CcmtsChannelReportLocation;
import com.topvision.ems.cmc.report.domain.CcmtsChannelReportOLT;
import com.topvision.ems.cmc.report.domain.CcmtsChannelReportPON;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-29-上午8:45:39
 * 
 */
@Controller("ccmtsChannelListReportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CcmtsChannelListReportAction extends BaseAction {
    private static final long serialVersionUID = -3051355597280589548L;
    @Autowired
    private CcmtsChannelListReportCreator ccmtsChannelListReportCreator;
    private Date statDate;

    /**
     * ccDeviceAssetReport.jsp 查询CCMTS设备信道使用情况,页面展示
     * 
     * @author YangYi add
     * @created @2013-9-11
     * @return
     */
    public String showCcmtsChannelAsset() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("userId", CurrentRequest.getCurrentUser().getUserId());
        List<CcmtsChannelReportLocation> locations = ccmtsChannelListReportCreator.getCcmtsChannelUsageReport(queryMap);
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        for (CcmtsChannelReportLocation location : locations) {
            if (location == null) {
                continue;
            }
            for (CcmtsChannelReportOLT channelReportOLT : location.getCcmtsChannelReportOLT()) {
                if (channelReportOLT == null) {
                    continue;
                }
                for (CcmtsChannelReportPON channelReportPON : channelReportOLT.getCcmtsChannelReportPON()) {
                    if (channelReportPON == null) {
                        continue;
                    }
                    for (CcmtsChannelReportCCMTS channelReportCCMTS : channelReportPON.getCcmtsChannelReportCCMTS()) {
                        if (channelReportCCMTS == null) {
                            continue;
                        }
                        String formatedMac = MacUtils.convertMacToDisplayFormat(channelReportCCMTS.getCcmtsMAC(),
                                displayRule);
                        channelReportCCMTS.setCcmtsMAC(formatedMac);
                    }
                }
            }
        }
        request.setAttribute("locations", locations);
        return SUCCESS;
    }

    /**
     * ccDeviceAssetReport.jsp 查询CCMTS设备信道使用情况，导出到EXCEL
     * 
     * @author YangYi add
     * @created @2013-9-12
     * @return
     */
    public String exprotCcmtsChannelReportToExcel() throws UnsupportedEncodingException {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("userId", CurrentRequest.getCurrentUser().getUserId());
        List<CcmtsChannelReportLocation> locations = ccmtsChannelListReportCreator.getCcmtsChannelUsageReport(queryMap);
        //add by fanzidong,需要在展示前格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String displayRule = uc.getMacDisplayStyle();
        for (CcmtsChannelReportLocation location : locations) {
            if (location == null) {
                continue;
            }
            for (CcmtsChannelReportOLT channelReportOLT : location.getCcmtsChannelReportOLT()) {
                if (channelReportOLT == null) {
                    continue;
                }
                for (CcmtsChannelReportPON channelReportPON : channelReportOLT.getCcmtsChannelReportPON()) {
                    if (channelReportPON == null) {
                        continue;
                    }
                    for (CcmtsChannelReportCCMTS channelReportCCMTS : channelReportPON.getCcmtsChannelReportCCMTS()) {
                        if (channelReportCCMTS == null) {
                            continue;
                        }
                        String formatedMac = MacUtils.convertMacToDisplayFormat(channelReportCCMTS.getCcmtsMAC(),
                                displayRule);
                        channelReportCCMTS.setCcmtsMAC(formatedMac);
                    }
                }
            }
        }
        statDate = new Date();
        ccmtsChannelListReportCreator.exportCcmtsDeviceListReportToExcel(locations, statDate);
        return NONE;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

}
