/***********************************************************************
 * $Id: CmcRealtimeAction.java,v1.0 2014年5月11日 上午9:28:20 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.service.CmcRealtimeInfoService;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.ems.cmc.upchannel.service.CmcUpChannelService;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.network.service.CommonConfigService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author loyal
 * @created @2014年5月11日-上午9:28:20
 *
 */
@Controller("cmcRealtimeAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcRealtimeAction extends BaseAction {
    private static final long serialVersionUID = 5978026326510686015L;
    private Long cmcId;
    private Long typeId;
    private Long entityId;
    private String textArea;
    private String channelIndexString;
    @Autowired
    private CmcRealtimeInfoService cmcRealtimeInfoService;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private CmcUpChannelService cmcUpChannelService;
    @Autowired
    private CommonConfigService commonConfigService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    private Logger logger = LoggerFactory.getLogger(CmcRealtimeAction.class);
    private boolean surportRealSnr = false;
    public static final String UTF8_BOM = "\uFEFF";
    private String channelIds = "";

    /**
     * 展示CC实时信息页面
     * @return
     */
    public String showCmcRealTimeData(){
        typeId = entityService.getEntity(cmcId).getTypeId();
        entityId = cmcService.getEntityIdByCmcId(cmcId);
        List<CmcUpChannelBaseShowInfo> upChannelList= cmcUpChannelService.getUpChannelBaseShowInfoList(cmcId);
        for(CmcUpChannelBaseShowInfo cmcUpChannelBaseShowInfo : upChannelList){
            channelIds += cmcUpChannelBaseShowInfo.getChannelId();
        }
        return SUCCESS;
    }
    
    /**
     * 获取CC实时信息
     * @return
     */
    public String getCmcRealTimeData(){
        Map<String,Object> re = cmcRealtimeInfoService.getCmcRealTimeData(cmcId);
        writeDataToAjax(JSONObject.fromObject(re));
        return NONE;
    }

    /**
     *
     * @return
     */
    public String saveCommonConfig(){
        commonConfigService.saveCommonConfig(textArea, entityTypeService.getCcmtsType());
        return NONE;
    }

    /**
     *
     * @return
     */
    public String readCommonConfig(){
        String re = commonConfigService.readCommonConfig(entityTypeService.getCcmtsType());
        writeDataToAjax(re);
        return NONE;
    }

    /**
     *
     * @return
     */
    public String sendCommonConfig(){
        String re = cmcRealtimeInfoService.sendCommonConfig(cmcId);
        writeDataToAjax(re);
        return NONE;
    }
    
    /**
     * 开启RemoteQuery
     * @return
     */
    public String openRemoteQuery(){
        boolean b = cmcRealtimeInfoService.openRemoteQuery(cmcId);
        writeDataToAjax("" + b);
        return NONE;
    }
    
    /**
     * 展示下发配置页面
     * @return
     */
    public String showDistributeConfig(){
        return SUCCESS;
    }
    
    /**
     * 导入配置
     * @return
     */
    public String importCommonConfig(){
        List<String> configList = new ArrayList<String>();
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();
            Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();
            while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
                String inputName = fileParameterNames.nextElement();
                String[] contentType = multiWrapper.getContentTypes(inputName);
                if (isNonEmpty(contentType)) {
                    String[] fileName = multiWrapper.getFileNames(inputName);
                    if (isNonEmpty(fileName)) {
                        File[] files = multiWrapper.getFiles(inputName);
                        if (files != null) {
                            for (File file1 : files) {
                                BufferedReader in = null;
                                try {
                                    boolean firstLine = true;
                                    in = new BufferedReader(new InputStreamReader(new FileInputStream(file1),"UTF-8"));
                                    String line = in.readLine();
                                    while (line != null) {
                                        if (firstLine) {
                                            line = removeUTF8BOM(line);
                                            firstLine = false;
                                        } 
                                        configList.add(line);
                                        line = in.readLine();
                                        
                                    }
                                    commonConfigService.saveCommonConfig(configList, entityTypeService.getCcmtsType());
                                    in.close();
                                } catch (Exception e) {
                                    in.close();
                                }
                            }
                        }
                    }
                }
            }
        json.put("success", true);
    } catch (Exception e) {
        logger.debug(e.getMessage(), e);
        json.put("success", false);
    }   
        json.put("configList", configList);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }
    
    public String getCmcSnr() {
        Map<String, String> map = new HashMap<String, String>();
        String[] channelIndexs = channelIndexString.split(",");
        String[] snrs = cmcRealtimeInfoService.getCmcSnr(entityId, channelIndexs);
        for (int i = 0; i < snrs.length; i++) {
            map.put("channel" + CmcIndexUtils.getChannelId(new Long(channelIndexs[i])).toString(), snrs[i]);
        }
        writeDataToAjax(JSONObject.fromObject(map));
        return NONE;
    }
    
    private static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }
    
    /**
     * 读取文件信息的时候判断数组是否有可用对象
     * 
     * @param objArray
     *            数组列表
     * @return String
     */
    private boolean isNonEmpty(Object[] objArray) {
        boolean result = false;
        for (int i = 0; i < objArray.length && !result; i++) {
            if (objArray[i] != null) {
                result = true;
            }
        }
        return result;
    }
    
    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getTextArea() {
        return textArea;
    }

    public void setTextArea(String textArea) {
        this.textArea = textArea;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getChannelIndexString() {
        return channelIndexString;
    }

    public void setChannelIndexString(String channelIndexString) {
        this.channelIndexString = channelIndexString;
    }

    public boolean isSurportRealSnr() {
        return surportRealSnr;
    }

    public void setSurportRealSnr(boolean surportRealSnr) {
        this.surportRealSnr = surportRealSnr;
    }

    public String getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(String channelIds) {
        this.channelIds = channelIds;
    }

}
