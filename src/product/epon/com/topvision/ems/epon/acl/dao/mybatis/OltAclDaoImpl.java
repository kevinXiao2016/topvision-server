/***********************************************************************
 * $ OltIgmpDaoImpl.java,v1.0 2011-11-23 18:10:03 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.acl.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.acl.dao.OltAclDao;
import com.topvision.ems.epon.acl.domain.AclListTable;
import com.topvision.ems.epon.acl.domain.AclPortACLListTable;
import com.topvision.ems.epon.acl.domain.AclRuleTable;
import com.topvision.framework.utils.EponConstants;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author jay
 * @created @2011-11-23-18:10:03
 * 
 * @Mybatis Modify by Rod @2013-10-18
 */

@Repository("oltAclDao")
public class OltAclDaoImpl extends MyBatisDaoSupport<Entity> implements OltAclDao {

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.acl.domain.OltAcl";
    }
    
    
    /**
     * 添加一个AclList
     * 
     * @param aclListTable
     *            AclList的参数
     */
    public void addAclList(AclListTable aclListTable) {
        getSqlSession().insert(getNameSpace("insertAclList"), aclListTable);
    }

    /**
     * 关联一个ACLLIST到一个端口
     * 
     * @param aclPortACLListTable
     *            关联参数
     */
    public void addAclPortACLList(AclPortACLListTable aclPortACLListTable) {
        getSqlSession().insert(getNameSpace("insertAclPortACLList"), aclPortACLListTable);
    }

    /**
     * 添加一个AclRule
     * 
     * @param aclRuleTable
     *            AclRule的参数
     */
    public void addAclRuleList(AclRuleTable aclRuleTable) {
        getSqlSession().insert(getNameSpace("insertAclRuleList"), aclRuleTable);
    }

    /**
     * 删除一个AclList
     * 
     * @param entityId
     *            设备Id
     * @param topAclListIndex
     *            AclList的index
     */
    public void deleteAclList(Long entityId, Integer topAclListIndex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        map.put("topAclListIndex", "" + topAclListIndex);
        getSqlSession().delete(getNameSpace("deleteAclList"), map);
    }

    /**
     * 删除一个acllist的关联
     * 
     * @param aclPortACLListTable
     *            删除管理的参数
     */
    public void deleteAclPortACLList(AclPortACLListTable aclPortACLListTable) {
        getSqlSession().delete(getNameSpace("deleteAclPortACLList"), aclPortACLListTable);
    }

    /**
     * 删除一个AclRule
     * 
     * @param entityId
     *            设备Id
     * @param topAclRuleListIndex
     *            AclList的index
     * @param topAclRuleIndex
     *            aclrule的Index
     */
    public void deleteAclRuleList(Long entityId, Integer topAclRuleListIndex, Integer topAclRuleIndex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        map.put("topAclRuleListIndex", "" + topAclRuleListIndex);
        map.put("topAclRuleIndex", "" + topAclRuleIndex);
        getSqlSession().delete(getNameSpace("deleteAclRuleList"), map);
    }

    /**
     * 获取设备所有AclList的方法
     * 
     * @param entityId
     *            设备ID
     * @return List<AclListTable>
     */
    public List<AclListTable> getAclList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getAclList"), entityId);
    }

    /**
     * 获取端口下关联AclList的方法
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @param topAclPortDirection
     *            规则方向
     * @return List<AclPortACLListTable>
     */
    public List<AclPortACLListTable> getAclPortACLList(Long entityId, Long portIndex, Integer topAclPortDirection) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        map.put("portIndex", "" + portIndex);
        map.put("topAclPortDirection", "" + topAclPortDirection);
        return getSqlSession().selectList(getNameSpace("getAclPortACLList"), map);
    }

    /**
     * 获取端口下关联AclList的方法
     * 
     * @param entityId
     *            设备ID
     * @return List<AclPortACLListTable>
     */
    public List<AclPortACLListTable> getAllAclPortACLList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getAllAclPortACLList"), entityId);        
    }

    /**
     * 获取ACL关联的端口
     * 
     * @param entityId
     *            设备ID
     * @param aclIndex
     *            ACL INDEX
     * @return List<AclPortACLListTable>
     */
    public List<AclPortACLListTable> getAclPortByAclList(Long entityId, Integer aclIndex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        map.put("topPortAclListIndex", "" + aclIndex);
        return getSqlSession().selectList(getNameSpace("getAclPortByAclList"), map);        
    }

    /**
     * 获取一个Acllist下的所有Aclrule
     * 
     * @param entityId
     *            设备Id
     * @param topAclRuleListIndex
     *            AclList的Index
     * @return List<AclRuleTable>
     */
    public List<AclRuleTable> getAclRuleList(Long entityId, Integer topAclRuleListIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topAclRuleListIndex", topAclRuleListIndex);
        return getSqlSession().selectList(getNameSpace("getAclRuleList"), map);    
    }

    /**
     * 获取所有的Aclrule
     * 
     * @param entityId
     *            设备Id
     * @return List<AclRuleTable>
     */
    public List<AclRuleTable> getAllAclRuleList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getAllAclRuleList"), entityId);    
    }

    /**
     * 修改一个AclList
     * 
     * @param aclListTable
     *            修改的参数
     */
    public void modifyAclList(AclListTable aclListTable) {
        getSqlSession().update(getNameSpace("updateAclList"), aclListTable);
    }

    /**
     * 修改一个AclRule
     * 
     * @param aclRuleTable
     */
    public void modifyAclRuleList(AclRuleTable aclRuleTable) {
        getSqlSession().update(getNameSpace("updateAclRuleList"), aclRuleTable);
    }

    /**
     * 保存AclList到数据库
     * 
     * @param aclListTables
     *            Acllist对象列表
     */
    public void saveAclList(Long entityId, final List<AclListTable> aclListTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAclListAll"), entityId);
            for (AclListTable aclListTable : aclListTables) {
                sqlSession.insert(getNameSpace("insertAclList"), aclListTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally{
            sqlSession.close();
        }
    }

    /**
     * 保存Acl管理端口对象到数据库
     * 
     * @param aclPortACLListTables
     *            关联列表
     */
    public void saveAclPortACLList(Long entityId, final List<AclPortACLListTable> aclPortACLListTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAclPortACLListAll"), entityId);
            for (AclPortACLListTable aclPortACLListTable : aclPortACLListTables) {
                sqlSession.insert(getNameSpace("insertAclPortACLList"), aclPortACLListTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
        } finally{
            sqlSession.close();
        }
    }

    /**
     * 保存AclRule到数据库
     * 
     * @param aclRuleTables
     *            aclRule对象列表
     */
    public void saveAclRuleList(Long entityId, final List<AclRuleTable> aclRuleTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAclRuleListAll"), entityId);
            for (AclRuleTable aclRuleTable : aclRuleTables) {
                sqlSession.insert(getNameSpace("insertAclRuleList"), aclRuleTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
        } finally{
            sqlSession.close();
        }
    }

    /**
     * 获取一个aclrule
     * 
     * @param aclRuleTables
     *            aclRule对象列表
     */
    public AclRuleTable getAclRule(Long entityId, Integer aclIndex, Integer aclRuleIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("aclIndex", aclIndex);
        map.put("aclRuleIndex", aclRuleIndex);
        return getSqlSession().selectOne(getNameSpace("getAclRule"), map);
    }

    @Override
    public void modifyAclListAclRuleNum(Long entityId, Integer aclIndex, Integer flag) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("aclIndex", aclIndex);
        AclListTable aclListTable = new AclListTable();
        aclListTable = getSqlSession().selectOne(getNameSpace("getAclListTable"), map);
        if (flag.equals(EponConstants.ACL_LIST_RULENUM_ADD)) {
            aclListTable.setTopAclRuleNum(aclListTable.getTopAclRuleNum() + 1);
        } else if (flag.equals(EponConstants.ACL_LIST_RULENUM_DEL)) {
            aclListTable.setTopAclRuleNum(aclListTable.getTopAclRuleNum() - 1);
        }
        getSqlSession().update(getNameSpace("updateAclList"), aclListTable);
    }
}