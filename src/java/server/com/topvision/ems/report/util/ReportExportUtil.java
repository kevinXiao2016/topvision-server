/***********************************************************************
 * $Id: ReportExportUtil.java,v1.0 2013-6-20 下午4:02:58 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.util;

import java.io.File;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

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
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.topvision.ems.report.domain.ReportTask;
import com.topvision.platform.SystemConstants;
import com.topvision.report.core.domain.DescrptionModel;

/**
 * @author Bravin
 * @created @2013-6-20-下午4:02:58
 * 
 */
public class ReportExportUtil {
    public static final String REPORTROOTFOLDER = SystemConstants.ROOT_REAL_PATH + "/META-INF/reportRootFolder/";
    public static final DateFormat fileNameFormatter = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 通用的报表路径生成方法
     * 
     * @param reportTask
     * @return
     */
    public static String getExportUrl(ReportTask reportTask, int type) {
        String template = reportTask.getReportCreatorBeanName();
        String task = "task" + reportTask.getTaskId();
        String fileName = reportTask.getTitle() + "-" + fileNameFormatter.format(new Date());
        StringBuilder sb = new StringBuilder();
        File fileCursor = new File(REPORTROOTFOLDER);
        if (!fileCursor.exists() || !fileCursor.isDirectory()) {
            fileCursor.mkdir();
        }
        sb.append(REPORTROOTFOLDER).append(template);
        fileCursor = new File(sb.toString());
        if (!fileCursor.exists() || !fileCursor.isDirectory()) {
            fileCursor.mkdir();
        }
        sb.append(File.separator).append(task);
        fileCursor = new File(sb.toString());
        if (!fileCursor.exists() || !fileCursor.isDirectory()) {
            fileCursor.mkdir();
        }
        sb.append(File.separator).append(fileName);
        switch (type) {
        case 0:
            sb.append(".xls");
            break;
        case 1:
            sb.append(".pdf");
            break;

        case 2:
            sb.append(".jsp");
            break;
        default:
            break;
        }
        return sb.toString();
    }

    /**
     * 导出excel
     * 
     * @param out
     *            文件流
     * @param reportContent
     *            报表内容
     * @param reportCondition
     *            报表查询条件
     * @param queryMap
     *            查询条件
     * @throws Exception
     */
    public static void writeContentToExcel(OutputStream out, JSONObject reportContent, JSONArray reportCondition,
            Map<String, String> queryMap, Boolean needPagination) throws Exception {
        WritableWorkbook workbook = Workbook.createWorkbook(out);
        WritableSheet sheet = workbook.createSheet("Sheet1", 0);
        JSONArray columns = reportContent.getJSONObject(ReportXmlParser.CONTENTHEADER)
                .getJSONArray(ReportXmlParser.COLUMNS);
        int row = 0;

        // 输出报表标题及时间信息
        row = outputTitleAndTime(sheet, reportContent, columns, row);

        // 输出统计条件
        row = outputCondition(sheet, columns, reportCondition, queryMap, row);

        // 输出上部分描述信息
        row = outputTopDescription(sheet, reportContent, columns, row);

        // 输出表头
        row = outputHeader(sheet, reportContent, columns, row);

        // 输出报表内容
        JSONObject contentData = reportContent.getJSONObject("contentData");
        JSONArray summaryList = new JSONArray();
        if (needPagination) {
            row += outputPaginationContent(workbook, row, columns, reportContent);
        } else {
            row += outputContent(sheet, row, columns, contentData, 0, summaryList);
        }

        // 输出下部分描述信息
        row = outputBottomDescription(sheet, reportContent, columns, row);
        // flush
        workbook.write();
        workbook.close();
    }

    /**
     * 输出报表标题及时间信息，并返回当前所属行号
     * 
     * @param sheet
     * @param reportContent
     * @throws Exception
     */
    private static int outputTitleAndTime(WritableSheet sheet, JSONObject reportContent, JSONArray columns, int row)
            throws Exception {
        Label label;
        // 设置每一列的宽度
        for (int i = 0, len = columns.size(); i < len; i++) {
            sheet.setColumnView(i, columns.getJSONObject(i).getInt(ReportXmlParser.WIDTH) / 7);
        }
        // 填入报表标题
        sheet.setRowView(0, 1000);
        WritableCellFormat cellFormat = getCellFormat(16, true, jxl.format.Alignment.CENTRE,
                jxl.format.VerticalAlignment.CENTRE, null);
        label = new Label(0, 0, ReportTaskUtil.getString(reportContent.getString(ReportXmlParser.TITLE), "report"),
                cellFormat);
        sheet.addCell(label);
        sheet.mergeCells(0, 0, columns.size() - 1, 0);
        row++;

        // 填入生成时间
        cellFormat = getCellFormat(13, false, jxl.format.Alignment.RIGHT, null, null);
        label = new Label(0, 1, reportContent.getString("time"), cellFormat);
        sheet.addCell(label);
        sheet.mergeCells(0, 1, columns.size() - 1, 1);
        return ++row;
    }

