/***********************************************************************
 * $ ConfigWorkServiceImpl.java,v1.0 2011-7-21 18:31:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.template.service.impl;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.template.domain.ApConfigParam;
import com.topvision.ems.template.domain.ApWorkTemplate;
import com.topvision.ems.template.service.ConfigWorkService;
import com.topvision.ems.template.works.ConfigWork;
import com.topvision.exception.service.ConfigException;
import com.topvision.exception.service.NoSuchWorkException;
import com.topvision.exception.service.NowIsWorkingException;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.message.event.WorkEvent;
import com.topvision.platform.message.event.WorkListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author jay
 * @created @2011-7-21-18:31:16
 */
@Service("configWorkService")
public class ConfigWorkServiceImpl extends BaseService implements ConfigWorkService, WorkListener {
    private static Logger logger = LoggerFactory.getLogger(ConfigWorkServiceImpl.class);
    // 不需要注入的内部内存变量
    private Map<String, ApWorkTemplate> workTemplates = null;
    private Map<String, Map<String, ApConfigParam>> configTemplates = null;
    private Boolean working = false;
    @Autowired
    MessageService messageService;

    /**
     * 配置失败
     * 
     * @param event
     */
    @Override
    public void failure(WorkEvent event) {
        logger.info("ConfigWorkServiceImpl.failure saveFailureAction");
    }

    /**
     * 配置成功
     * 
     * @param event
     */
    @Override
    public void success(WorkEvent event) {
        logger.info("ConfigWorkServiceImpl.success saveSuccessAction");
    }

    /**
     * 结束配置流程
     * 
     * @param event
     */
    @Override
    public void workEnd(WorkEvent event) {
        logger.info("ConfigWorkServiceImpl.workEnd saveEndTime");
    }

    /**
     * 开始配置流程
     * 
     * @param event
     */
    @Override
    public void workStart(WorkEvent event) {
        logger.info("ConfigWorkServiceImpl.workStart saveStartTime");
    }

    // 初始化方法
    /*
     * (non-Javadoc)
     * 
     * @see com.zetaframework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(WorkListener.class, this);
        initWorks();
        logger.info("workTemplates[" + workTemplates + "]");
        logger.info("configTemplates[" + configTemplates + "]");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.zetaframework.service.BaseService#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(WorkListener.class, this);
        workTemplates = null;
        configTemplates = null;
    }

    // 内部处理方法
    private void initWorks() {
        workTemplates = new HashMap<String, ApWorkTemplate>();
        configTemplates = new HashMap<String, Map<String, ApConfigParam>>();
        File dir = new File(SystemConstants.WEB_INF_REAL_PATH + "/template/work");
        if (dir != null && dir.exists()) {
            File[] workFiles = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().endsWith(".wt.xml");
                }
            });
            for (int i = 0; i < workFiles.length; i++) {
                Object re = readObject(workFiles[i]);
                if (re != null && re instanceof ApWorkTemplate) {
                    ApWorkTemplate awt = (ApWorkTemplate) re;
                    workTemplates.put(awt.getTemplateName(), awt);
                }
            }
            File[] workDirs = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory() && pathname.getName().endsWith(".wt.dir");
                }
            });
            for (int i = 0; i < workDirs.length; i++) {
                String temp = workDirs[i].getName();
                String dirName = temp.substring(0, temp.indexOf(".wt.dir"));
                if (dirName != null && !dirName.equalsIgnoreCase("") && workTemplates.containsKey(dirName)) {
                    if (!configTemplates.containsKey(dirName)) {
                        Map<String, ApConfigParam> t = new HashMap<String, ApConfigParam>();
                        configTemplates.put(dirName, t);
                    }
                    File[] configFiles = workDirs[i].listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.isFile() && pathname.getName().endsWith(".ct.xml");
                        }
                    });

                    for (int j = 0; j < configFiles.length; j++) {
                        Object re = readObject(configFiles[j]);
                        if (re != null && re instanceof ApConfigParam) {
                            ApConfigParam acp = (ApConfigParam) re;
                            if (acp.getTemplateName().equalsIgnoreCase(dirName)) {
                                Map<String, ApConfigParam> t = configTemplates.get(dirName);
                                t.put(acp.getModeName(), acp);
                            }
                        }
                    }
                }
            }
        }
    }

    private Object readObject(File file) {
        Object re = null;
        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
            re = decoder.readObject();
            decoder.close();
        } catch (FileNotFoundException e) {
            if (logger.isErrorEnabled()) {
                logger.error("file[" + file.toString() + "]can not find", e);
            }
        }
        return re;
    }

    private void writeObject(String path, Object o) {
        try {
            XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(path)));
            e.writeObject(o);
            e.close();
        } catch (FileNotFoundException e) {
            if (logger.isErrorEnabled()) {
                logger.error("file[" + path + "]can not find", e);
            }
        }
    }

    // 接口暴露的方法

    /**
     * 获取所有AP配置流程模板
     * 
     * @return
     */
    @Override
    public Map<String, ApWorkTemplate> getApWorkTemplates() {
        return null; // To change body of implemented methods use File |
                     // Settings | File Templates.
    }

