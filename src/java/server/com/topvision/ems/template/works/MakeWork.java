package com.topvision.ems.template.works;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.ems.template.domain.ApConfigParam;
import com.topvision.ems.template.domain.ApWorkParam;
import com.topvision.ems.template.domain.ApWorkTemplate;
import com.topvision.platform.ResourceManager;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 2011-7-19 Time: 9:32:31 To change this
 * template use File | Settings | File Templates.
 */
public class MakeWork {
    private Map<String, ApWorkTemplate> workTemplates = null;
    private Map<String, Map<String, ApConfigParam>> configTemplates = null;

    public static void main(String[] args) {
        MakeWork makeWork = new MakeWork();
        makeWork.go2();
    }

    private void go2() {
        initWorks();
        System.out.println("workTemplates = " + workTemplates);
        System.out.println("configTemplates = " + configTemplates);
    }

    private void initWorks() {
        workTemplates = new HashMap<String, ApWorkTemplate>();
        configTemplates = new HashMap<String, Map<String, ApConfigParam>>();
        File dir = new File("../");
        if (dir != null && dir.exists()) {
            File[] workFiles = dir.listFiles(new FileFilter() {
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

    @SuppressWarnings("unused")
    private void go1() {
        ApConfigParam acp = new ApConfigParam();
        acp.setModeName("test1");
        acp.setTemplateName(getResourceString("MakeWork.ConfigurationProcess","com.topvision.ems.resources.resources"));
        acp.setValue("ssid", "chinanet");
        acp.setValue("ssid1", "chinanet1");
        saveConfigParamMode(getResourceString("MakeWork.ConfigurationProcess","com.topvision.ems.resources.resources"), acp.getModeName(), acp);
    }

    @SuppressWarnings("unused")
    private void go() {
        ApWorkTemplate apWorkTemplate = new ApWorkTemplate();
        apWorkTemplate.setTemplateName(getResourceString("MakeWork.ConfigurationProcess","com.topvision.ems.resources.resources"));
        apWorkTemplate.setWorkClass("com.topvision.ems.template.works.StartConfigWork");
        List<ApWorkParam> apWorkParamList = new ArrayList<ApWorkParam>();
        ApWorkParam apWorkParam1 = new ApWorkParam();
        apWorkParam1.setParamKey("apmode");
        apWorkParam1.setParamName(getResourceString("MakeWork.APmode","com.topvision.ems.resources.resources"));
        apWorkParam1.setParamType(ApWorkParam.LIST);
        List<String> apWorkParam1ValueList = new ArrayList<String>();
        apWorkParam1ValueList.add("n/g");
        apWorkParam1ValueList.add("n/g/n");
        apWorkParam1.setDefaultValueList(apWorkParam1ValueList);
        apWorkParamList.add(apWorkParam1);
        ApWorkParam apWorkParam2 = new ApWorkParam();
        apWorkParam2.setParamKey("SSID");
        apWorkParam2.setParamName("SSID");
        apWorkParam2.setParamType(ApWorkParam.TEXT);
        List<String> apWorkParam2ValueList = new ArrayList<String>();
        apWorkParam2ValueList.add("ChinaNet");
        apWorkParam2.setDefaultValueList(apWorkParam2ValueList);
        apWorkParamList.add(apWorkParam2);
        ApWorkParam apWorkParam3 = new ApWorkParam();
        apWorkParam3.setParamKey("ch");
        apWorkParam3.setParamKey("ch");
        apWorkParam3.setParamName(getResourceString("MakeWork.WirelessChannel","com.topvision.ems.resources.resources"));
        apWorkParam3.setParamType(ApWorkParam.LIST);
        List<String> apWorkParam3ValueList = new ArrayList<String>();
        apWorkParam3ValueList.add("1");
        apWorkParam3ValueList.add("2");
        apWorkParam3ValueList.add("3");
        apWorkParam3ValueList.add("4");
        apWorkParam3ValueList.add("5");
        apWorkParam3ValueList.add("6");
        apWorkParam3ValueList.add("7");
        apWorkParam3ValueList.add("8");
        apWorkParam3ValueList.add("9");
        apWorkParam3ValueList.add("10");
        apWorkParam3ValueList.add("11");
        apWorkParam3ValueList.add("12");
        apWorkParam3ValueList.add("13");
        apWorkParam3.setDefaultValueList(apWorkParam3ValueList);
        apWorkParamList.add(apWorkParam3);
        apWorkTemplate.setWorkParamList(apWorkParamList);

        try {
            XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("../"
                    + apWorkTemplate.getTemplateName() + ".wt.xml")));
            e.writeObject(apWorkTemplate);
            e.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace(System.out);
        }

    }

    /**
     * 保存一个参数模板
     * 
     * @param workTemplateName
     * @param configTemplateName
     * @param apConfigParam
     */
    public void saveConfigParamMode(String workTemplateName, String configTemplateName, ApConfigParam apConfigParam) {
        String path = "../" + workTemplateName + ".wt.dir";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        writeObject(path + "/" + configTemplateName + ".ct.xml", apConfigParam);
    }

    private Object readObject(File file) {
        Object re = null;
        try {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
            re = decoder.readObject();
            decoder.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        }
        return re;
    }

    private void writeObject(String path, Object o) {
        try {
            XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(path)));
            e.writeObject(o);
            e.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        }
    }

    /*
     * key：properties文件的keymodule：资源文件
     */
    protected String getResourceString(String key, String module) {
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }
}