    /**
     * 输出统计条件，并返回当前所属行号
     * 
     * @param sheet
     * @param columns
     * @param queryMap
     * @return
     * @throws Exception
     */
    private static int outputCondition(WritableSheet sheet, JSONArray columns, JSONArray reportCondition,
            Map<String, String> queryMap, int row) throws Exception {
        Label label;

        // 如果存在统计条件，则输出
        JSONObject curCondition;
        String labelName;
        String curConditionId;
        String curConditionDisplay;
        boolean titleOutput = false;

        WritableCellFormat cellFormat;
        for (int i = 0, len = reportCondition.size(); i < len; i++) {
            // 当前查询条件，及其id和输出id
            curCondition = reportCondition.getJSONObject(i);
            curConditionId = curCondition.getString("id");
            curConditionDisplay = curConditionId + "Display";
            labelName = curCondition.getString("labelName");

            // 查询条件键值及其Display键值同时存在，才表示该条件应当被输出
            if (queryMap.containsKey(curConditionId) && !"".equals(queryMap.get(curConditionId))
                    && queryMap.containsKey(curConditionDisplay) && !"".equals(queryMap.get(curConditionDisplay))) {
                if (!titleOutput) {
                    // 如果标题尚未输出，则输出
                    titleOutput = true;
                    cellFormat = getCellFormat(14, true, jxl.format.Alignment.LEFT, null, null);
                    label = new Label(0, row, ReportTaskUtil.getString("report.condtions", "report"), cellFormat);
                    sheet.addCell(label);
                    sheet.mergeCells(0, row, columns.size() - 1, row);
                    row++;
                }
                cellFormat = getCellFormat(12, false, null, null, true);
                label = new Label(0, row, labelName + ":" + queryMap.get(curConditionDisplay), cellFormat);
                sheet.addCell(label);
                sheet.mergeCells(0, row, columns.size() - 1, row);
                row++;
            }
        }
        return row;
    }

    /**
     * 上半部分描述信息输出
     * 
     * @param sheet
     * @param reportContent
     * @param columns
     * @param row
     * @return
     * @throws Exception
     */
    private static int outputTopDescription(WritableSheet sheet, JSONObject reportContent, JSONArray columns, int row)
            throws Exception {
        Label label;
        if (reportContent.containsKey("description")) {
            // 隔开空行
            WritableCellFormat cellFormat;
            label = new Label(0, row, "");
            sheet.addCell(label);
            sheet.mergeCells(0, row, columns.size() - 1, row);
            row++;

            // 计算各部分描述信息起始列数
            int leftStart = 0;
            int middleStart = columns.size() / 3;
            int rightStart = middleStart + columns.size() / 3;
            // 合并相关行
            sheet.mergeCells(leftStart, row, middleStart - 1, row);
            sheet.mergeCells(middleStart, row, rightStart - 1, row);
            sheet.mergeCells(rightStart, row, columns.size() - 1, row);

            JSONArray descriptions = reportContent.getJSONArray("description");
            JSONObject curDescription;
            for (int i = 0, len = descriptions.size(); i < len; i++) {
                curDescription = descriptions.getJSONObject(i);
                String descText = ReportTaskUtil.getString(curDescription.getString("text"), "report");
                switch (curDescription.getString("position")) {
                case DescrptionModel.TOP_LEFT:
                    cellFormat = getCellFormat(12, true, jxl.format.Alignment.LEFT, null, true);
                    label = new Label(leftStart, row, descText, cellFormat);
                    sheet.addCell(label);
                    break;
                case DescrptionModel.TOP_MIDDLE:
                    cellFormat = getCellFormat(12, true, jxl.format.Alignment.LEFT, null, true);
                    label = new Label(middleStart, row, descText, cellFormat);
                    sheet.addCell(label);
                    break;
                case DescrptionModel.TOP_RIGHT:
                    cellFormat = getCellFormat(12, true, jxl.format.Alignment.RIGHT, null, true);
                    label = new Label(rightStart, row, descText, cellFormat);
                    sheet.addCell(label);
                    break;
                }
            }
            row++;
        }
        return row;
    }

