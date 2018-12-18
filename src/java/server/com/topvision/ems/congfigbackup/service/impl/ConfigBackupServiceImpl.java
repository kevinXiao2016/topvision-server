package com.topvision.ems.congfigbackup.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.congfigbackup.dao.ConfigAndBackupDao;
import com.topvision.ems.congfigbackup.service.ConfigAndBackupRecord;
import com.topvision.ems.congfigbackup.service.ConfigBackupService;
import com.topvision.ems.congfigbackup.service.ProductionConfigBackupService;
import com.topvision.ems.congfigbackup.util.ConfigBackupUtil;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.util.CurrentRequest;
import com.topvision.platform.util.StringUtil;

@Service("configBackupService")
public class ConfigBackupServiceImpl extends BaseService implements ConfigBackupService, EntityListener {
    private static final String STRING_TEMPLATE_DICT = "{0}META-INF{1}startConfig{1}{2}";
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    protected BeanFactory beanFactory;
    @Autowired
    private ConfigAndBackupDao configAndBackupDao;

    @Override
    public void downloadConfigFile(Long entityId, Long entityType, String ip) throws Exception {
        ConfigAndBackupRecord $record = getConfigAndBackupRecord(entityId, ConfigAndBackupRecord.BACKUP_CONFIG);
        try {
            EntityType $entityType = entityTypeService.getEntityType(entityType);
            String module = $entityType.getModule();
            ProductionConfigBackupService productionService = beanFactory.getBean(module + "ConfigBackupService",
                    ProductionConfigBackupService.class);
            productionService.execDownloadAndBackup(entityType, entityId, ip);
            $record.setResult(ConfigAndBackupRecord.SUCCESS);
        } catch (Exception e) {
            $record.setResult(e);
            recordOperation($record);
            throw e;
        }
        recordOperation($record);
    }

    @Override
    public void saveConfigFile(Long entityId, Long entityType, String ip) throws Exception {
        ConfigAndBackupRecord $record = getConfigAndBackupRecord(entityId, ConfigAndBackupRecord.SAVE_CONFIG);
        try {
            EntityType $entityType = entityTypeService.getEntityType(entityType);
            String module = $entityType.getModule();
            ProductionConfigBackupService productionService = beanFactory.getBean(module + "ConfigBackupService",
                    ProductionConfigBackupService.class);
            productionService.saveConfigFile(ip, entityType, entityId);
            $record.setResult(ConfigAndBackupRecord.SUCCESS);
        } catch (Exception e) {
            $record.setResult(e);
            throw e;
        } finally {
            recordOperation($record);
        }
    }

    private ConfigAndBackupRecord getConfigAndBackupRecord(Long entityId, int backupType) {
        ConfigAndBackupRecord $record = new ConfigAndBackupRecord();
        HttpServletRequest request = null;
        try {
            request = ServletActionContext.getRequest();
            $record.setClientIp(request.getRemoteHost());
            $record.setUserId(CurrentRequest.getCurrentUser().getUserId());
        } catch (Exception e) {
            $record.setUserId(-1L);
            $record.setClientIp("127.0.0.1");
        }
        $record.setOperationType(backupType);
        $record.setEntityId(entityId);
        return $record;
    }

    @Override
    public List<Entity> getEntitList() {
        return entityService.getCentralEntity();
    }

    @Override
    public void applyConfigToDevice(String filePath, Long entityType, String ip) throws Exception {
        Long entityId = entityService.getEntityIdByIp(ip);
        ConfigAndBackupRecord $record = getConfigAndBackupRecord(entityId, ConfigAndBackupRecord.APPLY_CONFIG);
        // 提取源文件名并获取源文件
        String dict = StringUtil.format(STRING_TEMPLATE_DICT, SystemConstants.ROOT_REAL_PATH, File.separator, filePath);
        try {
            EntityType $entityType = entityTypeService.getEntityType(entityType);
            String module = $entityType.getModule();
            ProductionConfigBackupService productionService = beanFactory.getBean(module + "ConfigBackupService",
                    ProductionConfigBackupService.class);
            productionService.applyConfigToDevice(entityId, entityType, ip, dict);
            $record.setResult(ConfigAndBackupRecord.SUCCESS);
        } catch (Exception e) {
            $record.setResult(e);
            throw e;
        } finally {
            recordOperation($record);
        }
    }

    @Override
    public void recordOperation(ConfigAndBackupRecord record) {
        configAndBackupDao.recordOperation(record);
    }

    @Override
    public List<ConfigAndBackupRecord> selectOperationRecords(Map<String, Object> map) {
        return configAndBackupDao.selectOperationRecords(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.message.event.EntityListener#entityRemoved(com.topvision.platform.
     * message.event.EntityEvent)
     */
    @Override
    public void entityRemoved(EntityEvent event) {
        Entity entity = event.getEntity();
        if (entity != null && entityTypeService.isOlt(entity.getTypeId())) {
            Long entityId = event.getEntity().getEntityId();
            String dict = StringUtil.format(STRING_TEMPLATE_DICT, SystemConstants.ROOT_REAL_PATH, File.separator,
                    entityId);
            ConfigBackupUtil.deleteDirectory(new File(dict));
        }
    }

    @Override
    public void entityAdded(EntityEvent event) {
    }

    @Override
    public void entityDiscovered(EntityEvent event) {
    }

    @Override
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
    }

    @Override
    public void entityChanged(EntityEvent event) {
    }

    @Override
    public void managerChanged(EntityEvent event) {
    }

    @Override
    public void entityReplaced(EntityEvent event) {
    }

}
