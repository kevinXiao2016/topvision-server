/***********************************************************************
 * $Id: EntityVersionServiceImpl.java,v1.0 2014年9月23日 下午3:15:26 $
 *
 * @author: loyal
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

import com.topvision.ems.upgrade.domain.UpgradeJobInfo;
import com.topvision.ems.upgrade.service.UpgradeJobService;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.ems.upgrade.domain.EntityVersion;
import com.topvision.ems.upgrade.exception.VersionFileErrorException;
import com.topvision.ems.upgrade.service.EntityVersionService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.SystemConstants;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:15:26
 */
@Service("entityVersionService")
public class EntityVersionServiceImpl extends BaseService implements EntityVersionService {
    private static String TEMPROOT = SystemConstants.ROOT_REAL_PATH + "META-INF/";
    private static String VERSIONROOT = SystemConstants.ROOT_REAL_PATH + "META-INF/versions/";
    private static String TFTPROOT = SystemConstants.ROOT_REAL_PATH + "META-INF/tftpTemp/";
    private static String FTPROOT = SystemConstants.ROOT_REAL_PATH + "META-INF/ftpTemp/UpgradeBin/";
    private static String UPGRADEPROPERTIES = "upgrade.properties";
    private static String TFTP = "tftp";
    private static String FTP = "ftp";

    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private UpgradeJobService upgradeJobService;

    @Override
    public List<EntityVersion> getEntityVersionList(String versionName, Long typeId) {
        String typeName = null;
        // 将设备类型ID转换成标准字符串
        if (typeId != null && typeId != -1) {
            EntityType entityType = entityTypeService.getEntityType(typeId);
            typeName = entityType.getDisplayName();
        }
        // 读取出所有合格的properties文件
        File dir = new File(VERSIONROOT);
        File[] files = dir.listFiles(new FileFilter() {
            public boolean accept(File f) {
                if (f.getName().endsWith(".properties")) {
                    return true;
                } else {
                    return false;
                }
            }

        });
        List<EntityVersion> re = new ArrayList<EntityVersion>();
        // 将properties文件转换为EntityVersion
        for (File file : files) {
            EntityVersion entityVersion = getEntityVersion(file, versionName, typeName);
            if (entityVersion != null) {
                re.add(entityVersion);
            }
        }
        return re;
    }

