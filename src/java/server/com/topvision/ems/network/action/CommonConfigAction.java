/***********************************************************************
 * $Id: CommonConfigAction.java,v1.0 2014年7月16日 下午2:13:34 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.action;

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

import com.topvision.ems.network.service.CommonConfigService;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author loyal
 * @created @2014年7月16日-下午2:13:34
 *
 */
@Controller("commonConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CommonConfigAction extends BaseAction {
    private static final long serialVersionUID = 1492650856637638194L;
    private Long sendCommandInterval;
    private Long pollInterval;
    private String textArea;
    private Long type;
    private Long folderId;
    private Boolean autoSendConfigSwitch;
    private Boolean failAutoSendConfigSwitch;// 失败后是否开启自动重新下发
    @Autowired
    private CommonConfigService commonConfigService;
    private final Logger logger = LoggerFactory.getLogger(CommonConfigAction.class);
    public static final String UTF8_BOM = "\uFEFF";

    public String showCommonConfigFileManage() {
        return SUCCESS;
    }

    public String showParamConfig() {
        sendCommandInterval = commonConfigService.getSendCommandInterval();
        pollInterval = commonConfigService.getPollInterval();
        autoSendConfigSwitch = commonConfigService.loadAutoSendConfigSwitch();
        failAutoSendConfigSwitch = commonConfigService.loadFailAutoSendConfigSwitch();
        return SUCCESS;
    }

    public String modifyParamConfig() {
        String message = "success";
        try {
            commonConfigService.modifySendCommandInterval(sendCommandInterval);
            commonConfigService.configAutoSendConfigSwitch(autoSendConfigSwitch);
            commonConfigService.configFailAutoSendConfigSwitch(failAutoSendConfigSwitch);
            commonConfigService.modifyPollInterval(pollInterval);
        } catch (Exception ex) {
            message = "fail";
            logger.debug("modifyParamConfig fail", ex);
        }
        writeDataToAjax(message);
        return NONE;
    }

    /**
    *
    * @return
    */
    public String saveCommonConfig() {
       commonConfigService.saveCommonConfig(textArea, type, folderId);
       return NONE;
   }

   /**
    *
    * @return
    */
    public String readCommonConfig() {
       String re = commonConfigService.readCommonConfig(type, folderId);
       writeDataToAjax(re);
       return NONE;
   }

   /**
    * 导入配置
     * 
    * @return
    */
    public String importCommonConfig() {
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
                                    in = new BufferedReader(new InputStreamReader(new FileInputStream(file1), "UTF-8"));
                                   String line = in.readLine();
                                   while (line != null) {
                                       if (firstLine) {
                                           line = removeUTF8BOM(line);
                                           firstLine = false;
                                        }
                                       configList.add(line);
                                       line = in.readLine();

                                   }
                                   commonConfigService.saveCommonConfig(configList, type, folderId);
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
       writeDataToAjax(JSONObject.fromObject(json));
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

    public Long getSendCommandInterval() {
        return sendCommandInterval;
    }

    public void setSendCommandInterval(Long sendCommandInterval) {
        this.sendCommandInterval = sendCommandInterval;
    }

    public Long getPollInterval() {
        return pollInterval;
    }

    public void setPollInterval(Long pollInterval) {
        this.pollInterval = pollInterval;
    }

    public String getTextArea() {
        return textArea;
    }

    public void setTextArea(String textArea) {
        this.textArea = textArea;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

	public Boolean getAutoSendConfigSwitch() {
		return autoSendConfigSwitch;
	}

	public void setAutoSendConfigSwitch(Boolean autoSendConfigSwitch) {
		this.autoSendConfigSwitch = autoSendConfigSwitch;
	}

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Boolean getFailAutoSendConfigSwitch() {
        return failAutoSendConfigSwitch;
    }

    public void setFailAutoSendConfigSwitch(Boolean failAutoSendConfigSwitch) {
        this.failAutoSendConfigSwitch = failAutoSendConfigSwitch;
    }

} 
