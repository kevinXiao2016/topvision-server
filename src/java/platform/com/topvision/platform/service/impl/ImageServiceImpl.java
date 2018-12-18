/***********************************************************************
 * $Id: ImageServiceImpl.java,v 1.1 Oct 17, 2009 7:11:12 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.topvision.framework.common.ZipUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.dao.ImageDao;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.ImageDirectory;
import com.topvision.platform.domain.ImageFile;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.ImageService;

@Service("imageService")
public class ImageServiceImpl extends BaseService implements ImageService {
    public static String RESOURCES_IMG_PATH = "/resources/images/";
    public static String ZIP_PATH = "/resources/zip/";
    public static String TEMP_PATH = "/tempfile/";
    @Autowired
    private ImageDao imageDao;
    @Autowired
    private SystemPreferencesDao systemPreferencesDao;

    @Override
    public List<ImageDirectory> getAllImageDirectory() {
        return imageDao.selectByMap(null);
    }

    @Override
    public List<ImageDirectory> getImageDirectory(String module) {
        return imageDao.getImageDirectory(module);
    }

    @Override
    public List<ImageFile> getImageFiles(Long directoryId) {
        return imageDao.getImageFiles(directoryId);
    }

    @Override
    @PostConstruct
    public void initialize() {
        String zipdir = SystemConstants.ROOT_REAL_PATH + ZIP_PATH;
        File zipfile = new File(zipdir);
        if (zipfile.exists()) {
            String tempdir = SystemConstants.ROOT_REAL_PATH + TEMP_PATH;
            String basepath = SystemConstants.ROOT_REAL_PATH + RESOURCES_IMG_PATH;
            try {
                List<ImageDirectory> directorys = getAllImageDirectory();
                for (int i = 0; i < directorys.size(); i++) {
                    ImageDirectory directory = directorys.get(i);
                    File file = new File(zipfile, directory.getPath());
                    if (!file.exists()) {
                        continue;
                    }
                    File[] files = file.listFiles();
                    for (int j = 0; j < files.length; j++) {
                        if (files[j].getName().toLowerCase().endsWith(".zip")) {
                            unzip(directory, files[j], tempdir, basepath);
                        }
                    }
                }
            } catch (DataAccessException e) {
                getLogger().error("ImageServiceImpl.initialize error", e);
            } catch (IOException e) {
                getLogger().error("ImageServiceImpl.initialize error", e);
            }
        }
    }

    @Override
    public void insertImageDirectory(ImageDirectory directory) {
        imageDao.insertEntity(directory);
    }

    @Override
    public void insertImageFile(ImageFile file) {
        imageDao.insertImageFile(file);
    }

    @Override
    public void insertImageFile(List<ImageFile> files) {
        imageDao.insertImageFile(files);
    }

    public void setImageDao(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    public void setSystemPreferencesDao(SystemPreferencesDao systemPreferencesDao) {
        this.systemPreferencesDao = systemPreferencesDao;
    }

    private void unzip(ImageDirectory directory, File file, String tempdir, String basepath) throws IOException {
        String name = file.getName();
        SystemPreferences preference = null;
        preference = systemPreferencesDao.selectByPrimaryKey(name + ".lastModified");
        if (preference == null) {
            preference = new SystemPreferences();
            preference.setName(name + ".lastModified");
            preference.setValue(String.valueOf(file.lastModified()));
            preference.setModule("Icon");
            systemPreferencesDao.insertEntity(preference);
        } else {
            if (Long.parseLong(preference.getValue()) >= file.lastModified()) {
                // todo
            }
            preference.setValue(String.valueOf(file.lastModified()));
            systemPreferencesDao.updateEntity(preference);
            return;
        }

        int index = name.lastIndexOf(".");
        if (index != -1) {
            name = name.substring(0, index);
        }
        // 解压缩入库
        ZipUtils.unzip(file.getAbsolutePath(), tempdir);
        File target = new File(tempdir, name);
        visit(directory.getDirectoryId(), target, basepath, directory.getModule());
        target.delete();
    }

    private void visit(Long superiorId, File target, String basePath, String module) {
        ImageDirectory directory = new ImageDirectory();
        directory.setModule(module);
        directory.setName(target.getName());
        directory.setSuperiorId(superiorId);
        imageDao.insertEntity(directory);
        File dir = new File(basePath + directory.getPath());
        if (!dir.exists()) {
            dir.mkdir();
        }

        File[] list = target.listFiles();
        if (list != null && list.length > 0) {
            for (int i = 0; i < list.length; i++) {
                if (list[i].isDirectory()) {
                    visit(directory.getDirectoryId(), list[i], basePath, module);
                } else {
                    ImageFile file = new ImageFile();
                    file.setDirectoryId(directory.getDirectoryId());

                    String name = list[i].getName();
                    int index = name.lastIndexOf(".");
                    if (index != -1) {
                        file.setName(name.substring(0, index));
                        file.setFormat(name.substring(index + 1));
                    } else {
                        file.setName(name);
                        file.setFormat("gif");
                    }

                    imageDao.insertImageFile(file);

                    list[i].renameTo(new File(dir, String.valueOf(file.getFileId()) + "." + file.getFormat()));

                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.service.ImageService#
     * updateImagePath(com.topvision.platform.domain.ImageFile)
     */
    @Override
    public void updateImagePath(ImageFile file) {
        imageDao.updateImagePath(file);
    }
}
