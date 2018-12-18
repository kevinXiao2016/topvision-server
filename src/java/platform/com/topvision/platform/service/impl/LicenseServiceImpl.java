/***********************************************************************
 * $Id: ServiceImpl.java,v1.0 2012-7-12 下午3:51:42 $
 * 
 * @author: dengl
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatHourlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
//import com.topvision.ems.facade.domain.EntityType;
//import com.topvision.ems.template.service.EntityTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.framework.service.BaseService;
import com.topvision.license.common.domain.Module;
import com.topvision.license.common.domain.V2Apply;
import com.topvision.license.common.exception.LicenseException;
import com.topvision.license.parser.LicenseIf;
import com.topvision.platform.dao.LicenseDao;
import com.topvision.platform.domain.LicenseView;
import com.topvision.platform.message.event.LicenseChangeEvent;
import com.topvision.platform.message.event.LicenseChangeListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.LicenseService;
import com.topvision.platform.service.SchedulerService;
import com.topvision.platform.service.job.LicenseCheckJob;

/**
 * @author dengl
 * @created @2012-7-12-下午3:51:42
 * 
 */
@Service("licenseService")
public class LicenseServiceImpl extends BaseService implements LicenseService {
    private static Logger logger = LoggerFactory.getLogger(LicenseServiceImpl.class);
    @Autowired
    private LicenseDao licenseDao;
    @Autowired
    private LicenseIf licenseIf;
    public static Map<String, EntityType> entityTypeMap = new HashMap<>();
    @Autowired
    private MessageService messageService;
    @Autowired
    private SchedulerService schedulerService;

    @PostConstruct
    public void initialize() {
        super.initialize();
        try {
            JobDetail job = newJob(LicenseCheckJob.class).withIdentity("LicenseCheckJob", "License").build();
            job.getJobDataMap().put(LicenseIf.class.getName(), licenseIf);
            TriggerBuilder<SimpleTrigger> builder = newTrigger()
                    .withIdentity(job.getKey().getName(), job.getKey().getGroup())
                    .withSchedule(repeatHourlyForever(24));
            schedulerService.scheduleJob(job, builder.build());
        } catch (SchedulerException e) {
            logger.error("", e);
        }
        List<EntityType> entityTypes = licenseDao.selectEntityTypes();
        for (EntityType entityType : entityTypes) {
            entityTypeMap.put(entityType.getName(), entityType);
        }
    }

