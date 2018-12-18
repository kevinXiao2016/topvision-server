/***********************************************************************
 * $Id: ExportAction.java,v1.0 2015-6-30 上午10:19:47 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exportAndImport.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.topvision.ems.exportAndImport.service.ExportService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.ems.network.service.EntityExportService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.util.FtpClientUtil;

import jxl.write.WriteException;
import net.sf.json.JSONObject;

/**
 * @author fanzidong
 * @created @2015-6-30-上午10:19:47
 * 
 */
@Controller("exportAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExportAction extends BaseAction {
    private static final long serialVersionUID = -5766222544407354394L;

    @Autowired
    private ExportService exportService;
    @Resource(name = "entityExportService")
    private EntityExportService entityExportService;
    @Resource(name = "messagePusher")
    private MessagePusher messagePusher;

    private static int HTTP_CLIENT_TIMEOUT = 180000;

    private Logger logger = LoggerFactory.getLogger(ExportAction.class);
    private String folderId;
    private String rootProperty;
    private String ip;
    private String mac;
    private String typeId;
    private String fileName;
    private String url;
    private String columnsStr;
    private String jconnectedId;
    private String operationId;

    /**
     * 打开导出导入页面
     * 
     * @return
     */
    public String showExportAndImport() {
        return SUCCESS;
    }

    /**
     * 生成待导出的EXCEL文件，并向前端返回文件名称
     * 
     * @return
     * @throws Exception
     */
    public String generateExportExcel() throws Exception {
        JSONObject resultObj = new JSONObject();
        // 生成excel文件并返回文件路径
        String fileName;
        try {
            fileName = exportService.entireExport();
            resultObj.put("fileName", fileName);
        } catch (Exception e) {
            resultObj.put("error", e.getMessage());
        }
        resultObj.write(response.getWriter());
        return NONE;
    }

    /**
     * 下载生成的EXCEL文件
     * 
     * @return
     * @throws IOException
     */
    public String downloadGenerateFile() throws IOException {
        exportService.downloadFile(fileName);
        return NONE;
    }

    /**
     * 打开导出设备页面
     * 
     * @return
     */
    public String showEntityExport() {
        return SUCCESS;
    }

    /**
     * 加载待导出的设备列表
     * 
     * @return
     * @throws IOException
     */
    public String loadEntity() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        // 组装查询条件
        if (folderId != null && !"".equals(folderId.trim())) {
            map.put("folderId", folderId);
        }
        if (ip != null && !"".equals(ip.trim())) {
            if (ip.indexOf("-") != -1) {
                // 是以-分隔开的ip段
                String[] ips = ip.split("-");
                map.put("startIp", ips[0]);
                map.put("endIp", ips[1]);
            } else {
                // 我们认为是一个ip地址
                map.put("ip", ip);
            }
        }
        if (mac != null && !"".equals(mac.trim())) {
            if (mac.indexOf("-") != -1) {
                // 我们认为是一个mac段
                String[] macs = mac.split("-");
                map.put("startMac", MacUtils.convertToMaohaoFormat(macs[0]));
                map.put("endMac", MacUtils.convertToMaohaoFormat(macs[1]));
            } else {
                // 我们认为是一个mac地址
                map.put("mac", mac);
            }
        }
        if (typeId != null && !"".equals(typeId.trim())) {
            map.put("typeId", typeId);
        }
        map.put("start", start);
        map.put("limit", limit);
        List<Entity> entityList = exportService.getExportEntity(map);
        Integer entityNum = exportService.getExportEntityNum(map);
        for (Entity entity : entityList) {
            try {
                StringBuilder ret = new StringBuilder();
                String[] folderNames = entity.getFolderName().split(",");
                for (int i = 0; i < folderNames.length; i++) {
                    ret.append(getResourceManager().getNotNullString(folderNames[i]));
                    if (i != folderNames.length - 1) {
                        ret.append(",");
                    }
                }
                entity.setFolderName(ret.toString());
                entity.setMac(MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule));
            } catch (Exception e) {

            }
        }
        JSONObject json = new JSONObject();
        json.put("data", entityList);
        json.put("rowCount", entityNum);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 导出设备到EXCEL
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String exportEntityToExcel() throws UnsupportedEncodingException {
        UserContext uc = (UserContext) super.getSession().get(UserContext.KEY);
        String macRule = uc.getMacDisplayStyle();
        // 查询条件
        // 组装查询条件
        Map<String, Object> map = new HashMap<String, Object>();
        if (folderId != null && !"".equals(folderId.trim())) {
            map.put("folderId", folderId);
        }
        if (ip != null && !"".equals(ip.trim())) {
            if (ip.indexOf("-") != -1) {
                // 是以-分隔开的ip段
                String[] ips = ip.split("-");
                map.put("startIp", ips[0]);
                map.put("endIp", ips[1]);
            } else {
                // 我们认为是一个ip地址
                map.put("ip", ip);
            }
        }
        if (mac != null && !"".equals(mac.trim())) {
            if (mac.indexOf("-") != -1) {
                // 我们认为是一个mac段
                String[] macs = mac.split("-");
                map.put("startMac", MacUtils.convertToMaohaoFormat(macs[0]));
                map.put("endMac", MacUtils.convertToMaohaoFormat(macs[1]));
            } else {
                // 我们认为是一个mac地址
                map.put("mac", mac);
            }
        }
        if (typeId != null && !"".equals(typeId.trim())) {
            map.put("typeId", typeId);
        }
        List<Entity> entityList = exportService.getExportEntity(map);
        for (Entity entity : entityList) {
            try {
                StringBuilder ret = new StringBuilder();
                String[] folderNames = entity.getFolderName().split(",");
                for (int i = 0; i < folderNames.length; i++) {
                    ret.append(getResourceManager().getNotNullString(folderNames[i]));
                    if (i != folderNames.length - 1) {
                        ret.append(",");
                    }
                }
                entity.setFolderName(ret.toString());
                entity.setMac(MacUtils.convertMacToDisplayFormat(entity.getMac(), macRule));
            } catch (Exception e) {

            }
        }
        entityExportService.exportEntityToExcel(entityList);
        return NONE;
    }

    public String exportGridToExcel() throws WriteException, Exception {
        ResourceManager rootResourceManager = ResourceManager
                .getResourceManager("com.topvision.platform.zetaframework.base.resources");

        // 通过url获取数据
        com.alibaba.fastjson.JSONArray data = getDataThroughUrl(url);

        // 生成excel文件，并返回文件名
        //sendMessage(false, rootResourceManager.getNotNullString("COMMON.beginExportExcel"));
        com.alibaba.fastjson.JSONArray columns = JSON.parseArray(columnsStr);
        String fileNameWithTimestamp = exportService.generateExcelFile(data, columns, fileName);
        //sendMessage(true, rootResourceManager.getNotNullString("COMMON.completeExportExcel"));

        // 返回文件名
        JSONObject json = new JSONObject();
        json.put("fileName", fileNameWithTimestamp);
        writeDataToAjax(json);
        return NONE;
    }

    public String downloadGridExcelFile() {
        File file = new File(fileName);
        FileInputStream fis = null;
        OutputStream out = null;
        try {
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            ServletActionContext.getResponse().setContentType("application/x-download");
            // 转码文件名，解决中文名的问题
            String fileName = file.getName();
            fileName = new String(fileName.getBytes(FtpClientUtil.GBK), FtpClientUtil.ISO);
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            int i;
            while ((i = fis.read(b)) > 0) {
                out.write(b, 0, i);
            }
        } catch (IOException e) {
        } finally {
            // 关闭流,并删除服务器端临时文件
            try {
                if (out != null)
                    out.close();
                if (fis != null)
                    fis.close();
                if (file.exists())
                    file.delete();
            } catch (IOException e) {
            }
        }
        return NONE;
    }

    /**
     * 发送导出excel进度消息
     * 
     * @param complete
     *            true/false，是否完成
     * @param text
     *            消息
     */
    private void sendMessage(Boolean complete, String text) {
        String seesionId = ServletActionContext.getRequest().getSession().getId();

        Message message = new Message(operationId);
        message.addSessionId(seesionId);
        message.setJconnectID(jconnectedId);
        JSONObject json = new JSONObject();
        json.put("complete", complete);
        json.put("text", text);
        message.setData(json);
        messagePusher.sendMessage(message);
    }

    /**
     * 通过url获取数据
     * 
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    private com.alibaba.fastjson.JSONArray getDataThroughUrl(String url) throws ClientProtocolException, IOException {
        com.alibaba.fastjson.JSONArray data = new com.alibaba.fastjson.JSONArray();

        // 创建httpClient，并赋予正确的cookie
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost(String.format("http://%s:3000%s", request.getServerName(), url));
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(HTTP_CLIENT_TIMEOUT).setConnectTimeout(HTTP_CLIENT_TIMEOUT).build();
        post.setConfig(requestConfig);
        Cookie[] cookies = request.getCookies();
        StringBuilder sb = new StringBuilder();
        for (Cookie cookie : cookies) {
            sb.append(";").append(cookie.getName()).append("=").append(cookie.getValue());
        }
        post.addHeader(new BasicHeader("Cookie", sb.substring(1)));

        // 执行请求，从返回中解析出数据
        CloseableHttpResponse httpResponse = httpclient.execute(post);
        try {
            HttpEntity entity = httpResponse.getEntity();
            if (entity == null) {
                return data;
            }
            long s = System.currentTimeMillis();
            String entityStr = convertStreamToString(entity.getContent());
            System.err.println("use time:" + (System.currentTimeMillis()-s));
            com.alibaba.fastjson.JSONObject content = JSON.parseObject(entityStr);
            data = content.getJSONArray(rootProperty);

            // 关闭请求连接
            EntityUtils.consume(entity);
            httpResponse.getEntity().getContent().close();
            httpResponse.close();
        } catch (Exception e) {
            httpResponse.getEntity().getContent().close();
            httpResponse.close();
        } finally {
            httpclient.close();
        }

        return data;
    }

    public String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        }
        return "";
    }

    private ResourceManager getResourceManager() {
        return ResourceManager.getResourceManager("com.topvision.ems.resources.resources");
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColumnsStr() {
        return columnsStr;
    }

    public void setColumnsStr(String columnsStr) {
        this.columnsStr = columnsStr;
    }

    public String getJconnectedId() {
        return jconnectedId;
    }

    public void setJconnectedId(String jconnectedId) {
        this.jconnectedId = jconnectedId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getRootProperty() {
        return rootProperty;
    }

    public void setRootProperty(String rootProperty) {
        this.rootProperty = rootProperty;
    }

}
