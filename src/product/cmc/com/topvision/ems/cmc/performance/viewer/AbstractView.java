package com.topvision.ems.cmc.performance.viewer;

import com.topvision.ems.cmc.perf.dao.CmcPerfDao;
import com.topvision.ems.performance.service.PerformanceService;
import com.topvision.ems.performance.service.Viewer;

/**
 * 为了实现国际化，在抽象类里面定义资源对象。
 * @author smsx
 * @created @2012-9-21-下午01:50:38
 *
 */
public abstract class AbstractView implements Viewer {

    //    protected String lang = null;
    //    protected ResourceManager cmcRes = null;
    //使用方法：cmcRes.getString("CCMTS.CPUMemUtilization")

    @SuppressWarnings("rawtypes")
    private PerformanceService performanceService;

    protected CmcPerfDao cmcPerfDao;

    /**
     * @return the cmcPerfDao
     */
    public CmcPerfDao getCmcPerfDao() {
        return cmcPerfDao;
    }

    /**
     * @param cmcPerfDao the cmcPerfDao to set
     */
    public void setCmcPerfDao(CmcPerfDao cmcPerfDao) {
        this.cmcPerfDao = cmcPerfDao;
    }

    public void initialize() {
        performanceService.registViewer(this);
        //        try{
        //            lang = ((UserContext) ServletActionContext.getRequest().getSession().getAttribute(UserContext.KEY)).getUser().getLanguage();
        //            
        //        }catch(Throwable t){
        //            lang = "zh_CN";
        //        }
        //        cmcRes = ResourceManager.getResourceManager("com.topvision.ems.cmc.resources",lang);
    }

    @SuppressWarnings("rawtypes")
    public PerformanceService getPerformanceService() {
        return performanceService;
    }

    @SuppressWarnings("rawtypes")
    public void setPerformanceService(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

}
