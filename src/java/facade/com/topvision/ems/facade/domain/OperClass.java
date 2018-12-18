/***********************************************************************
 * $ Collector.java,v1.0 2012-5-2 16:06:53 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author jay
 * @created @2012-5-2-16:06:53
 */
public abstract class OperClass implements Serializable {
    private static final long serialVersionUID = 3266962176888611156L;
    protected String[] oids;
    protected PerformanceDomain[] domains;
    protected Set<Long> monitorIdList = new HashSet<Long>();
    private Long monitorId;
    private Long previousFireTime;
    private Long nextFireTime;
    protected Long entityId;
    protected String perfService;
    protected String scheduler;
    protected String category;
    private String ipAddress;

    protected OperClass(String perfService, String scheduler, String category) {
        this.perfService = perfService;
        this.scheduler = scheduler;
        this.category = category;
    }

    /**
     * 获取调度执行类
     * 
     * @return Class<ExecParam>
     */
    public final String getScheduler() {
        return scheduler;
    }

    /**
     * 获取指标保持执行类
     * 
     * @return Class<DBSaveParam>
     */
    public final String getPerfService() {
        return perfService;
    }

    /**
     * 监视器类型
     * 
     * @return category
     */
    public final String getCategory() {
        return category;
    }

    /**
     * 任务删除标志
     * 
     * @return
     */
    public abstract boolean isTaskCancle();

    /**
     * 关闭采集组内的某个指标
     * 
     * @param targetName
     * @param data
     */
    public abstract void shutdownTarget(String targetName, Object data);

    /**
     * 开启采集组内的某个指标
     * 
     * @param targetName
     * @param data
     */
    public abstract void startUpTarget(String targetName, Object data);

    /**
     * 监视器设备实例的唯一key
     * 
     * @return identifyKey
     */
    public abstract long getIdentifyKey();

    /**
     * 设置任务唯一key
     * 
     * @param identifyKey
     * @return
     */
    public abstract void setIdentifyKey(Long identifyKey);

    /**
     * 获取所有需要采集的oid列表
     * 
     * @return String[]
     */
    public abstract String[] makeOids();

    /**
     * 获得所有需要采集的对象列表
     * 
     * @return
     */
    public abstract PerformanceDomain[] makeObjects();

    /**
     * 设置typeId
     * @param typeId
     */
    public void setDeviceTypeId(Long typeId) {

    }

    /**
     * 通过传递一批新加入的oid 构造需要执行的新的oid
     * 
     * @param monitorId
     *            监视器id
     * @param newOids
     *            一批新加入的oid
     * @param action
     *            添加还是减少采集指标
     * @return 挂接的monitor列表是否为空
     */
    public boolean makeOids(long monitorId, String[] newOids, int action) {
        Set<String> re = new HashSet<String>();
        if (action == ScheduleMessage.INSERT) {
            re.addAll(Arrays.asList(oids));
            re.addAll(Arrays.asList(newOids));
            monitorIdList.add(monitorId);
        } else if (action == ScheduleMessage.DELETE) {
            if (monitorIdList.contains(monitorId)) {
                re.addAll(Arrays.asList(oids));
                Set<String> newOid = new HashSet<String>();
                newOid.addAll(Arrays.asList(newOids));
                for (Iterator<String> iterator = re.iterator(); iterator.hasNext();) {
                    String oid = iterator.next();
                    if (newOid.contains(oid)) {
                        iterator.remove();
                    }
                }
                monitorIdList.remove(monitorId);
            }
        }
        oids = new String[re.size()];
        int i = 0;
        for (String oid : re) {
            oids[i++] = oid;
        }
        return monitorIdList.isEmpty();
    }

    /**
     * 通过传递一批新的Domain对象构成新的采集
     * 
     * 
     * @param monitorId
     * @param newDomains
     * @param action
     * @return
     */
    public boolean makeDomains(long monitorId, PerformanceDomain[] newDomains, int action) {
        List<PerformanceDomain> re = new ArrayList<PerformanceDomain>();
        if (action == ScheduleMessage.INSERT) {
            if (domains != null) {
                re.addAll(Arrays.asList(domains));
            }
            re.addAll(Arrays.asList(newDomains));
        } else if (action == ScheduleMessage.DELETE) {
            re.addAll(Arrays.asList(domains));
            List<PerformanceDomain> ne = new ArrayList<PerformanceDomain>();
            ne.addAll(Arrays.asList(newDomains));
            for (PerformanceDomain newDomain : ne) {
                for (PerformanceDomain reDomain : re) {
                    if (newDomain.compare(reDomain)) {
                        re.remove(reDomain);
                        break;
                    }
                }
            }
        }
        domains = new PerformanceDomain[re.size()];
        int i = 0;
        for (PerformanceDomain object : re) {
            domains[i++] = object;
        }
        return monitorIdList.isEmpty();
    }

    /**
     * @return the oids
     */
    public String[] getOids() {
        return oids;
    }

    /**
     * @param oids
     *            the oids to set
     */
    public void setOids(String[] oids) {
        this.oids = oids;
    }

    /**
     * @return the domains
     */
    public PerformanceDomain[] getDomains() {
        return domains;
    }

    /**
     * @param domains
     *            the domains to set
     */
    public void setDomains(PerformanceDomain[] domains) {
        this.domains = domains;
    }

    /**
     * @return the monitorId
     */
    public Long getMonitorId() {
        return monitorId;
    }

    /**
     * @param monitorId the monitorId to set
     */
    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }

    /**
     * @return the previousFireTime
     */
    public Long getPreviousFireTime() {
        return previousFireTime;
    }

    /**
     * @param previousFireTime the previousFireTime to set
     */
    public void setPreviousFireTime(Long previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    /**
     * @return the nextFireTime
     */
    public Long getNextFireTime() {
        return nextFireTime;
    }

    /**
     * @param nextFireTime the nextFireTime to set
     */
    public void setNextFireTime(Long nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public final Long getEntityId() {
        return entityId;
    }

    public final void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public final void setPerfService(String perfService) {
        this.perfService = perfService;
    }

    public final void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public final void setCategory(String category) {
        this.category = category;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

}