    @Override
    public String getEntityVersionProperty(String versionName) {
        StringBuilder sb = new StringBuilder();
        File file = new File(VERSIONROOT + versionName + ".properties");
        if (file.exists()) {
            BufferedReader br = null;
            try {
                FileReader fr = new FileReader(file);
                // 可以换成工程目录下的其他文本文件
                br = new BufferedReader(fr);
                String s = null;
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } catch (Exception e) {
                logger.debug("", e);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        return sb.toString();
    }

    @Override
    public List<Long> deleteEntityVersion(String versionName) {
        List<Long> re = new ArrayList<>();
        List<UpgradeJobInfo> upgradeJobInfos = upgradeJobService.getJobByVersionName(versionName);
        if (upgradeJobInfos != null) {
            for (UpgradeJobInfo upgradeJobInfo : upgradeJobInfos) {
                re.add(upgradeJobInfo.getJobId());
                try {
                    upgradeJobService.deleteJob(upgradeJobInfo.getJobId());
                } catch (Exception e) {
                    logger.debug("",e);
                }
            }
        }
        File file = new File(VERSIONROOT + versionName + ".properties");
        Properties properties = new Properties();
        FileReader fr = null;
        try {
            fr = new FileReader(file);
            properties.load(fr);
        } catch (IOException e) {
            logger.debug("file [" + file.getAbsolutePath() + "]", e);
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception e) {

            }
        }
        // 读取传输类型，tftp和ftp文件存放格式是不一样的
        String transferType = properties.getProperty("transferType");
        // ftp
        if (transferType.equalsIgnoreCase(FTP)) {
            // 镜像文件放在以ftp根目录下的以版本号为名称的目录里面
            File versiondir = new File(FTPROOT + versionName);
            deleteFile(versiondir);
            // tftp
        } else if (transferType.equalsIgnoreCase(TFTP)) {
            // 获取此properties文件描述的所有subtype
            String subType = properties.getProperty("subType", "");
            String[] sts;
            if (subType != null) {
                sts = subType.split(",");
            } else {
                // 不存在subtype则只有一个主镜像文件
                sts = new String[1];
                sts[1] = "";
            }
            for (String st : sts) {
                if (st != null) {
                    File binfile;
                    // 构造镜像文件路径
                    if (!st.trim().equalsIgnoreCase("")) {
                        binfile = new File(TFTPROOT + versionName + "-" + st + ".bin");
                    } else {
                        binfile = new File(TFTPROOT + versionName + ".bin");
                    }
                    deleteFile(binfile);
                }
            }
        }
        properties.clear();
        file.delete();
        return re;
    }

    @Override
    public void addEntityVersion(String tempName) {
        File tarFile = new File(TEMPROOT + tempName + ".gz.tar");
        File dir = new File(TEMPROOT + tempName);
        uncompressFile(tarFile, dir);
        File pfile = new File(dir, UPGRADEPROPERTIES);
        Properties properties = new Properties();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(pfile);
            properties.load(fileReader);
        } catch (IOException e) {
            logger.debug("file [" + pfile.getAbsolutePath() + "]", e);
            throw new VersionFileErrorException("file [" + pfile.getAbsolutePath() + "]", e);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (Exception e) {

                }
            }
        }
        String versionName = properties.getProperty("versionName");
        if (versionName == null || versionName.trim().equalsIgnoreCase("")) {
            logger.error("addEntityVersion VersionName is null");
            throw new VersionFileErrorException("addEntityVersion VersionName is null file [" + pfile.getAbsolutePath()
                    + "]");
        }
        // 读取传输类型，tftp和ftp文件存放格式是不一样的
        String transferType = properties.getProperty("transferType");
        if (transferType.equalsIgnoreCase(FTP)) {
            // ftp
            // 自动升级ftp根目录
            File upgradeDir = new File(FTPROOT);
            if (!upgradeDir.exists()) {
                upgradeDir.mkdir();
            }
            File versiondir = new File(upgradeDir, versionName);
            // 确保不存在versiondir
            deleteFile(versiondir);
            // 创建新的versiondir
            versiondir.mkdir();
            // 将properties文件移动到versions文件夹
            File moveProperties = new File(VERSIONROOT + versionName + ".properties");
            // 如果属性文件存在，需要先删除掉以前的，再生产新的
            // 确保不存在moveProperties
            deleteFile(moveProperties);
            pfile.renameTo(moveProperties);
            // 获取此properties文件描述的所有subtype
            String subType = properties.getProperty("subType", "");
            String[] sts;
            if (subType != null) {
                sts = subType.split(",");
            } else {
                // 不存在subtype则只有一个主镜像文件
                sts = new String[1];
                sts[1] = "";
            }
            for (String st : sts) {
                if (st != null) {
                    // 将镜像移动到版本目录下
                    File ifile;
                    File moveImage;
                    // 构造镜像文件路径
                    if (!st.trim().equalsIgnoreCase("")) {
                        ifile = new File(dir, versionName + "-" + st + ".bin");
                        moveImage = new File(versiondir, versionName + "-" + st + ".bin");
                    } else {
                        ifile = new File(dir, versionName + ".bin");
                        moveImage = new File(versiondir, versionName + ".bin");
                    }
                    // 确保不存在moveImage
                    deleteFile(moveImage);
                    ifile.renameTo(moveImage);
                }
            }
        } else if (transferType.equalsIgnoreCase(TFTP)) {
            // tftp
            // 将properties文件移动到versions文件夹
            File moveProperties = new File(VERSIONROOT + versionName + ".properties");
            // 确保不存在moveProperties
            deleteFile(moveProperties);
            pfile.renameTo(moveProperties);
            // 获取此properties文件描述的所有subtype
            String subType = properties.getProperty("subType", "");
            String[] sts;
            if (subType != null) {
                sts = subType.split(",");
            } else {
                // 不存在subtype则只有一个主镜像文件
                sts = new String[1];
                sts[1] = "";
            }
            for (String st : sts) {
                if (st != null) {
                    // 将镜像移动到版本目录下
                    File ifile;
                    File moveImage;
                    // 构造镜像文件路径
                    if (!st.trim().equalsIgnoreCase("")) {
                        ifile = new File(dir, versionName + "-" + st + ".bin");
                        moveImage = new File(TFTPROOT + versionName + "-" + st + ".bin");
                    } else {
                        ifile = new File(dir, versionName + ".bin");
                        moveImage = new File(TFTPROOT + versionName + ".bin");
                    }
                    // 确保不存在moveImage
                    deleteFile(moveImage);
                    ifile.renameTo(moveImage);
                }
            }
        }

