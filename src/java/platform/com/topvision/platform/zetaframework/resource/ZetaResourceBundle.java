/***********************************************************************
 * $Id: ZetaResourceBundle.java,v1.0 2013-5-8 下午4:14:39 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.topvision.framework.common.FileUtils;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.zetaframework.util.ResourceUtil;

/**
 * 这个类为何不继承自 PropertyResourceBundle 的原因是没有找到 PropertyResourceBundle 在资源加载完成后单独重载资源的机制，
 * clearCache是个静态方法，调用它可能会把所有的资源都重载了。故由ZetaResourceBundle进行管理
 * @author Bravin
 * @created @2013-5-8-下午4:14:39
 *
 */
public class ZetaResourceBundle {
    private static Logger logger = LoggerFactory.getLogger(ZetaResourceBundle.class);
    private static final String PROPERTY_FILE_SUFFIX = ".properties";
    private File file;
    private long lastLoadedTime;
    private PropertyResourceBundle bundle;

    public ZetaResourceBundle(String path, String locale) {
        this.file = getFile(path, locale);
        checkIfReloadResource();
    }

    private File getFile(String path, String locale) {
        String pattern = path.replace(".", "/").concat("_").concat(locale).concat(PROPERTY_FILE_SUFFIX);
        Resource[] resources = ResourceUtil.getResources(pattern);
        if (resources.length > 0) {
            try {
                return resources[0].getFile();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        return null;
    }

    public String getString(String key) {
        if (SystemConstants.isDevelopment) {
            checkIfReloadResource();
        }
        return bundle.getString(key);
    }

    /**
     * 重载资源
     * @throws IOException 
     */
    private void checkIfReloadResource() {
        long fileLastModified = 0;
        fileLastModified = file.lastModified();
        if (fileLastModified > lastLoadedTime) {
            bundle = null;
            //重载bundle
            InputStream is = null;
            try {
                is = new FileInputStream(file);
                bundle = new PropertyResourceBundle(is);
            } catch (IOException e) {
                logger.error("", e);
            } finally {
                FileUtils.closeQuitely(is);
            }
            //更新resourcebundle的最近更新时间
            lastLoadedTime = fileLastModified;
        }
    }

    public PropertyResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(PropertyResourceBundle bundle) {
        this.bundle = bundle;
    }

}
