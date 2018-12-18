/***********************************************************************
 * $Id: CameraPlanDaoImpl.java,v1.0 2013年12月23日 下午3:32:43 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.camera.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.camera.dao.CameraPlanDao;
import com.topvision.ems.epon.camera.domain.CameraPhysicalInfo;
import com.topvision.ems.epon.camera.domain.CameraPlanTable;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Bravin
 * @created @2013年12月23日-下午3:32:43
 *
 */
@Repository
public class CameraPlanDaoImpl extends MyBatisDaoSupport<Entity> implements CameraPlanDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.camera.domain.Camera";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.camera.dao.CameraPlanDao#batchInsertPlan(java.util.List)
     */
    @Override
    public void batchInsertPlan(List<CameraPlanTable> list) {
        // SqlSession session = getBatchSession();
        // try{
        for (CameraPlanTable plan : list) {
            try {
                getSqlSession().insert(getNameSpace("insertPlan"), plan);
            } catch (Exception e) {
                logger.debug("", e);
            }
        }
        // session.commit();
        // } catch (Exception e) {
        // logger.error("", e);
        // sqlSession.rollback();
        // } finally {
        // sqlSession.close();
        // }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.camera.dao.CameraPlanDao#batchInsertPhyInfo(java.util.List)
     */
    @Override
    public void batchInsertPhyInfo(List<CameraPhysicalInfo> list) {
        // SqlSession session = getBatchSession();
        // try{
        for (CameraPhysicalInfo plan : list) {
            try {
                getSqlSession().insert(getNameSpace("insertPhyInfo"), plan);
            } catch (Exception e) {
                logger.debug("", e);
            }
        }
        // session.commit();
        // } catch (Exception e) {
        // logger.error("", e);
        // sqlSession.rollback();
        // } finally {
        // sqlSession.close();
        // }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.camera.dao.CameraPlanDao#selectCameraPlanList(java.util.Map)
     */
    @Override
    public List<CameraPlanTable> selectCameraPlanList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("selectCameraPlanList"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.camera.dao.CameraPlanDao#queryCameraPlanCount(java.util.Map)
     */
    @Override
    public int queryCameraPlanCount(Map<String, Object> map) {
        return this.getSqlSession().selectOne(getNameSpace("queryPlanCount"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.camera.dao.CameraPlanDao#selectCameraPhyList(java.util.Map)
     */
    @Override
    public List<CameraPhysicalInfo> selectCameraPhyList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("selectCameraPhyList"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.camera.dao.CameraPlanDao#queryCameraPhyCount(java.util.Map)
     */
    @Override
    public int queryCameraPhyCount(Map<String, Object> map) {
        return this.getSqlSession().selectOne(getNameSpace("queryPhyCount"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.camera.dao.CameraPlanDao#deleteCameraPlan(java.lang.String)
     */
    @Override
    public void deleteCameraPlan(String cameraNo) {
        getSqlSession().delete(getNameSpace("deleteCameraPlan"), cameraNo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.camera.dao.CameraPlanDao#deletePhyInfo(java.lang.String)
     */
    @Override
    public void deletePhyInfo(String mac) {
        getSqlSession().delete(getNameSpace("deletePhyInfo"), mac);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.camera.dao.CameraPlanDao#modifyCameraPlan(com.topvision.ems.epon.
     * camera.domain.CameraPlanTable)
     */
    @Override
    public void modifyCameraPlan(CameraPlanTable table) {
        getSqlSession().update(getNameSpace("modifyCameraPlan"), table);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.camera.dao.CameraPlanDao#modifyPhyInfo(com.topvision.ems.epon.camera.
     * domain.CameraPhysicalInfo)
     */
    @Override
    public void modifyPhyInfo(CameraPhysicalInfo info) {
        getSqlSession().update(getNameSpace("modifyPhyInfo"), info);
    }

    @Override
    public void insertPlan(CameraPlanTable plan) {
        getSqlSession().insert(getNameSpace("insertPlan"), plan);
    }

    @Override
    public void insertPhyInfo(CameraPhysicalInfo info) {
        getSqlSession().insert(getNameSpace("insertPhyInfo"), info);
    }

}
