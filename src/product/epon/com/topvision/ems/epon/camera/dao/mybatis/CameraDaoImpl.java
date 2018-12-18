/***********************************************************************
 * $Id: CameraDaoImpl.java,v1.0 2013年12月10日 下午3:51:42 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.camera.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.camera.dao.CameraDao;
import com.topvision.ems.epon.camera.domain.CameraBindDuplicate;
import com.topvision.ems.epon.camera.domain.CameraFilterTable;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Bravin
 * @created @2013年12月10日-下午3:51:42
 *
 */
@Repository("cameraDao")
public class CameraDaoImpl extends MyBatisDaoSupport<CameraFilterTable> implements CameraDao {

    @Override
    public void batchInsertCameraList(Long entityId, List<CameraFilterTable> cameraList) {
        SqlSession session = getBatchSession();
        try {
            session.delete(getNameSpace("deleteCameraInfo"), entityId);
            for (CameraFilterTable filter : cameraList) {
                filter.setEntityId(entityId);
                session.insert(getNameSpace("batchInsertCamera"), filter);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.camera.domain.Camera";
    }

    @Override
    public List<CameraFilterTable> loadCameraList(Map<String, Object> map) {
        return this.getSqlSession().selectList(getNameSpace("queryAllCameraList"), map);
    }

    @Override
    public void insertCameraFilter(CameraFilterTable cameraFitler) {
        getSqlSession().insert(getNameSpace("batchInsertCamera"), cameraFitler);
    }

    @Override
    public int queryCameraCount(Map<String, Object> map) {
        return this.getSqlSession().selectOne(getNameSpace("queryCameraCount"), map);
    }

    @Override
    public void deleteCamera(CameraFilterTable cameraFitler) {
        this.getSqlSession().delete(getNameSpace("deleteCameraConfig"), cameraFitler);
    }

    @Override
    public void updateCamera(CameraFilterTable cameraFitler) {
        this.getSqlSession().update(getNameSpace("updateCameraConfig"), cameraFitler);
    }

    @Override
    public List<String> queryAllCameraType() {
        return this.getSqlSession().selectList(getNameSpace("queryAllCameraType"));
    }

    @Override
    public int checkDuplicate(CameraFilterTable filter) {
        CameraFilterTable $selected = getSqlSession().selectOne(getNameSpace("checkExisted"), filter);
        // 如果不存在同一ONU下IP或者MAC重复的，则表示无重复
        if ($selected != null) {
            return CameraBindDuplicate.CAMERA_DUP;
        }
        $selected = getSqlSession().selectOne(getNameSpace("checkIpDuplicatd"), filter);
        if ($selected != null) {
            return CameraBindDuplicate.IP_DUP;
        }
        $selected = getSqlSession().selectOne(getNameSpace("checkMacDuplicatd"), filter);
        if ($selected != null) {
            return CameraBindDuplicate.MAC_DUP;
        }
        /*
         * if (filter.getIp().equals($selected.getIp())) { //如果IP,MAC重复，表示CAMERA重复 if
         * (filter.getMac().equals($selected.getMac())) { return CameraBindDuplicate.CAMERA_DUP; }
         * else {//否则只是IP重复 return CameraBindDuplicate.IP_DUP; } } else if
         * (filter.getMac().equals($selected.getMac())) { return CameraBindDuplicate.MAC_DUP; }
         */
        return CameraBindDuplicate.NONE_DUP;
    }

}
