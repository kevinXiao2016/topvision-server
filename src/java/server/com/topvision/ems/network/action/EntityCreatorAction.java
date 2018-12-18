package com.topvision.ems.network.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.mp.SnmpConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.network.service.BatchDiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.NetworkConstants;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.exception.service.ExistEntityException;
import com.topvision.exception.service.ExistEntityInVirtualNetException;
import com.topvision.exception.service.OutOfLicenseException;
import com.topvision.framework.common.ExcelUtil;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.license.common.domain.Module;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.LicenseService;
import com.topvision.platform.service.SystemPreferencesService;

@Controller("entityCreatorAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityCreatorAction extends BaseAction {
    private static final long serialVersionUID = -1758926176449977196L;
    private static Logger logger = LoggerFactory.getLogger(EntityCreatorAction.class);
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private LicenseService licenseService;
    @Autowired
    private BatchDiscoveryService batchDiscoveryService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    private List<EntityType> entityTypes;
    private Entity entity;
    private long folderId;
    private int coordx;
    private int coordy;
    private long typeId;
    private long entityId;
    private SnmpParam snmpParam;
    private long virtualNetId;
    private File ipImportExcel;
    private String entityTypeString;
    private String batchIpString;
    private String dwrId;
    private String errorIpRows;
    private JSONArray entityTypesJson;
    private String snmpParamString;
    private String snmpVersionString;
    private Integer snmpVersion;
    private String readCommunity;
    private String writeCommunity;
    private String authProtocol;
    private String username;
    private String authPassword;
    private String privProtocol;
    private String privPassword;
    private byte retryCount;
    private Integer timeoutSeconds;
    private Integer pingTimeout;// ping超时时间，单位毫秒
    private JSONArray entityJson;
    private String snmpRetries;
    private String snmpTimeout;
    private String pingTimeoutConfig;
    private String snmpTabStr;

    /**
     * 显示新创建的设备信息.
     * 
     * @return
     */
    public String showNewEntity() {
        Long type = entityTypeService.getBaseType();
        entityTypes = entityTypeService.loadSubType(type);
        return SUCCESS;
    }

    /**
     * 显示批量新增设备
     * 
     * @return
     */
    public String showBatchNewEntity() {
        Properties snmpProperty = systemPreferencesService.getModulePreferences("Snmp");
        Properties pingProperty = systemPreferencesService.getModulePreferences("Ping");
        snmpRetries = snmpProperty.getProperty("Snmp.retries");
        snmpTimeout = snmpProperty.getProperty("Snmp.timeout");
        pingTimeoutConfig = pingProperty.getProperty("Ping.timeout");
        Long type = entityTypeService.getBaseType();
        entityTypes = entityTypeService.loadSubType(type);
        if (entityTypes.size() > 0) {
            entityTypesJson = JSONArray.fromObject(entityTypes);
        } else {
            entityTypesJson = JSONArray.fromObject(false);
        }
        return SUCCESS;
    }

    /**
     * 判断当前拓扑状态
     * 
     * @return
     * @throws Exception
     */
    public String checkBatchActionAvailable() throws Exception {
        if (batchDiscoveryService.getTopoFlag().get()) {
            writeDataToAjax("currentTopoBusy");
        } else {
            writeDataToAjax("currentTopoStart");
        }
        return NONE;
    }

    /**
     * 扫描多设备
     * 
     * @return
     * @throws Exception
     */
    public String scanEntity() throws Exception {
        JSONObject ret = new JSONObject();

        ResourceManager rm = ResourceManager.getResourceManager("com.topvision.ems.network.resources");

        List<String> ips = new ArrayList<String>();
        List<String> entityNames = new ArrayList<String>();
        List<String> topoNames = new ArrayList<String>();
        String[][] excelData = null;
        // 取出上传文件
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
                            // 只取单独的文件
                            try {

                                // excelData =
                                // ExcelUtil.getExcelDataForDiscovery(ExcelUtil.getExcelDataFor2007(file1).get(0));

                                excelData = ExcelUtil.getExcelDataForDiscovery(ExcelUtil.getExcelData(file1).get(0));
                                break;
                            } catch (Exception e) {
                                logger.error("", e);
                                ret.put("error", rm.getNotNullString("import.loadExcelFileError"));
                                ret.write(response.getWriter());
                                return NONE;
                            }
                        }
                    }
                }
            }
        }
        // 验证导入文件是否合法
        if (excelData == null) {
            ret.put("error", rm.getNotNullString("import.loadExcelFileError"));
            ret.write(response.getWriter());
            return NONE;
        }
        // 验证导入IP数量的合法性,整合到license
        if (excelData.length > 1000) {
            ret.put("error", rm.getNotNullString("import.tooManyEntity"));
            ret.write(response.getWriter());
            return NONE;
        }
        List<Integer> errorIpRow = new ArrayList<Integer>();
        for (int i = 1; i < excelData.length; i++) {
            if (excelData[i] != null && IpUtils.matches(excelData[i][0])) {
                ips.add(excelData[i][0]);
                entityNames.add(excelData[i][1]);
                topoNames.add(excelData[i][2]);
            } else {
                // 该行不是合法IP，进行记录
                errorIpRow.add(i - 1);
            }
        }
        // 如果没有合法行，则返回错误
        if (ips.size() == 0) {
            ret.put("error", rm.getNotNullString("import.noLegalIp"));
            ret.write(response.getWriter());
            return NONE;
        }
        ret.put("success", true);
        if (errorIpRow.size() > 0) {
            ret.put("errorIpRow", errorIpRow);
        }
        ret.write(response.getWriter());

        // 整理SNMP参数
        List<SnmpParam> snmpParams = new ArrayList<SnmpParam>();
        SnmpParam snmpParam;
        JSONArray snmpTabs = JSONArray.fromObject(snmpTabStr);
        JSONObject snmpTab;
        for (int i = 0, len = snmpTabs.size(); i < len; i++) {
            snmpParam = new SnmpParam();
            snmpTab = snmpTabs.getJSONObject(i);
            if (SnmpConstants.version3 == snmpTab.getInt("version")) {
                // 设置snmpV3配置下的username、authProtocol、authPass、privProtocl、privPass
                snmpParam.setUsername(snmpTab.getString("userName"));
                snmpParam.setAuthProtocol(snmpTab.getString("authProtocol"));
                snmpParam.setAuthPassword(snmpTab.getString("authPassword"));
                snmpParam.setPrivProtocol(snmpTab.getString("privProtocol"));
                snmpParam.setPrivPassword(snmpTab.getString("privPassword"));
            } else if (SnmpConstants.version2c == snmpTab.getInt("version")) {
                // 设置读写共同体名
                snmpParam.setCommunity(snmpTab.getString("readCommunity"));
                snmpParam.setWriteCommunity(snmpTab.getString("writeCommunity"));
            }
            snmpParam.setTimeout(timeoutSeconds);
            snmpParam.setRetry(retryCount);
            snmpParams.add(snmpParam);
        }
        // 获得选择的设备类型OID
        List<String> types = Arrays.asList(entityTypeString.split(","));
        String jconnectID = request.getParameter("jconnectID");

        batchDiscoveryService.batchDiscovery(new ArrayList<Long>(), dwrId, jconnectID, ips, entityNames, topoNames,
                snmpParams, types, (UserContext) getSession().get(UserContext.KEY));

        return NONE;
    }

    /**
     * 跳转拓扑结果展示界面
     * 
     * @return
     */
    public String showScanEntityResult() {
        return SUCCESS;
    }

    public String showUserEntity() {
        entityJson = JSONArray.fromObject(entityService.getEntityJson());
        return SUCCESS;
    }

    /**
     * 更新设备SNMP参数
     * 
     */
    public String updateEntitySnmpInfo() {
        SnmpParam param = entityService.getSnmpParamByEntity(entityId);
        param.setEntityId(entityId);
        param.setVersion(snmpVersion);
        // V2C模式下更新共同体
        if (snmpVersion == 1) {
            param.setCommunity(readCommunity);
            param.setWriteCommunity(writeCommunity);
        }
        // V3模式下更新相关的参数
        if (snmpVersion == 3) {
            // param.setSecurityLevel(securityLevel);
            param.setAuthProtocol(authProtocol);
            param.setUsername(username);
            param.setAuthPassword(authPassword);
            param.setPrivProtocol(privProtocol);
            param.setPrivPassword(privPassword);
        }
        entityService.updateSnmpParam(param);
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

    public String stopTopo() {
        batchDiscoveryService.shutDownBatchDiscovery();
        return NONE;
    }

    /**
     * 显示新建设备页面.
     * 
     * @return
     */
    public String addNewEntity() {
        Long baseType = entityTypeService.getBaseType();
        entityTypes = entityTypeService.loadSubType(baseType);
        return SUCCESS;
    }

    /**
     * 设备数量是否超限
     * 
     * @return
     */
    public String entityOutofLimit() {
        JSONObject jObj = new JSONObject();
        try {
            int count = entityService.loadEntitySnapCount(new HashMap<String, Object>());
            Module lm = licenseService.getLicenseModule(entityTypeService.getOltType());
            if (count >= Integer.valueOf(lm.getNumberOfEntities())) {
                jObj.put("outofLimit", true);
            } else {
                jObj.put("outofLimit", false);
            }
            jObj.put("success", true);
            jObj.put("msg", "");
        } catch (Exception e) {
            LOG.error("", e);
            jObj.put("success", false);
            jObj.put("msg", e.getMessage());
        }
        wirteAjaxData(jObj.toString());
        return NONE;
    }

    /**
     * 创建设备.
     * 
     * @return
     * @throws Exception
     */
    public String newEntity() throws Exception {
        JSONObject json = new JSONObject();
        try {
            EntityType type = entityService.topoEntityTypeId(entity, snmpParam);
            if (type != null) {
                entity.setCorpId(entityTypeService.getCorpBySysObjId(type.getSysObjectID()));
                entity.setTypeId(type.getTypeId());
                entity.setIcon48(type.getIcon48());
                json.put("type", entityTypeService.getEntityNetworkGroupIdByEntityTypeId(type.getTypeId()));
            }
            entity.setFolderId(folderId);
            entity.setParam(snmpParam);
            // 设置其默认为可管理的。
            entity.setStatus(Boolean.TRUE);
            // Add by Rod On Authority
            UserContext uc = (UserContext) getSession().get(UserContext.KEY);
            entity.setAuthorityUserId(uc.getUserId());
            entityService.txCreateEntity(entity);
            /*
             * if (!uc.getUser().getUserName().equals(CurrentRequest.ADMIN_USER)) { //TopoFolder
             * topoFolder = topologyService.getDisplayNameType(folderId); Long userTopoFolderId =
             * uc.getUser().getUserGroupId(); topologyService.txMoveEntity(userTopoFolderId,
             * Arrays.asList(entity.getEntityId())); }
             */
            // 设备添加后发送消息
            entityService.txCreateMessage(entity);
            json.put("id", entity.getEntityId());
            json.put("nodeId", entity.getEntityId());
            json.put("x", entity.getX());
            json.put("y", entity.getY());
            json.put("icon", entity.getIcon());
            json.put("text", entity.getDisplayName());
            json.put("objType", NetworkConstants.TYPE_ENTITY);
            json.put("userObjId", entity.getEntityId());
            json.put("ip", entity.getIp());
            json.put("name", entity.getShowName());
            json.put("sysName", entity.getShowSysName());
            json.put("alert", Level.CLEAR_LEVEL);
            json.put("state", false);
            json.put("snmpSupport", false);
            json.put("agentSupport", false);
            // json.put("showType", topoFolder.getShowType());
            json.put("exist", false);
        } catch (ExistEntityException eee) {
            logger.debug("New Entity.", eee);
            json.put("exist", true);
        } catch (ExistEntityInVirtualNetException eevne) {
            logger.debug("Entity exists in VirtualNet.", eevne);
            json.put("exist", true);
            json.put("virtualNetName", eevne.getMessage());
        } catch (OutOfLicenseException oole) {
            logger.debug("New Entity.", oole);
            json.put("licenseLimit", true);
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    /**
     * 通过类型ID创建设备.
     * 
     * @return
     * @throws Exception
     */
    public String createEntityByTypeId() throws Exception {
        JSONObject json = new JSONObject();
        try {
            EntityType type = entityTypeService.getEntityType(typeId);
            EntityType category = entityTypeService.getEntityType(entityTypeService
                    .getEntityNetworkGroupIdByEntityTypeId(type.getTypeId()));
            Entity entity = new Entity();
            entity.setIp("0.0.0.0");
            entity.setX(coordx);
            entity.setY(coordy);
            entity.setTypeId(typeId);
            entity.setCorpId(entityTypeService.getCorpBySysObjId(type.getSysObjectID()));
            entity.setName(type.getDisplayName());
            entity.setFolderId(folderId);
            entity.setIcon48(category.getIcon48());
            entityService.txCreateEntity(entity);

            json.put("entityId", entity.getEntityId());
            json.put("ip", entity.getIp());
            json.put("name", entity.getName());
            json.put("icon", entity.getIcon());
            json.put("x", entity.getX());
            json.put("y", entity.getY());
            json.put("alert", Level.CLEAR_LEVEL);
            json.put("state", false);
            json.put("type", category.getTypeId());
            json.put("snmpSupport", false);
            json.put("agentSupport", false);
            json.put("exist", false);
        } catch (ExistEntityException eee) {
            logger.error("Create Entity By type Id.", eee);
            json.put("exist", true);
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    public String pushNewEntityFromVirtual() {
        return SUCCESS;
    }

    private static void wirteAjaxData(String data) {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/text");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().print(data);
            response.getWriter().flush();
        } catch (IOException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("", e);
            }
        }
    }

    /**
     * 批量导入模板下载
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String downLoadBatchFileTemplate() throws UnsupportedEncodingException {
        String tmpFile = "batchImportTemplate.xlsx";
        StringBuilder fileName = new StringBuilder(SystemConstants.ROOT_REAL_PATH);
        fileName.append("META-INF/");
        fileName.append(tmpFile);
        int i;
        byte[] b = new byte[1024];
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/x-download");
        ServletActionContext.getResponse().addHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(tmpFile, "UTF-8"));
        try {
            File rFile = new File(fileName.toString());
            if (rFile != null) {
                fis = new FileInputStream(rFile);
                out = ServletActionContext.getResponse().getOutputStream();
                while ((i = fis.read(b)) > 0) {
                    out.write(b, 0, i);
                }
            }
        } catch (Exception e) {
            logger.debug("downLoadBatchFileTemplate is error:{}", e);
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
                logger.debug("downLoadBatchFileTemplate is error:{}", e);
            }
        }
        return NONE;
    }

    public int getCoordx() {
        return coordx;
    }

    public int getCoordy() {
        return coordy;
    }

    public Entity getEntity() {
        return entity;
    }

    public long getEntityId() {
        return entityId;
    }

    public List<EntityType> getEntityTypes() {
        return entityTypes;
    }

    public long getFolderId() {
        return folderId;
    }

    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setCoordx(int coordx) {
        this.coordx = coordx;
    }

    public void setCoordy(int coordy) {
        this.coordy = coordy;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public void setEntityTypes(List<EntityType> entityTypes) {
        this.entityTypes = entityTypes;
    }

    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public long getVirtualNetId() {
        return virtualNetId;
    }

    public void setVirtualNetId(long virtualNetId) {
        this.virtualNetId = virtualNetId;
    }

    public void setLicenseService(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    public void setBatchDiscoveryService(BatchDiscoveryService batchDiscoveryService) {
        this.batchDiscoveryService = batchDiscoveryService;
    }

    public File getIpImportExcel() {
        return ipImportExcel;
    }

    public void setIpImportExcel(File ipImportExcel) {
        this.ipImportExcel = ipImportExcel;
    }

    public String getBatchIpString() {
        return batchIpString;
    }

    public void setBatchIpString(String batchIpString) {
        this.batchIpString = batchIpString;
    }

    public String getEntityTypeString() {
        return entityTypeString;
    }

    public void setEntityTypeString(String entityTypeString) {
        this.entityTypeString = entityTypeString;
    }

    public byte getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(byte retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(Integer timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public String getDwrId() {
        return dwrId;
    }

    public void setDwrId(String dwrId) {
        this.dwrId = dwrId;
    }

    public String getSnmpParamString() {
        return snmpParamString;
    }

    public void setSnmpParamString(String snmpParamString) {
        this.snmpParamString = snmpParamString;
    }

    public JSONArray getEntityTypesJson() {
        return entityTypesJson;
    }

    public void setEntityTypesJson(JSONArray entityTypesJson) {
        this.entityTypesJson = entityTypesJson;
    }

    public Integer getPingTimeout() {
        return pingTimeout;
    }

    public void setPingTimeout(Integer pingTimeout) {
        this.pingTimeout = pingTimeout;
    }

    public String getSnmpVersionString() {
        return snmpVersionString;
    }

    public void setSnmpVersionString(String snmpVersionString) {
        this.snmpVersionString = snmpVersionString;
    }

    public JSONArray getEntityJson() {
        return entityJson;
    }

    public void setEntityJson(JSONArray entityJson) {
        this.entityJson = entityJson;
    }

    public Integer getSnmpVersion() {
        return snmpVersion;
    }

    public void setSnmpVersion(Integer snmpVersion) {
        this.snmpVersion = snmpVersion;
    }

    public String getReadCommunity() {
        return readCommunity;
    }

    public void setReadCommunity(String readCommunity) {
        this.readCommunity = readCommunity;
    }

    public String getWriteCommunity() {
        return writeCommunity;
    }

    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }

    public String getAuthProtocol() {
        return authProtocol;
    }

    public void setAuthProtocol(String authProtocol) {
        this.authProtocol = authProtocol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    public String getPrivProtocol() {
        return privProtocol;
    }

    public void setPrivProtocol(String privProtocol) {
        this.privProtocol = privProtocol;
    }

    public String getPrivPassword() {
        return privPassword;
    }

    public void setPrivPassword(String privPassword) {
        this.privPassword = privPassword;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public String getSnmpRetries() {
        return snmpRetries;
    }

    public void setSnmpRetries(String snmpRetries) {
        this.snmpRetries = snmpRetries;
    }

    public String getSnmpTimeout() {
        return snmpTimeout;
    }

    public void setSnmpTimeout(String snmpTimeout) {
        this.snmpTimeout = snmpTimeout;
    }

    public String getPingTimeoutConfig() {
        return pingTimeoutConfig;
    }

    public void setPingTimeoutConfig(String pingTimeoutConfig) {
        this.pingTimeoutConfig = pingTimeoutConfig;
    }

    public String getSnmpTabStr() {
        return snmpTabStr;
    }

    public void setSnmpTabStr(String snmpTabStr) {
        this.snmpTabStr = snmpTabStr;
    }

    public String getErrorIpRows() {
        return errorIpRows;
    }

    public void setErrorIpRows(String errorIpRows) {
        this.errorIpRows = errorIpRows;
    }

}