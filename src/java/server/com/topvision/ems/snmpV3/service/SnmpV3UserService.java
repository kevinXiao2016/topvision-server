/***********************************************************************
 * $Id: SnmpV3UserService.java,v1.0 2013-1-9 下午2:17:48 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Access;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Group;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3View;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013-1-9-下午2:17:48
 * 
 */
public interface SnmpV3UserService extends Service {
    /**
     * 加载用户列表
     * 
     * @param entityId
     * @return
     * @throws SQLException
     */
    List<UsmSnmpV3User> loadSnmpV3UserList(Long entityId) throws SQLException;

    /**
     * 加载Access列表
     * 
     * @param entityId
     * @param snmpSecurityLevel 
     * @return
     * @throws SQLException
     */
    List<VacmSnmpV3Access> loadSnmpV3AccessList(Long entityId, Integer snmpSecurityLevel) throws SQLException;

    /**
     * 加载视图列表
     * 
     * @param entityId
     * @return
     * @throws SQLException
     */
    List<VacmSnmpV3View> loadSnmpV3ViewList(Long entityId) throws SQLException;

    /**
     * 刷新v3配置信息，用户，组，视图全部刷新
     * 
     * @param entityId
     * @throws Exception 
     */
    void refreshSnmpV3Config(Long entityId) throws Exception;

    void addSnmpV3User(UsmSnmpV3User snmpV3User) throws SQLException;

    void addSnmpV3Group(Long entityId, VacmSnmpV3Group snmpV3Group) throws SQLException;

    void addSnmpV3View(Long entityId, VacmSnmpV3View snmpV3View);

    void deleteSnmpV3User(Long entityId, String v3Username);

    void deleteSnmpV3Group(Long entityId, String v3Groupname);

    void deleteSnmpV3View(Long entityId, String v3Viewname);

    void modifySnmpV3User(Long entityId, UsmSnmpV3User snmpV3User);

    void modifySnmpV3Group(Long entityId, VacmSnmpV3Group snmpV3Group);

    void modifySnmpV3View(Long entityId, VacmSnmpV3View snmpV3View);

    String tryUserValid(Long entityId, UsmSnmpV3User snmpV3User);

    void modifySnmpVersion(Long entityId, int snmpVersion, UsmSnmpV3User snmpV3User);

    VacmSnmpV3Access loadSnmpAccessInfo(Map<String, String> map) throws SQLException;

    void addView(Long entityId, String snmpViewName, String snmpViewSubtree, Integer snmpViewMode) throws SQLException;

    void addAccess(Long entityId, VacmSnmpV3Access access) throws SQLException;

    void deleteView(Long entityId, String snmpViewName, String snmpViewSubtree) throws SQLException;

    void deleteAccess(Long entityId, VacmSnmpV3Access access) throws SQLException;

    void deleteUser(Long entityId, String snmpUserName, String snmpUserEngineId, String snmpGroupName)
            throws SQLException;

    UsmSnmpV3User cloneUser(Long entityId, UsmSnmpV3User user);

    SnmpParam loadEntitySnmpConfig(Long entityId);

    UsmSnmpV3User querySnmpV3UserInfo(Long entityId, String snmpUserName, String snmpUserEngineId) throws SQLException;

    void activateUser(Long entityId, UsmSnmpV3User user);

    void updateSnmpVersion(Long entityId, Integer snmpVersion);


    String getAgentEngineId(Long entityId);

    List<UsmSnmpV3User> getAvaiableCloneUserList(Long entityId, String snmpUserName, String snmpUserEngineId)
            throws SQLException;

    void modifyAccess(Long entityId, VacmSnmpV3Access access) throws SQLException;

    void modifyView(Long entityId, String snmpViewName, String snmpViewSubtree, Integer snmpViewMode,
            String snmpPrvSubtree) throws SQLException;

    void modifyUser(UsmSnmpV3User snmpV3User) throws SQLException;

    VacmSnmpV3View loadSnmpViewInfo(Long entityId, String snmpViewName, String snmpViewSubtree) throws SQLException;

    List<UsmSnmpV3User> getAvaiableCloneEngineList(Long entityId, String snmpUserName) throws SQLException;

    List<VacmSnmpV3View> loadSnmpV3ViewNameList(Long entityId) throws SQLException;

}
