/***********************************************************************
 * $Id: CpuAndMemServiceImpl.java,v1.0 2015-6-24 上午11:02:06 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.template.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.topvision.ems.facade.domain.CpuAndMemData;
import com.topvision.ems.template.service.CpuAndMemService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;

/**
 * @author flack
 * @created @2015-6-24-上午11:02:06
 *
 */
public class CpuAndMemServiceImpl extends BaseService implements CpuAndMemService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Resource[] configXmlLocations;

    private EntityTypeService entityTypeService;

    @Override
    public Map<Long, CpuAndMemData> getCpuAndMemConfig(String moduleName) {
        // 包含配置文件中类型的所有子类型的对应数据
        Map<Long, CpuAndMemData> allTypeMap = new ConcurrentHashMap<Long, CpuAndMemData>();
        // 解析指定配置文件得到的结果
        Map<Long, CpuAndMemData> typeMap = parseXmlData(moduleName);
        for (Entry<Long, CpuAndMemData> entry : typeMap.entrySet()) {
            Long type = entry.getKey();
            CpuAndMemData data = entry.getValue();
            List<Long> typeIdList = entityTypeService.getSubTypeIdList(type);
            // Add by Victor@20160728因为license控制，有可能typeIdList为空
            if (typeIdList == null) {
                continue;
            }
            for (Long typeId : typeIdList) {
                allTypeMap.put(typeId, data);
            }
        }
        allTypeMap.putAll(typeMap);
        return allTypeMap;
    }

    @Override
    public CpuAndMemData getCpuAndMemData(String moduleName, Long deviceTypeId) {
        // 解析指定配置文件得到的结果
        Map<Long, CpuAndMemData> typeMap = parseXmlData(moduleName);
        CpuAndMemData data = typeMap.get(deviceTypeId);
        if (data != null) {
            return data;
        } else {
            List<Long> parentTypes = entityTypeService.getCategoryTypeByTypeId(deviceTypeId);
            for (Long type : parentTypes) {
                data = typeMap.get(type);
                if (data != null) {
                    return data;
                }
            }
        }
        return null;
    }

    @Override
    public Boolean isTypeContained(String moduleName, Long deviceTypeId) {
        Set<String> typeSet = parseXmlTypes(moduleName);
        if (typeSet.contains(String.valueOf(deviceTypeId))) {
            return true;
        } else {
            List<Long> parentTypes = entityTypeService.getCategoryTypeByTypeId(deviceTypeId);
            for (Long type : parentTypes) {
                if (typeSet.contains(String.valueOf(type))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取配置文件中的设备类型
     * 
     * @param moduleName
     * @return
     */
    private Set<String> parseXmlTypes(String moduleName) {
        Set<String> typeSet = new HashSet<String>();
        try {
            // 将xml文档转换为Document的对象
            Document document = getXmlDocument(moduleName);
            if (document != null) {
                List<Node> typeNodes = document.selectNodes("/devices/device/typeId");
                for (Node element : typeNodes) {
                    typeSet.add(element.getText());
                }
            }
        } catch (Throwable e) {
            logger.error("Test type contained in xml file failed:{}", e);
        }
        return typeSet;
    }

    /**
     * 对配置文件进行解析
     * 
     * @param moduleName
     * @return
     */
    private Map<Long, CpuAndMemData> parseXmlData(String moduleName) {
        // 用于存储解析结果,可能在多线程情况下使用,故使用同步map
        Map<Long, CpuAndMemData> typeMap = new ConcurrentHashMap<Long, CpuAndMemData>();
        // 将moduleName中指定的xml文档转换为Document的对象
        Document document = getXmlDocument(moduleName);
        if (document != null) {
            // 获取文档的根元素
            Element root = document.getRootElement();
            // 遍历当前元素(在此是根元素)的子元素
            Iterator<Element> rootItera = root.elementIterator();
            while (rootItera.hasNext()) {
                Element typeElement = rootItera.next();
                // 读取typeId
                String typeId = typeElement.element("typeId").getTextTrim();
                // 用于存储解析完成后的采集结点和计算算法
                CpuAndMemData resultData = new CpuAndMemData();
                // 用于存储解析的采集结点结果
                Map<String, String> nodeMap = new HashMap<String, String>();
                // 用于存储解析的计算算法结果
                Map<String, String> resultMap = new HashMap<String, String>();
                // 读取采集nodes
                Element nodes = typeElement.element("nodes");
                Iterator<Element> nodeIter = nodes.elementIterator();
                // 遍历node
                while (nodeIter.hasNext()) {
                    Element nodeElement = (Element) nodeIter.next();
                    String id = nodeElement.attributeValue("id");
                    String oid = nodeElement.getTextTrim();
                    nodeMap.put(id, oid);
                    logger.debug(typeId + "_" + id + "_" + oid);
                }
                // 保存结点解析结果
                resultData.setNodeMap(nodeMap);
                // 读取results
                Element results = typeElement.element("results");
                Iterator<Element> resultIter = results.elementIterator();
                // 遍历result
                while (resultIter.hasNext()) {
                    Element resultEle = resultIter.next();
                    String id = resultEle.attributeValue("id");
                    String alg = resultEle.getTextTrim();
                    resultMap.put(id, alg);
                    logger.debug(typeId + "_" + id + "_" + alg);
                }
                // 保存计算算法解析结果
                resultData.setResultMap(resultMap);
                typeMap.put(Long.parseLong(typeId), resultData);
            }
        }
        return typeMap;
    }

    /**
     * 获取指定xml文档的Document对象,xml文件必须在configXmlLocations中可以找到
     * 
     * @param moduleName
     *            模块名称,用于定位配置文件
     * @return Document对象
     */
    private Document getXmlDocument(String moduleName) {
        // 产生一个解析器对象
        SAXReader reader = new SAXReader();
        Document document = null;
        for (Resource resource : configXmlLocations) {
            try {
                if (resource.getURL().getPath().contains(moduleName)) {
                    document = reader.read(resource.getInputStream());
                    break;
                }
            } catch (Exception e) {
                logger.error("Get Module[{}] cpuAndMem.xml failed: {}", moduleName, e);
            }
        }
        return document;
    }

    public Resource[] getConfigXmlLocations() {
        return configXmlLocations;
    }

    public void setConfigXmlLocations(Resource[] configXmlLocations) {
        this.configXmlLocations = configXmlLocations;
    }

    public EntityTypeService getEntityTypeService() {
        return entityTypeService;
    }

    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }

}
