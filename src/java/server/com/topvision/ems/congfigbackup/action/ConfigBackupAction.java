package com.topvision.ems.congfigbackup.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.congfigbackup.exception.DeleteFileOrDirectoryFaildException;
import com.topvision.ems.congfigbackup.service.ConfigAndBackupRecord;
import com.topvision.ems.congfigbackup.service.ConfigBackupService;
import com.topvision.ems.congfigbackup.service.FileAutoSaveService;
import com.topvision.ems.congfigbackup.util.ConfigBackupUtil;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.util.StringUtil;

@Controller("configBackupAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConfigBackupAction extends BaseAction {
    private static final long serialVersionUID = 2412523417594750585L;
    private final Logger logger = LoggerFactory.getLogger(ConfigBackupAction.class);
    private static final String STRING_TEMPLATE_DICT = "{0}META-INF{1}startConfig{1}{2}";
    private Long entityId;
    private Long entityType;
    private String ip;
    private String filePate;
    private String autoWrite;
    private String saveBeforeWrite;
    // 格式为 00:00_1,2,3,4,5,6,7
    private String autoWriteTime;
    private String filePath;
    private Integer operationResult;
    private int operationType;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private ConfigBackupService configBackupService;
    @Autowired
    private FileAutoSaveService fileAutoSaveService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;

    /**
     * 显示备份配置页面
     * @return
     * @throws IOException
     */
    public String showConfigBackupView() throws IOException {
        return SUCCESS;
    }

    public String loadConfigBackupEntity() throws IOException {
        return NONE;
    }

    /**
     * 保存配置文件
     * @return
     * @throws Exception
     */
    public String saveConfigFile() throws Exception {
        JSONObject json = new JSONObject();
        Entity entity = entityService.getEntity(entityId);
        try {
            configBackupService.saveConfigFile(entityId, entity.getTypeId(), entity.getIp());
            json.put("success", true);
        } catch (Exception e) {
            logger.info("saveConfigFile error {}", e.getMessage());
            json.put("success", false);
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 备份配置文件,从设备上下载配置文件到本地
     * @return
     * @throws Exception
     */
    public String downConfigFile() throws Exception {
        configBackupService.downloadConfigFile(entityId, entityType, ip);
        return NONE;
    }

    /**
     * 显示自动备份配置页面
     * @return
     */
    public String showAutoWriteConfig() {
        Properties properties = systemPreferencesService.getModulePreferences("platform");
        autoWrite = properties.getProperty("file.autoWrite");
        saveBeforeWrite = properties.getProperty("file.saveBeforeWrite");
        autoWriteTime = properties.getProperty("file.autoWriteTime");
        return SUCCESS;
    }

    /**
     * 保存自动备份配置
     * @return
     * @throws SchedulerException
     * @throws ParseException
     */
    public String saveAutoWriteConfig() throws SchedulerException, ParseException {
        List<SystemPreferences> preferencesList = new ArrayList<SystemPreferences>();
        // save file.autoWrite
        SystemPreferences filePreference = new SystemPreferences();
        filePreference.setName("file.autoWrite");
        filePreference.setValue(autoWrite);
        filePreference.setModule("platform");
        preferencesList.add(filePreference);
        // save file.saveBeforeWrite
        filePreference = new SystemPreferences();
        filePreference.setName("file.saveBeforeWrite");
        filePreference.setValue(saveBeforeWrite);
        filePreference.setModule("platform");
        preferencesList.add(filePreference);
        // save file.autoWriteTime
        filePreference = new SystemPreferences();
        filePreference.setName("file.autoWriteTime");
        filePreference.setValue(autoWriteTime);
        filePreference.setModule("platform");
        preferencesList.add(filePreference);
        fileAutoSaveService.reConfigureTrigger(autoWriteTime);
        //save to database
        systemPreferencesService.savePreferences(preferencesList);
        return NONE;
    }

    /**
     * 加载配置文件内容
     * @return
     * @throws IOException
     */
    public String loadFileText() throws IOException {
        JSONObject json = new JSONObject();
        String path = StringUtil.format(STRING_TEMPLATE_DICT, SystemConstants.ROOT_REAL_PATH, File.separator, filePath);
        FileReader reader = new FileReader(path);
        BufferedReader bufferedReader = null;
        try {
            String line;
            bufferedReader = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "<br>");
                sb.append("\n");
            }
            json.put("text", sb.toString());
            writeDataToAjax(json);
        } catch (IOException e) {
            logger.info("", e);
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return NONE;
    }

    /**
     * 加载配置文件树结构
     * @return
     * @throws IOException
     */
    public String loadStartConfigList() throws IOException {
        JSONArray json = new JSONArray();
        String path = StringUtil.format(STRING_TEMPLATE_DICT, SystemConstants.ROOT_REAL_PATH, File.separator, filePath);
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            return NONE;
        }
        File[] files = file.listFiles();
        for (File $file : files) {
            String $name = $file.getName();
            JSONObject node = buildNode($name, path);
            if ($file.isDirectory()) {
                node.put("leaf", false);
                node.put("dateDirectory", true);
                json.add(node);
            } else if ($file.isFile()) {
                node.put("leaf", true);
                node.put("icon", "/images/arrange.gif");
                json.add(0, node);
            }
        }
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 构建文件树结点
     * @param leaf
     * @param path
     * @return
     */
    private JSONObject buildNode(String leaf, String path) {
        File readF = new File(path + File.separatorChar + leaf);
        JSONObject node = new JSONObject();
        node.put("text", readF.getName());
        return node;
    }

    /**
     * 展示导入的文件
     * 
     * @return
     */
    public String showImportFile() {
        return SUCCESS;
    }

    /**
     * 删除一个配置文件（夹）
     * 
     * @return
     * @throws Exception
     */
    public String deleteFile() throws Exception {
        filePath = StringUtil.format(STRING_TEMPLATE_DICT, SystemConstants.ROOT_REAL_PATH, File.separator, filePath);
        File file = new File(filePath);
        if (!ConfigBackupUtil.deleteDirectory(file)) {
            throw new DeleteFileOrDirectoryFaildException();
        }
        return NONE;
    }

    public String showConfigFileUsedList() {
        return SUCCESS;
    }

    /**
     * 查询所有人的操作记录
     * 
     * @return
     * @throws IOException
     */
    public String queryHistroyRecords() throws IOException {
        JSONObject json = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();
        if (entityId != -1) {
            map.put("entityId", entityId);
        }
        if (operationType != -1) {
            map.put("operationType", operationType);
        }
        List<ConfigAndBackupRecord> list = configBackupService.selectOperationRecords(map);
        json.put("data", list);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 获取设备列表，展示为树
     * 
     * @return
     * @throws IOException
     */
    public String getAllDeviceList() throws IOException {
        JSONArray treeJson = new JSONArray();
        //获取OLT设备和独立的CMC设备
        List<Entity> oltEntities = entityService.getEntityListByType(entityTypeService.getOltType());
        List<Entity> cmcWithIps = entityService.getEntityListByType(entityTypeService.getCcmtswithagentType());
        //获取系统展示设置
        UserContext uc = (UserContext) getSession().get(UserContext.KEY);
        String displayField = uc.getPreference("user.displayField");
        String displayRule = uc.getMacDisplayStyle();
        //将OLT设备数据处理为树节点数据
        JSONObject oltJson = new JSONObject();
        JSONArray oltArray = this.handleDeviceNode(oltEntities, displayField, displayRule);
        oltJson.put("text", "OLT");
        oltJson.put("expanded", true);
        oltJson.put("leaf", false);
        oltJson.put("disabled", true);
        oltJson.put("children", oltArray);
        treeJson.add(oltJson);
        //将独立型的CMC数据处理为树节点数据
        JSONObject cmcJson = new JSONObject();
        JSONArray cmcArray = this.handleDeviceNode(cmcWithIps, displayField, displayRule);
        cmcJson.put("text", "CMTS");
        cmcJson.put("expanded", true);
        cmcJson.put("leaf", false);
        cmcJson.put("disabled", true);
        cmcJson.put("children", cmcArray);
        treeJson.add(cmcJson);
        treeJson.write(response.getWriter());
        return NONE;
    }

    /**
     * 处理设备树结点
     * @param entityList
     * @param displayField
     * @param displayRule
     * @return
     */
    private JSONArray handleDeviceNode(List<Entity> entityList, String displayField, String displayRule) {
        JSONArray oltArray = new JSONArray();
        for (Entity entity : entityList) {
            JSONObject o = new JSONObject();
            if (Entity.IP_DISPLAY_FIELD.equals(displayField)) {
                o.put("text", entity.getIp());
            } else if (Entity.MAC_DISPLAY_FIELD.equals(displayField)) {
                //add by fanzidong,展示之前格式化MAC地址
                String formatedMac = MacUtils.convertMacToDisplayFormat(entity.getMac(), displayRule);
                o.put("text", formatedMac);
            } else if (Entity.SYS_NAME_DISPLAY_FIELD.equals(displayField)) {
                o.put("text", entity.getSysName());
            } else {
                o.put("text", entity.getName());
            }
            o.put("entityId", entity.getEntityId());
            o.put("typeId", entity.getTypeId());
            o.put("ip", entity.getIp());
            o.put("leaf", false);
            //标识设备节点,以防在删除文件的时候将该节点删除
            o.put("isDeviceNode", true);
            oltArray.add(o);
        }
        return oltArray;
    }

    /**
     * 导出配置文件
     * 
     * @throws IOException
     */
    public void downFile() throws IOException {
        int i;
        byte[] b = new byte[1024];
        OutputStream out = null;
        FileInputStream fis = null;
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=startConfig.cfg");
        filePath = StringUtil.format(STRING_TEMPLATE_DICT, SystemConstants.ROOT_REAL_PATH, File.separator, filePath);
        File file = new File(filePath);
        try {
            fis = new FileInputStream(file);
            out = response.getOutputStream();
            while ((i = fis.read(b)) > 0) {
                out.write(b, 0, i);
            }
        } catch (Exception e) {
            logger.info("downFile method is error:{}", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                logger.info("downFile method is error:{}", e);
            }
        }
    }

    /**
     * 配置文件应用到该设备
     * 
     * @return
     * @throws IOException
     */
    public String applyConfigToDevice() throws Exception {
        configBackupService.applyConfigToDevice(filePath, entityType, ip);
        return NONE;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getEntityType() {
        return entityType;
    }

    public void setEntityType(Long entityType) {
        this.entityType = entityType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getFilePate() {
        return filePate;
    }

    public void setFilePate(String filePate) {
        this.filePate = filePate;
    }

    /**
     * 查询所有人的操作记录
     * 
     * @return
     * @throws IOException
     */
    /* public String queryHistroyRecords() throws IOException {
         JSONObject json = new JSONObject();
         List<BatchConfigOperationLogRecord> list = oltConfigFileService.queryHistroyRecords();
         json.put("data", list);
         json.write(response.getWriter());
         return NONE;
     }*/

    public Integer getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Integer operationResult) {
        this.operationResult = operationResult;
    }

    public String getAutoWrite() {
        return autoWrite;
    }

    public void setAutoWrite(String autoWrite) {
        this.autoWrite = autoWrite;
    }

    public String getSaveBeforeWrite() {
        return saveBeforeWrite;
    }

    public void setSaveBeforeWrite(String saveBeforeWrite) {
        this.saveBeforeWrite = saveBeforeWrite;
    }

    public String getAutoWriteTime() {
        return autoWriteTime;
    }

    public void setAutoWriteTime(String autoWriteTime) {
        this.autoWriteTime = autoWriteTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

}
