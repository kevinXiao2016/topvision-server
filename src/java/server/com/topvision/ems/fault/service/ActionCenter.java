package com.topvision.ems.fault.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import com.topvision.ems.fault.dao.ActionDao;
import com.topvision.ems.fault.dao.AlertTypeDao;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.Action;
import com.topvision.platform.domain.User;
import com.topvision.platform.service.ActionService;
import com.topvision.platform.service.UserService;
import com.topvision.platform.util.CurrentRequest;
import com.topvision.platform.zetaframework.util.ZetaUtil;

public class ActionCenter {

    protected static final Logger logger = LoggerFactory.getLogger(ActionCenter.class);
    private Map<Integer, List<Action>> a2aRelas = new HashMap<Integer, List<Action>>();
    private Map<Integer, ActionService> actionServices = null;
    private AlertTypeDao alertTypeDao;
    private ActionDao actionDao;
    private TrapService trapService;
    private UserService userService;
    private EntityDao entityDao;

    public void destroy() {

    }

    /**
     * 
     * @param alert
     */
    public void executeAction(Alert alert) {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.fault.resources");
        //List<Action> actions = a2aRelas.get(alert.getTypeId());
        List<Action> actions = alertTypeDao.getUserActionOfAlertType(alert.getTypeId());

        for (int i = 0; actions != null && i < actions.size(); i++) {
            try {
                Action action = actions.get(i);
                if (action.getUserId() == null) {
                    continue;
                }
                Long entityId = alert.getEntityId();
                User user = userService.getUserEx(action.getUserId());
                if (!entityDao.isEntityInAuthorityTable(entityId, CurrentRequest.TABLE_ENTITY_PRE + user.getUserGroupId())) {
                    continue;
                }
                if (!action.isEnabled()) {
                    continue;
                }
                // SoundAction=4
                if (action.getActionTypeId() == 4) {
                    alert.setSound((String) action.getParamsObject());
                    continue;
                }
                if (actionServices != null && actionServices.containsKey(action.getActionTypeId())) {
                    // 增加发送行为时的内容 by victor @20120823
                    StringBuilder msg = new StringBuilder();
                    if(action.getActionTypeId() == 1||action.getActionTypeId() == 2){
                        if(alert.getStatus()==Alert.AUTO_CLEAR||alert.getStatus()==Alert.MANUAL_CLEAR){
                            msg.append(ZetaUtil.getStaticString("ALERT.thisAlertIsCleared","fault"));
                        } 
                    }
                    msg.append(alert.getFirstTimeStr());
                    msg.append(" - ").append(alert.getHost());
                    msg.append(" - ").append(alert.getSource());
                    msg.append(" - ").append(resourceManager.getNotNullString(alert.getTypeName())).append(":").append(alert.getMessage());
                    actionServices.get(action.getActionTypeId()).sendAction(action, alert, msg.toString());
                }
            } catch (Exception e) {
                logger.error("action error:", e);
            }
        }
    }

    public Map<Integer, ActionService> getActionServices() {
        return actionServices;
    }

    public void initialize() {
        reset();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.fault.service.AlertService#reset()
     */
    public void reset() {
        a2aRelas.clear();
        try {
            Map<Long, Action> actions = actionDao.getMapOfActions();
            List<AlertType> types = alertTypeDao.selectByMap(null);
            for (int i = 0; types != null && i < types.size(); i++) {
                int id = types.get(i).getTypeId();
                List<Long> actionIds = alertTypeDao.getActionsOfAlertType(id);
                List<Action> as = new ArrayList<Action>();
                a2aRelas.put(id, as);
                if (actionIds == null || actionIds.isEmpty()) {
                    continue;
                }
                for (long actionId : actionIds) {
                    if (actions.keySet().contains(actionId)) {
                        as.add(actions.get(actionId));
                    }
                }
            }
        } catch (DataAccessException e1) {
            if (logger.isDebugEnabled()) {
                logger.debug("reset.", e1.getMessage());
            }
        }
    }

    public void setActionDao(ActionDao actionDao) {
        this.actionDao = actionDao;
    }

    public void setActionServices(Map<Integer, ActionService> actionServices) {
        this.actionServices = actionServices;
    }

    public void setAlertTypeDao(AlertTypeDao alertTypeDao) {
        this.alertTypeDao = alertTypeDao;
    }

    /**
     * @return the trapService
     */
    public TrapService getTrapService() {
        return trapService;
    }

    /**
     * @param trapService the trapService to set
     */
    public void setTrapService(TrapService trapService) {
        this.trapService = trapService;
    }

    /**
     * @return the userService
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * @param userService the userService to set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @return the entityDao
     */
    public EntityDao getEntityDao() {
        return entityDao;
    }

    /**
     * @param entityDao the entityDao to set
     */
    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

}
