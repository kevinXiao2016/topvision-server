/***********************************************************************
 * $Id: ImageDao.java,v 1.1 Oct 17, 2009 7:11:40 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dao;

import java.util.List;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.domain.ImageDirectory;
import com.topvision.platform.domain.ImageFile;

/**
 * @author kelers
 * @Create Date Oct 17, 2009 7:11:40 PM
 */
public interface ImageDao extends BaseEntityDao<ImageDirectory> {

    /**
     * 通过模块名获取图片目录
     * 
     * @param module
     * @return
     */
    List<ImageDirectory> getImageDirectory(String module);

    /**
     * 通过目录ID获取图片文件
     * 
     * @param directoryId
     * @return
     */
    List<ImageFile> getImageFiles(Long directoryId);

    /**
     * 添加图片文件
     * 
     * @param file
     */
    void insertImageFile(ImageFile file);

    /**
     * 批量添加图片文件
     * 
     * @param files
     */
    void insertImageFile(List<ImageFile> files);

    /**
     * 修改图片路径
     * 
     * @param file
     */
    void updateImagePath(ImageFile file);
}
