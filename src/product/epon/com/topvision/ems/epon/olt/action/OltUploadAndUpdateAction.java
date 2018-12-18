/***********************************************************************
 * $Id: OltUploadAndUpdateAction.java,v1.0 2013年10月28日 上午9:18:53 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.epon.config.domain.OltControlFileCommand;
import com.topvision.ems.epon.exception.OltFileControlException;
import com.topvision.ems.epon.olt.domain.OltFileAttribute;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.epon.olt.service.OltUploadAndUpdateService;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.web.util.HttpResponseUtils;
import com.topvision.platform.domain.FtpConnectInfo;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.service.FtpConnectService;
import com.topvision.platform.zetaframework.util.FileUploadUtil;

/**
 * @author Bravin
 * @created @2013年10月28日-上午9:18:53
 *
 */
@Controller("oltUploadAndUpdateAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OltUploadAndUpdateAction extends AbstractEponAction {
    private static final long serialVersionUID = 2458308723213783758L;
    private final Logger logger = LoggerFactory.getLogger(OltUploadAndUpdateAction.class);
    @Autowired
    private FtpConnectService ftpConnectService;
    @Autowired
    private OltService oltService;
    @Autowired
    private OltUploadAndUpdateService oltUploadAndUpdateService;
    private boolean ftpServiceEnable;
    private String filePath;
    private String fileFileName;
    private String fileFtpServerFileName;
    private String chooseFileName;
    private String oltUpdateType;
    private String topOnuUpgradeFileName;
    private Boolean backNeedUpload;

    /**
     * 文件管理展示
     * 
     * @return String
     */
    public String showFileManage() {
        ftpServiceEnable = ftpConnectService.getFtpConnectStatus().isReachable();
        return SUCCESS;
    }

    /**
     * 文件展示
     * 
     * @return String
     */
    public String fileShow() {
        List<OltFileAttribute> setList;
        boolean flag = true;
        HttpResponseUtils.setXMLResponse(ServletActionContext.getResponse());
        try {
            setList = oltUploadAndUpdateService.getOltFilePath(entityId);
        } catch (SnmpException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("FileLoad Failure", e);
            }
            // flag = false;
            return NONE;
        }
        for (OltFileAttribute oltFileAttribute : setList) {
            if (oltFileAttribute.getFileAttribute().equals(EponConstants.FILE_TYPE_DIR)) {
                oltFileAttribute.setFileName(getString("Business.filePath", "epon"));
                // 设备对于文件夹也会有默认的大小，显示时需要对文件夹大小显示为空
                oltFileAttribute.setFileSize(null);
            }
        }
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("totalProperty", setList.size());
        json.put("data", setList);
        if (flag) {
            writeDataToAjax(JSONObject.fromObject(json));
        } else {
            writeDataToAjax("wrong");
        }
        return NONE;
    }

    /**
     * 实时获得文件上传的大小
     * 
     * @return String
     * @throws Exception
     */
    public String checkFileSize() throws Exception {
        String result;
        OltFileAttribute fileAttribute = new OltFileAttribute();
        String destFileName = request.getParameter("destFileName");
        fileAttribute.setFilePath(EponUtil.getOidFromStringWithLength(filePath));
        fileAttribute.setFileName(EponUtil.getOidFromStringWithLength(destFileName));
        fileAttribute.setEntityId(entityId);
        result = oltUploadAndUpdateService.getOltFileSize(fileAttribute);
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 获得文件上传的路径
     * 
     * @return String
     * @throws Exception
     */
    public String getFileDir() throws Exception {
        String result = oltUploadAndUpdateService.getFileDir(entityId, oltUpdateType);
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 打开进度条页面
     * 
     * @return String
     */
    public String showProgressBar() {
        return SUCCESS;
    }

    /**
     * 判断OLT上传文件状态 ， 根据Service抛出异常进行处理 该方法同downFile配合使用，方法目的是为了下发命令OLT上传文件，当有异常时不进行下一步
     * 如果成功，则服务器从FTP服务器读取文件开始写入客户端
     * 
     * @return String
     * @throws Exception
     */
    public String controlOltUpLoad() throws Exception {
        String result;
        OltControlFileCommand oltControlFileCommand = new OltControlFileCommand();
        oltControlFileCommand.setFileTransferProtocolType(EponConstants.FILE_TRANS_PRO_FTP);
        FtpConnectInfo connectInfo = ftpConnectService.getFtpConnectAttr();
        oltControlFileCommand.setServerIpAddress(connectInfo.getIp());
        oltControlFileCommand.setFtpUserName(connectInfo.getUserName());
        oltControlFileCommand.setFtpUserPassword(connectInfo.getPwd());
        // TODO 由于文件名长度过大的时候，容易造成加上时间标识符后出错，所以暂时在ftp服务器上的文件保持原名
        // String ftpServerFileName = "DownLoad"+EponUtil.getSystemDate() +
        // fileFileName;
        String ftpServerFileName = fileFileName;
        oltControlFileCommand.setTransferFileDstNamePath(ftpServerFileName);
        oltControlFileCommand.setTransferFileSrcNamePath(filePath + fileFileName);
        // 网管页面所谓的下载实际是对OLT设备的上传操作，OLT设备向FTP服务器上传文件
        oltControlFileCommand.setTransferAction(EponConstants.FILE_OLT_UPLOAD);
        try {
            oltUploadAndUpdateService.contorlOltFile(entityId, oltControlFileCommand);
            // 暂时不处理文件下载的等待时间
            if (oltUploadAndUpdateService.getOltTranslationStatus(entityId, 0L)
                    .equals(EponConstants.FILE_TRANS_SUCCESS)) {
                if (ftpConnectService.isFileExist(ftpServerFileName)) {
                    // 以下三步为了将文件写入相应的downLoad文件夹以作记录
                    /*
                     * File srcFile = new File(SystemConstants.ROOT_REAL_PATH + "META-INF/ftpTemp/"
                     * + ftpServerFileName.toString()); File dstFile = new
                     * File(SystemConstants.ROOT_REAL_PATH + "META-INF/ftpTemp/downLoad/" +
                     * EponUtil.getSystemDate() + ftpServerFileName.toString());
                     * EponUtil.writeFile(srcFile, dstFile);
                     */
                    result = "downLoadSuccess:" + ftpServerFileName + ":" + fileFileName;
                } else {
                    result = getString("Business.fileNotExistInFtpServer", "epon");
                }
            } else {
                result = getString("Business.downloadFileFormOltFailure", "epon");
            }
        } catch (OltFileControlException ofce) {
            result = getString(ofce.getMessage(), "epon");
        } catch (SnmpException se) {
            result = getString("Business.snmpWrong", "epon");
        } catch (Exception se) {
            result = "error";
            logger.error("controlOltUpLoad fail {}", se.toString());
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 服务器从FTP服务器上取得文件，并写入流输出
     * 
     * @throws IOException
     */
    public void downFile() throws IOException {
        int i;
        byte[] b = new byte[1024];
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/x-download");
        String fileName = fileFileName;
        fileName = URLEncoder.encode(fileName, "UTF-8");
        ServletActionContext.getResponse().addHeader("Content-Disposition",
                "attachment;filename=" + fileFtpServerFileName);
        /*
         * Properties prop = new Properties(); InputStream in = new FileInputStream(new
         * File(SystemConstants.WEB_INF_REAL_PATH + "/conf/ftp.properties")); prop.load(in);
         * StringBuilder sb = new StringBuilder(); sb.append(SystemConstants.ROOT_REAL_PATH);
         * sb.append(prop.getProperty("FileTemp.Root")); sb.append("/"); sb.append(fileName);
         */
        try {
            File rFile = ftpConnectService.downloadFile(fileName, fileName);
            // if (ftpConnectService.downloadFile(fileName, sb.toString())) {
            if (rFile != null) {
                fis = new FileInputStream(rFile);
                out = ServletActionContext.getResponse().getOutputStream();
                while ((i = fis.read(b)) > 0) {
                    out.write(b, 0, i);
                }
            }
        } catch (Exception e) {
            logger.debug("downFile method is error:{}", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (out != null) {
                    out.flush();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.debug("downFile method is error:{}", e);
            }
        }
    }

    /**
     * OLT设备升级的文件验证，根据升级类型进行相应判断
     * 
     * @return String
     * @throws IOException
     */
    public String oltUpdateValidate() throws IOException {
        String result;
        StringBuilder sb = new StringBuilder();
        if (!"mpu.bin".equals(fileFileName)) {
            sb.append("{success:false,msg:'");
            sb.append(getString("Business.wrongMpuaName", "epon"));
            sb.append("'}");
            result = sb.toString();
        } else {
            result = "{success:true,msg:'suc'}";
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 
     * 
     * @return String
     * @throws Exception
     */
    public String oltUpdateBack() throws Exception {
        JSONObject json = new JSONObject();
        Long fileSize = 0L;
        boolean uploadFileResult = true;
        // 如果提示同时上传到备用目录，则上传文件目的路径为appbak
        String result = null;
        StringBuilder toSrc = new StringBuilder();
        toSrc.append(chooseFileName);
        String isBakDstNameString = "";
        if (oltService.oltVersionCompare(entityId) >= 0) {
            if (!oltUpdateType.equals("bootrom")) {
                isBakDstNameString = "/tffs0/appbak/" + oltUpdateType + ".bin";
            } else {
                isBakDstNameString = "/tffs0/" + oltUpdateType + ".bin";
            }
        } else {
            if (oltUpdateType.equals("mpu")) {
                isBakDstNameString = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_MPU_BACK)
                        .getFileDirPath();
            } else if (oltUpdateType.equals("geu")) {
                isBakDstNameString = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_GEU_BACK)
                        .getFileDirPath();
            } else if (oltUpdateType.equals("epu")) {
                isBakDstNameString = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_EPU_BACK)
                        .getFileDirPath();
            } else if (oltUpdateType.equals("xgu")) {
                isBakDstNameString = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_XGU_BACK)
                        .getFileDirPath();
            } else if (oltUpdateType.equals("mpub")) {
                isBakDstNameString = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_MPUB_BACK)
                        .getFileDirPath();
            } else if (oltUpdateType.equals("bootrom")) {
                isBakDstNameString = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_BOOTROM_BACK)
                        .getFileDirPath();
            } else if (oltUpdateType.equals("bootrom_mpub")) {
                isBakDstNameString = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_MPUB_BOOTROM_BACK)
                        .getFileDirPath();
            } else if (oltUpdateType.equals("meu")) {
                isBakDstNameString = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_MEU_BACK)
                        .getFileDirPath();
            } else if (oltUpdateType.equals("mef")) {
                isBakDstNameString = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_MEF_BACK)
                        .getFileDirPath();
            } else if (oltUpdateType.equals("mgu")) {
                isBakDstNameString = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_MGU_BACK)
                        .getFileDirPath();
            } else if (oltUpdateType.equals("gpu")) {
                isBakDstNameString = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_GPU_BACK)
                        .getFileDirPath();
            }
        }
        FtpConnectInfo connectInfo = ftpConnectService.getFtpConnectAttr();

        // TODO由于设备没有遵守规则 网管取消对bin文件格式校验
        /*
         * if (!oltUpdateType.equals("onuUpgrade") && !BoardType4Agent.compareType(oltUpdateType,
         * new FileInputStream(file))) { write("WRONG_TYPE"); return NONE; } else {
         */
        if (backNeedUpload) {
            File file = FileUploadUtil.checkout(request, chooseFileName);
            fileSize = file.length();
            uploadFileResult = ftpConnectService.uploadFile(file, toSrc.toString());
        }
        if (uploadFileResult) {
        OltControlFileCommand oltControlFileCommand = new OltControlFileCommand();
        oltControlFileCommand.setFileTransferProtocolType(EponConstants.FILE_TRANS_PRO_FTP);
        oltControlFileCommand.setServerIpAddress(connectInfo.getIp());
        oltControlFileCommand.setFtpUserName(connectInfo.getUserName());
        oltControlFileCommand.setFtpUserPassword(connectInfo.getPwd());
        oltControlFileCommand.setTransferFileSrcNamePath(toSrc.toString());
        // 网管页面所谓的上传实际是对OLT设备的下载操作，OLT设备从FTP服务器下载文件
        oltControlFileCommand.setTransferAction(EponConstants.FILE_OLT_DOWNLOAD);
        oltControlFileCommand.setTransferFileDstNamePath(isBakDstNameString);
        try {
            oltUploadAndUpdateService.contorlOltFile(entityId, oltControlFileCommand);
            // 上传到备用板的操作前需要前台传入文件的大小，此处暂时用0代替,后续修改
                if (oltUploadAndUpdateService.getOltTranslationStatus(entityId, fileSize).equals(
                        EponConstants.FILE_TRANS_SUCCESS)) {
                result = "success";
            } else {
                result = "bakFailure";
            }
        } catch (OltFileControlException ofce) {
            result = ofce.getMessage();
        } catch (SnmpException se) {
            result = getString("Business.snmpWrong", "epon");
        }
        }

        json.put("result", result);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * OLT设备升级所需要的操作，包括对主控板，ONU以及其它部分的升级操作
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltUploadAndUpdateAction", operationName = "oltUpdate")
    public String oltUpdate() throws Exception {
        JSONObject json = new JSONObject();
        String result = null;
        Long fileSize = 0L;
        boolean uploadFileResult = false;
        StringBuilder toSrc = new StringBuilder();
        String transferFileDstNamePath = null;
        FtpConnectInfo connectInfo = ftpConnectService.getFtpConnectAttr();
        if (oltUpdateType.equals("mpu")) {
            toSrc.append(chooseFileName);
            if (oltService.oltVersionCompare(entityId) >= 0) {
                transferFileDstNamePath = "/tffs0/app/mpu.bin";
            } else {
                transferFileDstNamePath = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_MPU)
                        .getFileDirPath();
            }
        } else if (oltUpdateType.equals("epu")) {
            toSrc.append(chooseFileName);
            if (oltService.oltVersionCompare(entityId) >= 0) {
                transferFileDstNamePath = "/tffs0/app/epu.bin";
            } else {
                transferFileDstNamePath = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_EPU)
                        .getFileDirPath();
            }
        } else if (oltUpdateType.equals("geu")) {
            toSrc.append(chooseFileName);
            if (oltService.oltVersionCompare(entityId) >= 0) {
                transferFileDstNamePath = "/tffs0/app/geu.bin";
            } else {
                transferFileDstNamePath = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_GEU)
                        .getFileDirPath();
            }
        } else if (oltUpdateType.equals("xgu")) {
            toSrc.append(chooseFileName);
            if (oltService.oltVersionCompare(entityId) >= 0) {
                transferFileDstNamePath = "/tffs0/app/xgu.bin";
            } else {
                transferFileDstNamePath = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_XGU)
                        .getFileDirPath();
            }
        } else if (oltUpdateType.equals("onuUpgrade")) {
            toSrc.append(topOnuUpgradeFileName);
            if (oltService.oltVersionCompare(entityId) >= 0) {
                topOnuUpgradeFileName = "/tffs0/onuapp/" + topOnuUpgradeFileName;
            } else {
                topOnuUpgradeFileName = oltService.getOltFileDirEntry(entityId, EponConstants.DIR_ONU).getFileDirPath()
                        + topOnuUpgradeFileName;
            }
            transferFileDstNamePath = topOnuUpgradeFileName;
        } else if (oltUpdateType.equals("mpub")) {
            toSrc.append(chooseFileName);
            if (oltService.oltVersionCompare(entityId) >= 0) {
                transferFileDstNamePath = "/tffs0/app/mpub.bin";
            } else {
                transferFileDstNamePath = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_MPUB)
                        .getFileDirPath();
            }
        } else if (oltUpdateType.equals("bootrom")) {
            toSrc.append(chooseFileName);
            if (oltService.oltVersionCompare(entityId) >= 0) {
                transferFileDstNamePath = "/tffs0/bootrom.bin";
            } else {
                transferFileDstNamePath = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_BOOTROM)
                        .getFileDirPath();
            }
        } else if (oltUpdateType.equals("bootrom_mpub")) {
            toSrc.append(chooseFileName);
            transferFileDstNamePath = "/yaffs2/app/" + chooseFileName;
        } else if (oltUpdateType.equals("meu")) {
            toSrc.append(chooseFileName);
            transferFileDstNamePath = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_MEU).getFileDirPath();
        } else if (oltUpdateType.equals("mef")) {
            toSrc.append(chooseFileName);
            transferFileDstNamePath = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_MEF).getFileDirPath();
        } else if (oltUpdateType.equals("mgu")) {
            toSrc.append(chooseFileName);
            transferFileDstNamePath = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_MGU).getFileDirPath();
        } else if (oltUpdateType.equals("gpu")) {
            toSrc.append(chooseFileName);
            transferFileDstNamePath = oltService.getOltFileDirEntry(entityId, EponConstants.FILE_GPU).getFileDirPath();
        }

        File file = FileUploadUtil.checkout(request, chooseFileName);
        // TODO由于设备没有遵守规则 网管取消对bin文件格式校验
        /*
         * if (!oltUpdateType.equals("onuUpgrade") && !BoardType4Agent.compareType(oltUpdateType,
         * new FileInputStream(file))) { write("WRONG_TYPE"); return NONE; } else {
         */
        fileSize = file.length();
        uploadFileResult = ftpConnectService.uploadFile(file, toSrc.toString());
        // }
        if (uploadFileResult) {
            OltControlFileCommand oltControlFileCommand = new OltControlFileCommand();
            oltControlFileCommand.setFileTransferProtocolType(EponConstants.FILE_TRANS_PRO_FTP);
            oltControlFileCommand.setServerIpAddress(connectInfo.getIp());
            oltControlFileCommand.setFtpUserName(connectInfo.getUserName());
            oltControlFileCommand.setFtpUserPassword(connectInfo.getPwd());
            oltControlFileCommand.setTransferFileDstNamePath(transferFileDstNamePath);
            oltControlFileCommand.setTransferFileSrcNamePath(toSrc.toString());
            // 网管页面所谓的上传实际是对OLT设备的下载操作，OLT设备从FTP服务器下载文件
            oltControlFileCommand.setTransferAction(EponConstants.FILE_OLT_DOWNLOAD);
            try {
                // TODO 文件传输完毕需要接收到Trap之后判断状态。（此处逻辑需要修改）
                oltUploadAndUpdateService.contorlOltFile(entityId, oltControlFileCommand);
                // 获得主文件升级的状态
                int fileUpdateStatusResult = oltUploadAndUpdateService.getOltTranslationStatus(entityId, fileSize);
                if (fileUpdateStatusResult == EponConstants.FILE_TRANS_SUCCESS) {
                    if (oltUpdateType.equals("mpu") || oltUpdateType.equals("mpub") || oltUpdateType.equals("epu")
                            || oltUpdateType.equals("geu") || oltUpdateType.equals("xgu")
                            || oltUpdateType.equals("bootrom") || oltUpdateType.equals("bootrom_mpub")
                            || oltUpdateType.equals("meu") || oltUpdateType.equals("mef")
                            || oltUpdateType.equals("mgu") || oltUpdateType.equals("gpu")) {
                        // 该处逻辑需要修改为ONU升级逻辑一致。（屏蔽掉上传文件的动作）
                        result = "success";
                        operationResult = OperationLog.SUCCESS;
                    } else if (oltUpdateType.equals("onuUpgrade")) {
                        // 因为操作日志的原因，暂时将处理逻辑通过链式跳转的方式处理。
                        // String requestUrl = request.getRequestURI().replace("/epon/oltUpdate.tv",
                        // "/onu/onuUpdate.tv");
                        // response.sendRedirect(requestUrl);
                        return "onuUpgrade";
                    }
                } else if (fileUpdateStatusResult == EponConstants.FILE_TRANS_TIMELIMIT) {
                    result = "fileNoTime";
                } else if (fileUpdateStatusResult == EponConstants.FILE_TRANS_FAILURE) {
                    result = "fileNotExists";
                }
            } catch (OltFileControlException ofce) {
                result = ofce.getMessage();
                operationResult = OperationLog.FAILURE;
            } catch (SnmpException se) {
                result = getString("Business.snmpWrong", "epon");
                operationResult = OperationLog.FAILURE;
            }
        }
        json.put("result", result);
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 将上传的文件复制到本地FTP目录 并且下发命令上传文件到OLT设备
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltUploadAndUpdateAction", operationName = "upLoadFileToFile")
    public String upLoadFileToFile() throws Exception {
        JSONObject ret = new JSONObject();
        String result = "uploadError!";
        boolean uploadFileResult = false;
        Long fileSize = 0L;
        String destFileName = ServletActionContext.getRequest().getParameter("destFileName");
        StringBuilder toSrc = new StringBuilder();
        FtpConnectInfo connectInfo = ftpConnectService.getFtpConnectAttr();
        // TODO 由于文件名长度过大的时候，容易造成加上时间标识符后出错，所以暂时在ftp服务器上的文件保持原名
        // toSrc.append("UpLoad");
        // toSrc.append(EponUtil.getSystemDate());
        toSrc.append(destFileName);
        MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();
        Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();
        while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
            String inputName = fileParameterNames.nextElement();
            String[] contentType = multiWrapper.getContentTypes(inputName);
            if (isNonEmpty(contentType)) {
                String[] fileName = multiWrapper.getFileNames(inputName);
                if (isNonEmpty(fileName)) {
                    File[] files = multiWrapper.getFiles(inputName);
                    if (files != null) {
                        for (File tmpFile : files) {
                            fileSize = tmpFile.length();
                            uploadFileResult = ftpConnectService.uploadFile(tmpFile, toSrc.toString());
                            // result = EponUtil.writeFile(file1, toFile);
                            // 对于这里的上传操作，这里只处理单个文件
                            break;
                        }
                    }
                }
            }
        }
        // if (!result.equals("fileNotExists")) {
        if (uploadFileResult) {
            OltControlFileCommand oltControlFileCommand = new OltControlFileCommand();
            oltControlFileCommand.setFileTransferProtocolType(EponConstants.FILE_TRANS_PRO_FTP);
            oltControlFileCommand.setServerIpAddress(connectInfo.getIp());
            oltControlFileCommand.setFtpUserName(connectInfo.getUserName());
            oltControlFileCommand.setFtpUserPassword(connectInfo.getPwd());
            oltControlFileCommand.setTransferFileDstNamePath(filePath + destFileName);
            oltControlFileCommand.setTransferFileSrcNamePath(toSrc.toString());
            // 网管页面所谓的上传实际是对OLT设备的下载操作，OLT设备从FTP服务器下载文件
            oltControlFileCommand.setTransferAction(EponConstants.FILE_OLT_DOWNLOAD);
            try {
                oltUploadAndUpdateService.contorlOltFile(entityId, oltControlFileCommand);
                int fileStatusResult = oltUploadAndUpdateService.getOltTranslationStatus(entityId, fileSize);
                if (fileStatusResult == EponConstants.FILE_TRANS_SUCCESS) {
                    result = "success";
                    operationResult = OperationLog.SUCCESS;
                } else if (fileStatusResult == EponConstants.FILE_TRANS_TIMELIMIT) {
                    result = "fileNoTime";
                } else if (fileStatusResult == EponConstants.FILE_TRANS_FAILURE) {
                    result = "fileNotExists";
                } else if (fileStatusResult == -1) {
                    result = "fileNoResponse";
                }
            } catch (OltFileControlException ofce) {
                result = getString(ofce.getMessage(), "epon");
                operationResult = OperationLog.FAILURE;
            } catch (SnmpException se) {
                result = getString("Business.snmpWrong", "epon");
                operationResult = OperationLog.FAILURE;
            }
        }
        ret.put("result", result);
        writeDataToAjax(ret);
        return NONE;
    }

    /**
     * 显示文件上传，注入文件夹内容 参数中的filePath为所有文件夹组成的字符串，与其他方法中不同
     * 
     * @return String
     */
    public String upLoadFile() {
        String data = "[";
        if (filePath.split(",").length > 0) {
            for (int i = 0; i < filePath.split(",").length; i++) {
                data += "['" + filePath.split(",")[i] + "','" + filePath.split(",")[i] + "'],";
            }
            filePath = data.substring(0, data.length() - 1) + "]";
        }
        return SUCCESS;
    }

    /**
     * 文件删除
     * 
     * @return String
     * @throws Exception
     */
    @OperationLogProperty(actionName = "oltUploadAndUpdateAction", operationName = "deleteFile")
    public String deleteFile() throws Exception {
        OltFileAttribute oltFileAttribute = new OltFileAttribute();
        oltFileAttribute.setFilePath(filePath);
        oltFileAttribute.setFileName(fileFileName);
        oltFileAttribute.setFileManagementAction(EponConstants.FILE_DELETE);
        String result;
        try {
            oltUploadAndUpdateService.deleteOltFile(entityId, oltFileAttribute);
            result = "deleteOK";
            operationResult = OperationLog.SUCCESS;
        } catch (OltFileControlException ofce) {
            result = ofce.getMessage();
            logger.debug("oltFile Error:{}", ofce);
            operationResult = OperationLog.FAILURE;
        }
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * 读取文件信息的时候判断数组是否有可用对象
     * 
     * @param objArray
     *            数组列表
     * @return String
     */
    private boolean isNonEmpty(Object[] objArray) {
        boolean result = false;
        for (int index = 0; index < objArray.length && !result; index++) {
            if (objArray[index] != null) {
                result = true;
            }
        }
        return result;
    }

    public boolean isFtpServiceEnable() {
        return ftpServiceEnable;
    }

    public void setFtpServiceEnable(boolean ftpServiceEnable) {
        this.ftpServiceEnable = ftpServiceEnable;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileFileName() {
        return fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public String getFileFtpServerFileName() {
        return fileFtpServerFileName;
    }

    public void setFileFtpServerFileName(String fileFtpServerFileName) {
        this.fileFtpServerFileName = fileFtpServerFileName;
    }

    public String getChooseFileName() {
        return chooseFileName;
    }

    public void setChooseFileName(String chooseFileName) {
        this.chooseFileName = chooseFileName;
    }

    public String getOltUpdateType() {
        return oltUpdateType;
    }

    public void setOltUpdateType(String oltUpdateType) {
        this.oltUpdateType = oltUpdateType;
    }

    public String getTopOnuUpgradeFileName() {
        return topOnuUpgradeFileName;
    }

    public void setTopOnuUpgradeFileName(String topOnuUpgradeFileName) {
        this.topOnuUpgradeFileName = topOnuUpgradeFileName;
    }

    public Boolean getBackNeedUpload() {
        return backNeedUpload;
    }

    public void setBackNeedUpload(Boolean backNeedUpload) {
        this.backNeedUpload = backNeedUpload;
    }

}