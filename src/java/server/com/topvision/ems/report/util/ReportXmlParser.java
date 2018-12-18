/***********************************************************************
 * $Id: ReportXmlParser.java,v1.0 2014-6-16 下午1:50:41 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.sun.org.apache.xerces.internal.impl.Constants;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.topvision.ems.report.domain.Group;
import com.topvision.ems.report.domain.Report;
import com.topvision.ems.report.domain.ReportColumnReferences;
import com.topvision.ems.report.domain.ReportCondition;
import com.topvision.ems.report.domain.ReportGroup;
import com.topvision.ems.report.domain.ReportNode;
import com.topvision.ems.report.domain.ReportStructure;
import com.topvision.ems.report.exception.ReportException;

/**
 * 
 * @author Rod John
 * @created @2014-6-16-下午1:50:41
 * 
 */
public class ReportXmlParser {
    public static final String ID = "id";
    public static final String TYPE = "type";
    public static final String RESOURCES = "resources";
    public static final String RESOURCE = "resource";
    public static final String LABELNAME = "labelName";
    public static final String PLACEHOLDER = "placeHolder";
    public static final String PATH = "path";
    public static final String MODULE = "module";
    public static final String PROJECT = "project";
    public static final String LAZYLOAD = "lazyLoad";
    public static final String TITLE = "title";
    public static final String COLUMNS = "columns";
    public static final String CONTENTHEADER = "contentHeader";
    public static final String COLUMN = "column";
    public static final String SKELETON = "skeleton";
    public static final String NAMES = "names";
    public static final String NAME = "name";
    public static final String WIDTH = "width";
    public static final String EXCELWIDTH = "excelwidth";
    public static final String NEEDI18N = "needI18N";
    public static final String RENDER = "render";
    public static final String NOWRAP = "nowrap";
    public static final String NODE = "node";
    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String GROUPS = "groups";
    public static final String GROUP = "group";
    public static final String GROUPKEY = "groupKey";
    public static final String RELATIVE = "relative";
    public static final String CHILDRENS = "childrens";

    public static final String CONDITIONS = "conditions";
    public static final String CONDITION = "condition";
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String SORTCOLUMNS = "sortColumns";
    public static final String DISPLAYCOLUMNS = "displayColumns";
    public static final String DEVICETYPE = "deviceType";
    public static final String DEVICEIP = "deviceIp";
    public static final String ADMINSTATUS = "adminStatus";
    public static final String OPERSTATUS = "operStatus";
    public static final String DATAINDEX = "dataIndex";
    public static final String DISPLAYNAME = "displayName";
    public static final String SORTED = "sorted";
    public static final String DISPLAYED = "displayed";
    public static final String TRUESTRING = "true";
    public static final String FLASESTRING = "false";
    public static final String LINK = "link";
    public static final String LINKNAME = "linkname";
    public static final String LINKREPORT = "linkReport";
    public static final String TOPLEVEL = "topLevel";
    public static final String COMBINATION = "combination";
    public static final String FOLDER = "folder";
    public static final String COLOR = "color";
    public static final String COMPUTE = "compute";
    public static final String CASCADE = "cascade";
    public static final String PAGINATION = "pagination";

    public static void main(String[] args) throws Exception {
        Resource resource = new FileSystemResource(new File("D:\\reportcm.xml"));
        parser(resource);
    }

    /**
     * PARSER
     * 
     * @param xmlFile
     * @return
     * @throws Exception
     */
    public static Report parser(Resource resource) throws Exception {
        SAXReader saxReader = new SAXReader();
        // Config DTD validation false
        saxReader.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.LOAD_EXTERNAL_DTD_FEATURE, false);
        Document document = saxReader.read(resource.getInputStream());

