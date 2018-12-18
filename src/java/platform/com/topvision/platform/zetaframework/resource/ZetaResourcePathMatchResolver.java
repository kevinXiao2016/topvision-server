/***********************************************************************
 * $Id: ZetaResourcePathMatchResolver.java,v1.0 2013-3-26 上午8:43:10 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.resource;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import com.topvision.platform.zetaframework.exception.ResourceModuleNotFoundException;


/**
 * 
 * 解析资源文件路径 我们假定每一个资源文件的名称是唯一确定的，那么就一定可以在路径中唯一确定这个资源出来，写配置文件的方式对于开发人员来讲有点不友好，所以选择按路径查找 约定如下： 1.
 * 所有的资源文件路径的最后一个目录的目录名必须唯一
 * 
 * @author Bravin
 * @created @2013-3-26-上午8:43:10
 * 
 */
public class ZetaResourcePathMatchResolver {
    private static Logger logger = LoggerFactory.getLogger(ZetaResourcePathMatchResolver.class);
    // 约定都以大写作为alias，获取的时候不区分大小写
    private static final String BASIC_RESOURCE_MODULE_UPPERCASE = "BASE";
    private static final String DEFAULT_RESOURCE_MARKER = "resources";
    public static final String NON_STANDARD_RESOURCE_FILE_MARKER = "$";
    private static Map<String, String> resourceMap;
    private boolean resourcePathCollected;
    private List<Resource> resourceList;

    public void initialize() {
        resourceMap = new HashMap<String, String>();
    }

    /**
     * 解析整个系统，挖掘系统的 资源并解析出其别名
     */
    public void doResourceLocationScan() {
        logger.info("retrieve Resource Started! ");
        for (Resource resouce : resourceList) {
            try {
                File file = resouce.getFile();
                String alias = retrieveResourceAlias(file);
                //@TODO by bravin @20131021目前没有来得及做业务拆分，所以operationLog相关的会对现有的功能造成影响，目前先去掉这些功能，待业务拆分完毕再做处理
                if (alias == null) {
                    continue;
                }
                String absolutePath = retrieveResourcePath(file, alias);
                // 不允许相同别名的资源
                if (resourceMap.containsKey(alias)) {
                    throw new IllegalArgumentException(
                            "Resource module name/alias already existed! please alter the module!");
                }
                resourceMap.put(alias, absolutePath);
            } catch (Exception e) {
            }
        }
        logger.info("retrieve Resource finished!. ");
        resourcePathCollected = true;
    }

    /**
     * 获取资源的 相对路径： 如： com.topvision.platform 不保留文件的名称的目的是方便分析继承关系时好处理 由于SystemConstancets属于
     * platform包下，而我们这个功能不排除会移到 framework下，所以这里不适宜用 SystemConstants.WEB_INF_REAL_PATH截取字符串
     * 
     * @param file
     * @param alias 
     * @return
     */
    private String retrieveResourcePath(File file, String alias) {
        // 在取资源别名的时候就已经做了文件不能为目录的判断
        String path = file.getAbsolutePath();
        String webAppPathMatcher = String.format("webapp%sWEB-INF%sclasses%s", File.separator, File.separator,
                File.separator);
        int startIndex = path.indexOf(webAppPathMatcher) + webAppPathMatcher.length();
        path = path.substring(startIndex);
        int fileNameIndex = 0;
        /** 如果是以  /**\/ALIAS_${language}.properties为格式的资源，则在path后面补一个 $标志 */
        boolean isStandardResource = true;
        if(file.getName().contains(DEFAULT_RESOURCE_MARKER)){
            fileNameIndex = path.lastIndexOf(DEFAULT_RESOURCE_MARKER);
        }else {
            fileNameIndex =  path.toUpperCase().lastIndexOf(alias);
            isStandardResource = false;
            //fileNameIndex = path.length() - RESOURCE_FILE_NAME_RAIL_LENGTH ;
        }
        // /最后一个 '/' 符号去掉，只保留最原始的
        String absolutePath = path.substring(0, fileNameIndex-1);
        String calcPath =  StringUtils.replace(absolutePath, File.separator, ".");
        return isStandardResource?calcPath : calcPath.concat(NON_STANDARD_RESOURCE_FILE_MARKER); 
    }

    /**
     * 从一个文件中解析出其模块名称
     * modified by bravin@20131021增加一种资源别名的表示方法：
     *    /#{path}/ALIAS_#{language}.properties -> ALIAS
     *    比如：
     *      oltvlan_zh_CN.properties 这个资源用 OLTVLAN这个别名表示
     * 
     * @param file
     * @return
     */
    private String retrieveResourceAlias(File file) {
        if (file.isDirectory()) {
            throw new IllegalArgumentException("Resource File can not be a directory!");
        }
        String fileName = file.getName();
        if (fileName.startsWith("resources_")) {
            String path = file.getAbsolutePath();
            // Modify by Victor@20131016解决跨平台的问题，把windows的目录\\改为平台相关的File.separatorChar
            int fromIndex = path.lastIndexOf(File.separatorChar);
            int aliasStartIndex = path.lastIndexOf(File.separatorChar, fromIndex - 1);
            // 统一转换为大写方便交互
            return path.substring(aliasStartIndex + 1, fromIndex).toUpperCase();
        } else if (!fileName.contains("operationLog")) {
            return fileName.substring(0, fileName.indexOf("_")).toUpperCase();
        }
        return null;
    }

    /**
     * 解析出一个 资源别名所属的所有资源的路径
     * 
     * @param alias
     * @return
     */
    public Stack<String> getModuleResourcePathCollection(String alias) {
        if (!resourcePathCollected) {
            doResourceLocationScan();
        }
        return retrieveModuleExtendRelation(alias);
    }

    /**
     * 每一个模块只会取用一次，所以这里采取的策略是，每次取alias的资源关系时都从 资源集合中遍历
     * 
     * @param alias
     * @return
     */
    private Stack<String> retrieveModuleExtendRelation(String alias) {
        String modulePath = resourceMap.get(alias);
        String basePath = resourceMap.get(BASIC_RESOURCE_MODULE_UPPERCASE);
        Stack<String> relations = new Stack<String>();
        if (modulePath == null) {
            throw new ResourceModuleNotFoundException("can not find a  corresponding resource for module : " + alias);
        }
        Iterator<String> paths = resourceMap.values().iterator();
        while (paths.hasNext()) {
            String path = paths.next();
            //.是目录分割符
            if (modulePath.startsWith(path) && !modulePath.equals(path)) {
                relations.push(path);
            }
        }
        Collections.sort(relations, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                if (o1.length() > o2.length()) {
                    return 0;
                } else {
                    return 1;
                }
            }

        });
        //每次都把基本的资源文件打入到module中
        relations.add(0,basePath);
        relations.add(modulePath);
        return relations;
    }

    /**
     * @return the resourceList
     */
    public Resource[] getResourceList() {
        return (Resource[]) this.resourceList.toArray();
    }

    /**
     * @param resourceList
     *            the resourceList to set
     */
    public void setResourceList(Resource resource) {
        this.resourceList.add(resource);
    }

    /**
     * @param resourceList
     *            the resourceList to set
     */
    @SuppressWarnings("unchecked")
    public void setResourceList(Resource[] resourceList) {
        this.resourceList = Arrays.asList(resourceList);
    }

}