    /**
     * 底部描述信息输出
     * 
     * @param sheet
     * @param reportContent
     * @param columns
     * @param row
     * @return
     * @throws Exception
     */
    private static int outputBottomDescription(WritableSheet sheet, JSONObject reportContent, JSONArray columns,
            int row) throws Exception {
        Label label;
        if (reportContent.containsKey("description")) {
            WritableCellFormat cellFormat = getCellFormat(16, true, null, null, null);
            // 计算各部分描述信息起始列数
            int leftStart = 0;
            int middleStart = columns.size() / 3;
            int rightStart = middleStart + columns.size() / 3;
            // 合并相关行
            sheet.mergeCells(leftStart, row, middleStart - 1, row);
            sheet.mergeCells(middleStart, row, rightStart - 1, row);
            sheet.mergeCells(rightStart, row, columns.size() - 1, row);

            JSONArray descriptions = reportContent.getJSONArray("description");
            JSONObject curDescription;
            for (int i = 0, len = descriptions.size(); i < len; i++) {
                curDescription = descriptions.getJSONObject(i);
                switch (curDescription.getString("position")) {
                case DescrptionModel.BOTTOM_LEFT:
                    cellFormat = getCellFormat(12, true, jxl.format.Alignment.LEFT, null, true);
                    label = new Label(leftStart, row, curDescription.getString("text"), cellFormat);
                    sheet.addCell(label);
                    break;
                case DescrptionModel.BOTTOM_MIDDLE:
                    cellFormat = getCellFormat(12, true, jxl.format.Alignment.LEFT, null, true);
                    label = new Label(middleStart, row, curDescription.getString("text"), cellFormat);
                    sheet.addCell(label);
                    break;
                case DescrptionModel.BOTTOM_RIGHT:
                    cellFormat = getCellFormat(12, true, jxl.format.Alignment.RIGHT, null, true);
                    label = new Label(rightStart, row, curDescription.getString("text"), cellFormat);
                    sheet.addCell(label);
                    break;
                }
            }

            row++;
        }
        return row;
    }

    /**
     * 输出表头
     * 
     * @param sheet
     * @param reportContent
     * @param columns
     * @param row
     * @return
     * @throws Exception
     */
    private static int outputHeader(WritableSheet sheet, JSONObject reportContent, JSONArray columns, int row)
            throws Exception {
        Label label;
        WritableCellFormat columnFormat = getTitleCellFormat();
        // 该报表表头是否为复合表头
        boolean isCombination = reportContent.getBoolean(ReportXmlParser.COMBINATION);
        JSONObject currentCol;
        String currentColName;
        if (isCombination) {
            // 复合表头，需要特殊处理
            String prevComb = "";
            String curComb = "";
            int firstRowCount = 0;
            int secondRowCount = 0;
            int colSpanCount = 0;
            for (int i = 0; i < columns.size(); i++) {
                currentCol = columns.getJSONObject(i);
                prevComb = curComb;
                curComb = currentCol.getString(ReportXmlParser.COMBINATION);
                if (curComb == null || curComb == "") { // 该行没有父级表头
                    // 判断前面是否有需要合并的表头
                    if (prevComb != null && prevComb != "") {
                        // 需要结束表头合并，并放入第一行
                        label = new Label(firstRowCount, row, prevComb, columnFormat);
                        sheet.addCell(label);
                        sheet.mergeCells(firstRowCount, row, firstRowCount + colSpanCount - 1, row);
                        firstRowCount += colSpanCount;
                        colSpanCount = 0;
                    }
                    // 该行没有父级表头
                    currentColName = currentCol.getString("name");
                    label = new Label(firstRowCount++, row, currentColName, columnFormat);
                    sheet.addCell(label);
                    label = new Label(secondRowCount++, row + 1, currentColName, columnFormat);
                    sheet.addCell(label);
                    sheet.mergeCells(firstRowCount - 1, row, secondRowCount - 1, row + 1);
                } else { // 该行有父级表头
                         // 判断前面是否有表头需要合并
                    if (prevComb != null && prevComb != "" && !prevComb.equals(curComb)) {
                        // 将前面的表头合并放入第一行
                        label = new Label(firstRowCount, row, prevComb, columnFormat);
                        sheet.addCell(label);
                        sheet.mergeCells(firstRowCount, row, firstRowCount + colSpanCount - 1, row);
                        firstRowCount += colSpanCount;
                        colSpanCount = 0;
                        prevComb = curComb;
                    }
                    colSpanCount++;
                    // 如果已经是最后一列，则直接输出第一列
                    if (i == columns.size() - 1) {
                        label = new Label(firstRowCount, row, prevComb, columnFormat);
                        sheet.addCell(label);
                        sheet.mergeCells(firstRowCount, row, firstRowCount + colSpanCount - 1, row);
                        colSpanCount = 0;
                    }
                    // 直接加入第二行
                    label = new Label(secondRowCount++, row + 1, currentCol.getString("name"), columnFormat);
                    sheet.addCell(label);
                }
                int width = 0;
                if (currentCol.getInt("excelwidth") != 0) {
                    width = currentCol.getInt("excelwidth");
                } else {
                    width = currentCol.getInt("width");
                }
                sheet.setColumnView(i, width / 7);
            }
            row += 2;
        } else {
            // 单行表头，直接设置每一列宽度
            for (int i = 0; i < columns.size(); i++) {
                JSONObject column = columns.getJSONObject(i);
                int width = 0;
                if (column.getInt("excelwidth") != 0) {
                    width = column.getInt("excelwidth");
                } else {
                    width = column.getInt("width");
                }
                sheet.setColumnView(i, width / 7);
                label = new Label(i, row, column.getString("name"), columnFormat);
                sheet.addCell(label);
            }
            row++;
        }
        return row;
    }

