/***********************************************************************
 * $Id: OltUploadAndUpdateServiceImpl.java,v1.0 2013年10月28日 下午4:54:16 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.config.domain.OltControlFileCommand;
import com.topvision.ems.epon.exception.OltFileControlException;
import com.topvision.ems.epon.olt.domain.OltFileAttribute;
import com.topvision.ems.epon.olt.facade.OltFacade;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.olt.service.OltUploadAndUpdateService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author Bravin
 * @created @2013年10月28日-下午4:54:16
 *
 */
@Service("oltUploadAndUpdateService")
public class OltUploadAndUpdateServiceImpl extends BaseService implements OltUploadAndUpdateService {
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private OltService oltService;
    // 用来标示进行等待的线程 用于循环取文件状态的线程
    private final Object mutex = new Object();

    @Override
    @Deprecated
    public void upgradeMasterBoard(Long entityId, Long slotId) {
    }

    @Override
    public List<OltFileAttribute> getOltFilePath(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        return getOltFacade(snmpParam.getIpAddress()).getOltFilePath(snmpParam);
    }

    @Override
    public void deleteOltFile(Long entityId, OltFileAttribute oltFileAttribute) {
        // 判断文件传输状态
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        getOltFacade(snmpParam.getIpAddress()).deleteOltFile(snmpParam, oltFileAttribute);
    }

    @Override
    public void contorlOltFile(Long entityId, OltControlFileCommand oltControlFileCommand) {
        // 判断文件传输状态
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer transferStatus = getOltFacade(snmpParam.getIpAddress()).getOltFileTransferStatus(snmpParam);
        if (transferStatus == null) {
            throw new OltFileControlException("Business.connection");
        } else if (transferStatus.equals(EponConstants.FILE_INPROGRESS)) {
            throw new OltFileControlException("Business.fileControlInProgress");
        } else {
            getOltFacade(snmpParam.getIpAddress()).contorlOltFile(snmpParam, oltControlFileCommand);
        }
    }

    @Override
    public Integer getOltFileState(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer transferStatus = getOltFacade(snmpParam.getIpAddress()).getOltFileTransferStatus(snmpParam);
        if (transferStatus == null) {
            throw new OltFileControlException("Business.connection");
        }
        return transferStatus;
    }

    @Override
    public Integer getOltTranslationStatus(Long entityId, Long fileSize) {
        AtomicInteger count = new AtomicInteger(0);
        Integer fileStatus = EponConstants.FILE_INPROGRESS;
        Long waitTime = fileSize / EponConstants.EPON_FILE_TRANS_RATE;// 传输耗时时长，通过每次传输的时间计算的平均值
        Integer waitCount = (EponConstants.EPON_FILE_TRANS_MAX_TIME - waitTime.intValue() * 1000)
                / EponConstants.EPON_FILE_TRANS_WAIT_INTERVAL;// 等待次数，最大传输时间减去实际的传输时间除以每次的等待间隔
        try {
            Thread.sleep(waitTime * 1000);
        } catch (InterruptedException e1) {
            // 不需要记录，直接进入下一步，开始轮询
        }
        while (fileStatus.equals(EponConstants.FILE_INPROGRESS)) {
            try {
                try {
                    synchronized (mutex) {
                        mutex.wait(EponConstants.EPON_FILE_TRANS_WAIT_INTERVAL);
                    }
                } catch (InterruptedException e) {
                    logger.info("", "file trap receive");
                    fileStatus = getOltFileState(entityId);
                    continue;
                }
                fileStatus = getOltFileState(entityId);
                if (logger.isDebugEnabled()) {
                    logger.debug("count=" + count);
                    logger.debug("fileStatus=" + fileStatus);
                }
            } catch (SnmpException e) {
                logger.info("", e);
            } finally {
                count.incrementAndGet();
                if (count.intValue() > waitCount + 10) {
                    fileStatus = EponConstants.FILE_TRANS_TIMELIMIT;
                    break;
                }
            }
        }
        return fileStatus;
    }

    @Override
    public OltFileAttribute getOltFileAttribute(OltFileAttribute oltFileAttribute) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltFileAttribute.getEntityId());
        return getOltFacade(snmpParam.getIpAddress()).getDomainInfoLine(snmpParam, oltFileAttribute);
    }

    private OltFacade getOltFacade(String ip) {
        return facadeFactory.getFacade(ip, OltFacade.class);
    }

    @Override
    public String getOltFileSize(OltFileAttribute oltFileAttribute) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltFileAttribute.getEntityId());
        oltFileAttribute = getOltFacade(snmpParam.getIpAddress()).getDomainInfoLine(snmpParam, oltFileAttribute);
        return oltFileAttribute.getFileSize().toString();
    }

    @Override
    public String getFileDir(Long entityId, String fileType) {
        String fileDir = "tffs0";
        if (oltService.oltVersionCompare(entityId) >= 0) {
            fileDir = "tffs0";
        } else {
            if (fileType.equals("mpu")) {
                fileDir = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_MPU_BACK).getFileDirPath();
            } else if (fileType.equals("geu")) {
                fileDir = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_GEU_BACK).getFileDirPath();
            } else if (fileType.equals("epu")) {
                fileDir = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_EPU_BACK).getFileDirPath();
            } else if (fileType.equals("xgu")) {
                fileDir = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_XGU_BACK).getFileDirPath();
            } else if (fileType.equals("mpub")) {
                fileDir = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_MPUB_BACK).getFileDirPath();
            } else if (fileType.equals("meu")) {
                fileDir = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_MEU_BACK).getFileDirPath();
            } else if (fileType.equals("mef")) {
                fileDir = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_MEF_BACK).getFileDirPath();
            }
            fileDir = fileDir.split("/")[1];
        }
        return fileDir;
    }

}
