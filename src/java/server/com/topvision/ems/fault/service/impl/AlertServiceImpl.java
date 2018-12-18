package com.topvision.ems.fault.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.dao.AlertDao;
import com.topvision.ems.fault.dao.AlertTypeDao;
import com.topvision.ems.fault.dao.HistoryAlertDao;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertAboutUsers;
import com.topvision.ems.fault.domain.AlertStat;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.fault.domain.LevelStat;
import com.topvision.ems.fault.message.AlertEvent;
import com.topvision.ems.fault.message.AlertListener;
import com.topvision.ems.fault.service.ActionCenter;
import com.topvision.ems.fault.service.AlertConfirmConfigService;
import com.topvision.ems.fault.service.AlertFilterService;
import com.topvision.ems.fault.service.AlertService;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.TopoFolderEx;
import com.topvision.ems.performance.service.CachedThresholdAlertService;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.SystemPreferencesService;
import com.topvision.platform.util.CurrentRequest;
import com.topvision.platform.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("alertService")
public class AlertServiceImpl extends BaseService implements AlertService {
    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);
    @Autowired
    private AlertDao alertDao;
    @Autowired
    private AlertTypeDao alertTypeDao;
    @Autowired
    private HistoryAlertDao historyAlertDao;
    @Autowired
    private AlertFilterService alertFilterService;
    @Autowired
    private MessageService messageService;
    private List<AlertType> alertTypes = null;
    private JSONArray jsonAlertType = null;
    private final Map<Integer, AlertType> alertTypeMapping = new HashMap<Integer, AlertType>();
    @Autowired
    private ActionCenter actionCenter;
    @Autowired
    private AlertConfirmConfigService alertConfirmConfigService;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private CachedThresholdAlertService cachedThresholdAlertService;
    @Autowired
    private SystemPreferencesService systemPreferencesService;

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        reset();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#reset()
     */
    @Override
    public void reset() {
        jsonAlertType = null;
        try {
            alertTypes = alertTypeDao.getAllAlertType();
            for (int i = 0; i < alertTypes.size(); i++) {
                AlertType type = alertTypes.get(i);
                alertTypeMapping.put(type.getTypeId(), type);
            }

            // add by fanzidong on 2017.12.12
            // 增加告警类型国际化入库存储
            checkAlertTypeLocaleNameState();
        } catch (DataAccessException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Initialize alert type.", ex.getMessage());
            }
        }
    }

    private void checkAlertTypeLocaleNameState() {
        // 获取该任务是否执行过的标记
        SystemPreferences systemPreferences = systemPreferencesService.getSystemPreference("AlertType.localeNameInit");

        Boolean inited = false;
        if (systemPreferences != null) {
            inited = !"0".equals(systemPreferences.getValue());
        }

        if (!inited) {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    storeAlertTypeLocaleName();
                }
            });
            thread.start();
        }
    }

    private void storeAlertTypeLocaleName() {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");

        List<Integer> toBeLocaledList = new ArrayList<Integer>();

        for (AlertType alertType : alertTypes) {
            if (StringUtil.isEmpty(alertType.getLocalName())) {
                toBeLocaledList.add(alertType.getTypeId());
            }
        }

        while (toBeLocaledList.size() > 0) {
            Iterator<Integer> it = toBeLocaledList.iterator();
            while (it.hasNext()) {
                Integer typeId = it.next();
                AlertType alertType = alertTypeMapping.get(typeId);
                // 获取对应的localName
                try {
                    String localeName = resourceManager.getNotNullString(alertType.getDisplayName());
                    // 更新数据库
                    alertTypeDao.updateLocaleName(alertType.getTypeId(), localeName);
                    it.remove();
                } catch (Exception e) {

                }
            }

            if (toBeLocaledList.size() > 0) {
                try {
                    Thread.sleep(60000L);
                } catch (InterruptedException e) {
                    logger.error("", e);
                }
            }
        }

        // 到此处告警类型本地化名称完成
        SystemPreferences systemPreferences = systemPreferencesService.getSystemPreference("AlertType.localeNameInit");
        systemPreferences.setValue("1");
        systemPreferencesService.savePreferences(systemPreferences);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#fireAlert(com.topvision.
     * ems.fault.domain.Alert, java.lang.Boolean)
     */
    @Override
    public void fireAlert(Alert alert, Boolean immediately) {
        AlertEvent event = new AlertEvent(alert);
        event.setAlert(alert);
        event.setListener(AlertListener.class);
        if (alert.getLevelId() == 0) {
            event.setActionName("alertCleared");
        } else {
            event.setActionName("alertAdded");
        }
        if (immediately) {
            messageService.fireMessage(event);
        } else {
            messageService.addMessage(event);
        }
    }

    /**
     * 实时通知设备的最新告警状态.
     *
     * @param entityId
     * @param alert
     * @param desc
     */
    public void fireEntityAlert(Long entityId, int alertLevel, String desc, Long alertId, Timestamp lastTime) {
        EntityValueEvent event = new EntityValueEvent(entityId);
        event.setEntityId(entityId);
        event.setMaxAlertLevel(alertLevel);
        event.setMaxAlertDesc(desc);
        event.setAlertId(alertId);
        event.setMaxAlertTime(lastTime);
        event.setActionName("alertChanged");
        event.setListener(EntityValueListener.class);
        messageService.addMessage(event);
    }

    public ActionCenter getActionCenter() {
        return actionCenter;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#getActionsOfAlertType(java .lang.Integer)
     */
    @Override
    public List<Long> getActionsOfAlertType(Integer id) {
        return alertTypeDao.getActionsOfAlertType(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#getAlertById(java.lang.Long)
     */
    @Override
    public Alert getAlertById(Long alertId) {
        return alertDao.getAlertByAlertId(alertId);
    }

    /**
     * @return the alertDao
     */
    public AlertDao getAlertDao() {
        return alertDao;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#getAlertLevelInChildFolder
     * (java.lang.String)
     */
    @Override
    public List<TopoFolderEx> getAlertLevelInChildFolder(String path) {
        return alertDao.getMaxAlertInChildFolder(path);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#getAlertTypeById(java.lang .Integer)
     */
    @Override
    public AlertType getAlertTypeById(Integer typeId) {
        return alertTypeMapping.get(typeId);
    }

    /**
     * @return the alertTypeDao
     */
    public AlertTypeDao getAlertTypeDao() {
        return alertTypeDao;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#getAllAlertTypes()
     */
    @Override
    public List<AlertType> getAllAlertTypes() {
        alertTypes = alertTypeDao.getAllAlertType();
        return alertTypes;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#getEntityCurrentAlert(java .lang.Long,
     * com.topvision.framework.domain.Page, java.util.Map)
     */
    @Override
    public PageData<Alert> getEntityCurrentAlert(Long entityId, Page p, Map<String, String> map) {
        if (map == null) {
            map = new HashMap<String, String>();
        }
        map.put("entityId", entityId.toString());

        return this.queryCurrentAlert(p, map);
    }

    /**
     * @return the historyAlertDao
     */
    public HistoryAlertDao getHistoryAlertDao() {
        return historyAlertDao;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#getMapOfAlertTypes()
     */
    @Override
    public Map<Integer, AlertType> getMapOfAlertTypes() {
        return alertTypeMapping;
    }

    public List<Alert> getRecentAlert(Integer limit) {
        return alertDao.getRecentAlert(limit);
    }

    /**
     * @param monitorId
     * @param host
     * @param limit
     * @return List<Alert>
     */
    public List<Alert> getRecentAlert(Long monitorId, String host, Integer limit) {
        Map<String, String> map = new HashMap<String, String>();
        if (monitorId > 0) {
            map.put("monitorId", String.valueOf(monitorId));
        }
        if (host != null) {
            map.put("host", host);
        }
        if (limit > 0) {
            map.put("offset", "0");
            map.put("pageSize", String.valueOf(limit));
        }
        return alertDao.selectByMap(map);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#handleCurrentAlert(com. topvision
     * .framework.event .MyResultHandler, java.util.Map)
     */
    @Override
    public void handleCurrentAlert(MyResultHandler handler, Map<String, String> map) {

    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#handleRecentAlert(java.lang .Integer,
     * com.topvision.framework.event.MyResultHandler)
     */
    @Override
    public void handleRecentAlert(Integer limit, MyResultHandler handler) {
        alertDao.handleRecentAlert(limit, handler);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#insertAlert(com.topvision
     * .ems.fault.domain.Alert )
     */
    @Override
    public void insertAlert(Alert alert) {
        // 对于插入告警时网管数据已经不存在的情况,不进行任何处理(无法插入数据库)
        Entity entity = entityDao.selectEntityFromDB(alert.getEntityId());
        if (entity != null) {
            if (alert.getLevelId() > Level.CLEAR_LEVEL && alertFilterService.filter(alert)) {
                return;
            }
            alertDao.insertEntity(alert);
            snapTrigger(alert, false);
            // 执行告警动作
            actionCenter.executeAction(alert);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#loadFloatingAlert(java.lang .Long)
     */
    @Override
    public List<Alert> loadFloatingAlert(Long entityId) {
        return alertDao.loadFloatingAlert(entityId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#loadJSONAlertType()
     */
    @Override
    public JSONArray loadJSONAlertType() throws Exception {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        // 去掉非空判断，支持动态切换系统语言
        // if (jsonAlertType == null) {
        jsonAlertType = new JSONArray();
        List<AlertType> list = getAllAlertTypes();
        HashMap<String, JSONObject> map = new HashMap<String, JSONObject>(list == null ? 0 : list.size());
        JSONObject json = null;
        AlertType type = null;
        for (int i = 0; list != null && i < list.size(); i++) {
            type = list.get(i);
            if (type.getCategory() != 0) {
                continue;
            }
            json = new JSONObject();
            json.put("text", resourceManager.getString(type.getDisplayName()));
            json.put("expanded", false);
            json.put("id", String.valueOf(type.getTypeId()));
            json.put("children", new JSONArray());
            json.put("superiorId", type.getParentId());
            json.put("iconCls", "alertFolderIcon");
            map.put(String.valueOf(type.getTypeId()), json);
            jsonAlertType.add(json);
        }
        JSONObject parent = null;
        for (int i = 0; list != null && i < list.size(); i++) {
            type = list.get(i);
            if (type.getCategory() == 0) {
                continue;
            }
            json = new JSONObject();
            json.put("text", resourceManager.getString(type.getDisplayName()));
            json.put("expanded", true);
            json.put("id", String.valueOf(type.getTypeId()));
            json.put("children", new JSONArray());
            json.put("superiorId", type.getParentId());
            json.put("iconCls", "level" + type.getLevelId() + "Icon");
            json.put("typeId", type.getTypeId());
            map.put(String.valueOf(type.getTypeId()), json);
            parent = map.get(String.valueOf(type.getParentId()));
            if (parent == null) {
                jsonAlertType.add(json);
            } else {
                parent.getJSONArray("children").add(json);
                parent.put("iconCls", "alertFolderIcon");
                parent.put("expanded", false);
            }
        }
        // }
        return jsonAlertType;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#queryCurrentAlert(com. topvision
     * .framework.domain .Page, java.util.Map)
     */
    @Override
    public PageData<Alert> queryCurrentAlert(Page p, Map<String, String> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return alertDao.selectByMap(map, p);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService# restoreEntityCurrentAlertState
     * (com.topvision .framework.event.MyResultHandler)
     */
    @Override
    public void restoreEntityCurrentAlertState(MyResultHandler handler) {
        alertDao.restoreEntityCurrentAlertState(handler);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#saveAlertTypeParam(com. topvision
     * .ems.fault.domain .AlertType, java.util.List, java.util.List)
     */
    @Override
    public void saveAlertTypeParam(AlertType alertType, List<Long> eventIds, List<Long> actionIds) {
        alertTypeDao.updateAlertTypeActionRela(alertType.getTypeId(), actionIds);
        actionCenter.reset();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#selectByMap(java.util.Map)
     */
    @Override
    public List<Alert> selectByMap(Map<String, String> map) {
        return alertDao.selectByMap(map);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#selectCurrentAlert(java.util .Map)
     */
    @Override
    public List<Alert> selectCurrentAlert(Map<String, String> map) {
        return alertDao.selectCurrentAlert(map);
    }

    public void setActionCenter(ActionCenter actionCenter) {
        this.actionCenter = actionCenter;
    }

    /**
     * @param alertDao
     *            the alertDao to set
     */
    public void setAlertDao(AlertDao alertDao) {
        this.alertDao = alertDao;
    }

    public void setAlertFilterService(AlertFilterService alertFilterService) {
        this.alertFilterService = alertFilterService;
    }

    /**
     * @param alertTypeDao
     *            the alertTypeDao to set
     */
    public void setAlertTypeDao(AlertTypeDao alertTypeDao) {
        this.alertTypeDao = alertTypeDao;
    }

    /**
     * @param historyAlertDao
     *            the historyAlertDao to set
     */
    public void setHistoryAlertDao(HistoryAlertDao historyAlertDao) {
        this.historyAlertDao = historyAlertDao;
    }

    /**
     * @param messageService
     *            the messageService to set
     */
    public final void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * @param alert
     * @param immediately
     * @return Alert
     */
    private Alert snapTrigger(Alert alert, boolean immediately) {
        Alert max = null;
        if (alert.getEntityId() != null) {
            max = alertDao.getMaxLevelAlertByEntityId(alert.getEntityId());
            if (max == null) {
                fireEntityAlert(alert.getEntityId(), Level.CLEAR_LEVEL, "", alert.getAlertId(), alert.getLastTime());
            } else {
                fireEntityAlert(alert.getEntityId(), max.getLevelId(), max.getMessage(), max.getAlertId(),
                        max.getLastTime());
            }
        }
        // 实时通知告警接收者
        fireAlert(alert, immediately);
        return max;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#statAlertByEntity(java.util .Map)
     */
    @Override
    public List<AlertStat> statAlertByEntity(Map<String, Object> map) {
        return alertDao.topAlertByEntity(map);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#statAlertByLevel(java.util .Map)
     */
    @Override
    public List<LevelStat> statAlertByLevel(Map<String, String> map) {
        return alertDao.statAlertByLevel(map);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#statAlertByServer(java.util .Map)
     */
    @Override
    public List<AlertStat> statAlertByServer(Map<String, String> map) {
        return alertDao.statAlertByServer(map);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#txClearAlert(com.topvision
     * .ems.fault.domain. Alert, java.lang.String)
     */
    @Override
    public void txClearAlert(Alert alert, String clearMsg) {
        txClearAlert(alert, null, clearMsg);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#txClearAlert(com.topvision
     * .ems.fault.domain. Alert, java.lang.String, java.lang.String)
     */
    @Override
    public Alert txClearAlert(Alert alert, String clearUser, String clearMsg) {
        if (clearUser == null) {
            alert.setStatus(Alert.AUTO_CLEAR);
            alert.setClearUser("System");
            // 告警自动清除采用告警发生时间
            alert.setClearTime(alert.getClearTime());
            alert.setClearMessage(clearMsg);
        } else {
            alert.setStatus(Alert.MANUAL_CLEAR);
            // 告警手动清除 采用系统时间
            alert.setClearTime(new Timestamp(System.currentTimeMillis()));
            alert.setClearUser(clearUser);
            alert.setClearMessage(clearMsg);
        }
        alertDao.clearAlert(alert);
        alert2HistoryAlert(alert);
        try {
            // 执行告警动作
            if (alert.getIp() != null || alert.getHost() != null) {
                alert.setLevelId(Level.CLEAR_LEVEL);
                actionCenter.executeAction(alert);
            } else {
                alert.setIp("0.0.0.0");
                alert.setLevelId(Level.CLEAR_LEVEL);
                actionCenter.executeAction(alert);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return alert;
        /*
         * historyAlert.setClearMessage(clearMsg); historyAlertDao.insertEntity(historyAlert);
         * alertDao.deleteByPrimaryKey(alert.getAlertId()); alert.setLevelId((byte) 0); return
         * snapTrigger(alert, true);
         */
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#txClearAlert(java.util.List,
     * java.lang.String, java.lang.String)
     */
    @Override
    public List<Alert> txClearAlert(List<Long> alertIds, String clearUser, String clearMsg) {
        int size = alertIds == null ? 0 : alertIds.size();
        List<Alert> list = new ArrayList<Alert>();
        for (int i = 0; i < size; i++) {
            Alert alert = alertDao.getAlertByAlertId(alertIds.get(i));
            if (alert == null) {
                continue;
            }
            list.add(alert);
            txClearAlert(alert, clearUser, clearMsg);
        }

        cachedThresholdAlertService.removeThresholdAlertValue(list);
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#txConfirmAlert(com.topvision
     * .ems.fault.domain .Alert, java.lang.String, java.lang.String)
     */
    @Override
    public Alert txConfirmAlert(Alert alert, String confirmUser, String confirmMsg) {
        alert.setConfirmUser(confirmUser);
        alert.setStatus(Alert.CONFIRM);
        alert.setConfirmTime(new Timestamp(System.currentTimeMillis()));
        alert.setConfirmMessage(confirmMsg);
        alertDao.confirmAlert(alert);
        alert2HistoryAlert(alert);
        return alert;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#txConfirmAlert(java.util. List,
     * java.lang.String, java.lang.String)
     */
    @Override
    public List<Alert> txConfirmAlert(List<Long> alertIds, String confirmUser, String confirmMsg) {
        int size = alertIds == null ? 0 : alertIds.size();
        for (int i = 0; i < size; i++) {
            Alert alert = alertDao.getAlertByAlertId(alertIds.get(i));
            if (alert == null) {
                continue;
            }
            txConfirmAlert(alert, confirmUser, confirmMsg);
        }
        return null;
    }

    /**
     * 当前告警状态检查
     *
     * @param alert
     */
    private void alert2HistoryAlert(Alert alert) {
        boolean alertAutoConfirmConfig = alertConfirmConfigService.getAlertConfirmConfig();
        // 开启告警自动确认
        if (alertAutoConfirmConfig) {
            if (alert.getClearUser() != null) {
                HistoryAlert historyAlert = new HistoryAlert();
                historyAlert.setAlert(alert);
                historyAlertDao.insertEntity(historyAlert);
                alertDao.deleteByPrimaryKey(alert.getAlertId());
                alert.setLevelId((byte) 0);
                snapTrigger(alert, true);
                return;
            }
        } else {
            if (alert.getConfirmUser() != null && alert.getClearUser() != null) {
                HistoryAlert historyAlert = new HistoryAlert();
                historyAlert.setAlert(alert);
                historyAlertDao.insertEntity(historyAlert);
                alertDao.deleteByPrimaryKey(alert.getAlertId());
                alert.setLevelId((byte) 0);
                snapTrigger(alert, true);
                return;
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#updateAlertType(java.lang .Integer,
     * java.lang.Byte, java.lang.Boolean, java.util.List)
     */
    @Override
    public void updateAlertType(Integer typeId, Byte level, Boolean active, Integer updateLevel, String alertTimes,
            Boolean smartUpdate, List<Long> actionIds, String note) {
        AlertType alertType = alertTypeMapping.get(typeId);
        if (alertType != null) {
            alertType.setTypeId(typeId);
            alertType.setLevelId(level);
            alertType.setAlertTimes(alertTimes);
            alertType.setSmartUpdate(smartUpdate);
            alertType.setUpdateLevel(updateLevel);
            alertType.setActive(active);
            alertType.setNote(note);
            alertTypeDao.updateLevelByAlertType(alertType);
        }
        alertTypeDao.updateAlertTypeActionRela(typeId, actionIds);
        reset();
        actionCenter.reset();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#updateAlert(com.topvision
     * .ems.fault.domain.Alert )
     */
    @Override
    public void updateAlert(Alert alert) {
        // Modify by victor@20121228 修改EMS-3452问题，修改为过滤后不更新数据
        if (alert.getLevelId() > Level.CLEAR_LEVEL && alertFilterService.filter(alert)) {
            return;
        }
        alertDao.updateEntity(alert);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#getEntityAvailability(java .lang.Long,
     * java.lang.String, int)
     */
    @Override
    public Long getEntityAvailability(Long entityId, String alertType, int d) {
        Long noneTime = 0L;
        // 获取系统当前时间
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Long tempTime = new Timestamp(now.getTime() - Long.valueOf(d) * 24 * 3600 * 1000).getTime();
        // 获取设备创建时间
        Entity entity = alertDao.getEntityCreateTime(entityId);
        Long createTime = entity.getCreateTime().getTime();
        if (tempTime < createTime) {
            tempTime = createTime;
        }
        Alert alert = alertDao.getEntityAvailability(entityId, alertType);
        List<HistoryAlert> historyAlertList = historyAlertDao.getEntityAvailability(entityId, alertType);
        if (alert != null) {
            if (tempTime - alert.getLastTime().getTime() >= 0) {
                noneTime = 0L;
            } else if ((tempTime - alert.getLastTime().getTime() < 0)
                    && (tempTime - alert.getFirstTime().getTime() > 0)) {
                noneTime = noneTime + alert.getLastTime().getTime() - tempTime;
            } else {
                noneTime = noneTime + alert.getLastTime().getTime() - alert.getFirstTime().getTime();
            }
        }
        if (historyAlertList != null) {
            HistoryAlert historyAlert = new HistoryAlert();
            for (int i = 0; i < historyAlertList.size(); i++) {
                historyAlert = historyAlertList.get(i);
                if (tempTime - historyAlert.getLastTime().getTime() >= 0) {
                    continue;
                } else if ((tempTime - historyAlert.getLastTime().getTime() < 0)
                        && (tempTime - historyAlert.getFirstTime().getTime() > 0)) {
                    noneTime = noneTime + historyAlert.getLastTime().getTime() - tempTime;
                } else {
                    noneTime = noneTime + historyAlert.getLastTime().getTime() - historyAlert.getFirstTime().getTime();
                }
            }
        }
        return noneTime / 1000;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService# getRecentAlertByEntityIdAndLimit (int,
     * java.lang.Long)
     */
    @Override
    public List<Alert> getRecentAlertByEntityIdAndLimit(int limit, Long entityId) {
        return alertDao.getRecentAlertByEntityIdAndLimit(limit, entityId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.topvision.ems.fault.service.AlertService#getEntityCreateTime(java .lang.Long)
     */
    @Override
    public Long getEntityCreateTime(Long entityId) {
        Entity entity = alertDao.getEntityCreateTime(entityId);
        if (entity != null) {
            return entity.getCreateTime().getTime();
        }
        return 0L;
    }

    @Override
    public String getEntityNameByIp(String host) {
        return alertDao.getEntityNameByIp(host);
    }

    @Override
    public Long getEntityIdByMac(String cmcMac) {
        return alertDao.queryEntityIdByMac(cmcMac);
    }

    @Override
    public String getMacById(Long entityId) {
        return alertDao.queryMacByEntityId(entityId);
    }

    @Override
    public Alert getEntityAlertByType(Long entityId, String typeId) {
        return alertDao.getEntityAvailability(entityId, typeId);
    }

    @Override
    public List<AlertStat> statAlertListByEntity(Map<String, Object> map) {
        return alertDao.statAlertListByEntity(map);
    }

    @Override
    public Integer statAlertCountByEntity(Map<String, Object> map) {
        return alertDao.statAlertCountByEntity(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertService#getUserAlertTypeCollection(java.lang.Long)
     */
    @Override
    public Set<Integer> getUserAlertTypeCollection(List<Integer> userAlertTypes) {
        Set<Integer> result = new HashSet<>();
        for (Integer typeId : userAlertTypes) {
            AlertType alertType = this.getAlertTypeById(typeId);
            result.add(alertType.getTypeId());
            while (alertType != null && alertType.getCategory() != 0) {
                alertType = this.getAlertTypeById(alertType.getCategory());
                result.add(alertType.getTypeId());
            }
        }
        return result;
    }

    @Override
    public List<AlertAboutUsers> getuserAlertList(Map<String, Object> map) {
        return alertDao.getAlertInfoOfUsersAndChoose(map);
    }

    @Override
    public void updateAlertTypeOfUsers(Integer typeId, Byte level, Boolean active, Integer updateLevel,
            String alertTimes, Boolean smartUpdate, Long userId, List<Integer> userActionChoose, List<Long> actionIds,
            String note) {
        AlertType alertType = alertTypeMapping.get(typeId);
        if (alertType != null) {
            alertType.setTypeId(typeId);
            alertType.setLevelId(level);
            alertType.setAlertTimes(alertTimes);
            alertType.setSmartUpdate(smartUpdate);
            alertType.setUpdateLevel(updateLevel);
            alertType.setActive(active);
            alertType.setNote(note);
            alertTypeDao.updateLevelByAlertType(alertType);
        }
        alertTypeDao.insertAlertTypeAboutUsers(typeId, userId, userActionChoose, actionIds);
        // alertTypeDao.insertAlertTypeTrapAboutUsers(typeId, userId, actionIds);
        alertTypeDao.updateAlertTypeActionRela(typeId, actionIds);
        reset();
        actionCenter.reset();

    }

    @Override
    public List<Integer> getUserAlertActions(Integer alertTypeId, Long userId) {
        return alertTypeDao.getUserAlertActions(alertTypeId, userId);
    }

    @Override
    public List<AlertAboutUsers> getOneUserActionCs(Long userId) {
        return alertTypeDao.getOneUserActionChoose(userId);
    }

    @Override
    public void updateUserActionCs(Long oneUserId, String isTrue) {
        String ch = null;
        if (isTrue.equals("isEmail")) {
            ch = "Email";
        } else if (isTrue.equals("isMobile")) {
            ch = "Mobile";
        } else if (isTrue.equals("isNone")) {
            ch = "noAlert";
        } else if (isTrue.equals("isBoth")) {
            ch = "Email&Mobile";
        } else if (isTrue.equals("isUnConfig")) {
            ch = "unConfig";
        }
        alertTypeDao.updateUserActionCs(oneUserId, ch);
    }

    @Override
    public int getUserAlertListCount() {
        return alertTypeDao.selectUserAlertListNum();
    }

    @Override
    public List<AlertAboutUsers> getSendingInfoOfUsers(Integer alertTypeId) {
        return alertTypeDao.selectSendingInfo(alertTypeId);
    }

    @Override
    public List<Long> getConcernAlertTypes() {
        return alertDao.getConcernAlertTypes();
    }

}