    /**
     * 输出分页报表
     * 
     * @param sheet
     * @param row
     * @param columns
     * @param contentData
     * @return
     * @throws Exception
     * @throws RowsExceededException
     */
    private static int outputPaginationContent(WritableWorkbook workbook, int row, JSONArray columns,
            JSONObject reportContent) throws RowsExceededException, Exception {
        JSONObject contentData = reportContent.getJSONObject("contentData");
        WritableCellFormat contentFormat = getContentCellFormat();
        Label label;
        WritableSheet sheet;
        JSONObject currentChild;
        // 获取其子节点
        JSONArray childrens = contentData.getJSONArray("childrens");
        // 计算应当分几个sheet
        Integer sheetNum = childrens.size() / 60000 + 1, curSheetNum;
        // 前面几个sheet都是满配60000个
        for (int curSheet = 0; curSheet < sheetNum; curSheet++) {
            // 初始化好当前sheet,第一个除外
            if (curSheet == 0) {
                sheet = workbook.getSheet(curSheet);
            } else {
                sheet = workbook.createSheet("Sheet_" + curSheet, curSheet);
                row = 0;
                // 输出表头
                row = outputHeader(sheet, reportContent, columns, row);
            }
            if (curSheet == sheetNum - 1) {
                curSheetNum = childrens.size() % 60000;
            } else {
                curSheetNum = 60000;
            }
            // 填充当前sheet
            for (int i = 0; i < curSheetNum; i++) {
                currentChild = childrens.getJSONObject(curSheet * 60000 + i);
                for (int j = 0; j < columns.size(); j++) {
                    JSONObject column = columns.getJSONObject(j);
                    String contentTxt = currentChild.getString(column.getString("id"));
                    if (contentTxt == null || contentTxt.toLowerCase().equals("null")) {
                        contentTxt = "-";
                    }
                    label = new Label(j, row, contentTxt, contentFormat);
                    sheet.addCell(label);
                }
                row++;
            }
        }
        return row;
    }

