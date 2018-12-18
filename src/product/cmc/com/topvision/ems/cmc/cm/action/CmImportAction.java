package com.topvision.ems.cmc.cm.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.cm.service.CmImportService;
import com.topvision.ems.cmc.domain.CmImportError;
import com.topvision.ems.cmc.domain.CmImportInfo;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.cmc.util.StringUtils;
import com.topvision.framework.common.ExcelUtil;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;

import net.sf.json.JSONObject;

/**
 * 
 * @author YangYi
 * @created @2013-10-10-下午8:28:55 从CmAction拆分,CM信息导入部分
 * 
 */
@Controller("cmImportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmImportAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource(name = "cmImportService")
    private CmImportService cmImportService;
    private int start;
    private int limit;
    private String cmMac;
    private String cmAlias;
    private Boolean deleteStatus;
    private CmImportInfo cmImportInfo;// YangYi增加@20130910,用于CM导入信息手动修改
    protected Integer operationResult;

    /**
     * 显示CM导入信息页面
     * 
     * @return
     */
    public String showCmImportList() {
        return SUCCESS;
    }

    public String showCmInfoUpload() {
        return SUCCESS;
    }

    /**
     * 获取导入信息列表
     * 
     * @return
     */
    public String loadCmImportInfoList() {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> json = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(cmMac)) {
            String formatQueryMac = MacUtils.formatQueryMac(cmMac);
            if (formatQueryMac.indexOf(":") == -1) {
                map.put("queryMacWithoutSplit", formatQueryMac);
            }
            map.put("cmMac", formatQueryMac);
        }
        if (!StringUtils.isEmpty(cmAlias)) {
            //mysql中下划线是特殊的，like的时候必须转义
            if (cmAlias.contains("_")) {
                cmAlias = cmAlias.replace("_", "\\_");
            }
            map.put("cmAlias", cmAlias);
        }
        List<CmImportInfo> cmImportInfos = cmImportService.getCmImportInfoList(map, start, limit);
        //add by fanzidong,需要对MAC地址进行格式化，方便进行展示
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        for (CmImportInfo cmImportInfo : cmImportInfos) {
            String formatedMac = MacUtils.convertMacToDisplayFormat(cmImportInfo.getCmMacAddr(), macRule);
            cmImportInfo.setCmMacAddr(formatedMac);
        }
        long num = cmImportService.getCmImportInfoNum(map);
        json.put("data", cmImportInfos);
        json.put("rowCount", num);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 显示选定行的CM属性：MAC,别名/分类,用途/位置,导入日期
     * 
     * @author YangYi
     * @since 2013-09-10
     * @return
     */
    public String showCmInfoConfig() {
        Map<String, Object> map = new HashMap<String, Object>();
        //add by fanzidong, 在查询前需要格式化MAC地址
        cmMac = MacUtils.convertToMaohaoFormat(cmMac);
        map.put("cmMac", cmMac);
        List<CmImportInfo> cmImportInfos = cmImportService.getCmImportInfoList(map, 0, 1);
        //add by fanzidong,在展示前需要格式化MAC地址
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        if (cmImportInfos.size() > 0) {
            cmImportInfo = cmImportInfos.get(0);
            String formatedMac = MacUtils.convertMacToDisplayFormat(cmImportInfo.getCmMacAddr(), macRule);
            cmImportInfo.setCmMacAddr(formatedMac);
        } else {
            cmImportInfo = new CmImportInfo();
            cmImportInfo.setCmMacAddr(MacUtils.convertMacToDisplayFormat(cmMac, macRule));
        }
        return SUCCESS;
    }

    /**
     * 保存CM修改修改后的结果，别名/分类,用途/位置,导入日期为选当前日期
     * 
     * @author YangYi
     * @since 2013-09-10
     * 
     * @return
     */
    public String modifyCmInfo() {
        String result = null;
        Map<String, String> message = new HashMap<String, String>();
        cmImportInfo.setImportTime(new Date().getTime());
        //add by fanzidong,存储之前需要对MAC地址进行格式化
        cmImportInfo.setCmMacAddr(MacUtils.convertToMaohaoFormat(cmImportInfo.getCmMacAddr()));
        List<CmImportInfo> cmImportInfos = new ArrayList<CmImportInfo>();
        cmImportInfos.add(cmImportInfo);
        try {
            cmImportService.addCmImportInfo(cmImportInfos, false);
            result = "success";
            operationResult = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.error("", e);
            result = "error";
            operationResult = OperationLog.FAILURE;
        } finally {
            message.put("message", result);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    /**
     * 扫描CM导入信息
     * 
     * @return
     * @throws Exception
     */
    public String scanCmImportInfos() throws Exception {
        List<CmImportInfo> cmImportInfos = new ArrayList<CmImportInfo>();
        long time = System.currentTimeMillis();
        String[][] excelData = null;
        Map<String, Object> json = new HashMap<String, Object>();
        List<CmImportError> errors = new ArrayList<CmImportError>();
        long startTime = System.currentTimeMillis();
        try {
            MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();
            Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();
            while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
                String inputName = fileParameterNames.nextElement();
                String[] contentType = multiWrapper.getContentTypes(inputName);
                if (isNonEmpty(contentType)) {
                    String[] fileName = multiWrapper.getFileNames(inputName);
                    if (CmcUtil.isNonEmpty(fileName)) {
                        File[] files = multiWrapper.getFiles(inputName);
                        if (files != null) {
                            for (File file1 : files) {
                                try {
                                    excelData = ExcelUtil.getExcelDataFor2007(file1).get(0);
                                    break;
                                } catch (Exception e) {
                                    try {
                                        // added by haugndongsheng @2013-7-1 增加2003Excel文档支持
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
            long getFileTime = System.currentTimeMillis();
            logger.debug("get file cost: " + (getFileTime - startTime));
            if (excelData == null) {
                json.put("message", "FileWrong");
                writeDataToAjax(JSONObject.fromObject(json));
                return NONE;
            } else {
                for (int i = 1; i < excelData.length; i++) {
                    if (excelData[i] == null) {
                        continue;
                    }
                    if (excelData[i][0] == null || "".endsWith(excelData[i][0].toUpperCase())) {
                        continue;
                    }
                    String mac = excelData[i][0].toUpperCase().trim();
                    String alias = excelData[i].length >= 3 ? excelData[i][2].trim() : null;
                    String classified = excelData[i].length >= 2 ? excelData[i][1].trim() : null;
                    CmImportInfo cmImportInfo = new CmImportInfo();

                    if (!MacUtils.isMac(mac)) {
                        CmImportError error = new CmImportError();
                        error.setCmMacAddr(mac);
                        error.setCmClassified(classified);
                        error.setCmAlias(alias);
                        error.setErrorLine(i + 1);
                        error.setErrorMessage("macWrong");
                        errors.add(error);
                        continue;
                    }
                    if (classified != null && classified.length() > 10) {
                        CmImportError error = new CmImportError();
                        error.setCmMacAddr(mac);
                        error.setCmClassified(classified);
                        error.setCmAlias(alias);
                        error.setErrorLine(i + 1);
                        error.setErrorMessage("classifiedWrong");
                        errors.add(error);
                        continue;
                    }
                    if (alias != null && alias.length() > 256) {
                        CmImportError error = new CmImportError();
                        error.setCmMacAddr(mac);
                        error.setCmClassified(classified);
                        error.setCmAlias(alias);
                        error.setErrorLine(i + 1);
                        error.setErrorMessage("aliasWrong");
                        errors.add(error);
                        continue;
                    }
                    //add by fanzidong,在存储之前需要进行格式化
                    mac = MacUtils.convertToMaohaoFormat(mac);
                    cmImportInfo.setCmMacAddr(mac);
                    cmImportInfo.setCmClassified(classified);
                    cmImportInfo.setCmAlias(alias);
                    cmImportInfo.setImportTime(time);
                    cmImportInfos.add(cmImportInfo);
                }
                long afterFilterTime = System.currentTimeMillis();
                logger.debug("filter file cost: " + (afterFilterTime - getFileTime));
                logger.debug("filter file find cm number: " + cmImportInfos.size());
                cmImportService.addCmImportInfo(cmImportInfos, deleteStatus);
                long addMacTime = System.currentTimeMillis();
                logger.debug("add mac cost: " + (addMacTime - afterFilterTime));
                cmImportService.importErrorLog(errors);
                logger.debug("filter file find error cm number: " + errors.size());
                long exportErrorMacTime = System.currentTimeMillis();
                logger.debug("export error mac cost: " + (exportErrorMacTime - addMacTime));
                json.put("message", "success");
                json.put("number", cmImportInfos.size());
                json.put("errorNumber", errors.size());
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

    @SuppressWarnings("unused")
    private boolean matchesMac(String mac) {
        if (mac != null && !mac.isEmpty()) {
            // 用Pattern
            String regex = "^((\\d|[A-F]){2}:){5}" + "(\\d|[A-F]){2}$";
            if (mac.matches(regex)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 下载cm 导入模板
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String downLoadCmInfoImportTemplate() throws UnsupportedEncodingException {
        String tmpFile = "cmInfoImportTemplate.xlsx";
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
            logger.debug("downLoadCmInfoFileTemplate is error:{}", e);
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
                logger.debug("downLoadCmInfoFileTemplate is error:{}", e);
            }
        }
        return NONE;
    }

    /**
     * 下载cm 导入错误记录
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String downLoadErrorInfo() throws UnsupportedEncodingException {
        String tmpFile = "cmInfoImportErrorLog.xls";
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
            logger.debug("downLoadErrorInfo is error:{}", e);
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
                logger.debug("downLoadErrorInfo is error:{}", e);
            }
        }
        return NONE;
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

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public String getCmAlias() {
        return cmAlias;
    }

    public void setCmAlias(String cmAlias) {
        this.cmAlias = cmAlias;
    }

    public Boolean getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public CmImportInfo getCmImportInfo() {
        return cmImportInfo;
    }

    public void setCmImportInfo(CmImportInfo cmImportInfo) {
        this.cmImportInfo = cmImportInfo;
    }

}