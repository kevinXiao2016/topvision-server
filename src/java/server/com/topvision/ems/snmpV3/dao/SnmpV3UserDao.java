/***********************************************************************
 * $Id: SnmpV3UserDao.java,v1.0 2013-1-9 下午2:20:52 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topvision.ems.snmpV3.domain.SnmpV3Group;
import com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Access;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Group;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3View;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.framework.domain.BaseEntity;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013-1-9-下午2:20:52
 * 
 */
public interface SnmpV3UserDao extends BaseEntityDao<BaseEntity> {
    List<UsmSnmpV3User> selectSnmpV3UserList(Long entityId) throws SQLException;

    List<VacmSnmpV3Access> selectSnmpV3AccessList(Long entityId, Integer snmpSecurityLevel) throws SQLException;

    List<VacmSnmpV3View> selectSnmpV3ViewList(Long entityId) throws SQLException;

    void batchInsertSnmpV3UserList(Long entityId, List<UsmSnmpV3User> snmpV3UserList);

    void batchUpdateSnmpV3GroupList(Long entityId, List<VacmSnmpV3Group> snmpV3GroupList);

    void batchInsertSnmpV3AccessList(Long entityId, List<VacmSnmpV3Access> snmpV3AccessList);

    void batchInsertSnmpV3ViewList(Long entityId, List<VacmSnmpV3View> snmpV3ViewList);

    void deleteSnmpV3User(Long entityId, String v3Username);

    void deleteSnmpV3Group(Long entityId, String v3GroupName);

    void deleteSnmpV3View(Long entityId, String v3ViewName);

    void updateSnmpV3UserParameters(Long entityId, UsmSnmpV3User snmpV3User) throws SQLException;

    void updateSnmpV3Group(Long entityId, SnmpV3Group snmpV3Group);

    void updateSnmpV3View(Long entityId, VacmSnmpV3View snmpV3View);

    void updateSnmpVersion(Long entityId, int snmpVersion, UsmSnmpV3User snmpV3User);

    void selectManagerV3UserInfo(Long entityId);

    void updateManagerV3UserInfo(Long entityId, SnmpParam user);

    VacmSnmpV3Access selectSnmpAccessInfo(Map<String, String> map) throws SQLException;

    void deleteView(VacmSnmpV3View view) throws SQLException;

    void deleteAccess(VacmSnmpV3Access access) throws SQLException;

    void deleteUser(UsmSnmpV3User user) throws SQLException;

    void insertVacmAccess(VacmSnmpV3Access access) throws SQLException;

    void insertVacmView(VacmSnmpV3View view) throws SQLException;

    UsmSnmpV3User selectUserByName(Long entityId, String snmpUserName, String snmpUserEngineId) throws SQLException;

    void insertSnmpV3User(UsmSnmpV3User snmpV3User) throws SQLException;

    void updateVacmGroup(VacmSnmpV3Group vacmGroup) throws SQLException;

    List<UsmSnmpV3User> queryAvaiableCloneUserList(UsmSnmpV3User user) throws SQLException;

    void updateVacmAccess(VacmSnmpV3Access access) throws SQLException;

    void updateSnmpV3User(UsmSnmpV3User snmpV3User) throws SQLException;

    VacmSnmpV3View querySnmpViewInfo(VacmSnmpV3View view) throws SQLException;

    List<UsmSnmpV3User> queryAvaiableCloneEngineList(UsmSnmpV3User user) throws SQLException;

    List<VacmSnmpV3View> selectSnmpV3ViewNameList(Long entityId) throws SQLException;

}
