/***********************************************************************
 * $Id: ResourceModule.java,v1.0 2013-3-25 下午7:41:40 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.struts2.ServletActionContext;

import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.UserContext;

/**
 * 每一个模块就对应于一个ResourceModule
 * @author Bravin
 * @created @2013-3-25-下午7:41:40
 *
 */
public class ResourceModule {
    /**
     * 模块
     */
    private String module;
    /**
     * 语言-Mapper 的键值对
     * {
     *  zh_CN : MapperCN
     *  en_US : MapperUS
     * }
     */
    private Map<String, ResourceMapper> mapItems;

    /**
     * 资源文件Bundle的路径队列
     */
    private Stack<String> extendedPaths;

    /**
     * 代表这个资源文件的路径，不包含继承链
     */
    private String path;

    /**
     * 构造器
     */
    public ResourceModule() {
        extendedPaths = new Stack<String>();
        mapItems = new HashMap<String, ResourceMapper>();
    }

    /**
     * 构造器,module既可以是别名，也可以是路径
     */
    public ResourceModule(String module) {
        this();
        this.module = module;
    }

    /**
     * 加入一个资源路径
     * @param path
     */
    public void pushPath(String path) {
        extendedPaths.push(path);
    }

    /**
     * 根据传入的语言得到其国际化资源值
     * @param key
     * @param language
     * @return
     */
    public String getString(String key, String language) {
        if (language == null) {
            return getString(key);
        }
        ResourceMapper mapper = getMapper(language);
        return mapper.getString(key);
    }

    /**
     * 自动选择合适的语言找出国际化KEY
     * @param key
     * @return
     */
    public String getString(String key) {
        String language = SystemConstants.getInstance().getStringParam("language", "zh_CN");
        UserContext uc = null;
        try {
            uc = (UserContext) ServletActionContext.getRequest().getSession().getAttribute(UserContext.KEY);
        } catch (Exception e) {
        }
        if (uc != null) {
            //@MARK by bravin:有的用户可能没有设置语言，返回结果就是 null
            String uclanguage = uc.getUser().getLanguage();
            if (uclanguage != null) {
                language = uclanguage;
            }
        }
        ResourceMapper mapper = getMapper(language);
        return mapper.getString(key);
    }

    /**
     * 测试某种语言是否已经加载进入内存了
     * @param language
     * @return
     */
    public boolean containsLanguage(String language) {
        return mapItems.containsKey(language);
    }

    /**
     * 得到某种语言对应的Mapper
     * @param language
     * @return
     */
    public ResourceMapper getMapper(String language) {
        synchronized (mapItems) {
            ResourceMapper mapper = mapItems.get(language);
            if (mapper == null) {
                /** 加载某种语言的资源队列 **/
                mapper = new ResourceMapper(module, language);
                mapper.setLanguage(language);
                ZetaResourceLoader.loadResourceBundle(mapper, extendedPaths);
                mapItems.put(language, mapper);
            }
            return mapper;
        }
    }

    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * @param module the module to set
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * @return the mapItems
     */
    public Map<String, ResourceMapper> getMapItems() {
        return mapItems;
    }

    /**
     * @param mapItems the mapItems to set
     */
    public void setMapItems(Map<String, ResourceMapper> mapItems) {
        this.mapItems = mapItems;
    }

    /**
     * @return the extendedPath
     */
    public Stack<String> getExtendedPath() {
        return extendedPaths;
    }

    /**
     * @param extendedPath the extendedPath to set
     */
    public void setExtendedPath(Stack<String> extendedPath) {
        this.extendedPaths = extendedPath;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        pushPath(path);
        this.path = path;
    }

}
