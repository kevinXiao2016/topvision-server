/***********************************************************************
 * $Id: OltUploadAndUpdateService.java,v1.0 2013年10月28日 下午4:53:58 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.service;

import java.util.List;

import com.topvision.ems.epon.config.domain.OltControlFileCommand;
import com.topvision.ems.epon.olt.domain.OltFileAttribute;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013年10月28日-下午4:53:58
 *
 */
public interface OltUploadAndUpdateService extends Service {
    /**
     * 主控板升级
     * 
     * @param entityId
     *            设备ID
     * @param slotId
     *            主控板槽位ID
     */
    void upgradeMasterBoard(Long entityId, Long slotId);

    /**
     * 获得设备文件列表
     * 
     * @param entityId
     * @return
     */
    List<OltFileAttribute> getOltFilePath(Long entityId);

    /**
     * 删除OLT文件
     * 
     * @param entityId
     *            设备ID
     * @param oltFileAttribute
     *            OLT文件属性
     * @return String
     */
    void deleteOltFile(Long entityId, OltFileAttribute oltFileAttribute);

    /**
     * 获得OLT文件传输状态
     * 
     * @param entityId
     */
    Integer getOltFileState(Long entityId);

    /**
     * 用线程阻塞方法去取文件传输状态
     * 
     * @param entityId
     * @return
     */
    Integer getOltTranslationStatus(Long entityId, Long fileSize);

    /**
     * 获得单个文件的信息
     * 
     * @param oltFileAttribute
     * @return
     */
    // 此方式存在问题需要采用新的方式获得文件属性
    @Deprecated
    OltFileAttribute getOltFileAttribute(OltFileAttribute oltFileAttribute);

    /**
     * 获得单个文件的信息
     * 
     * @param oltFileAttribute
     * @return
     */
    String getOltFileSize(OltFileAttribute oltFileAttribute);

    /**
     * 获得文件路径信息
     * 
     * @param entityId
     * @param fileType
     * @return
     */
    String getFileDir(Long entityId, String fileType);

    void contorlOltFile(Long entityId, OltControlFileCommand oltControlFileCommand);
}
