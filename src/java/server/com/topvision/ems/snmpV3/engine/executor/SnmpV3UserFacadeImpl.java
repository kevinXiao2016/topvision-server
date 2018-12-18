/***********************************************************************
 * $Id: SnmpV3UserFacadeImpl.java,v1.0 2013-1-9 下午2:23:46 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.engine.executor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.snmpV3.dao.SnmpV3Dao;
import com.topvision.ems.snmpV3.facade.SnmpV3UserFacade;
import com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Access;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Group;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3View;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author Bravin
 * @created @2013-1-9-下午2:23:46
 * 
 */
@Engine("snmpV3UserFacade")
public class SnmpV3UserFacadeImpl extends EmsFacade implements SnmpV3UserFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;
    private SnmpV3Dao snmpV3Dao;
    private FacadeFactory facadeFactory;
    private EntityService entityService;
    private static final Integer STORAGE_TYPE = 3;

    /**
     * @return the snmpV3Dao
     */
    public SnmpV3Dao getSnmpV3Dao() {
        return snmpV3Dao;
    }

    /**
     * @param snmpV3Dao
     *            the snmpV3Dao to set
     */
    public void setSnmpV3Dao(SnmpV3Dao snmpV3Dao) {
        this.snmpV3Dao = snmpV3Dao;
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
     * @return the entityService
     */
    public EntityService getEntityService() {
        return entityService;
    }

    /**
     * @param entityService
     *            the entityService to set
     */
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    /**
     * @return the snmpExecutorService
     */
    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    /**
     * @param snmpExecutorService
     *            the snmpExecutorService to set
     */
    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3UserFacade#addSnmpV3User(com.topvision.framework.snmp
     * .SnmpParam, com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User)
     */
    @Override
    public void addSnmpV3User(SnmpParam snmpParam, UsmSnmpV3User snmpV3User) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3UserFacade#addSnmpV3Group(com.topvision.framework.snmp
     * .SnmpParam, com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Group)
     */
    @Override
    public void addSnmpV3Group(SnmpParam snmpParam, VacmSnmpV3Group snmpV3Group) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3UserFacade#addSnmpV3Access(com.topvision.framework.
     * snmp.SnmpParam, com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Group)
     */
    @Override
    public void addSnmpV3Access(SnmpParam snmpParam, VacmSnmpV3Group snmpV3Access) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3UserFacade#addSnmpV3View(com.topvision.framework.snmp
     * .SnmpParam, com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3View)
     */
    @Override
    public void addSnmpV3View(SnmpParam snmpParam, VacmSnmpV3View snmpV3View) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3UserFacade#deleteSnmpV3User(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User)
     */
    @Override
    public void deleteSnmpV3User(SnmpParam snmpParam, UsmSnmpV3User snmpV3User) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3UserFacade#deleteSnmpV3Group(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Group)
     */
    @Override
    public void deleteSnmpV3Group(SnmpParam snmpParam, VacmSnmpV3Group snmpV3Group) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3UserFacade#deleteSnmpV3Access(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Access)
     */
    @Override
    public void deleteSnmpV3Access(SnmpParam snmpParam, VacmSnmpV3Access snmpV3Access) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3UserFacade#deleteSnmpV3View(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3View)
     */
    @Override
    public void deleteSnmpV3View(SnmpParam snmpParam, VacmSnmpV3View snmpV3View) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3UserFacade#modifySnmpV3User(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User)
     */
    @Override
    public void setSnmpV3User(SnmpParam snmpParam, UsmSnmpV3User snmpV3User) {
        snmpExecutorService.setData(snmpParam, snmpV3User);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3UserFacade#testUserValid(com.topvision.framework.snmp
     * .SnmpParam, com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User)
     */
    @Override
    public String testUserValid(SnmpParam snmpParam, UsmSnmpV3User snmpV3User) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3UserFacade#ModifySnmpVersion(com.topvision.framework
     * .snmp.SnmpParam, int)
     */
    @Override
    public String ModifySnmpVersion(SnmpParam snmpParam, Integer snmpVersion) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3UserFacade#refreshSnmpV3UserList(com.topvision.framework
     * .snmp.SnmpParam)
     */
    @Override
    public List<UsmSnmpV3User> refreshSnmpV3UserList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, UsmSnmpV3User.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3UserFacade#refreshSnmpV3GroupList(com.topvision.framework
     * .snmp.SnmpParam)
     */
    @Override
    public List<VacmSnmpV3Group> refreshSnmpV3GroupList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, VacmSnmpV3Group.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3UserFacade#refreshSnmpV3AccessList(com.topvision.framework
     * .snmp.SnmpParam)
     */
    @Override
    public List<VacmSnmpV3Access> refreshSnmpV3AccessList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, VacmSnmpV3Access.class);
    }

    @Override
    public List<VacmSnmpV3View> refreshSnmpV3ViewList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, VacmSnmpV3View.class);
    }

    @Override
    public VacmSnmpV3View createView(SnmpParam snmpParam, VacmSnmpV3View view) {
        view.setSnmpViewStorageType(STORAGE_TYPE);
        view.setSnmpViewStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, view);
    }

    @Override
    public VacmSnmpV3Access createAccess(SnmpParam snmpParam, VacmSnmpV3Access access) {
        access.setSnmpAccessStorageType(STORAGE_TYPE);
        access.setSnmpAccessStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, access);
    }

    @Override
    public VacmSnmpV3View destoryView(SnmpParam snmpParam, VacmSnmpV3View view) {
        view.setSnmpViewStatus(RowStatus.DESTORY);
        return snmpExecutorService.setData(snmpParam, view);
    }

    @Override
    public VacmSnmpV3Access destoryAccess(SnmpParam snmpParam, VacmSnmpV3Access access) {
        access.setSnmpAccessStatus(RowStatus.DESTORY);
        return snmpExecutorService.setData(snmpParam, access);
    }

    @Override
    public String getSysObjectId(SnmpParam snmpParam) {
        String snmpVersionOid = "1.3.6.1.2.1.1.2.0";
        return snmpExecutorService.get(snmpParam, snmpVersionOid);
    }

    @Override
    public String getSnmpVersion(SnmpParam snmpParam) {
        String snmpVersionOid = "1.3.6.1.4.1.32285.11.2.3.1.1.3.0";
        return snmpExecutorService.get(snmpParam, snmpVersionOid);
    }

    @Override
    public void setSnmpVersion(SnmpParam snmpParam, String version) {
        String snmpVersionOid = "1.3.6.1.4.1.32285.11.2.3.1.1.3.0";
        snmpExecutorService.set(snmpParam, snmpVersionOid, version);
    }

    @Override
    public UsmSnmpV3User cloneUser(SnmpParam snmpParam, UsmSnmpV3User user) {
        user.setUsmUserStorageType(STORAGE_TYPE);
        user.setUsmUserStatus(RowStatus.CREATE_AND_WAIT);
        return snmpExecutorService.setData(snmpParam, user);
    }

    @Override
    public UsmSnmpV3User activateUser(SnmpParam snmpParam, UsmSnmpV3User user) {
        user.setUsmUserStatus(RowStatus.ACTIVE);
        // user.setUsmUserStatus(null);
        user.setUsmUserStorageType(null);
        return snmpExecutorService.setData(snmpParam, user);
    }

    @Override
    public VacmSnmpV3Group createSnmpV3Group(SnmpParam snmpParam, VacmSnmpV3Group snmpV3Group) {
        snmpV3Group.setSnmpGroupStorageType(STORAGE_TYPE);
        snmpV3Group.setSnmpGroupStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, snmpV3Group);
    }

    @Override
    public String getAgentEngineId(SnmpParam snmpParam) {
        String agentEngineOid = "1.3.6.1.6.3.10.2.1.1.0";
        return snmpExecutorService.get(snmpParam, agentEngineOid).toUpperCase();
    }

    @Override
    public void destoryUser(SnmpParam snmpParam, UsmSnmpV3User user) {
        user.setUsmUserStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, user);
    }

    @Override
    public void destoryGroup(SnmpParam snmpParam, VacmSnmpV3Group group) {
        group.setSnmpGroupStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, group);
    }

    @Override
    public void setAccess(SnmpParam snmpParam, VacmSnmpV3Access access) {
        snmpExecutorService.setData(snmpParam, access);
    }

    @Override
    public void setView(SnmpParam snmpParam, VacmSnmpV3View view) {
        view.setSnmpViewStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, view);
    }

    @Override
    public void setSnmpV3Group(SnmpParam snmpParam, VacmSnmpV3Group snmpV3Group) {
        snmpExecutorService.setData(snmpParam, snmpV3Group);
    }

    @Override
    public String getSnmpV3UserPulic(SnmpParam snmpParam, String oid) {
        return snmpExecutorService.get(snmpParam, oid);
    }

}
