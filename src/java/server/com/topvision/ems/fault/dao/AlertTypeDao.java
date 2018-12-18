package com.topvision.ems.fault.dao;

import java.util.List;

import com.topvision.ems.fault.domain.AlertAboutUsers;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.domain.Action;

public interface AlertTypeDao extends BaseEntityDao<AlertType> {

	/**
	 * 获得参数类型告警动作集合
	 * 
	 * @param typeId
	 * @return
	 */
	List<Long> getActionsOfAlertType(Integer typeId);

	/**
	 * 获得用户关联的告警动作
	 * 
	 * @param typeId
	 * @return
	 */
	List<Action> getUserActionOfAlertType(Integer typeId);

	/**
	 * 更新参数类型告警信息等级
	 * 
	 * @param alertType
	 */
	void updateLevelByAlertType(AlertType alertType);

	/**
	 * 获得所有告警类型
	 * 
	 * @return
	 */
	List<AlertType> getAllAlertType();

	/**
	 * 获得CC相关告警类型
	 * 
	 */
	List<AlertType> getCmcAlertTypes();

	/**
	 * 更新用户选择的告警设置
	 * @param actionIds 
	 * 
	 * @param
	 * @return void
	 */
	void insertAlertTypeAboutUsers(Integer typeId, Long userId, List<Integer> userActionChoose, List<Long> actionIds);

	/**
	 * 得到某个用户绑定的告警类型对应的告警动作
	 * 
	 * @param
	 * @return Integer
	 */
	List<Integer> getUserAlertActions(Integer alertTypeId, Long userId);

	List<String> getAlertAboutUsersOfuserName(Integer id);

	List<AlertAboutUsers> getOneUserActionChoose(Long userId);

	void updateUserActionCs(Long oneUserId, String ch);

	int selectUserAlertListNum();

	List<AlertAboutUsers> selectSendingInfo(Integer alertTypeId);

	/**
	 * 更新参数类型告警动作集合
	 * 
	 * @param typeId
	 * @param actionIds
	 */
	void updateAlertTypeActionRela(Integer typeId, List<Long> actionIds);

    void insertAlertTypeTrapAboutUsers(Integer typeId, Long userId, List<Long> actionIds);
	
	void updateLocaleName(Integer typeId, String localeName);
}
