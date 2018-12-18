/***********************************************************************
 * $Id: TelnetLoginAction.java,v1.0 2014年7月16日 上午9:05:43 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.network.service.TelnetLoginService;
import com.topvision.framework.common.ExcelUtil;
import com.topvision.framework.common.IpUtils;
import com.topvision.platform.SystemConstants;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author loyal
 * @created @2014年7月16日-上午9:05:43
 * 
 */
@Controller("telnetLoginAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TelnetLoginAction extends BaseAction {
    private static final long serialVersionUID = 7026884098321500140L;
    private int start;
    private int limit;
    private Long ip;
    private String userName;
    private String password;
    private String enablePassword;
    private Boolean isAAA;
    private String ipString;
    private TelnetLogin telnetLogin = new TelnetLogin();
    private Logger logger = LoggerFactory.getLogger(TelnetLoginAction.class);
    @Autowired
    private TelnetLoginService telnetLoginService;
    private boolean overwrite;

    public String getTelnetLoginConfig() {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", start);
        map.put("limit", limit);
        map.put("ipString", ipString);
        List<TelnetLogin> telnetLogins = telnetLoginService.getTelnetLoginConfig(map);
        json.put("data", telnetLogins);
        json.put("rowCount", telnetLoginService.getTelnetLoginConfigCount(map));
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String getTelnetLoginConfigByIp() {
        telnetLogin = telnetLoginService.getTelnetLoginConfigByIp(new IpUtils(ipString).longValue());
        writeDataToAjax(JSONObject.fromObject(telnetLogin));
        return NONE;
    }

    public String getGlobalTelnetLogin() {
        telnetLogin = telnetLoginService.getGlobalTelnetLogin();
        writeDataToAjax(JSONObject.fromObject(telnetLogin));
        return NONE;
    }

    public String showAddTelnetLogin() {
        return SUCCESS;
    }

    public String addTelnetLogin() {
        TelnetLogin telnetLogin = new TelnetLogin();
        Map<String, Object> json = new HashMap<String, Object>();
        if (telnetLoginService.getTelnetLoginConfigByIp(new IpUtils(ipString).longValue()) != null) {
            json.put("isExist", true);
        } else {
            telnetLogin.setIpString(ipString);
            telnetLogin.setIp(new IpUtils(ipString).longValue());
            telnetLogin.setUserName(userName);
            telnetLogin.setPassword(password);
            telnetLogin.setEnablePassword(enablePassword);
            telnetLogin.setIsAAA(isAAA);
            telnetLoginService.addTelnetLogin(telnetLogin);
            json.put("isExist", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String deleteTelnetLogin() {
        telnetLoginService.deleteTelnetLogin(ip);
        return NONE;
    }

    public String modifyTelnetLogin() {
        TelnetLogin telnetLogin = new TelnetLogin();
        telnetLogin.setIp(ip);
        telnetLogin.setUserName(userName);
        telnetLogin.setPassword(password);
        telnetLogin.setEnablePassword(enablePassword);
        telnetLogin.setIsAAA(isAAA);
        telnetLoginService.modifyTelnetLogin(telnetLogin);
        return NONE;
    }

    public String showModifyGlobalTelnetLogin() {
        TelnetLogin telnetLogin = telnetLoginService.getGlobalTelnetLogin();
        userName = telnetLogin.getUserName();
        password = telnetLogin.getPassword();
        enablePassword = telnetLogin.getEnablePassword();
        isAAA = telnetLogin.getIsAAA();
        return SUCCESS;
    }

    public String modifyGlobalTelnetLogin() {
        TelnetLogin telnetLogin = new TelnetLogin();
        telnetLogin.setUserName(userName);
        telnetLogin.setPassword(password);
        telnetLogin.setEnablePassword(enablePassword);
        telnetLogin.setIsAAA(isAAA);
        telnetLoginService.modifyGlobalTelnetLogin(telnetLogin);
        return NONE;
    }

    public String showModifyTelnetLogin() {
        telnetLogin = telnetLoginService.getTelnetLoginConfigByIp(ip);
        ipString = telnetLogin.getIpString();
        userName = telnetLogin.getUserName();
        password = telnetLogin.getPassword();
        enablePassword = telnetLogin.getEnablePassword();
        isAAA = telnetLogin.getIsAAA();
        return SUCCESS;
    }

    public String showTelnetLoginList() {
        telnetLogin = telnetLoginService.getGlobalTelnetLogin();
        userName = telnetLogin.getUserName();
        password = telnetLogin.getPassword();
        enablePassword = telnetLogin.getEnablePassword();
        isAAA = telnetLogin.getIsAAA();
        return SUCCESS;
    }

    public String exportToExcel() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (ipString != null && !"".equals(ipString.trim())) {
            map.put("ipString", ipString);
        }
        map.put("start", start);
        map.put("limit", Integer.MAX_VALUE);
        List<TelnetLogin> telnetLogins = telnetLoginService.getTelnetLoginConfig(map);
        telnetLoginService.exportToExcel(telnetLogins);
        return NONE;
    }

    public String showTelnetLoginImport() {
        return SUCCESS;
    }

    public String downLoadTemplate() throws UnsupportedEncodingException {
        String tmpFile = "telnetLoginTemplate.xlsx";
        StringBuilder fileName = new StringBuilder(SystemConstants.ROOT_REAL_PATH);
        fileName.append("META-INF/");
        fileName.append(tmpFile);
        int i;
        byte[] b = new byte[1024];
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/x-download");
        ServletActionContext.getResponse().addHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(tmpFile, "UTF-8"));
        try {
            File rFile = new File(fileName.toString());
            if (rFile != null) {
                fis = new FileInputStream(rFile);
                out = ServletActionContext.getResponse().getOutputStream();
                while ((i = fis.read(b)) > 0) {
                    out.write(b, 0, i);
                }
            }
        } catch (Exception e) {
            logger.debug("downLoadFileTemplate is error:{}", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (out != null) {
                    out.flush();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.debug("downLoadFileTemplate is error:{}", e);
            }
        }
        return NONE;
    }

    public String importTelnetLogin() {
        List<TelnetLogin> telnetLoginList = new ArrayList<TelnetLogin>();
        String[][] excelData = null;
        Map<String, Object> json = new HashMap<String, Object>();
        // 读取excel文件
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
                                try {
                                    excelData = ExcelUtil.getExcelDataFor2007(file1).get(0);
                                    break;
                                } catch (Exception e) {
                                    try {
                                        String fileNameStr = file1.getPath();
                                        excelData = ExcelUtil.getExcelDataFor2003(fileNameStr).get(0);
                                        break;
                                    } catch (Exception e1) {
                                        logger.error("", e);
                                        json.put("message", "FileWrong");
                                        writeDataToAjax(JSONObject.fromObject(json));
                                        return NONE;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // 判断文件数据是否为空
            if (excelData == null) {
                json.put("message", "FileWrong");
                writeDataToAjax(JSONObject.fromObject(json));
                return NONE;
            } else {
                // 确定列参数
                String[] titleRow = excelData[0];
                int ipColumnNum = -1;
                int userNameColumnNum = -1;
                int passWordColumnNum = -1;
                int enablePassWordColumnNum = -1;
                int isAAAColumnNum = -1;
                for (int i = 0; i < titleRow.length; i++) {
                    if (titleRow[i] != null && "IP".equals(titleRow[i])) {
                        ipColumnNum = i;
                    }
                    if (titleRow[i] != null && "UserName".equals(titleRow[i])) {
                        userNameColumnNum = i;
                    }
                    if (titleRow[i] != null && "Password".equals(titleRow[i])) {
                        passWordColumnNum = i;
                    }
                    if (titleRow[i] != null && "EnablePassword".equals(titleRow[i])) {
                        enablePassWordColumnNum = i;
                    }
                    if (titleRow[i] != null && "isAAA".equals(titleRow[i])) {
                        isAAAColumnNum = i;
                    }
                }

                if (ipColumnNum < 0 || (userNameColumnNum < 0 && passWordColumnNum < 0 && enablePassWordColumnNum < 0 && isAAAColumnNum < 0)) {
                    json.put("message", "FileWrong");
                    writeDataToAjax(JSONObject.fromObject(json));
                    return NONE;
                }
                for (int i = 1; i < excelData.length; i++) {
                    if (excelData[i] == null) {
                        continue;
                    }
                    //判断一行是否都为空
                    int size = excelData[i].length;
                    int nullNum = 0;
                    for (int s = 0; s < size; s++) {
                        if (excelData[i][s] != null && !"".equals(excelData[i][s].trim())) {
                            continue;
                        } else {
                            nullNum += 1;
                        }
                    }
                    if (nullNum == size) {
                        continue;
                    }
                    TelnetLogin telnetLogin = new TelnetLogin();
                    if (ipColumnNum >= 0) {
                        String ipString = "";
                        if (excelData[i].length > ipColumnNum && excelData[i][ipColumnNum] != null) {
                            ipString = excelData[i][ipColumnNum].trim();
                        }
                        if ("".equals(ipString)) {
                            json.put("message", "ValueWrong");
                            json.put("colunmn", i + 1);
                            writeDataToAjax(JSONObject.fromObject(json));
                            return NONE;
                        }
                        telnetLogin.setIpString(ipString);
                        if (ipString != "") {
                            try {
                                telnetLogin.setIp(new IpUtils(ipString).longValue());
                            } catch (Exception ex) {
                                json.put("message", "ValueWrong");
                                json.put("colunmn", i + 1);
                                writeDataToAjax(JSONObject.fromObject(json));
                                return NONE;
                            }
                        }
                    }
                    if (userNameColumnNum >= 0) {
                        String userName = "";
                        if (excelData[i].length > userNameColumnNum && excelData[i][userNameColumnNum] != null) {
                            userName = excelData[i][userNameColumnNum].trim();
                        }
                        if ("".equals(userName) || userName.length() > 32) {
                            json.put("message", "ValueWrong");
                            json.put("colunmn", i + 1);
                            writeDataToAjax(JSONObject.fromObject(json));
                            return NONE;
                        }
                        telnetLogin.setUserName(userName);
                    }
                    if (passWordColumnNum >= 0) {
                        String password = "";
                        if (excelData[i].length > passWordColumnNum && excelData[i][passWordColumnNum] != null) {
                            password = excelData[i][passWordColumnNum].trim();
                        }
                        if ("".equals(password) || password.length() > 32) {
                            json.put("message", "ValueWrong");
                            json.put("colunmn", i + 1);
                            writeDataToAjax(JSONObject.fromObject(json));
                            return NONE;
                        }
                        telnetLogin.setPassword(password);
                    }
                    if (enablePassWordColumnNum >= 0) {
                        String enablePassword = "";
                        if (excelData[i].length > enablePassWordColumnNum
                                && excelData[i][enablePassWordColumnNum] != null) {
                            enablePassword = excelData[i][enablePassWordColumnNum].trim();
                        }
                        if ("".equals(enablePassword) || enablePassword.length() > 32) {
                            json.put("message", "ValueWrong");
                            json.put("colunmn", i + 1);
                            writeDataToAjax(JSONObject.fromObject(json));
                            return NONE;
                        }
                        telnetLogin.setEnablePassword(enablePassword);
                    }
                    if (isAAAColumnNum >= 0) {
                        String isAAA = "";
                        if (excelData[i].length > isAAAColumnNum
                                && excelData[i][isAAAColumnNum] != null) {
                            isAAA = excelData[i][isAAAColumnNum].trim();
                        }
                        if ("".equals(isAAA) || isAAA.length() > 32) {
                            json.put("message", "ValueWrong");
                            json.put("colunmn", i + 1);
                            writeDataToAjax(JSONObject.fromObject(json));
                            return NONE;
                        }
                        telnetLogin.setIsAAA(Boolean.parseBoolean(isAAA));
                    }
                    telnetLoginList.add(telnetLogin);
                }
                telnetLoginService.importTelnetLogin(telnetLoginList, overwrite);
                json.put("message", "success");
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            json.put("message", "fail");
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
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

    public Long getIp() {
        return ip;
    }

    public void setIp(Long ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnablePassword() {
        return enablePassword;
    }

    public void setEnablePassword(String enablePassword) {
        this.enablePassword = enablePassword;
    }

    public String getIpString() {
        return ipString;
    }

    public void setIpString(String ipString) {
        this.ipString = ipString;
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

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public Boolean getIsAAA() {
        return isAAA;
    }

    public void setIsAAA(Boolean isAAA) {
        this.isAAA = isAAA;
    }
}
