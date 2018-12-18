/***********************************************************************
 * $Id: ExportServiceImpl.java,v1.0 2015-6-29 上午9:14:12 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exportAndImport.service.impl;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.topvision.ems.exportAndImport.dao.ExportDao;
import com.topvision.ems.exportAndImport.domain.EIConstant;
import com.topvision.ems.exportAndImport.service.ExportService;
import com.topvision.ems.exportAndImport.util.EIUtil;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.dao.MapNodeDao;
import com.topvision.ems.network.dao.TopoFolderDao;
import com.topvision.ems.network.domain.EntityFolderRela;
import com.topvision.ems.network.domain.MapNode;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.platform.dao.PortletItemDao;
import com.topvision.platform.dao.RoleDao;
import com.topvision.platform.dao.UserDao;
import com.topvision.platform.dao.UserPreferencesDao;
import com.topvision.platform.domain.PortletItem;
import com.topvision.platform.domain.Role;
import com.topvision.platform.domain.RoleFunctionRela;
import com.topvision.platform.domain.User;
import com.topvision.platform.domain.UserAuthFolder;
import com.topvision.platform.domain.UserPreferences;
import com.topvision.platform.domain.UserRoleRela;
import com.topvision.platform.util.FtpClientUtil;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * @author fanzidong
 * @created @2015-6-29-上午9:14:12
 * 
 */
