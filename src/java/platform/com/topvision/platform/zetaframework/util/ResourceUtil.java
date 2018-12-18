/***********************************************************************
 * $Id: ResourceUtil.java,v1.0 2013-5-9 上午9:54:10 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.filter.TypeFilter;

import com.topvision.framework.common.PathMatchingResourcePatternResolver;

/**
 * 一种方便的文件定位类，可以在系统中获取文件，流等
 * 举例：
 *    ResourceUtil.getResources("com/topvision/platform/zetaframework/base/resources_zh_CN.properties"
 *    就可以找到只包含这个文件的集合回来。
 *    ResourceUtil.getWebAppResource("admin/admin.jsp");//暂时未实现
 *    找到WebApp下面的文件
 * @author Bravin
 * @created @2013-5-9-上午9:54:10
 *
 */
public class ResourceUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResourceUtil.class);
    private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();
    private final List<TypeFilter> excludeFilters = new LinkedList<TypeFilter>();

    public ResourceUtil() {
    }

    /**
     * 
     * @param excludeFilter
     */
    public void addExcludeFilter(TypeFilter excludeFilter) {
        this.excludeFilters.add(0, excludeFilter);
    }

    /**
     * 
     * @param includeFilter
     */
    public void addIncludeFilter(TypeFilter includeFilter) {
        this.includeFilters.add(includeFilter);
    }

    /**
     * 查找模式匹配的文件
     * TODO 自动根据传入的格式进行处理：
     *   com.topvision....resourse_zh_CN.properties
     *   /com/topvision....resourse_zh_CN.properties
     * @param basePackage
     * @return 
     * @return
     */
    public static Resource[] getResources(String basePackage) {
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + basePackage;
        Resource[] resources = new Resource[] {};
        try {
            resources = resourcePatternResolver.getResources(pattern);
        } catch (IOException e) {
            logger.error("", e);
        }
        return resources;
    }


    /**
     * 
     * @param useDefaultFilters
     */
    public void resetFilters(boolean useDefaultFilters) {
        this.includeFilters.clear();
        this.excludeFilters.clear();
    }

}
