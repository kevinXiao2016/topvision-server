/***********************************************************************
 * $Id: MesageResourceManager.java,v 1.1 2008-5-2 下午02:02:22 niejun Exp $
 *
 * @author: niejun
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.topvision.framework.common.FileUtils;
import com.topvision.framework.exception.ResourceNotFoundException;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.zetaframework.util.ResourceUtil;

/**
 * @author niejun
 * @Create Date 2008-5-2 下午02:02:22
 */
public class ResourceManager {
    private static Logger logger = LoggerFactory.getLogger(ResourceManager.class);
    private static Map<String, Map<String, ResourceManager>> resourceManagers = new HashMap<String, Map<String, ResourceManager>>();
    private static final String JARFILE_INPUTSTRAM_CLASSNAME = "sun.net.www.protocol.jar.JarURLConnection$JarURLInputStream";

    private final static String RESOURCES = "resources";
    public final static String DEFAULTLANG = "zh_CN";
    public final static String SUPPORTLANG = ",zh_CN,en_US,";
    private static final String PROPERTY_FILE_SUFFIX = ".properties";

    private Properties properties = null;
    private long lastModified;
    private File file;

    /**
     * 获取默认的资源管理器.
     * 
     * @return
     */
    public static ResourceManager getResourceManager(String module) {
        // 使用系统默认语言赋值
        SystemConstants sc = SystemConstants.getInstance();
        String lang = sc.getStringParam("language", "zh_CN");
        // 通过ServletActionContext判断是否在Action层
        HttpServletRequest request = null;
        try {
            request = ServletActionContext.getRequest();
        } catch (NullPointerException e) {
            // 此种情况表示还未初始化Servlet或不在Action层 必然会抛出所以一般不需要打印
            logger.trace("", e);
        } catch (Exception e) {
            logger.debug("", e);
        } catch (NoClassDefFoundError e) {
            logger.debug("NoClassDefFoundError:{}", e);
        }
        if (request != null) {
            UserContext uc = (UserContext) request.getSession().getAttribute(UserContext.KEY);
            if (uc != null) {
                lang = uc.getUser().getLanguage();
            }
        }
        return getResourceManager(module, lang);
    }

    /**
     * 获取给定模块名称的资源管理器.
     * 
     * @param module
     * @return
     */
    public static ResourceManager getResourceManager(String module, String lang) {
        Map<String, ResourceManager> langMap = resourceManagers.get(module);
        if (langMap == null) {
            langMap = new HashMap<String, ResourceManager>();
            resourceManagers.put(module, langMap);
        }
        ResourceManager mgr = langMap.get(lang);
        if (mgr == null) {
            mgr = new ResourceManager(module, lang);
            langMap.put(lang, mgr);
        } else {
            if (SystemConstants.isDevelopment) {
                mgr.checkIfReloadResource();
            }
        }
        return mgr;
    }

    public static File getFile(String path, String locale) {
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

    private void checkIfReloadResource() {
        if (file != null) {
            long fileLastModified = 0;
            fileLastModified = file.lastModified();
            if (fileLastModified > lastModified) {
                // 重载bundle
                InputStream is = null;
                try {
                    is = new FileInputStream(file);
                    properties = new Properties();
                    properties.load(is);
                } catch (IOException e) {
                    logger.error("", e);
                } finally {
                    FileUtils.closeQuitely(is);
                }
                // 更新resourcebundle的最近更新时间
                lastModified = fileLastModified;
            }
        }
    }

    private ResourceManager(String pathName, String lang) {
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        if (pathName == null) {
            sb.append("com.topvision.platform").append(".").append(RESOURCES);
        } else {
            // Modify by Victor@20150429发现前面有空格导致取不到的错误
            sb.append(pathName.trim());
        }
        String path = sb.toString();
        path = path.replace('.', '/');
        sb = new StringBuilder();
        sb.append("/").append(path);
        // 如果lang不在支持列表中则使用配置文件中定义的语言
        String tmpLang = new StringBuilder().append(",").append(lang).append(",").toString();
        if (lang == null || lang.equalsIgnoreCase("") || SUPPORTLANG.indexOf(tmpLang) == -1) {
            SystemConstants sc = SystemConstants.getInstance();
            lang = sc.getStringParam("language", "zh_CN");
        }
        sb.append("_");
        sb.append(lang);
        sb.append(".properties");
        properties = new Properties();
        path = sb.toString();
        if (logger.isDebugEnabled()) {
            logger.debug("path = " + path);
        }
        is = getClass().getResourceAsStream(path);
        if (is != null) {
            try {
                properties.load(is);
            } catch (Exception ioe) {
                logger.error("Load message resource [{}] error:{}", path, ioe);
                if (logger.isDebugEnabled()) {
                    logger.debug("Load message resource error:" + path, ioe);
                }
            } finally {
                FileUtils.closeQuitely(is);
            }
            // Report JAR Resource Properties
            // Resource Properties in classPath jar file is not a file, only can read as inputstream
            // Jar File is not need to reload at all
            if (is.getClass().getName().equals(JARFILE_INPUTSTRAM_CLASSNAME)) {
                return;
            }
        }
        this.file = getFile(pathName, lang);
    }

    /**
     * 获取给定key的资源.如果不存在这个key则返回key本身
     * 
     * @param key
     * @return
     * @throws ResourceNotFoundException
     */
    public String getNotNullString(String key) {
        return properties.getProperty(key, key);
    }

    /**
     * 获取给定key的资源，如果不存在则返回null
     * 
     * @param key
     * @return
     */
    public String getNullString(String key) {
        return properties.getProperty(key, null);
    }

    /**
     * 获取给定key的资源.如果不存在这个key则返回key本身
     * 
     * @param key
     * @return
     * @throws ResourceNotFoundException
     */
    public String getNotNullString(String key, String... strings) {
        String v = properties.getProperty(key, key);
        for (int i = 0; i < strings.length; i++) {
            v = v.replace("{" + i + "}", strings[i]);
        }
        return v;
    }

    /**
     * 获取给定key的资源.
     * 
     * @param key
     * @return
     * @throws ResourceNotFoundException
     */
    public String getString(String key) {
        String v = properties.getProperty(key);
        if (v == null) {
            v = key;
        }
        return v;
    }

    /**
     * 获取给定key的资源, 并替换{i}参数.
     * 
     * @param key
     * @param strings
     * @return
     * @throws ResourceNotFoundException
     */
    public String getString(String key, String... strings) {
        try {
            String v = properties.getProperty(key);
            if (v == null) {
                v = key;
            } else {
                for (int i = 0; i < strings.length; i++) {
                    v = v.replace("{" + i + "}", strings[i] == null ? "" : strings[i]);
                }
            }
            return v;
        } catch (ResourceNotFoundException e) {
            for (int i = 0; i < strings.length; i++) {
                key = key.replaceAll("\\{" + i + "\\}", strings[i] == null ? "" : strings[i]);
            }
            return key;
        }

    }

    /**
     * 获取key表示资源值, 如果值为空返回value.
     * 
     * @param key
     * @param value
     * @return
     */
    public String getValue(String key, String value) {
        String v = properties.getProperty(key);
        if (v == null) {
            return value;
        }
        return v;
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * @return the lastModified
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * @param lastModified
     *            the lastModified to set
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file
     *            the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

}
