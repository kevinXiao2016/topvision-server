/***********************************************************************
 * $Id: SnmpV3UserServiceImpl.java,v1.0 2013-1-9 下午2:19:27 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.snmpV3.service.impl;

import java.rmi.UnexpectedException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.OnlineService;
import com.topvision.ems.snmpV3.dao.SnmpV3UserDao;
import com.topvision.ems.snmpV3.domain.SnmpAgentVersion;
import com.topvision.ems.snmpV3.facade.SnmpV3UserFacade;
import com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Access;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Group;
import com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3View;
import com.topvision.ems.snmpV3.service.SnmpV3UserService;
import com.topvision.ems.snmpV3.util.SnmpV3Util;
import com.topvision.framework.common.LoggerUtil;
import com.topvision.framework.exception.engine.SnmpNoResponseException;
import com.topvision.framework.exception.engine.SnmpV3Exception;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Bravin
 * @created @2013-1-9-下午2:19:27
 * 
 */
@Service("snmpV3UserService")
public class SnmpV3UserServiceImpl extends BaseService implements SnmpV3UserService, SynchronizedListener {
    private static final String NO_SUCH_OBJECT_MARKER = "noSuchObject";
    /**
     * 打开用户列表界面以及测试用户时的超时时间，由于时间太长的话用户不好，所以设置较短
     */
    private static final long DEFAULT_TESTUSER_TIMEOUT = 500;
    @Autowired
    private SnmpV3UserDao snmpV3UserDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OnlineService onlineService;

