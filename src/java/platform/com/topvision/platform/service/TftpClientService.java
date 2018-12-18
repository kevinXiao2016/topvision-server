/***********************************************************************
 * $Id: TftpClientService.java,v1.0 2014-1-21 上午10:50:03 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/

package com.topvision.platform.service;

import java.io.File;

import com.topvision.framework.service.Service;
import com.topvision.platform.domain.TftpClientInfo;

public interface TftpClientService extends Service {

    TftpClientInfo getTftpClientInfo();

    void modifyTftpClient(TftpClientInfo tftpClientInfo);

    Boolean downloadFile(String remoteFileName);

    Boolean uploadFile(File file, String remoteFileName);

    /**
     * 将服务器上的临时文件发送到内置TFTP服务器上
     * 
     * @param file
     * @param remoteFileName
     * @return
     */
    Boolean sendFileToInnerServer(File file, String remoteFileName);

}
