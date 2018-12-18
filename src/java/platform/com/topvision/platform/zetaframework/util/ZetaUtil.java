/***********************************************************************
 * $Id: ZetaUtil.java,v1.0 2013-3-26 下午3:38:02 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.platform.zetaframework.exception.ReousrceKeyNotFoundException;
import com.topvision.platform.zetaframework.resource.ResourceModule;
import com.topvision.platform.zetaframework.resource.ResourceRepository;

/**
 * 
 * 提供快速解析国际化字符串的功能，国际化字段的获取一般都是从这里来获取的，不建议直接从ResourceModule中直接去获取
 * @author Bravin
 * @created @2013-3-26-下午3:38:02
 *
 */
public class ZetaUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZetaUtil.class);
    private static final int MODULE_FILED = 0;
    private static final int KEY_FILED = 1;
    private final String module;

    /**
     * 可以新建一个ZetaUtil对象，下次处理的时候则可以处理不带模块的key
     * @param module
     */
    public ZetaUtil(String module) {
        this.module = module;
    }

    /**
     * 静态方法，得到一个key
     * @param key
     * @return
     * @throws Exception 
     */
    public static String getStaticString(String key) {
        return getStaticString(key, null, null);
    }

    /**
     * 静态方法，得到一个key,如果key中不包含模块，则使用 defaultModule
     * @param key
     * @return
     * @throws Exception 
     */
    public static String getStaticString(String key, String defaultModule) {
        if (key == null) {
            return null;
        }
        return getInnerString(key, defaultModule);
    }

    /**
     * 静态方法，指定语言得到一个key,如果key中不包含模块，则使用 defaultModule
     * @param key
     * @param defaultModule
     * @param language
     * @return
     */
    public static String getStaticString(String key, String defaultModule, String language) {
        if (key == null) {
            return null;
        }
        String keyFiled = null;
        String moduleFiled = null;
        if (key.indexOf(ResourceKeyAnalyzer.MODULE_SPERATOR) > 0) {
            String[] resolver = key.split(ResourceKeyAnalyzer.MODULE_SPERATOR);
            moduleFiled = resolver[MODULE_FILED];
            keyFiled = resolver[KEY_FILED];
        } else {
            keyFiled = key;
            moduleFiled = defaultModule;
        }
        return getInnerString(keyFiled, moduleFiled, language);
    }

    /**
     * 通过一个指定的路径获取resourceModule,常用于兼容 operationLog
     * @param key
     * @param modulePath
     * @param language
     * @return
     */
    public static String getOperationLog(String key, String modulePath, String language) {
        if (key == null) {
            return null;
        }
        ResourceModule operationLogModule = ResourceRepository.getOperationLogModule(modulePath);
        return operationLogModule.getString(key, language);

    }

    /**
     * 通用方法，既可以得到带模块的key，也可以得到构造函数指定的模块的key
     * @param key
     * @return
     */
    public String getString(String key) {
        return getStaticString(key, module);
    }

    /**
     * 通用方法，指定语言，既可以得到带模块的key，也可以得到构造函数指定的模块的key
     * @param key
     * @return
     */
    public String getString(String key, String language) {
        return getStaticString(key, module, language);
    }

    /**
     * TODO 整合 String.format 方法
     * @param key
     * @param defaultValues
     * @return
     */
    public String getFormatString(String key, String... defaultValues) {
        return null;
    }

    /**
     * 传入key，模块然后得到其value
     * @param key
     * @param module
     * @return
     */
    private static String getInnerString(String key, String module) {
        return getInnerString(key, module, null);
    }

    /**
     * 传入key，模块，并指定语言然后得到其value
     * @param key
     * @param module
     * @param language
     * @return
     */
    private static String getInnerString(String key, String module, String language) {
        String data = key;
        try {
            data = _getInnerString(key, module, language);
        } catch (ReousrceKeyNotFoundException e) {
            logger.error("can not found resource key : {} in module: {}", key, module);
        }
        //由于存在JSTL比JSP TAG先执行的问题导致显示时用户自设的@XXX@丢失问题，所以@@需要补会来
        return data;
    }

    public static String getPageRenderString(String key, String defaultModule, String language) {
        String data = key;
        String moduleFiled = null;
        try {
            if (key == null) {
                return null;
            }
            String keyFiled = null;
            if (key.indexOf(ResourceKeyAnalyzer.MODULE_SPERATOR) > 0) {
                String[] resolver = key.split(ResourceKeyAnalyzer.MODULE_SPERATOR);
                moduleFiled = resolver[MODULE_FILED];
                keyFiled = resolver[KEY_FILED];
            } else {
                keyFiled = key;
                moduleFiled = defaultModule;
            }
            data = _getInnerString(keyFiled, moduleFiled, language);
        } catch (ReousrceKeyNotFoundException e) {
            logger.error("can not found resource key : {} in module: {}", key, moduleFiled);
            data = "@" + key + "@";
        }
        //由于存在JSTL比JSP TAG先执行的问题导致显示时用户自设的@XXX@丢失问题，所以@@需要补会来
        return data;
    }

    private static String _getInnerString(String key, String module, String language) {
        if (key == null) {
            return null;
        }
        if (module == null) {
            logger.error("module can not be found with key :{}", key);
            return key;
        }
        ResourceModule resourceModule = ResourceRepository.getModule(module);
        //如果没有找到就返回key自身
        String data = key;
        data = resourceModule.getString(key, language);
        //由于存在JSTL比JSP TAG先执行的问题导致显示时用户自设的@XXX@丢失问题，所以@@需要补会来
        return data;
    }

}
