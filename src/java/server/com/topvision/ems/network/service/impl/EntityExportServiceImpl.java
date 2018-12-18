/***********************************************************************
 * $Id: EntityExportServiceImpl.java,v1.0 2013-11-1 上午8:34:39 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.EntityExportDao;
import com.topvision.ems.network.service.EntityExportService;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.service.BaseService;

/**
 * @author loyal
 * @created @2013-11-1-上午8:34:39
 * 
 */
@Service("entityExportService")
public class EntityExportServiceImpl extends BaseService implements EntityExportService {
    @Resource(name = "entityExportDao")
    private EntityExportDao entityExportDao;
    public static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityExportService#getEntity(java.util.Map, int, int)
     */
    public List<Entity> getEntity(Map<String, Object> map, int start, int limit) {
        return entityExportDao.selectEntity(map, start, limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.EntityExportService#getEntityNum(java.util.Map)
     */
    public Long getEntityNum(Map<String, Object> map) {
        return entityExportDao.selectEntityNum(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.EntityExportService#exportEntityToExcel(java.util.List)
     */
    public void exportEntityToExcel(List<Entity> entityList) {
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/msexcel");

        String fileName;
        try {
            fileName = URLEncoder.encode("EntityList" + "-" + fileNameFormatter.format(new Date()) + ".xls", "UTF-8");
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            createEntityExcelFile(entityList, workbook);
        } catch (UnsupportedEncodingException e) {
            logger.debug("unsupported Encoding Exception:", e);
        } catch (IOException e) {
            logger.debug("something wrong with I/O:", e);
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
                logger.debug("close fileStream error:{}", e);
            }
        }
    }

    /**
     * 导出设备
     * 
     * @param entityList
     * @param workbook
     */
    private void createEntityExcelFile(List<Entity> entityList, WritableWorkbook workbook) {
        // 创建sheet页和一些格式化信息
        WritableSheet sheet = workbook.createSheet("Sheet1", 0);

        sheet.setColumnView(0, 30);
        sheet.setColumnView(1, 30);
        sheet.setColumnView(2, 20);
        sheet.setColumnView(3, 25);
        sheet.setColumnView(4, 25);
        sheet.setColumnView(5, 40);
        sheet.setColumnView(6, 30);
        sheet.setColumnView(7, 30);
        sheet.setColumnView(8, 20);
        Label label;
        try {
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();

            label = new Label(0, 0, "IP", titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 0, "Alias", titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 0, "Region", titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 0, "SysName", titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 0, "Entity Type", titleCellFormat);
            sheet.addCell(label);
            label = new Label(5, 0, "MAC", titleCellFormat);
            sheet.addCell(label);
            label = new Label(6, 0, "Contact", titleCellFormat);
            sheet.addCell(label);
            label = new Label(7, 0, "Location", titleCellFormat);
            sheet.addCell(label);
            label = new Label(8, 0, "Note", titleCellFormat);
            sheet.addCell(label);
            

            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            if (entityList == null) {
                return;
            }
            // 遍历每个地域
            int rowNum = 1;
            for (Entity entity : entityList) {
                // 输出数据行
                label = new Label(0, rowNum, entity.getIp(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, entity.getName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, entity.getFolderName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, entity.getSysName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(4, rowNum, entity.getTypeName(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(5, rowNum, entity.getMac(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(6, rowNum, entity.getContact(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(7, rowNum, entity.getLocation(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(8, rowNum, entity.getNote(), contentCellFormat);
                sheet.addCell(label);
                rowNum++;
            }

            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("createCurrentAlarmReportExcelFile method is error:{}", e);
        }
    }
    
    @Override
    public List<String> getEntityFolder(Long entityId) {
        return entityExportDao.selectEntityFolder(entityId);
    }

    public EntityExportDao getEntityExportDao() {
        return entityExportDao;
    }

    public void setEntityExportDao(EntityExportDao entityExportDao) {
        this.entityExportDao = entityExportDao;
    }

}
