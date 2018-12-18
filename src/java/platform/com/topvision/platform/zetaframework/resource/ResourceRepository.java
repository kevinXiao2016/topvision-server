/***********************************************************************
 * $Id: WareHouse.java,v1.0 2013-3-25 下午7:49:00 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.resource;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bravin
 * @created @2013-3-25-下午7:49:00
 *
 */
public class ResourceRepository {
    private static Logger logger = LoggerFactory.getLogger(ResourceRepository.class);
    private static Map<String, ResourceModule> resourceModules;
    private static ZetaResourceLoader zetaResourceLoader;

    /**
     * 单例的对象
     */
    public void initialize() {
        resourceModules = new HashMap<String, ResourceModule>();
    }

    /**
     * 获取一个Module，如果module不存在就创建，如果其language对应的资源堆栈没有加载进入内存则进行加载
     * 存储以及获取的时候不区分大小写，统一以大写交流
     * @param module
     * @param language
     * @return
     */
    public static synchronized ResourceModule getModule(String module) {
        module = module.toUpperCase();
        ResourceModule rm = resourceModules.get(module);
        if (rm == null) {
            rm = new ResourceModule(module);
            //加载后将 resourceModule存储起来
            resourceModules.put(module, rm);
            try {
                load(module, rm);
            } catch (Exception e) {
                logger.error("load module error :{}", e);
            }
        }
        return rm;
    }

    /**
     * 操作日志通过路径获取国际化
     * @param modulePath
     * @return
     */
    public static ResourceModule getOperationLogModule(String modulePath) {
        ResourceModule rm = resourceModules.get(modulePath);
        if (rm == null) {
            rm = new ResourceModule(modulePath);
            //直接将路径加入到资源堆栈中
            rm.pushPath(modulePath);
            //加载后将 resourceModule存储起来
            resourceModules.put(modulePath, rm);
        }
        return rm;
    }

    /**
     * 加载一个模块进入WareHourse中来被管理
     * @param module
     * @param rm 
     * @param language 
     * @return
     */
    private static void load(String module, ResourceModule rm) {
        zetaResourceLoader.loadModule(module, rm);
    }

    /**
     * @return the resourceModules
     */
    public static Map<String, ResourceModule> getResourceModules() {
        return resourceModules;
    }

    /**
     * @param resourceModules the resourceModules to set
     */
    public static void setResourceModules(Map<String, ResourceModule> resourceModules) {
        ResourceRepository.resourceModules = resourceModules;
    }

    /**
     * @return the zetaResourceLoader
     */
    public ZetaResourceLoader getZetaResourceLoader() {
        return zetaResourceLoader;
    }

    /**
     * @param zetaResourceLoader the zetaResourceLoader to set
     */
    @SuppressWarnings("static-access")
    public void setZetaResourceLoader(ZetaResourceLoader zetaResourceLoader) {
        this.zetaResourceLoader = zetaResourceLoader;
    }

}
