/***********************************************************************
 * $Id: CmPollConfigAction.java,v1.0 2015年3月17日 上午8:46:22 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.config.action;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.topvision.ems.cm.cmpoll.config.domain.CmMac;
import com.topvision.ems.cm.cmpoll.exception.CmListSizeOverSpecificationException;
import com.topvision.platform.SystemConstants;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cm.cmpoll.config.service.CmPollConfigService;
import com.topvision.ems.cm.cmpoll.scheduler.domain.CmPollCollector;
import com.topvision.ems.cm.cmpoll.scheduler.service.CmPollSchedulerService;
import com.topvision.ems.cm.cmpoll.taskbuild.service.CmPollTaskBuildService;
import com.topvision.ems.exportAndImport.service.ExportService;
import com.topvision.framework.common.ExcelUtil;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.HttpUtils;
import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author loyal
 * @created @2015年3月17日-上午8:46:22
 * 
 */
@Controller("cmPollConfigAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmPollConfigAction extends BaseAction {
    private static final long serialVersionUID = -4008184309092477158L;
    private static final String MAC_TITLE = "MAC";

    @Resource(name = "cmPollConfigService")
    private CmPollConfigService cmPollConfigService;
    private final Logger logger = LoggerFactory.getLogger(CmPollConfigAction.class);
    @Resource(name = "cmPollSchedulerService")
    private CmPollSchedulerService cmPollSchedulerService;
    @Resource(name = "cmPollTaskBuildService")
    private CmPollTaskBuildService cmPollTaskBuildService;
    @Autowired
    private ExportService exportService;
    private Integer cmPollStatus;
    private Long cmPollInterval;
    private String completeTime;
    private String switchName;
    private String fileName;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * cmPollCollectList.jsp CM采集器监测页面
     * 
     * @return
     */
    public String showCmPollCollectList() {
        return SUCCESS;
    }

    public String getCmPollCompleteTime() {
        List<CmPollCollector> cmPollCollectorList = cmPollSchedulerService.getCmPollCollectorList();
        // 剩余时间： （总任务数 - 所有采集器本轮已完成任务数）/ (所有采集器五分钟内执行任务数 / 5)
        Long totalTask = cmPollTaskBuildService.getCmOnLine();
        Long totalCompleteTask = 0L; // 采集器本轮已完成任务数
        Long totalCompleteTask5m = 0L; // 采集器五分钟内执行任务数
        for (CmPollCollector cmPollCollector : cmPollCollectorList) {
            totalCompleteTask += totalCompleteTask + cmPollCollector.getRoundTotalTaskCounts();
            totalCompleteTask5m += totalCompleteTask5m + cmPollCollector.getExecutedTaskIn5m();
        }
        Double remainTime = 0D;// 剩余时间
        if (totalCompleteTask5m > 0) {
            // 剩余时间 = （（总任务数 - 所有采集器本轮已完成任务数）/ 所有采集器五分钟内执行任务数） × 五分钟
            remainTime = (double) ((((double) totalTask - (double) totalCompleteTask) / (totalCompleteTask5m)) * 5);
        }
        // remainTime == 0 : 所有任务已完成（totalTask - totalCompleteTask = 0）
        if (remainTime == 0) {
            completeTime = "0"; // 已完成
        } else {
            completeTime = sdf.format(new Date(System.currentTimeMillis() + (long) (remainTime * 60 * 1000))); // 当前时间　+　剩余时间
        }
        writeDataToAjax(completeTime);
        return NONE;
    }

    public String getCmPollCollectList() {
        List<CmPollCollector> cmPollCollectorList = cmPollSchedulerService.getCmPollCollectorList();
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("data", cmPollCollectorList);
        json.put("rowCount", cmPollCollectorList.size());
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 打开导入指定CMlist的页面
     * @return
     */
    public String showSpecifiedCmListImport() {
        return SUCCESS;
    }
    /**
     * terminalConfig.jsp 修改Cm轮询开关选择
     *
     * @return
     */
    public String loadSwitchName() {
        JSONObject ret = new JSONObject();
        switchName = cmPollConfigService.getCmPollSwitchName();
        ret.put("switchName", switchName);
        write(ret, HttpUtils.CONTENT_TYPE_HTML);
        return NONE;
    }

    public String loadCmPollInterval() {
        JSONObject ret = new JSONObject();
        cmPollInterval = cmPollConfigService.getCmPollInterval();
        ret.put("cmPollInterval", cmPollInterval);
        write(ret, HttpUtils.CONTENT_TYPE_HTML);
        return NONE;
    }

    /**
     * terminalConfig.jsp 修改Cm轮询开关选择
     *
     * @return
     */
    public String changeSwitchForCmPoll() {
        cmPollConfigService.changeSwitchForCmPoll(switchName, cmPollInterval);
        return NONE;
    }

    /**
     *
     * @return
     */
    public String loadSpecifiedCmListCount() throws IOException {
        JSONObject ret = new JSONObject();
        List<String> macList = cmPollConfigService.getSpecifiedCmList();
        ret.put("rowcount", macList.size());
        write(ret, HttpUtils.CONTENT_TYPE_HTML);
        return NONE;
    }

    /**
     * terminalConfig.jsp 修改Cm轮询开关选择
     *
     * @return
     */
    public String importSpecifiedCmList() throws IOException {
        JSONObject ret = new JSONObject();
        ret.put("isOverLimit", false);
        
        // 获取文件
        File file = null;
        try{
        	file = anlayseFileFromFlash();
        	if (file == null || !file.exists()) {
        		ret.put("error", "no file");
        		write(ret, HttpUtils.CONTENT_TYPE_HTML);
                return NONE;
        	}
        } catch(Exception e) {
        	ret.put("error", "no file");
    		write(ret, HttpUtils.CONTENT_TYPE_HTML);
            return NONE;
        }
        
        // 从文件中解析出数据
        List<String> macList = new ArrayList<>();
        try{
        	List<String[][]> data = ExcelUtil.getExcelData(file);
        	if (data == null || data.isEmpty()) {
        		ret.put("error", "file has no data");
        		write(ret, HttpUtils.CONTENT_TYPE_HTML);
                return NONE;
        	}
            for (String[][] macData : data) {
            	if (macData == null) {
            		continue;
            	}
                String[] firstRow = macData[0];
            	if (firstRow == null) {
            		continue;
            	}
                int macIndex = -1;
                for (int i = 0; i < firstRow.length ; i++) {
                	if (MAC_TITLE.equalsIgnoreCase(firstRow[i])) {
                		macIndex = i;
                		break;
                	}
                }
                if(macIndex == -1) {
                	continue;
                }

                for(int i=1; i< macData.length; i++) {
                    String mac = macData[i][macIndex];
                	if (mac == null || !MacUtils.isMac(mac)) {
                		continue;
                	}
                    if (macList.size() == 1000) {
                        throw new CmListSizeOverSpecificationException();
                    }
                    String macResult = MacUtils.convertToMaohaoFormat(mac);
                    if (!macList.contains(macResult)) {
                        macList.add(macResult);
                    }
                }
            }
        } catch(CmListSizeOverSpecificationException e) {
            ret.put("isOverLimit", true);
        } catch(Exception e) {
            ret.put("error", e.getMessage());
            write(ret, HttpUtils.CONTENT_TYPE_HTML);
            return NONE;
        }
        
        // 生成文件
        ret.put("rowcount", macList.size());
        if(macList.size() > 0) {
        	cmPollConfigService.importSpecifiedCmList(macList);
        } else {
        	ret.put("error", "no data");
        }
        write(ret, HttpUtils.CONTENT_TYPE_HTML);
        return NONE;
    }

    /**
     * terminalConfig.jsp 修改Cm轮询开关选择
     *
     * @return
     */
    public String generateSpecifiedCmListFile() {
        JSONObject json = new JSONObject();
        List<String> macList = cmPollConfigService.getSpecifiedCmList();
        List<CmMac> list = new ArrayList<>();

        for (String mac : macList) {
            CmMac cmMac = new CmMac();
            cmMac.setMAC(mac);
            list.add(cmMac);
        }
        try {
            String fileName = exportService.exportSheetContent("SpecifiedCmList.xls","mac",list);
            json.put("data", fileName);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("", e);
            }
            json.put("error", e.getMessage());
        }
        writeDataToAjax(json);
        return NONE;
    }
    
    public String exportSpecifiedCmListFile() {
        try {
            exportService.downloadFile("SpecifiedCmList.xls");
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("", e);
            }
        }
        return NONE;
    }

    /**
     * 下载导入模板
     *
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public String downLoadTemplate() throws UnsupportedEncodingException {
        String tmpFile = "specifiedCmPollImportTemplate.xlsx";
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

    public Integer getCmPollStatus() {
        return cmPollStatus;
    }

    public void setCmPollStatus(Integer cmPollStatus) {
        this.cmPollStatus = cmPollStatus;
    }

    public Long getCmPollInterval() {
        return cmPollInterval;
    }

    public void setCmPollInterval(Long cmPollInterval) {
        this.cmPollInterval = cmPollInterval;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
    
    
}