        // 删除临时解压目录
        deleteFile(dir);
        // 删除临时tar文件
        deleteFile(tarFile);
    }

    @Override
    public String getTempDir() {
        return TEMPROOT;
    }

    @Override
    public EntityVersion getEntityVersion(String versionName) {
        File file = new File(VERSIONROOT + versionName + ".properties");
        return getEntityVersion(file);
    }

    /****************************************** 以下是私有方法 *********************************************************/

    private EntityVersion getEntityVersion(File versionPropertiesFile) {
        return getEntityVersion(versionPropertiesFile, null, null);
    }

    private EntityVersion getEntityVersion(File versionPropertiesFile, String versionName, String typeName) {
        try {
            Properties properties = new Properties();
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(versionPropertiesFile);
                properties.load(fileReader);
            } catch (IOException e) {
                logger.debug("file [" + versionPropertiesFile.getAbsolutePath() + "]", e);
                throw new VersionFileErrorException("file [" + versionPropertiesFile.getAbsolutePath() + "]", e);
            } finally {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (Exception e) {

                    }
                }
            }
            String propertyType = properties.getProperty("propertyType");
            // 判断是否为版本属性文件
            if (propertyType == null || !propertyType.equalsIgnoreCase("TopVision Upgrade Packet")) {
                logger.debug("propertyType error[" + versionPropertiesFile.getName() + "]");
                return null;
            }
            String et = properties.getProperty("entityType", "");
            // 判断是否是期望设备类型的版本属性文件
            if (typeName != null) {
                List<String> ets = Arrays.asList(et.split(","));
                if (!ets.contains(typeName)) {
                    logger.trace("skip entitytype[" + versionPropertiesFile.getName() + "]");
                    return null;
                }
            }
            // 判断是否为期望的版本
            String vn = properties.getProperty("versionName", "");
            if (versionName != null) {
                if (!vn.contains(versionName)) {
                    logger.trace("skip versionName[" + versionPropertiesFile.getName() + "]");
                    return null;
                }
            }
            // 读取传输类型，tftp和ftp文件存放格式是不一样的
            String transferType = properties.getProperty("transferType");
            if (transferType == null) {
                logger.debug("transferType is null[" + versionPropertiesFile.getName() + "]");
                return null;
            }
            // ftp
            if (transferType.equalsIgnoreCase(FTP)) {
                // 镜像文件放在以ftp根目录下的以版本号为名称的目录里面
                File ftpRootDir = new File(FTPROOT);
                if (!ftpRootDir.exists()) {
                    ftpRootDir.mkdir();
                }
                File versiondir = new File(ftpRootDir, vn);
                if (versiondir.exists() && versiondir.isDirectory()) {
                    String subType = properties.getProperty("subType", "");
                    EntityVersion entityVersion = new EntityVersion();
                    entityVersion.setCreateTime(new Timestamp(versionPropertiesFile.lastModified()));
                    entityVersion.setPropertyFileName(versionPropertiesFile.getName());
                    entityVersion.setVersionName(vn);
                    entityVersion.setTypeDisplayNames(et);
                    entityVersion.setSubTypeString(subType);
                    entityVersion.setTransferType(transferType);
                    String[] sts;
                    if (subType != null) {
                        sts = subType.split(",");
                    } else {
                        // 不存在subtype则只有一个主镜像文件
                        sts = new String[1];
                        sts[1] = "";
                    }
                    boolean isError = false;
                    for (String st : sts) {
                        if (st != null) {
                            File binfile;
                            // 构造镜像文件路径
                            if (!st.trim().equalsIgnoreCase("")) {
                                binfile = new File(versiondir, vn + "-" + st + ".bin");
                            } else {
                                binfile = new File(versiondir, vn + ".bin");
                            }
                            if (!binfile.exists() || !binfile.isFile()) {
                                isError = true;
                                logger.debug("binfile not found fileName[" + versionPropertiesFile.getName() + "]"
                                        + "subType[" + st + "]");
                            } else {
                                entityVersion.getSubType().add(st.trim());
                            }
                        } else {
                            isError = true;
                            logger.debug("subType is null[" + versionPropertiesFile.getName() + "]");
                            return null;
                        }
                    }
                    if (!isError) {
                        return entityVersion;
                    } else {
                        return null;
                    }
                } else {
                    logger.debug("version dir not found[" + versionPropertiesFile.getName() + "]");
                    return null;
                }
                // tftp
            } else if (transferType.equalsIgnoreCase(TFTP)) {
                // 获取此properties文件描述的所有subtype
                String subType = properties.getProperty("subType", "");
                EntityVersion entityVersion = new EntityVersion();
                entityVersion.setCreateTime(new Timestamp(versionPropertiesFile.lastModified()));
                entityVersion.setPropertyFileName(versionPropertiesFile.getName());
                entityVersion.setVersionName(vn);
                entityVersion.setTypeDisplayNames(et);
                entityVersion.setSubTypeString(subType);
                entityVersion.setTransferType(transferType);
                String[] sts;
                if (subType != null) {
                    sts = subType.split(",");
                } else {
                    // 不存在subtype则只有一个主镜像文件
                    sts = new String[1];
                    sts[1] = "";
                }
                boolean isError = false;
                for (String st : sts) {
                    if (st != null) {
                        File binfile;
                        // 构造镜像文件路径
                        if (!st.trim().equalsIgnoreCase("")) {
                            binfile = new File(TFTPROOT + vn + "-" + st + ".bin");
                        } else {
                            binfile = new File(TFTPROOT + vn + ".bin");
                        }
                        if (!binfile.exists() || !binfile.isFile()) {
                            isError = true;
                            logger.debug("binfile not found fileName[" + versionPropertiesFile.getName() + "]"
                                    + "subType[" + st + "]");
                        } else {
                            entityVersion.getSubType().add(st.trim());
                        }
                    } else {
                        isError = true;
                        logger.debug("subType is null[" + versionPropertiesFile.getName() + "]");
                        return null;
                    }
                }
                if (!isError) {
                    return entityVersion;
                } else {
                    return null;
                }
            } else {
                logger.debug("transferType error[" + versionPropertiesFile.getName() + "]");
                return null;
            }
        } catch (Exception e) {
            logger.error("Properties file error[" + versionPropertiesFile.getName() + "]", e);
            return null;
        }
    }

    private void uncompressFile(File srcFile, File destDir) {
        FileInputStream fis = null;
        ArchiveInputStream in = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            fis = new FileInputStream(srcFile);
            GZIPInputStream is = new GZIPInputStream(new BufferedInputStream(fis));
            in = new ArchiveStreamFactory().createArchiveInputStream("tar", is);
            bufferedInputStream = new BufferedInputStream(in);
            TarArchiveEntry entry = (TarArchiveEntry) in.getNextEntry();
            while (entry != null) {
                String name = entry.getName();
                String[] names = name.split("/");
                String fileName = destDir.getAbsolutePath();
                if (!destDir.exists()) {
                    destDir.mkdir();
                }
                for (String str : names) {
                    fileName = fileName + File.separator + str;
                }
                if (name.endsWith("/")) {
                    mkFolder(fileName);
                } else {
                    BufferedOutputStream bufferedOutputStream = null;
                    try {
                        File file = mkFile(fileName);
                        bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                        int b;
                        while ((b = bufferedInputStream.read()) != -1) {
                            bufferedOutputStream.write(b);
                        }
                        bufferedOutputStream.flush();
                    } finally {
                        if (bufferedInputStream != null) {
                            try {
                                bufferedOutputStream.close();
                            } catch (Exception e) {

                            }
                        }
                    }
                }
                entry = (TarArchiveEntry) in.getNextEntry();
            }

        } catch (ArchiveException | IOException e) {
            logger.error("", e);
            throw new VersionFileErrorException("uncompressFile error srcFile[" + srcFile + "] destDir[" + destDir
                    + "]");
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (in != null) {
                    in.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }

    private void mkFolder(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    private File mkFile(String fileName) {
        File f = new File(fileName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            logger.error("", e);
        }
        return f;
    }

    private void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
        }
        file.delete();
    }
}
