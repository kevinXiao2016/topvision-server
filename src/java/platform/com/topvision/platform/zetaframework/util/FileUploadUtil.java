/***********************************************************************
 * $Id: FileUploadUtil.java,v1.0 2013年12月23日 下午4:03:43 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.util;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.topvision.framework.common.FileUtils;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.util.StringUtil;

/**
 * @author Bravin
 * @created @2013年12月23日-下午4:03:43
 *
 */
public class FileUploadUtil {

    /**
     * 从当前请求中检索出所有上传的文件
     * @param request
     * @return
     */
    public static File[] checkout(HttpServletRequest request) {
        MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) request;
        Enumeration<String> parameterKeys = multiWrapper.getFileParameterNames();
        while (parameterKeys != null && parameterKeys.hasMoreElements()) {
            String paramter = parameterKeys.nextElement();
            String[] contentType = multiWrapper.getContentTypes(paramter);
            if (isNonEmpty(contentType)) {
                String[] fileName = multiWrapper.getFileNames(paramter);
                if (isNonEmpty(fileName)) {
                    return multiWrapper.getFiles(paramter);
                }
            }
        }

        return null;
    }

    /**
     * 从当前请求中检索出指定field上上传的文件,无论有多少个匹配的文件，只取第一个
     * @param request
     * @param fileName
     * @return
     * @throws IOException 
     */
    public static File checkout(HttpServletRequest request, String fileName) throws IOException {
        MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
        File[] files = wrapper.getFiles("file");
        if (files != null && files.length > 0) {
            /* 由于上传时struts将文件名改了，所以难以通过文件名获得上传的文件
            for (File file : files) {
                ;
            }*/
            String copy = StringUtil.format("{0}{1}META-INF{1}fileTemp{1}{2}", SystemConstants.ROOT_REAL_PATH,
                    File.separator, fileName);
            /*String copy = SystemConstants.ROOT_REAL_PATH + File.separator + "META-INF" + File.separator + "fileTemp"
                    + File.separator + fileName;*/
            File copyFile = new File(copy);
            FileUtils.copy(files[0], copyFile);
            return copyFile;
        }
        return null;

    }

    /**
     * 读取文件信息的时候判断数组是否有可用对象
     * 
     * @param objArray
     *            数组列表
     * @return String
     */
    private static boolean isNonEmpty(Object[] objArray) {
        boolean result = false;
        for (int index = 0; index < objArray.length && !result; index++) {
            if (objArray[index] != null) {
                result = true;
            }
        }
        return result;
    }

}