    /**
     * 输出报表内容,返回值为内容占用行数
     * 
     * @param sheet
     * @param lineNum
     * @param columns
     * @param reportData
     * @param depth
     * @param summaryList
     * @return
     * @throws Exception
     */
    private static int outputContent(WritableSheet sheet, int lineNum, JSONArray columns, JSONObject reportData,
            int depth, JSONArray summaryList) throws Exception {
        // 该输出总占用行数
        int contentLineNum = 0;
        WritableCellFormat contentFormat = getContentCellFormat();
        WritableCellFormat sumFormat = getSumCellFormat();

        // 判断该数据是否为叶子节点，如果是叶子节点，则直接输出内容，否则输出其叶子节点
        Boolean isLeaf = false;
        if (reportData.containsKey("leaf")) {
            isLeaf = reportData.getBoolean("leaf");
        }
        if (isLeaf == true) {
            // 叶子节点直接输出
            outputLine(sheet, lineNum, columns, reportData, contentFormat, summaryList);
            return 1;
        }
        // 获取其子节点
        JSONArray childrens = reportData.getJSONArray("childrens");
        if (childrens == null || childrens.size() == 0) {
            // 子节点为空则返回
            return 0;
        }
        // 如果该节点有小计，则添加统计信息
        JSONObject nodeSum = reportData.getJSONObject("groups");
        if (nodeSum != null && !nodeSum.isNullObject()) {
            // 初始化改层已经统计过的key
            nodeSum.put("sumedKeys", new JSONArray());
            // 将改层的统计加入summaryList，方便输出时进行统计
            summaryList.add(nodeSum);
        }
        // 输出各子节点内容
        for (int i = 0; i < childrens.size(); i++) {
            // 将该层级的key-value赋予其子节点
            JSONObject currentChild = childrens.getJSONObject(i);
            JSONObject currentChildKeys = new JSONObject();
            if (reportData.containsKey("parentKeyList")) {
                JSONObject parentKeys = reportData.getJSONObject("parentKeyList");
                Iterator<?> it = parentKeys.keys();
                while (it.hasNext()) {
                    String key = it.next().toString();
                    currentChildKeys.put(key, parentKeys.getString(key));
                }
            }
            // 如果当前层级的key不是根root的话，则也将其加入该子节点的keys
            if ("ReportRoot".equals(reportData.getString("key"))) {
                currentChildKeys.put(reportData.getString("key"), reportData.getString("value"));
            }
            int currentLineNum = outputContent(sheet, lineNum + contentLineNum, columns, currentChild, depth + 1,
                    summaryList);
            contentLineNum += currentLineNum;
        }
        // 如果该节点有小计，则输出小计
        boolean summary = false;
        if (nodeSum != null && !nodeSum.isNullObject()) {
            if ((reportData.getString("value") != null && !reportData.getString("value").equals("") && !reportData.getString("value").equals("null"))
                    || (reportData.getString("key") != null && reportData.getString("key").equals("root"))) {
                summary = true;
                outputSummary(sheet, lineNum + contentLineNum, columns,
                        summaryList.getJSONObject(summaryList.size() - 1), sumFormat);
            }
            summaryList.remove(summaryList.size() - 1);
            // outputSummary(columns, summaryList.pop());
        }
        // 合并该depth对应列的行
        if (depth > 0) {
            for (int j = 0; j < columns.size(); j++) {
                JSONObject column = columns.getJSONObject(j);
                int columnDepth = column.getInt("level");
                if (columnDepth == depth) {
                    sheet.mergeCells(j, lineNum, j, lineNum + contentLineNum - 1);
                }
            }
        }
        if (summary) {
            contentLineNum++;
        }
        return contentLineNum;
    }

