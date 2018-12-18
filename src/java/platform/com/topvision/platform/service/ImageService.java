/***********************************************************************
 * $Id: ImageService.java,v 1.1 Oct 17, 2009 7:10:51 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import java.util.List;

import com.topvision.framework.service.Service;
import com.topvision.platform.domain.ImageDirectory;
import com.topvision.platform.domain.ImageFile;

/**
 * @author kelers
 * @Create Date Oct 17, 2009 7:10:51 PM
 */
public interface ImageService extends Service {

    /**
     * 获得所有图片目录
     * 
     * @return List<ImageDirectory>
     */
    List<ImageDirectory> getAllImageDirectory();

    /**
     * 获得某个模块的图片目录
     * 
     * @param module
     *            模块名
     * @return List<ImageDirectory>
     */
    List<ImageDirectory> getImageDirectory(String module);

    /**
     * 获得某个图片目录下的所有图片文件
     * 
     * @param directoryId
     * @return List<ImageFile>
     */
    List<ImageFile> getImageFiles(Long directoryId);

    /**
     * 加入一个图片目录
     * 
     * @param directory
     *            目录对象
     */
    void insertImageDirectory(ImageDirectory directory);

    /**
     * 加入一个图片文件
     * 
     * @param file
     *            图片文件对象
     */
    void insertImageFile(ImageFile file);

    /**
     * 加入一批图片对象
     * 
     * @param files
     *            图片对象列表
     */
    void insertImageFile(List<ImageFile> files);

    /**
     * 修改图片位置
     * 
     * @param file
     */
    void updateImagePath(ImageFile file);
}
