package com.topvision.ems.engine.executor.fault;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.facade.fault.SyslogFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.syslog.Syslog;
import com.topvision.framework.syslog.SyslogCallback;
import com.topvision.framework.syslog.SyslogManager;
import com.topvision.framework.syslog.SyslogServerParam;

@Facade("syslogFacade")
public class SyslogFacadeImpl extends BaseEngine implements SyslogFacade {
    @Autowired
    private SyslogManager syslogManager = null;

    @Override
    public void addSyslogListener() {
        SyslogCallback l = getCallback(SyslogCallback.class);
        if (l != null) {
            syslogManager.addListener(l);
        }
    }

    @Override
    public void removeSyslogListener() {
        SyslogCallback l = getCallback(SyslogCallback.class);
        if (l != null) {
            syslogManager.removeListener(l);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.facade.fault.SyslogFacade#sendSyslog(com.topvision.ems.facade.domain.Syslog)
     */
    @Override
    public void sendSyslog(Syslog syslog) {
        syslogManager.sendSyslog(syslog);
    }

    /**
     * @param syslogManager
     *            the syslogManager to set
     */
    public void setSyslogManager(SyslogManager syslogManager) {
        this.syslogManager = syslogManager;
    }

    @Override
    public void setSyslogServerParam(SyslogServerParam param) {
        syslogManager.reset(param);
    }
}