    /**
     * 输出数据行数据
     * 
     * @param sheet
     * @param lineNum
     * @param columns
     * @param lineData
     * @param contentFormat
     * @param summaryList
     * @throws RowsExceededException
     * @throws WriteException
     */
    private static void outputLine(WritableSheet sheet, int lineNum, JSONArray columns, JSONObject lineData,
            WritableCellFormat contentFormat, JSONArray summaryList) throws RowsExceededException, WriteException {
        Label label;
        for (int i = 0; i < columns.size(); i++) {
            JSONObject column = columns.getJSONObject(i);
            String contentTxt = lineData.getString(column.getString("id"));
            if (column.getBoolean("needI18N")) {
                contentTxt = ReportTaskUtil.getString(contentTxt, "report");
            }
            if (contentTxt == null || contentTxt.toLowerCase().equals("null")) {
                contentTxt = "-";
            }
            label = new Label(i, lineNum, contentTxt, contentFormat);
            sheet.addCell(label);
        }
        // 进行数据统计
        if (summaryList != null && summaryList.size() > 0) {
            for (int i = 0; i < summaryList.size(); i++) {
                // 获取当前统计元素需要统计的元素
                // var currentSummary = summaryList[i],
                // countColumns = currentSummary.groups;
                JSONObject currentSummary = summaryList.getJSONObject(i);
                JSONArray countColumns = currentSummary.getJSONArray("groups");
                JSONArray sumedKeys = currentSummary.getJSONArray("sumedKeys");
                // 判断该行是否已经统计过
                if (sumedKeys.contains(lineData.getString("groupKey"))) {
                    continue;
                }
                sumedKeys.add(lineData.getString("groupKey"));
                // 进行统计
                for (int j = 0; j < countColumns.size(); j++) {
                    JSONObject curCol = countColumns.getJSONObject(j);
                    String currentColName = curCol.getString("groupColumn");
                    String curColCompute = curCol.getString("compute").toLowerCase();
                    // 如果该列还没进行统计，初始化为0
                    if (!currentSummary.containsKey(currentColName)) {
                        currentSummary.put(currentColName, 0);
                    }
                    // 进行添加
                    if (lineData.containsKey(currentColName)
                            && !"null".equals(lineData.get(currentColName).toString().toLowerCase())) {
                        if (curColCompute.equals("avg")) {
                            if (!curCol.containsKey("count")) {
                                curCol.put("count", 0);
                            }
                            double caclSum = (currentSummary.getDouble(currentColName) * curCol.getInt("count")
                                    + lineData.getDouble(currentColName)) / (curCol.getInt("count") + 1);
                            currentSummary.put(currentColName, caclSum);
                            curCol.put("count", curCol.getInt("count") + 1);
                        } else if (curColCompute.equals("rowcount")) {
                            currentSummary.put(currentColName, currentSummary.getInt(currentColName) + 1);
                        } else {
                            if (lineData.get(currentColName).toString().indexOf("\\.") == -1) {
                                currentSummary.put(currentColName,
                                        currentSummary.getInt(currentColName) + lineData.getInt(currentColName));
                            } else if (lineData.get(currentColName) instanceof Double) {
                                currentSummary.put(currentColName,
                                        currentSummary.getDouble(currentColName) + lineData.getDouble(currentColName));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 输出统计行数据
     * 
     * @param sheet
     * @param lineNum
     * @param columns
     * @param nodeSum
     * @param sumFormat
     * @throws RowsExceededException
     * @throws WriteException
     */
    private static void outputSummary(WritableSheet sheet, int lineNum, JSONArray columns, JSONObject nodeSum,
            WritableCellFormat sumFormat) throws RowsExceededException, WriteException {
        Label label;
        DecimalFormat df = new DecimalFormat("0.0");
        // 填充该summary对应填入的总计列及对应文字
        nodeSum.put(nodeSum.getString("relative"),
                ReportTaskUtil.getString(nodeSum.getString("displayName"), "report"));
        // 遍历每一列，找出属于当前统计的列，进行输出
        for (int i = 0; i < columns.size(); i++) {
            JSONObject curCol = columns.getJSONObject(i);
            String curColName = curCol.getString("id");
            String txt = "-";
            if (nodeSum.containsKey(curColName)) {
                Object value = nodeSum.get(curColName);
                if (value instanceof Double) {
                    txt = df.format(value);
                } else {
                    txt = value.toString();
                }
            }
            label = new Label(i, lineNum, txt, sumFormat);
            sheet.addCell(label);
        }
    }

    private static WritableCellFormat getTitleCellFormat() throws Exception {
        WritableCellFormat titleCellFormat = getCellFormat(11, true, jxl.format.Alignment.CENTRE,
                jxl.format.VerticalAlignment.CENTRE, true);
        titleCellFormat.setBackground(Colour.IVORY);
        titleCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.GRAY_25);
        return titleCellFormat;
    }

    private static WritableCellFormat getContentCellFormat() throws Exception {
        WritableCellFormat contentCellFormat = getCellFormat(11, false, jxl.format.Alignment.CENTRE,
                jxl.format.VerticalAlignment.CENTRE, true);
        return contentCellFormat;
    }

    private static WritableCellFormat getSumCellFormat() throws WriteException {
        WritableCellFormat titleCellFormat = new WritableCellFormat();
        titleCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
        titleCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        titleCellFormat.setBackground(Colour.GRAY_25);
        return titleCellFormat;
    }

    private static WritableCellFormat getCellFormat(int fontSize, boolean bold, jxl.format.Alignment alignment,
            jxl.format.VerticalAlignment verticalAlignment, Boolean wrap) throws Exception {
        WritableFont font;
        if (bold) {
            font = new WritableFont(WritableFont.ARIAL, fontSize, WritableFont.BOLD);
        } else {
            font = new WritableFont(WritableFont.ARIAL, fontSize, WritableFont.NO_BOLD);
        }
        WritableCellFormat cellFormat = new WritableCellFormat(font);
        if (alignment != null) {
            cellFormat.setAlignment(alignment);
        }
        if (verticalAlignment != null) {
            cellFormat.setVerticalAlignment(verticalAlignment);
        }
        if (wrap != null && wrap == true) {
            cellFormat.setWrap(true);
        }
        return cellFormat;
    }
}