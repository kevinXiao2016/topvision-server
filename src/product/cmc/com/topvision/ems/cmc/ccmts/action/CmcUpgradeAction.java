/***********************************************************************
 * $Id: CmcConfigFileAction.java,v1.0 2013-11-1 上午10:32:50 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcDevSoftware;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.cmc.util.IPAddressUtils;
import com.topvision.ems.cmc.util.StringUtils;
import com.topvision.ems.devicesupport.version.util.DeviceFuctionSupport;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.constants.Symbol;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.TftpInfo;
import com.topvision.platform.service.TftpServerService;

import net.sf.json.JSONObject;

/**
 * @author dosion
 * @created @2013-11-1-上午10:32:50
 *
 */
@Controller("cmcUpgradeAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmcUpgradeAction extends BaseAction {
    private static final long serialVersionUID = -2387492237827442534L;
    private final Logger logger = LoggerFactory.getLogger(CmcUpgradeAction.class);
    @Autowired
    private CmcService cmcService;
    @Autowired
    private TftpServerService tftpServerService;

    private Long cmcId;
    private Integer fileType;
    private CmcDevSoftware cmcDevSoftware;
    private String filename;

    private String source;

    /**
     * 展示CMC升级页面
     * 
     * @return String
     */
    public String showCmcUpdate() {
        if (fileType == CmcConstants.UPDATE_IMAGE) {
            return "image";
        } else if (fileType == CmcConstants.UPDATE_CONFIG) {
            return "config";
        }
        return SUCCESS;
    }

    /**
     * 升级CMC
     * 
     * @return String
     * @throws Exception
     */
    public String updateCmc() throws Exception {
    	JSONObject ret = new JSONObject();
        CmcAttribute cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);

        String dolVersion = cmcAttribute.getDolVersion();
        CmcDevSoftware cmcDevSoftware = new CmcDevSoftware();
        TftpInfo tftpServerInfo = tftpServerService.getTftpServerAttr();
        String tftpRootPath = tftpServerInfo.getRealPath();

        Long cmcIndex = cmcAttribute.getCmcIndex();
        source = Symbol.BRACKET_LEFT + CmcIndexUtils.getSlotNo(cmcIndex).toString() + Symbol.SLASH
                + CmcIndexUtils.getPonNo(cmcIndex).toString() + Symbol.SLASH
                + CmcIndexUtils.getCmcId(cmcIndex).toString() + Symbol.BRACKET_RIGHT;
        String result = "transError";
        // UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        String destFileName = ServletActionContext.getRequest().getParameter("destFileName");
        StringBuilder toSrc = new StringBuilder();
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
                        for (File file1 : files) {
                            File toFile = new File(tftpRootPath + toSrc.toString());
                            result = CmcUtil.writeFile(file1, toFile);
                        }
                    }
                }
            }
        }

        try {
            // Properties prop = new Properties();
            // InputStream in = new FileInputStream(new File(SystemConstants.WEB_INF_REAL_PATH +
            // "/conf/tftp.properties"));
            // prop.load(in);
            // String tftpServer = prop.getProperty("TFtpServer.Ip");

            String tftpServer = tftpServerInfo.getIp();
            if (StringUtils.isEmpty(tftpServer) || IPAddressUtils.isLocalhost(tftpServer)) {
                tftpServer = EnvironmentConstants.getHostAddress();
            }

            cmcDevSoftware.setCmcId(cmcId);
            cmcDevSoftware.setEntityId(cmcService.getEntityIdByCmcId(cmcId));
            cmcDevSoftware.setTopCcmtsSwTftpTransferAction(1);// load
            cmcDevSoftware.setDocsDevSwServerAddressType(1);// ipv4
            cmcDevSoftware.setDocsDevSwFilename(toSrc.toString());// update file name
            cmcDevSoftware.setDocsDevSwServerTransportProtocol(1);// tftp
            if (isInetAddrVersion(dolVersion)) {
                cmcDevSoftware.setDocsDevSwServerAddress(tftpServer);
                cmcDevSoftware.setDocsDevSwServerAddressOld(null);
            } else {
                cmcDevSoftware.setDocsDevSwServerAddress(null);
                cmcDevSoftware.setDocsDevSwServerAddressOld(tftpServer);
            }
            cmcDevSoftware.setTopCcmtsSwTftpTransferFileType(fileType);
            int progress = cmcService.getCmcUpdateRecord(cmcId);
            if (progress > 0 && progress < 100) {
                result = "entityBusy";
            } else {
                cmcService.updateCmc(cmcDevSoftware);
                // 定时查询文件上传进度
                progress = cmcService.getCmcUpdateStatus(cmcId);
                switch (progress) {
                case 100:
                    result = "updateSuccess";
                    break;
                case 200:
                    result = "fileTransTimeout";
                    break;
                case 201:
                    result = "eraseError";
                    break;
                case 202:
                    result = "download image failed";
                    break;
                case 203:
                    result = "open image file failed";
                    break;
                case 204:
                    result = "length of image file is invalid";
                    break;
                case 205:
                    result = "check the magic of file failed";
                    break;
                case 206:
                    result = "erase flash failed";
                    break;
                case 207:
                    result = "write flash failed";
                    break;
                case 208:
                    result = "CRC verify failed";
                    break;
                case 209:
                    result = "decompress file failed";
                    break;
                case 300:
                    result = "snmpTimeout";
                    break;
                }
                // if (progress == 100) {
                // result = "updateSuccess";
                // } else if (progress == 200) {
                // result = "fileTransTimeout";
                // } else if (progress == 201) {
                // result = "eraseError";
                // } else if (progress == 300) {
                // result = "snmpTimeout";
                // }
            }

        } catch (Exception e) {
            result = "exception";
            logger.debug("updateCmc error:{}", e);
        }
        ret.put("result", result);
        writeDataToAjax(ret);
        return NONE;
    }

    /**
     * 判断升级docsDevSwServerAddress是否为修改后的InetAddress
     * 对于版本号2.2.6.X（2.2.6.6以上版本）、2.2.8.0版本以上（包括2.2.8.X、2.2.9.X版本）都是用InetAddr，其余用string
     * 
     * @param dolversion
     * @return
     */
    private boolean isInetAddrVersion(String dolversion) {
        if ((DeviceFuctionSupport.compareVersion(dolversion, "V2.2.6.6") >= 0
                && DeviceFuctionSupport.compareVersion(dolversion, "V2.2.7.0") == -1)
                || (DeviceFuctionSupport.compareVersion(dolversion, "V2.2.8.0") >= 0)) {
            return true;
        }
        return false;
    }

    /**
     * 获取拆分型CMC升级进度
     * 
     * @return String
     */
    public String getCmcUpdateProgress() {
        int progress = -1;
        String result = null;
        Map<String, Object> message = new HashMap<String, Object>();
        try {

            progress = cmcService.getCmcUpdateRecord(cmcId);
            result = "success1";
        } catch (Exception e) {
            result = "false";
            logger.debug("", e);
        } finally {
            message.put("message", result);
            message.put("progress", progress);
            writeDataToAjax(JSONObject.fromObject(message));
        }
        return NONE;
    }

    public String showDownloadConfig() {
        return SUCCESS;
    }

    public String uploadConfigFile() throws Exception {
        CmcAttribute cmcAttribute = cmcService.getCmcAttributeByCmcId(cmcId);
        String dolVersion = cmcAttribute.getDolVersion();
        String result = null;
        // EMS-11014 【EMS-V2.4.8.0】：设备快照-导出配置E型设备无法成功导出配置
        TftpInfo tftpServerInfo = tftpServerService.getTftpServerAttr();
        String tftpServer = tftpServerInfo.getIp();
        fileType = CmcConstants.UPDATE_CONFIG;
        // 设置参数，将配置文件上传到网管内置TFTP服务器
        try {
            // Properties prop = new Properties();
            // InputStream in = new FileInputStream(new File(SystemConstants.WEB_INF_REAL_PATH +
            // "/conf/tftp.properties"));
            // prop.load(in);
            // String tftpServer = prop.getProperty("TFtpServer.Ip");

            logger.debug("%%%%%%%tftpServer:" + tftpServer);
            if (tftpServer == null || tftpServer.equalsIgnoreCase("") || tftpServer.equalsIgnoreCase("127.0.0.1")
                    || tftpServer.equalsIgnoreCase("localhost")) {
                tftpServer = EnvironmentConstants.getHostAddress();
            }
            cmcDevSoftware = new CmcDevSoftware();
            cmcDevSoftware.setCmcId(cmcId);
            cmcDevSoftware.setEntityId(cmcService.getEntityIdByCmcId(cmcId));
            cmcDevSoftware.setTopCcmtsSwTftpTransferAction(2);// upload file to tftp
            cmcDevSoftware.setDocsDevSwServerAddressType(1);// ipv4
            cmcDevSoftware.setDocsDevSwFilename(filename);// file name
            cmcDevSoftware.setDocsDevSwServerTransportProtocol(1);// tftp
            if (isInetAddrVersion(dolVersion)) {
                cmcDevSoftware.setDocsDevSwServerAddress(tftpServer);
                cmcDevSoftware.setDocsDevSwServerAddressOld(null);
            } else {
                cmcDevSoftware.setDocsDevSwServerAddress(null);
                cmcDevSoftware.setDocsDevSwServerAddressOld(tftpServer);
            }
            cmcDevSoftware.setTopCcmtsSwTftpTransferFileType(fileType);
            int progress = cmcService.getCmcUpdateRecord(cmcId);
            if (progress > 0 && progress < 100) {
                result = "entityBusy";
            } else {
                cmcService.updateCmc(cmcDevSoftware);
                // 定时查询文件上传进度
                progress = cmcService.getCmcUpdateStatus(cmcId);
                if (progress == 100) {
                    result = "updateSuccess";
                } else if (progress == 200) {
                    result = "fileTransTimeout";
                } else if (progress == 201) {
                    result = "eraseError";
                } else if (progress == 300) {
                    result = "snmpTimeout";
                }
            }
        } catch (SnmpException e) {
            logger.debug("downloadConfigFile is error:{}", e);
            result = "snmpTimeout";
        }
        writeDataToAjax(result);
        return NONE;
    }

    public String downloadConfigFile() throws UnsupportedEncodingException {
        TftpInfo tftpServerInfo = tftpServerService.getTftpServerAttr();
        String tftpRootPath = tftpServerInfo.getRealPath();
        // 上传完成后将升级文件下载到本地
        // fileRealName.append("META-INF\\tftpTemp\\");
        // modify by YangYi 修改路径 兼容LINUX
        String fileRealName = tftpRootPath + filename;
        int i;
        byte[] b = new byte[1024];
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/x-download");
        ServletActionContext.getResponse().addHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        try {
            File rFile = new File(fileRealName);
            if (rFile != null) {
                fis = new FileInputStream(rFile);
                out = ServletActionContext.getResponse().getOutputStream();
                while ((i = fis.read(b)) > 0) {
                    out.write(b, 0, i);
                }
            }
        } catch (Exception e) {
            logger.debug("downloadConfigFile is error:{}", e);
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
                logger.debug("downloadConfigFile is error:{}", e);
            }
        }
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

    public CmcService getCmcService() {
        return cmcService;
    }

    public void setCmcService(CmcService cmcService) {
        this.cmcService = cmcService;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public CmcDevSoftware getCmcDevSoftware() {
        return cmcDevSoftware;
    }

    public void setCmcDevSoftware(CmcDevSoftware cmcDevSoftware) {
        this.cmcDevSoftware = cmcDevSoftware;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
