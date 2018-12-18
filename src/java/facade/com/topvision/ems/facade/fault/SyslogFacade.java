package com.topvision.ems.facade.fault;

import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.syslog.Syslog;
import com.topvision.framework.syslog.SyslogServerParam;

@EngineFacade(serviceName = "SyslogFacade", beanName = "syslogFacade")
public interface SyslogFacade extends Facade {
    /**
     * 添加syslog监视器
     * 
     */
    void addSyslogListener();

    /**
     * 删除syslog监视器
     * 
     */
    void removeSyslogListener();

    /**
     * 用户通过engine端发送syslog
     * 
     * @param syslog
     */
    void sendSyslog(Syslog syslog);

    /**
     * 设置Syslog server的参数
     * 
     * @param param
     */
    void setSyslogServerParam(SyslogServerParam param);
}