    @Override
    public List<EntityType> getAllEntityTypes() {
        List<EntityType> types = licenseDao.selectEntityTypes();
        types = mergeEntityType(types);
        Collections.sort(types, new Comparator<EntityType>() {
            @Override
            public int compare(EntityType o1, EntityType o2) {
                if (o1.getName().equals(o1.getModule()) && o2.getName().equals(o2.getModule())) {
                    return o1.getModule().compareTo(o2.getModule());
                } else if (o1.getName().equals(o1.getModule())) {
                    return -1;
                } else if (o2.getName().equals(o2.getModule())) {
                    return 1;
                } else {
                    return o1.getName().compareTo(o2.getName());
                }
            }
        });
        return types;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private List<EntityType> mergeEntityType(List<EntityType> entityTypes) {
        Map<String, EntityType> entityTypeMap = new HashMap<String, EntityType>();
        for (EntityType entityType : entityTypes) {
            entityType.setDisplayName(entityType.getDisplayName().replaceAll("\\([^)]+\\)", ""));
            entityTypeMap.put(entityType.getName(), entityType);
        }
        return new ArrayList(entityTypeMap.values());
    }

    @Override
    public LicenseView getLicenseView() {
        try {
            Document doc = licenseIf.getLicense();
            LicenseView licView = new LicenseView();

            Element root = doc.getDocumentElement();
            licView.setVersion(getChildElementValue(root, "version"));
            licView.setLicenseType(getChildElementValue(root, "licenseType"));
            licView.setOrganisation(getChildElementValue(root, "organisation"));
            licView.setNumberOfDays(getChildElementValue(root, "numberOfDays"));
            licView.setNumberOfEngines(getChildElementValue(root, "numberOfEngines"));
            licView.setNumberOfUsers(getChildElementValue(root, "numberOfUsers"));
            licView.setExpiryDate(getChildElementValue(root, "expiryDate"));
            licView.setMobile(getChildElementValue(root, "mobile"));
            licView.setUser(getChildElementValue(root, "contactUser"));
            licView.setPhone(getChildElementValue(root, "phone"));
            licView.setEmail(getChildElementValue(root, "email"));
            licView.setMobile(getChildElementValue(root, "mobile"));
            String endDate = this.licenseIf.getEndDate();
            if (endDate != null && !endDate.equals("-1")) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                licView.setEndDate(format.format(new Date(Long.valueOf(endDate))));
            } else {
                licView.setEndDate(endDate);
            }

            NodeList nodeList = root.getElementsByTagName("module");
            int len = nodeList.getLength();
            List<Module> modules = new ArrayList<Module>();
            for (int i = 0; i < len; i++) {
                Element e = (Element) nodeList.item(i);

                Module lm = new Module();
                lm.setName(getChildElementValue(e, "name"));
                lm.setDisplayName(getChildElementValue(e, "displayName"));
                lm.setInclude(getChildElementValue(e, "include"));
                lm.setExclude(getChildElementValue(e, "exclude"));
                lm.setEnabled(Boolean.parseBoolean(getChildElementValue(e, "enabled")));
                lm.setNumberOfEntities(Integer.parseInt(getChildElementValue(e, "numberOfEntities")));
                StringBuilder names = new StringBuilder();
                if (lm.getInclude() != null && !lm.getInclude().isEmpty()) {
                    for (String name : lm.getInclude()) {
                        if (entityTypeMap.containsKey(name)) {
                            names.append(entityTypeMap.get(name).getDisplayName()).append(",");
                        } else {
                            names.append(name).append(",");
                        }
                    }
                    names.deleteCharAt(names.length() - 1);
                }
                names = new StringBuilder();
                if (lm.getExclude() != null && !lm.getExclude().isEmpty()) {
                    for (String name : lm.getExclude()) {
                        if (entityTypeMap.containsKey(name)) {
                            names.append(entityTypeMap.get(name).getDisplayName()).append(",");
                        } else {
                            names.append(name).append(",");
                        }
                    }
                    names.deleteCharAt(names.length() - 1);
                }

                modules.add(lm);
            }
            licView.setModules(modules);
            return licView;
        } catch (LicenseException e) {
            logger.warn(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public Module getLicenseModule(String name) {
        LicenseView lView = getLicenseView();
        if (lView != null) {
            List<Module> modules = lView.getModules();
            for (Module m : modules) {
                if (m.getName().equals(name))
                    return m;
            }
        }

        return null;
    }

    @Override
    public byte[] getProductKey(LicenseView licenseView) {
        V2Apply v2Apply = new V2Apply();
        v2Apply.setLicenseType(licenseView.getLicenseType());
        v2Apply.setContactUser(licenseView.getUser());
        v2Apply.setPhone(licenseView.getPhone());
        v2Apply.setEmail(licenseView.getEmail());
        v2Apply.setOrganisation(licenseView.getOrganisation());
        v2Apply.setNumberOfDays(new Integer(licenseView.getNumberOfDays()));
        v2Apply.setNumberOfEngines(new Integer(licenseView.getNumberOfEngines()));
        v2Apply.setNumberOfUsers(new Integer(licenseView.getNumberOfUsers()));
        v2Apply.setMobile(licenseView.getMobile());
        List<Module> modules = new ArrayList<Module>();
        String entityTypes = licenseView.getSelectEntityTypes();
        entityTypes = entityTypes + ",";
        modules.add(createEntityTypeModule(entityTypes, "olt", licenseView.getOltNum()));
        modules.add(createEntityTypeModule(entityTypes, "onu", licenseView.getOnuNum()));
        modules.add(createEntityTypeModule(entityTypes, "cmc", licenseView.getCmcNum()));
        modules.add(createEntityTypeModule(entityTypes, "cmts", licenseView.getCmtsNum()));
        modules.add(createMobileModule(licenseView.getMobileSurport(), "mobile"));
        modules.add(createMobileModule(licenseView.getPnmpSurport(), "pnmp"));
        modules.add(createReportModule(licenseView.getReportSurport(), licenseView.getSelectReports(),
                licenseView.getUnSelectReports(), "report"));
        v2Apply.setModules(modules);
        return licenseIf.getProductKey(v2Apply);
    }

    /**
     * @param string
     * @return
     */
    private Module createReportModule(Boolean surport, String include, String exclude, String name) {
        Module m = new Module();
        m.setName(name);
        if (surport) {
            m.setEnabled(true);
        } else {
            m.setEnabled(false);
        }
        m.setInclude(include);
        m.setExclude(exclude);
        return m;
    }

    /**
     * @param string
     * @return
     */
    private Module createMobileModule(Boolean surport, String name) {
        Module m = new Module();
        m.setName(name);
        if (surport) {
            m.setEnabled(true);
        } else {
            m.setEnabled(false);
        }
        return m;
    }

    /**
     * @param string
     * @return
     */
    private Module createEntityTypeModule(String entityTypes, String name, Integer number) {
        Module m = new Module();
        m.setName(name);
        m.setNumberOfEntities(number);
        if (entityTypes.indexOf(name + ",") != -1) {
            m.setEnabled(true);
            m.setInclude(name);
            List<EntityType> types = licenseDao.selectEntityTypesByModule(name);
            StringBuilder exclude = new StringBuilder();
            for (EntityType type : types) {
                if (entityTypes.indexOf(type.getName() + ",") == -1) {
                    if (!type.getName().equals(type.getModule()) && entityTypes.indexOf(type.getModule()) == -1) {
                        // 如果是小类型并且他的父类型也在在排除范围，则不单独增加
                        continue;
                    }
                    exclude.append(type.getName()).append(",");
                }
            }
            m.setExclude(exclude.toString());
        } else {
            m.setEnabled(false);
            m.setExclude(name);
        }
        return m;
    }

    @Override
    public void importLicense(byte[] lic) {
        licenseIf.importLicense(lic);
        LicenseChangeEvent evt = new LicenseChangeEvent("license");
        evt.setActionName("licenseChanged");
        evt.setListener(LicenseChangeListener.class);
        messageService.fireMessage(evt);
    }

    @Override
    public boolean verifyLicense() {
        return licenseIf.checkLicense() && getUnauthorizedEntities().isEmpty();
    }

    private String getChildElementValue(Element e, String name) {
        NodeList nodeList = e.getElementsByTagName(name);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        } else {
            return null;
        }
    }

    @Override
    public Module getLicenseModule(Long type) {
        String moduleName = getModuleNameById(type);
        if (moduleName == null) {
            return null;
        }
        LicenseView lView = getLicenseView();
        if (lView != null) {
            List<Module> modules = lView.getModules();
            for (Module m : modules) {
                if (m.getName().equals(moduleName))
                    return m;
            }
        }
        return null;
    }

    private String getModuleNameById(Long id) {
        for (EntityType t : entityTypeMap.values()) {
            if (t.getTypeId() == id) {
                return t.getName();
            }
        }
        return null;
    }

    @Override
    public void onlineApply(LicenseView licenseView) {
    }

    @Override
    public List<Entity> getUnauthorizedEntities() {
        return licenseDao.selectUnauthorizedEntities();
    }

}
