/***********************************************************************
 * $Id: UpgradeStatusServiceImpl.java,v1.0 2014年11月22日 上午11:42:06 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.service.impl;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.topvision.ems.upgrade.annotation.UpgradeStatusProperty;
import com.topvision.ems.upgrade.service.UpgradeStatusService;
import com.topvision.ems.upgrade.utils.UpgradeStatusConstants;
import com.topvision.framework.exception.AnnotationException;
import com.topvision.framework.service.BaseService;

/**
 * @author loyal
 * @created @2014年11月22日-上午11:42:06
 * 
 */
@Service("upgradeStatusService")
public class UpgradeStatusServiceImpl extends BaseService implements UpgradeStatusService {
    private Map<Integer, String> map = new TreeMap<Integer, String>();
    private Map<Integer, String> resultStatusmap = new TreeMap<Integer, String>();
    private Map<Integer, String> errorStatusmap = new TreeMap<Integer, String>();
    private Map<Integer, String> nowStatusmap = new TreeMap<Integer, String>();

    @SuppressWarnings("static-access")
    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        try {
            UpgradeStatusConstants upgradeStatus = new UpgradeStatusConstants();
            Field[] fields = upgradeStatus.getClass().getFields();

            if (fields == null || fields.length == 0) {
                throw new AnnotationException();
            }

            for (int i = 0; i < fields.length; i++) {
                UpgradeStatusProperty property = fields[i].getAnnotation(UpgradeStatusProperty.class);
                if (property != null) {
                    Integer id = new Integer(property.id());
                    String displayName = property.displayName();
                    if (upgradeStatus.RESULT_STATUS_TYPE.equals(property.statusType())) {
                        resultStatusmap.put(id, displayName);
                    } else if (upgradeStatus.ERROR_STATUS_TYPE.equals(property.statusType())) {
                        errorStatusmap.put(id, displayName);
                    } else if (upgradeStatus.NOW_STATUS_TYPE.equals(property.statusType())) {
                        nowStatusmap.put(id, displayName);
                    }
                    map.put(id, displayName);
                }
            }
        } catch (Exception ex) {
            getLogger().error("loadEntityType in initialze:", ex);
        }
    }

    @Override
    public Map<Integer, String> getAllStatus() {
        return map;
    }

    @Override
    public Map<Integer, String> getResultStatus() {
        return resultStatusmap;
    }

    @Override
    public Map<Integer, String> getErrorStatus() {
        return errorStatusmap;
    }

    @Override
    public Map<Integer, String> getNowStatus() {
        return nowStatusmap;
    }

}