@Service("exportService")
public class ExportServiceImpl implements ExportService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private TopoFolderDao topoFolderDao;
    @Autowired
    private MapNodeDao mapNodeDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PortletItemDao portletItemDao;
    @Autowired
    private UserPreferencesDao userPreferencesDao;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private ExportDao exportDao;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final int SHEET_MAX_NUM = 60000;

    @Override
    public String generateExcelFile(com.alibaba.fastjson.JSONArray data, com.alibaba.fastjson.JSONArray columns,
            String title) throws WriteException, Exception {
        try {
            // 创造excel文件
            File path = new File(EIConstant.EXPORT_ROOT);
            path.mkdirs();
            // add by fanzidong,fileName需要去掉非法字符
            String fileName = title.replaceAll("[^\\u4e00-\\u9fa5a-zA-Z0-9.-]", "_");
            String fileNameWithTimestamp = fileName + "_" + fileNameFormatter.format(new Date()) + ".xls";
            String filePath = EIConstant.EXPORT_ROOT + fileNameWithTimestamp;
            File file = new File(filePath);
            file.createNewFile();
            WritableWorkbook workbook = Workbook.createWorkbook(file);

            // 填充excel文件
            writeExcelFile(workbook, title, data, columns);

            // 返回文件名
            return fileNameWithTimestamp;
        } catch (Exception e) {
            return null;
        }
    }

    private void writeExcelFile(WritableWorkbook workbook, String fileName, com.alibaba.fastjson.JSONArray data, com.alibaba.fastjson.JSONArray columns)
            throws WriteException, IOException {
        Label label;
        WritableCellFormat titleFormat = getTitleFormat();
        WritableCellFormat columnFormat = getTitleCellFormat();
        WritableCellFormat contentFormat = getContentCellFormat();
        
        // ScriptEngine for renderValue
        ScriptEngineManager mgr = new ScriptEngineManager();    
        ScriptEngine engine = mgr.getEngineByExtension("js");

        // 输出标题
        WritableSheet sheet = workbook.createSheet("Sheet1", 0);
        sheet.setRowView(0, 1000);
        label = new Label(0, 0, fileName, titleFormat);
        sheet.addCell(label);
        sheet.mergeCells(0, 0, columns.size() - 1, 0);

        int row = 1;

        // 输出内容数据
        // 判断需要几个sheet,每个sheet最多60000行数据
        Integer sheetNum = data.size() / SHEET_MAX_NUM + 1, curSheetNum;
        com.alibaba.fastjson.JSONObject record = new com.alibaba.fastjson.JSONObject();
        Object value;
        String valueStr;

        for (int curSheet = 0; curSheet < sheetNum; curSheet++) {
            // 初始化好当前sheet,第一个除外
            if (curSheet == 0) {
                sheet = workbook.getSheet(curSheet);
            } else {
                sheet = workbook.createSheet("Sheet" + (curSheet + 1), curSheet);
                row = 0;
            }
            // 输出表头
            for (int i = 0; i < columns.size(); i++) {
                com.alibaba.fastjson.JSONObject column =  columns.getJSONObject(i);
                sheet.setColumnView(i, column.getInteger("width") / 7);
                label = new Label(i, row, column.getString("title"), columnFormat);
                sheet.addCell(label);
            }
            row++;
            if (curSheet == sheetNum - 1) {
                curSheetNum = data.size() % SHEET_MAX_NUM;
            } else {
                curSheetNum = SHEET_MAX_NUM;
            }
            // 填充当前sheet
            for (int i = 0; i < curSheetNum; i++) {
                try {
                    record = data.getJSONObject(curSheet * SHEET_MAX_NUM + i);
                    for (int j = 0; j < columns.size(); j++) {
                        value = record.get(columns.getJSONObject(j).getString("index"));
                        if (columns.getJSONObject(j).containsKey("renderer")) {
                            valueStr = renderValue(engine, columns.getJSONObject(j).getString("rendererName"), columns.getJSONObject(j).getString("renderer"), value, record);
                        } else if (value == null || value.toString().toLowerCase().equals("null")) {
                            valueStr = "-";
                        } else {
                            valueStr = value.toString();
                        }
                        

                        label = new Label(j, row, valueStr, contentFormat);
                        sheet.addCell(label);
                    }
                    row++;
                } catch (Exception e) {
                    logger.debug("ouput data error", e);
                }
            }
        }

        // 关闭
        workbook.write();
        workbook.close();
    }
    
    private String renderValue(ScriptEngine engine, String renderName, String render, Object value, JSONObject data) {
        if (render == null || render.equals("")) {
            return value.toString();
        }
        JSONObject record = new JSONObject();
        record.put("data", data);
        try{
            engine.eval(render);
            Invocable inv = (Invocable) engine; 
            Object val = inv.invokeFunction(renderName, value, null, record);
            if(val != null) {
                return val.toString();
            } else {
                return value.toString();
            }
        }catch(Exception e) {
            return value.toString();
        }
    }

    private WritableCellFormat getTitleFormat() throws WriteException {
        WritableCellFormat cellFormat = new WritableCellFormat(
                new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD));
        cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        cellFormat.setAlignment(jxl.format.Alignment.CENTRE);
        return cellFormat;
    }

    private WritableCellFormat getTitleCellFormat() throws WriteException {
        WritableCellFormat titleCellFormat = new WritableCellFormat(
                new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
        titleCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
        titleCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        titleCellFormat.setBackground(Colour.GRAY_25);
        titleCellFormat.setBorder(Border.ALL, BorderLineStyle.HAIR);
        return titleCellFormat;
    }

    private WritableCellFormat getContentCellFormat() throws WriteException {
        WritableCellFormat contentCellFormat = new WritableCellFormat(
                new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD));
        contentCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
        contentCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        contentCellFormat.setWrap(true);
        return contentCellFormat;
    }

    @Override
    public void downloadFile(String fileName) {
        // 获取excel文件路径
        String filePath = EIConstant.EXPORT_ROOT + fileName;
        FileInputStream fis = null;
        OutputStream out = null;
        File file = new File(filePath);
        try {
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            ServletActionContext.getResponse().setContentType("application/x-download");
            // 转码文件名，解决中文名的问题
            fileName = new String(fileName.getBytes(FtpClientUtil.GBK), FtpClientUtil.ISO);
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            int i;
            while ((i = fis.read(b)) > 0) {
                out.write(b, 0, i);
            }
        } catch (IOException e) {
            logger.debug("", e);
        } finally {
            // 关闭流,并删除服务器端临时文件
            try {
                if (out != null)
                    out.close();
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                logger.debug("", e);
            } finally {
                // 删除文件
                file.delete();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.export.service.ExportService#entireExport()
     */
    @Override
    public String entireExport() throws Exception {
        // 生成excel文件名
        String fileName = "entireExport-" + fileNameFormatter.format(new Date()) + ".xls";

        // 生成excel文件
        File rootFolder = new File(EIConstant.EXPORT_ROOT);
        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            rootFolder.mkdir();
        }
        String filePath = EIConstant.EXPORT_ROOT + fileName;
        File generateFile = new File(filePath);
        OutputStream out = new FileOutputStream(generateFile);
        WritableWorkbook workbook = Workbook.createWorkbook(out);

        try {
            // 导出Role表数据
            exportRole(workbook);
            // 导出RoleFunctionRela表数据
            exportRoleFunctionItem(workbook);
            // 导出topofolder表数据
            exportTopoFolder(workbook);
            // 导出mapndode表数据
            exportMapNode(workbook);
            // 导出User表数据
            exportUsers(workbook);
            // 导出userrolerela
            exportUserRoleRela(workbook);
            // 导出userauthfolder表数据
            exportUserAuthFolder(workbook);
            // 导出userportletrela
            exportUserPortletRela(workbook);
            // 导出用户个性化
            exportUserPreferences(workbook);
            // 导出设备
            exportEntity(workbook);
            // 导出设备所在地域
            exportEntityFolderRela(workbook);
            // 导出设备别名
            exportEntityAlias(workbook);

            // flush
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.error("export entire excel error", e);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.debug("something wrong with I/O:", e);
            }
        }

        return fileName;
    }

    /**
     * 导出Role基本信息
     * 
     * @param workbook
     * @throws WriteException
     */
    private void exportRole(WritableWorkbook workbook) throws WriteException {
        // 从数据库获取role基本信息
        List<Role> roles = roleDao.getAllRoles();
        // 向对应的sheet中填充内容
        exportSheetContent(workbook, EIConstant.ROLE, roles);
    }

    /**
     * 导出Role对功能的控制
     * 
     * @param workbook
     * @throws WriteException
     */
    private void exportRoleFunctionItem(WritableWorkbook workbook) throws WriteException {
        // 从数据库获取Role对功能的控制信息
        List<RoleFunctionRela> relas = roleDao.getAllRoleFunctionRela();
        // 向对应的sheet中填充内容
        exportSheetContent(workbook, EIConstant.ROLEFUNCTIONRELA, relas);
    }

    /**
     * 导出地域基本信息
     * 
     * @param workbook
     * @throws WriteException
     */
    private void exportTopoFolder(WritableWorkbook workbook) throws WriteException {
        // 从数据库获取地域基本信息
        List<TopoFolder> topoFolders = topoFolderDao.loadAllFolders();
        // 向对应的sheet中填充内容
        exportSheetContent(workbook, EIConstant.TOPOFOLDER, topoFolders);
    }

    /**
     * 导出mapnode基本信息
     * 
     * @param workbook
     * @throws WriteException
     */
    private void exportMapNode(WritableWorkbook workbook) throws WriteException {
        // 从数据库获取mapnode基本信息
        List<MapNode> mapNodes = mapNodeDao.getAllMapNodes();
        // 向对应sheet中填充内容
        exportSheetContent(workbook, EIConstant.MAPNODE, mapNodes);
    }

    /**
     * 导出User基本信息
     * 
     * @param workbook
     * @throws WriteException
     */
    private void exportUsers(WritableWorkbook workbook) throws WriteException {
        // 从数据库获取User基本信息
        List<User> users = userDao.getAllUser();
        // 向对应的sheet中填充内容
        exportSheetContent(workbook, EIConstant.USER, users);
    }

    /**
     * 导出userauthfolder数据
     * 
     * @param workbook
     * @throws WriteException
     */
    private void exportUserAuthFolder(WritableWorkbook workbook) throws WriteException {
        // 从数据库获取userAuthFolder基本信息
        List<UserAuthFolder> userAuthFolders = userDao.getAllUserAuthFolder();
        // 向对应的sheet中填充内容
        exportSheetContent(workbook, EIConstant.USERAUTHFOLDER, userAuthFolders);
    }

    /**
     * 导出userRoleRela表数据
     * 
     * @param workbook
     * @throws WriteException
     */
    private void exportUserRoleRela(WritableWorkbook workbook) throws WriteException {
        // 从数据库获取userRoleRela基本信息
        List<UserRoleRela> userRoleRela = roleDao.getUserRoles();
        // 向对应的sheet中填充内容
        exportSheetContent(workbook, EIConstant.USERROLERELA, userRoleRela);
    }

    /**
     * 导出userPortletRela表数据
     * 
     * @param workbook
     * @throws WriteException
     */
    private void exportUserPortletRela(WritableWorkbook workbook) throws WriteException {
        // 从数据库获取userportlerrela基本信息
        List<PortletItem> portletItems = portletItemDao.loadAllPortletItem();
        // 向对应的sheet中填充内容
        exportSheetContent(workbook, EIConstant.USERPORTLETRELA, portletItems);
    }

    /**
     * 导出用户个性化
     * 
     * @param workbook
     * @throws WriteException
     */
    private void exportUserPreferences(WritableWorkbook workbook) throws WriteException {
        // 从数据库获取用户个性化信息
        List<UserPreferences> userPreferences = userPreferencesDao.getTotalUserPerferences();
        // 向对应的sheet中填充内容
        exportSheetContent(workbook, EIConstant.USERPREFERENCES, userPreferences);
    }

    /**
     * 导出设备
     * 
     * @param workbook
     * @throws WriteException
     */
    private void exportEntity(WritableWorkbook workbook) throws WriteException {
        // 从数据库获取设备导出信息
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("needIp", true);
        List<Entity> entities = entityDao.getEntityExportInfo(queryMap);
        // 向对应的sheet中填充内容
        exportSheetContent(workbook, EIConstant.ENTITY, entities);
    }

    /**
     * 导出设备所在地域
     * 
     * @param workbook
     * @throws WriteException
     */
    private void exportEntityFolderRela(WritableWorkbook workbook) throws WriteException {
        // 从数据库获取设备导出信息
        List<EntityFolderRela> entities = entityDao.getAllEntityFolders();
        // 向对应的sheet中填充内容
        exportSheetContent(workbook, EIConstant.ENTITYFOLDERRELA, entities);
    }

    /**
     * 导出设备别名
     * 
     * @param workbook
     * @throws WriteException
     */
    private void exportEntityAlias(WritableWorkbook workbook) throws WriteException {
        // 从数据看获取设备别名导出信息
        List<Entity> entities = exportDao.getAllEntityAlias();
        // 向对应的sheet中填充内容
        exportSheetContent(workbook, EIConstant.ENTITYALIAS, entities);
    }

    /**
     * 将数据放入指定excel的指定sheet中
     * 
     * @param workbook
     * @param sheetName
     * @param datas
     * @throws WriteException
     */
    public void exportSheetContent(WritableWorkbook workbook, String sheetName, List<?> datas) throws WriteException {
        Label label;

        // 为导出信息创建对应sheet
        WritableSheet sheet = workbook.createSheet(sheetName, workbook.getNumberOfSheets());
        if (datas == null || datas.size() == 0) {
            return;
        }
        Class<?> clazz = datas.get(0).getClass();
        // 获取对应sheet应该有的columns
        List<String> columns = EIUtil.getColumnList(clazz);

        // 遍历columns，输出标题行
        for (int i = 0, len = columns.size(); i < len; i++) {
            label = new Label(i, 0, columns.get(i));
            sheet.addCell(label);
        }

        // 输出内容行
        Object o;
        for (int i = 0, len = datas.size(); i < len; i++) {
            o = datas.get(i);
            Class<?> objectClass = o.getClass();
            // 遍历要输出的列
            for (int j = 0; j < columns.size(); j++) {
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(columns.get(j), objectClass);
                    Method getMethod = pd.getReadMethod();
                    Object value = getMethod.invoke(o);
                    if (value != null) {
                        label = new Label(j, i + 1, value.toString());
                    } else {
                        label = new Label(j, i + 1, "");
                    }
                    sheet.addCell(label);
                } catch (Exception e) {
                }
            }

        }
    }

    @Override
    public List<Entity> getExportEntity(Map<String, Object> queryMap) {
        return entityDao.getEntityExportInfo(queryMap);
    }

    @Override
    public Integer getExportEntityNum(Map<String, Object> queryMap) {
        return entityDao.getExportEntityNum(queryMap);
    }

    @Override
    public String exportSheetContent(String fileName, String sheetName, List<?> datas) throws WriteException, FileNotFoundException, IOException {
        return exportSheetContent(fileName, sheetName, datas,true);
    }

    @Override
    public String exportSheetContent(String fileName, String sheetName, List<?> datas, boolean isNewFile) throws WriteException, IOException {
        // 生成excel文件
        File rootFolder = new File(EIConstant.EXPORT_ROOT);
        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            rootFolder.mkdir();
        }
        String filePath = EIConstant.EXPORT_ROOT + fileName;
        File generateFile = new File(filePath);
        if (isNewFile) {
            if (generateFile.exists()) {
                generateFile.delete();
            }
        }
        OutputStream out = new FileOutputStream(generateFile,true);
        WritableWorkbook workbook = Workbook.createWorkbook(out);
        
        try {
            exportSheetContent(workbook,sheetName,datas);
            // flush
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            logger.error("export entire excel error", e);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.debug("something wrong with I/O:", e);
            }
        }

        return filePath;
    }

}