    @PreDestroy
    public void destory() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        Long timeTmp = 0L;

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "refreshSnmpV3Config");
        try {
            refreshSnmpV3Config(event.getEntityId());
            logger.info("refreshSnmpV3Config finished ");
        } catch (Exception e) {
            logger.error("refreshSnmpV3Config failed :", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "refreshSnmpV3Config", timeTmp);

    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3UserService#loadSnmpV3UserList(java.lang.Long)
     */
    @Override
    public List<UsmSnmpV3User> loadSnmpV3UserList(Long entityId) throws SQLException {
        return snmpV3UserDao.selectSnmpV3UserList(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3UserService#loadSnmpV3ViewList(java.lang.Long)
     */
    @Override
    public List<VacmSnmpV3View> loadSnmpV3ViewList(Long entityId) throws SQLException {
        return snmpV3UserDao.selectSnmpV3ViewList(entityId);
    }

    @Override
    public List<VacmSnmpV3View> loadSnmpV3ViewNameList(Long entityId) throws SQLException {
        return snmpV3UserDao.selectSnmpV3ViewNameList(entityId);
    }

    @Override
    public VacmSnmpV3Access loadSnmpAccessInfo(Map<String, String> map) throws SQLException {
        return snmpV3UserDao.selectSnmpAccessInfo(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3UserService#refreshSnmpV3Config(java.lang.Long)
     */
    @Override
    public void refreshSnmpV3Config(Long entityId) throws Exception {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        SnmpV3UserFacade facade = getFacade(snmpParam.getIpAddress());
        // snmpv3 user list
        try {
            List<UsmSnmpV3User> snmpV3UserList = facade.refreshSnmpV3UserList(snmpParam);
            snmpV3UserDao.batchInsertSnmpV3UserList(entityId, snmpV3UserList);
        } catch (Exception e) {
            logger.error("refresh snmpV3UserList error:{}", e);
            throw e;
        }
        // snmp v3 group list
        try {
            List<VacmSnmpV3Group> snmpV3GroupList = facade.refreshSnmpV3GroupList(snmpParam);
            snmpV3UserDao.batchUpdateSnmpV3GroupList(entityId, snmpV3GroupList);
        } catch (Exception e) {
            logger.error("refresh snmpV3GroupList error:{}", e);
            throw e;
        }
        // snmp v3 access list
        try {
            List<VacmSnmpV3Access> snmpV3AccessList = facade.refreshSnmpV3AccessList(snmpParam);
            snmpV3UserDao.batchInsertSnmpV3AccessList(entityId, snmpV3AccessList);
        } catch (Exception e) {
            logger.error("refresh snmpV3AccessList error:{}", e);
            throw e;
        }
        // snmp v3 view list
        try {
            List<VacmSnmpV3View> snmpV3ViewList = facade.refreshSnmpV3ViewList(snmpParam);
            snmpV3UserDao.batchInsertSnmpV3ViewList(entityId, snmpV3ViewList);
        } catch (Exception e) {
            logger.error("refresh snmpV3ViewList error:{}", e);
            throw e;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3UserService#addSnmpV3User(
     * com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User)
     */
    @Override
    public void addSnmpV3User(UsmSnmpV3User snmpV3User) throws SQLException {
        snmpV3UserDao.insertSnmpV3User(snmpV3User);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3UserService#addSnmpV3Group(java.lang.Long,
     * com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Group)
     */
    @Override
    public void addSnmpV3Group(Long entityId, VacmSnmpV3Group snmpV3Group) throws SQLException {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        SnmpV3UserFacade facade = getFacade(snmpParam.getIpAddress());
        facade.createSnmpV3Group(snmpParam, snmpV3Group);
        // UPDATE GROUP
        snmpV3UserDao.updateVacmGroup(snmpV3Group);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3UserService#addSnmpV3View(java.lang.Long,
     * com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3View)
     */
    @Override
    public void addSnmpV3View(Long entityId, VacmSnmpV3View snmpV3View) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3UserService#deleteSnmpV3User(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public void deleteSnmpV3User(Long entityId, String v3Username) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3UserService#deleteSnmpV3Group(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public void deleteSnmpV3Group(Long entityId, String v3Groupname) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3UserService#deleteSnmpV3View(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public void deleteSnmpV3View(Long entityId, String v3Viewname) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3UserService#modifySnmpV3User(java.lang.Long,
     * com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User)
     */
    @Override
    public void modifySnmpV3User(Long entityId, UsmSnmpV3User snmpV3User) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        SnmpV3UserFacade facade = getFacade(snmpParam.getIpAddress());
        // String oid = UsmSnmpV3User.getUserPublicOid(snmpV3User.getSnmpUserEngineId(),
        // snmpV3User.getSnmpUserName());
        // String snmpV3UserPulicBefore = facade.getSnmpV3UserPulic(snmpParam, oid);
        // 如果manager当前使用这个账户和设备通信，则要通过修改ownKeyChange来修改密码
        if (snmpParam.getVersion() == SnmpConstants.version3) {
            if (snmpV3User.getSnmpUserName().equals(snmpParam.getUsername())) {
                snmpV3User.setSnmpOwnAuthKeyChange(snmpV3User.getSnmpAuthKeyChange());
                snmpV3User.setSnmpOwnPrivKeyChange(snmpV3User.getSnmpPrivKeyChange());
            }
        }
        // snmpV3User.setSnmpUserPublic("test");
        facade.setSnmpV3User(snmpParam, snmpV3User);
        // String snmpV3UserPulicAfter = facade.getSnmpV3UserPulic(snmpParam, oid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3UserService#modifySnmpV3Group(java.lang.Long,
     * com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3Group)
     */
    @Override
    public void modifySnmpV3Group(Long entityId, VacmSnmpV3Group snmpV3Group) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        SnmpV3UserFacade facade = getFacade(snmpParam.getIpAddress());
        facade.setSnmpV3Group(snmpParam, snmpV3Group);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3UserService#modifySnmpV3View(java.lang.Long,
     * com.topvision.ems.snmpV3.facade.domain.VacmSnmpV3View)
     */
    @Override
    public void modifySnmpV3View(Long entityId, VacmSnmpV3View snmpV3View) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3UserService#tryUserValid(java.lang.Long,
     * com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User)
     */
    @Override
    public String tryUserValid(Long entityId, UsmSnmpV3User snmpV3User) {
        String result = "valid";
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        SnmpV3UserFacade facade = getFacade(snmpParam.getIpAddress());
        //Modify by Victor@20131109把直接调用NativePing改为调用onlineService，onlineService会从采集端ping，直接调用NativePing从Server，有些部署方式不支持
        Integer delay = onlineService.ping(snmpParam.getIpAddress());
        // ping不通
        if (delay == -1) {
            return "timeout";
        }
        try {
            String version = facade.getSnmpVersion(snmpParam);
            snmpParam.setTimeout(DEFAULT_TESTUSER_TIMEOUT);
            int versionInt = Integer.parseInt(version);
            // v2访问的时候能取到version并且还要支持V3的只有all模式下，因为网管使用v2去取设备version，不可能取到v3。所以只要取回来的不是ALL就认定版本不对
            if (SnmpAgentVersion.V1 == versionInt || SnmpAgentVersion.V2 == versionInt) {
                return "wrongVersion";
            }
        } catch (Exception e) {
            // 实际上这个result并没有其作用，由于超时的可能性较复杂，难以判断，故这个结果没有继续被利用
            // 如果当前使用的V3参数本身也是错误的，导致无法获取Version也会出此异常，但被测账户是正常的
            // result = "v2Wrong";
            logger.trace("testing snmp version : {}", e);
        }
        // 由于在设备不通的情况下怎么测都不会通，所以干脆去掉判断，直接下发，看是否成功
        try {
            // 由于设备的SNMP版本在测试时只会切换到ALL模式下，所以原始的snmpParam一定要保留，防止出错了没法恢复
            SnmpParam trySnmpParam = new SnmpParam();
            trySnmpParam.setIpAddress(snmpParam.getIpAddress());
            trySnmpParam.setVersion(SnmpConstants.version3);
            trySnmpParam.setUsername(snmpV3User.getSnmpUserName());
            String authPro = snmpV3User.getSnmpAuthProtocol().toUpperCase();
            int securityLevel = SecurityLevel.NOAUTH_NOPRIV;
            String privPro = snmpV3User.getSnmpPrivProtocol().toUpperCase();
            trySnmpParam.setAuthProtocol(authPro);
            trySnmpParam.setPrivProtocol(privPro);
            if (!"NOPRIV".equals(privPro)) {
                trySnmpParam.setPrivPassword(snmpV3User.getSnmpPrivKeyChange());
                securityLevel++;
            }
            // 不存在 NOAUTH_PRIV的情况,故Priv在前
            if (!"NOAUTH".equals(authPro)) {
                trySnmpParam.setAuthPassword(snmpV3User.getSnmpAuthKeyChange());
                securityLevel++;
            } else {
                // 如果是noauth,并且是priv，那就表示出错了
                if (securityLevel > 1) {
                    throw new UnexpectedException("wrong snmp protocol config");
                }
            }
            trySnmpParam.setSecurityLevel(securityLevel);
            // 使用新的USER去访问设备,获取设备SNMP版本号，如果返回的值包含于1,2,3,4，则说明正常，否则~~
            String resp = facade.getSysObjectId(trySnmpParam);
            if (NO_SUCH_OBJECT_MARKER.equalsIgnoreCase(resp)) {
                throw new SnmpV3Exception("requestDroped");
            }
            // 只要有返回结果就人判定测试正常，就把这个用户的测试参数保存在数据库中
            snmpV3UserDao.updateSnmpV3UserParameters(entityId, snmpV3User);
        } catch (SnmpV3Exception e) {
            result = e.getMessage();
        } catch (IllegalArgumentException e) {
            result = "wrongPwd";
        } catch (SnmpNoResponseException e) {
            result = "timeout";
        } catch (Exception e) {
            result = "unsupported";
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.snmpV3.service.SnmpV3UserService#modifySnmpVersion(java.lang.Long,
     * int, com.topvision.ems.snmpV3.facade.domain.UsmSnmpV3User)
     */
    @Override
    public void modifySnmpVersion(Long entityId, int snmpVersion, UsmSnmpV3User snmpV3User) {
    }

    /**
     * @return the snmpV3UserDao
     */
    public SnmpV3UserDao getSnmpV3UserDao() {
        return snmpV3UserDao;
    }

    /**
     * @param snmpV3UserDao
     *            the snmpV3UserDao to set
     */
    public void setSnmpV3UserDao(SnmpV3UserDao snmpV3UserDao) {
        this.snmpV3UserDao = snmpV3UserDao;
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
     * @param ipAddress
     * @return
     */
    private SnmpV3UserFacade getFacade(String ipAddress) {
        return facadeFactory.getFacade(ipAddress, SnmpV3UserFacade.class);
    }

    @Override
    public void addView(Long entityId, String snmpViewName, String snmpViewSubtree, Integer snmpViewMode)
            throws SQLException {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        VacmSnmpV3View view = new VacmSnmpV3View();
        view.setEntityId(entityId);
        view.setSnmpViewName(snmpViewName);
        view.setSnmpViewMode(snmpViewMode);
        StringBuilder subtree = new StringBuilder();
        StringBuilder viewMask = new StringBuilder();
        StringTokenizer st = new StringTokenizer(snmpViewSubtree, ".");
        while (st.hasMoreElements()) {
            String token = (String) st.nextElement();
            if ("*".equalsIgnoreCase(token)) {
                viewMask.append("0");
                subtree.append("0.");
            } else {
                viewMask.append("1");
                subtree.append(token);
                subtree.append(".");
            }
        }
        subtree.deleteCharAt(subtree.length() - 1);
        view.setSnmpViewSubtree(subtree.toString());
        // viewMask.append("11111111");
        int maskValue = Integer.parseInt(viewMask.toString(), 2);
        if (maskValue != 0) {
            view.setSnmpViewMaskOct(SnmpV3Util.number2Byte(Integer.toHexString(maskValue)));
        }
        getFacade(snmpParam.getIpAddress()).createView(snmpParam, view);
        snmpV3UserDao.insertVacmView(view);
    }

    @Override
    public void addAccess(Long entityId, VacmSnmpV3Access access) throws SQLException {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getFacade(snmpParam.getIpAddress()).createAccess(snmpParam, access);
        access.setEntityId(entityId);
        snmpV3UserDao.insertVacmAccess(access);
    }

    @Override
    public void deleteView(Long entityId, String snmpViewName, String snmpViewSubtree) throws SQLException {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        VacmSnmpV3View view = new VacmSnmpV3View();
        view.setEntityId(entityId);
        view.setSnmpViewName(snmpViewName);
        StringBuilder subtree = new StringBuilder();
        StringBuilder mask = new StringBuilder();
        StringTokenizer st = new StringTokenizer(snmpViewSubtree, ".");
        while (st.hasMoreElements()) {
            String token = (String) st.nextElement();
            if ("*".equalsIgnoreCase(token)) {
                mask.append("1");
                subtree.append("0.");
            } else {
                mask.append("0");
                subtree.append(token);
                subtree.append(".");
            }
        }
        subtree.deleteCharAt(subtree.length() - 1);
        view.setSnmpViewSubtree(subtree.toString());
        getFacade(snmpParam.getIpAddress()).destoryView(snmpParam, view);
        // 将结果保存到数据库
        view.setEntityId(entityId);
        snmpV3UserDao.deleteView(view);
    }

    @Override
    public void deleteAccess(Long entityId, VacmSnmpV3Access access) throws SQLException {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getFacade(snmpParam.getIpAddress()).destoryAccess(snmpParam, access);
        // 将结果保存到数据库
        access.setEntityId(entityId);
        snmpV3UserDao.deleteAccess(access);
    }

    @Override
    public void deleteUser(Long entityId, String snmpUserName, String snmpUserEngineId, String snmpGroupName)
            throws SQLException {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // DELETE USER_TABLE
        UsmSnmpV3User user = new UsmSnmpV3User();
        user.setSnmpUserName(snmpUserName);
        byte[] engine = SnmpV3Util.octo2byte(snmpUserEngineId);
        user.setSnmpUserEngineId(SnmpV3Util.bytes2OctetString(engine));
        getFacade(snmpParam.getIpAddress()).destoryUser(snmpParam, user);
        // DELETE GROUP_TABLE
        if (snmpGroupName != null) {
            VacmSnmpV3Group group = new VacmSnmpV3Group();
            group.setSnmpGroupName(snmpGroupName);
            group.setSnmpSecurityName(snmpUserName);
            group.setSnmpSecurityMode(VacmSnmpV3Group.DEFAULT_SECURITY_MODE);
            getFacade(snmpParam.getIpAddress()).destoryGroup(snmpParam, group);
        }
        // 将结果保存到数据库
        user.setEntityId(entityId);
        // 由于下发到设备的时候要求是按照位图的方式下发，所以这里需要转换回来
        user.setSnmpUserEngineId(snmpUserEngineId);
        snmpV3UserDao.deleteUser(user);
    }

    @Override
    public UsmSnmpV3User cloneUser(Long entityId, UsmSnmpV3User user) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        return getFacade(snmpParam.getIpAddress()).cloneUser(snmpParam, user);
    }

    @Override
    public SnmpParam loadEntitySnmpConfig(Long entityId) {
        return entityService.getSnmpParamByEntity(entityId);
    }

    @Override
    public UsmSnmpV3User querySnmpV3UserInfo(Long entityId, String username, String snmpUserEngineId)
            throws SQLException {
        return snmpV3UserDao.selectUserByName(entityId, username, snmpUserEngineId);
    }

    @Override
    public void activateUser(Long entityId, UsmSnmpV3User user) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getFacade(snmpParam.getIpAddress()).activateUser(snmpParam, user);
    }

    @Override
    public void updateSnmpVersion(Long entityId, Integer snmpVersion) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        SnmpV3UserFacade facade = getFacade(snmpParam.getIpAddress());
        facade.setSnmpVersion(snmpParam, snmpVersion.toString());
    }

    @Override
    public String getAgentEngineId(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        /**
         * 由于这是打开页面的时候去取engineId判断用户类型，所以最好不要将超时设置太长
         */
        snmpParam.setTimeout(DEFAULT_TESTUSER_TIMEOUT);
        SnmpV3UserFacade facade = getFacade(snmpParam.getIpAddress());
        return facade.getAgentEngineId(snmpParam);
    }

    @Override
    public List<UsmSnmpV3User> getAvaiableCloneUserList(Long entityId, String snmpUserName, String snmpUserEngineId)
            throws SQLException {
        UsmSnmpV3User user = new UsmSnmpV3User();
        user.setEntityId(entityId);
        user.setSnmpUserName(snmpUserName);
        user.setSnmpUserEngineId(snmpUserEngineId);
        return snmpV3UserDao.queryAvaiableCloneUserList(user);
    }

    @Override
    public void modifyAccess(Long entityId, VacmSnmpV3Access access) throws SQLException {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getFacade(snmpParam.getIpAddress()).setAccess(snmpParam, access);
        access.setEntityId(entityId);
        snmpV3UserDao.updateVacmAccess(access);

    }

    @Override
    public void modifyView(Long entityId, String snmpViewName, String snmpViewSubtree, Integer snmpViewMode,
            String snmpPrvSubtree) throws SQLException {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 先删除前面的视图
        VacmSnmpV3View view = new VacmSnmpV3View();
        view.setEntityId(entityId);
        view.setSnmpViewName(snmpViewName);
        view.setSnmpViewSubtree(snmpPrvSubtree);
        getFacade(snmpParam.getIpAddress()).destoryView(snmpParam, view);
        // 将结果保存到数据库
        view.setEntityId(entityId);
        snmpV3UserDao.deleteView(view);
        // 再添加新的视图
        view.setEntityId(entityId);
        view.setSnmpViewName(snmpViewName);
        view.setSnmpViewMode(snmpViewMode);
        StringBuilder subtree = new StringBuilder();
        StringBuilder viewMask = new StringBuilder();
        StringTokenizer st = new StringTokenizer(snmpViewSubtree, ".");
        while (st.hasMoreElements()) {
            String token = (String) st.nextElement();
            if ("*".equalsIgnoreCase(token)) {
                viewMask.append("0");
                subtree.append("0.");
            } else {
                viewMask.append("1");
                subtree.append(token);
                subtree.append(".");
            }
        }
        subtree.deleteCharAt(subtree.length() - 1);
        view.setSnmpViewSubtree(subtree.toString());
        int maskValue = Integer.parseInt(viewMask.toString(), 2);
        if (maskValue != 0) {
            Integer.toHexString(maskValue);
            view.setSnmpViewMaskOct(SnmpV3Util.number2Byte(Integer.toHexString(maskValue)));
        }
        getFacade(snmpParam.getIpAddress()).setView(snmpParam, view);
        // 视图的修改就是先删后加
        snmpV3UserDao.insertVacmView(view);
    }

    @Override
    public void modifyUser(UsmSnmpV3User snmpV3User) throws SQLException {
        snmpV3UserDao.updateSnmpV3User(snmpV3User);
    }

    @Override
    public VacmSnmpV3View loadSnmpViewInfo(Long entityId, String snmpViewName, String snmpViewSubtree)
            throws SQLException {
        VacmSnmpV3View view = new VacmSnmpV3View();
        view.setEntityId(entityId);
        view.setSnmpViewName(snmpViewName);
        view.setSnmpViewSubtree(snmpViewSubtree);
        return snmpV3UserDao.querySnmpViewInfo(view);
    }

    @Override
    public List<UsmSnmpV3User> getAvaiableCloneEngineList(Long entityId, String snmpUserName) throws SQLException {
        UsmSnmpV3User user = new UsmSnmpV3User();
        user.setEntityId(entityId);
        user.setSnmpUserName(snmpUserName);
        return snmpV3UserDao.queryAvaiableCloneEngineList(user);
    }

    /**
     * @return the messageService
     */
    public MessageService getMessageService() {
        return messageService;
    }

    /**
     * @param messageService
     *            the messageService to set
     */
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public List<VacmSnmpV3Access> loadSnmpV3AccessList(Long entityId, Integer snmpSecurityLevel) throws SQLException {
        return snmpV3UserDao.selectSnmpV3AccessList(entityId, snmpSecurityLevel);
    }

}