    /**
     * 通过流程模板名和参数模板名获得一个参数模板对象
     * 
     * @param workTemplateName
     * @param configTemplateName
     * @return
     */
    @Override
    public ApConfigParam getConfigParam(String workTemplateName, String configTemplateName) {
        return null; // To change body of implemented methods use File |
                     // Settings | File Templates.
    }

    /**
     * 通过模板名获得对应模板的所有参数模板
     * 
     * @param workTemplateName
     * @return
     */
    @Override
    public Map<String, ApConfigParam> getConfigParamByTemplate(String workTemplateName) {
        return null; // To change body of implemented methods use File |
                     // Settings | File Templates.
    }

    /**
     * 保存一个参数模板
     * 
     * @param workTemplateName
     * @param configTemplateName
     * @param apConfigParam
     */
    @Override
    public void saveConfigParamMode(String workTemplateName, String configTemplateName, ApConfigParam apConfigParam) {
        String path = SystemConstants.WEB_INF_REAL_PATH + "/template/work/" + workTemplateName + ".wt.dir";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        writeObject(path + "/" + configTemplateName + ".ct.xml", apConfigParam);
    }

    /**
     * 是否有正在执行的配置流程
     * 
     * @return
     */
    @Override
    public boolean isWorking() {
        return false; // To change body of implemented methods use File |
                      // Settings | File Templates.
    }

    /**
     * 执行一个配置流程
     * 
     * @param entitys
     *            需要配置的所有设备ID
     * @param workName
     *            配置流程名
     * @param apConfigParam
     *            配置流程参数
     * @throws com.topvision.exception.service.NowIsWorkingException
     *             配置流程全局只允许同时执行一个 当正在执行的时候 发起其他配置流程会抛出该异常
     * @throws com.topvision.exception.service.NoSuchWorkException
     *             没有找到对应的配置流程时抛出
     * @throws com.topvision.exception.service.ConfigException
     *             配置错误时抛出
     */
    @Override
    public void executeWorks(List<Long> entitys, String workName, ApConfigParam apConfigParam) {
        synchronized (working) {
            if (working) {
                throw new NowIsWorkingException();
            }
            working = true;
        }
        logger.info("working [" + workName + "]");
        try {
            if (workTemplates.containsKey(workName)) {
                ApWorkTemplate apWorkTemplate = workTemplates.get(workName);

                logger.info("apWorkTemplate [" + apWorkTemplate + "]");
                try {
                    ConfigWork configWork = (ConfigWork) Class.forName(apWorkTemplate.getWorkClass()).newInstance();
                    configWork.execute(entitys, apWorkTemplate.getWorkParamList(), apConfigParam, messageService);
                } catch (ClassNotFoundException e) {
                    logger.error("workName[" + workName + "]", e);
                    throw new ConfigException("workName[" + workName + "]", e);
                } catch (IllegalAccessException e) {
                    logger.error("workName[" + workName + "]", e);
                    throw new ConfigException("workName[" + workName + "]", e);
                } catch (InstantiationException e) {
                    logger.error("workName[" + workName + "]", e);
                    throw new ConfigException("workName[" + workName + "]", e);
                }
            } else {
                throw new NoSuchWorkException("workName[" + workName + "]");
            }
        } finally {
            working = false;
        }
    }
}