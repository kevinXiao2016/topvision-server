package com.topvision.ems.cmc.cm.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.cm.dao.CmImportDao;
import com.topvision.ems.cmc.cm.service.CmImportService;
import com.topvision.ems.cmc.domain.CmImportError;
import com.topvision.ems.cmc.domain.CmImportInfo;
import com.topvision.ems.report.util.ReportTaskUtil;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.SystemConstants;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 
 * @author YangYi
 * @created @2013-10-30-上午9:05:14 从CmServiceImpl拆分
 * 
 */
@Service("cmImportService")
public class CmImportServiceImpl extends BaseService implements CmImportService {
    @Resource(name = "cmImportDao")
    private CmImportDao cmImportDao;
    public static final String CMIMPORT_ERRORLOG_FOLDER = SystemConstants.ROOT_REAL_PATH + File.separator + "META-INF"
            + File.separator;

    @Override
    public List<CmImportInfo> getCmImportInfoList(Map<String, Object> map, Integer start, Integer limit) {
        return cmImportDao.selectCmImportInfoList(map, start, limit);
    }

    @Override
    public Long getCmImportInfoNum(Map<String, Object> map) {
        return cmImportDao.selectCmImportInfoNum(map);
    }

    @Override
    public void addCmImportInfo(List<CmImportInfo> cmImportInfos, Boolean delete) {
        if (delete) {
            cmImportDao.deleteCmImportInfo();
        }
        cmImportDao.batchInsertOrUpdateCmImportInfo(cmImportInfos);
    }

    @Override
    public void importErrorLog(List<CmImportError> errors) throws IOException {
        String filePath = CMIMPORT_ERRORLOG_FOLDER + "cmInfoImportErrorLog.xls";
        File generateFile = new File(filePath);
        generateFile.createNewFile();
        OutputStream out = new FileOutputStream(generateFile);
        try {
            Label label;
            WritableWorkbook workbook = Workbook.createWorkbook(out);
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);

            sheet.setColumnView(0, 30);
            sheet.setColumnView(1, 30);
            sheet.setColumnView(2, 60);
            sheet.setColumnView(3, 20);
            sheet.setColumnView(4, 20);
            WritableCellFormat titleCellFormat = ReportTaskUtil.getTitleCellFormat();

            label = new Label(0, 0, "Mac", titleCellFormat);
            sheet.addCell(label);
            label = new Label(1, 0, "Type", titleCellFormat);
            sheet.addCell(label);
            label = new Label(2, 0, "Alias(Position)", titleCellFormat);
            sheet.addCell(label);
            label = new Label(3, 0, "Error Message", titleCellFormat);
            sheet.addCell(label);
            label = new Label(4, 0, "Error Line", titleCellFormat);
            sheet.addCell(label);

            Integer rowNum = 1;
            Integer rowNumView = 1;
            WritableCellFormat contentCellFormat = ReportTaskUtil.getContentCellFormat();
            for (CmImportError error : errors) {
                label = new Label(0, rowNum, error.getCmMacAddr(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(1, rowNum, error.getCmClassified(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(2, rowNum, error.getCmAlias(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(3, rowNum, error.getErrorMessage(), contentCellFormat);
                sheet.addCell(label);
                label = new Label(4, rowNum, error.getErrorLine().toString(), contentCellFormat);
                sheet.addCell(label);
                rowNum++;
                rowNumView++;
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.debug("downFile method is error:{}", e);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.debug("downFile method is error:{}", e);
            }
        }
    }
}
