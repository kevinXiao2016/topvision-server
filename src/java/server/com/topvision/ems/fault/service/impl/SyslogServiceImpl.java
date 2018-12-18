package com.topvision.ems.fault.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.fault.SyslogFacade;
import com.topvision.ems.fault.dao.SyslogDao;
import com.topvision.ems.fault.parser.SyslogParser;
import com.topvision.ems.fault.service.AlertFilterService;
import com.topvision.ems.fault.service.SyslogService;
import com.topvision.ems.network.dao.EntityAddressDao;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntityAddress;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.syslog.Syslog;
import com.topvision.framework.syslog.SyslogCallback;
import com.topvision.framework.syslog.SyslogServerParam;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.facade.FacadeFactory;

public class SyslogServiceImpl extends BaseService implements SyslogService, SyslogCallback {
    public static final String SYSLOG_MODULE = "syslog";
    private static final Logger logger = LoggerFactory.getLogger(AlertFilterService.class);
    private List<SyslogParser> parsers;
    private SystemPreferencesDao systemPreferencesDao;
    private FacadeFactory facadeFactory;
    private EntityAddressDao entityAddressDao;
    private EntityDao entityDao;
    private SyslogDao syslogDao;

    @Override
    public void destroy() {
        super.destroy();
    }

    public void start() {
        if (logger.isInfoEnabled()) {
            logger.info("SyslogService start.");
        }
        List<SystemPreferences> prefs = systemPreferencesDao.selectByModule(SYSLOG_MODULE);
        if (prefs != null && !prefs.isEmpty()) {
            List<Integer> ports = new ArrayList<Integer>();
            for (SystemPreferences pref : prefs) {
                if (pref.getName().equals("syslog.listenPorts") && pref.getValue() != null
                        && !pref.getValue().isEmpty()) {
                    StringTokenizer tokens = new StringTokenizer(pref.getValue(), ",");
                    while (tokens.hasMoreTokens()) {
                        String s = tokens.nextToken();
                        ports.add(Integer.parseInt(s));
                    }
                }
            }
            if (!ports.isEmpty()) {
                List<SyslogFacade> facades = facadeFactory.getAllFacade(SyslogFacade.class);
                for (SyslogFacade facade : facades) {
                    SyslogServerParam param = new SyslogServerParam();
                    param.setListenPorts(ports);
                    facade.setSyslogServerParam(param);
                    facade.addSyslogListener();
                }
            }
        }
    }

    @Override
    public void onSyslog(Syslog syslog) {
        EntityAddress entityAddress = entityAddressDao.selectByAddress(syslog.getHost());
        if (entityAddress == null) {
            logger.info("receive the syslog from ,but device not exist.", syslog.getHost());
            return;
        }
        Entity entity = entityDao.selectByPrimaryKey(entityAddress.getEntityId());
        if (!entity.isStatus()) {
            logger.info("receive the syslog from ,but device not be managed.", entity.getEntityId());
            return;
        }
        //syslogDao.insertEntity(syslog);
        synchronized (parsers) {
            for (SyslogParser parser : parsers) {
                try {
                    // parser支持顺序，前面的优先级高于后面的，高优先级的可以中断后面的解析
                    if (!parser.parse(entityAddress.getEntityId(), syslog)) {
                        break;
                    }
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
    }

    /**
     * @param parsers
     *            the parsers to set
     */
    public void setParsers(List<SyslogParser> parsers) {
        this.parsers = parsers;
    }

    /**
     * @param systemPreferencesDao
     *            the systemPreferencesDao to set
     */
    public void setSystemPreferencesDao(SystemPreferencesDao systemPreferencesDao) {
        this.systemPreferencesDao = systemPreferencesDao;
    }

    /**
     * @return the facadeFactory
     */
    public FacadeFactory getFacadeFactory() {
        return facadeFactory;
    }

    /**
     * @param facadeFactory
     *            the facadeFactory to set
     */
    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    /**
     * @return the entityAddressDao
     */
    public EntityAddressDao getEntityAddressDao() {
        return entityAddressDao;
    }

    /**
     * @param entityAddressDao
     *            the entityAddressDao to set
     */
    public void setEntityAddressDao(EntityAddressDao entityAddressDao) {
        this.entityAddressDao = entityAddressDao;
    }

    /**
     * @return the entityDao
     */
    public EntityDao getEntityDao() {
        return entityDao;
    }

    /**
     * @param entityDao
     *            the entityDao to set
     */
    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    /**
     * @return the syslogDao
     */
    public SyslogDao getSyslogDao() {
        return syslogDao;
    }

    /**
     * @param syslogDao the syslogDao to set
     */
    public void setSyslogDao(SyslogDao syslogDao) {
        this.syslogDao = syslogDao;
    }

}