        // 获取根元素
        Element root = document.getRootElement();
        // Handle Report Info
        Report report = parserXmlReport(root);
        // Handle Report Skeleton
        Element skeleton = root.element(SKELETON);
        ReportStructure reportStructure = parserXmlReportStructure(report, skeleton);
        report.setReportStructure(reportStructure);
        // Handle Report Column
        handleReportColumnReferences(report);
        // Handle Report Conditions
        Element condition = root.element(CONDITIONS);
        List<ReportCondition> reportConditions = parserXmlReportCondition(condition);
        report.setReportConditions(reportConditions);
        return report;
    }

    /**
     * 解析XML REPORT
     * 
     * @param root
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Report parserXmlReport(Element root) {
        Report report = new Report();
        // Report Id
        report.setId(root.attributeValue(ID));
        // Report path
        Element path = root.element(PATH);
        report.setPath(path.getTextTrim());
        // Report module
        Element module = root.element(MODULE);
        report.setModule(module.getTextTrim());
        // Report Project
        Element project = root.element(PROJECT);
        if (project != null) {
            report.setProject(project.getTextTrim());
        }
        // Report LazyLoad
        Element lazyLoad = root.element(LAZYLOAD);
        report.setLazyLoad(lazyLoad.getTextTrim());
        // Report With Package
        Element withPackages = root.element(RESOURCES);
        if (withPackages != null) {
            List<Element> withPackage = withPackages.elements(RESOURCE);
            for (Element tmp : withPackage) {
                report.addWithPackages(tmp.getTextTrim());
            }
        }
        // Report Title
        Element title = root.element(TITLE);
        report.setTitle(title.getTextTrim());
        // Report Combination
        Element combination = root.element(COMBINATION);
        if (combination != null) {
            report.setCombination(Boolean.parseBoolean(combination.getTextTrim()));
        }
        // Report Level
        Element topLevel = root.element(TOPLEVEL);
        if (topLevel != null) {
            report.setTopLevel(Boolean.parseBoolean(topLevel.getTextTrim()));
        }
        // Report Columns
        Element columns = root.element(COLUMNS);
        List<Element> columnList = columns.elements(COLUMN);
        for (Element column : columnList) {
            ReportColumnReferences columnReferences = new ReportColumnReferences();
            columnReferences.setId(column.attributeValue(ID));
            columnReferences.setName(column.elementTextTrim(NAME));
            columnReferences.setWidth(Integer.parseInt(column.elementTextTrim(WIDTH)));
            if (column.elementTextTrim(EXCELWIDTH) == null) {
                columnReferences.setExcelwidth(0);
            } else {
                columnReferences.setExcelwidth(Integer.parseInt(column.elementTextTrim(EXCELWIDTH)));
            }
            columnReferences.setLinkId(column.elementTextTrim(LINK));
            columnReferences.setLinkName(column.elementTextTrim(LINKNAME));
            columnReferences.setNowrap(Boolean.valueOf(column.attributeValue(NOWRAP)));
            columnReferences.setNeedI18N(Boolean.valueOf(column.attributeValue(NEEDI18N)));
            columnReferences.setRender(column.attributeValue(RENDER));
            columnReferences.setCombination(column.elementTextTrim(COMBINATION));
            columnReferences.setColor(column.elementTextTrim(COLOR));
            report.addColumnReference(columnReferences);
        }
        // Report pagination
        Element pagination = root.element(PAGINATION);
        if (pagination != null) {
            try {
                report.setPagination(Integer.valueOf(pagination.getTextTrim()));
            } catch (Exception e) {
            }
        }
        return report;
    }

    /**
     * 解析XML REPORT SKELETON
     * 
     * @param report
     * @param skeleton
     * @return
     */
    @SuppressWarnings("unchecked")
    private static ReportStructure parserXmlReportStructure(Report report, Element skeleton) {
        ReportStructure reportStructure = new ReportStructure();
        Element node = skeleton.element(NODE);
        if (node != null) {
            reportStructure.setNodeKey(node.attributeValue(KEY));
            Element groups = node.element(GROUPS);
            if (groups != null) {
                ReportGroup reportGroup = new ReportGroup();
                if (groups.attributeValue("link") != null) {
                    reportGroup.setLink(Boolean.valueOf(groups.attributeValue("link")));
                }
                reportGroup.setRelative(groups.attributeValue(RELATIVE));
                reportGroup.setGroupKey(groups.attributeValue(GROUPKEY));
                reportGroup.setDisplayName(report.globalization(groups.attributeValue(DISPLAYNAME)));
                List<Element> group = groups.elements(GROUP);
                for (Element element : group) {
                    Group tmp = null;
                    if (element.attributeValue(COMPUTE) != null) {
                        tmp = new Group(element.attributeValue(COMPUTE), element.getTextTrim());
                    } else {
                        tmp = new Group(element.getTextTrim());
                    }
                    reportGroup.insertGroup(tmp);
                }
                reportStructure.setReportGroup(reportGroup);
            }
            Element names = node.element(NAMES);
            List<Element> name = names.elements(NAME);
            List<ReportColumnReferences> references = report.getColumnReferences();
            OUTERLOOP: for (Element element : name) {
                String elementName = element.getTextTrim();
                for (ReportColumnReferences tmp : references) {
                    if (tmp.getId().equals(elementName)) {
                        reportStructure.getNodeMaps().put(elementName, tmp);
                        continue OUTERLOOP;
                    }
                }
                throw new ReportException("reportStructure column " + elementName
                        + " has no definition in report column");
            }
            reportStructure.setChildreNode(parserXmlReportStructure(report, node));
        } else {
            return null;
        }
        return reportStructure;
    }

    /**
     * PARSER REPORT NODE TO MAP
     * 
     * @param reportNode
     * @return
     */
    public static Map<String, Object> handleJsonObject(ReportNode reportNode) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> childList = new ArrayList<>();

        map.put(KEY, reportNode.getNodeKey());
        map.put(VALUE, reportNode.getNodeValue());
        map.put(GROUPS, reportNode.getReportGroup());
        /*
         * for (Entry<String, String> entry : reportNode.getNodeMaps().entrySet()) {
         * map.put(entry.getKey(), entry.getValue()); }
         */

        map.put(CHILDRENS, childList);
        // If ReportDao returns size 0 result, then the reportNode equals rootNode
        if (reportNode.getNodeKey().equals(ReportNode.ROOTKEY) && reportNode.getChildrenNode() == null) {
            return map;
        }
        if (reportNode.getChildrenNode() != null) {
            for (ReportNode nextNode : reportNode.getChildrenNode()) {
                childList.add(handleJsonObject(nextNode));
            }
        } else {
            map.put("leaf", true);
            for (Entry<String, String> entry : reportNode.getNodeMaps().entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }
            while (true) {
                ReportNode parentNode = reportNode.getParentNode();
                map.put(reportNode.getNodeKey(), reportNode.getNodeValue());
                for (Entry<String, String> entry : parentNode.getNodeMaps().entrySet()) {
                    map.put(entry.getKey(), entry.getValue());
                }
                if (parentNode.getReportGroup() != null && parentNode.getReportGroup().getGroupKey() != null) {
                    String groupKey = parentNode.getReportGroup().getGroupKey();
                    map.put(GROUPKEY, map.get(groupKey));
                }
                if (parentNode.getNodeKey().equals(ReportNode.ROOTKEY)) {
                    break;
                }
                reportNode = parentNode;
            }
        }
        return map;
    }

    /**
     * PARSER REPORT COLUMNS
     * 
     * @param report
     */
    private static void handleReportColumnReferences(Report report) {
        int level = 1;
        ReportStructure structure = report.getReportStructure();
        while (structure != null) {
            for (ReportColumnReferences reference : report.getColumnReferences()) {
                if (structure.getReportGroup() != null) {
                    if (reference.getId().equals(structure.getReportGroup().getRelative())) {
                        reference.setGroup(structure.getReportGroup());
                    }
                }
                if (structure.getNodeMaps().containsKey(reference.getId())) {
                    reference.setLevel(level);
                }
            }
            structure = structure.getChildreNode();
            level++;
        }
    }

    @SuppressWarnings({ "unchecked" })
    private static List<ReportCondition> parserXmlReportCondition(Element condition) {
        if (condition != null) {
            List<Element> elements = condition.elements();
            if (elements == null || elements.size() == 0) {
                return null;
            }
            List<ReportCondition> reportConditions = new ArrayList<ReportCondition>();
            // 遍历所有条件，进行记录
            Element element;
            for (int i = 0, len = elements.size(); i < len; i++) {
                element = elements.get(i);
                // 记录每一个查询条件的类型，id，值
                String type = element.attributeValue(TYPE);
                String id = element.attributeValue(ID);
                String labelName = element.attributeValue(LABELNAME);
                String placeHolder = element.attributeValue(PLACEHOLDER);
                String value = element.getTextTrim();
                ReportCondition reportCondition = new ReportCondition(type, id, labelName, value);
                if(placeHolder!=null) {
                    reportCondition.setPlaceHolder(placeHolder);
                }
                reportConditions.add(reportCondition);
            }

            return reportConditions;
        }
        return null;
    }

}
