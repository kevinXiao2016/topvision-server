/***********************************************************************
 * $Id: CameraPlanAction.java,v1.0 2013年12月23日 下午3:03:48 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.camera.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.camera.domain.CameraPhysicalInfo;
import com.topvision.ems.epon.camera.domain.CameraPlanTable;
import com.topvision.ems.epon.camera.service.CameraPlanService;
import com.topvision.framework.common.FileUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.zetaframework.util.FileUploadUtil;

/**
 * @author Bravin
 * @created @2013年12月23日-下午3:03:48
 *
 */
@Controller("cameraPlanAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CameraPlanAction extends BaseAction {
    private static final Logger logger = LoggerFactory.getLogger(CameraPlanAction.class);
    private static final long serialVersionUID = -8483606354298481795L;
    @Autowired
    private CameraPlanService cameraPlanService;
    private String uploadExcel;
    private String fileName;
    private String ip;
    private String cameraNo;
    private String location;
    private String type;
    private String mac;
    private String note;
    private Boolean notUsePage;

    private Long entityId;
    private Long eponIndex;

    /**
     * 批量导入摄像头规划表
     * @return
     * @throws IOException 
     */
    public String uploadCameraPlan() throws IOException {
        logger.info("batch import camera plan info");
        File file = FileUploadUtil.checkout(request, fileName);
        FileInputStream fis = new FileInputStream(file);
        boolean hasIllegalInfo = cameraPlanService.batchImportPlan(fis);
        writeDataToAjax(hasIllegalInfo);
        return NONE;
    }

    /**
     * 批量导入摄像头硬件信息
     * @return
     * @throws IOException 
     */
    public String uploadPhysicalInfo() throws IOException {
        logger.info("batch import camera physical info");
        File file = FileUploadUtil.checkout(request, fileName);
        FileInputStream fis = new FileInputStream(file);
        boolean hasIllegalInfo = cameraPlanService.batchImportPhyInfo(fis);
        writeDataToAjax(hasIllegalInfo);
        return NONE;
    }

    /**
     * 加载硬件信息表
     * @return
     * @throws IOException 
     */
    public String loadCameraPhyList() throws IOException {
        Map<String, Object> map = new HashMap<>();
        if (notUsePage != null && notUsePage) {

        } else {
            map.put("start", start);
            map.put("limit", limit);
        }
        if (type != null) {
            map.put("type", type);
        }
        if (mac != null) {
            map.put("mac", MacUtils.formatMac(mac));
        }
        if (note != null) {
            map.put("note", note);
        }
        if (entityId != null) {
            map.put("entityId", entityId);
            map.put("eponIndex", eponIndex);
        }
        List<CameraPhysicalInfo> list = cameraPlanService.loadCameraPhyList(map);
        int count = cameraPlanService.getCameraPhyCount(map);
        JSONObject json = new JSONObject();
        json.put("rowCount", count);
        json.put("data", list);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 删除规划表表项
     * @return
     */
    public String deleteCameraPlan() {
        cameraPlanService.deleteCameraPlan(cameraNo);
        return NONE;
    }

    /**
     * 删除物理信息表项
     * @return
     */
    public String deletePhyInfo() {
        cameraPlanService.deletePhyInfo(mac);
        return NONE;
    }

    /**
     * 展示
     * @return
     */
    public String showCameraPlan() {
        return SUCCESS;
    }

    public String showCameraPhyInfo() {
        return SUCCESS;
    }

    /**
     * 修改规划表项
     * @return
     */
    public String modifyCameraPlan() {
        CameraPlanTable table = new CameraPlanTable();
        table.setCameraNo(cameraNo);
        table.setIp(ip);
        table.setLocation(location);
        cameraPlanService.modifyCameraPlan(table);
        return NONE;
    }

    /**
     * 修改物理信息备注
     * @return
     */
    public String modifyPhyInfo() {
        CameraPhysicalInfo info = new CameraPhysicalInfo();
        info.setMac(MacUtils.formatMac(mac));
        info.setType(type);
        info.setNote(note);
        cameraPlanService.modifyPhyInfo(info);
        return NONE;
    }

    /**
     * 手动添加摄像头规划
     * @return
     */
    public String addCameraPlan() {
        CameraPlanTable table = new CameraPlanTable();
        table.setCameraNo(cameraNo);
        table.setIp(ip);
        table.setLocation(location);
        cameraPlanService.addCameraPlan(table);
        return NONE;
    }

    /**
     * 手动添加硬件信息
     * @return
     */
    public String addCameraPhy() {
        CameraPhysicalInfo info = new CameraPhysicalInfo();
        info.setMac(MacUtils.formatMac(mac));
        info.setType(type);
        info.setNote(note);
        cameraPlanService.addCameraPhy(info);
        return NONE;
    }

    /**
     * 加载规划表 
     * @return
     * @throws IOException 
     */
    public String loadCameraPlanList() throws IOException {
        Map<String, Object> map = new HashMap<>();
        if (notUsePage != null && notUsePage) {

        } else {
            map.put("start", start);
            map.put("limit", limit);
        }
        if (ip != null) {
            map.put("ip", ip);
        }
        if (location != null) {
            map.put("location", location);
        }
        if (cameraNo != null) {
            map.put("cameraNo", cameraNo);
        }
        if (entityId != null) {
            map.put("entityId", entityId);
            map.put("eponIndex", eponIndex);
        }
        List<CameraPlanTable> list = cameraPlanService.loadCameraPlanList(map);
        int count = cameraPlanService.getCameraPlanCount(map);
        JSONObject json = new JSONObject();
        json.put("rowCount", count);
        json.put("data", list);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 下载规划表模板
     * @return
     * @throws IOException 
     */
    public String downloadPlanTemplate() {
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=cameraPlanTemplate.xlsx");
        int i;
        byte[] b = new byte[1024];
        String template = SystemConstants.WEB_INF_REAL_PATH + File.separator + "template" + File.separator
                + "cameraplan.xlsx";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(template));
            OutputStream out = response.getOutputStream();
            while ((i = fis.read(b)) > 0) {
                out.write(b, 0, i);
            }
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            FileUtils.closeQuitely(fis);
        }
        return NONE;
    }

    /**
     * 下载硬件信息模板
     * @return
     */
    public String downloadPhyTemplate() {
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=cameraPhysicalTemplate.xlsx");
        String template = SystemConstants.WEB_INF_REAL_PATH + File.separator + "template" + File.separator
                + "cameraphy.xlsx";
        int i;
        byte[] b = new byte[1024];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(template));
            OutputStream out = response.getOutputStream();
            while ((i = fis.read(b)) > 0) {
                out.write(b, 0, i);
            }
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            FileUtils.closeQuitely(fis);
        }
        return NONE;
    }

    public String getUploadExcel() {
        return uploadExcel;
    }

    public void setUploadExcel(String uploadExcel) {
        this.uploadExcel = uploadExcel;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCameraNo() {
        return cameraNo;
    }

    public void setCameraNo(String cameraNo) {
        this.cameraNo = cameraNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getNotUsePage() {
        return notUsePage;
    }

    public void setNotUsePage(Boolean notUsePage) {
        this.notUsePage = notUsePage;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getEponIndex() {
        return eponIndex;
    }

    public void setEponIndex(Long eponIndex) {
        this.eponIndex = eponIndex;
    }

}
