/***********************************************************************
 * $Id: ProactiveReportJob.java,v1.0 Sep 20, 2016 10:13:44 AM $
 * 
 * @author: Victorli
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin.job;

import org.jruby.embed.ScriptingContainer;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.topvision.ems.admin.service.PassiveReportService;
import com.topvision.ems.admin.service.ProactiveReportService;
import com.topvision.framework.scheduler.AbstractJob;
import com.topvision.license.parser.LicenseIf;
import com.topvision.platform.SystemConstants;

/**
 * @author Victorli
 * @created @Sep 20, 2016-10:13:44 AM
 *
 */
@PersistJobDataAfterExecution
public class ProactiveReportJob extends AbstractJob {
    @Override
    public void doJob(JobExecutionContext ctx) throws JobExecutionException {
        try {
            LicenseIf licenseIf = getService(LicenseIf.class);
            PassiveReportService passiveReportService = getService(PassiveReportService.class);
            String hid = licenseIf.getHid();
            ProactiveReportService proactiveReportService = getFacade(ProactiveReportService.class);
            proactiveReportService.report(hid);
            passiveReportService.getAbility().add(PassiveReportService.ABILITY_PROACTIVE);
            proactiveReportService.reportAbility(hid, passiveReportService.getAbility());
            proactiveReportService.reportEmsInfo(passiveReportService.getEmsInfo());
            proactiveReportService.reportHostIPs(hid, passiveReportService.getHostIPs());
            proactiveReportService.reportDeviceCount(hid, passiveReportService.getDeviceCount());

            // 此段代码有可能抛出异常，放到try的最后
            String jrubyScript = proactiveReportService.getDynamicInvokeScript(hid);
            ScriptingContainer container = new ScriptingContainer();
            proactiveReportService.reportDynamicInvokeResult(hid, container.runScriptlet(jrubyScript));
        } catch (Exception e) {
            logger.warn("远程汇报出错:{}", e.getMessage());
            logger.debug("远程汇报出错", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getFacade(Class<T> clazz) {
        StringBuilder serviceUrl = new StringBuilder("http://");
        serviceUrl.append(SystemConstants.getInstance().getStringParam("http.invoke.host", "ems.top-vision.cn"))
                .append(":").append(SystemConstants.getInstance().getIntParam("http.invoke.port", 8107));
        serviceUrl.append("/remoting/").append(clazz.getSimpleName());

        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(serviceUrl.toString());
        proxy.setServiceInterface(clazz);
        proxy.afterPropertiesSet();
        return (T) proxy.getObject();
    }
}
