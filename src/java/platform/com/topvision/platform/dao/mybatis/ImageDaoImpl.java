/***********************************************************************
 * $Id: ImageDaoImpl.java,v 1.1 Oct 17, 2009 7:12:13 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.ImageDao;
import com.topvision.platform.domain.ImageDirectory;
import com.topvision.platform.domain.ImageFile;

@Repository("imageDao")
public class ImageDaoImpl extends MyBatisDaoSupport<ImageDirectory> implements ImageDao {
    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.ibatis.MyBatisDaoSupport#getNameSpace()
     */
    @Override
    public String getDomainName() {
        return ImageDirectory.class.getName();
    }

    /**
     * 获取路径
     * 
     * @param directoryId
     * @return
     */
    public String getPath(Long directoryId) {
        return (String) getSqlSession().selectOne(getNameSpace("getPath"), directoryId);
    }

    /**
     * 更新路径
     * 
     * @param directory
     */
    public void updatePath(ImageDirectory directory) {
        getSqlSession().update(getNameSpace("updatePath"), directory);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.ImageDao#getImageDirectory(java.lang.String)
     */
    @Override
    public List<ImageDirectory> getImageDirectory(String module) {
        return getSqlSession().selectList(getNameSpace("getImageDirectoryByModule"), module);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.ImageDao#getImageFiles(java.lang.Long)
     */
    @Override
    public List<ImageFile> getImageFiles(Long directoryId) {
        return getSqlSession().selectList(getNameSpace("getImageFiles"), directoryId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.ibatis.MyBatisDaoSupport#insertEntity(java .lang.Object)
     */
    @Override
    public void insertEntity(ImageDirectory directory) {
        insertEntity(directory);
        String path = getPath(directory.getSuperiorId());
        if (path == null) {
            directory.setPath(String.valueOf(directory.getDirectoryId()));
        } else {
            directory.setPath(path + "/" + directory.getDirectoryId());
        }
        updatePath(directory);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.ImageDao#insertImageFile(com.topvision.platform
     * .domain.ImageFile)
     */
    @Override
    public void insertImageFile(ImageFile file) {
        getSqlSession().insert(getNameSpace("insertImageFile"), file);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.ImageDao#insertImageFile(java.util.List)
     */
    @Override
    public void insertImageFile(final List<ImageFile> files) {
        if (files == null || files.size() == 0) {
            return;
        }
        SqlSession session = getBatchSession();
        try {
            for (ImageFile file : files) {
                session.delete(getNameSpace("insertImageFile"), file);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.dao.ImageDao#updateImagePath(com.topvision.platform
     * .domain.ImageFile)
     */
    @Override
    public void updateImagePath(ImageFile file) {
        getSqlSession().update(getNameSpace("updateImagePath"), file);
    }
}
