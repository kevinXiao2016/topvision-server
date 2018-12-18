/***********************************************************************
 * $Id: SnmpV3FacadeImpl.java,v1.0 2013-1-9 上午9:52:17 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.engine.executor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.snmpV3.facade.SnmpV3Facade;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterProfile;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterTable;
import com.topvision.ems.snmpV3.facade.domain.SnmpNotifyTable;
import com.topvision.ems.snmpV3.facade.domain.SnmpTargetParams;
import com.topvision.ems.snmpV3.facade.domain.SnmpTargetTable;
import com.topvision.framework.annotation.Engine;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013-1-9-上午9:52:17
 * 
 */
@Engine("snmpV3Facade")
public class SnmpV3FacadeImpl extends EmsFacade implements SnmpV3Facade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

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
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#addTarget(com.topvision.
     * framework.snmp.SnmpParam,
     * com.topvision.ems.snmpV3.facade.domain.SnmpTargetTable)
     */
    @Override
    public void addTarget(SnmpParam snmpParam, SnmpTargetTable snmpTargetTable) {
        snmpTargetTable.setTargetRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, snmpTargetTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#modifyTarget(com.topvision
     * .framework.snmp.SnmpParam,
     * com.topvision.ems.snmpV3.facade.domain.SnmpTargetTable)
     */
    @Override
    public void modifyTarget(SnmpParam snmpParam, SnmpTargetTable snmpTargetTable) {
        // TODO 暂时有RowStatus.ACTIVE时不能修改的限制,若mib修改后再修改
        snmpTargetTable.setTargetRowStatus(RowStatus.NOT_IN_SERVICE);
        snmpExecutorService.setData(snmpParam, snmpTargetTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#deleteTarget(com.topvision
     * .framework.snmp.SnmpParam, java.lang.String)
     */
    @Override
    public void deleteTarget(SnmpParam snmpParam, String targetName) {
        SnmpTargetTable snmpTargetTable = new SnmpTargetTable();
        snmpTargetTable.setTargetName(targetName);
        snmpTargetTable.setTargetRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, snmpTargetTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#refreshTarget(com.topvision
     * .framework.snmp.SnmpParam)
     */
    @Override
    public List<SnmpTargetTable> refreshTarget(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, SnmpTargetTable.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#addTargetParams(com.topvision
     * .framework.snmp.SnmpParam,
     * com.topvision.ems.snmpV3.facade.domain.SnmpTargetParams)
     */
    @Override
    public void addTargetParams(SnmpParam snmpParam, SnmpTargetParams snmpTargetParams) {
        snmpTargetParams.setTargetParamsRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, snmpTargetParams);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#modifyTargetParams(com.topvision
     * .framework.snmp.SnmpParam,
     * com.topvision.ems.snmpV3.facade.domain.SnmpTargetParams)
     */
    @Override
    public void modifyTargetParams(SnmpParam snmpParam, SnmpTargetParams snmpTargetParams) {
        // TODO 暂时有RowStatus.ACTIVE时不能修改的限制,若mib修改后再修改
        snmpTargetParams.setTargetParamsRowStatus(RowStatus.NOT_IN_SERVICE);
        snmpExecutorService.setData(snmpParam, snmpTargetParams);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#deleteTargetParams(com.topvision
     * .framework.snmp.SnmpParam, java.lang.String)
     */
    @Override
    public void deleteTargetParams(SnmpParam snmpParam, String targetParamsName) {
        SnmpTargetParams snmpTargetParams = new SnmpTargetParams();
        snmpTargetParams.setTargetParamsName(targetParamsName);
        snmpTargetParams.setTargetParamsRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, snmpTargetParams);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#refreshTargetParams(com.
     * topvision.framework.snmp.SnmpParam)
     */
    @Override
    public List<SnmpTargetParams> refreshTargetParams(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, SnmpTargetParams.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#addNotify(com.topvision.
     * framework.snmp.SnmpParam,
     * com.topvision.ems.snmpV3.facade.domain.SnmpNotifyTable)
     */
    @Override
    public void addNotify(SnmpParam snmpParam, SnmpNotifyTable snmpNotifyTable) {
        snmpNotifyTable.setNotifyRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, snmpNotifyTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#modifyNotify(com.topvision
     * .framework.snmp.SnmpParam,
     * com.topvision.ems.snmpV3.facade.domain.SnmpNotifyTable)
     */
    @Override
    public void modifyNotify(SnmpParam snmpParam, SnmpNotifyTable snmpNotifyTable) {
        // TODO 暂时有RowStatus.ACTIVE时不能修改的限制,若mib修改后再修改
        snmpNotifyTable.setNotifyRowStatus(RowStatus.NOT_IN_SERVICE);
        snmpExecutorService.setData(snmpParam, snmpNotifyTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#delNotify(com.topvision.
     * framework.snmp.SnmpParam, java.lang.String)
     */
    @Override
    public void deleteNotify(SnmpParam snmpParam, String notifyName) {
        SnmpNotifyTable snmpNotifyTable = new SnmpNotifyTable();
        snmpNotifyTable.setNotifyName(notifyName);
        snmpNotifyTable.setNotifyRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, snmpNotifyTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#refreshNotify(com.topvision
     * .framework.snmp.SnmpParam)
     */
    @Override
    public List<SnmpNotifyTable> refreshNotify(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, SnmpNotifyTable.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#addNotifyProfile(com.topvision
     * .framework.snmp.SnmpParam,
     * com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterProfile)
     */
    @Override
    public void addNotifyProfile(SnmpParam snmpParam, SnmpNotifyFilterProfile snmpNotifyFilterProfile) {
        snmpNotifyFilterProfile.setNotifyFilterProfileRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, snmpNotifyFilterProfile);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#modifyNotifyProfile(com.
     * topvision.framework.snmp.SnmpParam,
     * com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterProfile)
     */
    @Override
    public void modifyNotifyProfile(SnmpParam snmpParam, SnmpNotifyFilterProfile snmpNotifyFilterProfile) {
        // TODO 暂时有RowStatus.ACTIVE时不能修改的限制,若mib修改后再修改
        snmpNotifyFilterProfile.setNotifyFilterProfileRowStatus(RowStatus.NOT_IN_SERVICE);
        snmpExecutorService.setData(snmpParam, snmpNotifyFilterProfile);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#deleteNotifyProfile(com.
     * topvision.framework.snmp.SnmpParam,
     * com.topvision.ems.snmpV3.facade.domain.SnmpNotifyFilterProfile)
     */
    @Override
    public void deleteNotifyProfile(SnmpParam snmpParam, SnmpNotifyFilterProfile snmpNotifyFilterProfile) {
        snmpNotifyFilterProfile.setNotifyFilterProfileRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, snmpNotifyFilterProfile);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#refreshNotifyProfile(com
     * .topvision.framework.snmp.SnmpParam)
     */
    @Override
    public List<SnmpNotifyFilterProfile> refreshNotifyProfile(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, SnmpNotifyFilterProfile.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#addNotifyFilter(com.topvision
     * .framework.snmp.SnmpParam,
     * com.topvision.ems.snmpV3.facade.domain.SnmpNotifyTable)
     */
    @Override
    public void addNotifyFilter(SnmpParam snmpParam, SnmpNotifyFilterTable snmpNotifyFilterTable) {
        snmpNotifyFilterTable.setNotifyFilterRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, snmpNotifyFilterTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#modifyNotifyFilter(com.topvision
     * .framework.snmp.SnmpParam,
     * com.topvision.ems.snmpV3.facade.domain.SnmpNotifyTable)
     */
    @Override
    public void modifyNotifyFilter(SnmpParam snmpParam, SnmpNotifyFilterTable snmpNotifyFilterTable) {
        // TODO 暂时有RowStatus.ACTIVE时不能修改的限制,若mib修改后再修改
        snmpNotifyFilterTable.setNotifyFilterRowStatus(RowStatus.NOT_IN_SERVICE);
        snmpExecutorService.setData(snmpParam, snmpNotifyFilterTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#deleteNotifyFilter(com.topvision
     * .framework.snmp.SnmpParam, java.lang.String)
     */
    @Override
    public void deleteNotifyFilter(SnmpParam snmpParam, String notifyFilterProfileName, String notifyFilterSubtree) {
        SnmpNotifyFilterTable snmpNotifyFilterTable = new SnmpNotifyFilterTable();
        snmpNotifyFilterTable.setNotifyFilterProfileName(notifyFilterProfileName);
        snmpNotifyFilterTable.setNotifyFilterSubtree(notifyFilterSubtree);
        snmpNotifyFilterTable.setNotifyFilterRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, snmpNotifyFilterTable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.snmpV3.facade.SnmpV3Facade#refreshNotifyFilter(com.
     * topvision.framework.snmp.SnmpParam)
     */
    @Override
    public List<SnmpNotifyFilterTable> refreshNotifyFilter(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, SnmpNotifyFilterTable.class);
    }

}
